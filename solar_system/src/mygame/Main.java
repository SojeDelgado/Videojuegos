package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    public double angulo, angulo2, angulo3, angulo4, angulo5, angulo6, angulo7, angulo8, angulo9, anguloLuna = 0; //Variables for the angles
    float orbitMercurio = 7.0f;
    float orbitVenus = 12.0f;
    float orbitTierra = 17.0f;
    float orbitMarte = 22.0f;
    float orbitJupiter = 32.0f;
    float orbitSaturno = 40.0f;
    float orbitUrano = 44.0f;
    float orbitNeptuno = 48.0f;
    float orbitPluton = 52.0f;
    
    
    
    //Crear una orbita dependiendo del radio
    private Node crearOrbita(float radio) {
        int numVertices = 100; // Número de vértices para aproximar el círculo
        Vector3f[] vertices = new Vector3f[numVertices];
        for (int i = 0; i < numVertices; i++) {
            float angulo = FastMath.TWO_PI * i / numVertices;
            float x = FastMath.cos(angulo) * radio;
            float y = FastMath.sin(angulo) * radio;
            vertices[i] = new Vector3f(x, y, 0);
        }

        Mesh orbitaMesh = new Mesh();
        orbitaMesh.setMode(Mesh.Mode.LineLoop);
        orbitaMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        Geometry orbitaGeom = new Geometry("Orbita", orbitaMesh);
        Material orbitaMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        orbitaMat.setColor("Color", ColorRGBA.White);
        orbitaGeom.setMaterial(orbitaMat);

        Node orbitaNodo = new Node("OrbitaNodo");
        orbitaNodo.attachChild(orbitaGeom);
        return orbitaNodo;
    }
    
    Sphere sol = new Sphere(32, 32, 5f);
    Geometry geom = new Geometry("Sphere", sol);
    
    Sphere mercurio = new Sphere(32, 32, 1f);
    Geometry geom2 = new Geometry("Sphere", mercurio);
    
    Sphere venus = new Sphere(32, 32, 2.2f);
    Geometry geom3 = new Geometry("Sphere", venus);

    Sphere tierra = new Sphere(32, 32, 2.2f);
    Geometry geom4 = new Geometry("Sphere", tierra);
    
    Sphere luna = new Sphere(32, 32, 0.5f);
    Geometry geomL = new Geometry("Sphere", luna);
    
    Sphere marte = new Sphere(32, 32, 1.5f);
    Geometry geom5 = new Geometry("Sphere", marte);

    Sphere jupiter = new Sphere(32, 32, 4f);
    Geometry geom6 = new Geometry("Sphere", jupiter);

    Sphere saturno = new Sphere(32, 32, 3f);
    Geometry geom7 = new Geometry("Sphere", saturno);
    
    Sphere urano = new Sphere(32, 32, 2f);
    Geometry geom8 = new Geometry("Sphere", urano);
    
    Sphere neptuno = new Sphere(32, 32, 2f);
    Geometry geom9 = new Geometry("Sphere", neptuno);
    
    Sphere pluton = new Sphere(32, 32, 1.0f);
    Geometry geom10 = new Geometry("Sphere", pluton);
    
    Vector3f camera = new Vector3f(0f, 0f, 125f);
    
    
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        Main app = new Main();
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
    
        flyCam.setMoveSpeed(12.0f);
        cam.setLocation(camera);
        
        //Orbitas
        Node orbitaMercurio = crearOrbita(orbitMercurio);
        Node orbitaVenus = crearOrbita(orbitVenus);
        Node orbitaTierra = crearOrbita(orbitTierra);
        Node orbitaMarte = crearOrbita(orbitMarte);
        Node orbitaJupiter = crearOrbita(orbitJupiter);
        Node orbitaSaturno = crearOrbita(orbitSaturno);
        Node orbitaUrano = crearOrbita(orbitUrano);
        Node orbitaNeptuno = crearOrbita(orbitNeptuno);
        Node orbitaPluton = crearOrbita(orbitPluton);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat5 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat6 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat7 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat8 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat9 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat10 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material matLuna = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        mat.setColor("Color", ColorRGBA.Yellow);
        mat2.setColor("Color", ColorRGBA.Brown);
        mat3.setColor("Color", ColorRGBA.White);
        mat4.setColor("Color", ColorRGBA.Blue);
        mat5.setColor("Color", ColorRGBA.Red);
        mat6.setColor("Color", ColorRGBA.Brown);
        mat7.setColor("Color", ColorRGBA.Cyan);
        mat8.setColor("Color", ColorRGBA.Gray);
        mat9.setColor("Color", ColorRGBA.LightGray);
        mat10.setColor("Color", ColorRGBA.Pink);
        matLuna.setColor("Color", ColorRGBA.White);
        
        geom.setMaterial(mat);
        geom2.setMaterial(mat2);
        geom3.setMaterial(mat3);
        geom4.setMaterial(mat4);
        geom5.setMaterial(mat5);
        geom6.setMaterial(mat6);
        geom7.setMaterial(mat7);
        geom8.setMaterial(mat8);
        geom9.setMaterial(mat9);
        geom10.setMaterial(mat10);
        geomL.setMaterial(matLuna);
        
        rootNode.attachChild(geom);
        rootNode.attachChild(geom2);
        rootNode.attachChild(geom3);
        rootNode.attachChild(geom4);
        rootNode.attachChild(geom5);
        rootNode.attachChild(geom6);
        rootNode.attachChild(geom7);
        rootNode.attachChild(geom8);
        rootNode.attachChild(geom9);
        rootNode.attachChild(geom10);
        rootNode.attachChild(geomL);
        rootNode.attachChild(orbitaMercurio);
        rootNode.attachChild(orbitaVenus);
        rootNode.attachChild(orbitaTierra);
        rootNode.attachChild(orbitaMarte);
        rootNode.attachChild(orbitaJupiter);
        rootNode.attachChild(orbitaSaturno);
        rootNode.attachChild(orbitaUrano);
        rootNode.attachChild(orbitaNeptuno);
        rootNode.attachChild(orbitaPluton);
        
    }


    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        angulo = angulo + 1.0;
        angulo2 = angulo2 + 0.5;
        angulo3 = angulo3 + 0.2;
        angulo4 = angulo4 + 0.1;
        angulo5 = angulo5 + 0.05;
        angulo6 = angulo6 + 0.02;
        angulo7 = angulo7 + 0.01;
        angulo8 = angulo8 + 0.0065;
        angulo9 = angulo9 + 0.0050;
        anguloLuna = anguloLuna + 5.0;
        
        
        float r = FastMath.DEG_TO_RAD;
        double Merc = FastMath.DEG_TO_RAD*angulo;
        double Ven = FastMath.DEG_TO_RAD*angulo2;
        double Tier = FastMath.DEG_TO_RAD*angulo3;
        double Marte = FastMath.DEG_TO_RAD*angulo4;
        double Jupi = FastMath.DEG_TO_RAD*angulo5;
        double Satur = FastMath.DEG_TO_RAD*angulo6;
        double Uran = FastMath.DEG_TO_RAD*angulo7;
        double Neptu = FastMath.DEG_TO_RAD*angulo8;
        double Pluton = FastMath.DEG_TO_RAD*angulo9;
        double radianesLuna = FastMath.DEG_TO_RAD*anguloLuna;
        
        //Position of the planets in the space
        
        int rMer = 7;
        int rVenus = 12;
        int rTierra = 17;
        int rLuna = 3;
        int rMarte = 22;
        int rJupiter = 32;
        int rSaturno = 40;
        int rUrano = 44;
        int rNeptuno = 48;
        int rPluton = 52;
        
        
        //Coordinates to make the planets go around the sun
        
        float xMer = (float) Math.sin(Merc)*rMer;
        float yMer = (float) Math.cos(Merc)*rMer;
        
        float xVenus = (float) Math.sin(Ven)*rVenus;
        float yVenus = (float) Math.cos(Ven)*rVenus;
        
        float xTierra = (float) Math.sin(Tier)*rTierra;
        float yTierra = (float) Math.cos(Tier)*rTierra;
        
        float xLuna = (float) Math.sin(radianesLuna) * rLuna + xTierra;
        float yLuna = (float) Math.cos(radianesLuna) * rLuna + yTierra;
                
        
        float xMarte = (float) Math.sin(Marte)*rMarte;
        float yMarte = (float) Math.cos(Marte)*rMarte;
        
        float xJupiter = (float) Math.sin(Jupi)*rJupiter;
        float yJupiter = (float) Math.cos(Jupi)*rJupiter;
        
        float xSaturno = (float) Math.sin(Satur)*rSaturno;
        float ySaturno = (float) Math.cos(Satur)*rSaturno;
        
        float xUrano = (float) Math.sin(Uran)*rUrano;
        float yUrano = (float) Math.cos(Uran)*rUrano;
        
        float xNeptuno = (float) Math.sin(Neptu)*rNeptuno;
        float yNeptuno = (float) Math.cos(Neptu)*rNeptuno;
        
        float xPluton = (float) Math.sin(Pluton)*rPluton;
        float yPluton = (float) Math.cos(Pluton)*rPluton;
        
         
        
        geom.rotate(r,0f,0f);
        geom2.rotate(r,0f,0f);
        geom3.rotate(r,0f,0f);
        geom4.rotate(r,0f,0f);
        geom5.rotate(r,0f,0f);
        geom6.rotate(r,0f,0f);
        geom7.rotate(r,0f,0f);
        geom8.rotate(r,0f,0f);
        geom9.rotate(r,0f,0f);
        geom10.rotate(r,0f,0f);
        geomL.rotate(r,0f,0f);
        
        //Rotation against the sun
        
        Vector3f orbMercurio = new Vector3f (xMer , yMer, 0);
        Vector3f orbVenus = new Vector3f (xVenus , yVenus, 0);
        
        Vector3f orbTierra = new Vector3f (xTierra , yTierra, 0);
        
        Vector3f orbLuna = new Vector3f (xLuna , yLuna, 0);
        
        Vector3f orbMarte = new Vector3f (xMarte , yMarte, 0);
        Vector3f orbJupiter = new Vector3f (xJupiter , yJupiter, 0);
        Vector3f orbSaturno = new Vector3f (xSaturno , ySaturno, 0);
        Vector3f orbUrano = new Vector3f (xUrano , yUrano, 0);
        Vector3f orbNeptuno = new Vector3f (xNeptuno , yNeptuno, 0);
        Vector3f orbPluton = new Vector3f (xPluton , yPluton, 0);
        
        geom2.setLocalTranslation(orbMercurio);
        geom3.setLocalTranslation(orbVenus);
        geom4.setLocalTranslation(orbTierra);
        geom5.setLocalTranslation(orbMarte);
        geom6.setLocalTranslation(orbJupiter);
        geom7.setLocalTranslation(orbSaturno);
        geom8.setLocalTranslation(orbUrano);
        geom9.setLocalTranslation(orbNeptuno);
        geom10.setLocalTranslation(orbPluton);
        geomL.setLocalTranslation(orbLuna);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
