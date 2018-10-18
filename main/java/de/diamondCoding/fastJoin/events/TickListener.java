package de.diamondCoding.fastJoin.events;

import de.diamondCoding.fastJoin.FastJoin;
import de.diamondCoding.fastJoin.FastJoinScreen;
import net.labymod.gui.labymodchat.GuiChatLayout;
import net.labymod.settings.LabyModAddonsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

public class TickListener {

    private FastJoin addon;

    public TickListener(FastJoin addon) {
        this.addon = addon;
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {

        System.out.println(Minecraft.getMinecraft().currentScreen);
        if(addon.enabeld) {
            if (Keyboard.isKeyDown(addon.fastJoinKey)) {
                if (Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen() || Minecraft.getMinecraft().currentScreen instanceof FastJoinScreen || Minecraft.getMinecraft().currentScreen instanceof LabyModAddonsGui || Minecraft.getMinecraft().currentScreen instanceof GuiEditSign || Minecraft.getMinecraft().currentScreen instanceof GuiScreenBook || Minecraft.getMinecraft().currentScreen instanceof GuiCreateWorld || Minecraft.getMinecraft().currentScreen instanceof GuiCommandBlock || Minecraft.getMinecraft().currentScreen instanceof GuiRenameWorld || Minecraft.getMinecraft().currentScreen instanceof GuiScreenAddServer || Minecraft.getMinecraft().currentScreen instanceof GuiChatLayout) {
                    return;
                }
                Minecraft.getMinecraft().displayGuiScreen(new FastJoinScreen(addon, Minecraft.getMinecraft().currentScreen, addon.lastJoin));
            }
        }
    }

}
