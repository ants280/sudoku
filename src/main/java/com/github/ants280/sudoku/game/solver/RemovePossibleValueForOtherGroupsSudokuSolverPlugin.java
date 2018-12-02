package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;

public class RemovePossibleValueForOtherGroupsSudokuSolverPlugin
		extends SudokuSolverPlugin
{
	public RemovePossibleValueForOtherGroupsSudokuSolverPlugin(
			SudokuBoard sudokuBoard)
	{
		super(sudokuBoard);
	}

	@Override
	public boolean makeMove()
	{
		return false;
	}
}
