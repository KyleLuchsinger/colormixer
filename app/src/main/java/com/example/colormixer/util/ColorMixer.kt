package com.example.colormixer.util

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min

object ColorMixer {
    fun mixColors(colors: List<Color>): Color {
        if (colors.isEmpty()) return Color.Transparent
        if (colors.size == 1) return colors.first()

        // Convert all colors to HSL for more natural mixing
        val hslColors = colors.map { rgbToHSL(it) }

        // Special case handling for known color combinations
        if (colors.size == 2) {
            val color1 = colors[0]
            val color2 = colors[1]

            // Check for blue + yellow = green
            if (isBlue(color1) && isYellow(color2) || isBlue(color2) && isYellow(color1)) {
                return Color(0xFF00FF00) // Pure green
            }

            // Check for red + blue = purple
            if (isRed(color1) && isBlue(color2) || isRed(color2) && isBlue(color1)) {
                return Color(0xFF800080) // Purple
            }

            // Check for red + yellow = orange
            if (isRed(color1) && isYellow(color2) || isRed(color2) && isYellow(color1)) {
                return Color(0xFFFF8C00) // Orange
            }
        }

        // For other combinations or more than 2 colors, use HSL mixing
        val mixedH = averageHue(hslColors.map { it.first })
        val mixedS = hslColors.map { it.second }.average()
        val mixedL = hslColors.map { it.third }.average()

        return hslToRGB(mixedH, mixedS.toFloat(), mixedL.toFloat())
    }

    private fun isBlue(color: Color): Boolean {
        return color.blue > 0.6 && color.red < 0.4 && color.green < 0.4
    }

    private fun isYellow(color: Color): Boolean {
        return color.red > 0.6 && color.green > 0.6 && color.blue < 0.4
    }

    private fun isRed(color: Color): Boolean {
        return color.red > 0.6 && color.green < 0.4 && color.blue < 0.4
    }

    private fun rgbToHSL(color: Color): Triple<Float, Float, Float> {
        val r = color.red
        val g = color.green
        val b = color.blue

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        var h: Float
        val s: Float
        val l = (max + min) / 2f

        if (max == min) {
            h = 0f
            s = 0f
        } else {
            val d = max - min
            s = if (l > 0.5f) d / (2f - max - min) else d / (max + min)
            h = when (max) {
                r -> (g - b) / d + (if (g < b) 6 else 0)
                g -> (b - r) / d + 2
                b -> (r - g) / d + 4
                else -> 0f
            }
            h /= 6f
        }

        return Triple(h, s, l)
    }

    private fun hslToRGB(h: Float, s: Float, l: Float): Color {
        fun hue2rgb(p: Float, q: Float, t: Float): Float {
            var tt = t
            if (tt < 0) tt += 1
            if (tt > 1) tt -= 1
            if (tt < 1/6f) return p + (q - p) * 6 * tt
            if (tt < 1/2f) return q
            if (tt < 2/3f) return p + (q - p) * (2/3f - tt) * 6
            return p
        }

        if (s == 0f) {
            return Color(l, l, l)
        }

        val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
        val p = 2 * l - q

        val r = hue2rgb(p, q, h + 1/3f)
        val g = hue2rgb(p, q, h)
        val b = hue2rgb(p, q, h - 1/3f)

        return Color(r.coerceAtMost(1f), g.coerceAtMost(1f), b.coerceAtMost(1f))
    }

    private fun averageHue(hues: List<Float>): Float {
        if (hues.isEmpty()) return 0f

        var x = 0f
        var y = 0f

        // Convert hue angles to x,y coordinates on a unit circle
        hues.forEach { hue ->
            val angle = hue * 2 * Math.PI
            x += kotlin.math.cos(angle).toFloat()
            y += kotlin.math.sin(angle).toFloat()
        }

        // Convert average x,y back to angle
        val averageAngle = kotlin.math.atan2(y, x)
        var averageHue = (averageAngle / (2 * Math.PI)).toFloat()
        if (averageHue < 0) averageHue += 1f

        return averageHue
    }
}