package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.collision.CollisionResults;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A town with fog and colisions.
 */
public class Main extends SimpleApplication implements ActionListener {
    private CharacterControl player;
    private PlayerConfig playerConfig;
    private final List<Enemy> enemies = new ArrayList<>();
    private boolean enemiesCreated = false;
    private BulletAppState bulletAppState;
    private Crosshair crosshair;
    private final int clicksToRemove = 3; // Número de clics necesarios para eliminar un enemigo

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
        bulletAppState.getPhysicsSpace().add(player);

        // Initialize the scene
        SceneInitializer sceneInitializer = new SceneInitializer(assetManager, rootNode, bulletAppState, viewPort);
        sceneInitializer.initializeScene();
        sceneInitializer.initializeFog();
        sceneInitializer.initializeLight();

        Vector3f playerLocation = player.getPhysicsLocation();
        if (playerLocation != null) { // Verificar si la ubicación del jugador es null
            for (int i = 0; i < 5; i++) { // Crear 10 enemigos
                Enemy enemy = new Enemy(assetManager, 0.1f, playerLocation, sceneInitializer); // Pasar la ubicación del jugador
                enemies.add(enemy);
                bulletAppState.getPhysicsSpace().add(enemy.getControl());
                rootNode.attachChild(enemy.getModel()); // Agregar el modelo del enemigo a la escena
            }
            enemiesCreated = true;
        }
        // Attach the Crosshair state
        Crosshair crosshair = new Crosshair();
        stateManager.attach(crosshair);
    }

    @Override
    public void simpleUpdate(float tpf) {
        playerConfig.simpleUpdate(tpf);

        // Actualizar enemigos
        for (Enemy enemy : enemies) {
            enemy.update(player.getPhysicsLocation(), enemies);
        }

        // Eliminar enemigos marcados
        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.isMarkedForRemoval()) {
                enemiesToRemove.add(enemy);
                rootNode.detachChild(enemy.getModel());
                bulletAppState.getPhysicsSpace().remove(enemy.getControl());
            }
        }
        enemies.removeAll(enemiesToRemove);
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        switch (binding) {
            case "Left":
                playerConfig.setLeft(value);
                break;
            case "Right":
                playerConfig.setRight(value);
                break;
            case "Up":
                playerConfig.setUp(value);
                break;
            case "Down":
                playerConfig.setDown(value);
                break;
            case "Jump":
                playerConfig.jump();
                break;
            case "Shoot":
                if (value) {
                    shoot();
                }
                break;
            default:
                break;
        }
    }

    private void shoot() {
        // Obtén la posición actual de la cámara
        Vector3f camPos = cam.getLocation();
        // Obtén la dirección de la cámara
        Vector3f camDir = cam.getDirection();
        // Calcular el punto final del rayo (puedes ajustar la longitud del rayo según sea necesario)
        

        // Crear el rayo
        Ray ray = new Ray(camPos, camDir);
        
        // Verificar colisiones con los enemigos
        for (Enemy enemy : enemies) {
            CollisionResults results = new CollisionResults();
            enemy.getModel().collideWith(ray, results);
            if (results.size() > 0) {
                // Imprimir la posición del enemigo en el momento del disparo
                System.out.println("Hit enemy: " + enemy);
                enemy.incrementHits();
                if (enemy.getHits() >= clicksToRemove) {
                    System.out.println("Marking enemy for removal: " + enemy);
                    enemy.markForRemoval();
                }
                break; // Solo verificar la colisión con un enemigo
            }
        }
    }

}
