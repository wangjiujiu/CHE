package com.qc.language.common.view.selectabletextview;

import java.util.Calendar;

public abstract class OnWordClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public void onClick(String word) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(word);
        }
    }

    protected abstract void onNoDoubleClick(String word);

}
