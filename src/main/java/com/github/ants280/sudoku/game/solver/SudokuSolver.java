package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Arrays;
import java.util.function.BiConsumer;

public abstract class SudokuSolver
{
	protected final SudokuBoard sudokuBoard;
	protected final BiConsumer<SudokuCell, SudokuValue> removeNearbyPossibleValuesConsumer;

	public SudokuSolver(SudokuBoard sudokuBoard)
	{
		this.sudokuBoard = sudokuBoard;
		this.removeNearbyPossibleValuesConsumer
				= getClearNearbyPossibleValuesConsumer(sudokuBoard);
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

	public abstract boolean makeMove();

	public abstract void solveFast();

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
