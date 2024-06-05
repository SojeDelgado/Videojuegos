/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mygame;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;

/**
 *
 * @author josem
 */
public class DebugArrow extends Geometry{
    public DebugArrow(AssetManager assetManager, Vector3f direction) {
        super("DebugArrow", new Arrow(direction));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        this.setMaterial(mat);
    }

    public DebugArrow(AssetManager assetManager, Vector3f direction, ColorRGBA color) {
        super("DebugArrow", new Arrow(direction));
        this.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m")); // Cambia el color aquí
    }
    
    public void setLineWidth(float width) {
        ((Arrow) this.getMesh()).setLineWidth(width);
    }

    public void setArrowExtent(Vector3f extent) {
        ((Arrow) this.getMesh()).setArrowExtent(extent);
    }
}