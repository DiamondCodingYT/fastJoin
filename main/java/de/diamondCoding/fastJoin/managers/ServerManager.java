package de.diamondCoding.fastJoin.managers;

import de.diamondCoding.fastJoin.FastJoin;
import de.diamondCoding.fastJoin.util.JoinabelServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    private static List<JoinabelServer> mcServers;
    public static boolean serverError = false;
    private static FastJoin addon;

    public static void init(FastJoin theAddon) {
        addon = theAddon;
        mcServers = new ArrayList<JoinabelServer>();
        fillServers();
    }

    public static void fillServers() {

        mcServers.clear();

        for(String element : addon.personalShortcuts.split(";")) {
            if(element.equals("")) {
                continue;
            }
            mcServers.add(new JoinabelServer(element.replaceAll(" ", "").split(":")[1], element.replaceAll(" ", "").split(":")[0]));
        }

        try {

            URL url = new URL("http://www.diamondcoding.eu/fastJoin/shortcuts.php");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("error")) {
                    serverError = true;
                } else {
                    String[] pairs = inputLine.split(":::");
                    for (String s : pairs) {
                        mcServers.add(new JoinabelServer(s.split("::")[1], s.split("::")[0]));
                    }
                }
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
            serverError = true;
        }
        if(serverError) {
            mcServers.add(new JoinabelServer("minesucht.net", "ms"));
            mcServers.add(new JoinabelServer("gommehd.net", "g"));
            mcServers.add(new JoinabelServer("hypixel.net", "hy"));
            mcServers.add(new JoinabelServer("griefergames.net", "grief"));
            mcServers.add(new JoinabelServer("cubecraft.net", "cube"));
            mcServers.add(new JoinabelServer("neruxvace.net", "nerux"));
            mcServers.add(new JoinabelServer("rewinside.tv", "rewi"));
            mcServers.add(new JoinabelServer("eu.mineplex.com", "mp"));
            mcServers.add(new JoinabelServer("timolia.de", "timo"));
            mcServers.add(new JoinabelServer("bausucht.net", "bs"));
            mcServers.add(new JoinabelServer("blocksmc.com", "blocks"));
            mcServers.add(new JoinabelServer("randymc.de", "randy"));
            mcServers.add(new JoinabelServer("teamkyudo.de", "kyudo"));
            mcServers.add(new JoinabelServer("varoxcraft.de", "varox"));
            mcServers.add(new JoinabelServer("havocmc.net", "havoc"));
            System.out.println("Cause of a Server error, the Shortcut List was filled from an offline List.");
        } else {
            System.out.println("The Server List was filled with the Servers from the Server. xD");
        }
    }

    public static String getFullIp(String shortcut) {
        for(JoinabelServer js : mcServers) {
            if(js.shortcut.equalsIgnoreCase(shortcut)) {
                return js.ip;
            }
        }
        return shortcut;
    }

    public static boolean hasIp(String shortcut) {
        for(JoinabelServer js : mcServers) {
            if(js.shortcut.equalsIgnoreCase(shortcut)) {
                return true;
            }
        }
        return false;
    }

    public static String getShortcut(String ip) {
        for(JoinabelServer js : mcServers) {
            if(js.ip.equalsIgnoreCase(ip)) {
                return js.shortcut;
            }
        }
        return "";
    }

    public static boolean hasShortcut(String ip) {
        for(JoinabelServer js : mcServers) {
            if(js.ip.equalsIgnoreCase(ip)) {
                return true;
            }
        }
        return false;
    }

}
