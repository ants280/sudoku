package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class SudokuSolver
{
	protected final SudokuBoard sudokuBoard;
	private final List<SudokuSolverPlugin> solverPlugins;
	private final BiConsumer<SudokuCell, SudokuValue> removeNearbyPossibleValuesConsumer;

	public SudokuSolver(SudokuBoard sudokuBoard)
	{
		this.sudokuBoard = sudokuBoard;
		this.removeNearbyPossibleValuesConsumer
				= getClearNearbyPossibleValuesConsumer(sudokuBoard);

		this.solverPlugins = Arrays.asList(
				new OnlyPossibleValueSudokuSolverPlugin(sudokuBoard, removeNearbyPossibleValuesConsumer),
				new LastPossibleValueInSectionSudokuSolverPlugin(sudokuBoard, removeNearbyPossibleValuesConsumer),
				new CullPossibleValuesSudokuSolverPlugin(sudokuBoard),
				new RemovePossibleValueForOtherGroupsSudokuSolverPlugin(sudokuBoard));
	}

	public void initialize()
	{
		sudokuBoard.getAllSudokuCells()
				.stream()
				.filter(sudokuCell -> sudokuCell.getValue() == null)
				.forEach(SudokuCell::restoreAllPossibleValues);

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

	private static BiConsumer<SudokuCell, SudokuValue>
			getClearNearbyPossibleValuesConsumer(SudokuBoard sudokuBoard)
	{
		return (sudokuCell, v) -> Arrays.stream(SectionType.values())
				.forEach(sectionType -> sudokuBoard.getSudokuCells(
				sectionType,
				sudokuCell.getIndex(sectionType))
				.stream()
				.filter(nearbySudokuCell -> nearbySudokuCell.getValue() == null
				&& nearbySudokuCell.hasPossibleValue(v))
				.forEach(nearbySudokuCell -> nearbySudokuCell
				.togglePossibleValue(v)));
	}
}
