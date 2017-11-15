package com.jpatterson.school.sudoku2.ui;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class SudokuActionListener
{
	private final SudokuFrame frame;

	SudokuActionListener(SudokuFrame frame)
	{
		this.frame = frame;
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
	
	public void solve(ActionEvent event)
	{
		// TODO
	}
	
	public void load(ActionEvent event)
	{
		// TODO
	}
}
