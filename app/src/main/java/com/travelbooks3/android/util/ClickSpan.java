package com.travelbooks3.android.util;

import android.content.Context;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by system777 on 2017-08-30.
 */

public class ClickSpan extends ClickableSpan {

    public interface ClickEventListener{
        void onClickEvent(String data);


    }


    private ClickSpan.ClickEventListener mClickEventListener = null;
    private Context context;
    private TextPaint textPaint;

    public ClickSpan(Context ctx)
    {
        super();
        context = ctx;

    }

    public void setOnClickEventListener(ClickSpan.ClickEventListener listener){
        mClickEventListener = listener;

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        textPaint = ds;
        ds.setColor(ds.linkColor);
        ds.setARGB(255, 30, 144, 255);


    }

    @Override
    public void onClick(View widget) {
        TextView tv = (TextView) widget;
        Spanned s = (Spanned) tv.getText();
        int start = s.getSpanStart(this);
        int end = s.getSpanEnd(this);
        String theWord = s.subSequence(start , end).toString();
        mClickEventListener.onClickEvent(theWord);
        //Toast.makeText(context, String.format("Tags for tags: %s", theWord), Toast.LENGTH_SHORT).show();

    }
}
