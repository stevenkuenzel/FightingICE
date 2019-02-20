package gamescene;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import enumerate.GameSceneName;
import loader.ResourceLoader;
import manager.GraphicManager;
import manager.InputManager;
import setting.FlagSetting;
import setting.GameSetting;
import setting.LaunchSetting;
import streaming.StreamingSetting;

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
	 * クラスコンストラクタ．
	 */
	public Launcher() {
		// 以下4行の処理はgamesceneパッケージ内クラスのコンストラクタには必ず含める
		this.gameSceneName = GameSceneName.LAUNCH;
		this.isGameEndFlag = false;
		this.isTransitionFlag = false;
		this.nextGameScene = null;
		//////////////////////////////////////

		this.nextGameSceneName = null;

	}

	/**
	 * Launcherシーンを初期化し, 次の遷移先のゲームシーンを設定するクラスコンストラクタ．
	 *
	 * @param nextGameSceneName
	 *            次の遷移先のゲームシーン名
	 */
	public Launcher(GameSceneName nextGameSceneName) {
		super();

		this.nextGameSceneName = nextGameSceneName;
		this.isFirstUpdate = true;
	}

	@Override
	public void initialize() {
		InputManager.getInstance().setSceneName(GameSceneName.LAUNCH);
	}

	@Override
	public void update() {
		if (this.isFirstUpdate && FlagSetting.enableWindow) {
			GraphicManager.getInstance().drawString("Now loading ...", GameSetting.STAGE_WIDTH / 2 - 80, 200);
			this.isFirstUpdate = false;

		} else {
			switch (this.nextGameSceneName.name()) {
			case "PLAY":
				Logger.getAnonymousLogger().log(Level.INFO, "Transition to PLAY");
				Play play = new Play();
				this.setTransitionFlag(true);
				this.setNextGameScene(play);
				break;

			case "REPLAY":
				Logger.getAnonymousLogger().log(Level.WARNING, "Transition to REPLAY");
				Replay replay = new Replay();
				this.setTransitionFlag(true);
				this.setNextGameScene(replay);
				break;
			case "STREAMING":	
				Logger.getAnonymousLogger().log(Level.INFO, "Transition to PLAY for Streaming");
				setCharacterName();
				Play streamng = new Play();
				this.setTransitionFlag(true);
				this.setNextGameScene(streamng);
				break;
			default:
				Logger.getAnonymousLogger().log(Level.WARNING, "This scene does not exist");
				this.setGameEndFlag(true);
			}

			if (FlagSetting.enableWindow) {
				// Loads resources
				ResourceLoader.getInstance().loadResource();
			}
		}
	}
	
	//StreamingModeでのキャラクター変更 
	private void setCharacterName(){
		Random rnd = new Random();
		if(FlagSetting.charaSelectionMode==0){
			int[] temp = new int[GameSetting.NORMAL_CHARACTERS.length];
			for(int i = 0;i<temp.length;i++){
				temp[i] = StreamingSetting.nextChara[i];
			}
			StreamingSetting.resetStatistics();
			int sum = temp[0]+temp[1]+temp[2];
			
			int[] p = {0,0};
			if(sum!=0){
				Logger.getAnonymousLogger().log(Level.INFO, "Character is slected by voting");
				for (int j = 0; j < 2; j++) {
					double target = rnd.nextDouble();
					int i = -1;
					while (target >= 0) {
						i++;
						target -= (double) temp[i] / (double) sum;
					}
					p[j] = i;
				}
			}else{
				Logger.getAnonymousLogger().log(Level.INFO, "Character is slected by Random");
				p[0]=rnd.nextInt(GameSetting.NORMAL_CHARACTERS.length);
				p[1]=rnd.nextInt(GameSetting.NORMAL_CHARACTERS.length);
			}
			if(p[0]==p[1]){
				if(p[0]==2){
					p[1]-=(rnd.nextInt(1)+1);
				}else{
					p[1]+=3;
				}
			}
			LaunchSetting.characterNames[0]=GameSetting.CHARACTERS[Math.max(0, p[0])];
			LaunchSetting.characterNames[1]=GameSetting.CHARACTERS[Math.max(0, p[1])];
		}else if(FlagSetting.charaSelectionMode==1){
			int p=2;
			if(LaunchSetting.characterNames[0].equals("ZEN")){
				p=0;
			}else if(LaunchSetting.characterNames[0].equals("GARNET")){
				p=1;
			}
			p=(p+rnd.nextInt(2)+1)%3;
			LaunchSetting.characterNames[0]=GameSetting.CHARACTERS[p];
			LaunchSetting.characterNames[1]=GameSetting.CHARACTERS[p+3];
		}else if(FlagSetting.charaSelectionMode==3){
			//ZvZ or GvG
			if(LaunchSetting.characterNames[0].equals("ZEN")){
				LaunchSetting.characterNames[0]=GameSetting.CHARACTERS[1];
				LaunchSetting.characterNames[1]=GameSetting.CHARACTERS[1];
			}else{
				LaunchSetting.characterNames[0]=GameSetting.CHARACTERS[0];
				LaunchSetting.characterNames[1]=GameSetting.CHARACTERS[0];
			}
		}else if(FlagSetting.charaSelectionMode==4){
			//ZvG or GvZ
			if(LaunchSetting.characterNames[0].equals("ZEN")){
				LaunchSetting.characterNames[0]=GameSetting.CHARACTERS[1];
				LaunchSetting.characterNames[1]=GameSetting.CHARACTERS[0];
			}else{
				LaunchSetting.characterNames[0]=GameSetting.CHARACTERS[0];
				LaunchSetting.characterNames[1]=GameSetting.CHARACTERS[1];
			}
		}else{
			int[] p = {0,0};
			p[0]=rnd.nextInt(GameSetting.NORMAL_CHARACTERS.length);
			p[1]=(p[0]+rnd.nextInt(2)+1)%3;
			LaunchSetting.characterNames[0]=GameSetting.CHARACTERS[p[0]];
			LaunchSetting.characterNames[1]=GameSetting.CHARACTERS[p[1]];
		}
		
	}
	
	@Override
	public void close() {

	}
}
