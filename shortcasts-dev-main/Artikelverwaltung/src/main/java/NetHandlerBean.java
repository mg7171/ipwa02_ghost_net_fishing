import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class NetHandlerBean {
    private String lat;
    private String lng;
    private String length;
    private String width;
    private String height;
    private String id;

    public void save() {
        String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=registerGhostNet";
        String parameters = "lat=" + lat + "&lng=" + lng + "&description=" + length + "m x " + width + "m x " + height + "m";

        sendPostRequest(url, parameters);
    }
    
    public void register() {
        String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=registerRescue";
        String parameters = "id=" + id;

        sendPostRequest(url, parameters);
    }
    
    public void lost() {
        String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=netLost";
        String parameters = "id=" + id;

        sendPostRequest(url, parameters);
    }
    
    public void rescued() {
        String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=netRescued";
        String parameters = "id=" + id;

        sendPostRequest(url, parameters);
    }

    private void sendPostRequest(String url, String parameters) {
        HttpURLConnection connection = null;
        try {
            URL apiUrl = new URL(url);
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataLength));

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(postData);
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println("POST request sent successfully");
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("POST request failed. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
        this.id = id;
    	
    }
}