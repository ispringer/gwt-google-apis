/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.visualization.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine.AnnotatedLegendPosition;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine.Options;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine.ScaleType;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine.WindowMode;

import java.util.Date;

/**
 * Tests for the AnnotatedTimeLine class.
 */
public class AnnotatedTimeLineTest extends VisualizationTest {
 
  public void testAnnotatedTimeLine() {
    loadApi(new Runnable() {
      @SuppressWarnings("deprecation")
      public void run() {
        Widget widget;
        Options options = Options.create();
        options.setAllowHtml(false);
        options.setAllValuesSuffix("ski");
        options.setAnnotationsWidth(5);
        options.setColors("blue", "purple", "pink");
        options.setDisplayAnnotations(true);
        options.setDisplayAnnotationsFilter(true);
        options.setDisplayExactValues(true);
        options.setDisplayZoomButtons(true);
        options.setMin(-1);
        options.setZoomEndTime(new Date(109, 1, 1));
        options.setZoomStartTime(new Date(107, 1, 1));

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());
      }
    });
  }

  public void testLegend() {
    loadApi(new Runnable() {
      @SuppressWarnings("deprecation")
      public void run() {
        Widget widget;
        Options options;

        options = Options.create();
        options.setLegendPosition(AnnotatedLegendPosition.NEW_ROW);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());

        options = Options.create();
        options.setLegendPosition(AnnotatedLegendPosition.SAME_ROW);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());
      }
    });
  }

  public void testScaleType() {
    loadApi(new Runnable() {
      @SuppressWarnings("deprecation")
      public void run() {
        Widget widget;
        Options options;

        options = Options.create();
        options.setScaleType(ScaleType.ALLFIXED);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());

        options = Options.create();
        options.setScaleType(ScaleType.ALLMAXIMIZE);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());

        options = Options.create();
        options.setScaleType(ScaleType.FIXED);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());

        options = Options.create();
        options.setScaleType(ScaleType.MAXIMIZE);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());
      }
    });
  }

  public void testWindowMode() {
    loadApi(new Runnable() {
      @SuppressWarnings("deprecation")
      public void run() {
        Widget widget;
        Options options;

        options = Options.create();
        options.setWindowMode(WindowMode.OPAQUE);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());

        options = Options.create();
        options.setWindowMode(WindowMode.TRANSPARENT);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());

        options = Options.create();
        options.setWindowMode(WindowMode.WINDOW);

        widget = AnnotatedTimeLine.createWidget(createAnnotatedDataTable(),
            options, 400, 400);
        RootPanel.get().add(widget);
        System.out.println(widget.getElement().getString());
      }
    });
  }

  @Override
  protected String getVisualizationPackage() {
    return AnnotatedTimeLine.PACKAGE;
  }

  @SuppressWarnings("deprecation")
  private AbstractDataTable createAnnotatedDataTable() {
    int year, month, day;

    DataTable data = DataTable.create();
    data.addColumn(ColumnType.DATE, "Date");
    data.addColumn(ColumnType.NUMBER, "Sold Pencils");
    data.addColumn(ColumnType.STRING, "title1");
    data.addColumn(ColumnType.STRING, "text1");
    data.addColumn(ColumnType.NUMBER, "Sold Pens");
    data.addColumn(ColumnType.STRING, "title2");
    data.addColumn(ColumnType.STRING, "text2");
    data.addRows(6);
    data.setValue(0, 0, new Date(year = 2008 - 1900, month = 1, day = 1));
    data.setValue(0, 1, 30000);
    data.setValue(0, 4, 40645);
    data.setValue(1, 0, new Date(year = 2008 - 1900, month = 1, day = 2));
    data.setValue(1, 1, 14045);
    data.setValue(1, 4, 20374);
    data.setValue(2, 0, new Date(year = 2008 - 1900, month = 1, day = 3));
    data.setValue(2, 1, 55022);
    data.setValue(2, 4, 50766);
    data.setValue(3, 0, new Date(year = 2008 - 1900, month = 1, day = 4));
    data.setValue(3, 1, 75284);
    data.setValue(3, 4, 14334);
    data.setValue(3, 5, "Out of Stock");
    data.setValue(3, 6, "Ran out of stock on pens at 4pm");
    data.setValue(4, 0, new Date(year = 2008 - 1900, month = 1, day = 5));
    data.setValue(4, 1, 41476);
    data.setValue(4, 2, "Bought Pens");
    data.setValue(4, 3, "Bought 200k pens");
    data.setValue(4, 4, 66467);
    data.setValue(5, 0, new Date(year = 2008 - 1900, month = 1, day = 6));
    data.setValue(5, 1, 33322);
    data.setValue(5, 4, 39463);

    return data;
  }
}
