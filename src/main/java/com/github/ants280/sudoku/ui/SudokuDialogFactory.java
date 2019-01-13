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
			String message,
			String exportContents)
	{
		JTextField textField = new JTextField(exportContents);
		addToolkit(textField);

		JOptionPane pane = new JOptionPane(
				message,
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, // OK button
				null, // icon
				null, // options
				null); // initialValues

		// Make the width as wide of the text, not the text field:
		Object messageArray = new Object[]
		{
			message,
			textField
		};
		int previousWidth = (int) pane.getPreferredSize().getWidth();
		pane.setMessage(messageArray);
		pane.setPreferredSize(new Dimension(previousWidth, (int) pane.getPreferredSize().getHeight()));

		JDialog dialog = pane.createDialog(title);
		dialog.setVisible(true);
		dialog.dispose();
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
