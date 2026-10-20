/*
TOPIC:
    Alegi procedura când CALL trebuie să trimită rânduri sau să schimbe date, și funcția când o singură valoare intră într-un SELECT.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Același JOIN pe emp_no stă pe server. CREATE nu citește salariul. CALL îl va citi. Funcția este vecinul, nu exemplul tipărit.
MECHANISM:
    main tipărește STORED_ROUTINES.procedureTheServerStores. Serverul, dacă îl primește, salvează emp_salary. Șirul nu este trimis din main.
LOOKALIKES:
    SELECT f_emp_avg_salary(10001) este funcția. AVG(salary) este agregatul, fără CREATE. DELIMITER $$ este clientul mysql.
BOOK THEORY:
    Manualul 9.7, 27.2 și 15.1.22: rutina stă pe server; CALL pornește procedura; funcția intră într-o expresie și nu poate trimite un set. Stored+Routines.pdf, pe paginile deschise, are TECHNICAL DIFFERENCES și nu are ER_SP_NO_RETSET.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_ROUTINES.java
BREAK LAB:
    STORED_ROUTINES.functionThatReturnsAResultSet. main nu o tipărește.
TRADE-OFFS:
    Funcția când valoarea unică intră în SELECT. AVG când nu vrei obiect în schemă. Aplicația când niciun alt client nu cheamă calculul.
ANTI-PATTERNS:
    SELECT emp_salary(10001), sau un CREATE FUNCTION care face SELECT fără INTO.
STAFF QUESTIONS:
    Întrebările 1–50 din StoredRoutines_Staff_Interview.
NOVEL TRANSFER:
    Mutarea unui colet este CALL. Codul de bare într-un SELECT este funcție. Numărul de colete este COUNT.
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
       CREATE schimbă schema la sfârșitul lui. CALL sau SELECT din expresie este propoziția la care se văd rândurile sau valoarea.

Exemplu:
       StoredRoutines_Terms.explainStatement()
       DELIMITER $$ nu este propoziția. Este comanda clientului mysql.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE același JOIN pe emp_no, pornit cu CALL -> main -> CREATE PROCEDURE emp_salary -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE funcția să trimită rândurile -> STORED_ROUTINES.functionThatReturnsAResultSet -> SELECT fără INTO în funcție -> nu garantează ER_SP_NO_RETSET în tur
    3. Granița
           -> Rutina este mecanismul. System.out doar afișează șirul. DELIMITER este clientul. Workbench nu este mecanismul.
    4. Alternativa
           -> Funcția când valoarea intră în SELECT. AVG când nu vrei obiect. Trigger-ul când codul pornește de la scrierea rândului.
    5. Anti-patternul
           -> SELECT emp_salary(10001), sau log_bin_trust_function_creators = 1 în loc de READS SQL DATA.
    6. Dovada
           -> main tipărește procedureTheServerStores. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> CREATE salvează rutina și nu mută salariul. CALL pornește procedura. Funcția intră în SELECT. SELECT fără INTO în funcție nu se creează.

🟢 Începător
    Ce este            exemplul minim: procedura care citește un angajat și pe care o pornești cu CALL
    De ce e nevoie     fără șirul din STORED_ROUTINES.java, comentariul că procedura nu întoarce o valoare pare regula
🟡 Producție
    Cum se scrie       System.out.println(STORED_ROUTINES.procedureTheServerStores())
🔴 Staff
    Unde               tipărirea nu este CREATE-ul executat. Vecinul este funcția. Efectul pe server rămâne NERULAT în tur.
Pași
    1. Ia propoziția din procedureTheServerStores.
    2. O tipărește.
    3. Nu deschide o conexiune și nu tipărește funcția care trimite un set.
*/
// Începător: lecția scurtă stă lângă clasa care deja alege între procedură și funcție.
// Producție: package _11_STORED_ROUTINES.fast. Dacă linia dispare, main nu mai este lângă STORED_ROUTINES.java.
// Expert: Pachetul nu predă până la capăt IN, OUT, COUNT sau ROUND. Copiii folderului rămân clasele deja scrise.
package _11_STORED_ROUTINES.fast;

// Începător: clasa pornește o singură alegere, fără să citească un salariu.
// Producție: public final class StoredRoutines_Fast. Fără ea, nu există main pentru exemplul minim.
// Expert: Nu predă fiecare copil. STORED_PROCEDURES, FUNCTION_AND_OPERATORS și agregatele rămân folderele lor.
public final class StoredRoutines_Fast {
    // Începător: pornește exemplul și scrie procedura, nu un rând din salaries.
    // Producție: public static void main(String[] args). args nu este o propoziție și nu schimbă serverul.
    // Expert: Nu cheamă functionThatReturnsAResultSet. Nu deschide JDBC. Tipărirea nu ține loc de CALL.
    public static void main(String[] args) {
        // Începător: pe ecran apare rutina pe care serverul ar putea să o salveze, iar salaries nu se citește de aici.
        // Producție: System.out.println(STORED_ROUTINES.procedureTheServerStores()), adică CREATE PROCEDURE emp_salary. Fără linie, exemplul minim dispare.
        // Expert: println nu trimite propoziția la 3306. O funcție tot nu se pornește cu CALL. AVG tot nu cere CREATE.
        System.out.println(_11_STORED_ROUTINES.STORED_ROUTINES.procedureTheServerStores());
        // Începător: exemplul s-a oprit după ce a arătat procedura.
        // Producție: acolada închide main. args rămâne nefolosit, pentru că URL-ul nu stă în argumente.
        // Expert: Refuzul root@localhost nu este un rezultat al acestei acolade. SELECT fără INTO rămâne în cealaltă metodă.
    }
    // Începător: lecția scurtă se oprește la alegerea procedurii.
    // Producție: acolada închide StoredRoutines_Fast. Nu mai există o a doua clasă Fast.
    // Expert: Cine întreabă doar IN iese la INPUT_PARAMETER. Cine întreabă doar media iese la AVG sau la funcția stocată.
}
