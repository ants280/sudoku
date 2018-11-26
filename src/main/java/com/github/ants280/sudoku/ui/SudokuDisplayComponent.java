package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

public class SudokuDisplayComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	private static final RenderingHints ANTIALIAS_ON_RENDERING_HINT
			= new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

	private final SudokuBoard board;
	private int cellLength;
	private Font valueFont;
	private Font possibleValueFont;
	private int xOffset;
	private int yOffset;
	private Integer selectedRow;
	private Integer selectedCol;

	public SudokuDisplayComponent(SudokuBoard board)
	{
		this.board = board;
		this.cellLength = 50;
		this.valueFont = new Font(null, Font.PLAIN, cellLength);
		this.possibleValueFont = valueFont
				.deriveFont((float) (cellLength / 3d));
		this.xOffset = 0;
		this.yOffset = 0;

		this.selectedRow = null;
		this.selectedCol = null;

		init();
	}

	private void init()
	{
		int boardLength = cellLength * 9;
		Dimension preferredSize = new Dimension(boardLength, boardLength);
		this.setPreferredSize(preferredSize);

		this.addComponentListener(
				new SudokuComponentListener(this::componentResized));
	}

	private void componentResized()
	{
		int minDimension = Math.min(this.getWidth(), this.getHeight());

		int newCellLength = minDimension / 9;
		if (cellLength != newCellLength)
		{
			cellLength = newCellLength;
			xOffset = (this.getWidth() - (cellLength * 9)) / 2;
			yOffset = (this.getHeight() - (cellLength * 9)) / 2;

			valueFont = valueFont.deriveFont((float) cellLength);
			possibleValueFont = possibleValueFont
					.deriveFont((float) (cellLength / 3d));

			this.repaint();
		}
	}

	//<editor-fold defaultstate="collapsed" desc="painting">
	@Override
	protected void paintComponent(Graphics graphics)
	{
		((Graphics2D) graphics).setRenderingHints(ANTIALIAS_ON_RENDERING_HINT);

		this.paintSelectedCellBackground(graphics);
		board.getAllSudokuCells()
				.forEach(sudokuCell -> this.paintCell(sudokuCell, graphics));
		this.paintLines(graphics);
	}

	private void paintSelectedCellBackground(Graphics graphics)
	{
		if (selectedRow != null && selectedCol != null)
		{
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.fillRect(
					xOffset + (selectedCol * cellLength),
					yOffset + (selectedRow * cellLength),
					cellLength,
					cellLength);
		}
	}

	private void paintCell(SudokuCell sudokuCell, Graphics graphics)
	{
		if (sudokuCell.getValue() != null)
		{
			graphics.setColor(sudokuCell.isLocked()
					? Color.DARK_GRAY : Color.BLACK);
			graphics.setFont(valueFont);
			int charWidth = graphics.getFontMetrics()
					.stringWidth(sudokuCell.getValue().toString());
			int fontHeightPx = (int) (valueFont.getSize() * 0.75d);
			int cellColumn = sudokuCell.getIndex(SectionType.COLUMN);
			int cellRow = sudokuCell.getIndex(SectionType.ROW);
			int colOffset = (int) (xOffset - (charWidth / 2d)
					+ (cellLength * (cellColumn + 0.5d)));
			int rowOffset = (int) (yOffset + (fontHeightPx / 2d)
					+ (cellLength * (cellRow + 0.5d)));

			graphics.drawString(
					sudokuCell.getValue().toString(), colOffset, rowOffset);
		}
		else
		{
			if (!sudokuCell.getPossibleValues().isEmpty())
			{
				graphics.setColor(Color.BLACK);
				graphics.setFont(possibleValueFont);
				int fontHeightPx = (int) (possibleValueFont.getSize() * 0.75d);
				FontMetrics fontMetrics = graphics.getFontMetrics();

				sudokuCell.getPossibleValues()
						.forEach(possibleValue -> this.paintPossibleCellValue(
						sudokuCell,
						possibleValue,
						graphics,
						fontMetrics,
						fontHeightPx));
			}
		}
	}

	private void paintPossibleCellValue(
			SudokuCell sudokuCell,
			Integer possibleValue,
			Graphics graphics,
			FontMetrics fontMetrics,
			int fontHeightPx)
	{
		int charWidth = fontMetrics.stringWidth(possibleValue.toString());
		double colPercentage = sudokuCell.getIndex(SectionType.COLUMN)
				+ ((1 + (2 * ((possibleValue - 1) % 3))) / 6d);
		double rowPercentage = sudokuCell.getIndex(SectionType.ROW)
				+ ((1 + (2 * ((possibleValue - 1) / 3))) / 6d);
		int colOffset = (int) (xOffset + cellLength * colPercentage
				- (charWidth / 2d));
		int rowOffset = (int) (yOffset + cellLength * rowPercentage
				+ (fontHeightPx / 2d));

		graphics.drawString(
				possibleValue.toString(), colOffset, rowOffset);
	}

	private void paintLines(Graphics graphics)
	{
		int boardLength = cellLength * 9;

		graphics.setColor(Color.BLACK);
		for (int i = 0; i <= 9; i++)
		{
			int offset = i * cellLength;

			// row
			graphics.drawLine(
					xOffset + 0,
					yOffset + offset,
					xOffset + boardLength,
					yOffset + offset);

			// col
			graphics.drawLine(
					xOffset + offset,
					yOffset + 0,
					xOffset + offset,
					yOffset + boardLength);

			// Paint thicker lines every three cells (except edges).
			if (i % 3 == 0)
			{
				if (i != 0)
				{
					// row
					graphics.drawLine(
							xOffset + 0,
							yOffset + offset - 1,
							xOffset + boardLength,
							yOffset + offset - 1);

					// col
					graphics.drawLine(
							xOffset + offset - 1,
							yOffset + 0,
							xOffset + offset - 1,
							yOffset + boardLength);
				}
				if (i != 9)
				{
					// row
					graphics.drawLine(
							xOffset + 0,
							yOffset + offset + 1,
							xOffset + boardLength,
							yOffset + offset + 1);

					// col
					graphics.drawLine(
							xOffset + offset + 1,
							yOffset + 0,
							xOffset + offset + 1,
							yOffset + boardLength);
				}
			}
		}
	}
//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="selectedCell">
	public void selectCellFromCoordinates(int x, int y)
	{
		this.setSelectedRow((y - yOffset) / cellLength);
		this.setSelectedCol((x - xOffset) / cellLength);
		this.repaint();
	}

	public void removeSelectedCell()
	{
		selectedRow = null;
		selectedCol = null;
	}

	public Integer getSelectedRow()
	{
		return selectedRow;
	}

	public Integer getSelectedCol()
	{
		return selectedCol;
	}

	public void incrementSelectedRow(int i)
	{
		if (selectedRow != null)
		{
			this.setSelectedRow(selectedRow + i);

			this.repaint();
		}
	}

	public void incrementSelectedCol(int i)
	{
		if (selectedCol != null)
		{
			this.setSelectedCol(selectedCol + i);

			this.repaint();
		}
	}

	private void setSelectedRow(int i)
	{
		if (i >= 0 && i < 9)
		{
			selectedRow = i;
		}
	}

	private void setSelectedCol(int i)
	{
		if (i >= 0 && i < 9)
		{
			selectedCol = i;
		}
	}
//</editor-fold>
}
