package taolian.graylog;

import java.io.IOException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZoomClient {
	private static final Logger LOG = LoggerFactory.getLogger(ZoomClient.class);
	
	private String _endpoint, _token;
	private String _proxyAddress, _proxyUser, _proxyPassword;
	private ZoomPayloadFormat _format;

	enum ZoomPayloadFormat {
		JSON, TEXT
	}

	public ZoomClient(String endpoint, String token) {
		_endpoint = endpoint;
		_token = token;
	}

	public void SetProxyAddress(String proxyAddress) {
		_proxyAddress = proxyAddress;
	}

	public void SetProxyUser(String proxyUser) {
		_proxyUser = proxyUser;
	}

	public void SetProxyPassword(String proxyPassword) {
		_proxyPassword = proxyPassword;
	}

	public void SetPayloadFormat(ZoomPayloadFormat format) {
		_format = format;
	}

	public void Send(String message) throws Exception {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			String url = _format == ZoomPayloadFormat.JSON ? String.format("%s?format=full", _endpoint) : _endpoint;
			HttpPost req = new HttpPost(url);

			req.setEntity(new StringEntity(message));
			req.setHeader(HttpHeaders.AUTHORIZATION, this._token);
			if(_format == ZoomPayloadFormat.JSON) {
				req.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			}
			
			LOG.info("URL: {}", url);
			LOG.info("Body: {}", message);
			try (CloseableHttpResponse resp = httpclient.execute(req)) {
				int statusCode = resp.getStatusLine().getStatusCode();
				if ( statusCode != 200) {					
					throw new Exception(String.format("Failed to POST to Zoom endpoint, %s:%s", statusCode, resp.getStatusLine().getReasonPhrase()));
				}
			}
		}
	}
}
