Minecraft-Bukkit-Circuit
========================

Ziel
----

Einfache kleine Schaltkreise sollen vordefiniert sein, um bestimmtes Verhalten leichter zu erreichen.
Alles kann natürlich mit entsprechenden Redstone Schaltungen erreicht werden. Diese sind allerdings sehr
groß und unheimlich schwer aufzubauen.
Im Wiki sind die Schaltkreise erklärt.

https://github.com/anhalter42/Minecraft-Bukkit-Circuit/wiki

Anforderungen
-------------

Es wird das Plugin Minecraft-Bukkit-Mahn42-Framework benötigt:

https://github.com/Mahn42/Minecraft-Bukkit-Mahn42-Framework

Zeichenerklärung
----------------

Aufbau
  - B = schwarze Wolle
  - S = Schild mit Typenbezeichnung in der ersten Zeile

42M1000 (Schleusenschaltkreis)
------

Ein Steuerschaltkreis für Schleusen.

Aufbau:
  8 7 6 5
  BBBBBBB
  SBBBBBB
  1 2 3 4

Pinbelegung:
- 2 Eingänge für das Signal zum Wechseln des Schleusenzustandes
  - Pin 1 für Tor 1 zum Antriggern des Zustandswechsel (Zustand 2 --> Zustand 1)
  - Pin 2 für Tor 2 zum Antriggern des Zustandswechsel (Zustand 1 --> Zustand 2)
- Pin 3 für Reset (Zustand 1 --> niedriges Niveau)
- Pin 4 Masse
- 3 Ausgänge 
  - Pin 5 Steuersignal für Tor 1 (niedriges Niveau)
  - Pin 6 Steuersignal für die Pumpe 
  - Pin 7 Steuersignal für Tor 2 (höheres Niveau)
- Pin 8 +Spannung

Funktionsweise:
- 2 Hauptzustände 
- Zustand 1 (Tor 1 auf, Becken leer gepumpt und Tor 2 zu)
- Zustand 2 (Tor 1 zu, Becken voll gepumpt und Tor 2 auf)
- Wechsel von 1 nach 2
  - Tor 1 schliessen --> Becken voll pumpen --> Tor 2 öffnen
- Wechsel von 2 nach 1
  - Tor 2 schliessen --> Becken leer pumpen --> Tor 1 öffnen
