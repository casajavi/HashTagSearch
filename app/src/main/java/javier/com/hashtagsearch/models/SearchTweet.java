package javier.com.hashtagsearch.models;

import android.support.annotation.Nullable;

import io.realm.RealmObject;

/**
 * Created by Javi on 4/30/17.
 */

public class SearchTweet extends RealmObject {

    private long id;

    private String tweetContent;

    private String username;

    private String profileName;

    private String userImageUrl;

    @Nullable
    private String tweetImage;

    public void initialize(long id, String tweetContent, String username, String profileName, String userImageUrl, String tweetImage) {
        this.id = id;
        this.tweetContent = tweetContent;
        this.username = username;
        this.profileName = profileName;
        this.userImageUrl = userImageUrl;
        this.tweetImage = tweetImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTweetContent() {
        return tweetContent;
    }

    public void setTweetContent(String tweetContent) {
        this.tweetContent = tweetContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    @Nullable
    public String getTweetImage() {
        return tweetImage;
    }

    public void setTweetImage(@Nullable String tweetImage) {
        this.tweetImage = tweetImage;
    }
}
