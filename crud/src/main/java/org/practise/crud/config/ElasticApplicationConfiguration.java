package org.practise.crud.config;

import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import java.net.http.HttpClient;


@Configuration
@EnableElasticsearchRepositories(basePackages = "org.practise.crud.repository")
@ComponentScan(basePackages = {"org.practise.crud.service"})
public class ElasticApplicationConfiguration extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedToLocalhost()
                .usingSsl(buildSSLContext())
                .withBasicAuth("elastic", "cA2KnsCF2X8_fCLy1w7n")
                .build();
    }


    private static SSLContext buildSSLContext(){
        try{
            return new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}
