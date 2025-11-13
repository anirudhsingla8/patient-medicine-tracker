package com.medicine.tracker.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a user in the medicine tracker application
 * Contains user account information including authentication details
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false, length = 255)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "password_last_changed")
    private LocalDateTime passwordLastChanged;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "fcm_token")
    @Getter
    @Setter
    private String fcmToken;
    
    @Override
    public String getPassword() {
        return password;
    }

    // Default constructor to initialize created_at
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (passwordLastChanged == null) {
            passwordLastChanged = LocalDateTime.now();
        }
    }
    
    // Update password last changed time
    @PreUpdate
    protected void onUpdate() {
        passwordLastChanged = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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