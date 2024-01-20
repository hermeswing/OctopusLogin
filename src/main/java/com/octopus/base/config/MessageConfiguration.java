package com.octopus.base.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

@Configuration
public class MessageConfiguration {

	/**
	 * <pre>
	 * MessageSourceAccessor
	 * - 다양한 getMessage 메서드를 제공하는 MessageSource를 쉽게 접근하게 해주는 helper class
	 * </pre>
	 * 
	 * @param messageSource
	 * @return
	 */
	@Bean // public `@Bean` method
	public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
		return new MessageSourceAccessor(messageSource);
	}
}
