# Gomoku-IA
Le but de ce projet est de réaliser une IA de Gomoku basée sur l'algorithme MINIMAX avec élagage Alpha Beta.
@Authors : @C0ulday @Balla-leye

- Version 1 : IA Capable de jouer selon une profondeur donnée contre le joueur Humain
- Version 2 : IA + Deep Learning c.à.d : enregistrer l'évaluation des tables dans des fichiers textes pour ne pas avoir à réévaluer la table à chaque fois et ainsi être plus rapide : Enregistrement des évaluations dans plusieurs fichiers sous des noms respectant ce schéma : "firstRow_firstCol_p1_p2_p3_p4_p5_profondeur.txt";
- Version 3 : Lancer l'IA en étant vapable de modifier les paramètres de poids des cases
- Version 4 : Faire affronter 2 IAs, afin de déterminer la meilleure pondération

Le programme a été réalisé pour un plateau de taille 15 x 15.
Toues les fichiers textes contenant les ID de plateau et leurs évaluations sont par défaut dans le répertoire eval_txt/.

Le plateau a été pondéré comme suit :
<img width="362" alt="Capture d’écran 2024-10-16 à 21 02 21" src="https://github.com/user-attachments/assets/fda2bd8e-1936-4e31-8e96-88cf2585d742">
