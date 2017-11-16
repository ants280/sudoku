package com.jpatterson.school.sudoku2.game;

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
		sleep();
		
		boolean valueFound = false;
		
		do
		{
			updatePossibleValues();
			
			setBoardValues();
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
					int rowNumber = board.getRowNumber(r, c);
					int colNumber = board.getColNumber(r, c);
					int groupNumber = board.getGroupNumber(r, c);
					
					for (Integer possibleValue : sudokuCell.getPossibleValues())
					{
					}
				}
			}
		}
	}
	
	private void setBoardValues()
	{
		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				final SudokuCell sudokuCell = board.getSudokuCell(r, c);
				if (sudokuCell.getValue() == null
					&& sudokuCell.getPossibleValues().size() == 1)
				{
					Integer value = sudokuCell
						.getPossibleValues()
						.iterator()
						.next();
					sudokuCell.setValue(value);
				}
			}
		}
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
}
