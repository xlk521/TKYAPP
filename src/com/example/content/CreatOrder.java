package com.example.content;

import java.util.Calendar;

import android.text.format.Time;
import com.example.content.Context;
public class CreatOrder {

	public String creatOrder(){
		//节约CPU
		Time time = new Time("GMT+8");
		time.setToNow();
	    int year = time.year;
	    int month = time.month;
	    int day = time.monthDay;
	    int minute = time.minute;
	    int hour = time.hour;
	    int sec = time.second;
	    String monthS = month+"";
	    String dayS = day+"";
	    String hourS = hour+"";
	    String minuteS = minute+"";
	    String secS = sec+"";
	    if (month<10) {
	    	monthS = "0"+month;
		}
	    if (day<10) {
	    	dayS = "0"+day;
		}
	    if (hour<10) {
	    	hourS = "0"+hour;
		}
	    if (minute<10) {
	    	minuteS = "0"+minute;
		}
	    if (sec<10) {
	    	secS = "0"+sec;
		}
	    String dateTime = year+""+monthS+""+dayS+""+hourS+""+minuteS+""+secS;
	    String order = Context.checi + Context.am + dateTime;
	    return order;
	}
	public String creatOrderC(){
		//时间比较准确
		String order = "";
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		String monthS = month+"";
	    String dayS = day+"";
	    String hourS = hour+"";
	    String minuteS = minute+"";
	    String secS = sec+"";
	    if (month<10) {
	    	monthS = "0"+month;
		}
	    if (day<10) {
	    	dayS = "0"+day;
		}
	    if (hour<10) {
	    	hourS = "0"+hour;
		}
	    if (minute<10) {
	    	minuteS = "0"+minute;
		}
	    if (sec<10) {
	    	secS = "0"+sec;
		}
	    String dateTime = year+""+monthS+""+dayS+""+hourS+""+minuteS+""+secS;
		order = Context.checi + Context.am + dateTime;
		return order;
	}
}
