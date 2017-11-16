package com.jpatterson.school.sudoku2.game;

import com.jpatterson.school.sudoku2.ui.Sudoku;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

			if (!valueFound) // level 3
			{
				valueFound = cullPossibleValues();
			}
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
					if (sudokuCell.getPossibleValues().size() == 1) // level 1
					{
						Integer value = sudokuCell
							.getPossibleValues()
							.iterator()
							.next();

						if (Sudoku.DEBUG)
						{
							System.out.printf("SOLVER: "
								+ "Setting value of %d at [%d,%d] "
								+ "because it is the only possible "
								+ "value in the cell",
								value, r + 1, c + 1);
						}

						sudokuCell.setValue(value);
						valueSet = true;
					}
					else // level 2
					{
						int groupNumber = board.getGroupNumber(r, c);
						for (Integer possibleValue : sudokuCell.getPossibleValues())
						{
							if (noPossibleValuesInCollectionContain(board.getOtherSudokuCellsForGroup(groupNumber, r, c), possibleValue)
								|| noPossibleValuesInCollectionContain(board.getOtherSudokuCellsForRow(r, c), possibleValue)
								|| noPossibleValuesInCollectionContain(board.getOtherSudokuCellsForCol(c, r), possibleValue))
							{
								if (Sudoku.DEBUG)
								{
									System.out.printf("SOLVER: "
										+ "Setting value of %d at [%d,%d] "
										+ "because it is the only possible "
										+ "spot for the value in the "
										+ "group, row, or column.\n",
										possibleValue, r + 1, c + 1);
								}

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

	/**
	 * If two cells in a row, col, or group (the "collection") have possible
	 * values a&b, no other cells in that collection can have those possible
	 * values. If this situation exists, the 'n' possible values that can only
	 * be in the 'n' cells are removed from other cells in the collection. In
	 * this situation, let 'n' = 2 (a&b). The same applies for situations with n
	 * > 2.
	 *
	 * @return whether or not any possible values were culled.
	 */
	private boolean cullPossibleValues()
	{
		boolean valuesCulled = false;
		for (int i = 0; i < 9; i++)
		{
			// row
//			List<SudokuCell> rowSudokuCells = board.getSudokuCellsForRow(i);
			
			// col
			
			// group
		}
		return valuesCulled;
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

	private boolean noPossibleValuesInCollectionContain(
		Collection<SudokuCell> otherSudokuCells, Integer possibleValue)
	{
		return otherSudokuCells.stream()
			.noneMatch(otherSudokuCell -> otherSudokuCell.getPossibleValues()
				.contains(possibleValue));
	}
}
