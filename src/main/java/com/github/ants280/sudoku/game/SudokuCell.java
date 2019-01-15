package com.github.ants280.sudoku.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SudokuCell
{
	private final Map<SectionType, Integer> sectionTypeIndices;
	private SudokuValue value;
	private boolean locked;
	private final Set<SudokuValue> possibleValues;
	private final List<Consumer<SudokuEvent<SudokuCell, SudokuValue>>> cellValueChangedConsumers;
	private final List<Consumer<SudokuEvent<SudokuCell, SudokuValue>>> cellPossibleValueChangedConsumers;
	private boolean listenersEnabled;

	public SudokuCell(
			int rowIndex,
			int columnIndex,
			int groupIndex,
			SudokuValue value,
			boolean locked)
	{
		if (locked && value == null)
		{
			throw new IllegalArgumentException(
					"Cannot lock an empty SudokuCell.");
		}

		this.sectionTypeIndices = new EnumMap<>(SectionType.class);
		sectionTypeIndices.put(SectionType.ROW, rowIndex);
		sectionTypeIndices.put(SectionType.COLUMN, columnIndex);
		sectionTypeIndices.put(SectionType.GROUP, groupIndex);

		this.value = value;
		this.locked = locked;
		this.possibleValues = EnumSet.noneOf(SudokuValue.class);

		this.cellValueChangedConsumers = new ArrayList<>();
		this.cellPossibleValueChangedConsumers = new ArrayList<>();
		this.listenersEnabled = true;
	}

	public int getIndex(SectionType sectionType)
	{
		return sectionTypeIndices.get(sectionType);
	}

	public SudokuValue getValue()
	{
		return value;
	}

	public Collection<SudokuValue> getPossibleValues()
	{
		return EnumSet.copyOf(possibleValues);
	}

	public boolean hasPossibleValue(SudokuValue value)
	{
		return possibleValues.contains(value);
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setValue(SudokuValue value)
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Cannot set value of locked SudokuCell.");
		}

		SudokuValue previousValue = this.value;

		this.value = value;

		if (listenersEnabled && previousValue != value)
		{
			SudokuEvent<SudokuCell, SudokuValue> cellValueChangedEvent
					= new SudokuEvent<>(this, previousValue, value);
			cellValueChangedConsumers
					.forEach(consumer -> consumer.accept(cellValueChangedEvent));
		}
	}

	public void clearPossibleValues()
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Reset possible values of a locked SudokuCell.");
		}

		possibleValues.clear();
	}

	public void restoreAllPossibleValues()
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Restoring possible values of a locked SudokuCell.");
		}

		Arrays.stream(SudokuValue.values())
				.filter(v -> !this.hasPossibleValue(v))
				.forEach(this::togglePossibleValue);
	}

	public void togglePossibleValue(SudokuValue value)
	{
		if (locked)
		{
			throw new IllegalArgumentException(
					"Cannot toggle possible values of locked SudokuCell.");
		}
		if (value == null)
		{
			throw new IllegalArgumentException(
					"Cannot toggle null possible value on SudokuCell.");
		}

		if (this.hasPossibleValue(value))
		{
			possibleValues.remove(value);
		}
		else
		{
			possibleValues.add(value);
		}

		if (listenersEnabled)
		{
			SudokuEvent<SudokuCell, SudokuValue> cellPossibleValueChangedEvent
					= new SudokuEvent<>(this, value, value);
			cellPossibleValueChangedConsumers
					.forEach(consumer -> consumer.accept(cellPossibleValueChangedEvent));
		}
	}

	public void setLocked(boolean locked)
	{
		if (locked && value == null)
		{
			throw new IllegalArgumentException(
					"Cannot lock an empty SudokuCell.");
		}

		this.locked = locked;

		this.possibleValues.clear();
	}

	public void resetFrom(SudokuCell otherSudokuCell)
	{
		value = otherSudokuCell.getValue();

		possibleValues.clear();
		possibleValues.addAll(otherSudokuCell.getPossibleValues());

		locked = otherSudokuCell.isLocked();
	}

	public void addCellValueChangedConsumer(
			Consumer<SudokuEvent<SudokuCell, SudokuValue>> cellValueChangedConsumer)
	{
		cellValueChangedConsumers.add(cellValueChangedConsumer);
	}

	public void addCellPossibleValueChangedConsumer(
			Consumer<SudokuEvent<SudokuCell, SudokuValue>> cellPossibleValueChangedConsumer)
	{
		cellPossibleValueChangedConsumers.add(cellPossibleValueChangedConsumer);
	}

	public void setListenersEnabled(boolean enabled)
	{
		listenersEnabled = enabled;
	}

	@Override
	public String toString()
	{
		return String.format("SudokuCell{r%d,c%d,g%d,v=%d,possibleValues=[%s],locked=%s}",
				this.getIndex(SectionType.ROW),
				this.getIndex(SectionType.COLUMN),
				this.getIndex(SectionType.GROUP),
				value == null ? 0 : value.getValue(),
				possibleValues.stream()
						.mapToInt(SudokuValue::getValue)
						.mapToObj(String::valueOf)
						.collect(Collectors.joining()),
				locked ? "Y" : "N");
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 17 * hash + Objects.hashCode(this.sectionTypeIndices);
		hash = 17 * hash + Objects.hashCode(this.value);
		hash = 17 * hash + (this.locked ? 1 : 0);
		hash = 17 * hash + (this.listenersEnabled ? 1 : 0);
		hash = 17 * hash + Objects.hashCode(this.possibleValues);
		hash = 17 * hash + Objects.hashCode(this.cellValueChangedConsumers);
		hash = 17 * hash + Objects.hashCode(this.cellPossibleValueChangedConsumers);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj
				|| obj != null
				&& this.getClass() == obj.getClass()
				&& this.locked == ((SudokuCell) obj).locked
				&& this.listenersEnabled == ((SudokuCell) obj).listenersEnabled
				&& Objects.equals(
						this.sectionTypeIndices,
						((SudokuCell) obj).sectionTypeIndices)
				&& Objects.equals(
						this.value,
						((SudokuCell) obj).value)
				&& Objects.equals(
						this.possibleValues,
						((SudokuCell) obj).possibleValues)
				&& Objects.equals(cellValueChangedConsumers,
						((SudokuCell) obj).cellValueChangedConsumers)
				&& Objects.equals(cellPossibleValueChangedConsumers,
						((SudokuCell) obj).cellPossibleValueChangedConsumers);
	}
}
