package eu.escapeadvisor.yournews;

public class Article {

    private String mSection;
    private String mTitle;
    private String mAuthor;
    private String mDate;
    private String mWebUrl;

    public Article (String section, String title, String webUrl, String author, String date) {
        mSection = section;
        mTitle = title;
        mWebUrl = webUrl;
        mAuthor = author;
        mDate = date;
    }

    public String getSection () {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

}


