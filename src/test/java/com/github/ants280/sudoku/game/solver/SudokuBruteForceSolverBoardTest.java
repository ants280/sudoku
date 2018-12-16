package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SudokuBruteForceSolverBoardTest
{
	private final String boardString;
	private final boolean expectedValue;
	private final String testName; // useful for test run message

	public SudokuBruteForceSolverBoardTest(
			String boardString,
			boolean expectedValue,
			String testName)
	{
		this.boardString = boardString;
		this.expectedValue = expectedValue;
		this.testName = testName;
	}

	@Parameterized.Parameters(name = "{index}: {2}, expecedCanBruteForceSolve={1}")
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
				// problem boards: (it would eventually be nice if these could be solved with logic)
				createTestCase("{004063100000010002000074683907000000006080900000000504825640000700090000009350700}", true, "6/5 stars 2017-11-18"),
				createTestCase("{003070600000159020900000005700000010006040900040000006400000002070362000009080700}", true, ":("),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345660}", false, "X no working last value"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345670}", true, "X last value be 8"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345678}", true, "X already solved"),
				createTestCase("{000000000000000000000000000000000000000000000000000000000000000000000000000000000}", true, "X empty board"));
	}

	private static Object[] createTestCase(String boardString, boolean expectedValue, String testName)
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
		SudokuSolver sudokuSolver = new SudokuBruteForceSolver(sudokuBoard);

		sudokuSolver.solveFast();
		boolean actualValue = sudokuBoard.isSolved();

		Assert.assertEquals(expectedValue, actualValue);
	}

	@Test
	public void testCannotBeSolvedWithLogic()
	{
		if (expectedValue && !testName.startsWith("X"))
		{
			SudokuBoard sudokuBoard = new SudokuBoard(boardString);
			SudokuSolver logicSudokuSolver = new SudokuLogicSolver(
					sudokuBoard,
					moveDescription ->
			{
				// no need to consume move description in test
			});

			logicSudokuSolver.initialize();
			logicSudokuSolver.solveFast();

			boolean solvedWithLogic = sudokuBoard.isSolved();
			Assert.assertFalse(solvedWithLogic); // If true, move to logic test!
		}
	}
}
