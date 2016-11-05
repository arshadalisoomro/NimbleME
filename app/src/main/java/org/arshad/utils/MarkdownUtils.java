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
package org.arshad.utils;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.arshad.nimbleme.AboutActivity;
import org.arshad.nimbleme.R;
import org.lib.Check;
import org.lib.PerformEditable;
import org.lib.TabIconView;

/**
 * Created by Arshay on 9/30/2016.
 */

public class MarkdownUtils {
    /**
     * Init class using Singleton Pattern
     * @return MarkdownUtils with new Instance
     */
    public static MarkdownUtils newInstance(){
        return new MarkdownUtils();
    }


    public void initTab(TabIconView mTabIconView, View.OnClickListener onClickListener) {

        mTabIconView.addTab(R.drawable.ic_shortcut_format_list_bulleted, R.id.id_shortcut_list_bulleted, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_list_numbers, R.id.id_shortcut_format_numbers, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_insert_link, R.id.id_shortcut_insert_link, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_insert_photo, R.id.id_shortcut_insert_photo, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_console, R.id.id_shortcut_console, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_bold, R.id.id_shortcut_format_bold, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_italic, R.id.id_shortcut_format_italic, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_header_1, R.id.id_shortcut_format_header_1, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_header_2, R.id.id_shortcut_format_header_2, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_header_3, R.id.id_shortcut_format_header_3, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_quote, R.id.id_shortcut_format_quote, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_xml, R.id.id_shortcut_xml, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_minus, R.id.id_shortcut_minus, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_strikethrough, R.id.id_shortcut_format_strikethrough, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_grid, R.id.id_shortcut_grid, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_header_4, R.id.id_shortcut_format_header_4, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_header_5, R.id.id_shortcut_format_header_5, onClickListener);
        mTabIconView.addTab(R.drawable.ic_shortcut_format_header_6, R.id.id_shortcut_format_header_6, onClickListener);
    }

    public void about(Context mContext) {
        mContext.startActivity(new Intent(mContext, AboutActivity.class));
    }

    public void insertLink(Context mContext, final PerformEditable performEditable) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.view_nimble_input_link_view, null);

        final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.DialogTheme)
                .setTitle("Specify link")
                .setView(rootView)
                .show();

        final TextInputLayout titleHint = (TextInputLayout) rootView.findViewById(R.id.inputNameHint);
        final TextInputLayout linkHint = (TextInputLayout) rootView.findViewById(R.id.inputHint);
        final EditText title = (EditText) rootView.findViewById(R.id.name);
        final EditText link = (EditText) rootView.findViewById(R.id.text);


        rootView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleStr = title.getText().toString().trim();
                String linkStr = link.getText().toString().trim();

                if (Check.isEmpty(titleStr)) {
                    titleHint.setError("Must not be Empty");
                    return;
                }
                if (Check.isEmpty(linkStr)) {
                    linkHint.setError("Must not be Empty");
                    return;
                }

                if (titleHint.isErrorEnabled())
                    titleHint.setErrorEnabled(false);
                if (linkHint.isErrorEnabled())
                    linkHint.setErrorEnabled(false);

                performEditable.perform(R.id.id_shortcut_insert_link, titleStr, linkStr);
                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void insertTable(Context mContext, final PerformEditable performEditable) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.view_nimble_input_table_view, null);

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("Specify Rows and Columns")
                .setView(rootView)
                .show();

        final TextInputLayout rowNumberHint = (TextInputLayout) rootView.findViewById(R.id.rowNumberHint);
        final TextInputLayout columnNumberHint = (TextInputLayout) rootView.findViewById(R.id.columnNumberHint);
        final EditText rowNumber = (EditText) rootView.findViewById(R.id.rowNumber);
        final EditText columnNumber = (EditText) rootView.findViewById(R.id.columnNumber);


        rootView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rowNumberStr = rowNumber.getText().toString().trim();
                String columnNumberStr = columnNumber.getText().toString().trim();

                if (Check.isEmpty(rowNumberStr)) {
                    rowNumberHint.setError("Must not be Empty");
                    return;
                }
                if (Check.isEmpty(columnNumberStr)) {
                    columnNumberHint.setError("Must not be Empty");
                    return;
                }


                if (rowNumberHint.isErrorEnabled())
                    rowNumberHint.setErrorEnabled(false);
                if (columnNumberHint.isErrorEnabled())
                    columnNumberHint.setErrorEnabled(false);

                performEditable.perform(R.id.id_shortcut_grid, Integer.parseInt(rowNumberStr), Integer.parseInt(columnNumberStr));
                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
