package eu.escapeadvisor.yournews;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static eu.escapeadvisor.yournews.MainActivity.LOG_TAG;

public class QueryUtils {

    private QueryUtils() {

    }

    private static List<Article> extractFeatureFromJson(String articleJSON, Context context) {

        context = context.getApplicationContext();
        String date = context.getResources().getString(R.string.no_info);

        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        List<Article> articles = new ArrayList<>();

        try {
            JSONObject data = new JSONObject(articleJSON);

            JSONObject response = data.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                String author = context.getResources().getString(R.string.no_author);
                JSONObject currentArticle = results.getJSONObject(i);
                String section = currentArticle.getString("sectionId").toString();
                String title = currentArticle.getString("webTitle").toString();
                String webUrl = currentArticle.getString("webUrl").toString();

                if (!currentArticle.isNull("webPublicationDate")) {
                    date = currentArticle.getString("webPublicationDate").toString();
                }

                JSONArray tags = currentArticle.getJSONArray("tags");
                int length = tags.length();
                boolean tagsNull = tags.isNull(0);
                if (tags != null) {

                    if (!tagsNull && length > 0) {
                        JSONObject authorInfo = tags.getJSONObject(0);

                        if (!authorInfo.isNull("webTitle"))
                            author = authorInfo.getString("webTitle").toString();
                    }
                }

                Article article = new Article(section, title, webUrl, author, date);
                articles.add(article);
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return articles;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode() + urlConnection.getErrorStream());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static List<Article> fetchArticleData(String requestUrl, Context context) {
        context = context.getApplicationContext();
        Log.i("fetchArticleData()", "fetchArticleData() was called");
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Article> articles = extractFeatureFromJson(jsonResponse, context);
        return articles;

    }

}
