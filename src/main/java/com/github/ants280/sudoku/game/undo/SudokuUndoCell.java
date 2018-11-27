package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;

public class SudokuUndoCell extends SudokuCell
{
	private final CommandHistory<SudokuCellChangeCommand> commandHistory;

	public SudokuUndoCell(
			CommandHistory<SudokuCellChangeCommand> commandHistory,
			int rowIndex,
			int columnIndex,
			int groupIndex,
			Integer value,
			boolean locked)
	{
		super(rowIndex, columnIndex, groupIndex, value, locked);
		this.commandHistory = commandHistory;
	}

	public CommandHistory<SudokuCellChangeCommand> getCommandHistory()
	{
		return commandHistory;
	}

	@Override
	public void setValue(Integer value)
	{
		Integer oldValue = this.getValue();

		super.setValue(value);

		commandHistory.addCommand(new SudokuCellChangeCommand(
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

		commandHistory.addCommand(new SudokuCellChangeCommand(
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

		commandHistory.addCommand(new SudokuCellChangeCommand(
				this,
				SudokuCellChangeType.ADD_POSSIBLE_VALUE,
				oldPossibleValuePresent ? value : null,
				value));

		return valueAdded;
	}
}
