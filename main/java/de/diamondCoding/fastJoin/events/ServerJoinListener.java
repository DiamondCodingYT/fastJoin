package de.diamondCoding.fastJoin.events;

import de.diamondCoding.fastJoin.FastJoin;
import de.diamondCoding.fastJoin.managers.RecentManager;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;

public class ServerJoinListener implements Consumer<ServerData> {

    private FastJoin addon;

    public ServerJoinListener(FastJoin addon) {
        this.addon = addon;
    }

    @Override
    public void accept(ServerData serverData) {
        RecentManager.addRecent(serverData.getIp());
    }

}
