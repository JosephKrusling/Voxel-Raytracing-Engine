package com.crosslink.battleprism.server.session;

import com.crosslink.battleprism.server.pipeline.ChannelHandler;
import com.crosslink.battleprism.server.pipeline.EventDecoder;
import com.crosslink.battleprism.server.pipeline.EventEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by Joseph on 5/17/2014.
 */
public class SessionPipelineManager {

    public Session session;

    public ChannelHandler channelHandler;
    public LengthFieldBasedFrameDecoder frameDecoder;
    public EventDecoder eventDecoder;

    public EventEncoder eventEncoder;
    public LengthFieldPrepender frameEncoder;

    public SessionPipelineManager(Session session) {
        this.session = session;
    }

    public ChannelPipeline addHandlers (ChannelPipeline pipeline) {
        if (pipeline == null)
            return null;

        channelHandler = new ChannelHandler(session);
        frameDecoder = new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2);
        eventDecoder = new EventDecoder(session);

        eventEncoder = new EventEncoder();
        frameEncoder = new LengthFieldPrepender(2);

        pipeline.addLast("channelHandler", channelHandler);
        //pipeline.addLast("frameDecoder", frameDecoder);
        pipeline.addLast("eventDecoder", eventDecoder);

        pipeline.addLast("eventEncoder", eventEncoder);
        //pipeline.addLast("frameEncoder", frameEncoder);

        return pipeline;
    }
}
