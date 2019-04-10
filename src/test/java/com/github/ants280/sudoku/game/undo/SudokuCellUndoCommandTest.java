package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuEvent;
import com.github.ants280.sudoku.game.SudokuValue;
import static com.github.ants280.sudoku.game.SudokuValue.*;
import static com.github.ants280.sudoku.game.undo.SudokuCellChangeType.*;
import org.junit.Assert;
import org.junit.Test;

public class SudokuCellUndoCommandTest
{
	@Test
	public void testUndo_notLoggedToCommandHistory()
	{
		CommandHistory<SudokuCellUndoCommand> commandHistory = new CommandHistory<>();
		SudokuCell sudokuCell = new SudokuCell(1, 2, 3, VALUE_3, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent = new SudokuEvent<>(sudokuCell, VALUE_1, VALUE_2);
		SudokuCellUndoCommand command = new SudokuCellUndoCommand(sudokuEvent, SET_VALUE);

		command.undo();

		Assert.assertEquals(0, commandHistory.getUndoCount());
		Assert.assertEquals(VALUE_1, sudokuCell.getValue());
	}

	@Test
	public void testRedo_notLoggedToCommandHistory()
	{
		CommandHistory<SudokuCellUndoCommand> commandHistory = new CommandHistory<>();
		SudokuCell sudokuCell = new SudokuCell(1, 2, 3, VALUE_3, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent = new SudokuEvent<>(sudokuCell, VALUE_1, VALUE_2);
		SudokuCellUndoCommand command = new SudokuCellUndoCommand(sudokuEvent, SET_VALUE);

		command.undo();
		command.redo();

		Assert.assertEquals(0, commandHistory.getUndoCount());
		Assert.assertEquals(VALUE_2, sudokuCell.getValue());
	}

	@Test
	public void testHashCode_same()
	{
		SudokuCell sudokuCell = new SudokuCell(1, 2, 3, VALUE_2, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell, VALUE_1, VALUE_2);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent2 = new SudokuEvent<>(sudokuCell, VALUE_1, VALUE_2);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(sudokuEvent2, SET_VALUE);

		int hashCode1 = command1.hashCode();
		int hashCode2 = command2.hashCode();

		Assert.assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testHashCode_different()
	{
		SudokuCell sudokuCell = new SudokuCell(1, 2, 3, VALUE_3, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell, VALUE_1, VALUE_3);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent2 = new SudokuEvent<>(sudokuCell, VALUE_2, VALUE_3);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(sudokuEvent2, SET_VALUE);
		int hashCode1 = command1.hashCode();
		int hashCode2 = command2.hashCode();

		Assert.assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void testEquals_same()
	{
		SudokuCell sudokuCell = new SudokuCell(1, 2, 3, VALUE_2, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell, VALUE_2, VALUE_1);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = command1;

		boolean equals = command1.equals(command2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_eq()
	{
		SudokuCell sudokuCell1 = new SudokuCell(1, 2, 3, VALUE_2, false);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, VALUE_2, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell1, VALUE_2, VALUE_1);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent2 = new SudokuEvent<>(sudokuCell2, VALUE_2, VALUE_1);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(sudokuEvent2, SET_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_null()
	{
		SudokuCell sudokuCell1 = new SudokuCell(1, 2, 3, VALUE_2, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell1, VALUE_2, VALUE_1);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = null;

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_wrongClass()
	{
		SudokuCell sudokuCell1 = new SudokuCell(1, 2, 3, VALUE_2, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell1, VALUE_2, VALUE_1);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		Object command2 = 1L;

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentSudokuCell()
	{
		SudokuCell sudokuCell1 = new SudokuCell(1, 2, 3, VALUE_1, false);
		SudokuCell sudokuCell2 = new SudokuCell(2, 1, 3, VALUE_1, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell1, VALUE_1, VALUE_2);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent2 = new SudokuEvent<>(sudokuCell2, VALUE_1, VALUE_2);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(sudokuEvent2, SET_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_changeType()
	{
		SudokuCell sudokuCell1 = new SudokuCell(1, 2, 3, VALUE_3, false);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, VALUE_3, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell1, VALUE_3, VALUE_4);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent2 = new SudokuEvent<>(sudokuCell2, VALUE_3, VALUE_4);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(sudokuEvent2, TOGGLE_POSSIBLE_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentInitialValue()
	{
		SudokuCell sudokuCell1 = new SudokuCell(1, 2, 3, VALUE_3, false);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, VALUE_7, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell1, VALUE_3, VALUE_4);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent2 = new SudokuEvent<>(sudokuCell2, VALUE_7, VALUE_4);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(sudokuEvent2, SET_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentUpdatedValue()
	{
		SudokuCell sudokuCell = new SudokuCell(1, 2, 3, VALUE_3, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent1 = new SudokuEvent<>(sudokuCell, VALUE_3, VALUE_2);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent2 = new SudokuEvent<>(sudokuCell, VALUE_3, VALUE_4);
		SudokuCellUndoCommand command1 = new SudokuCellUndoCommand(sudokuEvent1, SET_VALUE);
		SudokuCellUndoCommand command2 = new SudokuCellUndoCommand(sudokuEvent2, SET_VALUE);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testGetSudokuCell()
	{
		SudokuCell expectedSudokuCell = new SudokuCell(1, 2, 3, VALUE_2, false);
		SudokuEvent<SudokuCell, SudokuValue> sudokuEvent
				= new SudokuEvent<>(expectedSudokuCell, VALUE_3, VALUE_2);
		SudokuCellUndoCommand sudokuCellUndoCommand
				= new SudokuCellUndoCommand(sudokuEvent, SET_VALUE);

		SudokuCell actualSudokuCell = sudokuCellUndoCommand.getSudokuCell();

		Assert.assertEquals(expectedSudokuCell, actualSudokuCell);
	}
}
