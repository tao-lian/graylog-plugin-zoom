package taolian.graylog;

import java.io.IOException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ZoomClient {
	private String _endpoint, _token;
	private String _proxyAddress, _proxyUser, _proxyPassword;
	
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
	
	public void Send(String message) throws Exception {
		try(CloseableHttpClient httpclient = HttpClients.createDefault()){
		    HttpPost req = new HttpPost(this._endpoint);
		    req.setEntity(new StringEntity(message));
		    req.setHeader(HttpHeaders.AUTHORIZATION, this._token);
		    try (CloseableHttpResponse resp = httpclient.execute(req)) {
		    	if(resp.getStatusLine().getStatusCode() != 200)
					throw new Exception("Failed to POST to Zoom endpoint.");
		    }
		}
	}
}
