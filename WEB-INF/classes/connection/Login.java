package connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class Login {

	private String consumerKey;
	private String consumerSecret;

	private String tokenEndpoint;
	private String authEndpoint;
	private String callbackUrl;

	public Login() throws Exception {
		disableSslVerification();

		consumerKey = "JIsq3YltZhc2nKIKJlUTRm9ZKmoa";
		consumerSecret = "VdYN6S7UfQPXrBheoXPViODA1kga";

		tokenEndpoint = "https://172.0.17.2:9443/oauth2/token";
		authEndpoint = "https://172.0.17.2:9443/oauth2/authorize";
		callbackUrl = "https://172.0.17.1/";
	}

	private static void disableSslVerification() throws Exception {
		try
		{
        		// Create a trust manager that does not validate certificate chains
        		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                				return null;
            				}
            				public void checkClientTrusted(X509Certificate[] certs, String authType) {
            				}
            				public void checkServerTrusted(X509Certificate[] certs, String authType) {
					}
				}
			};

			// Install the all-trusting trust manager
        		SSLContext sc = SSLContext.getInstance("SSL");
        		sc.init(null, trustAllCerts, new java.security.SecureRandom());
        		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        	// Create all-trusting host name verifier
        		HostnameVerifier allHostsValid = new HostnameVerifier() {
            			public boolean verify(String hostname, SSLSession session) {
                			return true;
            			}
        		};

	        	// Install the all-trusting host verifier
        		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	   	} catch (Exception e) {
			System.out.println("Se fudeu mermao");
    		}
	}

	public String[] auth(String user, String pass) throws Exception {

		URL url = new URL(tokenEndpoint);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");

		//Header
		String encoded = Base64.getEncoder().encodeToString((consumerKey + ":" + consumerSecret).getBytes("UTF-8"));
		con.setRequestProperty("Authorization", "Basic " + encoded);
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		con.setDoOutput(true);

		String data = "grant_type=password&username=" + user + "&password=" + pass;
		byte[] dataInBytes = data.getBytes("UTF-8");
		OutputStream output = con.getOutputStream();
		output.write(dataInBytes);
		output.close();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String jsonString = response.toString();
		JSONObject jObject = new JSONObject(jsonString);
		String token = jObject.getString("access_token");
		String refresh = jObject.getString("refresh_token");
		String expiration = jObject.getString("expires_in");

		String[] params = new String[3];
		params[0] = token;
		params[1] = refresh;
		params[2] = expiration;
		return params;
	}

	public String search(String token, String gateway) throws Exception {
		URL url = new URL("https://172.0.17.4:5000/search");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");

		//Header
		con.setRequestProperty("Authorization","Bearer " + token);
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		con.setDoOutput(true);

		String data = "gateway=" + gateway + "&req=GetInfo";
		byte[] dataInBytes = data.getBytes("UTF-8");
		OutputStream output = con.getOutputStream();
		output.write(dataInBytes);
		output.close();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	public String getData(String token) throws Exception {
		URL url = new URL("https://172.0.17.4:5000/getdata");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");

                //Header
                con.setRequestProperty("Authorization","Bearer " + token);
                con.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                con.setDoOutput(true);

		BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                }
                in.close();

                return response.toString();
	}

}
