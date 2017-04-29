package com.crosslink.battleprism.client;

import com.crosslink.battleprism.client.engine.frame.Engine;
import com.crosslink.battleprism.client.interfaces.GameComponent;
import com.crosslink.battleprism.client.networking.NetClient;

public class Game {

    public static NetClient net;
    public static Engine display;

    public static Thread netThread;
    public static Thread displayThread;

    public static GameComponent activeState;

    public static void main(String[] args) {

        net = new NetClient();
        netThread = new Thread(net, "Network Thread");
        netThread.start();
        
        display = new Engine();
        displayThread = new Thread(display, "Display Thread");
        displayThread.start();
        
        int i = 0; //test edit

    }

    public static void terminate() {
        net.requestTerminate();
    }
}
