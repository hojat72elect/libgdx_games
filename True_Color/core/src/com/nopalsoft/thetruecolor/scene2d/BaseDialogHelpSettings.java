package com.nopalsoft.thetruecolor.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.Settings;
import com.nopalsoft.thetruecolor.screens.Screens;

public class BaseDialogHelpSettings extends BaseDialog {
    static final float WIDTH = 440;
    static final float HEIGHT = 600;

    public enum Languages {
        DEFAULT, ENGLISH, SPANISH, CHINESE_TAIWAN, RUSSIAN, FRENCH, JAPANESE, PORTUGUESE
    }

    Table tbColores;

    TextButton btDefault, btEnglish, btSpanish, btChineseTaiwan, btRussian, btFrench, btJapanese, btPortugese;
    TextButton btMore;

    BaseDialogMoreLanguages vtnaMoreLanguages;

    public BaseDialogHelpSettings(final Screens currentScreen) {
        super(currentScreen, WIDTH, HEIGHT, 80);
        setCloseButton(400, 560, 50);

        Label lbIdioma = new Label(Assets.languagesBundle.get("language"), new LabelStyle(Assets.fontSmall, Color.BLACK));
        lbIdioma.setPosition(getWidth() / 2f - lbIdioma.getWidth() / 2f, 555);
        addActor(lbIdioma);

        vtnaMoreLanguages = new BaseDialogMoreLanguages(currentScreen);

        btMore = crearBotton(Assets.languagesBundle.get("more"), null, Assets.flagMoreDrawable);
        btMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btMore.setChecked(false);
                vtnaMoreLanguages.show(currentScreen.stage);
            }
        });

        btDefault = crearBotton("Default", Languages.DEFAULT, null);

        btEnglish = crearBotton("English", Languages.ENGLISH, Assets.flagEnglishDrawable);

        btSpanish = crearBotton("Espa\u00F1ol", Languages.SPANISH, Assets.flagSpanishDrawable);

        btChineseTaiwan = crearBotton("\u4E2D\u6587", Languages.CHINESE_TAIWAN, Assets.flagChineseDrawable);

        btRussian = crearBotton("\u0420\u0443\u0441\u0441\u043a\u0438\u0439", Languages.RUSSIAN, Assets.flagRussianDrawable);
        btRussian.getLabel().setFontScale(.7f);

        btFrench = crearBotton("Fran\u00e7ais", Languages.FRENCH, Assets.flagFrenchDrawable);

        btJapanese = crearBotton("\u65e5\u672c\u8a9e", Languages.JAPANESE, Assets.flagJapaneseDrawable);

        btPortugese = crearBotton("Portugu\u00eas", Languages.PORTUGUESE, Assets.flagPortugueseDrawable);

        switch (Settings.selectedLanguage) {
            case DEFAULT:
                btDefault.setChecked(true);
                break;
            case ENGLISH:
                btEnglish.setChecked(true);
                break;
            case SPANISH:
                btSpanish.setChecked(true);
                break;
            case CHINESE_TAIWAN:
                btChineseTaiwan.setChecked(true);
                break;
            case RUSSIAN:
                btRussian.setChecked(true);
                break;
            case FRENCH:
                btFrench.setChecked(true);
                break;
            case JAPANESE:
                btJapanese.setChecked(true);
                break;
            case PORTUGUESE:
                btPortugese.setChecked(true);
                break;
        }


        ButtonGroup<TextButton> btGroup = new ButtonGroup<TextButton>(btDefault, btEnglish, btSpanish, btChineseTaiwan, btRussian, btFrench, btJapanese, btPortugese);
        btGroup.setMaxCheckCount(1);

        Table tbIdiomas = new Table();
        tbIdiomas.setSize(getWidth(), 200);
        tbIdiomas.setPosition(0, 300);

        tbIdiomas.defaults().expandX().pad(3f, 10, 3f, 10).fill().uniform();

        tbIdiomas.add(btDefault);
        tbIdiomas.add(btEnglish);
        tbIdiomas.add(btSpanish);
        tbIdiomas.row();
        tbIdiomas.add(btChineseTaiwan);
        tbIdiomas.add(btRussian);
        tbIdiomas.add(btFrench);
        tbIdiomas.row();
        tbIdiomas.add(btJapanese);
        tbIdiomas.add(btPortugese);
        tbIdiomas.add(btMore);
        tbIdiomas.row();

        // Los colores
        tbColores = new Table();
        tbColores.setSize(getWidth(), 240);

        fillTableColores();

        addActor(tbColores);
        addActor(tbIdiomas);
    }

    private void fillTableColores() {
        tbColores.clear();

        Image iBlue = new Image(Assets.barTimerDrawable);
        iBlue.setColor(Color.BLUE);

        Image iCyan = new Image(Assets.barTimerDrawable);
        iCyan.setColor(Color.CYAN);

        Image iGreen = new Image(Assets.barTimerDrawable);
        iGreen.setColor(Color.GREEN);

        Image iYellow = new Image(Assets.barTimerDrawable);
        iYellow.setColor(Color.YELLOW);

        Image iPink = new Image(Assets.barTimerDrawable);
        iPink.setColor(Color.PINK);

        Image iBrown = new Image(Assets.barTimerDrawable);
        iBrown.setColor(new Color(.6f, .3f, 0, 1));

        Image iPurple = new Image(Assets.barTimerDrawable);
        iPurple.setColor(Color.PURPLE);

        Image iRed = new Image(Assets.barTimerDrawable);
        iRed.setColor(Color.RED);

        tbColores.defaults().expandX().padTop(5).padBottom(5);

        tbColores.add(getNewLabelWithColor(idiomas.get("blue"), Color.BLUE));
        tbColores.add(iBlue).size(40).left();

        tbColores.add(getNewLabelWithColor(idiomas.get("cyan"), Color.CYAN));
        tbColores.add(iCyan).size(40).left();

        tbColores.row();
        tbColores.add(getNewLabelWithColor(idiomas.get("green"), Color.GREEN));
        tbColores.add(iGreen).size(40).left();

        tbColores.add(getNewLabelWithColor(idiomas.get("yellow"), Color.YELLOW));
        tbColores.add(iYellow).size(40).left();

        tbColores.row();
        tbColores.add(getNewLabelWithColor(idiomas.get("pink"), Color.PINK));
        tbColores.add(iPink).size(40).left();

        tbColores.add(getNewLabelWithColor(idiomas.get("brown"), new Color(.6f, .3f, 0, 1)));
        tbColores.add(iBrown).size(40).left();

        tbColores.row();
        tbColores.add(getNewLabelWithColor(idiomas.get("purple"), Color.PURPLE));
        tbColores.add(iPurple).size(40).left();

        tbColores.add(getNewLabelWithColor(idiomas.get("red"), Color.RED));
        tbColores.add(iRed).size(40).left();
    }

    private Label getNewLabelWithColor(String text, Color color) {
        LabelStyle lbStyleColores = new LabelStyle(Assets.fontSmall, color);
        Label label = new Label(text, lbStyleColores);
        if (Settings.selectedLanguage == Languages.RUSSIAN) {
            label.setFontScale(.7f);
        }
        return label;
    }

    private TextButton crearBotton(String texto, Languages language, TextureRegionDrawable flag) {
        TextButton btAux = new TextButton(texto, Assets.textButtonStyle);
        if (flag != null) {
            btAux.add(new Image(flag));
        }
        if (language != null) {
            btAux.addListener(addClickListener(language));
        }
        btAux.getLabel().setFontScale(.8f);
        return btAux;
    }

    private ClickListener addClickListener(final Languages language) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.selectedLanguage = language;
                Settings.save();
                Assets.loadAssetsWithSettings();
                idiomas = Assets.languagesBundle;
                fillTableColores();
            }
        };
    }
}