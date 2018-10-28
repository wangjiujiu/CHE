package com.qc.language.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qc.language.R;
import com.qc.language.common.fragment.CommonFragment;

/**
 * Created by beckett on 2018/9/19.
 */
public class HomeFragment extends CommonFragment {

    private LinearLayout classRl;
    private LinearLayout qrRl;
    private LinearLayout contactRl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_frag_main, container, false);

        classRl = (LinearLayout) root.findViewById(R.id.home_class);
        qrRl = (LinearLayout) root.findViewById(R.id.home_scan);
        contactRl = (LinearLayout) root.findViewById(R.id.home_contact_us);



        return root;
    }
}
