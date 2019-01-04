package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.function.Consumer;

public abstract class SudokuSolverPlugin
{
	protected final SudokuBoard sudokuBoard;
	private final Consumer<String> moveDescriptionConsumer;

	public SudokuSolverPlugin(
			SudokuBoard sudokuBoard,
			Consumer<String> moveDescriptionConsumer)
	{
		this.sudokuBoard = sudokuBoard;
		this.moveDescriptionConsumer = moveDescriptionConsumer;
	}

	/**
	 * Make a single change to the SudokuBoard, if possible.
	 *
	 * @return True if a single change could be made to the SudokuBoard.
	 */
	public abstract boolean makeMove();

	protected void logMove(String moveDescription)
	{
		if (moveDescriptionConsumer != null)
		{
			moveDescriptionConsumer.accept(moveDescription);
		}
	}
}
