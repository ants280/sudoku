package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;

public class SudokuCellChangeCommand implements Command
{
	private final SudokuCell sudokuCell;
	private final SudokuCellChangeType sudokuCellChangeType;
	private final Integer oldValue;
	private final Integer newValue;

	public SudokuCellChangeCommand(
			SudokuCell sudokuCell,
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
		sudokuCellChangeType.undo(sudokuCell, oldValue);
	}

	@Override
	public void redo()
	{
		sudokuCellChangeType.redo(sudokuCell, newValue);
	}
}
