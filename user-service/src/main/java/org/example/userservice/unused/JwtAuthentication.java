//package org.example.userservice.unused;
//
//import lombok.Data;
//import org.example.userservice.model.Role;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//import java.util.Set;
//
//@Data
//public class JwtAuthentication implements Authentication {
//    private boolean authenticated;
//    private String email;
//    private String username;
//    private Set<Role> roles;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return null;
//    }
//
//    @Override
//    public Object getDetails() {
//        return null;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return email;
//    }
//
//    @Override
//    public boolean isAuthenticated() {
//        return authenticated;
//    }
//
//    @Override
//    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//        this.authenticated = isAuthenticated;
//    }
//
//    @Override
//    public String getName() {
//        return username;
//    }
//}