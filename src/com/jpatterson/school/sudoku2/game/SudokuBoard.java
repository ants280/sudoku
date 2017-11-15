package com.jpatterson.school.sudoku2.game;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuBoard
{
	private final SudokuCell[][] board;

	private static final Set<Integer> VALUES = IntStream.rangeClosed(1, 9)
		.boxed()
		.collect(Collectors.toSet());

	public SudokuBoard(String boardString)
	{
		if (!boardString.matches("^\\d{81}$"))
		{
			throw new IllegalArgumentException("Illegal board: " + boardString);
		}

		this.board = IntStream.range(0, 9)
			.map(i -> i * 9)
			.mapToObj(i -> boardString.substring(i, i + 9)
				.chars()
				.mapToObj(ch -> (char) ch)
				.map(Character::valueOf)
				.map(character -> character.toString())
				.map(Integer::valueOf)
				.map(cellValue -> cellValue == 0 ? new MutableSudokuCell() : new ImmutableSudokuCell(cellValue))
				.toArray(SudokuCell[]::new))
			.toArray(SudokuCell[][]::new);
	}

	public SudokuBoard()
	{
		this("000" + "000" + "000"
			+ "000" + "000" + "000"
			+ "000" + "000" + "000"
			+ "000" + "000" + "000"
			+ "000" + "000" + "000"
			+ "000" + "000" + "000"
			+ "000" + "000" + "000"
			+ "000" + "000" + "000"
			+ "000" + "000" + "000");
	}

	@Override
	public String toString()
	{
		String boardValues = Arrays.stream(board)
			.flatMap(Arrays::stream)
			.map(sudokuCell -> sudokuCell.getValue() == null ? 0 : sudokuCell.getValue())
			.map(String::valueOf)
			.collect(Collectors.joining());

		return "{" + boardValues + "}";
	}

	public Integer getValue(int r, int c)
	{
		validate(r, c);

		return board[r][c].getValue();
	}

	public void setValue(int r, int c, Integer value)
	{
		validate(r, c);
		if (value != null && (value < 0 || value > 9))
		{
			throw new IllegalArgumentException("Invalid value: " + value);
		}

		board[r][c].setValue(value);
	}

	public int getGroupNumber(int r, int c)
	{
		validate(r, c);

		return (r / 3) * 3 + c / 3;
	}

	public int getRowNumber(int r, int c)
	{
		validate(r, c);

		return r;
	}

	public int getColNumber(int r, int c)
	{
		validate(r, c);

		return c;
	}

	public Set<Integer> getUnusedValuesForGroup(int groupNumber)
	{
		validate(groupNumber);

		int startingRow = 3 * (groupNumber / 3);
		int startingCol = 3 * (groupNumber % 3);
		Set<Integer> groupValues = IntStream.range(startingRow, startingRow + 3)
			.mapToObj(r -> IntStream.range(startingCol, startingCol + 3)
				.mapToObj(c -> board[r][c]))
			.flatMap(Function.identity())
			.filter(sudokuCell -> sudokuCell.getValue() != null) // TOOD: make SudokuCell abstract
			.map(SudokuCell::getValue)
			.collect(Collectors.toSet());

		return getRemainingValues(groupValues);
	}

	public Set<Integer> getUnusedValuesForRow(int rowNumber)
	{
		validate(rowNumber);

		Set<Integer> rowValues = Arrays.stream(board[rowNumber])
			.filter(sudokuCell -> sudokuCell.getValue() != null) // TOOD: make SudokuCell abstract
			.map(SudokuCell::getValue)
			.collect(Collectors.toSet());

		return getRemainingValues(rowValues);
	}

	public Set<Integer> getUnusedValuesForCol(int colNumber)
	{
		validate(colNumber);

		Set<Integer> colValues = Arrays.stream(board)
			.map(row -> row[colNumber])
			.filter(sudokuCell -> sudokuCell.getValue() != null) // TOOD: make SudokuCell abstract
			.map(SudokuCell::getValue)
			.collect(Collectors.toSet());

		return getRemainingValues(colValues);
	}

	public boolean isSolved()
	{
		return IntStream.range(0, 9)
			.allMatch(i -> getUnusedValuesForGroup(i).isEmpty()
				&& getUnusedValuesForRow(i).isEmpty()
				&& getUnusedValuesForCol(i).isEmpty());
	}

	private static Set<Integer> getRemainingValues(Set<Integer> sectionValues)
	{
		return VALUES.stream()
			.filter(i -> !sectionValues.contains(i))
			.collect(Collectors.toSet());
	}

	private void validate(int... coordinates) throws IllegalArgumentException
	{
		IntStream.of(coordinates)
			.forEach(this::validate);
	}

	private void validate(int coordinate) throws IllegalArgumentException
	{
		if (coordinate < 0 || coordinate >= 9)
		{
			throw new IllegalArgumentException(
				"Invalid coordinate: " + coordinate);
		}
	}
}
