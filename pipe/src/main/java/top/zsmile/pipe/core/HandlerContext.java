package top.zsmile.pipe.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerContext extends AbstractHandlerContext {
    private Handler handler;

    public HandlerContext(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void exec() {
        if (this.handler != null) {
            handler.work();
        }
        if (this.getNext() != null) {
            ChannelPipeline.executor.submit(new Runnable() {
                @Override
                public void run() {
                    getNext().exec();
                }
            });
        }
    }

    public static final class HeaderHandlerContext extends AbstractHandlerContext {

        @Override
        public void exec() {
            if (this.getNext() != null) {
                System.out.println("header");
                ChannelPipeline.executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        getNext().exec();
                    }
                });
            }
        }
    }

    public static final class TailHandlerContext extends AbstractHandlerContext {

        @Override
        public void exec() {
            System.out.println("tail");
        }
    }
}


