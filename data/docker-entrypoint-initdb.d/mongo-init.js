db = db.getSiblingDB('patientnotes');

db.createCollection("note");

db.note.insertMany([
    {
        patientId: "3",
        lastName: "TestInDanger",
        noteText: "Le patient déclare qu'il fume depuis peu",
        date: "2004-06-18"
    },
    {
        patientId: "3",
        lastName: "TestInDanger",
        noteText: "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé",
        date: "2004-06-18"
    },
    {
        patientId: "4",
        lastName: "TestEarlyOnset",
        noteText: "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments",
        date: "2002-06-28"
    },
    {
        patientId: "4",
        lastName: "TestEarlyOnset",
        noteText: "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps",
        date: "2002-06-28"
    },
    {
        patientId: "4",
        lastName: "TestEarlyOnset",
        noteText: "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé",
        date: "2002-06-28"
    },
    {
        patientId: "4",
        lastName: "TestEarlyOnset",
        noteText: "Taille, Poids, Cholestérol, Vertige et Réaction",
        date: "2002-06-28"
    },
    {
        patientId: "1",
        lastName: "TestNone",
        noteText: "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé",
        date: "2024-12-01"
    },
    {
        patientId: "2",
        lastName: "TestBorderline",
        noteText: "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement",
        date: "2024-12-02"
    },
    {
        patientId: "2",
        lastName: "TestBorderline",
        noteText: "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale",
        date: "2024-12-03"
    }
]);
