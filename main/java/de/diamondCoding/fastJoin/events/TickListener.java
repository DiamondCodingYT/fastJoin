package de.diamondCoding.fastJoin.events;

import de.diamondCoding.fastJoin.FastJoin;
import de.diamondCoding.fastJoin.FastJoinScreen;
import net.labymod.settings.LabyModAddonsGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class TickListener {

    FastJoin addon;

    public TickListener(FastJoin addon) {
        this.addon = addon;
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if(addon.enabeld) {
            if (Keyboard.isKeyDown(addon.fastJoinKey)) {
                if (Minecraft.getMinecraft().currentScreen instanceof FastJoinScreen) {
                    return;
                }
                if (Minecraft.getMinecraft().currentScreen instanceof LabyModAddonsGui) {
                    return;
                }
                if(Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) {
                    return;
                }
                Minecraft.getMinecraft().displayGuiScreen(new FastJoinScreen(Minecraft.getMinecraft().currentScreen, addon.lastJoin));
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                if(Minecraft.getMinecraft().currentScreen instanceof FastJoinScreen) {
                    Minecraft.getMinecraft().displayGuiScreen(FastJoinScreen.oldScreen);
                }
            }
        }
    }

}
