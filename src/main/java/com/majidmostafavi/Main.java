package com.majidmostafavi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        // Obtain a client that uses the default settings.
        final URI uri  = new URI("http://www.google.com/");
       // HttpClient myHC = HttpClient.newHttpClient();

        // Create a request.
/*        HttpRequest myReq = HttpRequest.newBuilder(
                new URI("http://www.google.com/")).build();*/


        ThreadFactory threadFactory = Thread.ofVirtual().factory();

       // Thread.startVirtualThread(runnable);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 1_000_000).forEach(i -> {
                executor.submit( () -> {
                    try {
                        Thread.sleep(Duration.ofSeconds(10));
                        HttpClient hc = HttpClient.newBuilder().build();
                        HttpRequest hr = HttpRequest.newBuilder(uri).GET().build();
                        HttpResponse<InputStream> myResp = hc.send(hr, HttpResponse.BodyHandlers.ofInputStream());
                        HttpHeaders hdrs = myResp.headers();
                        InputStream input = myResp.body();
                        System.out.println(Thread.currentThread().isVirtual() + ":" +i);
                    } catch (InterruptedException  |IOException e) {
                        throw new RuntimeException(e);
                    }


                    // Send the request and get the response. Here, an InputStream is
                    // used for the body.


                });
                // var future1 = executor.submit(() -> myResp);
                // var future2 = executor.submit(() -> fetchURL(url2));
                // response.send(future1.get() + future2.get());
            });
        } catch (Exception e) {
          //  response.fail(e);
        }


    }
}