package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.undo.CommandHistory;
import com.github.ants280.sudoku.game.undo.SudokuUndoBoard;
import com.github.ants280.sudoku.game.undo.SudokuUndoCellCommand;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class SudokuFrame
{
	private final JFrame frame;
	private final JMenuItem undoMenuItem;
	private final JMenuItem redoMenuItem;

	public SudokuFrame()
	{
		this.frame = new JFrame("Sudoku");
		this.undoMenuItem = new JMenuItem();
		this.redoMenuItem = new JMenuItem();

		init();
	}

	private void init()
	{
		JLabel messageLabel = new BorderedLabel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(Box.createGlue());
		topPanel.add(messageLabel);
		topPanel.add(Box.createGlue());

		CommandHistory<SudokuUndoCellCommand> commandHistory
				= new CommandHistory<>(this::undoRedoChanged);
		SudokuBoard board = new SudokuUndoBoard(commandHistory);
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
		JMenuItem clearPossibleValuesMenuItem = new JMenuItem();
		JMenuItem clearCellsMenuItem = new JMenuItem();
		JMenuItem lockCellsMenuItem = new JMenuItem();
		JMenuItem unLockCellsMenuItem = new JMenuItem();
		JMenuItem solveLogicMenuItem = new JMenuItem();
		JMenuItem solveBruteForceMenuItem = new JMenuItem();
		JMenu helpMenu = new JMenu();
		JMenuItem helpMenuItem = new JMenuItem();
		JMenuItem aboutMenuItem = new JMenuItem();

		fileMenu.add(restartMenuItem);
		fileMenu.add(loadMenuItem);
		fileMenu.add(exportMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		actionMenu.add(undoMenuItem);
		actionMenu.add(redoMenuItem);
		actionMenu.add(setValueMenuItem);
		actionMenu.add(setPossibleValueMenuItem);
		actionMenu.add(clearPossibleValuesMenuItem);
		actionMenu.addSeparator();
		actionMenu.add(clearCellsMenuItem);
		actionMenu.add(lockCellsMenuItem);
		actionMenu.add(unLockCellsMenuItem);
		actionMenu.addSeparator();
		actionMenu.add(solveLogicMenuItem);
		actionMenu.add(solveBruteForceMenuItem);
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(actionMenu);
		menuBar.add(helpMenu);

		SudokuUiManager sudokuUiManager
				= new SudokuUiManager(
						frame,
						sudokuDisplayComponent,
						board,
						messageLabel,
						commandHistory,
						Arrays.asList(
								setValueMenuItem,
								setPossibleValueMenuItem));
		sudokuUiManager.initMenu(
				fileMenu,
				restartMenuItem,
				loadMenuItem,
				exportMenuItem,
				exitMenuItem,
				actionMenu,
				undoMenuItem,
				redoMenuItem,
				setValueMenuItem,
				setPossibleValueMenuItem,
				clearPossibleValuesMenuItem,
				clearCellsMenuItem,
				lockCellsMenuItem,
				unLockCellsMenuItem,
				solveLogicMenuItem,
				solveBruteForceMenuItem,
				helpMenu,
				helpMenuItem,
				aboutMenuItem);

		this.undoRedoChanged(true, true);
		undoMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		redoMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));

		frame.setJMenuBar(menuBar);
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(sudokuDisplayComponent);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JFrame getFrame()
	{
		return frame;
	}

	private static class BorderedLabel extends JLabel
	{
		private static final Border EMPTY_BORDER
				= BorderFactory.createEmptyBorder(0, 0, 0, 0);
		private static final Border TOP_BOTTOM_BORDER
				= BorderFactory.createEmptyBorder(10, 0, 10, 0);
		private static final long serialVersionUID = 1L;

		@Override
		public void setText(String text)
		{
			super.setText(text);
			this.setBorder(text == null || text.isEmpty()
					? EMPTY_BORDER
					: TOP_BOTTOM_BORDER);
		}
	}

	private void undoRedoChanged(boolean undoEmpty, boolean redoEmpty)
	{
		undoMenuItem.setEnabled(!undoEmpty);
		redoMenuItem.setEnabled(!redoEmpty);
	}
}
