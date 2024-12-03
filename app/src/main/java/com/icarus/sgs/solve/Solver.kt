package com.icarus.sgs.solve

import android.graphics.Point

class Solver {

    private val transformMap = mutableMapOf<PuzzlePiece, List<Shape>>()

    // 旋转函数：根据角度旋转形状
    private fun rotate(shape: List<Point>, angle: Int): List<Point> {
        return when (angle) {
            90 -> shape.map { Point(-it.y, it.x) }
            180 -> shape.map { Point(-it.x, -it.y) }
            270 -> shape.map { Point(it.y, -it.x) }
            else -> shape
        }.normalized()
    }

    // 水平翻转：将所有点的 x 坐标取反
    private fun horizontalFlip(shape: List<Point>): List<Point> {
        return shape.map { Point(-it.x, it.y) }.normalized()
    }

    // 垂直翻转：将所有点的 y 坐标取反
    private fun verticalFlip(shape: List<Point>): List<Point> {
        return shape.map { Point(it.x, -it.y) }.normalized()
    }

    private fun List<Point>.normalized(): List<Point> {
        val minX = minOf { it.x }
        val minY = minOf { it.y }
        return map { Point(it.x - minX, it.y - minY) }.sortedBy { it.x * 100 + it.y }
    }

    // 生成形状的所有旋转和翻转版本
    private fun generateAllTransformations(puzzlePiece: PuzzlePiece): List<Shape> {
        return transformMap.getOrPut(puzzlePiece) {
            val transformations = mutableSetOf<List<Point>>()

            // 生成四个旋转版本
            for (angle in arrayOf(0, 90, 180, 270)) {
                val rotatedShape = rotate(puzzlePiece.points, angle)
                // 对每个旋转版本进行水平翻转和垂直翻转
                transformations.add(rotatedShape) // 添加旋转版本
                transformations.add(horizontalFlip(rotatedShape)) // 添加水平翻转
                transformations.add(verticalFlip(rotatedShape)) // 添加垂直翻转
            }
            transformations.map { Shape(puzzlePiece, it) }
        }
    }

    // 检查形状是否可以放置在网格中的指定位置
    private fun canPlace(puzzle: Puzzle, shape: Shape, startRow: Int, startCol: Int): Boolean {
        for ((dx, dy) in shape.shape) {
            val row = startRow + dx
            val col = startCol + dy
            if (row < 0 || row >= puzzle.size || col < 0 || col >= puzzle.size || puzzle.get(row, col) != null) {
                return false
            }
        }
        return true
    }

    // 将形状放置到网格中
    private fun placeShape(puzzle: Puzzle, shape: Shape, startRow: Int, startCol: Int, label: PuzzlePiece): Boolean {
        if (canPlace(puzzle, shape, startRow, startCol)) {
            for ((dx, dy) in shape.shape) {
                val row = startRow + dx
                val col = startCol + dy
                puzzle.put(row, col, label)
            }
            return true
        }
        return false
    }

    private fun undoPlaceShape(grid: Puzzle, shape: Shape, startRow: Int, startCol: Int) {
        for ((dx, dy) in shape.shape) {
            val row = startRow + dx
            val col = startCol + dy
            grid.put(row, col, null)
        }
    }

    // 回溯算法：尝试将形状放入网格，直到所有形状都成功放置
    private fun solve(puzzle: Puzzle, shapes: List<PuzzlePiece>, shapeIndex: Int): Boolean {
        if (shapeIndex == shapes.size) {
            return true // 所有形状已成功放置
        }
        val transformShapes = generateAllTransformations(shapes[shapeIndex]) // 获取当前形状的所有旋转版本
        for (shape in transformShapes) {
            for (row in 0 until puzzle.size) {
                for (col in 0 until puzzle.size) {
                    if (placeShape(puzzle, shape, row, col, shape.label)) {
                        place++
                        if (solve(puzzle, shapes, shapeIndex + 1)) { // 递归尝试下一个形状
                            return true
                        } else {
                            undoPlaceShape(puzzle, shape, row, col)
                        }
                    }
                }
            }
        }
        return false // 没有解，回退
    }

    private var place = 0

    fun solve(puzzle: Puzzle) {
        puzzle.solved = solve(puzzle, PuzzlePiece.entries.filter { it.points.isNotEmpty() }, 0)
    }

    private data class Shape(val label: PuzzlePiece, val shape: List<Point>)

    private operator fun Point.component1(): Int = x
    private operator fun Point.component2(): Int = y

}