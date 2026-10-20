package _16_SQL_Variables.USER_DEFINED_VS_SYSTEM_VARIABLES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8745706#questions


/*                                                   User-Defined vs System Variables


    ○ Variables in MySQL can be characterized

                   * according to the way they have been created

    ○ SET @p_emp_no = 0 creează variabila de utilizator pe sesiune. SET SESSION se scrie pentru o variabilă de sistem. max_connections nu are copie de sesiune.

                    * HISTORICAL: Both user-defined and system variables can be set as SESSION variables. ONLY a group onSystem variables can be set as session variables. Manualul 9.7, 15.7.6.1: @@SESSION și SET SESSION se aplică variabilelor de sistem. SET SESSION max_connections dă ERROR 1229. @ nu primește modificatorul @@SESSION.

    ○ SET SESSION max_connections

                    * not works because max_connections variable can be only global

    ○ SET SESSION sql_mode= 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION'

                    * works



                                             1. User-Defined


    ○ Variables that can be set by the user manually.


                                              2 System


    ○ Variables that are pre-defined on our system

                 * the MySQL server

    ○ Only System variables can be

                 * SET as global



 */

/*
TOPIC:
    SET @p_emp_no = 0 creează o variabilă de utilizator pe sesiune. SET SESSION sql_mode schimbă o variabilă de sistem deja definită de server. @ nu este @@SESSION și nu poate fi globală.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied for user 'root'@'localhost' (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Numărul inventat de client trebuie văzut de propoziția următoare pe aceeași conexiune, fără să schimbe sql_mode și fără să fie văzut de alt client.
MECHANISM:
    Serverul ține @p_emp_no doar pe sesiunea care a atribuit-o. Neinițializată, este NULL de tip șir. Atribuirea cu 0 îi fixează tipul întreg. La deconectare este eliberată. O variabilă de sistem are nume dat de server, valoare globală și, unde există, copie de sesiune. @@SESSION nu se aplică lui @.
LOOKALIKES:
    SET SESSION sql_mode este variabila de sistem. DECLARE v_avg_salary moare la END, nu la deconectare. SET GLOBAL max_connections nu are pereche @. SELECT @col FROM t întoarce textul din variabilă, nu coloana al cărei nume stă în @col.
BOOK THEORY:
    Manualul 9.7, 11.4, deschis în tur: @var_name este specifică sesiunii, nu este sensibilă la majuscule, are cel mult 64 de caractere. Alt client nu o vede, cu excepția unui cont care poate citi performance_schema.user_variables_by_thread. SET o atribuie cu = sau :=. În afara lui SET, atribuirea veche cu := este încă acceptată în 9.7 și este anunțată pentru scoatere. Ordinea de evaluare când citești și atribui în aceeași propoziție este nedefinită. @ nu poate fi identificator de tabelă sau coloană. LIMIT nu primește variabila ca literal. 15.7.6.1: modificatorii @@ se aplică doar variabilelor de sistem. SET SESSION max_connections dă ERROR 1229. User-Defined+vs+System+Variables.pdf spune că variabilele de utilizator se setează manual, că cele de sistem sunt predefinite, că doar cele de sistem pot fi globale și că max_connections nu poate fi de sesiune. PDF-ul scrie .max_connections(), care nu este propoziția.
BOOK EXAMPLE:
    setUserVariableOnSession
REPO DEMO:
    src/main/java/_16_SQL_Variables/USER_DEFINED_VS_SYSTEM_VARIABLES/USER_DEFINED_VS_SYSTEM_VARIABLES_.java
BREAK LAB:
    breakSessionOnSystemGlobalOnly ține SET SESSION max_connections = 1000. ERROR 1229. Nu creează @max_connections.
TRADE-OFFS:
    DECLARE când valoarea nu trebuie să supraviețuiască rutinei. SET SESSION când schimbi o variabilă de sistem a conexiunii. Aplicația când nicio propoziție nu citește numărul.
ANTI-PATTERNS:
    SET @col = 'c1' apoi SELECT @col FROM t, așteptând coloana c1. Rezultatul este textul c1, un rând, nu valorile coloanei.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlVariables_Staff_Interview.
NOVEL TRANSFER:
    Scriptul tejghelei ține numărul rețetei în @p_emp_no ca să îl trimită propoziției următoare. Tejgheaua de alături nu îl vede. sql_mode al tejghelei nu s-a schimbat.
SPACED REVIEW:
    O zi: 21 octombrie 2026, @ dispare la deconectare. Șapte zile: 27 octombrie 2026, @ nu înlocuiește numele coloanei. Treizeci de zile: 19 noiembrie 2026, ERROR 1229 nu este o variabilă de utilizator.

1. Enunț: Ce problemă rezolvă SET @p_emp_no = 0?
   Capcană: Creează o variabilă de sistem vizibilă tuturor conexiunilor.
   Cheie: setUserVariableOnSession o pune pe sesiunea curentă. Alt client nu o vede. La deconectare dispare. sql_mode nu se schimbă. Nu se scrie un rând.
   Demonstrație: setUserVariableOnSession
2. Enunț: Ce se întâmplă dacă o citești înainte de SET?
   Capcană: Eroare că variabila nu există, ca o coloană lipsă.
   Cheie: Neinițializată este NULL, tip șir. SET @p_emp_no = 0 îi dă tipul întreg. Efectul atribuirii se vede la sfârșitul lui SET, pe aceeași conexiune. Nu este un index.
   Demonstrație: setUserVariableOnSession
3. Enunț: De ce nu este SET SESSION sql_mode și de ce nu este DECLARE?
   Capcană: @p_emp_no este forma scurtă a lui @@SESSION, iar DECLARE lasă același nume după END.
   Cheie: SET SESSION sql_mode schimbă variabila de sistem. DECLARE moare la END. @ rămâne până la deconectare și nu primește @@SESSION. Doar variabilele de sistem pot fi globale.
   Demonstrație: setUserVariableOnSession
4. Enunț: Cum eșuează SET SESSION max_connections = 1000 dacă voiai o variabilă de utilizator?
   Capcană: Serverul creează @max_connections pe sesiune, cu valoarea 1000.
   Cheie: breakSessionOnSystemGlobalOnly nu are @. Este o variabilă de sistem doar globală. ERROR 1229. Nimic nu se schimbă. În tur nu a fost trimisă.
   Demonstrație: breakSessionOnSystemGlobalOnly
5. Enunț: Când nu folosești @ ca să alegi o coloană?
   Capcană: SET @col = 'c1' face SELECT @col FROM t să citească coloana c1.
   Cheie: @ întoarce valoarea ei, textul c1, nu identificatorul. Ca să citești coloana al cărei nume este într-o variabilă, construiești textul și îl PREPARE. Dacă numărul nu trebuie să intre într-o propoziție, rămâne în aplicație. Dacă moare cu rutina, este DECLARE.
   Demonstrație: setUserVariableOnSession

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE propoziția următoare de pe aceeași conexiune citește numărul -> setUserVariableOnSession -> SET @p_emp_no = 0 -> nu garantează că alt client îl vede și nu schimbă sql_mode
    2. Eșecul cel mai mic
           -> DECIDE scrii SESSION pe max_connections crezând că faci o variabilă de utilizator -> breakSessionOnSystemGlobalOnly -> SET SESSION max_connections = 1000 -> ERROR 1229 în manual; în tur nu a fost citit
    3. Granița
           -> @ și SET SESSION sunt mecanismele. .max_connections() din PDF nu este funcție. performance_schema.user_variables_by_thread este excepția de citire, nu un al doilea domeniu.
    4. Alternativa
           -> DECLARE când valoarea moare la END. SET SESSION când schimbi o variabilă de sistem a conexiunii. Aplicația când nicio propoziție nu o citește.
    5. Anti-patternul
           -> @ ținut ca nume de coloană. SELECT @col întoarce textul, nu coloana.
    6. Dovada
           -> setUserVariableOnSession și breakSessionOnSystemGlobalOnly. root fără parolă a fost refuzat.
    7. Interviu
           -> @ este pe sesiune, dispare la deconectare și nu este @@SESSION. Variabila de sistem o setează serverul dinainte. max_connections nu acceptă SET SESSION.
*/

