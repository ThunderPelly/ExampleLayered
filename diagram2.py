import matplotlib.pyplot as plt

# Daten für die Grafiken
metriken = ['LOC', 'Cyclomatische Komplexität', 'Cognitive Komplexität', 'Testabdeckung']
referenz_schichten = [524, 42, 9, 93.5]
messung_schichten = [596, 48, 10, 86.8]
veraenderung_schichten = [13.74, 14.28, 11.11, -6.68]

referenz_hexagonal = [714, 55, 11, 94.5]
messung_hexagonal = [763, 60, 12, 90.3]
veraenderung_hexagonal = [6.86, 9.09, 9.09, -4.21]

# Funktion zur Erstellung von Balkendiagrammen
def erstelle_balkendiagramm(x, y1, y2, y1_label, y2_label, titel, x_label, y_label):
    fig, ax = plt.subplots()
    breite = 0.35
    ind = range(len(x))

    # Erstellen Sie ein Balkendiagramm für y1
    bars1 = ax.bar([i - breite/2 for i in ind], y1, breite, label=y1_label, color='#f8cc13')

    # Erstellen Sie ein Balkendiagramm für y2
    bars2 = ax.bar([i + breite/2 for i in ind], y2, breite, label=y2_label, color='#df5111')

    # Schreiben Sie die Werte in die Balken für y1
    for bar in bars1:
        height = bar.get_height()
        ax.text(bar.get_x() + bar.get_width()/2, height, str(height), ha='center', va='bottom')

    # Schreiben Sie die Werte in die Balken für y2
    for bar in bars2:
        height = bar.get_height()
        ax.text(bar.get_x() + bar.get_width()/2, height, str(height), ha='center', va='bottom')


    ax.set_title(titel)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.set_xticks(ind)
    ax.set_xticklabels(x)
    ax.legend()


erstelle_balkendiagramm(metriken, referenz_schichten, referenz_hexagonal, 'Schichten Referenz', 'Hexagonal Referenz',
                         'Vergleich der Metriken zwischen Schichten- und Hexagonal-Architektur (Referenzwerte)',
                         'Metrik', 'Wert')
plt.savefig('vergleich_referenzwerte.png', transparent=True)  # Speichern Sie das Diagramm als PNG

erstelle_balkendiagramm(metriken, messung_schichten, messung_hexagonal, 'Schichten Messung', 'Hexagonal Messung',
                         'Vergleich der Metriken zwischen Schichten- und Hexagonal-Architektur (Messungen)',
                         'Metrik', 'Wert')
plt.savefig('vergleich_messungen.png', transparent=True)  # Speichern Sie das Diagramm als PNG

erstelle_balkendiagramm(metriken, veraenderung_schichten, veraenderung_hexagonal, 'Schichten-Architektur', 'Hexagonal-Architektur',
                         'Prozentuale Veränderung der Metriken zwischen Schichten- und Hexagonal-Architektur',
                         'Metrik', 'Prozentuale Veränderung')
plt.gcf().set_size_inches(10, 6)  # Setze die Größe des Diagramms auf 10x6 Zoll
plt.savefig('vergleich_veraenderung.png', transparent=True)  # Speichern Sie das Diagramm als PNG

plt.show()


