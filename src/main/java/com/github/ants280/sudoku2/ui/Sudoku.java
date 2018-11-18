package com.github.ants280.sudoku2.ui;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Sudoku {

	public static final boolean DEBUG = true;
	public static final String VERSION = "2.0.0";

	public static void main(final String[] args) {
		// Set the menu of the ConvexHullFrame on the mac menu.
		if (System.getProperty("mrj.version") != null) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		SwingUtilities.invokeLater(new Sudoku()::run);
	}

	private Sudoku() {
	}

	private void run() {
		setLookAndFeel();

		Window frame = new SudokuFrame();
		Thread.setDefaultUncaughtExceptionHandler(
			new SudokuUncaughtExceptionHandler(frame));

		// Center the Window on the screen.
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void setLookAndFeel() {
		try {
			if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
}
