/*
TOPIC:
    Termenii cu care alegi rutina: procedura, funcția, parametrul și propoziția la care se vede efectul.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Metodele de mai jos țin propoziția gata de trimis. Nu deschid conexiunea. root@localhost a fost refuzat, using password: NO.
MECHANISM:
    explainStoredRoutine ține CREATE PROCEDURE. explainFunction ține CREATE FUNCTION. Niciuna nu este trimisă.
LOOKALIKES:
    AVG nu este funcția stocată. DELIMITER nu este propoziția. @p_salary nu este coloana salary.
BOOK THEORY:
    Manualul 9.7, 27.2, 15.1.22 și 15.2.1, deschise în tur. Stored+Routines.pdf nu conține ER_SP_NO_RETSET.
BOOK EXAMPLE:
    explainStoredRoutine
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_ROUTINES.java
BREAK LAB:
    explainFunction, dacă i se scoate RETURNS, nu mai este funcția din 15.1.22.
TRADE-OFFS:
    Procedura pentru set. Funcția pentru o valoare. Agregatul când nu vrei obiect.
ANTI-PATTERNS:
    CALL pe funcție, sau SELECT pe procedură.
STAFF QUESTIONS:
    Întrebările 1–50 din StoredRoutines_Staff_Interview.
NOVEL TRANSFER:
    Coletul mutat este procedura. Codul de bare este funcția. Contorul din sesiune este parametrul.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei rutine stocate (stored routine):
       Rutina stocată este un set de propoziții păstrate pe server. Clienții o invocă în loc să retrimită fiecare propoziție.

Exemplu:
       StoredRoutines_Terms.explainStoredRoutine()
       CREATE PROCEDURE emp_salary nu citește încă salariul.

Definiția unei proceduri (procedure):
       Procedura se pornește cu CALL. Poate trimite un set de rezultate și poate scrie mai multe valori OUT.

Exemplu:
       StoredRoutines_Terms.explainProcedure()
       CALL emp_salary(10001) este momentul în care rândurile pleacă.

Definiția unei funcții (function):
       Funcția întoarce o valoare într-o expresie. Nu se pornește cu CALL și nu trimite un set.

Exemplu:
       StoredRoutines_Terms.explainFunction()
       SELECT f_emp_avg_salary(10001) evaluează media. CREATE nu o calculează.

Definiția unui parametru (parameter):
       IN duce o valoare și nu o aduce înapoi. OUT iese în variabila de utilizator. INOUT face ambele, pe aceeași variabilă.

Exemplu:
       StoredRoutines_Terms.explainParameter()
       @p_salary trebuie să aibă o valoare înainte de INOUT. Fără ea, intrarea este NULL.

Definiția unei propoziții (statement):
       CREATE schimbă schema la sfârșitul lui. CALL sau evaluarea expresiei este propoziția la care se văd rândurile sau valoarea.

Exemplu:
       StoredRoutines_Terms.explainStatement()
       DELIMITER $$ nu este propoziția. Este comanda clientului mysql.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE termenul care spune unde stă rutina -> explainStoredRoutine -> CREATE PROCEDURE emp_salary -> nu garantează un CALL
    2. Eșecul cel mai mic
           -> DECIDE funcția fără RETURNS -> explainFunction fără clauză -> serverul nu are tipul valorii -> nu garantează eroarea în tur
    3. Granița
           -> Termenul numește mecanismul MySQL. Metoda Java doar ține șirul.
    4. Alternativa
           -> Procedura pentru set. Funcția pentru expresie. INOUT pentru aceeași variabilă.
    5. Anti-patternul
           -> DELIMITER pus în șirul pe care îl primește serverul.
    6. Dovada
           -> explainStatement nu conține DELIMITER. explainParameter inițializează @p_salary. root@localhost a fost refuzat.
    7. Interviu
           -> Rutina stă pe server. CALL pornește procedura. Funcția intră în SELECT. Parametrul nu este coloana.
*/
// Începător: glosarul stă în fast și ține propozițiile termenilor, nu un al doilea curs.
// Producție: package _11_STORED_ROUTINES.fast. Dacă linia dispare, termenii nu mai sunt lângă lecție.
// Expert: Nu redenumește copiii. emp_salary și f_emp_avg_salary rămân numele din curs.
package _11_STORED_ROUTINES.fast;

