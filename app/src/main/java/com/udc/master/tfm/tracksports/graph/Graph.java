package com.udc.master.tfm.tracksports.graph;

import java.text.Format;

import com.androidplot.xy.XYSeries;

/**
 * Clase que representa una grafica
 * @author a.oteroc
 *
 */
public class Graph {

	/** Grafica con los puntos a mostrar */
	private XYSeries series;
	/** Color de la grafica */
	private int seriesColor;
	/** Indica si se mostara color de relleno o no*/
	private boolean fillColor;
	/** Indica si se muestran los valores de los puntos sobre la grafica */
	private boolean showPointValues;
	/** Titulo de la grafica */
	private String plotTite;
	/** Indica si se muestra la leyenda de la grafica */
	private boolean showLegend;
	/** Titulo de las coordenadas X*/
	private String xLabel;
	/** Titulo de las coordenadas Y*/
	private String yLabel;
	/** Formato de las coordenadas X*/
	private Format xFormat;
	/** Formato de las coordenadas Y*/
	private Format yFormat;


	/**
	 * Constructor por defecto
	 * @param series
	 * @param seriesColor
	 * @param fillColor
	 * @param showPointValues
	 * @param plotTite
	 * @param showLegend
	 * @param xLabel
	 * @param yLabel
	 * @param xFormat
	 * @param yFormat
	 */
	public Graph(XYSeries series, int seriesColor, boolean fillColor,
			boolean showPointValues, String plotTite, boolean showLegend,
			String xLabel, String yLabel, Format xFormat, Format yFormat) {
		this.series = series;
		this.seriesColor = seriesColor;
		this.fillColor = fillColor;
		this.showPointValues = showPointValues;
		this.plotTite = plotTite;
		this.showLegend = showLegend;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.xFormat = xFormat;
		this.yFormat = yFormat;
	}

	/**
	 * @return the series
	 */
	public XYSeries getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(XYSeries series) {
		this.series = series;
	}

	/**
	 * @return the seriesColor
	 */
	public int getSeriesColor() {
		return seriesColor;
	}

	/**
	 * @param seriesColor the seriesColor to set
	 */
	public void setSeriesColor(int seriesColor) {
		this.seriesColor = seriesColor;
	}

	/**
	 * @return the fillColor
	 */
	public boolean isFillColor() {
		return fillColor;
	}

	/**
	 * @param fillColor the fillColor to set
	 */
	public void setFillColor(boolean fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * @return the showPointValues
	 */
	public boolean isShowPointValues() {
		return showPointValues;
	}

	/**
	 * @param showPointValues the showPointValues to set
	 */
	public void setShowPointValues(boolean showPointValues) {
		this.showPointValues = showPointValues;
	}

	/**
	 * @return the plotTite
	 */
	public String getPlotTite() {
		return plotTite;
	}

	/**
	 * @param plotTite the plotTite to set
	 */
	public void setPlotTite(String plotTite) {
		this.plotTite = plotTite;
	}

	/**
	 * @return the showLegend
	 */
	public boolean isShowLegend() {
		return showLegend;
	}

	/**
	 * @param showLegend the showLegend to set
	 */
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	/**
	 * @return the xLabel
	 */
	public String getxLabel() {
		return xLabel;
	}

	/**
	 * @param xLabel the xLabel to set
	 */
	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	/**
	 * @return the yLabel
	 */
	public String getyLabel() {
		return yLabel;
	}

	/**
	 * @param yLabel the yLabel to set
	 */
	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}

	/**
	 * @return the xFormat
	 */
	public Format getxFormat() {
		return xFormat;
	}

	/**
	 * @param xFormat the xFormat to set
	 */
	public void setxFormat(Format xFormat) {
		this.xFormat = xFormat;
	}

	/**
	 * @return the yFormat
	 */
	public Format getyFormat() {
		return yFormat;
	}

	/**
	 * @param yFormat the yFormat to set
	 */
	public void setyFormat(Format yFormat) {
		this.yFormat = yFormat;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Graph [series=" + series + ", seriesColor=" + seriesColor
				+ ", fillColor=" + fillColor + ", showPointValues="
				+ showPointValues + ", plotTite=" + plotTite + ", showLegend="
				+ showLegend + ", xLabel=" + xLabel + ", yLabel=" + yLabel
				+ ", xFormat=" + xFormat + ", yFormat=" + yFormat + "]";
	}
}
