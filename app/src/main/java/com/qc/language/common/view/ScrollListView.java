package com.qc.language.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可以放在scrollview里的listview
 * @author Beckett_W
 * @version Id: ScrollListView, v 0.1 2017/8/8 18:11 Beckett_W Exp $$
 */
public class ScrollListView extends ListView {

    public ScrollListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public ScrollListView(Context context){
        super(context);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

