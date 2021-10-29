--question 3
SELECT DISTINCT tarif.tarif_hebdo, tarif.tarif_jour
FROM vehicule
         INNER JOIN categorie ON categorie.code_categ = vehicule.code_categ
         INNER JOIN tarif ON tarif.code_tarif = categorie.code_tarif
WHERE VEHICULE.MODELE = 'saxo1.1';