package com.stpass.clinical.model;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.stpass.clinical.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shujie
 * @mail: shuj@stpass
 * @description:
 * @time 2018-04-20  13:59
 * @change
 * @chang 2018-04-20  13:59
 * @class describe
 */
public class ItemSeekBar {
    public int[] colors;
    public float[] positions;
    public int percentage;
    public RectF rectF;
    public String decription;
    public int gravity;

    public ItemSeekBar() {
    }

    public ItemSeekBar(int[] colors, float[] positions, int percentage, String decription, int gravity) {
        this.colors = colors;
        this.positions = positions;
        this.percentage = percentage;
        this.decription = decription;
        this.gravity = gravity;
    }

    public static List<ItemSeekBar> getData(Context context) {
        List<ItemSeekBar> seekBars = new ArrayList<>();
        ItemSeekBar itemSeekBar = new ItemSeekBar();
        itemSeekBar.colors = new int[]{getColor(context, R.color.color_1), getColor(context, R.color.color_2),
                getColor(context, R.color.color_3), getColor(context, R.color.color_4),
                getColor(context, R.color.color_5), getColor(context, R.color.color_6)};
        itemSeekBar.positions = new float[]{0.1f, 0.4f, 0.5f, 0.7f, 0.8f, 1f};
        itemSeekBar.percentage = 100;
        itemSeekBar.gravity = Gravity.LEFT;
        seekBars.add(itemSeekBar);
        return seekBars;
    }

    public static int getColor(Context context, int colorId) {
        return ContextCompat.getColor(context, colorId);
    }
}
