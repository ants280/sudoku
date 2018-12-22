package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SudokuSolverTest
{
	private final String boardString;
	private final SolvableType solvableType;
	private final String testName; // useful for test run message

	public SudokuSolverTest(
			String boardString,
			SolvableType solvableType,
			String testName)
	{
		this.boardString = boardString;
		this.solvableType = solvableType;
		this.testName = testName;
	}

	@Parameterized.Parameters(name = "{index}: {2}, expectedSolveType={1} board={0}")
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
				createTestCase("{002689300849000020060470000170890402490020071206041089000054060080000195007918200}", SolvableType.LOGIC, "1/5 stars"),
				createTestCase("{000086200000000069060039071140060920020070040037040015290610080680000000001520000}", SolvableType.LOGIC, "2/5 stars"),
				createTestCase("{000086200000000069060039071140060920020070040037040015290610080680000000001520000}", SolvableType.LOGIC, "3/5 stars"),
				createTestCase("{003761005000032000060000008008290006000070000400016900700000030000520000100643200}", SolvableType.LOGIC, "4/5 stars"),
				createTestCase("{900801000060020100200070004050130200080050090007096050700010006002080030000309002}", SolvableType.LOGIC, "5/5 stars"),
				createTestCase("{000208009007040380050060000600900700070030010009007004000080060086070100500601000}", SolvableType.LOGIC, "6/5 stars"),
				createTestCase("{020008500700009020000040008980050700003060800007080059500090000070600002009700060}", SolvableType.LOGIC, "6/5 stars, requires RemovePossibleValueForOtherGroupsSudokuSolverPlugin"),
				createTestCase("{079010004000560080000070906104080090000000000050030701701040000040098000900050430}", SolvableType.LOGIC, "5/5 stars March 22, requires RemovePossibleValueForOtherGroupsSudokuSolverPlugin"),
				createTestCase("{000062507006000001010070008095400002600090005100005970700040050200000800401580000}", SolvableType.LOGIC, "5/5 stars 2017-11-17, requires RemovePossibleValueForOtherGroupsSudokuSolverPlugin"),
				createTestCase("{679810004410569087000070916104080090097020000050930701701040009040098170900050430}", SolvableType.LOGIC, "?, requires RemovePossibleValueForOtherGroupsSudokuSolverPlugin"),
				createTestCase("{370095000600080090008300007000010050160000034040060000700002900020030008000950042}", SolvableType.LOGIC, "6/5 stars 20170218, requires CullPossibleValuesSudokuSolverPlugin (and RemovePossibleValueForOtherGroupsSudokuSolverPlugin)"),
				createTestCase("{605040902001090003020015600900004800000060000008100009006430080500080200807020306}", SolvableType.LOGIC, "4/5 stars 2017-11-16, requires SetPossibleValuesSudokuSolverPlugin"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345660}", SolvableType.UNSOLVEABLE, "no working last value"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345670}", SolvableType.LOGIC, "last value be 8"),
				createTestCase("{123456789456789123789123456234567891567891234891234567345678912678912345912345678}", SolvableType.LOGIC, "already solved"),
				// problem boards: (it would eventually be nice if these could be solved with logic) [other than the last, empty board]
				createTestCase("{004063100000010002000074683907000000006080900000000504825640000700090000009350700}", SolvableType.BRUTE_FORCE, "6/5 stars 2017-11-18"),
				createTestCase("{003070600000159020900000005700000010006040900040000006400000002070362000009080700}", SolvableType.BRUTE_FORCE, ":("),
				createTestCase("{700082000000041503103005000002030098000020000390010700000700302801290000000150009}", SolvableType.BRUTE_FORCE, "6/5 stars 2018-12-22"),
				createTestCase("{000000000000000000000000000000000000000000000000000000000000000000000000000000000}", SolvableType.BRUTE_FORCE, "empty board"));
	}

	private static Object[] createTestCase(
			String boardString,
			SolvableType solvableType,
			String testName)
	{
		return new Object[]
		{
			boardString, solvableType, testName
		};
	}

	@Test
	public void testSolveFast_logic()
	{
		Collection<String> moveDescriptions = new HashSet<>();
		SudokuBoard logicBoard = new SudokuBoard(boardString);
		SudokuSolver logicSolver = new SudokuLogicSolver(
				logicBoard,
				moveDescription -> Assert.assertTrue(
						"Solver made move with publicate description",
						moveDescriptions.add(moveDescription)));
		logicSolver.initialize();
		logicSolver.solveFast();

		boolean actualSolved = logicBoard.isSolved();
		boolean expectedSolved = solvableType.isExpectedSolvable(SolvableType.LOGIC);

		Assert.assertEquals(expectedSolved, actualSolved);
	}

	@Test
	public void testSolveFast_bruteForce()
	{
		SudokuBoard board = new SudokuBoard(boardString);
		SudokuSolver solver = new SudokuBruteForceSolver(board);
		solver.solveFast();

		boolean actualSolved = board.isSolved();
		boolean expectedSolved = solvableType.isExpectedSolvable(SolvableType.BRUTE_FORCE);

		Assert.assertEquals(expectedSolved, actualSolved);
	}
}
