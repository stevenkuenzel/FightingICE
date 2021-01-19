package util;

public class CharacterRoundData {

    /**
     * The round number.
     */
    public int round = -1;

    /**
     *  Remaining HP, total number of received and consumed EP.
     */
    public int remainingHP;
    public int totalEP;
    public int consumedEP;

    /**
     * Counters for started, suffered and blocked close attacks and projectile.
     *
     * offensive: Attacks that were started by the player.
     * defensive: Attacks that were started by the opponent.
     *
     * Example: offensiveAttacksBlocked = Number of attacks that were started by the player and blocked by the opponent.
     */
    public int offensiveAttacksStarted;
    public int offensiveProjectilesStarted;

    /**
     *
     */
    public int offensiveProjectilesHit;

    public int offensiveProjectilesBlocked;

    public int offensiveAttacksHit;

    public int offensiveAttacksBlocked;

    /**
     * Number of opponent projectiles suffered.
     */
    public int defensiveProjectilesHit;

    public int defensiveProjectilesBlocked;

    /**
     * Number of opponent attacks suffered.
     */
    public int defensiveAttacksHit;

    public int defensiveAttacksBlocked;


    // Movement.
    public int totalAccelerationX;
    public int totalAccelerationY;
}
