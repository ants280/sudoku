package com.github.ants280.sudoku.game;

import java.util.Collections;
import java.util.Set;

public class ImmutableSudokuCell extends SudokuCell
{
	private final int value;

	public ImmutableSudokuCell(int value)
	{
		SudokuCell.validateValue(value);
		this.value = value;
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
}
