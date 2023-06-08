import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class LoginHandlerBean {
    private String username;
    private String password;
    private String userdata;

    public void login() {
        String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=login";
        String parameters = "username=" + username + "&password=" + password;

        String responseJson = sendPostRequest(url, parameters);
        JsonReader jsonReader = Json.createReader(new StringReader(responseJson));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        if (jsonObject.containsKey("session_token")) {
            String sessionToken = jsonObject.getString("session_token");

            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            Cookie sessionCookie = new Cookie("session_token", sessionToken);
            response.addCookie(sessionCookie);

            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (IOException e) {
                // Handle the exception...
            }
        }
    }
    
    public void checkUserSession() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        String sessionToken = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_token")) {
                    sessionToken = cookie.getValue();
                    break;
                }
            }
        }

        if (sessionToken != null) {
            String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=checkUserSession";
            String parameters = "session_token=" + sessionToken; 

            this.userdata = sendPostRequest(url, parameters);
        }
    }
    
    public void logout() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        String sessionToken = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_token")) {
                    sessionToken = cookie.getValue();
                    break;
                }
            }
        }

        if (sessionToken != null) {
            String url = "https://gabriel.lol/projects/studium/ipwa02_GhostNetFishing/index.php?action=doLogout";
            String parameters = "session_token=" + sessionToken; 

            this.userdata = sendPostRequest(url, parameters);
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (IOException e) {
                // Log exception here
                e.printStackTrace();
            }
        }
    }
    

    private String sendPostRequest(String url, String parameters) {
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

                System.out.println("POST request [" + url + "]sent successfully");
                System.out.println("Response: " + response.toString());
                return response.toString();
            } else {
                System.out.println("POST request failed. Response Code: " + responseCode);
                return "error";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return "error";
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserdata() {
        return userdata;
    }

    public void getUserdata(String userdata) {
        this.userdata = userdata;
    }
}