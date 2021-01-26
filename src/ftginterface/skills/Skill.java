package ftginterface.skills;

import enumerate.Action;

import java.util.Arrays;

public class Skill {
    private static final Action[] MOVEMENT_ACTIONS = new Action[] {Action.BACK_STEP, Action.BACK_JUMP, Action.FORWARD_WALK, Action.DASH, Action.FOR_JUMP, Action.JUMP, Action.CROUCH, Action.STAND};

    public String name;
    public Action action;

    public int damage, energyRequired, numOfFrames;

    public SkillLocation attackLocation;

    public int xFrom, xTo, yFrom, yTo, centerX, centerY;

    public int keyCount;
    public int totalFramesFromStartToFinish;
    public int startUp, cancelAfter, attackActive;

    public boolean isDown;
    public int speedX, speedY, attackSpeedX, attackSpeedY;

    public boolean isAttack, isGuard, isMovement, isProjectile;

    public Skill(String line) {
        loadFromCSV(line);
    }

    /**
     * Copy constructor.
     */
    public Skill(String name, Action action, int damage, int energyRequired, int numOfFrames, SkillLocation attackLocation, int xFrom, int xTo, int yFrom, int yTo, int centerX, int centerY, int keyCount, int totalFramesFromStartToFinish, int startUp, int cancelAfter, int attackActive, boolean isDown, int speedX, int speedY, int attackSpeedX, int attackSpeedY, boolean isAttack, boolean isGuard, boolean isMovement, boolean isProjectile) {
        this.name = name;
        this.action = action;
        this.damage = damage;
        this.energyRequired = energyRequired;
        this.numOfFrames = numOfFrames;
        this.attackLocation = attackLocation;
        this.xFrom = xFrom;
        this.xTo = xTo;
        this.yFrom = yFrom;
        this.yTo = yTo;
        this.centerX = centerX;
        this.centerY = centerY;
        this.keyCount = keyCount;
        this.totalFramesFromStartToFinish = totalFramesFromStartToFinish;
        this.startUp = startUp;
        this.cancelAfter = cancelAfter;
        this.attackActive = attackActive;
        this.isDown = isDown;
        this.speedX = speedX;
        this.speedY = speedY;
        this.attackSpeedX = attackSpeedX;
        this.attackSpeedY = attackSpeedY;
        this.isAttack = isAttack;
        this.isGuard = isGuard;
        this.isMovement = isMovement;
        this.isProjectile = isProjectile;
    }

    public Skill copy()
    {
        return new Skill(name, action,damage,energyRequired,numOfFrames,attackLocation,xFrom,xTo,yFrom,yTo,centerX,centerY,keyCount,totalFramesFromStartToFinish,startUp,cancelAfter,attackActive,
                isDown,speedX,speedY,attackSpeedX,attackSpeedY,isAttack,isGuard,isMovement,isProjectile);
    }

    @Override
    public String toString() {
        return this.name;
    }

    private void loadFromCSV(String line) {
        String[] columns = line.split(",");

        this.name = columns[0];
        this.isGuard = this.name.contains("GUARD") && !this.name.contains("RECOV");

        this.numOfFrames = Integer.parseInt(columns[1]);
        this.damage = Integer.parseInt(columns[17]);
        this.isAttack = this.damage > 0;
        this.energyRequired = -Integer.parseInt(columns[19]);


        this.attackLocation = SkillLocation.OnGround;

        if (this.name.startsWith("AIR")) {
            this.attackLocation = SkillLocation.InAir;
        }

        this.xFrom = Integer.parseInt(columns[9]);
        this.xTo = Integer.parseInt(columns[10]);

        this.yFrom = Integer.parseInt(columns[11]);
        this.yTo = Integer.parseInt(columns[12]);

        this.centerX = (this.xFrom + this.xTo) / 2;
        this.centerY = (this.yFrom + this.yTo) / 2;

        this.speedX = Integer.parseInt(columns[2]);
        this.speedY = Integer.parseInt(columns[3]);

        this.attackSpeedX = Integer.parseInt(columns[13]);
        this.attackSpeedY = Integer.parseInt(columns[14]);

        this.startUp = Integer.parseInt(columns[15]);
        this.attackActive = Integer.parseInt(columns[16]);

        this.isDown = Boolean.parseBoolean(columns[27]);
        this.cancelAfter = Integer.parseInt(columns[28]);

        if (this.cancelAfter == -1) {
            this.cancelAfter = this.numOfFrames;
        }

        this.action = Action.valueOf(this.name);
        this.isProjectile = (this.attackSpeedX + this.attackSpeedY) != 0;
        this.isMovement = Arrays.stream(MOVEMENT_ACTIONS).anyMatch(x -> x == this.action);

        this.keyCount = (int) this.name.chars().filter(x -> x == '_').count();
        this.totalFramesFromStartToFinish = numOfFrames + keyCount;
    }
}