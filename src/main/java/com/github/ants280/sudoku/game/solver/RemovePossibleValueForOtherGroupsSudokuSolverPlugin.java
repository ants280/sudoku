package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RemovePossibleValueForOtherGroupsSudokuSolverPlugin
		extends SudokuSolverPlugin
{
	public RemovePossibleValueForOtherGroupsSudokuSolverPlugin(
			SudokuBoard sudokuBoard,
			Consumer<String> moveDescriptionConsumer)
	{
		super(sudokuBoard, moveDescriptionConsumer);
	}

	@Override
	public boolean makeMove()
	{
		for (int groupIndex = 0; groupIndex < 9; groupIndex++)
		{
			List<SudokuCell> groupCells
					= sudokuBoard.getSudokuCells(
							SectionType.GROUP,
							groupIndex);

			if (this.tryToRemovePossibleValuesForOtherGroups(
					groupCells,
					groupIndex,
					SectionType.COLUMN)
					|| this.tryToRemovePossibleValuesForOtherGroups(
							groupCells,
							groupIndex,
							SectionType.ROW))
			{
				return true;
			}
		}

		return false;
	}

	private boolean tryToRemovePossibleValuesForOtherGroups(
			List<SudokuCell> groupCells,
			int groupIndex,
			SectionType sectionType)
	{
		List<SudokuCell> valuelessGroupCells
				= groupCells.stream()
						.filter(sudokuCell -> sudokuCell.getValue() == null)
						.collect(Collectors.toList());
		Map<Integer, List<SudokuCell>> cellsInGroupBySectionType
				= valuelessGroupCells.stream()
						.collect(Collectors.groupingBy(
								sudokuCell -> sudokuCell.getIndex(
										sectionType)));

		for (Map.Entry<Integer, List<SudokuCell>> sectionTypeInGroupEntry
				: cellsInGroupBySectionType.entrySet())
		{
			Integer sectionTypeIndex = sectionTypeInGroupEntry.getKey();
			List<SudokuValue> possibleValuesInSectionTypeIndex
					= sectionTypeInGroupEntry.getValue()
							.stream()
							.map(SudokuCell::getPossibleValues)
							.flatMap(Collection::stream)
							.distinct()
							.collect(Collectors.toList());
			for (SudokuValue possibleValue : possibleValuesInSectionTypeIndex)
			{
				boolean possibleValueOnlyInSectionTypeIndex = valuelessGroupCells.stream()
						.filter(groupCell -> groupCell.getIndex(sectionType) != sectionTypeIndex)
						.allMatch(groupCell -> !groupCell.getPossibleValues().contains(possibleValue));

				if (possibleValueOnlyInSectionTypeIndex)
				{
					List<SudokuCell> otherCellsInSectionType
							= sudokuBoard.getSudokuCells(sectionType, sectionTypeIndex)
									.stream()
									.filter(sudokuCell -> sudokuCell.getValue() == null
									&& sudokuCell.getIndex(SectionType.GROUP) != groupIndex
									&& sudokuCell.hasPossibleValue(possibleValue))
									.collect(Collectors.toList());

					if (!otherCellsInSectionType.isEmpty())
					{
						String moveDescription = String.format(
								"Removed possible value of %s from cells "
								+ "in %s %d, but not in group %d "
								+ "because the possible value must be "
								+ "in group %d for that %s.",
								possibleValue.getDisplayValue(),
								sectionType.getDisplayValue(),
								sectionTypeIndex,
								groupIndex,
								groupIndex,
								sectionType.getDisplayValue());
						this.logMove(moveDescription);

						otherCellsInSectionType
								.forEach(sudokuCell -> sudokuCell.togglePossibleValue(possibleValue));

						return true;
					}
				}
			}
		}

		return false;
	}
}
