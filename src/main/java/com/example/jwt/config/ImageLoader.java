package com.example.jwt.config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ImageLoader {

    public String loadImage(String imageUrl) {
        // Create an HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create a request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .build();

        // Send the request asynchronously and return a CompletableFuture
        CompletableFuture<String> completableFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int statusCode = response.statusCode();
                    if (statusCode == 200) { // If response is successful
                        return response.body(); // Return the image data as String
                    } else {
                        // If response is not successful, return null or handle as per requirement
                        return null;
                    }
                })
                .exceptionally(ex -> {
                    // Handle exception if any occurred during request
                    ex.printStackTrace();
                    return null;
                });

        try {
            // Wait for CompletableFuture to complete and return the image data
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            // Handle any exception occurred during CompletableFuture execution
            e.printStackTrace();
            return null;
        }
    }
}

