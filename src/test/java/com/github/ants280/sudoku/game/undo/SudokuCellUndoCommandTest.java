package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuEvent;
import com.github.ants280.sudoku.game.SudokuValue;
import org.junit.Assert;
import org.junit.Test;

public class SudokuCellUndoCommandTest
{
	@Test
	public void testUndo_notLoggedToCommandHistory()
	{
		CommandHistory<SudokuCellUndoCommand> commandHistory = new CommandHistory<>();
		SudokuCell sudokuUndoCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuCellUndoCommand command = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_1, SudokuValue.VALUE_2), SudokuCellChangeType.SET_VALUE);

		command.undo();

		Assert.assertEquals(0, commandHistory.getUndoCount());
		Assert.assertEquals(SudokuValue.VALUE_1, sudokuUndoCell.getValue());
	}

	@Test
	public void testRedo_notLoggedToCommandHistory()
	{
		CommandHistory<SudokuCellUndoCommand> commandHistory = new CommandHistory<>();
		SudokuCell sudokuUndoCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuCellUndoCommand command = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_1, SudokuValue.VALUE_2), SudokuCellChangeType.SET_VALUE);

		command.undo();
		command.redo();

		Assert.assertEquals(0, commandHistory.getUndoCount());
		Assert.assertEquals(SudokuValue.VALUE_2, sudokuUndoCell.getValue());
	}

	@Test
	public void testHashCode_same()
	{
		SudokuCell sudokuUndoCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_1, SudokuValue.VALUE_2), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_1, SudokuValue.VALUE_2), SudokuCellChangeType.SET_VALUE);

		int hashCode1 = command1.hashCode();
		int hashCode2 = command2.hashCode();
		command1.equals(command2);

		Assert.assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testHashCode_different()
	{
		SudokuCell sudokuUndoCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_1, SudokuValue.VALUE_3), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_2, SudokuValue.VALUE_3), SudokuCellChangeType.SET_VALUE);
		int hashCode1 = command1.hashCode();
		int hashCode2 = command2.hashCode();

		Assert.assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void testEquals_same()
	{
		SudokuCell sudokuUndoCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_2, SudokuValue.VALUE_1), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = command1;

		boolean equals = command1.equals(command2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_eq()
	{
		SudokuCell sudokuUndoCell1 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuCell sudokuUndoCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell1, SudokuValue.VALUE_2, SudokuValue.VALUE_1), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell2, SudokuValue.VALUE_2, SudokuValue.VALUE_1), SudokuCellChangeType.SET_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_null()
	{
		SudokuCell sudokuUndoCell1 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell1, SudokuValue.VALUE_2, SudokuValue.VALUE_1), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = null;

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_wrongClass()
	{
		SudokuCell sudokuUndoCell1 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell1, SudokuValue.VALUE_2, SudokuValue.VALUE_1), SudokuCellChangeType.SET_VALUE);
		Object command2 = 1L;

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentSudokuCell()
	{
		SudokuCell sudokuUndoCell1 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_1, false);
		SudokuCell sudokuUndoCell2 = new SudokuCell(2, 1, 3, SudokuValue.VALUE_1, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell1, SudokuValue.VALUE_1, SudokuValue.VALUE_2), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell2, SudokuValue.VALUE_1, SudokuValue.VALUE_2), SudokuCellChangeType.SET_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_changeType()
	{
		SudokuCell sudokuUndoCell1 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuCell sudokuUndoCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell1, SudokuValue.VALUE_3, SudokuValue.VALUE_4), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell2, SudokuValue.VALUE_3, SudokuValue.VALUE_4), SudokuCellChangeType.TOGGLE_POSSIBLE_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentInitialValue()
	{
		SudokuCell sudokuUndoCell1 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuCell sudokuUndoCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_7, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell1, SudokuValue.VALUE_3, SudokuValue.VALUE_4), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell2, SudokuValue.VALUE_7, SudokuValue.VALUE_4), SudokuCellChangeType.SET_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentUpdatedValue()
	{
		SudokuCell sudokuUndoCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_3, false);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_3, SudokuValue.VALUE_2), SudokuCellChangeType.SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(new SudokuEvent<>(sudokuUndoCell, SudokuValue.VALUE_3, SudokuValue.VALUE_4), SudokuCellChangeType.SET_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testGetSudokuCell()
	{
		SudokuCell expectedSudokuCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_2, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent
				= new SudokuEvent<>(expectedSudokuCell, SudokuValue.VALUE_3, SudokuValue.VALUE_2);
		SudokuCellUndoCommand sudokuCellUndoCommand
				= new SudokuCellUndoCommand(sudokuEvent, SudokuCellChangeType.SET_VALUE);

		SudokuCell actualSudokuCell = sudokuCellUndoCommand.getSudokuCell();

		Assert.assertEquals(expectedSudokuCell, actualSudokuCell);
	}
}
