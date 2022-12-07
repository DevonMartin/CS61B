package byow.Core;
import byow.TileEngine.Tileset;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRoomBuilding {
    @Test
    public void spawnRoom() {
        for (int i = 0; i < 50000; i++) {
            Engine e = new Engine();
            e.interactWithInputString("ns");
            assertTrue(e.toString(), e.playerOn().equals("floor"));
        }
    }
}
