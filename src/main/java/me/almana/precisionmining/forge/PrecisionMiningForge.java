package me.almana.precisionmining.forge;

import me.almana.precisionmining.PrecisionMiningClient;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(PrecisionMiningClient.MODID)
public final class PrecisionMiningForge {
    public PrecisionMiningForge() {
        if (FMLEnvironment.dist.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(PrecisionMiningForge::registerKeys);
            MinecraftForge.EVENT_BUS.addListener(PrecisionMiningForge::tick);
        }
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(PrecisionMiningClient.TOGGLE_KEY);
    }

    private static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PrecisionMiningClient.tick();
        }
    }
}
