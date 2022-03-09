package top.zsmile.pipe.core;

public abstract class AbstractHandlerContext {
    private AbstractHandlerContext next;
    private AbstractHandlerContext prev;

    public AbstractHandlerContext getPrev() {
        return prev;
    }

    public void setPrev(AbstractHandlerContext prev) {
        this.prev = prev;
    }

    public AbstractHandlerContext getNext() {
        return next;
    }

    public void setNext(AbstractHandlerContext next) {
        this.next = next;
    }

    public abstract void exec();
}
