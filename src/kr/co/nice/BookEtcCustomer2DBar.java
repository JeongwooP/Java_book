package kr.co.nice;

import java.awt.*;
import java.sql.*;
import java.util.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;

public class BookEtcCustomer2DBar extends JPanel {
	Connection conn;
	Statement stmt;
	ResultSet rs;
	String sql;

	Vector<String> vecName = new Vector<String>(); // 고객 이름 저장
	Vector<Integer> vecInt = new Vector<Integer>(); // 고객 대여횟수 저장

	public BookEtcCustomer2DBar() {
		AccDb();
		design();
	}

	public void AccDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
		} catch (Exception e) {
			System.out.println("BookEtcCustomer2DBar 연결 실패:" + e);
		}

		init();
	}

	public void init() {
		String sql = "SELECT C_IRUM,C_DAESU,RANK() OVER(ORDER BY C_DAESU DESC) AS RNK  FROM CUSTOMER";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			// 내장함수인 랭크함수를 사용 순위를 붙여서 5위까지 중복자 포함 받아온다..
			while (rs.next()) {
				if (rs.getInt("RNK") >= 6) {
					break;
				} else {
					vecName.add(rs.getString("C_IRUM"));
					vecInt.add(rs.getInt(2));
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(this, "자료 등록 오류입니다.");
			sqle.printStackTrace();
		}
	}

	public void design() {
		final CategoryDataset dataset = createDataset();
		// create a dataset...
		final JFreeChart chart = createChart(dataset);
		// create the chart...
		final ChartPanel chartPanel = new ChartPanel(chart);
		// add the chart to a panel...
		// ////////////////////////////////
		this.setLayout(new BorderLayout());
		this.add(chartPanel);
	}

	// /////////////////////////////////////////////////////////////////
	private JFreeChart createChart(final CategoryDataset dataset) {
		// 차트의 초기설정 && 데이타셋을 받아와서 장착한다..
		final JFreeChart chart = ChartFactory.createBarChart("Customer Top 5 Chart", // chart
																						// title
				"Customer Name", // domain axis label
				"Daesu", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
		);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, Color.lightGray);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	private CategoryDataset createDataset() {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < vecName.size(); i++) {
			dataset.addValue(vecInt.get(i), vecName.get(i), "고객명");
		}
		return dataset;
	}

	public static void main(String[] args) {
		BookEtcCustomer2DBar chart1 = new BookEtcCustomer2DBar();
		JFrame frame = new JFrame();
		frame.getContentPane().add(chart1);
		frame.setBounds(200, 200, 580, 400);
		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}