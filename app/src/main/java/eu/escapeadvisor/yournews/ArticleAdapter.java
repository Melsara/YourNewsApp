package eu.escapeadvisor.yournews;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    String date;
    private String onlyDate;
    private String onlyTime;
    private String TIME_SEPARATOR = "T";

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

        ImageView imageThumb = (ImageView) listItemView.findViewById(R.id.textView_image);
        String webThumb = currentArticle.getThumbnail();
        Glide.with(getContext())
                .load(webThumb)
                .placeholder(R.drawable.newspaper)
                .error(R.drawable.newspaper)
                .centerCrop()
                .into(imageThumb);

        return listItemView;
    }

    private String formatDate(String date) {
        String[] parts = date.split(TIME_SEPARATOR);
        onlyDate = parts[0];
        return onlyDate;
    }

    private String formatTime(String date) {
        String[] parts = date.split(TIME_SEPARATOR);
        onlyTime = parts[1];
        if (onlyTime != null && onlyTime.length() > 0 && onlyTime.charAt(onlyTime.length() - 1) == 'Z') {
            onlyTime = onlyTime.substring(0, onlyTime.length() - 1);
        }
        return onlyTime;
    }
}
