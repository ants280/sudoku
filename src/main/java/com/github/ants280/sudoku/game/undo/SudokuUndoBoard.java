package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;

public class SudokuUndoBoard extends SudokuBoard
{
	public SudokuUndoBoard(CommandHistory<SudokuUndoCellCommand> commandHistory)
	{
		super();

		this.initCommandHistoryForSudokuCells(commandHistory);
	}

	private void initCommandHistoryForSudokuCells(
			CommandHistory<SudokuUndoCellCommand> commandHistory)
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
			Integer cellValue,
			boolean locked)
	{
		return new SudokuUndoCell(
				rowIndex,
				columnIndex,
				groupIndex,
				cellValue,
				locked);
	}
}
