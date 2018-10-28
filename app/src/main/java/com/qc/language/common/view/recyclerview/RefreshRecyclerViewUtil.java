package com.qc.language.common.view.recyclerview;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;

import com.qc.language.R;

/**
 * @author Administrator
 * @version Id: RefreshRecyclerViewUtil, v 0.1 2017/8/8 19:20 Administrator Exp $$
 */
public class RefreshRecyclerViewUtil {
    @TargetApi(23)
    public static void initRefreshViewColorSchemeColors(SwipeRefreshLayout refreshRecyclerView, Resources resources) {
        if (Build.VERSION.SDK_INT >= 23) {
            refreshRecyclerView.setColorSchemeColors(resources.getColor(R.color.colorPrimary, null),
                    resources.getColor(R.color.colorPrimary, null),
                    resources.getColor(R.color.colorPrimary, null));
        } else {
            refreshRecyclerView.setColorSchemeColors(resources.getColor(R.color.colorPrimary),
                    resources.getColor(R.color.colorPrimary),
                    resources.getColor(R.color.colorPrimary));
        }
    }
}
