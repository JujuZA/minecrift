/**
 * Copyright 2013 Mark Browning, StellaArtois
 * Licensed under the LGPL 3.0 or later (See LICENSE.md for details)
 */
package com.mtbs3d.minecrift.gui;

import com.mtbs3d.minecrift.provider.MCOculus;
import com.mtbs3d.minecrift.api.IBasePlugin;
import com.mtbs3d.minecrift.api.PluginManager;
import com.mtbs3d.minecrift.settings.VRSettings;

import de.fruitfly.ovr.structs.HmdDesc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;

import java.util.List;

public class GuiRenderOpticsSettings  extends BaseGuiSettings implements GuiEventEx
{
    protected boolean reinit = false;
    private PluginModeChangeButton pluginModeChangeButton;

    static VRSettings.VrOptions[] defaultDisplayOptions = new VRSettings.VrOptions[] {
            //VRSettings.VrOptions.USE_ORTHO_GUI,
            VRSettings.VrOptions.MONO_FOV,
            VRSettings.VrOptions.DUMMY,
            VRSettings.VrOptions.FSAA,
            VRSettings.VrOptions.FSAA_SCALEFACTOR,
    };

    static VRSettings.VrOptions[] oculusDK2DisplayOptions = new VRSettings.VrOptions[]{
            VRSettings.VrOptions.HMD_NAME_PLACEHOLDER,
            VRSettings.VrOptions.ENABLE_DIRECT,
            VRSettings.VrOptions.RENDER_SCALEFACTOR,
            VRSettings.VrOptions.MIRROR_DISPLAY,
            VRSettings.VrOptions.FSAA,
            VRSettings.VrOptions.FSAA_SCALEFACTOR,
            VRSettings.VrOptions.CHROM_AB_CORRECTION,
            VRSettings.VrOptions.TIMEWARP,
            VRSettings.VrOptions.VIGNETTE,
            VRSettings.VrOptions.LOW_PERSISTENCE,
            VRSettings.VrOptions.DYNAMIC_PREDICTION,
            VRSettings.VrOptions.OVERDRIVE_DISPLAY,
            VRSettings.VrOptions.HIGH_QUALITY_DISTORTION,
            VRSettings.VrOptions.OTHER_RENDER_SETTINGS,
    };

    static VRSettings.VrOptions[] oculusDK1DisplayOptions = new VRSettings.VrOptions[] {
            VRSettings.VrOptions.HMD_NAME_PLACEHOLDER,
            VRSettings.VrOptions.ENABLE_DIRECT,
            VRSettings.VrOptions.RENDER_SCALEFACTOR,
            VRSettings.VrOptions.MIRROR_DISPLAY,
            VRSettings.VrOptions.FSAA,
            VRSettings.VrOptions.FSAA_SCALEFACTOR,
            VRSettings.VrOptions.CHROM_AB_CORRECTION,
            VRSettings.VrOptions.TIMEWARP,
            VRSettings.VrOptions.VIGNETTE,
            VRSettings.VrOptions.HIGH_QUALITY_DISTORTION,
            VRSettings.VrOptions.OTHER_RENDER_SETTINGS,
    };

    GameSettings settings;

