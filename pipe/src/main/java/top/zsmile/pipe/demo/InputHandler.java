package top.zsmile.pipe.demo;

import top.zsmile.pipe.core.AbstractHandlerContext;
import top.zsmile.pipe.core.Handler;

public class InputHandler implements Handler {
    @Override
    public void work() {
        System.out.println("input");
    }
}
