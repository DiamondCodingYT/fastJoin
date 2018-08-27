package de.diamondCoding.fastJoin;

import de.diamondCoding.fastJoin.events.ServerJoinListener;
import de.diamondCoding.fastJoin.events.TickListener;
import de.diamondCoding.fastJoin.managers.RecentManager;
import de.diamondCoding.fastJoin.managers.ServerManager;
import de.diamondCoding.fastJoin.util.BackgroundType;
import de.diamondCoding.fastJoin.util.RecentServer;
import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.ColorPicker;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;

public class FastJoin extends LabyModAddon {

    public int version = 9;
    public boolean enabeld = true;
    public boolean lastJoin = true;
    public int fastJoinKey = Keyboard.KEY_J;
    public BackgroundType backgroundType = BackgroundType.COLOR;
    public Color color;

    @Override
    public void onEnable() {

        System.out.println("Enabeling FastJoin");

        registerEvents();

        ServerManager.init();
        RecentManager.init(this);

        System.out.println("Enabeld FastJoin");

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void loadConfig() {
        this.enabeld = !this.getConfig().has("enabeld") || this.getConfig().get("enabeld").getAsBoolean();
        this.lastJoin = !this.getConfig().has("lastJoin") || this.getConfig().get("lastJoin").getAsBoolean();
        this.fastJoinKey = this.getConfig().has("fastJoin") ? this.getConfig().get("fastJoin").getAsInt() : fastJoinKey;
        this.backgroundType = BackgroundType.valueOf(this.getConfig().has("backgroundType") ? this.getConfig().get("backgroundType").getAsString() : backgroundType.name());
        final int red = this.getConfig().has("red") ? this.getConfig().get("red").getAsInt() : 255;
        final int green = this.getConfig().has("green") ? this.getConfig().get("green").getAsInt() : 255;
        final int blue = this.getConfig().has("blue") ? this.getConfig().get("blue").getAsInt() : 255;
        this.color = new Color(red, green, blue, 255);
        for(int i = 0; i < 10; i++) {
            if(this.getConfig().has("recent" + i)) {
                RecentManager.recentServers.add(new RecentServer(i, this.getConfig().get("recent" + i).getAsString()));
            }
        }
    }

    @Override
    protected void fillSettings(final List<SettingsElement> settings) {

        final BooleanElement enabeldElement = new BooleanElement( "Enabled" , new ControlElement.IconData( Material.LEVER ), new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted ) {
                FastJoin.this.enabeld = accepted;

                FastJoin.this.getConfig().addProperty("enabeld", enabeld);
                FastJoin.this.saveConfig();
            }
        } , enabeld);
        settings.add(enabeldElement);

        final BooleanElement lastJoinElement = new BooleanElement( "Show resent servers" , new ControlElement.IconData("icons/recents.png"), new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted ) {
                FastJoin.this.lastJoin = accepted;

                FastJoin.this.getConfig().addProperty("lastJoin", lastJoin);
                FastJoin.this.saveConfig();
            }
        } , lastJoin);
        settings.add(lastJoinElement);

        final KeyElement keyElement = new KeyElement( "Open FastJoin" ,
                new ControlElement.IconData("icons/openFastJoin.png") ,
                fastJoinKey, new Consumer<Integer>() {
            @Override
            public void accept( Integer accepted ) {
                if ( accepted == -1 ) {
                    return;
                }

                fastJoinKey = accepted;
                FastJoin.this.getConfig().addProperty("fastJoin", accepted);
                FastJoin.this.saveConfig();
            }
        } );
        settings.add( keyElement );

        final DropDownMenu<BackgroundType> alignmentDropDownMenu = new DropDownMenu<BackgroundType>( "Background" , 0, 0, 0, 0 )
                .fill( BackgroundType.values() );
        final DropDownElement<BackgroundType> alignmentDropDown = new DropDownElement<BackgroundType>( "Background", alignmentDropDownMenu );

        // Set selected entry
        alignmentDropDownMenu.setSelected( backgroundType );

        // Listen on changes
        alignmentDropDown.setChangeListener( new Consumer<BackgroundType>() {
            @Override
            public void accept( BackgroundType alignment ) {
                backgroundType = alignment;
                FastJoin.this.getConfig().addProperty("backgroundType", backgroundType.name());
                FastJoin.this.saveConfig();
            }
        } );

        // Change entry design (optional)
        alignmentDropDownMenu.setEntryDrawer( new DropDownMenu.DropDownEntryDrawer() {
            @Override
            public void draw( Object object, int x, int y, String trimmedEntry ) {
                // We translate the value and draw it
                String entry = object.toString();
                LabyMod.getInstance().getDrawUtils().drawString( entry, x, y );
            }
        } );

        // Add to sublist
        settings.add( alignmentDropDown );

        final ColorPickerCheckBoxBulkElement bulkElement = new ColorPickerCheckBoxBulkElement("Color");
        final ColorPicker colorPicker = new ColorPicker("Color", this.color, new ColorPicker.DefaultColorCallback() {

            @Override
            public final Color getDefaultColor() {
                return FastJoin.this.color;
            }

        }, 0, 0, 0, 0);

        colorPicker.setUpdateListener(new Consumer<Color>() {

            @Override
            public void accept(final Color color) {
                FastJoin.this.color = color;

                FastJoin.this.getConfig().addProperty("red", color.getRed());
                FastJoin.this.getConfig().addProperty("green", color.getGreen());
                FastJoin.this.getConfig().addProperty("blue", color.getBlue());
                FastJoin.this.saveConfig();
            }

        });

        colorPicker.setHasAdvanced(true);
        bulkElement.addColorPicker(colorPicker);

        settings.add(bulkElement);

    }

    private void registerEvents() {
        this.getApi().registerForgeListener(new TickListener(this));
        this.getApi().getEventManager().registerOnJoin(new ServerJoinListener(this));
    }

}
