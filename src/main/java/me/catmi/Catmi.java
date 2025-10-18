package me.catmi;

import me.catmi.util.*;
import me.catmi.util.config.LoadConfiguration;
import me.catmi.util.config.LoadModules;
import me.catmi.util.config.SaveConfiguration;
import me.catmi.util.config.SaveModules;
import me.catmi.clickgui.ClickGUI;
import me.catmi.command.CommandManager;
import me.catmi.players.friends.Friends;
import me.catmi.players.enemy.Enemies;
import me.catmi.settings.SettingsManager;
import me.catmi.event.EventProcessor;
import me.catmi.macro.MacroManager;
import me.catmi.module.ModuleManager;
import me.catmi.util.font.OGCFONT;
import me.catmi.util.render.CapeUtils;
import me.catmi.util.wing.RenderWings;
import me.catmi.util.wing.WingSettings;
import me.catmi.util.world.TpsUtils;
import me.catmi.util.font.CFontRenderer;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;

@Mod(modid = Catmi.MODID, name = Catmi.FORGENAME, version = Catmi.MODVER, clientSideOnly = true)
public class Catmi {
	public static final String MODID = "neko";
	public static String MODNAME = "Neko";
	public static final String MODVER = "v1";
	public static final String FORGENAME = "Neko";

	public static final Logger log = LogManager.getLogger(MODNAME);

	public ClickGUI clickGUI;
	public SettingsManager settingsManager;
	public Friends friends;
	public ModuleManager moduleManager;
	public SaveConfiguration saveConfiguration;
	public LoadConfiguration loadConfiguration;
	public SaveModules saveModules;
	public LoadModules loadModules;
	public CapeUtils capeUtils;
	public MacroManager macroManager;
	EventProcessor eventProcessor;
	public static ServerManager serverManager;
	public static CFontRenderer fontRenderer;
	public static OGCFONT fontRenderer2;
	public static Enemies enemies;
	public WingSettings wingSettings;
	public static RotationManager rotationManager;
	public static PotionManager potionManager;
	public static SpeedManager speedManager;
	public RotationManager2 rotationManager2;

	public static final EventBus EVENT_BUS = new EventManager();

	@Mod.Instance
	private static Catmi INSTANCE;

	public Catmi(){
		INSTANCE = this;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		wingSettings = new WingSettings(new Configuration(event.getSuggestedConfigurationFile()));
		wingSettings.loadConfig(); // Load all settings.

		// NOTE: HWID/auth checks removed for safety.
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new RenderWings(wingSettings)); // Register wing renderer.

		// NOTE: HWID/auth checks removed for safety.

		eventProcessor = new EventProcessor();
		eventProcessor.init();

		fontRenderer = new CFontRenderer(new Font("Ariel", Font.PLAIN, 18), true, false);
		fontRenderer2 = new OGCFONT(new Font("Ariel", Font.PLAIN, 18), true, false);

		TpsUtils tpsUtils = new TpsUtils();

		settingsManager = new SettingsManager();
		log.info("Settings initialized!");

		friends = new Friends();
		enemies = new Enemies();
		log.info("Friends and enemies initialized!");

		moduleManager = new ModuleManager();
		log.info("Modules initialized!");
		ReflectionFields.init();

		clickGUI = new ClickGUI();
		log.info("ClickGUI initialized!");

		macroManager = new MacroManager();
		log.info("Macros initialized!");

		saveConfiguration = new SaveConfiguration();
		Runtime.getRuntime().addShutdownHook(new Stopper());
		log.info("Config Saved!");

		loadConfiguration = new LoadConfiguration();
		log.info("Config Loaded!");

		rotationManager = new RotationManager();
		serverManager = new ServerManager();
		potionManager = new PotionManager();
		speedManager = new SpeedManager();
		rotationManager2 = new RotationManager2();

		saveModules = new SaveModules();
		Runtime.getRuntime().addShutdownHook(new Stopper());
		log.info("Modules Saved!");

		loadModules = new LoadModules();
		log.info("Modules Loaded!");

		CommandManager.initCommands();
		log.info("Commands initialized!");

		log.info("Initialization complete!\n");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		Display.setTitle(MODNAME + " " + MODVER);

		capeUtils = new CapeUtils();
		log.info("Capes initialised!");

		log.info("PostInitialization complete!\n");
	}

	public static Catmi getInstance(){
		return INSTANCE;
	}

	/**
	 * Safe placeholder for the original GetCapeLink method.
	 * Kept so other modules calling Catmi.getInstance().GetCapeLink() compile.
	 * This implementation intentionally does NOT send any external requests or webhooks.
	 */
	public void GetCapeLink() {
		// Intentionally left safe/empty to avoid leaking user data.
		log.info("GetCapeLink() called (NO-OP - HWID/webhook disabled).");
	}
}
