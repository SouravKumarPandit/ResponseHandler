package com.responsehandler.app;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemView
{
    private final Context context;

    public ItemView(Context  context) {
        this.context=context;
    }

    public LinearLayout getItemsView(int length) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(20,10,5,10);
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(3, 3, 3, 3);
        linearLayout.setLayoutParams(layoutParams);
        for (int i = 0; i < length; i++) {
            TextView textView = new TextView(context);
            linearLayout.addView(textView);
        }
        return linearLayout;
    }
}
