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

/**
 * Loads and stores the information about all skills.
 */
public class SkillLoader {
    /**
     * Stores the SkillLoaders for all known characters.
     */
    private static final HashMap<String, SkillLoader> KNOWN_SKILL_LOADERS = new HashMap<>();

    /**
     * Is the SkillLoader initialized?
     */
    public static boolean isInitialized = false;

    /**
     * Name of the character.
     */
    public String characterName;

    /**
     * All available skills.
     */
    public List<Skill> skills = new ArrayList<>();

    /**
     * List of attack skills in air and on ground. As well as movement skills.
     */
    public List<Skill> combatAir, combatGround, movementSkills;

    /**
     * Initializes the SKillLoader for all known characters.
     * @param fightingICEroot The path to the root directory of the FightingICE environment.
     */
    public static void initialize(String fightingICEroot) {
        String[] characterNames = new String[]{"ZEN", "LUD", "GARNET"};

        for (String characterName : characterNames) {
            SkillLoader skillLoader = new SkillLoader(characterName, fightingICEroot);

            KNOWN_SKILL_LOADERS.put(characterName, skillLoader);
        }
    }

    /**
     * Returns the according SkillLoader instance for the given character.
     * @param characterName The character's name.
     * @return The SkillLoader instance.
     */
    public static SkillLoader getSkillLoaderFor(String characterName) {
        if (KNOWN_SKILL_LOADERS.containsKey(characterName)) {
            return KNOWN_SKILL_LOADERS.get(characterName).copy();
        }

        return null;
    }

    /**
     * Creates an instance of SkillLoader.
     * @param characterName The character's name.
     * @param fightingICEroot The path to the root directory of the FightingICE environment.
     */
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

    /**
     * Copy constructor.
     */
    public SkillLoader(String characterName, List<Skill> skills) {
        this.characterName = characterName;
        this.skills = skills.stream().map(Skill::copy).collect(Collectors.toList());

        sortSkills();
    }

    /**
     * Sorts the available skills into the according subsets.
     */
    private void sortSkills()
    {
        combatAir = skills.stream().filter(x -> x.isAttack && x.attackLocation == SkillLocation.InAir).collect(Collectors.toList());
        combatGround = skills.stream().filter(x -> x.isAttack && x.attackLocation == SkillLocation.OnGround).collect(Collectors.toList());
        movementSkills = skills.stream().filter(x -> x.isMovement).collect(Collectors.toList());
    }


    /**
     * Creates a copy of the SkillLoader instance.
     * @return A copy of the SkillLoader instance.
     */
    public SkillLoader copy() {
        return new SkillLoader(characterName, skills);
    }

    /**
     * Returns all skills that match the given location and require at most a certain number of EP.
     * @param location The skill location.
     * @param maxEP The maximum energy points to be consumed.
     * @return All matching skills.
     */
    public List<Skill> getSkills(SkillLocation location, int maxEP) {
        return skills.stream().filter(x -> x.attackLocation == location && x.energyRequired <= maxEP).collect(Collectors.toList());
    }

    /**
     * Returns the Skill instance for a certain action.
     * @param action The Action.
     * @return The according Skill instance.
     */
    public Skill getSkillToAction(Action action) {
        Optional<Skill> skill = skills.stream().filter(x -> x.action == action).findFirst();

        return skill.orElse(null);
    }
}