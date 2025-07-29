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
import com.nopalsoft.thetruecolor.screens.BaseScreen;

public class DialogHelpSettings extends BaseDialog {
    static final float WIDTH = 440;
    static final float HEIGHT = 600;

    public enum Languages {
        DEFAULT, ENGLISH, SPANISH, CHINESE, RUSSIAN, FRENCH, JAPANESE, PORTUGUESE
    }

    Table colorsTable;

    TextButton buttonDefault, buttonEnglish, buttonSpanish, buttonChinese, buttonRussian, buttonFrench, buttonJapanese, buttonPortuguese, buttonMore;

    DialogMoreLanguages dialogMoreLanguages;

    public DialogHelpSettings(final BaseScreen currentScreen) {
        super(currentScreen, WIDTH, HEIGHT, 80);
        setCloseButton(560);

        Label languageLabel = new Label(Assets.languagesBundle.get("language"), new LabelStyle(Assets.fontSmall, Color.BLACK));
        languageLabel.setPosition(getWidth() / 2f - languageLabel.getWidth() / 2f, 555);
        addActor(languageLabel);

        dialogMoreLanguages = new DialogMoreLanguages(currentScreen);

        buttonMore = createButton(Assets.languagesBundle.get("more"), null, Assets.flagMoreDrawable);
        buttonMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonMore.setChecked(false);
                dialogMoreLanguages.show(currentScreen.stage);
            }
        });

        buttonDefault = createButton("Default", Languages.DEFAULT, null);

        buttonEnglish = createButton("English", Languages.ENGLISH, Assets.flagEnglishDrawable);

        buttonSpanish = createButton("Español", Languages.SPANISH, Assets.flagSpanishDrawable);

        buttonChinese = createButton("中文", Languages.CHINESE, Assets.flagChineseDrawable);

        buttonRussian = createButton("Русский", Languages.RUSSIAN, Assets.flagRussianDrawable);
        buttonRussian.getLabel().setFontScale(.7f);

        buttonFrench = createButton("Français", Languages.FRENCH, Assets.flagFrenchDrawable);

        buttonJapanese = createButton("日本語", Languages.JAPANESE, Assets.flagJapaneseDrawable);

        buttonPortuguese = createButton("Português", Languages.PORTUGUESE, Assets.flagPortugueseDrawable);

        switch (Settings.selectedLanguage) {
            case DEFAULT:
                buttonDefault.setChecked(true);
                break;
            case ENGLISH:
                buttonEnglish.setChecked(true);
                break;
            case SPANISH:
                buttonSpanish.setChecked(true);
                break;
            case CHINESE:
                buttonChinese.setChecked(true);
                break;
            case RUSSIAN:
                buttonRussian.setChecked(true);
                break;
            case FRENCH:
                buttonFrench.setChecked(true);
                break;
            case JAPANESE:
                buttonJapanese.setChecked(true);
                break;
            case PORTUGUESE:
                buttonPortuguese.setChecked(true);
                break;
        }


        ButtonGroup<TextButton> btGroup = new ButtonGroup<TextButton>(buttonDefault, buttonEnglish, buttonSpanish, buttonChinese, buttonRussian, buttonFrench, buttonJapanese, buttonPortuguese);
        btGroup.setMaxCheckCount(1);

        Table languagesTable = new Table();
        languagesTable.setSize(getWidth(), 200);
        languagesTable.setPosition(0, 300);

        languagesTable.defaults().expandX().pad(3f, 10, 3f, 10).fill().uniform();

        languagesTable.add(buttonDefault);
        languagesTable.add(buttonEnglish);
        languagesTable.add(buttonSpanish);
        languagesTable.row();
        languagesTable.add(buttonChinese);
        languagesTable.add(buttonRussian);
        languagesTable.add(buttonFrench);
        languagesTable.row();
        languagesTable.add(buttonJapanese);
        languagesTable.add(buttonPortuguese);
        languagesTable.add(buttonMore);
        languagesTable.row();

        // The colors
        colorsTable = new Table();
        colorsTable.setSize(getWidth(), 240);

        fillTableColores();

        addActor(colorsTable);
        addActor(languagesTable);
    }

    private void fillTableColores() {
        colorsTable.clear();

        Image imageBlue = new Image(Assets.barTimerDrawable);
        imageBlue.setColor(Color.BLUE);

        Image imageCyan = new Image(Assets.barTimerDrawable);
        imageCyan.setColor(Color.CYAN);

        Image imageGreen = new Image(Assets.barTimerDrawable);
        imageGreen.setColor(Color.GREEN);

        Image imageYellow = new Image(Assets.barTimerDrawable);
        imageYellow.setColor(Color.YELLOW);

        Image imagePink = new Image(Assets.barTimerDrawable);
        imagePink.setColor(Color.PINK);

        Image imageBrown = new Image(Assets.barTimerDrawable);
        imageBrown.setColor(new Color(.6f, .3f, 0, 1));

        Image imagePurple = new Image(Assets.barTimerDrawable);
        imagePurple.setColor(Color.PURPLE);

        Image imageRed = new Image(Assets.barTimerDrawable);
        imageRed.setColor(Color.RED);

        colorsTable.defaults().expandX().padTop(5).padBottom(5);

        colorsTable.add(getNewLabelWithColor(languages.get("blue"), Color.BLUE));
        colorsTable.add(imageBlue).size(40).left();

        colorsTable.add(getNewLabelWithColor(languages.get("cyan"), Color.CYAN));
        colorsTable.add(imageCyan).size(40).left();

        colorsTable.row();
        colorsTable.add(getNewLabelWithColor(languages.get("green"), Color.GREEN));
        colorsTable.add(imageGreen).size(40).left();

        colorsTable.add(getNewLabelWithColor(languages.get("yellow"), Color.YELLOW));
        colorsTable.add(imageYellow).size(40).left();

        colorsTable.row();
        colorsTable.add(getNewLabelWithColor(languages.get("pink"), Color.PINK));
        colorsTable.add(imagePink).size(40).left();

        colorsTable.add(getNewLabelWithColor(languages.get("brown"), new Color(.6f, .3f, 0, 1)));
        colorsTable.add(imageBrown).size(40).left();

        colorsTable.row();
        colorsTable.add(getNewLabelWithColor(languages.get("purple"), Color.PURPLE));
        colorsTable.add(imagePurple).size(40).left();

        colorsTable.add(getNewLabelWithColor(languages.get("red"), Color.RED));
        colorsTable.add(imageRed).size(40).left();
    }

    private Label getNewLabelWithColor(String text, Color color) {
        LabelStyle colorsLabelStyle = new LabelStyle(Assets.fontSmall, color);
        Label label = new Label(text, colorsLabelStyle);
        if (Settings.selectedLanguage == Languages.RUSSIAN) {
            label.setFontScale(.7f);
        }
        return label;
    }

    private TextButton createButton(String texto, Languages language, TextureRegionDrawable flag) {
        TextButton buttonAuxiliary = new TextButton(texto, Assets.textButtonStyle);
        if (flag != null) {
            buttonAuxiliary.add(new Image(flag));
        }
        if (language != null) {
            buttonAuxiliary.addListener(addClickListener(language));
        }
        buttonAuxiliary.getLabel().setFontScale(.8f);
        return buttonAuxiliary;
    }

    private ClickListener addClickListener(final Languages language) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.selectedLanguage = language;
                Settings.save();
                Assets.loadAssetsWithSettings();
                languages = Assets.languagesBundle;
                fillTableColores();
            }
        };
    }
}