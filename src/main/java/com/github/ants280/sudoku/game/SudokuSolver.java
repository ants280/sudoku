package com.github.ants280.sudoku.game;

import com.github.ants280.sudoku.game.solver.SudokuSolverPlugin;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class SudokuSolver
{
	private final List<SudokuSolverPlugin> solverPlugins;

	public SudokuSolver(
			SudokuBoard sudokuBoard,
			BiConsumer<SudokuCell, Integer> setValueConsumer,
			BiConsumer<SudokuCell, Integer> toggleSudokuCellPossibleValue)
	{
		this.solverPlugins = Arrays.asList();
	}

	public boolean makeMove()
	{
		return solverPlugins.stream()
				.anyMatch(SudokuSolverPlugin::makeMove);
	}

	public void solveFast()
	{
		while (this.makeMove())
		{
			// Run the plugin!
		}
	}
}
