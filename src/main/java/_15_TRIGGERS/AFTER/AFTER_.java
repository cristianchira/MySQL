package _15_TRIGGERS.AFTER;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/25178416#overview


/*                                                     Exercise


    ○ a new employee has been promoted to a manager

                   * annual salary should immediately increase with 20000

                                 - than the higher annual salary he earned until that moment

                  * a new record in dept_manager TABLE.

                  * create a trigger that will apply several modifications to the salaries table

                  * once the relevant record in the department manager table has been inserted.

         More precisely, our subtasks are make sure that

                  * the end date of the previously highest salary contract of that employee

                  * is the one from the execution of the insert statement.

                  * Insert a new record in the salaries table about the same employee that reflects their
                                - next contract as a manager.

         A start date the same as the new.from date from the newly inserted record in department manager a salary
                  *  equal to 20,000 higher than their highest ever salary

                  * and let that be a contract of indefinite duration.

         You can display that as a contract ending on the 1st of January in the year 9999.


                                  SQL  -- AFTER INSERT


    ○ DROP TRIGGER IF EXISTS trig_ins_dept_mng;

      DELIMITER $$

      CREATE TRIGGER trig_ins_dept_mng
      AFTER INSERT ON dept_manager
      FOR EACH ROW
      BEGIN

          DECLARE v_curr_salary int;

          SELECT  MAX(salary)
          INTO v_curr_salary FROM  salaries
          WHERE
              emp_no = new.emp_no;

     IF v_curr_salary IS NOT NULL THEN

          UPDATE salaries
          SET to_date = sysdate()

          WHERE  emp_no = NEW.emp_no and to_date = NEW.to_date;
          INSERT INTO salaries
          VALUES (new.emp_no, v_curr_salary + 20000, new.from_date,new.to_date);
    END IF;

    END $$

    DELIMITER ;

    DELETE FROM dept_manager WHERE emp_no = 111534;

    DELETE FROM salaries where salary=99393;

    INSERT INTO dept_manager VALUES(111534,'d009',date_format(sysdate(), '%y-%m-%d'),'9999-01-010');
                    HISTORICAL: '9999-01-010' nu este 1 ianuarie 9999. PDF-ul deschis, pagina 18: contractul se poate afișa ca 1 ianuarie în anul 9999. '%y' este anul pe două cifre.

SELECT
    *
FROM
    dept_manager
WHERE
    emp_no = 111534;

SELECT
    *
FROM
    salaries
WHERE
    emp_no = 111534;






 */


