package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;

public class SudokuUndoBoard extends SudokuBoard
{
	private final CommandHistory<SudokuUndoCellCommand> commandHistory;

	public SudokuUndoBoard(CommandHistory<SudokuUndoCellCommand> commandHistory)
	{
		super();

		this.commandHistory = commandHistory;
	}

	@Override
	protected SudokuCell createSudokuCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			Integer cellValue,
			boolean locked)
	{
		return new SudokuUndoCell(
				commandHistory,
				rowIndex,
				columnIndex,
				groupIndex,
				cellValue,
				locked);
	}
}
