package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuValue;
import org.junit.Assert;
import org.junit.Test;

public class SudokuUndoCellCommandTest
{
	@Test
	public void testUndo_notLoggedToCommandHistory()
	{
		CommandHistory<SudokuUndoCellCommand> commandHistory = new CommandHistory<>(null);
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_3, false);
		sudokuUndoCell.setCommandHistory(commandHistory);
		SudokuUndoCellCommand command = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);

		command.undo();

		Assert.assertEquals(0, commandHistory.getUndoCount());
		Assert.assertEquals(SudokuValue.VALUE_1, sudokuUndoCell.getValue());
	}

	@Test
	public void testRedo_notLoggedToCommandHistory()
	{
		CommandHistory<SudokuUndoCellCommand> commandHistory = new CommandHistory<>(null);
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_3, false);
		sudokuUndoCell.setCommandHistory(commandHistory);
		SudokuUndoCellCommand command = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);

		command.undo();
		command.redo();

		Assert.assertEquals(0, commandHistory.getUndoCount());
		Assert.assertEquals(SudokuValue.VALUE_2, sudokuUndoCell.getValue());
	}

	@Test
	public void testHashCode_same()
	{
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);

		int hashCode1 = command1.hashCode();
		int hashCode2 = command2.hashCode();

		Assert.assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testHashCode_different()
	{
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_3);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_2, SudokuValue.VALUE_3);
		int hashCode1 = command1.hashCode();
		int hashCode2 = command2.hashCode();

		Assert.assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void testEquals_same()
	{
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = command1;

		boolean equals = command1.equals(command2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_eq()
	{
		SudokuUndoCell sudokuUndoCell1 = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuUndoCell sudokuUndoCell2 = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell1, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell2, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);

		boolean equals = command1.equals(command2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_differentSudokuCell()
	{
		SudokuUndoCell sudokuUndoCell1 = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_1, false);
		SudokuUndoCell sudokuUndoCell2 = new SudokuUndoCell(2, 1, 3, SudokuValue.VALUE_1, false);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell1, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell2, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_changeType()
	{
		SudokuUndoCell sudokuUndoCell1 = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuUndoCell sudokuUndoCell2 = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell1, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_3, SudokuValue.VALUE_4);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell2, SudokuCellChangeType.TOGGLE_POSSIBLE_VALUE, SudokuValue.VALUE_3, SudokuValue.VALUE_4);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentInitialValue()
	{
		SudokuUndoCell sudokuUndoCell1 = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuUndoCell sudokuUndoCell2 = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_7, false);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell1, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_3, SudokuValue.VALUE_4);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell2, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_7, SudokuValue.VALUE_4);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentUpdatedValue()
	{
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_3, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_3, SudokuValue.VALUE_4);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}
}
