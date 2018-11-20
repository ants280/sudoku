package com.github.ants280.sudoku.game;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SudokuBoard
{
	private final SudokuCell[][] board;

	public SudokuBoard(String boardString)
	{
		this.board = fromString(boardString);
	}

	public SudokuBoard()
	{
//		this("{000" + "000" + "000"
//			+ "000" + "000" + "000"
//			+ "000" + "000" + "000"
//			+ "000" + "000" + "000"
//			+ "000" + "000" + "000"
//			+ "000" + "000" + "000"
//			+ "000" + "000" + "000"
//			+ "000" + "000" + "000"
//			+ "000" + "000" + "000}");

//		this("{000000000000000000000000000000000000000000000000000000000000000000000000000000000}");
//		this("{768945123239617854145823976473261589582394617691758342827136495956482731314579268}");
		// May 18-24 2015
//		this("{002689300849000020060470000170890402490020071206041089000054060080000195007918200}"); // 1/5 stars
//		this("{000086200000000069060039071140060920020070040037040015290610080680000000001520000}"); // 2/5 stars
//		this("{702186000050020000408070000600007002047205980200900004000090608000050040000841309}"); // 3/5 stars
//		this("{003761005000032000060000008008290006000070000400016900700000030000520000100643200}"); // 4/5 stars
//		this("{079010004000560080000070906104080090000000000050030701701040000040098000900050430}"); // 5/5 stars March 22
//		this("{000208009007040380050060000600900700070030010009007004000080060086070100500601000}"); // 6/5 stars
//		this("{900801000060020100200070004050130200080050090007096050700010006002080030000309002}"); // 5/5 stars
//		this("{370095000600080090008300007000010050160000034040060000700002900020030008000950042}"); // 6/5 stars 20170218
//		this("{679810004410569087000070916104080090097020000050930701701040009040098170900050430}");
		// November 16-18, 2017
//		this("{605040902001090003020015600900004800000060000008100009006430080500080200807020306}"); // 4/5 stars
//		this("{000062507006000001010070008095400002600090005100005970700040050200000800401580000}"); // 5/5 stars
		// problem boards:
		this("{004063100000010002000074683907000000006080900000000504825640000700090000009350700}"); // 6/5 stars 2017-11-18
//		this("{003070600000159020900000005700000010006040900040000006400000002070362000009080700}"); // :(
	}

	@Override
	public String toString()
	{
		String boardValues = Arrays.stream(board)
				.flatMap(Arrays::stream)
				.map(this::getCellValue)
				.map(String::valueOf)
				.collect(Collectors.joining());

		return "{" + boardValues + "}";
	}

	private static SudokuCell[][] fromString(String boardString)
	{
		if (!boardString.matches("^\\{\\d{81}\\}$"))
		{
			throw new IllegalArgumentException("Illegal board: " + boardString);
		}

		return IntStream.range(0, 9)
				.map(i -> i * 9)
				.mapToObj(i -> boardString.substring(i + 1, i + 10)
				.chars()
				.mapToObj(ch -> (char) ch)
				.map(Character::valueOf)
				.map(character -> character.toString())
				.map(Integer::valueOf)
				.map(cellValue -> cellValue == 0
				? new MutableSudokuCell()
				: new ImmutableSudokuCell(cellValue))
				.toArray(SudokuCell[]::new))
				.toArray(SudokuCell[][]::new);
	}

	private Integer getCellValue(SudokuCell sudokuCell)
	{
		if (sudokuCell.isEmpty() || sudokuCell.getValue() == null)
		{
			return 0;
		}
		return sudokuCell.getValue();
	}

	public SudokuCell getSudokuCell(int r, int c)
	{
		validateCoords(r, c);

		return board[r][c];
	}

	/**
	 * @param r The row of the SudokuCell
	 * @param c The column of the SudokuCell
	 * @param value The value to add to the the possible values of the
	 * SudokuCell
	 * @return Whether or not the value was successfully removed from the
	 * possible values.
	 */
	public boolean addPossibleValue(int r, int c, Integer value)
	{
		return changePossibleValue(
				r, c, value,
				SudokuCell::addPossibleValue);
	}

	/**
	 * @param r The row of the SudokuCell
	 * @param c The column of the SudokuCell
	 * @param value The value to remove the the possible values of the
	 * SudokuCell
	 * @return Whether or not the value was successfully removed from the
	 * possible values.
	 */
	public boolean removePossibleValue(int r, int c, Integer value)
	{
		return changePossibleValue(
				r, c, value,
				SudokuCell::removePossibleValue);
	}

	private boolean changePossibleValue(
			int r, int c, Integer value,
			BiFunction<SudokuCell, Integer, Boolean> changePossibleValueFunction)
	{
		return changePossibleValueFunction.apply(getSudokuCell(r, c), value);
	}

	public int getGroupNumber(int r, int c)
	{
		validateCoords(r, c);

		return ((r / 3) * 3) + (c / 3);
	}

	public int getRowNumber(int r, int c)  // TODO: DELETEME
	{
		validateCoords(r, c);

		return r;
	}

	public int getColNumber(int r, int c) // TODO: DELETEME
	{
		validateCoords(r, c);

		return c;
	}

	public Collection<SudokuCell> getSudokuCellsForGroup(int groupNumber)
	{
		return getOtherSudokuCellsForGroup(groupNumber, null, null);
	}

	public Collection<SudokuCell> getOtherSudokuCellsForGroup(
			int groupNumber, Integer rowNumber, Integer colNumber)
	{
		validateCoord(groupNumber);
		if (rowNumber != null || colNumber != null)
		{
			validateCoords(rowNumber, colNumber);
		}

		int startingRow = 3 * (groupNumber / 3);
		int startingCol = 3 * (groupNumber % 3);

		return IntStream.range(startingRow, startingRow + 3)
				.mapToObj(r -> IntStream.range(startingCol, startingCol + 3)
				.filter(c -> rowNumber == null || colNumber == null
				|| r != rowNumber || c != colNumber)
				.mapToObj(c -> board[r][c]))
				.flatMap(Function.identity())
				.collect(Collectors.toList());
	}

	public Set<Integer> getValuesForGroup(int groupNumber)
	{
		return getSectionValues(getSudokuCellsForGroup(groupNumber));
	}

	public Set<Integer> getUnusedValuesForGroup(int groupNumber)
	{
		return getRemainingValues(getValuesForGroup(groupNumber));
	}

	public Collection<SudokuCell> getSudokuCellsForRow(int rowNumber)
	{
		return getOtherSudokuCellsForRow(rowNumber, null);
	}

	public Collection<SudokuCell> getOtherSudokuCellsForRow(
			int rowNumber, Integer colNumber)
	{
		validateCoord(rowNumber);
		if (colNumber != null)
		{
			validateCoord(colNumber);
		}

		return IntStream.range(0, 9)
				.filter(c -> colNumber == null || c != colNumber)
				.mapToObj(c -> board[rowNumber][c])
				.collect(Collectors.toList());
	}

	public Set<Integer> getValuesForRow(int rowNumber)
	{
		return getSectionValues(getSudokuCellsForRow(rowNumber));
	}

	public Set<Integer> getUnusedValuesForRow(int rowNumber)
	{
		return getRemainingValues(getValuesForRow(rowNumber));
	}

	public Collection<SudokuCell> getSudokuCellsForCol(int colNumber)
	{
		return getOtherSudokuCellsForCol(colNumber, null);
	}

	public Collection<SudokuCell> getOtherSudokuCellsForCol(
			int colNumber, Integer rowNumber)
	{
		validateCoord(colNumber);
		if (rowNumber != null)
		{
			validateCoord(rowNumber);
		}

		return IntStream.range(0, 9)
				.filter(r -> rowNumber == null || r != rowNumber)
				.mapToObj(r -> board[r][colNumber])
				.collect(Collectors.toList());
	}

	public Set<Integer> getValuesForCol(int colNumber)
	{
		return getSectionValues(getSudokuCellsForCol(colNumber));
	}

	public Set<Integer> getUnusedValuesForCol(int colNumber)
	{
		return getRemainingValues(getValuesForCol(colNumber));
	}

	public boolean isSolved()
	{
		return IntStream.range(0, 9)
				.allMatch(i -> getUnusedValuesForGroup(i).isEmpty()
				&& getUnusedValuesForRow(i).isEmpty()
				&& getUnusedValuesForCol(i).isEmpty());
	}

	private static Set<Integer> getSectionValues(
			Collection<SudokuCell> sectionSudokuCells)
	{
		return sectionSudokuCells
				.stream()
				.filter(sudokuCell -> !sudokuCell.isEmpty())
				.map(SudokuCell::getValue)
				.collect(Collectors.toSet());
	}

	private static Set<Integer> getRemainingValues(Set<Integer> sectionValues)
	{
		return SudokuCell.LEGAL_CELL_VALUES.stream()
				.filter(i -> !sectionValues.contains(i))
				.collect(Collectors.toSet());
	}

	private void validateCoords(int... coordinates)
			throws IllegalArgumentException
	{
		IntStream.of(coordinates)
				.forEach(this::validateCoord);
	}

	private void validateCoord(int coordinate)
			throws IllegalArgumentException
	{
		if (coordinate < 0 || coordinate >= 9)
		{
			throw new IllegalArgumentException(
					"Invalid coordinate: " + coordinate);
		}
	}

	public Set<SudokuCell> getSudokuCellsInGroupSection(
			int groupNumber,
			SectionType sectionType,
			 int sectionNumber)
	{
		validateSection(sectionType, SectionType.ROW, SectionType.COL);

		int r = (sectionType == SectionType.ROW)
				? sectionNumber
				: (groupNumber / 3) * 3;
		int c = (sectionType == SectionType.COL)
				? sectionNumber
				: (groupNumber % 3) * 3;

		Stream<SudokuCell> sudokuCellsInGroupSectionStream
				= (sectionType == SectionType.ROW)
						? IntStream.range(c, c + 3).mapToObj(c2 -> board[r][c2])
						: IntStream.range(r, r + 3).mapToObj(r2 -> board[r2][c]);

		return sudokuCellsInGroupSectionStream.collect(Collectors.toSet());
	}

	private void validateSection(
			SectionType actualSectionType,
			SectionType... expectedSectionTypes)
			throws IllegalArgumentException
	{
		for (SectionType expectedSectionType : expectedSectionTypes)
		{
			if (expectedSectionType == actualSectionType)
			{
				return;
			}
		}
		throw new IllegalArgumentException(
				"Invalid SectionType: " + actualSectionType);
	}
}
