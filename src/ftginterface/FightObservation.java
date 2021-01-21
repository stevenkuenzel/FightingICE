package ftginterface;

import enumerate.Action;

import java.util.ArrayList;
import java.util.HashMap;

public class FightObservation {
    String name1, name2;
    HashMap<Action, ArrayList<StateAction>> p1Success, p2Success;

    public FightObservation(String name1, String name2,  HashMap<Action, ArrayList<StateAction>>[] successData)
    {
        this.name1 = name1;
        this.name2 = name2;
        this.p1Success = successData[0];
        this.p2Success = successData[1];
    }

    /**
     * Merges this instance of observations with another instance.
     * @param other The instance to merge with.
     */
    public void merge(FightObservation other)
    {
        if (!other.name2.equals(name2))
        {
            name2 += "," + other.name2;
        }

        mergeSamples(p1Success, other.p1Success);
        mergeSamples(p2Success, other.p2Success);
    }

    /**
     * Merges to maps of samples.
     * @param mine Samples of this instance.
     * @param others Samples of the instance to merge with.
     */
    private void mergeSamples(HashMap<Action, ArrayList<StateAction>> mine, HashMap<Action, ArrayList<StateAction>> others)
    {
        for (Action key : others.keySet()) {
            ArrayList<StateAction> successfulSamples;

            if (!mine.containsKey(key))
            {
                successfulSamples = new ArrayList<>();
                mine.put(key, successfulSamples);
            }
            else
            {
                successfulSamples = mine.get(key);
            }

            successfulSamples.addAll(others.get(key));
        }
    }

    public void export()
    {
        // TODO.
    }
}
