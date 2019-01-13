package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SudokuLogicSolver extends SudokuSolver
{
	private final List<SudokuSolverPlugin> solverPlugins;

	public SudokuLogicSolver(
			SudokuBoard sudokuBoard,
			Consumer<String> moveDescriptionConsumer)
	{
		super(sudokuBoard);

		this.solverPlugins = Arrays.asList(
				new OnlyPossibleValueSudokuSolverPlugin(
						sudokuBoard,
						moveDescriptionConsumer,
						removeNearbyPossibleValuesConsumer),
				new LastPossibleValueInSectionSudokuSolverPlugin(
						sudokuBoard,
						moveDescriptionConsumer,
						removeNearbyPossibleValuesConsumer),
				new CullPossibleValuesSudokuSolverPlugin(
						sudokuBoard,
						moveDescriptionConsumer),
				new RemovePossibleValueForOtherGroupsSudokuSolverPlugin(
						sudokuBoard,
						moveDescriptionConsumer),
				new SetPossibleValuesSudokuSolverPlugin(
						sudokuBoard,
						moveDescriptionConsumer));
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
		boolean moveMade;

		do
		{
			moveMade = this.makeMove();
		}
		while (moveMade);
	}
}
