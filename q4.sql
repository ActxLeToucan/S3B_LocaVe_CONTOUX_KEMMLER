--question 4
CREATE OR REPLACE FUNCTION f_agenceAvecToutesCateg RETURN VARCHAR2
IS
    v_agences VARCHAR2(200);
    r_agence agence%ROWTYPE;
    v_nbCateg NUMBER(3,0);
    v_nbCatAgence NUMBER(3,0);
    v_i NUMBER (3,0);
    CURSOR c_agences IS SELECT * FROM agence;
BEGIN
    v_agences := 'Les agences suivantes possèdent toutes les catégories de véhicule :';
    SELECT count(*) INTO v_nbCateg FROM categorie;
    OPEN c_agences;
    LOOP
        FETCH c_agences INTO r_agence;
        EXIT WHEN c_agences%notfound;
        SELECT count(DISTINCT code_categ) INTO v_nbCatAgence FROM vehicule WHERE code_ag = r_agence.code_ag;
        IF (v_nbCatAgence = v_nbCateg) THEN
            v_i := v_i + 1;
            v_agences := v_agences||CHR(9) || CHR(10)||r_agence.code_ag;
        END IF;
    END LOOP;
    CLOSE c_agences;
    IF (v_i = 0) THEN
        v_agences := 'Aucune agence ne possède toutes les catégories de véhicule.';
    END IF;
    RETURN v_agences;
END;

--teste la fonction ci-dessus
BEGIN
    DBMS_OUTPUT.PUT_LINE(f_agenceAvecToutesCateg());
END;