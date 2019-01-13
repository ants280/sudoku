package com.github.ants280.sudoku.game.solver;

import com.github.ants280.sudoku.game.SudokuBoard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.junit.Assert;
import org.junit.Test;

public class SudokuSolverPluginTest
{
	@Test
	public void testLogMove_moveDescriptionConsumer()
	{
		List<String> consumedMoveDescriptions = new ArrayList<>();
		Consumer<String> moveDescriptionConsumer = consumedMoveDescriptions::add;
		SudokuSolverPlugin plugin = new SudokuSolverPluginImpl(null, moveDescriptionConsumer);
		String moveDescription = "move made";
		List<String> expectedConsumedMoveDescriptions = Collections.singletonList(moveDescription);

		plugin.logMove(moveDescription);

		Assert.assertEquals(expectedConsumedMoveDescriptions, consumedMoveDescriptions);
	}

	@Test
	public void testLogMove_NULL_moveDescriptionConsumer()
	{
		SudokuSolverPlugin plugin = new SudokuSolverPluginImpl(null, null);
		String moveDescription = "move made";

		plugin.logMove(moveDescription);

		// Should not crash
	}

	private static class SudokuSolverPluginImpl extends SudokuSolverPlugin
	{
		public SudokuSolverPluginImpl(
				SudokuBoard sudokuBoard,
				Consumer<String> moveDescriptionConsumer)
		{
			super(sudokuBoard, moveDescriptionConsumer);
		}

		@Override
		public boolean makeMove()
		{
			return false;
		}
	}
}
