package _16_SQL_Variables;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692226#questions


/*                                             VARIABLES


    ○ Types of variables by scope:

                  * Local variables

                                - a variable that is visible only in the BEGIN – END block

                                - in which it was created.

                                - DECLARE is a keyword that can be used when creating local variables only

                                 - v_avg_salary is visible only in the BEGIN - END block

                  * Session variables

                  * Global variable


                                            Create a variable


    ○ set @p_emp_no = 0;                    -- convention is to use 0;


                                            Get variable


    ○ select @p_emp_no;

    ○ When you are defining a program,

                   * such as a stored procedure for instance,

                   * you can say you are using ‘parameters’ Variables

    ○ DELIMITER $$
      CREATE PROCEDURE procedure_name (in parameter , out parameter )

    ○ ‘parameters’ are a more abstract term


    ○ Once the structure has been solidified,

                  * Then it will be applied to the database.

    ○ The input value you insert

                  * is typically referred to as the ‘argument’,

                  * while the obtained output value is stored in a ‘variable’


                                       Implement a parameterized procedure


    ○ set @p_emp_no = 0;                    -- convention is to use 0;

      call employees.emp_info('Georgi', 'Facello', @p_emp_no);

      select @p_emp_no;



 */

/*
TOPIC:
    Alegi domeniul variabilei: SET SESSION schimbă copia de pe conexiunea curentă, SET GLOBAL o pregătește doar pentru conexiunile care se deschid după, DECLARE moare la END, iar @ rămâne pe sesiune până la deconectare.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied for user 'root'@'localhost' (using password: NO). Efectul pe server rămâne NERULAT AICI. EXPLAIN nu a fost citit.
INTENT:
    Valoarea trebuie văzută de propoziția următoare pe aceeași conexiune, ori doar în rutină, ori de conexiunile noi. După alegere, niciun rând stocat nu se schimbă.
MECHANISM:
    Serverul execută SET. Pentru o variabilă de sistem cu valoare de sesiune, sesiunea a copiat globalul la connect. SET SESSION sql_mode = 'TRADITIONAL' schimbă copia asta la sfârșitul propoziției. SET GLOBAL nu o rescrie, nici măcar în sesiunea care a trimis propoziția. SET nu se scrie în binary log.
LOOKALIKES:
    SET GLOBAL max_connections = 1000 nu este SET SESSION. max_connections este doar globală: SET SESSION max_connections dă ERROR 1229 și nu schimbă nicio variabilă. DECLARE v_avg_salary trăiește în BEGIN...END. SET @p_emp_no = 0 este variabila de utilizator, nu @@SESSION.sql_mode. Parametrul IN/OUT din PDF este rutina din _11_STORED_ROUTINES.
BOOK THEORY:
    Manualul 9.7, 15.7.6.1, deschis în tur: SET SESSION, SET LOCAL, @@SESSION și @@ schimbă valoarea de sesiune până la o nouă atribuire sau până la sfârșitul sesiunii. SET GLOBAL actualizează valoarea globală folosită la inițializarea sesiunilor noi și nu atinge sesiunile deja deschise. Dacă o atribuire din SET eșuează, nu se schimbă nicio variabilă și nici mysqld-auto.cnf. SET nu intră în binary log. 7.1.9: SHOW VARIABLES fără GLOBAL întoarce valorile de sesiune. 11.4: @var este specifică sesiunii, neinițializată este NULL de tip șir, și dispare când clientul iese. 15.6.4.1: DECLARE are domeniul blocului BEGIN...END; fără DEFAULT, valoarea inițială este NULL. 7.1.9.1: SET GLOBAL cere SYSTEM_VARIABLES_ADMIN sau SUPER, deja învechit. SET SESSION, pentru sql_mode, nu cere privilegiu special. Variables.pdf nu conține SET SESSION și nu conține DECLARE: desparte parametrul, argumentul și variabila de ieșire a procedurii. Global+Variables.pdf spune că globalul se aplică tuturor conexiunilor și că max_join_size este memorie; ambele contrazic 15.7.6.1 și max_join_size din 7.1.8. Local+Variables.pdf spune că locala se vede doar în BEGIN...END și că DECLARE creează doar variabile locale. User-Defined+vs+System+Variables.pdf spune că doar variabilele de sistem pot fi globale și că unele, cum este max_connections, nu au valoare de sesiune. În SESSION_VARIABLES nu există PDF.
BOOK EXAMPLE:
    sessionCopyTheCurrentConnectionKeeps
REPO DEMO:
    src/main/java/_16_SQL_Variables/SQL_VARIABLES.java
BREAK LAB:
    breakSessionKeywordOnGlobalOnly ține SET SESSION max_connections = 1000. Manualul 9.7 arată ERROR 1229 (HY000): variabila este GLOBAL și trebuie SET GLOBAL. În tur nu a fost trimisă.
TRADE-OFFS:
    SET PERSIST scrie și mysqld-auto.cnf, tot fără să rescrie sesiunile deschise, și cere același privilegiu ca SET GLOBAL. Valoarea de care nu are nevoie nicio propoziție de pe server rămâne în aplicație. Parametrul procedurii rămâne în _11_STORED_ROUTINES.
ANTI-PATTERNS:
    SET GLOBAL sql_mode = 'TRADITIONAL' când voiai doar conexiunea asta: copia @@SESSION.sql_mode rămâne cea de la connect, iar conexiunile noi primesc modul nou.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlVariables_Staff_Interview. Cheia este scrisă acolo. Nu există main care să o citească.
NOVEL TRANSFER:
    Ghișeul unei farmacii trebuie să refuze o dată invalidă doar pe conexiunea tejghelei deschise. SET SESSION sql_mode = 'TRADITIONAL' ține regula pe tejghea. SET GLOBAL ar copia regula la următoarea conectare, nu ar schimba tejgheaua deja deschisă. Totalul de rețete @ din script nu este sql_mode.
SPACED REVIEW:
    O zi: 21 octombrie 2026, SET GLOBAL nu rescrie @@SESSION deja copiat. Șapte zile: 27 octombrie 2026, SET SESSION max_connections este ERROR 1229. Treizeci de zile: 19 noiembrie 2026, valoarea care nu trebuie văzută de o altă propoziție pe server rămâne în aplicație.

1. Enunț: Ce problemă rezolvă alegerea dintre SET SESSION și SET GLOBAL?
   Capcană: Orice SET schimbă serverul pentru toți clienții deja conectați.
   Cheie: sessionCopyTheCurrentConnectionKeeps ține SET SESSION sql_mode = 'TRADITIONAL'. Se schimbă copia de pe conexiunea curentă, la sfârșitul propoziției. Celelalte conexiuni își păstrează copia. Niciun rând stocat nu se scrie.
   Demonstrație: sessionCopyTheCurrentConnectionKeeps
2. Enunț: Ce rămâne în tabele după SET SESSION sql_mode = 'TRADITIONAL'?
   Capcană: sql_mode se salvează ca rând și se vede după COMMIT.
   Cheie: Nu este numită o tabelă. Nu apare rând, index sau lacăt. Copia de sesiune se vede la sfârșitul lui SET, pe conexiunea care l-a trimis. SET nu intră în binary log.
   Demonstrație: sessionCopyTheCurrentConnectionKeeps
3. Enunț: De ce SET GLOBAL max_connections = 1000 nu este aceeași alegere?
   Capcană: GLOBAL rescrie și @@SESSION.sql_mode al conexiunii care a trimis propoziția.
   Cheie: max_connections este doar globală. SET GLOBAL schimbă valoarea folosită la conectările următoare și nu rescrie sesiunile deschise. SET SESSION max_connections nu o transformă în variabilă de sesiune: este ERROR 1229 și nu schimbă nicio variabilă.
   Demonstrație: breakSessionKeywordOnGlobalOnly
4. Enunț: Cum eșuează SET SESSION max_connections = 1000?
   Capcană: Serverul mută limita pe sesiune și conexiunea curentă poate deschide 1000 de fire.
   Cheie: breakSessionKeywordOnGlobalOnly este propoziția. Manualul arată ERROR 1229. Dacă o atribuire din SET eșuează, nu se schimbă nicio variabilă. În tur propoziția nu a fost trimisă.
   Demonstrație: breakSessionKeywordOnGlobalOnly
5. Enunț: Când lași valoarea în aplicație și când folosești DECLARE sau @?
   Capcană: Orice număr trebuie să fie @@SESSION, altfel serverul nu îl vede.
   Cheie: Dacă nicio propoziție de pe server nu trebuie să citească numărul, el rămâne în aplicație. DECLARE v_avg_salary moare la END. SET @p_emp_no = 0 rămâne pe sesiune până la deconectare și nu schimbă sql_mode. SET SESSION este pentru variabila de sistem a conexiunii curente.
   Demonstrație: sessionCopyTheCurrentConnectionKeeps

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE doar conexiunea asta trebuie să primească sql_mode TRADITIONAL -> sessionCopyTheCurrentConnectionKeeps -> SET SESSION sql_mode = 'TRADITIONAL' -> nu garantează că o conexiune deja deschisă de alt client s-a schimbat
    2. Eșecul cel mai mic
           -> DECIDE pui SESSION pe o variabilă doar globală -> breakSessionKeywordOnGlobalOnly -> SET SESSION max_connections = 1000 -> ERROR 1229 în manual; în tur nu a fost trimisă, deci nu garantează un rând de eroare observat
    3. Granița
           -> SET este mecanismul. Șirul din Java nu este execuția. Click-ul din Workbench nu închide sesiunea: o închide deconectarea. Comentariul de curs despre parametri nu este SET.
    4. Alternativa
           -> SET GLOBAL sau SET PERSIST când conexiunile noi trebuie să copieze valoarea și ai SYSTEM_VARIABLES_ADMIN. DECLARE când valoarea moare cu blocul. @ când propoziția următoare de pe aceeași conexiune trebuie să o vadă. Aplicația când serverul nu trebuie să o vadă.
    5. Anti-patternul
           -> SET GLOBAL sql_mode = 'TRADITIONAL' ca să repari conexiunea deja deschisă. Copia ei rămâne, iar următorul client primește modul.
    6. Dovada
           -> sessionCopyTheCurrentConnectionKeeps ține SET SESSION. breakSessionKeywordOnGlobalOnly ține SET SESSION max_connections = 1000. Planul nu a fost citit: root@localhost a fost refuzat fără parolă.
    7. Interviu
           -> SET SESSION schimbă copia conexiunii, la sfârșitul propoziției, fără rând nou. SET GLOBAL nu rescrie sesiunile deschise. SET SESSION max_connections este ERROR 1229.
*/

