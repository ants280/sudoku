package com.jpatterson.school.sudoku2.game;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SudokuBoard {

	private final int[][] board;

	private static final Set<Integer> VALUES = IntStream.rangeClosed(1, 9)
		.boxed()
		.collect(Collectors.toSet());

	public SudokuBoard(String boardString) {
		if (!boardString.matches("^\\d{81}$")) {
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
				.mapToInt(Integer::intValue)
				.toArray())
			.toArray(int[][]::new);
	}

	public SudokuBoard() {
		this(
			  "000" + "000" + "000"
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
	public String toString() {
		String boardValues = Stream.of(board)
			.flatMapToInt(IntStream::of)
			.mapToObj(Integer::toString)
			.collect(Collectors.joining());

		return "{" + boardValues + "}";
	}

	public int getValue(int r, int c) {
		validate(r, c);

		return board[r][c];
	}

	public void setValue(int r, int c, int value) {
		validate(r, c);
		if (value < 0 || value > 9) {
			throw new IllegalArgumentException("Invalid value: " + value);
		}

		board[r][c] = value;
	}

	public int getGroupNumber(int r, int c) {
		validate(r, c);

		return (r / 3) * 3 + c / 3;
	}

	public int getRowNumber(int r, int c) {
		validate(r, c);

		return r;
	}

	public int getColNumber(int r, int c) {
		validate(r, c);

		return c;
	}

	public Set<Integer> getUnusedValuesForGroup(int groupNumber) {
		validate(groupNumber);

		int startingRow = 3 * (groupNumber / 3);
		int startingCol = 3 * (groupNumber % 3);
		Set<Integer> groupValues = IntStream.range(startingRow, startingRow + 3)
			.mapToObj(r -> IntStream.range(startingCol, startingCol + 3)
				.map(c -> board[r][c])
				.boxed())
			.flatMap(Function.identity())
			.collect(Collectors.toSet());

		return getRemainingValues(groupValues);
	}

	public Set<Integer> getUnusedValuesForRow(int rowNumber) {
		validate(rowNumber);

		Set<Integer> rowValues = IntStream.of(board[rowNumber])
			.boxed()
			.collect(Collectors.toSet());

		return getRemainingValues(rowValues);
	}

	public Set<Integer> getUnusedValuesForCol(int colNumber) {
		validate(colNumber);

		Set<Integer> colValues = Stream.of(board)
			.mapToInt(row -> row[colNumber])
			.boxed()
			.collect(Collectors.toSet());

		return getRemainingValues(colValues);
	}

	public boolean isSolved() {
		return IntStream.range(0, 9)
			.allMatch(i -> getUnusedValuesForGroup(i).isEmpty()
				&& getUnusedValuesForRow(i).isEmpty()
				&& getUnusedValuesForCol(i).isEmpty());
	}

	private static Set<Integer> getRemainingValues(Set<Integer> sectionValues) {
		return VALUES.stream()
			.filter(i -> !sectionValues.contains(i))
			.collect(Collectors.toSet());
	}

	private void validate(int... coordinates) throws IllegalArgumentException {
		IntStream.of(coordinates)
			.forEach(this::validate);
	}

	private void validate(int coordinate) throws IllegalArgumentException {
		if (coordinate < 0 || coordinate >= 9) {
			throw new IllegalArgumentException(
				"Invalid coordinate: " + coordinate);
		}
	}
}
