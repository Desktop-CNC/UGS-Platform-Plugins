package com.cchraplak.ugs.platform.plugin.toolchanger;
/** Localizable strings for {@link com.cchraplak.ugs.platform.plugin.toolchanger}. */
class Bundle {
    /**
     * @return <i>Window</i>
     * @see WindowTopComponent
     */
    static String CTL_WindowAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_WindowAction");
    }
    /**
     * @return <i>Window Window</i>
     * @see WindowTopComponent
     */
    static String CTL_WindowTopComponent() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_WindowTopComponent");
    }
    /**
     * @return <i>This is a Window window</i>
     * @see WindowTopComponent
     */
    static String HINT_WindowTopComponent() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "HINT_WindowTopComponent");
    }
    private Bundle() {}
}
