package ftginterface;

import de.stevenkuenzel.xml.XElement;
import de.stevenkuenzel.xml.XLoadable;
import de.stevenkuenzel.xml.XSavable;
import enumerate.Action;
import fighting.Attack;

public class StateAction implements XSavable, XLoadable<StateAction> {
    public Action action;

    public StateAction(Action action) {
        this.action = action;
    }

    public int distanceX, distanceY;
    public boolean success = false;

    // Attack specific.
    public Attack attack;
    public boolean destroyed = false;

    @Override
    public XElement toXElement() {
        XElement xElement = new XElement("StateAction");
        xElement.addAttribute("Action", action != null ? action.name() : "NULL");
        xElement.addAttribute("Success", success);
        xElement.addAttribute("dX", distanceX);
        xElement.addAttribute("dY", distanceY);

        return xElement;
    }


    private static final StateAction defaultAction = new StateAction(Action.NEUTRAL);

    public static StateAction loadFromXML(XElement xElement) {
        return defaultAction.fromXElement(xElement);
    }

    @Override
    public StateAction fromXElement(XElement xElement) {

        StateAction result = new StateAction(Action.valueOf(xElement.getAttributeValue("Action")));

        result.success = xElement.getAttributeValueAsBool("Success");
        result.distanceX = xElement.getAttributeValueAsInt("dX");
        result.distanceY = xElement.getAttributeValueAsInt("dY");

        return result;
    }
}
