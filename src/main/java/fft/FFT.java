package fft;

import java.util.Arrays;

public class FFT {
    static class Complex {
        final double re, im;

        public Complex(Complex complex) {
            this.re = complex.re;
            this.im = complex.im;
        }

        public Complex(long re) {
            this.re = re;
            this.im = 0;
        }

        public Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }

        public static double getReal(Complex a) {
            if (a == null) {
                return 0;
            }
            return a.re;
        }

        @Override
        public String toString() {
            return String.format("%.3f+%.3fi", re, im);
        }

        public static Complex mult(Complex a, Complex b) {
            if (a == null || b == null) {
                return null;
            }
            return new Complex(a.re * b.re - a.im * b.im, a.re * b.im + a.im * b.re);
        }

        public static Complex conj(Complex a) {
            if (a == null) {
                return null;
            }
            return new Complex(a.re, -a.im);
        }

        public static Complex sub(Complex a, Complex b) {
            return add(a, neg(b));
        }

        public static Complex neg(Complex a) {
            if (a == null) {
                return null;
            }
            return new Complex(-a.re, -a.im);
        }

        public static Complex add(Complex a, Complex b) {
            if (a == null) {
                return b;
            }
            if (b == null) {
                return a;
            }
            return new Complex(a.re + b.re, a.im + b.im);
        }
    }

    public static Complex expj(double rad) {
        return new Complex(Math.cos(rad), Math.sin(rad));
    }

    public static void transform(Complex[] seg, int levels) {
        final int len = seg.length;
        assert len == 1 << levels;
        for (int i = 0; i < len; i++) {
            int j = Integer.reverse(i) >>> (32 - levels); // the bit reverse step for in-place operation
            if (j < i) {
                Complex t = seg[i];
                seg[i] = seg[j];
                seg[j] = t;
            }
        }
        Complex[] fr = new Complex[len / 2];
        for (int j = 0; j < len / 2; j++) {
            fr[j] = expj(Math.PI * 2 * j / len);
        }
        for (int level = 1; level <= levels; level++) {
            int halfSize = 1 << (level - 1);
            int step = 1 << (levels - level);
            for (int i = 0; i < len; i += halfSize * 2) {
                for (int j = i; j < i + halfSize; j++) {
                    Complex tre = Complex.mult(seg[j + halfSize], fr[(j - i) * step]);
                    seg[j + halfSize] = Complex.sub(seg[j], tre);
                    seg[j] = Complex.add(seg[j], tre);
                }
            }
        }
    }

    public static long[] conv(long[]... ls) {
        int maxLen = -ls.length + 1;
        for (long[] a : ls) {
            maxLen += a.length;
        }
        maxLen = Math.max(0, maxLen);
        int levels = (int) Math.ceil(Math.log(maxLen) / Math.log(2));
        int len = 1 << levels;
        Complex[] mult = new Complex[len];
        for (int i = 0; i < len; i++) {
            mult[i] = new Complex(1);
        }
        Complex[] ac = new Complex[len];
        for (long[] a : ls) {
            for (int i = 0; i < a.length; i++) {
                ac[i] = new Complex(a[i]);
            }
            for (int i = a.length; i < len; i++) {
                ac[i] = null;
            }
            transform(ac, levels);
            for (int i = 0; i < len; i++) {
                mult[i] = Complex.mult(mult[i], ac[i]);
            }
        }
        for (int i = 0; i < len; i++) {
            mult[i] = Complex.conj(mult[i]);
        }
        transform(mult, levels);
        long[] result = new long[maxLen];
        for (int i = 0; i < maxLen; i++) {
            result[i] = Math.round(Complex.getReal(mult[i]) / len);
        }
        return result;
    }

    public static void main(String[] args) {
        long[] arr1 = new long[3];
        arr1[0] = 1;
        arr1[1] = 1;
        arr1[2] = 1;

        long[] arr2 = new long[5];
        arr2[0] = 1;
        arr2[1] = 1;
        arr2[2] = 1;
        arr2[3] = 1;

        long[] arr3 = conv(arr1, arr2, new long[]{1});

        System.out.println(Arrays.toString(arr3));

        Complex[] unit = new Complex[16];
        unit[0] = new Complex(1);
        transform(unit, 4);
        System.out.println(Arrays.toString(unit));
    }
}
