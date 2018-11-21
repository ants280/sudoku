package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.util.function.BiConsumer;

public abstract class SudokuSolverPlugin
{
	protected final SudokuBoard sudokuBoard;
	protected final BiConsumer<SudokuCell, Integer> setValueConsumer;
	protected final BiConsumer<SudokuCell, Integer> toggleSudokuCellPossibleValue;

	public SudokuSolverPlugin(
			SudokuBoard sudokuBoard,
			BiConsumer<SudokuCell, Integer> setValueConsumer,
			BiConsumer<SudokuCell, Integer> toggleSudokuCellPossibleValue)
	{
		this.sudokuBoard = sudokuBoard;
		this.setValueConsumer = setValueConsumer;
		this.toggleSudokuCellPossibleValue = toggleSudokuCellPossibleValue;
	}

	/**
	 * Make a single change to the SudokuBoard, if possible.
	 *
	 * @return True if a single change could be made to the SudokuBoard.
	 */
	public abstract boolean makeMove();
}