/*
TOPIC:
    AFTER pornește după ce rândul a fost scris, și doar dacă BEFORE și scrierea au reușit. Poate schimba altă tabelă. Nu mai poate schimba NEW pe rândul care a pornit trigger-ul.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    După ce un angajat este scris în dept_manager, contractul de salariu trebuie închis la data execuției și trebuie să apară un contract nou, cu 20000 peste maximul de până atunci. Rândul din dept_manager rămâne.
MECHANISM:
    Serverul execută afterInsertRaisesTheManagerSalary după fiecare rând inserat în dept_manager. SELECT MAX(salary) INTO citește salaries. UPDATE închide contractul a cărui to_date este to_date din rândul nou de manager. INSERT scrie noul salariu. afterUpdateOfTheSameTable încearcă să schimbe salaries dintr-un trigger pornit tot de salaries, ceea ce 27.10 nu lasă.
LOOKALIKES:
    BEFORE ar putea schimba NEW pe dept_manager înainte de scriere, dar nu vede rândul deja scris și, la AUTO_INCREMENT, vede 0. CHECK refuză rândul. Un UPDATE pe aceeași tabelă care a pornit trigger-ul nu este un AFTER reușit.
BOOK THEORY:
    Manualul 9.7, 27.4.1: AFTER se execută doar dacă trigger-ele BEFORE și operația pe rând au reușit; SET NEW într-un AFTER nu are efect; eroarea din AFTER face să eșueze întreaga propoziție; pe tabela tranzacțională propoziția se dă înapoi. 27.10: un trigger nu poate modifica o tabelă deja folosită de propoziția care l-a pornit; nu poate întoarce un set de rezultate; SELECT ... INTO este calea permisă. 15.1.27: INSERT ... ON DUPLICATE KEY UPDATE pornește BEFORE INSERT pentru fiecare rând, apoi fie AFTER INSERT, fie BEFORE UPDATE și AFTER UPDATE. MySQL+Triggers.pdf, paginile 10–18: salariul devine cu 20000 mai mare decât cel mai mare de până atunci, apare un rând în department manager, se închide contractul celui mai mare salariu la data execuției, noul contract ia from date din rândul de manager și se poate afișa cu sfârșit la 1 ianuarie 9999. PDF-ul nu conține CREATE TRIGGER.
BOOK EXAMPLE:
    afterInsertRaisesTheManagerSalary
REPO DEMO:
    src/main/java/_15_TRIGGERS/AFTER/AFTER_.java
BREAK LAB:
    afterUpdateOfTheSameTable face UPDATE pe salaries dintr-un AFTER INSERT tot pe salaries. 27.10 nu lasă modificarea tabelei deja folosite de propoziția care a pornit trigger-ul. În tur șirul nu a fost trimis.
TRADE-OFFS:
    BEFORE când trebuie schimbat NEW pe rândul care se scrie. Procedura cu CALL când mărirea nu trebuie să pornească la fiecare rând din dept_manager.
ANTI-PATTERNS:
    SET NEW ca să îndrepți salariul după scriere, sau UPDATE pe aceeași tabelă care a pornit trigger-ul.
STAFF QUESTIONS:
    Întrebările 1–50 din Triggers_Staff_Interview. Acest fișier ține cele cinci de pe AFTER.
NOVEL TRANSFER:
    O scanare scrie mișcarea, iar cantitatea din raft, altă tabelă, se schimbă după ce mișcarea a rămas. Schimbarea aceleiași tabele de mișcări din propriul trigger nu este acest AFTER.
SPACED REVIEW:
    O zi: 21 octombrie 2026, AFTER vede rândul scris. Șapte zile: 27 octombrie 2026, SET NEW nu mai schimbă nimic. Treizeci de zile: 19 noiembrie 2026, aceeași tabelă nu poate fi modificată din trigger-ul pe care ea l-a pornit.

1. Enunț: Ce problemă rezolvă AFTER INSERT pe dept_manager?
   Capcană: Aplicația face UPDATE-ul pe salaries după INSERT, deci un al doilea client nu poate lăsa managerul fără mărire.
   Cheie: afterInsertRaisesTheManagerSalary pornește la fiecare rând scris în dept_manager, în aceeași propoziție. Închide contractul potrivit și scrie salariul cu 20000 peste maxim. Dacă trigger-ul eșuează, pe InnoDB se dă înapoi și rândul de manager.
   Demonstrație: afterInsertRaisesTheManagerSalary
2. Enunț: Ce se întâmplă cu rândul din dept_manager și cu salaries când INSERT-ul de manager reușește și corpul trigger-ului reușește?
   Capcană: Rândul de manager așteaptă COMMIT, iar salariul nou se vede abia atunci.
   Cheie: AFTER a pornit pentru că scrierea rândului a reușit. Efectul pe salaries este în aceeași propoziție. Trigger-ul nu dă COMMIT. Pe tabela tranzacțională, un eșec ulterior al propoziției dă înapoi și managerul, și salariul scris de trigger.
   Demonstrație: insertThatPromotesTheManager
3. Enunț: De ce un BEFORE INSERT pe dept_manager, cu SET NEW, nu este acest AFTER?
   Capcană: BEFORE vede rândul înainte, deci poate scrie și salariul nou, și este mai devreme, deci mai sigur.
   Cheie: BEFORE poate schimba NEW pe dept_manager înainte de scriere. Nu este momentul de după scriere. La AUTO_INCREMENT, BEFORE vede 0. AFTER pornește doar după ce rândul a rămas și poate lucra pe salaries, altă tabelă.
   Demonstrație: afterInsertRaisesTheManagerSalary
4. Enunț: Cum eșuează AFTER INSERT pe salaries care face UPDATE tot pe salaries?
   Capcană: Al doilea UPDATE îndreaptă salariul, fiindcă AFTER vede rândul scris.
   Cheie: afterUpdateOfTheSameTable modifică tabela deja folosită de propoziția care a pornit trigger-ul. 27.10 nu lasă asta. Corpul de pe dept_manager este permis fiindcă scrie în salaries, nu în dept_manager.
   Demonstrație: afterUpdateOfTheSameTable
5. Enunț: Când lași AFTER și când lași BEFORE sau CALL?
   Capcană: AFTER este mereu după COMMIT, deci este locul oricărei măriri.
   Cheie: AFTER când altă tabelă trebuie schimbată după ce rândul a rămas, în aceeași propoziție. BEFORE când trebuie schimbat NEW. CALL când mărirea nu trebuie să pornească la fiecare INSERT în dept_manager.
   Demonstrație: afterSetNewDoesNotRewriteSalary

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE promovarea scrie și salariul, în aceeași propoziție -> afterInsertRaisesTheManagerSalary -> AFTER INSERT pe dept_manager, UPDATE și INSERT pe salaries -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE UPDATE pe tabela care a pornit trigger-ul -> afterUpdateOfTheSameTable -> UPDATE salaries din AFTER INSERT pe salaries -> nu garantează că salariul se schimbă
    3. Granița
           -> AFTER este mecanismul. Șirul Java nu promovează pe nimeni. DELIMITER nu este propoziția. Workbench nu închide contractul.
    4. Alternativa
           -> BEFORE când trebuie schimbat NEW. CALL când mărirea este cerută, nu automată la fiecare rând de manager.
    5. Anti-patternul
           -> SET NEW într-un AFTER, sau UPDATE pe aceeași tabelă. Primul nu schimbă rândul. Al doilea nu este permis pe tabela deja folosită.
    6. Dovada
           -> afterInsertRaisesTheManagerSalary ține corpul din comentariu, cu data 9999-01-01. insertThatPromotesTheManager ține INSERT-ul. root@localhost a primit 1045, using password: NO.
    7. Interviu
           -> AFTER pornește după scriere, pe altă tabelă. SET NEW nu mai are efect. Eșecul dă înapoi propoziția pe InnoDB. Aceeași tabelă nu se modifică din trigger-ul pe care ea l-a pornit.
*/

