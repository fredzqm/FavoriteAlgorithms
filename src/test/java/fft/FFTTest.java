package fft;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class FFTTest {

    @Test
    public void testIdentical() {
        final long[][] ls = {{}, {0}, {1}, {2}, {0, 0}, {0, 1, 0}, {0, 0, 100, 103}};
        for (long[] x : ls) {
            assertArrayEquals(x, FFT.conv(x));
        }
    }

    @Test
    public void testBase() {
        assertArrayEquals(new long[]{}, FFT.conv(new long[]{}, new long[]{}));
        assertArrayEquals(new long[]{}, FFT.conv(new long[]{}, new long[]{1}));
        assertArrayEquals(new long[]{1}, FFT.conv(new long[]{1}, new long[]{1}));
    }

    @Test
    public void testLength2() {
        assertArrayEquals(new long[]{1, 2, 1}, FFT.conv(new long[]{1, 1}, new long[]{1, 1}));
        assertArrayEquals(new long[]{1, 3, 2}, FFT.conv(new long[]{1, 2}, new long[]{1, 1}));
        assertArrayEquals(new long[]{0, 1, 0}, FFT.conv(new long[]{1, 0}, new long[]{0, 1}));
        assertArrayEquals(new long[]{0, 0, 1}, FFT.conv(new long[]{0, 1}, new long[]{0, 1}));
        assertArrayEquals(new long[]{0, 1, 1}, FFT.conv(new long[]{0, 1}, new long[]{1, 1}));
    }

    @Test
    public void testMultitple() {
        assertArrayEquals(new long[]{0, 0, 1, 1}, FFT.conv(new long[]{1, 1}, new long[]{0, 1}, new long[]{0, 1}));
        assertArrayEquals(new long[]{0, 3, 4}, FFT.conv(new long[]{3, 4}, new long[]{1}, new long[]{1}, new long[]{0, 1}));
    }

    @Test
    public void testLength3() {
        assertArrayEquals(new long[]{1, 2, 3, 2, 1}, FFT.conv(new long[]{1, 1, 1}, new long[]{1, 1, 1}));
        assertArrayEquals(new long[]{1, 1, 2, 1, 1}, FFT.conv(new long[]{1, 1, 1}, new long[]{1, 0, 1}));
        assertArrayEquals(new long[]{0, 0, 1, 1, 1}, FFT.conv(new long[]{1, 1, 1}, new long[]{0, 0, 1}));
        assertArrayEquals(new long[]{1, 2, 4, 2, 3}, FFT.conv(new long[]{1, 2, 3}, new long[]{1, 0, 1}));
    }

    @Test
    public void testLength4() {
        assertArrayEquals(new long[]{1, 2, 3, 3, 2, 1}, FFT.conv(new long[]{1, 1, 1}, new long[]{1, 1, 1, 1}));
        assertArrayEquals(new long[]{1, 1, 2, 2, 2, 1}, FFT.conv(new long[]{1, 1, 1}, new long[]{1, 0, 1, 1}));
        assertArrayEquals(new long[]{1, 1, 1, 1, 1, 1}, FFT.conv(new long[]{1, 1, 1}, new long[]{1, 0, 0, 1}));
    }

    @Test
    public void testLong() {
        final int size = 10000;
        final int num = 20;
        long[][] x = new long[num][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < num; j++) {
                x[j][i] = (long) Math.random() * size;
            }
        }
        long[] expected = new long[(size - 1) * num + 1];
        expected[0] = 1;
        long t1 = System.currentTimeMillis();
        for (int k = 0; k < num - 1; k++) {
            long[] expected2 = new long[(size - 1) * num + 1];
            for (int i = 0; i < (size - 1) * k - 1; i++) {
                for (int j = 0; j < size; j++) {
                    expected2[i + j] += expected[i] * x[k][j];
                }
            }
            expected = expected2;
        }
        long t2 = System.currentTimeMillis();
        assertArrayEquals(expected, FFT.conv(x));
        long t3 = System.currentTimeMillis();
        System.out.printf("brutal force %d  fft %d", t2 - t1, t3 - t2);
    }
}
