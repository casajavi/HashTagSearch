package javier.com.hashtagsearch.models;

import android.support.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Javi on 4/30/17.
 */

public class Search extends RealmObject {

    @NonNull
    @PrimaryKey
    private String query;

    @NonNull
    private RealmList<SearchTweet> searchTweets;

    public Search() {
    }

    public Search(@NonNull String query, @NonNull RealmList<SearchTweet> searchTweets) {
        this.query = query;
        this.searchTweets = searchTweets;
    }

    @NonNull
    public String getQuery() {
        return query;
    }

    public void setQuery(@NonNull String query) {
        this.query = query;
    }

    @NonNull
    public RealmList<SearchTweet> getSearchTweets() {
        return searchTweets;
    }

    public void setSearchTweets(@NonNull RealmList<SearchTweet> searchTweets) {
        this.searchTweets = searchTweets;
    }
}
