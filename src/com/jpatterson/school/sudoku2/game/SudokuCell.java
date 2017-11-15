package com.jpatterson.school.sudoku2.game;

import java.util.Set;

public abstract class SudokuCell
{
	public abstract Integer getValue();

	public abstract Set<Integer> getPossibleValues();

	/**
	 * @param value The value to set.
	 * @return Whether or not the value changed.
	 */
	public abstract boolean setValue(Integer value);

	public abstract void resetPossibleValues();

	/**
	 * @param value The possible value to remove.
	 * @return Whether or not the possible value was removed (and was previously
	 * present).
	 */
	public abstract boolean removePossibleValue(int value);

	/**
	 * @param value The possible value to add.
	 * @return Whether or not the possible value was added (and was not already
	 * present).
	 */
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
