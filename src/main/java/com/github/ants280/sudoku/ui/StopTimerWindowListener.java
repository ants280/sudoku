package com.github.ants280.sudoku.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.Timer;

public class StopTimerWindowListener
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
