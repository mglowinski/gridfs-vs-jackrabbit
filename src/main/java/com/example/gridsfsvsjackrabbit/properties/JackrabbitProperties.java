package com.example.gridsfsvsjackrabbit.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Primary
@Component
@ConfigurationProperties(prefix = "jackrabbit")
public class JackrabbitProperties {

	private String url;
	private String login;
	private String password;
	private String directory;
	private String imageFileName;
}
