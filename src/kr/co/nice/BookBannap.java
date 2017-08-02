package kr.co.nice;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class BookBannap extends JPanel implements ActionListener{
	JTextField txtBbun,txtBjemok,txtBdaeil,txtBdaebun,txtJemok;
	static JTextField txtBbanil;
	JButton btnBbun,btnChange,btnBannap,btnNew,btnClose;
	DefaultTableModel mod;
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs1, rs2;
	String sql;
	
	static JFrame calFrame; 
	
	
	public BookBannap(){
		design();
		addListener();
		accDB();
	}
	private void addListener() {
		btnBbun.addActionListener(this);
		btnChange.addActionListener(this);
		btnBannap.addActionListener(this);
		btnNew.addActionListener(this);
		btnClose.addActionListener(this);
	}
	private void accDB() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.println("로딩 실패:" + e);
		}
		
		daeDisplay(); //대여 중인 도서자료 테이블에 출력
	}
	
	private void daeDisplay(){
		mod.setNumRows(0);	//표 초기화
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
			
			sql = "select b_bun, b_jemok, c_bun, c_irum, b_daeil from book inner join customer on b_daebun = c_bun order by b_daeil desc, b_bun asc";
			pstmt = conn.prepareStatement(sql);
			rs1 = pstmt.executeQuery();
			int count = 0;
			while(rs1.next()){
				String[] imsi = {
						rs1.getString("b_bun"),
						rs1.getString("b_jemok"),
						rs1.getString("c_bun"),
						rs1.getString("c_irum"),
						rs1.getString("b_daeil")	
				};
				mod.addRow(imsi);
				count++;
			}
			String[] imsi2 = {null, "전체 건수:" + count};
			mod.addRow(imsi2);
			rs1.close();
	
		} catch (Exception e) {
			System.out.println("daeDisplay err : " + e);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnBbun)) {	//반납할 도서번호 입력
			if(txtBbun.getText().equals("")){
				txtBbun.requestFocus();
				JOptionPane.showMessageDialog(this, "반납할 도서번호를 입력하시오.");
				return ;
			}
			try {
				conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
				sql = "select * from book where b_bun= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, txtBbun.getText());
				rs1 = pstmt.executeQuery();
				if(!rs1.next()){	//등록되지 않은 도서번호
					JOptionPane.showMessageDialog(this, "등록된 도서번호가 아닙니다.\n확인바람");
					txtBbun.requestFocus();
					return;
				}
				txtBjemok.setText(rs1.getString("b_jemok"));
				
				//정식으로 대여된 도서 여부 판단
				if(rs1.getString("b_daeyn").equals("n")){
					JOptionPane.showMessageDialog(this, "정식으로 대여된 도서가 아닙니다.\n확인바람");
					
					txtBbun.setText("");
					txtBjemok.setText("");
				    txtBbun.requestFocus();
				    return;
				}
				
				//반납작업 시작
				txtBdaeil.setText(rs1.getString("b_daeil"));
				Calendar calendar = Calendar.getInstance();
				String nalja = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DATE));
				txtBbanil.setText(nalja);
				txtBdaebun.setText(rs1.getString("b_daebun"));
				
				
				//반납되는 도서제목을 customer 테이블의 c_memo에서 제거---
				sql = "select * from customer where c_bun=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, txtBdaebun.getText());
				rs2 = pstmt.executeQuery();
				rs2.next();
				txtJemok.setText(rs2.getString("c_memo"));
				
				String ban_jemok = txtJemok.getText();
				int start = ban_jemok.indexOf(txtBjemok.getText());
				int end = txtBjemok.getText().length();
