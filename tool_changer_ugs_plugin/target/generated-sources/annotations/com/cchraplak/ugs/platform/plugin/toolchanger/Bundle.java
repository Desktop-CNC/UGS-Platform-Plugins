package com.cchraplak.ugs.platform.plugin.toolchanger;
/** Localizable strings for {@link com.cchraplak.ugs.platform.plugin.toolchanger}. */
class Bundle {
    /**
     * @return <i>ToolTable</i>
     * @see ToolTableTopComponent
     */
    static String CTL_ToolTableAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_ToolTableAction");
    }
    /**
     * @return <i>ToolTable Window</i>
     * @see ToolTableTopComponent
     */
    static String CTL_ToolTableTopComponent() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_ToolTableTopComponent");
    }
    /**
     * @return <i>This is a ToolTable window</i>
     * @see ToolTableTopComponent
     */
    static String HINT_ToolTableTopComponent() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "HINT_ToolTableTopComponent");
    }
    private Bundle() {}
}
