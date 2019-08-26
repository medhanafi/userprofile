package com.comoressoft.profile.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * OpenUP - Copyright Open Groupe
 * 
 * 
 * -----------------------------------
 * 
 * @author Open Groupe
 * @since 7 avr. 2015
 * @author GLA10234
 */
public final class ConnectionUtils {
	/**
	 * The HTTP protocol
	 */
	public static final String PROXY_HTTP = "http";

	/**
	 * The HTTPS protocol
	 */
	public static final String PROXY_HTTPS = "https";

	/**
	 * The SOCK protocol
	 */
	public static final String PROXY_SOCKS = "sock";

	private static final String PROPERTY_PROXY_SET = "proxySet";
	private static final String PROPERTY_PROXY_HTTP_HOST = "http.proxyHost";
	private static final String PROPERTY_PROXY_HTTP_PORT = "http.proxyPort";
	private static final String PROPERTY_PROXY_HTTPS_HOST = "https.proxyHost";
	private static final String PROPERTY_PROXY_HTTPS_PORT = "https.proxyPort";
	private static final String PROPERTY_PROXY_SOCK_HOST = "socksProxyHost";
	private static final String PROPERTY_PROXY_SOCK_PORT = "socksProxyPort";

	public static final long WAIT_TIME = 5000;
	public static final int BUFFER_SIZE = 10240;
	public static final byte[] BUFFER = new byte[BUFFER_SIZE];

	private static final String USER_AGENT = "Mozilla/5.0";

