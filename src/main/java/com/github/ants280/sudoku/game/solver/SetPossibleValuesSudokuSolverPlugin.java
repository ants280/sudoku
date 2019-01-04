package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An advanced version of the the CullPossibleValuesSudokuSolverPlugin which
 * checks for subsets of 'n' possible values which are shared between 'n' cells
 * in a section, but are present in other cells in the same section, which can
 * be culled.
 *
 * Also similar to the OnlyPossibleValueSudokuSolverPLugin, but this checks for
 * multiple values.
 */
public class SetPossibleValuesSudokuSolverPlugin extends SudokuSolverPlugin
{
	public SetPossibleValuesSudokuSolverPlugin(
			SudokuBoard sudokuBoard,
			Consumer<String> moveDescriptionConsumer)
	{
		super(sudokuBoard, moveDescriptionConsumer);
	}

	@Override
	public boolean makeMove()
	{
		for (SectionType sectionType : SectionType.values())
		{
			for (int index = 0; index < 9; index++)
			{
				if (this.didSetPossibleValues(sectionType, index))
				{
					return true;
				}
			}
		}

		return false;
	}

	private boolean didSetPossibleValues(SectionType sectionType, int index)
	{
		List<SudokuCell> sudokuCells
				= sudokuBoard.getSudokuCells(sectionType, index)
						.stream()
						.filter(sudokuCell -> sudokuCell.getValue() == null)
						.collect(Collectors.toList());

		Map<SudokuCell, Collection<SudokuValue>> cellPossibleValues
				= sudokuCells.stream()
						.collect(Collectors.toMap(
								Function.identity(),
								SudokuCell::getPossibleValues));

		List<SudokuValue> possibleValuesInSection = cellPossibleValues.values()
				.stream()
				.flatMap(Collection::stream)
				.distinct()
				.collect(Collectors.toList());

		// NOTE: if size == 1, use OnlyPossibleValueSudokuSolverPlugin
		if (possibleValuesInSection.size() < 2)
		{
			return false;
		}

		// test groups of possible values
		Iterator<Collection<SudokuValue>> possibleValuesIterator
				= new PossibleValuesIterator(possibleValuesInSection);

		while (possibleValuesIterator.hasNext())
		{
			Collection<SudokuValue> possibleValues
					= possibleValuesIterator.next();

			Map<SudokuCell, Collection<SudokuValue>> targetCells
					= cellPossibleValues.entrySet()
							.stream()
							.filter(possibleCellValuesEntry -> possibleCellValuesEntry.getValue().containsAll(possibleValues))
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			if (possibleValues.size() != targetCells.size()
					|| cellPossibleValues.entrySet()
							.stream()
							.anyMatch(cellPossibleValueEntry -> !targetCells.containsKey(cellPossibleValueEntry.getKey())
							&& cellPossibleValueEntry.getValue().stream().anyMatch(possibleValues::contains)))
			{
				continue;
			}

			List<Map.Entry<SudokuCell, Collection<SudokuValue>>> cellsToTrim
					= targetCells.entrySet()
							.stream()
							.filter(possibleCellValuesEntry -> !possibleValues.equals(possibleCellValuesEntry.getValue()))
							.collect(Collectors.toList());

			if (!cellsToTrim.isEmpty())
			{
				String moveDescription = String.format(
						"Trimming the possible values of some cells "
						+ "in %s %d to %s because those possible values "
						+ "must occupy the %d cells.",
						sectionType.getDisplayValue(),
						index + 1,
						possibleValues.stream()
								.map(SudokuValue::getDisplayValue)
								.collect(Collectors.toList()),
						possibleValues.size());
				this.logMove(moveDescription);

				cellsToTrim.forEach(cellToTrimEntry -> cellToTrimEntry.getValue()
						.forEach(possibleValue ->
						{
							if (!possibleValues.contains(possibleValue))
							{
								cellToTrimEntry.getKey().togglePossibleValue(possibleValue);
							}
						}));

				return true;
			}
		}

		return false;
	}

}
