package com.kayikci.learningplatform.auth;


import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.token.Token;
import com.kayikci.learningplatform.token.TokenRepository;
import com.kayikci.learningplatform.token.TokenType;
import com.kayikci.learningplatform.user.Role;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import com.kayikci.learningplatform.exception.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final int MAX_RETRIES = 10;

    private final Logger log = LoggerFactory.getLogger( getClass() );

    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())){
            log.warn("User mit gleicher E-Mail-Adresse existiert bereits. E-Mail-Adresse: {}", request.getEmail());
            throw new UserAlreadyExistsException("User mit Email-Adresse " + request.getEmail() + " existiert bereits.");
        }
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                        log.error("Benutzer für E-Mail-Adresse " + request.getEmail()+ " nicht gefunden.");
                        return new ResourceNotFoundException("Benutzer für E-Mail-Adresse " + request.getEmail()+ " nicht gefunden.");
                });
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        int retryCount = 0;
        while (tokenRepository.existsByToken(jwtToken) && retryCount < MAX_RETRIES) {
            jwtToken = jwtService.generateToken(user);
            retryCount++;
        }
        if (retryCount == MAX_RETRIES) {
            log.error("Maximale Anzahl von Token-Generierungsversuchen erreicht. Anzahl: {}", retryCount);
            throw new IllegalStateException("Maximale Anzahl von Token-Generierungsversuchen erreicht");
        }
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }



    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
