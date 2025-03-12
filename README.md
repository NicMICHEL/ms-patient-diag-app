# Micro Diag App

Une application web sécurisée permettant aux médecins de :
  - consulter les données personnelles des patients : nom, prénom, genre, date de naissance, adresse et numéro
de téléphone (l'adresse et le numéro de téléphone étant facultatifs).
  - mettre à jour l'adresse et le numéro de téléphone. 
  - consulter l'ensemble des notes associées au dossier patient.
  - ajouter une note.
  - connaître le niveau de risque de diabète déterminé en analysant les notes.


## La détermination du niveau de risque

Les mots suivants constituent les déclencheurs :
   - Hémoglobine A1C
   - Microalbumine
   - Taille
   - Poids
   - un parmi : Fume, Fumeur, Fumeuse, Fumer
   - un parmi : Anormal, Anormale, Anormales
   - Cholestérol
   - Vertige ou Vertiges
   - Rechute
   - Réaction
   - Anticorps

Les 4 niveaux de risque possibles :
- aucun risque (None);
- risque limité (Borderline); 
- danger (In Danger) ;
- apparition précoce (Early onset).

Les règles de détermination du niveau de risque :
- aucun risque (None) : Le dossier du patient ne contient aucune note du médecin contenant les déclencheurs;
- risque limité (Borderline) : Le dossier du patient contient entre deux et cinq déclencheurs et le patient
est âgé de plus de 30 ans ;
- danger (In Danger) : Dépend de l'âge et du sexe du patient. Si le patient est un homme de moins de 30 ans,
alors trois termes déclencheurs doivent être présents. Si le patient est une femme et a moins de 30 ans,
il faudra quatre termes déclencheurs. Si le patient a plus de 30 ans, alors il en faudra six ou sept ;
- apparition précoce (Early onset) : Encore une fois, cela dépend de l'âge et du sexe. Si le patient 
est un homme de moins de 30 ans, alors au moins cinq termes déclencheurs sont nécessaires. Si le patient 
est une femme et a moins de 30 ans, il faudra au moins sept termes déclencheurs. Si le patient a plus de 30 ans,
alors il en faudra huit ou plus.

  

## Lancement de l'application

Avant de mettre en route l'application, il faut ajouter un ficher nommé .env à la racine du projet.
Ce fichier contient les lignes suivantes (modifier les ports à votre convenance) :

    eurekaMSPort='9102' 
    
    frontMSPort='8083'
    
    gatewayMSPort='9009'
    
    patientMSPort='9001'
    
    riskMSPort='9006'
    
    notesMSPort='9005'

L'application se lance à partir du fichier docker-compose.yml
Une fois lancée, aller sur Keycloak à l'adresse http://localhost:8080/  S'identifier avec le nom d'utilisateur  admin 
et le mot de passe  admin. Se placer dans le realm microdiag-app, aller à l'onglet users / create new user.
Créer un user (username, email, firstname, lastname) et son mot de passe. 

Pour accéder à l'application, se rendre à l'adresse  http://localhost:8083/client/ 
8083 étant la valeur de la variable du port frontMSPort indiquée dans le fichier .env

Vous êtes redirigé vers le formulaire d'identification de Keycloak sur lequel vous entrez le nom d'utilisateur
et le mot de passe précédemment définis.


## Suggestions à mener pour appliquer le green code au projet

Dans la configuration actuelle de l'application, à chaque consultation d'un dossier, l'ensemble des notes associées
sont analysées afin de déterminer le niveau de risque. Pour ce faire, le service risk demande au service notes de lui
transmettre les notes puis il les analyse. Or le niveau de risque n'est susceptible de changer que lors de l'ajout
d'une nouvelle note. Une autre solution serait donc de garder (en base de données ou dans une liste clé / valeur) 
la présence ou non de chaque mot déclencheur dans les notes et le niveau de risque correspondant. Lors d'une
consultation du dossier, ces données seraient récupérées dans la base de données (ou la liste clé / valeur). Le service 
risque ne serait mobilisé que lors de l'ajout d'une nouvelle note. 