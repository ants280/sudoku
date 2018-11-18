package com.github.ants280.sudoku2.game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MutableSudokuCell extends SudokuCell
{
	private Integer value;
	private final Set<Integer> possibleValues;

	/**
	 * Creates a new MutableSudokuCel with no value and an empty set of possible
	 * values.
	 */
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
}
