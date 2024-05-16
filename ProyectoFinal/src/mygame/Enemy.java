package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;

public class Enemy {
    private final CharacterControl enemyControl;
    private final Vector3f walkDirection = new Vector3f();
    private final Spatial model; // Modelo visual del enemigo
    private Vector3f playerLocation;
    private final float speed = 0.3f; // Velocidad del enemigo


    public Enemy(AssetManager assetManager, float par, Vector3f playerLocation) {
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        enemyControl = new CharacterControl(capsuleShape, 0.05f);
        enemyControl.setJumpSpeed(20);
        enemyControl.setFallSpeed(30);
        enemyControl.setGravity(30);
        

        model = assetManager.loadModel("Models/Oto/OtoOldAnim.j3o"); // Cargar el modelo
        model.addControl(enemyControl); // Agregar el control de personaje al modelo
        
        

        if (playerLocation != null) {
            this.playerLocation = new Vector3f(playerLocation); // Crear una nueva copia de playerLocation

            float minDistance = 100; // La distancia mínima desde el jugador
            float maxDistance = 200; // La distancia máxima desde el jugador
            float distance = minDistance + (float)Math.random() * (maxDistance - minDistance); // Generar una distancia aleatoria entre minDistance y maxDistance
            float angle = (float)Math.random() * 2 * (float)Math.PI; // Generar un ángulo aleatorio
            float x = playerLocation.x + distance * (float)Math.cos(angle);
            float z = playerLocation.z + distance * (float)Math.sin(angle);
            enemyControl.setPhysicsLocation(new Vector3f(x, 10, z)); // Posición aleatoria a una distancia del jugador
        } else {
            enemyControl.setPhysicsLocation(new Vector3f((float)(Math.random()*50-25), 10, (float)(Math.random()*50-25))); // Posición aleatoria
        }
    }


    public void update(Vector3f playerLocation, List<Enemy> enemies) {
        if (playerLocation != null) {
            this.playerLocation = new Vector3f(playerLocation); // Actualizar la ubicación del jugador

            // Calcular la dirección hacia el jugador
            Vector3f directionToPlayer = playerLocation.subtract(enemyControl.getPhysicsLocation()).normalizeLocal();

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
        }
    }

    public CharacterControl getControl() {
        return enemyControl;
    }

    public Spatial getModel() {
        return model;
    }
}