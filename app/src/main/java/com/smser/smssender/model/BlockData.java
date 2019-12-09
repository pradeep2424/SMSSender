package com.smser.smssender.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BlockData implements Serializable {

    private String blockNumber;
    private String blockName;

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    @NonNull
    @Override
    public String toString() {
        return blockNumber;
    }
}
