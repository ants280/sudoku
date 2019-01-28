package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

/**
 * Generates permutations of the specified SudokuValues. Does not include the
 * empty permutation.
 */
public class PossibleValuesIterator implements Iterator<Collection<SudokuValue>>
{
	private final SudokuValue[] possibleValues;
	private final int max;
	private int index;

	public PossibleValuesIterator(Collection<SudokuValue> possibleValues)
	{
		this.possibleValues = possibleValues.stream()
				.toArray(SudokuValue[]::new);
		this.max = 1 << this.possibleValues.length;
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

		Collection<SudokuValue> possibleValuesGroup
				= EnumSet.noneOf(SudokuValue.class);

		IntStream.range(0, possibleValues.length)
				.filter(i -> ((1 << i) & index) != 0)
				.mapToObj(i -> possibleValues[i])
				.forEach(possibleValuesGroup::add);

		index++;

		return possibleValuesGroup;
	}
}
