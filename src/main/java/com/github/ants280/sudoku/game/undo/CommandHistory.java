package com.github.ants280.sudoku.game.undo;

import com.github.ants280.sudoku.game.SudokuEvent;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class CommandHistory<T extends Command>
{
	private final Deque<T> undoHistory;
	private final Deque<T> redoHistory;
	private final List<Consumer<SudokuEvent<Boolean>>> undoEmptyChangedConsumers;
	private final List<Consumer<SudokuEvent<Boolean>>> redoEmptyChangedConsumers;
	private boolean enabled;

	public CommandHistory()
	{
		this.undoHistory = new LinkedList<>();
		this.redoHistory = new LinkedList<>();
		this.undoEmptyChangedConsumers = new ArrayList<>();
		this.redoEmptyChangedConsumers = new ArrayList<>();
		this.enabled = true;
	}

	public void addUndoEmptyChangedConsumer(
			Consumer<SudokuEvent<Boolean>> undoEmptyChangedConsumer)
	{
		undoEmptyChangedConsumers.add(undoEmptyChangedConsumer);
	}

	public void addRedoEmptyChangedConsumer(
			Consumer<SudokuEvent<Boolean>> redoEmptyChangedConsumer)
	{
		redoEmptyChangedConsumers.add(redoEmptyChangedConsumer);
	}

	public void addCommand(T command)
	{
		if (enabled)
		{
			boolean previousUndoHistoryEmpty = undoHistory.isEmpty();
			boolean previousRedoHistoryEmpty = redoHistory.isEmpty();

			undoHistory.push(command);

			redoHistory.clear();

			SudokuEvent<Boolean> undoEmptyChangedConsumer
					= new SudokuEvent<>(previousUndoHistoryEmpty, false);
			SudokuEvent<Boolean> redoEmptyChangedConsumer
					= new SudokuEvent<>(previousRedoHistoryEmpty, true);
			undoEmptyChangedConsumers
					.forEach(consumer -> consumer.accept(undoEmptyChangedConsumer));
			redoEmptyChangedConsumers
					.forEach(consumer -> consumer.accept(redoEmptyChangedConsumer));
		}
	}

	public T undo()
	{
		if (enabled && !undoHistory.isEmpty())
		{
			boolean previousUndoHistoryEmpty = undoHistory.isEmpty();
			boolean previousRedoHistoryEmpty = redoHistory.isEmpty();

			T command = undoHistory.pop();

			command.undo();

			redoHistory.push(command);

			SudokuEvent<Boolean> undoEmptyChangedConsumer
					= new SudokuEvent<>(previousUndoHistoryEmpty, undoHistory.isEmpty());
			SudokuEvent<Boolean> redoEmptyChangedConsumer
					= new SudokuEvent<>(previousRedoHistoryEmpty, false);
			undoEmptyChangedConsumers
					.forEach(consumer -> consumer.accept(undoEmptyChangedConsumer));
			redoEmptyChangedConsumers
					.forEach(consumer -> consumer.accept(redoEmptyChangedConsumer));

			return command;
		}

		return null;
	}

	public T redo()
	{
		if (enabled && !redoHistory.isEmpty())
		{
			boolean previousUndoHistoryEmpty = undoHistory.isEmpty();
			boolean previousRedoHistoryEmpty = redoHistory.isEmpty();

			T command = redoHistory.pop();

			command.redo();

			undoHistory.push(command);

			SudokuEvent<Boolean> undoEmptyChangedConsumer
					= new SudokuEvent<>(previousUndoHistoryEmpty, false);
			SudokuEvent<Boolean> redoEmptyChangedConsumer
					= new SudokuEvent<>(previousRedoHistoryEmpty, redoHistory.isEmpty());
			undoEmptyChangedConsumers
					.forEach(consumer -> consumer.accept(undoEmptyChangedConsumer));
			redoEmptyChangedConsumers
					.forEach(consumer -> consumer.accept(redoEmptyChangedConsumer));

			return command;
		}

		return null;
	}

	public void reset()
	{
		if (enabled)
		{
			boolean previousUndoHistoryEmpty = undoHistory.isEmpty();
			boolean previousRedoHistoryEmpty = redoHistory.isEmpty();

			undoHistory.clear();
			redoHistory.clear();

			SudokuEvent<Boolean> undoEmptyChangedConsumer
					= new SudokuEvent<>(previousUndoHistoryEmpty, true);
			SudokuEvent<Boolean> redoEmptyChangedConsumer
					= new SudokuEvent<>(previousRedoHistoryEmpty, true);
			undoEmptyChangedConsumers
					.forEach(consumer -> consumer.accept(undoEmptyChangedConsumer));
			redoEmptyChangedConsumers
					.forEach(consumer -> consumer.accept(redoEmptyChangedConsumer));
		}
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public int getUndoCount()
	{
		return undoHistory.size();
	}
}
