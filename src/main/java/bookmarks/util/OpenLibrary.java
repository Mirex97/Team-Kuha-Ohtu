package bookmarks.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OpenLibrary {
	OkHttpClient client = new OkHttpClient();

	public JsonObject getByISBN(String isbn) throws IOException {
		String id = String.format("ISBN:%s", isbn);
		String url = String.format("https://openlibrary.org/api/books?bibkeys=%s&format=json&jscmd=data", id);
		Request request = new Request.Builder().url(url).get().build();

		Response response = client.newCall(request).execute();
		assert response.body() != null;

		JsonElement data = new JsonParser().parse(response.body().charStream());
		return data.getAsJsonObject().get(id).getAsJsonObject();
	}
}
