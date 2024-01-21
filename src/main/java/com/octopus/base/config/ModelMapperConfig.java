package com.octopus.base.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    
	private final ModelMapper modelMapper = new ModelMapper();

	// Model Mapper
	// DTO <-> Entity Type Change
	@Bean
	public ModelMapper modelMapper() {
		return modelMapper;
	}

	/**
	 * <pre>
	 * 정확히 일치하는 필드만 매핑
	 * 모든 source 속성 이름에는 모든 토큰이 일치해야 한다
	 * </pre>
	 * @return
	 */
	@Bean
	public ModelMapper stricMapper() {
		// 매핑전략설정
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		return modelMapper;
	}

	/**
	 * <pre>
	 * 지능적인 매핑
	 * 모든 destination 속성 이름 토큰이 일치해야 한다
	 * 모든 source 속성 이름은 일치하는 토큰이 하나 이상 있어야 한다.
     * </pre>
	 * @return
	 */
	@Bean
	public ModelMapper standardMapper() {
		// 매핑전략설정
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

		return modelMapper;
	}

	/**
	 * <pre>
	 * 느슨한 매핑
     * </pre>
	 * @return
	 */
	@Bean
	public ModelMapper looseMapper() {
		// 매핑전략설정
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

		return modelMapper;
	}
}
