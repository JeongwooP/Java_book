package kr.co.nice;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.border.BevelBorder;

public class BookMain extends JFrame implements ActionListener {
	public static JMenuItem book_dae, book_ban, book_customer, book_book, book_etc, book_close;
	public static JInternalFrame childWinDae, childWinBan, childWinCustomer, childWinBook, childWinEtc;
	public JDesktopPane desktopPane = new JDesktopPane(); // frame 생성

	public BookMain(String str) {
		super(str);
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

		JMenuBar mbar = new JMenuBar(); // 메뉴바
		mbar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		JMenu mbook = new JMenu("메뉴 선택");
		book_dae = new JMenuItem("대여관리");
		book_ban = new JMenuItem("반납관리");
		book_customer = new JMenuItem("고객관리");
		book_book = new JMenuItem("도서관리");
		book_etc = new JMenuItem("기타");
		book_close = new JMenuItem("종료");
		mbook.add(book_dae);
		mbook.add(book_ban);
		mbook.add(book_customer);
		mbook.add(book_book);
		mbook.add(book_etc);
		mbook.addSeparator();
		mbook.add(book_close);
		book_dae.addActionListener(this);
		book_ban.addActionListener(this);
		book_customer.addActionListener(this);
		book_book.addActionListener(this);
		book_etc.addActionListener(this);
		book_close.addActionListener(this);

		mbar.add(mbook);
		setJMenuBar(mbar); // frame에 메뉴바 장착

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(0, 0);
		this.setSize(dimension.width, dimension.height);
		this.setVisible(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(book_dae)) {
			createListen("대여", 580, 400, "dae");
			BookDaeyeo daeyeo = new BookDaeyeo();
			childWinDae.getContentPane().add(daeyeo);
			childWinDae.setLocation(10, 10);
			desktopPane.add(childWinDae);
			this.getContentPane().add(desktopPane);
			childWinDae.show();
			book_dae.setEnabled(false);
		} else if (e.getSource().equals(book_ban)) {
			createListen("반납", 500, 400, "ban");
			BookBannap bannap = new BookBannap();
			childWinBan.getContentPane().add(bannap);
			childWinBan.setLocation(110, 60);
			desktopPane.add(childWinBan);
			this.getContentPane().add(desktopPane);
			childWinBan.show();
			book_ban.setEnabled(false);
			
		} else if (e.getSource().equals(book_customer)) {
			createListen("고객", 430, 400, "customer");
			BookCustomer customer = new BookCustomer();
			childWinCustomer.getContentPane().add(customer);
			childWinCustomer.setLocation(210, 110);
			desktopPane.add(childWinCustomer);
			this.getContentPane().add(desktopPane);
			childWinCustomer.show();
			book_customer.setEnabled(false);
		} else if (e.getSource().equals(book_book)) {
			createListen("도서", 430, 680, "book");
			BookBook book = new BookBook();
			childWinBook.getContentPane().add(book);
			childWinBook.setLocation(310, 20);
			desktopPane.add(childWinBook);
			this.getContentPane().add(desktopPane);
			childWinBook.show();
			book_book.setEnabled(false);
		} else if (e.getSource().equals(book_etc)) {
			createListen("기타", 500, 400, "etc");
			BookEtc etc = new BookEtc();
			childWinEtc.getContentPane().add(etc);
			childWinEtc.setLocation(410, 210);
			desktopPane.add(childWinEtc);
			this.getContentPane().add(desktopPane);
			childWinEtc.show();
			book_etc.setEnabled(false);
		} else if (e.getSource().equals(book_close)) {
			int re = JOptionPane.showConfirmDialog(this, "정말 종료할까요?", "선택", JOptionPane.YES_NO_OPTION);
			if (re == JOptionPane.NO_OPTION)
				return;

			System.exit(0);
		}
	}

	private void createListen(String title, int w, int h, String str) {
		if (str.equals("dae")) {
			childWinDae = new JInternalFrame(title, false, false, false, true);
			childWinDae.setSize(w, h);
		} else if (str.equals("ban")) {
			childWinBan = new JInternalFrame(title, false, false, false, true);
			childWinBan.setSize(w, h);
		} else if (str.equals("customer")) {
			childWinCustomer = new JInternalFrame(title, false, false, false, true);
			childWinCustomer.setSize(w, h);
		} else if (str.equals("book")) {
			childWinBook = new JInternalFrame(title, false, false, false, true);
			childWinBook.setSize(w, h);
		} else if (str.equals("etc")) {
			childWinEtc = new JInternalFrame(title, false, false, false, true);
			childWinEtc.setSize(w, h);
		}
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		int y = calendar.get(Calendar.YEAR);
		int m = calendar.get(Calendar.MONTH) + 1;
		int d = calendar.get(Calendar.DATE);
		new BookMain("도서관리 ver 1.0   ★ 오늘은 " + y + "년 " + m + "월 " + d + "일");
	}
}
