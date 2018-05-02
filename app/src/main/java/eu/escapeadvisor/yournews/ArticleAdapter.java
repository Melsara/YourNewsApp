package eu.escapeadvisor.yournews;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    String date;
    private String onlyDate;
    private String onlyTime;
    private String DATE_SEPARATOR = "-";
    private String TIME_SEPARATOR = "T";
    private String Z_SEPARATOR = "Z ";

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        String section = currentArticle.getSection();
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.textView_section);
        sectionTextView.setText(section);

        String title = currentArticle.getTitle();
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.textView_title);
        titleTextView.setText(title);

        String author = currentArticle.getAuthor();
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.textView_author);
        authorTextView.setText(author);

        String date = currentArticle.getDate();
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.textView_date);
        String formattedDate = formatDate(date);
        dateTextView.setText(formattedDate);
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.textView_time);
        String formattedTime = formatTime(date);
        timeTextView.setText(formattedTime);

        return listItemView;
    }

    private String formatDate(String date) {
        String[] parts = date.split(TIME_SEPARATOR);
        onlyDate = parts[0];
        return onlyDate;
    }

    private String formatTime(String date) {
        String[] parts = date.split(TIME_SEPARATOR);
        String zTime = parts[1];
        String[] timeParts = zTime.split(Z_SEPARATOR);
        onlyTime = timeParts[0];
        return onlyTime;
    }
}
