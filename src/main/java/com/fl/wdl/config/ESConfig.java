package com.fl.wdl.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.fl.wdl.repository")
public class ESConfig extends AbstractElasticsearchConfiguration{
	
//	@Bean
//	public RestHighLevelClient restHighLevelClient() {
//		RestHighLevelClient client = new RestHighLevelClient(
//		        RestClient.builder(
//		                new HttpHost("localhost", 9200, "http")));
//		return client;
//	}

	@Override
	public RestHighLevelClient elasticsearchClient() {
		return RestClients.create(ClientConfiguration.localhost()).rest();
	}

}
