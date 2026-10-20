package _15_TRIGGERS;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/25178416#overview


/*                                            MySQL Triggers


    ○ A trigger is a MySQL object

                  * that can “trigger” a specific action or calculation

                                - ‘before’

                                - or ‘after’

                        an INSERT, UPDATE, or DELETE

                                - statement has been executed
                    HISTORICAL: „statement has been executed” nu este regula pentru BEFORE. Manualul 9.7, 15.1.27, deschis în tur: BEFORE se activează înainte de fiecare rând de modificat. AFTER se activează după. 27.4.1: un SET NEW într-un AFTER nu are efect, fiindcă schimbarea rândului s-a petrecut deja. Dacă trigger-ul eșuează, întreaga propoziție eșuează.

    ○ System functions = built-in functions

                 * often applied in practice,

                 * they provide data about the moment of the execution of a certain query


    ○ A type of Stored Program,

                  * associated with a table,

                  * that will be activated automatically once a specific event occurs

    ○ This event must be

                  * related to the associated table

                  * and represented by one of the following three DML statements:

                                - INSERT

                                - UPDATE

                                - DELETE


                                      SQL  -- BEFORE INSERT


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

    DELETE FROMsalaries where salary=99393;
                    HISTORICAL: spațiul lipsă dintre FROM și salaries nu este o propoziție. În AFTER_.java aceeași ștergere este scrisă cu spațiu. Serverul nu a primit-o în tur.

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
    Trigger-ul este programul de pe o tabelă permanentă care pornește singur la INSERT, UPDATE sau DELETE, câte o dată pe rând. Alegi BEFORE când valoarea trebuie schimbată înainte să fie scrisă, AFTER când o altă tabelă trebuie schimbată după ce rândul a rămas, și constrângerea când rândul trebuie să cadă.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Un salariu negativ nu are voie să rămână negativ, iar golirea tabelei cu TRUNCATE nu are voie să fie luată drept DELETE. După BEFORE, rândul rămâne cu 0. CHECK-ul nu lasă rândul.
MECHANISM:
    Serverul execută șirul din beforeInsertRewritesNegativeSalary la fiecare rând de INSERT, înainte să-l scrie. SET NEW.salary = 0 schimbă valoarea care urmează să fie scrisă. afterSetNewLeavesTheWrittenSalary face același SET după scriere și nu schimbă rândul. checkRefusesTheNegativeSalary refuză rândul. truncateDoesNotFireADeleteTrigger nu pornește un trigger de DELETE.
LOOKALIKES:
    AFTER vede rândul deja scris: SET NEW nu îl mai schimbă. CHECK (salary >= 0) refuză rândul și nu scrie 0. CALL pornește o procedură doar când clientul o trimite. TRUNCATE nu este DELETE.
BOOK THEORY:
    Manualul 9.7, 27.4.1, deschis în tur: BEFORE se activează înainte de fiecare rând inserat; AFTER este cealaltă valoare permisă; corpul de după FOR EACH ROW rulează o dată pe rând; într-un INSERT există doar NEW; un SET NEW într-un AFTER nu are efect, fiindcă schimbarea rândului s-a petrecut deja; dacă un BEFORE eșuează, operația pe rând nu se face; AFTER rulează doar dacă BEFORE și operația pe rând au reușit; eroarea din trigger face să eșueze întreaga propoziție; pe tabela tranzacțională eșecul dă propoziția înapoi, pe cea netranzacțională schimbările dinaintea erorii rămân. 15.1.27, deschis în tur: trigger-ul stă pe o tabelă permanentă, nu pe TEMPORARY și nu pe un view; TRUNCATE și DROP TABLE nu activează trigger-ul de DELETE, pentru că nu folosesc DELETE; verificarea de tip a coloanei are loc înainte de trigger, deci BEFORE nu transformă o valoare nepotrivită tipului într-una validă; implicit, acțiunile în cascadă ale cheii străine nu activează trigger-e; enable_cascade_triggers=ON are efect doar când innodb_native_foreign_keys=OFF, la CASCADE și SET NULL, și nu are efect când innodb_native_foreign_keys=ON. 27.10, deschis în tur: trigger-ul nu poate începe sau încheia o tranzacție și nu poate întoarce un set de rezultate. MySQL+Triggers.pdf, 18 pagini, deschis în tur: este un stored program pe o tabelă, pornit automat la INSERT, UPDATE sau DELETE; slide-ul spune „before or after” „statement has been executed”, propoziție corectată mai sus; funcțiile de sistem dau momentul execuției; exercițiul de promovare cere salariul cu 20000 peste maximul de până atunci, închiderea contractului celui mai mare salariu la data execuției și un contract afișat ca 1 ianuarie 9999. PDF-ul nu conține CREATE TRIGGER.
BOOK EXAMPLE:
    beforeInsertRewritesNegativeSalary
REPO DEMO:
    src/main/java/_15_TRIGGERS/Triggers.java
BREAK LAB:
    afterSetNewLeavesTheWrittenSalary ține SET NEW.salary = 0 într-un AFTER INSERT. Salariul scris rămâne cel din INSERT. În tur șirul nu a fost trimis: autentificarea a fost refuzată.
TRADE-OFFS:
    CHECK când rândul negativ nu are voie să existe. Procedura cu CALL când mărirea nu trebuie să pornească la fiecare INSERT. AFTER pe altă tabelă când trebuie văzut rândul deja scris, inclusiv numărul generat, pe care BEFORE îl vede 0 la AUTO_INCREMENT.
ANTI-PATTERNS:
    SET NEW într-un AFTER ca să „corectezi” salariul, sau TRUNCATE crezut că pornește trigger-ul de DELETE.
STAFF QUESTIONS:
    Întrebările 1–50 din Triggers_Staff_Interview. Cheia este scrisă acolo. Nu există main care să o citească.
NOVEL TRANSFER:
    Un depozit: scanarea scrie o mișcare, iar cantitatea din raft trebuie să se schimbe în aceeași propoziție. Cantitatea negativă păstrată ca 0 este BEFORE. Refuzul cantității negative este CHECK. Schimbarea raftului după ce mișcarea a rămas este AFTER pe altă tabelă.
SPACED REVIEW:
    O zi: 21 octombrie 2026, BEFORE schimbă valoarea înainte de scriere. Șapte zile: 27 octombrie 2026, SET NEW în AFTER nu schimbă rândul. Treizeci de zile: 19 noiembrie 2026, TRUNCATE nu pornește trigger-ul de DELETE.

Definiția unui trigger (trigger):
       Trigger-ul este programul legat de o tabelă permanentă, pornit de server la INSERT, UPDATE sau DELETE, o dată pentru fiecare rând.

Exemplu:
       Triggers_Terms.explainTrigger()
       Salariul -50 rămâne 0 dacă BEFORE INSERT face SET NEW.salary = 0.

Definiția unei tabele (table):
       Tabela permanentă este locul de care este legat trigger-ul. Un view și o tabelă TEMPORARY nu pot purta trigger.

Exemplu:
       Triggers_Terms.explainTable()
       salary este tabela de pe care pornește before_salary_insert.

Definiția unei coloane (column):
       Coloana văzută de trigger este NEW la INSERT și la UPDATE, și OLD la UPDATE și la DELETE. OLD este doar citit.

Exemplu:
       Triggers_Terms.explainColumn()
       NEW.salary este valoarea care urmează să fie scrisă.

Definiția unei propoziții (statement):
       Propoziția care scrie rândul este cea care pornește trigger-ul. Dacă trigger-ul eșuează, întreaga propoziție eșuează.

Exemplu:
       Triggers_Terms.explainStatement()
       TRUNCATE TABLE salary nu pornește trigger-ul de DELETE.

Definiția unei tranzacții (transaction):
       Trigger-ul nu poate da COMMIT sau ROLLBACK. Pe tabela tranzacțională, eșecul dă înapoi toată propoziția, inclusiv ce a scris trigger-ul.

Exemplu:
       Triggers_Terms.explainTransaction()
       START TRANSACTION în corpul trigger-ului nu este permis.

1. Enunț: Ce problemă rezolvă alegerea dintre BEFORE, AFTER și CHECK pe salary?
   Capcană: Orice program de pe server refuză salariul negativ, deci trigger-ul și CHECK lasă aceeași tabelă.
   Cheie: beforeInsertRewritesNegativeSalary păstrează rândul și scrie 0 în loc de valoarea negativă, înainte ca rândul să fie scris. checkRefusesTheNegativeSalary nu lasă rândul. AFTER nu mai poate schimba NEW.
   Demonstrație: beforeInsertRewritesNegativeSalary
2. Enunț: Ce rămâne în salary după un INSERT cu salariul -50, dacă singurul program este AFTER INSERT care face SET NEW.salary = 0?
   Capcană: Rămâne 0, fiindcă AFTER tot vede NEW și tot poate să-l schimbe.
   Cheie: afterSetNewLeavesTheWrittenSalary nu schimbă rândul deja scris. 27.4.1: SET NEW într-un AFTER nu are efect. Dacă coloana a acceptat -50, -50 rămâne.
   Demonstrație: afterSetNewLeavesTheWrittenSalary
3. Enunț: De ce CHECK (salary >= 0) nu este before_salary_insert?
   Capcană: Ambele opresc salariul negativ, deci sunt aceeași regulă, iar trigger-ul ține locul cheii străine când refuzi un părinte lipsă.
   Cheie: CHECK refuză rândul. BEFORE scrie altă valoare și rândul rămâne. Catalogul îi desparte: trigger-ul rulează cod, constrângerea refuză rândul, iar trigger-ul nu ține locul lui FOREIGN KEY.
   Demonstrație: checkRefusesTheNegativeSalary
4. Enunț: Cum eșuează credința că TRUNCATE TABLE salary pornește trigger-ul AFTER DELETE de pe salary?
   Capcană: TRUNCATE șterge rândurile, deci corpul de DELETE rulează pentru fiecare.
   Cheie: truncateDoesNotFireADeleteTrigger nu folosește DELETE. 15.1.27: TRUNCATE TABLE și DROP TABLE nu activează trigger-ul de DELETE. Niciun corp nu vede OLD.
   Demonstrație: truncateDoesNotFireADeleteTrigger
5. Enunț: Când lași CHECK, când lași CALL și când rămâi pe AFTER?
   Capcană: BEFORE este mereu mai sigur, fiindcă poate și refuza, și rescrie, și vede numărul generat.
   Cheie: CHECK când rândul negativ nu are voie să existe. CALL când cineva trebuie să ceară mărirea, nu fiecare INSERT. AFTER când o altă tabelă trebuie schimbată după ce rândul a rămas; la AUTO_INCREMENT, BEFORE vede 0, nu numărul generat.
   Demonstrație: beforeInsertRewritesNegativeSalary

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE salariul negativ rămâne în tabelă, dar ca 0 -> beforeInsertRewritesNegativeSalary -> BEFORE INSERT SET NEW.salary = 0 -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE AFTER ca să corectezi salariul deja scris -> afterSetNewLeavesTheWrittenSalary -> SET NEW.salary = 0 -> nu garantează că -50 devine 0
    3. Granița
           -> Trigger-ul este mecanismul MySQL. Șirul din Java nu este execuția. DELIMITER este comanda clientului mysql, nu propoziția. Click-ul din Workbench nu leagă programul de tabelă.
    4. Alternativa
           -> CHECK când rândul trebuie să cadă. CALL când programul trebuie cerut. AFTER pe altă tabelă când rândul care pornește trigger-ul trebuie să rămână deja scris.
    5. Anti-patternul
           -> SET NEW într-un AFTER, sau TRUNCATE crezut DELETE. Salariul rămâne cel scris, iar corpul de DELETE nu pornește.
    6. Dovada
           -> beforeInsertRewritesNegativeSalary ține CREATE TRIGGER BEFORE INSERT. afterSetNewLeavesTheWrittenSalary ține SET NEW după scriere. root@localhost a fost refuzat cu 1045, using password: NO.
    7. Interviu
           -> BEFORE schimbă valoarea înainte de scriere și rândul rămâne. AFTER nu mai schimbă NEW. CHECK nu lasă rândul. TRUNCATE nu pornește DELETE.
*/

