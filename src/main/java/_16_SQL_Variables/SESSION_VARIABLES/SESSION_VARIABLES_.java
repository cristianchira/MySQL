package _16_SQL_Variables.SESSION_VARIABLES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8745698#questions


/*                                                Session Variables


    ○ A variable that exists only for

                  * the SESSION in which you are operating

    ○ It is defined on our server,

                  * and it lives there

                  * it is visible to the connection being used only


                                                      Session


    ○ A series of

                  * information exchange interactions,

                  * or a dialogue, between

                                - a computer

                                - and a user

    ○ A SESSION

                  * BEGINS at a certain point in time

                                - a session comes out because of a connection that has been made successfully.

                  * TERMINATES at another,later point.

                                - sesiunea se termină când clientul se deconectează. Atunci variabilele @ ale clientului sunt eliberate.

                                - HISTORICAL: once you close the session tab, you will end the established connection and terminate the MySQL session. Manualul 9.7, 11.4: all variables for a given client session are automatically freed when that client exits. Click-ul din Workbench nu este mecanismul.


    ○ Fiecare client care se conectează primește propria sesiune și propria copie a variabilelor de sistem. Un cont poate ține mai multe conexiuni.

                  * HISTORICAL: If there are 100 users connected to the server at the same time, this means there will be 100 connections in 100 sessions, no more and no less. Manualul 9.7, 7.1.9: the server maintains a set of session variables for each client that connects. Contul nu este conexiunea.

    ○ However, you will be the only one who can see

                  * the variable you have created in your session.


    ○ EXAMPLE

                 * e.g. a dialogue between the MySQL server

                 * and a client application like MySQL Workbench


                                                     SQL


    ○ SET SESSION sql_mode = 'TRADITIONAL' schimbă variabila de sistem de pe conexiunea curentă. SET @var_name nu schimbă @@SESSION.sql_mode.

    ○ HISTORICAL: SET @var_name = value; prezentat aici ca propoziția variabilei de sesiune. Manualul 9.7, 11.4: @var_name este variabilă definită de utilizator, specifică sesiunii. 15.7.6.1: variabila de sistem de sesiune se scrie SET SESSION, SET LOCAL, @@SESSION sau @@.

 */

