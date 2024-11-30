package com.icarus.sgs.solve

import android.content.Context
import org.json.JSONArray

/**
 * 使用查表的方式快速求解
 */
object FastSolver {

    private val solves = mutableListOf<Solve>()
    private val isInitialized: Boolean get() = solves.isNotEmpty()

    @Synchronized
    fun initSolver(context: Context) {
        if (isInitialized) return
        val solvesJson = context.assets.open("solves.json")
            .bufferedReader(Charsets.UTF_8)
            .use {
                it.readText()
            }
        val solves = JSONArray(solvesJson)
        for (i in 0 until solves.length()) {
            val solveJson = solves.getJSONObject(i)
            val m = solveJson.getInt("M")
            val d = solveJson.getInt("D")
            val result = Array(7) { Array(7) { PuzzlePiece.EMPTY } }
            for (row in 0 until 7) {
                for (col in 0 until 7) {
                    val piece = solveJson.getJSONObject("solve").getString("$row,$col")
                    result[row][col] = PuzzlePiece.valueOf(piece)
                }
            }
            FastSolver.solves.add(Solve(m, d, result))
        }
    }

    fun solve(puzzle: Puzzle) {
        solves.find {
            it.m == puzzle.date.monthValue && it.d == puzzle.date.dayOfMonth
        }?.let { solve ->
            for (row in 0 until 7) {
                for (col in 0 until 7) {
                    puzzle.put(row, col, solve.solve[row][col])
                }
            }
            puzzle.solved = true
        }
    }

    private class Solve(val m: Int, val d: Int, val solve: Array<Array<PuzzlePiece>>)

}