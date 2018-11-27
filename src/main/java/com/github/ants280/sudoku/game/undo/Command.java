package com.github.ants280.sudoku.game.undo;

public interface Command
{
	void undo();

	void redo();
}
