package com.github.ants280.sudoku.ui;

import java.awt.event.KeyEvent;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public enum MoveDirection
{
	LEFT(-1, 0, KeyEvent.VK_LEFT),
	RIGHT(1, 0, KeyEvent.VK_RIGHT),
	UP(0, -1, KeyEvent.VK_UP),
	DOWN(0, 1, KeyEvent.VK_DOWN);

	private final int dx;
	private final int dy;
	private final int[] keyCodes;
	private static final Map<Integer, MoveDirection> KEY_CODES
			= Arrays.stream(MoveDirection.values())
					.flatMap(moveDirection -> Arrays.stream(moveDirection.keyCodes)
					.mapToObj(keyCode -> new SimpleEntry<>(moveDirection, keyCode)))
					// throws exception if duplicate keys (Entry::Value) exist:
					.collect(Collectors.toMap(Entry::getValue, Entry::getKey));

	MoveDirection(int dx, int dy, int... keyCodes)
	{
		this.dx = dx;
		this.dy = dy;
		this.keyCodes = keyCodes;
	}

	public int getDx()
	{
		return dx;
	}

	public int getDy()
	{
		return dy;
	}

	public static MoveDirection fromKeyCode(int keyCode)
	{
		return KEY_CODES.get(keyCode);
	}
}
