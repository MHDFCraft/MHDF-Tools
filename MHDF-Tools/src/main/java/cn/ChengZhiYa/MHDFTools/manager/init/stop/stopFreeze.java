package cn.ChengZhiYa.MHDFTools.manager.init.stop;

import cn.ChengZhiYa.MHDFTools.command.subcommand.misc.freeze.Freeze;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

public class stopFreeze implements Invitable {

    @Override
    public void init() {
        Freeze.freezeUUID.clear();
    }
}