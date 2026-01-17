package com.daytonjwatson.mcsr.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

public class WorldManager {

    public static World copyAndLoadWorld(JavaPlugin plugin, String templateFolderName, String worldName) throws IOException {
        // Source: plugins/YourPlugin/world_templates/<templateFolderName>
        File source = new File(plugin.getDataFolder(), "world_templates" + File.separator + templateFolderName);
        if (!source.exists() || !source.isDirectory()) {
            throw new IOException("Template world folder not found: " + source.getAbsolutePath());
        }

        File worldContainer = Bukkit.getWorldContainer();
        File target = new File(worldContainer, worldName);

        // If target exists, you must decide what to do (delete, overwrite, or abort)
        if (target.exists()) {
            throw new IOException("Target world already exists: " + target.getAbsolutePath());
        }

        copyDirectory(source.toPath(), target.toPath(), path ->
                shouldSkip(path.getFileName().toString())
        );

        // IMPORTANT: ensure uid.dat exists in the copied world or delete it;
        // commonly you want to delete uid.dat so Spigot regenerates a unique UID.
        File uidDat = new File(target, "uid.dat");
        if (uidDat.exists()) uidDat.delete();

        // Now load
        WorldCreator wc = new WorldCreator(worldName);
        // Optional: wc.environment(World.Environment.NORMAL); wc.type(WorldType.NORMAL); wc.generateStructures(true);
        World world = Bukkit.createWorld(wc);

        if (world == null) {
            throw new IOException("Failed to load world: " + worldName);
        }

        return world;
    }

    public static boolean shouldSkip(String name) {
        // session.lock is fine to skip; some templates might have caches you don't want.
        // You usually DO want region/, level.dat, data/, DIM-1, DIM1 etc.
        return name.equalsIgnoreCase("session.lock");
    }

    public static void copyDirectory(Path source, Path target, java.util.function.Predicate<Path> skipPredicate) throws IOException {
        Files.walk(source).forEach(from -> {
            try {
                Path rel = source.relativize(from);
                if (rel.toString().isEmpty()) return;

                if (skipPredicate.test(from)) return;

                Path to = target.resolve(rel);
                if (Files.isDirectory(from)) {
                    Files.createDirectories(to);
                } else {
                    Files.createDirectories(to.getParent());
                    Files.copy(from, to, StandardCopyOption.COPY_ATTRIBUTES);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Optional helper if you want delete support later
    public static void deleteWorldFolder(Path worldPath) throws IOException {
        if (!Files.exists(worldPath)) return;
        Files.walk(worldPath)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException ignored) {}
                });
    }
}