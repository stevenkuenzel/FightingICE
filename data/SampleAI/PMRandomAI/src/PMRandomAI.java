import java.util.Random;

import aiinterface.PMAIInterface;
import struct.FrameData;
import struct.GameData;
import struct.Key;

public class PMRandomAI implements PMAIInterface {

	public static final int SEARCH_TIME = 150 * 100000;
	int n = 0;
	FrameData frameData;
	Random rnd;
	@Override
	public void close() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("AI close");
	}

	@Override
	public void getInformation(FrameData arg0) {
		// TODO 自動生成されたメソッド・スタブ
		//System.out.println("AI run getInformation");
		this.frameData = arg0;
	}

	@Override
	public int initialize(GameData arg0) {
		// TODO 自動生成されたメソッド・スタブ
		//System.out.println("AI run initialize");
		rnd = new Random();
		return 0;
	}

	@Override
	public Key[] input() {
		// TODO 自動生成されたメソッド・スタブ
		Key[] ks = new Key[2];
		for (int i = 0; i < ks.length; i++) {
			ks[i] = new Key();
			ks[i].A = (rnd.nextInt(10) > 4) ? true : false;
			ks[i].B = (rnd.nextInt(10) > 4) ? true : false;
			ks[i].C = (rnd.nextInt(10) > 4) ? true : false;
			ks[i].U = (rnd.nextInt(10) > 4) ? true : false;
			ks[i].D = (rnd.nextInt(10) > 4) ? true : false;
			ks[i].L = (rnd.nextInt(10) > 4) ? true : false;
			ks[i].R = (rnd.nextInt(10) > 4) ? true : false;
		}
		return ks;
	}

	@Override
	public void processing() {
		if(!frameData.getEmptyFlag() && frameData.getRemainingTimeMilliseconds() > 0){
//			long start = System.nanoTime();
//			while (System.nanoTime() - start <= SEARCH_TIME) {
//			}

			//System.out.println("AI run processing "+ frameData.getCurrentFrameNumber());
		}
		// TODO 自動生成されたメソッド・スタブ

	}
	
	@Override
	public void roundEnd(int p1Hp, int p2Hp, int frames){

	}


}
