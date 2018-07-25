package de.diamondCoding.fastJoin.managers;

import de.diamondCoding.fastJoin.util.JoinabelServer;

import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    public static List<JoinabelServer> mcServer;

    public static void init() {
        mcServer = new ArrayList<JoinabelServer>();
        mcServer.add(new JoinabelServer("minesucht.net", "ms"));
        mcServer.add(new JoinabelServer("gommehd.net", "g"));
        mcServer.add(new JoinabelServer("hypixel.net", "hy"));
        mcServer.add(new JoinabelServer("griefergames.net", "grief"));
        mcServer.add(new JoinabelServer("cubecraft.net", "cube"));
        mcServer.add(new JoinabelServer("neruxvace.net", "nerux"));
        mcServer.add(new JoinabelServer("rewinside.tv", "rewi"));
        mcServer.add(new JoinabelServer("eu.mineplex.com", "mp"));
        mcServer.add(new JoinabelServer("timolia.de", "timo"));
        mcServer.add(new JoinabelServer("bausucht.net", "bs"));
        mcServer.add(new JoinabelServer("blocksmc.com", "blocks"));
        mcServer.add(new JoinabelServer("randymc.de", "randy"));
        mcServer.add(new JoinabelServer("teamkudo.de", "kudo"));
        mcServer.add(new JoinabelServer("varoxcraft.de", "varox"));
        mcServer.add(new JoinabelServer("havocmc.net", "havoc"));
    }

    public static String getFullIp(String shortcut) {
        for(JoinabelServer js : mcServer) {
            if(js.shortcut.equalsIgnoreCase(shortcut)) {
                return js.ip;
            }
        }
        return shortcut;
    }

    public static boolean hasIp(String shortcut) {
        for(JoinabelServer js : mcServer) {
            if(js.shortcut.equalsIgnoreCase(shortcut)) {
                return true;
            }
        }
        return false;
    }

    public static String getShortcut(String ip) {
        for(JoinabelServer js : mcServer) {
            if(js.ip.equalsIgnoreCase(ip)) {
                return js.shortcut;
            }
        }
        return "";
    }

    public static boolean hasShortcut(String ip) {
        for(JoinabelServer js : mcServer) {
            if(js.ip.equalsIgnoreCase(ip)) {
                return true;
            }
        }
        return false;
    }

}
