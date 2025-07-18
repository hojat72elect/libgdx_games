package me.vrekt.oasis.gui.guis.hud;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.EnumMap;

import me.vrekt.oasis.GameManager;
import me.vrekt.oasis.entity.player.sp.PlayerSP;
import me.vrekt.oasis.gui.Gui;
import me.vrekt.oasis.gui.GuiManager;
import me.vrekt.oasis.gui.GuiType;
import me.vrekt.oasis.gui.Styles;
import me.vrekt.oasis.gui.guis.hud.components.HudAttributeComponent;
import me.vrekt.oasis.gui.guis.hud.components.HudGameActionComponent;
import me.vrekt.oasis.gui.guis.hud.components.HudHotbarComponent;
import me.vrekt.oasis.gui.guis.hud.components.HudInteractionsComponent;
import me.vrekt.oasis.gui.guis.hud.components.HudItemAcquiredComponent;
import me.vrekt.oasis.gui.guis.hud.components.HudItemHintComponent;
import me.vrekt.oasis.gui.guis.hud.components.HudNewQuestComponent;
import me.vrekt.oasis.gui.guis.hud.components.HudPlayerHealthComponent;
import me.vrekt.oasis.gui.guis.hud.components.HudPlayerHintComponent;

/**
 * Main game user interface.
 */
public final class GameHudGui extends Gui {

    // avoid allocating strings over and over
    // may or may not matter much, but regardless.
    private static final String FPS = "FPS: ";
    private static final String PING = "Ping: ";
    private static final String MSPT = "MSPT: ";
    private static final String MSPT_SERVER = "S-MSPT: ";
    private static final String MS = "ms";
    private final StringBuilder builder;

    private final PlayerSP player;
    private final EnumMap<HudComponentType, HudComponent> components = new EnumMap<>(HudComponentType.class);
    private final VisLabel debugComponentText;
    private final MathContext performanceMetricsContext;

    public GameHudGui(GuiManager guiManager) {
        super(GuiType.HUD, guiManager);

        this.player = guiManager.getGame().player();
        this.isShowing = true;

        performanceMetricsContext = new MathContext(2, RoundingMode.FLOOR);

        debugComponentText = new VisLabel();
        debugComponentText.setStyle(Styles.getSmallWhite());

        components.put(HudComponentType.ATTRIBUTE, new HudAttributeComponent(guiManager));
        // components.put(HudComponentType.ARTIFACT, new HudArtifactComponent(guiManager));
        components.put(HudComponentType.INTERACTIONS, new HudInteractionsComponent(guiManager));
        components.put(HudComponentType.HINT, new HudPlayerHintComponent(guiManager));
        components.put(HudComponentType.HOT_BAR, new HudHotbarComponent(guiManager));
        components.put(HudComponentType.ITEM_HINT, new HudItemHintComponent(guiManager));
        components.put(HudComponentType.GAME_ACTION, new HudGameActionComponent(guiManager));
        components.put(HudComponentType.HEALTH, new HudPlayerHealthComponent(guiManager));
        components.put(HudComponentType.ITEM_ACQUIRED, new HudItemAcquiredComponent(guiManager));
        components.put(HudComponentType.NEW_QUEST, new HudNewQuestComponent(guiManager));

        initializeDebugComponent();
        builder = new StringBuilder();
        this.timedUpdateInterval = 1f;
    }

    /**
     * Get a hud component
     *
     * @param type the type
     * @param <T>  TYPE
     * @return the component
     */
    public <T extends HudComponent> T getComponent(HudComponentType type) {
        return (T) components.get(type);
    }

    @Override
    public void update() {
        final float now = GameManager.tick();
        for (HudComponent component : components.values()) {
            component.update(now);
        }
    }

    /**
     * Initialize separate ui component for debug stats.
     */
    private void initializeDebugComponent() {
        final VisTable debugTable = new VisTable();
        debugTable.setVisible(true);
        debugTable.top().left();
        debugTable.add(debugComponentText).top().left();
        guiManager.addGui(debugTable);
    }

    @Override
    public void timedUpdate(float tick) {
        builder.setLength(0);
        builder.append(FPS)
                .append(Gdx.graphics.getFramesPerSecond())
                .append(StringUtils.LF)
                .append(MSPT)
                .append(BigDecimal.valueOf(player.getWorldState().averageTickingTime()).round(performanceMetricsContext))
                .append(StringUtils.LF).append(Math.floor(player.getX())).append(",").append(Math.floor(player.getY()));

        // if in a multiplayer game display ping.
        if (guiManager.getGame().isInMultiplayerGame()) {
            builder.append(StringUtils.LF);
            builder.append(PING)
                    .append(player.getConnection().getPingMs())
                    .append(StringUtils.SPACE);
        } else if (guiManager.getGame().isHostingMultiplayerGame()) {
            // otherwise display average MSPT.
            builder.append(StringUtils.LF);
            builder.append(MSPT_SERVER)
                    .append(guiManager.getGame().integratedServer().mspt())
                    .append(MS);
        }

        debugComponentText.setText(builder.toString());
    }

    @Override
    public void show() {
        super.show();

        debugComponentText.setVisible(true);
        for (HudComponent component : components.values()) {
            if (component.componentType != HudComponentType.HINT
                    && component.componentType != HudComponentType.ITEM_HINT
                    && component.componentType != HudComponentType.NEW_QUEST)
                component.show();
        }
    }

    @Override
    public void hide() {
        super.hide();

        debugComponentText.setVisible(false);
        for (HudComponent component : components.values()) {
            if (component.componentType != HudComponentType.HINT
                    && component.componentType != HudComponentType.NEW_QUEST) {
                component.hide();
            }
        }
    }

    /**
     * Hide certain components while the chat is open
     */
    public void hideComponentsForChat() {
        getComponent(HudComponentType.HOT_BAR).hide();
        getComponent(HudComponentType.ARTIFACT).hide();
    }

    /**
     * Show hidden components from the chat being opened
     */
    public void showComponentsAfterChat() {
        getComponent(HudComponentType.HOT_BAR).show();
        getComponent(HudComponentType.ARTIFACT).show();
    }
}
