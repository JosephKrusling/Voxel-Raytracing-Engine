package com.crosslink.battleprism.client.networking.pipeline;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by Joseph on 5/30/2014.
 */
public class PipelineManager {

    public ChannelHandler channelHandler;

    public LengthFieldBasedFrameDecoder frameDecoder;
    public EventDecoder eventDecoder;

    public EventEncoder eventEncoder;

    public ChannelPipeline addHandlers(ChannelPipeline pipeline) {
        if (pipeline == null)
            return null;

        channelHandler = new ChannelHandler();
        frameDecoder = new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2);
        eventDecoder = new EventDecoder();

        eventEncoder = new EventEncoder();

        pipeline.addLast("channelHandler", channelHandler);
        //pipeline.addLast("frameDecoder", frameDecoder);
        pipeline.addLast("eventDecoder", eventDecoder);

        pipeline.addLast("eventEncoder", eventEncoder);

        return pipeline;
    }


}
