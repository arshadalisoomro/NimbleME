/*
 * Copyright 2016. Arshad Ali Soomro<arshadalisoomro7@gmail.com>
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
package org.arshad.nimbleme;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.arshad.utils.AppPreferences;
import org.arshad.utils.Utils;
import org.lib.MarkdownPreviewView;

import static org.arshad.nimbleme.R.id.about_googleplus;
/**
 * Created by Arshay on 9/30/2016.
 */
public class AboutActivity extends AppCompatActivity{

    // Load Settings
    private AppPreferences appPreferences;
    private MarkdownPreviewView mMarkdownView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        appPreferences = new AppPreferences(AboutActivity.this);
        setInitialConfiguration();
        setScreenElements();
    }

    private void setInitialConfiguration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.action_about);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Utils.darker(appPreferences.getPrimaryColorPref(), 0.8));
            toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            if (!appPreferences.getNavigationBlackPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }

    }

    private void setScreenElements() {

        TextView header = (TextView) findViewById(R.id.header);
        final TextView appNameVersion = (TextView) findViewById(R.id.app_name);
        final CardView about_1 = (CardView) findViewById(R.id.about_1);
        CardView aboutGooglePluse = (CardView) findViewById(about_googleplus);

        header.setBackgroundColor(appPreferences.getPrimaryColorPref());
        appNameVersion.setText(getResources().getString(R.string.app_name) + " "
                + Utils.getAppVersionName(getApplicationContext()) + " \""
                + getResources().getString(R.string.app_codename_beta) + "\"");
        about_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.goToGooglePlus(AboutActivity.this, "+ArshadAliSoomro");
            }
        });

        aboutGooglePluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Not yet Implemented!", Snackbar.LENGTH_LONG).show();
            }
        });

        mMarkdownView = (MarkdownPreviewView) findViewById(R.id.about_markdownView);
        mMarkdownView.setOnLoadingFinishListener(new MarkdownPreviewView.OnLoadingFinishListener() {
            @Override
            public void onLoadingFinish() {
                mMarkdownView.parseMarkdown("#### About " + appNameVersion.getText() + " \n " +
                        " ------------------------------------------------- " + " \n " +
                        " The word __Nimble__ means" +
                        " _Quick and light in action_," +
                        " so this ___Android App___ is _fairly fast_" +
                        " and _light Markdown Editor_ with some" +
                        " good LAF (Look and Feel) that's why this" +
                        " little Effort is named As " +
                        getResources().getString(R.string.app_name), true);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

}
