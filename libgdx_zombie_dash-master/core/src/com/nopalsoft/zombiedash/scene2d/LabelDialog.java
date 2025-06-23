package com.nopalsoft.zombiedash.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.zombiedash.Assets;

public class LabelDialog extends Table {

	Label lbl;

	public LabelDialog(String text, boolean isInverted) {
		// this.debug();

		lbl = new Label(text, Assets.labelStyleHelpDialog);
		lbl.setWrap(true);

		float width = 75;
		float height = 100;

		if (lbl.getWidth() > width)
			width = lbl.getWidth();
		if (lbl.getHeight() > height)
			height = lbl.getHeight();

		setSize(width, height);

		if (isInverted) {
			defaults().pad(45, 10, 10, 10);
			pad(0);
			setBackground(Assets.helpDialogInverted);
		}
		else {
			defaults().pad(10, 10, 45, 10);
			pad(0);
			setBackground(Assets.helpDialog);
		}

		add(lbl).expand().fill();

	}

	@Override
	public void setPosition(float x, float y) {
		x -= 35;
		super.setPosition(x, y);
	}

}
