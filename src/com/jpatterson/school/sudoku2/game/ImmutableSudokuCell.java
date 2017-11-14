package com.jpatterson.school.sudoku2.game;

import java.util.Collections;
import java.util.Set;

public class ImmutableSudokuCell implements SudokuCell
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
	public void setValue(Integer value)
	{
		throw new UnsupportedOperationException("Value cannot be set");
	}

	@Override
	public void resetPossibleValues()
	{
		// NOOP
	}

	@Override
	public void removePossibleValue(int value)
	{
		// NOOP
	}
}
