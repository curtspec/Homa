package com.curtspec2019.homa.adapter;

import android.content.Context;
import android.widget.TextView;

import com.curtspec2019.homa.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class ChartMarker extends MarkerView {

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    TextView tv;

    public ChartMarker(Context context, int layoutResource) {
        super(context, layoutResource);
        tv = findViewById(R.id.marker_text);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tv.setText(Utils.formatNumber(e.getY(), 0, true));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        int extraHeight = (int) (getHeight() * 0.3);
        return new MPPointF(-(getWidth() / 2), -getHeight() - extraHeight);
    }
}
