package eu.escapeadvisor.yournews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.i("startLoading()", "startLoading() was called");
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        Log.i("loadInBackground()", "loadInBackground() was called");
        if (mUrl == null) {
            return null;
        }

        final List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }
}
