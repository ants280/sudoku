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
public class CullPussibleValuesSudokuSolverPlugin extends SudokuSolverPlugin
{
	public CullPussibleValuesSudokuSolverPlugin(SudokuBoard sudokuBoard)
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

				List<Collection<Integer>> possibleValueGroups = sudokuCells.stream()
						.map(SudokuCell::getPossibleValues)
						.filter(collection -> !collection.isEmpty())
						.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet() // Map.Entry<Set<Integer>, Long>
						.stream()
						.filter(entry -> entry.getValue() > 1 && entry.getKey().size() == entry.getValue())
						.map(Map.Entry::getKey)
						.collect(Collectors.toList());

				for (Collection<Integer> possibleValues : possibleValueGroups)
				{
					for (int possibleValue : possibleValues)
					{
						List<SudokuCell> sudokuCellsToCull = sudokuCells.stream()
								.filter(sudokuCell -> !possibleValues.equals(sudokuCell.getPossibleValues())
								&& sudokuCell.getPossibleValues().contains(possibleValue))
								.collect(Collectors.toList());

						if (!sudokuCellsToCull.isEmpty())
						{
							sudokuCellsToCull.forEach(sudokuCell
									-> sudokuCell.removePossibleValue(possibleValue));

							return true;
						}
					}
				}

			}
		}

		return false;
	}
}
