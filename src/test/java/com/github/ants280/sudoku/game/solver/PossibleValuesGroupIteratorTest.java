package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuValue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;

public class PossibleValuesGroupIteratorTest
{
	@Test
	public void testIterate_threePossibleValues()
	{
		List<SudokuValue> possibleValues = Arrays.asList(
				SudokuValue.VALUE_1,
				SudokuValue.VALUE_3,
				SudokuValue.VALUE_4);
		// NOTE : Expect IndexOutOfBoundsException to be thrown if iterator produces more possible value groups than specified below.
		List<Collection<SudokuValue>> expectedPossibleValuesGroups = Arrays.asList(
				Stream.of(SudokuValue.VALUE_1).collect(Collectors.toSet()),
				Stream.of(SudokuValue.VALUE_3).collect(Collectors.toSet()),
				Stream.of(SudokuValue.VALUE_1, SudokuValue.VALUE_3).collect(Collectors.toSet()),
				Stream.of(SudokuValue.VALUE_4).collect(Collectors.toSet()),
				Stream.of(SudokuValue.VALUE_1, SudokuValue.VALUE_4).collect(Collectors.toSet()),
				Stream.of(SudokuValue.VALUE_3, SudokuValue.VALUE_4).collect(Collectors.toSet()),
				Stream.of(SudokuValue.VALUE_1, SudokuValue.VALUE_3, SudokuValue.VALUE_4).collect(Collectors.toSet()));

		PossibleValuesIterator possibleValuesIterator
				= new PossibleValuesIterator(possibleValues);

		int i = 0;
		while (possibleValuesIterator.hasNext())
		{
			Collection<SudokuValue> actualPossibleValuesGroup = possibleValuesIterator.next();
			Collection<SudokuValue> expectedPossibleValuesGroup = expectedPossibleValuesGroups.get(i);

			Assert.assertEquals("index " + i, expectedPossibleValuesGroup, actualPossibleValuesGroup);

			i++;
		}

		Assert.assertEquals("possible values size", 7, i);
	}

	@Test
	public void testIterate_allPossibleValues()
	{
		List<SudokuValue> possibleValues = Arrays.asList(SudokuValue.values());
		PossibleValuesIterator possibleValuesIterator
				= new PossibleValuesIterator(possibleValues);
		int expectedCount = 511; // 2^9 - 1

		int actualCount = 0;
		while (possibleValuesIterator.hasNext())
		{
			possibleValuesIterator.next();
			actualCount++;
		}

		Assert.assertEquals(expectedCount, actualCount);
	}

	@Test
	public void testHasNext_empty()
	{
		List<SudokuValue> possibleValues = Collections.emptyList();
		PossibleValuesIterator possibleValuesIterator
				= new PossibleValuesIterator(possibleValues);

		boolean hasNext = possibleValuesIterator.hasNext();

		Assert.assertFalse(hasNext);
	}

	@Test(expected = NoSuchElementException.class)
	public void testNoSuchElementException()
	{
		List<SudokuValue> possibleValues = Collections.emptyList();
		PossibleValuesIterator possibleValuesIterator
				= new PossibleValuesIterator(possibleValues);

		possibleValuesIterator.next();

		Assert.fail("Expected exception to be thrown.");
	}
}
