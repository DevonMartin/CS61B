package byow.Core;
import byow.TileEngine.Tileset;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRoomBuilding {
    @Test
    public void spawnRoom() {
        for (int i = 0; i < 500; i++) {
            Engine e = new Engine();
            e.interactWithInputString("ns");
//            assertEquals(e.toString(), Engine.floor, e.playerOn());
        }
    }
}
