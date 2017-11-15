package com.jpatterson.school.sudoku2.ui;

import java.awt.event.ActionEvent;

public class SudokuActionListener
	//implements ActionListener
{
	private final SudokuFrame frame; // TODO: Is this used?
	
	SudokuActionListener(SudokuFrame frame)
	{
		this.frame = frame;
	}

//	@Override
//	public void actionPerformed(ActionEvent e)
//	{
//		throw new UnsupportedOperationException("Not supported yet.");
//	}
	
	public void restart(ActionEvent event)
	{
		// TODO
	}
	
	public void exit(ActionEvent event)
	{
		Runtime.getRuntime().exit(0);
	}
}
