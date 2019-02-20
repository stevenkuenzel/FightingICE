package streaming;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat {
	private Date date;
	private String name;
	private String msg;
	public Chat(String str){
		try {
			String temp[] = str.split(",");
			String datetime = temp[0];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss");
			this.date = sdf.parse( datetime );
			this.name = temp[1];
			this.msg = temp[2];
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getMsg(){
		return this.msg;
	}
}