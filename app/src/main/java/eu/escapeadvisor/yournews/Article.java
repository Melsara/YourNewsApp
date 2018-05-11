package eu.escapeadvisor.yournews;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    private String mSection;
    private String mTitle;
    private String mAuthor;
    private String mDate;
    private String mWebUrl;
    private String mThumbnail;

    public Article (String section, String title, String webUrl, String author, String date) {
        mSection = section;
        mTitle = title;
        mWebUrl = webUrl;
        mAuthor = author;
        mDate = date;
    }

    public Article (String section, String title, String webUrl, String author, String date, String thumbnail) {
        mSection = section;
        mTitle = title;
        mWebUrl = webUrl;
        mAuthor = author;
        mDate = date;
        mThumbnail = thumbnail;
    }

    protected Article(Parcel in) {
        mThumbnail = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

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

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mThumbnail);

    }
}


