package gpovallas.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;

public class MyPieChart extends PieChart{

	
	public MyPieChart(Context context) {
		super(context);

		// customize legends
		Legend l = getLegend();
		l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setXEntrySpace(7);
		l.setYEntrySpace(5);
	}

	public MyPieChart(Context context, AttributeSet attrs) {
		super(context, attrs);

		// customize legends
		Legend l = getLegend();
		l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setXEntrySpace(7);
		l.setYEntrySpace(5);
	}

	public MyPieChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// customize legends
		Legend l = getLegend();
		l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setXEntrySpace(7);
		l.setYEntrySpace(5);
	}


	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		setDescription("");
		setHoleRadius(0f);
		setDrawHoleEnabled(false);
		setRotationEnabled(true);
		setHoleColorTransparent(true);
		setDrawSliceText(false);
		setTransparentCircleRadius(0);
		setRotationEnabled(false);
		setHighlightEnabled(false);
		highlightValues(null);

		setNoDataText("Actualmente no hay datos disponibles");
		Paint p = getPaint(Chart.PAINT_INFO);
		p.setTextSize(14);
		p.setColor(Color.parseColor("#B7CED0"));

	}
	
}
