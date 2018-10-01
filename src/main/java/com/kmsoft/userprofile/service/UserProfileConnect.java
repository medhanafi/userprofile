package com.kmsoft.userprofile.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class UserProfileConnect {

	
	public static final long WAIT_TIME = 5000;
	public static final int BUFFER_SIZE = 10240;
	public static final byte[] BUFFER = new byte[BUFFER_SIZE];

	private static final String USER_AGENT = "Mozilla/5.0";

	public JsonNode getDataFromUrl(final String url) throws IOException {

		String contentString = "";
		CloseableHttpClient httpclient = this.openConnection();
		RequestConfig config = RequestConfig.custom().setConnectTimeout(2000).build();
		HttpGet request = new HttpGet(url);
		CloseableHttpResponse response = null;

		try {
			request.setHeader("User-Agent", USER_AGENT);
			request.setHeader("Accept-Language", "en-US,en;q=0.5");
			request.setHeader("Accept-Charset", "UTF-8");
			request.setConfig(config);
			response = httpclient.execute(request);

			InputStream is = response.getEntity().getContent();
			if (is != null) {
				try {
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					int bufferReadSize;
					while ((bufferReadSize = is.read(BUFFER, 0, BUFFER_SIZE)) >= 0) {
						byteArrayOutputStream.write(BUFFER, 0, bufferReadSize);
					}
					byte[] bytes = byteArrayOutputStream.toByteArray();
					contentString = new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
				} finally {
					is.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		final JsonNode jsonNode = UserProfileParser.getJsonNodeFromString(contentString);
		return jsonNode.get("results").get(0);

	}
	public  CloseableHttpClient openConnection() {

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setExpectContinueEnabled(false);
        requestConfigBuilder.setCookieSpec(CookieSpecs.DEFAULT);
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setMaxConnTotal(1000);
        builder.setDefaultRequestConfig(requestConfigBuilder.build());
        builder.setConnectionManager(connectionManager);
        ConnectionReuseStrategy reuseStrategy = new ConnectionReuseStrategy() {
            public boolean keepAlive(HttpResponse response, HttpContext context) {
                return false;
            }

        };
        builder.setConnectionReuseStrategy(reuseStrategy);
        return builder.build();

    }
}
