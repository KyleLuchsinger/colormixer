package com.example.colormixer.util

import androidx.compose.ui.geometry.Offset
import com.example.colormixer.model.Path

object PathIntersectionUtil {
    fun checkPathIntersection(path1: Path, path2: Path): List<Offset> {
        val intersections = mutableListOf<Offset>()

        for (i in 0 until path1.points.size - 1) {
            val line1Start = path1.points[i]
            val line1End = path1.points[i + 1]

            for (j in 0 until path2.points.size - 1) {
                val line2Start = path2.points[j]
                val line2End = path2.points[j + 1]

                findIntersection(
                    line1Start, line1End,
                    line2Start, line2End
                )?.let { intersection ->
                    intersections.add(intersection)
                }
            }
        }

        return intersections
    }

    private fun findIntersection(
        line1Start: Offset,
        line1End: Offset,
        line2Start: Offset,
        line2End: Offset
    ): Offset? {
        // Line intersection math implementation
        // Returns the point of intersection if lines intersect, null otherwise
        return null // Actual implementation needed
    }
}