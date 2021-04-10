package com.adrianwowk.spawncommand;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnCommand extends JavaPlugin implements CommandExecutor, Listener {
    Server server;
    ConsoleCommandSender console;
    public static String prefix;
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    public static ItemStack magicFireStick;
    public static ShapedRecipe magicFireStickRecipe;
    public static ItemStack fwi;
    public static ShapedRecipe fwir;
    public static FireworkMeta fwm;

    public SpawnCommand() {
        this.server = Bukkit.getServer();
        this.console = this.server.getConsoleSender();
        this.prefix = ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "SpawnCommand" + ChatColor.GRAY + "] ";
    }

    public void onEnable() {
        // Register command tab completer and executer
//
//        initMagicStick();
//        initTearGasFireWork();

        getCommand("spawn").setExecutor(this);
        getCommand("cancel").setExecutor(this);
        getCommand("ckick").setExecutor(this);
        getCommand("afk").setExecutor(this);
        getCommand("crestart").setExecutor(this);

        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        // Server Console Message
        this.getLogger().info(ChatColor.GREEN + "=================================");
        this.getLogger().info(ChatColor.GREEN + "         [SpawnCommand]          ");
        this.getLogger().info(ChatColor.GREEN + "  Has been successfuly enabled!  ");
        this.getLogger().info(ChatColor.GREEN + "     Author - Adrian Wowk        ");
        this.getLogger().info(ChatColor.GREEN + "=================================");
    }

    public void onDisable() {

    }

//    @EventHandler
//    public void fireworkExplodeEvent(final FireworkExplodeEvent e){
//        Firework fw = e.getEntity();
//
//        if (fw.getFireworkMeta().isUnbreakable() && fw.isShotAtAngle()){
//            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Firework Detonated");
//
//            //summon area effect clouds
//            Location loc = fw.getLocation();
//
//            for (int i = 0; i < 12; i++) {
//                AreaEffectCloud aec = (AreaEffectCloud) loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
//
//                PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS, 1200, 4, false, true, false);
//                aec.addCustomEffect(pe, true);
//                pe = new PotionEffect(PotionEffectType.CONFUSION, 600, 0, false, true, false);
//                aec.addCustomEffect(pe, true);
//                pe = new PotionEffect(PotionEffectType.SLOW, 600, 0, false, true, false);
//                aec.addCustomEffect(pe, true);
//                pe = new PotionEffect(PotionEffectType.WITHER, 300, 0, false, true, false);
//                aec.addCustomEffect(pe, true);
//
//                aec.setColor(Color.fromRGB(217, 240, 173));
//                loc.setY(loc.getY() + 0.25);
//            }
//        }
//    }

    @EventHandler
    public void playerMoveEvent(final PlayerMoveEvent e) {

        Location movedFrom = e.getFrom();
        Location movedTo = e.getTo();
        Player p = e.getPlayer();
        if ((movedFrom.getX() != movedTo.getX()) && (movedFrom.getY() != movedTo.getY()) && (movedFrom.getZ() != movedTo.getZ())) {
            if (map.containsKey(e.getPlayer().getName())) {
                if (map.get(e.getPlayer().getName())) {
                    playerCancelCommand(e.getPlayer());
                }
            }

        }


    }

    @EventHandler
    public void playerDamageEvent(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (map.containsKey(e.getEntity().getName())) {
                if (map.get(e.getEntity().getName())) {
                    playerCancelCommand((Player) e.getEntity());
                }
            }
        }

    }

    @EventHandler
    public void lecturn(final PlayerInteractEvent e){
        Player p = e.getPlayer();

        if (!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;

        if (!(e.getClickedBlock().getX() == 0 && e.getClickedBlock().getY() == 64 && e.getClickedBlock().getZ() == -6))
            return;

        if (p.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK)
            return;

        e.setCancelled(true);
        Bukkit.getServer().dispatchCommand(
                Bukkit.getConsoleSender(),
                "ibooks open examplebook " + p.getName());

    }

//    @EventHandler
//    public void magicStickRightClick(final PlayerInteractEvent e){
//        Player p = e.getPlayer();
//
//        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR)))
//            return;
//
//        if (!(p.getInventory().getItemInMainHand().isSimilar(magicFireStick)))
//            return;
//
//        if (p.getLevel() < 2){
//            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1.0f,1.0f);
//            p.sendMessage("You do not have enough xp to use the fire stick");
//            return;
//        }
//
//        p.setLevel(p.getLevel()-1);
//        Location loc = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY() + 1.25, e.getPlayer().getLocation().getZ(), e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch() - 2);
//        e.getPlayer().getWorld().spawnEntity(loc, EntityType.SMALL_FIREBALL);
//        p.playSound(p.getLocation(),Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT, 1.0f,1.0f);
//    }

    public static void spawnFireworks(Location location, int amount) {
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    @EventHandler
    public void entityBigChungusExplode(final EntityExplodeEvent e) {
//            System.out.println(ChatColor.RED + "Exploded");
        for (Block b : e.blockList()) {
            if ((b.getX() <= 8 && b.getX() >= -8) && (b.getZ() <= 8 && b.getZ() >= -8)) {
//                    System.out.println(ChatColor.RED + "(" + b.getLocation().getX() + ", " + b.getLocation().getZ() + "):" + "(" + b.getX() + ", " + b.getZ() + "):" + "True");
                e.setCancelled(true);
                spawnFireworks(e.getLocation(), 1);
                e.getEntity().remove();
                break;
            }
        }
    }

//    @EventHandler
//    public void eggThrow(final PlayerEggThrowEvent e){
//        e.setHatching(false);
//
//        Location loc = e.getEgg().getLocation();
//
//        loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 10000, 0, 1, 0, 0.05);
//    }


    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {


            if (cmd.getName().equalsIgnoreCase("spawn")) {
                playerSpawnCommand((Player) sender);
            } else if (cmd.getName().equalsIgnoreCase("cancel")) {
                playerCancelCommand((Player) sender);
            } else if (cmd.getName().equalsIgnoreCase("ckick")) {
                playerKickCommand((Player) sender, args);
            } else if (cmd.getName().equalsIgnoreCase("afk")) {
                playerKickCommand((Player) sender, new String[]{args[0], "&aYou#were#kicked#for#being#&4&lAFK^&ePlease#log#back#in#when#you#are#back!"});
            } else if (cmd.getName().equalsIgnoreCase("crestart")) {
                try {
                    restartCommand();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (cmd.getName().equalsIgnoreCase("shutdown")) {
                try {
                    shutDownCommand();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        return true;
    }

    public void shutDownCommand() throws IOException {

                for (Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[Warning]: "
                            + ChatColor.YELLOW + "The Server will be closing for the night in 60 seconds! The Server will restart at 7AM tomorrow morning!");
                }


        for (int i = 0; i < 10; i++){
            int finalI = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()){
                        p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[Warning]: "
                                + ChatColor.YELLOW + "The Server will be closing for the night in " + (10 - finalI) + " seconds! The Server will restart at 7AM tomorrow morning!");
                    }
                }
            }, (20L * i) + (20L* 60));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()){
                    playerKickCommand(p,  new String[]{p.getName(), "&aThe#Server#is#&4&lShutting#Down^&ePlease#log#back#on#tomorrow!^&3The#Server#starts#at#7AM"});
                }

                String path="cmd /c start C:\\Users\\adyow\\Desktop\\Minecraft\\Servers\\BRHS\\sign.bat";
                Runtime rn=Runtime.getRuntime();
                try {
                    Process pr=rn.exec(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, (20L * 10)  + (20L* 60));

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().shutdown();
            }
        }, ((20L * 10) + 10L) + (20L* 60));
    }

    public boolean restartCommand() throws IOException {

        for (Player p : Bukkit.getOnlinePlayers()){
            playerKickCommand(p,  new String[]{p.getName(), "&aThe#Server#is#&4&lRestarting^&ePlease#log#back#in#when#the#server#come#back#online!"});
        }

        String path="cmd /c start C:\\Users\\adyow\\Desktop\\Minecraft\\Servers\\BRHS\\delay10.bat";
        Runtime rn=Runtime.getRuntime();
        Process pr=rn.exec(path);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                    Bukkit.getServer().shutdown();
            }
        }, 20L);

        return false;
    }

    public boolean playerKickCommand(final Player p, final String[] args) {

        String message = args[1];

        message = message.replace('&','§');
        message = message.replace('#', ' ');
        message = message.replace('^', '\n');

        // /ckick wowkster &aYou#were#kicked#for#being#&4&lAFK^&ePlease#log#back#in#when#you#are#back!

        Bukkit.getServer().dispatchCommand(
                Bukkit.getConsoleSender(),
                "kick " + args[0] + " " + message);
        return false;

    }

    public boolean playerCancelCommand(final Player p) {

        map.put(p.getName(), false);
        sendMessage(p, "[\"\",{\"text\":\"Teleport \",\"bold\":true,\"color\":\"red\"},{\"text\":\"cancled.\",\"color\":\"red\"}]");

        return false;

    }

    public boolean playerSpawnCommand(final Player p) {

        if (!p.hasPermission("sc.spawn")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to use that command");
            return false;
        }

        if (!map.containsKey(p.getName())) {
            map.put(p.getName(), false);
        }

        if (!map.get(p.getName())) {
            map.put(p.getName(), true);

            sendMessage(p, "[\"\",{\"text\":\"Teleporting\",\"bold\":true,\"color\":\"green\"},{\"text\":\" in 5 seconds. \",\"color\":\"green\"},{\"text\":\"Click\",\"underlined\":true,\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/cancel\"}},{\"text\":\" to cancel...\",\"color\":\"green\"}]");

            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    if (map.get(p.getName()))
                        sendMessage(p, "[\"\",{\"text\":\"Teleporting \",\"bold\":true,\"color\":\"red\"},{\"text\":\"in 3 seconds...\",\"color\":\"red\"}]");
                }
            }, 60L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    if (map.get(p.getName()))
                        sendMessage(p, "[\"\",{\"text\":\"Teleporting \",\"bold\":true,\"color\":\"red\"},{\"text\":\"in 2 seconds...\",\"color\":\"red\"}]");
                }
            }, 80L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    if (map.get(p.getName()))
                        sendMessage(p, "[\"\",{\"text\":\"Teleporting \",\"bold\":true,\"color\":\"red\"},{\"text\":\"in 1 second...\",\"color\":\"red\"}]");
                }
            }, 100L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    if (map.get(p.getName()))
                        p.teleport(new Location(Bukkit.getWorld("world"), 0, 65, 0, 90, 0));
                }
            }, 120L);


        }
        return false;
    }

    public void sendMessage(Player player, String message) {
        Bukkit.getServer().dispatchCommand(
                Bukkit.getConsoleSender(),
                "tellraw " + player.getName() + " " + message);
    }
