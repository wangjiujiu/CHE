package com.qc.language.common.view.textview.imageload;

import android.content.Context;

import com.qc.language.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class ImageLoad {

    public static void loadPlaceholder(Context context, String url, Target target) {

        Picasso picasso = new Picasso.Builder(context).loggingEnabled(true).build();
        picasso.load(url)
                .placeholder(R.mipmap.moren)
                .error(R.mipmap.moren)
                .transform(new ImageTransform())
                .into(target);
    }

}
