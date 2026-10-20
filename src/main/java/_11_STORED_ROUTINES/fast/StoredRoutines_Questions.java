/*
TOPIC:
    Întrebări despre alegerea dintre procedură, funcție, agregat și operator, pe salariul din employees.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști cine pornește rutina și ce rămâne în schemă înainte de CALL.
MECHANISM:
    Șirul din STORED_ROUTINES.procedureTheServerStores. Această clasă nu îl trimite.
LOOKALIKES:
    Funcția intră în SELECT. AVG nu are CREATE. = NULL nu găsește salariul lipsă.
BOOK THEORY:
    Manualul 9.7, 15.1.22: CALL pentru procedură, expresie pentru funcție, ER_SP_NO_RETSET dacă funcția trimite un set. PDF-ul Stored+Routines.pdf nu conține eroarea.
BOOK EXAMPLE:
    STORED_ROUTINES.procedureTheServerStores
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_ROUTINES.java
BREAK LAB:
    CREATE FUNCTION cu SELECT fără INTO, ținut în functionThatReturnsAResultSet.
TRADE-OFFS:
    AVG când nu vrei obiect. Aplicația când niciun alt client nu cheamă calculul.
ANTI-PATTERNS:
    SELECT emp_salary(10001), sau log_bin_trust_function_creators = 1.
STAFF QUESTIONS:
    Întrebările 1–50 din StoredRoutines_Staff_Interview. Aici sunt cinci, cu cheia scrisă.
NOVEL TRANSFER:
    Mutarea coletului este CALL. Codul de bare este funcție. Numărul de colete este COUNT.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei rutine stocate (stored routine):
       Rutina stocată este un set de propoziții păstrate pe server. Clienții o invocă în loc să retrimită fiecare propoziție.

Exemplu:
       StoredRoutines_Terms.explainStoredRoutine()

1. Enunț: Ce problemă rezolvă CREATE PROCEDURE emp_salary față de JOIN-ul rescris în fiecare client?
   Capcană: CREATE citește deja salariul lui 10001, deci CALL este opțional.
   Cheie: procedureTheServerStores salvează rutina. Rândurile pleacă la CALL. Până atunci salaries este neatins.
   Demonstrație: STORED_ROUTINES.procedureTheServerStores
2. Enunț: Ce rămâne în schemă după CREATE FUNCTION f_emp_avg_salary, înainte de SELECT?
   Capcană: Media lui 10001 este deja scrisă într-o tabelă a funcției.
   Cheie: Funcția există. Media nu. Valoarea apare când expresia este evaluată. CREATE nu face COMMIT pe salaries.
   Demonstrație: STORED_ROUTINES.functionInSelectReturnsOneValue
3. Enunț: De ce emp_salary nu este AVG(salary)?
   Capcană: Ambele se pornesc cu CALL, pentru că întorc salariul.
   Cheie: Procedura cere CALL și poate trimite rânduri. AVG este agregat built-in, fără CREATE și fără EXECUTE pe un obiect al tău.
   Demonstrație: STORED_ROUTINES.procedureTheServerStores
4. Enunț: Ce se rupe dacă funcția face SELECT AVG(salary) fără INTO?
   Capcană: Serverul întoarce rândurile și ignoră RETURNS.
   Cheie: functionThatReturnsAResultSet este ER_SP_NO_RETSET. Procedura are voie să trimită setul. Funcția nu.
   Demonstrație: STORED_ROUTINES.functionThatReturnsAResultSet
5. Enunț: Când lași calculul în aplicație?
   Capcană: Orice salariu mediu trebuie să fie rutină, altfel nu este pe server.
   Cheie: Când niciun alt client nu evaluează expresia și nu îți trebuie EXECUTE. AVG când nu vrei obiect. Procedura când trebuie setul sau scrierea.
   Demonstrație: STORED_ROUTINES.selectOfAProcedure

Întrebări adăugate:

*/
// Începător: întrebările scurte stau în acest folder, fără o metodă care să răspundă singură.
// Producție: package _11_STORED_ROUTINES.fast. Corpul clasei rămâne gol.
// Expert: Nu muta aici banca lui COUNT sau a lui OUT. Subiectul este alegerea rutinei.
package _11_STORED_ROUTINES.fast;

// Începător: clasa ține întrebările în comentariu și nu trimite nimic la server.
// Producție: public final class StoredRoutines_Questions. Dacă primește o metodă, nu mai este clasa goală cerută la creare.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall și nu acoperă până la capăt ROUND sau COALESCE.
public final class StoredRoutines_Questions {
    // Începător: nu există o propoziție în corp, fiindcă răspunsurile stau în comentariu.
    // Producție: acolada închide StoredRoutines_Questions. Sub ea nu se adaugă un main.
    // Expert: procedureTheServerStores rămâne în STORED_ROUTINES.java. Golul de aici nu șterge cele cinci întrebări.
}
