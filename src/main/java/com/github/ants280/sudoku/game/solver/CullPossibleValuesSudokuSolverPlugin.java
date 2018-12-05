package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A plugin that removes possible values from sections (SectionTypes) when
 * possible. If multiple cells in a section (n cells) that contain the same m
 * possible values and n = m and other cells in the section contain any of the
 * same possible values, the possible values can be removed from the cells not
 * in the group of n.
 *
 * For example, if three cells in a group have only the possible values 4, 5,
 * and 6, those possible values can be removed from all other sets of possible
 * values in the group.
 */
public class CullPossibleValuesSudokuSolverPlugin extends SudokuSolverPlugin
{
	public CullPossibleValuesSudokuSolverPlugin(
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
				List<SudokuCell> sudokuCells
						= sudokuBoard.getSudokuCells(sectionType, index);

				List<Collection<SudokuValue>> possibleValueGroups
						= this.getPossibleValueGroups(sudokuCells);

				for (Collection<SudokuValue> possibleValues : possibleValueGroups)
				{
					for (SudokuValue possibleValue : possibleValues)
					{
						List<SudokuCell> sudokuCellsToCull
								= this.getCellsToCull(
										sudokuCells,
										possibleValues,
										possibleValue);

						if (!sudokuCellsToCull.isEmpty())
						{
							String moveDescription = String.format(
									"Removed possible value of %s "
									+ "from some cells in %s %d "
									+ "because other cells in the %s "
									+ "must have possible values of %s",
									possibleValue,
									sectionType.getDisplayValue(),
									index + 1,
									sectionType.getDisplayValue(),
									possibleValues.stream()
											.map(SudokuValue::getDisplayValue)
											.collect(Collectors.toList()));
							moveDescriptionConsumer.accept(moveDescription);

							sudokuCellsToCull.forEach(sudokuCell
									-> sudokuCell.togglePossibleValue(possibleValue));

							return true;
						}
					}
				}

			}
		}

		return false;
	}

	private List<Collection<SudokuValue>> getPossibleValueGroups(List<SudokuCell> sudokuCells)
	{
		return sudokuCells.stream()
				.map(SudokuCell::getPossibleValues)
				.filter(collection -> !collection.isEmpty())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
				.entrySet() // Map.Entry<Set<Integer>, Long>
				.stream()
				.filter(entry -> entry.getValue() > 1 && entry.getKey().size() == entry.getValue())
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	private List<SudokuCell> getCellsToCull(
			List<SudokuCell> sudokuCells,
			Collection<SudokuValue> possibleValues,
			SudokuValue possibleValue)
	{
		return sudokuCells.stream()
				.filter(sudokuCell -> sudokuCell.getValue() == null
				&& !possibleValues.equals(sudokuCell.getPossibleValues())
				&& sudokuCell.hasPossibleValue(possibleValue))
				.collect(Collectors.toList());
	}
}
