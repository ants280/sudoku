package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.solver.SudokuBruteForceSolver;
import com.github.ants280.sudoku.game.solver.SudokuSolver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

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
	private static final String CLEAR_POSSIBLE_VALUES_MI = "Clear possible values";
	private static final String CLEAR_CELLS_MI = "Clear cells...";
	private static final String LOCK_CELLS_MI = "Lock cells...";
	private static final String UNLOCK_CELLS_MI = "Unlock cells...";
	private static final String SOLVE_LOGIC_MI = "Solve with Logic...";
	private static final String SOLVE_BRUTE_FORCE_MI = "Solve with Brute Force...";
	private static final String HELP_M = "Help";
	private static final String HELP_MI = "Help";
	private static final String ABOUT_MI = "About";

	private final JFrame frame;
	private final SudokuDisplayComponent sudokuDisplayComponent;
	private final SudokuBoard board;
	private final JLabel messageLabel;
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
			JLabel messageLabel,
			Collection<JMenuItem> selectedCellMenuItems)
	{

		this.frame = frame;
		this.sudokuDisplayComponent = sudokuDisplayComponent;
		this.board = board;
		this.messageLabel = messageLabel;
		this.initialBoard = new SudokuBoard(board.toString());
		this.selectedCellMenuItems = selectedCellMenuItems;
		this.actionCommands = this.createActionCommands();
		this.mouseListener = new SudokuMouseListener(
				this::selectCell,
				this::setValue,
				this::setPossibleValue);
		this.keyListener = new SudokuKeyListener(
				this::setSelectedCellValue,
				this::moveSelectedCell);
		this.listenersAdded = false;
	}

	public static void manage(
			SudokuFrame frame,
			SudokuDisplayComponent sudokuDisplayComponent,
			SudokuBoard board,
			JLabel messageLabel,
			JMenu fileMenu,
			JMenuItem restartMenuItem,
			JMenuItem loadMenuItem,
			JMenuItem exportMenuItem,
			JMenuItem exitMenuItem,
			JMenu actionMenu,
			JMenuItem setValueMenuItem,
			JMenuItem setPossibleValueMenuItem,
			JMenuItem clearPossibleValuesMenuItem,
			JMenuItem clearCellsMenuItem,
			JMenuItem lockCellsMenuItem,
			JMenuItem unLockCellsMenuItem,
			JMenuItem solveLogicMenuItem,
			JMenuItem solveBruteForceMenuItem,
			JMenu helpMenu,
			JMenuItem helpMenuItem,
			JMenuItem aboutMenuItem)
	{
		SudokuUiManager sudokuActionListener
				= new SudokuUiManager(
						frame,
						sudokuDisplayComponent,
						board,
						messageLabel,
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
				clearPossibleValuesMenuItem,
				clearCellsMenuItem,
				lockCellsMenuItem,
				unLockCellsMenuItem,
				solveLogicMenuItem,
				solveBruteForceMenuItem,
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
		tempActionCommands.put(CLEAR_POSSIBLE_VALUES_MI, this::clearPossibleValues);
		tempActionCommands.put(CLEAR_CELLS_MI, this::clearCells);
		tempActionCommands.put(LOCK_CELLS_MI, () -> this.lockCells(true));
		tempActionCommands.put(UNLOCK_CELLS_MI, () -> this.lockCells(false));
		tempActionCommands.put(SOLVE_LOGIC_MI, this::solveLogic);
		tempActionCommands.put(SOLVE_BRUTE_FORCE_MI, this::solveBruteForce);
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
			JMenuItem clearPossibleValuesMenuItem,
			JMenuItem clearCellsMenuItem,
			JMenuItem lockCellsMenuItem,
			JMenuItem unLockCellsMenuItem,
			JMenuItem solveLogicMenuItem,
			JMenuItem solveBruteForceMenuItem,
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
		clearPossibleValuesMenuItem.setText(CLEAR_POSSIBLE_VALUES_MI);
		clearCellsMenuItem.setText(CLEAR_CELLS_MI);
		lockCellsMenuItem.setText(LOCK_CELLS_MI);
		unLockCellsMenuItem.setText(UNLOCK_CELLS_MI);
		solveLogicMenuItem.setText(SOLVE_LOGIC_MI);
		solveBruteForceMenuItem.setText(SOLVE_BRUTE_FORCE_MI);
		helpMenu.setText(HELP_M);
		helpMenuItem.setText(HELP_MI);
		aboutMenuItem.setText(ABOUT_MI);

		restartMenuItem.addActionListener(this);
		loadMenuItem.addActionListener(this);
		exportMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		setValueMenuItem.addActionListener(this);
		setPossibleValueMenuItem.addActionListener(this);
		clearPossibleValuesMenuItem.addActionListener(this);
		clearCellsMenuItem.addActionListener(this);
		lockCellsMenuItem.addActionListener(this);
		unLockCellsMenuItem.addActionListener(this);
		solveLogicMenuItem.addActionListener(this);
		solveBruteForceMenuItem.addActionListener(this);
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
		this.updateMessageLabel();
		selectedCellMenuItems
				.forEach(menuItem -> menuItem.setEnabled(false));
		sudokuDisplayComponent.removeSelectedCell();
	}

	private void endGame()
	{
		this.removeListeners();
		this.updateMessageLabel();
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
		this.showValueDialog(
				"[value]",
				"Select cell value",
				sudokuCell -> true,
				(selectedSudokuCell, v)
				-> v.equals(selectedSudokuCell.getValue()),
				this::setSudokuCellValue,
				true);
	}

	private void setPossibleValue()
	{
		this.showValueDialog(
				"[possible values]",
				"Select possible\n"
				+ "cell values",
				sudokuCell -> sudokuCell.getValue() == null,
				(selectedSudokuCell, v)
				-> selectedSudokuCell.getPossibleValues().contains(v),
				this::toggleSudokuCellPossibleValue,
				false);
	}

	private void showValueDialog(
			String title,
			String message,
			Predicate<SudokuCell> canSetValuePredicate,
			BiPredicate<SudokuCell, Integer> buttonSelectedFunction,
			BiConsumer<SudokuCell, Integer> valueClickConsumer,
			boolean closeOnDialogOnButtonClick)
	{
		Integer r = sudokuDisplayComponent.getSelectedRow();
		Integer c = sudokuDisplayComponent.getSelectedCol();
		Predicate<SudokuCell> lockedSudokuCellPredicate = SudokuCell::isLocked;
		if (r != null && c != null
				&& lockedSudokuCellPredicate.negate()
						.and(canSetValuePredicate)
						.test(board.getSudokuCells(SectionType.ROW, r).get(c)))
		{
			SelectSudokuCellDialog selectSudokuCellDialog
					= new SelectSudokuCellDialog(
							frame,
							title,
							message,
							buttonSelectedFunction,
							valueClickConsumer,
							closeOnDialogOnButtonClick,
							board.getSudokuCells(SectionType.ROW, r).get(c));

			selectSudokuCellDialog.setVisible(true);
		}
	}

	private void setSudokuCellValue(SudokuCell sudokuCell, Integer v)
	{
		if (!sudokuCell.isLocked())
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
	}

	private void toggleSudokuCellPossibleValue(
			SudokuCell sudokuCell,
			Integer v)
	{
		if (!sudokuCell.isLocked())
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
	}

	private void setSelectedCellValue(Integer cellValue)
	{
		SudokuCell selectedSudokuCell = board.getSudokuCells(
				SectionType.ROW,
				sudokuDisplayComponent.getSelectedRow())
				.get(sudokuDisplayComponent.getSelectedCol());

		this.setSudokuCellValue(selectedSudokuCell, cellValue);
	}

	private void solveLogic()
	{
		SudokuSolverPopup sudokuSolverPopup
				= new SudokuSolverPopup(
						frame,
						board,
						() ->
				{
					sudokuDisplayComponent.repaint();
					this.updateMessageLabel();
				});

		sudokuSolverPopup.setVisible(true);
	}

	private void solveBruteForce()
	{
		int choice = JOptionPane.showConfirmDialog(
				frame,
				"This will find the first valid solution for the board, "
				+ "if any.\n",
				"Solve Brute force?",
				JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION)
		{
			SudokuSolver solver = new SudokuBruteForceSolver(board);

			solver.solveFast();

			sudokuDisplayComponent.repaint();
			this.updateMessageLabel();
		}
	}

	private void load()
	{
		Object boardToLoad = JOptionPane.showInputDialog(
				frame,
				"Enter a saved game to load.\n"
				+ "WARNING: This cannot be undone.",
				"Load " + frame.getTitle(),
				JOptionPane.INFORMATION_MESSAGE,
				null, // Icon
				null, // selectionValues (null implies textbox)
				null); // initialSelectionValue

		if (boardToLoad != null)
		{
			if (SudokuBoard.isValidSavedBoard(boardToLoad.toString()))
			{
				SudokuBoard loadedBoard
						= new SudokuBoard(boardToLoad.toString());
				board.resetFrom(loadedBoard); // Note: all valued cells locked

				initialBoard.resetFrom(board);

				sudokuDisplayComponent.repaint();
				this.startGame();
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
				null, // selectionValues (null implies textbox)
				board.toString());
	}

	private void clearCells()
	{
		int choice = JOptionPane.showConfirmDialog(
				frame,
				"Clear all cells.\n"
				+ "WARNING: This will remove all values and possible values "
				+ "and cannot be undone.",
				"Clear all cells?",
				JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION)
		{
			board.getAllSudokuCells().forEach(this::clearSudokuCell);

			initialBoard.resetFrom(board);

			sudokuDisplayComponent.removeSelectedCell();
			sudokuDisplayComponent.repaint();
		}
	}

	private void clearPossibleValues()
	{
		board.getAllSudokuCells()
				.stream()
				.filter(sudokuCell -> !sudokuCell.isLocked())
				.forEach(SudokuCell::clearPossibleValues);

		sudokuDisplayComponent.repaint();
	}

	private void lockCells(boolean lockedState)
	{
		int choice = JOptionPane.showConfirmDialog(
				frame,
				String.format(
						"%s all cells.\n"
						+ "WARNING: This will remove all possible values "
						+ "and cannot be undone.",
						lockedState ? "Lock" : "Unlock"),
				"Lock all cells?",
				JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION)
		{
			board.getAllSudokuCells()
					.forEach(sudokuCell -> this.lockSudokuCell(
					sudokuCell,
					lockedState));

			initialBoard.resetFrom(board);

			sudokuDisplayComponent.removeSelectedCell();
			sudokuDisplayComponent.repaint();
		}
	}

	private void clearSudokuCell(SudokuCell sudokuCell)
	{
		sudokuCell.setLocked(false);
		sudokuCell.setValue(null);
		sudokuCell.clearPossibleValues();
	}

	private void lockSudokuCell(SudokuCell sudokuCell, boolean lockedState)
	{
		if (sudokuCell.getValue() != null)
		{
			sudokuCell.setLocked(lockedState);
		}
		else
		{
			sudokuCell.clearPossibleValues();
		}
	}

	private void updateMessageLabel()
	{
		messageLabel.setText(board.isSolved() ? "You win!" : "");
		frame.pack(); // keep the board canvas the same size
	}
}
