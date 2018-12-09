package com.github.ants280.sudoku.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SudokuMouseListener
		extends MouseAdapter
		implements MouseListener
{
	private final MouseXyConsumer singleClickConsumer;
	private final Runnable doubleClickRunnable;
	private final Runnable rightClickRunnable;

	public SudokuMouseListener(
			MouseXyConsumer singleClickConsumer,
			Runnable doubleClickRunnable,
			Runnable rightClickRunnable)
	{
		this.singleClickConsumer = singleClickConsumer;
		this.doubleClickRunnable = doubleClickRunnable;
		this.rightClickRunnable = rightClickRunnable;
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent)
	{
		singleClickConsumer.accept(mouseEvent.getX(), mouseEvent.getY());
		switch (mouseEvent.getButton())
		{
			case MouseEvent.BUTTON1: // left mouse button
				if (mouseEvent.getClickCount() == 2)
				{
					doubleClickRunnable.run();
				}
				break;
			case MouseEvent.BUTTON3: // right mouse button
				rightClickRunnable.run();
				break;
			default:
				break;
		}
	}

}
