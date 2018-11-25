package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SudokuBruteForceSolver extends SudokuSolver
{
	private final Predicate<SudokuCell> HAS_VALID_SECTIONS
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

		canBruteForceSolve(0);
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

		SudokuCell initialSudokuCell = new SudokuCell(sudokuCell);
		Set<Integer> possibleValuesToTry
				= initialSudokuCell.getPossibleValues();
		for (Integer possibleValue : possibleValuesToTry)
		{
			sudokuCell.setValue(possibleValue);

			if (HAS_VALID_SECTIONS.test(sudokuCell)
					&& canBruteForceSolve(index + 1))
			{
				return true;
			}

			sudokuCell.setValue(null);
		}
		// restore possible values:
		sudokuCell.resetFrom(initialSudokuCell);

		return false;
	}

	private boolean hasDuplicateValues(List<SudokuCell> sudokuCells)
	{
		int[] cellValues = sudokuCells.stream()
				.filter(sudokuCell -> sudokuCell.getValue() != null)
				.mapToInt(SudokuCell::getValue)
				.toArray();

		return cellValues.length
				!= Arrays.stream(cellValues).distinct().count();
	}
}
