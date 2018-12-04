package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SudokuLogicSolverTest
{
	private final String boardString;
	private final boolean expectedValue;
	private final String testName; // useful for test run message

	public SudokuLogicSolverTest(
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
				createTestCase("{000208009007040380050060000600900700070030010009007004000080060086070100500601000}", true, "6/5 stars"),
				createTestCase("{020008500700009020000040008980050700003060800007080059500090000070600002009700060}", true, "6/5 stars, requires RemovePossibleValueForOtherGroupsSudokuSolverPlugin"),
				createTestCase("{079010004000560080000070906104080090000000000050030701701040000040098000900050430}", true, "5/5 stars March 22, requires RemovePossibleValueForOtherGroupsSudokuSolverPlugin"),
				createTestCase("{000062507006000001010070008095400002600090005100005970700040050200000800401580000}", true, "5/5 stars 2017-11-17, requires RemovePossibleValueForOtherGroupsSudokuSolverPlugin"),
				createTestCase("{679810004410569087000070916104080090097020000050930701701040009040098170900050430}", true, "?, requires RemovePossibleValueForOtherGroupsSudokuSolverPlugin"),
				createTestCase("{370095000600080090008300007000010050160000034040060000700002900020030008000950042}", true, "6/5 stars 20170218, requires CullPossibleValuesSudokuSolverPlugin (and RemovePossibleValueForOtherGroupsSudokuSolverPlugin)"),
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
		SudokuSolver sudokuSolver = new SudokuLogicSolver(sudokuBoard);

		sudokuSolver.initialize();
		sudokuSolver.solveFast();
		boolean actualValue = sudokuBoard.isSolved();

		Assert.assertEquals(expectedValue, actualValue);
	}
}
