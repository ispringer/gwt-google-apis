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
package com.google.gwt.maps.client.overlay;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.TestUtilities;
import com.google.gwt.maps.client.InfoWindowContent.MapBlowupContent;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler.MarkerClickEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.ArrayList;

/**
 * Tests the Marker class.
 */
public class MarkerTest extends GWTTestCase {
  // length of time to wait for asynchronous test to complete.
  static final int ASYNC_DELAY_MSEC = 5000;

  @Override
  public String getModuleName() {
    return "com.google.gwt.maps.GoogleMapsTest";
  }

  /**
   * Runs before each test method.
   */
  @Override
  public void gwtSetUp() {
    TestUtilities.cleanDom();
  }

  public void testMarkerCloseInfoWindow() {
    LatLng center = LatLng.newInstance(0, 0);
    final MapWidget map = new MapWidget(center, 1);
    map.setSize("300px", "300px");
    final Marker m = new Marker(center);
    map.addOverlay(m);
    RootPanel.get().add(map);
    InfoWindowContent content = new InfoWindowContent("<i>Hello World!</i>");
    InfoWindow info = map.getInfoWindow();
    info.open(m, content);
    DeferredCommand.addCommand(new Command() {

      public void execute() {
        m.closeInfoWindow();
        finishTest();
      }

    });
    delayTestFinish(ASYNC_DELAY_MSEC);
  }

  public void testMarkerGetTitle() {
    Marker marker = new Marker(LatLng.newInstance(0, 0));
    String result = marker.getTitle();
    assertNull("expected null title", result);

    MarkerOptions mo = MarkerOptions.newInstance();
    mo.setTitle("Non-null title");
    marker = new Marker(LatLng.newInstance(0, 1), mo);
    result = marker.getTitle();
    assertNotNull("expected non-null title", result);
  }

  public void testMarkerLatLng() {
    LatLng p1 = LatLng.newInstance(45, 45);
    LatLng p2 = LatLng.newInstance(50, 50);
    Marker m = new Marker(p1);
    assertTrue("Compare points", LatLng.newInstance(45, 45).isEquals(
        m.getLatLng()));
    m.setLatLng(p2);
    assertTrue("Compare points", LatLng.newInstance(50, 50).isEquals(
        m.getLatLng()));
  }

  public void testMarkerShowMapBlowup() {
    LatLng center = LatLng.newInstance(0, 0);
    final MapWidget map = new MapWidget(center, 1);
    map.setSize("300px", "300px");
    Marker m = new Marker(center);
    map.addOverlay(m);
    RootPanel.get().add(map);
    m.showMapBlowup();
  }

  public void testMarkerShowMapBlowupContent() {
    LatLng center = LatLng.newInstance(0, 0);
    final MapWidget map = new MapWidget(center, 1);
    map.setSize("300px", "300px");
    Marker m = new Marker(center);
    map.addOverlay(m);
    RootPanel.get().add(map);
    MapBlowupContent content = new MapBlowupContent(
        MapType.getSatelliteMap(), 3);
    m.showMapBlowup(content);
  }

  /**
   * Test of a marker subclass.
   */
  public void testSubclassMarker() {
    class MyMarker extends Marker {
      final String infoText;

      MyMarker(LatLng latlng, String text) {
        super(latlng);
        infoText = text;
      }
    }

    final String marker1Text = "Marker One";

    MyMarker marker1 = new MyMarker(LatLng.newInstance(1, 1), marker1Text);
    marker1.addMarkerClickHandler(new MarkerClickHandler() {

      public void onClick(MarkerClickEvent event) {
        Marker m = event.getSender();
        assertTrue("instanceof MyMarker", m instanceof MyMarker);
        MyMarker myMarker = (MyMarker) m;
        assertEquals("marker 1 click event", marker1Text, myMarker.infoText);
        finishTest();
      }

    });

    final MapWidget map = new MapWidget();
    map.setSize("300px", "300px");

    map.addOverlay(marker1);
    RootPanel.get().add(map);
    delayTestFinish(ASYNC_DELAY_MSEC);

    marker1.trigger(new MarkerClickEvent(marker1));
  }
  
  /**
   * Test of marker custom z-index
   */
  public void testZIndexProcess() {
    MarkerOptions opts = MarkerOptions.newInstance();
    opts.setZIndexProcess(new MarkerOptions.ZIndexProcess() {
      
      private double count = 0;
      
      public double computeZIndex(Marker marker) {
        return (count = count - 1);
      }
    });
    
    LatLng p1 = LatLng.newInstance(45, 45);
    LatLng p2 = LatLng.newInstance(50, 50);
    Marker m1 = new Marker(p1, opts);
    Marker m2 = new Marker(p2, opts);
    
    MapWidget map = new MapWidget(p1, 1);
    map.setSize("300px", "300px");
    map.addOverlay(m1);
    map.addOverlay(m2);
    RootPanel.get().add(map);
    
    // From this point, this test investigates the DOM structure of created map,
    // therefore it might break if internal implementation of Maps changes.
    
    Element div = map.getElement().getFirstChildElement().getFirstChildElement();
    NodeList<Element> imgs = div.getElementsByTagName("img");
    
    ArrayList<Element> markers = new ArrayList<Element>();
    for (int i = 0; i < imgs.getLength(); i++) {
      Element img = imgs.getItem(i);
      String src = img.getPropertyString("src"); 
      if (src != null && src.endsWith("/marker.png")) {
        markers.add(img);
      }
    }
    assertEquals(2, markers.size());
    
    assertEquals("-1", markers.get(0).getStyle().getZIndex());
    assertEquals("-2", markers.get(1).getStyle().getZIndex());
  }
}
