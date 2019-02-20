package streaming;

import java.util.LinkedList;

import setting.GameSetting;

public class StreamingSetting {
	//public static LinkedList<Chat> chatList = new LinkedList<Chat>();
	
	public static int cheer = 0;
	
	public static int[] nextChara = new int[GameSetting.CHARACTERS.length];
	
	public synchronized static void resetStatistics(){
		cheer = 0;
		nextChara = new int[GameSetting.CHARACTERS.length];
	}
	
	public synchronized static void resetCheer(){
		cheer = 0;
	}
	
	public synchronized static void cheerPlayer(boolean player){
		cheer +=  player? 1:-1;
	}
	
	public synchronized static int getCheer(){
		return cheer;
	}
	
	public synchronized static void voteCharacter(int c){
		nextChara[c]+=1;
	}
	
}
