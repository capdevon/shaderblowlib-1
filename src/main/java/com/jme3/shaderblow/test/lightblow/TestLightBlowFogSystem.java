package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowFogSystem extends SimpleApplication {

    private Node npc = new Node();

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestLightBlowFogSystem app = new TestLightBlowFogSystem();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(10);
        createSky();
        addLighting();

        Spatial char_boy = assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        Material mat = assetManager.loadMaterial("Materials/LightBlow/Fog_System/LightBlow_Fog.j3m");
        char_boy.setMaterial(mat);
        TangentBinormalGenerator.generate(char_boy);
        // rootNode.attachChild(char_boy);

        Node[] fog1 = new Node[20];
        for (int i = 0; i < fog1.length; i++) {

            Node node = new Node("fog1_" + i);
            node.attachChild(char_boy.clone(false));
            node.setLocalTranslation(0, 0, -(i + 3) * 5);
            System.out.println(node.getName());
            npc.attachChild(node);
        }

        Spatial char_boy2 = assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        Material mat2 = assetManager.loadMaterial("Materials/LightBlow/Fog_System/LightBlow_Fog_Skybox.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(2.0f, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);
        // rootNode.attachChild(char_boy2);

        Node[] fog2 = new Node[20];
        for (int i = 0; i < fog2.length; i++) {

            Node ndd = new Node("fog2_" + i);
            ndd.attachChild(char_boy2.clone(false));
            ndd.setLocalTranslation(3, 0, -(i + 3) * 5);
            System.out.println(ndd.getName());
            npc.attachChild(ndd);
        }

        rootNode.attachChild(npc);
    }

    private void addLighting() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);
    }

    private void createSky() {
        TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, SkyFactory.EnvMapType.CubeMap));
    }

}
