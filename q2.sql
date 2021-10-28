--question 2
CREATE OR REPLACE PROCEDURE proc_majCalendrier(p_datedebut DATE, p_datefin DATE, p_immatriculation VARCHAR2)
IS
    v_dateactu DATE;
    v_dispo BOOLEAN;
    v_ancienneVal calendrier.paslibre%TYPE;
    r_reservation dossier%ROWTYPE;
    CURSOR c_reservations (p_immatriculation VARCHAR2) IS SELECT * FROM dossier WHERE no_imm=p_immatriculation;
BEGIN
    v_dateactu := p_datedebut;
    WHILE (v_dateactu <= p_datefin) LOOP
        DBMS_OUTPUT.PUT_LINE('date acteulle : '||to_char(v_dateactu, 'DD-MM-YYYY'));
        v_dispo := true;
        OPEN c_reservations (p_immatriculation);
        LOOP
            FETCH c_reservations INTO r_reservation;
            EXIT WHEN c_reservations%notfound;
            IF (r_reservation.DATE_RETRAIT <= v_dateactu AND v_dateactu <= r_reservation.DATE_RETOUR) THEN
                v_dispo := false;
            END IF;
        END LOOP;
        CLOSE c_reservations;

        SELECT paslibre into v_ancienneVal FROM calendrier WHERE NO_IMM = p_immatriculation AND DATEJOUR = v_dateactu;
        IF (v_dispo) THEN
            UPDATE calendrier SET paslibre = null WHERE datejour = v_dateactu;
            IF (v_ancienneVal IS NOT NULL) THEN
                DBMS_OUTPUT.PUT_LINE('modifié de x à null !');
            END IF;
        ELSE
            UPDATE calendrier SET paslibre = 'x' WHERE datejour = v_dateactu;
            IF (v_ancienneVal IS NULL) THEN
                DBMS_OUTPUT.PUT_LINE('modifié de null à x !');
            END IF;
        END IF;
        v_dateactu := v_dateactu + 1;
    END LOOP;
END;


--teste de la procedure
BEGIN
    proc_majCalendrier(to_date('01-10-2015','DD-MM-YYYY'),to_date('15-10-2015','DD-MM-YYYY'),'1234ya54');
END;

--modif d'une valeur pour tester la porcedure apres
UPDATE calendrier SET paslibre = NULL WHERE DATEJOUR = to_date('11-10-2015','DD-MM-YYYY') AND NO_IMM = '1234ya54';