package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.ImmutableSudokuCell;
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
		this.valueFont = new Font("times", Font.PLAIN, cellLength);
		this.possibleValueFont = new Font("times", Font.PLAIN, cellLength / 3);
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
//		this.cellLength = 50;
//		this.valueFont = new Font("times", Font.PLAIN, cellLength);
//		this.possibleValueFont = new Font("times", Font.PLAIN, cellLength / 3);

		int minDimension = Math.min(this.getWidth(), this.getHeight());
		System.out.printf("%d: [w,h]=[%d,%d], cellLength=%d, minDimension=%d%n",
				System.currentTimeMillis(), this.getWidth(), this.getHeight(), cellLength, minDimension);

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

//		float fontSize = (float) (minDimension / (grid.getLength() * 3d));
//
//		if (tileFont.getSize2D() != fontSize)
//		{
//			tileFont = tileFont.deriveFont(fontSize);
//			this.repaint();
//		}
	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		((Graphics2D) graphics).setRenderingHints(ANTIALIAS_ON_RENDERING_HINT);

		this.paintSelectedCellBackground(graphics);
		this.paintCells(graphics);
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

	private void paintCells(Graphics graphics)
	{
		for (int row = 0; row < 9; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				this.paintCell(row, col, graphics);
			}
		}
	}

	private void paintCell(int row, int col, Graphics graphics)
	{
		SudokuCell sudokuCell = board.getSudokuCell(row, col);
		if (sudokuCell.getValue() != null)
		{
			graphics.setColor(sudokuCell instanceof ImmutableSudokuCell
					? Color.DARK_GRAY : Color.BLACK);
			graphics.setFont(valueFont);
			int fontHeightPx = (int) (valueFont.getSize() * 0.75d);
			FontMetrics fontMetrics = graphics.getFontMetrics();
			int charWidth = getFontWidth(fontMetrics, sudokuCell.getValue());
			int colOffset = (int) (xOffset + (cellLength * (col + 0.5d)) - (charWidth / 2d));
			int rowOffset = (int) (yOffset + (cellLength * (row + 0.5d)) + (fontHeightPx / 2d));

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
						row,
						col,
						possibleValue,
						graphics,
						fontMetrics,
						fontHeightPx));
			}
		}
	}

	private void paintPossibleCellValue(
			int row,
			int col,
			Integer possibleValue,
			Graphics graphics,
			FontMetrics fontMetrics,
			int fontHeightPx)
	{
		int charWidth = fontMetrics.stringWidth(possibleValue.toString());
		double colPercentage = col + ((1 + (2 * ((possibleValue - 1) % 3))) / 6d);
		double rowPercentage = row + ((1 + (2 * ((possibleValue - 1) / 3))) / 6d);
		int colOffset = (int) (xOffset + cellLength * colPercentage
				- (charWidth / 2d));
		int rowOffset = (int) (yOffset + cellLength * rowPercentage
				+ (fontHeightPx / 2d));

		graphics.drawString(
				possibleValue.toString(), colOffset, rowOffset);
	}

	private int getFontWidth(FontMetrics fontMetrics, Integer cellValue)
	{
		return fontMetrics.stringWidth(cellValue.toString());
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

	public void selectCellFromCoordinates(int x, int y)
	{
		this.setSelectedRow((y - yOffset) / cellLength);
		this.setSelectedCol((x - xOffset) / cellLength);
		this.repaint();

		if (Sudoku.DEBUG)
		{
			System.out.printf("selected cell: x,y=[%d,%d] => col,row=[%d,%d]\n",
					x,
					y,
					selectedCol,
					selectedRow);
		}
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
		if (Sudoku.DEBUG)
		{
			System.out.printf(
					"Incrementing selected row (%d) by %d.\n", selectedRow, i);
		}

		this.setSelectedRow(selectedRow + i);
		this.repaint();
	}

	public void incrementSelectedCol(int i)
	{
		if (Sudoku.DEBUG)
		{
			System.out.printf(
					"Incrementing selected col (%d) by %d.\n", selectedCol, i);
		}

		this.setSelectedCol(selectedCol + i);
		this.repaint();
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

	public void setSelectedCellValue(int cellValue)
	{
		if (selectedRow != null && selectedCol != null)
		{
			board.getSudokuCell(selectedRow, selectedCol)
					.setValue(cellValue == 0 ? null : cellValue);
			this.repaint();
		}
	}
}
