package com.app.dealworkflowtracker.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails { // <-- Implement UserDetails here

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 3, max = 50)
  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @NotBlank
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @NotBlank
  @Column(name = "last_name", nullable = false)
  private String lastName;

  @NotBlank
  @Email
  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false, length = 255)
  private String password;

  @Column(name = "postal_code", length = 7)
  private String postalCode;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();

  // Audit fields using primitive IDs to prevent recursive joins
  @Column(name = "created_by", updatable = false)
  private Long createdBy;

  @Column(name = "created_on", nullable = false, updatable = false)
  private LocalDateTime createdOn;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "updated_on")
  private LocalDateTime updatedOn;

  @PrePersist
  protected void onCreate() {
    this.createdOn = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedOn = LocalDateTime.now();
  }

  // ==========================================
  // Spring Security UserDetails Implementations
  // ==========================================

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
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

  // ==========================================

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}