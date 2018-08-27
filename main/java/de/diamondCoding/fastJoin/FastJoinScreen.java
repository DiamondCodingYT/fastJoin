package de.diamondCoding.fastJoin;

import java.awt.Color;

import de.diamondCoding.fastJoin.managers.RecentManager;
import de.diamondCoding.fastJoin.managers.ServerManager;
import de.diamondCoding.fastJoin.util.BackgroundType;
import net.labymod.core.LabyModCore;
import net.labymod.core.ServerPingerData;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.manager.ServerInfoRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class FastJoinScreen extends GuiScreen {

    private FastJoin addon;

    private GuiTextField ip;
    private GuiScreen oldScreen;
    private boolean showResents;

    private int animationTime = 10;
    private int ani = 0;

    private ServerInfoRenderer serverInfoRenderer;
    private long updateCooldown = 2000L;
    private long lastUpdate = 0L;

    public FastJoinScreen(FastJoin addon, GuiScreen old, boolean lastJoin) {
        this.addon = addon;
        oldScreen = old;
        showResents = lastJoin;
        serverInfoRenderer = new ServerInfoRenderer("empty", new ServerPingerData("empty", 3000L));
    }

    private List<GuiButton> resents;

    public void initGui() {

        Keyboard.enableRepeatEvents(true);

        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96 + 12 - 48, "Join"));
        this.buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96 + 36 - 48, "Back"));

        ip = new GuiTextField(3, fontRendererObj, width / 2 - 100, height / 4 + 96 + 12 - 36 - 48, 200, 20);


        ip.setMaxStringLength(0);
        ip.setFocused(true);

        //recent Buttons
        if(showResents) {

            int top = (height / 4 + 96 + 12 - 36 - 48 - 12 - 20);
            int bottom = (height / 4 + 96 + 36 - 48 + 20 + 20);
            int middel = (bottom - top) / 2 + top;
            int pos = middel - (5 * 24) / 2 + 2;

            for (int i = 10; i < 20; i++) {
                GuiButton btn;
                int x = 8;
                int num = i - 10;
                if(i >= 15) {
                    x = width - 8 - 90;
                    num = i - 15;
                }
                if (RecentManager.getResent(i - 10) != null) {
                    btn = new GuiButton(i, x, pos + (num * 24), 90, 20, "" + RecentManager.getResent(i - 10).ip);
                } else {
                    btn = new GuiButton(i, x, pos + (num * 24), 90, 20, "Server not set");
                }
                this.buttonList.add(btn);
            }
        }

    }


    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        ip.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ip.mouseClicked(mouseX, mouseY, mouseButton);
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String joinIp = "";
    private String shortcut = "";

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        ip.setMaxStringLength(50);
        ip.textboxKeyTyped(typedChar, keyCode);

        if(typedChar == '\r') {
            actionPerformed(buttonList.get(0));
        }
        if(keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(oldScreen);
        }

        if(ani >= animationTime) {
            joinIp = ip.getText();
            shortcut = ServerManager.getShortcut(joinIp);
            joinIp = ServerManager.getFullIp(joinIp);
        } else {
            joinIp = "";
        }

        this.lastUpdate = System.currentTimeMillis();
        LabyModCore.getServerPinger().pingServer(null, this.lastUpdate, joinIp.equalsIgnoreCase("") ? "empty" : joinIp, new Consumer<ServerPingerData>() {
            public void accept(ServerPingerData accepted) {
                if (accepted == null || accepted.getTimePinged() == lastUpdate) {
                    serverInfoRenderer = new ServerInfoRenderer(joinIp.equalsIgnoreCase("") ? "empty" : joinIp, accepted);
                }
            }
        });

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if(this.mc.theWorld == null) {
            if(addon.backgroundType.equals(BackgroundType.COLOR)) {
                drawRect(0, 0, Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().displayWidth, addon.color.getRGB());
            } else {
                drawDefaultBackground();
            }
        } else {
            drawDefaultBackground();
        }

        if(ani < animationTime) {
            ani++;
        }

        Color a = new Color(5, 5, 5, 100);
        int left = width / 2 - 100 - 20;
        int top = height / 4 + 96 + 12 - 36 - 48 - 12 - 20;
        int right = width / 2 + 100 + 20;
        int bottom = height / 4 + 96 + 36 - 48 + 20 + 20;
        float boxHeight = bottom - top;
        float boxWidth = right - left;
        drawRect(left, top, (int) (left + (boxWidth / animationTime) * ani), (int) (top + (boxHeight / animationTime) * ani), a.getRGB());

        if(showResents) {
            left = 0;
            right = 90 + 2 * 8;
            boxWidth = right - left;
            drawRect(left, top, (int) (left + (boxWidth / animationTime) * ani), (int) (top + (boxHeight / animationTime) * ani), a.getRGB());
            left = width - 90 - 2 * 8;
            right = width;
            boxWidth = right - left;
            drawRect(left, top, (int) (left + (boxWidth / animationTime) * ani), (int) (top + (boxHeight / animationTime) * ani), a.getRGB());
        }

        if(ani >= animationTime) {
            drawString(fontRendererObj, "§8IP: §3" + joinIp, width / 2 - 100, height / 4 + 96 + 12 - 36 - 48 - 12, 0xffffffff);
            if (!shortcut.equals("")) {
                drawString(fontRendererObj, "§4Shortcut: " + shortcut, width / 2 - 100 + 100, height / 4 + 96 + 12 - 36 - 48 - 12, 0xffffffff);
            }
        }

        if(ani >= animationTime) {
            //infoRenderer.drawEntry(width / 2 - 100, height / 4 + 96 + 36 - 48 + 24, 90, mouseX, mouseY);

            DrawUtils drawUtils = LabyMod.getInstance().getDrawUtils();
            int leftBound = this.width / 2 - 150;
            int rightBound = this.width / 2 + 150;
            int posY = 20;
            int height = 30;
            drawUtils.drawRectangle(leftBound, posY - 4, rightBound, posY + 6 + height, -2147483648);
            this.serverInfoRenderer.drawEntry(leftBound + 3, posY, rightBound - leftBound, mouseX, mouseY);

        }

        if(ani >= animationTime) {
            ip.drawTextBox();
            ip.setFocused(true);
            for(GuiButton btn : buttonList) {
                btn.visible = true;
            }
        } else {
            ip.setFocused(false);
            ip.setText("");
            joinIp = "";
            for(GuiButton btn : buttonList) {
                btn.visible = false;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 1) {
            LabyMod.getInstance().connectToServer(joinIp);
        }
        if(button.id == 2) {
            mc.displayGuiScreen(oldScreen);
        }
        if(button.id > 9 && button.id < 20) {
            if(RecentManager.getResent(button.id - 10) != null) {
                if(button.displayString.startsWith("§4")) {
                    LabyMod.getInstance().connectToServer(RecentManager.getResent(button.id - 10).ip);
                } else {
                    for(GuiButton btn : buttonList) {
                        if(btn.displayString.startsWith("§4")) {
                            btn.displayString = btn.displayString.replaceFirst("§4", "");
                        }
                    }
                    button.displayString = "§4" + button.displayString;
                }
            }
        }
    }

}
