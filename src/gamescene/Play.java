package gamescene;

import enumerate.GameSceneName;
import fighting.Fighting;
import informationcontainer.RoundResult;
import input.KeyData;
import manager.GameManager;
import struct.FrameData;
import struct.GameData;

import java.util.ArrayList;

/*
TODO: See processingBreakTime()
 */

/**
 * 対戦中のシーンを扱うクラス．
 */
public class Play extends GameScene {

    /**
     * 対戦処理を行うクラスのインスタンス．
     */
    public Fighting fighting;
//    private Fighting fighting;

    /**
     * 現在のフレーム．
     */
    public int nowFrame;
//    private int nowFrame;

    /**
     * 各ラウンド前に行う初期化処理内における経過フレーム数．
     */
    private int elapsedBreakTime;

    /**
     * 現在のラウンド．
     */
    private int currentRound;

    /**
     * 各ラウンドの開始時かどうかを表すフラグ．
     */
    private boolean roundStartFlag;

    /**
     * 対戦処理後のキャラクターデータなどのゲーム情報を格納したフレームデータ．
     */
    private FrameData frameData;

    /**
     * 対戦処理に用いるP1, P2の入力情報．
     */
    private KeyData keyData;

    /**
     * 各ラウンド終了時のP1, P2の残り体力, 経過時間を格納するリスト．
     */
    private ArrayList<RoundResult> roundResults;


    private int endFrame;

    /**
     * Creates an instance of Play.
     *
     * @param gameManager
     */
    public Play(GameManager gameManager) {
        super(gameManager);

        // 以下4行の処理はgamesceneパッケージ内クラスのコンストラクタには必ず含める
        this.gameSceneName = GameSceneName.PLAY;
        this.isGameEndFlag = false;
        this.isTransitionFlag = false;
        this.nextGameScene = null;
        //////////////////////////////////////

    }

    @Override
    public void initialize() {
        this.gameManager.inputManager.setSceneName(GameSceneName.PLAY);

        this.fighting = new Fighting();
        this.fighting.initialize(this.gameManager);

        this.nowFrame = 0;
        this.elapsedBreakTime = 0;
        this.currentRound = 1;
        this.roundStartFlag = true;
        this.endFrame = -1;

        this.frameData = new FrameData();
        this.keyData = new KeyData();
        this.roundResults = new ArrayList<>();


        GameData gameData = new GameData(this.fighting.getCharacters(), this.gameManager);
        this.gameManager.inputManager.createAIcontroller();
        this.gameManager.inputManager.initializeAI(gameData);
    }

    @Override
    public void update() {
        if (this.currentRound <= this.gameManager.NUM_OF_ROUNDS) {
//		if (this.currentRound <= GameSetting.ROUND_MAX) {
            // ラウンド開始時に初期化
            if (this.roundStartFlag) {
                initRound();

            } else if (this.elapsedBreakTime < 1) { // Skips the break time.

//			} else if (this.elapsedBreakTime < GameSetting.BREAKTIME_FRAME_NUMBER) {
                // break time
                processingBreakTime();
                this.elapsedBreakTime++;

            } else {
                // processing
                processingGame();

                if (this.endFrame == -1) {
                    this.nowFrame++;
                } else if (this.endFrame % 30 == 0) {
                    this.nowFrame++;
                }
            }

        } else {
            this.isGameEndFlag = true;
        }
    }

    /**
     * 各ラウンド開始時に, 対戦情報や現在のフレームなどの初期化を行う．
     */
    private void initRound() {
        this.fighting.initRound();
        this.nowFrame = 0;
        this.roundStartFlag = false;
        this.elapsedBreakTime = 0;
        this.keyData = new KeyData();

        this.gameManager.inputManager.clear();
    }

    /**
     * 各ラウンド開始時における, インターバル処理を行う．
     */
    private void processingBreakTime() {

        this.gameManager.inputManager.setFrameData(new FrameData());

        // TODO: Why is that called every frame?
        this.fighting.initRound();
    }

    /**
     * 対戦処理を行う.<br>
     * <p>
     * 1. P1, P2の入力を受け取る.<br>
     * 2. 対戦処理を行う.<br>
     * 3. 対戦後のFrameDataを取得する.<br>
     * 4. リプレイファイルにログを出力する.<br>
     * 5. ゲーム画面を描画する.<br>
     * 6. 対戦後の画面情報(ScreenData)を取得する．<br>
     * 7. AIにFrameData及びScreenDataを渡す．<br>
     * 8. ラウンドが終了しているか判定する.<br>
     */
    private void processingGame() {
        if (this.endFrame != -1) {
            this.keyData = new KeyData();
            if (this.endFrame % 30 == 0) {
                this.fighting.processingFight(this.nowFrame, this.keyData);
            }
        } else {
            this.keyData = new KeyData(this.gameManager.inputManager.getKeyData());
            this.fighting.processingFight(this.nowFrame, this.keyData);
        }

        this.frameData = this.fighting.createFrameData(this.nowFrame, this.currentRound);


        this.gameManager.inputManager.setFrameData(this.frameData);
        this.gameManager.inputManager.processAI();

        if (isTimeOver()) {
//		if (isBeaten() || isTimeOver()) {
            processingRoundEnd();
        }
    }

    /**
     * ラウンド終了時の処理を行う.
     */
    private void processingRoundEnd() {
        this.endFrame = 0;
        this.fighting.processingRoundEnd(currentRound);
        RoundResult roundResult = new RoundResult(this.frameData);
        this.roundResults.add(roundResult);

        // AIに結果を渡す
        this.gameManager.inputManager.sendRoundResult(roundResult);
        this.currentRound++;
        this.roundStartFlag = true;
        this.endFrame = -1;

    }

//    /**
//     * キャラクターが倒されたかどうかを判定する.
//     *
//     * @return {@code true}: P1 or P2が倒された，{@code false}: otherwise
//     */
//    private boolean isBeaten() {
//        return FlagSetting.limitHpFlag
//                && (this.frameData.getCharacter(true).getHp() <= 0 || this.frameData.getCharacter(false).getHp() <= 0);
//    }

    /**
     * 1ラウンドの制限時間が経過したかどうかを判定する.<br>
     * Training modeのときは, Integerの最大との比較を行う.
     *
     * @return {@code true}: 1ラウンドの制限時間が経過した， {@code false}: otherwise
     */
    private boolean isTimeOver() {
        return this.nowFrame >= this.gameManager.ROUND_FRAME_NUMBER - 1;
//            return this.nowFrame >= GameSetting.ROUND_FRAME_NUMBER - 1;
    }


    @Override
    public void close() {
        this.fighting = null;
        this.frameData = null;
        this.keyData = null;
        // AIの実行を終了する
        this.gameManager.inputManager.closeAI();
        this.roundResults.clear();
    }
}
