package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * A town with fog and colisions.
 */
public class Main extends SimpleApplication implements ActionListener{
    private CharacterControl player;
    private PlayerConfig playerConfig;
    private final List<Enemy> enemies = new ArrayList<>();
    private boolean enemiesCreated = false;
    private BulletAppState bulletAppState;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState(); // Inicializar bulletAppState aquí
        stateManager.attach(bulletAppState);
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
        
        Vector3f playerLocation = player.getPhysicsLocation();
        if (playerLocation != null) { // Verificar si la ubicación del jugador es null
            for (int i = 0; i < 10; i++) { // Crear 10 enemigos
                Enemy enemy = new Enemy(assetManager, 0.1f, playerLocation, sceneInitializer); // Pasar la ubicación del jugador
                enemies.add(enemy);
                bulletAppState.getPhysicsSpace().add(enemy.getControl());
                rootNode.attachChild(enemy.getModel()); // Agregar el modelo del enemigo a la escena
            }
            enemiesCreated = true;
        }
        
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
        
        // Actualizar enemigos
        for (Enemy enemy : enemies) {
            enemy.update(player.getPhysicsLocation(), enemies);
        }
    }
}