package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.undo.CommandHistory;
import com.github.ants280.sudoku.game.undo.SudokuUndoCellCommand;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.stream.IntStream;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
		this.tableModel = new DefaultTableModel(columnNames, 0)
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		this.table = new JTable(tableModel);

		table.setPreferredScrollableViewportSize(new Dimension(80, 50));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// must be befor next statement:
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
			switch (mouseEvent.getButton())
			{
				case MouseEvent.BUTTON1: // left mouse button
					if (mouseEvent.getClickCount() == 2)
					{
						int rowAtPoint = table.rowAtPoint(
								mouseEvent.getPoint());
						undoToRow(rowAtPoint);
					}
					break;
				default:
					break;
			}
		}

		private void undoToRow(int rowNumber)
		{
			Object undoIndexColumnValue = table.getModel().getValueAt(
					rowNumber,
					undoIndexColumnIndex);
			int undoIndex = Integer.parseInt(undoIndexColumnValue.toString());
			System.out.println("Double click at row " + rowNumber + "Undo number = " + undoIndex); // TODO

			int currentUndoCount = commandHistory.getUndoCount();
			int delta = undoIndex - currentUndoCount;

			System.out.printf("%sing next %d commands%n", (delta < 0 ? "undo" : "redo"), Math.abs(delta));
			if (delta != 0)
			{
				IntStream.range(0, Math.abs(delta))
						.forEach(i ->
						{
							if (delta < 0)
							{
								commandHistory.undo();
							}
							else
							{
								commandHistory.redo();
							}
						});

				repaintCanvasCallback.run();
			}
		}
	}
}
