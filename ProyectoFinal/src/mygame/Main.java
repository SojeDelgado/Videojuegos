package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.FastMath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Main extends SimpleApplication implements ActionListener {
    private CharacterControl player;
    private PlayerConfig playerConfig;
    private final List<Enemy> enemies = new ArrayList<>();
    private BulletAppState bulletAppState;
    private BitmapText enemyCounterText;
    private int currentWave = 0;
    private final int[] waveEnemies = {10, 20, 25}; // Número de enemigos por oleada
    private final int clicksToRemove = 3; // Número de clics necesarios para eliminar un enemigo
    private SceneInitializer sceneInitializer;
    private float waveInterval = 7.0f; // Tiempo de espera entre oleadas en segundos
    private float timeSinceLastWave = 0.0f;

    private float spawnInterval = 1.0f; // Intervalo de aparición en segundos
    private float timeSinceLastSpawn = 0.0f; // Tiempo transcurrido desde la última aparición
    private int enemiesToSpawn = 0; // Número de enemigos pendientes de aparecer

    private final Random random = new Random();
    private final Vector3f minSpawnBounds = new Vector3f(-50, 0, -50); // Limites mínimos de aparición
    private final Vector3f maxSpawnBounds = new Vector3f(50, 0, 50);   // Limites máximos de aparición

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        playerConfig = new PlayerConfig(cam, inputManager, this);
        player = playerConfig.getPlayer();

        bulletAppState.getPhysicsSpace().add(player);

        sceneInitializer = new SceneInitializer(assetManager, rootNode, bulletAppState, viewPort);
        sceneInitializer.initializeScene();
        sceneInitializer.initializeFog();
        sceneInitializer.initializeLight();

        initCrosshair();
        initEnemyCounter();
        spawnWave();
    }

    private void initCrosshair() {
        Crosshair crosshair = new Crosshair();
        stateManager.attach(crosshair);
    }

    private void initEnemyCounter() {
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        enemyCounterText = new BitmapText(font, false);
        enemyCounterText.setSize(font.getCharSet().getRenderedSize());
        enemyCounterText.setLocalTranslation(10, cam.getHeight() - enemyCounterText.getLineHeight(), 0);
        guiNode.attachChild(enemyCounterText);
        updateEnemyCounter();
    }

    private void updateEnemyCounter() {
        enemyCounterText.setText("Enemigos restantes: " + enemies.size());
    }

    private void spawnWave() {
        if (currentWave >= waveEnemies.length) {
            // Lógica para manejar la aparición del jefe de 15 clics después de la tercera oleada
            if (currentWave == waveEnemies.length) {
                enemiesToSpawn = 1; // Solo un enemigo
                currentWave++;
                System.out.println("Spawn boss with 15 clicks");
            } else {
                // No hay más oleadas después del jefe
                return;
            }
        } else {
            enemiesToSpawn = waveEnemies[currentWave];
            currentWave++;
            System.out.println("Spawn wave " + currentWave + " with " + enemiesToSpawn + " enemies");
        }
        updateEnemyCounter();
    }

    private void spawnEnemy() {
        Vector3f spawnLocation = getRandomSpawnLocation();
        Enemy enemy;

        // Determinar qué tipo de enemigo generar en función de la oleada actual
        if (currentWave <= 1) {
            enemy = new Enemy(assetManager, 0.1f, player.getPhysicsLocation(), sceneInitializer, 3);
        } else if (currentWave == 2) {
            enemy = new Enemy(assetManager, 0.1f, player.getPhysicsLocation(), sceneInitializer, 5);
        } else if (currentWave == 3) {
            // Mezclar enemigos de diferentes tipos en la tercera oleada
            int randomNumber = FastMath.nextRandomInt(0, 1); // 0 o 1
            if (randomNumber == 0) {
                enemy = new Enemy(assetManager, 0.1f, player.getPhysicsLocation(), sceneInitializer, 3);
            } else {
                enemy = new Enemy(assetManager, 0.1f, player.getPhysicsLocation(), sceneInitializer, 5);
            }
        } else {
            // En oleadas posteriores, generar solo el enemigo jefe que requiere 15 clics
            enemy = new Enemy(assetManager, 0.1f, player.getPhysicsLocation(), sceneInitializer, 15);
        }

        enemies.add(enemy);
        bulletAppState.getPhysicsSpace().add(enemy.getControl());
        rootNode.attachChild(enemy.getModel());
        updateEnemyCounter();
        System.out.println("Spawned enemy at " + spawnLocation + ", total enemies: " + enemies.size());
    }


    private Vector3f getRandomSpawnLocation() {
        int maxAttempts = 10; // Número máximo de intentos para encontrar una posición válida
        for (int i = 0; i < maxAttempts; i++) {
            float x = minSpawnBounds.x + random.nextFloat() * (maxSpawnBounds.x - minSpawnBounds.x);
            float z = minSpawnBounds.z + random.nextFloat() * (maxSpawnBounds.z - minSpawnBounds.z);
            float y = 0; // Ajusta la altura según sea necesario

            Vector3f potentialLocation = new Vector3f(x, y, z);
            if (isLocationValid(potentialLocation)) {
                return potentialLocation;
            }
        }

        // Si no se encuentra una posición válida, devolvemos una posición predeterminada
        return new Vector3f(0, 0, 0);
    }

    private boolean isLocationValid(Vector3f location) {
        // Aquí puedes agregar las verificaciones necesarias para asegurar que la ubicación es válida.
        // Por ejemplo, puedes verificar que no esté dentro de edificios u otros objetos.

        // En este caso, simplemente verificamos que la posición y no esté fuera de los límites del terreno
        return location.x >= minSpawnBounds.x && location.x <= maxSpawnBounds.x
                && location.z >= minSpawnBounds.z && location.z <= maxSpawnBounds.z
                && location.y >= minSpawnBounds.y && location.y <= maxSpawnBounds.y;
    }

    @Override
    public void simpleUpdate(float tpf) {
        playerConfig.simpleUpdate(tpf);

        // Actualizar enemigos
        for (Enemy enemy : enemies) {
            enemy.update(player.getPhysicsLocation(), enemies);
        }

        // Eliminar enemigos marcados
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.isMarkedForRemoval()) {
                rootNode.detachChild(enemy.getModel());
                bulletAppState.getPhysicsSpace().remove(enemy.getControl());
                iterator.remove();
            }
        }
        updateEnemyCounter();

        // Generar nueva oleada si todos los enemigos fueron eliminados
        if (enemies.isEmpty() && enemiesToSpawn == 0) {
            timeSinceLastWave += tpf;
            if (timeSinceLastWave >= waveInterval) {
                spawnWave();
                timeSinceLastWave = 0.0f; // Reiniciar el contador
            }
        }

        // Aparición gradual de enemigos
        if (enemiesToSpawn > 0) {
            timeSinceLastSpawn += tpf;
            if (timeSinceLastSpawn >= spawnInterval) {
                spawnEnemy();
                enemiesToSpawn--;
                timeSinceLastSpawn = 0;
                System.out.println("Enemies to spawn: " + enemiesToSpawn);
            }
        }
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
        Vector3f camPos = cam.getLocation();
        Vector3f camDir = cam.getDirection();

        Ray ray = new Ray(camPos, camDir);

        for (Enemy enemy : enemies) {
            CollisionResults results = new CollisionResults();
            enemy.getModel().collideWith(ray, results);
            if (results.size() > 0) {
                enemy.incrementHits();
                if (enemy.getHits() >= enemy.getClicksToDestroy()) { // Usar el método getter aquí
                    enemy.markForRemoval();
                }
                break;
            }
        }
    }
}
