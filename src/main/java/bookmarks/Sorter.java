package bookmarks;

import java.util.*;
import java.util.stream.Collectors;

public class Sorter {
	private static int min(int a, int b) {
		return (a < b ? a : b);
	}

	// Gets the longest common prefix of two strings, when given it has length >= ini
	private static int getLcp(String a, String b, int ini) {
		int mx = min(a.length(), b.length());
		for (int i = ini; i < mx; ++i) {
			if (a.charAt(i) != b.charAt(i)) return i;
		}
		return mx;
	}

	// Returns true if a[i] <= b[i], considering no character as smallest possible character
	private static boolean compChar(String a, String b, int i) {
		if (a.length() <= i) return true;
		if (b.length() <= i) return false;
		return a.charAt(i) <= b.charAt(i);
	}

	// Returns the permutation that stably sorts the input integers
	// Just does mergesort. This should be replaced by just calling some library function probably
	public static int[] sortInts(int[] data) {
		int n = data.length;

		int[] perm = new int[n];
		for (int i = 0; i < n; ++i) perm[i] = i;
		int[] tmpPerm = new int[n];

		for (int hl = 1; hl < n; hl *= 2) {
			// Merge sections of length 2*hl
			for (int a = 0; a + hl < n; a += 2 * hl) {
				int b = a + hl;
				int c = min(b + hl, n);
				// Merge [a, b) and [b, c)

				int i = 0;
				int j = 0;
				while(true) {
					int t = a+i+j;
					if (t == c) break;
					
					boolean pa = true;
					if (a+i != b && b+j != c) {
						pa = (data[perm[a+i]] <= data[perm[b+j]]);
					} else if (a+i == b) {
						pa = false;
					}

					if (pa) {
						tmpPerm[t] = perm[a+i];
						++i;
					} else {
						tmpPerm[t] = perm[b+j];
						++j;
					}
				}

				// Move data back from auxiliary array
				for (int x = a; x < c; ++x) {
					perm[x] = tmpPerm[x];
				}
			}
		}

		return perm;
	}

	// Returns the permutation that stably sorts the input strings
	// Does string mergesort for sorting.
	public static int[] sortStrings(String[] data) {
		int n = data.length;

		int[] lcp = new int[n]; // LCP-array
		int[] perm = new int[n]; // Result permutation
		for (int i = 0; i < n; ++i) perm[i] = i;

		// tmp variables for storing data
		int[] tmpLcp = new int[n];
		int[] tmpPerm = new int[n];

		for (int hl = 1; hl < n; hl *= 2) {

			// Merge sections of length 2 * hl
			for (int a = 0; a+hl < n; a += 2*hl) {
				int b = a + hl;
				int c = min(n, b + hl);
				// Merge parts [a, b) and [b, c)

				int i = 0;
				int j = 0;
				while(true) {
					int t = a+i+j; // Where the chosen entry will go
					if (t == c) break;

					boolean pa = true; // Do we pick the one from the first segment
					if (a+i != b && b+j != c) {
						// Compare first non-matching character
						int lp = getLcp(data[perm[a+i]], data[perm[b+j]], min(lcp[a+i], lcp[b+j]));
						pa = compChar(data[perm[a+i]], data[perm[b+j]], lp);
						
						if (pa) lcp[b+j] = lp;
						else lcp[a+i] = lp;
					} else if (a+i == b) {
						pa = false; // first part fully used
					}

					if (pa) {
						// Pick from first segment
						tmpLcp[t] = lcp[a+i];
						tmpPerm[t] = perm[a+i];
						++i;
					} else {
						// Pick from second segment
						tmpLcp[t] = lcp[b+j];
						tmpPerm[t] = perm[b+j];
						++j;
					}
				}

				// Move data back from auxiliary arrays
				for (int x = a; x < c; ++x) {
					perm[x] = tmpPerm[x];
					lcp[x] = tmpLcp[x];
				}
			}
		}

		return perm;
	}


	// Permutes the given array with the given permutation (0-indexed)
	public static <T> void permute(T[] data, int[] perm) {
		int n = perm.length;

		// Reverse the permutation
		int[] pr = new int[n];
		for (int i = 0; i < n; ++i) pr[perm[i]] = i;

		for (int i = 0; i < n; ++i) {
			int t = pr[i];
			while(t != i) {
				// Do a swap
				T tmp = data[i];
				data[i] = data[t];
				data[t] = tmp;
				pr[i] = pr[t];
				pr[t] = t;
				t = pr[i];
			}
		}
	}
}
