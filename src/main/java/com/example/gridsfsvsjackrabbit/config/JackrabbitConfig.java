package com.example.gridsfsvsjackrabbit.config;

import com.example.gridsfsvsjackrabbit.properties.JackrabbitProperties;
import org.apache.jackrabbit.commons.JcrUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@Configuration
public class JackrabbitConfig {

	@Bean
	public Node receiptsNode(JackrabbitProperties jackRabbitProperties) throws RepositoryException {
		Repository remoteRepository = JcrUtils.getRepository(jackRabbitProperties.getUrl());
		Session jackrabbitSession = remoteRepository.login();
		Node rootNode = jackrabbitSession.getRootNode();
		if (!rootNode.hasNode(jackRabbitProperties.getDirectory())) {
			rootNode.addNode(jackRabbitProperties.getDirectory());
		}
		return rootNode.getNode(jackRabbitProperties.getDirectory());
	}

}
