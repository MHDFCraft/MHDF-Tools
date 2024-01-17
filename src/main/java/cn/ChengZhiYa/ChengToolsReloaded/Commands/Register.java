package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.LoginUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.LoginUtil.LoginExists;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.*;

public final class Register implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Bukkit.getScheduler().runTaskAsynchronously(ChengToolsReloaded.instance, () -> {
                if (args.length == 1) {
                    String Password = args[0];
                    Player player = (Player) sender;
                    if (ifLogin(player)) {
                        sender.sendMessage(i18n("Login.AlreadyLogin"));
                        return;
                    }
                    int MinPasswordLength = ChengToolsReloaded.instance.getConfig().getInt("LoginSystemSettings.MinPaswordLength");
                    int MaxPasswordLength = ChengToolsReloaded.instance.getConfig().getInt("LoginSystemSettings.MaxPaswordLength");
                    List<String> EasyPasswords = ChengToolsReloaded.instance.getConfig().getStringList("LoginSystemSettings.EasyPasswords");
                    if (Password.length() < MinPasswordLength) {
                        sender.sendMessage(i18n("Login.LengthShort", String.valueOf(MinPasswordLength)));
                        return;
                    }
                    if (Password.length() > MaxPasswordLength) {
                        sender.sendMessage(i18n("Login.LengthLong", String.valueOf(MaxPasswordLength)));
                        return;
                    }
                    for (String EasyPassword : EasyPasswords) {
                        if (Password.equals(EasyPassword)) {
                            sender.sendMessage(i18n("Login.EasyPassword"));
                            return;
                        }
                    }
                    if (LoginExists(player.getName())) {
                        sender.sendMessage(i18n("Login.AlreadyRegister"));
                        return;
                    }
                    StringHasMap.getHasMap().put(player.getName() + "_Login", "t");
                    LoginUtil.Register(player.getName(), Sha256(Password));
                    if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
                        StringHasMap.getHasMap().put(player.getName() + "_LoginIP", Objects.requireNonNull(player.getAddress()).getHostName());
                    }
                    sender.sendMessage(i18n("Login.RegisterDone"));
                } else {
                    sender.sendMessage(i18n("Usage.Register", label));
                }
            });
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
