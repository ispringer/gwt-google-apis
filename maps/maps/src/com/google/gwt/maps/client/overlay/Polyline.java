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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.event.PolylineCancelLineHandler;
import com.google.gwt.maps.client.event.PolylineClickHandler;
import com.google.gwt.maps.client.event.PolylineEndLineHandler;
import com.google.gwt.maps.client.event.PolylineLineUpdatedHandler;
import com.google.gwt.maps.client.event.PolylineRemoveHandler;
import com.google.gwt.maps.client.event.PolylineCancelLineHandler.PolylineCancelLineEvent;
import com.google.gwt.maps.client.event.PolylineClickHandler.PolylineClickEvent;
import com.google.gwt.maps.client.event.PolylineEndLineHandler.PolylineEndLineEvent;
import com.google.gwt.maps.client.event.PolylineLineUpdatedHandler.PolylineLineUpdatedEvent;
import com.google.gwt.maps.client.event.PolylineRemoveHandler.PolylineRemoveEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.impl.HandlerCollection;
import com.google.gwt.maps.client.impl.JsUtil;
import com.google.gwt.maps.client.impl.MapEvent;
import com.google.gwt.maps.client.impl.PolylineFactoryImpl;
import com.google.gwt.maps.client.impl.PolylineImpl;
import com.google.gwt.maps.client.impl.PolylineOptionsImpl;
import com.google.gwt.maps.client.impl.EventImpl.LatLngCallback;
import com.google.gwt.maps.client.impl.EventImpl.VoidCallback;
import com.google.gwt.maps.client.overlay.Overlay.ConcreteOverlay;

/**
 * This is a map overlay that draws a polyline on the map, using the vector
 * drawing facilities of the browser if they are available, or an image overlay
 * from Google servers otherwise.
 */
public final class Polyline extends ConcreteOverlay {

  public static Polyline fromEncoded(EncodedPolyline polyline) {
    return new Polyline(PolylineFactoryImpl.impl.fromEncoded(polyline));
  }
  
  public static Polyline fromEncoded(String color, int weight, double opacity,
      String encodedPoints, int zoomFactor, String encodedLevels, int numLevels) {
    JavaScriptObject optionsJso = PolylineOptionsImpl.impl.construct();
    PolylineOptionsImpl.impl.setColor(optionsJso, color);
    PolylineOptionsImpl.impl.setWeight(optionsJso, weight);
    PolylineOptionsImpl.impl.setOpacity(optionsJso, opacity);
    PolylineOptionsImpl.impl.setPoints(optionsJso, encodedPoints);
    PolylineOptionsImpl.impl.setZoomFactor(optionsJso, zoomFactor);
    PolylineOptionsImpl.impl.setLevels(optionsJso, encodedLevels);
    PolylineOptionsImpl.impl.setNumLevels(optionsJso, numLevels);
    return new Polyline(PolylineFactoryImpl.impl.fromEncoded(optionsJso));
  }

  public static Polyline fromEncoded(String encodedPoints, int zoomFactor,
      String encodedLevels, int numLevels) {
    JavaScriptObject optionsJso = PolylineOptionsImpl.impl.construct();
    PolylineOptionsImpl.impl.setPoints(optionsJso, encodedPoints);
    PolylineOptionsImpl.impl.setZoomFactor(optionsJso, zoomFactor);
    PolylineOptionsImpl.impl.setLevels(optionsJso, encodedLevels);
    PolylineOptionsImpl.impl.setNumLevels(optionsJso, numLevels);
    return new Polyline(PolylineFactoryImpl.impl.fromEncoded(optionsJso));
  }

  /**
   * Used to create a new Polyline by wrapping an existing GPolyline object.
   * This method is invoked by the jsio library.
   * 
   * @param jsoPeer GPolyline object to wrap.
   * @return a new instance of Polyline.
   */
  @SuppressWarnings("unused")
  private static Polyline createPeer(JavaScriptObject jsoPeer) {
    return new Polyline(jsoPeer);
  }

  private HandlerCollection<PolylineCancelLineHandler> polylineCancelLineHandlers;
  private HandlerCollection<PolylineClickHandler> polylineClickHandlers;
  private HandlerCollection<PolylineEndLineHandler> polylineEndLineHandlers;
  private HandlerCollection<PolylineLineUpdatedHandler> polylineLineUpdatedHandlers;
  private HandlerCollection<PolylineRemoveHandler> polylineRemoveHandlers;

  public Polyline(LatLng[] points) {
    super(PolylineImpl.impl.construct(JsUtil.toJsList(points)));
  }

  public Polyline(LatLng[] points, String color) {
    super(PolylineImpl.impl.construct(JsUtil.toJsList(points), color));
  }

