package com.spring.what.leaf.common;

import lombok.Data;

@Data
public class Result {

    private Long id;

    private Status status;

    public Result() {

    }

    public Result(Long id, Status status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Result{" + "id=" + id +
                ", status=" + status +
                '}';
    }
}
