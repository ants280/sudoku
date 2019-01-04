package com.github.ants280.sudoku.game;

public class SudokuEvent<T>
{
	private final T oldValue;
	private final T newValue;

	public SudokuEvent(T oldValue, T newValue)
	{
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public T getOldValue()
	{
		return oldValue;
	}

	public T getNewValue()
	{
		return newValue;
	}
}
