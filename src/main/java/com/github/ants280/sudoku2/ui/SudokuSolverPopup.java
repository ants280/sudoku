package com.github.ants280.sudoku2.ui;

import com.github.ants280.sudoku2.game.SudokuBoard;
import com.github.ants280.sudoku2.game.SudokuSolver;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class SudokuSolverPopup
{
	private final SudokuCanvas canvas;
	private final SwingWorker<?, ?> swingWorker;
	private final JDialog popupDialog;

	public SudokuSolverPopup(Frame popupOwner, SudokuCanvas canvas, SudokuBoard board)
	{
		this.canvas = canvas;
		this.swingWorker = new SwingWorker<Void, Void>()
		{
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
				canvas.repaint();
			}
		};

		this.popupDialog = new JDialog(popupOwner, "Solver", true);

		initPopupDialog();
	}

	private void initPopupDialog()
	{
		JProgressBar progressBar = new JProgressBar();

		JButton startButton = new JButton("Start");
		startButton.addActionListener(actionEvent
				->
		{
			startButton.setEnabled(false);
			startButton.setText("Solving...");
			progressBar.setIndeterminate(true);

			swingWorker.execute();
		});

		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(progressBar);
		panel.add(startButton);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		popupDialog.add(panel);
		popupDialog.pack();
		popupDialog.setResizable(false);
		popupDialog.setLocationRelativeTo(popupDialog.getParent());
		popupDialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				swingWorker.cancel(true);
			}
		});
	}

	public void setVisible(boolean visible)
	{
		popupDialog.setVisible(visible);
	}
}
