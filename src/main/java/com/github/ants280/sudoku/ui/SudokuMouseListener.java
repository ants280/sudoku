package com.github.ants280.sudoku.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SudokuMouseListener
		extends MouseAdapter
		implements MouseListener
{
	private final MouseXyConsumer singleClickConsumer;
	private final MouseXyConsumer doubleClickConsumer;
	private final MouseXyConsumer rightClickConsumer;

	public SudokuMouseListener(
			MouseXyConsumer singleClickConsumer,
			MouseXyConsumer doubleClickConsumer,
			MouseXyConsumer rightClickConsumer)
	{
		this.singleClickConsumer = singleClickConsumer;
		this.doubleClickConsumer = doubleClickConsumer;
		this.rightClickConsumer = rightClickConsumer;
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent)
	{
		singleClickConsumer.accept(
				mouseEvent.getX(),
				mouseEvent.getY());

		switch (mouseEvent.getButton())
		{
			case MouseEvent.BUTTON1: // left mouse button
				if (mouseEvent.getClickCount() == 2)
				{
					doubleClickConsumer.accept(
							mouseEvent.getX(),
							mouseEvent.getY());
				}
				break;
			case MouseEvent.BUTTON3: // right mouse button
				rightClickConsumer.accept(
						mouseEvent.getX(),
						mouseEvent.getY());
				break;
			default:
				break;
		}
	}
}
