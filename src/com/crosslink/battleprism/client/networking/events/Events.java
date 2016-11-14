package com.crosslink.battleprism.client.networking.events;

/**
 * Created by Joseph on 7/14/2014.
 */
public class Events {

    public static class Incoming {
        public static final byte DESTROY_VOXEL = 0x00;
        public static final byte SEND_CHUNK = 0x01;
    }

    public static class Outgoing {
        public static final byte DESTROY_VOXEL = 0x00;

        public static final byte REQUEST_CHUNK = 0x02;
    }

}
