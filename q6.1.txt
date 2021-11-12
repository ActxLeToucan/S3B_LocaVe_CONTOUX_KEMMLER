CREATE OR REPLACE TRIGGER tr_maj_km_vehicule AFTER
UPDATE ON dossier
FOR EACH ROW
DECLARE
    v_ancienKm vehicule.kilometres%TYPE;
    ex_kmInvalid EXCEPTION;
BEGIN
    IF :new.kil_retour IS NOT NULL THEN
        SELECT kilometres INTO v_ancienKm FROM vehicule WHERE no_imm = :new.no_imm;
        IF :new.kil_retour < v_ancienKm THEN
            RAISE ex_kmInvalid;
        END IF;
        UPDATE vehicule SET kilometres = :new.kil_retour WHERE no_imm = :new.no_imm;
    END IF;
EXCEPTION
    WHEN ex_kmInvalid THEN
        DBMS_OUTPUT.PUT_LINE('Le nouveau kilometrage du véhicule ne peut pas être plus bas.');
END;


-- test du trigger ci-dessus
UPDATE DOSSIER set KIL_RETOUR = 99 where NO_DOSSIER = 10;

select KILOMETRES from VEHICULE where NO_IMM = '1234ya54';
select KIL_RETOUR from DOSSIER where NO_DOSSIER = 10;