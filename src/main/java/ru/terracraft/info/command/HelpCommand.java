package ru.terracraft.info.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.terracraft.info.TerraCraftInfoPlugin;
import ru.terracraft.info.content.ContentManager;
import ru.terracraft.info.lang.Lang;
import ru.terracraft.info.util.LocalizedHelpTopic;
import ru.terracraft.info.util.MessageUtil;

import java.util.List;
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
                .withHelp(new LocalizedHelpTopic(plugin, "help", Lang.HELP_COMMAND_HELP_SHORT, Lang.HELP_COMMAND_HELP_FULL))
                .executes((CommandSender sender, CommandArguments args) -> {
                    execute(sender);
                    return 1;
                })
                .register();
    }

    private void execute(CommandSender sender) {
        var cfg = plugin.getInfoConfig();
        var lang = plugin.getLangManager();
        var content = plugin.getContentManager();
        Player player = sender instanceof Player p ? p : null;

        sender.sendMessage(MessageUtil.parse(lang.raw(Lang.HELP_HEADER, player), player));

        Map<String, List<ContentManager.CommandEntry>> groups = content.groups(player);
        boolean any = false;

        for (Map.Entry<String, List<ContentManager.CommandEntry>> e : groups.entrySet()) {
            String group = e.getKey();
            List<ContentManager.CommandEntry> commands = e.getValue();

            if (!shouldShow(player, group, cfg.help.strictGroupCheck)) continue;
            any = true;

            if (!group.equalsIgnoreCase("default")) {
                String gh = lang.raw(Lang.HELP_GROUP_HEADER, player);
                if (gh != null) {
                    sender.sendMessage(MessageUtil.parse(gh.replace("{group}", group), player));
                }
            }

            String entryFormat = lang.raw(Lang.HELP_ENTRY_FORMAT, player);
            String aliasesFormat = lang.raw(Lang.HELP_ALIASES_FORMAT, player);
            String hoverClick = lang.raw(Lang.HELP_HOVER_CLICK, player);

            for (ContentManager.CommandEntry cmd : commands) {
                String aliases = (cmd.aliases() == null || cmd.aliases().isEmpty())
                        ? "" : String.join(", ", cmd.aliases());

                Component line = MessageUtil.parse(
                        entryFormat
                                .replace("{cmd}", cmd.cmd())
                                .replace("{description}", cmd.description()),
                        player
                );

                line = line.clickEvent(ClickEvent.suggestCommand(cmd.cmd()))
                        .hoverEvent(HoverEvent.showText(
                                MessageUtil.parse(
                                        hoverClick.replace("{cmd}", cmd.cmd()),
                                        player)));

                if (!aliases.isEmpty()) {
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

            Class<?> cachedDataClass = Class.forName("net.luckperms.api.cacheddata.CachedData");
            Class<?> permissionDataClass = Class.forName("net.luckperms.api.cacheddata.CachedPermissionData");

            Object cached = user.getClass().getMethod("getCachedData").invoke(user);
            Object permData = cachedDataClass.getMethod("getPermissionData").invoke(cached);
            return (boolean) permissionDataClass
                    .getMethod("checkPermission", String.class)
                    .invoke(permData, "group." + group);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("LuckPerms check failed for group '" + group + "': " + e.getMessage());
            return true;
        }
    }
}