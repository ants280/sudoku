package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuValue;

public class SudokuUndoCellCommand implements Command
{
	private final SudokuUndoCell sudokuCell;
	private final SudokuCellChangeType sudokuCellChangeType;
	private final SudokuValue initialValue;
	private final SudokuValue updatedValue;

	public SudokuUndoCellCommand(
			SudokuUndoCell sudokuCell,
			SudokuCellChangeType sudokuCellChangeType,
			SudokuValue initialValue,
			SudokuValue updatedValue)
	{
		this.sudokuCell = sudokuCell;
		this.sudokuCellChangeType = sudokuCellChangeType;
		this.initialValue = initialValue;
		this.updatedValue = updatedValue;
	}

	@Override
	public void undo()
	{
		sudokuCell.getCommandHistory().setEnabled(false);
		sudokuCellChangeType.applyChange(sudokuCell, initialValue);
		sudokuCell.getCommandHistory().setEnabled(true);
	}

	@Override
	public void redo()
	{
		sudokuCell.getCommandHistory().setEnabled(false);
		sudokuCellChangeType.applyChange(sudokuCell, updatedValue);
		sudokuCell.getCommandHistory().setEnabled(true);
	}
}
