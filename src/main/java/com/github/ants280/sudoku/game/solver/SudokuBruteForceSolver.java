package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;

public class SudokuBruteForceSolver
{
	private final SudokuBoard sudokuBoard;

	public SudokuBruteForceSolver(SudokuBoard sudokuBoard)
	{
		this.sudokuBoard = sudokuBoard;
	}

	public SudokuBoard getSolvedSudokuBoard()
	{
		return sudokuBoard;
	}
}
