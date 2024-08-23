package cn.ChengZhiYa.MHDFTools.manager.init.stop;

import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.freeze.Freeze;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

public class stopFreeze implements Invitable {

    @Override
    public void start() {
        Freeze.freezeUUID.clear();
    }
}