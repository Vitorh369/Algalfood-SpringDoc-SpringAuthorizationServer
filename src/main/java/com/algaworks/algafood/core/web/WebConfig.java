package com.algaworks.algafood.core.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	ApiDeprecationHandler apiDeprecationHandler;

	@Bean
	Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
}
