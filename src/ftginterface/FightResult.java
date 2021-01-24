package ftginterface;

import util.CharacterRoundData;

public class FightResult {
    public CharacterRoundData[][] result;
    public FightObservation observation;


    public FightResult(CharacterRoundData[][] result, FightObservation observation)
    {
        this.result = result;
        this.observation = observation;
    }
}
