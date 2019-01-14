package com.github.ants280.sudoku.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.function.Predicate;
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

	public static Optional<String> showLoadDialog(
			JFrame parentComponent,
			String title,
			String message,
			Predicate<String> validationFunction,
			String invalidPopupTitle,
			String invalidPopupMessage)
	{
		JTextField textField = new JTextField();

		Object messageArray = new Object[]
		{
			message,
			textField
		};
		JOptionPane pane = new JOptionPane(
				messageArray,
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION,
				null, // icon
				null, // options
				null); // initialValues

		JDialog dialog = pane.createDialog(title);
		initTextComponent(textField, dialog, true);
		pane.addPropertyChangeListener(event ->
		{
			if (!JOptionPane.VALUE_PROPERTY.equals(event.getPropertyName())
					&& !JOptionPane.INPUT_VALUE_PROPERTY.equals(event.getPropertyName()))
			{
				return;
			}

			if (event.getNewValue() == null
					|| !event.getNewValue().equals(JOptionPane.OK_OPTION))
			{
				textField.setText(null);
			}
			else if (!validationFunction.test(textField.getText()))
			{
				JOptionPane.showMessageDialog(
						parentComponent,
						invalidPopupMessage,
						invalidPopupTitle,
						JOptionPane.ERROR_MESSAGE);
				dialog.setVisible(true); // keep dialog open
			}
		});
		dialog.setLocationRelativeTo(parentComponent);
		dialog.setVisible(true);
		dialog.dispose();

		Object inputValue = textField.getText();
		return Optional.ofNullable(
				inputValue == null || "".equals(inputValue)
				? null
				: inputValue.toString());
	}

	public static void showExportDialog(
			JFrame parentComponent,
			String title,
			String message,
			String exportContents)
	{
		JTextField textField = new JTextField(exportContents);

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
		initTextComponent(textField, dialog, true);
		dialog.setLocationRelativeTo(parentComponent);
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

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(ex.getMessage()));
		panel.add(new JScrollPane(textArea));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JOptionPane pane = new JOptionPane(
				panel,
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION, // OK button
				null, // icon
				null, // options
				null); // initialValues

		JDialog dialog = pane.createDialog(title);
		initTextComponent(textArea, dialog, false);
		dialog.setLocationRelativeTo(parentComponent);
		dialog.setVisible(true);
		dialog.dispose();
	}

	private static void initTextComponent(JTextComponent textComponent, JDialog dialog, boolean editable)
	{
		// add toolkit
		JMenuItem copyMenuItem = new JMenuItem(
				new DefaultEditorKit.CopyAction());
		copyMenuItem.setEnabled(false);
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(copyMenuItem);
		textComponent.addCaretListener(caretEvent -> copyMenuItem.setEnabled(
				caretEvent.getDot() != caretEvent.getMark()));
		if (editable)
		{
			JMenuItem cutMenuItem = new JMenuItem(
					new DefaultEditorKit.CutAction());
			cutMenuItem.setEnabled(false);
			JMenuItem pasteMenuItem = new JMenuItem(
					new DefaultEditorKit.PasteAction());
			popupMenu.add(cutMenuItem, 0); // add to front
			popupMenu.add(pasteMenuItem);
			textComponent.addCaretListener(caretEvent -> cutMenuItem.setEnabled(
					caretEvent.getDot() != caretEvent.getMark()));
		}
		textComponent.setComponentPopupMenu(popupMenu);


		// focus to textcomponent when dialog loads
		dialog.addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowGainedFocus(WindowEvent e)
			{
				textComponent.requestFocusInWindow();
			}
		});
		// select all the text when focus is granted
		textComponent.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent e)
			{
				textComponent.selectAll();
			}
		});

		textComponent.setEditable(editable);
	}
}
