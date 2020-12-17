package com.example.gridsfsvsjackrabbit.config;

import com.example.gridsfsvsjackrabbit.properties.JackrabbitProperties;
import org.apache.jackrabbit.commons.JcrUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jcr.*;

@Configuration
public class JackrabbitConfig {

	@Bean
	public Node receiptsNode(JackrabbitProperties jackRabbitProperties) throws RepositoryException {
		Repository remoteRepository = JcrUtils.getRepository(jackRabbitProperties.getUrl());
		Session jackrabbitSession = remoteRepository.login(getJackrabbitCredentials(jackRabbitProperties));
		Node rootNode = jackrabbitSession.getRootNode();
		if (!rootNode.hasNode(jackRabbitProperties.getDirectory())) {
			rootNode.addNode(jackRabbitProperties.getDirectory());
		}
		return rootNode.getNode(jackRabbitProperties.getDirectory());
	}

	private SimpleCredentials getJackrabbitCredentials(JackrabbitProperties jackRabbitProperties) {
		return new SimpleCredentials(
				jackRabbitProperties.getLogin(),
				jackRabbitProperties.getPassword().toCharArray()
		);
	}

}
