def create_files_with_text(string_list, template):
    try:
        for i, string_content in enumerate(string_list):
            filename = f'{string_content}.json'
            
            # Datei öffnen und Text einfügen
            with open(filename, 'w') as file:
                file.write(template.replace("%id%", string_content))
                
            print(f'Datei {filename} erstellt und Text eingefügt.')
            
    except Exception as e:
        print(f'Fehler beim Erstellen der Dateien: {e}')

# Beispielaufruf des Skripts
liste_von_strings = ["heart", "aero_amulet", "geo_amulet", "pyro_amulet", "earth_medallion", "ender_medallion", "wind_medallion", "headband_of_valor", "titan_band", "whirlwind_boots", "hookshot", "quiver", "bomb", "bomb_bag"]
text_einzufuegen = r"""{
	"parent": "minecraft:item/generated",
	"textures": {
		"layer0": "legendgear:item/%id%"
	}
}"""

create_files_with_text(liste_von_strings, text_einzufuegen)