import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
/*
 * 主类，显示日历记事本主页面
 */

public class CalendarWin extends JFrame implements FocusListener{
	
	private JLabel preYear,preMonth,center,nextMonth,nextYear;
	private String dayText;
	private int year,month,day;
	private CalendarInfomation CalendarInfomation;//日期信息、
	private CalendarPad calenderPad;//日历
	private CalendarNotePad notePad;//日记本、
	private JTextField[] showDay;
	//保存，删除，读取日
	private JButton saveDailyRecord,deleteDailyRecord,readDailyRecord;
	private File dir;
	
	/*
	 * 构造方法，完成窗口初始化
	 */
	public CalendarWin(){
		dir = new File("./dailyRecord");//建立存储日记的文件
		dir.mkdir();//创建目录
		showDay = new JTextField[42];
		for(int i = 0;i<showDay.length;i++){
			showDay[i] = new JTextField();
			showDay[i].setBackground(Color.white);
			showDay[i].setLayout(new GridLayout(3, 3));
			showDay[i].addFocusListener(this);
			showDay[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e){
					JTextField text = (JTextField) e.getSource();
					String str = text.getText().trim();
					try {
						day = Integer.parseInt(str);
					} catch (NumberFormatException exp) {
						// TODO: handle exception
					}
					CalendarInfomation.setDay(day);
					notePad.setShowMessage(year,month,day);
					notePad.setText("");
					
					
					
				}
			});
		}
		CalendarInfomation = new CalendarInfomation();
		calenderPad = new CalendarPad();
		notePad = new CalendarNotePad();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONDAY)+1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		JPanel titlePanel = new JPanel();
		initTitlePanel(titlePanel);
		CalendarInfomation.setYear(year);
		CalendarInfomation.setMonth(month);
		CalendarInfomation.setDay(day);
		calenderPad.setCalendarInfomation(CalendarInfomation);
		calenderPad.setShowDayTextField(showDay);
		notePad.setShowMessage(year, month, day);
		calenderPad.showMonthCalendar();
		doMark();//给有日志的日期做标记
		add(titlePanel,BorderLayout.NORTH);
		JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT,calenderPad,notePad);
		add(splitV, BorderLayout.CENTER);
		saveDailyRecord = new JButton("保存日志");
		deleteDailyRecord = new JButton("删除日志");
		readDailyRecord = new JButton("读取日志");
		saveDailyRecord.addActionListener(new ActionListener() {
			//给保存日志按钮添加动作监听器
			
			@Override
			public void actionPerformed(ActionEvent e) {
				notePad.save(dir,year,month,day);//保存日志
				doMark();
			}
		});
		deleteDailyRecord.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				notePad.delete(dir,year,month,day);
				doMark();
				
			}
		});
		readDailyRecord.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				notePad.read(dir,year,month,day);
				doMark();
				
			}
		});
		JPanel pSouth = new JPanel();
		pSouth.setBackground(new Color(216, 224, 231));
		pSouth.add(saveDailyRecord);
		pSouth.add(deleteDailyRecord);
		pSouth.add(readDailyRecord);
		add(pSouth, BorderLayout.SOUTH);
		setBounds(60,60,400,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		validate();
				
	}
	/*
	 * 初始化修改年份和月份的界面
	 */
	public void initTitlePanel(JPanel titlePanel) {
		dayText = year+"年"+month+"月";
		preYear = new JLabel("<<",JLabel.CENTER);
		preMonth = new JLabel("<",JLabel.CENTER);
		center = new JLabel(dayText,JLabel.CENTER);
		nextMonth = new JLabel(">",JLabel.CENTER);
		nextYear = new JLabel(">>",JLabel.CENTER);
		preYear.setToolTipText("上一年");
		preMonth.setToolTipText("上一月");
		nextMonth.setToolTipText("下一月");
		nextYear.setToolTipText("下一年");
		preYear.setBorder(javax.swing.BorderFactory.createEmptyBorder(2,10,0,0));
		preMonth.setBorder(javax.swing.BorderFactory.createEmptyBorder(2,15,0,0));
		nextMonth.setBorder(javax.swing.BorderFactory.createEmptyBorder(2,0,0,15));
		nextYear.setBorder(javax.swing.BorderFactory.createEmptyBorder(2,0,0,10));
		preYear.addMouseListener(new MyMouseListener(preYear));
		preMonth.addMouseListener(new MyMouseListener(preMonth));
		nextMonth.addMouseListener(new MyMouseListener(nextMonth));
		nextYear.addMouseListener(new MyMouseListener(nextYear));
		titlePanel.add(preYear);
		titlePanel.add(preMonth);
		titlePanel.add(center);
		titlePanel.add(nextMonth);
		titlePanel.add(nextYear);
		titlePanel.setBackground(new Color(216, 224, 231));
		titlePanel.setPreferredSize(new java.awt.Dimension(210,25));
		
	}
	/*
	 * 定义鼠标监听类
	 */
	class MyMouseListener implements MouseListener{
		
		JLabel label;
		 public MyMouseListener(final JLabel label) {
			this.label = label;
		}
		 //鼠标进入时，将标签颜色改为蓝色
		 @Override
			public void mouseEntered(MouseEvent me) {
				label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
				label.setForeground(Color.BLUE);
				
			}
		 //鼠标离开，将标签颜色改为黑色
		 @Override
			public void mouseExited(MouseEvent me) {
				label.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
				label.setForeground(java.awt.Color.BLACK);
				
			}
		 //鼠标按下，将标签颜色改为白色
		 @Override
			public void mousePressed(MouseEvent me) {
			 label.setForeground(java.awt.Color.WHITE);	
			}
		 //鼠标放开，将标签颜色改为黑色
		 @Override
			public void mouseReleased(MouseEvent me) {
				label.setForeground(java.awt.Color.BLACK);
				
			}
			//鼠标点击，修改日期
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == nextYear){
				year++;
			}else if (e.getSource() == preYear) {
				year--;
				
			}else if (e.getSource() == nextMonth) {
				month++;
				if(month>12){
					month = 1;
					year++;
				}
				
			}else if (e.getSource() == preMonth) {
				month--;
				if(month<1){
					month = 12;
					year--;
				}
				
			}
			center.setText(year+"年"+month+"月");
			CalendarInfomation.setYear(year);
			CalendarInfomation.setMonth(month);
			calenderPad.setCalendarInfomation(CalendarInfomation);
			calenderPad.showMonthCalendar();
			notePad.setShowMessage(year, month, 1);
			notePad.setText("");
			doMark();
			
			
		}
		
	}
	//组件获得焦点，改变颜色
	@Override
	public void focusGained(FocusEvent e) {
		Component com = (Component) e.getSource();
		com.setBackground(new Color(255, 187, 255));
		
	}
	//组件失去焦点，改变颜色
	@Override
	public void focusLost(FocusEvent e) {
		Component com = (Component) e.getSource();
		com.setBackground(Color.white);
		
	}
	//给所有日期添加标记

	public void doMark() {
		for(int i = 0;i<showDay.length;i++){
			showDay[i].removeAll();
			String str = showDay[i].getText().trim();
			try {
				int n = Integer.parseInt(str);
				if(isHaveDailRecord(n)==true){
					JLabel mess = new JLabel("有");
					mess.setFont(new Font("timesroman", Font.PLAIN, 11));
					mess.setForeground(Color.RED);
					showDay[i].add(mess);
				}
				
			} catch (Exception exp) {
				
			}
		}
		calenderPad.repaint();
		calenderPad.validate();
		
	}
	//判断该日期是否有日志，有返回true，否则返回false
	public boolean isHaveDailRecord(int n) {
		String key = ""+year+""+month+""+n;
		String[] dayFile = dir.list();
		boolean boo = false;
		for(int k = 0;k<dayFile.length;k++){
			if(dayFile[k].equals(key+".tat")){
				boo = true;
				break;
			}
		}
		return boo;
	}
	public static void main(String[] args) {
		new CalendarWin();
		
	}

}
