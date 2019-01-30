package com.github.ants280.sudoku.game.solver.plugins;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import com.github.ants280.sudoku.game.solver.SudokuSolverPlugin;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LastPossibleValueInSectionSudokuSolverPlugin extends SudokuSolverPlugin
{
	private final BiConsumer<SudokuCell, SudokuValue> removeNearbyPossibleValuesConsumer;

	public LastPossibleValueInSectionSudokuSolverPlugin(
			SudokuBoard sudokuBoard,
			Consumer<String> moveDescriptionConsumer,
			BiConsumer<SudokuCell, SudokuValue> removeNearbyPossibleValuesConsumer)
	{
		super(sudokuBoard, moveDescriptionConsumer);

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
						String moveDescription = String.format(
								"Setting value of cell at [r,c]=[%d,%d] to %s "
								+ "because it is the last place "
								+ "in one of the sections the cell is in "
								+ "that a %s can go.",
								sudokuCell.getIndex(SectionType.ROW) + 1,
								sudokuCell.getIndex(SectionType.COLUMN) + 1,
								possibleValue.getDisplayValue(),
								possibleValue.getDisplayValue());
						this.logMove(moveDescription);

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
