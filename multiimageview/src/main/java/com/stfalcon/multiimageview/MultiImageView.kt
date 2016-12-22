/*******************************************************************************
 * Copyright 2016 stfalcon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.stfalcon.multiimageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.util.AttributeSet
import android.widget.ImageView
import java.util.*

/**
 * Created by Anton Bevza on 12/22/16.
 */

class MultiImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
    //Shape of view
    var shape = Shape.NONE
        set(value) {
            field = value
            invalidate()
        }
    //Corners radius for rectangle shape
    var rectCorners = 100

    private val bitmaps = ArrayList<Bitmap>()
    private val path = Path()
    private val rect = RectF()
    private var multiDrawable: Drawable? = null

    /**
     * Add image to view
     */
    fun addImage(bitmap: Bitmap) {
        bitmaps.add(bitmap)
        refresh()
    }

    /**
     * Remove all images
     */
    fun clear() {
        bitmaps.clear()
        refresh()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refresh()
    }

    /**
     * recreate MultiDrawable and set it as Drawable to ImageView
     */
    private fun refresh() {
        multiDrawable = MultiDrawable(bitmaps)
        setImageDrawable(multiDrawable)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            if (drawable != null) {
                //if shape not set - just draw
                if (shape != Shape.NONE) {
                    path.reset()
                    //ImageView size
                    rect.set(0f, 0f, width.toFloat(), height.toFloat())
                    if (shape == Shape.RECTANGLE) {
                        //Rectangle with corners
                        path.addRoundRect(rect, rectCorners.toFloat(),
                                rectCorners.toFloat(), Path.Direction.CW)
                    } else {
                        //Oval
                        path.addOval(rect, Path.Direction.CW)
                    }
                    //Clip with shape
                    canvas.clipPath(path)
                }
                super.onDraw(canvas)
            }
        }
    }

    //Types of shape
    enum class Shape {
        CIRCLE, RECTANGLE, NONE
    }
}

class MultiDrawable(val bitmaps: ArrayList<Bitmap>) : Drawable() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val items = ArrayList<PhotoItem>()

    /**
     * Create PhotoItem with position and size depends of count of images
     */
    private fun init() {
        items.clear()
        if (bitmaps.size == 1) {
            val bitmap = scaleCenterCrop(bitmaps[0], bounds.width(), bounds.height())
            items.add(PhotoItem(bitmap, Rect(0, 0, bounds.width(), bounds.height())))
        } else if (bitmaps.size == 2) {
            val bitmap1 = scaleCenterCrop(bitmaps[0], bounds.width(), bounds.height() / 2)
            val bitmap2 = scaleCenterCrop(bitmaps[1], bounds.width(), bounds.height() / 2)
            items.add(PhotoItem(bitmap1, Rect(0, 0, bounds.width() / 2, bounds.height())))
            items.add(PhotoItem(bitmap2, Rect(bounds.width() / 2, 0, bounds.width(), bounds.height())))
        } else if (bitmaps.size == 3) {
            val bitmap1 = scaleCenterCrop(bitmaps[0], bounds.width(), bounds.height() / 2)
            val bitmap2 = scaleCenterCrop(bitmaps[1], bounds.width() / 2, bounds.height() / 2)
            val bitmap3 = scaleCenterCrop(bitmaps[2], bounds.width() / 2, bounds.height() / 2)
            items.add(PhotoItem(bitmap1, Rect(0, 0, bounds.width() / 2, bounds.height())))
            items.add(PhotoItem(bitmap2, Rect(bounds.width() / 2, 0, bounds.width(), bounds.height() / 2)))
            items.add(PhotoItem(bitmap3, Rect(bounds.width() / 2, bounds.height() / 2, bounds.width(), bounds.height())))
        }
        if (bitmaps.size == 4) {
            val bitmap1 = scaleCenterCrop(bitmaps[0], bounds.width() / 2, bounds.height() / 2)
            val bitmap2 = scaleCenterCrop(bitmaps[1], bounds.width() / 2, bounds.height() / 2)
            val bitmap3 = scaleCenterCrop(bitmaps[2], bounds.width() / 2, bounds.height() / 2)
            val bitmap4 = scaleCenterCrop(bitmaps[3], bounds.width() / 2, bounds.height() / 2)
            items.add(PhotoItem(bitmap1, Rect(0, 0, bounds.width() / 2, bounds.height() / 2)))
            items.add(PhotoItem(bitmap2, Rect(0, bounds.height() / 2, bounds.width() / 2, bounds.height())))
            items.add(PhotoItem(bitmap3, Rect(bounds.width() / 2, 0, bounds.width(), bounds.height() / 2)))
            items.add(PhotoItem(bitmap4, Rect(bounds.width() / 2, bounds.height() / 2, bounds.width(), bounds.height())))
        }
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            items.forEach {
                canvas.drawBitmap(it.bitmap, bounds, it.position, paint)
            }
        }
    }

    /**
     * scale and center crop image
     */
    private fun scaleCenterCrop(source: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
        return ThumbnailUtils.extractThumbnail(source, newWidth, newHeight)
    }

    /***
     * Data class for store bitmap and position
     */
    data class PhotoItem(val bitmap: Bitmap, val position: Rect)


    //***Needed to override***//
    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        init()
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter) {
        paint.colorFilter = colorFilter
    }
    //***------------------***//
}

