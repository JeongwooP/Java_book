package kr.co.nice;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class BookEtc extends JPanel implements ChangeListener, ActionListener{
	JTabbedPane pane;
	JButton btnClose;
	
	public BookEtc() {
		this.setLayout(new BorderLayout());
		
	    pane = new JTabbedPane();

	    BookEtcCustomer etcCustomer = new BookEtcCustomer();
	    pane.addTab("고객관련",etcCustomer);
	
	    BookEtcBook etcBook = new BookEtcBook();
	    pane.addTab("도서관련",etcBook);
	
	    BookEtcCustomer2DBar etcChart1 = new BookEtcCustomer2DBar();
	    pane.addTab("차트보기(우수고객)",etcChart1);
	    
	    BookEtcBookLine etcChart2 = new BookEtcBookLine();
	    pane.addTab("차트보기(인기도서)",etcChart2);
	    
	    pane.setSelectedIndex(0); 
	    pane.requestFocus();
	    
	    this.setLayout(new BorderLayout());
	    this.add("Center",pane);
	    
	    btnClose = new JButton("닫기");
	    btnClose.addActionListener(this);
	    this.add("South",btnClose);
	}

	public void stateChanged(ChangeEvent e) {   
		//int index = pane.getSelectedIndex();
		//System.out.println(pane.getTitleAt(index));
	}
	public void actionPerformed(ActionEvent e){
			//System.exit(0);
		BookMain.book_etc.setEnabled(true);
		BookMain.childWinEtc.dispose();
	}
	public static void main(String[] args) {
		BookEtc bookEtc = new BookEtc();
		JFrame frame=new JFrame("기타");
		frame.getContentPane().add(bookEtc);
		frame.setBounds(200,200,500,400);
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
