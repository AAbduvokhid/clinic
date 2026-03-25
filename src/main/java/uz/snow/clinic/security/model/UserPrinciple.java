/*
package uz.snow.clinic.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.util.Utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Builder
public class UserPrinciple implements UserDetails {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private Long departmentId;
    private Boolean isAdmin;
    private Collection authorities;

    public static UserPrinciple build(User user) {
        List authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();
        return UserPrinciple.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .phone(user.getPhone())
                .departmentId(user.getDepartmentId())
                .isAdmin(Utils.isAdmin(Optional.of(user))).build();

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserPrinciple that = (UserPrinciple) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
*/
