package ftginterface;

import de.sk.xml.XElement;
import de.sk.xml.XSavable;
import enumerate.Action;
import fighting.Attack;

public class StateAction implements XSavable {
    public Action action;

    public StateAction(Action action)
    {
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
}
