package com.github.ants280.sudoku2.ui;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultEditorKit;

public class SudokuUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	private final Component parentComponent;

	public SudokuUncaughtExceptionHandler(Component parentComponent)
	{
		this.parentComponent = parentComponent;
	}

	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		JTextArea textArea = new JTextArea(getFormattedStackTrace(e), 15, 30);

		JMenuItem copyMenuItem = new JMenuItem(
				new DefaultEditorKit.CopyAction());
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(copyMenuItem);
		textArea.addCaretListener(caretEvent -> copyMenuItem.setEnabled(
				caretEvent.getDot() != caretEvent.getMark()));
		textArea.setComponentPopupMenu(popupMenu);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(e.getMessage()));
		panel.add(new JScrollPane(textArea));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JOptionPane.showOptionDialog(
				parentComponent,
				panel,
				"Error",
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
				null,
				null,
				null);
	}

	private static String getFormattedStackTrace(Throwable e)
	{
		StringWriter stackTraceWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTraceWriter));
		return stackTraceWriter.toString();
	}
}
