package com.github.ants280.sudoku.game;

import static com.github.ants280.sudoku.game.SectionType.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SudokuBoard
{
	private final List<SudokuCell> allSudokuCells;
	private final Map<SectionType, SudokuCell[][]> sectionTypeCells;

	public SudokuBoard(String boardString)
	{
		this.allSudokuCells = getAllSudokuCells(boardString);
		this.sectionTypeCells = new EnumMap<>(SectionType.class);

		SudokuCell[][] boardRows = getBoardRows(allSudokuCells);
		sectionTypeCells.put(ROW, boardRows);
		sectionTypeCells.put(COL, getBoardColsFromBoardRows(boardRows));
		sectionTypeCells.put(GROUP, getBoardGroupsFromBoardRows(boardRows));
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
		String boardValues = allSudokuCells.stream()
				.map(SudokuCell::getValue)
				.map(v -> v == null ? 0 : v)
				.map(String::valueOf)
				.collect(Collectors.joining());

		return "{" + boardValues + "}";
	}

	//<editor-fold defaultstate="collapsed" desc="static constructor helpers">
	private static List<SudokuCell> getAllSudokuCells(String boardString)
	{
		if (!isValidSavedBoard(boardString))
		{
			throw new IllegalArgumentException("Illegal board: " + boardString);
		}

		return boardString.substring(1, boardString.length() - 1)
				.chars()
				.mapToObj(ch -> (char) ch)
				.map(Character::valueOf)
				.map(character -> character.toString())
				.map(Integer::valueOf)
				.map(cellValue -> cellValue == 0
				? new MutableSudokuCell()
				: new ImmutableSudokuCell(cellValue))
				.collect(Collectors.toList());
	}

	private static SudokuCell[][] getBoardRows(List<SudokuCell> sudokuCells)
	{
		return IntStream.range(0, 9)
				.map(i -> i * 9)
				.mapToObj(r -> sudokuCells.subList(r, r + 9))
				.map(rowList -> rowList.stream().toArray(SudokuCell[]::new))
				.toArray(SudokuCell[][]::new);
	}

	private static SudokuCell[][] getBoardColsFromBoardRows(
			SudokuCell[][] boardRows)
	{
		return IntStream.range(0, 9)
				.mapToObj(c -> IntStream.range(0, 9)
				.mapToObj(r -> boardRows[r][c])
				.toArray(SudokuCell[]::new))
				.toArray(SudokuCell[][]::new);
	}

	private static SudokuCell[][] getBoardGroupsFromBoardRows(
			SudokuCell[][] boardRows)
	{
		Function<Integer, Integer> _r = g -> (3 * (g / 3)); // starting row
		Function<Integer, Integer> _c = g -> (3 * (g % 3)); // starting col
		return IntStream.range(0, 9)
				.mapToObj(g -> IntStream.range(_r.apply(g), _r.apply(g) + 3)
				.mapToObj(r -> IntStream.range(_c.apply(g), _c.apply(g) + 3)
				.mapToObj(c -> boardRows[r][c]))
				.flatMap(Function.identity())
				.toArray(SudokuCell[]::new))
				.toArray(SudokuCell[][]::new);
	}
	//</editor-fold>

	public void resetFrom(SudokuBoard other)
	{
		IntStream.range(0, 81)
				.forEach(i -> allSudokuCells.get(i)
				.setValue(other.getAllSudokuCells().get(i).getValue()));
	}

	/**
	 * Used by {@link com.github.ants280.sudoku.game.SudokuBoard#resetFrom(com.github.ants280.sudoku.game.SudokuBoard)
	 * }
	 *
	 * @return The SudokuCells on the board, ordered by row, then column.
	 */
	private List<SudokuCell> getAllSudokuCells()
	{
		return Collections.unmodifiableList(allSudokuCells);
	}

	public SudokuCell getSudokuCell(SectionType sectionType, int r, int c)
	{
		validateCoords(r, c);

		return sectionTypeCells.get(sectionType)[r][c];
	}

	public SudokuCell[] getSudokuCells(
			SectionType sectionType,
			int sectionNumber)
	{
		validateCoords(sectionNumber);

		return Arrays.copyOf(
				sectionTypeCells.get(sectionType)[sectionNumber],
				9);
	}

	public boolean isSolved()
	{
		Predicate<SudokuCell[]> hasAllValues
				= sudokuCells -> Arrays.stream(sudokuCells)
						.map(SudokuCell::getValue)
						.filter(v -> v != null)
						.mapToInt(Integer::intValue)
						.map(i -> 1 << i)
						.sum() == ((1 << 10) - 2);

		return sectionTypeCells.values()
				.stream()
				.flatMap(Stream::of)
				.allMatch(hasAllValues);
	}

	public static int getGroupNumber(int r, int c)
	{
		validateCoords(r, c);

		return ((r / 3) * 3) + (c / 3);
	}

	public static boolean isValidSavedBoard(String boardString)
	{
		return boardString.matches("^\\{\\d{81}\\}$");
	}

	private static void validateCoords(int... coordinates)
			throws IllegalArgumentException
	{
		IntStream.of(coordinates)
				.forEach(SudokuBoard::validateCoord);
	}

	private static void validateCoord(int coordinate)
			throws IllegalArgumentException
	{
		if (coordinate < 0 || coordinate >= 9)
		{
			throw new IllegalArgumentException(
					"Invalid coordinate: " + coordinate);
		}
	}
}
