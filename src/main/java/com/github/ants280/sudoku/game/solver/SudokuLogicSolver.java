package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.Arrays;
import java.util.List;

public class SudokuLogicSolver extends SudokuSolver
{
	private final List<SudokuSolverPlugin> solverPlugins;

	public SudokuLogicSolver(SudokuBoard sudokuBoard)
	{
		super(sudokuBoard);

		this.solverPlugins = Arrays.asList(
				new OnlyPossibleValueSudokuSolverPlugin(sudokuBoard, removeNearbyPossibleValuesConsumer),
				new LastPossibleValueInSectionSudokuSolverPlugin(sudokuBoard, removeNearbyPossibleValuesConsumer),
				new CullPossibleValuesSudokuSolverPlugin(sudokuBoard),
				new RemovePossibleValueForOtherGroupsSudokuSolverPlugin(sudokuBoard));
	}

	@Override
	public boolean makeMove()
	{
		return solverPlugins.stream()
				.anyMatch(SudokuSolverPlugin::makeMove);
	}

	@Override
	public void solveFast()
	{
		while (this.makeMove())
		{
			// Run the plugin!
		}
	}
}
