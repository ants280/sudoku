package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class LastPossibleValueInSectionSudokuSolverPlugin extends SudokuSolverPlugin
{
	private final BiConsumer<SudokuCell, SudokuValue> removeNearbyPossibleValuesConsumer;

	public LastPossibleValueInSectionSudokuSolverPlugin(
			SudokuBoard sudokuBoard,
			BiConsumer<SudokuCell, SudokuValue> removeNearbyPossibleValuesConsumer)
	{
		super(sudokuBoard);

		this.removeNearbyPossibleValuesConsumer
				= removeNearbyPossibleValuesConsumer;
	}

	@Override
	public boolean makeMove()
	{
		for (SudokuCell sudokuCell : sudokuBoard.getAllSudokuCells())
		{
			if (sudokuCell.getValue() == null)
			{
				for (SudokuValue possibleValue : sudokuCell.getPossibleValues())
				{
					boolean onlyPossibleValueInASection = Arrays.stream(SectionType.values())
							.map(sectionType -> sudokuBoard.getSudokuCells(sectionType, sudokuCell.getIndex(sectionType)))
							.anyMatch(sudokuCells -> sudokuCells.stream()
							.allMatch(otherSudokuCell -> otherSudokuCell.equals(sudokuCell)
							|| otherSudokuCell.getValue() != null
							|| !otherSudokuCell.hasPossibleValue(possibleValue)));

					if (onlyPossibleValueInASection)
					{
						sudokuCell.setValue(possibleValue);

						removeNearbyPossibleValuesConsumer.accept(sudokuCell, possibleValue);

						return true;
					}
				}
			}
		}

		return false;
	}
}
