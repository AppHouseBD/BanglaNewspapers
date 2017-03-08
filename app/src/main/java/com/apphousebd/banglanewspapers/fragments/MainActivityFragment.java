package com.apphousebd.banglanewspapers.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.apphousebd.banglanewspapers.R;
import com.apphousebd.banglanewspapers.utilities.NetworkUtility;

import static com.apphousebd.banglanewspapers.mainUI.MainActivity.WEB_URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ProgressBar progressBar;
    private WebView webView;
    private float m_downX;
    private SetWebViewListener mSetWebViewListener;

    private String baseUrl = "file:///android_asset/Error.html";
    private String host = baseUrl;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance(String url, String host) {

        Bundle args = new Bundle();
        args.putString(WEB_URL, url);
        args.putString("host", host);

        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ///attaching the interface's instance with the main activity,so that the main
        ///activity can work with current fragment
        if (!(context instanceof SetWebViewListener)) throw new AssertionError();
        mSetWebViewListener = (SetWebViewListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            baseUrl = bundle.getString(WEB_URL);
            host = bundle.getString("host");
//
//            Toast.makeText(getContext(),
//                    bundle.getString(WEB_URL), Toast.LENGTH_SHORT).show();
        }

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ///changing the progressbar color
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


        // Load an ad into the AdMob banner view.

        webView = (WebView) view.findViewById(R.id.webview);

        webView.clearCache(true);
        webView.clearHistory();
        loadLink();
        mSetWebViewListener.setWebView(webView);

        return view;
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void loadLink() {

        String url;
        if (NetworkUtility.hasNetworkConnection(getContext())) {

            url = baseUrl;
        } else {
            url = "file:///android_asset/Error.html";
        }

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new CustomWebClient());
        webView.setWebChromeClient(new MyWebChromeClient(getContext()));

        ///these two setting is about fitting the webpage into the webview
//        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });

        webView.loadUrl(url);
    }


    ///creating an interface to pass the webview of the current fragment to main activity
    public interface SetWebViewListener {
        void setWebView(WebView webView);

        void invalidateMenu();
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }
    }

    ///custom webclient
    private class CustomWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            Log.i("external url", "shouldOverrideUrlLoading: clicked: " + url);
//            Toast.makeText(getContext(), Uri.parse(url).getHost() + "  " + host, Toast.LENGTH_SHORT).show();
            /***********************************************************************************
             this method determines if we should go from the web view,that is from
             the app to a browser or load the url in the webview
             if the method returns true, then app will look for a browser to open the url,
             otherwise it will load the url in the webview
             ************************************************************************************/
            if (Uri.parse(url).getHost().endsWith(host)) {

                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);

            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

//            webView.loadUrl("file:///android_asset/Error.html");

            progressBar.setVisibility(View.GONE);
//            getActivity().invalidateOptionsMenu();
            mSetWebViewListener.invalidateMenu();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
//            getActivity().invalidateOptionsMenu();
            webView.scrollTo(0, 0);
            mSetWebViewListener.invalidateMenu();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
//            getActivity().invalidateOptionsMenu();
            webView.scrollTo(0, 0);
            mSetWebViewListener.invalidateMenu();
        }
    }


}
