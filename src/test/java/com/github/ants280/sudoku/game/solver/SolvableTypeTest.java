package com.github.ants280.sudoku.game.solver;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SolvableTypeTest
{
	private final SolvableType actualSolvableType;
	private final SolvableType requestedSolvableType;
	private final boolean expectedSolvable;

	public SolvableTypeTest(
			SolvableType actualSolvableType,
			SolvableType requestedSolvableType,
			boolean expectedSolvable)
	{
		this.actualSolvableType = actualSolvableType;
		this.requestedSolvableType = requestedSolvableType;
		this.expectedSolvable = expectedSolvable;
	}

	@Parameterized.Parameters(name = "{index}: SolvableType.{0}.isExpectedSolvable(SolvableType.{1} should equal {2}")
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
				createTestCase(SolvableType.BRUTE_FORCE, SolvableType.BRUTE_FORCE, true),
				createTestCase(SolvableType.BRUTE_FORCE, SolvableType.LOGIC, false),
				createTestCase(SolvableType.BRUTE_FORCE, SolvableType.UNSOLVEABLE, false),
				createTestCase(SolvableType.LOGIC, SolvableType.BRUTE_FORCE, true),
				createTestCase(SolvableType.LOGIC, SolvableType.LOGIC, true),
				createTestCase(SolvableType.LOGIC, SolvableType.UNSOLVEABLE, false),
				createTestCase(SolvableType.UNSOLVEABLE, SolvableType.BRUTE_FORCE, false),
				createTestCase(SolvableType.UNSOLVEABLE, SolvableType.LOGIC, false),
				createTestCase(SolvableType.UNSOLVEABLE, SolvableType.UNSOLVEABLE, false));
	}

	private static Object[] createTestCase(
			SolvableType actualSolvableType,
			SolvableType requestedSolvableType,
			boolean expectedSolvable)
	{
		return new Object[]
		{
			actualSolvableType, requestedSolvableType, expectedSolvable
		};
	}

	@Test
	public void testIsExpectedSolvable()
	{
		boolean actualSolvable
				= actualSolvableType.isExpectedSolvable(requestedSolvableType);

		Assert.assertEquals(expectedSolvable, actualSolvable);
	}
}
