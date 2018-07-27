package de.diamondCoding.fastJoin;

import com.sun.prism.paint.Color;
import de.diamondCoding.fastJoin.managers.RecentManager;
import de.diamondCoding.fastJoin.managers.ServerManager;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class FastJoinScreen extends GuiScreen {

    public GuiTextField ip;
    public static GuiScreen oldScreen;
    boolean showResents;
    int recentWith = 0;

    public FastJoinScreen(GuiScreen old, boolean lastJoin) {
        oldScreen = old;
        showResents = lastJoin;
    }

    private List<GuiButton> resents;

    public void initGui() {

        if(showResents) {
            recentWith = width / 4;
            if (recentWith < 80) {
                recentWith = 80;
            }
        }

        Keyboard.enableRepeatEvents(true);

        this.buttonList.add(new GuiButton(1, width / 2 - 100 + (recentWith / 2), height / 4 + 96 + 12 - 48, "Join"));
        this.buttonList.add(new GuiButton(2, width / 2 - 100 + (recentWith / 2), height / 4 + 96 + 36 - 48, "Back"));

        ip = new GuiTextField(3, fontRendererObj, width / 2 - 100 + (recentWith / 2), height / 4 + 96 + 12 - 36 - 48, 200, 20);


        ip.setMaxStringLength(0);
        ip.setFocused(true);

        //recent Buttons
        if(showResents) {
            for (int i = 10; i < 20; i++) {
                GuiButton btn = null;
                if (RecentManager.getResent(i - 10) != null) {
                    btn = new GuiButton(i, 8, 8 + ((i - 10) * 24), recentWith, 20, "" + RecentManager.getResent(i - 10).ip);
                } else {
                    btn = new GuiButton(i, 8, 8 + ((i - 10) * 24), recentWith, 20, "Server not set");
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

    String joinIp = "";
    String shortcut = "";

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        ip.setMaxStringLength(50);
        ip.textboxKeyTyped(typedChar, keyCode);

        if(typedChar == '\r') {
            actionPerformed(buttonList.get(0));
        }

        joinIp = ip.getText();
        shortcut = ServerManager.getShortcut(joinIp);
        joinIp = ServerManager.getFullIp(joinIp);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        drawString(fontRendererObj, "§8IP: §3" + joinIp, width / 2 - 100 + (recentWith / 2), height / 4 + 96 + 12 - 36 - 48 - 12, 0xffffffff);
        if (!shortcut.equals("")) {
            drawString(fontRendererObj, "§4Shortcut: " + shortcut, width / 2 - 100 + 100 + (recentWith / 2), height / 4 + 96 + 12 - 36 - 48 - 12, 0xffffffff);
        }

        ip.drawTextBox();

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
