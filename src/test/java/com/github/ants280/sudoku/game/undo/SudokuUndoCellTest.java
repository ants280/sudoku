package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuValue;
import org.junit.Assert;
import org.junit.Test;

public class SudokuUndoCellTest
{
	@Test
	public void testEquals_sameObject()
	{
		SudokuUndoCell sudokuCell = new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_4, true);

		boolean equals = sudokuCell.equals(sudokuCell);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_sameDataAndCommandHistory()
	{
		SudokuUndoCell sudokuCell1
				= new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_4, true);
		SudokuUndoCell sudokuCell2
				= new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_4, true);

		CommandHistory<SudokuUndoCellCommand> commandHistory
				= new CommandHistory<>(null);

		sudokuCell1.setCommandHistory(commandHistory);
		sudokuCell2.setCommandHistory(commandHistory);

		boolean equals = sudokuCell1.equals(sudokuCell2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_differentData()
	{
		SudokuUndoCell sudokuCell1
				= new SudokuUndoCell(1, 2, 3, SudokuValue.VALUE_4, true);
		SudokuUndoCell sudokuCell2
				= new SudokuUndoCell(5, 6, 7, SudokuValue.VALUE_8, true);

		boolean equals = sudokuCell1.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}
}
