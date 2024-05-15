package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;

/**
 * A town with fog and colisions.
 */
public class Main extends SimpleApplication implements ActionListener{
    private CharacterControl player;
    private PlayerConfig playerConfig;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        playerConfig = new PlayerConfig(cam, inputManager, this);
        player = playerConfig.getPlayer();
        
        
        // Set up physics 
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().add(player);
        
        // Initialize the scene
        SceneInitializer sceneInitializer = new SceneInitializer(assetManager, rootNode, bulletAppState, viewPort);
        sceneInitializer.initializeScene();
        sceneInitializer.initializeFog();
        sceneInitializer.initializeLight();
    }
 
    /** These are our custom actions triggered by key presses.
    * We do not walk yet, we just keep track of the direction the user pressed.
     * @param binding
     * @param value */
    @Override
    public void onAction(String binding, boolean value, float tpf) {
        switch (binding) {
            case "Left" -> playerConfig.setLeft(value);
            case "Right" -> playerConfig.setRight(value);
            case "Up" -> playerConfig.setUp(value);
            case "Down" -> playerConfig.setDown(value);
            case "Jump" -> playerConfig.jump();
            default -> {
            }
        }
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        playerConfig.simpleUpdate(tpf);
    }
}