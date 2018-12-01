package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
	public CullPossibleValuesSudokuSolverPlugin(SudokuBoard sudokuBoard)
	{
		super(sudokuBoard);
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

				List<Collection<Integer>> possibleValueGroups
						= getPossibleValueGroups(sudokuCells);

				for (Collection<Integer> possibleValues : possibleValueGroups)
				{
					for (int possibleValue : possibleValues)
					{
						List<SudokuCell> sudokuCellsToCull
								= this.getCellsToCull(
										sudokuCells,
										possibleValues,
										possibleValue);

						if (!sudokuCellsToCull.isEmpty())
						{
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

	private List<Collection<Integer>> getPossibleValueGroups(List<SudokuCell> sudokuCells)
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
			Collection<Integer> possibleValues,
			int possibleValue)
	{
		return sudokuCells.stream()
				.filter(sudokuCell -> sudokuCell.getValue() == null
				&& !possibleValues.equals(sudokuCell.getPossibleValues())
				&& sudokuCell.hasPossibleValue(possibleValue))
				.collect(Collectors.toList());
	}
}
