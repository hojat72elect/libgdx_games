package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.MainSokoban;
import com.nopalsoft.sokoban.Settings;
import com.nopalsoft.sokoban.screens.MainMenuScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class LevelSelector extends Group {

	protected MainMenuScreen menuScreen;
	protected I18NBundle idiomas;
	protected MainSokoban game;

	Label lbTitulo;

	ScrollPane scrollPane;
	Table contenedor;

	/** Pagina actual (cada pagina tiene 15 niveles) */
	int actualPage;
	int totalStars;

	VentanaLevel vtLevel;

	public LevelSelector(Screens currentScreen) {
		setSize(600, 385);
		setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 70);
		menuScreen = (MainMenuScreen) currentScreen;
		game = currentScreen.game;
		idiomas = game.idiomas;

		setBackGround(Assets.backgroundVentana);

		vtLevel = new VentanaLevel(currentScreen);

		Table tbTitulo;
		tbTitulo = new Table();

		tbTitulo.setSize(300, 50);
		tbTitulo.setPosition(getWidth() / 2f - tbTitulo.getWidth() / 2f, 324);

		lbTitulo = new Label("Niveles", new LabelStyle(Assets.font, Color.WHITE));
		tbTitulo.add(lbTitulo);

		contenedor = new Table();
		scrollPane = new ScrollPane(contenedor);
		scrollPane.setSize(getWidth() - 100, getHeight() - 100);
		scrollPane.setPosition(getWidth() / 2f - scrollPane.getWidth() / 2f, 30);
		scrollPane.setScrollingDisabled(false, true);
		//

		contenedor.defaults().padLeft(5).padRight(5);

		for (int i = 0; i < Settings.arrLevel.length; i++) {
			totalStars += Settings.arrLevel[i].numStars;
		}
		totalStars += 2;// Por defecto ya tengo 3 esterllasd

		int numeroPages = (int) (Settings.NUM_MAPS / 15f);
		if (Settings.NUM_MAPS % 15f != 0)
			numeroPages++;

		for (int col = 0; col < numeroPages; col++) {
			contenedor.add(getListLevel(col));
		}

		actualPage = 0;

		addActor(tbTitulo);
		addActor(scrollPane);

		scrollToPage(0);

	}

	private void setBackGround(TextureRegionDrawable imageBackground) {
		Image img = new Image(imageBackground);
		img.setSize(getWidth(), getHeight());
		addActor(img);

	}

	/**
	 * Cada lista tiene 15 items
	 * 
	 * @param list
	 * @return
	 */
	public Table getListLevel(int list) {
		Table content = new Table();

		int level = list * 15;
		content.defaults().pad(7, 12, 7, 12);
		for (int col = 0; col < 15; col++) {
			Button oButton = getLevelButton(level);
			content.add(oButton).size(76, 83);
			level++;

			if (level % 5 == 0)
				content.row();

			// para esconder mundos que no existen
			if (level > Settings.NUM_MAPS)
				oButton.setVisible(false);

		}
		return content;

	}

	private void scrollToPage(int page) {

		Table tabToScrollTo = (Table) contenedor.getChildren().get(page);
		scrollPane.scrollTo(tabToScrollTo.getX(), tabToScrollTo.getY(), tabToScrollTo.getWidth(), tabToScrollTo.getHeight());

	}

	public void nextPage() {
		actualPage++;
		if (actualPage >= contenedor.getChildren().size)
			actualPage = contenedor.getChildren().size - 1;
		scrollToPage(actualPage);
	}

	public void previousPage() {
		actualPage--;
		if (actualPage < 0)
			actualPage = 0;
		scrollToPage(actualPage);

	}

	public Button getLevelButton(final int level) {
		final TextButton button;

		final int skullsToNextLevel = (int) (level * 1f);// Solo necesito 1 estrella para desbloquear el siguiente nivel

		if (!(totalStars >= skullsToNextLevel)) {
			button = new TextButton("", Assets.styleTextButtonLevelLocked);
			button.setDisabled(true);
		}
		else {

			button = new TextButton("" + (level + 1), Assets.styleTextButtonLevel);

			// Estoy agregando mundos que no existen para poder llenar la tabla por eso pongo este fix
			boolean completed = false;
			if (level < Settings.arrLevel.length) {
				if (Settings.arrLevel[level].numStars == 1)
					completed = true;
			}

			Image imgLevel;

			if (completed)
				imgLevel = new Image(Assets.levelStar);
			else
				imgLevel = new Image(Assets.levelOff);

			button.row();
			button.add(imgLevel).size(10).padBottom(2);
		}

		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!button.isDisabled()) {
					vtLevel.show(getStage(), level, Settings.arrLevel[level].bestMoves, Settings.arrLevel[level].bestTime);

				}
			}
		});

		menuScreen.addEfectoPress(button);

		return button;

	}
}
