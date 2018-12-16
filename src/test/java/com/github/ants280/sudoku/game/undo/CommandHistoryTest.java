package com.github.ants280.sudoku.game.undo;

import java.util.function.BiConsumer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CommandHistoryTest
{
	private BiConsumer<Boolean, Boolean> mockUndoRedoEmptyConsumer;
	private CommandHistory<Command> commandHistory;

	@Before
	public void setUp()
	{
		mockUndoRedoEmptyConsumer = Mockito.mock(BiConsumer.class);
		commandHistory = new CommandHistory<>(mockUndoRedoEmptyConsumer);
	}

	@Test
	public void testAddCommand()
	{
		Command mockCommand1 = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand1);
		Command mockCommand2 = Mockito.mock(Command.class);

		commandHistory.undo();
		commandHistory.addCommand(mockCommand2);
		commandHistory.redo(); // expected noop

		Mockito.verify(
				mockCommand1,
				Mockito.times(0))
				.redo();
		Mockito.verify(
				mockUndoRedoEmptyConsumer,
				Mockito.times(2))
				.accept(false, true);
	}

	@Test
	public void testAddCommand_enabled_false()
	{
		Command mockCommand1 = Mockito.mock(Command.class);
		commandHistory.setEnabled(false);
		commandHistory.addCommand(mockCommand1);

		int expectedUndoCount = commandHistory.getUndoCount();
		int actualUndoCount = 0;

		Assert.assertEquals(expectedUndoCount, actualUndoCount);
	}

	@Test
	public void testUndo()
	{
		Command mockCommand1 = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand1);
		Command mockCommand2 = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand2);

		commandHistory.undo();

		Mockito.verify(
				mockCommand2,
				Mockito.times(1))
				.undo();
		Mockito.verify(
				mockCommand1,
				Mockito.times(0))
				.undo();
	}

	@Test
	public void testRedo()
	{
		Command mockCommand1 = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand1);
		Command mockCommand2 = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand2);

		commandHistory.undo();
		commandHistory.redo();

		Mockito.verify(
				mockCommand2,
				Mockito.times(1))
				.redo();
	}

	@Test
	public void testReset()
	{
		Command mockCommand = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand);
		commandHistory.reset();
		commandHistory.undo();

		Mockito.verify(
				mockCommand,
				Mockito.times(0))
				.undo();
	}

	@Test
	public void testReset_enabled_false()
	{
		Command mockCommand = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand);
		commandHistory.setEnabled(false);
		commandHistory.reset();
		commandHistory.undo();

		Mockito.verify(
				mockCommand,
				Mockito.times(0))
				.undo();
	}

	@Test
	public void testIsEnabled_defaultTrue()
	{
		Assert.assertTrue(commandHistory.isEnabled());
	}

	@Test
	public void testSetEnabled_false()
	{
		commandHistory.setEnabled(false);

		Assert.assertFalse(commandHistory.isEnabled());
	}

	@Test
	public void testSetEnabled_true()
	{
		commandHistory.setEnabled(false);
		commandHistory.setEnabled(true);

		Assert.assertTrue(commandHistory.isEnabled());
	}

	@Test
	public void testGetUndoCount()
	{
		for (int i = 0; i < 10; i++)
		{
			Command mockCommand = Mockito.mock(Command.class);
			commandHistory.addCommand(mockCommand);
		}
		commandHistory.redo(); // noop = 10
		commandHistory.undo(); // 9
		commandHistory.undo(); // 8
		commandHistory.undo(); // 7
		commandHistory.redo(); // 8
		commandHistory.undo(); // 7
		commandHistory.undo(); // 6
		commandHistory.redo(); // 7

		int actualUndoCount = commandHistory.getUndoCount();
		int expectedUndoCount = 7;

		Assert.assertEquals(expectedUndoCount, actualUndoCount);
	}

	@Test
	public void testHashCode_notEnabled()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>(null);
		CommandHistory<Command> commandHistory2 = new CommandHistory<>(null);
		commandHistory1.setEnabled(true);
		commandHistory2.setEnabled(false);

		int hashCode1 = commandHistory1.hashCode();
		int hashCode2 = commandHistory2.hashCode();

		Assert.assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void testEquals_same()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>(null);
		CommandHistory<Command> commandHistory2 = commandHistory1;

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_null()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>(null);
		CommandHistory<Command> commandHistory2 = null;

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_wrongObject()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>(null);
		Object commandHistory2 = 1L;

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_notEnabled()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>(null);
		CommandHistory<Command> commandHistory2 = new CommandHistory<>(null);
		commandHistory1.setEnabled(false);

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_diffeentUndo()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>(mockUndoRedoEmptyConsumer);
		CommandHistory<Command> commandHistory2 = new CommandHistory<>(mockUndoRedoEmptyConsumer);
		Command mockCommand = Mockito.mock(Command.class);
		commandHistory1.addCommand(mockCommand);

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_diffeentRedo()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>(mockUndoRedoEmptyConsumer);
		CommandHistory<Command> commandHistory2 = new CommandHistory<>(mockUndoRedoEmptyConsumer);
		Command mockCommand = Mockito.mock(Command.class);
		commandHistory1.addCommand(mockCommand);
		commandHistory1.addCommand(mockCommand);
		commandHistory2.addCommand(mockCommand);
		commandHistory1.undo();

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}
}