    public GuiRenderOpticsSettings(GuiScreen par1GuiScreen, VRSettings par2vrSettings, GameSettings gameSettings)
    {
    	super( par1GuiScreen, par2vrSettings);
        screenTitle = "Stereo Renderer Settings";
        settings = gameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        String productName = "";

        // this.screenTitle = var1.translateKey("options.videoTitle");
        this.buttonList.clear();
        this.buttonList.add(new GuiButtonEx(ID_GENERIC_DONE, this.width / 2 - 100, this.height / 6 + 180, "Done"));
        this.buttonList.add(new GuiButtonEx(ID_GENERIC_DEFAULTS, this.width / 2 - 100, this.height / 6 + 160, "Reset To Defaults"));

        pluginModeChangeButton = new PluginModeChangeButton(ID_GENERIC_MODE_CHANGE, this.width / 2 - 78, this.height / 6 - 14, (List<IBasePlugin>)(List<?>) PluginManager.thePluginManager.stereoProviderPlugins, this.guivrSettings.stereoProviderPluginID);
        this.buttonList.add(pluginModeChangeButton);
        pluginModeChangeButton.enabled = true;

        VRSettings.VrOptions[] var10 = null;
        if( Minecraft.getMinecraft().stereoProvider instanceof MCOculus )
        {
            HmdDesc hmd = Minecraft.getMinecraft().hmdInfo.getHMDInfo();
            productName = hmd.ProductName;
            if (!hmd.IsReal)
                productName += " (Debug)";

            if (hmd.ProductName.contains("DK2"))      // Hacky. Improve.
                var10 = oculusDK2DisplayOptions;
            else
                var10 = oculusDK1DisplayOptions;
        }
        else
            var10 = defaultDisplayOptions;

        int var11 = var10.length;

        for (int var12 = 2; var12 < var11 + 2; ++var12)
        {
            VRSettings.VrOptions var8 = var10[var12 - 2];
            int width = this.width / 2 - 155 + var12 % 2 * 160;
            int height = this.height / 6 + 21 * (var12 / 2) - 10;

            if (var8 == VRSettings.VrOptions.DUMMY)
                continue;

            if (var8.getEnumFloat())
            {
                float minValue = 0.0f;
                float maxValue = 1.0f;
                float increment = 0.001f;

                if (var8 == VRSettings.VrOptions.RENDER_SCALEFACTOR)
                {
                    minValue = 0.5f;
                    maxValue = 2.5f;
                    increment = 0.1f;
                }
                else if (var8 == VRSettings.VrOptions.FSAA_SCALEFACTOR) // TODO: Only 2X is currently scaling GUI correctly (on AUTO gui size only)
                {
                    minValue = 1.1f;
                    maxValue = 2.5f;
                    increment = 0.1f;
                }
                else if (var8 == VRSettings.VrOptions.MONO_FOV)
                {
                    minValue = 30f;
                    maxValue = 110f;
                    increment = 1f;
                }

                GuiSliderEx slider = new GuiSliderEx(var8.returnEnumOrdinal(), width, height, var8, this.guivrSettings.getKeyBinding(var8), minValue, maxValue, increment, this.guivrSettings.getOptionFloatValue(var8));
                slider.setEventHandler(this);
                slider.enabled = getEnabledState(var8);
                this.buttonList.add(slider);
            }
            else
            {
                if (var8 == VRSettings.VrOptions.HMD_NAME_PLACEHOLDER)
                {
                    GuiSmallButtonEx button = new GuiSmallButtonEx(9999, width, height, var8, productName);
                    button.enabled = false;
                    this.buttonList.add(button);
                }
                else
                {
                    String keyBinding = this.guivrSettings.getKeyBinding(var8);
                    GuiSmallButtonEx button = new GuiSmallButtonEx(var8.returnEnumOrdinal(), width, height, var8, keyBinding);
                    button.enabled = getEnabledState(var8);
                    this.buttonList.add(button);
                }
            }
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        VRSettings.VrOptions num = VRSettings.VrOptions.getEnumOptions(par1GuiButton.id);
        Minecraft minecraft = Minecraft.getMinecraft();

        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == ID_GENERIC_DONE)
            {
                minecraft.vrSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
            else if (par1GuiButton.id == ID_GENERIC_DEFAULTS)
            {
                minecraft.vrSettings.useChromaticAbCorrection = true;

                minecraft.vrSettings.useTimewarp = true;
                minecraft.vrSettings.useVignette = true;
                minecraft.vrSettings.useLowPersistence = true;
                minecraft.vrSettings.useDynamicPrediction = true;
                minecraft.vrSettings.renderScaleFactor = 1.1f;
                minecraft.vrSettings.useDisplayMirroring = true;
                minecraft.vrSettings.useDisplayOverdrive = true;
                minecraft.vrSettings.useHighQualityDistortion = true;
                minecraft.vrSettings.useFsaa = false;
                minecraft.vrSettings.fsaaScaleFactor = 1.4f;

                minecraft.reinitFramebuffers = true;
			    this.guivrSettings.saveOptions();
            }
            else if (par1GuiButton.id == ID_GENERIC_MODE_CHANGE) // Mode Change
            {
                Minecraft.getMinecraft().vrSettings.stereoProviderPluginID = pluginModeChangeButton.getSelectedID();
                Minecraft.getMinecraft().vrSettings.saveOptions();
                Minecraft.getMinecraft().stereoProvider.resetRenderConfig();
                Minecraft.getMinecraft().stereoProvider = PluginManager.configureStereoProvider(Minecraft.getMinecraft().vrSettings.stereoProviderPluginID);
                minecraft.reinitFramebuffers = true;
                this.reinit = true;
            }
            else if (par1GuiButton.id == VRSettings.VrOptions.OTHER_RENDER_SETTINGS.returnEnumOrdinal())
            {
                Minecraft.getMinecraft().vrSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiOtherRenderOpticsSettings(this, this.guivrSettings));
            }
            else if (par1GuiButton instanceof GuiSmallButtonEx)
            {
                this.guivrSettings.setOptionValue(((GuiSmallButtonEx)par1GuiButton).returnVrEnumOptions(), 1);
                par1GuiButton.displayString = this.guivrSettings.getKeyBinding(VRSettings.VrOptions.getEnumOptions(par1GuiButton.id));
            }

            if (num == VRSettings.VrOptions.CHROM_AB_CORRECTION ||
                num == VRSettings.VrOptions.TIMEWARP ||
                num == VRSettings.VrOptions.VIGNETTE ||
                num == VRSettings.VrOptions.RENDER_SCALEFACTOR ||
                num == VRSettings.VrOptions.MIRROR_DISPLAY ||
                num == VRSettings.VrOptions.ENABLE_DIRECT ||
                num == VRSettings.VrOptions.LOW_PERSISTENCE ||
                num == VRSettings.VrOptions.DYNAMIC_PREDICTION ||
                num == VRSettings.VrOptions.OVERDRIVE_DISPLAY ||
                num == VRSettings.VrOptions.FSAA)
	        {
                minecraft.reinitFramebuffers = true;
	        }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        if (reinit)
        {
            initGui();
            reinit = false;
        }
        super.drawScreen(par1,par2,par3);
    }

