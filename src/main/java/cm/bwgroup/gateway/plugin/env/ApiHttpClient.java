package cm.bwgroup.gateway.plugin.env;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiHttpClient {

    public ApiHttpClient() {
    }

    public void executeSyncGetRequest(String uri, @Nullable String secretKey) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request  = HttpRequest.newBuilder()
                .header("Authorization", "Bearer "+ secretKey)
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }

    public CompletableFuture<String> executeAsyncGetRequest(String uri, @Nullable String secretKey) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request  = HttpRequest.newBuilder()
                .header("Authorization", "Bearer "+ secretKey)
                .uri(URI.create(uri))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    public void executePostRequest(String uri, String data, @Nullable String secretKey) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer "+ secretKey)
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.statusCode());
    }
}
