/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ounl.lifelonglearninghub.nfcecology.db.charts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

/**
 * Scatter demo chart.
 */
public class ScatterChart extends AbstractChart {
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Scatter chart";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "Randomly generated values for the scatter chart";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
    String[] titles = new String[] { "0-5 mins", "5-20 mins", "20-60 mins", "1-2 hours", "More than 2 hours" };
    List<double[]> x = new ArrayList<double[]>();
    List<double[]> values = new ArrayList<double[]>();
    int count = 20;
    int length = titles.length;
    Random r = new Random();
    for (int i = 0; i < length; i++) {
      double[] xValues = new double[count];
      double[] yValues = new double[count];
      for (int k = 0; k < count; k++) {
        xValues[k] = k + r.nextInt() % 10;
        yValues[k] = k * 2 + r.nextInt() % 10;
      }
      x.add(xValues);
      values.add(yValues);
    }
    int[] colors = new int[] { Color.BLUE, Color.CYAN, Color.MAGENTA, Color.LTGRAY, Color.GREEN };
    PointStyle[] styles = new PointStyle[] { PointStyle.X, PointStyle.DIAMOND, PointStyle.TRIANGLE,
        PointStyle.SQUARE, PointStyle.CIRCLE };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    setChartSettings(renderer, "Distribution of learning moments by duration", "Day of the week", "Hour of the day", 1, 7, 0, 24, Color.GRAY, Color.LTGRAY);
    renderer.setXLabels(10);
    renderer.setYLabels(10);
    
    renderer.setApplyBackgroundColor(true);
    renderer.setBackgroundColor(Color.WHITE);
    renderer.setMarginsColor(Color.WHITE);     
    
    length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    return ChartFactory.getScatterChartIntent(context, buildDataset(titles, x, values), renderer);
  }

}
