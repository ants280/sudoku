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
				SudokuCellChangeType.CHANGE_VALUE,
				oldValue,
				value));
	}

	@Override
	public boolean removePossibleValue(int value)
	{
		boolean oldPossibleValuePresent
				= this.getPossibleValues().contains(value);

		boolean valueRemoved = super.removePossibleValue(value);

		commandHistory.addCommand(new SudokuUndoCellCommand(
				this,
				SudokuCellChangeType.REMOVE_POSSIBLE_VALUE,
				oldPossibleValuePresent ? value : null,
				value));

		return valueRemoved;
	}

	@Override
	public boolean addPossibleValue(int value)
	{
		boolean oldPossibleValuePresent
				= this.getPossibleValues().contains(value);

		boolean valueAdded = super.addPossibleValue(value);

		commandHistory.addCommand(new SudokuUndoCellCommand(
				this,
				SudokuCellChangeType.ADD_POSSIBLE_VALUE,
				oldPossibleValuePresent ? value : null,
				value));

		return valueAdded;
	}
}
