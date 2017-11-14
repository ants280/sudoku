package com.jpatterson.school.sudoku2.game;

import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

public class SudokuBoardTest
{
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidBoardSize()
	{
		SudokuBoard board = new SudokuBoard("123456789");

		fail("The following sudkuBoard should be invalid because it is too small: " + board);
	}

	@Test
	public void testToString()
	{
		String boardValue
			= "123456789"
			+ "456789123"
			+ "789123456"
			+ "234567891"
			+ "567891234"
			+ "891234567"
			+ "345678912"
			+ "678912345"
			+ "912345670";
		SudokuBoard board = new SudokuBoard(boardValue);

		String actualBoardString = board.toString();
		String expectedBoardString = "{" + boardValue + "}";

		assertEquals(expectedBoardString, actualBoardString);
	}

	@Test
	public void testGetValue()
	{
		SudokuBoard board = new SudokuBoard(
			"000000000"
			+ "000000400"
			+ "000000000"
			+ "000000000"
			+ "000000000"
			+ "000000000"
			+ "000000000"
			+ "000000000"
			+ "000000000");
		int r = 1;
		int c = 6;
		int value = 4;

		int actualValue = board.getValue(r, c);

		assertSame(value, actualValue);
	}

	@Test
	public void testSetValue()
	{
		SudokuBoard board = new SudokuBoard();
		int r = 1;
		int c = 6;
		int value = 4;

		board.setValue(r, c, value);
		int actualValue = board.getValue(r, c);

		assertSame(value, actualValue);
	}

	@Test
	public void testGetGroupNumber()
	{
		SudokuBoard board = new SudokuBoard();

		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				int expectedGroupNumber = (r / 3) * 3 + c / 3;
				//System.out.printf("making sure [%d,%d] is in group %d\n", r, c, expectedGroupNumber);

				int actualGroupNumber = board.getGroupNumber(r, c);

				assertSame(String.format("Incorrect group for [%d,%d]", r, c),
					expectedGroupNumber, actualGroupNumber);
			}
		}
	}

	@Test
	public void testGetRowNumber()
	{
		SudokuBoard board = new SudokuBoard();

		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				assertSame(r, board.getRowNumber(r, c));
			}
		}
	}

	@Test
	public void testGetColNumber()
	{
		SudokuBoard board = new SudokuBoard();

		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				assertSame(c, board.getColNumber(r, c));
			}
		}
	}

	@Test
	public void testGetUnusedValuesForGroup()
	{
		SudokuBoard board = new SudokuBoard(
			"000000000"
			+ "000000000"
			+ "000000000"
			+ "382000000"
			+ "401000000"
			+ "569000000"
			+ "000000000"
			+ "000000000"
			+ "000000000");

		Set<Integer> unusedValuesForRow = board.getUnusedValuesForGroup(3);

		assertSame(1, unusedValuesForRow.size());
		assertTrue(unusedValuesForRow.contains(7));
	}

	@Test
	public void testGetUnusedValuesForGroup_extreme()
	{
		String[] boardStrings = new String[]
		{
			"123000000456000000789000000000000000000000000000000000000000000000000000000000000",
			"000123000000456000000789000000000000000000000000000000000000000000000000000000000",
			"000000123000000456000000789000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000123000000456000000789000000000000000000000000000000000",
			"000000000000000000000000000000123000000456000000789000000000000000000000000000000",
			"000000000000000000000000000000000123000000456000000789000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000123000000456000000789000000",
			"000000000000000000000000000000000000000000000000000000000123000000456000000789000",
			"000000000000000000000000000000000000000000000000000000000000123000000456000000789"
		};

		for (int i = 0; i < 9; i++)
		{
			SudokuBoard board = new SudokuBoard(boardStrings[i]);

			Set<Integer> unusedValuesForGroup = board.getUnusedValuesForGroup(i);

			assertTrue(String.format("Unused values found in group %d: %s. BoardString = %s", i, unusedValuesForGroup, boardStrings[i]),
				unusedValuesForGroup.isEmpty());
		}
	}

	@Test
	public void testGenUnusedValuesForRow()
	{
		SudokuBoard board = new SudokuBoard(
			"000000000"
			+ "000000000"
			+ "000000000"
			+ "000000000"
			+ "000000000"
			+ "000000000"
			+ "000000000"
			+ "123406789"
			+ "000000000");

		Set<Integer> unusedValuesForRow = board.getUnusedValuesForRow(7);

		assertSame(1, unusedValuesForRow.size());
		assertTrue(unusedValuesForRow.contains(5));
	}

	@Test
	public void testGenUnusedValuesForCol()
	{
		SudokuBoard board = new SudokuBoard(
			"000001000"
			+ "000002000"
			+ "000002000"
			+ "000004000"
			+ "000005000"
			+ "000006000"
			+ "000007000"
			+ "000008000"
			+ "000009000");

		Set<Integer> unusedValuesForRow = board.getUnusedValuesForCol(5);

		assertSame(1, unusedValuesForRow.size());
		assertTrue(unusedValuesForRow.contains(3));
	}

	@Test
	public void testIsSolved_empty()
	{
		SudokuBoard board = new SudokuBoard();

		assertFalse(board.isSolved());
	}

	@Test
	public void testIsSolved_solved()
	{
		SudokuBoard board = new SudokuBoard(
			"123456789"
			+ "456789123"
			+ "789123456"
			+ "234567891"
			+ "567891234"
			+ "891234567"
			+ "345678912"
			+ "678912345"
			+ "912345678");

		assertTrue(board.isSolved());
	}

	@Test
	public void testIsSolved_missingLast()
	{
		SudokuBoard board = new SudokuBoard(
			"123456789"
			+ "456789123"
			+ "789123456"
			+ "234567891"
			+ "567891234"
			+ "891234567"
			+ "345678912"
			+ "678912345"
			+ "912345670");

		assertFalse(board.isSolved());
	}
}
