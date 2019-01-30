package com.github.ants280.sudoku.game.solver.plugins;

import com.github.ants280.sudoku.game.SectionType;
import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import com.github.ants280.sudoku.game.solver.SudokuSolverPlugin;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OnlyPossibleValueSudokuSolverPlugin extends SudokuSolverPlugin
{
	private final BiConsumer<SudokuCell, SudokuValue> removeNearbyPossibleValuesConsumer;

	public OnlyPossibleValueSudokuSolverPlugin(
			SudokuBoard sudokuBoard,
			Consumer<String> moveDescriptionConsumer,
			BiConsumer<SudokuCell, SudokuValue> removeNearbyPossibleValuesConsumer)
	{
		super(sudokuBoard, moveDescriptionConsumer);

		this.removeNearbyPossibleValuesConsumer
				= removeNearbyPossibleValuesConsumer;
	}

	@Override
	public boolean makeMove()
	{
		Optional<SudokuCell> onePossibleValueSudoukCellOptional
				= sudokuBoard.getAllSudokuCells()
						.stream()
						.filter(sudokuCell -> sudokuCell.getValue() == null
						&& sudokuCell.getPossibleValues().size() == 1)
						.findFirst();

		if (onePossibleValueSudoukCellOptional.isPresent())
		{
			SudokuCell sudokuCell = onePossibleValueSudoukCellOptional.get();
			SudokuValue value = sudokuCell.getPossibleValues().iterator().next();

			String moveDescription = String.format(
					"Setting value of cell at [r,c]=[%d,%d] to %s "
					+ "because it is the only possible value "
					+ "in one of its sections.",
					sudokuCell.getIndex(SectionType.ROW) + 1,
					sudokuCell.getIndex(SectionType.COLUMN) + 1,
					value.getDisplayValue());
			this.logMove(moveDescription);

			sudokuCell.setValue(value);

			removeNearbyPossibleValuesConsumer.accept(sudokuCell, value);
		}

		return onePossibleValueSudoukCellOptional.isPresent();
	}
}
