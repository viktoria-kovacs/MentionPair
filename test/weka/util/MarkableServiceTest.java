package weka.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Range;

import weka.domain.Markable;

public class MarkableServiceTest {

	private MarkableService markableService;

	private Range<Integer> head = Range.closed(8, 10);
	private Range<Integer> foot = Range.closed(50, 55);
	private List<Markable> markables;

	@Before
	public void setUp() {
		markableService = new MarkableService();
		markables = new ArrayList<>();
	}

	/**
	 * |cp1|
	 *    |h|...|cp2|...|f|...|cp3|
	 */
	@Test
	public void getCpsBetween_shouldOnlyAddBetween() {
		addMarkable(4, 9);
		addMarkable(12, 15);
		addMarkable(56, 60);

		assertCpsBetweenEquals(2);
	}

	/**
	 *  |h|
	 * |cp|...|f|
	 */
	@Test
	public void getCpsBetween_shouldAddWhenCpStartsWhereHeadEnds() {
		addMarkable(4, 10);

		assertCpsBetweenEquals(1);
	}

	/**
	 * |h|...|f|
	 *         |cp|
	 */
	@Test
	public void getCpsBetween_shouldNotAddWhenCpStartsWhereFootEnds() {
		addMarkable(55, 60);

		assertCpsBetweenEquals(0);
	}

	/**
	 * |h|...|cp1|
	 *       |cp2  |...|f|
	 */
	@Test
	public void getCpsBetween_shouldOnlyAddOneWhereTwoMarkablesStart() {
		addMarkable(12, 20);
		addMarkable(12, 25);

		assertCpsBetweenEquals(3);
	}

	/**
	 * |h|...|cp1  |
	 *         |cp2|...|f|
	 */
	@Test
	public void getCpsBetween_shouldOnlyAddOneWhereTwoMarkablesEnd() {
		addMarkable(12, 20);
		addMarkable(15, 20);

		assertCpsBetweenEquals(3);
	}

	private void addMarkable(int from, int to) {
		Markable markable = new Markable();
		markable.setSpan(Range.closed(from, to));
		markables.add(markable);
	}

	private void assertCpsBetweenEquals(long assertion) {
		long result = markableService.getCpsBetween(markables, head, foot);

		assertEquals(assertion, result);
	}

}
