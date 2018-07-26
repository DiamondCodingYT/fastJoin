package de.diamondCoding.fastJoin.managers;

import de.diamondCoding.fastJoin.FastJoin;
import de.diamondCoding.fastJoin.util.RecentServer;

import java.util.ArrayList;
import java.util.List;

public class RecentManager {

    static FastJoin addon;

    public static List<RecentServer> recentServers;

    public static void init(FastJoin theAddon) {
        addon = theAddon;
        recentServers = new ArrayList<RecentServer>();
    }

    public static void addResent(String ip) {

        for(RecentServer rs : recentServers) {
            if(rs.ip.equals(ip)) {
                int oldPos = rs.position;
                for(RecentServer rs2 : recentServers) {
                    if(rs2.position < oldPos) {
                        rs2.position = rs2.position + 1;
                    }
                }
                rs.position = 0;
                return;
            }
        }

        for(RecentServer rs : recentServers) {
            if(rs.position == 9) {
                recentServers.remove(rs);
            } else {
                rs.position = rs.position + 1;
            }
        }
        recentServers.add(new RecentServer(0, ip));

        for(RecentServer rs : recentServers) {
            addon.getConfig().addProperty("recent" + rs.position, rs.ip);
        }
        addon.saveConfig();
    }

    public static RecentServer getResent(int pos) {
        for(RecentServer rs : recentServers) {
            if(rs.position == pos) {
                return rs;
            }
        }
        return null;
    }

}
