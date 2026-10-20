package _16_SQL_Variables.GLOBAL_VARIABLES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8745702#questions


/*                                                     Global Variables


    ○ SET GLOBAL schimbă valoarea folosită când o conexiune nouă își copiază variabilele. Conexiunile deja deschise își păstrează copia, inclusiv conexiunea care a trimis SET GLOBAL.

                  * HISTORICAL: global variables apply to ALL connections related to a specific server. Manualul 9.7, 15.7.6.1: the global variable change does not affect the session value for any current client sessions (not even the session within which the global value change occurs).


    ○ You CANNOT set just any variable as global

                  * a specific group of pre-defined variables in MySQL

                  * is suitable for this job.

          They are called System Variables


    ○ EXAMPLE OF System Variable

                 * max_connections este variabilă de sistem doar globală. Limitează câte conexiuni poate ține serverul. Nu se apelează ca funcție.

                 * HISTORICAL: .max_connections(). Manualul 9.7, 15.7.6.1: propoziția este SET GLOBAL max_connections = 1000 sau SET @@GLOBAL.max_connections = 1000. SET SESSION max_connections dă ERROR 1229.

                               - indicates the maximum number of connections to a server

                               - that can be established at a certain point in time.


                 * max_join_size limitează câte accesări de rânduri poate estima optimizerul într-un join. Peste limită, propoziția este respinsă. Nu alocă memorie.

                 * HISTORICAL: .max_join_size() sets the maximum memory space allocated for the joins created by a certain connection. Manualul 9.7, 7.1.8: max_join_size is a limit on row accesses in base tables. A value other than DEFAULT sets sql_big_selects to 0.


                SET GLOBAL max_connections = 1000;


                                                      SQL


    1. SET GLOBAL var_name = value;


    2. SET @@global.var_name = value;





 */


