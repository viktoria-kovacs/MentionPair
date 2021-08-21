package weka.util;

import com.google.common.collect.Range;
import org.junit.Before;
import org.junit.Test;
import weka.domain.Markable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MarkableService2Test {

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
	 * |cp1|            |cp4|
	 * |h|..|cp2||cp3|..|f|
	 */
	@Test
	public void test1() {
		addMarkable(6, 10);
		addMarkable(11, 15);
		addMarkable(16, 20);
		addMarkable(50, 60);

		assertCpsBetweenEquals(9);
	}

	/**
	 * |cp1| |cp3||cp4|  |cp5|
	 * |h|..|   cp2   |..|f|
	 */
	@Test
	public void test2() {
		addMarkable(6, 10);
		addMarkable(11, 20);
		addMarkable(11, 15);
		addMarkable(16, 20);
		addMarkable(50, 60);

		assertCpsBetweenEquals(8);
	}

	/**
	 * |cp1| |cp3|     |cp5|
	 * |h|..|   cp2   |..|f|
	 */
	@Test
	public void test3() {
		addMarkable(6, 10);
		addMarkable(11, 20);
		addMarkable(11, 15);
		addMarkable(50, 60);

		assertCpsBetweenEquals(7);
	}

	/**
	 * |cp1| |cp3|      |cp4|           |cp6|
	 * |h|..|   cp2   ||   cp5   |..     |f|
	 */

	@Test
	public void test4() {
		addMarkable(5, 10);
		addMarkable(11, 23);
		addMarkable(14, 22);
		addMarkable(25, 38);
		addMarkable(33, 37);
		addMarkable(50, 55);

		assertCpsBetweenEquals(11);
	}

	/**
	 *                   |cp5|
	 * |cp1|   |cp3|   |   cp4|       |cp6|
	 * |h|..|   cp2            |..     |f|
	 */

	@Test
	public void test5() {
		addMarkable(5, 10);
		addMarkable(11, 38);
		addMarkable(14, 22);
		addMarkable(25, 38);
		addMarkable(33, 37);
		addMarkable(50, 55);

		assertCpsBetweenEquals(9);
	}

	/**
	 *              |cp3||cp4|   |cp3||cp4|
	 * |cp1|      |  cp3      ||   cp4     |       |cp6|
	 * |h|..    |    cp2                    |..     |f|
	 */

	@Test
	public void test6() {
		addMarkable(5, 10);
		addMarkable(11, 38);
		addMarkable(11, 25);
		addMarkable(12, 14);
		addMarkable(16, 20);
		addMarkable(26, 38);
		addMarkable(27, 30);
		addMarkable(33, 37);
		addMarkable(50, 55);

		assertCpsBetweenEquals(12);
	}
	
	/**
	 * |cp1|    |cp4|
	 *   |h|....|f|
	 */
	@Test
	public void test7() {
		addMarkable(7, 10);
		addMarkable(50, 58);
		
		assertCpsBetweenEquals(2);
	}


	private void addMarkable(int from, int to) {
		Markable markable = new Markable();
		markable.setSpan(Range.closed(from, to));
		markables.add(markable);
	}

	private void assertCpsBetweenEquals(long assertion) {
		long result = markableService.getCpsBetween2(markables, head, foot, "Dem");

		assertEquals(assertion, result);
	}

}
