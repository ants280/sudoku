package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;

public class SudokuBruteForceSolver extends SudokuSolver
{
	public SudokuBruteForceSolver(SudokuBoard sudokuBoard)
	{
		super(sudokuBoard);
	}

	@Override
	public boolean makeMove()
	{
		return false;
	}

	@Override
	public void solveFast()
	{
		super.initialize();
	}
}
