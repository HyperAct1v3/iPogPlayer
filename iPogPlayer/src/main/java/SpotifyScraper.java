import okhttp3.*;
import com.google.gson.*;


public class SpotifyScraper {
    private static final String API_KEY = "34434ab380mshf1dbd9d7657b1dbp19e378jsnf3cd8322411c";
    private static final String API_HOST = "spotify-scraper.p.rapidapi.com";

    private final OkHttpClient client = new OkHttpClient();

    public JsonObject search(String query) throws Exception {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(API_HOST)
                .addPathSegments("v1/search")
                .addQueryParameter("term", query)
                .addQueryParameter("type", "track")
                .addQueryParameter("limit", "100")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", API_KEY)
                .addHeader("X-RapidAPI-Host", API_HOST)
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        String responseBody = response.body().string();

        System.out.println("DEBUG: API RESPONSE BODY: " + responseBody);

        return JsonParser.parseString(responseBody).getAsJsonObject();
    }

    public void printSearchResults(JsonObject json) {
        JsonArray results = json.getAsJsonObject("tracks").getAsJsonArray("items");

        for (int i = 0; i < results.size(); i++) {
            JsonObject track = results.get(i).getAsJsonObject();

            String title = track.get("title").getAsString();
            String artist = track.getAsJsonArray("artists")
                    .get(0).getAsJsonObject()
                    .get("name").getAsString();

            System.out.println((i + 1) + ". " + title + " - " + artist);
        }

    }

    public Track[] buildTrackArray(JsonObject json) {
        if (!json.has("tracks") || json.get("tracks").isJsonNull()) {
            return new Track[0];
        }
        JsonObject tracksObj = json.getAsJsonObject("tracks");

        if (!tracksObj.has("items") || tracksObj.get("items").isJsonNull()) {
            return new Track[0];
        }

        JsonArray items = tracksObj.getAsJsonArray("items");
        int size = items.size();
        Track[] tracks = new Track[size];

        for (int i = 0; i < size; i++) {
            try {
                JsonObject obj = items.get(i).getAsJsonObject();
                String title = obj.has("name") ? obj.get("name").getAsString() : "Unknown Title";
                String artist = "Unknown Artist";
                if (obj.has("artists") && !obj.getAsJsonArray("artists").isEmpty()) {
                    artist = obj.getAsJsonArray("artists")
                            .get(0).getAsJsonObject()
                            .get("name").getAsString();
                }
                String spotifyUrl = "";
                if (obj.has("id")) {
                    String id = obj.get("id").getAsString();
                    spotifyUrl = "https://open.spotify.com/track/" + id;
                }
                tracks[i] = new Track(title, artist, spotifyUrl);

            } catch (Exception e) {
                System.out.println("Skipped a bad Track" + e.getMessage());
            }
        }

        return tracks;

    }
}
