package com.github.ants280.sudoku.game;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuCell
{
	private static final Collection<Integer> LEGAL_CELL_VALUES
			= IntStream.rangeClosed(1, 9)
					.boxed()
					.collect(Collectors.toList());
	private final Map<SectionType, Integer> sectionTypeIndices;
	private Integer value;
	private boolean locked;
	// TODO: make this be a binary string to simplify toggling.  Also sholud reduce dependence on getPossibleValues...
	private final Set<Integer> possibleValues;

	public SudokuCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			Integer value,
			boolean locked)
	{
		this.sectionTypeIndices = new EnumMap<>(SectionType.class);
		sectionTypeIndices.put(SectionType.ROW, rowIndex);
		sectionTypeIndices.put(SectionType.COLUMN, columnIndex);
		sectionTypeIndices.put(SectionType.GROUP, groupIndex);

		this.value = value;
		this.locked = locked;
		this.possibleValues = new HashSet<>();
	}

	public int getIndex(SectionType sectionType)
	{
		return sectionTypeIndices.get(sectionType);
	}

	public Integer getValue()
	{
		return value;
	}

	public Collection<Integer> getPossibleValues()
	{
		return Collections.unmodifiableSet(possibleValues);
	}

	public boolean hasPossibleValue(int value)
	{
		return possibleValues.contains(value);
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setValue(Integer value)
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Cannot set value of locked SudokuCell.");
		}

		SudokuCell.validateValue(value);

		this.value = value;
	}

	public void clearPossibleValues()
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Reset possible values of a locked SudokuCell.");
		}

		possibleValues.clear();
	}

	public void restoreAllPossibleValues()
	{
		LEGAL_CELL_VALUES.stream()
				.filter(v -> !this.hasPossibleValue(v))
				.forEach(this::togglePossibleValue);
	}

	public void togglePossibleValue(int value)
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Cannot toggle possible values of locked SudokuCell.");
		}

		SudokuCell.validateValue(value);

		if (hasPossibleValue(value))
		{
			possibleValues.add(value);
		}
		else
		{
			possibleValues.remove(value);
		}
	}

	public void setLocked(boolean locked)
	{
		if (locked && value == null)
		{
			throw new IllegalArgumentException(
					"Cannot lock an empty SudokuCell.");
		}

		this.locked = locked;

		this.possibleValues.clear();
	}

	private static void validateValue(Integer value)
			throws IllegalArgumentException
	{
		if (value != null && !LEGAL_CELL_VALUES.contains(value))
		{
			throw new IllegalArgumentException("Invalid value: " + value);
		}
	}

	public void resetFrom(SudokuCell otherSudokuCell)
	{
		value = otherSudokuCell.getValue();

		possibleValues.clear();
		possibleValues.addAll(otherSudokuCell.getPossibleValues());

		locked = otherSudokuCell.isLocked();
	}

	@Override
	public String toString()
	{
		return getValue() == null
				? getPossibleValues().toString()
				: getValue().toString();
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 17 * hash + Objects.hashCode(this.sectionTypeIndices);
		hash = 17 * hash + Objects.hashCode(this.value);
		hash = 17 * hash + (this.locked ? 1 : 0);
		hash = 17 * hash + Objects.hashCode(this.possibleValues);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass())
		{
			return false;
		}

		final SudokuCell other = (SudokuCell) obj;
		return this.locked == other.isLocked()
				&& Objects.equals(
						this.sectionTypeIndices,
						other.sectionTypeIndices)
				&& Objects.equals(
						this.value,
						other.value)
				&& Objects.equals(
						this.possibleValues,
						other.possibleValues);
	}
}
