package com.crosslink.battleprism.server.session;

import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.server.events.IncomingEvent;
import com.crosslink.battleprism.server.events.impl.incoming.IncomingDestroyVoxel;
import com.crosslink.battleprism.server.events.impl.outgoing.OutgoingDestroyVoxel;
import com.crosslink.battleprism.server.events.impl.outgoing.OutgoingSendChunk;
import com.crosslink.battleprism.server.session.group.SessionGroup;
import com.crosslink.battleprism.server.world.nodes.ChunkMap;
import com.crosslink.battleprism.server.world.nodes.NodePosition;
import com.crosslink.battleprism.server.world.nodes.RootChunk;
import io.netty.channel.Channel;

/**
 * Created by Joseph on 5/17/2014.
 */
public class Session {

    public Channel channel;
    public long lastConnectionEvent;

    private boolean channelActive = false;

    public Session() {
        SessionGroup.allSessions.sessions.add(this);
    }

    public void end() {
        channelActive = false;
        channel.close();
        SessionGroup.allSessions.sessions.remove(this);
    }

    public void channelInactive() {
        System.out.println("Session ended from: " + channel.remoteAddress().toString());
        end();
        channel = null;
        lastConnectionEvent = System.currentTimeMillis();
    }

    public void channelActive(Channel newChannel) {
        channelActive = true;
        channel = newChannel;
        lastConnectionEvent = System.currentTimeMillis();
        System.out.println("Session started from: " + channel.remoteAddress().toString());
        RootChunk root = new RootChunk(new Vec3I(0,0,0));
        root.createChildVoxel(Octant.BOTTOM_BACK_LEFT, false);
        root.createChildVoxel(Octant.TOP_BACK_LEFT, false);
//        channel.writeAndFlush(new OutgoingSendChunk(root));
    }

    public void event(IncomingEvent event) {
        switch (event.getEventType()) {
            case 0:
                IncomingDestroyVoxel incoming = (IncomingDestroyVoxel) event;

                NodePosition nodePosition = new NodePosition(incoming.getRootIndex(), incoming.getRelativePosition());
                RootChunk affectedRoot = ChunkMap.getRootChunk(incoming.getRootIndex());
                //affectedRoot.deleteVoxel(nodePosition, incoming.getLevel(), false);

                OutgoingDestroyVoxel outgoing = new OutgoingDestroyVoxel(incoming.getRootIndex(), incoming.getRelativePosition(), incoming.getLevel());
                for (Session session : SessionGroup.allSessions.sessions) {
                    if (channelActive)
                        session.channel.writeAndFlush(outgoing);
                }

                System.out.println(incoming.getRelativePosition().toString());
        }
    }

}
