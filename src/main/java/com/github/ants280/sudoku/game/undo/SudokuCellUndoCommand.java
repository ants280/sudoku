package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuEvent;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Objects;

public class SudokuCellUndoCommand implements Command
{
	private final SudokuEvent<SudokuCell, SudokuValue> cellValueChangedEvent;
	private final SudokuCellChangeType sudokuCellChangeType;

	public SudokuCellUndoCommand(
			SudokuEvent<SudokuCell, SudokuValue> cellValueChangedEvent,
			SudokuCellChangeType sudokuCellChangeType)
	{
		this.cellValueChangedEvent = cellValueChangedEvent;
		this.sudokuCellChangeType = sudokuCellChangeType;
	}

	public SudokuCell getSudokuCell()
	{
		return cellValueChangedEvent.getSource();
	}

	@Override
	public void undo()
	{
		sudokuCellChangeType.applyChange(
				cellValueChangedEvent.getSource(),
				cellValueChangedEvent.getOldValue());
	}

	@Override
	public void redo()
	{
		sudokuCellChangeType.applyChange(
				cellValueChangedEvent.getSource(),
				cellValueChangedEvent.getNewValue());
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 71 * hash + Objects.hashCode(this.sudokuCellChangeType);
		hash = 71 * hash + Objects.hashCode(this.cellValueChangedEvent);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj
				|| obj != null
				&& this.getClass() == obj.getClass()
				&& this.sudokuCellChangeType == ((SudokuCellUndoCommand) obj).sudokuCellChangeType
				&& Objects.equals(this.cellValueChangedEvent,
						((SudokuCellUndoCommand) obj).cellValueChangedEvent);
	}
}
