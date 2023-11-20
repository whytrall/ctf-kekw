package org.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        String urlString = "http://webhook.site/db55cde0-f081-48ee-999b-4287bee53acb";

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the request method (GET is the default)
        conn.setRequestMethod("GET");

        // Get the server response code to determine if the request was successful
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        // If responseCode is 200, the request was successful
        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print result
            System.out.println(response.toString());
        }
    }
}