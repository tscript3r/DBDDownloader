package pl.tscript3r.dbdd.utils;

import javax.net.ssl.*;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Disables SSL verification for all hosts
 *
 * @author ADzierzon
 */
class OpenTrustManager {

    private static final TrustManager[] trustAllCerts = new TrustManager[]{
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
                public void checkClientTrusted(X509Certificate[] xcs, String string, Socket socket) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string, Socket socket) {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) {

                }
            }
    };


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