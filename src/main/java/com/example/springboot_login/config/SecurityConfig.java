package com.example.springboot_login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/css/**", "/index").permitAll() // /css/** と /index へのアクセスを全て許可
                .requestMatchers("/user/**").hasRole("USER") // /user/** へのアクセスを USER ロールを持つユーザーに限定
                .requestMatchers("/admin/**").hasRole("ADMIN") // /admin/** へのアクセスを ADMIN
                                                               // ロールを持つユーザーに限定
                .anyRequest().authenticated() // その他の全てのリクエストは認証を要求
        ).formLogin(formLogin -> formLogin.loginPage("/login").permitAll() // ログインページへのアクセスを全て許可
        ).logout(logout -> logout.logoutUrl("/logout").permitAll() // ログアウトURLへのアクセスを全て許可
        );
        return http.build();
    }

    /**
     * パスワードエンコーダーを構成します。
     * 
     * @return BCryptPasswordEncoderインスタンス
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * カスタムUserDetailsServiceとPasswordEncoderを使用してDaoAuthenticationProviderを構成します。
     * 
     * @return 構成されたDaoAuthenticationProviderインスタンス
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }
}
