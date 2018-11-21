package com.github.ants280.sudoku.game;

import static com.github.ants280.sudoku.game.SectionType.*;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class SudokuBoardTest
{
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidBoardSize()
	{
		SudokuBoard board = new SudokuBoard("{123456789}");

		fail("The following sudkuBoard should be invalid because it is too small: " + board);
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

		assertEquals(expectedBoardString, actualBoardString);
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
		int r = 1;
		int c = 6;
		Integer value = 4;

		Integer actualValue = board.getSudokuCell(ROW, r, c).getValue();

		assertEquals(value, actualValue);
	}

	@Test
	public void testSetValue()
	{
		SudokuBoard board = new SudokuBoard();
		int r = 1;
		int c = 6;
		Integer value = 4;

		board.getSudokuCell(ROW, r, c).setValue(value);
		Integer actualValue = board.getSudokuCell(ROW, r, c).getValue();

		assertEquals(value, actualValue);
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

		int[] expectedValues = new int[]
		{
			3, 8, 2, 4, 0, 1, 5, 6, 9
		};
		int[] actualValues = getValues(board.getSudokuCells(GROUP, 3));

		assertArrayEquals(expectedValues, actualValues);
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
		int[] expectedValues = new int[]
		{
			1, 2, 3, 4, 5, 6, 7, 8, 9
		};

		for (int i = 0; i < 9; i++)
		{
			SudokuBoard board = new SudokuBoard(boardStrings[i]);

			int[] actualValues = getValues(board.getSudokuCells(GROUP, i));

			assertArrayEquals(String.format(
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

		int[] expectedValues = new int[]
		{
			1, 2, 3, 4, 0, 6, 7, 8, 9
		};
		int[] actualValues = getValues(board.getSudokuCells(ROW, 7));

		assertArrayEquals(expectedValues, actualValues);
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

		int[] expectedValues = new int[]
		{
			1, 2, 2, 4, 5, 6, 7, 8, 9
		};
		int[] actualValues = getValues(board.getSudokuCells(COL, 5));

		assertArrayEquals(expectedValues, actualValues);
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

		assertFalse(board.isSolved());
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

		assertTrue(board.isSolved());
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

		assertFalse("8 is missing from the last cell", board.isSolved());
	}

	private static int[] getValues(SudokuCell[] sudokuCells)
	{
		return Arrays.stream(sudokuCells)
				.map(SudokuCell::getValue)
				.map(v -> v == null ? 0 : v)
				.mapToInt(Integer::intValue)
				.toArray();
	}
}
