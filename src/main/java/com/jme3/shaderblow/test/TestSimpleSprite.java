package com.jme3.shaderblow.test;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestSimpleSprite extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestSimpleSprite app = new TestSimpleSprite();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        Spatial sprite = assetManager.loadModel("Models/SimpleSprite/SimpleSprite.blend");
        Material mat1 = assetManager.loadMaterial("Materials/SimpleSprite/SimpleSprite_1.j3m");
        sprite.setMaterial(mat1);
        sprite.setLocalTranslation(0, 0, 0);
        TangentBinormalGenerator.generate(sprite);
        rootNode.attachChild(sprite);

        Spatial sprite2 = assetManager.loadModel("Models/SimpleSprite/SimpleSprite.blend");
        Material mat2 = assetManager.loadMaterial("Materials/SimpleSprite/SimpleSprite_2.j3m");
        sprite2.setMaterial(mat2);
        sprite2.setLocalTranslation(1, 0, 0);
        sprite2.setLocalScale(0.5f, 1, 1);
        TangentBinormalGenerator.generate(sprite2);
        rootNode.attachChild(sprite2);

        flyCam.setMoveSpeed(10);
        viewPort.setBackgroundColor(ColorRGBA.Gray);
    }

}