/*
TOPIC:
    SET SESSION sql_mode = 'TRADITIONAL' schimbă copia variabilei de sistem de pe conexiunea curentă, la sfârșitul propoziției, până la o nouă atribuire sau până la deconectare. Celelalte conexiuni nu o văd.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied for user 'root'@'localhost' (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Doar conexiunea asta trebuie să lucreze în TRADITIONAL. După propoziție, globalul rămâne, iar tabelele nu primesc un rând.
MECHANISM:
    La connect, serverul copiază în sesiune valoarea globală curentă, cu excepțiile scrise la variabilă. SET SESSION înlocuiește copia. SET LOCAL și @@SESSION sunt aceeași copie de sistem, nu DECLARE. Pentru sql_mode nu este privilegiu special. sql_log_bin este sesiune restricționată și cere SESSION_VARIABLES_ADMIN.
LOOKALIKES:
    SET GLOBAL sql_mode nu schimbă @@SESSION deja copiat. SET @var_name este variabila de utilizator din 11.4, nu sql_mode. DECLARE moare la END. SET SESSION max_connections este ERROR 1229.
BOOK THEORY:
    Manualul 9.7, 15.7.6.1 și 7.1.9, deschise în tur: sesiunea se inițializează din global la connect. SET SESSION rămâne până se schimbă sau până sesiunea se termină și nu atinge alți clienți. SHOW VARIABLES fără GLOBAL întoarce sesiunea. @@var într-o expresie întoarce sesiunea dacă există, altfel globalul. SET @@var se referă mereu la sesiune. În folderul SESSION_VARIABLES nu există PDF. Variables.pdf vorbește despre parametri, nu despre SET SESSION. User-Defined+vs+System+Variables.pdf spune că și variabilele de utilizator și cele de sistem pot fi de sesiune, cu limită: unele sistem, ca max_connections, sunt doar globale.
BOOK EXAMPLE:
    setSessionSqlMode
REPO DEMO:
    src/main/java/_16_SQL_Variables/SESSION_VARIABLES/SESSION_VARIABLES_.java
BREAK LAB:
    breakGlobalNameWithSessionKeyword ține SET SESSION max_connections = 1000. ERROR 1229. În tur nu a fost trimisă.
TRADE-OFFS:
    SET GLOBAL când conexiunile noi trebuie să copieze valoarea. Aplicația când nicio propoziție de pe conexiune nu depinde de mod.
ANTI-PATTERNS:
    SET GLOBAL sql_mode = 'TRADITIONAL' crezând că tejgheaua deja conectată și-a schimbat modul.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlVariables_Staff_Interview.
NOVEL TRANSFER:
    Tejgheaua deschisă a farmaciei trebuie să refuze datele invalide până la deconectare. SET SESSION sql_mode = 'TRADITIONAL' nu schimbă tejgheaua de alături și nu schimbă globalul pe care îl va copia tura următoare.
SPACED REVIEW:
    O zi: 21 octombrie 2026, copia se schimbă la sfârșitul lui SET. Șapte zile: 27 octombrie 2026, SET LOCAL nu este DECLARE. Treizeci de zile: 19 noiembrie 2026, GLOBAL nu repară sesiunea deja copiată.

1. Enunț: Ce problemă rezolvă SET SESSION sql_mode = 'TRADITIONAL'?
   Capcană: Modul se schimbă pentru toți utilizatorii, pentru că sql_mode este al serverului.
   Cheie: setSessionSqlMode schimbă copia conexiunii curente. Celelalte își păstrează copia. Globalul rămâne. Nu se scrie un rând.
   Demonstrație: setSessionSqlMode
2. Enunț: Când se vede modul nou și ce rămâne în tabele?
   Capcană: Se vede la COMMIT, pentru că SET este o tranzacție pe un rând de configurare.
   Cheie: Se vede la sfârșitul propoziției, pe conexiunea care a trimis-o. Nu există rând, index sau lacăt. SET nu intră în binary log. La deconectare copia moare.
   Demonstrație: setSessionSqlMode
3. Enunț: De ce nu este SET GLOBAL sql_mode și de ce nu este SET @var_name?
   Capcană: GLOBAL rescrie și sesiunea curentă, iar @var_name este forma scurtă a lui @@SESSION.sql_mode.
   Cheie: SET GLOBAL nu atinge @@SESSION deja copiat. @var_name este variabila de utilizator, specifică sesiunii, dar nu este sql_mode. SET LOCAL sql_mode este tot sesiunea de sistem.
   Demonstrație: setSessionSqlMode
4. Enunț: Cum eșuează SET SESSION max_connections = 1000?
   Capcană: max_connections primește o copie de sesiune egală cu 1000.
   Cheie: breakGlobalNameWithSessionKeyword. ERROR 1229. Nimic nu se schimbă. În tur nu a fost trimisă.
   Demonstrație: breakGlobalNameWithSessionKeyword
5. Enunț: Când nu trimiți SET SESSION?
   Capcană: Fără SET SESSION, conexiunea nu are sql_mode.
   Cheie: Conexiunea a copiat deja globalul la connect. Trimiți SET SESSION doar când copia asta trebuie să difere. Dacă și conexiunile noi trebuie să difere, ești la SET GLOBAL sau SET PERSIST, știind că sesiunea deschisă nu se rescrie. Dacă numărul nu intră în nicio propoziție, rămâne în aplicație.
   Demonstrație: setSessionSqlMode

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE doar conexiunea asta schimbă modul -> setSessionSqlMode -> SET SESSION sql_mode = 'TRADITIONAL' -> nu garantează că @@GLOBAL.sql_mode s-a schimbat
    2. Eșecul cel mai mic
           -> DECIDE pui SESSION pe max_connections -> breakGlobalNameWithSessionKeyword -> SET SESSION max_connections = 1000 -> ERROR 1229 în manual; în tur nu a fost observat
    3. Granița
           -> SET SESSION este mecanismul. Închiderea tabului din Workbench nu este mecanismul: deconectarea eliberează @ și pierde copia. SET @var_name din curs nu este sql_mode.
    4. Alternativa
           -> SET GLOBAL sau SET PERSIST pentru conexiunile noi. DECLARE pentru valoarea care moare la END. @ pentru o valoare inventată pe sesiune.
    5. Anti-patternul
           -> SET GLOBAL sql_mode ca să schimbi tejgheaua deja deschisă. Tejgheaua își păstrează copia.
    6. Dovada
           -> setSessionSqlMode și breakGlobalNameWithSessionKeyword. Autentificarea root fără parolă a căzut.
    7. Interviu
           -> SET SESSION schimbă copia conexiunii la sfârșitul propoziției. SET GLOBAL nu o rescrie. SET SESSION max_connections este ERROR 1229.
*/

