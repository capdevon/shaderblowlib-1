package com.jme3.shaderblow.test;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.util.SkyFactory.EnvMapType;

/**
 * @author H
 */
public class SceneHelper {

    /**
     *
     * @param assetManager
     * @param rootNode
     * @return
     */
    public static Geometry buildFloor(AssetManager assetManager, Node rootNode) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("ColorMap", texture);

        Box box = new Box(40, 0.25f, 40);
        box.scaleTextureCoordinates(new Vector2f(10, 10));
        Geometry floor = new Geometry("Floor", box);
        floor.setMaterial(mat);
        floor.setLocalTranslation(0, -0.25f, 0);

        rootNode.attachChild(floor);
        return floor;
    }

    /**
     *
     * @param rootNode
     */
    public static void buildLights(Node rootNode) {
        DirectionalLight sun = new DirectionalLight();
        sun.setName("SunLight");
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setName("AmbientLight");
        ambient.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        rootNode.addLight(ambient);
    }

    /**
     *
     * @param am
     * @param rootNode
     */
    public static void buildTestModel(AssetManager am, Node rootNode) {
        String dirName = "Materials/MatCap/";
        loadModel(am, dirName + "MatCap1.j3m", new Vector3f(0, 0, 0), rootNode);
        loadModel(am, dirName + "MatCapBump1.j3m", new Vector3f(2, 0, 0), rootNode);
        loadModel(am, dirName + "MatCap2.j3m", new Vector3f(-2, 0, 0), rootNode);
    }

    private static void loadModel(AssetManager am, String matDef, Vector3f position, Node parent) {
        Spatial sp = am.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        Material mat = am.loadMaterial(matDef);
        sp.setMaterial(mat);
        TangentBinormalGenerator.generate(sp);
        sp.setLocalTranslation(position);
        parent.attachChild(sp);
    }

    /**
     *
     * @param assetManager
     * @param rootNode
     */
    public static void buildSkybox(AssetManager assetManager, Node rootNode) {
        Node mainScene = new Node();
        mainScene.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", EnvMapType.CubeMap));
        rootNode.attachChild(mainScene);
    }
}
