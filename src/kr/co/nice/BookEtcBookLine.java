package kr.co.nice;

import java.sql.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BookEtcBookLine extends JPanel {
	Connection conn;
	Statement stmt;
	ResultSet rs;
	String sql;
	Vector<String> vecName = new Vector<String>();
	Vector<Integer> vecInt = new Vector<Integer>();

	public BookEtcBookLine() {
		AccDb();
		design();
	}

	public void AccDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
		} catch (Exception e) {
			System.out.println("BookEtcBook 연결 실패:" + e);
		}
		init();
	}

	public void init() {
		String sql = "SELECT B_JEMOK,B_DAESU,RANK() OVER(ORDER BY B_DAESU DESC) AS RNK FROM BOOK";

		try {
			stmt = conn.createStatement();
			// System.out.println(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getInt("RNK") >= 6) {
					break;
				} else {
					vecName.add(rs.getString("B_JEMOK"));
					vecInt.add(rs.getInt("B_DAESU"));
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
		this.setLayout(new BorderLayout());
		// create a dataset...
		final CategoryDataset dataset = createDataset();

		// create the chart...
		final JFreeChart chart = createChart(dataset);

		// add the chart to a panel...
		final ChartPanel chartPanel = new ChartPanel(chart);
		this.add(chartPanel);
	}

	private JFreeChart createChart(final CategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createLineChart("Popular Book 5 Chart", // chart
																						// title
				"Book Title", // domain axis label
				"Daesu", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
		);

		// chart.setBackgroundPaint(Color.white);

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.white);

		// customise the range axis...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true);

		// customise the renderer...
		final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		// renderer.setDrawShapes(true);

		renderer.setSeriesStroke(0, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
				new float[] { 10.0f, 6.0f }, 0.0f));
		renderer.setSeriesStroke(1, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
				new float[] { 6.0f, 6.0f }, 0.0f));
		renderer.setSeriesStroke(2, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
				new float[] { 2.0f, 6.0f }, 0.0f));
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	private CategoryDataset createDataset() {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < vecName.size(); i++) {
			dataset.addValue(vecInt.get(i), "BOOT TITLE", vecName.get(i));
		}
		return dataset;
	}

	public static void main(String[] args) {
		BookEtcBookLine chart2 = new BookEtcBookLine();
		JFrame frame = new JFrame();
		frame.getContentPane().add(chart2);
		frame.setBounds(200, 200, 580, 400);
		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}