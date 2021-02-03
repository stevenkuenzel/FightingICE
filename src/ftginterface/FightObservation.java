package ftginterface;

import de.sk.util.PathUtil;
import de.sk.xml.XAttribute;
import de.sk.xml.XElement;
import enumerate.Action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FightObservation {
    String name1, name2;
    HashMap<Action, ArrayList<StateAction>> p1Success, p2Success;

    public FightObservation(String name1, String name2, HashMap<Action, ArrayList<StateAction>>[] successData) {
        this.name1 = name1;
        this.name2 = name2;
        this.p1Success = successData[0];
        this.p2Success = successData[1];
    }

    /**
     * Merges this instance of observations with another instance.
     *
     * @param other The instance to merge with.
     */
    public void merge(FightObservation other) {
        if (!other.name2.equals(name2)) {
            name2 += "," + other.name2;
        }

        mergeSamples(p1Success, other.p1Success);
        mergeSamples(p2Success, other.p2Success);
    }

    /**
     * Merges to maps of samples.
     *
     * @param mine   Samples of this instance.
     * @param others Samples of the instance to merge with.
     */
    private void mergeSamples(HashMap<Action, ArrayList<StateAction>> mine, HashMap<Action, ArrayList<StateAction>> others) {
        for (Action key : others.keySet()) {
            ArrayList<StateAction> successfulSamples;

            if (!mine.containsKey(key)) {
                successfulSamples = new ArrayList<>();
                mine.put(key, successfulSamples);
            } else {
                successfulSamples = mine.get(key);
            }

            successfulSamples.addAll(others.get(key));
        }
    }

    public XElement export(int id) {

        if (p1Success != null && id == 0) return export(name1, name2, p1Success);
        if (p2Success != null && id == 1) return export(name2, name1, p2Success);

        return null;
    }

    private XElement export(String controllerName, String opponentName, HashMap<Action, ArrayList<StateAction>> samples) {
        ArrayList<Action> sortedKeys = new ArrayList<>(samples.keySet());
        sortedKeys.sort(Comparator.comparingInt(Enum::ordinal));


//        // 1. Export the data ??
//        XElement xController = new XElement("Controller", new XAttribute("Name", controllerName), new XAttribute("Opponent", opponentName));
//
//        XElement xActions = xController.addChild("Actions");
//
//        for (Action action : sortedKeys) {
//            List<StateAction> success = samples.get(action).stream().filter(x -> x.success).collect(Collectors.toList());
//
//
//            if (!success.isEmpty()) {
//                int n = samples.get(action).size();
//                int sr = (int) (100d * (double) success.size() / (double) n);
//
//                xActions.addChild("Action", new XAttribute("Name", action.name()), new XAttribute("Success", sr), new XAttribute("N", n));
//            }
//        }
//
//        xController.save(PathUtil.Companion.getOutputDir() + "Info_" + controllerName + "_" + opponentName + ".xml");


        // 2. Export the raw information for postprocessing and reuse.

        XElement xRaw = new XElement("StateActions", new XAttribute("Name", controllerName), new XAttribute("Opponent", opponentName));

        for (Action action : sortedKeys) {
            ArrayList<StateAction> data = samples.get(action);

            XElement xAction = xRaw.addChild("Action", new XAttribute("Name", action.name()));

            for (StateAction stateAction : data) {
                xAction.addChild(stateAction.toXElement());
            }
        }

        return xRaw;

//        xRaw.save(PathUtil.Companion.getOutputDir() + "Raw_" + controllerName + "_" + opponentName + ".xml");
    }
}
