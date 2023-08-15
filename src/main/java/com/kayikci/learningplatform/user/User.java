package com.kayikci.learningplatform.user;


import com.kayikci.learningplatform.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Length(min = 1, max = 30)
  @NotEmpty(message = "Vorname darf nicht leer sein")
  private String firstname;

  @Length(min = 1, max = 30)
  @NotEmpty(message = "Nachname darf nicht leer sein")
  private String lastname;

  @Column(unique = true)
  @Email(message = "Bitte gültige E-Mail-Adresse angeben")
  @NotEmpty(message = "E-Mail darf nicht leer sein")
  private String email;


  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
