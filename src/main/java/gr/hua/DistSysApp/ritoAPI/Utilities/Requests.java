package gr.hua.DistSysApp.ritoAPI.Utilities;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public abstract class Requests {

    public static String get(String url) {

        HttpURLConnection urlConnection;
        URL targetUrl;
        int responseCode;

        String responseContent = "";

        try {
            targetUrl = new URL(url);
            urlConnection = (HttpURLConnection) targetUrl.openConnection();
            urlConnection.setRequestMethod("GET");

            urlConnection.setConnectTimeout(900000000);
            urlConnection.setReadTimeout(900000000);

            responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Success

                Scanner scan = new Scanner(new InputStreamReader(urlConnection.getInputStream()));

                while (scan.hasNextLine()) {
                    responseContent += scan.nextLine();
                }

                scan.close();
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseContent;
    }
}