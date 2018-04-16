package fft;

public class FFT {

    static int[] conv(int[] a, int[] b, int mod) {
        return null;
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

    public static void main(String[] args) {
        System.out.println(gcd(30, 72));
        System.out.println(gcd(120, 91));
        System.out.println(gcd(3, 5));
        System.out.println(inv(120, 91));
        System.out.println(inv(91, 120));
        System.out.println(inv(3, 5));
    }
}
