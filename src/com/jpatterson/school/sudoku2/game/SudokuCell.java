package com.jpatterson.school.sudoku2.game;

import java.util.Set;

public abstract class SudokuCell
{
	public abstract Integer getValue();

	public abstract Set<Integer> getPossibleValues();

	public abstract void setValue(Integer value);

	public abstract void resetPossibleValues();

	public abstract boolean removePossibleValue(int value);
	
	public abstract boolean addPossibleValue(int value); // TODO: The possibleValue methods are only aplicable to MutableSudokuCell maybe put these methods theer?
	
	public final boolean isEmpty()
	{
		return getValue() == null;
	}

	protected static void validateValue(Integer value) throws IllegalArgumentException
	{
		if (value != null && (value < 1 || value > 9))
		{
			throw new IllegalArgumentException("Invalid value: " + value);
		}
	}
}