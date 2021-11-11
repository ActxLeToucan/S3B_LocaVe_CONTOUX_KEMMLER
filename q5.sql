CREATE OR REPLACE FUNCTION f_clientPlusieursModeles RETURN VARCHAR2
IS
    v_clients VARCHAR2(1000);
    r_client client%ROWTYPE;
    v_nbModeles NUMBER(3,0);
    v_i NUMBER(3,0);
    CURSOR c_client IS SELECT * FROM client;
BEGIN
    v_clients := 'Les clients suivants ont loué au moins deux modèles différents :';
    v_i := 0;
    OPEN c_client;
    LOOP
        FETCH c_client INTO r_client;
        EXIT WHEN c_client%notfound;
        SELECT count(DISTINCT modele) INTO v_nbModeles
        FROM dossier INNER JOIN vehicule on vehicule.NO_IMM = dossier.NO_IMM WHERE code_cli = r_client.code_cli;
        IF (v_nbModeles >= 2) THEN
            v_i := v_i + 1;
            v_clients := v_clients||CHR(9)||CHR(10)||r_client.nom||', résidant à '||r_client.ville||' ('||r_client.codpostal||')';
        END IF;
    END LOOP;
    CLOSE c_client;
    IF (v_i = 0) THEN
        v_clients := 'Aucun client n a loué au moins deux modèles différents.';
    END IF;
    RETURN v_clients;
END;

--teste la fonction ci-dessus
BEGIN
    DBMS_OUTPUT.PUT_LINE(f_clientPlusieursModeles());
END;