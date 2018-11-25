package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SudokuBruteForceSolverTest
{
	private final String boardString;
	private final boolean expectedValue;
	private final String testName; // useful for test run message

	public SudokuBruteForceSolverTest(
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
				createTestCase("{004063100000010002000074683907000000006080900000000504825640000700090000009350700}", true, "level 6/5, 2017-11-18"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345660}", false, "no working last value"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345670}", true, "last valuld be 8"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345678}", true, "already solved"));
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
}
