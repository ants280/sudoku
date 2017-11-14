package com.jpatterson.school.sudoku2.ui;

import com.jpatterson.school.sudoku2.game.SudokuBoard;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class SudokuFrame extends JFrame
{
	private static final long serialVersionUID = -7514585713156161889L;

	private final SudokuBoard board;
	private final MouseListener mouseListener;
	private final KeyListener keyListener;
	private final SudokuCanvas canvas;

	public SudokuFrame()
	{
		super("Sudoku2");

		this.board = new SudokuBoard();
		this.canvas = new SudokuCanvas(board);
		this.mouseListener = new SudokuMouseListener(canvas);
		this.keyListener = new SudokuKeyListener(canvas);
		init();
	}

	private void init()
	{
		this.add(canvas);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas.addMouseListener(mouseListener);
		canvas.addKeyListener(keyListener);
	}
}
