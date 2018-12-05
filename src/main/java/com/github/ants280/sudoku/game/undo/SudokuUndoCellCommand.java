package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Objects;

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

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 71 * hash + Objects.hashCode(this.sudokuCell);
		hash = 71 * hash + Objects.hashCode(this.sudokuCellChangeType);
		hash = 71 * hash + Objects.hashCode(this.initialValue);
		hash = 71 * hash + Objects.hashCode(this.updatedValue);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}

		final SudokuUndoCellCommand other = (SudokuUndoCellCommand) obj;
		return Objects.equals(this.sudokuCell, other.sudokuCell)
				&& this.sudokuCellChangeType == other.sudokuCellChangeType
				&& this.initialValue == other.initialValue
				&& this.updatedValue == other.updatedValue;
	}
}
