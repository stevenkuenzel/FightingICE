package aitest;

import aiinterface.AIInterface;
import aiinterface.CommandCenter;
import enumerate.Action;
import enumerate.State;
import simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.Key;

import java.util.*;

public class Xai implements AIInterface {
    GameData gameData;
    boolean playerNumber;
    FrameData frameData;
    boolean isControl;
    Simulator simulator;
    CommandCenter commandCenter;
    private Key key;

    Random random = new Random();

    Action[] actionAir = new Action[]{Action.AIR_GUARD, Action.AIR_A, Action.AIR_B, Action.AIR_DA, Action.AIR_DB,
            Action.AIR_FA, Action.AIR_FB, Action.AIR_UA, Action.AIR_UB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB,
            Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB};
    Action[] actionGround = new Action[]{Action.STAND_D_DB_BA, Action.BACK_STEP, Action.FORWARD_WALK, Action.DASH,
            Action.JUMP, Action.FOR_JUMP, Action.BACK_JUMP, Action.STAND_GUARD, Action.CROUCH_GUARD, Action.THROW_A,
            Action.THROW_B, Action.STAND_A, Action.STAND_B, Action.CROUCH_A, Action.CROUCH_B, Action.STAND_FA,
            Action.STAND_FB, Action.CROUCH_FA, Action.CROUCH_FB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB,
            Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_D_DB_BB};

    @Override
    public int initialize(GameData gd, boolean playerNumber) {
        this.gameData = gd;
        this.playerNumber = playerNumber;
        this.simulator = gd.getSimulator();
        this.commandCenter = new CommandCenter();
        this.key = new Key();

        return 0;
    }

    @Override
    public void getInformation(FrameData fd, boolean isControl) {
        this.frameData = fd;
        this.isControl = isControl;
        this.commandCenter.setFrameData(this.frameData, playerNumber);
    }

    @Override
    public void getInformation(FrameData fd) {
        getInformation(fd, true);
    }

    @Override
    public void processing() {
        if (canProcessing()) {


            if (commandCenter.getSkillFlag()) {
                key = commandCenter.getSkillKey();
            } else {
                key.empty();
                commandCenter.skillCancel();

                Action bestAction = evolution();

                commandCenter.commandCall(bestAction.name());
            }
        }
    }

    private Action evolution() {
        FrameData simulatorAheadFrameData = simulator.simulate(frameData, playerNumber, null, null, 14);

        CharacterData myCharacter = simulatorAheadFrameData.getCharacter(playerNumber);
        CharacterData oppCharacter = simulatorAheadFrameData.getCharacter(!playerNumber);

        int myHP = myCharacter.getHp();
        int oppHP = oppCharacter.getHp();


        Object[]  myFiltered = Arrays.stream((myCharacter.getState() == State.AIR ? actionAir : actionGround)).filter(y -> myCharacter.getEnergy() >= gameData.getEPOf(y)).toArray();
        Object[]  oppFiltered = Arrays.stream((oppCharacter.getState() == State.AIR ? actionAir : actionGround)).filter(y -> oppCharacter.getEnergy() >= gameData.getEPOf(y)).toArray();

        Action[] myActions = new Action[myFiltered.length];
        Action[] oppActions = new Action[oppFiltered.length];

        for (int i = 0; i < myActions.length; i++) {
            myActions[i] = (Action) myFiltered[i];
        }
        for (int i = 0; i < oppActions.length; i++) {
            oppActions[i] = (Action) oppFiltered[i];
        }


        List<Genome> pop = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            pop.add(new Genome(random.nextInt(myActions.length), random.nextInt(oppActions.length), random));
        }

