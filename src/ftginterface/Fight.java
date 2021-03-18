package ftginterface;

import aiinterface.AIInterface;
import core.Game;
import enumerate.Action;
import fighting.Attack;
import fighting.Character;
import ftginterface.skills.SkillLoader;
import gamescene.Play;
import manager.InputManager;
import util.CharacterDiskInformation;
import util.CharacterRoundData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates a fight in FightingICE.
 */
public class Fight {

    /**
     * Returns true, if an action is of type 'Attack'.
     *
     * @param action The action to check.
     * @return True, if the action is an attack.
     */
    private static boolean isAttack(Action action) {
        for (Action attackAction : ATTACK_ACTIONS) {
            if (attackAction == action) return true;
        }

        return false;
    }

    /**
     * Set of attack actions.
     */
    private static final Action[] ATTACK_ACTIONS = new Action[]{
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

    /**
     * Number of rounds.
     */
    int rounds;

    /**
     * Number of frames per round.
     */
    int framesPerRound;

    /**
     * Start with random initial positions?
     */
    boolean randomInitialPositions;

    /**
     * Create a FightObservation instance for this fight?
     */
    boolean isObserved;

    /**
     * Path of the FightingICE environment.
     */
    String fightingICEroot;

    /**
     * Player instances.
     */
    Player[] players = new Player[2];

    /**
     * Map, that stores the AI controllers to register with a given name.
     */
    HashMap<String, AIInterface> toRegister = new HashMap<>();


    /**
     * Game instance.
     */
    Game game;

    /**
     * Play scene instance.
     */
    Play play;

    /**
     * Character instances.
     */
    fighting.Character[] characters;

    /**
     * Last executed action.
     */
    Action[] lastAction = new Action[]{null, null};

    /**
     * Open StateAction (waits for an attack instance to be assigned).
     */
    StateAction[] stateActions = new StateAction[]{null, null};

    /**
     * List of StateActions that were not evaluated (success) yet. This typically happens at the end of the round.
     */
    ArrayList<StateAction>[] pending = new ArrayList[]{new ArrayList<StateAction>(), new ArrayList<StateAction>()};

    /**
     * Stores all StateAction instances for every action called.
     */
    HashMap<Action, ArrayList<StateAction>>[] successData = new HashMap[]{new HashMap<Action, ArrayList<StateAction>>(), new HashMap<Action, ArrayList<StateAction>>()};

    /**
     * Creates an instance of Fight.
     *
     * @param rounds                 The number of rounds. Default = 3.
     * @param framesPerRound         The number of frames per round. Default = 3,600.
     * @param randomInitialPositions If true, the characters start at random positions to add uncertainty to the fight.
     * @param isObserved             If true, information about all attack skills (starting location, success) is captured and provided after the fight.
     * @param fightingICEroot        The directory where the 'data' folder of FightingICE is located. Default = ".", if data is located in the working directory of the application.
     */
    public Fight(int rounds, int framesPerRound, boolean randomInitialPositions, boolean isObserved, String fightingICEroot) {
        this.rounds = rounds;
        this.framesPerRound = framesPerRound;
        this.randomInitialPositions = randomInitialPositions;
        this.isObserved = isObserved;
        this.fightingICEroot = fightingICEroot;

        if (!SkillLoader.isInitialized) {
            SkillLoader.initialize(fightingICEroot);
        }
    }

    /**
     * Determines a player.
     * @param index The player index (0 or 1).
     * @param name The player name (name of the AI agent, e.g. ANNBot).
     * @param character The character name (ZEN, LUD, GARNET).
     */
    public void setPlayer(int index, String name, String character) {
        players[index] = new Player(index, name, character);
    }

    /**
     * Determines a player.
     * @param index The player index (0 or 1).
     * @param ai The AI agent instance.
     * @param character The character name (ZEN, LUD, GARNET).
     */
    public void setPlayer(int index, AIInterface ai, String character) {
        String aiName = ai.getClass().getName();//+ (index + 1);

        if (aiName.contains(".")) {
            String[] split = aiName.split("\\.");
            aiName = split[split.length - 1];
        }

        aiName += "" + (index + 1);

        setPlayer(index, ai, aiName, character);
    }

    /**
     * Determines a player. Used if the same AI agent is registered twice (with different names).
     * @param index The player index (0 or 1).
     * @param ai The AI agent instance.
     * @param aiName The player name (name of the AI agent, e.g. ANNBot).
     * @param character The character name (ZEN, LUD, GARNET).
     */
    public void setPlayer(int index, AIInterface ai, String aiName, String character) {
        setPlayer(index, aiName, character);
        registerAIInterface(ai, aiName);
    }

    /**
     * Adds an AI agent to the registration queue.
     * @param ai The AI agent.
     * @param name The name it is registered with.
     */
    private void registerAIInterface(AIInterface ai, String name) {
        toRegister.put(name, ai);
    }

    /**
     * Initializes the fight.
     */
    private void initialize() {
//        if (players[0] == null || players[1] == null) {
//            throw new Exception("Players are not initialized. Use setPlayer(..) twice before initialize().");
//        }

        // Create the Game instance and take the necessary settings.
        game = new Game(fightingICEroot, players[0].name, players[1].name, new CharacterDiskInformation(fightingICEroot));
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

        // Call the internal initalization.
        game.initialize();
    }


    /**
     * Runs the fight until termination.
     * @return The result of the fight.
     */
    public FightResult run() {
        initialize();

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

        return new FightResult(getRoundData(), isObserved ? getObservation() : null);
    }

    /**
     * Returns the observation of the fight, if recorded.
     */
    private FightObservation getObservation() {
        return new FightObservation(players[0].name, players[1].name, successData);
    }

    /**
     * Observes the current frame.
     * @param roundEnd Is the current round finished?
     */
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

    /**
     * Observes the character with the given id in the current frame.
     * @param id The character id (0 or 1).
     * @param roundEnd Is the current round finished?
     */
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

                    if (!successData[id].containsKey(stateAction.action)) {
                        listOfSuccessfulSamples = new ArrayList<>();
                        successData[id].put(stateAction.action, listOfSuccessfulSamples);
                    } else {
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

    /**
     * Returns true, if two succeeding actions are somehow related to the other.
     * @param action1 The first action.
     * @param action2 The second (following) action.
     */
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
    private CharacterRoundData[][] getRoundData() {
        CharacterRoundData[][] data = new CharacterRoundData[2][rounds];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < characters[i].allCharacterRoundData.size(); j++) {
                data[i][j] = characters[i].allCharacterRoundData.get(j);
            }
        }

        return data;
    }
}
