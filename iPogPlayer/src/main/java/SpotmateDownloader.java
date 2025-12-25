import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class SpotmateDownloader {
    private static final String API_KEY = "34434ab380mshf1dbd9d7657b1dbp19e378jsnf3cd8322411c";
    private static final String API_HOST = "spotify-downloader12.p.rapidapi.com";
    private final OkHttpClient client = new OkHttpClient();

    public String getMp3Link(String spotifyUrl) throws Exception {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(API_HOST)
                .addPathSegment("convert")
                .addQueryParameter("urls", spotifyUrl)
                .build();

        RequestBody body = RequestBody.create("{}", MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("X-RapidAPI-Key", API_KEY)
                .addHeader("X-RapidAPI-Host", API_HOST)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String json = response.body().string();

            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            if (obj.has("url") && !obj.get("url").isJsonNull()) {
                return obj.get("url").getAsString();
            } else {
                throw new Exception("API ERROR : no url found :( full responce: " + json);
            }
        }
    }

    public File downloadMp3 (String mp3Url, String folder, String fileName) throws Exception {

        Request request = new Request.Builder().url(mp3Url).build();
        Response response = client.newCall(request).execute();

        byte[] data = response.body().bytes();

        File dir = new File(folder);

        if (!dir.exists()) {
            dir.mkdirs();
        }
            File file = new File(dir, fileName + ".mp3");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            }
        return file;
    }
}
