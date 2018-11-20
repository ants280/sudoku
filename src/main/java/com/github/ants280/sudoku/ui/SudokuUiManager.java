package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private static final String RESTART_MI = "Restart";
	private static final String LOAD_MI = "Load Game...";
	private static final String EXPORT_MI = "Export Game...";
	private static final String EXIT_MI = "Exit";
	private static final String ACTION_M = "Action";
	private static final String SET_VALUE_MI = "Set value..."; // TODO : disable if no SudokuCell is selecetd.
	private static final String SET_POSSIBLE_VALUE_MI = "Set possible value..."; // TODO : disable if no SudokuCell is selecetd.
	private static final String SOLVE_MI = "Solve";
	private static final String HELP_M = "Help";
	private static final String HELP_MI = "Help";
	private static final String ABOUT_MI = "About";

	private final JFrame frame;
	private final SudokuDisplayComponent sudokuDisplayComponent;
	private final SudokuBoard board;
	private final Map<String, Runnable> actionCommands;
	private final SudokuMouseListener mouseListener;
	private final SudokuKeyListener keyListener;
	private boolean listenersAdded;

	private SudokuUiManager(
			JFrame frame,
			SudokuDisplayComponent sudokuDisplayComponent,
			SudokuBoard board)
	{

		this.frame = frame;
		this.sudokuDisplayComponent = sudokuDisplayComponent;
		this.board = board;
		this.actionCommands = this.createActionCommands();
		this.mouseListener = new SudokuMouseListener(
				this::selectCell,
				this::setValue,
				this::setPossibleValue);
		this.keyListener = new SudokuKeyListener(
				this::setValue,
				this::moveSelectedCell);
		this.listenersAdded = false;
	}

	public static void manage(
			SudokuFrame frame,
			SudokuDisplayComponent sudokuDisplayComponent,
			SudokuBoard board,
			JMenu fileMenu,
			JMenuItem restartMenuItem,
			JMenuItem loadMenuItem,
			JMenuItem exportMenuItem,
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
				= new SudokuUiManager(frame, sudokuDisplayComponent, board);

		sudokuActionListener.initMenu(
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

		sudokuActionListener.addListeners();
	}

	private Map<String, Runnable> createActionCommands()
	{
		Map<String, Runnable> tempActionCommands = new HashMap<>();
		tempActionCommands.put(RESTART_MI, this::restart);
		tempActionCommands.put(LOAD_MI, this::load);
		tempActionCommands.put(EXPORT_MI, this::export);
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
			JMenuItem exportMenuItem,
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
		exportMenuItem.setText(EXPORT_MI);
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
		exportMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		setValueMenuItem.addActionListener(this);
		setPossibleValueMenuItem.addActionListener(this);
		solveMenuItem.addActionListener(this);
		helpMenuItem.addActionListener(this);
		aboutMenuItem.addActionListener(this);
	}

	private void addListeners()
	{
		if (!listenersAdded)
		{
			listenersAdded = true;
			frame.addKeyListener(keyListener);
			sudokuDisplayComponent.addMouseListener(mouseListener);
		}
	}

	private void removeListeners()
	{
		if (listenersAdded)
		{
			listenersAdded = false;
			frame.removeKeyListener(keyListener);
			sudokuDisplayComponent.removeMouseListener(mouseListener);
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		actionCommands.get(actionEvent.getActionCommand()).run();
	}

	private void restart()
	{
		this.addListeners();
		// TODO : implement game restarting
	}

	private void exit()
	{
		Runtime.getRuntime().exit(0);
	}

	private void help()
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

	private void about()
	{
		JOptionPane.showMessageDialog(frame,
				"(c) 2017 Jacob Patterson"
				+ "\n"
				+ "\nDescription taken from my newspaper,"
				+ "\n(c) Universal Uclick",
				"About " + frame.getTitle(),
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void setPossibleValue()
	{
		Integer r = sudokuDisplayComponent.getSelectedRow();
		Integer c = sudokuDisplayComponent.getSelectedCol();
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
						sudokuDisplayComponent.repaint();
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

	private void selectCell(int x, int y)
	{
		sudokuDisplayComponent.selectCellFromCoordinates(x, y);
	}

	private void moveSelectedCell(MoveDirection moveDirection)
	{
		switch (moveDirection)
		{
			case UP:
				sudokuDisplayComponent.incrementSelectedRow(-1);
				break;
			case DOWN:
				sudokuDisplayComponent.incrementSelectedRow(1);
				break;
			case LEFT:
				sudokuDisplayComponent.incrementSelectedCol(-1);
				break;
			case RIGHT:
				sudokuDisplayComponent.incrementSelectedCol(1);
				break;
			default:
				throw new IllegalArgumentException(
						"Unknown moveDirection: " + moveDirection);
		}
	}

	// TODO: combine code with setPossibleValue(), but distinguish possible values from normal ones
	private void setValue()
	{
		Integer r = sudokuDisplayComponent.getSelectedRow();
		Integer c = sudokuDisplayComponent.getSelectedCol();
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
			sudokuDisplayComponent.repaint();

			if (board.isSolved())
			{
				this.removeListeners();
			}
		}
	}

	private void setValue(Integer cellValue)
	{
		if (Sudoku.DEBUG)
		{
			System.out.printf("\tSetting value '%d' at [%s,%s]\n",
					cellValue,
					sudokuDisplayComponent.getSelectedRow(),
					sudokuDisplayComponent.getSelectedCol());
		}

		sudokuDisplayComponent.setSelectedCellValue(cellValue);

		if (board.isSolved())
		{
			this.removeListeners();
		}
	}

	private void solve()
	{
		// TODO: make listeners work with solving (disabled while solving, enabled again when solving finished if board is not yet solved).
		SudokuSolverPopup sudokuSolverPopup
				= new SudokuSolverPopup(frame, sudokuDisplayComponent, board);
		sudokuSolverPopup.setVisible(true);
	}

	private void load()
	{
		this.addListeners();
		// TODO implement game loading
	}

	private void export()
	{
		// TODO implement game exporting
	}
}
