package com.parma.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private BCryptPasswordEncoder bcryptPasswordEncoder;

  @Autowired
  private DataSource dataSource;

  @Value("${spring.queries.users-query}")
  private String usersQuery;

  @Value("${spring.queries.auth-query}")
  private String authQuery;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(authQuery)
        .dataSource(dataSource).passwordEncoder(bcryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.authorizeRequests().antMatchers("/").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/registration").permitAll()
        .antMatchers("/home").hasAuthority("ROLE_USER")
        .anyRequest().authenticated().and().csrf()
        .disable().formLogin().loginPage("/login").failureUrl("/login?error=true")
        .defaultSuccessUrl("/home")
        .usernameParameter("username").passwordParameter("password").and().logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
        .and().exceptionHandling()
        .accessDeniedPage("/access-denied");
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
  }
  
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    BCryptPasswordEncoder beCryptPasswordEncoder = new BCryptPasswordEncoder();
    return beCryptPasswordEncoder;
  }

}