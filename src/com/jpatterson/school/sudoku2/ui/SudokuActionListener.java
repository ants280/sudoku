package com.jpatterson.school.sudoku2.ui;

import com.jpatterson.school.sudoku2.game.SudokuBoard;
import com.jpatterson.school.sudoku2.game.SudokuCell;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

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
		Integer r = canvas.getSelectedRow();
		Integer c = canvas.getSelectedCol();
		// TODO: It would be nice to also have this popup on right click (after selection the cell)
		if (r != null && c != null)
		{
			JDialog dialog = new JDialog(frame, "Select", true);

			JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
			SudokuCell selectedSudokuCell = board.getSudokuCell(r, c);
			for (int i = 1; i <= 9; i++)
			{
				int j = i;
				JToggleButton possibleValueButton = new JToggleButton(
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
							canvas.repaint();
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

	public void solve(ActionEvent event)
	{
		// TODO
	}

	public void load(ActionEvent event)
	{
		// TODO
	}
}
