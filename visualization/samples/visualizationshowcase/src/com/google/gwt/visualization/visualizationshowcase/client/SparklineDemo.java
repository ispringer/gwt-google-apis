/*
 * Copyright 2009 Google Inc.
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
package com.google.gwt.visualization.visualizationshowcase.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDrawOptions;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Visualization;

/**
 * Demonstrates ad hoc visualization wrapping.  
 * 
 * @see <a href="http://code.google.com/apis/visualization/documentation/gallery/imagesparkline.html"
 * > Image Sparkline Reference. </a>
 */
public class SparklineDemo implements LeftTabPanel.WidgetProvider {
  private Widget widget;

  public SparklineDemo() {
    AbstractDrawOptions options = JavaScriptObject.createObject().cast(); 
    options.set("width", 120.0);
    options.set("height", 40.0);
    options.set("showAxisLines", false);
    options.set("showValueLabels", false);
    options.set("labelPosition", "left");
    
    DataTable data = DataTable.create();
    
    data.addColumn(ColumnType.NUMBER, "Revenue");
    data.addColumn(ColumnType.NUMBER, "Licenses");
    data.addRows(10);
    data.setValue(0,0,435);
    data.setValue(1,0,438);
    data.setValue(2,0,512);
    data.setValue(3,0,460);
    data.setValue(4,0,491);
    data.setValue(5,0,487);
    data.setValue(6,0,552);
    data.setValue(7,0,511);
    data.setValue(8,0,505);
    data.setValue(9,0,509);

    data.setValue(0,1,132);
    data.setValue(1,1,131);
    data.setValue(2,1,137);
    data.setValue(3,1,142);
    data.setValue(4,1,140);
    data.setValue(5,1,139);
    data.setValue(6,1,147);
    data.setValue(7,1,146);
    data.setValue(8,1,151);
    data.setValue(9,1,149);
    
    widget = new Visualization<AbstractDrawOptions>(data, options) {
      @Override
      protected native JavaScriptObject createJso(Element div) /*-{
        return new $wnd.google.visualization.ImageSparkLine(div);
      }-*/;
    };
  }

  public Widget getWidget() {
    return widget;
  }
}
