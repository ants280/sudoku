package com.github.ants280.sudoku.game.undo;

import org.junit.Assert;
import org.junit.Test;

public class SudokuUndoBoardTest
{
	@Test
	public void testCreateSudokuCell()
	{
		SudokuUndoBoard sudokuUndoBoard = new SudokuUndoBoard(null);

		sudokuUndoBoard.getAllSudokuCells()
				.forEach(sudokuCell -> Assert.assertTrue(
				sudokuCell instanceof SudokuUndoCell));
	}

	@Test
	public void testCreateSudokuCell_initCommandHistoryForSudokuCells()
	{
		CommandHistory<SudokuUndoCellCommand> commandHistory
				= new CommandHistory<>(null);
		SudokuUndoBoard sudokuUndoBoard = new SudokuUndoBoard(commandHistory);

		sudokuUndoBoard.getAllSudokuCells()
				.forEach(sudokuCell -> Assert.assertSame(
				commandHistory,
				((SudokuUndoCell) sudokuCell).getCommandHistory()));
	}
}
