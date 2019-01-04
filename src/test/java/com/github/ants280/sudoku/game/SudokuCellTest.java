package com.github.ants280.sudoku.game;

import java.util.Collection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SudokuCellTest
{
	private SudokuCell sudokuCell;

	@Before
	public void setUp() throws Exception
	{
		int rowIndex = 4;
		int columnIndex = 3;
		int groupIndex = 2;
		SudokuValue value = SudokuValue.VALUE_1;
		boolean locked = false;

		this.sudokuCell = new SudokuCell(rowIndex, columnIndex, groupIndex, value, locked);
	}

	@Test
	public void testGetIndex_row()
	{
		int actualIndex = sudokuCell.getIndex(SectionType.ROW);
		int expectedIntex = 4;

		Assert.assertEquals(expectedIntex, actualIndex);
	}

	@Test
	public void testGetIndex_column()
	{
		int actualIndex = sudokuCell.getIndex(SectionType.COLUMN);
		int expectedIntex = 3;

		Assert.assertEquals(expectedIntex, actualIndex);
	}

	@Test
	public void testGetIndex_group()
	{
		int actualIndex = sudokuCell.getIndex(SectionType.GROUP);
		int expectedIntex = 2;

		Assert.assertEquals(expectedIntex, actualIndex);
	}

	@Test
	public void testGetValue()
	{
		SudokuValue actualValue = sudokuCell.getValue();
		SudokuValue expectedValue = SudokuValue.VALUE_1;

		Assert.assertEquals(expectedValue, actualValue);
	}

	@Test
	public void testGetValue_nullIsOk()
	{
		sudokuCell = new SudokuCell(0, 0, 0, null, false);

		SudokuValue actualValue = sudokuCell.getValue();
		SudokuValue expectedValue = null;

		Assert.assertEquals(expectedValue, actualValue);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullBadIfLocked()
	{
		sudokuCell = new SudokuCell(0, 0, 0, null, true);

		Assert.fail("expected exception");
	}

	@Test
	public void testGetPossibleValues_emptyDefault()
	{
		Collection<SudokuValue> possibleValues = sudokuCell.getPossibleValues();

		Assert.assertTrue(possibleValues.isEmpty());
	}

	@Test
	public void testGetPossibleValues_one()
	{
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_6);

		Collection<SudokuValue> possibleValues = sudokuCell.getPossibleValues();

		Assert.assertFalse(possibleValues.isEmpty());
		Assert.assertEquals(1, possibleValues.size());
		Assert.assertTrue(possibleValues.contains(SudokuValue.VALUE_6));
	}

	@Test
	public void testGetPossibleValues_immutable()
	{
		sudokuCell.getPossibleValues()
				.add(SudokuValue.VALUE_6);

		Assert.assertTrue(
				"The object's collection should not be mutated even though the copy was.",
				sudokuCell.getPossibleValues().isEmpty());
	}

	@Test
	public void testHasPossibleValue_no()
	{
		boolean hasPossibleValue = sudokuCell.hasPossibleValue(SudokuValue.VALUE_8);

		Assert.assertFalse(hasPossibleValue);
	}

	@Test
	public void testHasPossibleValue_yes()
	{
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_5);

		boolean hasPossibleValue = sudokuCell.hasPossibleValue(SudokuValue.VALUE_5);

		Assert.assertTrue(hasPossibleValue);
	}

	@Test
	public void testHasPossibleValue_doubleToggleNo()
	{
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_3);
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_3);

		boolean hasPossibleValue = sudokuCell.hasPossibleValue(SudokuValue.VALUE_3);

		Assert.assertFalse(hasPossibleValue);
	}

	@Test
	public void testIsLocked_constructor_f()
	{
		sudokuCell = new SudokuCell(0, 0, 0, null, false);

		boolean actualLocked = sudokuCell.isLocked();
		boolean expectedLocked = false;

		Assert.assertEquals(expectedLocked, actualLocked);
	}

	@Test
	public void testIsLocked_constructor_t()
	{
		sudokuCell = new SudokuCell(0, 0, 0, SudokuValue.VALUE_1, true);

		boolean actualLocked = sudokuCell.isLocked();
		boolean expectedLocked = true;

		Assert.assertEquals(expectedLocked, actualLocked);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsLocked_constructor_t_noValue()
	{
		sudokuCell = new SudokuCell(0, 0, 0, null, true);

		Assert.fail("expected exception");
	}

	@Test
	public void testIsLocked_set_f()
	{
		sudokuCell = new SudokuCell(0, 0, 0, SudokuValue.VALUE_2, true);
		sudokuCell.setLocked(false);

		boolean actualLocked = sudokuCell.isLocked();
		boolean expectedLocked = false;

		Assert.assertEquals(expectedLocked, actualLocked);
	}

	@Test
	public void testIsLocked_set_true()
	{
		sudokuCell = new SudokuCell(0, 0, 0, SudokuValue.VALUE_3, false);
		sudokuCell.setLocked(true);

		boolean actualLocked = sudokuCell.isLocked();
		boolean expectedLocked = true;

		Assert.assertEquals(expectedLocked, actualLocked);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsLocked_set_true_noValue()
	{
		sudokuCell = new SudokuCell(0, 0, 0, null, false);
		sudokuCell.setLocked(true);

		Assert.fail("expected exception");
	}

	@Test
	public void testIsLocked_set_t_possibleValuesCleared()
	{
		sudokuCell = new SudokuCell(0, 0, 0, SudokuValue.VALUE_7, false);
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_1);
		sudokuCell.setLocked(true);

		Collection<SudokuValue> possibleValues = sudokuCell.getPossibleValues();

		Assert.assertTrue(possibleValues.isEmpty());
	}

	@Test
	public void testSetValue()
	{
		sudokuCell = new SudokuCell(0, 0, 0, null, false);

		SudokuValue expectedValue = SudokuValue.VALUE_1;
		sudokuCell.setValue(expectedValue);
		SudokuValue actualValue = sudokuCell.getValue();

		Assert.assertEquals(expectedValue, actualValue);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetValue_locked()
	{
		sudokuCell = new SudokuCell(0, 0, 0, SudokuValue.VALUE_7, true);

		sudokuCell.setValue(SudokuValue.VALUE_1);

		Assert.fail("expected exception");
	}

	@Test
	public void testClearPossibleValues()
	{
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_1);
		sudokuCell.clearPossibleValues();

		Assert.assertTrue(sudokuCell.getPossibleValues().isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testClearPossibleValues_locked()
	{
		sudokuCell.setLocked(true);
		sudokuCell.clearPossibleValues();

		Assert.fail("expected exception");
	}

	@Test
	public void testRestoreAllPossibleValues()
	{
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_7);
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_8);
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_7);
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_4);
		sudokuCell.restoreAllPossibleValues();

		for (SudokuValue sudokuValue : SudokuValue.values())
		{
			Assert.assertTrue(sudokuCell.hasPossibleValue(sudokuValue));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRestoreAllPossibleValues_locked()
	{
		sudokuCell = new SudokuCell(0, 0, 0, SudokuValue.VALUE_1, true);

		sudokuCell.restoreAllPossibleValues();

		Assert.fail("expected exception");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTogglePossibleValue_locked()
	{
		sudokuCell = new SudokuCell(0, 0, 0, SudokuValue.VALUE_7, false);
		sudokuCell.setLocked(true);
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_1);

		Assert.fail("expected exception");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTogglePossibleValue_null()
	{
		sudokuCell = new SudokuCell(0, 0, 0, SudokuValue.VALUE_7, false);
		sudokuCell.togglePossibleValue(null);

		Assert.fail("expected exception");
	}

	@Test
	public void testResetFrom()
	{
		sudokuCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, true);
		SudokuCell sudokuCell2 = new SudokuCell(5, 6, 7, SudokuValue.VALUE_8, false);
		sudokuCell2.togglePossibleValue(SudokuValue.VALUE_1);
		sudokuCell2.togglePossibleValue(SudokuValue.VALUE_2);

		sudokuCell.resetFrom(sudokuCell2);

		Assert.assertSame(1, sudokuCell.getIndex(SectionType.ROW));
		Assert.assertSame(2, sudokuCell.getIndex(SectionType.COLUMN));
		Assert.assertSame(3, sudokuCell.getIndex(SectionType.GROUP));
		Assert.assertEquals(SudokuValue.VALUE_8, sudokuCell.getValue());
		Assert.assertEquals(false, sudokuCell.isLocked());
		Assert.assertEquals(2, sudokuCell.getPossibleValues().size());
		Assert.assertTrue(sudokuCell.hasPossibleValue(SudokuValue.VALUE_1));
		Assert.assertTrue(sudokuCell.hasPossibleValue(SudokuValue.VALUE_2));
	}

	@Test
	public void testEquals_same()
	{
		SudokuCell sudokuCell2 = sudokuCell;

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_null()
	{
		SudokuCell sudokuCell2 = null;

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentClass()
	{
		sudokuCell = new SudokuCell(0, 0, 0, null, false);
		Object sudokuCell2 = 143L;

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_equal()
	{
		sudokuCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, true);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, true);

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_differentLocked()
	{
		sudokuCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, false);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, true);

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentValue()
	{
		sudokuCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_9, true);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, true);

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentRow()
	{
		sudokuCell = new SudokuCell(5, 2, 3, SudokuValue.VALUE_9, true);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, true);

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentColumn()
	{
		sudokuCell = new SudokuCell(1, 8, 3, SudokuValue.VALUE_9, true);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, true);

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentGroup()
	{
		sudokuCell = new SudokuCell(1, 2, 2, SudokuValue.VALUE_9, true);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_4, true);

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_differentPossibleValues()
	{
		sudokuCell = new SudokuCell(1, 2, 3, null, false);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, null, false);

		sudokuCell.togglePossibleValue(SudokuValue.VALUE_1);
		sudokuCell2.togglePossibleValue(SudokuValue.VALUE_2);

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_samePossibleValues()
	{
		sudokuCell = new SudokuCell(1, 2, 3, null, false);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, null, false);

		sudokuCell.togglePossibleValue(SudokuValue.VALUE_1);
		sudokuCell2.togglePossibleValue(SudokuValue.VALUE_1);

		boolean equals = sudokuCell.equals(sudokuCell2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testToString_different()
	{
		sudokuCell = new SudokuCell(1, 2, 3, null, false);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, null, false);
		sudokuCell.togglePossibleValue(SudokuValue.VALUE_1);
		sudokuCell2.togglePossibleValue(SudokuValue.VALUE_1);
		sudokuCell2.togglePossibleValue(SudokuValue.VALUE_5);

		String toString = sudokuCell.toString();
		String toString2 = sudokuCell2.toString();

		Assert.assertNotEquals(toString, toString2);
		Assert.assertTrue(toString2.contains("[15]"));
	}
	@Test
	public void testToString_lockedDifferent()
	{
		sudokuCell = new SudokuCell(1, 2, 3, SudokuValue.VALUE_1, false);
		SudokuCell sudokuCell2 = new SudokuCell(1, 2, 3, SudokuValue.VALUE_1, true);

		String toString = sudokuCell.toString();
		String toString2 = sudokuCell2.toString();

		Assert.assertNotEquals(toString, toString2);
		Assert.assertTrue(toString.endsWith("N}"));
		Assert.assertTrue(toString2.endsWith("Y}"));
	}

	@Test
	public void testToString_noValue()
	{
		sudokuCell = new SudokuCell(1, 2, 3, null, false);

		String toString = sudokuCell.toString();

		Assert.assertTrue(toString.contains("0"));
	}
}
