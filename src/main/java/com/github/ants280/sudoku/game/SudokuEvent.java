package com.github.ants280.sudoku.game;

import java.util.Objects;

public class SudokuEvent<T, U>
{
	private final T source;
	private final U oldValue;
	private final U newValue;

	public SudokuEvent(T source, U oldValue, U newValue)
	{
		this.source = source;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public T getSource()
	{
		return source;
	}

	public U getOldValue()
	{
		return oldValue;
	}

	public U getNewValue()
	{
		return newValue;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 37 * hash + Objects.hashCode(this.source);
		hash = 37 * hash + Objects.hashCode(this.oldValue);
		hash = 37 * hash + Objects.hashCode(this.newValue);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj
				|| obj != null
				&& this.getClass() == obj.getClass()
				&& Objects.equals(this.source, ((SudokuEvent) obj).source)
				&& Objects.equals(this.oldValue, ((SudokuEvent) obj).oldValue)
				&& Objects.equals(this.newValue, ((SudokuEvent) obj).newValue);
	}

}
