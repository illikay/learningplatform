package com.kayikci.learningplatform.auth;

import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.config.LogoutService;
import com.kayikci.learningplatform.user.UserRepository;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final Logger log = LoggerFactory.getLogger(getClass());


    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping("/usermanagement/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Validated @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }


    @PostMapping("/usermanagement/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Validated @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("usermanagement/check-account")
    public ResponseEntity<Boolean> checkAccountExists(@Validated @RequestBody CheckAccountRequest checkAccountRequest) {
        if (authenticationService.checkAccountExists(checkAccountRequest.getEmail())) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("usermanagement/verify")
    public ResponseEntity<VerifyResponse> verifyToken(@RequestHeader("Authorization") String token) {
        String username = "";
        try {
            username = jwtService.extractUsernameForController(token);
        }
        catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new VerifyResponse("Invalid token"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VerifyResponse("An error occurred"));
        }
        if (jwtService.isTokenValidForUser(token, username)){
            return ResponseEntity.ok(new VerifyResponse("success"));
        }
        return ResponseEntity.ok(new VerifyResponse("error"));
    }

 /* @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    // Get the validation errors and create a custom error response
    List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

    // Return a response entity with the error response and a bad request status
    return ResponseEntity.badRequest().body(errors.toString());
  }
*/


}
