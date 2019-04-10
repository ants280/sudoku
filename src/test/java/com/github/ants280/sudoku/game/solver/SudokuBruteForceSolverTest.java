package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import org.junit.Assert;
import org.junit.Test;

public class SudokuBruteForceSolverTest
{
	@Test
	public void testMakeMove()
	{
		SudokuBoard board = new SudokuBoard();
		SudokuSolver solver = new SudokuBruteForceSolver(board);

		boolean moveMade = solver.makeMove();

		Assert.assertFalse(
				"The logic solver should not be able to make individual moves. "
				+ "Use solveFast().",
				moveMade);
	}
}