//    private void initMagicStick() {
//        magicFireStick = new ItemStack(Material.BLAZE_ROD);
//        ItemMeta itemMeta = magicFireStick.getItemMeta();
//        assert itemMeta != null;
//        itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Magic Fire Stick");
//        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 5, true);
//        List<String> lore = new ArrayList<>();
//        lore.add("\"Ooh That's Hot.");
//        lore.add("That's Hot.\"");
//        lore.add("-Will Smith");
//        itemMeta.setLore(lore);
//        magicFireStick.setItemMeta(itemMeta);
//
//        NamespacedKey magicFireStickKey = new NamespacedKey((Plugin) this, "magic_fire_stick");
//        magicFireStickRecipe = new ShapedRecipe(magicFireStickKey, magicFireStick);
//
//        magicFireStickRecipe.shape(
//                "FDF",
//                "DBD",
//                "FDF");
//        magicFireStickRecipe.setIngredient('F', Material.FIRE_CHARGE);
//        magicFireStickRecipe.setIngredient('D', Material.DIAMOND);
//        magicFireStickRecipe.setIngredient('B', Material.BLAZE_ROD);
//        Bukkit.addRecipe(magicFireStickRecipe);
//    }
//
//    public void initTearGasFireWork() {
//        fwi = new ItemStack(Material.FIREWORK_ROCKET);
//        fwm = (FireworkMeta)fwi.getItemMeta();
//
//        fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).with(FireworkEffect.Type.BALL_LARGE).build());
//        fwm.setUnbreakable(true);
//        fwm.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Tear Gas Grenade");
//
//        fwi.setItemMeta(fwm);
//
//        NamespacedKey magicFireStickKey = new NamespacedKey((Plugin) this, "tear_gas_firework");
//        fwir = new ShapedRecipe(magicFireStickKey, fwi);
//
//        fwir.shape(
//                "FD ",
//                "   ",
//                "   ");
//        fwir.setIngredient('F', Material.FIREWORK_ROCKET);
//        fwir.setIngredient('D', Material.DIAMOND);
//        Bukkit.addRecipe(fwir);
//    }
//
}
//
