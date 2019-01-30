package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuValue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class SudokuKeyListener
		extends KeyAdapter
		implements KeyListener
{
	private final Consumer<SudokuValue> setSelectedCellValueConsumer;
	private final Consumer<MoveDirection> moveSelectedCellConsumer;

	public SudokuKeyListener(
			Consumer<SudokuValue> setSelectedCellValueConsumer,
			Consumer<MoveDirection> moveSelectedCellConsumer)
	{
		this.setSelectedCellValueConsumer = setSelectedCellValueConsumer;
		this.moveSelectedCellConsumer = moveSelectedCellConsumer;
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
			case KeyEvent.VK_0:
			case KeyEvent.VK_1:
			case KeyEvent.VK_2:
			case KeyEvent.VK_3:
			case KeyEvent.VK_4:
			case KeyEvent.VK_5:
			case KeyEvent.VK_6:
			case KeyEvent.VK_7:
			case KeyEvent.VK_8:
			case KeyEvent.VK_9:
			case KeyEvent.VK_NUMPAD0:
			case KeyEvent.VK_NUMPAD1:
			case KeyEvent.VK_NUMPAD2:
			case KeyEvent.VK_NUMPAD3:
			case KeyEvent.VK_NUMPAD4:
			case KeyEvent.VK_NUMPAD5:
			case KeyEvent.VK_NUMPAD6:
			case KeyEvent.VK_NUMPAD7:
			case KeyEvent.VK_NUMPAD8:
			case KeyEvent.VK_NUMPAD9:
				setSelectedCellValueConsumer
						.accept(SudokuValue.fromChar(event.getKeyChar()));
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
				moveSelectedCellConsumer.accept(
						MoveDirection.fromKeyCode(event.getKeyCode()));
				break;
			default:
				break;
		}
	}
}
