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

import java.awt.*;
import java.util.*;
import java.util.List;

public class Xai implements AIInterface {
    GameData gameData;
    boolean playerNumber;
    FrameData frameData;
    boolean isControl;
    Simulator simulator;
    CommandCenter commandCenter;
    private Key key;

    SkillLoader skillLoader;

    Random random = new Random();

    @Override
    public int initialize(GameData gd, boolean playerNumber) {
        this.gameData = gd;
        this.playerNumber = playerNumber;
        this.simulator = gd.getSimulator();
        this.commandCenter = new CommandCenter();
        this.key = new Key();

        skillLoader = new SkillLoader(gd.getCharacterName(playerNumber));

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

                FrameData simulatorAheadFrameData = simulator.simulate(frameData, playerNumber, null, null, 14);

                Skill bestSkillOpponent = evolution(simulatorAheadFrameData, !playerNumber, null);
                Skill bestSkill = evolution(simulatorAheadFrameData, playerNumber, bestSkillOpponent);

                commandCenter.commandCall(bestSkill.action.name());
            }
        }
    }

    List<Result> known = new ArrayList<>();

    public class Result {
        int score;
        Skill skill;


        public Result(int score, Skill skill) {
            this.score = score;
            this.skill = skill;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Result)
            {
                Result other = (Result) obj;

                return skill.action == other.skill.action;
            }

            return false;
        }
    }

    private void evaluate(SkillLocation skillLocation, int ep, Genome genome, Skill opponentSkill, int myHP, int oppHP) {
        Skill selected = skillLoader.find(skillLocation, ep, genome.dX, genome.dY, random.nextDouble() <= genome.pMove);

        if (selected != null) {
            Result r = new Result(-9999, selected);
            genome.skill = selected;

            if (known.contains(r))
            {
                genome.fitness = known.stream().filter(x -> x.equals(r)).findFirst().get().score;
                genome.evaluated = true;
            }

            Deque<Action> my = new LinkedList<>();
            my.add(selected.action);

            Deque<Action> opp = new LinkedList<>();
            if (opponentSkill != null) opp.add(opponentSkill.action);

            int numOfFrames = selected.totalFramesFromStartToFinish;
            if (opponentSkill != null && opponentSkill.totalFramesFromStartToFinish > numOfFrames)
                numOfFrames = opponentSkill.totalFramesFromStartToFinish;

            FrameData result = simulator.simulate(frameData, playerNumber, my, opp, numOfFrames);

            genome.evaluated = true;

            genome.fitness = getScore(result, playerNumber, myHP, oppHP);
            r.score = genome.fitness;

            if (!known.contains(r)) known.add(r);

        } else {
            genome.fitness = -9999;
        }
    }


    private Skill evolution(FrameData simulatorAheadFrameData, boolean playerNumber, Skill opponentSkill) {
        known.clear();
        CharacterData myCharacter = simulatorAheadFrameData.getCharacter(playerNumber);
        CharacterData oppCharacter = simulatorAheadFrameData.getCharacter(!playerNumber);

        int myHP = myCharacter.getHp();
        int oppHP = oppCharacter.getHp();

        List<Genome> pop = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            pop.add(new Genome(random.nextDouble(), random.nextDouble(), random.nextDouble(), random));
        }

        for (int g = 0; g < 50; g++) {

            if (g > 0) {
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
                    child = child.mutate();
                }

                pop.add(child);
            }

            // Evaluate.
            for (Genome genome : pop) {

                if (genome.evaluated) continue;

                SkillLocation location = myCharacter.getState() == State.AIR ? SkillLocation.InAir : SkillLocation.OnGround;
                int ep = myCharacter.getEnergy();

                evaluate(location, ep, genome, opponentSkill, myHP, oppHP);
            }

            pop.sort(Comparator.comparingDouble(o -> -o.fitness));

            if (g > 0) {
                pop.remove(pop.size() - 1);
            }


