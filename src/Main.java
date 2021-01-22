import aitest.Xai;
import de.sk.xml.XElement;
import ftginterface.Fight;
import ftginterface.FightObservation;
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

        Fight f = new Fight(3, 3600, true, true);
        f.setPlayer(0, new Xai(), "ZEN");
//        f.setPlayer(1, new Xai(), "ZEN");
        f.setPlayer(1, "Thunder", "ZEN");

        f.initialize();
        f.run();


        FightObservation x = f.getObservation();

        x.export();
        int  y = 0;
    }
}
