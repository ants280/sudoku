package com.github.ants280.sudoku.game;

import com.github.ants280.sudoku.game.solver.SudokuSolverPlugin;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class SudokuSolver
{
	private final SudokuBoard sudokuBoard;
	private final List<SudokuSolverPlugin> solverPlugins;
	private final BiConsumer<SudokuCell, Integer> removeNearbyPossibleValuesConsumer;

	public SudokuSolver(
			SudokuBoard sudokuBoard,
			BiConsumer<SudokuCell, Integer> setValueConsumer,
			BiConsumer<SudokuCell, Integer> toggleSudokuCellPossibleValue)
	{
		this.sudokuBoard = sudokuBoard;
		this.removeNearbyPossibleValuesConsumer
				= getClearNearbyPossibleValuesConsumer(sudokuBoard);

		BiConsumer<SudokuCell, Integer> updatedSetValueConsumer
				= setValueConsumer.andThen(removeNearbyPossibleValuesConsumer);
		this.solverPlugins = Arrays.asList();
	}

	public void initialize()
	{
		sudokuBoard.getAllSudokuCells()
				.stream()
				.filter(sudokuCell -> sudokuCell.getValue() == null)
				.forEach(sudokuCell -> sudokuCell.resetPossibleValues());
		sudokuBoard.getAllSudokuCells()
				.stream()
				.filter(sudokuCell -> sudokuCell.getValue() != null)
				.forEach(sudokuCell -> removeNearbyPossibleValuesConsumer
				.accept(sudokuCell, sudokuCell.getValue()));
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

	private static BiConsumer<SudokuCell, Integer>
			getClearNearbyPossibleValuesConsumer(SudokuBoard sudokuBoard)
	{
		return (sudokuCell, v) ->
		{
			Arrays.stream(SectionType.values())
					.forEach(sectionType -> sudokuBoard.getSudokuCells(sectionType, sudokuCell.getIndex(sectionType))
					.forEach(nearbySudokuCell -> nearbySudokuCell.removePossibleValue(v)));
		};
	}
}
