package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class SudokuBruteForceSolver extends SudokuSolver
{
	private final Predicate<SudokuCell> hasValidSections
			= sudokuCell -> Arrays.stream(SectionType.values())
					.map(sectionType -> sudokuBoard.getSudokuCells(
					sectionType,
					sudokuCell.getIndex(sectionType)))
					.noneMatch(this::hasDuplicateValues);

	public SudokuBruteForceSolver(SudokuBoard sudokuBoard)
	{
		super(sudokuBoard);
	}

	@Override
	public boolean makeMove()
	{
		return false;
	}

	@Override
	public void solveFast()
	{
		super.initialize();

		this.canBruteForceSolve(0);
	}

	private boolean canBruteForceSolve(int index)
	{
		List<SudokuCell> allSudokuCells = sudokuBoard.getAllSudokuCells();
		if (index >= allSudokuCells.size())
		{
			return sudokuBoard.isSolved();
		}

		SudokuCell sudokuCell = allSudokuCells.get(index);
		if (sudokuCell.getValue() != null)
		{
			return canBruteForceSolve(index + 1);
		}

		Collection<SudokuValue> possibleValues = sudokuCell.getPossibleValues();
		for (SudokuValue possibleValue : possibleValues)
		{
			sudokuCell.setValue(possibleValue);

			if (hasValidSections.test(sudokuCell)
					&& canBruteForceSolve(index + 1))
			{
				return true;
			}
		}

		sudokuCell.setValue(null);
		return false;
	}

	private boolean hasDuplicateValues(List<SudokuCell> sudokuCells)
	{
		Collection<SudokuValue> usedValuesSet
				= EnumSet.noneOf(SudokuValue.class);

		return !sudokuCells.stream()
				.filter(sudokuCell -> sudokuCell.getValue() != null)
				.map(SudokuCell::getValue)
				// "true if this collection changed as a result of the call" :
				.allMatch(usedValuesSet::add);
	}
}
