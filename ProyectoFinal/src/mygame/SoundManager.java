package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioData.DataType;
import com.jme3.scene.Node;
import java.util.Random;

public class SoundManager {
    private AudioNode shootSound;
    private AudioNode startSound;
    private AudioNode hitSound;
    private AudioNode roundChangeSound;
    private AudioNode roundEndSound;
    private AudioNode footStep;
    
    private AudioNode[] hitSounds; // Array de sonidos de golpeo
    private Random random;

    public SoundManager(AssetManager assetManager, Node rootNode) {
        // Cargar el sonido de disparo
        shootSound = new AudioNode(assetManager, "Sounds/shoot.wav", DataType.Buffer);
        shootSound.setPositional(false);
        shootSound.setLooping(false);
        shootSound.setVolume(2);  // Ajusta el volumen según sea necesario
        rootNode.attachChild(shootSound);

        // Cargar el sonido de inicio
        startSound = new AudioNode(assetManager, "Sounds/start.wav", DataType.Buffer);
        startSound.setPositional(false);
        startSound.setLooping(false);
        startSound.setVolume(2);  // Ajusta el volumen según sea necesario
        rootNode.attachChild(startSound);

        // Cargar el sonido de impacto
        hitSound = new AudioNode(assetManager, "Sounds/hitmarker.wav", DataType.Buffer);
        hitSound.setPositional(false);
        hitSound.setLooping(false);
        hitSound.setVolume(3);  // Ajusta el volumen según sea necesario
        rootNode.attachChild(hitSound);
        
        // Cargar el sonido de cambio de ronda
        roundChangeSound = new AudioNode(assetManager, "Sounds/roundstart.wav", DataType.Buffer);
        roundChangeSound.setPositional(false);
        roundChangeSound.setLooping(false);
        roundChangeSound.setVolume(2);  // Ajusta el volumen según sea necesario
        rootNode.attachChild(roundChangeSound);
        
        // Cargar el sonido de cambio de ronda
        roundEndSound = new AudioNode(assetManager, "Sounds/roundend.wav", DataType.Buffer);
        roundEndSound.setPositional(false);
        roundEndSound.setLooping(false);
        roundEndSound.setVolume(2);  // Ajusta el volumen según sea necesario
        rootNode.attachChild(roundEndSound);
        
        
        // Cargar el sonido de pisadas
        footStep = new AudioNode(assetManager, "Sounds/footstep.wav", DataType.Buffer);
        footStep.setPositional(false);
        footStep.setLooping(false);
        footStep.setVolume(1);  // Ajusta el volumen según sea necesario
        rootNode.attachChild(footStep);
        
        // Cargar los sonidos de golpeo
        hitSounds = new AudioNode[3]; // 3 sonidos de golpeo diferentes
        for (int i = 0; i < hitSounds.length; i++) {
            hitSounds[i] = new AudioNode(assetManager, "Sounds/hit" + (i+1) + ".wav", DataType.Buffer);
            hitSounds[i].setPositional(false);
            hitSounds[i].setLooping(false);
            hitSounds[i].setVolume(3);  // Ajusta el volumen según sea necesario
            rootNode.attachChild(hitSounds[i]);
        }
        random = new Random();
    }

    public void playShootSound() {
        shootSound.playInstance();
    }

    public void playStartSound() {
        startSound.playInstance();
    }

    public void playHitSound() {
        hitSound.playInstance();
    }
    
    public void playRoundChangeSound() {
        roundChangeSound.playInstance();
    }
    
    public void playRoundEndSound() {
        roundEndSound.playInstance();
    }
    
    public void playFootstepsSound(){
        footStep.playInstance();
    }
    
    // Método para reproducir un sonido de golpeo aleatorio
    public void playRandomHitSound() {
        int randomIndex = random.nextInt(hitSounds.length);
        hitSounds[randomIndex].playInstance();
    }
}
