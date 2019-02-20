package streaming;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient extends Thread {
	private final int PORT_JAVA = 6000;

	public ChatClient() {
		String[] Command = { "cmd", "/c", "BotTwitch.py" }; // 起動コマンドを指定する
		Runtime runtime = Runtime.getRuntime(); // ランタイムオブジェクトを取得する
		try {
			Logger.getAnonymousLogger().log(Level.INFO, "Start twitch bot");
			runtime.exec(Command); // 指定したコマンドを実行する
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Logger.getAnonymousLogger().log(Level.INFO, "Start Connect ");
		while (true) {
			try {
				byte[] buf = new byte[1024];

				Socket sock = new Socket("localhost", PORT_JAVA);
				InputStream is = sock.getInputStream();
				OutputStream os = sock.getOutputStream();
				DataInputStream dis = new DataInputStream(is);

				dis.read(buf);

				String str = new String(buf, "UTF-8");
				System.out.println(str);
				
				Chat nc = new Chat(str);
//				addNewChat(nc);
				if(nc.getMsg().contains("!1")){
					StreamingSetting.cheerPlayer(true);
				}else if(nc.getMsg().contains("!2")){
					StreamingSetting.cheerPlayer(false);
				}else if(nc.getMsg().contains("!ZEN")){
					StreamingSetting.voteCharacter(0);
				}else if(nc.getMsg().contains("!GARNET")){
					StreamingSetting.voteCharacter(1);
				}else if(nc.getMsg().contains("!LUD")){
					StreamingSetting.voteCharacter(2);
				}
				is.close();
				os.close();
				sock.close();
			} catch (Exception e) {
				e.printStackTrace();
				Logger.getAnonymousLogger().log(Level.WARNING, "Restart twitch bot");
				String[] Command = { "cmd", "/c", "BotTwitch.py" }; // 起動コマンドを指定する
				Runtime runtime = Runtime.getRuntime(); // ランタイムオブジェクトを取得する
				try {
					runtime.exec(Command); // 指定したコマンドを実行する
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
		}
	}	
	
}