public class SESSION_VARIABLES_ {

    /*
    🟢 Începător
        Ce este            schimbarea unui mod doar pe conexiunea deschisă
        De ce e nevoie     fără propoziție, SET @var_name din curs pare că a schimbat sql_mode
    🟡 Producție
        Cum se scrie       SET SESSION sql_mode = 'TRADITIONAL'
    🔴 Staff
        Unde               copia de sesiune, la sfârșitul SET. Globalul și celelalte conexiuni rămân. Nu este privilegiu special pentru sql_mode
    Pași
        1. Ține propoziția.
        2. Nu o trimite.
        3. Nu o înlocui cu SET @var_name.
    */
    // Începător: metoda păstrează propoziția care schimbă modul pe conexiunea asta.
    // Producție: public static String setSessionSqlMode(). Dacă dispare, rămâne doar @var_name din comentariu.
    // Expert: Nu deschide JDBC. sql_log_bin ar fi o sesiune restricționată; sql_mode nu este acel caz. SET nu intră în binary log.
    public static String setSessionSqlMode() {
        // Începător: conexiunea lucrează apoi în TRADITIONAL. Un alt client nu își schimbă modul. Nu se scrie un rând.
        // Producție: SET SESSION sql_mode = 'TRADITIONAL'. SET LOCAL sql_mode = 'TRADITIONAL' este aceeași copie. Dacă linia dispare, nu mai ai variabila de sistem de sesiune.
        // Expert: SET @@SESSION.sql_mode și SET @@sql_mode sunt tot sesiunea. SET GLOBAL sql_mode nu rescrie copia deja luată. Linia nu este un index și nu ține lacăt. În tur nu a fost trimisă.
        return "SET SESSION sql_mode = 'TRADITIONAL'";
        // Începător: metoda a înapoiat modul, nu un rând de configurare.
        // Producție: acolada închide setSessionSqlMode. Șirul nu a plecat către 3306.
        // Expert: Refuzul root@localhost lasă copia NERULATĂ. Acolada nu este deconectarea care ar elibera @.
    }

    /*
    🟢 Începător
        Ce este            cuvântul SESSION pus pe o variabilă care nu are sesiune
        De ce e nevoie     fără el, orice nume din SHOW VARIABLES pare să accepte SESSION
    🟡 Producție
        Cum se scrie       SET SESSION max_connections = 1000
    🔴 Staff
        Unde               ERROR 1229. Globalul rămâne. Nu se creează o copie de sesiune
    Pași
        1. Ține eșecul separat.
        2. Nu îl trimite.
    */
    // Începător: metoda păstrează propoziția care nu schimbă limita și nu schimbă modul.
    // Producție: public static String breakGlobalNameWithSessionKeyword(). Dacă dispare, eșecul nu mai are șir.
    // Expert: Nu este lipsa lui SYSTEM_VARIABLES_ADMIN. Este domeniul. SET GLOBAL max_connections este fișierul vecin.
    public static String breakGlobalNameWithSessionKeyword() {
        // Începător: serverul refuză și lasă conexiunea cu modul pe care îl avea.
        // Producție: SET SESSION max_connections = 1000. Manualul arată ERROR 1229 (HY000). Dacă linia dispare, nu mai ai granița cu variabila doar globală.
        // Expert: Eșecul oprește tot SET-ul, deci o a doua atribuire din aceeași propoziție nu s-ar fi aplicat. În tur nu a fost trimisă.
        return "SET SESSION max_connections = 1000";
        // Începător: metoda s-a oprit la refuz.
        // Producție: acolada închide breakGlobalNameWithSessionKeyword.
        // Expert: Codul 1229 nu a fost citit de pe server. Acolada nu copiază globalul în sesiune.
    }
}
