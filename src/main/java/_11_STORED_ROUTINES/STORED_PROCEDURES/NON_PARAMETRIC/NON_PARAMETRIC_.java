package _11_STORED_ROUTINES.STORED_PROCEDURES.NON_PARAMETRIC;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692214#questions


/*                                                       CREATE PROCEDURES SYNTAX

    ○ DROP PROCEDURE IF EXISTS procedure_name;
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



 */

/*
TOPIC:
    Procedura fără parametri tot are lista (), la CREATE. La CALL, CALL list_limit și CALL list_limit() sunt același apel. Corpul nu primește un emp_no: citește ce este scris în el.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    O listă fixă, LIMIT 1000 pe employees, stă pe server și se pornește fără să trimiți un număr. CREATE nu întoarce rândurile. CALL le trimite.
MECHANISM:
    Serverul execută șirul din procedureWithoutParameters. 15.1.22: dacă nu există parametri, lista goală () este obligatorie la CREATE. 15.2.1: la invocare, CALL p și CALL p() sunt echivalente.
LOOKALIKES:
    emp_salary(IN p_emp_no) cere argument la CALL. Funcția fără parametri tot se cheamă într-o expresie, nu cu CALL. DELIMITER nu înlocuiește ().
BOOK THEORY:
    Manualul 9.7, 15.1.22 și 15.2.1, deschise în tur: lista de parametri trebuie să fie prezentă; CALL fără argumente poate omite parantezele doar la invocare. Nu există PDF în acest folder. Stored+Procedures.pdf, deschis la părinte, spune că procedura poate fi creată fără parametri și că parantezele rămân la nume.
BOOK EXAMPLE:
    procedureWithoutParameters
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_PROCEDURES/NON_PARAMETRIC/NON_PARAMETRIC_.java
BREAK LAB:
    createMissingEmptyList ține CREATE PROCEDURE list_limit BEGIN fără (). Lista lipsește. În tur șirul nu a fost trimis.
TRADE-OFFS:
    IN când fiecare CALL trebuie să aleagă alt emp_no. Propoziția din client când LIMIT 1000 se schimbă la fiecare rulare.
ANTI-PATTERNS:
    CREATE PROCEDURE list_limit BEGIN, copiind ideea că CALL list_limit poate sta fără paranteze.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    O procedură care listează rafturile pline nu primește un cod. CREATE tot cere (). CALL poate fi scris cu sau fără paranteze. Un cod diferit la fiecare apel cere IN, nu această formă.
SPACED REVIEW:
    O zi: 21 octombrie 2026, () la CREATE. Șapte zile: 27 octombrie 2026, CALL list_limit și CALL list_limit() sunt același apel. Treizeci de zile: 19 noiembrie 2026, un emp_no schimbător cere IN.

1. Enunț: Ce problemă rezolvă o procedură fără parametri?
   Capcană: Fără parametri, serverul nu poate salva rutina, deci lista goală este o eroare.
   Cheie: procedureWithoutParameters salvează un corp fix, SELECT cu LIMIT 1000. Nu primește emp_no. Rândurile pleacă la CALL, nu la CREATE.
   Demonstrație: procedureWithoutParameters
2. Enunț: Ce se întâmplă cu rândurile la CREATE PROCEDURE list_limit()?
   Capcană: LIMIT 1000 din corp șterge restul rândurilor din employees.
   Cheie: CREATE schimbă schema. SELECT nu rulează încă, deci nu citește și nu șterge. CALL list_limit trimite cel mult 1000 de rânduri în rezultat și lasă tabela întreagă.
   Demonstrație: procedureWithoutParameters
3. Enunț: De ce list_limit nu este emp_salary(IN p_emp_no)?
   Capcană: Ambele se scriu fără argument, pentru că parantezele sunt opționale peste tot.
   Cheie: emp_salary fără argument nu are valoarea din WHERE. list_limit nu are ce primi. callEitherParentheses este echivalența de la 15.2.1, doar când nu există argumente.
   Demonstrație: callEitherParentheses
4. Enunț: Cum eșuează CREATE PROCEDURE list_limit BEGIN fără ()?
   Capcană: Este legal, pentru că CALL list_limit este legal fără paranteze.
   Cheie: createMissingEmptyList confundă invocarea cu definiția. 15.1.22 cere () la CREATE. 15.2.1 permite omiterea doar la CALL fără argumente.
   Demonstrație: createMissingEmptyList
5. Enunț: Când adaugi un IN și când lași procedura fără parametri?
   Capcană: Orice procedură serioasă are cel puțin un parametru, altfel nu este rutină.
   Cheie: Fără parametri când corpul este același la fiecare CALL. IN când emp_no se schimbă. Funcția fără parametri când valoarea unică intră într-o expresie și nu vrei un set de 1000 de rânduri.
   Demonstrație: callEitherParentheses

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE același LIMIT 1000, fără emp_no -> procedureWithoutParameters -> CREATE PROCEDURE list_limit() BEGIN SELECT e.emp_no FROM employees e LIMIT 1000; END -> nu garantează 1000 de rânduri: tabela poate avea mai puține
    2. Eșecul cel mai mic
           -> DECIDE CREATE fără paranteze, ca la CALL -> createMissingEmptyList -> CREATE PROCEDURE list_limit BEGIN -> nu garantează că serverul a acceptat forma lui CALL
    3. Granița
           -> () la CREATE este sintaxa rutinei. CALL list_limit fără paranteze este invocarea. DELIMITER nu ține locul listei.
    4. Alternativa
           -> IN p_emp_no când filtrul se schimbă. Funcția când rezultatul este o valoare, nu 1000 de rânduri.
    5. Anti-patternul
           -> CREATE PROCEDURE list_limit BEGIN, copiat din „CALL merge și fără paranteze”. Lista lipsește și rutina nu se salvează.
    6. Dovada
           -> procedureWithoutParameters ține (). callEitherParentheses ține ambele CALL. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> CREATE cere (). CALL list_limit și CALL list_limit() sunt același apel. Corpul nu primește emp_no. CREATE nu trimite rândurile.
*/

