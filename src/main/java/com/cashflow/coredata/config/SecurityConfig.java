package com.cashflow.coredata.config;

import com.cashflow.auth.core.config.CashFlowSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("!test")
@EnableMethodSecurity
@Import(CashFlowSecurityConfig.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
            throws Exception {
        return CashFlowSecurityConfig.securityFilterChain(
                httpSecurity,
                null,
                null
        );
    }
}
