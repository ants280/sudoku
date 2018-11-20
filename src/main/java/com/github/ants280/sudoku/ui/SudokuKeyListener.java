package com.github.ants280.sudoku.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SudokuKeyListener
		extends KeyAdapter
		implements KeyListener
{
	private final Consumer<Integer> setSelectedCellValueConsumer;
	private final Consumer<MoveDirection> moveSelectedCellConsumer;
	private static final Map<Integer, MoveDirection> MOVE_DIRECTIONS
			= new HashMap<>();

	static
	{
		MOVE_DIRECTIONS.put(KeyEvent.VK_UP, MoveDirection.UP);
		MOVE_DIRECTIONS.put(KeyEvent.VK_DOWN, MoveDirection.RIGHT);
		MOVE_DIRECTIONS.put(KeyEvent.VK_LEFT, MoveDirection.LEFT);
		MOVE_DIRECTIONS.put(KeyEvent.VK_RIGHT, MoveDirection.RIGHT);
	}

	public SudokuKeyListener(
			Consumer<Integer> setSelectedCellValueConsumer,
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
				Integer cellValue = Integer.parseInt(
						Character.valueOf(event.getKeyChar()).toString());
				setSelectedCellValueConsumer.accept(cellValue);
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
				moveSelectedCellConsumer.accept(
						MOVE_DIRECTIONS.get(event.getKeyCode()));
				break;
			default:
				break;
		}
	}
}