public class Triggers {

    /*
    🟢 Începător
        Ce este            programul care schimbă salariul negativ în 0 înainte ca rândul să fie scris
        De ce e nevoie     fără el, -50 rămâne -50, iar CHECK ar fi șters rândul în loc să-l păstreze
    🟡 Producție
        Cum se scrie       CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW cu SET NEW.salary = 0
    🔴 Staff
        Unde               activarea este înaintea scrierii rândului; AFTER nu mai poate face acest SET; CHECK refuză rândul
    Pași
        1. Ține CREATE TRIGGER pe salary, nu pe un view.
        2. Nu trimite șirul: în tur, root@localhost a primit 1045.
        3. Nu pune DELIMITER în șir: este comanda clientului.
    */
    // Începător: metoda păstrează programul care îndreaptă salariul înainte de scriere.
    // Producție: public static String beforeInsertRewritesNegativeSalary(). Dacă dispare, fast nu mai are alegerea.
    // Expert: Metoda nu deschide JDBC. Nu înlocuiește BEFORE cu CHECK și nu amână efectul până la COMMIT.
    public static String beforeInsertRewritesNegativeSalary() {
        // Începător: la fiecare salariu nou, valoarea negativă devine 0, iar rândul rămâne.
        // Producție: CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW BEGIN IF NEW.salary < 0 THEN SET NEW.salary = 0; END IF; END. Fără șir, rămâne comentariul că trigger-ul rulează după ce propoziția s-a executat.
        // Expert: FOR EACH ROW este o dată pe rând, nu o dată pe propoziție. Verificarea de tip a coloanei a avut loc înainte, deci acest SET nu salvează un text pus într-o coloană întreagă. Trigger-ul nu ține locul lui FOREIGN KEY.
        return "CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW BEGIN IF NEW.salary < 0 THEN SET NEW.salary = 0; END IF; END";
        // Începător: metoda a înapoiat programul, nu un rând de salariu.
        // Producție: acolada închide beforeInsertRewritesNegativeSalary. Șirul nu a fost trimis către 3306.
        // Expert: Refuzul 1045 lasă efectul pe server NERULAT. Acolada nu este un AFTER și nu este TRUNCATE.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a îndrepta salariul după ce rândul a fost deja scris
        De ce e nevoie     fără ea, AFTER pare că poate și el să pună 0
    🟡 Producție
        Cum se scrie       CREATE TRIGGER after_salary_insert AFTER INSERT ON salary FOR EACH ROW SET NEW.salary = 0
    🔴 Staff
        Unde               SET NEW nu are efect după scriere; rândul rămâne cu valoarea acceptată de coloană
    Pași
        1. Ține același SET ca la BEFORE, dar cu AFTER.
        2. Nu îl trimite în tur.
        3. Nu îl numi refuz: rândul nu cade.
    */
    // Începător: metoda păstrează programul care pare că îndreaptă salariul și nu-l îndreaptă.
    // Producție: public static String afterSetNewLeavesTheWrittenSalary(). Dacă dispare, eșecul rămâne doar în comentariu.
    // Expert: Șirul nu este un CHECK și nu este un trigger de DELETE. Eroarea dintr-un AFTER tot face să cadă întreaga propoziție, dar acest SET nu este o eroare: nu are efect.
    public static String afterSetNewLeavesTheWrittenSalary() {
        // Începător: salariul scris rămâne cel din INSERT, chiar dacă programul pune 0 după aceea.
        // Producție: CREATE TRIGGER after_salary_insert AFTER INSERT ON salary FOR EACH ROW SET NEW.salary = 0. Dacă linia dispare, nu mai ai cel mai mic eșec de lângă BEFORE.
        // Expert: AFTER pornește doar dacă BEFORE și scrierea rândului au reușit. Pe InnoDB, o eroare de mai târziu dă propoziția înapoi; acest SET nu este acea eroare. Numărul generat, pe care BEFORE îl vede 0, nu este acest șir.
        return "CREATE TRIGGER after_salary_insert AFTER INSERT ON salary FOR EACH ROW SET NEW.salary = 0";
        // Începător: metoda a păstrat programul care nu schimbă rândul deja scris.
        // Producție: acolada închide afterSetNewLeavesTheWrittenSalary. Șirul a rămas în proces.
        // Expert: Folderul AFTER predă schimbarea altei tabele. Aici rămâne doar faptul că SET NEW este prea târziu.
    }

    /*
    🟢 Începător
        Ce este            regula care nu lasă salariul negativ să rămână deloc
        De ce e nevoie     fără ea, trigger-ul pare că ține locul oricărei reguli
    🟡 Producție
        Cum se scrie       ALTER TABLE salary ADD CONSTRAINT chk_salary CHECK (salary >= 0)
    🔴 Staff
        Unde               expresia FALSE refuză rândul; BEFORE cu SET NEW l-ar fi păstrat cu 0
    Pași
        1. Ține CHECK separat de CREATE TRIGGER.
        2. Nu îl trimite în tur.
        3. Nu îl pune în locul unei chei străine.
    */
    // Începător: metoda păstrează regula care scoate rândul, nu programul care îl îndreaptă.
    // Producție: public static String checkRefusesTheNegativeSalary(). Dacă dispare, vecinul rămâne o poveste.
    // Expert: CHECK nu pornește la TRUNCATE ca un trigger și nu vede OLD. Trigger-ul nu ține locul lui FOREIGN KEY.
    public static String checkRefusesTheNegativeSalary() {
        // Începător: salariul negativ nu rămâne în tabelă, și nu este înlocuit cu 0.
        // Producție: ALTER TABLE salary ADD CONSTRAINT chk_salary CHECK (salary >= 0). Dacă linia dispare, nu mai ai regula cu care se confundă trigger-ul.
        // Expert: Numele chk_salary este unic în schemă la CHECK. BEFORE care face SET NEW.salary = 0 lasă un alt rând decât a trimis clientul. Această regulă nu lasă rândul.
        return "ALTER TABLE salary ADD CONSTRAINT chk_salary CHECK (salary >= 0)";
        // Începător: metoda s-a oprit la regula care refuză.
        // Producție: acolada închide checkRefusesTheNegativeSalary. ALTER nu a rulat.
        // Expert: 1045 a oprit turul înainte de orice refuz de CHECK. FOREIGN KEY rămâne alt folder.
    }

    /*
    🟢 Începător
        Ce este            golirea tabelei, care nu pornește programul de ștergere pe rând
        De ce e nevoie     fără ea, TRUNCATE pare un DELETE pentru fiecare salariu
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE salary
    🔴 Staff
        Unde               DROP TABLE și TRUNCATE nu folosesc DELETE, deci trigger-ul de DELETE nu se activează
    Pași
        1. Ține TRUNCATE, nu DELETE.
        2. Nu o trimite în tur.
        3. Nu o lua drept dovadă că OLD a fost văzut.
    */
    // Începător: metoda păstrează golirea care nu cheamă programul de pe fiecare rând șters.
    // Producție: public static String truncateDoesNotFireADeleteTrigger(). Dacă dispare, rămâne doar comentariul că orice ștergere pornește trigger-ul.
    // Expert: Ștergerea unei partiții nu activează trigger-ul de DELETE. REPLACE, spre deosebire de TRUNCATE, este exemplul din 15.1.27 care șterge un rând și poate activa DELETE.
    public static String truncateDoesNotFireADeleteTrigger() {
        // Începător: tabela salary rămâne fără rânduri, iar programul de DELETE nu pornește.
        // Producție: TRUNCATE TABLE salary. Dacă linia dispare, nu mai ai propoziția care se confundă cu DELETE.
        // Expert: Implicit, nici cascada unei chei străine nu activează trigger-ul. enable_cascade_triggers nu schimbă TRUNCATE. Pe innodb_native_foreign_keys=ON, variabila aceea nu are efect nici la CASCADE.
        return "TRUNCATE TABLE salary";
        // Începător: metoda a păstrat golirea, nu un DELETE.
        // Producție: acolada închide truncateDoesNotFireADeleteTrigger. Tabela nu a fost golită din acest proces.
        // Expert: Accesul refuzat nu dovedește că un trigger de DELETE a tăcut. Șirul nu a plecat.
    }
}