//				System.out.println(start + " " + end);
				txtJemok.setSelectionStart(start);
				txtJemok.setSelectionEnd(start + end + 1);
				txtJemok.requestFocus();
				txtJemok.replaceSelection(""); //반납도서 제목 없애기
				
				//마지막 콤마 제거하기
				try {
					String str = txtJemok.getText();
					int a = str.length() - 1;
					String ss = str.substring(a);
					if(ss.equals(",")) 
						str = str.substring(0, a);
					txtJemok.setText(str);
					
				} catch (Exception e2) {
					// 반납할 도서가 하나일 경우는 무시
				}
				
				btnBannap.setEnabled(true);	//반납버튼 활성화
			} catch (Exception e2) {
				System.out.println("반납 오류:" + e2);
			} finally {
				try {
					if (rs1 != null)
						rs1.close();
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}else if(e.getSource().equals(btnChange)) {
			BookCal bookCal = new BookCal();
			calFrame = new JFrame("반납일 변경");
			calFrame.add(bookCal);
			calFrame.setBounds(300, 300, 250, 200);
			calFrame.setVisible(true);
		}else if(e.getSource().equals(btnBannap)) {	//반납
	
			try{
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
			
			//고객갱신(c_memo)
			sql = "update customer set c_memo=? where c_bun =?"; /*라커 목록 업데이트 사용 가능*/
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, txtJemok.getText());
			pstmt.setString(2, txtBdaebun.getText());
			pstmt.executeUpdate();

			//도서갱신(b_daeyn, b_daebun, b_banil)
			sql = "update book set b_daeyn = 'n', b_daebun = 0, b_banil = ? where b_bun = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, txtBbanil.getText());
			pstmt.setString(2, txtBbun.getText());
			pstmt.executeUpdate();
			
			btnBannap.setEnabled(false);
			btnNew.setEnabled(false);
			
			daeDisplay(); //갱신 후 대여도서 목록 다시 보기
			
			} catch (Exception e2) {
				System.out.println("반납에러 :" + e2);
			}finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		
			
		}else if(e.getSource().equals(btnNew)) {
			txtBbun.setText("");
			txtBjemok.setText("");
			txtBdaeil.setText("");
			txtBbanil.setText("");
			txtBdaebun.setText("");
			txtJemok.setText("");
			txtBbun.requestFocus();
			btnNew.setEnabled(false);
			
		}else if(e.getSource().equals(btnClose)) {
			BookMain.book_ban.setEnabled(true);
			BookMain.childWinBan.dispose();	
		}
	}
	
	public void design(){
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		//도서정보 패널========================
		JPanel bookPn=new JPanel(new GridLayout(3,1));
		bookPn.setBorder(new TitledBorder(new TitledBorder("고객 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
		JPanel bPn1=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bPn2=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bPn3=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bPn4=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		txtBbun=new JTextField("",5);
		txtBjemok=new JTextField("",20);
		txtBdaeil=new JTextField("",10);
		txtBbanil=new JTextField("",10);
		txtBdaebun=new JTextField("",5);
		txtJemok=new JTextField("",25);  //반납되는 도서 제목을 고객메모에서 제거하기 위함
		
		btnBbun=new JButton("확인");
		btnBbun.setMargin(new Insets(0, 3, 0, 3));
		btnChange=new JButton("변경");
		btnChange.setMargin(new Insets(0, 3, 0, 3));

		bPn1.add(new JLabel("번호:"));
		bPn1.add(txtBbun);
		bPn1.add(btnBbun);
		
		bPn2.add(new JLabel("제목:"));
		bPn2.add(txtBjemok);
		txtBjemok.setEditable(false);
		
		bPn3.add(new JLabel("대여일:"));
		bPn3.add(txtBdaeil);	
		txtBdaeil.setEditable(false);
		bPn3.add(new JLabel("      반납일:"));
		bPn3.add(txtBbanil);
		bPn3.add(btnChange);	
		
		bPn4.add(new JLabel("대여자 번호:"));
		bPn4.add(txtBdaebun);
		bPn4.add(txtJemok);   //고객메모란의 대여도서 제목중 반납되는 비디오 제목만 제거하기 위해 사용  
		txtJemok.setVisible(true); //숨긴다.
		txtBdaebun.setEditable(false);
		
		JPanel bottomPn=new JPanel();
		bottomPn.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		btnBannap=new JButton("반납 확인");
		btnNew=new JButton("새로 입력");
		btnClose=new JButton(" 닫 기 ");
		
		bottomPn.add(btnBannap);
		bottomPn.add(btnNew);
		JLabel lbl=new JLabel("    "); 
		bottomPn.add(lbl);
		bottomPn.add(btnClose);
		
		bookPn.add(bPn1);  bookPn.add(bPn2); 	bookPn.add(bPn3);	
		
		this.add(bookPn);
		this.add(bPn4);
		this.add(bottomPn);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//도서 목록 테이블 삽입
		String[][]data=new String[0][4];
		String []cols={"번호","제목","대번","이름","대여일"};
		mod=new DefaultTableModel(data,cols){ //테이블 내용 수정 불가
			    public boolean isCellEditable(int rowIndex, int mColIndex) {
				   return false;
				}
			   };
		JTable tab=new JTable(mod);
		tab.getColumnModel().getColumn(0).setPreferredWidth(20);
		tab.getColumnModel().getColumn(1).setPreferredWidth(150);
		tab.getColumnModel().getColumn(2).setPreferredWidth(20);
		tab.getColumnModel().getColumn(3).setPreferredWidth(30);
		tab.setSelectionBackground(Color.green);
		JScrollPane pa=new JScrollPane(tab);
		this.add(pa);
		
		btnBannap.setEnabled(false);  //반납 버튼 비활성화
		btnNew.setEnabled(false);     
	}
	
	public static void main(String[] args) {
		BookBannap bookBannap = new BookBannap();
		JFrame frame=new JFrame("반납 창");
		frame.getContentPane().add(bookBannap);
		frame.setResizable(false);
		frame.setBounds(200,200,500,400);
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}