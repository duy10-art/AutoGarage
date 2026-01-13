package test;

import model.Auto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AutoTest {

    @Test
    void testIstOldtimer() {
        Auto auto = new Auto("VW","Polo" ,1990, 3000, false);
        assertTrue(auto.istOldtimer());
    }

    @Test
    void testKeinOldtimer() {
        Auto auto = new Auto("BMW", "M3", 2015, 20000, false);
        assertFalse(auto.istOldtimer());
    }
}
