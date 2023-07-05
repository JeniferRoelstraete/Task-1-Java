package com.mindhub.homebanking.configurations;

import jdk.jfr.Enabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/login").permitAll()

                .antMatchers(HttpMethod.POST, "/api/logout").permitAll()

                .antMatchers("/web/styles.css").permitAll()

                .antMatchers("/web/index.html").permitAll()

                .antMatchers("/web/index.js").permitAll()

                .antMatchers("/web/assets/**").permitAll()

                .antMatchers("/manager.html").hasAuthority("ADMIN")

                .antMatchers("/h2-console/**").hasAuthority("ADMIN")

                .antMatchers("/api/clients").hasAuthority("CLIENT")

                .antMatchers("/api/clients/{id}").hasAuthority("CLIENT")

                .antMatchers("/api/clients/current/**").hasAuthority("CLIENT")

                .antMatchers("/api/accounts").hasAuthority("CLIENT")

                .antMatchers("/api/accounts/{id}").hasAuthority("CLIENT")

                .antMatchers(HttpMethod.POST, "/api/clients").hasAuthority("CLIENT")

                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")

                .antMatchers(HttpMethod.POST, "/api/client/current/transactions").hasAuthority("CLIENT")

                .antMatchers(HttpMethod.POST, "/api/client/current/accounts").hasAuthority("CLIENT")

                .antMatchers("/web/account.html").hasAuthority("CLIENT")

                .antMatchers("/web/account.js").hasAuthority("CLIENT")

                .antMatchers("/web/accounts.html").hasAuthority("CLIENT")

                .antMatchers("/web/accounts.js").hasAuthority("CLIENT")

                .antMatchers("/web/cards.html").hasAuthority("CLIENT")

                .antMatchers("/web/cards.js").hasAuthority("CLIENT")

                .antMatchers("/web/create-cards.html").hasAuthority("CLIENT")

                .antMatchers("/web/create-cards.js").hasAuthority("CLIENT")

                .antMatchers("/web/transfers.html").hasAuthority("CLIENT")

                .antMatchers("/web/transfers.js").hasAuthority("CLIENT")

                .anyRequest().denyAll();

        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        //desabilita esa opcion para que pueda funcionar, consola es externa
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        //si el usuario no esta autenticando no le alcanza, se le envia un fallo de auteticacion
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        //si el login fue con exito,que elimine la marcas de spring establece, para no pedir al usuario que no se logie
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        //si falla el inicio de secciin simplemente envia una respuesta de falla de auteticacion
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        //si el logout es exitoso, envia una mensaje de respuesta de exito
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }
}
