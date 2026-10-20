package _15_TRIGGERS.BEFORE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/25178416#overview


/*                                   SQL  -- BEFORE INSERT


    ○ use employees;

       COMMIT;

      # Before insert
       DELIMITER $$

       CREATE TRIGGER  before_salary_insert
       BEFORE INSERT ON salary
       FOR EACH ROW
       BEGIN

            IF NEW.salary < 0  THEN
            SET NEW.salary =0;
            END IF;

      END$$

     DELIMITER ;


                                    SQL -- BEFORE UPDATE


    ○  # Before update
         DELIMITER $$

         CREATE TRIGGER  before_salaries_update

         BEFORE UPDATE ON salaries
         FOR EACH ROW
         BEGIN

            IF NEW.salary < 0  THEN
            SET NEW.salary = OLD.salary;
            END IF;

    END$$

DELIMITER ;



                                                      EXERCISE BEFORE INSERT


    ○ Task

                  * Create a trigger that checks if

                                -the hire date of an employee is higher

                                - than the current date.

                        If true,

                                 - set this date to be the current date.

                        Format the output appropriately (YY-MM-DD).


    ○ Solution


    DELIMITER $$

    CREATE TRIGGER trig_hire_date

    BEFORE INSERT ON employees

    FOR EACH ROW

    BEGIN

                IF NEW.hire_date > date_format(sysdate(), '%Y-%m-%d') THEN

                                SET NEW.hire_date = date_format(sysdate(), '%Y-%m-%d');

                END IF;

    END $$

    DELIMITER ;



INSERT employees VALUES ('999904', '1970-01-31', 'John', 'Johnson', 'M', '2025-01-01');

SELECT

    *

FROM

    employees

ORDER BY emp_no DESC;




 */

