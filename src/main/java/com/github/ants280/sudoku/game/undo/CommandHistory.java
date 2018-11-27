package com.github.ants280.sudoku.game.undo;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.BiConsumer;

public class CommandHistory<T extends Command> implements Command
{
	private final Deque<T> undoHistory;
	private final Deque<T> redoHistory;
	private final BiConsumer<Boolean, Boolean> undoRedoEmptyConsumer;

	public CommandHistory(BiConsumer<Boolean, Boolean> undoRedoEmptyConsumer)
	{
		this.undoHistory = new LinkedList<>();
		this.redoHistory = new LinkedList<>();
		this.undoRedoEmptyConsumer = undoRedoEmptyConsumer;
	}

	public void addCommand(T command)
	{
		undoHistory.add(command);

		redoHistory.clear();

		undoRedoEmptyConsumer.accept(false, true);
	}

	@Override
	public void undo()
	{
		if (!undoHistory.isEmpty())
		{
			T command = undoHistory.pop();

			command.undo();

			redoHistory.push(command);

			undoRedoEmptyConsumer.accept(undoHistory.isEmpty(), false);
		}
	}

	@Override
	public void redo()
	{
		if (!redoHistory.isEmpty())
		{
			T command = redoHistory.pop();

			command.redo();

			undoHistory.push(command);

			undoRedoEmptyConsumer.accept(false, redoHistory.isEmpty());
		}
	}

	public void reset()
	{
		undoHistory.clear();
		redoHistory.clear();

		undoRedoEmptyConsumer.accept(true, true);
	}
}
