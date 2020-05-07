package com.udc.master.tfm.tracksports.graph;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.androidplot.Plot.BorderStyle;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

/**
 * Fragmento que representa una grafica
 * @author a.oteroc
 *
 */
public class GraphFragment extends Fragment {

	/** Grafica donde se muestra la informacion del recorrido */
	private XYPlot plot;
    /** Lista de series que se incluyen en la grafica */
    private List<XYSeries> series = new ArrayList<XYSeries>();

	/**
	 * Metodo que anade una serie a la grafica
	 */
	public void addSeries(Graph graph) {
		if (!series.contains(graph.getSeries())) {
			series.add(graph.getSeries());
			LineAndPointFormatter formatter = new LineAndPointFormatter(
					graph.getSeriesColor(), 
					Color.BLACK, 
					graph.isFillColor() ? graph.getSeriesColor() : null, 
					new PointLabelFormatter(graph.isShowPointValues() ? graph.getSeriesColor() : Color.TRANSPARENT));
			plot.addSeries(graph.getSeries(), formatter);
			setPlotInfo(graph);
			plot.redraw();
		}
	}
	
	/**
	 * Metodo que anade una listas de series a la grafica
	 * @param graphs
	 */
	public void addSeries(List<Graph> graphs) {
		for (Graph graph : graphs) {
			addSeries(graph);
		}
	}
	
	/**
	 * Metodo que elimina todas las series de la grafica
	 */
	public void removeSeries() {
		for (XYSeries serie : series) {
			plot.removeSeries(serie);
		}
		series.clear();
		plot.redraw();
	}
	
	/**
	 * Metodo que incializa la grafica
	 * @param plotView
	 */
	public void initPlot(View plotView) {
		//Se configura los detalles de la grafica por defecto
		plot = (XYPlot) plotView;
        plot.getGraph().setLinesPerRangeLabel(1);
		plot.getGraph().setLinesPerRangeLabel(1);
       /* plot.getGraphWidget().setDomainLabelOrientation(-45);
        plot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
        plot.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);
        plot.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
        plot.getTitleWidget().getLabelPaint().setColor(Color.BLACK);*/
        plot.setBorderStyle(BorderStyle.NONE, null, null);
      /*  plot.getGraphWidget().setMarginTop(25);
        plot.getGraphWidget().setMarginBottom(60);
        plot.getGraphWidget().setMarginLeft(60);
        plot.getGraphWidget().setMarginRight(25);*/
	}

	/**
	 * Metodo que setea la informacion de la grafica
	 * @param graph
	 */
	private void setPlotInfo(Graph graph) {
		if (graph.getPlotTite() != null) {
			plot.setTitle(graph.getPlotTite());
		} else {
			//plot.getGraphWidget().setMarginTop(0);
			//plot.getTitleWidget().getLabelPaint().setColor(Color.TRANSPARENT);
		}
		if (graph.getxLabel() != null) {
			plot.setDomainLabel(graph.getxLabel());
		} else {
			//plot.getGraphWidget().setMarginBottom(0);
			//plot.getGraphWidget().getDomainLabelPaint().setColor(Color.TRANSPARENT);
			//plot.getDomainLabelWidget().getLabelPaint().setColor(Color.TRANSPARENT);
		}
		if (graph.getxFormat() != null) {
			//plot.getGraphWidget().setDomainValueFormat(graph.getxFormat());
		}
		if (graph.getyLabel() != null) {
			plot.setRangeLabel(graph.getyLabel());
		} else {
			//plot.getGraphWidget().setMarginLeft(0);
			//plot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);
			//plot.getRangeLabelWidget().getLabelPaint().setColor(Color.TRANSPARENT);
		}
		if (graph.getyFormat() != null) {
			//plot.getGraphWidget().setRangeValueFormat(graph.getyFormat());
		}
		//plot.getLegendWidget().setVisible(graph.isShowLegend());
	}
}
