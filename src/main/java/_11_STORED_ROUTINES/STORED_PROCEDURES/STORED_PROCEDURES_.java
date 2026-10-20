package _11_STORED_ROUTINES.STORED_PROCEDURES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692208#questions

// https://dev.mysql.com/doc/refman/8.0/en/create-procedure.html


/*                                           The MySQL Syntax for Stored Procedures


    ○ semicolons ;

                  * they function as a statement terminator

                                - technically, they can also be called delimiters

    ○ DELIMITER $$    OR  DELIMITER //

                  * by typing DELIMITER $$,

                                - you’ll be able to use the dollar symbols as your delimiter


                                        CREATE PROCEDURES SYNTAX


    1. DROP PROCEDURE IF EXISTS procedure_name;
      DELIMITER $$
      CREATE PROCEDURE procedure_name(param_1, param_2)
       BEGIN
          SELECT * FROM employees
          LIMIT 1000;
      END$$

      DELIMITER;  -- reset delimiter to;

                  *  Parameters represent certain values that the procedure

                                 - will use to complete the calculation it is supposed to execute


    2. DROP PROCEDURE IF EXISTS procedure_name;
        DELIMITER $$
      CREATE PROCEDURE procedure_name()
      BEGIN
          SELECT * FROM employees
          LIMIT 1000;
      END$$

      DELIMITER;   -- reset delimiter to;


                   * A procedure can be created without parameters too!

                   * Nevertheless, the parentheses must always be attached to its name


                                          CALL PROCEDURE


    1. CALL stored_procedure();

                 * if database is selected

                 *  work without parenthesis

    2. CALL database_name.stored_procedure();

                 * work without parenthesis

    3. from WorkBench

                * https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692216#questions

 */