    @Override
    public void event(int id, VRSettings.VrOptions enumm)
    {
        if (enumm == VRSettings.VrOptions.RENDER_SCALEFACTOR ||
            enumm == VRSettings.VrOptions.FSAA_SCALEFACTOR)
        {
            Minecraft.getMinecraft().reinitFramebuffers = true;
        }
    }

    @Override
    protected String[] getTooltipLines(String displayString, int buttonId)
    {
        VRSettings.VrOptions e = VRSettings.VrOptions.getEnumOptions(buttonId);
    	if( e != null )
    	switch(e)
    	{
        case FSAA:
            return new String[] {
                    "Full-Scene AntiAliasing (supersampling)",
                    "  Recommended: ON; greatly improves visual quality at",
                    "  the expense of greatly increased rendering cost.",
                    "  ON:  game is rendered at a higher resolution internally.",
                    "  OFF: game is rendered at the native resolution.",
                    "Will only be available if supported by your graphics",
                    "driver."};
        case FSAA_SCALEFACTOR:
            return new String[] {
                    "Full-Screen AntiAliasing Render Scale",
                    "  What multiple of native resolution should be rendered?",
                    "  Recommended value: 4X"};
    	case CHROM_AB_CORRECTION:
    		return new String[] {
    				"Chromatic aberration correction", 
    				"Corrects for color distortion due to lenses", 
    				"  OFF - no correction", 
    				"  ON - correction applied"} ;
        case TIMEWARP:
            return new String[] {
                    "Reduces perceived head track latency by sampling sensor",
                    "position just before the view is presented to your eyes,",
                    "and rotating the rendered view subtly to match the new",
                    "sensor orientation.",
                    "  ON  - Timewarp applied. Some ghosting may be observed",
                    "        during fast changes of head position.",
                    "  OFF - No timewarp applied, higher latency head",
                    "        tracking."
            };
            case RENDER_SCALEFACTOR:
                return new String[] {
                        "Determines quality of rendered image. Higher values",
                        "increase quality but increase rendering load.",
                        "Defaults to 1.1X."
                };
            case ENABLE_DIRECT:
                return new String[] {
                        "Direct rendering to HMD. Not currently working so",
                        "OFF currently."
                };
            case MIRROR_DISPLAY:
                return new String[] {
                        "Mirrors image on HMD to separate desktop window.",
                        "Only relevant if Direct mode is ON. Not currently",
                        " working so set to NO currently."
                };
            case DYNAMIC_PREDICTION:
                return new String[]{
                        "If supported by your HMD, reduces perceived head",
                        " track latency by continually monitoring to head",
                        "to screen latency, and adjusting the frame-timing",
                        "appropriately.",
                        "  ON  - Dynamic prediction applied. A small",
                        "        coloured square will be seen in the top",
                        "        right of any mirrored display",
                        "  OFF - Not applied."
                };
            case OVERDRIVE_DISPLAY:
                return new String[] {
                        "If supported by your HMD, attempts to reduce",
                        "perceived image smearing during black-to-",
                        "bright transitions.",
                        "  ON  - Smearing may be reduced.",
                        "  OFF - Not applied."
                };
            case LOW_PERSISTENCE:
                return new String[] {
                        "If supported by your HMD, displays each frame on the",
                        "the HMDs OLED screen for a very short period of time.",
                        "This greatly reduces perceived blurring during head",
                        "motion.",
                        "  ON  - Low persistence reduces image blur on",
                        "        head movement.",
                        "  OFF - Not applied."
                };
            case VIGNETTE:
                return new String[] {
                        "If enabled, blurs the edges of the distortion",
                        "displayed for each eye, making the edges less",
                        "noticeable at the edges of your field of view.",
                        "  ON  - FOV edges blurred.",
                        "  OFF - No burring of distortion edges. The edge",
                        "        of distortion may be more noticeable",
                        "        within your field of view."
                };
            case HIGH_QUALITY_DISTORTION:
                return new String[] {
                        "If enabled, and render scale is greater than one,",
                        "uses an improved downsampling algorithm to anti-",
                        "alias the image.",
                        "  ON  - Higher quality if render scale is increased",
                        "        above 1. May increase perceived latency",
                        "        slightly.",
                        "  OFF - Standard downsampling algorithm used. Faster."
                };
            case OTHER_RENDER_SETTINGS:
                return new String[] {
                        "Configure IPD and FOV border settings."
                };
    	default:
    		return null;
    	}
    	else
    	switch(buttonId)
    	{
	    	case ID_GENERIC_DEFAULTS:
	    		return new String[] {
	    			"Resets all values on this screen to their defaults"
	    		};
    		default:
    			return null;
    	}
    }

    private boolean getEnabledState(VRSettings.VrOptions var8)
    {
        String s = var8.getEnumString();

        if (var8 == VRSettings.VrOptions.ENABLE_DIRECT)
        {
            return false;
        }

        return true;
    }
}
