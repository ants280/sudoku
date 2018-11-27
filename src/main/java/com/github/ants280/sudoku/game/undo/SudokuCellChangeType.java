package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import java.util.function.BiConsumer;

public enum SudokuCellChangeType
{
	ADD_POSSIBLE_VALUE(
			(sudokuCell, oldValue) ->
	{
		if (oldValue != null)
		{
			sudokuCell.removePossibleValue(oldValue);
		}
	},
			(sudokuCell, newValue) -> sudokuCell.addPossibleValue(newValue)),
	REMOVE_POSSIBLE_VALUE(
			(sudokuCell, oldValue) ->
	{
		if (oldValue != null)
		{
			sudokuCell.addPossibleValue(oldValue);
		}
	},
			(sudokuCell, newValue) -> sudokuCell.removePossibleValue(newValue)),
	CHANGE_VALUE(
			(sudokuCell, oldValue) -> sudokuCell.setValue(oldValue),
			(sudokuCell, newValue) -> sudokuCell.setValue(newValue));

	private final BiConsumer<SudokuCell, Integer> undoCommand;
	private final BiConsumer<SudokuCell, Integer> redoCommand;

	SudokuCellChangeType(
			BiConsumer<SudokuCell, Integer> undoCommand,
			BiConsumer<SudokuCell, Integer> redoCommand)
	{
		this.undoCommand = undoCommand;
		this.redoCommand = redoCommand;
	}

	public void undo(SudokuCell sudokuCell, Integer oldValue)
	{
		undoCommand.accept(sudokuCell, oldValue);
	}

	public void redo(SudokuCell sudokuCell, Integer newValue)
	{
		redoCommand.accept(sudokuCell, newValue);
	}
}