        for (int g = 0; g < 150; g++) {
            // Evaluate.
            for (Genome genome : pop) {

                if (genome.evaluated) continue;

                Deque<Action> my = new LinkedList<>();
                my.add(myActions[genome.myIndex]);

                Deque<Action> opp = new LinkedList<>();
                opp.add(oppActions[genome.oppIndex]);

                FrameData result = simulator.simulate(frameData, playerNumber, my, opp, 45);

                // Get dmg of: oppActions[genome.oppIndex]
                Action oppAction = oppActions[genome.oppIndex];


                int dmg = gameData.getDamageOf(oppAction);
                genome.opponentDamage = dmg;


                genome.fitness = getScore(result, myHP, oppHP);// * (dmg + 1);
                genome.evaluated = true;
            }

            int fMin = Integer.MAX_VALUE;
            int fMax = Integer.MIN_VALUE;

            for (Genome genome : pop) {
                if (genome.fitness > fMax) fMax = genome.fitness;
                if (genome.fitness < fMin) fMin = genome.fitness;
            }

            if (fMin < fMax) {

                for (Genome genome : pop) {
                    genome.fitnessMod = ((double) (genome.fitness - fMin) / (double) (fMax - fMin));
                    genome.fitnessMod *= genome.opponentDamage;
                }
            }

            pop.sort(Comparator.comparingDouble(o -> -o.fitnessMod));


            int[] dist = createDistr(pop);

            int p1 = rws(dist);
            int p2 = rws(dist);

            Genome child;

            if (p1 != p2) {
                // Cross.

                child = pop.get(p1).cross(pop.get(p2));
            } else {
                child = pop.get(p1).copy();
            }

            if (p1 == p2 || random.nextBoolean()) {
                child = child.mutate(myActions.length, oppActions.length);
            }

            pop.add(child);
        }

        return myActions[pop.get(0).myIndex];
    }

    private int[] createDistr(List<Genome> pop) {
        int[] dist = new int[pop.size()];

        int last = 0;

        for (int i = pop.size() - 1; i >= 0; i--) {
            dist[i] = pop.size() - i + last;

            last = dist[i];
        }

        return dist;
    }

    public int rws(int[] dist) {
        int max = dist[0] + 1;

        int value = random.nextInt(max);

        for (int i = dist.length - 1; i >= 0; i--) {
            value -= dist[i];

            if (value <= 0) {
                return i;
            }
        }

        return -1;
    }

    public int getScore(FrameData fd, int myOriginalHp, int oppOriginalHp) {
        return (fd.getCharacter(playerNumber).getHp() - myOriginalHp) - (fd.getCharacter(!playerNumber).getHp() - oppOriginalHp);
    }

    public class Genome {
        public int myIndex = -1;
        public int oppIndex = -1;

        public int fitness = 0;
        public double fitnessMod = 0;
        public int opponentDamage = 0;
        public boolean evaluated = false;

        Random random;

        public Genome(int a, int b, Random random) {
            myIndex = a;
            oppIndex = b;

            this.random = random;
        }

        public Genome copy() {
            return new Genome(myIndex, oppIndex, random);
        }

        public Genome cross(Genome with) {
            if (random.nextBoolean()) {
                return new Genome(myIndex, with.oppIndex, random);
            } else {
                return new Genome(with.myIndex, oppIndex, random);
            }
        }

        public Genome mutate(int myMax, int oppMax) {
            int nextMy = myIndex;
            int nextOpp = oppIndex;

            int max = 5;

            if (random.nextBoolean()) {
                int next = (1 + random.nextInt(max)) * 2 - max;

                nextMy += next;
                if (nextMy < 0) nextMy = 0;
                if (nextMy >= myMax) nextMy = myMax - 1;

            }

            if (random.nextBoolean()) {
                int next = (1 + random.nextInt(max)) * 2 - max;

                nextOpp += next;
                if (nextOpp < 0) nextOpp = 0;
                if (nextOpp >= oppMax) nextOpp = oppMax - 1;
            }

            return new Genome(nextMy, nextOpp, random);
        }
    }


    @Override
    public Key input() {
        return key;
    }

    @Override
    public void close() {

    }

    @Override
    public void roundEnd(int p1Hp, int p2Hp, int frames) {

    }



    /*
    FROM MCTSAI
     */

    public boolean canProcessing() {
        return !frameData.getEmptyFlag() && frameData.getRemainingFramesNumber() > 0;
    }
}
