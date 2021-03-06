package de.diamondCoding.fastJoin;

import de.diamondCoding.fastJoin.events.ServerJoinListener;
import de.diamondCoding.fastJoin.events.TickListener;
import de.diamondCoding.fastJoin.managers.RecentManager;
import de.diamondCoding.fastJoin.managers.ServerManager;
import de.diamondCoding.fastJoin.util.RecentServer;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import org.lwjgl.input.Keyboard;
import java.util.List;

public class FastJoin extends LabyModAddon {

    public int version = 9;
    public boolean enabled = true;
    public boolean lastJoin = true;
    public int fastJoinKey = Keyboard.KEY_J;
    public String personalShortcuts = "";

    @Override
    public void onEnable() {

        System.out.println("Enabling FastJoin");

        registerEvents();

        ServerManager.init(this);
        RecentManager.init(this);

        System.out.println("Enabled FastJoin");

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void loadConfig() {
        if(!this.getConfig().has("updatedToNewShortcutFormat")) {
            this.getConfig().addProperty("personalShortcuts", "");
            this.getConfig().addProperty("updatedToNewShortcutFormat", true);
            this.saveConfig();
        }
        this.enabled = !this.getConfig().has("enabled") || this.getConfig().get("enabled").getAsBoolean();
        this.lastJoin = !this.getConfig().has("lastJoin") || this.getConfig().get("lastJoin").getAsBoolean();
        this.fastJoinKey = this.getConfig().has("fastJoin") ? this.getConfig().get("fastJoin").getAsInt() : fastJoinKey;
        this.personalShortcuts = this.getConfig().has("personalShortcuts") ? this.getConfig().get("personalShortcuts").getAsString() : personalShortcuts;
        //here comes the fix for a stupid mistake:
        ServerManager.fillServers();
        FastJoinScreen.animationTime = this.getConfig().has("aniTime") ? this.getConfig().get("aniTime").getAsInt() : FastJoinScreen.animationTime;
        for(int i = 0; i < 10; i++) {
            if(this.getConfig().has("recent" + i)) {
                RecentManager.recentServers.add(new RecentServer(i, this.getConfig().get("recent" + i).getAsString()));
            }
        }
    }

    @Override
    protected void fillSettings(final List<SettingsElement> settings) {

        final BooleanElement enabledElement = new BooleanElement( "Enabled" , new ControlElement.IconData( Material.LEVER ), new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted ) {
                FastJoin.this.enabled = accepted;

                FastJoin.this.getConfig().addProperty("enabled", enabled);
                FastJoin.this.saveConfig();
            }
        } , enabled);
        settings.add(enabledElement);

        final BooleanElement lastJoinElement = new BooleanElement( "Show recent servers" , new ControlElement.IconData(Material.REDSTONE_COMPARATOR), new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted ) {
                FastJoin.this.lastJoin = accepted;

                FastJoin.this.getConfig().addProperty("lastJoin", lastJoin);
                FastJoin.this.saveConfig();
            }
        } , lastJoin);
        settings.add(lastJoinElement);

//        final SliderElement scalingSliderElement = new SliderElement( "Animation Time", new ControlElement.IconData( Material.WATCH ), FastJoinScreen.animationTime );
//        scalingSliderElement.setRange( 1, 30 );
//        scalingSliderElement.setSteps( 1 );
//        scalingSliderElement.addCallback( new Consumer<Integer>() {
//            @Override
//            public void accept( Integer accepted ) {
//                FastJoinScreen.animationTime = accepted;
//
//                FastJoin.this.getConfig().addProperty("aniTime", accepted);
//                FastJoin.this.saveConfig();
//            }
//        } );
//        settings.add( scalingSliderElement );

        final NumberElement numberElement = new NumberElement( "Animation Frames", new ControlElement.IconData( Material.WATCH ), FastJoinScreen.animationTime );
        numberElement.setSteps( 1 );
        numberElement.setMinValue(1);
        numberElement.setMaxValue(99999);
        numberElement.addCallback( new Consumer<Integer>() {
            @Override
            public void accept( Integer accepted ) {
                FastJoinScreen.animationTime = accepted;

                FastJoin.this.getConfig().addProperty("aniTime", accepted);
                FastJoin.this.saveConfig();
            }
        } );
        settings.add(numberElement);

        final KeyElement keyElement = new KeyElement( "Open FastJoin",
                new ControlElement.IconData(Material.EYE_OF_ENDER),
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

    }

    private void registerEvents() {
        this.getApi().registerForgeListener(new TickListener(this));
        this.getApi().getEventManager().registerOnJoin(new ServerJoinListener(this));
    }

}
