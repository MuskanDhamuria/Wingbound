package wingbound.registry;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import wingbound.Wingbound;
import wingbound.items.BabyFireDragonSpawnEggItem;
import wingbound.items.FireDragonEggItem;

public final class ModItems {
	public static Item FIRE_DRAGON_EGG;
	public static Item BABY_FIRE_DRAGON_SPAWN_EGG;

	private ModItems() {
	}

	public static void register() {
		FIRE_DRAGON_EGG = registerItem("fire_dragon_egg", FireDragonEggItem::new);
		BABY_FIRE_DRAGON_SPAWN_EGG = registerItem("baby_fire_dragon_spawn_egg", BabyFireDragonSpawnEggItem::new);
	}

	private static Item registerItem(String name, Function<Item.Properties, Item> factory) {
		Identifier id = Wingbound.id(name);
		Item.Properties properties = new Item.Properties()
			.setId(ResourceKey.create(Registries.ITEM, id))
			.stacksTo(16);
		Item item = factory.apply(properties);
		return Registry.register(BuiltInRegistries.ITEM, id, item);
	}
}
