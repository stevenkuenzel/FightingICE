# FightingICE
A modified variant of the [FightingICE environment][0] dedicated to neuroevolution. To allow fast computation and simulation of fights between agents, all components concerning acoustical or visual elements have been removed.

## Features
* Run fights between AI controllers directly from Java with negligible implementation effort.
* Multiple fights can be executed concurrently.
* Observe fights: Retrieve further information about which attack skills have been successful or not in which situation. For more information please refer to my [dissertation][6].
* Uncertainty: Random initial character positions, if desired.
* The original implementation of the reference controllers still may be used.

## Limitations
* The feature of developing visual-based AI controllers is not supported.
* The fight and controllers are not running in separate threads any more. (Anyhow, as multiple fights can be run concurrently, there is not net disadvantage.)

## Credits
This software is based on the appreciated work of the Intelligent Computer Entertainment Lab at Ritsumeikan University, Kyoto, Japan. The source code of FightingICE can be found in the respective [GitHub repository][1].

## Quick Start
1. Checkout the project from GitHub and import it into your IDE
2. Note that the *data* folder has to be located in the working directory of your application. 
3. Run the application via the **main** method of the class **Main** (it provides the implementation of an exemplary fight between *MctsAi* and *Thunder*).
4. Your own AI controller has to implement the **aiinterface.AIInterface** interface. Please follow the introductory examples [here][2] and [here][3].
5. Retrieve information about the fight from the **ftginterface.FightResult** instance returned by the **run** method of the **ftginterface.Fight** instance.

## Future Work
TBA

## Further Reading
Please refer to my [dissertation][6] for further information.

## Scientific Papers Concerning Neuroevolution in FightingICE

[Deduction of fighting game countermeasures using Neuroevolution of Augmenting Topologies][4], Kristo and Maulidevi, 2016

[Coping with opponents: multi-objective evolutionary neural networks for fighting games][5], Künzel and Meyer-Nieberg, 2020


[0]: http://www.ice.ci.ritsumei.ac.jp/~ftgaic/index.htm
[1]: https://github.com/TeamFightingICE/FightingICE
[2]: http://www.ice.ci.ritsumei.ac.jp/~ftgaic/index-2.html
[3]: http://www.ice.ci.ritsumei.ac.jp/~ftgaic/index-2h.html
[4]: https://ieeexplore.ieee.org/document/7936127
[5]: https://link.springer.com/article/10.1007/s00521-020-04794-x
[6]: TO-BE-PUBLISHED-SOON