package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final boolean httpsDisabled;

  public SecurityConfiguration(@Value("${https.disabled}") boolean httpsDisabled) {
    this.httpsDisabled = httpsDisabled;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    if (!httpsDisabled)
      http.requiresChannel().anyRequest().requiresSecure();

    http.authorizeRequests().antMatchers("/**").hasRole("USER").and().httpBasic().and().csrf().disable();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    auth.inMemoryAuthentication().withUser("user").password(passwordEncoder.encode("password")).roles("USER");
  }
}
