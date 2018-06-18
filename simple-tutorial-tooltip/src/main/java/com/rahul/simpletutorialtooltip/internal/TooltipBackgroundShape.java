package com.rahul.simpletutorialtooltip.internal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;

/**
 * @author Rahul Verma on 13/01/17 <rv@videoder.com>
 */

public class TooltipBackgroundShape extends Drawable {

    private static final float DEFAULT_CORNER_RADII = Utils.dpToPx(3);

    private float cornerRadius = Utils.dpToPx(4);
    private int color = Color.BLUE;
    private int alpha = 255;
    private ColorFilter colorFilter;

    private Rect originalPadding = new Rect(Utils.dpToPx(10), Utils.dpToPx(10), Utils.dpToPx(5), Utils.dpToPx(10)), offsetPadding = new Rect();
    private Paint paint;

    private float arrowWidth = Utils.dpToPx(18);
    private float arrowLength = Utils.dpToPx(15);
    private float shadowSize = Utils.dpToPx(2);

    @Alignment.TooltipAlignment
    private int alignment = Alignment.DOWN;

    @FloatRange(from = -1.0f, to = 1.0f)
    private float arrowPosOffset;

    private RectF shapeRect;
    private Path shapeArrow;
    private RectF shapeRectShadow;
    private Path shapeArrowShadow;


    private TooltipBackgroundShape() {
        evalOffsetPadding();
    }

    public void setArrowPosOffsetPx(long offset) {

        float horizontalCenter = getBounds().width() / 2F;
        float horizontalTrackLength = horizontalCenter - (cornerRadius + (arrowLength / 2f));
        setArrowPosOffset(offset / horizontalTrackLength);
    }

    public void setPadding(Rect rect) {
        this.originalPadding.set(rect);
        evalOffsetPadding();
        invalidateSelf();
    }

    private void evalOffsetPadding() {
        switch (alignment) {

            case Alignment.DOWN:
                this.offsetPadding = new Rect(originalPadding.left, (int) (originalPadding.top + arrowLength), originalPadding.right, (int) (originalPadding.bottom + shadowSize));
                break;
            case Alignment.END:
                this.offsetPadding = new Rect((int) (originalPadding.left + arrowWidth), originalPadding.top, originalPadding.right, (int) (originalPadding.bottom + shadowSize));
                break;
            case Alignment.START:
                this.offsetPadding = new Rect(originalPadding.left, originalPadding.top, (int) (originalPadding.right + arrowWidth), (int) (originalPadding.bottom + shadowSize));
                break;
            case Alignment.UP:
                this.offsetPadding = new Rect(originalPadding.left, originalPadding.top, originalPadding.right, (int) (originalPadding.bottom + arrowLength + shadowSize));
                break;
        }
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        padding.set(this.offsetPadding);
        return true;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
        }

        if (shapeRect == null) {
            initShapeRect();
        }
        if (shapeRectShadow == null) {
            initShapeRectShadow();
        }
        if (shapeArrow == null) {
            initShapeArrow();
        }
        if (shapeArrowShadow == null) {
            initShapeArrowShadow();
        }

        paint.setColorFilter(null);
        paint.setColor(0X33000000);
        canvas.drawRoundRect(shapeRectShadow, cornerRadius, cornerRadius, paint);
        canvas.drawPath(shapeArrowShadow, paint);

        paint.setColor(color);
        paint.setAlpha(alpha);
        if (colorFilter != null)
            paint.setColorFilter(colorFilter);


