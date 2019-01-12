package com.github.ants280.sudoku.ui;

import java.awt.Component;
import java.lang.Thread.UncaughtExceptionHandler;

public class SudokuUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	private final Component parentComponent;

	public SudokuUncaughtExceptionHandler(Component parentComponent)
	{
		this.parentComponent = parentComponent;
	}

	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		SudokuDialogFactory.showExceptionDialog(
				parentComponent,
				"Error",
				e instanceof Exception ? (Exception) e : new Exception(e));
	}
}
