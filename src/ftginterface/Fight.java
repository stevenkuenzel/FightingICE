package ftginterface;

import aiinterface.AIInterface;
import core.Game;
import enumerate.Action;
import fighting.Attack;
import fighting.Character;
import gamescene.Play;
import manager.InputManager;
import util.CharacterDiskInformation;
import util.CharacterRoundData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fight {

    private static boolean isAttack(Action action) {
        for (Action attackAction : ATTACK_ACTIONS) {
            if (attackAction == action) return true;
        }

        return false;
    }

    private static Action[] ATTACK_ACTIONS = new Action[]
            {
                    Action.AIR_A,
                    Action.AIR_B,
                    Action.AIR_DA,
                    Action.AIR_DB,
                    Action.AIR_D_DB_BA,
                    Action.AIR_D_DB_BB,
                    Action.AIR_D_DF_FA,
                    Action.AIR_D_DF_FB,
                    Action.AIR_FA,
                    Action.AIR_FB,
                    Action.AIR_F_D_DFA,
                    Action.AIR_F_D_DFB,
                    Action.AIR_UA,
                    Action.AIR_UB,
                    Action.CROUCH_A,
                    Action.CROUCH_B,
                    Action.CROUCH_FA,
                    Action.CROUCH_FB,
                    Action.STAND_A,
                    Action.STAND_B,
                    Action.STAND_D_DB_BA,
                    Action.STAND_D_DB_BB,
                    Action.STAND_D_DF_FA,
                    Action.STAND_D_DF_FB,
                    Action.STAND_D_DF_FC,
                    Action.STAND_FA,
                    Action.STAND_FB,
                    Action.STAND_F_D_DFA,
                    Action.STAND_F_D_DFB,
                    Action.THROW_A,
                    Action.THROW_B
            };

    int rounds, framesPerRound;
    boolean randomInitialPositions;

    Player[] players = new Player[2];

    HashMap<String, AIInterface> toRegister = new HashMap<>();

    Game game;

    boolean isObserved;

    public Fight(int rounds, int framesPerRound, boolean randomInitialPositions, boolean isObserved) {
        this.rounds = rounds;
        this.framesPerRound = framesPerRound;
        this.randomInitialPositions = randomInitialPositions;
        this.isObserved = isObserved;
    }

    public void setPlayer(int index, String name, String character) {
//        if (index < 0 || index > 1) throw new Exception("Invalid player id.");

        players[index] = new Player(index, name, character);
    }

    public void setPlayer(int index, AIInterface ai, String character) {
        String name = ai.getClass().getName() + (index + 1);

        setPlayer(index, name, character);
        registerAIInterface(ai, name);
    }

    private void registerAIInterface(AIInterface ai, String name) {
        toRegister.put(name, ai);
    }

    public void initialize()  {
        if (players[0] == null || players[1] == null) {
//            throw new Exception("Players are not initialized. Use setPlayer(..) twice before initialize().");
        }

        game = new Game(players[0].name, players[1].name, new CharacterDiskInformation());
        game.setCharacterNames(players[0].character, players[1].character);
        game.randomInitialPositions = randomInitialPositions;
        game.NUM_OF_ROUNDS = rounds;
        game.ROUND_FRAME_NUMBER = framesPerRound;

        // Register all fighter instances.
        InputManager inputManager = game.inputManager;

        for (String key : toRegister.keySet()) {
            inputManager.registerAI(key, toRegister.get(key));
        }

        toRegister.clear();

        game.initialize();
    }

    Play play;
    fighting.Character[] characters;

    public void run() {

        while (!game.isExit()) {
            game.update();

            // Get references to the Play and Character instances.
            if (play == null && game.currentGameScene instanceof Play) {
                play = (Play) game.currentGameScene;
                characters = play.fighting.getCharacters();
            }

            // Observe the skills.
            if (isObserved && play != null) {
                observe(play.nowFrame == framesPerRound);
            }

            if (play != null && play.isGameEnd()) break;
        }

        game.close();


    }

    public FightObservation getObservation()
    {
        return new FightObservation(players[0].name , players[1].name, successData);
    }

    Action[] lastAction = new Action[]{null, null};
    StateAction[] stateActions = new StateAction[]{null, null};
    ArrayList<StateAction>[] pending = new ArrayList[]{new ArrayList<StateAction>(), new ArrayList<StateAction>()};
    HashMap<Action, ArrayList<StateAction>>[] successData = new HashMap[]{new HashMap<Action, ArrayList<StateAction>>(), new HashMap<Action, ArrayList<StateAction>>()};

    private void observe(boolean roundEnd) {
        observe(0, roundEnd);
        observe(1, roundEnd);

        if (roundEnd) {
            for (Character character : characters) {
                character.allAttacks.clear();
            }

            pending[0].clear();
            pending[1].clear();
        }
    }

    private void observe(int id, boolean roundEnd) {
        Character character = characters[id];
        Character opponent = characters[(id + 1) % 2];

        // 1. Find out, whether a new attack was started.
        if (!isRelationBetweenTwoActions(lastAction[id], character.getAction())) {

            // Do only capture attacks. In future, also movement or guarding actions may be captured.
            boolean captureAction = isAttack(character.getAction());

            if (captureAction) {
                // A new action was started.
//                int distanceX = Math.abs(character.getX() - opponent.getX());
//                int distanceY = character.getY() - opponent.getY();

//                boolean isLeft = character.getX() < opponent.getX();
//                boolean isTop = character.getY() < opponent.getY();

                stateActions[id] = new StateAction(character.getAction());
            }

            lastAction[id] = character.getAction();
        }

        // 2. Assign the attack, if an action was started previously.
        if (stateActions[id] != null) {
            StateAction current = stateActions[id];

            if (current.attack == null) {
                Attack attack = null;

                if (character.getAttack() != null && character.getAttack().id >= 0) {
                    attack = character.getAttack();
                } else if (character.projectileAttack != null) {
                    attack = character.projectileAttack;
                }

                if (attack != null) {
                    current.distanceX = Math.abs(character.getX() - opponent.getX());
                    current.distanceY = character.getY() - opponent.getY();
                    current.attack = attack;

                    pending[id].add(current);
                    stateActions[id] = null;
                }
            }
        }

        if (!roundEnd) return;

        // 3. Evaluate the success of all (pending) attacks. Only at round end.
        for (StateAction stateAction : pending[id]) {
            for (Attack attack : character.allAttacks) {
                assert stateAction.attack != null;

                if (stateAction.attack.id != attack.id) continue;

                // Only attacks that are destroyed may have hit the opponent.
                if (attack.destroyed()) {
                    stateAction.success = attack.hit();
                    stateAction.destroyed = true;

                    // Save the sample to the list of all successful samples for the action at hand.
                    ArrayList<StateAction> listOfSuccessfulSamples;

                    if (!successData[id].containsKey(stateAction.action))
                    {
                        listOfSuccessfulSamples = new ArrayList<>();
                        successData[id].put(stateAction.action, listOfSuccessfulSamples);
                    }
                    else
                    {
                        listOfSuccessfulSamples = successData[id].get(stateAction.action);
                    }

                    listOfSuccessfulSamples.add(stateAction);

                    // Free memory.
                    stateAction.attack = null;
                }

                break;
            }
        }
    }

    private boolean isRelationBetweenTwoActions(Action action1, Action action2) {
        if (action1 == null) return false;

        return (action1 == action2) ||
                (action1 == Action.STAND_GUARD && action2 == Action.STAND_GUARD_RECOV) ||
                (action1 == Action.CROUCH_GUARD && action2 == Action.CROUCH_GUARD_RECOV) ||
                (action1 == Action.AIR_GUARD && action2 == Action.AIR_GUARD_RECOV);
    }

    /**
     * Returns a list with two entries ([0] = p1, [1] = p2). Each is a list of round data ([0] = round 1, ...) in turn.
     *
     * @return List of round data.
     */
    public List<List<CharacterRoundData>> getRoundData() {
        List<List<CharacterRoundData>> data = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            data.add(new ArrayList<>(characters[i].allCharacterRoundData));
        }

        return data;
    }
}
