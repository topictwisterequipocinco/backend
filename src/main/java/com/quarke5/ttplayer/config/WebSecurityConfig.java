package com.quarke5.ttplayer.config;

import com.quarke5.ttplayer.security.filter.JwtRequestFilter;
import com.quarke5.ttplayer.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private JwtRequestFilter jwtRequestFilter;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
                             JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/**").permitAll()
                /*.authorizeRequests().antMatchers("/auth/**", "/user/**", "/joboffer/joboffers", "/applicant/save", "/publisher/save", "/person/save").permitAll()
                .antMatchers("/applicant/**", "/publisher/**", "/user/**", "/joboffer/**").hasRole("ADMIN")
                .antMatchers("/user/userList", "/applicant/applicants", "/publisher/publishers", "/person/**").hasRole("UTN")
                .antMatchers("/applicant/**", "/joboffer/postulate/**", "/joboffer/jobapplicants").hasRole("APPLICANT")
                .antMatchers("/publisher/**", "/joboffer/{id}", "/joboffer/update/**", "/joboffer/delete/**",
                        "/joboffer/joboffers", "/joboffer/save", "/joboffer/jobapplicants/**").hasRole("PUBLISHER")*/
                .anyRequest().authenticated().and()
                .exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //Registra el service para usuarios y el encriptador de contrasenia
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}

