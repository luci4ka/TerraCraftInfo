package ru.net.terracraft.info.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.net.terracraft.info.TerraCraftInfoPlugin;
import ru.net.terracraft.info.config.InfoConfig;
import ru.net.terracraft.info.lang.Lang;
import ru.net.terracraft.info.util.MessageUtil;

import java.util.Map;
import java.util.UUID;

public class HelpCommand {

    private final TerraCraftInfoPlugin plugin;

    public HelpCommand(TerraCraftInfoPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        new CommandAPICommand("help")
                .withAliases("помощь")
                .executes((CommandSender sender, CommandArguments args) -> {
                    execute(sender);
                    return 1; // ResultingCommandExecutor — возвращаем int
                })
                .register();
    }

    private void execute(CommandSender sender) {
        var cfg = plugin.getInfoConfig();
        var lang = plugin.getLangManager();
        Player player = sender instanceof Player p ? p : null;

        sender.sendMessage(MessageUtil.parse(lang.raw(Lang.HELP_HEADER, player), player));

        boolean any = false;
        for (Map.Entry<String, InfoConfig.GroupEntry> e : cfg.help.groups.entrySet()) {
            String group = e.getKey();
            InfoConfig.GroupEntry entry = e.getValue();

            if (!shouldShow(player, group, cfg.help.strictGroupCheck)) continue;

            any = true;

            // Заголовок группы — только если не default
            if (!group.equalsIgnoreCase("default")) {
                String gh = lang.raw(Lang.HELP_GROUP_HEADER, player);
                if (gh != null) {
                    sender.sendMessage(MessageUtil.parse(
                            gh.replace("{group}", group), player));
                }
            }

            String entryFormat = lang.raw(Lang.HELP_ENTRY_FORMAT, player);
            String aliasesFormat = lang.raw(Lang.HELP_ALIASES_FORMAT, player);
            String hoverClick = lang.raw(Lang.HELP_HOVER_CLICK, player);

            for (InfoConfig.CommandEntry cmd : entry.commands()) {
                String aliases = (cmd.aliases() == null || cmd.aliases().isEmpty())
                        ? "" : String.join(", ", cmd.aliases());

                Component line = MessageUtil.parse(
                        entryFormat
                                .replace("{cmd}", cmd.cmd())
                                .replace("{description}", cmd.description()),
                        player
                );

                // Клик на команду + ховер
                line = line.clickEvent(ClickEvent.suggestCommand(cmd.cmd()))
                        .hoverEvent(HoverEvent.showText(
                                MessageUtil.parse(
                                        (hoverClick == null ? "<gray>Нажми: <white>{cmd}</white></gray>" : hoverClick)
                                                .replace("{cmd}", cmd.cmd()),
                                        player)));

                if (!aliases.isEmpty() && aliasesFormat != null) {
                    line = line.append(MessageUtil.parse(
                            aliasesFormat.replace("{aliases}", aliases), player));
                }

                sender.sendMessage(line);
            }
        }

        if (!any) {
            sender.sendMessage(MessageUtil.parse(lang.raw(Lang.HELP_NO_GROUPS, player), player));
        }
    }

    private boolean shouldShow(Player player, String group, boolean strict) {
        if (group.equalsIgnoreCase("default")) return true;
        if (player == null) return !strict;
        if (!strict) return true;

        try {
            Class<?> provider = Class.forName("net.luckperms.api.LuckPermsProvider");
            Object lp = provider.getMethod("get").invoke(null);
            Object userManager = lp.getClass().getMethod("getUserManager").invoke(lp);
            Object user = userManager.getClass().getMethod("getUser", UUID.class)
                    .invoke(userManager, player.getUniqueId());
            if (user == null) return false;

            Object cached = user.getClass().getMethod("getCachedData").invoke(user);
            Object permData = cached.getClass().getMethod("getPermissionData").invoke(cached);
            return (boolean) permData.getClass()
                    .getMethod("checkPermission", String.class)
                    .invoke(permData, "group." + group);
        } catch (Throwable t) {
            // LuckPerms не установлен — показываем все
            return true;
        }
    }
}