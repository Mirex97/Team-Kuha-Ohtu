package bookmarks.util;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import bookmarks.util.auth.YoutubeAuth;
import java.io.IOException;
import java.sql.SQLException;

public class YoutubeApi {

    public Video getYoutubeItemDetails(String id) throws SQLException, IOException{
        try {
            YouTube youtube = new YouTube.Builder(YoutubeAuth.HTTP_TRANSPORT, YoutubeAuth.JSON_FACTORY, new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("APP_ID").build();

            String apiKey = "AIzaSyDZKySIHVQRL9Trsb7vLZ7uRMSQRZv7OfA"; // you can get it from https://console.cloud.google.com/apis/credentials
            YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet");
			
            listVideosRequest.setId(id); // add list of video IDs here
            listVideosRequest.setKey(apiKey);
            VideoListResponse listResponse = listVideosRequest.execute();
			
			if (listResponse.getItems().isEmpty()) {
				return null;
			}
			
			return listResponse.getItems().get(0);

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}