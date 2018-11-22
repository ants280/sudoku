package com.github.ants280.sudoku.game;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class SudokuCell
{
	public static final Set<Integer> LEGAL_CELL_VALUES
			= IntStream.rangeClosed(1, 9)
					.boxed()
					.collect(Collectors.toSet());
	private final Map<SectionType, Integer> sectionTypeIndices;

	public SudokuCell(int rowIndex, int columnIndex, int groupIndex)
	{
		SudokuBoard.validateIndices(rowIndex, columnIndex, groupIndex);
		this.sectionTypeIndices = new EnumMap<>(SectionType.class);
		sectionTypeIndices.put(SectionType.ROW, rowIndex);
		sectionTypeIndices.put(SectionType.COLUMN, columnIndex);
		sectionTypeIndices.put(SectionType.GROUP, groupIndex);
	}

	public int getIndex(SectionType sectionType)
	{
		return sectionTypeIndices.get(sectionType);
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
