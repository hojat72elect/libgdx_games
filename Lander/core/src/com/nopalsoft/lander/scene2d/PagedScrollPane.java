package com.nopalsoft.lander.scene2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class PagedScrollPane extends ScrollPane {

    private boolean wasPanDragFling = false;

    private Table content;

    public PagedScrollPane() {

        super(null);

        setup();
    }

    private void setup() {

        content = new Table();

        content.defaults().space(250);

        super.setWidget(content);
    }

    public void addPage(Actor page) {

        content.add(page).expandY().fillY();
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

        if (content != null) {

            for (Cell cell : content.getCells()) {

                cell.width(width);
            }

            content.invalidate();
        }
    }

    public void setPageSpacing(float pageSpacing) {

        if (content != null) {

            content.defaults().space(pageSpacing);

            for (Cell cell : content.getCells()) {

                cell.space(pageSpacing);
            }

            content.invalidate();
        }
    }

    private void scrollToPage() {

        final float width = getWidth();

        final float scrollX = getScrollX();

        final float maxX = getMaxX();

        if (scrollX >= maxX || scrollX <= 0)
            return;

        Array<Actor> pages = content.getChildren();

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
            // setScrollX(MathUtils.clamp(pageX, 0, maxX));
        }
    }
}
