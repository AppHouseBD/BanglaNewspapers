package com.apphousebd.banglanewspapers.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.apphousebd.banglanewspapers.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Asif Imtiaz Shaafi on 02, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class HomePageAdapter extends RecyclerView.Adapter {

    public static final int AD_INDEX_DURATION = 5;
    ///ids for checking weather to load ad or title name
    private static final int TITLE_VIEW_TYPE = 1;
    private static final int AD_VIEW_TYPE = 2;
    private Context mContext;
    private List<Object> mRecyclerViewItems;
    private List<String> mNewspaperNames;

    ///interface
    private HomeItemListener mHomeItemListener;


    public HomePageAdapter(Context context, List<Object> mRecyclerViewItems) {
        this.mRecyclerViewItems = mRecyclerViewItems;
        mNewspaperNames = Arrays.asList(context.getResources().getStringArray(R.array.newspaper_names));
        mContext = context;

        if (!(context instanceof HomeItemListener)) throw new AssertionError();
        mHomeItemListener = (HomeItemListener) context;
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position % AD_INDEX_DURATION == 0 && position != 0) ? AD_VIEW_TYPE : TITLE_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case AD_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_ad_item_view, parent, false);

                return new NativeAdExpressHolder(view);

            case TITLE_VIEW_TYPE:
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_item_view, parent, false);

                return new HomeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType) {
            case AD_VIEW_TYPE:

                NativeAdExpressHolder adExpressHolder = (NativeAdExpressHolder) holder;
                NativeExpressAdView adView =
                        (NativeExpressAdView) mRecyclerViewItems.get(position);

                ViewGroup adCardView = (ViewGroup) adExpressHolder.itemView;
                adCardView.removeAllViews();

                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                adCardView.addView(adView);

                break;
            case TITLE_VIEW_TYPE:
            default:
                HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
                homeViewHolder.bindViews((String) mRecyclerViewItems.get(position));
        }

    }


    public interface HomeItemListener {

        void loadPage(String newspaperName);
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private TextView mTextView;

        private CardView container;
//        private RelativeLayout container;

        ///text drawable for the thumbnail image
        private TextDrawable mTextDrawable;
        ///color generator for creating random color for thumbnail background
        private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

        private HomeViewHolder(View itemView) {
            super(itemView);

//            container = (RelativeLayout) itemView.findViewById(R.id.recycle_container);
            container = (CardView) itemView.findViewById(R.id.recycle_container);
            container.setOnClickListener(this);

            mImageView = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            mTextView = (TextView) itemView.findViewById(R.id.recycle_title_text_view);

        }

        void bindViews(String title) {
            mTextView.setText(title);
//
//            String thumbnailText = "";
//
//            if (!TextUtils.isEmpty(title)) {
//                int index = title.indexOf("(");
//                thumbnailText = title.substring(index + 1, index + 2);
//            }
//
//            ///******creating thumbnail******///
//
//            ///generating random color
//            int color = mColorGenerator.getRandomColor();
//
//            //image
//            mTextDrawable = TextDrawable.builder()
//                    .buildRound(thumbnailText, color);
//
//            mImageView.setImageDrawable(mTextDrawable);

            int imageIndex = mNewspaperNames.indexOf(title) + 1;
            Glide.with(mContext).load(mContext.getResources().getIdentifier("_" + imageIndex,"drawable",
                    mContext.getPackageName()))
                    .dontAnimate()
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {

            int index = getAdapterPosition();
            if (index % AD_INDEX_DURATION != 0 || index == 0) {
                String name = (String) mRecyclerViewItems.get(index);
                Log.d("index", "onClick: " + name);
                mHomeItemListener.loadPage(name);
            }

//            mHomeItemListener.loadPage(getAdapterPosition() + 1);
        }
    }

    private class NativeAdExpressHolder extends RecyclerView.ViewHolder {

        public NativeAdExpressHolder(View itemView) {
            super(itemView);
        }
    }
}
