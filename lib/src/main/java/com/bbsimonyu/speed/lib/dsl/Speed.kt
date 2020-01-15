@file:Suppress("unused")

package com.bbsimonyu.speed.lib.dsl

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.PointF
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.bbsimonyu.speed.lib.*

/**
 * @author Simon Yu
 */

/**
 */
class Speed : LifecycleObserver {
    internal lateinit var view: View
    private var rotate: Rotate? = null
    private var translation: TranslationSet? = null
    private var visibility: Visibility? = null
    private var scale: ScaleSet? = null

    fun rotate(init: Rotate.() -> Unit): Rotate {
        val rotate = Rotate()
        rotate.init()
        this.rotate = rotate
        return rotate
    }

    fun translation(init: TranslationSet.() -> Unit): TranslationSet {
        val translation = TranslationSet()
        translation.init()
        this.translation = translation
        return translation
    }

    fun visibility(init: Visibility.() -> Unit): Visibility {
        val visibility = Visibility()
        visibility.init()
        this.visibility = visibility
        return visibility
    }

    fun scale(init: ScaleSet.() -> Unit): ScaleSet {
        val scale = ScaleSet()
        scale.init()
        this.scale = scale
        return scale
    }

    fun start() {

    }

    fun stop(stop: () -> Unit) {
        stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

    }
}

class Speeds {
    private val list: MutableSet<Speed> = mutableSetOf()

    fun view(view: View, init: Speed.(view: View) -> Unit): Speed {
        val speed = Speed()
        speed.init(view)
        list.add(speed)
        return speed
    }
}

open class Motion : HasMotion, Initial<UnitValue> {
    override var duration: UnitValue? = null
    override var speed: UnitValue? = null
    override var initial: UnitValue? = null
    override var distance: UnitValue? = null
    override var acceleration: UnitValue? = null
}

class Rotate : Motion(), HasPivot, HasDirection, Apply {
    override fun apply(view: View): Animator {
        val initialValue = initial?.normalize(view) ?: view.rotation
        speed?.let { s ->
            // speed is set
            val speedValue = s.normalize(view)
            duration?.let { dur ->
                val durationValue = dur.normalize(view)
                val distanceValue = speedValue * dur.normalize(view)
                return ValueAnimator.ofFloat(initialValue, initialValue + distanceValue).apply {
                    duration = durationValue.toLong()
                }
            } ?: run {
                // duration not set
                distance?.let { dis ->
                    // distance set
                    val distanceValue = dis.normalize(view)
                    val durationValue = distanceValue / speedValue
                    return ValueAnimator.ofFloat(initialValue, initialValue + distanceValue).apply {
                        duration = durationValue.toLong()
                    }
                } ?: run {
                    // distance not set
                    val distanceValue = Float.MAX_VALUE
                    val durationValue = (distanceValue - initialValue) / speedValue
                    return ValueAnimator.ofFloat(initialValue, initialValue + distanceValue).apply {
                        duration = durationValue.toLong()
                    }
                }
            }
        } ?: run {
            // speed not set
            duration?.let {  dur ->
                val durationValue = dur.normalize(view)
                distance?.let { dis ->
                    val distanceValue = dis.normalize(view)
                    return ValueAnimator.ofFloat(initialValue, initialValue + distanceValue).apply {
                        duration = durationValue.toLong()
                    }
                } ?: run { // distance not set
                    throw RuntimeException()
                }
            } ?: run { // duration not set
                distance?.let { dis ->
                    val distanceValue = dis.normalize(view)
                    return ValueAnimator.ofFloat(initialValue, initialValue + distanceValue).apply {
                        duration = configure.defaultDuration.toLong()
                    }
                } ?: run { // distance not set
                    throw RuntimeException()
                }
            }
        }
    }

    override lateinit var pivot: PointF

    override lateinit var direction: Direction
}

class Translation : Motion(), HasDirection, Apply {
    override fun apply(view: View): Animator {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override lateinit var direction: Direction
}

class Visibility : Motion(), HasDirection, Apply {
    override fun apply(view: View): Animator {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override lateinit var direction: Direction
}

class TranslationSet {
    private lateinit var x: Translation
    private lateinit var y: Translation
    private lateinit var z: Translation
    fun x(init: Translation.() -> Unit): Translation {
        val translation = Translation()
        translation.init()
        x = translation
        return translation
    }

    fun y(init: Translation.() -> Unit): Translation {
        val translation = Translation()
        translation.init()
        y = translation
        return translation
    }

    fun z(init: Translation.() -> Unit): Translation {
        val translation = Translation()
        translation.init()
        z = translation
        return translation
    }
}

class ScaleSet : HasPivot {
    override lateinit var pivot: PointF
    private var x: Scale? = null
    private var y: Scale? = null

    fun x(init: Scale.() -> Unit): Scale {
        val scale = Scale()
        scale.init()
        x = scale
        return scale
    }

    fun y(init: Scale.() -> Unit): Scale {
        val scale = Scale()
        scale.init()
        y = scale
        return scale
    }
}

class Scale : Motion(), HasDirection, Apply {
    override lateinit var direction: Direction

    override fun apply(view: View): Animator {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

interface Initial<T> {
    var initial: UnitValue?
}

interface HasPivot {
    var pivot: PointF
}

interface HasDirection {
    var direction: Direction
}

/**
 * Define properties that can be used by a motion
 */
interface HasMotion {
    var duration: UnitValue?
    var speed: UnitValue?
    var distance: UnitValue?
    var acceleration: UnitValue?
}

interface Apply {
    fun apply(view: View): Animator
}

infix fun Float.x(y: Float): PointF {
    return PointF(this, y)
}

fun LifecycleOwner.speed(view: View, init: Speed.(View) -> Unit): Speed {
    val speed = Speed()
    speed.init(view)
    return speed
}

/**
 * Start a new motion for a set of views
 */
fun speed(init: Speeds.() -> Unit): Speeds {
    val speeds = Speeds()
    speeds.init()
    return speeds
}

/**
 * Animate the view by Speed
 */
fun View.speed(init: Speed.() -> Unit): Speed {
    val speed = Speed()
    speed.view = this
    speed.init()
    return speed
}

fun sample(view: View) {

    // create motion for a single view
    val motion = view.speed {
        rotate {
            initial = 200f.px()
            speed = 5f.dp()
            pivot = 0.5f x 0.5f
        }
        visibility {
            duration = 200f.millisecond()
        }
        translation {
            x {
                initial = 0f.px()
                speed = 20f.second()
            }
        }
    }

    // create motions for a set of views
    speed {
        view(view) {
            rotate {
                initial = 0f.px()
            }
            scale {
                pivot = 0.5f x 0.5f
                x {
                    distance = 0.5f.percentScaleX()
                    direction = Direction.DECRESSE
                }
            }
        }
    }

    motion.start()

    motion.stop {
        // actually stopped
    }
}
