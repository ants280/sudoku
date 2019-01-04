package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Objects;

public class SudokuUndoCellCommand implements Command
{
	private final SudokuCell sudokuCell;
	private final SudokuCellChangeType sudokuCellChangeType;
	private final SudokuValue initialValue;
	private final SudokuValue updatedValue;

	public SudokuUndoCellCommand(
			SudokuCell sudokuCell,
			SudokuCellChangeType sudokuCellChangeType,
			SudokuValue initialValue,
			SudokuValue updatedValue)
	{
		this.sudokuCell = sudokuCell;
		this.sudokuCellChangeType = sudokuCellChangeType;
		this.initialValue = initialValue;
		this.updatedValue = updatedValue;
	}

	public SudokuCell getSudokuCell()
	{
		return sudokuCell;
	}

	public SudokuCellChangeType getSudokuCellChangeType()
	{
		return sudokuCellChangeType;
	}

	@Override
	public void undo()
	{
		sudokuCellChangeType.applyChange(sudokuCell, initialValue);
	}

	@Override
	public void redo()
	{
		sudokuCellChangeType.applyChange(sudokuCell, updatedValue);
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
		return this == obj
				|| obj != null
				&& this.getClass() == obj.getClass()
				&& this.sudokuCellChangeType == ((SudokuUndoCellCommand) obj).sudokuCellChangeType
				&& this.initialValue == ((SudokuUndoCellCommand) obj).initialValue
				&& this.updatedValue == ((SudokuUndoCellCommand) obj).updatedValue
				&& Objects.equals(this.sudokuCell, ((SudokuUndoCellCommand) obj).sudokuCell);
	}
}