public class NON_PARAMETRIC_ {

    /*
    🟢 Începător
        Ce este            o procedură salvată, care nu cere un număr când o pornești
        De ce e nevoie     fără (), serverul nu o ia drept procedură fără parametri
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE list_limit() BEGIN SELECT e.emp_no FROM employees e LIMIT 1000; END
    🔴 Staff
        Unde               schema se schimbă la CREATE. Rândurile din rezultat apar la CALL. LIMIT nu șterge tabela
    Pași
        1. Pune () după nume.
        2. Nu pune un IN.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează procedura fără parametri, cu lista goală.
    // Producție: public static String procedureWithoutParameters(). Dacă dispare, () rămâne doar în comentariu.
    // Expert: () nu este un parametru opțional. Este lista obligatorie, goală.
    public static String procedureWithoutParameters() {
        // Începător: serverul ține o listă fixă de numere de angajat, fără să întrebe pe cine.
        // Producție: CREATE PROCEDURE list_limit() BEGIN SELECT e.emp_no FROM employees e LIMIT 1000; END. Fără șir, comentariul „work without parenthesis” pare că se aplică și la CREATE.
        // Expert: SELECT fără INTO este legal aici și interzis într-o funcție. Nu există OUT de citit după CALL.
        return "CREATE PROCEDURE list_limit() BEGIN SELECT e.emp_no FROM employees e LIMIT 1000; END";
        // Începător: metoda a înapoiat procedura, nu rândurile.
        // Producție: acolada închide procedureWithoutParameters. Șirul nu a creat list_limit.
        // Expert: Refuzul root@localhost lasă efectul NERULAT. Acolada nu este CALL.
    }

    /*
    🟢 Începător
        Ce este            cele două feluri de a porni aceeași procedură fără argumente
        De ce e nevoie     fără ele, parantezele de la CALL par o altă procedură
    🟡 Producție
        Cum se scrie       CALL list_limit; CALL list_limit()
    🔴 Staff
        Unde               15.2.1 le face echivalente. Nu scutesc () de la CREATE
    Pași
        1. Scrie ambele apeluri.
        2. Nu le trimite în tur.
        3. Nu le folosi ca model pentru CREATE.
    */
    // Începător: metoda păstrează cele două apeluri care cer același corp.
    // Producție: public static String callEitherParentheses(). Dacă dispare, echivalența rămâne o frază.
    // Expert: Echivalența dispare dacă procedura are un IN: CALL emp_salary fără argument nu filtrează.
    public static String callEitherParentheses() {
        // Începător: ambele apeluri cer lista de numere, fără să trimită un emp_no.
        // Producție: CALL list_limit; CALL list_limit(). Dacă linia dispare, nu mai ai perechea din 15.2.1.
        // Expert: A doua formă nu este CREATE PROCEDURE list_limit(). Prima nu este o procedură cu parametru opțional.
        return "CALL list_limit; CALL list_limit()";
        // Începător: metoda s-a oprit la apeluri.
        // Producție: acolada închide callEitherParentheses. Niciun set nu a fost citit.
        // Expert: Nu este SELECT list_limit(). O funcție s-ar evalua așa. Procedura nu stă în expresie.
    }

    /*
    🟢 Începător
        Ce este            CREATE-ul care copiază apelul fără paranteze
        De ce e nevoie     fără el, cele două omisiuni par aceeași regulă
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE list_limit BEGIN SELECT e.emp_no FROM employees e LIMIT 1000; END
    🔴 Staff
        Unde               lista lipsește la definiție. 15.1.22 nu acceptă forma pe care 15.2.1 o acceptă la CALL
    Pași
        1. Scoate () de la CREATE.
        2. Nu trimite șirul.
        3. Nu îl numi apel.
    */
    // Începător: metoda păstrează definiția căreia îi lipsește lista.
    // Producție: public static String createMissingEmptyList(). Dacă dispare, eșecul se confundă cu CALL list_limit.
    // Expert: Eșecul este sintactic la CREATE. Nu este ER_SP_NO_RETSET.
    public static String createMissingEmptyList() {
        // Începător: serverul nu salvează rutina dacă după nume nu vine lista, măcar goală.
        // Producție: CREATE PROCEDURE list_limit BEGIN SELECT e.emp_no FROM employees e LIMIT 1000; END. Dacă linia dispare, nu mai ai CREATE-ul fără ().
        // Expert: CALL list_limit rămâne legal după un CREATE care a avut (). Această linie nu a avut lista.
        return "CREATE PROCEDURE list_limit BEGIN SELECT e.emp_no FROM employees e LIMIT 1000; END";
        // Începător: metoda a păstrat forma greșită.
        // Producție: acolada închide createMissingEmptyList. Șirul a rămas în proces.
        // Expert: Nu compara cu un IN lipsă la CALL emp_salary. Aici lipsește lista din definiție.
    }
}
