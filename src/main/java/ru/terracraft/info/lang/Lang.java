package ru.terracraft.info.lang;

public enum Lang {

    PREFIX("prefix"),

    // ─── Rules ────────────────────────────────
    RULES_HEADER("rules.header"),
    RULES_FOOTER("rules.footer"),
    RULES_HELP_SHORT("rules.help.short"),
    RULES_HELP_FULL("rules.help.full"),

    // ─── Info ─────────────────────────────────
    INFO_HEADER("info.header"),
    INFO_HELP_SHORT("info.help.short"),
    INFO_HELP_FULL("info.help.full"),

    // ─── Help ─────────────────────────────────
    HELP_HEADER("help.header"),
    HELP_GROUP_HEADER("help.group_header"),
    HELP_ENTRY_FORMAT("help.entry_format"),
    HELP_ALIASES_FORMAT("help.aliases_format"),
    HELP_NO_GROUPS("help.no_groups"),
    HELP_HOVER_CLICK("help.hover_click"),
    HELP_COMMAND_HELP_SHORT("help.command.help.short"),
    HELP_COMMAND_HELP_FULL("help.command.help.full"),

    // ─── Links ────────────────────────────────
    LINKS_HEADER("links.header"),
    LINKS_ENTRY_FORMAT("links.entry_format"),
    LINKS_HOVER_OPEN("links.hover_open"),
    LINKS_HELP_SHORT("links.help.short"),
    LINKS_HELP_FULL("links.help.full"),

    // ─── Pagination ───────────────────────────
    PAGE_HEADER("pagination.header"),
    PAGE_FOOTER("pagination.footer"),
    PAGE_NEXT_LABEL("pagination.next_label"),
    PAGE_PREV_LABEL("pagination.prev_label"),
    PAGE_INDICATOR("pagination.page_indicator"),
    PAGE_HOVER_NEXT("pagination.hover_next"),
    PAGE_HOVER_PREV("pagination.hover_prev"),

    // ─── Update ───────────────────────────────
    UPDATE_AVAILABLE("update.available");

    private final String path;

    Lang(String path) { this.path = path; }

    public String getPath() { return path; }
}