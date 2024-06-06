package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;

public class Enemy {
    private final CharacterControl enemyControl;
    private final Vector3f walkDirection = new Vector3f();
    private final Spatial model; // Modelo visual del enemigo
    private Vector3f playerLocation;
    private final float speed = 0.3f; // Velocidad del enemigo
    private final SceneInitializer sceneInitializer;
    private int hits = 0;
    private boolean markedForRemoval = false;
    private final int clicksToDestroy; // Número de clics necesarios para eliminar al enemigo
    
    private final int damage = 10; // Daño que hace al jugador
    private float attackCooldown = 1.0f; // Tiempo de cooldown entre golpes en segundos
    private float timeSinceLastAttack = 0.0f; // Tiempo transcurrido desde el último golpe
    private final SoundManager soundManager;

    public Enemy(AssetManager assetManager, float par, Vector3f playerLocation, SceneInitializer sceneInitializer, int clicksToDestroy, SoundManager soundManager) {
        this.sceneInitializer = sceneInitializer;
        this.clicksToDestroy = clicksToDestroy;
        this.soundManager = soundManager;
        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 0, 1);
        enemyControl = new CharacterControl(capsuleShape, 0.05f);
        enemyControl.setJumpSpeed(20);
        enemyControl.setFallSpeed(30);
        enemyControl.setGravity(30);

        if (clicksToDestroy == 3) {
            model = assetManager.loadModel("Models/enemigo2/Ene.j3o");
            model.setLocalScale(0.9f); // Ajustar el tamaño del modelo
        } else if (clicksToDestroy == 5) {
            model = assetManager.loadModel("Models/enemigo1/enemigo1.j3o");
            model.setLocalScale(0.3f); // Ajustar el tamaño del modelo
        } else if (clicksToDestroy == 15) {
            model = assetManager.loadModel("Models/Oto/OtoOldAnim.j3o");
            model.setLocalScale(2.0f); // Ajustar el tamaño del modelo
        } else {
            // Si se proporciona un número de clics desconocido, se usa un modelo predeterminado
            model = assetManager.loadModel("Models/enemigo1/enemigo1.j3o");
            model.setLocalScale(0.3f); // Ajustar el tamaño del modelo
        }
        model.addControl(enemyControl); // Agregar el control de personaje al modelo

        if (playerLocation != null) {
            this.playerLocation = new Vector3f(playerLocation); // Crear una nueva copia de playerLocation

            float minDistance = 100; // La distancia mínima desde el jugador
            float maxDistance = 200; // La distancia máxima desde el jugador
            float distance = minDistance + (float)Math.random() * (maxDistance - minDistance); // Generar una distancia aleatoria entre minDistance y maxDistance
            float angle = (float)Math.random() * 2 * (float)Math.PI; // Generar un ángulo aleatorio
            float x = playerLocation.x + distance * (float)Math.cos(angle);
            float z = playerLocation.z + distance * (float)Math.sin(angle);
            float y = sceneInitializer.getTerrainHeight(x, z);
            enemyControl.setPhysicsLocation(new Vector3f(x, y + 5, z)); // Posición aleatoria a una distancia del jugador
        } else {
            enemyControl.setPhysicsLocation(new Vector3f((float)(Math.random()*50-25), 10, (float)(Math.random()*50-25))); // Posición aleatoria
        }
    }

    public void update(float tpf, Vector3f playerLocation, List<Enemy> enemies, PlayerConfig player) {
        if (playerLocation != null) {
            this.playerLocation = new Vector3f(playerLocation); // Actualizar la ubicación del jugador

            // Calcular la dirección hacia el jugador
            Vector3f directionToPlayer = playerLocation.subtract(enemyControl.getPhysicsLocation()).normalizeLocal();
            
            // Actualiza el tiempo transcurrido desde el último golpe
            timeSinceLastAttack += tpf;
            
            // Detecta la distancia entre el enemigo y el jugador
            float distanceToPlayer = enemyControl.getPhysicsLocation().distance(playerLocation);
            
            // Si el enemigo está lo suficientemente cerca del jugador, golpéalo
             if (distanceToPlayer < 2.0f && timeSinceLastAttack >= attackCooldown) { 
                // Reduce la vida del jugador
                player.reducePlayerHealth(damage);
                soundManager.playRandomHitSound();
                timeSinceLastAttack = 0.0f;
            }

            model.lookAt(playerLocation, Vector3f.UNIT_Y);
            model.rotate(-FastMath.HALF_PI, 0, 0); // Rotar el modelo 90 grados alrededor del eje X

            // Calcular la dirección de separación de otros enemigos
            Vector3f separation = new Vector3f();
            for (Enemy enemy : enemies) {
                if (enemy != this) { // No calcular la separación con uno mismo
                    float distance = enemyControl.getPhysicsLocation().distance(enemy.getControl().getPhysicsLocation());
                    if (distance < 10) { // Si el enemigo está demasiado cerca
                        Vector3f directionFromEnemy = enemyControl.getPhysicsLocation().subtract(enemy.getControl().getPhysicsLocation()).normalizeLocal();
                        separation.addLocal(directionFromEnemy);
                    }
                }
            }

            // Calcular la dirección final como una combinación de la dirección hacia el jugador y la dirección de separación
            Vector3f finalDirection = directionToPlayer.add(separation).normalizeLocal();

            walkDirection.set(finalDirection).multLocal(speed);
            enemyControl.setWalkDirection(walkDirection);

            // Verificar si el enemigo ha caído fuera del mapa
            if (enemyControl.getPhysicsLocation().y < 0) { // Ajusta el valor '0' al nivel más bajo de tu mapa
                // Marcar el enemigo para su eliminación
                markForRemoval();
            }
        }
    }

    public CharacterControl getControl() {
        return enemyControl;
    }

    public Spatial getModel() {
        return model;
    }

    public void incrementHits() {
        hits++;
    }

    public int getHits() {
        return hits;
    }

    public void markForRemoval() {
        markedForRemoval = true;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public int getClicksToDestroy() {
        return clicksToDestroy;
    }
}
