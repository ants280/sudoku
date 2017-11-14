package com.jpatterson.school.sudoku2.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SudokuMouseListener extends MouseAdapter {

	private final SudokuCanvas canvas;

	public SudokuMouseListener(SudokuCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		switch (event.getButton()) {
			case MouseEvent.BUTTON1: // left mouse button
				canvas.selectCellFromCoordinates(event.getX(), event.getY());
				break;
			default:
				break;
		}
	}
}
