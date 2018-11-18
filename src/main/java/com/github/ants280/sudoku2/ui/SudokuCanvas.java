package com.github.ants280.sudoku2.ui;

import com.github.ants280.sudoku2.game.ImmutableSudokuCell;
import com.github.ants280.sudoku2.game.SudokuBoard;
import com.github.ants280.sudoku2.game.SudokuCell;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class SudokuCanvas extends Canvas
{
	private static final long serialVersionUID = 1136949818910622554L;

	private final SudokuBoard board;
	private final int cellLength;
	private final Font valueFont;
	private final Font possibleValueFont;
	private Integer selectedRow;
	private Integer selectedCol;

	public SudokuCanvas(SudokuBoard board)
	{
		this.board = board;
		this.cellLength = 50;
		this.valueFont = new Font("times", Font.PLAIN, cellLength);
		this.possibleValueFont = new Font("times", Font.PLAIN, cellLength / 3);

		// TODO: reset these on new game creation
		this.selectedRow = null;
		this.selectedCol = null;

		init();
	}

	private void init()
	{
		this.setSize(getBoardLength(), getBoardLength());
	}

	@Override
	public void update(Graphics graphics)
	{
		BufferedImage lastDrawnImage
				= (BufferedImage) this.createImage(getWidth(), getHeight());

		//Draws the shape onto the BufferedImage
		this.paint(lastDrawnImage.getGraphics());

		//Draws the BufferedImage onto the PaintPanel
		graphics.drawImage(lastDrawnImage, 0, 0, this);
	}

	@Override
	public void paint(Graphics graphics)
	{
		((Graphics2D) graphics).setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		paintSelectedCellBackground(graphics);
		paintCells(graphics);
		paintLines(graphics);
	}

	private void paintSelectedCellBackground(Graphics graphics)
	{
		if (selectedRow != null && selectedCol != null)
		{
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.fillRect(
					selectedCol * (cellLength + 1),
					selectedRow * (cellLength + 1),
					cellLength + 1,
					cellLength + 1);
		}
	}

	private void paintCells(Graphics graphics)
	{
		for (int row = 0; row < 9; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				paintCell(row, col, graphics);
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
			int colOffset = getOffset(col + 0.5d) - (charWidth / 2);
			int rowOffset = getOffset(row + 0.5d) + (fontHeightPx / 2);

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
						.forEach(possibleValue -> paintPossibleCellValue(
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
		int charWidth = getFontWidth(fontMetrics, possibleValue);
		int colOffset = getOffset(
				col + ((1 + (2 * ((possibleValue - 1) % 3))) / 6d))
				- (charWidth / 2);
		int rowOffset = getOffset(
				row + ((1 + (2 * ((possibleValue - 1) / 3))) / 6d))
				+ (fontHeightPx / 2);

		graphics.drawString(
				possibleValue.toString(), colOffset, rowOffset);
	}

	private int getFontWidth(FontMetrics fontMetrics, Integer cellValue)
	{
		return fontMetrics.charsWidth(cellValue.toString().toCharArray(), 0, 1);
	}

	private void paintLines(Graphics graphics)
	{
		graphics.setColor(Color.BLACK);
		for (int i = 0; i <= 9; i++)
		{
			int offset = i * (cellLength + 1);
			graphics.drawLine(0, offset, getBoardLength(), offset); // row
			graphics.drawLine(offset, 0, offset, getBoardLength()); // col

			// Paint thicker lines every three cells (except edges).
			if (i % 3 == 0)
			{
				if (i != 0)
				{
					graphics.drawLine(0, offset - 1, getBoardLength(), offset - 1); // row
					graphics.drawLine(offset - 1, 0, offset - 1, getBoardLength()); // col
				}
				if (i != 9)
				{
					graphics.drawLine(0, offset + 1, getBoardLength(), offset + 1); // row
					graphics.drawLine(offset + 1, 0, offset + 1, getBoardLength()); // col
				}
			}
		}
	}

	private int getBoardLength()
	{
		return getOffset(9);
	}

	private int getOffset(double cellNumber)
	{
		return (int) (cellNumber * (cellLength + 1)) + 1;
	}

	public void selectCellFromCoordinates(int x, int y)
	{
		this.setSelectedRow(y / cellLength);
		this.setSelectedCol(x / cellLength);
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
			System.out.printf("Incrementing selected row (%d) by %d.\n", selectedRow, i);
		}

		this.setSelectedRow(selectedRow + i);
		this.repaint();
	}

	public void incrementSelectedCol(int i)
	{
		if (Sudoku.DEBUG)
		{
			System.out.printf("Incrementing selected col (%d) by %d.\n", selectedCol, i);
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
