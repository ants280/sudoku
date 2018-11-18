package com.github.ants280.sudoku2.ui;

import com.github.ants280.sudoku2.game.SudokuBoard;
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
		SudokuActionListener actionListener
				= new SudokuActionListener(this, canvas, board);
		this.mouseListener = new SudokuMouseListener(canvas, actionListener);
		this.keyListener = new SudokuKeyListener(canvas);
		init(actionListener);
	}

	private void init(SudokuActionListener actionListener)
	{
		this.setJMenuBar(createJMenuBar(actionListener));
		this.add(canvas);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas.addMouseListener(mouseListener);
		canvas.addKeyListener(keyListener);
	}

	private JMenuBar createJMenuBar(SudokuActionListener actionListener)
	{
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(createMenuItem("Restart", actionListener::restart));
		fileMenu.add(createMenuItem("Load Game", actionListener::load));
		fileMenu.add(createMenuItem("Exit", actionListener::exit));
		JMenu actionMenu = new JMenu("Action");
		actionMenu.add(createMenuItem("Set value", actionListener::setValue)); // TODO disable if no SudokuCell is selecetd.
		actionMenu.add(createMenuItem("Set possible value", actionListener::setPossibleValue));  // TODO disable if no SudokuCell is selecetd.
		actionMenu.add(createMenuItem("Solve", actionListener::solve));
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(createMenuItem("Help", actionListener::help));
		helpMenu.add(createMenuItem("About", actionListener::about));

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(actionMenu);
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
