package com.github.ants280.sudoku.game;

import static com.github.ants280.sudoku.game.SectionType.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuBoard
{
	private final List<SudokuCell> allSudokuCells;
	private final List<SudokuCell> allSudokuCellsView;
	private final Map<SectionType, Map<Integer, List<SudokuCell>>> sectionTypeCells;
	private static final Collection<SudokuValue> ALL_SUDOKU_VALUES
			= EnumSet.allOf(SudokuValue.class);
	private static final Predicate<List<SudokuCell>> CELL_SECTION_HAS_ALL_VALUES
			= sudokuCells -> sudokuCells.stream()
					.map(SudokuCell::getValue)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet())
					.equals(ALL_SUDOKU_VALUES);
	private final List<Consumer<SudokuEvent<SudokuBoard, Boolean>>> solvedChangedConsumers;
	private boolean previousSolved;
	private boolean listenersEnabled;

	public SudokuBoard(String boardString)
	{
		this.allSudokuCells = getAllSudokuCells(boardString);
		this.allSudokuCellsView = Collections.unmodifiableList(allSudokuCells);
		this.sectionTypeCells = new EnumMap<>(SectionType.class);
		this.solvedChangedConsumers = new ArrayList<>();
		this.previousSolved = false;
		this.listenersEnabled = true;

		this.init();
	}

	public SudokuBoard()
	{
		this("{000000000000000000000000000000000000000000000000000000000000000000000000000000000}");
	}

	public SudokuBoard(SudokuBoard other)
	{
		this(other.toString());
	}

	private void init()
	{
		sectionTypeCells.put(ROW, allSudokuCells.stream().collect(Collectors
				.groupingBy(sudokuCell -> sudokuCell.getIndex(ROW))));
		sectionTypeCells.put(COLUMN, allSudokuCells.stream().collect(Collectors
				.groupingBy(sudokuCell -> sudokuCell.getIndex(COLUMN))));
		sectionTypeCells.put(GROUP, allSudokuCells.stream().collect(Collectors
				.groupingBy(sudokuCell -> sudokuCell.getIndex(GROUP))));

		this.addCellValueChangedConsumer(
				cellValueChangedEvent -> this.handleCellValueChanged());
	}

	@Override
	public String toString()
	{
		return String.format("{%s}",
				allSudokuCells.stream()
						.map(SudokuCell::getValue)
						.map(v -> v == null ? "0" : v.getDisplayValue())
						.collect(Collectors.joining()));
	}

	private static List<SudokuCell> getAllSudokuCells(String boardString)
	{
		if (!isValidSavedBoard(boardString))
		{
			throw new IllegalArgumentException(
					"Illegal board: " + boardString);
		}

		SudokuCell[] allSudokuCellsArray = new SudokuCell[81];
		for (int i = 0; i < allSudokuCellsArray.length; i++)
		{
			char valueChar = boardString.charAt(i + 1);
			SudokuValue cellValue = SudokuValue.fromChar(valueChar);
			int rowIndex = i / 9;
			int columnIndex = i % 9;
			int groupIndex = (rowIndex / 3) * 3 + columnIndex / 3;
			allSudokuCellsArray[i] = new SudokuCell(
					rowIndex,
					columnIndex,
					groupIndex,
					cellValue,
					cellValue != null);
		}
		return Arrays.asList(allSudokuCellsArray);
	}

	public void resetFrom(SudokuBoard other)
	{
		IntStream.range(0, 81)
				.forEach(i -> allSudokuCells.get(i)
				.resetFrom(other.getAllSudokuCells().get(i)));

		if (listenersEnabled)
		{
			SudokuEvent<SudokuBoard, Boolean> solvedChangedEvent
					= new SudokuEvent<>(this, previousSolved, false);
			solvedChangedConsumers
					.forEach(consumer -> consumer.accept(solvedChangedEvent));
		}

		previousSolved = false;
	}

	// Returning unmodifiable view of list
	@SuppressWarnings("squid:S4275")
	public List<SudokuCell> getAllSudokuCells()
	{
		return allSudokuCellsView;
	}

	public List<SudokuCell> getSudokuCells(
			SectionType sectionType,
			int sectionIndex)
	{
		if (sectionIndex < 0 || sectionIndex >= 9)
		{
			throw new IllegalArgumentException(
					"Invalid index: " + sectionIndex);
		}

		return sectionTypeCells.get(sectionType).get(sectionIndex);
	}

	private void handleCellValueChanged()
	{
		boolean currentSolved = this.isSolved();

		if (listenersEnabled && currentSolved != previousSolved)
		{
			SudokuEvent<SudokuBoard, Boolean> solvedChangedEvent
					= new SudokuEvent<>(this, previousSolved, currentSolved);
			solvedChangedConsumers
					.forEach(consumer -> consumer.accept(solvedChangedEvent));
		}

		previousSolved = currentSolved;
	}

	public void addSolvedChangedConsumer(
			Consumer<SudokuEvent<SudokuBoard, Boolean>> boardSolvedChangedConsumer)
	{
		solvedChangedConsumers.add(boardSolvedChangedConsumer);
	}

	public boolean removeSolvedChangedConsumer(
			Consumer<SudokuEvent<SudokuBoard, Boolean>> boardSolvedChangedConsumer)
	{
		return solvedChangedConsumers.remove(boardSolvedChangedConsumer);
	}

	public void addCellValueChangedConsumer(
			Consumer<SudokuEvent<SudokuCell, SudokuValue>> cellValueChangedConsumer)
	{
		allSudokuCells.forEach(sudokuCell -> sudokuCell
				.addCellValueChangedConsumer(cellValueChangedConsumer));
	}

	public void addCellPossibleValueChangedConsumer(
			Consumer<SudokuEvent<SudokuCell, SudokuValue>> cellPossibleValueChangedConsumer)
	{
		allSudokuCells.forEach(sudokuCell -> sudokuCell
				.addCellPossibleValueChangedConsumer(cellPossibleValueChangedConsumer));
	}

	public boolean isSolved()
	{
		return sectionTypeCells.values()
				.stream()
				.map(Map::values)
				.flatMap(Collection::stream)
				.allMatch(CELL_SECTION_HAS_ALL_VALUES);
	}

	public static boolean isValidSavedBoard(String boardString)
	{
		return boardString != null && boardString.matches("^\\{\\d{81}\\}$");
	}

	public void setListenersEnabled(boolean enabled)
	{
		listenersEnabled = enabled;

		allSudokuCells.forEach(cell -> cell.setListenersEnabled(enabled));

		if (enabled)
		{
			boolean currentSolved = this.isSolved();

			SudokuEvent<SudokuBoard, Boolean> solvedChangedEvent
					= new SudokuEvent<>(this, previousSolved, currentSolved);
			solvedChangedConsumers
					.forEach(consumer -> consumer.accept(solvedChangedEvent));

			previousSolved = currentSolved;
		}
	}
}
