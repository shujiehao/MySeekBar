package com.stpass.clinical.adapter;

import android.content.Context;

import com.stpass.clinical.model.ItemSeekBar;
import com.stpass.clinical.widget.StatusSeekBar;

import java.util.List;

/**
 * Created by Administrator on 2018-04-19.
 */

public class MySeekBarAdapter implements StatusSeekBar.SeekBarAdapterListener {
    List<ItemSeekBar> seekBars;
    Context context;

    public MySeekBarAdapter(List<ItemSeekBar> seekBars, Context context) {
        this.seekBars = seekBars;
        this.context = context;
    }


    @Override
    public List<ItemSeekBar> getItemSeekBar() {
        return seekBars;
    }

}
