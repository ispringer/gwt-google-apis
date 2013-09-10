/*
 * Copyright 2013 Ian P. Springer
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
package com.google.gwt.visualization.client.visualizations;

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.ajaxloader.client.Properties;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDrawOptions;
import com.google.gwt.visualization.client.Selectable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.*;

/**
 * Geochart visualization (the successor to {@link GeoMap}).
 * 
 * @see <a href=
 *      "https://developers.google.com/chart/interactive/docs/gallery/geochart"
 *      >Visualization: Geochart</a>
 */
public class GeoChart extends Visualization<GeoChart.Options> implements Selectable {
  /**
   * How to display data on the map.
   */
  public static enum DisplayMode {
    /**
     * Choose based on the format of the DataTable.
     */
    AUTO,

    /**
     * Color regions inside the map according to the number given.
     */
    REGIONS,

    /**
     * Put markers on the map, changing size and color according to the number
     * given.
     */
    MARKERS
  }

  /**
   * Options for drawing the chart.
   *
   */
  public static class Options extends AbstractDrawOptions {
    public static Options create() {
      return JavaScriptObject.createObject().cast();
    }

    protected Options() {
    }

    public final native void setColorAxis(ColorAxis colorAxis) /*-{
      this.colorAxis = colorAxis;
    }-*/;

    public final void setDisplayMode(DisplayMode mode) {
      setDisplayMode(mode.name().toLowerCase());
    }

    private native void setDisplayMode(String mode) /*-{
      this.displayMode = mode;
    }-*/;

    public final native void setHeight(int height) /*-{
      this.height = height;
    }-*/;

    public final native void setKeepAspectRatio(boolean keepAspectRatio) /*-{
        this.keepAspectRatio = keepAspectRatio;
    }-*/;

    public final native void setRegion(String region) /*-{
      this.region = region;
    }-*/;

    public final native void setWidth(int width) /*-{
      this.width = width;
    }-*/;
  }

  public static class ColorAxis extends Properties {
    public static ColorAxis create() {
      return JavaScriptObject.createObject().cast();
    }

    protected ColorAxis() {
    }

    public final native void setMinValue(int minValue) /*-{
        this.minValue = minValue;
    }-*/;

    public final native void setMaxValue(int maxValue) /*-{
        this.maxValue = maxValue;
    }-*/;

    public final void setValues(int... values) {
      setValues(ArrayHelper.toJsArrayInteger(values));
    }

    public final native void setValues(JsArrayInteger values) /*-{
        this.values = values;
    }-*/;

    public final void setColors(String... colors) {
      setColors(ArrayHelper.toJsArrayString(colors));
    }

    public final native void setColors(JsArrayString colors) /*-{
        this.colors = colors;
    }-*/;
  }

  public static final String PACKAGE = "geochart";

  public GeoChart() {
    super();
    // Setting size makes  a big difference for this visualization.
    setSize("100%", "100%");
  }

  public GeoChart(AbstractDataTable data, Options options) {
    super(data, options);
    // Setting size makes  a big difference for this visualization.
    setSize("100%", "100%");
  }

  public void addReadyHandler(ReadyHandler handler) {
    Handler.addHandler(this, "ready", handler);
  }

  public void addRegionClickHandler(RegionClickHandler handler) {
    Handler.addHandler(this, "regionClick", handler);
  }

  public final void addSelectHandler(SelectHandler handler) {
    Selection.addSelectHandler(this, handler);
  }

  public final JsArray<Selection> getSelections() {
    return Selection.getSelections(this);
  }

  public final void setSelections(JsArray<Selection> sel) {
    Selection.setSelections(this, sel);
  }

  @Override
  protected native JavaScriptObject createJso(Element parent) /*-{
    return new $wnd.google.visualization.GeoChart(parent);
  }-*/;

}
