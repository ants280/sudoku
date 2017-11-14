package com.jpatterson.school.sudoku2.game;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuCell
{
	private final Collection<Integer> ALL_POSSIBLE_VALUES = IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toList());
	private Integer value;
	private final Set<Integer> possibleValues;

	public SudokuCell(Integer value)
	{
		this.value = value;
		this.possibleValues = (value == null)
			? new HashSet<>(ALL_POSSIBLE_VALUES)
			: Collections.emptySet();
	}

	public Integer getValue()
	{
		return value;
	}

	public void setValue(Integer value)
	{
		if (value < 1 || value > 9)
		{
			throw new IllegalArgumentException("Invalid value: " + value);
		}
		
		this.value = value;
		
		if (value == null)
		{
			possibleValues.addAll(ALL_POSSIBLE_VALUES);
		}
		else
		{
			possibleValues.clear();
		}
	}
}
