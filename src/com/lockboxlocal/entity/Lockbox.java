package com.lockboxlocal.entity;

public class Lockbox {

    String name;
    String contents;
    int locked;
    long relockTimestamp;
    Long unlockTimestamp;
    long unlockDelay;
    long relockDelay;

    public Lockbox() {

    }

    public Lockbox(String name, String contents, int locked, long relockTimestamp, long unlockTimestamp, long unlockDelay, long relockDelay) {
        this.name = name;
        this.contents = contents;
        this.locked = locked;
        this.relockTimestamp = relockTimestamp;
        this.unlockTimestamp = unlockTimestamp;
        this.unlockDelay = unlockDelay;
        this.relockDelay = relockDelay;
    }

    public Long getUnlockTimestamp() {
        return unlockTimestamp;
    }

    public int getLocked() {
        return locked;
    }

    public String getContents() {
        return contents;
    }

    public long getRelockTimestamp() {
        return relockTimestamp;
    }

    public void setUnlockTimestamp(Long unlockTimestamp) {
        this.unlockTimestamp = unlockTimestamp;
    }

    public void setRelockTimestamp(long relockTimestamp) {
        this.relockTimestamp = relockTimestamp;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public long getUnlockDelay() {
        return unlockDelay;
    }

    public long getRelockDelay() {
        return relockDelay;
    }
}