public class SQL_VARIABLES {

    /*
    🟢 Începător
        Ce este            propoziția care schimbă un mod doar pe conexiunea deschisă
        De ce e nevoie     fără ea, comentariul despre parametri pare că a schimbat deja sql_mode
    🟡 Producție
        Cum se scrie       SET SESSION sql_mode = 'TRADITIONAL'
    🔴 Staff
        Unde               copia de sesiune, la sfârșitul propoziției. SET GLOBAL nu o atinge. Nu este un rând și nu intră în binary log
    Pași
        1. Ține propoziția gata de trimis.
        2. Nu o amesteca cu SET SESSION max_connections.
        3. Nu o trimite: root@localhost a fost refuzat.
    */
    // Începător: metoda păstrează propoziția care schimbă modul doar pe conexiunea asta.
    // Producție: public static String sessionCopyTheCurrentConnectionKeeps(). Dacă dispare, fast nu mai are alegerea.
    // Expert: Nu deschide JDBC. Nu este DECLARE și nu este SET @p_emp_no. Parametrul IN din comentariul de curs rămâne la rutină.
    public static String sessionCopyTheCurrentConnectionKeeps() {
        // Începător: conexiunea care o trimite lucrează apoi cu modul TRADITIONAL. Celelalte conexiuni rămân cum erau. Niciun rând nu se scrie.
        // Producție: SET SESSION sql_mode = 'TRADITIONAL'. Dacă linia dispare, rămâne doar comentariul despre procedură.
        // Expert: SET LOCAL ar fi același domeniu de sistem, nu o variabilă locală. SET GLOBAL sql_mode nu rescrie @@SESSION deja copiat. Linia nu ține lacăt și nu alege un index.
        return "SET SESSION sql_mode = 'TRADITIONAL'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris.
        // Producție: acolada închide sessionCopyTheCurrentConnectionKeeps. Șirul nu a plecat către 3306.
        // Expert: Refuzul root@localhost lasă efectul NERULAT. Acolada nu este ROLLBACK, pentru că nicio scriere de rând nu a pornit.
    }