// Începător: clasa dă exemplu pentru fiecare termen, fără să creeze rutina.
// Producție: public final class StoredRoutines_Terms. Fără ea, definițiile nu au metodă.
// Expert: Nu deschide JDBC. Nu amestecă AVG cu funcția stocată.
public final class StoredRoutines_Terms {

    /*
    🟢 Începător
        Ce este            setul de propoziții păstrat pe server
        De ce e nevoie     fără el, fiecare client retrimite JOIN-ul
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.emp_no FROM employees e WHERE e.emp_no = p_emp_no; END
    🔴 Staff
        Unde               schema se schimbă la CREATE. Rândul se vede la CALL. DELIMITER nu este în șir
    Pași
        1. Ține CREATE PROCEDURE.
        2. Nu pune CALL în același șir.
        3. Nu îl trimite.
    */
    // Începător: metoda păstrează rutina, nu salariul.
    // Producție: public static String explainStoredRoutine(). Dacă dispare, termenul nu are propoziție.
    // Expert: Nu este CREATE FUNCTION. Nu cere RETURNS.
    public static String explainStoredRoutine() {
        // Începător: serverul ține minte cum se cere un angajat, iar angajatul nu este citit încă.
        // Producție: CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.emp_no FROM employees e WHERE e.emp_no = p_emp_no; END. Dacă linia dispare, rămâne doar definiția în cuvinte.
        // Expert: SELECT fără INTO este legal în procedură. Aceeași formă într-o funcție este ER_SP_NO_RETSET.
        return "CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.emp_no FROM employees e WHERE e.emp_no = p_emp_no; END";
        // Începător: metoda a înapoiat rutina.
        // Producție: acolada închide explainStoredRoutine. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă schema neschimbată. Acolada nu este CALL.
    }

    /*
    🟢 Începător
        Ce este            pornirea procedurii
        De ce e nevoie     fără CALL, CREATE pare că a și trimis rândurile
    🟡 Producție
        Cum se scrie       CALL emp_salary(10001)
    🔴 Staff
        Unde               setul se vede la sfârșitul lui CALL. Nu este SELECT emp_salary(10001)
    Pași
        1. Ține CALL cu argument.
        2. Nu îl pune pe funcție.
        3. Nu îl trimite.
    */
    // Începător: metoda păstrează apelul procedurii.
    // Producție: public static String explainProcedure(). Dacă dispare, CALL rămâne un cuvânt.
    // Expert: ROW_COUNT() după CALL privește ultima propoziție din corp, după 15.2.1.
    public static String explainProcedure() {
        // Începător: abia acum se cer rândurile angajatului 10001.
        // Producție: CALL emp_salary(10001). Dacă linia dispare, nu mai ai momentul în care se văd rândurile.
        // Expert: Argumentul este IN. Nu se întoarce în @p_emp_no. Funcția nu se pornește așa.
        return "CALL emp_salary(10001)";
        // Începător: metoda s-a oprit la apel.
        // Producție: acolada închide explainProcedure. Setul nu a fost citit.
        // Expert: Nu este CALL list_limit. Acolo nu există argument.
    }

