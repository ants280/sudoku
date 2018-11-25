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

	public SudokuBruteForceSolverTest(String boardString, boolean expectedValue)
	{
		this.boardString = boardString;
		this.expectedValue = expectedValue;
	}

	@Parameterized.Parameters
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345660}", false),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345670}", true),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345678}", true));
	}

	private static Object[] createTestCase(String boardString, boolean expectedValue)
	{
		return new Object[]
		{
			boardString, expectedValue
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
