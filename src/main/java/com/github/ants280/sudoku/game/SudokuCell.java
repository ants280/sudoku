package com.github.ants280.sudoku.game;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class SudokuCell
{
	public static final Set<Integer> LEGAL_CELL_VALUES
			= IntStream.rangeClosed(1, 9)
					.boxed()
					.collect(Collectors.toSet());
	private final int rowNumber;
	private final int columnNumber;
	private final int groupNumber;

	public SudokuCell(int rowNumber, int columnNumber, int groupNumber)
	{
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
		this.groupNumber = groupNumber;
	}

	public int getRowNumber()
	{
		return rowNumber;
	}

	public int getColumnNumber()
	{
		return columnNumber;
	}

	public int getGroupNumber()
	{
		return groupNumber;
	}

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
	public abstract boolean addPossibleValue(int value);

	protected static void validateValue(Integer value)
			throws IllegalArgumentException
	{
		if (value != null && !LEGAL_CELL_VALUES.contains(value))
		{
			throw new IllegalArgumentException("Invalid value: " + value);
		}
	}

	@Override
	public String toString()
	{
		return getValue() == null
				? getPossibleValues().toString()
				: getValue().toString();
	}
}
