package ru.terracraft.info.util;

import com.github.ezplugins.updater.UpdateChecker;
import com.github.ezplugins.updater.UpdateResult;
import org.bukkit.Bukkit;
import ru.terracraft.info.TerraCraftInfoPlugin;
import ru.terracraft.info.lang.Lang;

public class UpdateNotifier {

    private static final String GITHUB_OWNER = "luci4ka";
    private static final String GITHUB_REPO  = "TerraCraftInfo";

    private final TerraCraftInfoPlugin plugin;
    private UpdateResult lastResult;

    public UpdateNotifier(TerraCraftInfoPlugin plugin) {
        this.plugin = plugin;
    }

    public void checkAsync() {
        String currentVersion = plugin.getPluginMeta().getVersion();

        UpdateChecker checker = UpdateChecker.builder(GITHUB_OWNER, GITHUB_REPO, currentVersion)
                .build();

        checker.checkNowAsync().thenAccept(result -> {
            this.lastResult = result;

            if (result.hasError()) {
                String error = result.getError().map(Throwable::getMessage).orElse("unknown error");
                plugin.getLogger().warning("Update check failed: " + error);
                return;
            }

            if (result.isUpdateAvailable()) {
                String latest = result.getLatestVersion().orElse("unknown");
                plugin.getLogger().info("Доступно обновление: " + latest);

                Bukkit.getScheduler().runTask(plugin, () ->
                        Bukkit.getOnlinePlayers().stream()
                                .filter(p -> p.hasPermission("terracraftinfo.update"))
                                .forEach(p -> {
                                    String msg = plugin.getLangManager()
                                            .raw(Lang.UPDATE_AVAILABLE, p)
                                            .replace("{version}", latest)
                                            .replace("{url}", result.getReleaseUrl().orElse(""));
                                    p.sendMessage(MessageUtil.parse(msg, p));
                                })
                );
            } else {
                plugin.getLogger().info("Обновлений нет. Версия актуальна.");
            }
        });
    }

    public UpdateResult getLastResult() {
        return lastResult;
    }
}