package javier.com.hashtagsearch.presenter;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;

import java.util.List;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Javi on 4/30/17.
 */

public class SearchPresenter extends Observable<List<Tweet>> {

    private static final int RESULT_LIMIT = 10;

    public SearchPresenter(final String query) {
        super(new OnSubscribe<List<Tweet>>() {
            @Override
            public void call(final Subscriber<? super List<Tweet>> subscriber) {
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                Call<Search> searchCall = twitterApiClient.getSearchService().tweets(query, null
                        , null, null, SearchTimeline.ResultType.POPULAR.toString(),
                        RESULT_LIMIT, null, null, null, null);

                searchCall.enqueue(new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        try {
                            if (result.data != null && result.data.tweets.size() > 0) {
                                subscriber.onNext(result.data.tweets);
                            } else {
                                subscriber.onNext(null);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            subscriber.onNext(null);
                        }

                        subscriber.onCompleted();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        subscriber.onError(exception);
                    }
                });
            }
        });
    }

}
