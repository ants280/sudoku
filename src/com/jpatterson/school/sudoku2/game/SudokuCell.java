package com.jpatterson.school.sudoku2.game;

import java.util.Set;

public interface SudokuCell
{
	Integer getValue();

	Set<Integer> getPossibleValues();
	
	void setValue(Integer value);
	
	void resetPossibleValues();
	
	void removePossibleValue(int value);

	static void validateValue(Integer value) throws IllegalArgumentException
	{
		if (value != null && (value < 1 || value > 9))
		{
			throw new IllegalArgumentException("Invalid value: " + value);
		}
	}
}
