package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import java.util.Objects;

public class SudokuUndoCell extends SudokuCell
{
	private CommandHistory<SudokuUndoCellCommand> commandHistory;

	public SudokuUndoCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			Integer value,
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
	public void setValue(Integer value)
	{
		Integer oldValue = this.getValue();

		super.setValue(value);

		commandHistory.addCommand(new SudokuUndoCellCommand(
				this,
				SudokuCellChangeType.SET_VALUE,
				oldValue,
				value));
	}

	@Override
	public void togglePossibleValue(int value)
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
		final SudokuUndoCell other = (SudokuUndoCell) obj;
		return Objects.equals(this.commandHistory, other.commandHistory);
	}

}
