package kr.co.nice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class BookEtcCustomer extends JPanel{
	private JButton btnCall,btnCdae,btnCjuyo;
	private DefaultTableModel mod;
	private Boolean juyomode = false; 
	//주요 모드 = 대여횟수 5회 이상.. 고객..
	
	public BookEtcCustomer(){
		design();

	}
	public void design(){
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new TitledBorder("고객 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
		JPanel cPn1=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		btnCall=new JButton("전체고객");
		btnCdae=new JButton("대여중인 고객");
		btnCjuyo=new JButton("주요 고객");
	
		cPn1.add(new JLabel("고객 자료 보기"));
		cPn1.add(btnCall);		
		cPn1.add(btnCdae);
		cPn1.add(btnCjuyo);
		this.add("North",cPn1);
		
		//비디오 목록 테이블 삽입
		String[][]data=new String[0][4];
		String []cols={"번호","이름","전화","대여횟수","메모"};
		mod=new DefaultTableModel(data,cols);
		JTable tab=new JTable(mod);
		tab.getColumnModel().getColumn(0).setPreferredWidth(20);
		tab.getColumnModel().getColumn(1).setPreferredWidth(26);
		tab.getColumnModel().getColumn(3).setPreferredWidth(20);
		tab.getColumnModel().getColumn(4).setPreferredWidth(50);
		tab.setPreferredScrollableViewportSize(new Dimension(10,120)); 

		tab.setSelectionBackground(Color.green);
		JScrollPane scroll=new JScrollPane(tab);
		this.add("Center",scroll);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	public static void main(String[] args) {
		BookEtcCustomer bookEtcCustomer =new BookEtcCustomer();
		JFrame frame=new JFrame();
		frame.getContentPane().add(bookEtcCustomer);
		frame.setBounds(200,200,580,400);
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
