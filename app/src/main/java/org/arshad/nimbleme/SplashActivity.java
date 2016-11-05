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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
/**
 * Created by Arshay on 9/30/2016.
 */
public class SplashActivity extends AppCompatActivity {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            Window window = this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));

            initAndFinish();
        }

        public void initAndFinish(){
            findViewById(R.id.iv).animate().alpha(1).scaleY(1.5f).scaleX(1.5f).setDuration(2000L).setInterpolator(new AccelerateDecelerateInterpolator());
            findViewById(R.id.tv).animate().alpha(1).scaleY(1.2f).scaleX(1.2f).setDuration(2000L).setInterpolator(new AccelerateDecelerateInterpolator());
            findViewById(R.id.tv2).animate().alpha(1).scaleY(1.1f).scaleX(1.1f).setDuration(2000L).setInterpolator(new AccelerateDecelerateInterpolator());
            findViewById(R.id.tv3).animate().alpha(1).scaleY(1.1f).scaleX(1.1f).setDuration(2000L).setInterpolator(new AccelerateDecelerateInterpolator());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, WritingActivity.class));
                    finish();
                }
            },3210L);

        }

}