/*
TOPIC:
    Procedura stocată este rutina pe care o pornești cu CALL. CREATE o pune în schemă. Corpul poate trimite rânduri. Parantezele goale rămân la nume chiar dacă nu există parametri.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    JOIN-ul pe employees și salaries pentru un emp_no stă o dată pe server. După CREATE, rândurile nu s-au schimbat. După CALL emp_salary(10001) clientul primește setul.
MECHANISM:
    Serverul execută șirul din createEmpSalaryTheServerStores. DELIMITER nu este în șir: este comanda clientului mysql, citită în Stored+Procedures.pdf. Efectul pe schemă se vede la sfârșitul lui CREATE. Setul se vede la sfârșitul lui CALL.
LOOKALIKES:
    CREATE FUNCTION f_emp_avg_salary are RETURNS și nu se pornește cu CALL. CALL list_limit fără argumente este procedura fără parametri, nu aceasta. Un SELECT trimis din client nu lasă obiectul emp_salary în schemă.
BOOK THEORY:
    Manualul 9.7, 15.1.22, deschis în tur: CREATE PROCEDURE cere CREATE ROUTINE; lista de parametri este obligatorie; IN este implicit; corpul poate fi un SELECT care trimite un set. 15.2.1: CALL invocă procedura creată înainte. Stored+Procedures.pdf, deschis în tur: punctul și virgula termină propoziția pentru client, iar DELIMITER $$ schimbă acest terminator ca să poți scrie corpul.
BOOK EXAMPLE:
    createEmpSalaryTheServerStores
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_PROCEDURES/STORED_PROCEDURES_.java
BREAK LAB:
    createWithoutParentheses ține CREATE PROCEDURE emp_salary BEGIN fără (). 15.1.22 cere lista, chiar goală. În tur șirul nu a fost trimis.
TRADE-OFFS:
    Funcția când îți trebuie o singură valoare în SELECT. Propoziția trimisă din client când rutina ar fi folosită o singură dată și nu vrei un obiect de șters.
ANTI-PATTERNS:
    Să trimiți DELIMITER $$ către server ca și cum ar fi prima propoziție din corp.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos. Banca de 50 stă în StoredRoutines_Staff_Interview, pe alegerea dintre copii.
NOVEL TRANSFER:
    O etichetă de raft salvată ca procedură: CALL o tipărește. CREATE nu mută coletul.
SPACED REVIEW:
    O zi: 21 octombrie 2026, CREATE nu citește salariul. Șapte zile: 27 octombrie 2026, fără () procedura nu se creează. Treizeci de zile: 19 noiembrie 2026, DELIMITER rămâne la client.

1. Enunț: Ce problemă rezolvă CREATE PROCEDURE emp_salary față de JOIN-ul rescris în fiecare client?
   Capcană: După CREATE, salariul lui 10001 este deja în rezultat, deci CALL este opțional.
   Cheie: createEmpSalaryTheServerStores salvează rutina. Rândurile pleacă la CALL emp_salary(10001), ținut în callEmpSalary. Până atunci schema are procedura și salaries este neatins.
   Demonstrație: createEmpSalaryTheServerStores
2. Enunț: Ce se întâmplă cu schema și cu rândurile la sfârșitul lui CREATE, înainte de CALL?
   Capcană: SELECT-ul din corp ține lacăt pe emp_no pe toată durata în care procedura există.
   Cheie: Se schimbă schema. Corpul nu rulează la CREATE, deci nu alege index și nu ține lacăt. Lacătul, dacă planul îl cere, apare la CALL, pe durata SELECT-ului, nu cât trăiește rutina.
   Demonstrație: createEmpSalaryTheServerStores
3. Enunț: De ce emp_salary nu este f_emp_avg_salary?
   Capcană: Ambele se pornesc cu CALL și ambele pot sta în lista unui SELECT.
   Cheie: callEmpSalary este CALL. Funcția are RETURNS și se evaluează într-o expresie. Procedura poate trimite setul din SELECT fără INTO. Funcția, cu același SELECT, este ER_SP_NO_RETSET.
   Demonstrație: callEmpSalary
4. Enunț: Cum eșuează CREATE PROCEDURE emp_salary BEGIN fără paranteze?
   Capcană: Parantezele sunt necesare doar când există un parametru, deci lipsa lor este legală.
   Cheie: createWithoutParentheses omite lista. 15.1.22: lista este obligatorie; fără parametri se scrie (). CALL fără paranteze este alt fapt: 15.2.1 permite CALL p și CALL p() doar la invocare, când nu există argumente.
   Demonstrație: createWithoutParentheses
5. Enunț: Când trimiți JOIN-ul din client și când îl lași în procedură?
   Capcană: Orice JOIN pe salaries trebuie să fie procedură, altfel optimizerul nu îl vede.
   Cheie: Procedura când mai mulți clienți trebuie să cheme același corp și să aibă EXECUTE, nu dreptul pe tabele. Propoziția din client când corpul se schimbă des sau se rulează o dată. Funcția când rezultatul este o valoare în expresie, nu setul de rânduri.
   Demonstrație: callEmpSalary

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE același JOIN pe emp_no stă pe server -> createEmpSalaryTheServerStores -> CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) ... END -> nu garantează rânduri: CALL nu este în acest șir
    2. Eșecul cel mai mic
           -> DECIDE procedura fără paranteze la CREATE -> createWithoutParentheses -> CREATE PROCEDURE emp_salary BEGIN -> nu garantează textul erorii în tur: autentificarea a căzut
    3. Granița
           -> CREATE și CALL sunt mecanismul. DELIMITER $$ este clientul mysql. Workbench nu salvează rutina prin click, ci prin propoziția pe care o trimite.
    4. Alternativa
           -> Funcția când valoarea unică intră în SELECT. CALL fără argumente când procedura nu are parametri. Propoziția din client când nu vrei obiect în schemă.
    5. Anti-patternul
           -> DELIMITER $$ trimis serverului înainte de CREATE. Serverul nu schimbă terminatorul din această comandă de client, iar corpul nu se salvează.
    6. Dovada
           -> createEmpSalaryTheServerStores ține CREATE. callEmpSalary ține CALL emp_salary(10001). root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> CREATE pune emp_salary în schemă și nu citește salariul. CALL trimite setul. Fără () la CREATE procedura nu există. DELIMITER nu face parte din rutină.
*/

public class STORED_PROCEDURES_ {

