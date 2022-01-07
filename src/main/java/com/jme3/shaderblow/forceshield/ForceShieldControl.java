package com.jme3.shaderblow.forceshield;

import java.util.ArrayList;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;

/**
 *
 * @author shaderblow
 */
public class ForceShieldControl extends AbstractControl {

    private final Material material;
    private final ArrayList<Vector3f> collisions = new ArrayList<>();
    private final ArrayList<Float> collisionTimes = new ArrayList<>();
    private boolean numChanged;
    private boolean work;
    private float timer = 0;
    private float maxTime = 0.4f;
    private float timerSize = maxTime * 3f;
    private final int MAX_HITS = 4;

    /**
     * Create a ForceShieldControl instance.
     *
     * @param material
     */
    public ForceShieldControl(Material material) {
        this.material = material;
    }

    public void registerHit(Vector3f position) {
        if (isEnabled()) {
            timer = 0f;
            material.setBoolean("Work", true);
            work = true;

            Vector3f localPos = new Vector3f();
            spatial.worldToLocal(position, localPos);
            collisions.add(new Vector3f(localPos.x, localPos.y, localPos.z));
            collisionTimes.add(maxTime);
            numChanged = true;
            updateCollisionPoints();
        }
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

    public void setEffectSize(float size) {
        material.setFloat("MaxDistance", size / spatial.getLocalScale().x);
    }

    public float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
        timerSize = maxTime * 3f;
    }

    @Override
    public void controlUpdate(float tpf) {
        if (work) {

            if (timer > timerSize) {
                collisions.clear();
                collisionTimes.clear();
                numChanged = false;
                material.setBoolean("Work", false);
                timer = 0f;
                work = false;
                
            } else {
                updateCollision(tpf);
            }
        }
    }

    @Override
    public void controlRender(RenderManager rm, ViewPort vp) {
        // TODO Auto-generated method stub
    }
    
    protected void updateCollision(float tpf) {
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

}
