package fft;

import java.io.IOException;
import java.util.Arrays;

public class KInversions {
    public static long mod = (5 << 25) + 1;
    public static long gen = 71;
    public static int levels = 5;
    public static int len = 1 << levels;

    // need 2*n <= len

    static {
        int ww = 25;
        while (ww-- > levels) {
            gen = gen * gen % mod;
        }
    }

    /**
     * https://www.wikiwand.com/en/Cooley%E2%80%93Tukey_FFT_algorithm
     *
     * @param a
     * @param gen
     */
    public static void transform(long[] a, long gen) {
        for (int i = 0; i < len; i++) {
            int j = Integer.reverse(i) >>> (32 - levels); // the bit reverse step for in-place operation
            if (j < i) {
                long t = a[i];
                a[i] = a[j];
                a[j] = t;
            }
        }
        long[] fr = new long[len / 2];
        fr[0] = 1;
        for (int j = 1; j < len / 2; j++) {
            fr[j] = fr[j - 1] * gen % mod;
        }
        for (int level = 1; level <= levels; level++) {
            int halfSize = 1 << (level - 1);
            int step = 1 << (levels - level);
            for (int i = 0; i < len; i += halfSize * 2) {
                for (int j = i; j < i + halfSize; j++) {
                    long tre = a[j + halfSize] * fr[(j - i) * step] % mod;
                    a[j + halfSize] = (a[j] + mod - tre) % mod;
                    a[j] = (a[j] + tre) % mod;
                }
            }
        }
    }

    public static long inv(long N, long M) {
        long x = 0, lastx = 1, y = 1, q, t, a = N, b = M;
        while (b != 0) {
            q = a / b;
            t = a % b;
            a = b;
            b = t;
            t = x;
            x = lastx - q * x;
            lastx = t;
        }
        return (lastx + M) % M;
    }

    public static void main(String[] args) throws IOException {
        long[] arr1 = new long[len];
        arr1[1] = 1;
        arr1[3] = 1;
        long[] arr2 = new long[len];
        arr2[1] = 1;
        arr2[3] = 1;
        arr2[4] = 1;

        conv(arr1, arr2);

        System.out.println(Arrays.toString(arr1));
    }

    private static void conv(long[] arr1, long[] arr2) {
        transform(arr1, gen);
        transform(arr2, gen);
        long scale = inv(len, mod);
        for (int i = 0; i < len; i++) {
            arr1[i] = arr1[i] * arr2[i] % mod * scale % mod;
        }
        transform(arr1, inv(gen, mod));
    }
}
