package javier.com.hashtagsearch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import javier.com.hashtagsearch.MainActivity;
import javier.com.hashtagsearch.R;
import javier.com.hashtagsearch.models.Search;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    @BindView(R.id.listView)
    ListView listView;

    private List<Search> searchList;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ButterKnife.bind(this, view);

        searchList = null;

        searchList = ((MainActivity) getActivity()).retrieveSearches();

        final ArrayList<String> queries = new ArrayList<>();

        for (Search search : searchList) {
            queries.add(0, search.getQuery().toUpperCase());
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext()
                , R.layout.item_history
                , android.R.id.text1, queries);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                for (Search search : searchList) {
                    if (search.getQuery().equalsIgnoreCase(queries.get(position))) {
                        ((MainActivity) getActivity()).searchResultListener.onSearchCompleted(search);
                        ((MainActivity) getActivity()).setupSearchUI(search.getQuery());
                    }
                }
            }
        });

        return view;
    }

    public boolean isFragmentVisible() {
        return HistoryFragment.this.isVisible();
    }
}
