package com.kayikci.learningplatform.auth;


import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.token.Token;
import com.kayikci.learningplatform.token.TokenRepository;
import com.kayikci.learningplatform.token.TokenType;
import com.kayikci.learningplatform.user.Role;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

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





  public boolean emailExists(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found for email: " + email));
    return user != null;
  }

  public AuthenticationResponse register(RegisterRequest request) {

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
            .orElseThrow(() -> new ResourceNotFoundException("User für E-Mail-Adresse " + request.getEmail()+ " not found"));
    String jwtToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
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
