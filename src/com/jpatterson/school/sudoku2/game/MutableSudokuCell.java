package com.jpatterson.school.sudoku2.game;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MutableSudokuCell extends SudokuCell
{
	private static final Collection<Integer> ALL_POSSIBLE_VALUES = IntStream.rangeClosed(1, 9)
		.boxed()
		.collect(Collectors.toList());

	private Integer value;
	private final Set<Integer> possibleValues;

	public MutableSudokuCell()
	{
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
	public void setValue(Integer value)
	{
		SudokuCell.validateValue(value);

		this.value = value;

		if (value == null)
		{
			resetPossibleValues();
		}
		else
		{
			possibleValues.clear();
		}
	}

	@Override
	public void resetPossibleValues()
	{
		possibleValues.addAll(ALL_POSSIBLE_VALUES);
	}

	@Override
	public void removePossibleValue(int value)
	{
		SudokuCell.validateValue(value);

		possibleValues.remove(value);
	}

	@Override
	public void addPossibleValue(int value)
	{
		SudokuCell.validateValue(value);
		
		possibleValues.add(value);
	}
}
