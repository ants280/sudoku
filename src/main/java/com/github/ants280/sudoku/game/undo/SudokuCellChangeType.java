package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;

public enum SudokuCellChangeType
{
	ADD_POSSIBLE_VALUE(
			(sudokuCell, oldValue, newValue) ->
	{
		if (oldValue == null)
		{
			sudokuCell.removePossibleValue(newValue);
		}
	},
			(sudokuCell, oldValue, newValue) -> sudokuCell.addPossibleValue(newValue)),
	REMOVE_POSSIBLE_VALUE(
			(sudokuCell, oldValue, newValue) ->
	{
		if (oldValue == null)
		{
			sudokuCell.addPossibleValue(newValue);
		}
	},
			(sudokuCell, oldValue, newValue) -> sudokuCell.removePossibleValue(newValue)),
	CHANGE_VALUE(
			(sudokuCell, oldValue, newValue) -> sudokuCell.setValue(oldValue),
			(sudokuCell, oldValue, newValue) -> sudokuCell.setValue(newValue));

	private final SudokuCellChange undoCommand;
	private final SudokuCellChange redoCommand;

	SudokuCellChangeType(
			SudokuCellChange undoCommand,
			SudokuCellChange redoCommand)
	{
		this.undoCommand = undoCommand;
		this.redoCommand = redoCommand;
	}

	public void undo(SudokuCell sudokuCell, Integer oldValue, Integer newValue)
	{
		undoCommand.changeCell(sudokuCell, oldValue, newValue);
	}

	public void redo(SudokuCell sudokuCell, Integer oldValue, Integer newValue)
	{
		redoCommand.changeCell(sudokuCell, oldValue, newValue);
	}

	private static interface SudokuCellChange
	{
		void changeCell(SudokuCell cell, Integer oldValue, Integer newValue);
	}
}
