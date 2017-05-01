package javier.com.hashtagsearch.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import javier.com.hashtagsearch.MainActivity;
import javier.com.hashtagsearch.R;
import javier.com.hashtagsearch.adapters.TweetRecyclerViewAdapter;
import javier.com.hashtagsearch.models.Search;
import javier.com.hashtagsearch.models.SearchTweet;

public class SearchResultsFragment extends Fragment implements MainActivity.SearchResultListener , TweetRecyclerViewAdapter.OnItemListener{


    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.layout_new_search)
    RelativeLayout relativeLayoutNewSearch;

    private TweetRecyclerViewAdapter adapter;
    private ImagePopup imagePopup;

    public SearchResultsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        ButterKnife.bind(this, view);
        // Set the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TweetRecyclerViewAdapter(null, this, getContext());
        recyclerView.setAdapter(adapter);

        ((MainActivity) getActivity()).setSearchResultListener(this);

        imagePopup = new ImagePopup(getActivity());
        imagePopup.setBackgroundColor(Color.BLACK);
        imagePopup.setWindowWidth(640);
        imagePopup.setWindowHeight(640);
        imagePopup.setHideCloseIcon(true);
        imagePopup.setImageOnClickClose(true);
        return view;
    }

    @Override
    public void onSearchCompleted(Search results) {
        relativeLayoutNewSearch.setVisibility(View.GONE);
        adapter.updateItems(results.getSearchTweets());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemImageInteraction(Drawable drawable) {
        imagePopup.initiatePopup(drawable);
    }

}
