package fft;

import java.io.IOException;
import java.util.Arrays;

public class KInversions {
    public static long mod = (5 << 25) + 1;
    public static long gen = 71;
    public static int levels = 5;
    public static int len = 1 << levels;
    public static long[] w = new long[levels + 1];

    // need 2*n <= len

    static {
        int ww = 25;
        while (ww-- > levels) {
            gen = gen * gen % mod;
        }
    }

    public static void transform(long[] a, long gen) {
        for (int i = 0; i < len; i++) {
            int j = Integer.reverse(i) >>> (32 - levels); // the bit reverse step for in-place operation
            if (j < i) {
                long t = a[i];
                a[i] = a[j];
                a[j] = t;
            }
        }

        w[levels] = gen;
        for (int i = levels - 1; i >= 0; i--) {
            w[i] = w[i + 1] * w[i + 1] % mod;
        }
        for (int l = 1, hs = 1; (1 << l) <= len; l++, hs <<= 1) {
            long[] fr = new long[hs];
            fr[0] = 1;
            for (int j = 1; j < hs; j++)
                fr[j] = fr[j - 1] * w[l] % mod;
            for (int i = 0; i < len; i += (1 << l)) {
                for (int j = i; j < i + hs; j++) {
                    long tre = a[j + hs] * fr[j - i] % mod;
                    a[j + hs] = a[j] + mod - tre;
                    if (a[j + hs] >= mod)
                        a[j + hs] -= mod;
                    a[j] += tre;
                    if (a[j] >= mod)
                        a[j] -= mod;
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
