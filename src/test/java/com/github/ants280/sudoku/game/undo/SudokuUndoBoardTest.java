package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.function.BiConsumer;
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

	@Test
	public void testResetFrom_undoHistoryCleared()
	{
		BiConsumer<Boolean, Boolean> undoRedoEmptyConsumer = (undoEmpty, redoEmpty) ->
		{
		};
		CommandHistory<SudokuUndoCellCommand> commandHistory
				= new CommandHistory<>(undoRedoEmptyConsumer);
		SudokuUndoBoard sudokuUndoBoard = new SudokuUndoBoard(commandHistory);

		sudokuUndoBoard.getAllSudokuCells()
				.get(0)
				.setValue(SudokuValue.VALUE_1);
		sudokuUndoBoard.resetFrom(new SudokuBoard());

		Assert.assertEquals(0, commandHistory.getUndoCount());
	}
}
