package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Objects;

public class SudokuUndoCell extends SudokuCell
{
	private CommandHistory<SudokuUndoCellCommand> commandHistory;

	public SudokuUndoCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			SudokuValue value,
			boolean locked)
	{
		super(rowIndex, columnIndex, groupIndex, value, locked);
		this.commandHistory = null;
	}

	public CommandHistory<SudokuUndoCellCommand> getCommandHistory()
	{
		return commandHistory;
	}

	public void setCommandHistory(
			CommandHistory<SudokuUndoCellCommand> commandHistory)
	{
		this.commandHistory = commandHistory;
	}

	@Override
	public void setValue(SudokuValue value)
	{
		SudokuValue oldValue = this.getValue();

		super.setValue(value);

		commandHistory.addCommand(new SudokuUndoCellCommand(
				this,
				SudokuCellChangeType.SET_VALUE,
				oldValue,
				value));
	}

	@Override
	public void togglePossibleValue(SudokuValue value)
	{
		super.togglePossibleValue(value);

		commandHistory.addCommand(new SudokuUndoCellCommand(
				this,
				SudokuCellChangeType.TOGGLE_POSSIBLE_VALUE,
				value,
				value));
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 31 * hash + Objects.hashCode(this.commandHistory);
		hash = 31 * hash + super.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj
				|| obj != null
				&& this.getClass() == obj.getClass()
				&& Objects.equals(
						this.commandHistory,
						((SudokuUndoCell) obj).commandHistory)
				&& super.equals(obj);
	}
}
