package nmccoy.legendgear.item;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.item.Item.Settings;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import nmccoy.legendgear.mixin.ArmorItemAccessor;

public class WhirlwindBoots extends ArmorItem {

	public static final ArmorMaterial MATERIAL = new ArmorMaterial() {

		@Override
		public int getDurability(Type type) {
			return 215;
		}

		@Override
		public int getEnchantability() {
			return 1; // Free enchanting
		}

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
		}

		@Override
		public float getKnockbackResistance() {
			return 0;
		}

		@Override
		public String getName() {
			return "whirlwind_boots";
		}

		@Override
		public int getProtection(Type type) {
			return 2;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return null;
		}

		@Override
		public float getToughness() {
			return 0;
		}
		
	};
	
	
	private final Multimap<EntityAttribute, EntityAttributeModifier> allAttributeModifiers;
	
	public WhirlwindBoots(Settings settings) {
		super(MATERIAL, Type.BOOTS, settings);
		
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getAttributeModifiers(EquipmentSlot.FEET);
		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		
		UUID uuid = ArmorItemAccessor.getModifiers().get(type);
		
		// Keep existing modifiers
		modifiers.forEach((attribute, modifier) -> {
			builder.put(attribute, modifier);
		});
		
		builder.put(
				EntityAttributes.GENERIC_MOVEMENT_SPEED,
				new EntityAttributeModifier(
						uuid, 
						"Speed modifier", 
						(double) 0.2, 
						Operation.ADDITION)
				);

		allAttributeModifiers = builder.build();
	}
	
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == type.getEquipmentSlot() ? allAttributeModifiers : super.getAttributeModifiers(slot);
	}

}
