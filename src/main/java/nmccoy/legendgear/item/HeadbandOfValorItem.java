package nmccoy.legendgear.item;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import nmccoy.legendgear.mixin.ArmorItemAccessor;

public class HeadbandOfValorItem extends ArmorItem {

	public static final ArmorMaterial MATERIAL = new ArmorMaterial() {

		@Override
		public int getDurability(Type type) {
			return 155;
		}

		@Override
		public int getEnchantability() {
			return 1; // Free enchanting
		}

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
		}

		@Override
		public float getKnockbackResistance() {
			return 0;
		}

		@Override
		public String getName() {
			return "headband_of_valor";
		}

		@Override
		public int getProtection(Type type) {
			return 1;
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
	
	public HeadbandOfValorItem(Settings settings) {
		super(MATERIAL, Type.HELMET, settings);
		
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getAttributeModifiers(EquipmentSlot.HEAD);
		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		
		UUID uuid = ArmorItemAccessor.getModifiers().get(type);
		
		// Keep existing modifiers
		modifiers.forEach((attribute, modifier) -> {
			builder.put(attribute, modifier);
		});
		
		builder.put(
				EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(
						uuid, 
						"Attack modifier", 
						(double) 4, 
						Operation.ADDITION)
				);

		allAttributeModifiers = builder.build();
	}
	
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == type.getEquipmentSlot() ? allAttributeModifiers : super.getAttributeModifiers(slot);
	}

}