/*
TOPIC:
    SET GLOBAL max_connections = 1000 schimbă limita de conexiuni a serverului pentru conectările de după. Nu rescrie copia de variabile a sesiunilor deja deschise și nu este o alocare de memorie pentru join.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied for user 'root'@'localhost' (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Conexiunile noi trebuie să pornească de la o limită nouă. După propoziție, sesiunile deschise rămân cu copia lor, iar nicio tabelă nu primește un rând.
MECHANISM:
    Serverul execută SET GLOBAL. Cere SYSTEM_VARIABLES_ADMIN sau SUPER. max_connections este doar globală. Efectul pe conectările noi se vede la connect, nu la commit. SET nu se scrie în binary log. Dacă atribuirea eșuează, nu se schimbă nimic.
LOOKALIKES:
    SET SESSION sql_mode = 'TRADITIONAL' schimbă copia conexiunii curente. SET SESSION max_connections dă ERROR 1229. SET @p_emp_no nu este o variabilă de sistem și nu poate fi globală. max_join_size nu este memoria join-ului: este limita de accesări de rânduri.
BOOK THEORY:
    Manualul 9.7, 15.7.6.1 și 7.1.9, deschise în tur: valoarea globală inițializează sesiunea la connect. Schimbarea globală nu atinge sesiunile curente, nici pe cea care a executat SET. 7.1.8: max_join_size este limita de accesări de rânduri; o valoare diferită de DEFAULT pune sql_big_selects pe 0. 7.1.9.1: SET GLOBAL cere SYSTEM_VARIABLES_ADMIN sau SUPER. SET PERSIST scrie și mysqld-auto.cnf. Global+Variables.pdf spune că globalul se aplică tuturor conexiunilor, scrie SET GLOBAL și SET @@global, iar max_join_size ar fi memorie. Memoria și „toate conexiunile” contrazic paginile deschise. PDF-ul nu conține ERROR 1229.
BOOK EXAMPLE:
    setGlobalMaxConnections
REPO DEMO:
    src/main/java/_16_SQL_Variables/GLOBAL_VARIABLES/Global_Variables.java
BREAK LAB:
    breakSessionOnGlobalOnly ține SET SESSION max_connections = 1000. ERROR 1229 în manual. În tur nu a fost trimisă.
TRADE-OFFS:
    SET PERSIST max_connections = 1000 dacă limita trebuie să supraviețuiască restartului. Tot nu rescrie sesiunile deschise. SET SESSION sql_mode când vrei doar conexiunea curentă.
ANTI-PATTERNS:
    SET GLOBAL sql_mode = 'TRADITIONAL' ca să schimbi modul conexiunii deja deschise. Copia ei rămâne.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlVariables_Staff_Interview.
NOVEL TRANSFER:
    Depozitul vrea ca următoarea conectare a unui scanner să pornească cu o limită nouă de sesiuni. SET GLOBAL max_connections nu schimbă scannerul deja autentificat.
SPACED REVIEW:
    O zi: 21 octombrie 2026, sesiunea deschisă nu își pierde copia. Șapte zile: 27 octombrie 2026, ERROR 1229. Treizeci de zile: 19 noiembrie 2026, SET PERSIST dacă trebuie să rămână după restart.

1. Enunț: Ce problemă rezolvă SET GLOBAL max_connections = 1000?
   Capcană: Limita se aplică imediat fiecărei conexiuni deschise, ca un UPDATE pe o tabelă de sesiuni.
   Cheie: setGlobalMaxConnections schimbă valoarea globală. Conexiunile noi o copiază la connect. Cele deschise nu sunt rescrise. Nu se scrie un rând.
   Demonstrație: setGlobalMaxConnections
2. Enunț: Ce se întâmplă cu rândurile și cu sesiunea curentă?
   Capcană: max_connections se inserează ca rând și @@SESSION al conexiunii curente devine 1000.
   Cheie: Nu există tabelă în propoziție. max_connections nu are valoare de sesiune. Efectul pentru clienții noi este la connect. SET nu intră în binary log.
   Demonstrație: setGlobalMaxConnections
3. Enunț: De ce nu este SET SESSION sql_mode?
   Capcană: GLOBAL și SESSION sunt sinonime, iar max_connections se mută pe sesiune dacă scrii SESSION.
   Cheie: SET SESSION sql_mode schimbă copia conexiunii. SET SESSION max_connections este ERROR 1229 și nu schimbă globalul. @p_emp_no nu poate fi global.
   Demonstrație: breakSessionOnGlobalOnly
4. Enunț: Cum eșuează dacă scrii SET SESSION max_connections = 1000?
   Capcană: Serverul creează o copie de sesiune și conexiunea curentă primește 1000.
   Cheie: breakSessionOnGlobalOnly. ERROR 1229. Nicio variabilă nu se schimbă. În tur nu a fost trimisă.
   Demonstrație: breakSessionOnGlobalOnly
5. Enunț: Când alegi SET PERSIST în loc de SET GLOBAL?
   Capcană: SET GLOBAL rămâne după restart, pentru că limita este un rând.
   Cheie: SET GLOBAL ține până la oprirea serverului. SET PERSIST scrie mysqld-auto.cnf și tot nu rescrie sesiunile deschise. Dacă îți trebuie doar conexiunea curentă, ești la SET SESSION, nu aici.
   Demonstrație: setGlobalMaxConnections

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE conexiunile noi trebuie să vadă limita -> setGlobalMaxConnections -> SET GLOBAL max_connections = 1000 -> nu garantează că o sesiune deja deschisă și-a schimbat vreo copie
    2. Eșecul cel mai mic
           -> DECIDE scrii SESSION pe max_connections -> breakSessionOnGlobalOnly -> SET SESSION max_connections = 1000 -> ERROR 1229 în manual; în tur nu garantează un mesaj citit de pe server
    3. Granița
           -> SET GLOBAL este mecanismul. .max_connections() din slide nu este o funcție. Șirul Java nu execută. Workbench nu aplică globalul.
    4. Alternativa
           -> SET SESSION când ținta este copia conexiunii, pe o variabilă care are sesiune, cum este sql_mode. SET PERSIST când valoarea trebuie să treacă de restart.
    5. Anti-patternul
           -> SET GLOBAL sql_mode ca să repari conexiunea curentă. Copia rămâne cea de la connect.
    6. Dovada
           -> setGlobalMaxConnections și breakSessionOnGlobalOnly. root@localhost a fost refuzat. EXPLAIN nu a fost citit.
    7. Interviu
           -> SET GLOBAL pregătește conectările noi, nu rescrie sesiunile deschise și nu scrie un rând. SET SESSION max_connections este ERROR 1229. max_join_size nu este memorie.
*/

