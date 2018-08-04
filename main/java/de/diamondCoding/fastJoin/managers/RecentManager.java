package de.diamondCoding.fastJoin.managers;

import de.diamondCoding.fastJoin.FastJoin;
import de.diamondCoding.fastJoin.util.RecentServer;

import java.util.ArrayList;
import java.util.Iterator;
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

        boolean iterating = true;
        Iterator<RecentServer> iter = recentServers.iterator();
        while (iter.hasNext() && iterating) {
            //System.out.println("Starting a new Ding");
            RecentServer rs = iter.next();
            if(rs.position == 9) {
                //System.out.println("Deliting Number 9...");
                recentServers.remove(rs);
                iterating = false;
                //System.out.println("Number 9 was deleted and stoped iterating");
            }
        }

        iterating = true;
        Iterator<RecentServer> iter2 = recentServers.iterator();
        while (iter2.hasNext() && iterating) {
            //System.out.println("Starting a new Ding for Backshifting");
            RecentServer rs = iter2.next();
            //System.out.println("Backshifting...");
            rs.position = rs.position + 1;
            //System.out.println("Backshifted");
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
