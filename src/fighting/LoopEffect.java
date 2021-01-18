package fighting;

/**
 * 波動拳のエフェクトを管理するクラス.<br>
 * Effectクラスを継承している.
 */
public class LoopEffect extends Effect {

	/**
	 * 指定されたデータでLoopEffectのインスタンスを作成するクラスコンストラクタ．
	 *
	 * @param attack
	 *            攻撃オブジェクト
	 */
	public LoopEffect(Attack attack) {
		super(attack);
	}

	/**
	 * Updates the effect's state.<br>
	 * If effect display time has elapsed, set the elapsed frame to 0;
	 *
	 * @return {@code true}
	 */
	public boolean update() {
		if (!super.update()) {
			this.currentFrame = 0;
		}

		return true;
	}

}
