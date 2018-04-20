package com.stpass.clinical.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.stpass.clinical.R;
import com.stpass.clinical.model.ItemSeekBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-04-19.
 */

public class StatusSeekBar extends View {
    private String TAG = "MySeekBar";
    private SeekBarAdapterListener adapter;
    private Paint mLinePaint;
    private TextPaint mTextPaint;
    private int progress = 50;
    private int triangleColor;
    int measureWidth = 0;
    private Paint trianglePaint;
    private Path trianglePath;
    private RectF triangleRect;
    public int triangleWidth = 60;
    public int triangleHeight = 46;
    public Context mContext;
    private float line_height;
    private int triangleMarginTop;
    private OnProgressListener onProgressListener;
    private int textPadding;
    private List<PercentForColor> progressAttr = new ArrayList<>();

    public StatusSeekBar(Context context) {
        super(context);
    }

    public StatusSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(measureWidth, (int) (line_height + triangleMarginTop + triangleHeight));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 100;//根据自己的需要更改
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (adapter != null) {
            int startLeft = getPaddingLeft();
            int percent = 0;
            canvas.save();
            List<ItemSeekBar> seekBars = adapter.getItemSeekBar();
            float lineButtom = getPaddingTop() + line_height;
            int lineWidth = measureWidth - getPaddingLeft() - getPaddingRight();
            progressAttr.clear();
            for (int i = 0; i < seekBars.size(); i++) {
                ItemSeekBar seekBar = seekBars.get(i);
                int rectFWidth = (lineWidth) * seekBar.percentage / 100;
                seekBar.rectF = new RectF(startLeft, getPaddingTop(), startLeft + rectFWidth, lineButtom);
                startLeft += rectFWidth;
                PercentForColor percentForColor = new PercentForColor(percent, percent + seekBar.percentage, seekBar.colors, seekBar.positions);
                percent += seekBar.percentage;
                progressAttr.add(percentForColor);
                LinearGradient backGradient = new LinearGradient(0, line_height / 2, rectFWidth, line_height / 2, seekBar.colors, seekBar.positions, Shader.TileMode.CLAMP);
                mLinePaint.clearShadowLayer();
                mLinePaint.setShader(backGradient);
                canvas.drawRect(seekBar.rectF, mLinePaint);
            }
            RectF rect = new RectF(getPaddingLeft(), getPaddingTop(), measureWidth - getPaddingRight(), lineButtom + getPaddingTop());
            triangleRect = new RectF();
            triangleRect.top = lineButtom + triangleMarginTop;
            triangleRect.bottom = triangleRect.top + triangleHeight;
            triangleRect.left = getPaddingLeft() + lineWidth * progress / 100 - triangleWidth / 2;
            triangleRect.right = triangleRect.left + triangleWidth;
            // 绘制三角形
            trianglePath = new Path();
            trianglePath.moveTo(triangleRect.left, triangleRect.bottom);
            trianglePath.lineTo(triangleRect.left + triangleRect.width() / 2, triangleRect.top);
            trianglePath.lineTo(triangleRect.right, triangleRect.bottom);
            trianglePath.close();
            trianglePaint.setColor(getColorForProgress(progress));
            canvas.drawPath(trianglePath, trianglePaint);

            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            int baseLineY = (int) (rect.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2);
            String progressStr = String.valueOf(progress);
            float textWidth = mTextPaint.measureText(progressStr) + textPadding;
            float x = getPaddingLeft() + (lineWidth) * progress / 100;
            if (x < getPaddingLeft() + textWidth) {
                x = getPaddingLeft() + textWidth / 2;
            } else if (x > measureWidth - getPaddingRight() - textWidth) {
                x = measureWidth - getPaddingRight() - textWidth / 2;
            }
            canvas.drawText(progressStr, x, baseLineY, mTextPaint);
            canvas.restore();
        }
    }

    public void setAdapter(SeekBarAdapterListener adapter) {
        this.adapter = adapter;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.StatusSeekBar);
        line_height = typedArray.getDimension(R.styleable.StatusSeekBar_line_height, 42);
        triangleWidth = (int) typedArray.getDimension(R.styleable.StatusSeekBar_triangle_width, 56);
        triangleHeight = (int) typedArray.getDimension(R.styleable.StatusSeekBar_triangle_height, 36);
        triangleMarginTop = (int) typedArray.getDimension(R.styleable.StatusSeekBar_triangle_marginTop, 6);
        textPadding = (int) typedArray.getDimension(R.styleable.StatusSeekBar_text_padding, 8);
        typedArray.recycle();
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(24);
        mTextPaint.setColor(Color.WHITE);

        mLinePaint = new Paint();
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth(40);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);

        trianglePaint = new Paint();
        trianglePaint.setAntiAlias(true);
        trianglePaint.setStyle(Paint.Style.FILL);
        trianglePaint.setStrokeCap(Paint.Cap.ROUND);
        triangleColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        trianglePaint.setColor(triangleColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (isTouch(x, y)) {
                    int percent = (x - getPaddingLeft()) * 100 / (measureWidth - getPaddingLeft() - getPaddingRight());
                    if (percent >= 0 && percent <= 100) {
                        progress = percent;
                        invalidate();
                        if (onProgressListener != null) {
                            onProgressListener.progressChange(progress);
                        }
                    }
                }
                break;
        }
        return true;
    }

    public boolean isTouch(int x, int y) {
        int padding = 8;//加大手势范围
        Region region = new Region();
        Rect rect = new Rect();
        triangleRect.round(rect);
        rect.left = rect.left - padding;
        rect.top = rect.top - padding;
        rect.right = rect.right + padding;
        rect.bottom = rect.bottom + padding;
        region.set(rect);
        return region.contains(x, y);
    }

    public interface OnProgressListener {
        void progressChange(int progress);
    }

    public interface SeekBarAdapterListener {
        List<ItemSeekBar> getItemSeekBar();
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    /**
     * 获取某个百分比位置的颜色
     *
     * @param radio 取值[0,1]
     * @return color
     */
    public int getColor(float radio, int[] colorArr, float[] positionArr) {
        int startColor;
        int endColor;
        if (radio >= 1) {
            return colorArr[colorArr.length - 1];
        }
        for (int i = 0; i < positionArr.length; i++) {
            if (radio <= positionArr[i]) {
                if (i == 0) {
                    return colorArr[0];
                }
                startColor = colorArr[i - 1];
                endColor = colorArr[i];
                return getColorFrom(startColor, endColor, radio);
            }
        }
        return -1;
    }

    /**
     * 取两个颜色间的渐变区间 中的某一点的颜色
     *
     * @param startColor s
     * @param endColor   e
     * @param radio      r
     * @return color
     */
    public int getColorFrom(int startColor, int endColor, float radio) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }

    public int getColorForProgress(int progress) {
        for (int i = 0; i < progressAttr.size(); i++) {
            PercentForColor bean = progressAttr.get(i);
            if (progress >= bean.startPercentage && progress <= bean.endPercentage) {
                float radio = (progress - bean.startPercentage) / (bean.endPercentage - bean.startPercentage);
                return getColor(radio, bean.colors, bean.positions);
            }
        }
        return Color.argb(255, 0, 0, 0);
    }

    public class PercentForColor {
        public float startPercentage;
        public float endPercentage;
        public int[] colors;
        public float[] positions;

        public PercentForColor(float startPercentage, float endPercentage, int[] colors, float[] positions) {
            this.startPercentage = startPercentage;
            this.endPercentage = endPercentage;
            this.colors = colors;
            this.positions = positions;
        }
    }
}
