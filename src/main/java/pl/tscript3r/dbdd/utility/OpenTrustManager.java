package pl.tscript3r.dbdd.utility;

import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

/**
 * Disables SSL verification
 * @author ADzierzon
 *
 */
public class OpenTrustManager {

	private static TrustManager[] trustAllCerts = new TrustManager[] { 
		new X509ExtendedTrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
	
			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
	
			@Override
			public void checkClientTrusted(X509Certificate[] xcs, String string, Socket socket)
					throws CertificateException {
	
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] xcs, String string, Socket socket)
					throws CertificateException {
	
			}
	
			@Override
			public void checkClientTrusted(X509Certificate[] xcs, String string, SSLEngine ssle)
					throws CertificateException {
	
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] xcs, String string, SSLEngine ssle)
					throws CertificateException {
	
			}
		}
	};


	/**
	 * Disables SSL verification for all hosts
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
    public static void apply() throws NoSuchAlgorithmException, KeyManagementException {
    	SSLContext sc = SSLContext.getInstance("SSL");
    	sc.init(null, trustAllCerts, new java.security.SecureRandom());
    	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    	// Create all-trusting host name verifier
    	HostnameVerifier allHostsValid = new HostnameVerifier() {
    		@Override
    		public boolean verify(String hostname, SSLSession session) {
    			return true;
    		}
    	};

    	// Install the all-trusting host verifier
    	HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}