package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;

public class SudokuUndoBoard extends SudokuBoard
{
	private final CommandHistory<SudokuUndoCellCommand> commandHistory;

	public SudokuUndoBoard(CommandHistory<SudokuUndoCellCommand> commandHistory)
	{
		super();
		this.commandHistory = commandHistory;

		this.initCommandHistoryForSudokuCells();
	}

	private void initCommandHistoryForSudokuCells()
	{
		this.getAllSudokuCells()
				.stream()
				.forEach(sudokuCell -> ((SudokuUndoCell) sudokuCell)
				.setCommandHistory(commandHistory));
	}

	@Override
	protected SudokuCell createSudokuCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			SudokuValue cellValue,
			boolean locked)
	{
		return new SudokuUndoCell(
				rowIndex,
				columnIndex,
				groupIndex,
				cellValue,
				locked);
	}

	@Override
	public void resetFrom(SudokuBoard other)
	{
		super.resetFrom(other);

		commandHistory.reset();
	}
}
