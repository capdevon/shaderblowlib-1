package com.jme3.shaderblow.test.electricity;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 * @author cvlad
 */
public class TestElectricity5 extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestElectricity5 app = new TestElectricity5();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        // this.assetManager.registerLocator("assets", FileLocator.class);
        Node model = (Node) assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        Material litMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        model.setMaterial(litMat);

        Electricity5Material mat = new Electricity5Material(assetManager, "MatDefs/Electricity/Electricity5.j3md");
        mat.setLayers(10);
        mat.setWidth(0.2f);
        mat.setFloat("speed", 0.1f);
        mat.setFloat("thickness", 0.14f);

        Texture noiseTex = assetManager.loadTexture("Textures/Electricity/noise.png");
        noiseTex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("noise", noiseTex);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
        mat.getAdditionalRenderState().setDepthWrite(false);
        mat.getAdditionalRenderState().setDepthTest(true);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        mat.setColor("color", ColorRGBA.Blue);

        for (Spatial child : model.getChildren()) {
            if (child instanceof Geometry) {
                Mesh mesh = ((Geometry) child).getMesh();
                Geometry geo = new Geometry("electrified_" + child.getName());
                geo.setQueueBucket(Bucket.Transparent);
                geo.setMesh(mesh);
                geo.setMaterial(mat);
                model.attachChild(geo);
            }
        }

        rootNode.attachChild(model);
        model.setLocalTranslation(new Vector3f(0, -1.5f, 5));

        addLighting();
        flyCam.setMoveSpeed(20);
    }

    private void addLighting() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(new ColorRGBA(0.6f, 0.6f, 0.6f, 0.6f));
        sun.setDirection(new Vector3f(-1, -1, -1));
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.6f, 0.6f, 0.6f, 0.6f));
        rootNode.addLight(ambient);
    }

    @Override
    public void simpleUpdate(float tpf) {
        // TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // TODO: add render code
    }
}
