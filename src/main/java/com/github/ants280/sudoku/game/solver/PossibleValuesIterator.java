package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

/**
 * Generates permutations of the specified SudokuValues. Does not include the
 * empty permutation.
 */
public class PossibleValuesIterator implements Iterator<Collection<SudokuValue>>
{
	private final List<SudokuValue> possibleValues;
	private final EnumSet<SudokuValue> possibleValuesGroup;
	private final int max;
	private int index;

	public PossibleValuesIterator(List<SudokuValue> possibleValues)
	{
		this.possibleValues = new ArrayList<>(possibleValues);
		this.possibleValuesGroup = EnumSet.noneOf(SudokuValue.class);
		this.max = 1 << this.possibleValues.size();
		this.index = 1;
	}

	@Override
	public boolean hasNext()
	{
		return index < max;
	}

	@Override
	public Collection<SudokuValue> next()
	{
		if (!this.hasNext())
		{
			throw new NoSuchElementException();
		}

		possibleValuesGroup.clear();
		IntStream.range(0, possibleValues.size())
				.filter(i -> ((1 << i) & index) != 0)
				.mapToObj(possibleValues::get)
				.forEach(possibleValuesGroup::add);

		index++;

		return possibleValuesGroup.clone();
	}

}
