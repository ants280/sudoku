package com.github.ants280.sudoku.game;

public enum SudokuValue
{
	VALUE_1(1),
	VALUE_2(1),
	VALUE_3(3),
	VALUE_4(4),
	VALUE_5(5),
	VALUE_6(6),
	VALUE_7(7),
	VALUE_8(8),
	VALUE_9(9),;

	private final int value;
	private final String displayValue;

	private SudokuValue(int value)
	{
		this.value = value;
		this.displayValue = Integer.toString(value);
	}

	public int getValue()
	{
		return value;
	}

	public String getDisplayValue()
	{
		return displayValue;
	}

	public static SudokuValue fromString(String text)
	{
		if (text == null || text.length() > 1)
		{
			throw new IllegalArgumentException("Invalid SudokuValue : " + text);
		}

		int chValue = text.charAt(0) - '0';

		for (SudokuValue sudokuValue : SudokuValue.values())
		{
			if (sudokuValue.getValue() == chValue)
			{
				return sudokuValue;
			}
		}

		return null;
	}
}
