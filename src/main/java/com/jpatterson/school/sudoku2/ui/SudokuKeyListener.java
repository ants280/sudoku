package com.jpatterson.school.sudoku2.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SudokuKeyListener extends KeyAdapter
{

	private final SudokuCanvas canvas;

	public SudokuKeyListener(SudokuCanvas canvas)
	{
		this.canvas = canvas;
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
				setSelectedCellValue(cellValue);
				break;
			case KeyEvent.VK_UP:
				canvas.incrementSelectedRow(-1);
				break;
			case KeyEvent.VK_DOWN:
				canvas.incrementSelectedRow(1);
				break;
			case KeyEvent.VK_LEFT:
				canvas.incrementSelectedCol(-1);
				break;
			case KeyEvent.VK_RIGHT:
				canvas.incrementSelectedCol(1);
				break;
			default:
				break;
		}
	}

	private void setSelectedCellValue(int cellValue)
	{
		if (Sudoku.DEBUG)
		{
			System.out.printf("\tSetting value '%d' at [%s,%s]\n",
				cellValue,
				canvas.getSelectedRow(),
				canvas.getSelectedCol());
		}

		canvas.setSelectedCellValue(cellValue);

		// TODO: Remove mouse & key listeners if hame is finished
	}
}
