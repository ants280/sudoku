package com.github.ants280.sudoku.ui;

import org.junit.Assert;
import org.junit.Test;

public class MoveDirectionTest
{
	@Test
	public void testGetDxDy_exactlyOneIsZero()
	{
		for (MoveDirection moveDirection : MoveDirection.values())
		{
			int dx = moveDirection.getDx();
			int dy = moveDirection.getDy();

			Assert.assertTrue((dx == 0) ^ (dy == 0));
		}
	}

	@Test
	public void testGetDxDy_unique()
	{
		for (MoveDirection moveDirection : MoveDirection.values())
		{
			int dx = moveDirection.getDx();
			int dy = moveDirection.getDy();

			for (MoveDirection otherMoveDirection : MoveDirection.values())
			{
				if (moveDirection != otherMoveDirection
						&& dx == otherMoveDirection.getDx()
						&& dy == otherMoveDirection.getDy())
				{
					Assert.fail(String.format(
							"MoveDirection.%s and MoveDirection.$s "
							+ "have the same dx and dy (%d and%d)",
							moveDirection,
							otherMoveDirection,
							dx,
							dy));
				}
			}
		}
	}
}
