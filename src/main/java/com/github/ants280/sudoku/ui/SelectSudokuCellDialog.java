package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class SelectSudokuCellDialog implements ActionListener
{
	private final JDialog dialog;
	private final BiConsumer<SudokuCell, SudokuValue> valueClickConsumer;
	private final boolean closeOnDialogOnButtonClick;
	private final SudokuCell selectedSudokuCell;

	public SelectSudokuCellDialog(
			JFrame parentFrame,
			String title,
			String message,
			BiPredicate<SudokuCell, SudokuValue> buttonSelectedFunction,
			BiConsumer<SudokuCell, SudokuValue> valueClickConsumer,
			boolean closeOnDialogOnButtonClick,
			SudokuCell selectedSudokuCell)
	{
		this.dialog = new JDialog(parentFrame, title, true);
		this.valueClickConsumer = valueClickConsumer;
		this.closeOnDialogOnButtonClick = closeOnDialogOnButtonClick;
		this.selectedSudokuCell = selectedSudokuCell;

		this.initDialog(message, buttonSelectedFunction);
	}

	private void initDialog(
			String message,
			BiPredicate<SudokuCell, SudokuValue> buttonSelectedFunction)
	{

		JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
		for (SudokuValue value : SudokuValue.values())
		{
			AbstractButton valueButton = new JToggleButton(
					value.getDisplayValue(),
					buttonSelectedFunction.test(selectedSudokuCell, value));

			valueButton.addActionListener(this);

			possibleValueButtonsPanel.add(valueButton);
		}

		String htmlDialogMessage = String.format(
				"<html>%s</html>",
				message.replaceAll("\n", "<br>"));
		dialog.add(new JLabel(htmlDialogMessage), BorderLayout.NORTH);
		dialog.add(possibleValueButtonsPanel, BorderLayout.CENTER);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(dialog.getParent());
	}

	public void setVisible(boolean visible)
	{
		dialog.setVisible(visible);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		String actionCommand = actionEvent.getActionCommand();
		if (actionCommand == null || actionCommand.length() > 1)
		{
			throw new IllegalArgumentException(
					"Invalid action : " + actionEvent);
		}

		SudokuValue buttonValue = SudokuValue.fromChar(actionCommand.charAt(0));

		valueClickConsumer.accept(selectedSudokuCell, buttonValue);

		if (closeOnDialogOnButtonClick)
		{
			dialog.setVisible(false);
		}
	}
}
