package eu.escapeadvisor.yournews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>{
    ProgressBar progressBar;
    TextView emptyView;
    ImageView emptyImage;
    static final String URL_KEY =
            "https://content.guardianapis.com/search?order-by=newest&section=technology&page-size=20&api-key=[API-KEY]&show-fields=thumbnail&show-tags=contributor";
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
                startActivity(openArticle);
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
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Log.i("onCreateLoader()", "onCreateLoader() was called");
        return new ArticleLoader(this, URL_KEY);

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
