package com.github.ants280.sudoku.ui;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Sudoku
{
	public static final boolean DEBUG = true;
	// NOTE: The version only shows as non-null when `mvn package` is run.
	public static final String VERSION
			= Sudoku.class.getPackage().getSpecificationVersion();

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

		Window frame = new SudokuFrame();
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
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}

		//UIManager.getDefaults().entrySet().forEach(System.out::println);
		// System look and feel overrides :
		UIManager.put("Slider.paintValue", Boolean.FALSE);
	}
}
