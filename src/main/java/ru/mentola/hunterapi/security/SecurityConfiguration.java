package ru.mentola.hunterapi.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.mentola.hunterapi.filtering.JwtAuthenticationFilter;
import ru.mentola.hunterapi.filtering.UserAgentFilter;
import ru.mentola.hunterapi.model.user.UserRole;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("api/v1/inspection/log").hasAnyAuthority(UserRole.ADMIN.name())
                        .requestMatchers("api/v1/user/*").hasAnyAuthority(UserRole.ADMIN.name())
                        .requestMatchers("api/v1/inspection/create").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.DEFAULT.name())
                        .requestMatchers("api/v1/inspection/").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.DEFAULT.name())
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<UserAgentFilter> userAgentFilter() {
        FilterRegistrationBean<UserAgentFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserAgentFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
