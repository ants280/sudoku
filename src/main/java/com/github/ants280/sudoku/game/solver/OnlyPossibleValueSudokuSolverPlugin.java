package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import com.github.ants280.sudoku.game.SudokuCell;
import java.util.Optional;
import java.util.function.BiConsumer;

public class OnlyPossibleValueSudokuSolverPlugin extends SudokuSolverPlugin
{
	private final BiConsumer<SudokuCell, Integer> removeNearbyPossibleValuesConsumer;

	public OnlyPossibleValueSudokuSolverPlugin(
			SudokuBoard sudokuBoard,
			BiConsumer<SudokuCell, Integer> removeNearbyPossibleValuesConsumer)
	{
		super(sudokuBoard);

		this.removeNearbyPossibleValuesConsumer
				= removeNearbyPossibleValuesConsumer;
	}

	@Override
	public boolean makeMove()
	{
		Optional<SudokuCell> onePossibleValueSudoukCellOptional
				= sudokuBoard.getAllSudokuCells()
						.stream()
						.filter(sudokuCell -> sudokuCell.getPossibleValues().size() == 1)
						.findFirst();

		if (onePossibleValueSudoukCellOptional.isPresent())
		{
			SudokuCell sudokuCell = onePossibleValueSudoukCellOptional.get();
			int value = sudokuCell.getPossibleValues().iterator().next();

			sudokuCell.setValue(value);

			removeNearbyPossibleValuesConsumer.accept(sudokuCell, value);
		}

		return onePossibleValueSudoukCellOptional.isPresent();
	}
}
