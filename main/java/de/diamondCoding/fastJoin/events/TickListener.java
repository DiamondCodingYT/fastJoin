package de.diamondCoding.fastJoin.events;

import de.diamondCoding.fastJoin.FastJoin;
import de.diamondCoding.fastJoin.FastJoinScreen;
import net.labymod.gui.GuiTagsAdd;
import net.labymod.gui.ModGuiScreenServerList;
import net.labymod.labyconnect.gui.GuiFriendsAddFriend;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.mojang.GuiContainerCreativeCustom;
import net.labymod.settings.LabyModAddonsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class TickListener {

    private FastJoin addon;

    public TickListener(FastJoin addon) {
        this.addon = addon;
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (addon.enabled) {
            if (Keyboard.isKeyDown(addon.fastJoinKey)) {
                if (Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()
                        || Minecraft.getMinecraft().currentScreen instanceof FastJoinScreen
                        || Minecraft.getMinecraft().currentScreen instanceof LabyModAddonsGui
                        || Minecraft.getMinecraft().currentScreen instanceof GuiEditSign
                        || Minecraft.getMinecraft().currentScreen instanceof GuiScreenBook
                        || Minecraft.getMinecraft().currentScreen instanceof GuiCreateWorld
                        || Minecraft.getMinecraft().currentScreen instanceof GuiCommandBlock
                        || Minecraft.getMinecraft().currentScreen instanceof GuiRenameWorld
                        || Minecraft.getMinecraft().currentScreen instanceof GuiScreenAddServer
                        || Minecraft.getMinecraft().currentScreen instanceof GuiFriendsLayout
                        || Minecraft.getMinecraft().currentScreen instanceof GuiFriendsAddFriend
                        || Minecraft.getMinecraft().currentScreen instanceof GuiTagsAdd
                        || Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreativeCustom
                        || Minecraft.getMinecraft().currentScreen instanceof ModGuiScreenServerList) {
                    return;
                }
                System.out.println(Minecraft.getMinecraft().currentScreen);
                Minecraft.getMinecraft().displayGuiScreen(new FastJoinScreen(addon, Minecraft.getMinecraft().currentScreen, addon.lastJoin));
            }
        }
    }

}
