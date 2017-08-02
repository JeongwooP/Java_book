package kr.co.nice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class BookEtcBook extends JPanel{
	
	private JButton btnAllcustomer,btnDae,btnBan;
	private JComboBox btnJang;
	private DefaultTableModel mod;	
	private int selectedMode; 
	// 1 = all customer, 2 = 대여중인 도서, 3 = 비치된 도서, 0 = default
	
	public BookEtcBook() {
		design();

	}
	private void design(){
		this.setLayout(new BorderLayout());
		//도서정보 패널========================
		this.setBorder(new TitledBorder(new TitledBorder("고객 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
		JPanel bPn1=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		btnAllcustomer=new JButton("전체 도서");
		btnDae=new JButton("대여중");
		btnBan=new JButton("비치중");
		btnJang = new JComboBox();
		btnJang.addItem("장르별");
		
		bPn1.add(new JLabel("도서 자료 보기"));
		bPn1.add(btnAllcustomer);		
		bPn1.add(btnDae);
		bPn1.add(btnBan);
		bPn1.add(btnJang);
		this.add("North",bPn1);
		
		//도서 목록 테이블 삽입
		String[][]data=new String[0][4];
		String []cols={"번호","제목","장르","대여횟수","메모"};
		mod=new DefaultTableModel(data,cols);
		JTable tab=new JTable(mod);
		
		tab.getColumnModel().getColumn(0).setPreferredWidth(7);
		tab.getColumnModel().getColumn(1).setPreferredWidth(25);
		tab.getColumnModel().getColumn(2).setPreferredWidth(7);
		tab.getColumnModel().getColumn(3).setPreferredWidth(7);
		tab.getColumnModel().getColumn(4).setPreferredWidth(50);
		tab.setPreferredScrollableViewportSize(new Dimension(10,120));
		tab.setSelectionBackground(Color.green);
		JScrollPane scroll=new JScrollPane(tab);
		this.add("Center",scroll);
		
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	public static void main(String[] args) {
		BookEtcBook bookEtcBook =new BookEtcBook();
		JFrame frame=new JFrame();
		frame.getContentPane().add(bookEtcBook);
		frame.setBounds(200,200,580,400);
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
