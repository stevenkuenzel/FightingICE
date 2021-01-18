package fighting;

/**
 * 攻撃が当たったときに表示するエフェクト及びアッパー攻撃のエフェクトを管理するクラス.<br>
 * Effectクラスを継承している.
 */
public class HitEffect extends Effect {

	/**
	 * The boolean value whether the attack conducted by the character hits the
	 * opponent or not.
	 */
	private boolean isHit;

	/**
	 * The horizontal variation of the display position of this effect.
	 */
	private int variationX;

	/**
	 * The vertical variation of the display position of this effect.
	 */
	private int variationY;

	/**
	 * 指定されたデータでHitEffectのインスタンスを作成するコンストラクタ．
	 *
	 * @param attack
	 *            攻撃オブジェクト
	 * @param isHit
	 *            the boolean value whether the attack conducted by the
	 *            character hits the opponent or not
	 *
	 * @param variation
	 *            エフェクト画像の表示位置をvariationX, variationYに従って変動させるかどうか
	 * @param framesPerImage
	 *            1枚のエフェクト画像の表示フレーム数
	 */
	public HitEffect(Attack attack, boolean isHit, boolean variation, int framesPerImage) {
		super(attack, framesPerImage);
		this.initialize(isHit, variation);
	}

	/**
	 * 指定されたデータでHitEffectのインスタンスを作成するコンストラクタ．
	 *
	 * @param attack
	 *            攻撃オブジェクト
	 * @param isHit
	 *            the boolean value whether the attack conducted by the
	 *            character hits the opponent or not
	 *
	 * @param variation
	 *            エフェクト画像の表示位置をvariationX, variationYに従って変動させるかどうか
	 */
	public HitEffect(Attack attack, boolean isHit, boolean variation) {
		super(attack);
		this.initialize(isHit, variation);
	}

	/**
	 * 指定されたデータでHitEffectのインスタンスを作成するコンストラクタ．
	 *
	 * @param attack
	 *            攻撃オブジェクト
	 * @param isHit
	 *            the boolean value whether the attack conducted by the
	 *            character hits the opponent or not
	 */
	public HitEffect(Attack attack,  boolean isHit) {
		this(attack, isHit, true);
	}

	/**
	 * Initializes the hit effect.
	 *
	 * @param isHit
	 *            the boolean value whether the attack conducted by the
	 *            character hits the opponent or not
	 * @param variation
	 *            エフェクト画像の表示位置をvariationX, variationYに従って変動させるかどうか
	 */
	private void initialize(boolean isHit, boolean variation) {
		this.isHit = isHit;
		this.variationX = variation ? (int) (Math.random() * 30) - 15 : 0;
		this.variationY = variation ? (int) (Math.random() * 30) - 15 : 0;
	}

	/**
	 * Returns the boolean value whether the attack conducted by the character
	 * hits the opponent or not.
	 *
	 * @return the boolean value whether the attack conducted by the character
	 *         hits the opponent or not
	 */
	public boolean isHit() {
		return this.isHit;
	}

	/**
	 * Returns the horizontal variation of the display position of this effect.
	 *
	 * @return the horizontal variation of the display position of this effect
	 */
	public int getVariationX() {
		return this.variationX;
	}

	/**
	 * Returns the vertical variation of the display position of this effect.
	 *
	 * @return the vertical variation of the display position of this effect
	 */
	public int getVariationY() {
		return this.variationY;
	}

}
