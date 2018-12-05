package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.undo.CommandHistory;
import com.github.ants280.sudoku.game.undo.SudokuUndoCellCommand;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class SudokuLogicSolverTable
{
	private final DefaultTableModel tableModel;
	private final CommandHistory<SudokuUndoCellCommand> commandHistory;
	private final JTable table;

	public SudokuLogicSolverTable(CommandHistory<SudokuUndoCellCommand> commandHistory)
	{
		this.commandHistory = commandHistory;

		Object[] columnNames = new Object[]
		{
			"Jump",
			"Move Description"
		};
		this.tableModel = new DefaultTableModel(columnNames, 0)
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		this.table = new JTable(tableModel);

		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumn(columnNames[0])
				.setCellRenderer(new ButtonCellRenderer(table));
	}

	public void addRow(String moveDescription)
	{
		Object[] rowData = new Object[]
		{
			commandHistory.getMostRecentUndo(),
			moveDescription
		};

		tableModel.addRow(rowData);
	}

	public JTable getDisplayComponent()
	{
		return table;
	}

	private static class ButtonCellRenderer
			extends AbstractCellEditor
			implements TableCellEditor, TableCellRenderer, ActionListener
	{
		private final JTable table;
		private final JButton button;

		private ButtonCellRenderer(JTable table)
		{
			this.table = table;
			this.button = new JButton("Jump here");

			button.addActionListener(this);
			button.setBorderPainted(false);
		}

		@Override
		public Object getCellEditorValue()
		{
			return button;
		}

		@Override
		public Component getTableCellRendererComponent(
				JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column)
		{
			return button;
		}

		@Override
		public Component getTableCellEditorComponent(
				JTable table,
				Object value,
				boolean isSelected,
				int row,
				int column)
		{
			return button;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent)
		{
			System.out.println("Button clicked");
		}
	}
}
