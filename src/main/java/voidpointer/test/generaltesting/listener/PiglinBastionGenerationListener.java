package voidpointer.test.generaltesting.listener;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.AsyncStructureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.bukkit.generator.structure.Structure.BASTION_REMNANT;

public final class PiglinBastionGenerationListener implements Listener {
    private final Logger log;

    public PiglinBastionGenerationListener(final Logger log) {
        this.log = log;
    }


    public void register(final Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority=EventPriority.MONITOR) /* we need to be 100% sure it isn't cancelled */
    public void equipPiglinsOnBastionGeneration(final AsyncStructureSpawnEvent structureSpawnEvent) {
        log.debug("[{}] chunk ({};{}), corners ({}) ({}), gen:{}",
                structureSpawnEvent.getStructure().getKey().getKey(),
                structureSpawnEvent.getChunkX(), structureSpawnEvent.getChunkZ(),
                structureSpawnEvent.getBoundingBox().getMin(), structureSpawnEvent.getBoundingBox().getMax(),
                structureSpawnEvent.getWorld().isChunkGenerated(structureSpawnEvent.getChunkX(),
                        structureSpawnEvent.getChunkZ()));

        if (structureSpawnEvent.isCancelled() || (BASTION_REMNANT != structureSpawnEvent.getStructure()))
            return;

        final World world = structureSpawnEvent.getWorld();
        final BoundingBox bb = structureSpawnEvent.getBoundingBox();
        final Collection<Vector> structureChunks = getChunksOf(bb);

        for (final Vector structureChunk : structureChunks) {
            world.getChunkAtAsync(structureChunk.getBlockX(), structureChunk.getBlockZ()).thenAccept(chunk -> {
                for (final Entity entity : chunk.getEntities()) {
                    log.debug("[{} {}] ({})", entity.getClass().getSimpleName(), entity.getUniqueId(),
                            entity.getLocation().toVector());
                    if (!(entity instanceof PiglinAbstract piglin) || !bb.contains(piglin.getLocation().toVector()))
                        continue;
                    updatePiglinEquipment(piglin);
                    log.debug("[{} {}] updated", entity.getClass().getSimpleName(), entity.getUniqueId());
                }
            });
        }
    }

    private Collection<Vector> getChunksOf(final BoundingBox boundingBox) {
        final Vector min = boundingBox.getMin();
        final Vector max = boundingBox.getMax();
        final Set<Vector> chunks = new HashSet<>();
        for (int z = min.getBlockZ(); z < max.getBlockZ(); z += 16)
            for (int x = min.getBlockX(); x <= max.getBlockX(); x += 16)
                chunks.add(new Vector(x >> 4, 0, z >> 4));
        return chunks;
    }

    private void updatePiglinEquipment(final PiglinAbstract piglin) {
        EntityEquipment piglinEquipment = piglin.getEquipment();
        piglinEquipment.setArmorContents(generateNetheriteArmor());
        piglinEquipment.setItemInMainHand(generateNetheriteAxe());
    }

    private ItemStack[] generateNetheriteArmor() {
        return new ItemStack[] {
                new ItemStack(Material.NETHERITE_BOOTS),
                new ItemStack(Material.NETHERITE_LEGGINGS),
                new ItemStack(Material.NETHERITE_CHESTPLATE),
                new ItemStack(Material.NETHERITE_HELMET),
        };
    }

    private ItemStack generateNetheriteAxe() {
        return new ItemStack(Material.NETHERITE_AXE);
    }
}
