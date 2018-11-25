package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SudokuSolverTest
{
	private final String boardString;
	private final boolean expectedValue;
	private final String testName; // useful for test run message

	public SudokuSolverTest(
			String boardString,
			boolean expectedValue,
			String testName)
	{
		this.boardString = boardString;
		this.expectedValue = expectedValue;
		this.testName = testName;
	}

	@Parameterized.Parameters(name = "{index}: {2}, expectedValue={1}")
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
				createTestCase("{002689300849000020060470000170890402490020071206041089000054060080000195007918200}", true, "1/5 stars"),
				createTestCase("{000086200000000069060039071140060920020070040037040015290610080680000000001520000}", true, "2/5 stars"),
				createTestCase("{000086200000000069060039071140060920020070040037040015290610080680000000001520000}", true, "3/5 stars"),
				createTestCase("{003761005000032000060000008008290006000070000400016900700000030000520000100643200}", true, "4/5 stars"),
				createTestCase("{900801000060020100200070004050130200080050090007096050700010006002080030000309002}", true, "5/5 stars"),
				createTestCase("{000000000000000000000000000000000000000000000000000000000000000000000000000000000}", false, "empty board"));
	}

	private static Object[] createTestCase(
			String boardString,
			boolean expectedValue,
			String testName)
	{
		return new Object[]
		{
			boardString, expectedValue, testName
		};
	}

	@Test
	public void test()
	{
		SudokuBoard sudokuBoard = new SudokuBoard(boardString);
		SudokuSolver sudokuSolver = new SudokuSolver(sudokuBoard);

		sudokuSolver.initialize();
		sudokuSolver.solveFast();
		boolean actualValue = sudokuBoard.isSolved();

		Assert.assertEquals(expectedValue, actualValue);
	}
}
