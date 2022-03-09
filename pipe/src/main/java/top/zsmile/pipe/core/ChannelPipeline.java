package top.zsmile.pipe.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChannelPipeline {
    private AbstractHandlerContext head;
    private AbstractHandlerContext tail;

    public static final ExecutorService executor = Executors.newCachedThreadPool();//线程池

    public ChannelPipeline() {
        this.head = new HandlerContext.HeaderHandlerContext();
        this.tail = new HandlerContext.TailHandlerContext();
        this.head.setNext(this.tail);
        this.tail.setPrev(this.head);
    }

    public void addFirst(Handler handler) {
        HandlerContext first = new HandlerContext(handler);
        AbstractHandlerContext next = this.head.getNext();

        first.setPrev(this.head);
        first.setNext(next);

        next.setPrev(first);
        this.head.setNext(first);
    }

    public void addLast(Handler handler) {
        HandlerContext last = new HandlerContext(handler);
        AbstractHandlerContext prev = this.tail.getPrev();

        last.setPrev(prev);
        last.setNext(this.tail);

        prev.setNext(last);
        this.tail.setPrev(last);
    }

    public void run() {
        executor.submit(() -> {
            this.head.exec();
        });
    }

}
