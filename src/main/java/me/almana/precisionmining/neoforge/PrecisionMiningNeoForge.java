package me.almana.precisionmining.neoforge;

import me.almana.precisionmining.PrecisionMiningClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(PrecisionMiningClient.MODID)
public final class PrecisionMiningNeoForge {
    public PrecisionMiningNeoForge(IEventBus modBus, Dist dist) {
        if (dist.isClient()) {
            modBus.addListener(PrecisionMiningNeoForge::registerKeys);
            NeoForge.EVENT_BUS.addListener(PrecisionMiningNeoForge::tick);
        }
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(PrecisionMiningClient.TOGGLE_KEY);
    }

    private static void tick(ClientTickEvent.Post event) {
        PrecisionMiningClient.tick();
    }
}
