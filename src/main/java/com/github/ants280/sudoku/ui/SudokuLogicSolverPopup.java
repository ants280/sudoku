package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.solver.SudokuLogicSolver;
import com.github.ants280.sudoku.game.solver.SudokuSolver;
import com.github.ants280.sudoku.game.undo.CommandHistory;
import com.github.ants280.sudoku.game.undo.SudokuCellUndoCommand;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SudokuLogicSolverPopup implements ActionListener, ChangeListener
{
	private final SudokuBoard sudokuBoard;
	private final CommandHistory<SudokuCellUndoCommand> commandHistory;
	private final Runnable repaintCanvasCallback;
	private final SudokuSolver sudokuSolver;
	private final SudokuLogicSolverTable solverTable;
	private final JPanel solverTablePanel;
	private final JDialog popupDialog;
	private final JSlider timerSlider;
	private final Timer timer;
	private final JProgressBar progressBar;
	private final JCheckBox resetPossibleValuesWhenStartingCheckBox;
	private final JCheckBox closePopupOnSolveCheckBox;
	private final JButton startStopButton;
	private static final String ACTION_TIMER = "timer";
	private static final String BUTTON_RESET_POSSIBLE_VALUES
			= toHtml("Reset possible values");
	private static final String BUTTON_CLOSE_SOLVER_WHEN_SOLVED
			= toHtml("Close solver when solved");
	private static final String BUTTON_START = "Start";
	private static final String BUTTON_STOP = "Stop";
	private static final int ONE_SECOND_IN_MILLIS
			= (int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);

	public SudokuLogicSolverPopup(
			JFrame popupOwner,
			SudokuBoard sudokuBoard,
			CommandHistory<SudokuCellUndoCommand> commandHistory,
			Runnable repaintCanvasCallback)
	{
		this.sudokuBoard = sudokuBoard;
		this.commandHistory = commandHistory;
		this.repaintCanvasCallback = repaintCanvasCallback;
		this.solverTable = new SudokuLogicSolverTable(
				commandHistory,
				repaintCanvasCallback);
		this.solverTablePanel = new JPanel();
		this.sudokuSolver = new SudokuLogicSolver(
				sudokuBoard,
				solverTable::addRow);
		this.popupDialog = new JDialog(popupOwner, "Solver", true);
		this.timerSlider = new JSlider(
				SwingConstants.VERTICAL, // orientation
				0, // min
				3 * ONE_SECOND_IN_MILLIS, // max
				ONE_SECOND_IN_MILLIS); // value
		this.timer = new Timer(timerSlider.getValue(), null);
		this.progressBar = new JProgressBar();
		this.resetPossibleValuesWhenStartingCheckBox
				= new JCheckBox(BUTTON_RESET_POSSIBLE_VALUES, true);
		this.closePopupOnSolveCheckBox
				= new JCheckBox(BUTTON_CLOSE_SOLVER_WHEN_SOLVED, false);
		this.startStopButton = new JButton(BUTTON_START);

		this.init();
	}

	private void init()
	{
		timerSlider.addChangeListener(this);
		timerSlider.setMajorTickSpacing(ONE_SECOND_IN_MILLIS);
		timerSlider.setMinorTickSpacing(ONE_SECOND_IN_MILLIS / 4);
		timerSlider.setPaintTicks(true);
		timerSlider.setPaintLabels(true);
		timerSlider.setLabelTable(this.createLabelTable());

		timer.setInitialDelay(timer.getDelay());
		timer.setActionCommand(ACTION_TIMER);
		timer.addActionListener(this);

		this.updateProgressBar();

		startStopButton.addActionListener(this);

		solverTablePanel.setPreferredSize(new Dimension(300, 200));
		solverTablePanel.setLayout(
				new BoxLayout(solverTablePanel, BoxLayout.Y_AXIS));
		solverTablePanel.add(new JLabel(toHtml(
				"Double click an entry below to jump to the state of the "
				+ "board right before the solver made the move(s).")));
		solverTablePanel.add(
				new JScrollPane(solverTable.getDisplayComponent()));
		solverTablePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		solverTablePanel.setVisible(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(timerSlider);
		panel.add(progressBar);
		panel.add(resetPossibleValuesWhenStartingCheckBox);
		panel.add(closePopupOnSolveCheckBox);
		panel.add(startStopButton);
		panel.add(solverTablePanel);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		popupDialog.add(panel);
		popupDialog.pack();
		popupDialog.setLocationRelativeTo(popupDialog.getParent());
		popupDialog.addWindowListener(new StopTimerWindowListener(timer));
		popupDialog.setResizable(true);
	}

	private Dictionary<Integer, JLabel> createLabelTable()
	{
		// Dictionary is required by JSlider.setLabelTable(Dictionary), so Hashtable must be used.
		@SuppressWarnings("squid:S1149")
		Dictionary<Integer, JLabel> labelTable = new Hashtable<>();

		for (int i = timerSlider.getMinimum();
				i <= timerSlider.getMaximum();
				i += ONE_SECOND_IN_MILLIS)
		{
			labelTable.put(
					i,
					new JLabel(String.format(
							"%.2f sec/move",
							Double.valueOf(i) / ONE_SECOND_IN_MILLIS)));
		}

		return labelTable;
	}

	public void setVisible(boolean visible)
	{
		popupDialog.setVisible(visible);
	}

	private void updateProgressBar()
	{
		long cellWithValues = sudokuBoard.getAllSudokuCells()
				.stream()
				.filter(sudokuCell -> sudokuCell.getValue() != null)
				.count();

		progressBar.setValue((int) (cellWithValues / 81d));
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		switch (actionEvent.getActionCommand())
		{
			case BUTTON_START:
				if (resetPossibleValuesWhenStartingCheckBox.isSelected())
				{
					resetPossibleValuesWhenStartingCheckBox.setSelected(false);

					commandHistory.reset();
					sudokuSolver.initialize();
				}

				if (timerSlider.getValue() == 0)
				{
					sudokuSolver.solveFast();
					this.handleBoardSolved();
					solverTable.setEnabled(true);
					this.showSolverTablePopup();
				}
				else
				{
					timer.start();
					startStopButton.setText(BUTTON_STOP);
					resetPossibleValuesWhenStartingCheckBox.setEnabled(false);
					solverTable.setEnabled(false);
				}
				repaintCanvasCallback.run();
				break;
			case BUTTON_STOP:
				timer.stop();
				startStopButton.setText(BUTTON_START);
				resetPossibleValuesWhenStartingCheckBox.setEnabled(true);
				solverTable.setEnabled(true);
				break;
			case ACTION_TIMER:
				boolean moveMade = sudokuSolver.makeMove();

				if (!moveMade || sudokuBoard.isSolved()) // TODO: Subscribe to events
				{
					timer.stop();
					this.handleBoardSolved();
					solverTable.setEnabled(true);
				}

				if (moveMade)
				{
					this.showSolverTablePopup();
					repaintCanvasCallback.run();
				}
				break;
			default:
				throw new IllegalArgumentException(actionEvent.paramString());
		}
	}

	private void showSolverTablePopup()
	{
		if (!solverTablePanel.isVisible())
		{
			solverTablePanel.setVisible(true);
			popupDialog.pack();
		}
	}

	private void handleBoardSolved()
	{
		if (sudokuBoard.isSolved()) // TODO: Subscribe to events
		{
			if (closePopupOnSolveCheckBox.isSelected())
			{
				popupDialog.setVisible(false);
			}
			else
			{
				solverTable.addRow(SudokuUiManager.BOARD_SOLVED_MESSAGE);
			}
		}
		else
		{
			solverTable.addRow("Solver stuck");
		}

		startStopButton.setEnabled(false);
		startStopButton.setText(BUTTON_STOP);
	}

	@Override
	public void stateChanged(ChangeEvent changeEvent)
	{
		timer.setDelay(timerSlider.getValue());
	}

	private static String toHtml(String text)
	{
		return String.format("<html>%s</html>", text);
	}

	private static class StopTimerWindowListener
			extends WindowAdapter
			implements WindowListener
	{
		private final Timer timer;

		public StopTimerWindowListener(Timer timer)
		{
			this.timer = timer;
		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			timer.stop();
		}
	}
}
