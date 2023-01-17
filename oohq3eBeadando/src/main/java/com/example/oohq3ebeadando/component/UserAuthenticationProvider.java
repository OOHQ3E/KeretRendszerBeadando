package com.example.oohq3ebeadando.component;

import com.example.oohq3ebeadando.model.User;
import com.example.oohq3ebeadando.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserAuthenticationProvider(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String uname = authentication.getName();
        String passwd = authentication.getCredentials().toString();

        User tempUser = repository.findByUname(uname);
        if (tempUser == null){
            throw new BadCredentialsException("User not found");
        }
        if (passwordEncoder.matches(passwd,tempUser.getPasswd())){
            return new UsernamePasswordAuthenticationToken(uname,passwd,getUserAuths(tempUser.getAuth()));
        }
        else {
            throw new BadCredentialsException("Password is incorrect");
        }
    }
    private List<GrantedAuthority> getUserAuths(String userAuths){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String[] auths = userAuths.split(",");
        for (String auth : auths){
            grantedAuthorities.add(new SimpleGrantedAuthority(auth));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
