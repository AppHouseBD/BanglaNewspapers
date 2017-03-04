package com.apphousebd.banglanewspapers.mainUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.apphousebd.banglanewspapers.R;
import com.apphousebd.banglanewspapers.adapter.HomePageAdapter;
import com.apphousebd.banglanewspapers.fragments.HomeFragment;
import com.apphousebd.banglanewspapers.fragments.MainActivityFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainActivityFragment.SetWebViewListener,
        HomePageAdapter.HomeItemListener { ///implementing interface to get the webview reference

    public static final String WEB_URL = "web_url";

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NativeExpressAdView adView;

    private WebView mWebView;

    Toolbar toolbar;

    //    arrays for newspaper names and links
    private List<String> newspaperNames;
    private List<String> newspapersLinkHosts;
    private List<String> newspaperLinks;


    public static int titleIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        newspaperNames = new ArrayList<>();
        newspaperNames.add(0, "Home");

        for (String title : Arrays.asList(getResources().getStringArray(R.array.newspaper_names))) {
            newspaperNames.add(title);
        }

        newspaperLinks = Arrays.asList(getResources().getStringArray(R.array.newspaper_links));
        newspapersLinkHosts = Arrays.asList(getResources().getStringArray(R.array.newspaper_link_host));

        setNavigationView();

        adView = (NativeExpressAdView) findViewById(R.id.ad_view);
        adView.loadAd(new AdRequest.Builder()
                .build());

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFragment();

    }

    private void setNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                unCheckNavigationMenuItem(titleIndex);

                switch (item.getItemId()) {
                    case R.id.action_home:
                        titleIndex = 0;
                        break;
                    case R.id.action_prothom_alo:
                        titleIndex = 1;
                        break;
                    case R.id.action_ittefaq:
                        titleIndex = 2;
                        break;
                    case R.id.action_amader_somoy:
                        titleIndex = 3;
                        break;
                    case R.id.action_noya_digonto:
                        titleIndex = 4;
                        break;
                    case R.id.action_kaler_kontho:
                        titleIndex = 5;
                        break;
                    case R.id.action_jugantor:
                        titleIndex = 6;
                        break;
                    case R.id.action_somokal:
                        titleIndex = 7;
                        break;
                    case R.id.action_amar_desh:
                        titleIndex = 8;
                        break;
                    case R.id.action_jayjaydin:
                        titleIndex = 9;
                        break;
                    case R.id.action_bd_protidin:
                        titleIndex = 10;
                        break;
                    default:
                        titleIndex = 0;
                }

                loadFragment();
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
    }

    private void unCheckNavigationMenuItem(int index) {
        mNavigationView.getMenu().getItem(index).setChecked(false);
    }

    private void loadFragment() {

        mNavigationView.getMenu().getItem(titleIndex).setChecked(true);
        final FragmentManager manager = getSupportFragmentManager();

        if (titleIndex != 0) {
            adView.setVisibility(View.VISIBLE);
            toolbar.setTitle(newspaperNames.get(titleIndex));
        } else {
            adView.setVisibility(View.GONE);
            toolbar.setTitle(getResources().getString(R.string.app_name));
        }

        if (manager.findFragmentByTag(newspaperNames.get(titleIndex)) != null) {
            return;
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                transaction.replace(R.id.fragment,
                        getFragment(titleIndex), newspaperNames.get(titleIndex));

                transaction.commitAllowingStateLoss();

                //refresh toolbar
                invalidateOptionsMenu();
            }
        });
    }

    private Fragment getFragment(int id) {
        Fragment fragment;

        if (id != 0) {
            fragment = MainActivityFragment.newInstance(newspaperLinks.get(titleIndex - 1), newspapersLinkHosts.get(titleIndex - 1));
        } else {
            fragment = new HomeFragment();
        }

        return fragment;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (isDrawerOpen()) {
            mDrawerLayout.closeDrawers();
        }
//        Toast.makeText(this, "before: " + webView.getUrl(), Toast.LENGTH_SHORT).show();
        /***********************************************************************************
         checking if the web view can go back,that is if the user has gone to
         new page in the web view from the main page, if true, then send back the user to
         the previous page otherwise do regular back button work
         ************************************************************************************/
        else if (mWebView != null && mWebView.canGoBack() && titleIndex != 0) {
            mWebView.goBack();
        } else if (titleIndex != 0) {
            unCheckNavigationMenuItem(titleIndex);
            titleIndex = 0;
            loadFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mWebView != null && titleIndex != 0) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mWebView != null && titleIndex != 0) {
            if (!mWebView.canGoBack()) {
                menu.getItem(0).setEnabled(false);
                menu.getItem(0).getIcon().setAlpha(130);
            } else {
                menu.getItem(0).setEnabled(true);
                menu.getItem(0).getIcon().setAlpha(255);
            }

            if (!mWebView.canGoForward()) {
                menu.getItem(1).setEnabled(false);
                menu.getItem(1).getIcon().setAlpha(130);
            } else {
                menu.getItem(1).setEnabled(true);
                menu.getItem(1).getIcon().setAlpha(255);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.action_back) {
            back();
        }

        if (item.getItemId() == R.id.action_forward) {
            forward();
        }
        if (item.getItemId() == R.id.action_refresh) {
            mWebView.reload();
        }
        if (item.getItemId() == R.id.action_about_us) {
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void back() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    private void forward() {
        if (mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }

    //drawer checking
    protected boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    ///attaching the webview of the fragment with the main activity's webview reference
    ///so that when we work with this webview,the effects apply on the fragment's webview
    @Override
    public void setWebView(WebView webView) {
        this.mWebView = webView;
    }

    @Override
    public void invalidateMenu() {
        invalidateOptionsMenu();
        if (mWebView != null)
            mWebView.scrollTo(0, 0);
    }

    @Override
    public void loadPage(String newspaperName) {
        Log.d("main index", "loadPage: " + newspaperName);
        int id = newspaperNames.indexOf(newspaperName);
        Log.d("main index", "loadPage: index: " + id);
        unCheckNavigationMenuItem(titleIndex);
        titleIndex = id;
        loadFragment();
    }
}
