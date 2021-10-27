--question 1
SELECT DISTINCT vehicule.no_imm, vehicule.modele
FROM vehicule
         INNER JOIN dossier ON vehicule.no_imm = dossier.no_imm
WHERE code_categ = 'c1'
MINUS
SELECT DISTINCT vehicule.no_imm, vehicule.modele
FROM vehicule
         INNER JOIN dossier ON vehicule.no_imm = dossier.no_imm
WHERE code_categ = 'c1'
  AND ((date_retrait <= to_date('01-10-2015', 'DD-MM-YYYY') AND date_retour >= to_date('01-10-2015', 'DD-MM-YYYY'))
    OR (date_retrait <= to_date('07-10-2015', 'DD-MM-YYYY') AND date_retour >= to_date('07-10-2015', 'DD-MM-YYYY')));


/*
NO_IMM,MODELE
6213yd54,twingo
*/