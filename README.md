Minecraft-Bukkit-Circuit
========================

Ziel
----

Einfache kleine Schaltkreise sollen vordefiniert sein, um bestimmtes Verhalten leichter zu erreichen.
Alles kann natürlich mit entsprechenden Redstone Schaltungen erreicht werden. DIese sind allerdings sehr
groß und unheimlich schwer aufzubauen.

Sluice
------
Ein Steuerschaltkreis für Schleusen.

Einfache Schleuse:
- 2 Eingäng für das Signal zum Wechseln des Schleusenzustandes
  - für Tor 1 zum Antriggern des Zustandswechsel
  - für Tor 2 zum Antriggern des Zustandswechsel
- 1 Eingang für Reset (Tor1 auf, Tor2 zu --> niedriges Niveau)
- 3 Ausgänge 
  - Steuersignal für Tor 1 (niedriges Niveau)
  - Steuersignal für Tor 2 (höheres Niveau)
  - Steuersignal für die Pumpe 

