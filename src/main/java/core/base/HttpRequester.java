package base;

import org.apache.http.client.utils.URIBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequester {
    public static HttpResponse<String> sendGET (String endpoint, @Nullable Map headers, @Nullable Map params) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            var requestBuilder = buildRequest(endpoint, params);
            buildHeaders(requestBuilder, headers);
            HttpRequest request = requestBuilder.GET().build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static HttpResponse<String> sendPOST (String endpoint, @Nullable Map headers, @Nullable Map params, String payload) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            var requestBuilder = buildRequest(endpoint, params);
            // Add Headers
            buildHeaders(requestBuilder, headers);
            // Build the request
            HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(payload)).build();
            // Send the request
            return  httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static HttpResponse<String> sendPUT (String endpoint, @Nullable Map headers, @Nullable Map params, String payload) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            var requestBuilder = buildRequest(endpoint, params);
            // Add Headers
            buildHeaders(requestBuilder, headers);
            // Build the request
            HttpRequest request = requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(payload)).build();
            // Send the request
            return  httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private static HttpRequest.Builder buildRequest (String endpoint, @Nullable Map params) {
        try {
            URIBuilder builder = new URIBuilder(endpoint);
            // Add parameters
            buildParams(builder, params);
            // Build URI
            URI uri = builder.build();
            return HttpRequest.newBuilder(uri);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private static void buildParams (URIBuilder builder, @Nullable Map<String, String> params) {
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }
        } else System.out.println("The Parameters are intentionally left blank.");
    }
    private static void buildHeaders(HttpRequest.Builder requestBuilder, @Nullable Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        } else System.out.println("The Headers are intentionally left blank.");
    }
}