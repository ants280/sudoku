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
	private final List<Consumer<SudokuEvent<CommandHistory<T>, Boolean>>> undoEmptyChangedConsumers;
	private final List<Consumer<SudokuEvent<CommandHistory<T>, Boolean>>> redoEmptyChangedConsumers;
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
			Consumer<SudokuEvent<CommandHistory<T>, Boolean>> undoEmptyChangedConsumer)
	{
		undoEmptyChangedConsumers.add(undoEmptyChangedConsumer);
	}

	public void addRedoEmptyChangedConsumer(
			Consumer<SudokuEvent<CommandHistory<T>, Boolean>> redoEmptyChangedConsumer)
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

			SudokuEvent<CommandHistory<T>, Boolean> undoEmptyChangedConsumer
					= new SudokuEvent<>(this, previousUndoHistoryEmpty, false);
			SudokuEvent<CommandHistory<T>, Boolean> redoEmptyChangedConsumer
					= new SudokuEvent<>(this, previousRedoHistoryEmpty, true);
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

			enabled = false;
			command.undo();
			enabled = true;

			redoHistory.push(command);

			SudokuEvent<CommandHistory<T>, Boolean> undoEmptyChangedConsumer
					= new SudokuEvent<>(this, previousUndoHistoryEmpty, undoHistory.isEmpty());
			SudokuEvent<CommandHistory<T>, Boolean> redoEmptyChangedConsumer
					= new SudokuEvent<>(this, previousRedoHistoryEmpty, false);
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

			enabled = false;
			command.redo();
			enabled = true;

			undoHistory.push(command);

			SudokuEvent<CommandHistory<T>, Boolean> undoEmptyChangedConsumer
					= new SudokuEvent<>(this, previousUndoHistoryEmpty, false);
			SudokuEvent<CommandHistory<T>, Boolean> redoEmptyChangedConsumer
					= new SudokuEvent<>(this, previousRedoHistoryEmpty, redoHistory.isEmpty());
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

			SudokuEvent<CommandHistory<T>, Boolean> undoEmptyChangedConsumer
					= new SudokuEvent<>(this, previousUndoHistoryEmpty, true);
			SudokuEvent<CommandHistory<T>, Boolean> redoEmptyChangedConsumer
					= new SudokuEvent<>(this, previousRedoHistoryEmpty, true);
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
