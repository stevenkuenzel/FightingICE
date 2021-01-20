package ftginterface;

import aiinterface.AIInterface;
import core.Game;
import gamescene.Play;
import manager.InputManager;
import util.CharacterDiskInformation;
import util.CharacterRoundData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fight {
    int rounds, framesPerRound;
    boolean randomInitialPositions;

    Player[] players = new Player[2];

    HashMap<String, AIInterface> toRegister = new HashMap<>();

    Game game;


    public Fight(int rounds, int framesPerRound, boolean randomInitialPositions) {
        this.rounds = rounds;
        this.framesPerRound = framesPerRound;
        this.randomInitialPositions = randomInitialPositions;
    }

    public void setPlayer(int index, String name, String character) {
//        if (index < 0 || index > 1) throw new Exception("Invalid player id.");

        players[index] = new Player(index, name, character);
    }

    public void setPlayer(int index, AIInterface ai, String character) {
        String name = ai.getClass().getName() + (index + 1);

        setPlayer(index, name, character);
        registerAIInterface(ai, name);
    }

    private void registerAIInterface(AIInterface ai, String name) {
        toRegister.put(name, ai);
    }

    public void initialize() {
        game = new Game(players[0].name, players[1].name, new CharacterDiskInformation());
        game.setCharacterNames(players[0].character, players[1].character);
        game.randomInitialPositions = randomInitialPositions;
        game.NUM_OF_ROUNDS = rounds;
        game.ROUND_FRAME_NUMBER = framesPerRound;

        // Register all fighter instances.
        InputManager inputManager = game.inputManager;

        for (String key : toRegister.keySet()) {
            inputManager.registerAI(key, toRegister.get(key));
        }

        game.initialize();
    }

    Play play;
    fighting.Character[] characters;

    public void run()
    {

        while (!game.isExit())
        {
            game.update();

            if (play == null && game.currentGameScene instanceof Play)
            {
                play = (Play) game.currentGameScene;
                characters = play.fighting.getCharacters();
            }

            if (characters != null)
            {
                if (play.nowFrame % 600 == 0)                System.out.println(characters[0].getHp() + "  --  " + characters[1].getHp());
            }

            if (play != null && play.isGameEnd()) break;
        }

        game.close();
    }

    /**
     * Returns a list with two entries ([0] = p1, [1] = p2). Each is a list of round data ([0] = round 1, ...) in turn.
     * @return List of round data.
     */
    public List<List<CharacterRoundData>> getRoundData()
    {
        List<List<CharacterRoundData>> data = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            data.add(new ArrayList<>(characters[i].allCharacterRoundData));
        }

        return data;
    }
}