public class AFTER_ {

    /*
    🟢 Începător
        Ce este            programul care, după un manager nou, închide salariul vechi și scrie unul mai mare
        De ce e nevoie     fără el, rândul din dept_manager rămâne fără contractul nou
    🟡 Producție
        Cum se scrie       CREATE TRIGGER trig_ins_dept_mng AFTER INSERT ON dept_manager FOR EACH ROW, cu SELECT MAX INTO, UPDATE și INSERT pe salaries
    🔴 Staff
        Unde               AFTER pornește doar după scrierea rândului; salaries este altă tabelă, deci nu este tabela care a pornit trigger-ul
    Pași
        1. Ține corpul din comentariu, ca o singură propoziție.
        2. Data de sfârșit din șir este 9999-01-01.
        3. Nu trimite șirul: 1045 în tur.
    */
    // Începător: metoda păstrează programul de după promovare.
    // Producție: public static String afterInsertRaisesTheManagerSalary(). Dacă dispare, exercițiul rămâne doar în comentariu.
    // Expert: Nu deschide JDBC. WHERE-ul închide rândurile din salaries a căror to_date este to_date din rândul nou de manager, nu doar rândul întors de MAX. PDF-ul cere închiderea contractului celui mai mare salariu.
    public static String afterInsertRaisesTheManagerSalary() {
        // Începător: după ce managerul este scris, salariul maxim de până atunci crește cu 20000 și contractul vechi se închide.
        // Producție: CREATE TRIGGER trig_ins_dept_mng AFTER INSERT ON dept_manager FOR EACH ROW BEGIN DECLARE v_curr_salary INT; SELECT MAX(salary) INTO v_curr_salary FROM salaries WHERE emp_no = NEW.emp_no; IF v_curr_salary IS NOT NULL THEN UPDATE salaries SET to_date = SYSDATE() WHERE emp_no = NEW.emp_no AND to_date = NEW.to_date; INSERT INTO salaries VALUES (NEW.emp_no, v_curr_salary + 20000, NEW.from_date, NEW.to_date); END IF; END. Dacă linia dispare, PDF-ul rămâne fără propoziție.
        // Expert: SELECT fără INTO ar întoarce un set de rezultate, oprit de 27.10. COMMIT în acest corp nu este permis. Dacă UPDATE-ul eșuează, întreaga propoziție de INSERT în dept_manager eșuează, iar pe InnoDB se dă înapoi. SET NEW.emp_no aici nu ar schimba rândul deja scris.
        return "CREATE TRIGGER trig_ins_dept_mng AFTER INSERT ON dept_manager FOR EACH ROW BEGIN DECLARE v_curr_salary INT; SELECT MAX(salary) INTO v_curr_salary FROM salaries WHERE emp_no = NEW.emp_no; IF v_curr_salary IS NOT NULL THEN UPDATE salaries SET to_date = SYSDATE() WHERE emp_no = NEW.emp_no AND to_date = NEW.to_date; INSERT INTO salaries VALUES (NEW.emp_no, v_curr_salary + 20000, NEW.from_date, NEW.to_date); END IF; END";
        // Începător: metoda a înapoiat programul, nu promovarea făcută.
        // Producție: acolada închide afterInsertRaisesTheManagerSalary. Șirul nu a plecat la 3306.
        // Expert: 1045 lasă efectul NERULAT. Spațiul lipsă din Triggers.java, la DELETE FROM, nu este acest șir.
    }

