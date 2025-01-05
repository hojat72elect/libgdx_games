package com.nGame.utils.scene2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.nGame.utils.ng.Assets;
import com.nGame.utils.Utils;
import com.nGame.utils.ng.Log;

/**
 * Created by oli on 23.09.2014.
 */
class Pager extends ScrollPane {

    private int pages = 0;
    private Table pageTable;
    private boolean wasPanDragFling;
    private float pageSpaceing;
    private boolean IMouseWheeleX = false, IMouseWheeleY = false;
    private boolean ITouch = true;
    private boolean canScrollX = true, canScrollY = true;
    private float scrollValueY;
    private float scrollValueX;

    public Pager(float width, float height) {
        super(null, Assets.I.skin);
        setWidth(width);
        setHeight(height);
        pageTable = Utils.Ui.createTable(false);
        setWidget(pageTable);
        setScrollingDisabled(false, true);
        setSmoothScrolling(true);
        getStyle().vScrollKnob = null;
        getStyle().vScroll = null;
        getStyle().hScrollKnob = null;
        getStyle().hScroll = null;
        super.setWidget(pageTable);
        //setFlickScroll(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (wasPanDragFling && !isPanning() && !isDragging() && !isFlinging()) {
            wasPanDragFling = false;
            scrollToPage();
        } else {
            if (isPanning() || isDragging() || isFlinging()) {
                wasPanDragFling = true;
            }
        }

    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        if (pageTable != null) {
            for (Cell cell : pageTable.getCells()) {
                cell.width(width);
            }
            pageTable.invalidate();
        }

    }


    public void setPageSpacing(float pageSpacing) {
        this.pageSpaceing = pageSpacing;
        if (pageTable != null) {
            pageTable.defaults().space(pageSpacing);
            for (Cell cell : pageTable.getCells()) {
                cell.space(pageSpacing);
            }
            pageTable.invalidate();
        }

    }


    void scrollToPage() {


        final float width = getWidth();
        final float scrollX = getScrollX();
        final float maxX = getMaxX();

        if (scrollX >= maxX || scrollX <= 0) return;

        Array<Actor> pages = pageTable.getChildren();
        float pageX = 0;
        float pageWidth = 0;

        if (pages.size > 0) {
            for (Actor a : pages) {
                pageX = a.getX();
                pageWidth = a.getWidth();
                if (scrollX < (pageX + pageWidth * 0.5)) {
                    break;
                }
            }
            setScrollX(MathUtils.clamp(pageX - (width - pageWidth) / 2, 0, maxX));
        }


    }

    public void toPage(int page) {
        setScrollX((pageTable.getChildren().first().getWidth() * page) + pageSpaceing * page);
    }

    public void nextPage() {
        Log.d("scroll: w:" + getWidth() + "  x:" + getScrollX());
        setScrollX(getScrollX() + getWidth() + pageSpaceing);
    }

    public void prevPage() {
        Log.d("scroll: w:-" + getWidth() + "  x:" + getScrollX());
        setScrollX(getScrollX() - getWidth() - pageSpaceing);
    }

    public void addPage(Actor actor) {
        pageTable.add(actor).width(getWidth());
        pages++;

    }

    public void removePage(Actor actor) {
        if (pageTable.removeActor(actor)) {
            pages--;
        } else {
            Log.d("canot remove page [" + actor + "] from pager");
        }
    }

    @Override
    protected float getMouseWheelX() {
        if (IMouseWheeleX) {
            return super.getMouseWheelX();
        } else {
            return 0;
        }
    }

    @Override
    protected float getMouseWheelY() {
        if (IMouseWheeleX) {
            return super.getMouseWheelY();
        } else {
            return 0;
        }
    }


    @Override
    protected void scrollX(float pixelsX) {
        if (canScrollX) {
            super.scrollX(pixelsX);
        } else {
            super.scrollX(scrollValueX);
        }
    }

    @Override
    protected void scrollY(float pixelsY) {
        if (canScrollX) {
            super.scrollY(pixelsY);
        } else {
            super.scrollY(scrollValueY);
        }

    }

    public void enableScrolling() {

        canScrollX = true;
        canScrollY = true;
    }

    public void disableScrolling() {

        scrollValueX = getVisualScrollX();
        scrollValueY = getVisualScrollY();
        canScrollX = false;
        canScrollY = false;

    }
}
