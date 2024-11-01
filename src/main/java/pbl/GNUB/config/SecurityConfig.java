package pbl.GNUB.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http        
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/member/new", "/member/login", "member/logout", "/main", "/css/**", "/js/**").permitAll() // Allow access to sign-up and login pages
                .anyRequest().authenticated() // All other requests require authentication
            )
            
            .formLogin(form -> form
                .loginPage("/member/login") // Custom login page
                .loginProcessingUrl("/member/login") // 폼 action 경로
                .defaultSuccessUrl("/main", true) // 로그인 성공 시 리다이렉트될 경로
                .failureUrl("/member/login?error=true") // 로그인 실패 시 리다이렉트 경로
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/main") // 로그아웃 성공 후 메인페이지 이동
                .invalidateHttpSession(true) // 세션 무효화
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}