public class Global_Variables {

    /*
    🟢 Începător
        Ce este            propoziția care schimbă limita de conexiuni pentru cine se conectează după
        De ce e nevoie     fără ea, slide-ul .max_connections() pare o funcție gata de rulat
    🟡 Producție
        Cum se scrie       SET GLOBAL max_connections = 1000
    🔴 Staff
        Unde               valoarea globală, văzută de o sesiune nouă la connect. Cere SYSTEM_VARIABLES_ADMIN. Nu intră în binary log
    Pași
        1. Ține propoziția.
        2. Nu o trimite în tur.
        3. Nu o confunda cu SET SESSION.
    */
    // Începător: metoda păstrează propoziția care schimbă limita serverului, nu un rând dintr-o tabelă.
    // Producție: public static String setGlobalMaxConnections(). Dacă dispare, slide-ul cu paranteze rămâne singura formă.
    // Expert: Nu deschide JDBC. Nu alocă memoria join-ului. SET PERSIST ar scrie și mysqld-auto.cnf; metoda nu o ține.
    public static String setGlobalMaxConnections() {
        // Începător: după propoziție, o conexiune nouă poate fi refuzată când limita este atinsă. Conexiunile deja deschise nu își schimbă copiile.
        // Producție: SET GLOBAL max_connections = 1000. Fără șir, rămâne .max_connections() din curs, pe care serverul nu o execută ca funcție.
        // Expert: SET @@GLOBAL.max_connections = 1000 este aceeași atribuire globală. SET SESSION max_connections este ERROR 1229. Linia nu ține lacăt și nu alege un index. În tur nu a fost trimisă.
        return "SET GLOBAL max_connections = 1000";
        // Începător: metoda a înapoiat limita, nu a deconectat pe nimeni.
        // Producție: acolada închide setGlobalMaxConnections. Șirul nu a plecat către 3306.
        // Expert: Refuzul root@localhost lasă limita NERULATĂ. Acolada nu este SET PERSIST.
    }

    /*
    🟢 Începător
        Ce este            același nume, cu SESSION, pe o variabilă care nu are copie de sesiune
        De ce e nevoie     fără text, pare că SESSION transformă max_connections într-o variabilă de sesiune
    🟡 Producție
        Cum se scrie       SET SESSION max_connections = 1000
    🔴 Staff
        Unde               ERROR 1229, înainte de orice schimbare de global sau de sesiune
    Pași
        1. Ține eșecul separat de SET GLOBAL.
        2. Nu îl trimite.
    */
    // Începător: metoda păstrează propoziția care nu schimbă limita.
    // Producție: public static String breakSessionOnGlobalOnly(). Dacă dispare, eșecul rămâne doar în comentariu.
    // Expert: Nu este refuzul de privilegiu SYSTEM_VARIABLES_ADMIN. Este domeniul greșit. sql_mode cu SET SESSION este cealaltă clasă.
    public static String breakSessionOnGlobalOnly() {
        // Începător: serverul nu mută limita pe conexiunea curentă și nu o schimbă pentru nimeni.
        // Producție: SET SESSION max_connections = 1000. Manualul arată ERROR 1229 (HY000). Dacă linia dispare, nu mai ai perechea greșită.
        // Expert: Eșecul unei atribuiri lasă tot SET-ul neschimbat. Nu se scrie mysqld-auto.cnf. În tur șirul nu a fost trimis.
        return "SET SESSION max_connections = 1000";
        // Începător: metoda s-a oprit înainte de o limită nouă.
        // Producție: acolada închide breakSessionOnGlobalOnly.
        // Expert: ERROR 1229 nu a fost citit de pe 3306. Acolada nu acordă SYSTEM_VARIABLES_ADMIN.
    }
}
