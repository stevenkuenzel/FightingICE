package fighting;


/**
 * 攻撃とそれに対応するエフェクトの画像を管理する親クラス．
 */
public class Effect {

    /**
     * 1枚の画像を表示させる上限のフレーム数．
     */
    protected static final int FRAME_PER_IMAGE = 5;

    /**
     * The attack related to this effect.
     */
    protected Attack attack;

    /**
     * エフェクトが生成されてからの経過時間．
     */
    protected int currentFrame;

    /**
     * 1枚の画像を表示させるフレーム数．
     */
    protected int framesPerImage;

    /**
     * 引数として渡されたデータを用いてEffectクラスのインスタンスを生成するクラスコンストラクタ．
     *
     * @param attack         Attackクラスのインスタンス
     * @param framesPerImage 1枚のエフェクト画像の表示フレーム数
     */
    public Effect(Attack attack, int framesPerImage) {
        this.attack = attack;
        this.currentFrame = 0;
        this.framesPerImage = framesPerImage;
    }

    /**
     * 引数として渡されたデータを用いてEffectクラスのインスタンスを生成するクラスコンストラクタ．
     *
     * @param attack Attackクラスのインスタンス
     */
    public Effect(Attack attack) {
        this(attack, FRAME_PER_IMAGE);
    }

    /**
     * Updates the effect's state.
     *
     * @return {@code true} if the elapsed time since the effect was generated
     * has not exceeded the time for displaying the effect,
     * {@code false} otherwise
     */
    public boolean update() {
        return false;
    }

    /**
     * Returns the attack related to this effect.
     *
     * @return the attack related to this effect
     */
    public Attack getAttack() {
        return this.attack;
    }
}
