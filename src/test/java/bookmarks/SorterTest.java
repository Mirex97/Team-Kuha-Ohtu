package bookmarks;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Random;

public class SorterTest {
	@Test
	public void sortingStringsWorks() {
		Random r = new Random(666);

		int n = 10000;
		int max_m = 20;
		int c = 2;

		String[] data = new String[n];
		for (int i = 0; i < n; ++i) {
			int m = r.nextInt(max_m + 1);
			char[] rawStr = new char[m];
			for (int j = 0; j < m; ++j) rawStr[j] = (char) ('a' + r.nextInt(c));
			data[i] = new String(rawStr);
		}

		int[] perm = Sorter.sortStrings(data);

		// Check that what's given is a permutation
		assertEquals(perm.length, n);
		boolean[] apps = new boolean[n];
		for (int i = 0; i < n; ++i) apps[i] = false;
		for (int i = 0; i < n; ++i) apps[perm[i]] = true;
		for (int i = 0; i < n; ++i) assertTrue(apps[i]);

		// Check that the given permutation sorts the strings
		for (int i = 0; i + 1 < n; ++i) {
			assertTrue(data[perm[i]].compareTo(data[perm[i + 1]]) <= 0);
		}

		// Check that the given permutation stably sorts the strings
		for (int i = 0; i + 1 < n; ++i) {
			if (data[perm[i]].equals(data[perm[i + 1]])) assertTrue(perm[i] < perm[i + 1]);
		}
	}

	@Test
	public void sortingIntsWorks() {
		Random r = new Random(666);

		int n = 100000;
		int maxv = 1000;

		int[] data = new int[n];
		for (int i = 0; i < n; ++i) data[i] = r.nextInt(maxv);

		int[] perm = Sorter.sortInts(data);

		// Check that what's given is a permutation
		assertEquals(perm.length, n);
		boolean[] apps = new boolean[n];
		for (int i = 0; i < n; ++i) apps[i] = false;
		for (int i = 0; i < n; ++i) apps[perm[i]] = true;
		for (int i = 0; i < n; ++i) assertTrue(apps[i]);

		// Check that the given permutation sorts the strings
		for (int i = 0; i + 1 < n; ++i) {
			assertTrue(data[perm[i]] <= data[perm[i + 1]]);
		}

		// Check that the given permutation stably sorts the strings
		for (int i = 0; i + 1 < n; ++i) {
			if (data[perm[i]] == data[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);
		}
	}

	@Test
	public void permutingWorks() {
		Random r = new Random(666);

		int n = 10;
		int[] perm = new int[n];
		Integer[] data = new Integer[n];
		for (int i = 0; i < n; ++i) data[i] = i;

		for (int i = 1; i < n; ++i) {
			int j = r.nextInt(i + 1);
			perm[i] = perm[j];
			perm[j] = i;
		}

		Sorter.permute(data, perm);

		for (int i = 0; i < n; ++i) {
			assertEquals((int) data[i], perm[i]);
		}
	}
}
