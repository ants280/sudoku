package com.github.ants280.sudoku.game;

import java.util.Collection;
import java.util.EnumSet;
import org.junit.Assert;
import org.junit.Test;

public class SudokuValueTest
{
	@Test
	public void testFromChar_unique()
	{
		Collection<SudokuValue> values = EnumSet.noneOf(SudokuValue.class);

		for (char ch = '1'; ch <= '9'; ch++)
		{
			SudokuValue value = SudokuValue.fromChar(ch);

			Assert.assertTrue("value already added : " + ch, values.add(value));
		}

		Assert.assertEquals(SudokuValue.values().length, values.size());
	}
}
