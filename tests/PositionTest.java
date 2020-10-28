import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    public void testConstructor() {
        Position position = new Position(50.50D, 37.62D);

        assertEquals(50.50, position.getPosX());
        assertEquals(37.62, position.getPosY());

    }

}