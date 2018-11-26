package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.solver.SudokuSolver;
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
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SudokuSolverPopup implements ActionListener, ChangeListener
{
	private final SudokuBoard sudokuBoard;
	private final Runnable repaintCanvasCallback;
	private final SudokuSolver sudokuSolver;
	private final JDialog popupDialog;
	private final JSlider timerSlider;
	private final Timer timer;
	private final JProgressBar progressBar;
	private final JCheckBox resetPossibleValuesWhenStartingCheckBox;
	private final JButton startStopButton;
	private static final String ACTION_TIMER = "timer";
	private static final String BUTTON_RESET_POSSIBLE_VALUES
			= "Reset possible values";
	private static final String BUTTON_START = "Start";
	private static final String BUTTON_STOP = "Stop";
	private static final int ONE_SECOND_IN_MILLIS
			= (int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);

	public SudokuSolverPopup(
			JFrame popupOwner,
			SudokuBoard sudokuBoard,
			Runnable repaintCanvasCallback)
	{
		this.sudokuBoard = sudokuBoard;
		this.repaintCanvasCallback = repaintCanvasCallback;
		this.sudokuSolver = new SudokuSolver(sudokuBoard);
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
		this.startStopButton = new JButton(BUTTON_START);

		init();
	}

	private void init()
	{
		timerSlider.addChangeListener(this);
		timerSlider.setMajorTickSpacing(ONE_SECOND_IN_MILLIS);
		timerSlider.setMinorTickSpacing(ONE_SECOND_IN_MILLIS / 4);
		timerSlider.setPaintTicks(true);
		timerSlider.setPaintLabels(true);
		//Create the label table
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
		timerSlider.setLabelTable(labelTable);

		timer.setInitialDelay(timer.getDelay());
		timer.setActionCommand(ACTION_TIMER);
		timer.addActionListener(this);

		this.updateProgressBar();

		startStopButton.addActionListener(this);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(timerSlider);
		panel.add(progressBar);
		panel.add(resetPossibleValuesWhenStartingCheckBox);
		panel.add(startStopButton);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		popupDialog.add(panel);
		popupDialog.pack();
		popupDialog.setLocationRelativeTo(popupDialog.getParent());
		popupDialog.addWindowListener(new StopTimerWindowListener(timer));
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
					sudokuSolver.initialize();
				}

				if (timerSlider.getValue() == 0)
				{
					sudokuSolver.solveFast();
					popupDialog.setVisible(false);
				}
				else
				{
					timer.start();
					startStopButton.setText(BUTTON_STOP);
					resetPossibleValuesWhenStartingCheckBox.setEnabled(false);
				}
				repaintCanvasCallback.run();
				break;
			case BUTTON_STOP:
				timer.stop();
				startStopButton.setText(BUTTON_START);
				resetPossibleValuesWhenStartingCheckBox.setEnabled(true);
				break;
			case ACTION_TIMER:
				boolean moveMade = sudokuSolver.makeMove();

				if (!moveMade || sudokuBoard.isSolved())
				{
					timer.stop();
					popupDialog.setVisible(false);
				}

				if (moveMade)
				{
					repaintCanvasCallback.run();
				}
				break;
			default:
				throw new IllegalArgumentException(actionEvent.paramString());
		}
	}

	@Override
	public void stateChanged(ChangeEvent changeEvent)
	{
		timer.setDelay(timerSlider.getValue());
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
