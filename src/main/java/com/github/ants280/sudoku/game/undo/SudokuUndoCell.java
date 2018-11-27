package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;

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
}
