package com.github.ants280.sudoku.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SudokuEventTest
{

	public SudokuEventTest()
	{
	}

	@Before
	public void setUp()
	{
	}

	@Test
	public void testGetters()
	{
		Integer source = 1337;
		String oldValue = "old value";
		String newValue = "new value";
		SudokuEvent<Integer, String> sudokuEvent
				= new SudokuEvent<>(source, oldValue, newValue);

		Assert.assertEquals(source, sudokuEvent.getSource());
		Assert.assertEquals(oldValue, sudokuEvent.getOldValue());
		Assert.assertEquals(newValue, sudokuEvent.getNewValue());
	}

	@Test
	public void testEquals_same()
	{
		SudokuEvent<Integer, String> sudokuEvent1
				= new SudokuEvent<>(1337, "old value", "new value");
		SudokuEvent<Integer, String> sudokuEvent2 = sudokuEvent1;

		boolean equals = sudokuEvent1.equals(sudokuEvent2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_null()
	{
		SudokuEvent<Integer, String> sudokuEvent1
				= new SudokuEvent<>(1337, "old value", "new value");
		SudokuEvent<Integer, String> sudokuEvent2 = null;

		boolean equals = sudokuEvent1.equals(sudokuEvent2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_wrongObject()
	{
		SudokuEvent<Integer, String> sudokuEvent1
				= new SudokuEvent<>(1337, "old value", "new value");
		Integer sudokuEvent2 = 1337;

		boolean equals = sudokuEvent1.equals(sudokuEvent2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_equal()
	{
		SudokuEvent<Integer, String> sudokuEvent1
				= new SudokuEvent<>(1337, "old value", "new value");
		SudokuEvent<Integer, String> sudokuEvent2
				= new SudokuEvent<>(1337, "old value", "new value");

		boolean equals = sudokuEvent1.equals(sudokuEvent2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_different_source()
	{
		SudokuEvent<Integer, String> sudokuEvent1
				= new SudokuEvent<>(1337, "old value", "new value");
		SudokuEvent<Integer, String> sudokuEvent2
				= new SudokuEvent<>(2, "old value", "new value");

		boolean equals = sudokuEvent1.equals(sudokuEvent2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_different_oldValue()
	{
		SudokuEvent<Integer, String> sudokuEvent1
				= new SudokuEvent<>(1337, "old value", "new value");
		SudokuEvent<Integer, String> sudokuEvent2
				= new SudokuEvent<>(1337, "old valueB", "new value");

		boolean equals = sudokuEvent1.equals(sudokuEvent2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_different_newValue()
	{
		SudokuEvent<Integer, String> sudokuEvent1
				= new SudokuEvent<>(1337, "old value", "new value");
		SudokuEvent<Integer, String> sudokuEvent2
				= new SudokuEvent<>(1337, "old value", "new valueB");

		boolean equals = sudokuEvent1.equals(sudokuEvent2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_copyConstructor()
	{
		SudokuEvent<Integer, String> sudokuEvent1
				= new SudokuEvent<>(1337, "old value", "new value");
		SudokuEvent<Integer, String> sudokuEvent2
				= new SudokuEvent<>(sudokuEvent1);

		boolean equals = sudokuEvent1.equals(sudokuEvent2);

		Assert.assertTrue(equals);
	}
}
