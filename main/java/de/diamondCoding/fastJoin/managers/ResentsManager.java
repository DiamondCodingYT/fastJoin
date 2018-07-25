package de.diamondCoding.fastJoin.managers;

import de.diamondCoding.fastJoin.FastJoin;
import de.diamondCoding.fastJoin.util.ResentServer;

import java.util.ArrayList;
import java.util.List;

public class ResentsManager {

    static FastJoin addon;

    public static List<ResentServer> resents;

    public static void init(FastJoin theAddon) {
        addon = theAddon;
        resents = new ArrayList<ResentServer>();
    }

    public static void addResent(String ip) {

        for(ResentServer rs : resents) {
            if(rs.ip.equals(ip)) {
                int oldPos = rs.position;
                for(ResentServer rs2 : resents) {
                    if(rs2.position < oldPos) {
                        rs2.position = rs2.position + 1;
                    }
                }
                rs.position = 0;
                return;
            }
        }

        for(ResentServer rs : resents) {
            if(rs.position == 9) {
                resents.remove(rs);
            } else {
                rs.position = rs.position + 1;
            }
        }
        resents.add(new ResentServer(0, ip));

        for(ResentServer rs : resents) {
            addon.getConfig().addProperty("resent" + rs.position, rs.ip);
        }
        addon.saveConfig();
    }

    public static ResentServer getResent(int pos) {
        for(ResentServer rs : resents) {
            if(rs.position == pos) {
                return rs;
            }
        }
        return null;
    }

}
