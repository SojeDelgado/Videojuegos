package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.Vector3f;
import com.jme3.bullet.BulletAppState;
import java.util.ArrayList;
import java.util.List;

public class WaveManager extends BaseAppState {
    private int currentWave = 0;
    private final int[] enemiesPerWave = {10, 20, 25};
    private final SimpleApplication app;
    private final BulletAppState bulletAppState;
    private final List<Enemy> enemies;
    private final Vector3f playerLocation;
    private final SceneInitializer sceneInitializer;
    private BitmapText enemyCounterText;

    public WaveManager(SimpleApplication app, BulletAppState bulletAppState, List<Enemy> enemies, Vector3f playerLocation, SceneInitializer sceneInitializer) {
        this.app = app;
        this.bulletAppState = bulletAppState;
        this.enemies = enemies;
        this.playerLocation = playerLocation;
        this.sceneInitializer = sceneInitializer;
    }

    @Override
    protected void initialize(Application app) {
        // Inicializar contador de enemigos
        BitmapFont font = this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        enemyCounterText = new BitmapText(font, false);
        enemyCounterText.setSize(font.getCharSet().getRenderedSize() * 2);
        enemyCounterText.setLocalTranslation(10, this.app.getCamera().getHeight() - 10, 0);
        this.app.getGuiNode().attachChild(enemyCounterText);
        
        startNextWave();
    }

    @Override
    protected void cleanup(Application app) {
        this.app.getGuiNode().detachChild(enemyCounterText);
    }

    @Override
    protected void onEnable() {
        enemyCounterText.setCullHint(BitmapText.CullHint.Never);
    }

    @Override
    protected void onDisable() {
        enemyCounterText.setCullHint(BitmapText.CullHint.Always);
    }

    @Override
    public void update(float tpf) {
        // Actualizar el contador de enemigos
        enemyCounterText.setText("Enemies: " + enemies.size());

        // Verificar si la oleada actual ha terminado
        if (enemies.isEmpty() && currentWave < enemiesPerWave.length) {
            startNextWave();
        }
    }

    public void startNextWave() {
        if (currentWave < enemiesPerWave.length) {
            int numEnemies = enemiesPerWave[currentWave];
            for (int i = 0; i < numEnemies; i++) {
                if (currentWave == 0) {
                    // Crear enemigos que requieran 3 clics para ser eliminados
                    Enemy enemy = new Enemy(app.getAssetManager(), 0.1f, playerLocation, sceneInitializer, 3);
                    enemies.add(enemy);
                } else if (currentWave == 1) {
                    // Crear enemigos que requieran 5 clics para ser eliminados
                    Enemy enemy = new Enemy(app.getAssetManager(), 0.1f, playerLocation, sceneInitializer, 5);
                    enemies.add(enemy);
                } else if (currentWave == 2) {
                    // Crear enemigos que requieran 3 clics para ser eliminados
                    Enemy enemy1 = new Enemy(app.getAssetManager(), 0.1f, playerLocation, sceneInitializer, 3);
                    enemies.add(enemy1);
                    // Crear enemigos que requieran 5 clics para ser eliminados
                    Enemy enemy2 = new Enemy(app.getAssetManager(), 0.1f, playerLocation, sceneInitializer, 5);
                    enemies.add(enemy2);
                }
                // Agregar más lógica para manejar diferentes tipos de oleadas según sea necesario
            }
            if (currentWave == 2) {
                // Agregar un enemigo jefe que requiera 15 clics en la tercera oleada
                Enemy bossEnemy = new Enemy(app.getAssetManager(), 0.1f, playerLocation, sceneInitializer, 15);
                enemies.add(bossEnemy);
            }
            currentWave++;
        }
    }
}
