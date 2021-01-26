package ftginterface;

import java.util.Locale;

public class Player {
    public int id;
    public String name;
    public String character;

    public Player(int id, String name, String character) {
        this.id = id;
        this.name = name;
        this.character = character.toUpperCase(Locale.ROOT);
    }
}
