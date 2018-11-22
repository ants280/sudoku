package com.github.ants280.sudoku.game;

import java.util.Collections;
import java.util.Set;

public class ImmutableSudokuCell extends SudokuCell
{
	private final int value;

	public ImmutableSudokuCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			int cellValue)
	{
		super(rowIndex, columnIndex, groupIndex);

		SudokuCell.validateValue(cellValue);
		this.value = cellValue;
	}

	@Override
	public Integer getValue()
	{
		return value;
	}

	@Override
	public Set<Integer> getPossibleValues()
	{
		return Collections.emptySet();
	}

	@Override
	public boolean setValue(Integer value)
	{
		return false;
	}

	@Override
	public void resetPossibleValues()
	{
		// NOOP
	}

	@Override
	public boolean removePossibleValue(int value)
	{
		return false;
	}

	@Override
	public boolean addPossibleValue(int value)
	{
		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 71 * hash + this.value;
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
		final ImmutableSudokuCell other = (ImmutableSudokuCell) obj;
		if (this.value != other.value)
		{
			return false;
		}
		return true;
	}

}
