package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuEvent;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Objects;

// TODO: Rename class to SudokuCellUndoCommand
public class SudokuUndoCellCommand extends SudokuEvent<SudokuCell, SudokuValue> implements Command
{
	private final SudokuCellChangeType sudokuCellChangeType;

	public SudokuUndoCellCommand(
			SudokuCell sudokuCell,
			SudokuCellChangeType sudokuCellChangeType,
			SudokuValue initialValue,
			SudokuValue updatedValue)
	{
		super(sudokuCell, initialValue, updatedValue);
		this.sudokuCellChangeType = sudokuCellChangeType;
	}

	public SudokuCellChangeType getSudokuCellChangeType()
	{
		return sudokuCellChangeType;
	}

	@Override
	public void undo()
	{
		sudokuCellChangeType.applyChange(this.getSource(), this.getOldValue());
	}

	@Override
	public void redo()
	{
		sudokuCellChangeType.applyChange(this.getSource(), this.getNewValue());
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 71 * hash + Objects.hashCode(this.sudokuCellChangeType);
		hash = 71 * hash + super.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj
				|| obj != null
				&& this.getClass() == obj.getClass()
				&& this.sudokuCellChangeType == ((SudokuUndoCellCommand) obj).sudokuCellChangeType
				&& super.equals(obj);
	}
}
