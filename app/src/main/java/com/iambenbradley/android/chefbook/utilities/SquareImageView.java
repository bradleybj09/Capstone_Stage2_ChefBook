package com.iambenbradley.android.chefbook.utilities;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ben on 12/18/2016.
 */

public class SquareImageView extends ImageView {
    public SquareImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width,width);
    }
}
