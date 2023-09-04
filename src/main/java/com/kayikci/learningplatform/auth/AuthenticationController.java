package com.kayikci.learningplatform.auth;

import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.config.LogoutService;

import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {


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
