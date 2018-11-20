package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractButton;
import javax.swing.JButton;
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
		// TODO : implement game restarting
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
		if (r != null && c != null
				&& board.getSudokuCell(r, c).getValue() == null)
		{
			JDialog dialog = new JDialog(frame, "[possible values]", true);

			JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
			SudokuCell selectedSudokuCell = board.getSudokuCell(r, c);
			for (int i = 1; i <= 9; i++)
			{
				int j = i;
				AbstractButton possibleValueButton = new JToggleButton(
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

	// TODO: combine code with setPossibleValue(ActionEvent), but distinguish possible values from normal ones
	public void setValue(ActionEvent event)
	{
		Integer r = canvas.getSelectedRow();
		Integer c = canvas.getSelectedCol();
		// TODO: It would be nice to also have this popup on right click (after selection the cell)
		if (r != null && c != null)
		{
			JDialog dialog = new JDialog(frame, "[value]", true);

			JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
			SudokuCell selectedSudokuCell = board.getSudokuCell(r, c);
			for (int i = 1; i <= 9; i++)
			{
				Integer v = i;
				AbstractButton valueButton = new JButton(
						String.valueOf(i));
				valueButton.setEnabled(
						!v.equals(selectedSudokuCell.getValue()));

				valueButton.addActionListener(
						actionEvent -> changeValue(r, c, v, dialog));

				possibleValueButtonsPanel.add(valueButton);
			}

			dialog.add(new JLabel("Select cell value"), BorderLayout.NORTH);
			dialog.add(possibleValueButtonsPanel, BorderLayout.CENTER);
			dialog.pack();
			dialog.setResizable(false);
			dialog.setLocationRelativeTo(frame);
			dialog.setVisible(true);
		}
	}

	private void changeValue(Integer r, Integer c, Integer v, JDialog dialog)
	{
		boolean valueChanged = board.getSudokuCell(r, c).setValue(v);

		dialog.setVisible(false);
		if (valueChanged)
		{
			canvas.repaint();
		}
	}

	public void solve(ActionEvent event)
	{
		SudokuSolverPopup sudokuSolverPopup
				= new SudokuSolverPopup(frame, canvas, board);
		sudokuSolverPopup.setVisible(true);
	}

	public void load(ActionEvent event)
	{
		// TODO implement game loading
	}
}