  public Polyline(LatLng[] points, String color, int weight) {
    super(PolylineImpl.impl.construct(JsUtil.toJsList(points), color, weight));
  }

  public Polyline(LatLng[] points, String color, int weight, double opacity) {
    super(PolylineImpl.impl.construct(JsUtil.toJsList(points), color, weight,
        opacity));
  }

  private Polyline(JavaScriptObject jsoPeer) {
    super(jsoPeer);
  }

  /**
   * This event is fired when the polyline is being edited and the edit is
   * canceled. See {@link Polyline#setEditingEnabled(boolean)}
   * 
   * @param handler the handler to call when this event fires.
   */
  public void addPolylineCancelLineHandler(
      final PolylineCancelLineHandler handler) {
    maybeInitPolylineCancelLineHandlers();

    polylineCancelLineHandlers.addHandler(handler, new VoidCallback() {
      @Override
      public void callback() {
        PolylineCancelLineEvent e = new PolylineCancelLineEvent(Polyline.this);
        handler.onCancel(e);
      }
    });
  }

  /**
   * This event is fired when the polyline is clicked. Note that this event also
   * subsequently triggers a "click" event on the map, where the polyline is
   * passed as the overlay argument within that event.
   * 
   * @param handler the handler to call when this event fires.
   */
  public void addPolylineClickHandler(final PolylineClickHandler handler) {
    maybeInitPolylineClickHandlers();

    polylineClickHandlers.addHandler(handler, new LatLngCallback() {
      @Override
      public void callback(LatLng latlng) {
        PolylineClickEvent e = new PolylineClickEvent(Polyline.this, latlng);
        handler.onClick(e);
      }
    });
  }

  /**
   * This event is fired when the polyline is being edited and the edit is
   * completed. See {@link Polyline#setEditingEnabled(boolean)}
   * 
   * @param handler the handler to call when this event fires.
   */
  public void addPolylineEndLineHandler(final PolylineEndLineHandler handler) {
    maybeInitPolylineEndLineHandlers();

    polylineEndLineHandlers.addHandler(handler, new LatLngCallback() {
      @Override
      public void callback(LatLng latlng) {
        PolylineEndLineEvent e = new PolylineEndLineEvent(Polyline.this, latlng);
        handler.onEnd(e);
      }
    });
  }

  /**
   * This event is fired when the polyline has a vertex inserted. See
   * {@link Polyline#insertVertex(int,LatLng)}
   * 
   * @param handler the handler to call when this event fires.
   */
  public void addPolylineLineUpdatedHandler(
      final PolylineLineUpdatedHandler handler) {
    maybeInitPolylineLineUpdatedHandlers();

    polylineLineUpdatedHandlers.addHandler(handler, new VoidCallback() {
      @Override
      public void callback() {
        PolylineLineUpdatedEvent e = new PolylineLineUpdatedEvent(Polyline.this);
        handler.onUpdate(e);
      }
    });
  }

  /**
   * This event is fired when the polyline is removed from the map, using
   * {@link com.google.gwt.maps.client.MapWidget#removeOverlay} or
   * {@link com.google.gwt.maps.client.MapWidget#clearOverlays}.
   * 
   * @param handler the handler to call when this event fires.
   */
  public void addPolylineRemoveHandler(final PolylineRemoveHandler handler) {
    maybeInitPolylineRemoveHandlers();

    polylineRemoveHandlers.addHandler(handler, new VoidCallback() {
      @Override
      public void callback() {
        PolylineRemoveEvent e = new PolylineRemoveEvent(Polyline.this);
        handler.onRemove(e);
      }
    });
  }

  /**
   * Removes the vertex with the given index in the polyline and updates the
   * shape of the polyline accordingly. The {@link Polyline} must already be
   * added to the map via
   * {@link com.google.gwt.maps.client.MapWidget#addOverlay(Overlay)}.
   */
  public void deleteVertex(int index) {
    PolylineImpl.impl.deleteVertex(jsoPeer, index);
  }

  /**
   * Returns the bounds for this polyline.
   * 
   * @return the bounds for this polyline.
   */
  public LatLngBounds getBounds() {
    return PolylineImpl.impl.getBounds(jsoPeer);
  }

  /**
   * Returns the length (in meters) of the polyline along the surface of a
   * spherical Earth.
   * 
   * @return the length (in meters) of the polyline along the surface of a
   *         spherical Earth.
   */
  public double getLength() {
    return PolylineImpl.impl.getLength(jsoPeer);
  }

  /**
   * Returns the vertex with the given index in the polyline.
   * 
   * @param index the index in the polyline to query.
   * @return the point represented by the specified index.
   */
  public LatLng getVertex(int index) {
    return PolylineImpl.impl.getVertex(super.jsoPeer, index);
  }

