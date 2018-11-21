package com.github.ants280.sudoku.ui;

import static com.github.ants280.sudoku.game.SectionType.ROW;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.swing.AbstractButton;
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
	private static final String SET_VALUE_MI = "Set value...";
	private static final String SET_POSSIBLE_VALUE_MI = "Set possible value...";
	private static final String SOLVE_MI = "Solve";
	private static final String HELP_M = "Help";
	private static final String HELP_MI = "Help";
	private static final String ABOUT_MI = "About";

	private final JFrame frame;
	private final SudokuDisplayComponent sudokuDisplayComponent;
	private final SudokuBoard board;
	private final Collection<JMenuItem> selectedCellMenuItems;
	private final SudokuBoard initialBoard;
	private final Map<String, Runnable> actionCommands;
	private final SudokuMouseListener mouseListener;
	private final SudokuKeyListener keyListener;
	private boolean listenersAdded;

	private SudokuUiManager(
			JFrame frame,
			SudokuDisplayComponent sudokuDisplayComponent,
			SudokuBoard board,
			Collection<JMenuItem> selectedCellMenuItems)
	{

		this.frame = frame;
		this.sudokuDisplayComponent = sudokuDisplayComponent;
		this.board = board;
		this.initialBoard = new SudokuBoard(board.toString());
		this.selectedCellMenuItems = selectedCellMenuItems;
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
				= new SudokuUiManager(
						frame,
						sudokuDisplayComponent,
						board,
						Arrays.asList(
								setValueMenuItem,
								setPossibleValueMenuItem));

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
			selectedCellMenuItems
					.forEach(menuItem -> menuItem.setEnabled(false));
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

	private void startGame()
	{
		this.addListeners();
		selectedCellMenuItems
				.forEach(menuItem -> menuItem.setEnabled(false));
		sudokuDisplayComponent.removeSelectedCell();
	}

	private void endGame()
	{
		this.removeListeners();
		selectedCellMenuItems.forEach(menuItem -> menuItem.setEnabled(false));
		sudokuDisplayComponent.removeSelectedCell();
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		actionCommands.get(actionEvent.getActionCommand()).run();
	}

	private void restart()
	{
		board.resetFrom(initialBoard);
		sudokuDisplayComponent.repaint();

		this.startGame();
	}

	private void exit()
	{
		Runtime.getRuntime().exit(0);
	}

	private void help()
	{
		JOptionPane.showMessageDialog(
				frame,
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
		JOptionPane.showMessageDialog(
				frame,
				"(c) 2017 Jacob Patterson"
				+ "\n"
				+ "\nDescription taken from my newspaper,"
				+ "\n(c) Universal Uclick",
				"About " + frame.getTitle(),
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void selectCell(int x, int y)
	{
		sudokuDisplayComponent.selectCellFromCoordinates(x, y);
		selectedCellMenuItems.forEach(menuItem -> menuItem.setEnabled(true));
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

	private void setValue()
	{
		showValueDialog(
				"[value]",
				"Select cell value",
				sudokuCell -> true,
				(selectedSudokuCell, v)
				-> !v.equals(selectedSudokuCell.getValue()),
				this::changeCellValue,
				true);
	}

	private void setPossibleValue()
	{
		showValueDialog(
				"[possible values]",
				"Select possible\n"
				+ "cell values",
				sudokuCell -> sudokuCell.getValue() == null,
				(selectedSudokuCell, v)
				-> selectedSudokuCell.getPossibleValues().contains(v),
				this::changePossibleCellValue,
				false);
	}

	private void showValueDialog(
			String dialogTitle,
			String dialogMessage,
			Predicate<SudokuCell> canSetValuePredicate,
			BiPredicate<SudokuCell, Integer> buttonSelectedFunction,
			BiConsumer<SudokuCell, Integer> valueClickConsumer,
			boolean closeOnDialogOnButtonClick)
	{
		Integer r = sudokuDisplayComponent.getSelectedRow();
		Integer c = sudokuDisplayComponent.getSelectedCol();
		if (r != null && c != null
				&& canSetValuePredicate.test(board.getSudokuCell(ROW, r, c)))
		{
			JDialog dialog = new JDialog(frame, dialogTitle, true);

			JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
			SudokuCell selectedSudokuCell = board.getSudokuCell(ROW, r, c);
			for (int i = 1; i <= 9; i++)
			{
				Integer v = i;
				AbstractButton valueButton = new JToggleButton(
						String.valueOf(i),
						buttonSelectedFunction.test(selectedSudokuCell, v));

				valueButton.addActionListener(actionEvent ->
				{
					valueClickConsumer.accept(selectedSudokuCell, v);

					if (closeOnDialogOnButtonClick)
					{
						dialog.setVisible(false);
					}
				});

				possibleValueButtonsPanel.add(valueButton);
			}

			String htmlDialogMessage = String.format(
					"<html>%s</html>",
					dialogMessage.replaceAll("\n", "<br>"));
			dialog.add(new JLabel(htmlDialogMessage), BorderLayout.NORTH);
			dialog.add(possibleValueButtonsPanel, BorderLayout.CENTER);
			dialog.pack();
			dialog.setResizable(false);
			dialog.setLocationRelativeTo(frame);
			dialog.setVisible(true);
		}
	}

	private void changeCellValue(SudokuCell sudokuCell, Integer v)
	{
		boolean valueChanged = sudokuCell.setValue(v);

		if (valueChanged)
		{
			sudokuDisplayComponent.repaint();

			if (board.isSolved())
			{
				this.endGame();
			}
		}
	}

	private void changePossibleCellValue(SudokuCell sudokuCell, Integer v)
	{
		boolean possibleValueChanged
				= sudokuCell.getPossibleValues().contains(v)
				? sudokuCell.removePossibleValue(v)
				: sudokuCell.addPossibleValue(v);

		if (possibleValueChanged)
		{
			sudokuDisplayComponent.repaint();
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
			this.endGame();
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
		Object boardToLoad = JOptionPane.showInputDialog(
				frame,
				"Enter a saved game to load.",
				"Load " + frame.getTitle(),
				JOptionPane.INFORMATION_MESSAGE,
				null, // Icon
				null, // selectionValues (null implies textbox
				null); // initialSelectionValue

		if (boardToLoad != null)
		{
			if (SudokuBoard.isValidSavedBoard(boardToLoad.toString()))
			{
				SudokuBoard loadedBoard
						= new SudokuBoard(boardToLoad.toString());
				board.resetFrom(loadedBoard);
				initialBoard.resetFrom(board);

				sudokuDisplayComponent.repaint();

				this.addListeners();
			}
			else
			{
				JOptionPane.showMessageDialog(
						frame,
						"Error loading board.\n"
						+ "It should be something like '{<81 digits>}' "
						+ "(without quotes).",
						"Invalid Board for " + frame.getTitle(),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void export()
	{
		JOptionPane.showInputDialog(
				frame,
				"Copy the game state to load later.\n"
				+ "Warning: this does not save possible values.",
				"Export " + frame.getTitle(),
				JOptionPane.INFORMATION_MESSAGE,
				null, // Icon
				null, // selectionValues (null implies textbox
				board.toString());
	}
}
