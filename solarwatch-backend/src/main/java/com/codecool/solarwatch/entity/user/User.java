package com.codecool.solarwatch.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "app_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    // factory for registration
    public static User create(String email, String encodedPassword, String fullName, Set<Role> roles) {
        User u = new User();
        u.email = email;
        u.password = encodedPassword;
        u.fullName = fullName;
        if (roles != null) u.roles.addAll(roles);
        return u;
    }

    // domain methods
    public void changeEmail(String newEmail) { this.email = newEmail; }
    public void setEncodedPassword(String encoded) { this.password = encoded; }
    public void rename(String newName) { this.fullName = newName; }
    public void grant(Role role) { this.roles.add(role); }
    public void revoke(Role role) { this.roles.remove(role); }
    public void setRoles(Set<Role> newRoles) {
        this.roles.clear();
        if (newRoles != null) this.roles.addAll(newRoles);
    }

    // UserDetails
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toSet());
    }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
