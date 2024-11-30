package com.icarus.sgs.solve

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PuzzleView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var puzzle = Puzzle()

    private var ceil = puzzle.size
    private var paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width - paddingStart - paddingEnd
        val height = height - paddingTop - paddingBottom
        val ceilWidth = width / ceil
        val ceilHeight = height / ceil
        val start = paddingStart + 1
        val top = paddingTop + 2
        for (row in 0 until ceil) {
            for (col in 0 until ceil) {
                val piece = puzzle.getSolved(row, col)
                drawCeil(
                    canvas,
                    (start + col * ceilWidth).toFloat(),
                    (top + row * ceilHeight).toFloat(),
                    (start + (col + 1) * ceilWidth).toFloat(),
                    (top + (row + 1) * ceilHeight).toFloat(),
                    piece
                )
                //获取上下左右的piece,注意边界情况
                val leftPiece = if (col > 0) puzzle.getSolved(row, col - 1) else null
                val rightPiece = if (col < ceil - 1) puzzle.getSolved(row, col + 1) else null
                val topPiece = if (row > 0) puzzle.getSolved(row - 1, col) else null
                val bottomPiece = if (row < ceil - 1) puzzle.getSolved(row + 1, col) else null
                //如果上下左右对应边界的piece不同,绘制分割线

                paint.strokeWidth = 2F
                if (piece != leftPiece) {
                    paint.color = Color.BLACK
                } else {
                    paint.color = Color.TRANSPARENT
                }
                canvas.drawLine(
                    (start + col * ceilWidth).toFloat(),
                    (top + row * ceilHeight).toFloat(),
                    (start + col * ceilWidth).toFloat(),
                    (top + (row + 1) * ceilHeight).toFloat(),
                    paint
                )
                if (piece != rightPiece) {
                    paint.color = Color.BLACK
                } else {
                    paint.color = Color.TRANSPARENT
                }
                canvas.drawLine(
                    (start + (col + 1) * ceilWidth).toFloat(),
                    (top + row * ceilHeight).toFloat(),
                    (start + (col + 1) * ceilWidth).toFloat(),
                    (top + (row + 1) * ceilHeight).toFloat(),
                    paint
                )
                if (piece != topPiece) {
                    paint.color = Color.BLACK
                } else {
                    paint.color = Color.TRANSPARENT
                }
                canvas.drawLine(
                    (start + col * ceilWidth).toFloat(),
                    (top + row * ceilHeight).toFloat(),
                    (start + (col + 1) * ceilWidth).toFloat(),
                    (top + row * ceilHeight).toFloat(),
                    paint
                )
                if (piece != bottomPiece) {
                    paint.color = Color.BLACK
                } else {
                    paint.color = Color.TRANSPARENT
                }
                canvas.drawLine(
                    (start + col * ceilWidth).toFloat(),
                    (top + (row + 1) * ceilHeight).toFloat(),
                    (start + (col + 1) * ceilWidth).toFloat(),
                    (top + (row + 1) * ceilHeight).toFloat(),
                    paint
                )
            }
        }
    }

    private fun drawCeil(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, piece: PuzzlePiece?) {
        paint.color = piece.color
        canvas.drawRect(left, top, right, bottom, paint)
        paint.color = textColor
        paint.textSize = (right - left) / 3.5F
        val baseLine = (bottom + top - paint.fontMetrics.bottom - paint.fontMetrics.top) / 2
        if (piece == PuzzlePiece.DATE_M) {
            paint.color = textColor
            paint.textSize = (right - left) / 3.5F
            val text = month(puzzle.date.monthValue)
            val textWidth = paint.measureText(text)
            canvas.drawText(
                text,
                (left + right - textWidth) / 2,
                baseLine,
                paint
            )
        } else if (piece == PuzzlePiece.DATE_D) {
            val text = day(puzzle.date.dayOfMonth)
            val textWidth = paint.measureText(text)
            canvas.drawText(
                text,
                (left + right - textWidth) / 2,
                baseLine,
                paint
            )
        }
    }

    private val textColor = 0xFFE6CAB0.toInt()
    private val bgColor = 0XFF5A402F.toInt()
    private val bgHighlightColor = 0XFF865F43.toInt()

    private fun month(month: Int): String {
        return when (month) {
            1 -> "一月"
            2 -> "二月"
            3 -> "三月"
            4 -> "四月"
            5 -> "五月"
            6 -> "六月"
            7 -> "七月"
            8 -> "八月"
            9 -> "九月"
            10 -> "十月"
            11 -> "十一月"
            12 -> "十二月"
            else -> "未知"
        }
    }

    private fun day(day: Int): String {
        return day.toString()
    }

    private val PuzzlePiece?.color: Int
        get() {
            return when (this) {
                PuzzlePiece.L -> 0XFF779649.toInt()
                PuzzlePiece.J -> 0XFFFFEE6F.toInt()
                PuzzlePiece.P -> 0XFF6B798E.toInt()
                PuzzlePiece.N -> 0XFF007175.toInt()
                PuzzlePiece.V -> 0XFFA67EB7.toInt()
                PuzzlePiece.Z -> 0XFFF18F60.toInt()
                PuzzlePiece.G -> 0XFFE3EB98.toInt()
                PuzzlePiece.O -> 0XFFAB1D22.toInt()
                PuzzlePiece.EMPTY, PuzzlePiece.DATE_M, PuzzlePiece.DATE_D -> bgHighlightColor
                else -> bgColor
            }
        }

}