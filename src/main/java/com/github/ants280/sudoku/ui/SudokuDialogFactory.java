package com.github.ants280.sudoku.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

/**
 * A collection of JOptionPane-like dialogs which allow copy-pasting of text and
 * allow validation
 */
public class SudokuDialogFactory
{
	private SudokuDialogFactory()
	{
	}

	public static String showLoadDialog(
			JFrame parentComponent,
			String title,
			String message,
			Function<String, Boolean> validationFunction,
			String invalidPopupTitle,
			String invalidPopupMessage)
	{
		return null;
	}

	public static void showExportDialog(
			JFrame parentComponent,
			String title,
			String[] messageLines,
			String exportContents)
	{
		JTextField textField = new JTextField(exportContents);
		addToolkit(textField);

		Object[] message = new Object[messageLines.length + 1];
		System.arraycopy(messageLines, 0, message, 0, messageLines.length);
		message[message.length - 1] = textField;
		Object[] okOptions = new Object[]
		{
			"OK" // TODO: Get proper text for L&F
		};
		JOptionPane optionPane = new JOptionPane(
				message,
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				null, //icon,
				okOptions,
				okOptions[0]);

//		textField.setPreferredSize(optionPane.getPreferredSize());
		Dimension textFieldPreferredSize = textField.getPreferredSize();
		Dimension optionPanePreferredSize = optionPane.getPreferredSize();
		textField.setPreferredSize(new Dimension(
				(int) optionPanePreferredSize.getWidth(),
				(int) textFieldPreferredSize.getHeight()));

		JDialog dialog = new JDialog(parentComponent, title, true);
		dialog.setContentPane(optionPane);
		dialog.pack();
		dialog.setLocationRelativeTo(dialog.getParent());

		dialog.setVisible(true);
	}

	public static void showExceptionDialog(
			Component parentComponent,
			String title,
			Exception ex)
	{
		StringWriter stackTraceWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stackTraceWriter));

		JTextArea textArea = new JTextArea(stackTraceWriter.toString(), 15, 30);
		addToolkit(textArea);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(ex.getMessage()));
		panel.add(new JScrollPane(textArea));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JOptionPane.showOptionDialog(
				parentComponent,
				panel,
				title,
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
				null,
				null,
				null);
	}

	private static void addToolkit(JTextComponent textComponent)
	{
		JMenuItem copyMenuItem = new JMenuItem(
				new DefaultEditorKit.CopyAction());
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(copyMenuItem);
		textComponent.addCaretListener(caretEvent -> copyMenuItem.setEnabled(
				caretEvent.getDot() != caretEvent.getMark()));
		textComponent.setComponentPopupMenu(popupMenu);
	}
}
