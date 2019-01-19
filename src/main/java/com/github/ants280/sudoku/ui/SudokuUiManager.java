package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuEvent;
import com.github.ants280.sudoku.game.SudokuValue;
import com.github.ants280.sudoku.game.solver.SudokuBruteForceSolver;
import com.github.ants280.sudoku.game.solver.SudokuLogicSolver;
import com.github.ants280.sudoku.game.solver.SudokuSolver;
import com.github.ants280.sudoku.game.undo.CommandHistory;
import com.github.ants280.sudoku.game.undo.SudokuCellChangeType;
import com.github.ants280.sudoku.game.undo.SudokuCellUndoCommand;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
	private final CommandHistory<SudokuCellUndoCommand> commandHistory;
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
			CommandHistory<SudokuCellUndoCommand> commandHistory,
			JMenu setValueMenu,
			JMenu setPossibleValueMenu)
	{

		this.frame = frame;
		this.sudokuDisplayComponent = sudokuDisplayComponent;
		this.board = board;
		this.messageLabel = messageLabel;
		this.commandHistory = commandHistory;
		this.initialBoard = new SudokuBoard(board.toString());
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

		this.init(setValueMenu, setPossibleValueMenu);
	}

	private void init(JMenu setValueMenu, JMenu setPossibleValueMenu)
	{
		this.createValueMenuItems(this::setSudokuCellValue)
				.forEach(setValueMenu::add);
		this.createValueMenuItems(this::toggleSudokuCellPossibleValue)
				.forEach(setPossibleValueMenu::add);

		this.addListeners();

		board.addSolvedChangedConsumer(this::handleSolvedChanged);
	}

	private void handleSolvedChanged(SudokuEvent<SudokuBoard, Boolean> solvedChangedEvent)
	{
		if (solvedChangedEvent.getNewValue())
		{
			assert board.isSolved();

			this.removeListeners();
			sudokuDisplayComponent.removeSelectedCell();
		}
		else
		{
			this.addListeners();
			commandHistory.reset();
		}

		if (!solvedChangedEvent.getOldValue()
				.equals(solvedChangedEvent.getNewValue()))
		{
			messageLabel.setText(board.isSolved() ? BOARD_SOLVED_MESSAGE : null);
		}
	}

	private Map<String, Runnable> createActionCommands()
	{
		Map<String, Runnable> tempActionCommands = new HashMap<>();
		tempActionCommands.put(RESTART_MI, this::restart);
		tempActionCommands.put(LOAD_MI, this::load);
		tempActionCommands.put(EXPORT_MI, this::export);
		tempActionCommands.put(EXIT_MI, this::exit);
		tempActionCommands.put(UNDO_MI, commandHistory::undo);
		tempActionCommands.put(REDO_MI, commandHistory::redo);
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
			sudokuDisplayComponent.getComponent()
					.addMouseListener(mouseListener);
		}
	}

	private void removeListeners()
	{
		if (listenersAdded)
		{
			listenersAdded = false;
			frame.removeKeyListener(keyListener);
			sudokuDisplayComponent.getComponent()
					.removeMouseListener(mouseListener);
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		actionCommands.get(actionEvent.getActionCommand()).run();
	}

	private void restart()
	{
		board.resetFrom(initialBoard);
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
		}
	}

	private void toggleSudokuCellPossibleValue(
			SudokuCell sudokuCell,
			SudokuValue v)
	{
		if (!sudokuCell.isLocked())
		{
			sudokuCell.togglePossibleValue(v);
		}
	}

	private void solveLogic()
	{
		Consumer<SudokuCell> selectSudokuCellConsumer = sudokuCell ->
		{
			if (sudokuCell == null)
			{
				sudokuDisplayComponent.removeSelectedCell();
			}
			else
			{
				sudokuDisplayComponent.selectCell(sudokuCell);
			}
		};
		SudokuLogicSolverPopup sudokuSolverPopup
				= new SudokuLogicSolverPopup(
						frame,
						board,
						commandHistory,
						selectSudokuCellConsumer);

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

			solver.solveFast();
			commandHistory.reset();
		}
	}

	private void load()
	{
		Optional<String> optionalBoardText
				= SudokuDialogFactory.showLoadDialog(
						frame,
						"Load " + frame.getTitle(),
						"Enter a saved game to load.\n"
						+ "WARNING: This cannot be undone.",
						SudokuBoard::isValidSavedBoard,
						"Invalid Board for " + frame.getTitle(),
						"Error loading board.\n"
						+ "It should be something like '{<81 digits>}' "
						+ "(without quotes).");

		if (optionalBoardText.isPresent())
		{
			commandHistory.reset();
			SudokuBoard loadedBoard
					= new SudokuBoard(optionalBoardText.get());
			board.resetFrom(loadedBoard); // Note: all valued cells locked
			initialBoard.resetFrom(board);

			sudokuDisplayComponent.removeSelectedCell();
		}
	}

	private void export()
	{
		SudokuDialogFactory.showExportDialog(
				frame,
				"Export " + frame.getTitle(),
				"Copy the game state to load later.\n"
				+ "WARNING: This does not save possible values.",
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
		}
	}

	private void clearPossibleValues()
	{
		board.getAllSudokuCells()
				.stream()
				.filter(sudokuCell -> !sudokuCell.isLocked())
				.forEach(SudokuCell::clearPossibleValues);
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

	private void getHint()
	{
		sudokuDisplayComponent.removeSelectedCell();

		CommandHistory<SudokuCellUndoCommand> hintCommandHistory = new CommandHistory<>();
		SudokuBoard hintBoard = new SudokuBoard(board);
		hintBoard.addCellValueChangedConsumer(
				cellValueChangedEvent -> hintCommandHistory.addCommand(
						new SudokuCellUndoCommand(cellValueChangedEvent, SudokuCellChangeType.SET_VALUE)));
		SudokuSolver hintSolver = new SudokuLogicSolver(hintBoard, null);

		hintSolver.initialize();
		boolean moveMade = hintSolver.makeMove();

		if (moveMade)
		{
			SudokuCellUndoCommand lastSetValueCommand = hintCommandHistory.undo();
			if (lastSetValueCommand != null)
			{
				SudokuCell hintCell = lastSetValueCommand.getSudokuCell();
				sudokuDisplayComponent.selectCell(hintCell);
			}
		}
	}
}
