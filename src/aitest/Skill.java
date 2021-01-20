package aitest;

import enumerate.Action;

import java.util.Arrays;

public class Skill {
    static Action[] MOVEMENT_ACTIONS = new Action[] {Action.BACK_STEP, Action.BACK_JUMP, Action.FORWARD_WALK, Action.DASH, Action.FOR_JUMP, Action.JUMP, Action.CROUCH, Action.STAND};

    public String name;
    public Action action;

    public int damage, energyRequired, numOfFrames;

    public SkillLocation attackLocation;

    public int xFrom, xTo, yFrom, yTo;

    public int centerX, centerY;

    public int keyCount;
    public int totalFramesFromStartToFinish;
    public int startUp, cancelAfter;

    public boolean isDown;

    public int speedX, speedY;
    public int attackSpeedX, attackSpeedY;

    public int attackActive;



    public double basicValue;
    public double activeFramesRatio;

    public Skill(String line) {
        loadFromCSV(line);

//        dmgPerFrame = (double) damage / (double) numOfFrames;
        activeFramesRatio = (double) this.attackActive / (double) this.numOfFrames;
        basicValue = this.damage * activeFramesRatio;
    }

public boolean isAttack, isGuard, isMovement;


    @Override
    public String toString() {
        return this.name;
        //" [" + this.xFrom + " | " + this.yFrom + " >> " + this.xTo + " | " + this.yTo + "]";
    }

	/*
	0	motionName : THROW_A
1	frameNumber : 30
2	speedX : 5
3	speedY : 0
4	hitAreaLeft : 120
5	hitAreaRight : 160
6	hitAreaUp : 100
7	hitAreaDown : 305
8	state : STAND
9	attack.hitAreaLeft : 140
10	attack.hitAreaRight : 210
11	attack.hitAreaUp : 100
12	attack.hitAreaDown : 150
13	attack.speedX : 0
14	attack.speedY : 0
15	attack.StartUp : 5
16	attack.Active : 1
17	attack.HitDamage : 10
18	attack.GuardDamage : 0
19	attack.StartAddEnergy : -5
20	attack.HitAddEnergy : 2
21	attack.GuardAddEnergy : 0
22	attack.GiveEnergy : 10
23	attack.ImpactX : 0
24	attack.ImpactY : 0
25	attack.GiveGuardRecov : 0
26	attack.AttackType : 4
27	attack.DownProp : FALSE
28	cancelAbleFrame : -1
29	cancelAbleMotionLevel : -1
30	motionLevel : 10
31	control : FALSE
32	landingFlag : FALSE
33	Image : STAND_A
	 */


    public boolean isProjectile() {
        return (this.attackSpeedX + this.attackSpeedY) != 0;
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
        this.isMovement = Arrays.stream(MOVEMENT_ACTIONS).anyMatch(x -> x == this.action);

        this.keyCount = (int) this.name.chars().filter(x -> x == '_').count();
        this.totalFramesFromStartToFinish = numOfFrames + keyCount;

        //motionName,frameNumber,speedX,speedY,hitAreaLeft,hitAreaRight,hitAreaUp,hitAreaDown,state,attack.hitAreaLeft,attack.hitAreaRight,attack.hitAreaUp,attack.hitAreaDown,attack.speedX,attack.speedY,attack.StartUp,attack.Active,attack.HitDamage,attack.GuardDamage,attack.StartAddEnergy,attack.HitAddEnergy,attack.GuardAddEnergy,attack.GiveEnergy,attack.ImpactX,attack.ImpactY,attack.GiveGuardRecov,attack.AttackType,attack.DownProp,cancelAbleFrame,cancelAbleMotionLevel,motionLevel,control,landingFlag,Image

        //THROW_A,30,5,0,120,160,100,305,STAND,140,210,100,150,0,0,5,1,10,0,-5,2,0,10,0,0,0,4,FALSE,-1,-1,10,FALSE,FALSE,STAND_A
        //STAND_D_DF_FA,60,0,0,120,160,100,305,STAND,205,250,175,195,3,0,20,150,10,2,-2,3,5,5,10,0,5,1,FALSE,54,2,3,FALSE,FALSE,STAND_FA
    }

}
