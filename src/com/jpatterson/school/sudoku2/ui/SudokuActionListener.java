package com.jpatterson.school.sudoku2.ui;

import com.jpatterson.school.sudoku2.game.SudokuBoard;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SudokuActionListener
{
	private final Frame frame;
	private final SudokuCanvas canvas;
	private final SudokuBoard board;

	SudokuActionListener(Frame frame, SudokuCanvas canvas, SudokuBoard board)
	{
		this.frame = frame;
		this.canvas = canvas;
		this.board = board;
	}

	public void restart(ActionEvent event)
	{
		// TODO
	}

	public void exit(ActionEvent event)
	{
		Runtime.getRuntime().exit(0);
	}

	public void help(ActionEvent event)
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

	public void about(ActionEvent event)
	{
		JOptionPane.showMessageDialog(frame,
			"(c) 2017 Jacob Patterson"
			+ "\n"
			+ "\nDescription taken from my newspaper,"
			+ "\n(c) Universal Uclick",
			"About " + frame.getTitle(),
			JOptionPane.INFORMATION_MESSAGE);
	}

	public void setPossibleValue(ActionEvent event)
	{
		Integer selectedRow = canvas.getSelectedRow();
		Integer selectedCol = canvas.getSelectedCol();
		// TODO: It would be nice to also have this popup on right click (after selection the cell)
		if (selectedRow != null && selectedCol != null)
		{
			JDialog dialog = new JDialog(frame, "Select possible cell value", true);

			JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
			for (int i = 1; i <= 9; i++)
			{
				int j = i;
				JButton possibleValueButton = new JButton(String.valueOf(i));
				// TODO: It would be cool if seleted possible values could be checked and uncheckd.
				possibleValueButton.addActionListener(actionEvent
					-> 
					{
						// TODO: Make method?
						board.addPossibleValue(selectedRow, selectedCol, j);

						dialog.setVisible(false);
				});

				possibleValueButtonsPanel.add(possibleValueButton);
			}

			dialog.add(possibleValueButtonsPanel);
			dialog.pack();
			dialog.setLocationRelativeTo(frame);
			dialog.setVisible(true);
		}
	}

	public void solve(ActionEvent event)
	{
		// TODO
	}

	public void load(ActionEvent event)
	{
		// TODO
	}
}
