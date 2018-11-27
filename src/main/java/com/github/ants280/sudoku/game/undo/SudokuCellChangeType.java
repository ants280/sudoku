package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import java.util.function.BiConsumer;

public enum SudokuCellChangeType
{
	TOGGLE_POSSIBLE_VALUE(SudokuCell::togglePossibleValue),
	SET_VALUE(SudokuCell::setValue);

	private final BiConsumer<SudokuCell, Integer> undoRedoCommand;

	SudokuCellChangeType(BiConsumer<SudokuCell, Integer> undoRedoCommand)
	{
		this.undoRedoCommand = undoRedoCommand;
	}

	public void applyChange(SudokuCell sudokuCell, Integer value)
	{
		undoRedoCommand.accept(sudokuCell, value);
	}
}
