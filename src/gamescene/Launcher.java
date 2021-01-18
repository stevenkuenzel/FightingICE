package gamescene;

import enumerate.GameSceneName;
import manager.GameManager;

/**
 * 起動情報を基に次のゲームシーンを初期化し, 必要なリソースを読み込むクラス．
 */
public class Launcher extends GameScene {

    /**
     * 次の遷移先のゲームシーン．
     */
    private GameSceneName nextGameSceneName;

    /**
     * 最初のアップデートかどうかを表すフラグ．
     */
    private boolean isFirstUpdate;


    /**
     * Launcherシーンを初期化し, 次の遷移先のゲームシーンを設定するクラスコンストラクタ．
     *
     * @param nextGameSceneName 次の遷移先のゲームシーン名
     */
    public Launcher(GameSceneName nextGameSceneName, GameManager gameManager) {
        super(gameManager);

        this.nextGameSceneName = nextGameSceneName;
        this.isFirstUpdate = true;
    }

    @Override
    public void initialize() {
        this.gameManager.inputManager.setSceneName(GameSceneName.LAUNCH);
    }

    @Override
    public void update() {
        if (this.nextGameSceneName == GameSceneName.PLAY) {
            Play play = new Play(this.gameManager);
            setTransitionFlag(true);
            setNextGameScene(play);

        } else {
            setGameEndFlag(true);
        }
    }

    @Override
    public void close() {

    }
}
