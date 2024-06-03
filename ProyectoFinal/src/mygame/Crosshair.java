package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;

public class Crosshair extends BaseAppState {
    private BitmapText crosshair;

    @Override
    protected void initialize(Application app) {
        SimpleApplication simpleApp = (SimpleApplication) app;
        BitmapFont font = simpleApp.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        crosshair = new BitmapText(font, false);
        crosshair.setSize(font.getCharSet().getRenderedSize() * 2);
        crosshair.setText("+");
        crosshair.setLocalTranslation(
                simpleApp.getCamera().getWidth() / 2 - font.getCharSet().getRenderedSize() / 3 * 2,
                simpleApp.getCamera().getHeight() / 2 + crosshair.getLineHeight() / 2,
                0);
        simpleApp.getGuiNode().attachChild(crosshair);
    }

    @Override
    protected void cleanup(Application app) {
        SimpleApplication simpleApp = (SimpleApplication) app;
        simpleApp.getGuiNode().detachChild(crosshair);
    }

    @Override
    protected void onEnable() {
        crosshair.setCullHint(BitmapText.CullHint.Never);
    }

    @Override
    protected void onDisable() {
        crosshair.setCullHint(BitmapText.CullHint.Always);
    }
}

