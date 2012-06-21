Minecraft-Bukkit-Circuit
========================

Ziel
----

Einfache kleine Schaltkreise sollen vordefiniert sein, um bestimmtes Verhalten leichter zu erreichen.
Alles kann natürlich mit entsprechenden Redstone Schaltungen erreicht werden. DIese sind allerdings sehr
groß und unheimlich schwer aufzubauen.

42M1000 (Schleusenschaltkreis)
------

Ein Steuerschaltkreis für Schleusen.

Pinbelegung:
- 2 Eingängw für das Signal zum Wechseln des Schleusenzustandes
  - für Tor 1 zum Antriggern des Zustandswechsel (Zustand 2 --> Zustand 1)
  - für Tor 2 zum Antriggern des Zustandswechsel (Zustand 1 --> Zustand 2)
- 1 Eingang für Reset (Zustand 1 --> niedriges Niveau)
- 3 Ausgänge 
  - Steuersignal für Tor 1 (niedriges Niveau)
  - Steuersignal für Tor 2 (höheres Niveau)
  - Steuersignal für die Pumpe 

Funktionsweise:
- 2 Hauptzustände 
  - Zustand 1 (Tor 1 auf, Becken leer gepumpt und Tor 2 zu)
  - Zustand 2 (Tor 1 zu, Becken voll gepumpt und Tor 2 auf)
  - Wechsel von 1 nach 2
    - Tor 1 schliessen --> Becken voll pumpen --> Tor 2 öffnen
  - Wechsel von 2 nach 1
    - Tor 2 schliessen --> Becken leer pumpen --> Tor 1 öffnen
