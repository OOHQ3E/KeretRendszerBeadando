package com.example.oohq3ebeadando.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                    .antMatchers(HttpMethod.GET,"/").permitAll()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/admin").hasRole("ADMIN")
                    .antMatchers("/editUser/**").hasRole("ADMIN")
                    .antMatchers("/deleteUser/**").hasRole("ADMIN")
                    .antMatchers("/cars").authenticated()
                    .antMatchers("/addCar").authenticated()
                    .antMatchers("/updateCar/**").authenticated()
                    .antMatchers("/deleteCar/**").hasRole("ADMIN")
                    .antMatchers("/mail").authenticated()
                    .antMatchers("/sendmail").authenticated()

                    .antMatchers("/bicycles").authenticated()
                    .antMatchers("/addBicycle").authenticated()
                    .antMatchers("/updateBicycle/**").authenticated()
                    .antMatchers("/deleteBicycle/**").hasRole("ADMIN")

                    .antMatchers("/rollerscooters").authenticated()
                    .antMatchers("/addRollerscooter").authenticated()
                    .antMatchers("/addRollerscooter/**").authenticated()
                    .antMatchers("/deleteRollerscooter/**").hasRole("ADMIN")

                    .and().formLogin().permitAll();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
