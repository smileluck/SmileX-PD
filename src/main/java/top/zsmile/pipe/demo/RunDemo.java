package top.zsmile.pipe.demo;

import top.zsmile.pipe.core.ChannelPipeline;

import java.util.ArrayList;

public class RunDemo {
    public static void main(String[] args) {
        ChannelPipeline channelPipeline = new ChannelPipeline();
        channelPipeline.addFirst(new InputHandler());
        channelPipeline.addLast(new TranslateHandler());
        channelPipeline.addLast(new OutputHandler());
        channelPipeline.run();



        ChannelPipeline channelPipeline2 = new ChannelPipeline();
        channelPipeline2.addFirst(new InputHandler());
        channelPipeline2.addLast(new TranslateHandler());
        channelPipeline2.addLast(new OutputHandler());
        channelPipeline2.run();

        Integer.valueOf()
    }
}
