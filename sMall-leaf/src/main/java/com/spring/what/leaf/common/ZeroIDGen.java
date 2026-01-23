package com.spring.what.leaf.common;

import com.spring.what.leaf.IDGen;

public class ZeroIDGen implements IDGen {
    @Override
    public Result getId(String key) {
        return new Result(0L, Status.OK);
    }

    @Override
    public boolean init() {
        return true;
    }
}
