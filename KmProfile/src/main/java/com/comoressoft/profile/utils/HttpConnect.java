package com.comoressoft.profile.utils;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Component;






/**
 * 
 * HTTP connect
 *
 * @since 31 mai 2016
 * @author MHA14633
 */
@Component
public class HttpConnect {
   
  
    /**
     * 
     * Constructor
     */
    public HttpConnect() {
    }

    /**
     * CloseableHttpClient to prepare http client connexion
     * 
     * @param proxy
     *            to use if needed app this.configuration
     * @return Http connect closeable object
     * @throws CrawlerException
     */
    public CloseableHttpClient getHttpConnect() throws Exception {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(128);
        connectionManager.setDefaultMaxPerRoute(24);
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(5000);
        requestConfigBuilder.setSocketTimeout(5000);
        requestConfigBuilder.setExpectContinueEnabled(false);
        requestConfigBuilder.setCookieSpec(CookieSpecs.DEFAULT);
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(requestConfigBuilder.build());
        builder.setConnectionManager(connectionManager);
        ConnectionReuseStrategy reuseStrategy = new ConnectionReuseStrategy() {

            @Override
            public boolean keepAlive(HttpResponse response, HttpContext context) {
                return false;
            }
        };
        builder.setConnectionReuseStrategy(reuseStrategy);

      
        return builder.build();
    }



}