//            int fMin = Integer.MAX_VALUE;
//            int fMax = Integer.MIN_VALUE;
//
//            for (Genome genome : pop) {
//                if (genome.fitness > fMax) fMax = genome.fitness;
//                if (genome.fitness < fMin) fMin = genome.fitness;
//            }
//
//            if (fMin < fMax) {
//
//                for (Genome genome : pop) {
//                    genome.fitnessMod = ((double) (genome.fitness - fMin) / (double) (fMax - fMin));
//                    genome.fitnessMod *= genome.opponentDamage;
//                }
//            }


        }


        int sum = 0;

        for (Genome genome : pop) {
            sum += genome.fitness;
        }

        return pop.get(0).skill;
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

    public int getScore(FrameData fd, boolean playerNumber, int myOriginalHp, int oppOriginalHp) {
        return (fd.getCharacter(playerNumber).getHp() - myOriginalHp) - (fd.getCharacter(!playerNumber).getHp() - oppOriginalHp);
    }

    public class Genome {
        public double dX, dY;
        public double pMove;
        Random random;
        public Skill skill;
        public boolean evaluated;
        public int fitness;

        public Genome(double dX, double dY, double pMove, Random random) {
            this.dX = dX;
            this.dY = dY;
            this.pMove = pMove;
            this.random = random;
        }

        public Genome copy() {
            return new Genome(dX, dY, pMove, random);
        }

        private double getI(int i) {
            if (i == 0) return dX;
            if (i == 1) return dY;
            if (i == 2) return pMove;

            return -1d;
        }

        private void setI(int i, double value) {
            if (i == 0) dX = value;
            if (i == 1) dY = value;
            if (i == 2) pMove = value;
        }

        public Genome cross(Genome with) {


            if (random.nextBoolean()) {
                double[] values = new double[3];

                for (int i = 0; i < values.length; i++) {
                    if (random.nextBoolean()) {
                        values[i] = getI(i);
                    } else {
                        values[i] = with.getI(i);
                    }
                }

                return new Genome(values[0], values[1], values[2], random);
            } else {
                return new Genome((dX + with.dX) / 2d, (dY + with.dY) / 2d, (pMove + with.pMove) / 2d, random);
            }
        }

        public Genome mutate() {
            double max = 0.2d;


            double[] values = new double[]{dX, dY, pMove};

            for (int i = 0; i < values.length; i++) {
                if (random.nextBoolean()) {
                    double vNext = values[i] + (2d * random.nextDouble() * max) - max;

                    double vMin = (i == 1 ? -1d : 0d);

                    if (vNext < vMin) vNext = vMin;
                    if (vNext > 1d) vNext = 1d;
                }
            }

            return new Genome(values[0], values[1], values[2], random);
        }
        /*

        public Genome mutate(int xMax, int yMax) {
            int nextDX = dX;
            int nextDY = dY;

            int max = 100;

            if (random.nextBoolean()) {
                int next = (1 + random.nextInt(max)) * 2 - max;

                nextDX += next;
                if (nextDX < 0) nextDX = 0;
                if (nextDX >= xMax) nextDX = xMax - 1;

            }

            if (random.nextBoolean()) {
                int next = (1 + random.nextInt(max)) * 2 - max;

                nextDY += next;
                if (nextDY <= -yMax) nextDY = -yMax + 1;
                if (nextDY >= yMax) nextDY = yMax - 1;
            }

            return new Genome(nextDX, nextDY, random);
        }
         */
    }

//    public class Genome {
//        public int myIndex = -1;
//        public int oppIndex = -1;
//
//        public int fitness = 0;
//        public double fitnessMod = 0;
//        public int opponentDamage = 0;
//        public boolean evaluated = false;
//
//        Random random;
//
//        public Genome(int a, int b, Random random) {
//            myIndex = a;
//            oppIndex = b;
//
//            this.random = random;
//        }
//
//        public Genome copy() {
//            return new Genome(myIndex, oppIndex, random);
//        }
//
//        public Genome cross(Genome with) {
//            if (random.nextBoolean()) {
//                return new Genome(myIndex, with.oppIndex, random);
//            } else {
//                return new Genome(with.myIndex, oppIndex, random);
//            }
//        }
//
//        public Genome mutate(int myMax, int oppMax) {
//            int nextMy = myIndex;
//            int nextOpp = oppIndex;
//
//            int max = 5;
//
//            if (random.nextBoolean()) {
//                int next = (1 + random.nextInt(max)) * 2 - max;
//
//                nextMy += next;
//                if (nextMy < 0) nextMy = 0;
//                if (nextMy >= myMax) nextMy = myMax - 1;
//
//            }
//
//            if (random.nextBoolean()) {
//                int next = (1 + random.nextInt(max)) * 2 - max;
//
//                nextOpp += next;
//                if (nextOpp < 0) nextOpp = 0;
//                if (nextOpp >= oppMax) nextOpp = oppMax - 1;
//            }
//
//            return new Genome(nextMy, nextOpp, random);
//        }
//    }


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
