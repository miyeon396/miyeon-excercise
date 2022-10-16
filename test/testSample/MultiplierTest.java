package testSample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultiplierTest {

    @Test
    public void multiply() {
        Multiplier multiplier = new Multiplier();
        assertEquals(multiplier.Multiply(8, 9), 72);
    }

    @Test
    public void multiply1() {
        Multiplier multiplier = new Multiplier();
        assertEquals(multiplier.Multiply(2, 3, 4), 24);
    }
}