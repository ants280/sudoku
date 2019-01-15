package com.github.ants280.sudoku.ui;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuEvent;
import com.github.ants280.sudoku.game.SudokuValue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JComponent;

public class SudokuDisplayComponent
{
	private static final RenderingHints ANTIALIAS_ON_RENDERING_HINT
			= new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

	private final JComponent component;
	private final SudokuBoard board;
	private int cellLength;
	private Font valueFont;
	private Font possibleValueFont;
	private int xOffset;
	private int yOffset;
	private Integer selectedRow;
	private Integer selectedCol;
	private final List<Consumer<SudokuEvent<?, SudokuCell>>> selectedCellChangedConsumers;

	public SudokuDisplayComponent(SudokuBoard board)
	{
		this.component = new SudokuDisplayComponentImpl();
		this.board = board;
		this.cellLength = 50;
		this.valueFont = new Font(null, Font.PLAIN, cellLength);
		this.possibleValueFont = valueFont
				.deriveFont((float) (cellLength / 3d));
		this.xOffset = 0;
		this.yOffset = 0;

		this.selectedRow = null;
		this.selectedCol = null;
		this.selectedCellChangedConsumers = new ArrayList<>();

		this.init();
	}

	private void init()
	{
		int boardLength = cellLength * 9;
		component.setPreferredSize(new Dimension(boardLength, boardLength));
		component.addComponentListener(
				new SudokuComponentListener(this::componentResized));

		this.addSelectedCellChangedConsumer(selectedCellChangedConsumer -> component.repaint());
		board.addCellValueChangedConsumer(cellValueChangedEvent -> component.repaint());
		board.addCellPossibleValueChangedConsumer(cellPossibleValueChangedEvent -> component.repaint());
	}

	public JComponent getComponent()
	{
		return component;
	}

	private void componentResized()
	{
		Rectangle componentBounds = component.getBounds();
		int width = (int) componentBounds.getWidth();
		int height = (int) componentBounds.getHeight();
		int minDimension = Math.min(width, height);

		cellLength = minDimension / 9;
		xOffset = (width - (cellLength * 9)) / 2;
		yOffset = (height - (cellLength * 9)) / 2;

		valueFont = valueFont.deriveFont((float) cellLength);
		possibleValueFont = possibleValueFont
				.deriveFont((float) (cellLength / 3d));

		component.repaint();
	}

	//<editor-fold defaultstate="collapsed" desc="painting">
	private class SudokuDisplayComponentImpl extends JComponent
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics graphics)
		{
			((Graphics2D) graphics).setRenderingHints(ANTIALIAS_ON_RENDERING_HINT);

			SudokuDisplayComponent.this.paintComponent(graphics);
		}
	}

	private void paintComponent(Graphics graphics)
	{
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
					.stringWidth(sudokuCell.getValue().getDisplayValue());
			int fontHeightPx = (int) (valueFont.getSize() * 0.75d);
			int cellColumn = sudokuCell.getIndex(SectionType.COLUMN);
			int cellRow = sudokuCell.getIndex(SectionType.ROW);
			int colOffset = (int) (xOffset - (charWidth / 2d)
					+ (cellLength * (cellColumn + 0.5d)));
			int rowOffset = (int) (yOffset + (fontHeightPx / 2d)
					+ (cellLength * (cellRow + 0.5d)));

			graphics.drawString(
					sudokuCell.getValue().getDisplayValue(),
					colOffset,
					rowOffset);
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
			SudokuValue possibleValue,
			Graphics graphics,
			FontMetrics fontMetrics,
			int fontHeightPx)
	{
		int charWidth = fontMetrics.stringWidth(possibleValue.getDisplayValue());
		int possibleValueCol = (possibleValue.getValue() - 1) % 3;
		int possibleValueRow = (possibleValue.getValue() - 1) / 3;
		double colPercentage = sudokuCell.getIndex(SectionType.COLUMN)
				+ ((1 + (2 * possibleValueCol)) / 6d);
		double rowPercentage = sudokuCell.getIndex(SectionType.ROW)
				+ ((1 + (2 * possibleValueRow)) / 6d);
		int colOffset = (int) (xOffset + cellLength * colPercentage
				- (charWidth / 2d));
		int rowOffset = (int) (yOffset + cellLength * rowPercentage
				+ (fontHeightPx / 2d));

		graphics.drawString(
				possibleValue.getDisplayValue(), colOffset, rowOffset);
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
		this.setSelectedCellIndices(
				(y - yOffset) / cellLength,
				(x - xOffset) / cellLength);
	}

	public void selectCell(SudokuCell sudokuCell)
	{
		this.removeSelectedCell(); // in case the selection fails.

		this.setSelectedCellIndices(
				sudokuCell.getIndex(SectionType.ROW),
				sudokuCell.getIndex(SectionType.COLUMN));
	}

	public void removeSelectedCell()
	{
		SudokuCell previousSelectedCell = this.getSelectedCell();
		selectedRow = null;
		selectedCol = null;

		SudokuEvent<?, SudokuCell> selectedCellChangedEvent
				= new SudokuEvent<>(this, previousSelectedCell, null);
		selectedCellChangedConsumers.forEach(consumer -> consumer.accept(selectedCellChangedEvent));
	}

	public SudokuCell getSelectedCell()
	{
		return selectedRow == null && selectedCol == null
				? null
				: board.getSudokuCells(SectionType.ROW, selectedRow)
						.get(selectedCol);
	}

	public void moveSelectedCell(MoveDirection moveDirection)
	{
		if (selectedRow != null && selectedCol != null)
		{
			switch (moveDirection)
			{
				case UP:
					this.setSelectedCellIndices(selectedRow - 1, selectedCol);
					break;
				case DOWN:
					this.setSelectedCellIndices(selectedRow + 1, selectedCol);
					break;
				case LEFT:
					this.setSelectedCellIndices(selectedRow, selectedCol - 1);
					break;
				case RIGHT:
					this.setSelectedCellIndices(selectedRow, selectedCol + 1);
					break;
				default:
					throw new IllegalArgumentException(
							"Unknown moveDirection: " + moveDirection);
			}
		}
	}

	private void setSelectedCellIndices(int r, int c)
	{
		if (r < 0 || r >= 9 || c < 0 || c >= 9)
		{
			return;
		}
		SudokuCell previousSelectedCell = this.getSelectedCell();
		selectedRow = r;
		selectedCol = c;
		SudokuCell currentSelectedCell = this.getSelectedCell();
		SudokuEvent<?, SudokuCell> selectedCellChangedEvent
				= new SudokuEvent<>(this, previousSelectedCell, currentSelectedCell);
		selectedCellChangedConsumers.forEach(
				consumer -> consumer.accept(selectedCellChangedEvent));
	}

	public void addSelectedCellChangedConsumer(
			Consumer<SudokuEvent<?, SudokuCell>> selectedCellChangedConsumer)
	{
		selectedCellChangedConsumers.add(selectedCellChangedConsumer);
	}
//</editor-fold>
}
