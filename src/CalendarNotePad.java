import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * 实现编辑，删除，显示日志的功能
 */
public class CalendarNotePad extends JPanel{
	
	private JTextArea text;//编辑日志的文本区
	private JTextField showMessaga;//显示日期
	private JPopupMenu menu;//菜单
	private Color color;
	private JMenuItem itemCopy,itemCut,itemPaste,itemClear;//菜单项
	
	/*
	 * 构造方法，用来创建对象
	 */
	public CalendarNotePad() {
		menu = new JPopupMenu();
		itemCut = new JMenuItem("剪切");
		itemCopy = new JMenuItem("复制");
		itemPaste = new JMenuItem("粘贴");
		itemClear = new JMenuItem("清空");
		itemCut.addActionListener(new ActionListen());
		itemCopy.addActionListener(new ActionListen());
		itemPaste.addActionListener(new ActionListen());
		itemClear.addActionListener(new ActionListen());
		menu.add(itemCut);
		menu.add(itemCopy);
		menu.add(itemPaste);
		menu.add(itemClear);
		showMessaga = new JTextField();
		showMessaga.setHorizontalAlignment(JTextField.CENTER);
		showMessaga.setFont(new Font("timesRoman", Font.BOLD, 16));
		showMessaga.setForeground(Color.blue);
		showMessaga.setBackground(new Color(216,224,231));
		showMessaga.setBorder(BorderFactory.createRaisedBevelBorder());
		showMessaga.setEditable(false);
		text = new JTextArea(10,10);
		text.addMouseListener(new MouseAdapter() {
			public void monsePressed(MouseEvent e){
				if(e.getModifiers()==InputEvent.BUTTON3_MASK)
					menu.show(text, e.getX(), e.getY());
			}
		});
		setLayout(new BorderLayout());
		add(showMessaga,BorderLayout.NORTH);
		add(new JScrollPane(text), BorderLayout.CENTER);
		
	}
	//保存日志
	public void save(File dir, int year, int month, int day) {
		String dailyContent = text.getText();//日志内容
		String fileName = ""+year+""+month+""+day+".txt";
		String key = ""+year+""+month+""+day;
		String[] dayFile = dir.list();
		boolean boo = false;
		//查看某天的日志是否存在
		for(int k = 0;k<dayFile.length;k++){
			if(dayFile[k].startsWith(key)){
				boo = true;
				break;
			}
		}
		String m;
		if(boo){
			//日志存在，修改日志
			m = ""+year+"年"+month+"月"+day+"有日志，确定修改日志吗？";
		}else{
			//不存在日志，保存日志
			m = ""+year+"年"+month+"月"+day+"还没有日志，保存日志吗？";
		}
		int ok = JOptionPane.showConfirmDialog(this,m, "询问",
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(ok == JOptionPane.YES_OPTION){
			try {
				File f = new File(dir,fileName);
				RandomAccessFile out = new RandomAccessFile(f, "rw");
				long fileEnd = out.length();
				byte[] bb = dailyContent.getBytes();
				out.write(bb);
				out.close();
			} catch (IOException exp) {
				
			}
		}
		
	}
	//删除日志
	public void delete(File dir, int year, int month, int day) {
		String key = ""+year+""+month+""+day;
		String[] dayFile = dir.list();
		boolean boo = false;
		for(int k = 0;k<dayFile.length;k++){
			if(dayFile[k].startsWith(key)){
				boo = true;
				break;
			}
		}
		if(boo){
			String m = "删除"+year+"年"+month+"月"+day+"的日志吗？";
			int ok = JOptionPane.showConfirmDialog(this,m, "询问",JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if(ok==JOptionPane.YES_OPTION){
				String fileName = ""+year+""+month+""+day+".txt";
				File deleteFile = new File(dir,fileName);
				
			}			
			}else {
				String m = ""+year+"年"+month+"月"+day+"无日志记录";
				JOptionPane.showMessageDialog(this,m, "提示",JOptionPane.WARNING_MESSAGE);
			}
		}
		
		
	
	//读取日志 
	public void read(File dir, int year, int month, int day) {
		String fileName =""+year+""+month+""+day+".txt";
		String key = ""+year+""+month+""+day;
		String[] dayFile = dir.list();
		boolean boo = false;
		for(int k = 0;k<dayFile.length;k++){
			if(dayFile[k].startsWith(key)){
				boo = true;
				break;
			}
		}
		if(boo){
			String m = ""+year+"年"+month+"月"+day+"有日志，要显示内容吗？";
			int ok = JOptionPane.showConfirmDialog(this,m, "询问",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(ok == JOptionPane.YES_OPTION){
				text.setText(null);
				try {
					File f = new File(dir,fileName);
					FileReader inOne = new FileReader(f);
					BufferedReader inTwo = new BufferedReader(inOne);
					String s = null;
					while((s = inTwo.readLine())!=null)
						text.append(s+"\n");
					inOne.close();
					inTwo.close();
				} catch (IOException exp) {
					
				}
			}
		}else{
			String m = ""+year+"年"+month+"月"+day+"无日志记录";
			JOptionPane.showMessageDialog(this,m, "提示",JOptionPane.WARNING_MESSAGE);
		}
		
	}

    //设置显示的日期
	public void setShowMessage(int year, int month, int day) {
		showMessaga.setText(""+year+"年"+month+"月"+day+"日");
		
		
	}
	//将显示日志的文本框

	public void setText(String str) {
		text.setText(str);
		
	}

	
	/*
	 * 定义菜单监听类
	 */
	class ActionListen implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == itemCut)
				text.cut();
			else if (e.getSource()==itemCopy) 
				text.copy();
			else if (e.getSource() ==itemPaste) 
				text.paste();
			else if (e.getSource()== itemClear) 
				text.setText(null);
			
			
		}
		
	}


}
