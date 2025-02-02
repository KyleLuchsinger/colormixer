package com.example.colormixer.ui.game.logic

import androidx.compose.ui.geometry.Offset
import com.example.colormixer.model.Path

class IntersectionLogic {
    fun hasIntersections(
        path: Path,
        existingPaths: List<Path>,
        isDestination: (Offset) -> Boolean
    ): Boolean {
        val startPoint = path.points.first()
        val endPoint = path.points.last()

        // Check intersections with existing paths
        val intersectsExisting = existingPaths.any { existingPath ->
            pathsIntersect(path, existingPath, startPoint, endPoint)
        }

        // Check self-intersections (excluding start/end points)
        val hasSelfIntersections = if (path.points.size > 2) {
            val segments = path.points.zipWithNext().toList()
            segments.dropLast(1).withIndex().any { (i, segment1) ->
                segments.drop(i + 2).any { segment2 ->
                    // Skip if either segment involves start or end point
                    if (segment1.first == startPoint || segment1.second == endPoint ||
                        segment2.first == startPoint || segment2.second == endPoint) {
                        false
                    } else {
                        linesIntersect(
                            segment1.first.x, segment1.first.y,
                            segment1.second.x, segment1.second.y,
                            segment2.first.x, segment2.first.y,
                            segment2.second.x, segment2.second.y
                        )
                    }
                }
            }
        } else false

        return intersectsExisting || hasSelfIntersections
    }

    fun wouldCreateIntersection(
        from: Offset,
        to: Offset,
        existingPaths: List<Path>,
        currentPath: Path?,
        isDestination: (Offset) -> Boolean,
        isSourcePoint: (Offset) -> Boolean
    ): Boolean {
        // Don't check intersections if we're starting from a source or ending at a destination
        if (isSourcePoint(from) || isDestination(to)) {
            return false
        }

        // Check against existing complete paths
        val intersectsComplete = existingPaths.any { path ->
            path.points.zipWithNext().any { (p1, p2) ->
                // Skip if this segment involves start/end of existing path
                if (p1 == path.points.first() || p2 == path.points.last()) {
                    false
                } else {
                    linesIntersect(from.x, from.y, to.x, to.y, p1.x, p1.y, p2.x, p2.y)
                }
            }
        }

        // Check against current path (except last segment)
        if (currentPath != null && currentPath.points.size > 1) {
            val selfIntersect = currentPath.points.dropLast(1)
                .zipWithNext()
                .any { (p1, p2) ->
                    linesIntersect(from.x, from.y, to.x, to.y, p1.x, p1.y, p2.x, p2.y)
                }
            return intersectsComplete || selfIntersect
        }

        return intersectsComplete
    }

    private fun pathsIntersect(path1: Path, path2: Path, startPoint: Offset, endPoint: Offset): Boolean {
        return path1.points.zipWithNext().any { (start1, end1) ->
            path2.points.zipWithNext().any { (start2, end2) ->
                // Skip intersection check if either point is a start or end point
                if (start1 == startPoint || end1 == endPoint ||
                    start2 == path2.points.first() || end2 == path2.points.last()) {
                    false
                } else {
                    linesIntersect(
                        start1.x, start1.y, end1.x, end1.y,
                        start2.x, start2.y, end2.x, end2.y
                    )
                }
            }
        }
    }

    private fun linesIntersect(
        a1x: Float, a1y: Float, a2x: Float, a2y: Float,
        b1x: Float, b1y: Float, b2x: Float, b2y: Float
    ): Boolean {
        // Handle vertical and horizontal lines explicitly
        val aVertical = a1x == a2x
        val aHorizontal = a1y == a2y
        val bVertical = b1x == b2x
        val bHorizontal = b1y == b2y

        // If both lines are horizontal or both vertical, they can't intersect
        if ((aHorizontal && bHorizontal) || (aVertical && bVertical)) {
            return false
        }

        // Test horizontal line against vertical line
        if (aHorizontal && bVertical) {
            val minX = minOf(a1x, a2x)
            val maxX = maxOf(a1x, a2x)
            val minY = minOf(b1y, b2y)
            val maxY = maxOf(b1y, b2y)
            return (b1x >= minX && b1x <= maxX && a1y >= minY && a1y <= maxY)
        }

        // Test vertical line against horizontal line
        if (aVertical && bHorizontal) {
            val minX = minOf(b1x, b2x)
            val maxX = maxOf(b1x, b2x)
            val minY = minOf(a1y, a2y)
            val maxY = maxOf(a1y, a2y)
            return (a1x >= minX && a1x <= maxX && b1y >= minY && b1y <= maxY)
        }

        return false
    }
}