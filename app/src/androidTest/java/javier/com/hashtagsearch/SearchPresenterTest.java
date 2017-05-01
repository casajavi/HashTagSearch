package javier.com.hashtagsearch;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.Tweet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import javier.com.hashtagsearch.presenter.SearchPresenter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class SearchPresenterTest {

    private static final int TIMEOUT = 30000;
    private static final String QUERY = "Android";
    private static final String TWITTER_KEY = "pp2tpNNPnWwh3Rzn5vyl8kIYG";
    private static final String TWITTER_SECRET = "4Bdc1O7lggk10SYtP5B2YxTQOVjo4WOHOK85UV6G7WNRBQGuLF";

    @BeforeClass
    public static void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(appContext, new Twitter(authConfig));
    }


    @Test(timeout = TIMEOUT)
    public void testSucces() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        SearchPresenter searchPresenter = new SearchPresenter(QUERY);

        searchPresenter.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Tweet>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<Tweet> tweets) {
                assertNotNull(tweets);
                signal.countDown();
            }
        });

        // Wait for all callbacks
        signal.await(TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Test(timeout = TIMEOUT)
    public void testFailure() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        SearchPresenter searchPresenter = new SearchPresenter("");

        searchPresenter.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Tweet>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                assertNotNull(e);
                signal.countDown();
            }

            @Override
            public void onNext(List<Tweet> tweets) {
                signal.countDown();
            }
        });

        // Wait for all callbacks
        signal.await(TIMEOUT, TimeUnit.MILLISECONDS);
    }
}
