package com.github.ants280.sudoku.game;

public enum SectionType
{
	ROW("row"),
	COLUMN("column"),
	GROUP("group");

	private final String displayValue;

	SectionType(String displayValue)
	{
		this.displayValue = displayValue;
	}

	public String getDisplayValue()
	{
		return displayValue;
	}
}
