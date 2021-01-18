package informationcontainer;

import struct.FrameData;

/**
 * ラウンドの結果を処理するクラス．
 */
public class RoundResult {

	/**
	 * 現在のラウンド数．
	 */
	private int currentRound;

	/**
	 * P1とP2の残りHPを格納する配列．
	 */
	private int[] remainingHPs;

	/**
	 * ラウンドの経過フレーム数．
	 */
	private int elapsedFrame;

	/**
	 * クラスコンストラクタ．
	 */
	public RoundResult() {
		this.currentRound = -1;
		this.remainingHPs = new int[2];
		this.elapsedFrame = -1;
	}

	/**
	 * 指定された値でRoundResultを更新するクラスコンストラクタ．
	 *
	 * @param round
	 *            ラウンド数
	 * @param hp
	 *            P1,P2の残りHP
	 * @param frame
	 *            経過フレーム数
	 */
	public RoundResult(int round, int[] hp, int frame) {
		this.currentRound = round;
		this.remainingHPs = hp;
		this.elapsedFrame = frame;
	}

	/**
	 * 引数として渡されたフレームデータから結果に関する情報を取得し，RoundResultを更新するクラスコンストラクタ．
	 *
	 * @param frameData
	 *            フレーム内のゲームデータ
	 */
	public RoundResult(FrameData frameData) {
		this.currentRound = frameData.getRound();
		this.elapsedFrame = frameData.getFramesNumber() + 1;
		this.remainingHPs = new int[] { frameData.getCharacter(true).getHp(), frameData.getCharacter(false).getHp() };
	}

	/**
	 * 現在のラウンド数を返す．
	 *
	 * @return 現在のラウンド数
	 */
	public int getRound() {
		return this.currentRound;
	}

	/**
	 * P1,P2の残りHPを格納した配列を返す．
	 *
	 * @return P1,P2の残りHPを格納した配列
	 */
	public int[] getRemainingHPs() {
		return this.remainingHPs.clone();
	}

	/**
	 * 経過フレーム数を返す．
	 *
	 * @return 経過フレーム数
	 */
	public int getElapsedFrame() {
		return this.elapsedFrame;
	}
}