	static Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);
	@Autowired
	private HttpConnect httpConnect;

	/**
	 * Constructor
	 */
	private ConnectionUtils() {
		super();
	}

	/**
	 * Define the proxy
	 * 
	 * @param type The type (http, https, sock)
	 * @param host The host
	 * @param port The port
	 */
	public static void setProxy(final String type, final String host, final int port) {
		clearProxy();

		if (type != null && StringUtils.isNotBlank(host) && port > 0) {
			System.setProperty(PROPERTY_PROXY_SET, "true");

			if (PROXY_HTTP.equalsIgnoreCase(type)) {
				System.setProperty(PROPERTY_PROXY_HTTP_HOST, host);
				System.setProperty(PROPERTY_PROXY_HTTP_PORT, String.valueOf(port));
			} else if (PROXY_HTTPS.equalsIgnoreCase(type)) {
				System.setProperty(PROPERTY_PROXY_HTTPS_HOST, host);
				System.setProperty(PROPERTY_PROXY_HTTPS_PORT, String.valueOf(port));
			} else if (PROXY_SOCKS.equalsIgnoreCase(type)) {
				System.setProperty(PROPERTY_PROXY_SOCK_HOST, host);
				System.setProperty(PROPERTY_PROXY_SOCK_PORT, String.valueOf(port));
			}
		}
	}

	/**
	 * Clear the proxy
	 */
	public static void clearProxy() {
		System.clearProperty(PROPERTY_PROXY_SET);

		System.clearProperty(PROPERTY_PROXY_HTTP_HOST);
		System.clearProperty(PROPERTY_PROXY_HTTP_PORT);
		System.clearProperty(PROPERTY_PROXY_HTTPS_HOST);
		System.clearProperty(PROPERTY_PROXY_HTTPS_PORT);
		System.clearProperty(PROPERTY_PROXY_SOCK_HOST);
		System.clearProperty(PROPERTY_PROXY_SOCK_PORT);
	}

	/**
	 * Try to load data from an URL
	 * 
	 * @param post
	 * 
	 * @param urlString The URL
	 * @param proxy
	 * @param i
	 * @return The content
	 * @throws IOException   On loading failed
	 * @throws CoreException
	 */
	public static String getDataFromUrl(String method, final String urlString, int lineToPass,
			Triple<String, String, Integer> proxy) throws IOException {
		Proxy proxyNet = getNetProxy(proxy);
		HttpsURLConnection con = null;
		String[] urls = StringUtils.split(urlString, '?');
		String urlBase = urls[0];
		String urlParameters = urls[1];
		StringBuffer response = null;

		final URL obj = new URL(urlBase);

		if (proxyNet != null) {
			con = (HttpsURLConnection) obj.openConnection(proxyNet);
		} else {
			logger.info("====== There is no proxy specified for this request =======");
			logger.info("This request is not allowed " + urlString + " without using proxy, please try again");
			// throw new CoreException("This request is not allowed (Get
			// Data
			// from" + urlString + ") without using proxy, please try
			// again");
			con = (HttpsURLConnection) obj.openConnection();
		}

		// add request header
		con.setRequestMethod(method);
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setConnectTimeout(5000);
		con.setDoOutput(true);

		// Send request

		final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();

		response = new StringBuffer();

		final int responseCode = con.getResponseCode();
		if (responseCode == HttpStatus.SC_SERVICE_UNAVAILABLE) {
			logger.error("Error HTTP 503 Service unavailable");
		} else if (responseCode == HttpStatus.SC_OK) {
			logger.info("Service: OK {}", urlString);
			String inputLine;
			try (final BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
			}
		} else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
			// this.defineProxyAsBlacklisted();

		}

		return response.toString();
	}

	public static ImmutablePair<String, Integer> getAvailableData(final String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		urlConnection.connect();

		int size = 10240;
		int bufferReadSize;
		byte[] buffer = new byte[size];
		InputStream is = urlConnection.getInputStream();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		while ((bufferReadSize = is.read(buffer, 0, size)) >= 0) {
			byteArrayOutputStream.write(buffer, 0, bufferReadSize);
		}

		byte[] bytes = byteArrayOutputStream.toByteArray();

		return ImmutablePair.of(new String(bytes, 0, bytes.length, StandardCharsets.UTF_8),
				urlConnection.getResponseCode());

	}

	/**
	 * Try to load the image from an URL
	 * 
	 * @param urlString The URL
	 * @return the buffered image (can be null)
	 * @throws IOException On loading failed
	 */
	public static BufferedImage getImagesFromUrl(final String urlString) throws IOException {
		if (StringUtils.isNotBlank(urlString)) {
			final URL url = new URL(urlString);
			return ImageIO.read(url);
		}
		return null;
	}

	public static byte[] getImagesFromUri(File imgPath) throws IOException {

		return IOUtils.toByteArray(new FileInputStream(imgPath));
	}

	@SuppressWarnings("unused")
	private byte[] resizeImage(BufferedImage image, String format) throws IOException {

//		 IOUtils.write(data, output);
//		BufferedImage img = ImageIO.read(new FileInputStream(imgPath));

		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		ImageIO.write(Scalr.resize(image, Method.QUALITY, Mode.FIT_TO_HEIGHT, 150, Scalr.OP_ANTIALIAS), format, bais);

		return bais.toByteArray();
	}

	public static String getContent(String url) throws Exception {
		StringBuffer result = new StringBuffer();
		BufferedReader rd = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpget);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			try {
				rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

			} finally {
				response.close();
				if (rd != null) {
					rd.close();
				}
			}

			return result.toString();
		} else {
			throw new Exception("Error " + statusCode + ": Failed to get content from " + url);
		}

	}

	public static CloseableHttpResponse getConnection(String url) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader(HttpHeaders.USER_AGENT, USER_AGENT);
		CloseableHttpResponse response = httpclient.execute(httpget);
		return response;
	}

	public static Pair<HttpEntity, Integer> makHttpRequest(final String uri, final int port, final String userName,
			final String password) throws ClientProtocolException, IOException {
		Pair<HttpEntity, Integer> result = null;
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		Matcher match = Pattern.compile("([a-z]*\\.[a-z]{2,4})").matcher(uri);
		credsProvider.setCredentials(new AuthScope(match.find() == true ? match.group() : uri, port),
				new UsernamePasswordCredentials(userName, password));
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		try {
			HttpGet httpget = new HttpGet(uri);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				result = Pair.of(response.getEntity(), response.getStatusLine().getStatusCode());
			} finally {
				if (response != null)
					response.close();
			}
		} finally {
			if (httpclient != null)
				httpclient.close();
		}
		return result;
	}

	public Pair<Document, StatusLine> getDataFromUrl(String url) throws Exception {
		Document content = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader(HttpHeaders.USER_AGENT, USER_AGENT);
			CloseableHttpClient httpclient = httpConnect.getHttpConnect();
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					content = Jsoup.parse(instream, StandardCharsets.UTF_8.name(), url);
				} finally {
					instream.close();
				}
			}
		} catch (IOException e) {
			throw new IOException("Error: can't get content from url " + url, e);
		} finally {
			response.close();
		}
		return Pair.of(content, response.getStatusLine());
	}

	public Pair<MutableTriple<Document, Integer, String>, Triple<String, String, Integer>> getDataFromUrl(
			String urlDetail, Triple<String, String, Integer> proxy) throws Exception {
		Document content = null;
		String contentString = "";
		boolean parsDoc = false;
		if ((proxy != null) || (proxy == null && !isGooglePlay(urlDetail))) {

			CloseableHttpClient httpclient = httpConnect.getHttpConnect();
			RequestConfig config = null;

			if (isGooglePlay(urlDetail)) {
				String host = proxy.getMiddle();
				int port = proxy.getRight();
				HttpHost proxyHttp = new HttpHost(host, port);
				config = RequestConfig.custom().setProxy(proxyHttp).setConnectionRequestTimeout(2000).build();
				parsDoc = true;
			} else {
				config = RequestConfig.custom().setConnectTimeout(2000).build();
			}

			HttpGet request = new HttpGet(urlDetail);
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
						if (parsDoc) {
							content = Jsoup.parse(is, StandardCharsets.UTF_8.name(), urlDetail);
						} else {
							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							int bufferReadSize;
							while ((bufferReadSize = is.read(BUFFER, 0, BUFFER_SIZE)) >= 0) {
								byteArrayOutputStream.write(BUFFER, 0, bufferReadSize);
							}
							byte[] bytes = byteArrayOutputStream.toByteArray();
							contentString = new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
						}

					} finally {
						is.close();
					}
				}
			} catch (IOException e) {
				throw new IOException(e);
			}

			return Pair.of(MutableTriple.of(content, response.getStatusLine().getStatusCode(), contentString), proxy);
		} else {
			return null;
		}

	}

	private static boolean isGooglePlay(String urlDetail) {
		if (urlDetail.contains("google")) {
			return true;
		}
		return false;
	}

	private static Proxy getNetProxy(Triple<String, String, Integer> proxy) {
		if (proxy != null) {
			return new Proxy(Type.valueOf(proxy.getLeft().toUpperCase()),
					new InetSocketAddress(proxy.getMiddle(), proxy.getRight()));
		}
		return null;
	}

	public static String getDataFromUrlNoProxy(String methode, String url, int lineToPass) {
		StringBuffer response = null;
		try {
			String[] urls = StringUtils.split(url, '?');
			String urlBase = urls[0];
			String urlParameters = urls[1];
			URL obj = new URL(urlBase);

			final HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod(methode);
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setDoOutput(true);

			try (final DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.writeBytes(urlParameters);
				wr.flush();
			}
			response = new StringBuffer();
			final int responseCode = con.getResponseCode();

			if (responseCode == 200) {
				String inputLine;
				try (final BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
					for (int i = 0; i < lineToPass; i++) {
						in.readLine();
					}
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			// LOGGER.error("Errors occurred in
			// ConnectionUtils#getDataFromUrlNoProxy()", e);
		}
		return response.toString();
	}

	public String getData(final String urlReview, int line, final Triple<String, String, Integer> proxy)
			throws Exception {
		if (proxy != null) {
			String host = proxy.getMiddle();
			int port = proxy.getRight();
			CloseableHttpClient httpclient = httpConnect.getHttpConnect();
			StringBuilder sb = new StringBuilder();

			try {
				HttpHost proxyHttp = new HttpHost(host, port);
				RequestConfig config = RequestConfig.custom().setProxy(proxyHttp).setConnectionRequestTimeout(5000)
						.build();
				HttpPost request = new HttpPost(urlReview);
				request.setConfig(config);
				CloseableHttpResponse response = httpclient.execute(request);
				try {
					// final String content = EntityUtils.toString(response.getEntity());
					String inputLine;
					try (final BufferedReader in = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
						for (int i = 0; i < line; i++) {
							in.readLine();
						}
						while ((inputLine = in.readLine()) != null) {
							sb.append(inputLine);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					response.close();
				}

			} catch (

			IOException e1) {
				// log.info("ERROR CHECK PROXY {} {}", proxy, e1);

			}

			return sb.toString();
		} else {
			return null;
		}
	}
	
	public static String getData() {
		InputStream inputStream = ConnectionUtils.class
				.getClassLoader().getResourceAsStream("m_data.html");
		try {
			return IOUtils.toString(inputStream,StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}