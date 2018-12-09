package com.github.ants280.sudoku.game;

import static com.github.ants280.sudoku.game.SectionType.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuBoard
{
	private final List<SudokuCell> allSudokuCells;
	private final Map<SectionType, Map<Integer, List<SudokuCell>>> sectionTypeCells;
	private static final Collection<SudokuValue> ALL_SUDOKU_VALUES
			= EnumSet.allOf(SudokuValue.class);
	private static final Predicate<List<SudokuCell>> CELL_SECTION_HAS_ALL_VALUES
			= sudokuCells -> sudokuCells.stream()
					.map(SudokuCell::getValue)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet())
					.equals(ALL_SUDOKU_VALUES);

	public SudokuBoard(String boardString)
	{
		this.allSudokuCells = Collections
				.unmodifiableList(this.getAllSudokuCells(boardString));

		this.sectionTypeCells = new EnumMap<>(SectionType.class);
		sectionTypeCells.put(ROW, allSudokuCells.stream().collect(Collectors
				.groupingBy(sudokuCell -> sudokuCell.getIndex(ROW))));
		sectionTypeCells.put(COLUMN, allSudokuCells.stream().collect(Collectors
				.groupingBy(sudokuCell -> sudokuCell.getIndex(COLUMN))));
		sectionTypeCells.put(GROUP, allSudokuCells.stream().collect(Collectors
				.groupingBy(sudokuCell -> sudokuCell.getIndex(GROUP))));
	}

	public SudokuBoard()
	{
		this("{000000000000000000000000000000000000000000000000000000000000000000000000000000000}");
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

	private List<SudokuCell> getAllSudokuCells(String boardString)
	{
		if (!isValidSavedBoard(boardString))
		{
			throw new IllegalArgumentException("Illegal board: " + boardString);
		}

		SudokuCell[] allSudokuCellsArray = new SudokuCell[81];
		for (int i = 0; i < allSudokuCellsArray.length; i++)
		{
			char valueChar = boardString.charAt(i + 1);
			SudokuValue cellValue = SudokuValue.fromChar(valueChar);
			int rowIndex = i / 9;
			int columnIndex = i % 9;
			int groupIndex = (rowIndex / 3) * 3 + columnIndex / 3;
			allSudokuCellsArray[i] = createSudokuCell(
					rowIndex,
					columnIndex,
					groupIndex,
					cellValue,
					cellValue != null);
		}
		return Arrays.asList(allSudokuCellsArray);
	}

	protected SudokuCell createSudokuCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			SudokuValue cellValue,
			boolean locked)
	{
		return new SudokuCell(
				rowIndex,
				columnIndex,
				groupIndex,
				cellValue,
				locked);
	}

	public void resetFrom(SudokuBoard other)
	{
		IntStream.range(0, 81)
				.forEach(i -> allSudokuCells.get(i)
				.resetFrom(other.getAllSudokuCells().get(i)));
	}

	public List<SudokuCell> getAllSudokuCells()
	{
		return allSudokuCells;
	}

	public List<SudokuCell> getSudokuCells(
			SectionType sectionType,
			int sectionIndex)
	{
		validateIndices(sectionIndex);

		return sectionTypeCells.get(sectionType).get(sectionIndex);
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
		return boardString.matches("^\\{\\d{81}\\}$");
	}

	private static void validateIndices(int... indices)
	{
		IntStream.of(indices)
				.forEach(SudokuBoard::validateIndex);
	}

	private static void validateIndex(int index)
	{
		if (index < 0 || index >= 9)
		{
			throw new IllegalArgumentException(
					"Invalid index: " + index);
		}
	}
}
