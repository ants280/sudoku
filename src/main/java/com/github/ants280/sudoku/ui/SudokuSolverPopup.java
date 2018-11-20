package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuSolver;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class SudokuSolverPopup
{
	private final SwingWorker<?, ?> swingWorker;
	private final JDialog popupDialog;

	public SudokuSolverPopup(
			JFrame popupOwner,
			SudokuDisplayComponent sudokuDisplayComponent,
			SudokuBoard board)
	{
		this.popupDialog = new JDialog(popupOwner, "Solver", true);
		this.swingWorker
				= new SudokuSolverPopupSwingWorker(
						popupDialog,
						sudokuDisplayComponent,
						board);

		initPopupDialog();
	}

	private void initPopupDialog()
	{
		JProgressBar progressBar = new JProgressBar();

		JButton startButton = new JButton("Start");
		startButton.addActionListener(actionEvent -> handleStartButtonClick(
				startButton,
				progressBar));

		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(progressBar);
		panel.add(startButton);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		popupDialog.add(panel);
		popupDialog.pack();
		popupDialog.setResizable(false);
		popupDialog.setLocationRelativeTo(popupDialog.getParent());
		popupDialog.addWindowListener(
				new CancelSwingWorkerWindowListener(swingWorker));
	}

	private void handleStartButtonClick(
			JButton startButton, JProgressBar progressBar)
	{
		startButton.setEnabled(false);
		startButton.setText("Solving...");
		progressBar.setIndeterminate(true);

		swingWorker.execute();
	}

	public void setVisible(boolean visible)
	{
		popupDialog.setVisible(visible);
	}

	private static class SudokuSolverPopupSwingWorker
			extends SwingWorker<Void, Void>
	{
		private final JDialog popupDialog;
		private final SudokuDisplayComponent sudokuDisplayComponent;
		private final SudokuBoard board;

		public SudokuSolverPopupSwingWorker(
				JDialog popupDialog,
				SudokuDisplayComponent sudokuDisplayComponent,
				SudokuBoard board)
		{
			this.popupDialog = popupDialog;
			this.sudokuDisplayComponent = sudokuDisplayComponent;
			this.board = board;
		}

		@Override
		protected Void doInBackground() throws Exception
		{
			SudokuSolver sudokuSolver = new SudokuSolver(board);

			//Thread.sleep(10_000);
			sudokuSolver.start();

			if (Sudoku.DEBUG)
			{
				System.out.println("done solving");
			}
			popupDialog.setVisible(false);
			return null;
		}

		@Override
		protected void done()
		{
			sudokuDisplayComponent.repaint();
		}
	}

	private static class CancelSwingWorkerWindowListener
			extends WindowAdapter
			implements WindowListener
	{
		private final SwingWorker<?, ?> swingWorker;

		public CancelSwingWorkerWindowListener(SwingWorker<?, ?> swingWorker)
		{
			this.swingWorker = swingWorker;
		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			swingWorker.cancel(true);
		}
	}
}
