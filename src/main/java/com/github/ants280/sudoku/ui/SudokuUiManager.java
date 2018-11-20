package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class SudokuUiManager implements ActionListener
{
	private static final String FILE_M = "File";
	private static final String ACTION_M = "Action";
	private static final String HELP_M = "Help";
	private static final String RESTART_MI = "Restart";
	private static final String LOAD_MI = "Load Game";
	private static final String EXIT_MI = "Exit";
	private static final String SET_VALUE_MI = "Set value..."; // TODO : disable if no SudokuCell is selecetd.
	private static final String SET_POSSIBLE_VALUE_MI = "Set possible value..."; // TODO : disable if no SudokuCell is selecetd.
	private static final String SOLVE_MI = "Solve";
	private static final String HELP_MI = "Help";
	private static final String ABOUT_MI = "About";

	private final JFrame frame;
	private final SudokuCanvas canvas;
	private final SudokuBoard board;
	private final Map<String, Runnable> actionCommands;

	private SudokuUiManager(
			JFrame frame,
			SudokuCanvas canvas,
			SudokuBoard board)
	{

		this.frame = frame;
		this.canvas = canvas;
		this.board = board;
		this.actionCommands = this.createActionCommands();
	}

	public static void manage(
			SudokuFrame frame,
			SudokuCanvas canvas,
			SudokuBoard board,
			JMenu fileMenu,
			JMenuItem restartMenuItem,
			JMenuItem loadMenuItem,
			JMenuItem exitMenuItem,
			JMenu actionMenu,
			JMenuItem setValueMenuItem,
			JMenuItem setPossibleValueMenuItem,
			JMenuItem solveMenuItem,
			JMenu helpMenu,
			JMenuItem helpMenuItem,
			JMenuItem aboutMenuItem)
	{
		SudokuUiManager sudokuActionListener
				= new SudokuUiManager(frame, canvas, board);

		sudokuActionListener.initMenu(
				fileMenu,
				restartMenuItem,
				loadMenuItem,
				exitMenuItem,
				actionMenu,
				setValueMenuItem,
				setPossibleValueMenuItem,
				solveMenuItem,
				helpMenu,
				helpMenuItem,
				aboutMenuItem);
		sudokuActionListener.initDisplayComponent(canvas);
	}

	private Map<String, Runnable> createActionCommands()
	{
		Map<String, Runnable> tempActionCommands = new HashMap<>();
		tempActionCommands.put(RESTART_MI, this::restart);
		tempActionCommands.put(LOAD_MI, this::load);
		tempActionCommands.put(EXIT_MI, this::exit);
		tempActionCommands.put(SET_VALUE_MI, this::setValue);
		tempActionCommands.put(SET_POSSIBLE_VALUE_MI, this::setPossibleValue);
		tempActionCommands.put(SOLVE_MI, this::solve);
		tempActionCommands.put(HELP_MI, this::help);
		tempActionCommands.put(ABOUT_MI, this::about);
		return tempActionCommands;
	}

	private void initMenu(
			JMenu fileMenu,
			JMenuItem restartMenuItem,
			JMenuItem loadMenuItem,
			JMenuItem exitMenuItem,
			JMenu actionMenu,
			JMenuItem setValueMenuItem,
			JMenuItem setPossibleValueMenuItem,
			JMenuItem solveMenuItem,
			JMenu helpMenu,
			JMenuItem helpMenuItem,
			JMenuItem aboutMenuItem)
	{
		fileMenu.setText(FILE_M);
		restartMenuItem.setText(RESTART_MI);
		loadMenuItem.setText(LOAD_MI);
		exitMenuItem.setText(EXIT_MI);
		actionMenu.setText(ACTION_M);
		setValueMenuItem.setText(SET_VALUE_MI);
		setPossibleValueMenuItem.setText(SET_POSSIBLE_VALUE_MI);
		solveMenuItem.setText(SOLVE_MI);
		helpMenu.setText(HELP_M);
		helpMenuItem.setText(HELP_MI);
		aboutMenuItem.setText(ABOUT_MI);

		restartMenuItem.addActionListener(this);
		loadMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		setValueMenuItem.addActionListener(this);
		setPossibleValueMenuItem.addActionListener(this);
		solveMenuItem.addActionListener(this);
		helpMenuItem.addActionListener(this);
		aboutMenuItem.addActionListener(this);
	}

	private void initDisplayComponent(SudokuCanvas canvas)
	{
		MouseListener mouseListener = new SudokuMouseListener(
				this::selectCell,
				this::setValue,
				this::setPossibleValue);
		KeyListener keyListener = new SudokuKeyListener(canvas);

		canvas.addMouseListener(mouseListener);
		canvas.addKeyListener(keyListener);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		actionCommands.get(actionEvent.getActionCommand()).run();
	}

	public void restart()
	{
		// TODO : implement game restarting
	}

	public void exit()
	{
		Runtime.getRuntime().exit(0);
	}

	public void help()
	{
		JOptionPane.showMessageDialog(frame,
				"Complete the grid,"
				+ "\nso that every row,"
				+ "\ncolumn, and 3x3 box"
				+ "\ncontains every"
				+ "\ndigit from 1 to 9"
				+ "\ninclusively.",
				"Help for " + frame.getTitle(),
				JOptionPane.QUESTION_MESSAGE);
	}

	public void about()
	{
		JOptionPane.showMessageDialog(frame,
				"(c) 2017 Jacob Patterson"
				+ "\n"
				+ "\nDescription taken from my newspaper,"
				+ "\n(c) Universal Uclick",
				"About " + frame.getTitle(),
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void setPossibleValue()
	{
		Integer r = canvas.getSelectedRow();
		Integer c = canvas.getSelectedCol();
		// TODO: It would be nice to also have this popup on right click (after selection the cell)
		if (r != null && c != null
				&& board.getSudokuCell(r, c).getValue() == null)
		{
			JDialog dialog = new JDialog(frame, "[possible values]", true);

			JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
			SudokuCell selectedSudokuCell = board.getSudokuCell(r, c);
			for (int i = 1; i <= 9; i++)
			{
				int j = i;
				AbstractButton possibleValueButton = new JToggleButton(
						String.valueOf(i),
						selectedSudokuCell.getPossibleValues().contains(i));

				possibleValueButton.addActionListener(actionEvent
						->
				{
					boolean possibleValueChanged
							= selectedSudokuCell.getPossibleValues().contains(j)
							? board.removePossibleValue(r, c, j)
							: board.addPossibleValue(r, c, j);

					if (possibleValueChanged)
					{
						canvas.repaint();
					}
				});

				possibleValueButtonsPanel.add(possibleValueButton);
			}

			dialog.add(new JLabel("Select possible cell values"), BorderLayout.NORTH);
			dialog.add(possibleValueButtonsPanel, BorderLayout.CENTER);
			dialog.pack();
			dialog.setResizable(false);
			dialog.setLocationRelativeTo(frame);
			dialog.setVisible(true);
		}
	}

	public void selectCell(int x, int y)
	{
		canvas.selectCellFromCoordinates(x, y);
	}

	// TODO: combine code with setPossibleValue(), but distinguish possible values from normal ones
	public void setValue()
	{
		Integer r = canvas.getSelectedRow();
		Integer c = canvas.getSelectedCol();
		// TODO: It would be nice to also have this popup on right click (after selection the cell)
		if (r != null && c != null)
		{
			JDialog dialog = new JDialog(frame, "[value]", true);

			JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
			SudokuCell selectedSudokuCell = board.getSudokuCell(r, c);
			for (int i = 1; i <= 9; i++)
			{
				Integer v = i;
				AbstractButton valueButton = new JButton(
						String.valueOf(i));
				valueButton.setEnabled(
						!v.equals(selectedSudokuCell.getValue()));

				valueButton.addActionListener(
						actionEvent -> changeValue(r, c, v, dialog));

				possibleValueButtonsPanel.add(valueButton);
			}

			dialog.add(new JLabel("Select cell value"), BorderLayout.NORTH);
			dialog.add(possibleValueButtonsPanel, BorderLayout.CENTER);
			dialog.pack();
			dialog.setResizable(false);
			dialog.setLocationRelativeTo(frame);
			dialog.setVisible(true);
		}
	}

	private void changeValue(Integer r, Integer c, Integer v, JDialog dialog)
	{
		boolean valueChanged = board.getSudokuCell(r, c).setValue(v);

		dialog.setVisible(false);
		if (valueChanged)
		{
			canvas.repaint();
		}
	}

	public void solve()
	{
		SudokuSolverPopup sudokuSolverPopup
				= new SudokuSolverPopup(frame, canvas, board);
		sudokuSolverPopup.setVisible(true);
	}

	public void load()
	{
		// TODO implement game loading
	}
}
