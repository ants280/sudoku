package com.github.ants280.sudoku.game;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

		Integer actualValue = board.getSudokuCell(r, c).getValue();

		assertEquals(value, actualValue);
	}

	@Test
	public void testSetValue()
	{
		SudokuBoard board = new SudokuBoard();
		int r = 1;
		int c = 6;
		Integer value = 4;

		board.getSudokuCell(r, c).setValue(value);
		Integer actualValue = board.getSudokuCell(r, c).getValue();

		assertEquals(value, actualValue);
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
				//System.out.printf("{making sure [%d,%d] is in group %d\n", r, c, expectedGroupNumber);

				int actualGroupNumber = board.getGroupNumber(r, c);

				assertSame(String.format("{Incorrect group for [%d,%d]", r, c),
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

		Set<Integer> unusedValuesForRow = board.getUnusedValuesForGroup(3);

		assertSame(1, unusedValuesForRow.size());
		assertTrue(unusedValuesForRow.contains(7));
	}

	@Test
	public void testGetUnusedValuesForGroup_extreme()
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

		Set<Integer> unusedValuesForRow = board.getUnusedValuesForRow(7);

		assertSame(1, unusedValuesForRow.size());
		assertTrue(unusedValuesForRow.contains(5));
	}

	@Test
	public void testGenUnusedValuesForCol()
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

	@Test
	public void testGetCellsInGroupSection()
	{
		// group 0
		assertGetCellsInGroupSectionValid(0, SectionType.ROW, 0, "{123000000000000000000000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(0, SectionType.COL, 0, "{100000000200000000300000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(0, SectionType.ROW, 1, "{000000000123000000000000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(0, SectionType.COL, 1, "{010000000020000000030000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(0, SectionType.ROW, 2, "{000000000000000000123000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(0, SectionType.COL, 2, "{001000000002000000003000000000000000000000000000000000000000000000000000000000000}");
		// group 1
		assertGetCellsInGroupSectionValid(1, SectionType.ROW, 0, "{000123000000000000000000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(1, SectionType.COL, 3, "{000100000000200000000300000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(1, SectionType.ROW, 1, "{000000000000123000000000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(1, SectionType.COL, 4, "{000010000000020000000030000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(1, SectionType.ROW, 2, "{000000000000000000000123000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(1, SectionType.COL, 5, "{000001000000002000000003000000000000000000000000000000000000000000000000000000000}");
		// group 2
		assertGetCellsInGroupSectionValid(2, SectionType.ROW, 0, "{000000123000000000000000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(2, SectionType.COL, 6, "{000000100000000200000000300000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(2, SectionType.ROW, 1, "{000000000000000123000000000000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(2, SectionType.COL, 7, "{000000010000000020000000030000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(2, SectionType.ROW, 2, "{000000000000000000000000123000000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(2, SectionType.COL, 8, "{000000001000000002000000003000000000000000000000000000000000000000000000000000000}");
		// group 3
		assertGetCellsInGroupSectionValid(3, SectionType.ROW, 3, "{000000000000000000000000000123000000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(3, SectionType.COL, 0, "{000000000000000000000000000100000000200000000300000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(3, SectionType.ROW, 4, "{000000000000000000000000000000000000123000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(3, SectionType.COL, 1, "{000000000000000000000000000010000000020000000030000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(3, SectionType.ROW, 5, "{000000000000000000000000000000000000000000000123000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(3, SectionType.COL, 2, "{000000000000000000000000000001000000002000000003000000000000000000000000000000000}");
		// group 4
		assertGetCellsInGroupSectionValid(4, SectionType.ROW, 3, "{000000000000000000000000000000123000000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(4, SectionType.COL, 3, "{000000000000000000000000000000100000000200000000300000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(4, SectionType.ROW, 4, "{000000000000000000000000000000000000000123000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(4, SectionType.COL, 4, "{000000000000000000000000000000010000000020000000030000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(4, SectionType.ROW, 5, "{000000000000000000000000000000000000000000000000123000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(4, SectionType.COL, 5, "{000000000000000000000000000000001000000002000000003000000000000000000000000000000}");
		// group 5
		assertGetCellsInGroupSectionValid(5, SectionType.ROW, 3, "{000000000000000000000000000000000123000000000000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(5, SectionType.COL, 6, "{000000000000000000000000000000000100000000200000000300000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(5, SectionType.ROW, 4, "{000000000000000000000000000000000000000000123000000000000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(5, SectionType.COL, 7, "{000000000000000000000000000000000010000000020000000030000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(5, SectionType.ROW, 5, "{000000000000000000000000000000000000000000000000000123000000000000000000000000000}");
		assertGetCellsInGroupSectionValid(5, SectionType.COL, 8, "{000000000000000000000000000000000001000000002000000003000000000000000000000000000}");
		// group 6
		assertGetCellsInGroupSectionValid(6, SectionType.ROW, 6, "{000000000000000000000000000000000000000000000000000000123000000000000000000000000}");
		assertGetCellsInGroupSectionValid(6, SectionType.COL, 0, "{000000000000000000000000000000000000000000000000000000100000000200000000300000000}");
		assertGetCellsInGroupSectionValid(6, SectionType.ROW, 7, "{000000000000000000000000000000000000000000000000000000000000000123000000000000000}");
		assertGetCellsInGroupSectionValid(6, SectionType.COL, 1, "{000000000000000000000000000000000000000000000000000000010000000020000000030000000}");
		assertGetCellsInGroupSectionValid(6, SectionType.ROW, 8, "{000000000000000000000000000000000000000000000000000000000000000000000000123000000}");
		assertGetCellsInGroupSectionValid(6, SectionType.COL, 2, "{000000000000000000000000000000000000000000000000000000001000000002000000003000000}");
		// group 7
		assertGetCellsInGroupSectionValid(7, SectionType.ROW, 6, "{000000000000000000000000000000000000000000000000000000000123000000000000000000000}");
		assertGetCellsInGroupSectionValid(7, SectionType.COL, 3, "{000000000000000000000000000000000000000000000000000000000100000000200000000300000}");
		assertGetCellsInGroupSectionValid(7, SectionType.ROW, 7, "{000000000000000000000000000000000000000000000000000000000000000000123000000000000}");
		assertGetCellsInGroupSectionValid(7, SectionType.COL, 4, "{000000000000000000000000000000000000000000000000000000000010000000020000000030000}");
		assertGetCellsInGroupSectionValid(7, SectionType.ROW, 8, "{000000000000000000000000000000000000000000000000000000000000000000000000000123000}");
		assertGetCellsInGroupSectionValid(7, SectionType.COL, 5, "{000000000000000000000000000000000000000000000000000000000001000000002000000003000}");
		// group 8
		assertGetCellsInGroupSectionValid(8, SectionType.ROW, 6, "{000000000000000000000000000000000000000000000000000000000000123000000000000000000}");
		assertGetCellsInGroupSectionValid(8, SectionType.COL, 6, "{000000000000000000000000000000000000000000000000000000000000100000000200000000300}");
		assertGetCellsInGroupSectionValid(8, SectionType.ROW, 7, "{000000000000000000000000000000000000000000000000000000000000000000000123000000000}");
		assertGetCellsInGroupSectionValid(8, SectionType.COL, 7, "{000000000000000000000000000000000000000000000000000000000000010000000020000000030}");
		assertGetCellsInGroupSectionValid(8, SectionType.ROW, 8, "{000000000000000000000000000000000000000000000000000000000000000000000000000000123}");
		assertGetCellsInGroupSectionValid(8, SectionType.COL, 8, "{000000000000000000000000000000000000000000000000000000000000001000000002000000003}");
	}

	private void assertGetCellsInGroupSectionValid(
			int groupNumber, SectionType sectionType, int sectionNumber, String boardString)
	{
		assertTrue(boardString.matches("^\\{0*10*20*30*\\}$"));
		SudokuBoard sudokuBoard = new SudokuBoard(boardString);

		Collection<SudokuCell> sudokuCellsInGroupSection
				= sudokuBoard.getSudokuCellsInGroupSection(groupNumber, sectionType, sectionNumber);

		List<Integer> actualSudokuCellValues = sudokuCellsInGroupSection.stream()
				.map(SudokuCell::getValue)
				.collect(Collectors.toList());
		assertSame(3, actualSudokuCellValues.size());
		assertTrue(actualSudokuCellValues.contains(1));
		assertTrue(actualSudokuCellValues.contains(2));
		assertTrue(actualSudokuCellValues.contains(3));
	}
}
