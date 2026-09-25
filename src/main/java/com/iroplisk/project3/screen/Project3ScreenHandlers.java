package com.iroplisk.project3.screen;

import com.iroplisk.project3.Project3;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class Project3ScreenHandlers {
    public static final ScreenHandlerType<CalcinatorInterfaceHandler> CALCINATOR_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Project3.MOD_ID, "calcinator"),
                    new ExtendedScreenHandlerType<>(CalcinatorInterfaceHandler::new));
    public static final ScreenHandlerType<AludelInterfaceHandler> ALUDEL_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Project3.MOD_ID, "aludel"),
                    new ExtendedScreenHandlerType<>(AludelInterfaceHandler::new));
    public static final ScreenHandlerType<AlchemicalBagHandler> ALCHEMICAL_BAG_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Project3.MOD_ID, "alchemical_bag"),
                    new ExtendedScreenHandlerType<>(AlchemicalBagHandler::new));
    public static final ScreenHandlerType<AlchemicalBagHandler> ALCHEMICAL_BAG_SMALL_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Project3.MOD_ID, "alchemical_bag_small"),
                    new ExtendedScreenHandlerType<>(AlchemicalBagHandler::new));
    public static final ScreenHandlerType<AlchemicalBagHandler> ALCHEMICAL_BAG_MEDIUM_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Project3.MOD_ID, "alchemical_bag_medium"),
                    new ExtendedScreenHandlerType<>(AlchemicalBagHandler::new));
    public static final ScreenHandlerType<AlchemicalBagHandler> ALCHEMICAL_BAG_LARGE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Project3.MOD_ID, "alchemical_bag_large"),
                    new ExtendedScreenHandlerType<>(AlchemicalBagHandler::new));
    public static final ScreenHandlerType<ResearchStationInterfaceHandler> RESEARCH_STATION_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Project3.MOD_ID, "research_table"),
                    new ExtendedScreenHandlerType<>(ResearchStationInterfaceHandler::new));
    public static final ScreenHandlerType<TabletInterfaceHandler> TABLET_INTERFACE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Project3.MOD_ID, "transmutation_tablet"),
                    new ExtendedScreenHandlerType<>(TabletInterfaceHandler::new));

    public static void registerScreenHandlers() {
        Project3.LOGGER.info("Registering Screen Handlers for " + Project3.MOD_ID);
    }
}
