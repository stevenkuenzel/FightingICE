package manager;

import fighting.Fighting;
import gamescene.GameScene;
import loader.ResourceLoader;
import util.CharacterDiskInformation;

import java.util.Random;

/*
TODO: Limit MCTS iterations.
 */

/**
 * 各ゲームシーンの初期化, 更新, 終了処理を管理するマネージャクラス．
 */
public abstract class GameManager {

	/**
	 * 現在のゲームシーン．<br>
	 * Menu, Launch, Play, Resultなどが入る．
	 *
	 * @see GameScene
	 */
	protected GameScene currentGameScene;

	/**
	 * ゲームの終了要求があったかどうかを表すフラグ．
	 */
	protected boolean isExitFlag;


	public String[] aiNames = { "KeyBoard", "KeyBoard" };
	public String[] characterNames = { "ZEN", "ZEN" };
	public InputManager inputManager;
	public ResourceLoader resourceLoader;
	public CharacterDiskInformation characterDiskInformation;


	public Fighting fighting;

	public int ROUND_FRAME_NUMBER = 3600;
	public int NUM_OF_ROUNDS = 3;



	// The Java RNG is known to be of poor quality (https://docs.oracle.com/javase/8/docs/api/java/util/Random.html). However, this is enough for its purpose within FightingICE.
	public Random random = new Random();
	public boolean randomInitialPositions = false;

	/**
	 * クラスコンストラクタ．<br>
	 * 現在のゲームシーンをnull，ゲームの終了要求がない状態(false)としてインスタンスの初期化を行う．
	 */
	public GameManager(CharacterDiskInformation characterDiskInformation, String ai1, String ai2) {

		this.aiNames[0] = ai1;
		this.aiNames[1] = ai2;

		this.resourceLoader = new ResourceLoader();
		this.inputManager = new InputManager(this);
		this.characterDiskInformation = characterDiskInformation;

		this.currentGameScene = null;
		this.isExitFlag = false;
	}

	/**
	 * 初期化処理の抽象メソッド．<br>
	 * GameManagerクラスを継承したクラスでオーバーライドして用いる．
	 */
	public abstract void initialize();

	/**
	 * 終了処理の抽象メソッド．<br>
	 * GameManagerクラスを継承したクラスでオーバーライドして用いる．
	 */
	public abstract void close();

	/**
	 * 現在のゲームシーンの更新を行う．<br>
	 * ゲーム終了判定が下されず，次のシーンへの遷移要求がない場合は，現在のゲームシーンの更新を行う．<br>
	 * ゲーム終了判定が下されず，次のシーンへの遷移要求がある場合は，現在のシーンの終了処理と遷移処理を行う．<br>
	 * ゲーム終了判定が下された場合は，ゲームの終了要求があったかどうかのフラグをtrueにする．
	 */
	public void update() {
		this.inputManager.update();

		if (!currentGameScene.isGameEnd()) {
			if (currentGameScene.isTransition()) {

				// 現在のシーンの終了処理
				currentGameScene.close();

				// 遷移先のシーンを現在のシーンにセットし,初期化処理を行う
				currentGameScene = currentGameScene.getNextGameScene();
				currentGameScene.initialize();
			}

			// 現在のシーンの更新
			currentGameScene.update();
		} else {
			this.isExitFlag = true;
		}
	}

	/**
	 * ゲームをスタートする．<br>
	 * 引数に指定したゲームシーンを現在のゲームシーンとして設定し，ゲームの開始処理を行う．
	 *
	 * @param startGameScene
	 *            開始させるゲームシーン
	 */
	public void startGame(GameScene startGameScene) {
		this.currentGameScene = startGameScene;
		this.currentGameScene.initialize();
	}

	/**
	 * ゲームの終了要求があったかどうかのフラグを返す．<br>
	 * GameManagerの終了処理の判断に用いられる．
	 *
	 * @return ゲームの終了要求があったかどうかのフラグ
	 */
	public boolean isExit() {
		return this.isExitFlag;
	}


	/**
	 * Sets the names of the two AI fighter characters.
	 * @param char1
	 * @param char2
	 */
	public void setCharacterNames(String char1, String char2)
	{
		characterNames[0] = char1;
		characterNames[1] = char2;
	}
}
