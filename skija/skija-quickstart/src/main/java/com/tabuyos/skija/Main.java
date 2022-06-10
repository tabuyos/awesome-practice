/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.skija;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.jetbrains.skija.BackendRenderTarget;
import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.ColorSpace;
import org.jetbrains.skija.DirectContext;
import org.jetbrains.skija.FramebufferFormat;
import org.jetbrains.skija.Paint;
import org.jetbrains.skija.PixelGeometry;
import org.jetbrains.skija.Rect;
import org.jetbrains.skija.Surface;
import org.jetbrains.skija.SurfaceColorFormat;
import org.jetbrains.skija.SurfaceOrigin;
import org.jetbrains.skija.SurfaceProps;

/**
 * Main
 *
 * @author tabuyos
 * @since 2022/6/6
 */
@SuppressWarnings("FieldCanBeLocal")
public class Main {

  public static void main(String[] args) {
    new Main().run();
  }

  private Display display;
  private GLCanvas glCanvas;
  private DirectContext context;
  private Surface surface;
  private BackendRenderTarget renderTarget;

  protected void run() {
    display = new Display();
    Shell shell = new Shell(display);
    shell.setText("Skija SWT Demo");
    shell.setLayout(new FillLayout());

    GLData data = new GLData();
    data.doubleBuffer = true;

    glCanvas = new GLCanvas(shell, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE, data);
    glCanvas.setCurrent();
    context = DirectContext.makeGL();

    Listener listener = event -> {
      switch (event.type) {
        case SWT.Paint:
          onPaint(event);
          break;
        case SWT.Resize:
          onResize(event);
          break;
        case SWT.Dispose:
          onDispose();
          break;
        default:
          break;
      }
    };
    glCanvas.addListener(SWT.Paint, listener);
    glCanvas.addListener(SWT.Resize, listener);
    shell.addListener(SWT.Dispose, listener);

    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }

    display.dispose();
  }

  protected void release() {
    if (surface != null) {
      surface.close();
      surface = null;
    }
    if (renderTarget != null) {
      renderTarget.close();
      renderTarget = null;
    }
  }

  protected void onResize(Event event) {
    release();
    Rectangle rect = glCanvas.getClientArea();
    renderTarget = BackendRenderTarget.makeGL(rect.width,
                                              rect.height,
                                              0,
                                              8,
                                              0,
                                              FramebufferFormat.GR_GL_RGBA8);
    surface = Surface.makeFromBackendRenderTarget(context,
                                                  renderTarget,
                                                  SurfaceOrigin.BOTTOM_LEFT,
                                                  SurfaceColorFormat.RGBA_8888,
                                                  ColorSpace.getDisplayP3(),
                                                  new SurfaceProps(PixelGeometry.RGB_H));
  }

  protected void onPaint(Event event) {
    if (surface == null) {
      return;
    }
    Canvas canvas = surface.getCanvas();
    paint(canvas);
    context.flush();
    glCanvas.swapBuffers();
  }

  protected void onDispose() {
    release();
    context.close();
  }

  protected void paint(Canvas canvas) {
    canvas.clear(0xFFFFFFFF);
    canvas.save();
    canvas.translate(100, 100);
    canvas.rotate(System.currentTimeMillis() % 1000 / 1000f * 360f);
    canvas.drawRect(Rect.makeLTRB(-100, -100, 100, 100), new Paint().setColor(0xFFCC3333));
    canvas.restore();
  }
}
