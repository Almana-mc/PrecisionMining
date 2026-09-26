package me.almana.precisionmining.fabric;

import me.almana.precisionmining.PrecisionMiningClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;*/

public final class PrecisionMiningFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //? if >=26.1 {
        KeyMappingHelper.registerKeyMapping(PrecisionMiningClient.TOGGLE_KEY);
        //?} else
        /*KeyBindingHelper.registerKeyBinding(PrecisionMiningClient.TOGGLE_KEY);*/
        ClientTickEvents.END_CLIENT_TICK.register(client -> PrecisionMiningClient.tick());
    }
}
