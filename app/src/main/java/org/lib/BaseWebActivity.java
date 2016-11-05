/*
 * Copyright 2016. Arshad Ali <arshadalisoomro7@gmail.com.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.arshad.nimbleme.R;

/**
 * Created by Arshay on 9/27/2016.
 */

public class BaseWebActivity extends AppCompatActivity {

    public static final String CONTENT_KEY = "extra_content";
    public static final String URL_KEY = "extra_url";
    public static final String TITLE_KEY = "extra_title";


    protected ObservableWebView mWebView;


    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }

    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.pauseTimers();
            mWebView.stopLoading();
            mWebView.setFocusable(true);
            mWebView.removeAllViews();
            mWebView.clearHistory();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    /**
     * New intent intent.
     *
     * @param context      the context
     * @param url          url
     * @param content      url
     * @param defaultTitle the default title
     * @return the intent
     */
    public static void load(Context context, String url, String content, String defaultTitle) {
        Intent intent = new Intent(context, BaseWebActivity.class);
        intent.putExtra(URL_KEY, url);
        intent.putExtra(TITLE_KEY, defaultTitle);
        intent.putExtra(CONTENT_KEY, content);
        context.startActivity(intent);
    }

    /**
     * Load url.
     *
     * @param //context      the context
     * @param //url          the url
     * @param //defaultTitle the default title
     */
    public static void loadUrl(Context context, @NonNull String url, @Nullable String defaultTitle) {
        load(context, url, "", defaultTitle);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nimble_web);

        mWebView = (ObservableWebView) findViewById(R.id.webView);

        enableJavascript();
        enableCaching();
        enableClient();
        enableAdjust();
        zoomedOut();

        mWebView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy, int x, int y) {
                // Empty Placeholder
            }
        });

    }

    private void zoomedOut() {
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
    }

    private void enableClient() {
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new WebClient());
    }

    private void enableAdjust() {
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
    }

    /**
     * Enable caching.
     */
    private void enableCaching() {
        mWebView.getSettings().setAppCachePath(getFilesDir() + getPackageName() + "/cache");
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setAllowFileAccess(true);
    }

    /**
     * Enable javascript.
     */
    private void enableJavascript() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    protected void refresh() {
        mWebView.reload();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        this.switchScreenConfiguration(null);
                    } else {
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                        } else {
                            finish();
                        }
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void switchScreenConfiguration(MenuItem item) {
        Configuration configuration = this.getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        }
    }

    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // Placeholder
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }


        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Some title").setMessage(message).setPositiveButton("OK", null);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();
            return true;
        }

    }


    private class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Snackbar.make(view, "Something went wrong ", Snackbar.LENGTH_LONG).show();
        }


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Snackbar.make(view, "Something went wrong errorCode:" + errorCode, Snackbar.LENGTH_LONG).show();
        }
    }


}