    /*
    🟢 Începător
        Ce este            valoarea unică salvată sub un nume
        De ce e nevoie     fără RETURNS, nu este funcție
    🟡 Producție
        Cum se scrie       CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no)
    🔴 Staff
        Unde               media se vede la SELECT, nu la CREATE. Fără READS SQL DATA, cu binary logging, apare 1418
    Pași
        1. Pune RETURNS și READS SQL DATA.
        2. Nu pune CALL.
        3. Nu o trimite.
    */
    // Începător: metoda păstrează funcția care intră într-un SELECT.
    // Producție: public static String explainFunction(). Dacă dispare, vecinul procedurii nu are propoziție.
    // Expert: Parametrul este IN. Nu există OUT. AVG din corp este agregatul, nu obiectul.
    public static String explainFunction() {
        // Începător: serverul ține minte media unui angajat și o dă ca pe o valoare, nu ca pe un tabel.
        // Producție: CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no). Dacă linia dispare, rămâne doar cuvântul funcție.
        // Expert: NO SQL nu stă lângă READS SQL DATA. SELECT f_emp_avg_salary(10001) este invocarea. CALL nu este.
        return "CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no)";
        // Începător: metoda a înapoiat funcția, nu media.
        // Producție: acolada închide explainFunction. Funcția nu a fost creată.
        // Expert: 1418 nu a fost citit. Acolada nu setează log_bin_trust_function_creators.
    }

    /*
    🟢 Începător
        Ce este            aceeași variabilă, care intră cu un număr și iese mărită
        De ce e nevoie     fără inițializare, INOUT primește NULL
    🟡 Producție
        Cum se scrie       SET @p_salary = 10; CALL emp_raise_marker(@p_salary)
    🔴 Staff
        Unde               10 devine 11 în variabila de sesiune. salaries nu este numit
    Pași
        1. Inițializează variabila.
        2. Treci-o la INOUT.
        3. Nu o confunda cu coloana.
    */
    // Începător: metoda păstrează parametrul care se întoarce în același loc.
    // Producție: public static String explainParameter(). Dacă dispare, IN, OUT și INOUT rămân sinonime.
    // Expert: OUT ar fi început cu NULL și ar fi pierdut 10. IN nu ar fi scris înapoi.
    public static String explainParameter() {
        // Începător: zece intră și unsprezece se poate citi după apel, în aceeași variabilă.
        // Producție: SET @p_salary = 10; CALL emp_raise_marker(@p_salary). Dacă linia dispare, nu mai ai inițializarea cerută de 15.2.1.
        // Expert: @p_salary nu este coloana salary. SET din procedură nu este UPDATE. NULL + 1 rămâne NULL dacă lipsește SET-ul.
        return "SET @p_salary = 10; CALL emp_raise_marker(@p_salary)";
        // Începător: metoda a înapoiat parametrul, nu un rând mărit.
        // Producție: acolada închide explainParameter. Variabila nu a devenit 11 pe server.
        // Expert: Nu este SELECT @p_emp_no după un IN. IN nu scrie înapoi.
    }

    /*
    🟢 Începător
        Ce este            propoziția care chiar cere rândurile, după ce rutina există
        De ce e nevoie     fără ea, DELIMITER pare partea din server
    🟡 Producție
        Cum se scrie       CALL emp_salary(10001)
    🔴 Staff
        Unde               efectul pe rânduri se vede la CALL. CREATE a schimbat doar schema
    Pași
        1. Ține CALL.
        2. Nu pune DELIMITER.
        3. Nu îl trimite.
    */
    // Începător: metoda păstrează propoziția la care se văd rândurile.
    // Producție: public static String explainStatement(). Dacă dispare, momentul rămâne o poveste.
    // Expert: DELIMITER $$ este clientul mysql, din Stored+Procedures.pdf și din 15.1.22. Nu intră în acest șir.
    public static String explainStatement() {
        // Începător: rândurile pleacă la apel, nu când rutina a fost salvată.
        // Producție: CALL emp_salary(10001). Dacă linia dispare, nu mai ai propoziția de după CREATE.
        // Expert: O funcție s-ar vedea la SELECT f_emp_avg_salary(10001), tot la evaluare, nu la CREATE. Niciuna nu este comanda DELIMITER.
        return "CALL emp_salary(10001)";
        // Începător: metoda s-a oprit la propoziția care cere rândurile.
        // Producție: acolada închide explainStatement. CALL nu a rulat.
        // Expert: Nu este un COMMIT. Procedura poate conține COMMIT; funcția nu. Acest șir nu îl are.
    }
}
