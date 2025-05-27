package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ExchangeRateClient {
    // Get the key from environment variable
    private static final String API_KEY = System.getenv("EXCHANGE_API_KEY");

    // Fail if it's mising
    // static initializer, runs exactly ONCE when the class is first loaded by the JVM
    static {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException(
                    "Missing required EXCHANGE_API_KEY environment variable");
        }
    }

    // Build the API URL
    private static final String BASE_URL =
            "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest";


    private final HttpClient httpClient;
    private final Gson gson;

    // ExchangeRate object constructor
    public ExchangeRateClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.gson = new Gson();
    }

    /**
     * Fetches latest rates from `base` → `target`
     */
    public RateResponse getRate(String base, String target)
            throws IOException, InterruptedException {
        // Build the request URL
        String uri = String.format("%s/%s", BASE_URL, base);

        // Create the HTTP Request
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .build();

        // Send the request and get a response (String)
        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Check the status code
        if (resp.statusCode() != 200) {
            throw new IOException("HTTP " + resp.statusCode() + ": " + resp.body());
        }

        // check for errors before parsing
        JsonObject jsonTree = gson.fromJson(resp.body(), JsonObject.class);
        if (jsonTree.has("error")) {
            throw new IOException("API error: " + jsonTree.get("error").getAsJsonObject());
        }

        // Map the JSON into our RateResponse Object and return
        return gson.fromJson(resp.body(), RateResponse.class);
    }
}
