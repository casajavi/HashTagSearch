package javier.com.hashtagsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import javier.com.hashtagsearch.fragments.SearchResultsFragment;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "pp2tpNNPnWwh3Rzn5vyl8kIYG";
    private static final String TWITTER_SECRET = "4Bdc1O7lggk10SYtP5B2YxTQOVjo4WOHOK85UV6G7WNRBQGuLF";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    // Show Current Search
                    return true;
                case R.id.navigation_history:
                    // Show Search History
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new SearchResultsFragment(),SearchResultsFragment.class.getSimpleName()).commit();

        BottomNavigationView navigation = ButterKnife.findById(this, R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:

                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
