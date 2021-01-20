package aitest;

import enumerate.Action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SkillLoader {

    public List<Skill> skills = new ArrayList<>();
//    public List<Skill> skillsAttack = new ArrayList<>();

    public SkillLoader(String characterName) {
        String workingDir = Paths.get("").toAbsolutePath().toString() + "/";
        String filePath = workingDir + "data/characters/" + characterName + "/Motion.csv";

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 1; i < lines.size(); i++) {
                Skill skill = new Skill(lines.get(i));
//                if (skill.name.contains("RECOV") || skill.name.contains("SUFFER")) continue;

                if (skill.isMovement || skill.isGuard || skill.isAttack) {
                    skills.add(skill);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int x = 0;
    }

    public List<Skill> getSkills(SkillLocation location, int maxEP) {
        return skills.stream().filter(x -> x.attackLocation == location && x.energyRequired <= maxEP).collect(Collectors.toList());
    }

    public Skill find(SkillLocation location, int maxEP, double x, double y, boolean move) {
        List<Skill> available = skills.stream().filter(z -> z.attackLocation == location && z.energyRequired <= maxEP && (z.isMovement == move)).collect(Collectors.toList());

        double xMin = Double.MAX_VALUE;
        double xMax = Double.NEGATIVE_INFINITY;
        double yMin = Double.MAX_VALUE;
        double yMax = Double.NEGATIVE_INFINITY;

        for (Skill skill : available) {
            if (move) {
                if (skill.speedX < xMin) xMin = skill.speedX;
                if (skill.speedX > xMax) xMax = skill.speedX;
                if (skill.speedY < yMin) yMin = skill.speedY;
                if (skill.speedY > yMax) yMax = skill.speedY;
            } else {
                if (skill.centerX < xMin) xMin = skill.centerX;
                if (skill.centerX > xMax) xMax = skill.centerX;
                if (skill.centerY < yMin) yMin = skill.centerY;
                if (skill.centerY > yMax) yMax = skill.centerY;
            }
        }


        double dMin = Double.MAX_VALUE;
        Skill selected = null;

        for (Skill skill : available) {
            double dx, dy = 0d;

            if (move) {
                dx = skill.speedX;
                dy = skill.speedY;
            } else {
                dx = skill.centerX;
                dy = skill.centerY;
            }

            dx = (dx - xMin) / (xMax - xMin);
            dy = (dy - yMin) / (yMax - yMin);

            dx -= x;
            dy -= y;

            double d = dx * dx + dy * dy;

            if (d < dMin) {
                dMin = d;
                selected = skill;
            }
        }

        return selected;
    }

    public Skill getSkillToAction(Action action) {
        Optional<Skill> skill = skills.stream().filter(x -> x.action == action).findFirst();

        return skill.orElse(null);

    }
}