/*
TOPIC:
    BEFORE schimbă rândul care urmează să fie scris. La INSERT există doar NEW. La UPDATE, OLD este salariul de dinainte, iar NEW este cel care încă nu a fost scris.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Salariul negativ nu rămâne negativ, iar o dată de angajare din viitor nu rămâne în viitor. Rândul rămâne, cu valoarea pusă de trigger.
MECHANISM:
    Serverul execută beforeInsertSetsNegativeToZero înainte să scrie rândul în salary. SET NEW.salary = 0 înlocuiește valoarea. beforeUpdateKeepsOldSalary, la UPDATE, pune înapoi OLD.salary când NEW.salary este negativ. insertTriggerThatReadsOld cere OLD la un INSERT, unde nu există rând vechi.
LOOKALIKES:
    AFTER nu mai poate face SET NEW. CHECK refuză rândul în loc să scrie 0. Un DELETE are doar OLD, nu NEW.
BOOK THEORY:
    Manualul 9.7, 27.4.1, deschis în tur: BEFORE este momentul de dinaintea fiecărui rând; SET NEW.col_name este permis în BEFORE dacă contul are UPDATE pe coloană; OLD este doar citit; la INSERT nu există OLD; la DELETE nu există NEW; la UPDATE există ambele; într-un BEFORE, NEW pe o coloană AUTO_INCREMENT este 0, nu numărul generat; dacă BEFORE eșuează, operația pe rând nu se face. 15.1.27: verificarea de tip a coloanei este înainte de activare, deci BEFORE nu preface o valoare nepotrivită tipului. MySQL+Triggers.pdf nu arată CREATE TRIGGER și nu arată exercițiul de hire_date; hire_date stă în comentariul acestei clase.
BOOK EXAMPLE:
    beforeInsertSetsNegativeToZero
REPO DEMO:
    src/main/java/_15_TRIGGERS/BEFORE/BEFORE_.java
BREAK LAB:
    insertTriggerThatReadsOld citește OLD.salary într-un BEFORE INSERT. 27.4.1: la INSERT nu există rând vechi. În tur șirul nu a fost trimis.
TRADE-OFFS:
    AFTER când trebuie schimbată altă tabelă după scriere. CHECK când data din viitor nu are voie să rămână nici măcar ca azi.
ANTI-PATTERNS:
    OLD într-un INSERT, sau BEFORE folosit ca să transforme un text într-un întreg.
STAFF QUESTIONS:
    Întrebările 1–50 din Triggers_Staff_Interview. Acest fișier ține cele cinci de pe BEFORE.
NOVEL TRANSFER:
    O recepție de marfă: cantitatea negativă se scrie 0 înainte ca rândul să rămână. Refuzul recepției este CHECK, nu acest BEFORE.
SPACED REVIEW:
    O zi: 21 octombrie 2026, SET NEW înainte de scriere. Șapte zile: 27 octombrie 2026, OLD lipsește la INSERT. Treizeci de zile: 19 noiembrie 2026, AUTO_INCREMENT în BEFORE este 0.

1. Enunț: Ce problemă rezolvă BEFORE INSERT pe salary când salariul venit este negativ?
   Capcană: Rândul este respins, ca la o constrângere, deci -50 nu lasă nimic în tabelă.
   Cheie: beforeInsertSetsNegativeToZero face SET NEW.salary = 0 înainte de scriere. Rândul rămâne, cu 0. CHECK ar fi refuzat rândul.
   Demonstrație: beforeInsertSetsNegativeToZero
2. Enunț: La BEFORE UPDATE, NEW.salary este negativ. Ce salariu rămâne scris?
   Capcană: Rămâne valoarea negativă, iar OLD este doar un nume pentru aceeași valoare.
   Cheie: beforeUpdateKeepsOldSalary pune NEW.salary = OLD.salary. OLD este citit și nu poate fi modificat. Valoarea de dinaintea UPDATE-ului rămâne.
   Demonstrație: beforeUpdateKeepsOldSalary
3. Enunț: De ce un AFTER INSERT cu SET NEW.salary = 0 nu este acest BEFORE?
   Capcană: Ambele văd NEW, deci ambele scriu 0.
   Cheie: AFTER se activează după ce rândul a fost scris. SET NEW nu mai are efect. BEFORE este singurul moment în care SET NEW schimbă ce urmează să fie scris.
   Demonstrație: beforeInsertSetsNegativeToZero
4. Enunț: Cum eșuează BEFORE INSERT care face SET NEW.salary = OLD.salary?
   Capcană: OLD este 0, deci salariul nou devine 0 și rândul rămâne.
   Cheie: insertTriggerThatReadsOld cere un rând vechi la INSERT. 27.4.1: la INSERT există doar NEW. Nu există OLD de pus înapoi.
   Demonstrație: insertTriggerThatReadsOld
5. Enunț: Când lași acest BEFORE și când lași CHECK sau AFTER?
   Capcană: BEFORE vede și numărul AUTO_INCREMENT, deci este locul în care scrii rândul copil cu id-ul nou.
   Cheie: BEFORE când valoarea trebuie schimbată și rândul trebuie să rămână. CHECK când data din viitor sau salariul negativ nu au voie să lase un rând. AFTER când trebuie văzut rândul scris: în BEFORE, NEW pe AUTO_INCREMENT este 0.
   Demonstrație: beforeInsertClampsFutureHireDate

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE salariul negativ rămâne, dar ca 0 -> beforeInsertSetsNegativeToZero -> SET NEW.salary = 0 -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE OLD la un INSERT -> insertTriggerThatReadsOld -> SET NEW.salary = OLD.salary -> nu garantează un salariu 0
    3. Granița
           -> BEFORE este mecanismul. Șirul Java nu scrie rândul. DELIMITER nu face parte din propoziție. Workbench nu este momentul activării.
    4. Alternativa
           -> CHECK când rândul trebuie să cadă. AFTER când o altă tabelă se schimbă după scriere.
    5. Anti-patternul
           -> OLD într-un INSERT, sau BEFORE care încearcă să facă valid un text pus într-o coloană întreagă. Verificarea de tip este înainte de trigger.
    6. Dovada
           -> beforeInsertSetsNegativeToZero și beforeUpdateKeepsOldSalary țin propozițiile din comentariu. root@localhost a primit 1045, using password: NO.
    7. Interviu
           -> BEFORE schimbă NEW înainte de scriere. La INSERT nu există OLD. La UPDATE, OLD este valoarea de dinainte și este doar citit. AUTO_INCREMENT în BEFORE este 0.
*/

public class BEFORE_ {

    /*
    🟢 Începător
        Ce este            programul care pune 0 înainte să se scrie un salariu negativ
        De ce e nevoie     fără el, -50 rămâne în salary
    🟡 Producție
        Cum se scrie       CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW cu SET NEW.salary = 0
    🔴 Staff
        Unde               activarea este înaintea scrierii; la INSERT nu există OLD; tipul coloanei a fost deja verificat
    Pași
        1. Ține CREATE-ul din comentariu, ca o singură propoziție.
        2. Nu îl trimite.
        3. Nu adăuga DELIMITER.
    */
    // Începător: metoda păstrează programul de dinaintea salariului nou.
    // Producție: public static String beforeInsertSetsNegativeToZero(). Dacă dispare, comentariul rămâne singura propoziție.
    // Expert: Nu deschide JDBC. Nu mută SET-ul într-un AFTER. Nu înlocuiește tabela salary cu un view.
    public static String beforeInsertSetsNegativeToZero() {
        // Începător: salariul negativ devine 0, iar rândul se scrie.
        // Producție: CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW BEGIN IF NEW.salary < 0 THEN SET NEW.salary = 0; END IF; END. Dacă linia dispare, nu mai ai propoziția din comentariu, gata de trimis.
        // Expert: SET NEW cere privilegiul UPDATE al contului DEFINER, nu al celui care face INSERT. CURRENT_USER în trigger este DEFINER. Un text într-o coloană întreagă nu ajunge aici ca să fie prefăcut în 0.
        return "CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW BEGIN IF NEW.salary < 0 THEN SET NEW.salary = 0; END IF; END";
        // Începător: metoda a înapoiat programul, nu rândul scris.
        // Producție: acolada închide beforeInsertSetsNegativeToZero. Șirul nu a plecat la 3306.
        // Expert: 1045 lasă efectul NERULAT. Acolada nu dovedește că -50 a devenit 0 pe server.
    }

