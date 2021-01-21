package ftginterface;

import enumerate.Action;
import fighting.Attack;

public class StateAction {
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
}
