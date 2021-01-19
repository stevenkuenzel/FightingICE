import aiinterface.AIInterface;
import core.Game;
import fighting.Character;
import ftginterface.Fight;
import gamescene.Play;
import loader.ResourceLoader;
import manager.InputManager;
import util.CharacterDiskInformation;
import util.CharacterRoundData;

import java.util.List;

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

        Fight f = new Fight(3, 300, true);
        f.setPlayer(0, "MctsAi", "ZEN");
        f.setPlayer(1, "Thunder", "ZEN");

        f.initialize();
        f.run();

        List<List<CharacterRoundData>> l = f.getRoundData();
        int x = 0;
    }
}
