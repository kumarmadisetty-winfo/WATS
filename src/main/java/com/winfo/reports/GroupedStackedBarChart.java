package com.winfo.reports;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.stream.IntStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;

public class GroupedStackedBarChart {

	
	public void createBar(Document document, Map<String, Map<String, Integer>> testRuns) throws DocumentException, IOException {
		CategoryDataset dataset = createDataset(testRuns);
		JFreeChart chart = createChart(dataset);

		int width = 560; // Adjust the width of the chart image
		int height = 300; // Adjust the height of the chart image

		// Create a temporary ChartPanel to set the background color to white
		ChartPanel tempChartPanel = new ChartPanel(chart);
		tempChartPanel.setBackground(Color.WHITE);

		// Create an image from the chart
		BufferedImage chartImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = chartImage.createGraphics();
		tempChartPanel.setBounds(0, 0, width, height);
		tempChartPanel.paint(graphics2d);
		graphics2d.dispose();

		// Convert the BufferedImage to an Image object
		Image chartPdfImage = Image.getInstance(chartImage, null);

		// Set the position and alignment of the chart image in the PDF
		chartPdfImage.setAlignment(Image.ALIGN_CENTER);

		IntStream.range(0, 4).forEach(i-> {
			try {
				document.add(new Paragraph("\n"));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		});

		// Add the chart image to the PDF
		try {
			document.add(chartPdfImage);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	private static CategoryDataset createDataset(Map<String, Map<String, Integer>> testRuns) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(Map.Entry<String, Map<String, Integer>> entry : testRuns.entrySet()) {
			dataset.addValue(entry.getValue().get("pass"), "Passes", entry.getKey());
			dataset.addValue(entry.getValue().get("fail"), "Failed", entry.getKey());
		}
		return dataset;
	}

	private static JFreeChart createChart(CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);

		CategoryPlot plot = chart.getCategoryPlot();
		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		renderer.setItemMargin(0.1);
		renderer.setSeriesPaint(0, new Color(0, 204, 136));
		renderer.setSeriesPaint(1, new Color(238, 85, 0));
		renderer.setDrawBarOutline(false);
		renderer.setMaximumBarWidth(0.05);
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(new Color(225, 230, 235));
		  NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
		    double minValue = 0.0;
		    double maxValue = 100.0;
		    yAxis.setRange(minValue, maxValue);
		return chart;
	}



}
