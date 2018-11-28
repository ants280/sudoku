package com.github.ants280.sudoku.game.undo;

public class SudokuUndoCellCommand implements Command
{
	private final SudokuUndoCell sudokuCell;
	private final SudokuCellChangeType sudokuCellChangeType;
	private final Integer initialValue;
	private final Integer updatedValue;

	public SudokuUndoCellCommand(
			SudokuUndoCell sudokuCell,
			SudokuCellChangeType sudokuCellChangeType,
			Integer initialValue,
			Integer updatedValue)
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
