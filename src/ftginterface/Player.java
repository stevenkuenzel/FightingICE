package ftginterface;

import java.util.Locale;

/**
 * Stores information about the two players.
 */
public class Player {
    public int id;
    public String name;
    public String character;

    /**
     * Creates a new instance of Player.

     */
    public Player(int id, String name, String character) {
        this.id = id;
        this.name = name;
        this.character = character.toUpperCase(Locale.ROOT);
    }
}
