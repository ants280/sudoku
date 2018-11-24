package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SudokuCell;
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
	private final BiConsumer<SudokuCell, Integer> valueClickConsumer;
	private final boolean closeOnDialogOnButtonClick;
	private final SudokuCell selectedSudokuCell;

	public SelectSudokuCellDialog(
			JFrame parentFrame,
			String title,
			String message,
			BiPredicate<SudokuCell, Integer> buttonSelectedFunction,
			BiConsumer<SudokuCell, Integer> valueClickConsumer,
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
			BiPredicate<SudokuCell, Integer> buttonSelectedFunction)
	{

		JPanel possibleValueButtonsPanel = new JPanel(new GridLayout(3, 3));
		for (int i = 1; i <= 9; i++)
		{
			Integer v = i;
			AbstractButton valueButton = new JToggleButton(
					String.valueOf(i),
					buttonSelectedFunction.test(selectedSudokuCell, v));

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
		int buttonValue = Integer.parseInt(actionEvent.getActionCommand());

		valueClickConsumer.accept(selectedSudokuCell, buttonValue);

		if (closeOnDialogOnButtonClick)
		{
			dialog.setVisible(false);
		}
	}
}