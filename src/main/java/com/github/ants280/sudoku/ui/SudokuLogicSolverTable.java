package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.undo.CommandHistory;
import com.github.ants280.sudoku.game.undo.SudokuUndoCellCommand;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.stream.IntStream;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SudokuLogicSolverTable
{
	private final DefaultTableModel tableModel;
	private final CommandHistory<SudokuUndoCellCommand> commandHistory;
	private final JTable table;

	public SudokuLogicSolverTable(
			CommandHistory<SudokuUndoCellCommand> commandHistory,
			Runnable repaintCanvasCallback)
	{
		this.commandHistory = commandHistory;

		String moveDescriptionColumnHeader = "Move Description";
		String undoIndexColumnHeader = "Undo Index";
		Object[] columnNames = new Object[]
		{
			moveDescriptionColumnHeader,
			undoIndexColumnHeader
		};
		this.tableModel = new UneditableTableModel(columnNames);
		this.table = new JTable(tableModel);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumn(moveDescriptionColumnHeader)
				.setCellRenderer(new ValueToolTipTableCellRenderer());

		// must be before next statement:
		int undoxIndexColumnIndex = table.getColumnModel()
				.getColumnIndex(undoIndexColumnHeader);
		table.removeColumn(table.getColumn(undoIndexColumnHeader));

		table.addMouseListener(new SudokuLogicTableMouseListener(
				table,
				undoxIndexColumnIndex,
				commandHistory,
				repaintCanvasCallback));
	}

	public void addRow(String moveDescription)
	{
		Object[] rowData = new Object[]
		{
			moveDescription,
			commandHistory.getUndoCount()
		};

		tableModel.addRow(rowData);
	}

	public JTable getDisplayComponent()
	{
		return table;
	}

	public void setEnabled(boolean enabled)
	{
		table.setEnabled(enabled);
	}

	private static class SudokuLogicTableMouseListener
			extends MouseAdapter
			implements MouseListener
	{
		private final JTable table;
		private final int undoIndexColumnIndex;
		private final CommandHistory<SudokuUndoCellCommand> commandHistory;
		private final Runnable repaintCanvasCallback;

		public SudokuLogicTableMouseListener(
				JTable table,
				int undoIndexColumnIndex,
				CommandHistory<SudokuUndoCellCommand> commandHistory,
				Runnable repaintCanvasCallback)
		{
			this.table = table;
			this.undoIndexColumnIndex = undoIndexColumnIndex;
			this.commandHistory = commandHistory;
			this.repaintCanvasCallback = repaintCanvasCallback;
		}

		@Override
		public void mouseReleased(MouseEvent mouseEvent)
		{
			if (mouseEvent.getButton() == MouseEvent.BUTTON1
					&& mouseEvent.getClickCount() == 2)
			{
				int rowAtPoint = table.rowAtPoint(
						mouseEvent.getPoint());
				this.undoToRow(rowAtPoint);
			}
		}

		private void undoToRow(int rowNumber)
		{
			Object undoIndexColumnValue = table.getModel().getValueAt(
					rowNumber,
					undoIndexColumnIndex);
			int undoIndex = Integer.parseInt(undoIndexColumnValue.toString());

			int currentUndoCount = commandHistory.getUndoCount();
			int delta = undoIndex - currentUndoCount;

			if (delta != 0)
			{
				Runnable action = delta < 0
						? commandHistory::undo
						: commandHistory::redo;
				IntStream.range(0, Math.abs(delta))
						.forEach(i -> action.run());

				repaintCanvasCallback.run();
			}
		}
	}

	private static class UneditableTableModel extends DefaultTableModel
	{
		private static final long serialVersionUID = 1L;

		public UneditableTableModel(Object[] columnNames)
		{
			super(columnNames, 0);
		}

		@Override
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	}

	private static class ValueToolTipTableCellRenderer
			extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(
				JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column)
		{
			if (value != null)
			{
				this.setToolTipText(value.toString());
			}

			return super.getTableCellRendererComponent(
					table,
					value,
					isSelected,
					hasFocus,
					row,
					column);
		}
	}
}
