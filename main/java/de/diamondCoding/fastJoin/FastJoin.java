package de.diamondCoding.fastJoin;

import de.diamondCoding.fastJoin.events.ServerJoinListener;
import de.diamondCoding.fastJoin.events.TickListener;
import de.diamondCoding.fastJoin.managers.RecentManager;
import de.diamondCoding.fastJoin.managers.ServerManager;
import de.diamondCoding.fastJoin.util.RecentServer;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.KeyElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class FastJoin extends LabyModAddon {

    public String pr = "§6[§3Addon§6] §7";
    public boolean enabeld = true;
    public boolean lastJoin = true;
    public int fastJoinKey = Keyboard.KEY_J;

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

        final BooleanElement lastJoinElement = new BooleanElement( "Show resent servers" , new ControlElement.IconData( Material.ENDER_CHEST ), new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted ) {
                FastJoin.this.lastJoin = accepted;

                FastJoin.this.getConfig().addProperty("lastJoin", lastJoin);
                FastJoin.this.saveConfig();
            }
        } , lastJoin);
        settings.add(lastJoinElement);

        KeyElement keyElement = new KeyElement( "Open FastJoin" ,
                new ControlElement.IconData( Material.REDSTONE_COMPARATOR ) ,
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

    public void registerEvents() {
        this.getApi().registerForgeListener(new TickListener(this));
        this.getApi().getEventManager().registerOnJoin(new ServerJoinListener(this));
    }

}
