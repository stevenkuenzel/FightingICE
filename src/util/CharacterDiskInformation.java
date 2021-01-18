package util;

import fighting.Motion;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CharacterDiskInformation
{
	public String[] names = { "ZEN", "LUD", "GARNET" };
	private final HashMap<String, InformationContainer> map = new HashMap<>();

	public CharacterDiskInformation()
	{

		for (String name : names) {
			map.put(name, new InformationContainer(name));
		}
	}

	public int size()
	{
		return names.length;
	}

	public InformationContainer get(String name)
	{
		return map.get(name);
	}


	public class InformationContainer {
		public String characterName;
		public int graphicSizeX, graphicSizeY, graphicAdjustX;
		public int[] graphicAdjustInitialX = new int[2];
		public List<Motion> motionList;

		public InformationContainer(String characterName) {
			this.characterName = characterName;
			loadDataFromDisk();
		}

		public InformationContainer(InformationContainer parent) {
			this.characterName = parent.characterName;
			this.graphicSizeX = parent.graphicSizeX;
			this.graphicSizeY = parent.graphicSizeY;
			this.graphicAdjustX = parent.graphicAdjustX;

			this.motionList = parent.motionList;
		}

		private void loadDataFromDisk() {
			try {
				String wd = Paths.get("").toAbsolutePath().toString() + "/";

				// Size ...
				List<String> lines = Files.readAllLines(Paths.get(wd + "./data/characters/" + characterName + "/gSetting.txt"));
				String[] size = lines.get(0).split(",", 0);
				String[] center = lines.get(1).split(",", 0);

				this.graphicSizeX = Integer.parseInt(size[0]);
				this.graphicSizeY = Integer.parseInt(size[1]);
				this.graphicAdjustX = Integer.parseInt(center[0]);
				this.graphicAdjustInitialX[0] = Integer.parseInt(center[2]);
				this.graphicAdjustInitialX[1] = Integer.parseInt(center[3]);

				// Motions / Skills ...
				lines = Files.readAllLines(Paths.get(wd + "./data/characters/" + characterName + "/Motion.csv"));
				ArrayList<Motion> motionList = new ArrayList<>(lines.size());

				for (int i = 1; i < lines.size(); i++) {
					String[] st = lines.get(i).split(",", 0);
					Motion motion = new Motion(st);
					motionList.add(motion);
				}

				this.motionList = Collections.unmodifiableList(motionList);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