        canvas.drawRoundRect(shapeRect, cornerRadius, cornerRadius, paint);
        canvas.drawPath(shapeArrow, paint);
    }

    private void initShapeRect() {
        switch (alignment) {

            case Alignment.DOWN:
                shapeRect = new RectF(0, arrowLength, getBounds().width(), getBounds().height() - shadowSize);
                break;
            case Alignment.END:
                shapeRect = new RectF(arrowLength, 0, getBounds().width(), getBounds().height() - shadowSize);
                break;
            case Alignment.START:
                shapeRect = new RectF(0, 0, getBounds().width() - arrowLength, getBounds().height() - shadowSize);
                break;
            case Alignment.UP:
                shapeRect = new RectF(0, 0, getBounds().width(), getBounds().height() - (arrowLength + shadowSize));
                break;
        }
    }

    private void initShapeRectShadow() {
        switch (alignment) {

            case Alignment.DOWN:
                shapeRectShadow = new RectF(0, arrowLength, getBounds().width(), getBounds().height());
                break;
            case Alignment.END:
                shapeRectShadow = new RectF(arrowLength, 0, getBounds().width(), getBounds().height());
                break;
            case Alignment.START:
                shapeRectShadow = new RectF(0, 0, getBounds().width() - arrowLength, getBounds().height());
                break;
            case Alignment.UP:
                shapeRectShadow = new RectF(0, 0, getBounds().width(), getBounds().height() - arrowLength);
                break;
        }
    }

    private void initShapeArrow() {

        PointF[] points = new PointF[3];

        if (alignment == Alignment.DOWN || alignment == Alignment.UP) {

            float horizontalCenter = getBounds().width() / 2F;
            float horizontalTrackLength = horizontalCenter - (cornerRadius + (arrowLength / 2f));
            horizontalCenter = horizontalCenter + (arrowPosOffset * horizontalTrackLength);

            if (alignment == Alignment.UP) {
                points[0] = new PointF(horizontalCenter - (arrowWidth / 2f), getBounds().height() - (arrowLength + shadowSize));
                points[1] = new PointF(horizontalCenter + (arrowWidth / 2f), getBounds().height() - (arrowLength + shadowSize));
                points[2] = new PointF(horizontalCenter, getBounds().height() - shadowSize);
            } else if (alignment == Alignment.DOWN) {
                points[0] = new PointF(horizontalCenter - (arrowWidth / 2f), arrowLength);
                points[1] = new PointF(horizontalCenter + (arrowWidth / 2f), arrowLength);
                points[2] = new PointF(horizontalCenter, 0);
            }
        } else if (alignment == Alignment.START || alignment == Alignment.END) {

            float verticalCenter = ((getBounds().height() - (shadowSize + cornerRadius + cornerRadius + arrowWidth)) / 2F) + cornerRadius + (arrowLength / 2f);
            float verticalTrackLength = verticalCenter - (cornerRadius + (arrowLength / 2f));
            verticalCenter = verticalCenter + (arrowPosOffset * verticalTrackLength);

            if (alignment == Alignment.END) {
                points[0] = new PointF(arrowLength, verticalCenter - (arrowWidth / 2f));
                points[1] = new PointF(arrowLength, verticalCenter + (arrowWidth / 2f));
                points[2] = new PointF(0, verticalCenter);
            } else if (alignment == Alignment.START) {
                points[0] = new PointF(getBounds().width() - arrowLength, verticalCenter - (arrowWidth / 2f));
                points[1] = new PointF(getBounds().width() - arrowLength, verticalCenter + (arrowWidth / 2f));
                points[2] = new PointF(getBounds().width(), verticalCenter);
            }
        }

        shapeArrow = new Path();
        shapeArrow.moveTo(points[0].x, points[0].y);
        shapeArrow.lineTo(points[1].x, points[1].y);
        shapeArrow.lineTo(points[2].x, points[2].y);
        shapeArrow.lineTo(points[0].x, points[0].y);
        shapeArrow.close();

    }

    private void initShapeArrowShadow() {
        PointF[] points = new PointF[3];

        if (alignment == Alignment.DOWN || alignment == Alignment.UP) {

            float horizontalCenter = getBounds().width() / 2F;
            float horizontalTrackLength = horizontalCenter - (cornerRadius + (arrowLength / 2f));
            horizontalCenter = horizontalCenter + (arrowPosOffset * horizontalTrackLength);

            if (alignment == Alignment.UP) {
                points[0] = new PointF(horizontalCenter - (arrowWidth / 2f), getBounds().height() - arrowLength);
                points[1] = new PointF(horizontalCenter + (arrowWidth / 2f), getBounds().height() - arrowLength);
                points[2] = new PointF(horizontalCenter, getBounds().height());
            } else if (alignment == Alignment.DOWN) {
                points[0] = new PointF(horizontalCenter - (arrowWidth / 2f), arrowLength);
                points[1] = new PointF(horizontalCenter + (arrowWidth / 2f), arrowLength);
                points[2] = new PointF(horizontalCenter, 0);
            }
        } else if (alignment == Alignment.START || alignment == Alignment.END) {

            float verticalCenter = ((getBounds().height() - (shadowSize + cornerRadius + cornerRadius + arrowWidth)) / 2F) + cornerRadius + (arrowLength / 2f);
            float verticalTrackLength = verticalCenter - (cornerRadius + (arrowLength / 2f));
            verticalCenter = verticalCenter + (arrowPosOffset * verticalTrackLength);

            if (alignment == Alignment.END) {
                points[0] = new PointF(arrowLength, (verticalCenter - (arrowWidth / 2f)) + shadowSize);
                points[1] = new PointF(arrowLength, (verticalCenter + (arrowWidth / 2f)) + shadowSize);
                points[2] = new PointF(0, verticalCenter + shadowSize);
            } else if (alignment == Alignment.START) {
                points[0] = new PointF(getBounds().width() - arrowLength, (verticalCenter - (arrowWidth / 2f)) + shadowSize);
                points[1] = new PointF(getBounds().width() - arrowLength, (verticalCenter + (arrowWidth / 2f)) + shadowSize);
                points[2] = new PointF(getBounds().width(), verticalCenter + shadowSize);
            }
        }

        shapeArrowShadow = new Path();
        shapeArrowShadow.moveTo(points[0].x, points[0].y);
        shapeArrowShadow.lineTo(points[1].x, points[1].y);
        shapeArrowShadow.lineTo(points[2].x, points[2].y);
        shapeArrowShadow.lineTo(points[0].x, points[0].y);
        shapeArrowShadow.close();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getAlpha() {
        return alpha;
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidateSelf();
    }

    @Override
    public void invalidateSelf() {
        shapeArrow = null;
        shapeArrowShadow = null;
        shapeRect = null;
        shapeRectShadow = null;
        super.invalidateSelf();
    }

    @Alignment.TooltipAlignment
    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(@Alignment.TooltipAlignment int alignment) {
        this.alignment = alignment;
        evalOffsetPadding();
        invalidateSelf();
    }

    public float getArrowLength() {
        return arrowLength;
    }

    public void setArrowLength(@FloatRange(from = 0) float arrowLength) {
        this.arrowLength = arrowLength;
        evalOffsetPadding();
        invalidateSelf();
    }

    public float getArrowPosOffset() {
        return arrowPosOffset;
    }

    public void setArrowPosOffset(@FloatRange(from = -1.0f, to = 1.0f) float arrowPosOffset) {
        this.arrowPosOffset = arrowPosOffset;
        evalOffsetPadding();
        invalidateSelf();
    }

    public float getArrowWidth() {
        return arrowWidth;
    }

    public void setArrowWidth(@FloatRange(from = 0) float arrowWidth) {
        this.arrowWidth = arrowWidth;
        evalOffsetPadding();
        invalidateSelf();
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    @Nullable
    @Override
    public ColorFilter getColorFilter() {
        return colorFilter;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
        invalidateSelf();
    }

    public Rect getOffsetPadding() {
        return offsetPadding;
    }

    public Rect getOriginalPadding() {
        return originalPadding;
    }

    public Paint getPaint() {
        return paint;
    }

    public float getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(@Px int shadowSize) {
        this.shadowSize = shadowSize;
        evalOffsetPadding();
        invalidateSelf();
    }

    public Path getShapeArrow() {
        return shapeArrow;
    }

    public Path getShapeArrowShadow() {
        return shapeArrowShadow;
    }

    public RectF getShapeRect() {
        return shapeRect;
    }

    public RectF getShapeRectShadow() {
        return shapeRectShadow;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(@Px int cornerRadius) {
        this.cornerRadius = cornerRadius;
        evalOffsetPadding();
        invalidateSelf();
    }

    public void setArrowSize(@Px int length, @Px int width) {
        arrowLength = length;
        arrowWidth = width;
        evalOffsetPadding();
        invalidateSelf();
    }


    public static class Builder {
        private TooltipBackgroundShape tooltipBackgroundShape;

        public Builder() {
            this.tooltipBackgroundShape = new TooltipBackgroundShape();
        }

        public Builder setAlignment(@Alignment.TooltipAlignment int alignment) {
            tooltipBackgroundShape.setAlignment(alignment);
            return this;
        }

        public Builder setPadding(int left, int top, int right, int bottom) {
            tooltipBackgroundShape.setPadding(new Rect(left, top, right, bottom));
            return this;
        }

        public Builder setPadding(Rect padding) {
            tooltipBackgroundShape.setPadding(padding);
            return this;
        }

        public Builder setColor(@ColorInt int color) {
            tooltipBackgroundShape.setColor(color);
            return this;
        }

        public Builder setShadowSize(@Px int shadowSize) {
            tooltipBackgroundShape.setShadowSize(shadowSize);
            return this;
        }

        public Builder setArrowPosOffset(@FloatRange(from = -1.0f, to = 1.0f) float offset) {
            tooltipBackgroundShape.setArrowPosOffset(offset);
            return this;
        }

        public Builder setArrowLength(@FloatRange(from = 0) float arrowLength) {
            tooltipBackgroundShape.setArrowLength(arrowLength);
            return this;
        }

        public Builder setArrowWidth(@FloatRange(from = 0) float arrowWidth) {
            tooltipBackgroundShape.setArrowWidth(arrowWidth);
            return this;
        }

        public TooltipBackgroundShape build() {
            return tooltipBackgroundShape;
        }

    }


}
