package com.jpatterson.school.sudoku2.ui;

import com.jpatterson.school.sudoku2.game.SudokuBoard;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class SudokuCanvas extends Canvas {

	private static final long serialVersionUID = 1136949818910622554L;
	private final SudokuBoard board;
	private final int cellLength;
	private final Font font;
	private Integer selectedRow;
	private Integer selectedCol;

	public SudokuCanvas(SudokuBoard board) {
		this.board = board;
		this.cellLength = 50;
		this.font = new Font("times", Font.PLAIN, cellLength);
		this.setFont(font);
		this.setSize(getBoardLength(), getBoardLength());

		// TODO: reset these on new game creation
		this.selectedRow = null;
		this.selectedCol = null;
	}
	
	@Override
	public void update(Graphics graphics) {
		BufferedImage lastDrawnImage
			= (BufferedImage) this.createImage(getWidth(), getHeight());

		//Draws the shape onto the BufferedImage
		this.paint(lastDrawnImage.getGraphics());

		//Draws the BufferedImage onto the PaintPanel
		graphics.drawImage(lastDrawnImage, 0, 0, this);
	}

	@Override
	public void paint(Graphics graphics) {
		((Graphics2D) graphics).setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		paintSelectedCellBackground(graphics);
		paintCells(graphics);
		paintLines(graphics);
	}

	private void paintSelectedCellBackground(Graphics graphics) {
		if (selectedRow != null && selectedCol != null) {
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.fillRect(
				selectedCol * (cellLength + 1),
				selectedRow * (cellLength + 1),
				cellLength + 1,
				cellLength + 1);
		}
	}

	private void paintCells(Graphics graphics) {
		int fontHeightPx = (int) (font.getSize() * 0.75d);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		for (int row = 0; row < 9; row++) {
			int rowOffset = getOffset(row + 0.5d) + fontHeightPx / 2;
			for (int col = 0; col < 9; col++) {
				paintCell(row, col, graphics, fontMetrics, rowOffset);
			}
		}
	}

	private void paintCell(int row, int col, Graphics graphics, FontMetrics fontMetrics, int rowOffset) {

		int cellValue = board.getValue(row, col);
		if (cellValue != 0) {
			Color cellColor = Color.BLACK;
			graphics.setColor(cellColor);

			int charWidth = getFontWidth(fontMetrics, cellValue);
			int colOffset = getOffset(col + 0.5d) - charWidth / 2;
			graphics.drawString(String.valueOf(cellValue), colOffset, rowOffset);
		}
	}

	private int getFontWidth(FontMetrics fontMetrics, Integer cellValue) {
		return fontMetrics.charsWidth(cellValue.toString().toCharArray(), 0, 1);
	}

	private void paintLines(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		for (int i = 0; i <= 9; i++) {
			int offset = i * (cellLength + 1);
			graphics.drawLine(0, offset, getBoardLength(), offset); // row
			graphics.drawLine(offset, 0, offset, getBoardLength()); // col

			// Paint thicker lines every three cells (except edges).
			if (i % 3 == 0) {
				if (i != 0) {
					graphics.drawLine(0, offset - 1, getBoardLength(), offset - 1); // row
					graphics.drawLine(offset - 1, 0, offset - 1, getBoardLength()); // col
				}
				if (i != 9) {
					graphics.drawLine(0, offset + 1, getBoardLength(), offset + 1); // row
					graphics.drawLine(offset + 1, 0, offset + 1, getBoardLength()); // col
				}
			}
		}
	}

	private int getBoardLength() {
		return getOffset(9);
	}

	private int getOffset(double cellNumber) {
		return (int) (cellNumber * (cellLength + 1)) + 1;
	}

	public void selectCellFromCoordinates(int x, int y) {
		this.setSelectedRow(y / cellLength);
		this.setSelectedCol(selectedCol = x / cellLength);
		this.repaint();

		if (Sudoku.DEBUG) {
			System.out.printf("selected cell: x,y=[%d,%d] => col,row=[%d,%d]\n",
				x,
				y,
				selectedCol,
				selectedRow);
		}
	}

	public Integer getSelectedRow() {
		return selectedRow;
	}

	public Integer getSelectedCol() {
		return selectedCol;
	}

	public void incrementSelectedRow(int i) {
		if (Sudoku.DEBUG) {
			System.out.printf("Incrementing selected row (%d) by %d.\n", selectedRow, i);
		}

		this.setSelectedRow(selectedRow + i);
		this.repaint();
	}

	public void incrementSelectedCol(int i) {
		if (Sudoku.DEBUG) {
			System.out.printf("Incrementing selected col (%d) by %d.\n", selectedCol, i);
		}

		this.setSelectedCol(selectedCol + i);
		this.repaint();
	}

	private void setSelectedRow(int i) {
		if (i >= 0 && i < 9) {
			selectedRow = i;
		}
	}

	private void setSelectedCol(int i) {
		if (i >= 0 && i < 9) {
			selectedCol = i;
		}
	}

	public void setSelectedCellValue(int cellValue) {
		if (selectedRow != null && selectedCol != null) {
			board.setValue(selectedRow, selectedCol, cellValue);
			this.repaint();
		}
	}
}
