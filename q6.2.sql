CREATE TABLE tableAudit (
  no_dossier NUMBER(6),
  "date" DATE NOT NULL,
  nom VARCHAR2(40) NOT NULL,
  marque VARCHAR2(20) NOT NULL,
  modele VARCHAR2(20) NOT NULL,
  PRIMARY KEY (no_dossier),
  CONSTRAINT fk_audit FOREIGN KEY (no_dossier) REFERENCES dossier(no_dossier)
);

CREATE OR REPLACE TRIGGER tr_audit AFTER
INSERT ON dossier
FOR EACH ROW
DECLARE
    v_date DATE;
    v_nom client.nom%TYPE;
    v_marque vehicule.marque%TYPE;
    v_modele vehicule.modele%TYPE;
BEGIN
    SELECT sysdate INTO v_date FROM DUAL;
    SELECT nom INTO v_nom FROM client WHERE code_cli = :new.code_cli;
    SELECT marque INTO v_marque FROM vehicule WHERE no_imm = :new.no_imm;
    SELECT modele INTO v_modele FROM vehicule WHERE no_imm = :new.no_imm;

    IF :new.date_retour - :new.date_retrait > 7 THEN
            INSERT INTO tableAudit VALUES (:new.no_dossier,v_date,v_nom,v_marque,v_modele);
    END IF;
END;

-- test du trigger
insert into DOSSIER values (21, to_date('20-12-2020', 'DD-MM-YYYY'), to_date('31-12-2020', 'DD-MM-YYYY'),null, null, null, 't1', 'x', null, null, null, 'roule001', '1234ya54', 'Nancy', 'Nancy', 'Nancy');
select * from tableAudit;
select * from DOSSIER;