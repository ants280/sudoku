package com.github.ants280.sudoku.ui;

import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Sudoku
{
	public static void main(final String[] args)
	{
		SwingUtilities.invokeLater(new Sudoku()::run);
	}

	private Sudoku()
	{
	}

	private void run()
	{
		setLookAndFeel();

		Window frame = new SudokuFrame().getFrame();
		Thread.setDefaultUncaughtExceptionHandler(
				new SudokuUncaughtExceptionHandler(frame));

		// Center the Window on the screen.
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException
				| InstantiationException
				| IllegalAccessException
				| UnsupportedLookAndFeelException ex)
		{
			Logger.getLogger(Sudoku.class.getName())
					.log(
							Level.SEVERE,
							"Could not set system Look-And-Feel",
							ex);
		}

		// System look and feel overrides : UIManager.getDefaults()
		UIManager.put("Slider.paintValue", Boolean.FALSE);
	}
}
