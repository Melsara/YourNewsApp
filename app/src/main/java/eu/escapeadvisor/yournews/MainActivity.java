package eu.escapeadvisor.yournews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>{
    private ProgressBar progressBar;
    private TextView emptyView;
    private ImageView emptyImage;
    static final String URL_KEY =
            "https://content.guardianapis.com/search";
    private ArticleAdapter mAdapter;
    private static int ARTICLE_LOADER_ID = 1;
    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        emptyView = findViewById(R.id.emptyView);
        emptyImage = findViewById(R.id.emptyImage);
        final ListView articleListView = (ListView) findViewById(R.id.list);
        articleListView.setEmptyView(emptyView);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        articleListView.setAdapter(mAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article currentArticle = mAdapter.getItem(i);
                String url = currentArticle.getWebUrl();
                Intent openArticle = new Intent(Intent.ACTION_VIEW);
                openArticle.setData(Uri.parse(url));
                PackageManager pm = getPackageManager();
                List<ResolveInfo> activities = pm.queryIntentActivities(openArticle, PackageManager.MATCH_ALL);
                boolean launchIntent = activities.size() > 0;
                if (launchIntent) {
                    startActivity(openArticle);
                }
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            Log.i("initLoader()", "initLoader() is about to be called");
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

        } else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
            emptyImage.setImageResource(R.drawable.sad);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(URL_KEY);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "68e50147-ab2a-43e8-83c8-2cbdb885c2ce");
        uriBuilder.appendQueryParameter("section", "technology");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", orderBy);

        Log.i("onCreateLoader()", "onCreateLoader() was called and this was the URL" + uriBuilder.toString());

        return new ArticleLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        Log.i("onLoadFinished()", "onLoadFinished() was called");
        progressBar.setVisibility(View.GONE);
        mAdapter.clear();
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        } else {
            emptyView.setText(R.string.empty_view);
            emptyImage.setImageResource(R.drawable.newspaper);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.i("onLoadReset()", "onLoadReset() was called");
        mAdapter.clear();
    }

}
