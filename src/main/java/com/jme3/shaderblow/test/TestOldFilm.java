/*
 * Copyright (c) 2009-2012 ShaderBlow
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'ShaderBlow' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.shaderblow.test;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.shaderblow.filter.OldFilmFilter;
import com.jme3.shaderblow.test.SceneHelper;

/**
 *
 * @author ShaderBlow
 */
public class TestOldFilm extends SimpleApplication {

    private OldFilmFilter oldFilmFilter;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestOldFilm app = new TestOldFilm();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(10);

        SceneHelper.buildTestModel(assetManager, rootNode);
        SceneHelper.buildSkybox(assetManager, rootNode);
        SceneHelper.buildFloor(assetManager, rootNode);
        SceneHelper.buildLights(rootNode);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        // fpp.setNumSamples(4);
        float colorDensity = 0.7f;
        float noiseDensity = 0.4f;
        float scratchDensity = 0.3f;
        float vignettingValue = 0.9f;
        ColorRGBA filterColor = new ColorRGBA(112f / 255f, 66f / 255f, 20f / 255f, 1.0f);
        oldFilmFilter = new OldFilmFilter(filterColor, colorDensity, noiseDensity, scratchDensity, vignettingValue);
        fpp.addFilter(oldFilmFilter);
        viewPort.addProcessor(fpp);

        initInputs();

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize());
        String cmd = "space: Filter ON/OFF"
                + "\nY/H: Color Density UP/DOWN"
                + "\nU/J: Noise Density UP/DOWN"
                + "\nI/K: Scratching Density UP/DOWN"
                + "\nO/L: Vignetting Diameter UP/DOWN";
        ch.setText(cmd);
        ch.setColor(new ColorRGBA(1f, 0.8f, 0.1f, 1f));
        ch.setLocalTranslation(settings.getWidth() * 0.01f, settings.getHeight() * 0.99f, 0);
        guiNode.attachChild(ch);
    }

    private void initInputs() {
        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("ColorDensityUp", new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addMapping("ColorDensityDown", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("NoiseDensityUp", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("NoiseDensityDown", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("ScratchDensityUp", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("ScratchDensityDown", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("VignettingValueUp", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("VignettingValueDown", new KeyTrigger(KeyInput.KEY_L));

        inputManager.addListener(acl, "toggle");
        inputManager.addListener(anl, "ColorDensityUp", "ColorDensityDown", "NoiseDensityUp", "NoiseDensityDown",
                "ScratchDensityUp", "ScratchDensityDown", "VignettingValueUp", "VignettingValueDown");
    }

    final ActionListener acl = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("toggle") && keyPressed) {
                boolean enabled = oldFilmFilter.isEnabled();
                oldFilmFilter.setEnabled(!enabled);
            }
        }
    };

    final AnalogListener anl = new AnalogListener() {
        @Override
        public void onAnalog(String name, float isPressed, float tpf) {
            if (name.equals("ColorDensityUp")) {
                oldFilmFilter.setColorDensity(oldFilmFilter.getColorDensity() + 0.01f);
                System.out.println("OldFilm color density : " + oldFilmFilter.getColorDensity());
            }
            if (name.equals("ColorDensityDown")) {
                oldFilmFilter.setColorDensity(oldFilmFilter.getColorDensity() - 0.01f);
                System.out.println("OldFilm color density : " + oldFilmFilter.getColorDensity());
            }
            if (name.equals("NoiseDensityUp")) {
                oldFilmFilter.setNoiseDensity(oldFilmFilter.getNoiseDensity() + 0.01f);
                System.out.println("OldFilm noise density : " + oldFilmFilter.getNoiseDensity());
            }
            if (name.equals("NoiseDensityDown")) {
                oldFilmFilter.setNoiseDensity(oldFilmFilter.getNoiseDensity() - 0.01f);
                System.out.println("OldFilm noise density : " + oldFilmFilter.getNoiseDensity());
            }
            if (name.equals("ScratchDensityUp")) {
                oldFilmFilter.setScratchDensity(oldFilmFilter.getScratchDensity() + 0.01f);
                System.out.println("OldFilm scratch density : " + oldFilmFilter.getScratchDensity());
            }
            if (name.equals("ScratchDensityDown")) {
                oldFilmFilter.setScratchDensity(oldFilmFilter.getScratchDensity() - 0.01f);
                System.out.println("OldFilm scratch density : " + oldFilmFilter.getScratchDensity());
            }
            if (name.equals("VignettingValueUp")) {
                oldFilmFilter.setVignettingValue(oldFilmFilter.getVignettingValue() + 0.01f);
                System.out.println("OldFilm vignetting diameter : " + oldFilmFilter.getVignettingValue());
            }
            if (name.equals("VignettingValueDown")) {
                oldFilmFilter.setVignettingValue(oldFilmFilter.getVignettingValue() - 0.01f);
                System.out.println("OldFilm vignetting diameter : " + oldFilmFilter.getVignettingValue());
            }
        }
    };
}
