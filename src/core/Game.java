package core;

import enumerate.GameSceneName;
import gamescene.Launcher;
import manager.GameManager;
import util.CharacterDiskInformation;

/**
 * ゲームの起動情報を設定し, 開始するゲームシーンを設定するクラス．
 */
public class Game extends GameManager {

    /**
     * Creates an instance of Game.
     * @param ai1 Name of the first AI fighter.
     * @param ai2 Name of the second AI fighter.
     * @param characterDiskInformation Motion and size information from the drive.
     */
    public Game(String fightingICEroot, String ai1, String ai2, CharacterDiskInformation characterDiskInformation) {
        super(fightingICEroot, characterDiskInformation, ai1, ai2);
    }

    @Override
    public void initialize() {
        Launcher launcher = new Launcher(GameSceneName.PLAY, this);
        this.startGame(launcher);
    }

    @Override
    public void close() {
        this.currentGameScene = null;
    }

}
