package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.undo.CommandHistory;
import com.github.ants280.sudoku.game.undo.SudokuUndoCellCommand;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
	}

	public void addRow(String moveDescription)
	{
		Object[] rowData = new Object[]
		{
			moveDescription,
			commandHistory.getMostRecentUndo()
		};

		tableModel.addRow(rowData);
	}

	public JTable getDisplayComponent()
	{
		return table;
	}
}