    /*
    🟢 Începător
        Ce este            propoziția care pune cuvântul SESSION pe o variabilă care nu are copie de sesiune
        De ce e nevoie     fără ea, pare că orice nume de variabilă acceptă SESSION
    🟡 Producție
        Cum se scrie       SET SESSION max_connections = 1000
    🔴 Staff
        Unde               eșecul este ERROR 1229, înainte de orice schimbare. Nici globalul, nici sesiunea nu se modifică
    Pași
        1. Ține textul greșit separat.
        2. Nu îl trimite în tur.
        3. Nu îl folosi ca plan în EXPLAIN.
    */
    // Începător: metoda păstrează cel mai mic SQL care nu schimbă limita și nu schimbă modul.
    // Producție: public static String breakSessionKeywordOnGlobalOnly(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: Nu este un index lipsă. max_connections rămâne variabila doar globală din Global_Variables.
    public static String breakSessionKeywordOnGlobalOnly() {
        // Începător: serverul refuză propoziția și lasă conexiunile așa cum erau.
        // Producție: SET SESSION max_connections = 1000. Manualul 9.7 arată ERROR 1229 (HY000). Dacă linia dispare, nu mai ai textul greșit lângă SET SESSION sql_mode.
        // Expert: O atribuire eșuată anulează tot SET-ul, deci nici mysqld-auto.cnf nu s-ar fi scris. SET GLOBAL max_connections este cealaltă propoziție, nu aceasta. În tur nu a fost trimisă.
        return "SET SESSION max_connections = 1000";
        // Începător: metoda s-a oprit la textul care nu trece.
        // Producție: acolada închide breakSessionKeywordOnGlobalOnly. Șirul a rămas în proces.
        // Expert: ERROR 1229 din manual nu a fost citit de pe serverul local. Acolada nu deschide o sesiune nouă.
    }
}
