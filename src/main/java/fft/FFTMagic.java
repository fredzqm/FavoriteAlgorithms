package fft;

import java.util.Arrays;

public class FFTMagic {
    public static long mod = (5 << 25) + 1;
    public static long gen = 71;
    public static int levels = 5;
    public static int len = 1 << levels;  // need 2*n <= len

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

    static long gcd(long N, long M) {
        long a = M, b = N;
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    static long inv(long N, long M) {
        if (gcd(N, M) != 1) {
            throw new RuntimeException("Cannot do inverse of N, because N and M are not co-prime");
        }
        N = N % M;
//        long x = 0, x1 = 1;
        long y = 1, y1 = 0;
        long a = M, b = N;
        // invariant M*x1+N*y1=a && M*x+N*y=b
        while (b != 1) {
            long q = a / b;
            long t = a % b; // a - q*b
            // b = a' - q*b' = (M*x1+N*y1) - q*(M*x+N*y)
            // b = M*(x1-q*x) - N*(y1-q*y)
            a = b;
            b = t;
//            t = x;
//            x = x1 - q * x;
//            x1 = t;
            t = y;
            y = y1 - q * y;
            y1 = t;
        }
        long l = y % M;
        if (l < 0) {
            l += M;
        }
        return l;
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

    public static void main(String[] args) {
        long t = gen;
        for (int l = 0; l < len; l++) {
            t = t * gen % mod;
        }
        long f = gen;
        for (int l = 0; l < levels; l++) {
            f = f * f % mod;
        }
        // this assertion need to be true in order for this to work
        // mod must be a prime number, or a pseudo-prime number to the base of gen
        System.out.println(t == gen);

        long[] arr1 = new long[len];
        arr1[1] = 1;
        arr1[3] = 1;

        long[] arr2 = new long[len];
        arr2[1] = 1;
        arr2[3] = 1;
        arr2[4] = 1;

        conv(arr1, arr2);

        System.out.println(Arrays.toString(arr1));

        long[] unit = new long[len];
        unit[0] = 1;
        transform(unit, gen);
        System.out.println(Arrays.toString(unit));
    }
}
