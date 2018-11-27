package com.github.ants280.sudoku.game.undo;

public class SudokuCellChangeCommand implements Command
{
	private final SudokuUndoCell sudokuCell;
	private final SudokuCellChangeType sudokuCellChangeType;
	private final Integer oldValue;
	private final Integer newValue;

	public SudokuCellChangeCommand(
			SudokuUndoCell sudokuCell,
			SudokuCellChangeType sudokuCellChangeType,
			Integer oldValue,
			Integer newValue)
	{
		this.sudokuCell = sudokuCell;
		this.sudokuCellChangeType = sudokuCellChangeType;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public void undo()
	{
		sudokuCell.getCommandHistory().setEnabled(false);
		sudokuCellChangeType.undo(sudokuCell, oldValue);
		sudokuCell.getCommandHistory().setEnabled(true);
	}

	@Override
	public void redo()
	{
		sudokuCell.getCommandHistory().setEnabled(false);
		sudokuCellChangeType.redo(sudokuCell, newValue);
		sudokuCell.getCommandHistory().setEnabled(true);
	}
}
