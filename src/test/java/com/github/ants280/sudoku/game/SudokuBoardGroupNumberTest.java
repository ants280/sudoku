package com.github.ants280.sudoku.game;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SudokuBoardGroupNumberTest
{
	private final int r;
	private final int c;
	private final int expectedGroupNumber;

	public SudokuBoardGroupNumberTest(int r, int c, int expectedGroupNumber)
	{
		this.r = r;
		this.c = c;
		this.expectedGroupNumber = expectedGroupNumber;
	}

	@Parameters(name = "{index}: SudokuBoard.getGroupNumber(r:{0}, c:{1})"
			+ " = expectedGroupNumber:{2}")
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
				createTestCase(0, 0, 0),
				createTestCase(0, 1, 0),
				createTestCase(0, 2, 0),
				createTestCase(0, 3, 1),
				createTestCase(0, 4, 1),
				createTestCase(0, 5, 1),
				createTestCase(0, 6, 2),
				createTestCase(0, 7, 2),
				createTestCase(0, 8, 2),
				createTestCase(1, 0, 0),
				createTestCase(1, 1, 0),
				createTestCase(1, 2, 0),
				createTestCase(1, 3, 1),
				createTestCase(1, 4, 1),
				createTestCase(1, 5, 1),
				createTestCase(1, 6, 2),
				createTestCase(1, 7, 2),
				createTestCase(1, 8, 2),
				createTestCase(2, 0, 0),
				createTestCase(2, 1, 0),
				createTestCase(2, 2, 0),
				createTestCase(2, 3, 1),
				createTestCase(2, 4, 1),
				createTestCase(2, 5, 1),
				createTestCase(2, 6, 2),
				createTestCase(2, 7, 2),
				createTestCase(2, 8, 2),
				createTestCase(3, 0, 3),
				createTestCase(3, 1, 3),
				createTestCase(3, 2, 3),
				createTestCase(3, 3, 4),
				createTestCase(3, 4, 4),
				createTestCase(3, 5, 4),
				createTestCase(3, 6, 5),
				createTestCase(3, 7, 5),
				createTestCase(3, 8, 5),
				createTestCase(4, 0, 3),
				createTestCase(4, 1, 3),
				createTestCase(4, 2, 3),
				createTestCase(4, 3, 4),
				createTestCase(4, 4, 4),
				createTestCase(4, 5, 4),
				createTestCase(4, 6, 5),
				createTestCase(4, 7, 5),
				createTestCase(4, 8, 5),
				createTestCase(5, 0, 3),
				createTestCase(5, 1, 3),
				createTestCase(5, 2, 3),
				createTestCase(5, 3, 4),
				createTestCase(5, 4, 4),
				createTestCase(5, 5, 4),
				createTestCase(5, 6, 5),
				createTestCase(5, 7, 5),
				createTestCase(5, 8, 5),
				createTestCase(6, 0, 6),
				createTestCase(6, 1, 6),
				createTestCase(6, 2, 6),
				createTestCase(6, 3, 7),
				createTestCase(6, 4, 7),
				createTestCase(6, 5, 7),
				createTestCase(6, 6, 8),
				createTestCase(6, 7, 8),
				createTestCase(6, 8, 8),
				createTestCase(7, 0, 6),
				createTestCase(7, 1, 6),
				createTestCase(7, 2, 6),
				createTestCase(7, 3, 7),
				createTestCase(7, 4, 7),
				createTestCase(7, 5, 7),
				createTestCase(7, 6, 8),
				createTestCase(7, 7, 8),
				createTestCase(7, 8, 8),
				createTestCase(8, 0, 6),
				createTestCase(8, 1, 6),
				createTestCase(8, 2, 6),
				createTestCase(8, 3, 7),
				createTestCase(8, 4, 7),
				createTestCase(8, 5, 7),
				createTestCase(8, 6, 8),
				createTestCase(8, 7, 8),
				createTestCase(8, 8, 8));
	}

	private static Object[] createTestCase(
			int r,
			int c,
			int expectedGroupNumber)
	{
		return new Object[]
		{
			r, c, expectedGroupNumber
		};
	}

	@Test
	public void test()
	{
		int actualGroupNumber = SudokuBoard.getGroupNumber(r, c);

		Assert.assertEquals(expectedGroupNumber, actualGroupNumber);
	}
}
