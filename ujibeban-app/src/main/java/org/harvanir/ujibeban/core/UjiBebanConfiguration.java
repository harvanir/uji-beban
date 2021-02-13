package org.harvanir.ujibeban.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** @author Harvan Irsyadi */
@EnableConfigurationProperties(UjiBebanProperties.class)
@Configuration(proxyBeanMethods = false)
public class UjiBebanConfiguration {}
