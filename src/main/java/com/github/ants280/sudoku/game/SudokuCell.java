package com.github.ants280.sudoku.game;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SudokuCell
{
	private final Map<SectionType, Integer> sectionTypeIndices;
	private SudokuValue value;
	private boolean locked;
	private final Set<SudokuValue> possibleValues;

	public SudokuCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			SudokuValue value,
			boolean locked)
	{
		if (locked && value == null)
		{
			throw new IllegalArgumentException(
					"Cannot lock an empty SudokuCell.");
		}

		this.sectionTypeIndices = new EnumMap<>(SectionType.class);
		sectionTypeIndices.put(SectionType.ROW, rowIndex);
		sectionTypeIndices.put(SectionType.COLUMN, columnIndex);
		sectionTypeIndices.put(SectionType.GROUP, groupIndex);

		this.value = value;
		this.locked = locked;
		this.possibleValues = EnumSet.noneOf(SudokuValue.class);
	}

	public int getIndex(SectionType sectionType)
	{
		return sectionTypeIndices.get(sectionType);
	}

	public SudokuValue getValue()
	{
		return value;
	}

	public Collection<SudokuValue> getPossibleValues()
	{
		return EnumSet.copyOf(possibleValues);
	}

	public boolean hasPossibleValue(SudokuValue value)
	{
		return possibleValues.contains(value);
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setValue(SudokuValue value)
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Cannot set value of locked SudokuCell.");
		}

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
		if (locked)
		{
			throw new IllegalArgumentException(
					"Restoring possible values of a locked SudokuCell.");
		}

		Arrays.stream(SudokuValue.values())
				.filter(v -> !this.hasPossibleValue(v))
				.forEach(this::togglePossibleValue);
	}

	public void togglePossibleValue(SudokuValue value)
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Cannot toggle possible values of locked SudokuCell.");
		}

		if (this.hasPossibleValue(value))
		{
			possibleValues.remove(value);
		}
		else
		{
			possibleValues.add(value);
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
		return String.format("SudokuCell{r%d,c%d,g%d,v=%d,possibleValues=[%s],locked=%s}",
				this.getIndex(SectionType.ROW),
				this.getIndex(SectionType.COLUMN),
				this.getIndex(SectionType.GROUP),
				value == null ? 0 : value.getValue(),
				possibleValues.stream()
						.mapToInt(SudokuValue::getValue)
						.mapToObj(String::valueOf)
						.collect(Collectors.joining()),
				locked ? "Y" : "N");
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
		return this == obj
				|| obj != null
				&& this.getClass() == obj.getClass()
				&& this.locked == ((SudokuCell) obj).locked
				&& Objects.equals(
						this.sectionTypeIndices,
						((SudokuCell) obj).sectionTypeIndices)
				&& Objects.equals(
						this.value,
						((SudokuCell) obj).value)
				&& Objects.equals(
						this.possibleValues,
						((SudokuCell) obj).possibleValues);
	}
}