  /**
   * Returns the number of vertices in the polyline.
   * 
   * @return the number of vertices in the polyline.
   */
  public int getVertexCount() {
    return PolylineImpl.impl.getVertexCount(super.jsoPeer);
  }

  /**
   * Inserts a new point at the given index in the polyline and updates its
   * shape. The {@link Polyline} must already be added to the map via
   * {@link com.google.gwt.maps.client.MapWidget#addOverlay(Overlay)}.
   * 
   * @param index the index into the polyline at which to insert this point.
   * @param latlng the value of the point to add.
   */
  public void insertVertex(int index, LatLng latlng) {
    PolylineImpl.impl.insertVertex(jsoPeer, index, latlng);
  }

  /**
   * Returns true if the polyline is visible on the map.
   * 
   * @return true if the polyline is visible on the map.
   */
  public boolean isVisible() {
    return !PolylineImpl.impl.isHidden(jsoPeer);
  }

  /**
   * Removes a single handler of this map previously added with
   * {@link Polyline#addPolylineCancelLineHandler(PolylineCancelLineHandler)}.
   * 
   * @param handler the handler to remove
   */
  public void removePolylineCancelLineHandler(PolylineCancelLineHandler handler) {
    if (polylineCancelLineHandlers != null) {
      polylineCancelLineHandlers.removeHandler(handler);
    }
  }

  /**
   * Removes a single handler of this map previously added with
   * {@link Polyline#addPolylineClickHandler(PolylineClickHandler)}.
   * 
   * @param handler the handler to remove.
   */
  public void removePolylineClickHandler(PolylineClickHandler handler) {
    if (polylineClickHandlers != null) {
      polylineClickHandlers.removeHandler(handler);
    }
  }

  /**
   * Removes a single handler of this map previously added with
   * {@link Polyline#addPolylineEndLineHandler(PolylineEndLineHandler)}.
   * 
   * @param handler the handler to remove
   */
  public void removePolylineEndLineHandler(PolylineEndLineHandler handler) {
    if (polylineEndLineHandlers != null) {
      polylineEndLineHandlers.removeHandler(handler);
    }
  }

  /**
   * Removes a single handler of this map previously added with
   * {@link Polyline#addPolylineLineUpdatedHandler(PolylineLineUpdatedHandler)}.
   * 
   * @param handler the handler to remove
   */
  public void removePolylineLineUpdatedHandler(
      PolylineLineUpdatedHandler handler) {
    if (polylineLineUpdatedHandlers != null) {
      polylineLineUpdatedHandlers.removeHandler(handler);
    }
  }

  /**
   * Removes a single handler of this map previously added with
   * {@link Polyline#addPolylineRemoveHandler(PolylineRemoveHandler)}.
   * 
   * @param handler the handler to remove
   */
  public void removePolylineRemoveHandler(PolylineRemoveHandler handler) {
    if (polylineRemoveHandlers != null) {
      polylineRemoveHandlers.removeHandler(handler);
    }
  }

  /**
   * Allows a user to construct (or modify) a GPolyline object by clicking on
   * additional points on the map. The {@link Polyline} must already be added to
   * the map via
   * {@link com.google.gwt.maps.client.MapWidget#addOverlay(Overlay)}, even if
   * the polyline is initially unpopulated and contains no vertices. Each click
   * adds an additional vertex to the polyline chain, and drawing may be
   * terminated through either a double-click or clicking again on the last
   * point added, at which point an {@link PolylineEndLineEvent} event will be
   * triggered if the polyline was successfully completed; otherwise, a
   * {@link PolylineCancelLineEvent} event will be triggered, but the polyline
   * will not be removed from the map. If modifying an existing {@link Polyline},
   * vertices are connected from either the starting or ending points of the
   * existing polyline, specified in the optional {link
   * {@link PolyEditingOptions#setFromStart(boolean)}.
   */
  public void setDrawingEnabled() {
    PolylineImpl.impl.enableDrawing(jsoPeer);
  }

  /**
   * Enable drawing as in {@link Polyline#setDrawingEnabled()} but with control
   * over the polyline drawing parameters.
   * 
   * @param opts parameters for the polyline editing session.
   */
  public void setDrawingEnabled(PolyEditingOptions opts) {
    PolylineImpl.impl.enableDrawing(jsoPeer, opts);
  }

  /**
   * Allows modification of an existing {@link Polyline} chain of points. When
   * enabled, users may select and drag existing vertices. Unless a vertex limit
   * less than current number of vertices is specified by
   * {@link PolyEditingOptions#setMaxVertices(int)}, "ghost" points will also
   * be added at the midpoints of polyline sections, allowing users to
   * interpolate new vertices by clicking and dragging these additional
   * vertices. A {@link PolylineLineUpdatedEvent} event will be triggered
   * whenever vertex is added or moved.
   */
  public void setEditingEnabled(boolean enabled) {
    if (enabled) {
      PolylineImpl.impl.enableEditing(jsoPeer);
    } else {
      PolylineImpl.impl.disableEditing(jsoPeer);
    }
  }

