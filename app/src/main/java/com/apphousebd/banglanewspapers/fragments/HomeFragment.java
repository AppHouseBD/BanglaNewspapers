package com.apphousebd.banglanewspapers.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apphousebd.banglanewspapers.R;
import com.apphousebd.banglanewspapers.adapter.HomePageAdapter;
import com.apphousebd.banglanewspapers.utilities.DisplayMatrix;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.apphousebd.banglanewspapers.adapter.HomePageAdapter.AD_INDEX_DURATION;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private List<Object> recyclerItemList;

    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        ///setting the list
        recyclerItemList = new ArrayList<>();
        addTitles();
        addAds();

        HomePageAdapter adapter =
                new HomePageAdapter(getContext(), recyclerItemList);

        recyclerView.setAdapter(adapter);

        return view;
    }

    private void addAds() {
        for (int i = AD_INDEX_DURATION; i < recyclerItemList.size(); i += AD_INDEX_DURATION) {
            NativeExpressAdView adView = new NativeExpressAdView(getContext());
            adView.setAdUnitId(getString(R.string.home_recycler_ad_id_small));
            recyclerItemList.add(i, adView);
        }

        ///setting the ad size dynamically with the recyclerview size
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float density = getContext().getResources().getDisplayMetrics().density;

                AdSize size =
                        new AdSize(((int) (recyclerView.getWidth() / density)) - DisplayMatrix.dpToPx(getContext(), 8),
                                80);
//                Toast.makeText(getContext(), "width: " + (int) (recyclerView.getWidth() / density) + " height: " + DisplayMatrix.dpToPx(getContext(), 80), Toast.LENGTH_SHORT).show();
// 5 ,  5 + 5 = 10
                for (int i = AD_INDEX_DURATION; i < recyclerItemList.size(); i += AD_INDEX_DURATION) {
                    NativeExpressAdView adView = (NativeExpressAdView) recyclerItemList.get(i);
                    adView.setAdSize(size);
                    adView.loadAd(new AdRequest.Builder()
                            .build());
                }
            }
        });
    }

    private void addTitles() {
        for (String title : Arrays.asList(getResources().getStringArray(R.array.newspaper_names))) {
            recyclerItemList.add(title);
        }
    }
}
