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
				// problem boards: (it would eventually be nice if these could be solved with logic
				createTestCase("{079010004000560080000070906104080090000000000050030701701040000040098000900050430}", true, "5/5 stars March 22"),
				createTestCase("{370095000600080090008300007000010050160000034040060000700002900020030008000950042}", true, "6/5 stars 20170218"),
				createTestCase("{679810004410569087000070916104080090097020000050930701701040009040098170900050430}", true, ""),
				createTestCase("{605040902001090003020015600900004800000060000008100009006430080500080200807020306}", true, "4/5 stars 2017-11-16"),
				createTestCase("{000062507006000001010070008095400002600090005100005970700040050200000800401580000}", true, "5/5 stars 2017-11-17"),
				createTestCase("{004063100000010002000074683907000000006080900000000504825640000700090000009350700}", true, "6/5 stars 2017-11-18"),
				createTestCase("{003070600000159020900000005700000010006040900040000006400000002070362000009080700}", true, ":("),
				createTestCase("{004063100000010002000074683907000000006080900000000504825640000700090000009350700}", true, "level 6/5, 2017-11-18"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345660}", false, "no working last value"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345670}", true, "last valuld be 8"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345678}", true, "already solved"),
				createTestCase("{000000000000000000000000000000000000000000000000000000000000000000000000000000000}", true, "empty board"));
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
