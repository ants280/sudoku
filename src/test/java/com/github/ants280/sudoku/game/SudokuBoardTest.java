package com.github.ants280.sudoku.game;

import static com.github.ants280.sudoku.game.SectionType.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class SudokuBoardTest
{
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidBoardSize()
	{
		SudokuBoard board = new SudokuBoard("{123456789}");

		Assert.fail("The following sudkuBoard should be invalid "
				+ "because it is too small: " + board);
	}

	@Test
	public void testToString()
	{
		String boardValue
				= "{123456789"
				+ "456789123"
				+ "789123456"
				+ "234567891"
				+ "567891234"
				+ "891234567"
				+ "345678912"
				+ "678912345"
				+ "912345670}";

		SudokuBoard board = new SudokuBoard(boardValue);

		String actualBoardString = board.toString();
		String expectedBoardString = boardValue;

		Assert.assertEquals(expectedBoardString, actualBoardString);
	}

	@Test
	public void testGetValue()
	{
		String boardString
				= "{000000000"
				+ "000000400"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000}";
		SudokuBoard board = new SudokuBoard(boardString);
		int allCellsIndex = 15;
		int r = 1;
		int c = 6;
		int group = 2;
		int groupIndex = 3;
		SudokuValue value = SudokuValue.VALUE_4;

		Assert.assertEquals(
				value,
				board.getAllSudokuCells().get(allCellsIndex).getValue());
		Assert.assertEquals(
				value,
				board.getSudokuCells(ROW, r).get(c).getValue());
		Assert.assertEquals(
				value,
				board.getSudokuCells(COLUMN, c).get(r).getValue());
		Assert.assertEquals(
				value,
				board.getSudokuCells(GROUP, group).get(groupIndex).getValue());
	}

	@Test
	public void testSetValue()
	{
		SudokuBoard board = new SudokuBoard();
		int r = 1;
		int c = 6;
		SudokuValue value = SudokuValue.VALUE_4;

		board.getSudokuCells(ROW, r).get(c).setValue(value);
		SudokuValue actualValue = board.getSudokuCells(ROW, r).get(c).getValue();

		Assert.assertEquals(value, actualValue);
	}

	@Test
	public void testGetSudokuCells_group()
	{
		String boardString
				= "{000000000"
				+ "000000000"
				+ "000000000"
				+ "382000000"
				+ "401000000"
				+ "569000000"
				+ "000000000"
				+ "000000000"
				+ "000000000}";
		SudokuBoard board = new SudokuBoard(boardString);

		List<Integer> expectedValues = Arrays.asList(3, 8, 2, 4, 0, 1, 5, 6, 9);
		List<Integer> actualValues = getValues(board.getSudokuCells(GROUP, 3));

		Assert.assertEquals(expectedValues, actualValues);
	}

	@Test
	public void testGetSudokuCells_groupExtreme()
	{
		String[] boardStrings = new String[]
		{
			"{123000000456000000789000000000000000000000000000000000000000000000000000000000000}",
			"{000123000000456000000789000000000000000000000000000000000000000000000000000000000}",
			"{000000123000000456000000789000000000000000000000000000000000000000000000000000000}",
			"{000000000000000000000000000123000000456000000789000000000000000000000000000000000}",
			"{000000000000000000000000000000123000000456000000789000000000000000000000000000000}",
			"{000000000000000000000000000000000123000000456000000789000000000000000000000000000}",
			"{000000000000000000000000000000000000000000000000000000123000000456000000789000000}",
			"{000000000000000000000000000000000000000000000000000000000123000000456000000789000}",
			"{000000000000000000000000000000000000000000000000000000000000123000000456000000789}"
		};
		List<Integer> expectedValues = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

		for (int i = 0; i < 9; i++)
		{
			SudokuBoard board = new SudokuBoard(boardStrings[i]);

			List<Integer> actualValues = getValues(board.getSudokuCells(GROUP, i));

			Assert.assertEquals(String.format(
					"Unused values found in group %d. BoardString = %s",
					i,
					boardStrings[i]),
					expectedValues,
					actualValues);
		}
	}

	@Test
	public void testGetSudokuCells_row()
	{
		String boardString
				= "{000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "123406789"
				+ "000000000}";
		SudokuBoard board = new SudokuBoard(boardString);

		List<Integer> expectedValues = Arrays.asList(1, 2, 3, 4, 0, 6, 7, 8, 9);
		List<Integer> actualValues = getValues(board.getSudokuCells(ROW, 7));

		Assert.assertEquals(expectedValues, actualValues);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSudokuCells_invalidIndex_low()
	{
		SudokuBoard board = new SudokuBoard();

		board.getSudokuCells(SectionType.COLUMN, -1);

		Assert.fail("Expected exception to be thrown.");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSudokuCells_invalidIndex_high()
	{
		SudokuBoard board = new SudokuBoard();

		board.getSudokuCells(SectionType.COLUMN, 9);

		Assert.fail("Expected exception to be thrown.");
	}

	@Test
	public void testGetSudokuCells_col()
	{
		String boardString
				= "{000001000"
				+ "000002000"
				+ "000002000"
				+ "000004000"
				+ "000005000"
				+ "000006000"
				+ "000007000"
				+ "000008000"
				+ "000009000}";
		SudokuBoard board = new SudokuBoard(boardString);

		List<Integer> expectedValues = Arrays.asList(1, 2, 2, 4, 5, 6, 7, 8, 9);
		List<Integer> actualValues = getValues(board.getSudokuCells(COLUMN, 5));

		Assert.assertEquals(expectedValues, actualValues);
	}

	@Test
	public void testIsSolved_empty()
	{
		String sudokuBoard
				= "{000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000}";
		SudokuBoard board = new SudokuBoard(sudokuBoard);

		Assert.assertFalse(board.isSolved());
	}

	@Test
	public void testIsSolved_solved()
	{
		String boardString
				= "{123456789"
				+ "456789123"
				+ "789123456"
				+ "234567891"
				+ "567891234"
				+ "891234567"
				+ "345678912"
				+ "678912345"
				+ "912345678}";
		SudokuBoard board = new SudokuBoard(boardString);

		Assert.assertTrue(board.isSolved());
	}

	@Test
	public void testIsSolved_missingLast()
	{
		String boardString
				= "{123456789"
				+ "456789123"
				+ "789123456"
				+ "234567891"
				+ "567891234"
				+ "891234567"
				+ "345678912"
				+ "678912345"
				+ "912345670}";
		SudokuBoard board = new SudokuBoard(boardString);

		Assert.assertFalse("8 is missing from the last cell", board.isSolved());
	}

	@Test
	public void testResetFrom()
	{
		String boardStringA
				= "{000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000"
				+ "000000000}";
		String boardStringB
				= "{123456789"
				+ "456789123"
				+ "789123456"
				+ "234567891"
				+ "567891234"
				+ "891234567"
				+ "345678912"
				+ "678912345"
				+ "912345678}";
		SudokuBoard boardA = new SudokuBoard(boardStringA);
		SudokuBoard boardB = new SudokuBoard(boardStringB);

		boardA.resetFrom(boardB);

		Assert.assertTrue(boardA.isSolved());
	}

	@Test
	public void testCopyConstructor()
	{
		String boardValue
				= "{123456789"
				+ "456789123"
				+ "789123456"
				+ "234567891"
				+ "567891234"
				+ "891234567"
				+ "345678912"
				+ "678912345"
				+ "912345670}";
		SudokuBoard sudokuBoard1 = new SudokuBoard(boardValue);
		SudokuBoard sudokuBoard2 = new SudokuBoard(sudokuBoard1);

		sudokuBoard1.getSudokuCells(ROW, 8).get(8)
				.setValue(SudokuValue.VALUE_8);

		Assert.assertNotEquals(sudokuBoard1.isSolved(), sudokuBoard2.isSolved());
	}

	@Test
	public void testAddSolvedChangedConsumer()
	{
		String boardValue
				= "{123456789"
				+ "456789123"
				+ "789123456"
				+ "234567891"
				+ "567891234"
				+ "891234567"
				+ "345678912"
				+ "678912345"
				+ "912345670}";
		SudokuBoard sudokuBoard = new SudokuBoard(boardValue);
		AtomicBoolean listenerTriggered = new AtomicBoolean(false);
		Consumer<SudokuEvent<SudokuBoard, Boolean>> boardSolvedChangedConsumer = event -> listenerTriggered.set(true);
		sudokuBoard.addSolvedChangedConsumer(boardSolvedChangedConsumer);

		sudokuBoard.getSudokuCells(ROW, 8).get(8)
				.setValue(SudokuValue.VALUE_8);

		Assert.assertTrue(listenerTriggered.get());
	}

	@Test
	public void testRemoveSolvedChangedConsumer()
	{
		String boardValue
				= "{123456789"
				+ "456789123"
				+ "789123456"
				+ "234567891"
				+ "567891234"
				+ "891234567"
				+ "345678912"
				+ "678912345"
				+ "912345670}";
		SudokuBoard sudokuBoard = new SudokuBoard(boardValue);
		AtomicBoolean listenerTriggered = new AtomicBoolean(false);
		Consumer<SudokuEvent<SudokuBoard, Boolean>> boardSolvedChangedConsumer = event -> listenerTriggered.set(true);
		sudokuBoard.addSolvedChangedConsumer(boardSolvedChangedConsumer); // See previous test
		sudokuBoard.removeSolvedChangedConsumer(boardSolvedChangedConsumer);

		sudokuBoard.getSudokuCells(ROW, 8).get(8)
				.setValue(SudokuValue.VALUE_8);

		Assert.assertFalse(listenerTriggered.get());
	}

	private static List<Integer> getValues(List<SudokuCell> sudokuCells)
	{
		return sudokuCells.stream()
				.map(SudokuCell::getValue)
				.map(v -> v == null ? 0 : v.getValue())
				.collect(Collectors.toList());
	}
}
