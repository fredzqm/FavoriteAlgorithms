package quickUnion;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class QuickUnionTest {

    @Test
    public void testSimple() {
        QuickUnion u = new QuickUnion(10);
        assertNotEquals(u.find(0), u.find(1));
        assertNotEquals(u.find(0), u.find(2));

        u.union(0, 1);
        assertEquals(u.find(0), u.find(1));
        assertNotEquals(u.find(0), u.find(2));

        u.union(2, 1);
        assertEquals(u.find(0), u.find(1));
        assertEquals(u.find(1), u.find(2));
        assertEquals(u.find(0), u.find(2));
    }

    @Test
    public void testChain1() {
        QuickUnion u = new QuickUnion(10);
        u.union(1, 2);
        u.union(2, 3);
        u.union(3, 4);
        u.union(4, 5);
        assertEquals(u.find(1), u.find(5));
    }

    @Test
    public void testChain2() {
        QuickUnion u = new QuickUnion(10);
        u.union(2, 1);
        u.union(3, 2);
        u.union(4, 3);
        u.union(5, 4);
        assertEquals(u.find(1), u.find(5));
    }

    @Test
    public void testRing1() {
        QuickUnion u = new QuickUnion(10);
        u.union(1, 2);
        u.union(2, 3);
        u.union(3, 4);
        u.union(4, 5);
        u.union(5, 1);
        assertEquals(u.find(1), u.find(5));
    }

    @Test
    public void testRing2() {
        QuickUnion u = new QuickUnion(10);
        u.union(2, 1);
        u.union(3, 2);
        u.union(4, 3);
        u.union(5, 4);
        u.union(1, 5);
        assertEquals(u.find(1), u.find(5));
    }

    @Test
    public void testStar1() {
        QuickUnion u = new QuickUnion(10);
        u.union(1, 2);
        u.union(1, 3);
        u.union(1, 4);
        u.union(1, 5);
        assertEquals(u.find(1), u.find(5));
        assertEquals(u.find(4), u.find(5));
        assertEquals(u.find(2), u.find(4));
    }

    @Test
    public void testStar2() {
        QuickUnion u = new QuickUnion(10);
        u.union(2, 1);
        u.union(3, 1);
        u.union(4, 1);
        u.union(5, 1);
        assertEquals(u.find(1), u.find(5));
        assertEquals(u.find(4), u.find(5));
        assertEquals(u.find(2), u.find(4));
    }

    @Test
    public void testUnionTwice() {
        QuickUnion u = new QuickUnion(10);

        u.union(0, 1);
        u.union(1, 0);
        assertEquals(u.find(0), u.find(1));
        assertEquals(u.find(0), u.find(1));
        assertNotEquals(u.find(0), u.find(2));
    }
}
