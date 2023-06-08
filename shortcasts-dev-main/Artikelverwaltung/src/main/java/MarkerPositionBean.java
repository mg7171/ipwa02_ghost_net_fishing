import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import jakarta.enterprise.context.RequestScoped; 
import jakarta.inject.Named;

@Named
@RequestScoped
public class MarkerPositionBean {

    public List<MarkerPosition> getMarkerPositionObject() {
        String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=getMarkerPositions";
        String json = getJsonFromUrl(url);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<MarkerPosition>>() {}.getType();
        List<MarkerPosition> markerPositions = gson.fromJson(json, listType);
        return markerPositions;
    }
    
    public String getMarkerPositions() {
        List<MarkerPosition> markerPositions = getMarkerPositionObject();
        Gson gson = new Gson();
        String json = gson.toJson(markerPositions);
        return json;
    }
    
    public List<MarkerPosition> getCurrentUserNetsObject() {
        String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=getCurrentUserNets";
        String json = getJsonFromUrl(url);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<MarkerPosition>>() {}.getType();
        List<MarkerPosition> markerPositions = gson.fromJson(json, listType);
        return markerPositions;
    }
    
    public String getCurrentUserNets() {
        List<MarkerPosition> markerPositions = getCurrentUserNetsObject();
        Gson gson = new Gson();
        String json = gson.toJson(markerPositions);
        return json;
    }


    private String getJsonFromUrl(String url) {
        StringBuilder json = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            URL apiUrl = new URL(url);
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return json.toString();
    }
	
}