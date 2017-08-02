package kr.co.nice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.JobAttributes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class BookBook extends JPanel implements ActionListener{
	JTextField txtBbun,txtBjemok,txtBjang,txtBkuil,txtBdaesu,txtBdaeyn,txtBdaebun,txtBdaeil,txtBbanil;
	JTextArea taBmemo;
	JButton btnInsert,btnUpdate,btnDel,btnFind,btnOption,btnClose;
	JButton btnF,btnP,btnN,btnL,btnin,btnsu;
	JLabel lblRec,lblPic;


	String sql, imgPath; //--- 그림 파일 경로 변수
	int iTotal = 0;	// 자료의 갯수
	int iLast = 0; 	// 마지막 레코드 번호
	JPanel picPn;
	File file;  //--- 이미지 로딩하기 위한 변수
	boolean isInsert = false;	// Insert 버튼 눌림 여부
	boolean isUpdate = false;	// Update 버튼 눌림 여부
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public BookBook(){
		design();
		addListener();
		accDB();
		init();
		display();
	}
	
	private void accDB() {
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.println("book accDB err:" + e);
		}
	}
	
	private void addListener() {
		btnInsert.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDel.addActionListener(this);
		btnFind.addActionListener(this);
		btnOption.addActionListener(this);
		btnClose.addActionListener(this);
		btnF.addActionListener(this);
		btnP.addActionListener(this);
		btnN.addActionListener(this);
		btnL.addActionListener(this);
		btnsu.addActionListener(this);

	}
	
	private void init() {
		try {
			sql = "select * from book order by b_bun asc";
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			rs.last();
			iTotal = rs.getRow();	//전체 자료 수 얻기
			iLast = rs.getInt("b_bun");	//마지막 고객번호 얻기(추가용)
//			System.out.println(iTotal + " " + iLast);
			rs.first();
			
		} catch (Exception e) {
			System.out.println("init err:" + e);
		}
	}

	private void display() {
		try {
			txtBbun.setText(rs.getString("b_bun"));
//			txtBdaeyn.setText(rs.getString("b_daeyn"));
			if(rs.getString("b_daeyn").equals("y")){
				txtBdaeyn.setText("대여중");
				txtBdaeyn.setForeground(Color.RED);
			}else{
				txtBdaeyn.setText("비치중");
				txtBdaeyn.setForeground(Color.BLACK);
			}
			txtBjemok.setText(rs.getString("b_jemok"));
			txtBjang.setText(rs.getString("b_jang"));
			txtBdaesu.setText(rs.getString("b_daesu"));
			txtBdaebun.setText(rs.getString("b_daebun"));
			
			if(rs.getString("b_daeil")==null){
				txtBdaeil.setText("");
			}else{
				txtBdaeil.setText(rs.getString("b_daeil").substring(0, 10));
			}
			if(rs.getString("b_kuipil")==null){
				txtBkuil.setText("");
			}else{
				txtBkuil.setText(rs.getString("b_kuipil").substring(0,10));
			}
			if(rs.getString("b_banil")==null){
				txtBbanil.setText("");
			}else{
				txtBbanil.setText(rs.getString("b_banil").substring(0,10));
			}
			
			taBmemo.setText(rs.getString("b_memo"));
			
			lblRec.setText(rs.getRow() + " / " + iTotal);
		
			//이미지 출력
			imgPath = "c:/work/book/" + rs.getString("b_image");
			displayImage();
		} catch (Exception e) {
			System.out.println("book display err : " + e);
		}

	}
	
	private void displayImage(){
		if(imgPath != null){
			ImageIcon icon = new ImageIcon(imgPath);
			lblPic.setIcon(icon);
			lblPic.setToolTipText("경로는 " + imgPath.toLowerCase());
		}else{
				lblPic.setText(null);
			}
		}
	
	
	public void actionPerformed(ActionEvent e) {
		try {
			if(e.getSource().equals(btnF)) {
				rs.first();
				display();
			}else if(e.getSource().equals(btnP)) {
				if(rs.isFirst()) return;
				rs.previous();
				display();
			}else if(e.getSource().equals(btnN)) {
				if(rs.isLast()) return;
				rs.next();
				display();
			}else if(e.getSource().equals(btnL)) {
				rs.last();
				display();
			}
			
		} catch (Exception e2) {
			System.out.println("레코드 이동 오류:" + e);
		}
		if(e.getSource().equals(btnInsert)) {	//신규 도서
			if(isInsert == false){
				btnInsert.setText("확인");
				isInsert = true;
				
				txtBbun.setText(iLast + 1 + "");
				txtBjemok.setEditable(true);
				txtBjang.setEditable(true);
				txtBkuil.setEditable(true);
				taBmemo.setEditable(true);
				taBmemo.setBackground(Color.WHITE);
				
				txtBjemok.setText(null);
				txtBjang.setText(null);
				taBmemo.setText(null);
				Calendar cal = Calendar.getInstance();
				String imsi = cal.get(Calendar.YEAR) + "-" +
						(cal.get(Calendar.MONTH)+1) + "-" +
						cal.get(Calendar.DATE);
				txtBkuil.setText(imsi);
				txtBdaeyn.setText("n");
				txtBdaebun.setText(null);
				txtBdaesu.setText("0");
				txtBdaeil.setText(null);
				txtBbanil.setText(null);
				
				imgPath = null; 	//도서 이미지 경로 초기화
				
				//이미지 삽입버튼으로 이미지 대체
				picPn.setBorder(BorderFactory.createEmptyBorder(50, 100, 50 ,100));
				btnin = new JButton("그림삽입");
				btnin.addActionListener(this);
				lblPic.setVisible(false);
				picPn.add("Center", btnin);
				
				txtBjemok.requestFocus();
			}else{
				btnInsert.setText("신규");
				isInsert = false;
				
				txtBjemok.setEditable(false);
				txtBjang.setEditable(false);
				txtBkuil.setEditable(false);
				taBmemo.setEditable(false);
				taBmemo.setBackground(Color.LIGHT_GRAY);
				
				//신구도서 추가 작업
				try {
					sql = "insert into book values(?, ?, ?, ?, 0, 'n', 0, null, null, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, txtBbun.getText());
					pstmt.setString(2, txtBjemok.getText());
					pstmt.setString(3, txtBjang.getText());
					pstmt.setString(4, txtBkuil.getText());
					pstmt.setString(5, taBmemo.getText());
					pstmt.setString(6, file.getName());
//					System.out.println("imgPath: " + imgPath);
					pstmt.executeUpdate();
					
					init();
					rs.last();
					display();
				} catch (Exception e2){
				System.out.println(" 어루" + e2);
				}
			}
	
		}else if(e.getSource().equals(btnUpdate)) {
			//제목, 장르, 구입일만 수정에 참여
			if(isUpdate == false){
				btnUpdate.setText("완료");
				isUpdate = true;
				
				txtBjemok.setEditable(true);
				txtBjang.setEditable(true);
				txtBkuil.setEditable(true);
				//수정 작업------
			}else{
				btnUpdate.setText("수정");
				isUpdate = false;
				
				try {
				sql = "update book set b_jemok = ?, b_jang = ?, b_kuipil = ? where b_bun = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, txtBjemok.getText());
				pstmt.setString(2, txtBjang.getText());
				pstmt.setString(3, txtBkuil.getText());
				pstmt.setString(4, txtBbun.getText());
				pstmt.executeUpdate();
				
				int currentRow = rs.getRow();
				init();
				rs.absolute(currentRow);
				display();
				
				} catch (Exception e2) {
					System.out.println("수정 오류:" + e2);
				}
				
				txtBjemok.setEditable(false);
				txtBjang.setEditable(false);
				txtBkuil.setEditable(false);
				
				
			}
		}else if(e.getSource().equals(btnDel)) {
			//대여중인 도서는 삭제 불가
			int re = JOptionPane.showConfirmDialog(this, "정말 삭제할까요?", "삭제", JOptionPane.YES_NO_OPTION); 
			
			if(re == JOptionPane.YES_OPTION){
				try{
					if(rs.getRow() == 0){
						JOptionPane.showMessageDialog(this, "삭제할 도서가 없어요");
						return;
					}
					if(txtBdaeyn.getText().equals("비치중")){
						int currentRow = rs.getRow();
						sql = "delete from book where b_bun = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, txtBbun.getText());
						pstmt.executeUpdate();
						
						init();
						
						if(currentRow == 1){
							
						}else{
							rs.absolute(currentRow);
						}
						
						display();
					}else{
						JOptionPane.showMessageDialog(this, "대여중인 도서입니다.");
					}
					}catch (Exception e2) {
						System.out.println("삭제 오류" + e2);
					}
				}
			}
			
		else if(e.getSource().equals(btnOption)) {
			//생략...
			
		}else if(e.getSource().equals(btnClose)) {
			BookMain.book_book.setEnabled(true);
			BookMain.childWinBook.dispose();
		}else if(e.getSource().equals(btnsu)) {
			
		}else if(e.getSource().equals(btnin)){	//이미지 삽입용 파일
			JFileChooser chooser = new JFileChooser("c:/work/book/");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = chooser.showOpenDialog(this);
			if(result == JFileChooser.CANCEL_OPTION){
				file = null;
			}else{
				file = chooser.getSelectedFile();
				imgPath = file.getPath().replace("\\", "/");
//				System.out.println(imgPath);
				
				displayImage();
				
				lblPic.setVisible(true);
				btnin.setVisible(false);
				
			}
			
		}
	}

	public void design(){
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		//도서정보 패널========================
		JPanel bookPn=new JPanel(new GridLayout(6,1));
		bookPn.setBorder(new TitledBorder(new TitledBorder("도서 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
		JPanel bPn1=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bPn2=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bPn3=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bPn4=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bPn5=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bPn6=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		txtBbun=new JTextField("",5);
		txtBjemok=new JTextField("",30);
		txtBjang=new JTextField("",10);
		txtBkuil=new JTextField("",10);
		txtBdaesu=new JTextField("",5);
		txtBdaeyn=new JTextField("",5);
		txtBdaebun=new JTextField("",5);
		txtBdaeil=new JTextField("",10);
		txtBbanil=new JTextField("",10);
		taBmemo=new JTextArea(2,30);
		JScrollPane scroll=new JScrollPane(taBmemo);
		taBmemo.setBackground(Color.lightGray);
		
		txtBbun.setEditable(false);
		txtBjemok.setEditable(false);
		txtBjang.setEditable(false);
		txtBkuil.setEditable(false);
		txtBdaesu.setEditable(false);
		txtBdaeyn.setEditable(false);
		txtBdaebun.setEditable(false);
		txtBdaeil.setEditable(false);
		txtBbanil.setEditable(false);
		taBmemo.setEditable(false);
				
		btnInsert=new JButton("신규");
		btnUpdate=new JButton("수정");
		btnDel=new JButton("삭제");
		btnFind=new JButton("검색");
		btnOption=new JButton("옵션");
		btnClose=new JButton("닫기");
		btnF=new JButton(" <<= ");
		btnP=new JButton("  <= ");
		btnN=new JButton(" =>  ");
		btnL=new JButton(" =>> ");
		lblRec=new JLabel(" 0 / 0 ",JLabel.CENTER);
		bPn1.add(new JLabel("번호 :"));
		bPn1.add(txtBbun);
		bPn1.add(new JLabel("                              대여표시 :"));
		bPn1.add(txtBdaeyn);
		
		bPn2.add(new JLabel("제목 :"));
		bPn2.add(txtBjemok);	

		bPn3.add(new JLabel("장르 :"));
		bPn3.add(txtBjang);
		bPn3.add(new JLabel("                구입일 :"));
		bPn3.add(txtBkuil);
		
		bPn4.add(new JLabel("대여횟수 :"));
		bPn4.add(txtBdaesu);
		bPn4.add(new JLabel("                 대여자번호 :"));
		bPn4.add(txtBdaebun);
		
		bPn5.add(new JLabel("대여일 :"));
		bPn5.add(txtBdaeil);
		bPn5.add(new JLabel("            반납일 :"));
		bPn5.add(txtBbanil);

		bPn6.add(new JLabel("메모 :"));
		bPn6.add(scroll);
		
		bookPn.add(bPn1);  bookPn.add(bPn2); 	bookPn.add(bPn3);
		bookPn.add(bPn4);  bookPn.add(bPn5); 	bookPn.add(bPn6);
		this.add(bookPn);
		
		//이미지 패널----------------------------------------------------
		picPn=new JPanel(new BorderLayout());
		lblPic = new JLabel();
		//lblPic.setPreferredSize(new Dimension(1000, 300));
		picPn.add(lblPic);

		this.add(picPn);

		//레코드 이동 패널------------------------------------------------
		JPanel movePn=new JPanel();
		movePn.setBorder(new TitledBorder(new TitledBorder("레코드 이동"), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
		movePn.add(btnF);
		movePn.add(btnP);
		movePn.add(lblRec);
		movePn.add(btnN);
		movePn.add(btnL);
		
		this.add(movePn);
		
        //명령 버튼 패널--------------------------------------------------
		JPanel bottomPn1=new JPanel();
		bottomPn1.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		bottomPn1.add(btnInsert);
		bottomPn1.add(btnUpdate);
		bottomPn1.add(btnDel);
		bottomPn1.add(btnFind);
		bottomPn1.add(btnOption);
		bottomPn1.add(btnClose);
		
		this.add(bottomPn1);
		
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		btnsu=new JButton("그림 수정");

	}
	

	public static void main(String[] args){
		BookBook bookBook =new BookBook();
		
		JFrame frame=new JFrame("도서 창");
		frame.getContentPane().add(bookBook);
		frame.setBounds(440,110,430,700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
