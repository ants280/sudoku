package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuValue;
import org.junit.Assert;
import org.junit.Test;

public class SudokuUndoCellCommandTest
{
	@Test
	public void testUndo_notLoggedToCommandHistory()
	{
		// TODO
	}

	@Test
	public void testRedo_notLoggedToCommandHistory()
	{
		// TODO
	}

	@Test
	public void testHashCode_same()
	{
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_4, true);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);

		int hashCode1 = command1.hashCode();
		int hashCode2 = command2.hashCode();

		Assert.assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testHashCode_different()
	{
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_4, true);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_3, SudokuValue.VALUE_2);

		int hashCode1 = command1.hashCode();
		int hashCode2 = command2.hashCode();

		Assert.assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void testEquals_same()
	{
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_4, true);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);

		boolean equals = command1.equals(command2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_different()
	{
		SudokuUndoCell sudokuUndoCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_4, true);
		SudokuUndoCellCommand command1 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_2);
		SudokuUndoCellCommand command2 = new SudokuUndoCellCommand(sudokuUndoCell, SudokuCellChangeType.SET_VALUE, SudokuValue.VALUE_1, SudokuValue.VALUE_3);

		boolean equals = command1.equals(command2);

		Assert.assertFalse(equals);
	}

}
