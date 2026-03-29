package me.almana.precisionmining;

public final class PrecisionMiningState {
    private static boolean enabled = true;

    private PrecisionMiningState() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static boolean toggle() {
        enabled = !enabled;
        return enabled;
    }
}
