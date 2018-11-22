package com.github.ants280.sudoku.game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MutableSudokuCell extends SudokuCell
{
	private Integer value;
	private final Set<Integer> possibleValues;

	public MutableSudokuCell(int rowIndex, int columnIndex, int groupIndex)
	{
		super(rowIndex, columnIndex, groupIndex);

		this.value = null;
		this.possibleValues = new HashSet<>();
	}

	@Override
	public Integer getValue()
	{
		return value;
	}

	@Override
	public Set<Integer> getPossibleValues()
	{
		return Collections.unmodifiableSet(possibleValues);
	}

	@Override
	public boolean setValue(Integer value)
	{
		SudokuCell.validateValue(value);

		Integer oldValue = this.value;
		this.value = value;

		possibleValues.clear();

		return this.value == null
				? oldValue != null
				: !this.value.equals(oldValue);
	}

	@Override
	public void resetPossibleValues()
	{
		possibleValues.addAll(SudokuCell.LEGAL_CELL_VALUES);
	}

	@Override
	public boolean removePossibleValue(int value)
	{
		SudokuCell.validateValue(value);

		return possibleValues.remove(value);
	}

	@Override
	public boolean addPossibleValue(int value)
	{
		SudokuCell.validateValue(value);

		return possibleValues.add(value);
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 83 * hash + Objects.hashCode(this.value);
		hash = 83 * hash + Objects.hashCode(this.possibleValues);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final MutableSudokuCell other = (MutableSudokuCell) obj;
		if (!Objects.equals(this.value, other.value))
		{
			return false;
		}
		if (!Objects.equals(this.possibleValues, other.possibleValues))
		{
			return false;
		}
		return true;
	}
}