public class USER_DEFINED_VS_SYSTEM_VARIABLES_ {

    /*
    🟢 Începător
        Ce este            un număr inventat de client, vizibil pe conexiunea lui
        De ce e nevoie     fără propoziție, cursul pare că orice variabilă setată manual este și de sistem
    🟡 Producție
        Cum se scrie       SET @p_emp_no = 0
    🔴 Staff
        Unde               sesiunea clientului, la sfârșitul lui SET. Tipul devine întreg. Altă sesiune nu o vede. Nu intră în binary log
    Pași
        1. Ține atribuirea cu @.
        2. Nu o trimite.
        3. Nu pune @@SESSION în fața ei.
    */
    // Începător: metoda păstrează numărul pe care propoziția următoare de pe aceeași conexiune îl poate citi.
    // Producție: public static String setUserVariableOnSession(). Dacă dispare, nu mai ai @ lângă SET SESSION.
    // Expert: Nu deschide JDBC. Nu schimbă sql_mode. Citirea din performance_schema este excepția din 11.4, nu această linie.
    public static String setUserVariableOnSession() {
        // Începător: conexiunea ține 0 sub numele @p_emp_no. Un alt client nu primește 0. Nu se scrie un rând.
        // Producție: SET @p_emp_no = 0. În SET, = și := sunt ambele atribuire. Dacă linia dispare, rămâne doar descrierea din comentariu.
        // Expert: În afara lui SET, = ar fi comparație, iar := este forma veche, încă în 9.7. Neinițializat ar fi fost NULL șir. Linia nu este identificator de coloană și nu ține lacăt. În tur nu a fost trimisă.
        return "SET @p_emp_no = 0";
        // Începător: metoda a înapoiat atribuirea, nu un angajat scris în tabelă.
        // Producție: acolada închide setUserVariableOnSession. Șirul nu a plecat către 3306.
        // Expert: Refuzul root@localhost lasă @ NERULAT. Acolada nu eliberează variabila: o eliberează deconectarea.
    }

