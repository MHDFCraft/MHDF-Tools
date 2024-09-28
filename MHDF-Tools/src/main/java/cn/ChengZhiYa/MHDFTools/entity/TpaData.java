package cn.ChengZhiYa.MHDFTools.entity;

import lombok.Data;

@Data
public final class TpaData {
    String targetPlayerName;
    Integer tpaOutTime;

    public TpaData(String targetPlayerName, Integer tpaOutTime) {
        this.targetPlayerName = targetPlayerName;
        this.tpaOutTime = tpaOutTime;
    }

    public void takeTime(Integer takeTime) {
        tpaOutTime = tpaOutTime - takeTime;
    }
}
