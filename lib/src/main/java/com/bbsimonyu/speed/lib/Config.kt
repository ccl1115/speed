package com.bbsimonyu.speed.lib

import android.content.Context
import android.graphics.PointF
import android.view.animation.Interpolator
import com.bbsimonyu.speed.lib.dsl.x

/**
 * @author Simon Yu
 */

class Config() {
    var defaultSpeed: Float = 5f
    var defaultDuration: Float = 350f
    var defaultPivot: PointF = 0.5f x 0.5f
    var defaultInterpolator: Interpolator = Interpolator {
        val v = it - 1f
        v * v * v * v + 1f
    }
    lateinit var context: Context
}

lateinit var configure: Config

fun configureSpeed(init: Config.() -> Unit) {
    val config = Config()
    config.init()
    configure = config
}

