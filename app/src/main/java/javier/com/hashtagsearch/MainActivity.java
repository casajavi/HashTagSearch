package javier.com.hashtagsearch;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import javier.com.hashtagsearch.fragments.HistoryFragment;
import javier.com.hashtagsearch.fragments.SearchResultsFragment;
import javier.com.hashtagsearch.models.Search;
import javier.com.hashtagsearch.models.SearchTweet;
import javier.com.hashtagsearch.presenter.SearchPresenter;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "pp2tpNNPnWwh3Rzn5vyl8kIYG";
    private static final String TWITTER_SECRET = "4Bdc1O7lggk10SYtP5B2YxTQOVjo4WOHOK85UV6G7WNRBQGuLF";

    private static final long UPDATE_INTERVAL = 60;


    @BindView(R.id.progress_bar_loading)
    ProgressBar progressBarLoading;
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    public SearchResultListener searchResultListener;
    private CompositeSubscription compositeSubscription;
    private String currentQuery = "";
    private Search currentSearch;
    private SearchView searchView;

    private SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
    private HistoryFragment historyFragment = new HistoryFragment();

    private Realm realm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    // Show Current Search
                    if (historyFragment.isFragmentVisible()) {
                        getSupportFragmentManager().popBackStack();
                    }
                    return true;
                case R.id.navigation_history:
                    // Show Search History
                    if (!historyFragment.isFragmentVisible()) {
                        if (updateSubscription != null) {
                            updateSubscription.unsubscribe();
                        }
                        compositeSubscription.remove(updateSubscription);
                        getSupportFragmentManager().beginTransaction().add(R.id.content, historyFragment
                                , HistoryFragment.class.getSimpleName())
                                .addToBackStack(HistoryFragment.class.getSimpleName()).commit();
                    }
                    return true;
            }
            return false;
        }

    };

    //region RX subscriptions and observers
    private Subscription updateSubscription;
    private Subscription searchSubscription;
    Observer<List<Tweet>> searchObserver = new Observer<List<Tweet>>() {
        @Override
        public void onCompleted() {
            // Removes subscription
            compositeSubscription.remove(searchSubscription);
        }

        @Override
        public void onError(Throwable e) {
            progressBarLoading.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext()
                    , R.string.message_search_error, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(List<Tweet> tweets) {
            // Writes results to realm
            if (tweets != null) {
                writeToRealm(tweets);
            } else {
                progressBarLoading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext()
                        , R.string.message_no_results, Toast.LENGTH_LONG).show();
            }
        }
    };
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().add(R.id.content, searchResultsFragment
                , SearchResultsFragment.class.getSimpleName())
                .addToBackStack(SearchResultsFragment.class.getSimpleName()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        compositeSubscription = new CompositeSubscription();
    }

    /**
     * Handles the search intent from the Search View
     *
     * @param intent users search intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    public void setSearchResultListener(SearchResultListener searchResultListener) {
        this.searchResultListener = searchResultListener;
    }

    /**
     * Retrieves all stored searches
     *
     * @return list of searches
     */
    public RealmResults<Search> retrieveSearches() {
        RealmQuery<Search> searchQuery = realm.where(Search.class);

        return searchQuery.findAll();
    }

    /**
     * Sets up the UI to match the current search
     *
     * @param query query string for search bar
     */
    public void setupSearchUI(String query) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        searchView.setQuery(query, false);
    }

    /**
     * Retrieves the search string from intent and initializes the search
     *
     * @param intent The users search intent
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            progressBarLoading.setVisibility(View.VISIBLE);
            currentQuery = intent.getStringExtra(SearchManager.QUERY);
            currentQuery.toUpperCase();
            if (!currentQuery.isEmpty()) {
                initiateSearch();
            } else {
                Toast.makeText(this, R.string.message_empty_query, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initiateSearch() {
        SearchPresenter searchPresenter = new SearchPresenter(currentQuery);

        searchSubscription = searchPresenter.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(searchObserver);

        compositeSubscription.add(searchSubscription);
    }


    private void initiateSearchUpdate() {
        Observable<Long> updateObservable = Observable.timer(UPDATE_INTERVAL, TimeUnit.SECONDS);
        updateSubscription = updateObservable
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        initiateSearch();
                    }
                });
        compositeSubscription.add(updateSubscription);
    }


    /**
     * Handles the logic to persist the data
     *
     * @param searchResults the results to be saved
     */
    private void writeToRealm(final List<Tweet> searchResults) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        final RealmList<SearchTweet> searchTweets = new RealmList<>();
        currentSearch = null;
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (final Tweet tweet : searchResults) {
                        SearchTweet searchTweet = realm.createObject(SearchTweet.class);
                        searchTweet.initialize(tweet.id, tweet.text, tweet.user.screenName
                                , tweet.user.name, tweet.user.profileImageUrl
                                , tweet.entities.media == null ? null : tweet.entities.media.get(0).mediaUrl);
                        searchTweets.add(searchTweet);
                    }
                }
            });

            currentSearch = new Search(currentQuery, searchTweets);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(currentSearch);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressBarLoading.setVisibility(View.GONE);
        searchResultListener.onSearchCompleted(currentSearch);
        initiateSearchUpdate();
    }

    public interface SearchResultListener {
        void onSearchCompleted(Search results);
    }

}
