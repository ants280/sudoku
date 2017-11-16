package com.jpatterson.school.sudoku2.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuSolver
{
	private final SudokuBoard board;

	public SudokuSolver(SudokuBoard board)
	{
		this.board = board;
	}

	public void start()
	{
		resetPossibleValues();
		//sleep();  //TODO: it would be nice to report back to the parent popup every time a value is found.  maybe the parent should control this so users can pause between found values if desired

		boolean valueFound;

		do
		{
			updatePossibleValues();

			valueFound = setBoardValues();
		}
		while (valueFound);
	}

	private void resetPossibleValues()
	{
		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				board.getSudokuCell(r, c).resetPossibleValues();
			}
		}
	}

	private void updatePossibleValues()
	{
		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				SudokuCell sudokuCell = board.getSudokuCell(r, c);
				if (sudokuCell.getValue() == null)
				{
					int groupNumber = board.getGroupNumber(r, c);
					Set<Integer> usedValues = new HashSet<>();

					usedValues.addAll(board.getValuesForRow(r));
					usedValues.addAll(board.getValuesForCol(c));
					usedValues.addAll(board.getValuesForGroup(groupNumber));

					usedValues.stream()
						.forEach(sudokuCell::removePossibleValue);
				}
			}
		}
	}

	private boolean setBoardValues()
	{
		boolean valueSet = false;
		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				final SudokuCell sudokuCell = board.getSudokuCell(r, c);
				if (sudokuCell.getValue() == null)
				{
					if (sudokuCell.getPossibleValues().size() == 1)
					{
						Integer value = sudokuCell
							.getPossibleValues()
							.iterator()
							.next();
						sudokuCell.setValue(value);
						valueSet = true;
					}
					else
					{
						for (Integer possibleValue : sudokuCell.getPossibleValues())
						{
							if (noPossibleValuesInCollectionContain(getOtherSudokuCellsForGroup(r, c), possibleValue)
								|| noPossibleValuesInCollectionContain(getOtherSudokuCellsForRow(r, c), possibleValue)
								|| noPossibleValuesInCollectionContain(getOtherSudokuCellsForCol(r, c), possibleValue))
							{
								sudokuCell.setValue(possibleValue);
								valueSet = true;
								break;
							}
						}
					}
				}
			}
		}
		
		return valueSet;
	}

	private void sleep()
	{
		try
		{
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		}
		catch (InterruptedException ex)
		{
			System.err.println(ex);
		}
	}

	private Collection<SudokuCell> getOtherSudokuCellsForRow(int r, int c)
	{
		return IntStream.range(0, 9)
			.filter(r2 -> r2 != r)
			.mapToObj(r2 -> board.getSudokuCell(r2, c))
			.collect(Collectors.toList());
	}

	private Collection<SudokuCell> getOtherSudokuCellsForCol(int r, int c)
	{
		return IntStream.range(0, 9)
			.filter(c2 -> c2 != c)
			.mapToObj(c2 -> board.getSudokuCell(r, c2))
			.collect(Collectors.toList());
	}

	private Collection<SudokuCell> getOtherSudokuCellsForGroup(int r, int c)
	{
		int groupNumber = board.getGroupNumber(r, c);
		int startingRow = 3 * (groupNumber / 3);
		int startingCol = 3 * (groupNumber % 3);
		return IntStream.range(startingRow, startingRow + 3)
			.mapToObj(r2 -> IntStream.range(startingCol, startingCol + 3)
				.filter(c2 -> r2 != r || c2 != c)
				.mapToObj(c2 -> board.getSudokuCell(r2, c2)))
			.flatMap(Function.identity())
			.collect(Collectors.toList());
	}

	private boolean noPossibleValuesInCollectionContain(
		Collection<SudokuCell> otherSudokuCells, Integer possibleValue)
	{
		return otherSudokuCells.stream()
			.noneMatch(otherSudokuCell -> otherSudokuCell.getPossibleValues()
				.contains(possibleValue));
	}
}
