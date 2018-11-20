package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class SudokuFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public SudokuFrame()
	{
		super("Sudoku2");

		init();
	}

	private void init()
	{
		SudokuBoard board = new SudokuBoard();
		SudokuDisplayComponent sudokuDisplayComponent
				= new SudokuDisplayComponent(board);

		JMenu fileMenu = new JMenu();
		JMenuItem restartMenuItem = new JMenuItem();
		JMenuItem loadMenuItem = new JMenuItem();
		JMenuItem exportMenuItem = new JMenuItem();
		JMenuItem exitMenuItem = new JMenuItem();
		JMenu actionMenu = new JMenu();
		JMenuItem setValueMenuItem = new JMenuItem();
		JMenuItem setPossibleValueMenuItem = new JMenuItem();
		JMenuItem solveMenuItem = new JMenuItem();
		JMenu helpMenu = new JMenu();
		JMenuItem helpMenuItem = new JMenuItem();
		JMenuItem aboutMenuItem = new JMenuItem();

		fileMenu.add(restartMenuItem);
		fileMenu.add(loadMenuItem);
		fileMenu.add(exportMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		actionMenu.add(setValueMenuItem);
		actionMenu.add(setPossibleValueMenuItem);
		actionMenu.add(solveMenuItem);
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(actionMenu);
		menuBar.add(helpMenu);

		SudokuUiManager.manage(
				this,
				sudokuDisplayComponent,
				board,
				fileMenu,
				restartMenuItem,
				loadMenuItem,
				exportMenuItem,
				exitMenuItem,
				actionMenu,
				setValueMenuItem,
				setPossibleValueMenuItem,
				solveMenuItem,
				helpMenu,
				helpMenuItem,
				aboutMenuItem);

		this.setJMenuBar(menuBar);
		this.add(sudokuDisplayComponent);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
