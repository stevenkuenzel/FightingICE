package ftginterface.skills;

import enumerate.Action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SkillLoader {

    private static final HashMap<String, SkillLoader> KNOWN_SKILL_LOADERS = new HashMap<>();

    public static boolean isInitialized = false;

    public static void initialize(String fightingICEroot) {
        String[] characterNames = new String[]{"ZEN", "LUD", "GARNET"};

        for (String characterName : characterNames) {
            SkillLoader skillLoader = new SkillLoader(characterName, fightingICEroot);

            KNOWN_SKILL_LOADERS.put(characterName, skillLoader);
        }
    }

    public static SkillLoader getSkillLoaderFor(String characterName) {
        if (KNOWN_SKILL_LOADERS.containsKey(characterName)) {
            return KNOWN_SKILL_LOADERS.get(characterName).copy();
        }

        return null;
    }

    public String characterName;
    public List<Skill> skills = new ArrayList<>();

    public List<Skill> combatAir, combatGround;

    public SkillLoader(String characterName, String fightingICEroot) {
        this.characterName = characterName;
        String filePath = fightingICEroot + "/data/characters/" + characterName + "/Motion.csv";

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 1; i < lines.size(); i++) {
                Skill skill = new Skill(lines.get(i));

                // Only store usable skills.
                if (skill.isMovement || skill.isGuard || skill.isAttack) {
                    skills.add(skill);
                }
            }

            sortSkills();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sortSkills()
    {
        combatAir = skills.stream().filter(x -> x.isAttack && x.attackLocation == SkillLocation.InAir).collect(Collectors.toList());
        combatGround = skills.stream().filter(x -> x.isAttack && x.attackLocation == SkillLocation.OnGround).collect(Collectors.toList());
    }

    public SkillLoader(String characterName, List<Skill> skills) {
        this.characterName = characterName;
        this.skills = skills.stream().map(Skill::copy).collect(Collectors.toList());

        sortSkills();
    }

    public SkillLoader copy() {
        return new SkillLoader(characterName, skills);
    }

    public List<Skill> getSkills(SkillLocation location, int maxEP) {
        return skills.stream().filter(x -> x.attackLocation == location && x.energyRequired <= maxEP).collect(Collectors.toList());
    }


    public Skill getSkillToAction(Action action) {
        Optional<Skill> skill = skills.stream().filter(x -> x.action == action).findFirst();

        return skill.orElse(null);
    }

}
