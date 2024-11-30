package com.icarus.sgs.solve

import android.graphics.Point

enum class PuzzlePiece(val points: List<Point>) {
    L(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3), Point(1, 3))),
    J(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3), Point(1, 2))),
    G(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(1, 2), Point(1, 3))),
    N(listOf(Point(0, 0), Point(0, 1), Point(1, 0), Point(2, 0), Point(2, 1))),
    V(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(1, 0), Point(2, 0))),
    Z(listOf(Point(0, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(2, 2))),
    P(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(1, 0), Point(1, 1))),
    O(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(1, 0), Point(1, 1), Point(1, 2))),
    DATE_M(emptyList()),
    DATE_D(emptyList()),
    EMPTY(emptyList())
}