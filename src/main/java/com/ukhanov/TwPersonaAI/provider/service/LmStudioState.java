package com.ukhanov.TwPersonaAI.provider.service;

public class LmStudioState {
    private static volatile boolean busy = false;

    public static boolean isBusy() {
        return busy;
    }

    public static void setBusy(boolean value) {
        busy = value;
    }
}

