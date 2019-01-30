package com.github.ants280.sudoku.ui;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MoveDirection
{
	LEFT(-1, 0, KeyEvent.VK_LEFT),
	RIGHT(1, 0, KeyEvent.VK_RIGHT),
	UP(0, -1, KeyEvent.VK_UP),
	DOWN(0, 1, KeyEvent.VK_DOWN);

	private final int dx;
	private final int dy;
	private final int keyCode;
	private static final Map<Integer, MoveDirection> KEY_CODES
			= Arrays.stream(MoveDirection.values())
					.collect(Collectors.toMap(
							MoveDirection::getKeyCode,
							Function.identity()));

	MoveDirection(int dx, int dy, int keyCode)
	{
		this.dx = dx;
		this.dy = dy;
		this.keyCode = keyCode;
	}

	public int getDx()
	{
		return dx;
	}

	public int getDy()
	{
		return dy;
	}

	public int getKeyCode()
	{
		return keyCode;
	}

	public static MoveDirection fromKeyCode(int keyCode)
	{
		return KEY_CODES.get(keyCode);
	}
}