    /*
    🟢 Începător
        Ce este            salariul negativ care intră în tabelă și trebuie îndreptat înainte de scriere
        De ce e nevoie     fără el, CREATE-ul nu arată ce valoare pleacă de la client
    🟡 Producție
        Cum se scrie       INSERT INTO salary (emp_no, salary) VALUES (1, -50)
    🔴 Staff
        Unde               valoarea care rămâne este cea din NEW după BEFORE, nu -50; CHECK nu ar fi lăsat rândul
    Pași
        1. Ține INSERT-ul cu -50.
        2. Nu îl trimite.
        3. Nu îl numi rezultatul de pe server.
    */
    // Începător: metoda păstrează salariul pe care programul îl poate îndrepta.
    // Producție: public static String insertOfNegativeSalary(). Dacă dispare, nu mai ai rândul de dinaintea scrierii.
    // Expert: Șirul nu este un UPDATE și nu citește OLD. Pe InnoDB, eșecul trigger-ului ar da înapoi toată propoziția.
    public static String insertOfNegativeSalary() {
        // Începător: clientul trimite -50. Cu programul de dinainte, în tabelă rămâne 0.
        // Producție: INSERT INTO salary (emp_no, salary) VALUES (1, -50). Dacă linia dispare, nu mai ai valoarea care intră în NEW.
        // Expert: Dacă BEFORE eșuează, operația pe acest rând nu se face. AFTER nu pornește în cazul acela. Verificarea de tip este înainte, deci 'abc' nu ajunge în NEW ca să fie prefăcut.
        return "INSERT INTO salary (emp_no, salary) VALUES (1, -50)";
        // Începător: metoda s-a oprit la salariul trimis.
        // Producție: acolada închide insertOfNegativeSalary. INSERT-ul nu a rulat.
        // Expert: Nu s-a citit un plan. TRUNCATE nu este acest INSERT.
    }

    /*
    🟢 Începător
        Ce este            programul care, la schimbarea unui salariu, păstrează valoarea veche dacă cea nouă este negativă
        De ce e nevoie     fără el, UPDATE-ul ar scrie -50 peste salariul de dinainte
    🟡 Producție
        Cum se scrie       CREATE TRIGGER before_salaries_update BEFORE UPDATE ON salaries FOR EACH ROW cu SET NEW.salary = OLD.salary
    🔴 Staff
        Unde               OLD este doar citit; NEW încă nu este scris; tabela din comentariu este salaries, nu salary
    Pași
        1. Ține UPDATE-ul de dinainte, pe salaries.
        2. Nu îl trimite.
        3. Nu folosi OLD într-un INSERT.
    */
    // Începător: metoda păstrează programul de dinaintea schimbării.
    // Producție: public static String beforeUpdateKeepsOldSalary(). Dacă dispare, UPDATE-ul din comentariu nu este o propoziție gata de trimis.
    // Expert: Nu amesteca salary cu salaries. Comentariul le ține pe ambele; această propoziție este cea de UPDATE.
    public static String beforeUpdateKeepsOldSalary() {
        // Începător: dacă salariul nou este negativ, rămâne salariul care era deja acolo.
        // Producție: CREATE TRIGGER before_salaries_update BEFORE UPDATE ON salaries FOR EACH ROW BEGIN IF NEW.salary < 0 THEN SET NEW.salary = OLD.salary; END IF; END. Dacă linia dispare, nu mai ai OLD lângă NEW.
        // Expert: OLD nu poate fi ținta unui SET. La DELETE ar exista doar OLD, fără NEW de schimbat. Două trigger-e BEFORE UPDATE pe aceeași tabelă pornesc în ordinea creării, dacă nu scrii PRECEDES sau FOLLOWS.
        return "CREATE TRIGGER before_salaries_update BEFORE UPDATE ON salaries FOR EACH ROW BEGIN IF NEW.salary < 0 THEN SET NEW.salary = OLD.salary; END IF; END";
        // Începător: metoda a înapoiat programul de schimbare.
        // Producție: acolada închide beforeUpdateKeepsOldSalary. UPDATE-ul nu a rulat.
        // Expert: Numele before_salaries_update trebuie să fie unic în schemă, nu doar pe tabelă.
    }

