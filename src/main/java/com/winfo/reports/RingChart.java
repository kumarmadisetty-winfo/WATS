package com.winfo.reports;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleInsets;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class RingChart {
	public static final Logger logger = Logger.getLogger(RingChart.class);

	public void createPDF(PdfWriter writer, int pass, int fail) throws Exception {
		try {
			PdfContentByte cb = writer.getDirectContent();
			addChart(cb, pass, fail);
		} catch (Exception e) {
			logger.error("Exception occured while generating the RingChart");
		} finally {
		}
	}

	private void addChart(PdfContentByte cb, int pass, int fail) throws Exception, IOException {
		long pctPM = Math.round(20);
		long pctOA = Math.round(15);
		long pctWPI = Math.round(5);
		long pctTDF = Math.round(25);
		long pctNE = 100 - (pctPM + pctOA + pctWPI + pctTDF);
		long pctEngaged = (((pass * 100) / (pass + fail)));

		JFreeChart chart = createChart(pass, fail, pctWPI, pctTDF, pctNE);

		int width = 200;
		int height = 100;
		PdfTemplate template = cb.createTemplate(width, height);
		Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
		Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
		chart.draw(graphics2d, rectangle2d);
		graphics2d.dispose();
		cb.addTemplate(template, 640, 1020);

		Font percentFont1 = createFont("OpenSans-Light.ttf", 22, 116, 112, 100);
		Font percentFont2 = createFont("OpenSans-Light.ttf", 10, 116, 112, 100);
		if (pctEngaged == 100) {
			// FOR 3 DIGITS PERCENTAGE FORMAT
			addPhrase(cb, String.valueOf(pctEngaged), percentFont1, 755, 1075, 240, 310, 10, Element.ALIGN_RIGHT);
			addPhrase(cb, "%", percentFont2, 765, 1078, 211, 299, 10, Element.ALIGN_RIGHT);
		} else if (pctEngaged >= 10) {
			// FOR 2 DIGITS PERCENTAGE FORMAT
			addPhrase(cb, String.valueOf(pctEngaged), percentFont1, 750, 1075, 210, 310, 10, Element.ALIGN_RIGHT);
			addPhrase(cb, "%", percentFont2, 760, 1078, 211, 299, 10, Element.ALIGN_RIGHT);
		} else {
			// FOR 1 DIGIT PERCENTAGE FORMAT
			addPhrase(cb, String.valueOf(pctEngaged), percentFont1, 745, 1075, 210, 310, 10, Element.ALIGN_RIGHT);
			addPhrase(cb, "%", percentFont2, 755, 1078, 211, 299, 10, Element.ALIGN_RIGHT);
		}
	}

	private void addPhrase(PdfContentByte cb, String strText, Font font, float llx, float lly, float urx, float ury,
			float leading, int alignment) throws DocumentException {
		Phrase phrase = new Phrase(strText, font);
		ColumnText ct = new ColumnText(cb);
		ct.setSimpleColumn(phrase, llx, lly, urx, ury, leading, alignment);
		ct.go();
	}

	private Font createFont(String fileName, float size, int red, int green, int blue)
			throws DocumentException, IOException {
		BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
		Font font = new Font(baseFont, size);
		font.setColor(red, green, blue);
		return font;
	}

	public JFreeChart createChart(long pass, long fail, long pctWPI, long pctTDF, long pctNE) {
		// Set up the data set for the donut/ring chart
		DefaultPieDataset rDataSet = new DefaultPieDataset();
		rDataSet.setValue("Pass", (((pass*100)/(pass+fail))));
		rDataSet.setValue("Fail", (((fail*100)/(pass+fail))));

		// Initialize values
		boolean bShowLegend = false;
		String strTitle = null;

		// Create ring plot
		CustomDonutPlot rPlot = new CustomDonutPlot(rDataSet);
		rPlot.setLabelGenerator(new StandardPieSectionLabelGenerator(Locale.ENGLISH));
		rPlot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
		rPlot.setSectionDepth(0.30);
		JFreeChart chart = new JFreeChart(strTitle, JFreeChart.DEFAULT_TITLE_FONT, rPlot, bShowLegend);
		ChartFactory.getChartTheme().apply(chart);

		// Create the chart
		rPlot.setBackgroundPaint(Color.WHITE);
		rPlot.setCenterText(null);
		rPlot.setLabelGenerator(null);
		rPlot.setOutlineVisible(false);
		rPlot.setShadowGenerator(null);
		rPlot.setSeparatorsVisible(false);
		rPlot.setShadowPaint(null);
		rPlot.setSectionOutlinesVisible(false);
		rPlot.setOuterSeparatorExtension(0);
		rPlot.setInnerSeparatorExtension(0);
		// Set colors of the chart
		rPlot.setSectionPaint("Pass", new Color(0, 255, 0));
		rPlot.setSectionPaint("Fail", new Color(255, 0, 0));

		return chart;
	}

	public static class CustomDonutPlot extends RingPlot {
		private static final long serialVersionUID = 1L;

		public CustomDonutPlot(DefaultPieDataset dataSet) {
			super(dataSet);
		}

		@Override
		protected void drawItem(Graphics2D g2, int section, Rectangle2D dataArea, PiePlotState state, int currentPass) {
			super.drawItem(g2, section, dataArea, state, currentPass);

		}

		@Override
		protected Rectangle2D getArcBounds(Rectangle2D unexploded, Rectangle2D exploded, double angle, double extent,
				double explodePercent) {
			if (explodePercent > 0.0) {
				this.setSectionDepth(0.33);// to match inner arc
				java.awt.geom.Arc2D.Double arc1 = new java.awt.geom.Arc2D.Double(unexploded, angle, extent / 2.0D, 0);
				Point2D point1 = arc1.getEndPoint();
				Rectangle2D mix = new Rectangle2D.Double(exploded.getX(), exploded.getY(), unexploded.getWidth(),
						unexploded.getHeight());
				java.awt.geom.Arc2D.Double arc2 = new java.awt.geom.Arc2D.Double(mix, angle, extent / 2.0D, 0);

				Point2D point2 = arc2.getEndPoint();
				double deltaX = (point1.getX() - point2.getX()) * explodePercent;
				double deltaY = (point1.getY() - point2.getY()) * explodePercent;
				return new java.awt.geom.Rectangle2D.Double(unexploded.getX() - deltaX, unexploded.getY() - deltaY,
						exploded.getWidth(), exploded.getHeight());
			} else {
				this.setSectionDepth(0.3);// default depth
				return super.getArcBounds(unexploded, exploded, angle, extent, explodePercent);
			}
		}

	}

}
