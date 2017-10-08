import java.util.Calendar;

/*
	 * 处理和日志 有关的数据
	 */
	
public class CalendarInfomation {
	int year = -1,month = -1,day = -1;
	public void setYear(int year){
		this.year = year;
		
	}
	public int getYear(){
		return year;
		
	}
	public void setMonth(int month){
		if(month<=12&&month>1)
			this.month = month;
		else
			this.month = 1;
		
	}
	public int getMonth(){
		return month;
		
	}
	

	public   void setDay(int day) {
		this.day = day;
		
		
	}
	public int getDay(){
		return day;
		
	}
	/*
	 * 处理日志数据
	 */
	public String[] getMonthCalendar(){
		String [] day = new String[42];
		Calendar calen = Calendar.getInstance();
		//获得这个月的第一天，year年month月1日，注意0表示一月
		calen.set(year,month-1,1);
		int weekDay = calen.get(Calendar.DAY_OF_WEEK)-1;
		int dayAmount = 0;
		if(month==1||month==3||month==5||month==7||month==8||month==10||month==12)
		dayAmount = 31;
		if(month==4||month==6||month==9||month==11)
			dayAmount = 30;
		if(month==2)
			if(((year%4==0)&&(year%100!=0))||(year%400==0))
				dayAmount = 29;
			else
				dayAmount = 28;
		for(int i = 0;i<weekDay;i++)
			day[i] = "";
		for(int i = weekDay,n = 1;i<weekDay+dayAmount;i++){
			day[i] = String.valueOf(n);
			n++;
		}
		for(int i = weekDay+dayAmount;i<42;i++)
			day[i] = "";
		return day;
	}

}
