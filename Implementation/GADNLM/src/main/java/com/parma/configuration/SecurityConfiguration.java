package com.parma.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.parma.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private BCryptPasswordEncoder bcryptPasswordEncoder; 

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    UserDetailsServiceImpl userDetailsService = mongoUserDetails();    
    auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.authorizeRequests().antMatchers("/").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/register").permitAll()
        .antMatchers("/calibrate").hasAuthority("ROLE_USER")
        .anyRequest().authenticated().and().csrf()
        .disable().formLogin().loginPage("/login").failureUrl("/login?error=true")
        .defaultSuccessUrl("/calibrate")
        .usernameParameter("username").passwordParameter("password").and().logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
        .and().exceptionHandling()
        .accessDeniedPage("/access-denied");
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
  }
  
  @Primary
  @Bean
  public UserDetailsServiceImpl mongoUserDetails() {
    return new UserDetailsServiceImpl();
  }
  
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    BCryptPasswordEncoder beCryptPasswordEncoder = new BCryptPasswordEncoder();
    return beCryptPasswordEncoder;
  }
  
}



