package javier.com.hashtagsearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import javier.com.hashtagsearch.MainActivity;
import javier.com.hashtagsearch.R;
import javier.com.hashtagsearch.adapters.TweetRecyclerViewAdapter;
import javier.com.hashtagsearch.models.Search;
import javier.com.hashtagsearch.models.SearchTweet;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SearchResultsFragment extends Fragment implements MainActivity.SearchResultListener {

    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.list)
    RecyclerView recyclerView;

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
        recyclerView.setAdapter(new TweetRecyclerViewAdapter(null, mListener, getContext()));

        ((MainActivity)getActivity()).setSearchResultListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSearchCompleted(Search results) {
        recyclerView.setAdapter(new TweetRecyclerViewAdapter(results.getSearchTweets(), mListener, getContext()));
    }

    public boolean isFragmentVisible() {
        return SearchResultsFragment.this.isVisible();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SearchTweet item);
    }
}
