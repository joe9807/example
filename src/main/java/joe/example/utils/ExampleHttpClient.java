package joe.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.Example;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExampleHttpClient {
    public static void sendRequest(Example example) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            final HttpUriRequest httpRequest = RequestBuilder.post(example.getCallbackUrl()).setCharset(StandardCharsets.UTF_8)
                    .setEntity(new StringEntity(mapper.writeValueAsString(example), ContentType.APPLICATION_JSON)).build();

            CloseableHttpResponse response = createHttpClient().execute(httpRequest);
            if (HttpStatus.valueOf(response.getStatusLine().getStatusCode()) != HttpStatus.OK) {
                throw new RuntimeException("Response code is no OK: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient createHttpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        //if (useProxy) {
          //  builder = disableVerification(HttpClientBuilder.create()).setProxy(new HttpHost("127.0.0.1", 8888));
        //}
        return builder.build();
    }

    private static HttpClientBuilder disableVerification(HttpClientBuilder builder) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
            }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            builder.setSSLContext(sc);
            builder.setSSLHostnameVerifier((arg0, arg1) -> true);
        } catch (Exception ignored) {}

        return builder;
    }
}
