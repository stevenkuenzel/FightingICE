import de.stevenkuenzel.xml.XElement;
import ftginterface.Fight;
import ftginterface.FightResult;

/**
 * FightingICEのメインメソッドを持つクラス．
 */
public class Main {

    /**
     * Entry point.
     *
     * @param args No arguments are considered.
     */
    public static void main(String[] args) {


        // Creates an observed fight over three rounds a 3,600 frames. The initial positions of the characters are certainly random.
        // Note that the directory where the data-folder of FightingICE is located has to be provided. Leave to "." if it is located in the working directory of the applicaiton.
        int numOfRounds = 3;
        Fight fight = new Fight(numOfRounds, 3600, true, true, ".");

        // Set the two controllers and their respective character models.
        fight.setPlayer(0, "MctsAi", "ZEN");
        fight.setPlayer(1, "Thunder", "ZEN");

        // Run the fight an retrieve the results.
        FightResult result = fight.run();

        // Print the remaining HP of the characters.
        for (int i = 0; i < numOfRounds; i++) {
            System.out.println("Round " + (i + 1) + ": " + result.result[0][i].remainingHP + "  <-->  " + result.result[1][i].remainingHP);
        }

        // Save the observation data of MctsAi in an XElement and export it to an XML-file in the working directory.
        XElement observationOfMctsAi = result.observation.export(0);
        observationOfMctsAi.save("Observation_MctsAi.xml");
    }
}