    /*
    🟢 Începător
        Ce este            o variabilă de sistem doar globală, scrisă ca și cum ar fi de sesiune
        De ce e nevoie     fără ea, pare că orice nume primită de utilizator devine @
    🟡 Producție
        Cum se scrie       SET SESSION max_connections = 1000
    🔴 Staff
        Unde               ERROR 1229. Nu se creează @max_connections și nu se schimbă limita
    Pași
        1. Ține propoziția fără @.
        2. Nu o trimite.
    */
    // Începător: metoda păstrează confuzia dintre o variabilă de sistem și una inventată de client.
    // Producție: public static String breakSessionOnSystemGlobalOnly(). Dacă dispare, granița nu mai are șir.
    // Expert: Nu este SET @p_emp_no. Nu este privilegiul SYSTEM_VARIABLES_ADMIN, care ar apărea la SET GLOBAL reușit ca sintaxă dar refuzat ca drept.
    public static String breakSessionOnSystemGlobalOnly() {
        // Începător: serverul nu inventează o variabilă de utilizator și nu mută limita pe conexiune.
        // Producție: SET SESSION max_connections = 1000. Manualul arată ERROR 1229 (HY000). Dacă linia dispare, rămâne doar SET @p_emp_no.
        // Expert: @@SESSION.max_connections este aceeași greșeală de domeniu. O atribuire eșuată nu schimbă restul unui SET cu mai multe nume. În tur nu a fost trimisă.
        return "SET SESSION max_connections = 1000";
        // Începător: metoda s-a oprit la propoziția care nu creează @.
        // Producție: acolada închide breakSessionOnSystemGlobalOnly.
        // Expert: ERROR 1229 nu a fost citit de pe server. Acolada nu scrie în user_variables_by_thread.
    }
}
