package com.shanglan.pulongwan.thread;

/**
 * Created by cuishiying on 2017/7/27.
 */
public class Lock {
    private boolean lock = true;

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
