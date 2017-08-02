package kr.co.nice;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class BookCustomer extends JPanel implements ActionListener{
	JTextField txtCbun,txtCirum,txtCjunhwa,txtCjuso,txtCdaesu;
	JTextArea taCmemo;
	JButton btnInsert,btnOk,btnUpdate,btnDel,btnFind,btnOption,btnClose;
	JButton btnF,btnP,btnN,btnL;
	JLabel lblRec;
	
	int iTotal = 0;	// 자료의 갯수
	int iLast = 0; 	// 마지막 레코드 번호
	boolean isInsert = false;	// Insert 버튼 눌림 여부
	boolean isUpdate = false;	// Update 버튼 눌림 여부
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	String sql;
	
	// 생성자
	public BookCustomer(){
		design();
		addListener();
		accDB();
		init();
		display();
	}
	
	public void addListener() {
		btnInsert.addActionListener(this);
		btnOk.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDel.addActionListener(this);
		btnFind.addActionListener(this);
		btnOption.addActionListener(this);
		btnClose.addActionListener(this);
		btnF.addActionListener(this);
		btnP.addActionListener(this);
		btnN.addActionListener(this);
		btnL.addActionListener(this);
	}

	public void accDB()	{
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.println("로딩 실패:" + e);
		}
		
	}
	
	private void init(){
		try {
			sql = "select * from customer order by c_bun asc";
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			rs.last();
			iTotal = rs.getRow();	//전체 자료 수 얻기
			iLast = rs.getInt("c_bun");	//마지막 고객번호 얻기(추가용)
//			System.out.println(iTotal + " " + iLast);
			rs.first();
			
		} catch (Exception e) {
			System.out.println("init err:" + e);
		}
		
	}
	
	private void display() {
		try {
			txtCbun.setText(rs.getString("c_bun"));
			txtCirum.setText(rs.getString("c_irum"));
			txtCjunhwa.setText(rs.getString("c_junhwa"));
			txtCdaesu.setText(rs.getString("c_daesu"));
			txtCjuso.setText(rs.getString("c_juso"));
			taCmemo.setText(rs.getString("c_memo"));
			lblRec.setText(rs.getRow() + " / " + iTotal); //(1/10)
		} catch (Exception e) {
			System.out.println("display err:" + e);
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
		if(e.getSource().equals(btnInsert)) {  /*라커 열기 닫기에 사용 가능*/
			if(isInsert == false){
				btnInsert.setText("취소");
				btnOk.setEnabled(true);
				isInsert = true;
				
				txtCirum.setEditable(true);
				txtCjunhwa.setEditable(true);
				txtCjuso.setEditable(true);
				
				txtCbun.setText(String.valueOf(iLast + 1));
				txtCirum.setText("");
				txtCjunhwa.setText("");
				txtCjuso.setText("");
				txtCdaesu.setText("");
				taCmemo.setText("");
				txtCirum.requestFocus();
			}else{
				btnInsert.setText("신규");
				btnOk.setEnabled(false);
				isInsert = false;
				
				txtCirum.setEditable(false);
				txtCjunhwa.setEditable(false);
				txtCjuso.setEditable(false);
				display();
			}
		}else if(e.getSource().equals(btnOk)) {	//라커 물품 등록에 사용 가능
			try {
				sql = "insert into customer values(?, ?, ?, ?, 0,'')";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, txtCbun.getText());
				pstmt.setString(2, txtCirum.getText());
				pstmt.setString(3, txtCjunhwa.getText());
				pstmt.setString(4, txtCjuso.getText());
				pstmt.executeUpdate();	//추가 완료
				
				init(); //전체 건수 출력
				rs.last();	//추가된 자료를 출력해야 하므로
				display();
				
				txtCirum.setEditable(false);
				txtCjunhwa.setEditable(false);
				txtCjuso.setEditable(false);
				btnInsert.setText("신규");
				btnOk.setEnabled(false);
				isInsert = false;
	
			
				
			} catch (Exception e2) {
				System.out.println("신규확인 오류:" + e2);
			}
		}else if(e.getSource().equals(btnUpdate)) { //수정시 레코드(<< < > >>)는 false로 만들기
			if(isUpdate == false){
				btnUpdate.setText("완료");
				isUpdate = true;
				
				txtCirum.setEditable(true);
				txtCjunhwa.setEditable(true);
				txtCjuso.setEditable(true);
				
			}else{
				btnUpdate.setText("수정");
				isUpdate = false;
				//수정작업-------
				try {
					sql = "update customer set c_irum = ?, c_junhwa=?,c_juso=? where c_bun=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, txtCirum.getText());
					pstmt.setString(2, txtCjunhwa.getText());
					pstmt.setString(3, txtCjuso.getText());
					pstmt.setString(4, txtCbun.getText());
					pstmt.executeUpdate();
					
					int currentRow = rs.getRow(); //현재 레코드 순서 기억
					init();
					rs.absolute(currentRow);
					display();
					
				} catch (Exception e2) {
					System.out.println("수정 오류:" + e2);
				}
				//------------
				
				
				txtCirum.setEditable(false);
				txtCjunhwa.setEditable(false);
				txtCjuso.setEditable(false);
			}
		}else if(e.getSource().equals(btnDel)) {
			int re = JOptionPane.showConfirmDialog(this, "정말 삭제할까요?", "삭제", JOptionPane.YES_NO_OPTION); 	//확인 차원 라커에서 사용 "정말 꺼낼까요?"
			
			if(re == JOptionPane.YES_OPTION){
				try {
					if(rs.getRow() == 0){
						JOptionPane.showMessageDialog(this, "삭제할 자료가 없어요");
						return;
					}
					//현재 대여중인 고객은 삭제 불가
					if(taCmemo.getText().equals("")){
						int currentRow = rs.getRow();
						sql = "delete from customer where c_bun=?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, txtCbun.getText());
						pstmt.executeUpdate();
						
						init();
						
						if(currentRow == 1){
							
						}else{
							rs.absolute(currentRow - 1);
						}
						
						display();
						
					}else{
						JOptionPane.showMessageDialog(this, "도서 반납 후 삭제 가능!");
						}
					} catch (Exception e2) {
					System.out.println("삭제 오류:" + e2);
				}
				
			}
		
		
		}else if(e.getSource().equals(btnFind)) { /*라커에 기능 추가 가능*/
			//고객번호 검색
			String fbun = JOptionPane.showInputDialog(this, "몇 번 고객을 검색하시겠습니까?");
			if(fbun == null) return;
			
			try {
				int currentRow = rs.getRow(); //검색결과가 없읅 경우 대비
				rs.beforeFirst();
				int couFind = 0;
				while(rs.next()){
					String str = rs.getString("c_bun");
					if(fbun.equals(str)){
						display();
						couFind++;
						break;
					}
				}
				if(couFind == 0){
					JOptionPane.showMessageDialog(this, "검색 결과가 없습니다");
				}
			} catch (Exception e2) {
				System.out.println("검색 오류" + e2);
			}
		}else if(e.getSource().equals(btnOption)) {
			//생략
		}else if(e.getSource().equals(btnClose)) {
			BookMain.book_customer.setEnabled(true);
			BookMain.childWinCustomer.dispose();
		}
	}
	
	public void design(){
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		//고객정보 패널========================
		JPanel customerPn=new JPanel(new GridLayout(4,1));
		customerPn.setBorder(new TitledBorder(new TitledBorder("고객 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
		JPanel cPn1=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel cPn2=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel cPn3=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel cPn4=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		txtCbun=new JTextField("",5);
		txtCirum=new JTextField("",10);
		txtCjunhwa=new JTextField("",15);
		txtCjuso=new JTextField("",28);
		txtCdaesu=new JTextField("",5);
		taCmemo=new JTextArea(2,28);
		JScrollPane scroll=new JScrollPane(taCmemo);
		taCmemo.setBackground(Color.lightGray);
		
		txtCbun.setEditable(false);
		txtCirum.setEditable(false);
		txtCjunhwa.setEditable(false);
		txtCjuso.setEditable(false);
		txtCdaesu.setEditable(false);
		taCmemo.setEditable(false);
		
		btnInsert=new JButton("신규");
		btnOk=new JButton("확인");
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
		cPn1.add(new JLabel("번호 :"));
		cPn1.add(txtCbun);
		cPn1.add(new JLabel("       이름 :"));
		cPn1.add(txtCirum);
		
		cPn2.add(new JLabel("전화 :"));
		cPn2.add(txtCjunhwa);	
		cPn2.add(new JLabel("      대여횟수 :"));
		cPn2.add(txtCdaesu);
		
		cPn3.add(new JLabel("주소 :"));
		cPn3.add(txtCjuso);
		
		cPn4.add(new JLabel("메모 :"));
		cPn4.add(scroll);

		customerPn.add(cPn1);  customerPn.add(cPn2); 	customerPn.add(cPn3);	customerPn.add(cPn4);
		this.add(customerPn);
		
		btnOk.setEnabled(false);
		
		//레코드 이동 패널========================
		JPanel movePn=new JPanel();
		movePn.setBorder(new TitledBorder(new TitledBorder("레코드 이동"), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
		movePn.add(btnF);
		movePn.add(btnP);
		movePn.add(lblRec);
		movePn.add(btnN);
		movePn.add(btnL);
		
		this.add(movePn);
		
        //명령 버튼 패널========================
		JPanel bottomPn1=new JPanel();
		bottomPn1.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		bottomPn1.add(btnInsert);
		bottomPn1.add(btnOk);
		JLabel lbl1=new JLabel("    "); 
		bottomPn1.add(lbl1);
		bottomPn1.add(btnUpdate);
		bottomPn1.add(btnDel);
		
		JPanel bottomPn2=new JPanel();
		bottomPn2.add(btnFind);
		bottomPn2.add(btnOption);
		JLabel lbl2=new JLabel("                          "); 
		bottomPn2.add(lbl2);
		bottomPn2.add(btnClose);
		
		this.add(bottomPn1);
		this.add(bottomPn2);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	public static void main(String[] args) {
		BookCustomer bookCustomer=new BookCustomer();
		JFrame frame=new JFrame("고객 창");
		frame.getContentPane().add(bookCustomer);
		frame.setBounds(200,200,430,450);
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}