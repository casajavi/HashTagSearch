package javier.com.hashtagsearch.models;

import android.support.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Javi on 4/30/17.
 */

public class Search extends RealmObject {

    @NonNull
    private String query;

    @NonNull
    private RealmList<SearchTweet> searchTweets;

    public Search() {
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
