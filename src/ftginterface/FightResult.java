package ftginterface;

import util.CharacterRoundData;

/**
 * Stores the result of a fight.
 */
public class FightResult {
    /**
     * Stores the round results for both characters. Structure [Character ID: 0, 1][Round number: 0, .., N - 1].
     */
    public CharacterRoundData[][] result;

    /**
     * Stores the observation results.
     */
    public FightObservation observation;


    /**
     * Creates a new instance of FightResult.
     */
    public FightResult(CharacterRoundData[][] result, FightObservation observation)
    {
        this.result = result;
        this.observation = observation;
    }
}
