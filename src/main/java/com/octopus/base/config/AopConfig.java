package com.octopus.base.config;

import com.octopus.base.intercepter.TransactionLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    @Bean
    public TransactionLogAspect transactionLogAspect() {
        return new TransactionLogAspect();
    }
}
