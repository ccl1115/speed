package com.bbsimonyu.speed.lib

import android.view.View
import android.view.ViewGroup

/**
 * Float value with unit specified
 *
 * 0.5f.parentWidth() // 50% of parent view width
 * 0.2f.parentHeight()
 * 200f.px()
 * 400f.dp()
 * 25f.sp()
 * @author Simon Yu
 */
class UnitValue(val value: Float, val unit: Unit) {
    fun normalize(view: View): Float {
        return when (unit) {
            Unit.PX -> value
            Unit.DP -> view.context.resources.displayMetrics.density * value
            Unit.SP -> view.context.resources.displayMetrics.scaledDensity * value
            Unit.PARENT_WIDTH -> (view.parent as ViewGroup).measuredWidth * value
            Unit.PARENT_HEIGHT -> (view.parent as ViewGroup).measuredWidth * value
            Unit.PERCENT_SCALE_X -> (view.parent as ViewGroup).scaleX * value
            Unit.PERCENT_SCALE_Y -> (view.parent as ViewGroup).scaleY * value
            Unit.WINDOW_WIDTH -> view.context.resources.displayMetrics.widthPixels * value
            Unit.WINDOW_HEIGHT -> view.context.resources.displayMetrics.heightPixels * value
            Unit.SELF_WIDTH -> view.measuredWidth * value
            Unit.SELF_HEIGHT -> view.measuredHeight * value
            Unit.SECOND -> value * 1000
            Unit.MILLISECOND -> value
        }
    }
}


enum class Unit {
    PX,
    DP,
    SP,
    PARENT_WIDTH,
    PARENT_HEIGHT,
    SELF_WIDTH,
    SELF_HEIGHT,
    PERCENT_SCALE_X,
    PERCENT_SCALE_Y,
    WINDOW_WIDTH,
    WINDOW_HEIGHT,
    SECOND,
    MILLISECOND,
}

fun Float.dp(): UnitValue {
    return UnitValue(this, Unit.DP)
}

fun Float.px(): UnitValue {
    return UnitValue(this, Unit.PX)
}

fun Float.sp(): UnitValue {
    return UnitValue(this, Unit.SP)
}

fun Float.parentWidth(): UnitValue {
    return UnitValue(this, Unit.PARENT_WIDTH)
}

fun Float.parentHeight(): UnitValue {
    return UnitValue(this, Unit.PARENT_HEIGHT)
}

fun Float.selfWidth(): UnitValue {
    return UnitValue(this, Unit.SELF_WIDTH)
}

fun Float.selfHeight(): UnitValue {
    return UnitValue(this, Unit.SELF_HEIGHT)
}

fun Float.percentScaleX(): UnitValue {
    return UnitValue(this, Unit.PERCENT_SCALE_X)
}

fun Float.percentScaleY(): UnitValue {
    return UnitValue(this, Unit.PERCENT_SCALE_Y)
}

fun Float.second(): UnitValue {
    return UnitValue(this, Unit.SECOND)
}

fun Float.millisecond(): UnitValue {
    return UnitValue(this, Unit.MILLISECOND)
}
