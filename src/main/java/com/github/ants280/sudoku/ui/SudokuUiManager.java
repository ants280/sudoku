package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import com.github.ants280.sudoku.game.solver.SudokuBruteForceSolver;
import com.github.ants280.sudoku.game.solver.SudokuLogicSolver;
import com.github.ants280.sudoku.game.solver.SudokuSolver;
import com.github.ants280.sudoku.game.undo.CommandHistory;
import com.github.ants280.sudoku.game.undo.SudokuUndoBoard;
import com.github.ants280.sudoku.game.undo.SudokuUndoCellCommand;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class SudokuUiManager implements ActionListener
{
	public static final String FILE_M = "File";
	public static final String RESTART_MI = "Restart";
	public static final String LOAD_MI = "Load Game...";
	public static final String EXPORT_MI = "Export Game...";
	public static final String EXIT_MI = "Exit";
	public static final String ACTION_M = "Action";
	public static final String UNDO_MI = "Undo";
	public static final String REDO_MI = "Redo";
	public static final String HINT_MI = "Hint";
	public static final String SET_VALUE_MI = "Set value...";
	public static final String SET_POSSIBLE_VALUE_MI = "Set possible value...";
	public static final String CLEAR_POSSIBLE_VALUES_MI = "Clear possible values";
	public static final String CLEAR_CELLS_MI = "Clear cells...";
	public static final String LOCK_CELLS_MI = "Lock cells...";
	public static final String UNLOCK_CELLS_MI = "Unlock cells...";
	public static final String SOLVE_LOGIC_MI = "Solve with Logic...";
	public static final String SOLVE_BRUTE_FORCE_MI = "Solve with Brute Force...";
	public static final String HELP_M = "Help";
	public static final String HELP_MI = "Help";
	public static final String ABOUT_MI = "About";
	public static final String BOARD_SOLVED_MESSAGE = "Board Solved";

	private final JFrame frame;
	private final SudokuDisplayComponent sudokuDisplayComponent;
	private final SudokuBoard board;
	private final JLabel messageLabel;
	private final CommandHistory<SudokuUndoCellCommand> commandHistory;
	private final JMenu setValueMenu;
	private final JMenu setPossibleValueMenu;
	private final SudokuBoard initialBoard;
	private final Map<String, Runnable> actionCommands;
	private final SudokuMouseListener mouseListener;
	private final SudokuKeyListener keyListener;
	private boolean listenersAdded;

	public SudokuUiManager(
			JFrame frame,
			SudokuDisplayComponent sudokuDisplayComponent,
			SudokuBoard board,
			JLabel messageLabel,
			CommandHistory<SudokuUndoCellCommand> commandHistory,
			JMenu setValueMenu,
			JMenu setPossibleValueMenu)
	{

		this.frame = frame;
		this.sudokuDisplayComponent = sudokuDisplayComponent;
		this.board = board;
		this.messageLabel = messageLabel;
		this.commandHistory = commandHistory;
		this.initialBoard = new SudokuBoard(board.toString());
		this.setValueMenu = setValueMenu;
		this.setPossibleValueMenu = setPossibleValueMenu;
		this.actionCommands = this.createActionCommands();
		this.mouseListener = new SudokuMouseListener(
				this::selectCell,
				this::setValue,
				this::setPossibleValue);
		this.keyListener = new SudokuKeyListener(
				sudokuValue -> this.setSudokuCellValue(
						sudokuDisplayComponent.getSelectedCell(),
						sudokuValue),
				this.sudokuDisplayComponent::moveSelectedCell);
		this.listenersAdded = false;

		this.init();
	}

	private void init()
	{
		this.createValueMenuItems(this::setSudokuCellValue)
				.forEach(setValueMenu::add);
		this.createValueMenuItems(this::toggleSudokuCellPossibleValue)
				.forEach(setPossibleValueMenu::add);

		this.addListeners();
	}

	private Map<String, Runnable> createActionCommands()
	{
		Map<String, Runnable> tempActionCommands = new HashMap<>();
		tempActionCommands.put(RESTART_MI, this::restart);
		tempActionCommands.put(LOAD_MI, this::load);
		tempActionCommands.put(EXPORT_MI, this::export);
		tempActionCommands.put(EXIT_MI, this::exit);
		tempActionCommands.put(UNDO_MI, this::undo);
		tempActionCommands.put(REDO_MI, this::redo);
		tempActionCommands.put(HINT_MI, this::getHint);
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

	private void addListeners()
	{
		if (!listenersAdded)
		{
			listenersAdded = true;
			frame.addKeyListener(keyListener);
			sudokuDisplayComponent.addMouseListener(mouseListener);
			this.disableSelectedCellMenus();
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
		this.disableSelectedCellMenus();
		sudokuDisplayComponent.removeSelectedCell();
		commandHistory.reset();
	}

	private void endGame()
	{
		this.removeListeners();
		this.updateMessageLabel();
		this.disableSelectedCellMenus();
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
		SudokuCell selectedCell = sudokuDisplayComponent.getSelectedCell();
		setValueMenu.setEnabled(selectedCell != null);
		setPossibleValueMenu.setEnabled(
				selectedCell != null && selectedCell.getValue() == null);
	}

	private void disableSelectedCellMenus()
	{
		setValueMenu.setEnabled(false);
		setPossibleValueMenu.setEnabled(false);
	}

	private void setValue(int x, int y)
	{
		if (sudokuDisplayComponent.getSelectedCell() != null)
		{
			this.showValuePopupMenu(
					x,
					y,
					"Select cell value",
					this::setSudokuCellValue);
		}
	}

	private void setPossibleValue(int x, int y)
	{
		SudokuCell selectedCell = sudokuDisplayComponent.getSelectedCell();
		if (selectedCell != null
				&& selectedCell.getValue() == null)
		{
			this.showValuePopupMenu(
					x,
					y,
					"Select possible cell values",
					this::toggleSudokuCellPossibleValue);
		}
	}

	private void showValuePopupMenu(
			int x,
			int y,
			String title,
			BiConsumer<SudokuCell, SudokuValue> valueClickConsumer)
	{
		SudokuCell selectedCell = sudokuDisplayComponent.getSelectedCell();
		Predicate<SudokuCell> lockedSudokuCellPredicate = SudokuCell::isLocked;
		if (selectedCell != null
				&& lockedSudokuCellPredicate.negate().test(selectedCell))
		{
			JPopupMenu popupMenu = new JPopupMenu(title);

			this.createValueMenuItems(valueClickConsumer)
					.forEach(popupMenu::add);

			popupMenu.show(frame, x, y);
		}
	}

	private List<JMenuItem> createValueMenuItems(
			BiConsumer<SudokuCell, SudokuValue> valueClickConsumer)
	{
		return Stream.of(SudokuValue.values())
				.map(sudokuValue -> this.createValueMenuItem(
				sudokuValue,
				valueClickConsumer))
				.collect(Collectors.toList());
	}

	private JMenuItem createValueMenuItem(
			SudokuValue sudokuValue,
			BiConsumer<SudokuCell, SudokuValue> valueClickConsumer)
	{
		JMenuItem menuItem = new JMenuItem(sudokuValue.getDisplayValue());

		menuItem.addActionListener(actionEvent -> valueClickConsumer.accept(
				sudokuDisplayComponent.getSelectedCell(),
				sudokuValue));

		return menuItem;
	}

	private void setSudokuCellValue(
			SudokuCell sudokuCell,
			SudokuValue value)
	{
		if (!sudokuCell.isLocked())
		{
			SudokuValue oldValue = sudokuCell.getValue();
			sudokuCell.setValue(value == oldValue ? null : value);
			setPossibleValueMenu.setEnabled(value != null && value == oldValue);

			sudokuDisplayComponent.repaint();
			this.updateMessageLabel();

			if (board.isSolved())
			{
				this.endGame();
			}
		}
	}

	private void toggleSudokuCellPossibleValue(
			SudokuCell sudokuCell,
			SudokuValue v)
	{
		if (!sudokuCell.isLocked())
		{
			sudokuCell.togglePossibleValue(v);

			sudokuDisplayComponent.repaint();
		}
	}

	private void solveLogic()
	{
		SudokuLogicSolverPopup sudokuSolverPopup
				= new SudokuLogicSolverPopup(
						frame,
						board,
						commandHistory,
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
				+ "if any.\n"
				+ "This will clear all possible values "
				+ "and only update the board if it possible to solve.",
				"Solve Brute force?",
				JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION)
		{
			SudokuSolver solver = new SudokuBruteForceSolver(board);

			commandHistory.reset();
			commandHistory.setEnabled(false);
			solver.solveFast();
			commandHistory.setEnabled(true);

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
				commandHistory.reset();
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
				+ "WARNING: This does not save possible values.",
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
			commandHistory.reset();
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
			commandHistory.reset();
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
		messageLabel.setText(board.isSolved() ? BOARD_SOLVED_MESSAGE : "");
		frame.pack(); // keep the board canvas the same size
	}

	private void undo()
	{
		commandHistory.undo();

		sudokuDisplayComponent.repaint();
		this.updateMessageLabel();
	}

	private void redo()
	{
		commandHistory.redo();

		sudokuDisplayComponent.repaint();
		this.updateMessageLabel();
	}

	private void getHint()
	{
		// TODO: disable menu item when board is solved.
		if (board.isSolved())
		{
			return;
		}

		sudokuDisplayComponent.removeSelectedCell();
		sudokuDisplayComponent.repaint();

		BiConsumer<Boolean, Boolean> undoRedoEmptyConsumer = (undoEmpty, redoEmpty) ->
		{
		};
		CommandHistory<SudokuUndoCellCommand> hintCommandHistory = new CommandHistory<>(undoRedoEmptyConsumer);
		SudokuUndoBoard hintBoard = new SudokuUndoBoard(hintCommandHistory);
		hintBoard.resetFrom(board); // TODO: it would be nice to have a SudokuBoard(copyConstructor) that calls resetFrom().
		Consumer<String> moveDescriptionConsumer = moveDescription ->
		{
		};
		SudokuSolver hintSolver
				= new SudokuLogicSolver(hintBoard, moveDescriptionConsumer);

		hintCommandHistory.setEnabled(false);
		hintSolver.initialize();
		hintCommandHistory.setEnabled(true);
		boolean moveMade = hintSolver.makeMove();

		if (moveMade)
		{
			// TODO: It should be possible to specify to the solver not to remove possible values after the move is made. (or to do so in separate moves)
			while (hintCommandHistory.getUndoCount() > 1)
			{
				hintCommandHistory.undo();
			}

			SudokuUndoCellCommand firstCommand = hintCommandHistory.undo();
			if (firstCommand != null)
			{
				SudokuCell hintCell = firstCommand.getSudokuCell();
				sudokuDisplayComponent.selectCell(hintCell);
				sudokuDisplayComponent.repaint();
			}
		}
	}
}
