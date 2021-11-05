package com.jme3.shaderblow.forceshield;

import java.io.IOException;
import java.util.ArrayList;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;

/**
 *
 * @author shaderblow
 */
public class ForceShieldControl implements Control {

    private final Material material;
    private float maxTime;
    private final ArrayList<Vector3f> collisions;
    private final ArrayList<Float> collisionTimes;
    private Spatial model;
    private boolean numChanged;
    private boolean enabled;
    private boolean work;
    private float timer;
    private float timerSize;
    private final int MAX_HITS;

    public ForceShieldControl(Material material) {
        this.material = material;
        numChanged = false;
        enabled = true;
        work = false;
        maxTime = 0.4f;
        timerSize = maxTime * 3f;
        collisionTimes = new ArrayList<>();
        collisions = new ArrayList<>();
        MAX_HITS = 4;
        timer = 0;
    }

    public void registerHit(Vector3f position) {
        if (!enabled) {
            return;
        }

        timer = 0f;
        material.setBoolean("Work", true);
        work = true;

        Vector3f lposition = new Vector3f();
        model.worldToLocal(position.clone(), lposition);
        collisions.add(new Vector3f(lposition.x, lposition.y, lposition.z));
        collisionTimes.add(maxTime);
        numChanged = true;
        updateCollisionPoints();
    }

    public void setColor(ColorRGBA color) {
        material.setColor("Color", color);
    }

    public void setVisibility(float percent) {
        material.setFloat("MinAlpha", percent);
    }

    public void setTexture(Texture texture) {
        material.setTexture("ColorMap", texture);
    }

    public Material getMaterial() {
        return material;
    }

    public void setEffectSize(float size) {
        material.setFloat("MaxDistance", size / model.getLocalScale().x);
    }

    protected void updateCollisionAlpha() {
        float[] alphas = new float[Math.min(collisionTimes.size(), MAX_HITS)];
        for (int i = 0; i < alphas.length && i < MAX_HITS; i++) {
            alphas[i] = collisionTimes.get(collisions.size() - 1 - i) / maxTime;
        }
        material.setParam("CollisionAlphas", VarType.FloatArray, alphas);
    }

    protected void updateCollisionPoints() {
        Vector3f[] collisionsArray = new Vector3f[Math.min(collisions.size(), MAX_HITS)];
        for (int i = 0; i < collisions.size() && i < MAX_HITS; i++) {
            collisionsArray[i] = collisions.get(collisions.size() - 1 - i);
        }
        material.setParam("Collisions", VarType.Vector3Array, collisionsArray);
        //material.setInt("CollisionNum", Math.min(collisions.size(), MAX_HITS));
    }

    public float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
        timerSize = maxTime * 3f;
    }

    @Override
    public void setSpatial(Spatial model) {
        this.model = model;
        model.setMaterial(material);
        model.setQueueBucket(Bucket.Transparent);
    }

    @Override
    public void update(float tpf) {
        if (work && enabled) {

            if (timer > timerSize) {
                collisions.clear();
                collisionTimes.clear();
                numChanged = false;
                material.setBoolean("Work", false);
                timer = 0f;
                work = false;
                return;
            }

            for (int i = 0; i < collisionTimes.size(); i++) {
                float time = collisionTimes.get(i);
                time -= tpf;
                if (time <= 0) {
                    collisionTimes.remove(i);
                    collisions.remove(i);
                    numChanged = true;
                    i--;
                    continue;
                }
                collisionTimes.set(i, time);
            }
            if (numChanged) {
                updateCollisionPoints();
            }

            timer += tpf * 3f;
            updateCollisionAlpha();

            numChanged = false;
        }
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        // TODO Auto-generated method stub
    }

    @Override
    public void read(JmeImporter arg0) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public void write(JmeExporter arg0) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
