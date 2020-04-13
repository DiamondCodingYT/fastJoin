package de.diamondCoding.fastJoin;

import de.diamondCoding.fastJoin.managers.ServerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShortcutsScreen extends GuiScreen {

    private FastJoin addon;
    private GuiTextField addField;
    private GuiScreen oldScreen;
    private int delay = 3;
    String msg = "";

    public ShortcutsScreen(FastJoin addon, GuiScreen old) {
        this.addon = addon;
        oldScreen = old;
    }

    ScaledResolution sr = null;
    public void initGui() {

        Keyboard.enableRepeatEvents(true);

        sr = new ScaledResolution(Minecraft.getMinecraft());

        addField = new GuiTextField(3, fontRendererObj, sr.getScaledWidth() / 40 * 3, 20, sr.getScaledWidth() / 10 * 5, 20);
        addField.setMaxStringLength(0);
        addField.setFocused(true);

        this.buttonList.add(new GuiButton(1, (sr.getScaledWidth() / 10) * 6 + (sr.getScaledWidth() / 40)*0, 20, sr.getScaledWidth() / 10 * 1, 20, "Add"));
        this.buttonList.add(new GuiButton(2, (sr.getScaledWidth() / 10) * 7 + (sr.getScaledWidth() / 40)*1, 20, sr.getScaledWidth() / 10 * 1, 20, "Clear"));
        this.buttonList.add(new GuiButton(3, (sr.getScaledWidth() / 10) * 8 + (sr.getScaledWidth() / 40)*2, 20, sr.getScaledWidth() / 10 * 1, 20, "Back"));

        updateMsg();
        updateRemoveButtons();

    }


    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        addField.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        addField.mouseClicked(mouseX, mouseY, mouseButton);
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        addField.setMaxStringLength(50);
        addField.textboxKeyTyped(typedChar, keyCode);

        if(keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(oldScreen);
        }
        if(typedChar == '\r') {
            actionPerformed(buttonList.get(0));
        }

        updateMsg();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        if(delay > 0) {
            delay--;
            addField.setMaxStringLength(0);
            addField.setText("");
            for(GuiButton btn : buttonList) {
                btn.visible = false;
            }
        } else {
            for(GuiButton btn : buttonList) {
                btn.visible = true;
            }
            addField.drawTextBox();
            addField.setFocused(true);
        }

        if(sr != null) {
            drawString(fontRendererObj, msg, sr.getScaledWidth() / 40 * 3, 50, 0xffffffff);
        }

        try {
            int index = 0;
            for (String s : addon.personalShortcuts.split(";")) {
                if (s.equalsIgnoreCase(" ") || s.equalsIgnoreCase("")) {
                    continue;
                }
                String shortcut = s.split(" ")[0];
                String ip = s.split(" ")[1];
                drawString(fontRendererObj, "§8Shortcut: §3" + shortcut + " §8IP: §3" + ip, sr.getScaledWidth() / 40 * 3, 70 + (20) * index, 0xffffffff);
                index++;
            }
        } catch(Exception exception) { //just to be sure if, anything is woring here lets just panic!
            addon.personalShortcuts = "";
            addon.getConfig().addProperty("personalShortcuts", addon.personalShortcuts);
            addon.saveConfig();
            ServerManager.fillServers();
            Minecraft.getMinecraft().displayGuiScreen(oldScreen);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateMsg() {
        if(addField.getText().split(" ").length >= 2) {
            String shortcut = addField.getText().split(" ")[0];
            String ip = addField.getText().split(" ")[1];
            msg = "§8Shortcut: §3" + shortcut + " §8IP: §3" + ip + " §aRichtig? dann 'Add'";
        } else {
            msg = "§4Format: SHORTCUT IP";
        }
    }

    public void updateRemoveButtons() {
        List<GuiButton> rmv = new ArrayList<GuiButton>();
        for(GuiButton btn : buttonList) {
            if(btn.id >= 10) {
                rmv.add(btn);
            }
        }
        for(GuiButton btn : rmv) {
            buttonList.remove(btn);
        }
        int index = 0;
        for(String s : addon.personalShortcuts.split(";")) {
            if(s.equalsIgnoreCase(" ") || s.equalsIgnoreCase("")) {
                continue;
            }
            this.buttonList.add(new GuiButton(10 + index, (sr.getScaledWidth() / 10) * 8 + (sr.getScaledWidth() / 40) * 2, 70 + (20) * index - 7, sr.getScaledWidth() / 10 * 1, 20, "Remove"));
            index++;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 1) {
            addField.setText(addField.getText().replaceAll(";", "")); //just in case
            if(addField.getText().split(" ").length >= 2) {
                String shortcut = addField.getText().split(" ")[0];
                String ip = addField.getText().split(" ")[1];
                if(ip.length() > 0 && shortcut.length() > 0) {
                    addon.personalShortcuts = addon.personalShortcuts + shortcut + " " + ip + ";";
                    addon.getConfig().addProperty("personalShortcuts", addon.personalShortcuts);
                    addon.saveConfig();
                    ServerManager.fillServers();
                    addField.setText("");
                    updateRemoveButtons();
                    updateMsg();
                }
            }
        }
        if(button.id == 2) {
            addField.setText("");
            updateMsg();
        }
        if(button.id == 3) {
            Minecraft.getMinecraft().displayGuiScreen(oldScreen);
        }
        if(button.id >= 10) {
            int lineIndex = button.id - 10;
            int index = 0;
            String newPersonalShortcuts = "";
            for(String s : addon.personalShortcuts.split(";")) {
                if(!(index == lineIndex)) {
                    newPersonalShortcuts += s + ";";
                }
                index++;
            }
            addon.personalShortcuts = newPersonalShortcuts;
            addon.getConfig().addProperty("personalShortcuts", addon.personalShortcuts);
            addon.saveConfig();
            ServerManager.fillServers();
            updateRemoveButtons();
            updateMsg();
        }

    }

}
