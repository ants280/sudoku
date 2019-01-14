package com.github.ants280.sudoku.game.undo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CommandHistoryTest
{
	private CommandHistory<Command> commandHistory;

	@Before
	public void setUp()
	{
		commandHistory = new CommandHistory<>();
	}

	@Test
	public void testAddCommand()
	{
		AtomicInteger undoConsumed = new AtomicInteger(0); // should be mutable
		AtomicInteger redoConsumed = new AtomicInteger(0); // should be mutable
		commandHistory.addUndoEmptyChangedConsumer(undoEmptyChangedEvent ->
		{
			if (undoEmptyChangedEvent.getNewValue())
			{
				undoConsumed.incrementAndGet();
			}
		});
		commandHistory.addRedoEmptyChangedConsumer(redoEmptyChangedEvent ->
		{
			if (redoEmptyChangedEvent.getNewValue())
			{
				redoConsumed.incrementAndGet();
			}
		});
		Command mockCommand1 = Mockito.mock(Command.class);
		Command mockCommand2 = Mockito.mock(Command.class);

		commandHistory.addCommand(mockCommand1); // undo = 1, redo = 0 [empty = false,true]
		commandHistory.undo(); // undo = 0, redo = 1 [empty = true, false]
		commandHistory.addCommand(mockCommand2);  // undo = 1, redo = 0 [empty = false,true]
		commandHistory.redo(); // expected noop [because undo stack is empty]

		Mockito.verify(
				mockCommand1,
				Mockito.times(0))
				.redo();
		Assert.assertEquals(1, undoConsumed.get());
		Assert.assertEquals(2, redoConsumed.get());
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
	public void testHashCode_same()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = commandHistory1;

		int hashCode1 = commandHistory1.hashCode();
		int hashCode2 = commandHistory2.hashCode();

		Assert.assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testHashCode_equalButNotSame()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = new CommandHistory<>();

		int hashCode1 = commandHistory1.hashCode();
		int hashCode2 = commandHistory2.hashCode();

		Assert.assertNotEquals("Object reference/address should be hashCode", hashCode1, hashCode2);
	}

	@Test
	public void testHashCode_notEnabled()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = new CommandHistory<>();
		commandHistory1.setEnabled(true);
		commandHistory2.setEnabled(false);

		int hashCode1 = commandHistory1.hashCode();
		int hashCode2 = commandHistory2.hashCode();

		Assert.assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void testEquals_same()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = commandHistory1;

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_equalButDifferencReferences()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = new CommandHistory<>();

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse("Even if both histories are empty, they use different maps, so they cannot be equal.", equals);
	}

	@Test
	public void testEquals_null()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = null;

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_wrongObject()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		Object commandHistory2 = 1L;

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_notEnabled()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = new CommandHistory<>();
		commandHistory1.setEnabled(false);

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_diffeentUndo()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = new CommandHistory<>();
		Command mockCommand = Mockito.mock(Command.class);
		commandHistory1.addCommand(mockCommand);

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_diffeentRedo()
	{
		CommandHistory<Command> commandHistory1 = new CommandHistory<>();
		CommandHistory<Command> commandHistory2 = new CommandHistory<>();
		Command mockCommand = Mockito.mock(Command.class);
		commandHistory1.addCommand(mockCommand);
		commandHistory1.addCommand(mockCommand);
		commandHistory2.addCommand(mockCommand);
		commandHistory1.undo();

		boolean equals = commandHistory1.equals(commandHistory2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testRedo_empty_not_enabled()
	{
		AtomicBoolean redoConsumed = new AtomicBoolean(false); // should be mutable
		commandHistory.addRedoEmptyChangedConsumer(redoEmptyChangedEvent ->
		{
			redoConsumed.set(true);
		});

		commandHistory.setEnabled(false);
		commandHistory.redo();

		Assert.assertFalse(redoConsumed.get());
	}

	@Test
	public void testPeekNextRedo_empty1()
	{
		Command nextRedoCommand = commandHistory.peekNextRedo();

		Assert.assertNull(nextRedoCommand);
	}

	@Test
	public void testPeekNextRedo_empty2()
	{
		Command mockCommand = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand);

		Command nextRedoCommand = commandHistory.peekNextRedo();

		Assert.assertNull(nextRedoCommand);
	}

	@Test
	public void testPeekNextRedo()
	{
		Command mockCommand0 = Mockito.mock(Command.class);
		Command mockCommand1 = Mockito.mock(Command.class);
		Command mockCommand2 = Mockito.mock(Command.class);
		Command mockCommand3 = Mockito.mock(Command.class);
		Command mockCommand4 = Mockito.mock(Command.class);
		Command mockCommand5 = Mockito.mock(Command.class);
		commandHistory.addCommand(mockCommand0);
		commandHistory.addCommand(mockCommand1);
		commandHistory.addCommand(mockCommand2);
		commandHistory.addCommand(mockCommand3);
		commandHistory.addCommand(mockCommand4);
		commandHistory.addCommand(mockCommand5);
		commandHistory.undo();
		commandHistory.undo();
		commandHistory.undo();

		Command nextRedoCommand = commandHistory.peekNextRedo();

		Assert.assertEquals(mockCommand3, nextRedoCommand);
		Assert.assertNotEquals(mockCommand3, mockCommand2); // [for sanity about the test library]
	}
}
