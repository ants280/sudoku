package com.github.ants280.sudoku.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class SudokuComponentListener
		extends ComponentAdapter
		implements ComponentListener
{
	private final Runnable componentResizedRunnable;

	public SudokuComponentListener(Runnable componentResizedRunnable)
	{
		this.componentResizedRunnable = componentResizedRunnable;
	}

	@Override
	public void componentResized(ComponentEvent componentEvent)
	{
		componentResizedRunnable.run();
	}
}
