package com.example.offres.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Order(1)
public class SpringBootAdminSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    BCryptPasswordEncoder bbCryptPasswordEncoder;

    @Autowired
    UserDetailsService adminDetailsServiceImpl;


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminDetailsServiceImpl).passwordEncoder(bbCryptPasswordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/admin/**")
                .authorizeRequests().anyRequest().authenticated()
                .and().formLogin().loginPage("/admin/login")
                .defaultSuccessUrl("/admin/home", true)
                .failureUrl("/admin/login?error")
                .permitAll()
                .and().logout().logoutUrl("/admin/logout").logoutSuccessUrl("/admin/login")
                .and().exceptionHandling().accessDeniedPage("/admin/accessdenied");
        http.csrf().disable();
    }
}