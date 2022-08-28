package br.com.atech.usermanager.config;

import br.com.atech.usermanager.dto.security.UserCustomDTO;

import br.com.atech.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfigAdapter extends WebSecurityConfigurerAdapter {


    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserService userService) throws Exception {

        builder.userDetailsService(login -> new UserCustomDTO(userService.findValidUserByEmail(login))).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManagerBean();
    }







//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("ROLE");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest().hasRole("ROLE")
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .csrf().disable();
//    }
//
//    @Bean
//    public List<AuthorizationServerConfigurer> listBean(){
//        return Collections.emptyList();
//    }
//
//
//
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
