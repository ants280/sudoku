package com.jpatterson.school.sudoku2.ui;

import com.jpatterson.school.sudoku2.game.SudokuBoard;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
		this.setJMenuBar(createJMenuBar());
		this.add(canvas);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas.addMouseListener(mouseListener);
		canvas.addKeyListener(keyListener);
	}

	private JMenuBar createJMenuBar()
	{
		SudokuActionListener actionListener
			= new SudokuActionListener(this);

		JMenu fileMenu = new JMenu("File");
			fileMenu.add(createMenuItem("Restart", actionListener::restart));
			fileMenu.add(createMenuItem("Solve", null)); // TODO
			fileMenu.add(createMenuItem("Load Game", null)); // TODO
			fileMenu.add(createMenuItem("Exit", actionListener::exit));
		JMenu helpMenu = new JMenu("Help");
			helpMenu.add(createMenuItem("Help", actionListener::help));
			helpMenu.add(createMenuItem("About", actionListener::about));

		JMenuBar menuBar = new JMenuBar();
			menuBar.add(fileMenu);
			menuBar.add(helpMenu);

		return menuBar;
	}

	private JMenuItem createMenuItem(
		String title, ActionListener actionListener)
	{
		JMenuItem menuItem = new JMenuItem(title);
		menuItem.addActionListener(actionListener);

		return menuItem;
	}
}
