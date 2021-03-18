package ftginterface;

import de.stevenkuenzel.xml.XElement;
import de.stevenkuenzel.xml.XLoadable;
import de.stevenkuenzel.xml.XSavable;
import enumerate.Action;
import fighting.Attack;
import org.jetbrains.annotations.NotNull;

/**
 * Stores information about an action and the game state.
 */
public class StateAction implements XSavable, XLoadable<StateAction> {
    /**
     * Creates a new instance of StateAction.
     * @param action The Action that has been started.
     */
    public StateAction(Action action) {
        this.action = action;
    }

    /**
     * The Action that has been started.
     */
    public Action action;

    /**
     * The distance to the opponent at the moment when the hitbox appears first.
     */
    public int distanceX, distanceY;

    /**
     * Flag that determines whether the attack has hit the opponent.
     */
    public boolean success = false;

    /**
     * Flag that determines whether the attack instance (FightingICE) has been destroyed.
     */
    public boolean destroyed = false;

    /**
     * Reference to the FightingICE attack instance.
     */
    public Attack attack;


    @Override
    public XElement toXElement() {
        XElement xElement = new XElement("StateAction");
        xElement.addAttribute("Action", action != null ? action.name() : "NULL");
        xElement.addAttribute("Success", success);
        xElement.addAttribute("dX", distanceX);
        xElement.addAttribute("dY", distanceY);

        return xElement;
    }


    /**
     * Placeholder element.
     */
    private static final StateAction defaultAction = new StateAction(Action.NEUTRAL);

    /**
     * Creates a new StateAction instance based on an XML-file.
     * @param xElement The XML-file's content.
     * @return The StateAction instance.
     */
    public static StateAction loadFromXML(XElement xElement) {
        return defaultAction.fromXElement(xElement);
    }

    @Override
    public StateAction fromXElement(XElement xElement, @NotNull Object... objects) {

        StateAction result = new StateAction(Action.valueOf(xElement.getAttributeValue("Action")));

        result.success = xElement.getAttributeValueAsBool("Success");
        result.distanceX = xElement.getAttributeValueAsInt("dX");
        result.distanceY = xElement.getAttributeValueAsInt("dY");

        return result;
    }
}
