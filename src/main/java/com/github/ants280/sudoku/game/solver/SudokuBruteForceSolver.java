package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SudokuBruteForceSolver extends SudokuSolver
{
	private static final Predicate<List<SudokuCell>> HAS_NO_DUPLICATE_VALUES
			= sudokuCells -> sudokuCells.size()
			== sudokuCells.stream().distinct().count();
	private final Predicate<SudokuCell> HAS_VALID_SECTIONS
			= sudokuCell -> Arrays.stream(SectionType.values())
					.map(sectionType -> sudokuBoard.getSudokuCells(
					sectionType,
					sudokuCell.getIndex(sectionType)))
					.allMatch(HAS_NO_DUPLICATE_VALUES);

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
//		SudokuBoard initialBoard = new SudokuBoard(sudokuBoard);

		super.initialize();

		boolean bruteForceSolverSuccess = canBruteForceSolve(0);

//		if (!bruteForceSolverSuccess)
//		{
//			sudokuBoard.resetFrom(initialBoard); // TODO: is this needed?
//		}
	}

	private boolean canBruteForceSolve(int index)
	{
		List<SudokuCell> allSudokuCells = sudokuBoard.getAllSudokuCells();
		if (index >= allSudokuCells.size())
		{
			return sudokuBoard.isSolved();
		}

		SudokuCell sudokuCell = allSudokuCells.get(index);
		Set<Integer> possibleValuesToTry
				= new HashSet<>(sudokuCell.getPossibleValues());
		for (Integer possibleValue : possibleValuesToTry)
		{
			sudokuCell.setValue(possibleValue);

			if (HAS_VALID_SECTIONS.test(sudokuCell)
					&& canBruteForceSolve(index + 1))
			{
				return true;
			}
			else
			{
				sudokuCell.setValue(null);
			}
		}

		return false;
	}
}
