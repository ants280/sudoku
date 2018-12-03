package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuCell;
import com.github.ants280.sudoku.game.SudokuValue;
import java.util.function.BiConsumer;

public enum SudokuCellChangeType
{
	TOGGLE_POSSIBLE_VALUE(SudokuCell::togglePossibleValue),
	SET_VALUE(SudokuCell::setValue);

	private final BiConsumer<SudokuCell, SudokuValue> undoRedoCommand;

	SudokuCellChangeType(BiConsumer<SudokuCell, SudokuValue> undoRedoCommand)
	{
		this.undoRedoCommand = undoRedoCommand;
	}

	public void applyChange(SudokuCell sudokuCell, SudokuValue value)
	{
		undoRedoCommand.accept(sudokuCell, value);
	}
}
