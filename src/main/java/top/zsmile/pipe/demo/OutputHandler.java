package top.zsmile.pipe.demo;

import top.zsmile.pipe.core.Handler;

public class OutputHandler implements Handler {
    @Override
    public void work() {
        System.out.println("out");
    }
}