    /*
    🟢 Începător
        Ce este            rândul de manager care pornește programul de pe salarii
        De ce e nevoie     fără el, nu se vede propoziția care activează AFTER
    🟡 Producție
        Cum se scrie       INSERT INTO dept_manager VALUES (111534, 'd009', DATE_FORMAT(SYSDATE(), '%Y-%m-%d'), '9999-01-01')
    🔴 Staff
        Unde               data 9999-01-01 este 1 ianuarie 9999; '9999-01-010' din comentariu rămâne marcată și nu este acest șir
    Pași
        1. Ține INSERT-ul care pornește trigger-ul.
        2. Nu îl trimite.
        3. Nu folosi ziua 010.
    */
    // Începător: metoda păstrează promovarea care pornește programul.
    // Producție: public static String insertThatPromotesTheManager(). Dacă dispare, rămâne doar data greșită din comentariu.
    // Expert: '%Y-%m-%d' este anul pe patru cifre. Comentariul cu '%y' și '9999-01-010' rămâne, marcat HISTORICAL.
    public static String insertThatPromotesTheManager() {
        // Începător: angajatul 111534 devine manager, iar programul de după poate să-i scrie salariul.
        // Producție: INSERT INTO dept_manager VALUES (111534, 'd009', DATE_FORMAT(SYSDATE(), '%Y-%m-%d'), '9999-01-01'). Dacă linia dispare, nu mai ai propoziția care activează AFTER.
        // Expert: REPLACE ar putea activa și DELETE, nu doar INSERT. TRUNCATE pe dept_manager nu ar activa acest AFTER INSERT. Cascada unei chei străine, implicit, nu activează trigger-ul.
        return "INSERT INTO dept_manager VALUES (111534, 'd009', DATE_FORMAT(SYSDATE(), '%Y-%m-%d'), '9999-01-01')";
        // Începător: metoda s-a oprit la rândul de manager.
        // Producție: acolada închide insertThatPromotesTheManager. INSERT-ul nu a rulat.
        // Expert: Ștergerile din comentariu, pe dept_manager și pe salaries, nu sunt acest șir. Ele golesc înainte, ca exercițiul să poată fi repetat, și nu sunt trigger-ul.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a pune 0 în salariu după ce rândul a fost scris
        De ce e nevoie     fără ea, AFTER pare că poate schimba valoarea deja scrisă
    🟡 Producție
        Cum se scrie       CREATE TRIGGER after_salary_noop AFTER INSERT ON salary FOR EACH ROW SET NEW.salary = 0
    🔴 Staff
        Unde               SET NEW nu are efect după scriere; nu este o eroare care dă propoziția înapoi
    Pași
        1. Ține SET NEW într-un AFTER.
        2. Nu îl trimite.
        3. Nu îl numi CHECK.
    */
    // Începător: metoda păstrează programul care nu mai poate îndrepta salariul.
    // Producție: public static String afterSetNewDoesNotRewriteSalary(). Dacă dispare, rămâne doar povestea.
    // Expert: Șirul nu modifică altă tabelă. Nu este eșecul de la aceeași tabelă, care stă în cealaltă metodă.
    public static String afterSetNewDoesNotRewriteSalary() {
        // Începător: salariul scris rămâne cel din INSERT.
        // Producție: CREATE TRIGGER after_salary_noop AFTER INSERT ON salary FOR EACH ROW SET NEW.salary = 0. Dacă linia dispare, nu mai ai SET-ul prea târziu.
        // Expert: BEFORE cu același SET ar fi scris 0. Acest AFTER pornește doar dacă scrierea a reușit, apoi nu schimbă NEW. O eroare aruncată din corp ar fi alt drum: ar face să cadă propoziția.
        return "CREATE TRIGGER after_salary_noop AFTER INSERT ON salary FOR EACH ROW SET NEW.salary = 0";
        // Începător: metoda a păstrat programul fără efect asupra rândului scris.
        // Producție: acolada închide afterSetNewDoesNotRewriteSalary. Șirul nu a plecat.
        // Expert: Folderul BEFORE predă SET NEW la timp. Aici rămâne doar faptul că momentul a trecut.
    }

    /*
    🟢 Începător
        Ce este            programul care încearcă să schimbe tabela din care tocmai a venit rândul
        De ce e nevoie     fără el, pare că AFTER poate face UPDATE pe orice tabelă, inclusiv pe cea care l-a pornit
    🟡 Producție
        Cum se scrie       CREATE TRIGGER after_salary_same AFTER INSERT ON salary FOR EACH ROW UPDATE salary SET salary = 0 WHERE emp_no = NEW.emp_no
    🔴 Staff
        Unde               27.10: trigger-ul nu poate modifica o tabelă deja folosită de propoziția care l-a pornit
    Pași
        1. Ține UPDATE pe aceeași tabelă cu ON.
        2. Nu îl trimite.
        3. Nu îl confunda cu UPDATE-ul pe salaries pornit de dept_manager.
    */
    // Începător: metoda păstrează programul oprit fiindcă scrie înapoi în tabela care l-a pornit.
    // Producție: public static String afterUpdateOfTheSameTable(). Dacă dispare, eșecul de producție rămâne fără propoziție.
    // Expert: Corpul de promovare scrie în salaries în timp ce propoziția scrie în dept_manager. Asta este altă tabelă. Acest șir nu este acel corp.
    public static String afterUpdateOfTheSameTable() {
        // Începător: programul vrea să pună 0 în salary chiar în momentul în care salary primește rândul.
        // Producție: CREATE TRIGGER after_salary_same AFTER INSERT ON salary FOR EACH ROW UPDATE salary SET salary = 0 WHERE emp_no = NEW.emp_no. Dacă linia dispare, nu mai ai tabela folosită de două ori.
        // Expert: Un trigger nu poate întoarce un set de rezultate și nu poate folosi SQL dinamic. Acest UPDATE nici nu ajunge să fie „al doilea salariu”: tabela este deja folosită de INSERT. Pe replica cu replicare pe rând, trigger-ul de pe replica nu pornește pentru propoziția venită de la sursă.
        return "CREATE TRIGGER after_salary_same AFTER INSERT ON salary FOR EACH ROW UPDATE salary SET salary = 0 WHERE emp_no = NEW.emp_no";
        // Începător: metoda a păstrat programul care nu are voie să schimbe aceeași tabelă.
        // Producție: acolada închide afterUpdateOfTheSameTable. UPDATE-ul nu a rulat.
        // Expert: 1045 nu este refuzul acestei tabele. Șirul nu a fost trimis, deci simptomul rămâne cel din manual, nu unul citit de pe server.
    }
}