    /*
    🟢 Începător
        Ce este            programul care nu lasă data de angajare în viitor; o aduce la data de azi
        De ce e nevoie     fără el, exercițiul din comentariu rămâne fără propoziție
    🟡 Producție
        Cum se scrie       CREATE TRIGGER trig_hire_date BEFORE INSERT ON employees cu SET NEW.hire_date la data de azi
    🔴 Staff
        Unde               rândul rămâne, cu azi; CHECK ar fi refuzat data din viitor și nu ar fi scris rândul
    Pași
        1. Ține trigger-ul de pe employees.
        2. Nu îl trimite.
        3. Nu îl numi constrângere.
    */
    // Începător: metoda păstrează programul care mută data din viitor la azi.
    // Producție: public static String beforeInsertClampsFutureHireDate(). Dacă dispare, exercițiul rămâne doar în comentariu.
    // Expert: PDF-ul de trigger nu conține hire_date. Propoziția vine din comentariul acestei clase. '%Y-%m-%d' este anul pe patru cifre, spre deosebire de '%y' marcat în AFTER.
    public static String beforeInsertClampsFutureHireDate() {
        // Începător: angajatul cu data în viitor rămâne în tabelă, iar data scrisă este cea de azi.
        // Producție: CREATE TRIGGER trig_hire_date BEFORE INSERT ON employees FOR EACH ROW BEGIN IF NEW.hire_date > DATE_FORMAT(SYSDATE(), '%Y-%m-%d') THEN SET NEW.hire_date = DATE_FORMAT(SYSDATE(), '%Y-%m-%d'); END IF; END. Dacă linia dispare, nu mai ai exercițiul gata de trimis.
        // Expert: SYSDATE din comentariu este funcția de sistem pe care slide-ul o leagă de momentul execuției. CHECK (hire_date <= data de azi) ar refuza rândul. Acest SET îl păstrează.
        return "CREATE TRIGGER trig_hire_date BEFORE INSERT ON employees FOR EACH ROW BEGIN IF NEW.hire_date > DATE_FORMAT(SYSDATE(), '%Y-%m-%d') THEN SET NEW.hire_date = DATE_FORMAT(SYSDATE(), '%Y-%m-%d'); END IF; END";
        // Începător: metoda a înapoiat programul de pe data de angajare.
        // Producție: acolada închide beforeInsertClampsFutureHireDate. Angajatul 999904 din comentariu nu a fost inserat.
        // Expert: INSERT-ul din comentariu, cu data 2025-01-01, nu este această propoziție. El arată rândul care pornește trigger-ul, nu trigger-ul.
    }

    /*
    🟢 Începător
        Ce este            programul de INSERT care caută un salariu vechi, deși rândul este nou
        De ce e nevoie     fără el, OLD pare că există și la adăugare
    🟡 Producție
        Cum se scrie       CREATE TRIGGER before_salary_reads_old BEFORE INSERT ON salary FOR EACH ROW SET NEW.salary = OLD.salary
    🔴 Staff
        Unde               27.4.1: la INSERT nu există rând vechi; OLD este pentru UPDATE și DELETE
    Pași
        1. Ține OLD în INSERT.
        2. Nu îl trimite.
        3. Nu îl repara în șir: eșecul este propoziția.
    */
    // Începător: metoda păstrează programul greșit, care caută salariul de dinainte la un rând nou.
    // Producție: public static String insertTriggerThatReadsOld(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: La DELETE ar fi invers: există OLD și nu există NEW. Nu schimba șirul într-un UPDATE ca să pară că merge.
    public static String insertTriggerThatReadsOld() {
        // Începător: programul vrea salariul vechi al unui om care încă nu are rând.
        // Producție: CREATE TRIGGER before_salary_reads_old BEFORE INSERT ON salary FOR EACH ROW SET NEW.salary = OLD.salary. Dacă linia dispare, nu mai ai cel mai mic eșec de pe BEFORE.
        // Expert: OLD este doar citit și aici nici nu există. SET NEW într-un AFTER tot nu ar citi un OLD de INSERT. Numele trigger-ului trebuie să fie unic în schemă.
        return "CREATE TRIGGER before_salary_reads_old BEFORE INSERT ON salary FOR EACH ROW SET NEW.salary = OLD.salary";
        // Începător: metoda a păstrat programul care nu are de unde să citească.
        // Producție: acolada închide insertTriggerThatReadsOld. Șirul nu a plecat.
        // Expert: 1045 nu este refuzul acestui OLD. Refuzul nu a fost văzut, pentru că serverul nu a primit șirul.
    }
}
