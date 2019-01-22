package com.github.ants280.sudoku.game.solver;

public enum SolvableType
{
	BRUTE_FORCE,
	LOGIC, // (should also be solveable with BRUTE_FORCE
	UNSOLVEABLE;

	public boolean isExpectedSolvable(SolvableType solvableType)
	{
		return this == SolvableType.LOGIC && solvableType != SolvableType.UNSOLVEABLE
				|| this != SolvableType.UNSOLVEABLE && solvableType == SolvableType.BRUTE_FORCE;
	}
}
