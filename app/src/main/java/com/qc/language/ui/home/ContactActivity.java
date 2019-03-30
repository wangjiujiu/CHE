package com.qc.language.ui.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.common.activity.CommonActivity;

/**
 * Created by beckett on 2018/10/29.
 */
public class ContactActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_contact_setting);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        toolbarTitleTextView.setText("联系方式");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setListener();

    }


    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactActivity.this.finish();
            }
        });



    }

}
