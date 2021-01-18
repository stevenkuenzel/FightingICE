import core.Game;
import fighting.Character;
import gamescene.Play;
import manager.InputManager;
import util.CharacterDiskInformation;

/**
 * FightingICEのメインメソッドを持つクラス．
 */
public class Main {

    /**
     * 起動時に入力した引数に応じて起動情報を設定し, それを基にゲームを開始する．<br>
     * このメソッドはFightingICEのメインメソッドである．
     *
     * @param options 起動時に入力した全ての引数を格納した配列
     */
    public static void main(String[] options) {
        Game game = new Game("MctsAi", "Thunder", new CharacterDiskInformation());
        game.setCharacterNames("ZEN", "ZEN");
        game.ROUND_FRAME_NUMBER = 600;
        game.NUM_OF_ROUNDS = 3;
        game.randomInitialPositions = true;

        InputManager inputManager = game.inputManager;

        game.initialize();

        Play play = null;
        Character[] characters = null;

        int i = 0;

        while (!game.isExit())
        {
            game.update();
            i++;

            System.out.println(""+i);


            if (play == null && game.currentGameScene instanceof Play)
            {
                play = (Play)game.currentGameScene;
                characters = play.fighting.getCharacters();
            }

            if (play != null && play.isGameEnd())
            {
                break;
            }
        }

        int x = 0;
    }
}
