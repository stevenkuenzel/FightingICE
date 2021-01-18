package manager;

import aiinterface.AIController;
import aiinterface.AIInterface;
import enumerate.GameSceneName;
import informationcontainer.RoundResult;
import input.KeyData;
import struct.FrameData;
import struct.GameData;
import struct.Key;

import java.util.HashMap;

/**
 * AIやキーボード等の入力関連のタスクを管理するマネージャークラス．
 */
public class InputManager {

    /**
     * キー入力を格納するバッファ．
     */
    private KeyData buffer;


    /**
     * AIコントローラを格納する配列．
     */
    private AIController[] ais;

    /**
     * ゲームのシーン名．
     */
    private GameSceneName sceneName;

    /**
     * Python側で定義されたAI名とAIInterfaceをセットで管理するマップ.
     */
    private HashMap<String, AIInterface> predifinedAIs;

    /**
     * Default number of devices.
     */
    private final static int DEFAULT_DEVICE_NUMBER = 2;


    public GameManager gameManager;


    public InputManager(GameManager gameManager) {
        this.gameManager = gameManager;
        sceneName = GameSceneName.HOME_MENU;
        this.predifinedAIs = new HashMap<>();
    }


    /**
     * Pythonでの処理のために用意されたAI名とAIインタフェースをマップに追加する．
     *
     * @param name AI名
     * @param ai   AIインタフェース
     */
    public void registerAI(String name, AIInterface ai) {
        this.predifinedAIs.put(name, ai);
    }

    /**
     * 毎フレーム実行され，キーボード入力及びAIの入力情報を取得する．
     */
    public void update() {
        Key[] keys = new Key[DEFAULT_DEVICE_NUMBER];

        if (this.ais != null) {
            for (int i = 0; i < keys.length; i++) {

                keys[i] = getKeyFromAI(this.ais[i]);
            }
        }

        this.setKeyData(new KeyData(keys));
    }


    /**
     * AIの情報を格納したコントローラをInputManagerクラスに取り込む．
     */
    public void createAIcontroller() {
        String[] aiNames = this.gameManager.aiNames.clone();


        this.ais = new AIController[DEFAULT_DEVICE_NUMBER];

        for (int i = 0; i < 2; i++) {
            if (this.predifinedAIs.containsKey(aiNames[i])) {
                this.ais[i] = new AIController(this.predifinedAIs.get(aiNames[i]));
            } else {
                this.ais[i] = this.gameManager.resourceLoader.loadAI(aiNames[i]);
            }

        }
    }

    /**
     * Initializes the AI fighters.
     */
    public void initializeAI(GameData gameData) {
        for (int i = 0; i < this.ais.length; i++) {
            if (this.ais[i] != null) {
                this.ais[i].initialize(gameData, i == 0);
            }
        }
    }

    /**
     * Processes the AI fighters.
     */
    public void processAI() {
        for (AIController ai : this.ais) {
            ai.process();
        }
    }

    /**
     * Closes the AI fighters.
     */
    public void closeAI() {
        this.buffer = new KeyData();

        for (AIController ai : this.ais) {
            if (ai != null)
                ai.gameEnd();
        }
        this.ais = null;
    }

    /**
     * AIのキー入力を取得する．
     *
     * @param ai AIの情報を格納したコントローラ
     * @return AIのキー入力．
     * @see AIController
     * @see Key
     */
    private Key getKeyFromAI(AIController ai) {
        if (ai == null)
            return new Key();
        return new Key(ai.getInput());
    }

    /**
     * Provides the current (or delayed) FrameData to the AI fighters.
     * @param frameData
     */
       public void setFrameData(FrameData frameData) {
        for (AIController ai : this.ais) {
            if (ai != null) {
                if (!frameData.getEmptyFlag()) {
                    ai.setFrameData(new FrameData(frameData));
                } else {
                    ai.setFrameData(new FrameData());
                }
            }
        }
    }

    /**
     * AIコントローラに現在のラウンドの結果を送信する．
     *
     * @param roundResult 現在のラウンドの結果
     * @see RoundResult
     */
    public void sendRoundResult(RoundResult roundResult) {
        for (AIController ai : this.ais) {
            if (ai != null) {
                ai.informRoundResult(roundResult);
            }
        }
    }

    /**
     * 各AIコントローラ内に保持されているフレームデータをクリアする.
     */
    public void clear() {
        for (AIController ai : this.ais) {
            if (ai != null) {
                ai.clear();
            }
        }
    }

    /**
     * 入力されたキーを格納しているキーバッファを取得する．
     *
     * @return キーバッファ
     */
    public KeyData getKeyData() {
        return this.buffer;
    }

    /**
     * 入力されたキーをバッファにセットする．
     *
     * @param data 入力キーデータ
     */
    public void setKeyData(KeyData data) {
        this.buffer = data;
    }

    /**
     * シーン名を取得する．
     *
     * @return 現在のシーン名
     */
    public GameSceneName getSceneName() {
        return this.sceneName;
    }

    /**
     * 引数のシーン名をフィールド変数にセットする．
     *
     * @param sceneName シーン名
     */
    public void setSceneName(GameSceneName sceneName) {
        this.sceneName = sceneName;
    }

}
