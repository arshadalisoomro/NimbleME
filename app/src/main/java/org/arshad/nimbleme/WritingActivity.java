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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.arshad.utils.MarkdownUtils;
import org.lib.ExpandableLinearLayout;
import org.lib.MarkdownPreviewView;
import org.lib.PerformEditable;
import org.lib.TabIconView;

import java.io.File;

import ren.qinc.edit.PerformEdit;
/**
 * Created by Arshay on 9/30/2016.
 */
public class WritingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private static final int SYSTEM_GALLERY = 615;
    private EditText mMdEditText;
    private MarkdownPreviewView mMarkdownView;
    private ExpandableLinearLayout mdToolbar, mdEditor, mdPreview;
    private TabIconView mTabIconView;
    private PerformEdit mPerformEdit;
    private PerformEditable mPerformEditable;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));



        mdToolbar = (ExpandableLinearLayout) findViewById(R.id.action_other_operate);

        mdEditor = (ExpandableLinearLayout) findViewById(R.id.md_editor_container);
        mdEditor.setVisibility(View.VISIBLE);
        mdEditor.expand();

        mdPreview = (ExpandableLinearLayout)findViewById(R.id.md_preview_container);
        mdPreview.setVisibility(View.GONE);

        mMdEditText = (EditText) findViewById(R.id.post_description_editText);

        mMarkdownView = (MarkdownPreviewView) findViewById(R.id.markdownView);

        mTabIconView = (TabIconView) findViewById(R.id.tabIconView);

        mPerformEdit = new PerformEdit(mMdEditText);
        mPerformEditable = new PerformEditable(mMdEditText);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MarkdownUtils.newInstance().initTab(mTabIconView, this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private MenuItem mUndo, mRedo, mActionOtherOperate, mActionPreview;

    private void initOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor_act, menu);
        mActionOtherOperate = menu.findItem(R.id.action_other_operate);
        mUndo = menu.findItem(R.id.action_undo);
        mRedo = menu.findItem(R.id.action_redo);
        mActionPreview = menu.findItem(R.id.action_preview);
        if (mdToolbar.isExpanded())
            mActionOtherOperate.setIcon(R.drawable.ic_arrow_up);
        else
            mActionOtherOperate.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_other_operate) {
            if (!mdToolbar.isExpanded())
                mActionOtherOperate.setIcon(R.drawable.ic_arrow_up);
            else
                mActionOtherOperate.setIcon(R.drawable.ic_add_white_24dp);
            mdToolbar.toggle();
            return true;
        } else if(item.getItemId() == R.id.action_preview) {
            if (!mdEditor.isExpanded()){
                mdEditor.setVisibility(View.VISIBLE);
                mdPreview.setVisibility(View.GONE);
                mActionPreview.setIcon(R.drawable.ic_action_preview);
                mdPreview.collapse();
                mActionOtherOperate.setEnabled(true);
                mUndo.setEnabled(true);
                mRedo.setEnabled(true);
                mdToolbar.collapse();
            } else {
                mdEditor.setVisibility(View.GONE);
                mdPreview.setVisibility(View.VISIBLE);
                mActionPreview.setIcon(R.drawable.ic_actionpreview_off);
                mdPreview.expand();
                mdToolbar.collapse();
                mActionOtherOperate.setIcon(R.drawable.ic_add_white_24dp);
                mActionOtherOperate.setEnabled(false);
                mUndo.setEnabled(false);
                mRedo.setEnabled(false);
                mMarkdownView.parseMarkdown(mMdEditText.getText().toString(), true);
            }

            mdEditor.toggle();
            return true;
        } else if(item.getItemId() ==  R.id.action_undo ) {
            mPerformEdit.undo();
            return true;
        } else if(item.getItemId() == R.id.action_redo) {
            mPerformEdit.redo();
            return true;
        }

        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            MarkdownUtils.newInstance().about(WritingActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == SYSTEM_GALLERY) {
            Uri uri = data.getData();
            String[] pojo = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.managedQuery(uri, pojo, null, null, null);
            if (cursor != null) {
                int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(colunm_index);
                Uri.fromFile(new File(path));
                getPerformEditable().perform(R.id.id_shortcut_insert_photo, Uri.fromFile(new File(path)));
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (R.id.id_shortcut_insert_photo == view.getId()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);// Pick an item from the images
            intent.setType("image/*");
            startActivityForResult(intent, SYSTEM_GALLERY);
            return;
        } else if (R.id.id_shortcut_insert_link == view.getId()) {
            MarkdownUtils.newInstance().insertLink(WritingActivity.this, getPerformEditable());
            return;
        } else if (R.id.id_shortcut_grid == view.getId()) {
            MarkdownUtils.newInstance().insertTable(WritingActivity.this, getPerformEditable());
            return;
        }else {
            getPerformEditable().onClick(view);
            return;
        }
    }

    private PerformEditable getPerformEditable() {
        return mPerformEditable;
    }
}