  /**
   * Enable editing as in {@link Polyline#setEditingEnabled(boolean)}, but with
   * control over the polyline drawing parameters.
   * 
   * @param opts parameters for the polyline editing session.
   */
  public void setEditingEnabled(PolyEditingOptions opts) {
    PolylineImpl.impl.enableEditing(jsoPeer, opts);
  }

  /**
   * Changes the style of the polyline. The {@link Polyline} must already be
   * added to the map via
   * {@link com.google.gwt.maps.client.MapWidget#addOverlay(Overlay)}
   * 
   * @param style options for drawing the polyline.
   */
  public void setStrokeStyle(PolyStyleOptions style) {
    PolylineImpl.impl.setStrokeStyle(jsoPeer, style);
  }

  /**
   * Show or hide the polyline.
   * 
   * @param visible true to show the polyline.
   */
  public void setVisible(boolean visible) {
    if (visible) {
      PolylineImpl.impl.show(jsoPeer);
    } else {
      PolylineImpl.impl.hide(jsoPeer);
    }
  }

  /**
   * Returns <code>true</code> if this environment supports the
   * {@link Polyline#setVisible} method.
   * 
   * @return <code>true</code> if setVisible(false) is supported in the
   *         current environment.
   */
  public boolean supportsHide() {
    return PolylineImpl.impl.supportsHide(jsoPeer);
  }

  /**
   * Manually trigger the specified event on this object.
   * 
   * Note: The trigger() methods are provided for unit testing purposes only.
   * 
   * @param event an event to deliver to the handler.
   */
  void trigger(PolylineCancelLineEvent event) {
    maybeInitPolylineCancelLineHandlers();
    polylineCancelLineHandlers.trigger();
  }

  /**
   * Manually trigger the specified event on this object.
   * 
   * Note: The trigger() methods are provided for unit testing purposes only.
   * 
   * @param event an event to deliver to the handler.
   */
  void trigger(PolylineClickEvent event) {
    maybeInitPolylineClickHandlers();
    polylineClickHandlers.trigger(event.getLatLng());
  }

  /**
   * Manually trigger the specified event on this object.
   * 
   * Note: The trigger() methods are provided for unit testing purposes only.
   * 
   * @param event an event to deliver to the handler.
   */
  void trigger(PolylineEndLineEvent event) {
    maybeInitPolylineEndLineHandlers();
    polylineEndLineHandlers.trigger(event.getLatLng());
  }

  /**
   * Manually trigger the specified event on this object.
   * 
   * Note: The trigger() methods are provided for unit testing purposes only.
   * 
   * @param event an event to deliver to the handler.
   */
  void trigger(PolylineLineUpdatedEvent event) {
    maybeInitPolylineLineUpdatedHandlers();
    polylineLineUpdatedHandlers.trigger();
  }

  /**
   * Manually trigger the specified event on this object.
   * 
   * Note: The trigger() methods are provided for unit testing purposes only.
   * 
   * @param event an event to deliver to the handler.
   */
  void trigger(PolylineRemoveEvent event) {
    maybeInitPolylineRemoveHandlers();
    polylineRemoveHandlers.trigger();
  }

  private void maybeInitPolylineCancelLineHandlers() {
    if (polylineCancelLineHandlers == null) {
      polylineCancelLineHandlers = new HandlerCollection<PolylineCancelLineHandler>(
          jsoPeer, MapEvent.CANCELLINE);
    }
  }

  private void maybeInitPolylineClickHandlers() {
    if (polylineClickHandlers == null) {
      polylineClickHandlers = new HandlerCollection<PolylineClickHandler>(
          jsoPeer, MapEvent.CLICK);
    }
  }

  private void maybeInitPolylineEndLineHandlers() {
    if (polylineEndLineHandlers == null) {
      polylineEndLineHandlers = new HandlerCollection<PolylineEndLineHandler>(
          jsoPeer, MapEvent.ENDLINE);
    }
  }

  private void maybeInitPolylineLineUpdatedHandlers() {
    if (polylineLineUpdatedHandlers == null) {
      polylineLineUpdatedHandlers = new HandlerCollection<PolylineLineUpdatedHandler>(
          jsoPeer, MapEvent.LINEUPDATED);
    }
  }

  private void maybeInitPolylineRemoveHandlers() {
    if (polylineRemoveHandlers == null) {
      polylineRemoveHandlers = new HandlerCollection<PolylineRemoveHandler>(
          jsoPeer, MapEvent.REMOVE);
    }
  }

}