    /*
    🟢 Începător
        Ce este            rutina salvată, care știe să citească un angajat și salariul lui
        De ce e nevoie     fără ea, fiecare client retrimite aceleași propoziții
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.first_name, e.last_name, s.salary FROM employees e JOIN salaries s ON e.emp_no = s.emp_no WHERE e.emp_no = p_emp_no; END
    🔴 Staff
        Unde               obiectul apare la sfârșitul lui CREATE. Setul apare la CALL. DELIMITER nu este în șir
    Pași
        1. Pune lista de parametri, cu un IN.
        2. Lasă SELECT fără INTO, legal în procedură.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează procedura pe care serverul o poate salva.
    // Producție: public static String createEmpSalaryTheServerStores(). Dacă dispare, folderul nu mai are CREATE-ul gata de trimis.
    // Expert: Șirul nu deschide JDBC. Nu este CREATE FUNCTION și nu cere RETURNS.
    public static String createEmpSalaryTheServerStores() {
        // Începător: serverul ține minte cum se citesc numele și salariul pentru un număr.
        // Producție: CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.first_name, e.last_name, s.salary FROM employees e JOIN salaries s ON e.emp_no = s.emp_no WHERE e.emp_no = p_emp_no; END. Fără șir, rămâne doar comentariul cu DELIMITER.
        // Expert: IN nu întoarce o valoare schimbată în corp. Același SELECT într-o funcție este ER_SP_NO_RETSET. Șirul nu conține DELIMITER $$.
        return "CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.first_name, e.last_name, s.salary FROM employees e JOIN salaries s ON e.emp_no = s.emp_no WHERE e.emp_no = p_emp_no; END";
        // Începător: metoda a înapoiat procedura, nu salariul.
        // Producție: acolada închide createEmpSalaryTheServerStores. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă rutina NERULAT. Acolada nu este CALL.
    }

    /*
    🟢 Începător
        Ce este            pornirea procedurii pentru angajatul 10001
        De ce e nevoie     fără ea, pare că CREATE a și citit rândurile
    🟡 Producție
        Cum se scrie       CALL emp_salary(10001)
    🔴 Staff
        Unde               setul se vede la sfârșitul lui CALL. Nu este SELECT emp_salary(10001)
    Pași
        1. Ține CALL cu argumentul.
        2. Nu îl trimite în tur.
        3. Nu pune procedura în lista de SELECT.
    */
    // Începător: metoda păstrează apelul care chiar cere rândurile.
    // Producție: public static String callEmpSalary(). Dacă dispare, momentul în care se văd rândurile rămâne o poveste.
    // Expert: CALL nu este invocarea unei funcții. ROW_COUNT() după CALL privește ultima propoziție din corp, după 15.2.1, nu numărul de rutine din schemă.
    public static String callEmpSalary() {
        // Începător: clientul cere rândurile angajatului 10001 abia aici.
        // Producție: CALL emp_salary(10001). Dacă linia dispare, rămâne CREATE-ul, care nu citește salariul.
        // Expert: Argumentul este IN. Nu se întoarce într-o variabilă de utilizator. Pentru OUT ai nevoie de @variabilă, nu de un literal.
        return "CALL emp_salary(10001)";
        // Începător: metoda s-a oprit la apel, fără să îl trimită.
        // Producție: acolada închide callEmpSalary. Setul nu a fost citit.
        // Expert: Nu este CALL list_limit, procedura fără parametri. Parantezele de aici țin argumentul.
    }

    /*
    🟢 Începător
        Ce este            procedura scrisă fără paranteze la CREATE
        De ce e nevoie     fără ea, CALL p fără paranteze pare că scutește și CREATE-ul
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE emp_salary BEGIN SELECT e.emp_no FROM employees e LIMIT 1; END
    🔴 Staff
        Unde               eșecul este la CREATE. 15.2.1 permite omiterea parantezelor doar la CALL, când nu există argumente
    Pași
        1. Omite () după nume.
        2. Nu trimite șirul.
        3. Nu îl confunda cu CALL emp_salary.
    */
    // Începător: metoda păstrează CREATE-ul căruia îi lipsesc parantezele.
    // Producție: public static String createWithoutParentheses(). Dacă dispare, eșecul rămâne în comentariul de curs.
    // Expert: () gol este legal și obligatoriu fără parametri. Lipsa listei nu este IN.
    public static String createWithoutParentheses() {
        // Începător: serverul nu salvează procedura dacă numele nu are lista de parametri.
        // Producție: CREATE PROCEDURE emp_salary BEGIN SELECT e.emp_no FROM employees e LIMIT 1; END. Dacă linia dispare, nu mai ai forma fără ().
        // Expert: CALL p și CALL p() sunt echivalente la invocare fără argumente. Această linie este CREATE, nu CALL.
        return "CREATE PROCEDURE emp_salary BEGIN SELECT e.emp_no FROM employees e LIMIT 1; END";
        // Începător: metoda a păstrat forma greșită.
        // Producție: acolada închide createWithoutParentheses. Șirul a rămas în proces.
        // Expert: Eșecul nu este ER_SP_NO_RETSET. Acela este un SELECT fără INTO într-o funcție.
    }
}
