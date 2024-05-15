/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author josem
 */
public class SceneInitializer {
    private final AssetManager assetManager;
    private final Node rootNode;
    private final BulletAppState bulletAppState;
    private final ViewPort viewPort;

    public SceneInitializer(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState, ViewPort viewPort) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
        this.viewPort = viewPort;
    }

    public void initializeScene() {
        
        // We load the scene from the zip file and adjust its size.
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneGeo = assetManager.loadModel("main.scene");
        sceneGeo.setLocalScale(2f);
        sceneGeo.setLocalTranslation(0, -1, 0);
        rootNode.attachChild(sceneGeo);

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneGeo);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneGeo.addControl(landscape);

        // We attach the scene to the rootnode and the physics space,
        // to make it appear in the game world.
        rootNode.attachChild(sceneGeo);
        bulletAppState.getPhysicsSpace().add(landscape);
    }
    
    // initialize fog
    public void initializeFog() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        FogFilter fogFilter = new FogFilter();
        fogFilter.setFogDistance(155);
        fogFilter.setFogDensity(2.0f);
        fogFilter.setFogColor(ColorRGBA.Gray);
        fpp.addFilter(fogFilter);
        viewPort.addProcessor(fpp);
    }
    
    // initialize DL
    public void initializeLight() {
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(1f, -1f, -1f));
        rootNode.addLight(dl);
    }
}
