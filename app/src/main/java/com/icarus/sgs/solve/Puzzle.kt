package com.icarus.sgs.solve

import java.time.LocalDate

class Puzzle(val date: LocalDate) {

    constructor() : this(LocalDate.now())

    fun getSolved(row: Int, col: Int): PuzzlePiece? {
        val piece = grid[row][col]
        if (solved) return piece
        if (piece == PuzzlePiece.DATE_D || piece == PuzzlePiece.DATE_M || piece == PuzzlePiece.EMPTY) {
            return piece
        }
        return null
    }

    fun get(row: Int, col: Int): PuzzlePiece? {
        return grid[row][col]
    }

    fun put(row: Int, col: Int, value: PuzzlePiece?) {
        grid[row][col] = value
    }

    var solved = false

    val size = 7

    private val grid = Array(size) { Array<PuzzlePiece?>(size) { null } }

    init {
        grid[0][6] = PuzzlePiece.EMPTY
        grid[1][6] = PuzzlePiece.EMPTY
        grid[6][3] = PuzzlePiece.EMPTY
        grid[6][4] = PuzzlePiece.EMPTY
        grid[6][5] = PuzzlePiece.EMPTY
        grid[6][6] = PuzzlePiece.EMPTY
        val month = date.monthValue
        val day = date.dayOfMonth
        val monthX = (month - 1) / 6
        val monthY = (month - 1) % 6
        val dayX = (day - 1) / 7 + 2
        val dayY = (day - 1) % 7
        grid[monthX][monthY] = PuzzlePiece.DATE_M
        grid[dayX][dayY] = PuzzlePiece.DATE_D
    }

}