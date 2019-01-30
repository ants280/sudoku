package com.github.ants280.sudoku.ui;

public enum MoveDirection
{
	LEFT(-1, 0),
	RIGHT(1, 0),
	UP(0, -1),
	DOWN(0, 1);

	private final int dx;
	private final int dy;

	MoveDirection(int dx, int dy)
	{
		this.dx = dx;
		this.dy = dy;
	}

	public int getDx()
	{
		return dx;
	}

	public int getDy()
	{
		return dy;
	}
}
