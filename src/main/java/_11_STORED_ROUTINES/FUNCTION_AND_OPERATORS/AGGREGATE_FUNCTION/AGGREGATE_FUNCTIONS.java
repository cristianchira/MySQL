package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION;


//artin G. Aggregate Function
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345176#overview


// Aggregate Function Descriptions
// https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html


/*                                             Aggregate_Function


    ○ They are applied on multiple rows of a single column of a table

                  * and return an output of a single value

                  * they ignore NULL values unless told not to


                                          Aggregate Function  query


    ○ SELECT COUNT(column_name)
       FROM table_name;


    1. COUNT()

                  * counts the number of NON-NULL records in a field

                  * the parentheses after COUNT()

                                - must start right after the keyword,

                                - not after a whitespace in order to work

    1. SELECT COUNT(DISTINCT column_name)
          FROM table_name;

    2. SUM()

                  * sums all the NON-NULL values in a column

    3. MIN()

                 * returns the minimum value from the entire list

   4. MAX()

                 * returns the maximum value from the entire list


   5. AVG()

                * Return the average value of the argument

 */

/*
TOPIC:
    Agregatul ia multe rânduri dintr-o coloană și lasă o singură valoare pe grup. Fără GROUP BY, grupul este toată tabela. NULL nu intră în AVG, SUM, MIN, MAX și COUNT(coloană). COUNT(*) numără rândurile.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Din salaries rămâne o singură medie, nu lista de salarii. Tabela nu se schimbă. Nu se creează o rutină.
MECHANISM:
    Serverul execută șirul din aggregateCollapsesTheTable. 14.19.1: fără GROUP BY, agregatul grupează toate rândurile și ignoră NULL, dacă nu s-a spus altfel. Rezultatul se vede la sfârșitul propoziției.
LOOKALIKES:
    f_emp_avg_salary este o funcție stocată, cu CREATE și cu un emp_no. ROUND(AVG(salary), 2) rotunjește valoarea deja agregată și nu este el însuși agregatul. WHERE salary IS NOT NULL filtrează rânduri înainte; agregatul sare peste NULL în coloană și tot numără rândul la COUNT(*).
BOOK THEORY:
    Manualul 9.7, 14.19.1, deschis în tur: agregatele lucrează pe seturi, adesea cu GROUP BY; ignoră NULL dacă nu s-a spus altfel; fără GROUP BY echivalează cu un singur grup pe toate rândurile; SUM și AVG dau DECIMAL pentru argument exact și DOUBLE pentru argument aproximativ; SUM și AVG nu lucrează pe valori temporale fără conversie. Introduction.pdf, deschis în tur: se aplică pe mai multe rânduri ale unei coloane și întorc o valoare.
BOOK EXAMPLE:
    aggregateCollapsesTheTable
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/AGGREGATE_FUNCTIONS.java
BREAK LAB:
    salaryBesideAggregate ține SELECT salary, AVG(salary) FROM salaries. salary nu este grupat și nu este valoarea unică a agregatului. În tur șirul nu a fost trimis.
TRADE-OFFS:
    O funcție stocată când media este doar pentru un emp_no și trebuie să aibă nume în schemă. Aplicația când setul este deja la client și nu vrei un al doilea drum la server.
ANTI-PATTERNS:
    SELECT salary, AVG(salary) FROM salaries, ca și cum media ar sta lângă fiecare salariu fără GROUP BY.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Greutatea totală a coletelor de pe un raft este SUM pe grupul raftului. Un colet cu greutate lipsă nu intră în SUM. Lista de colete nu este agregatul.
SPACED REVIEW:
    O zi: 21 octombrie 2026, o valoare pe grup. Șapte zile: 27 octombrie 2026, salary lângă AVG nu este grupul. Treizeci de zile: 19 noiembrie 2026, NULL iese din AVG și rămâne în COUNT(*).

1. Enunț: Ce problemă rezolvă agregatul față de lista de salarii?
   Capcană: AVG(salary) lasă în tabelă doar salariul mediu și șterge celelalte rânduri.
   Cheie: aggregateCollapsesTheTable întoarce o valoare în rezultat. salaries rămâne. Nu există CREATE. Nu există CALL.
   Demonstrație: aggregateCollapsesTheTable
2. Enunț: Ce se întâmplă cu NULL și cu rândurile când nu există GROUP BY?
   Capcană: Un salariu NULL trage media la zero și face grupul să dispară.
   Cheie: 14.19.1: NULL este ignorat de AVG, dacă nu s-a spus altfel. Fără GROUP BY, grupul este toată tabela. Rândul cu NULL rămâne în tabelă. COUNT(*) l-ar număra; AVG nu îl bagă în medie.
   Demonstrație: aggregateCollapsesTheTable
3. Enunț: De ce AVG(salary) nu este f_emp_avg_salary(10001)?
   Capcană: Funcția stocată este doar un alt nume pentru agregat, deci ambele medii sunt pe același grup.
   Cheie: Agregatul de aici nu primește emp_no și nu stă în schemă. Funcția stocată primește p_emp_no, are RETURNS și cere privilegiu pe rutină. Aceeași medie pe un om este WHERE sau argument, nu alt agregat.
   Demonstrație: aggregateCollapsesTheTable
4. Enunț: Cum eșuează SELECT salary, AVG(salary) FROM salaries?
   Capcană: Serverul repetă media lângă fiecare salariu și asta este GROUP BY implicit.
   Cheie: salaryBesideAggregate pune o coloană neagregată lângă valoarea unică a grupului întreg. Agregatul nu întoarce salariul fiecărui rând. Ca să ai media pe angajat, GROUP BY emp_no trebuie să fie scris.
   Demonstrație: salaryBesideAggregate
5. Enunț: Când lași agregatul în server și când calculezi media în aplicație?
   Capcană: Media în aplicație este mereu aceeași, pentru că a văzut aceleași rânduri.
   Cheie: Serverul când setul este mare și îți trebuie o valoare, nu rândurile. Aplicația când rândurile sunt deja la client și nu vrei încă un SELECT. Funcția stocată când expresia cu emp_no trebuie să aibă nume pentru mai mulți clienți.
   Demonstrație: salaryBesideAggregate

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE o medie pe toată tabela -> aggregateCollapsesTheTable -> SELECT AVG(salary) AS avg_salary FROM salaries -> nu garantează că NULL a intrat în medie
    2. Eșecul cel mai mic
           -> DECIDE salariul lângă media lui, fără grup -> salaryBesideAggregate -> SELECT salary, AVG(salary) FROM salaries -> nu garantează un rând pe fiecare salariu
    3. Granița
           -> Agregatul este mecanismul MySQL. Comentariul că spațiul înainte de paranteză strică COUNT este despre built-in, nu despre o rutină stocată. Java nu calculează media.
    4. Alternativa
           -> GROUP BY emp_no când media este pe angajat. COUNT(*) când numeri rânduri, nu valori. Funcția stocată când vrei nume și EXECUTE.
    5. Anti-patternul
           -> SELECT salary, AVG(salary) FROM salaries. Coloana și agregatul pe tot grupul nu sunt același rezultat.
    6. Dovada
           -> aggregateCollapsesTheTable ține AVG. salaryBesideAggregate ține coloana de lângă el. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> Agregatul lasă o valoare pe grup și nu schimbă tabela. Fără GROUP BY grupul este toată tabela. NULL nu intră în AVG. salary lângă AVG nu este grupul.
*/

public class AGGREGATE_FUNCTIONS {

    /*
    🟢 Începător
        Ce este            o singură medie, scoasă din toate salariile
        De ce e nevoie     fără ea, lista de salarii pare că trebuie citită întreagă la client
    🟡 Producție
        Cum se scrie       SELECT AVG(salary) AS avg_salary FROM salaries
    🔴 Staff
        Unde               fără GROUP BY grupul este tabela. NULL nu intră în medie. DECIMAL dacă salary este exact, după 14.19.1
    Pași
        1. Pune doar agregatul în listă.
        2. Nu pune salary lângă el.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează propoziția care lasă o valoare din multe rânduri.
    // Producție: public static String aggregateCollapsesTheTable(). Dacă dispare, agregatul rămâne doar o listă de nume.
    // Expert: Nu este CREATE FUNCTION. Nu este ROUND. ROUND ar schimba scara acestei valori, nu grupul.
    public static String aggregateCollapsesTheTable() {
        // Începător: din salariile păstrate rămâne o medie, iar tabela rămâne întreagă.
        // Producție: SELECT AVG(salary) AS avg_salary FROM salaries. Dacă linia dispare, nu mai ai propoziția fără GROUP BY.
        // Expert: 14.19.1: SUM și AVG nu se aplică corect pe un timp, fără conversie. salary numeric exact dă DECIMAL, nu DOUBLE.
        return "SELECT AVG(salary) AS avg_salary FROM salaries";
        // Începător: metoda a înapoiat propoziția, nu media.
        // Producție: acolada închide aggregateCollapsesTheTable. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă planul NERULAT. Acolada nu este un index pe salary.
    }

    /*
    🟢 Începător
        Ce este            un salariu pus lângă media întregii tabele
        De ce e nevoie     fără el, pare că agregatul repetă valoarea pe fiecare rând
    🟡 Producție
        Cum se scrie       SELECT salary, AVG(salary) FROM salaries
    🔴 Staff
        Unde               salary nu face parte din grupul unic. Media pe angajat cere GROUP BY emp_no
    Pași
        1. Pune coloana lângă agregat.
        2. Nu pune GROUP BY, ca să rămână eșecul.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează propoziția care amestecă un salariu cu media tuturor.
    // Producție: public static String salaryBesideAggregate(). Dacă dispare, eșecul rămâne o frază.
    // Expert: GROUP BY emp_no ar face din salary, dacă depinde de emp_no, o valoare pe grup. Acest șir nu are grupul.
    public static String salaryBesideAggregate() {
        // Începător: serverul nu poate arăta și fiecare salariu, și o singură medie a tuturor, ca și cum ar fi același grup.
        // Producție: SELECT salary, AVG(salary) FROM salaries. Dacă linia dispare, nu mai ai coloana neagregată.
        // Expert: COUNT(*) lângă salary are aceeași problemă de grup. WHERE nu o repară. HAVING filtrează grupuri, nu înlocuiește GROUP BY.
        return "SELECT salary, AVG(salary) FROM salaries";
        // Începător: metoda a păstrat amestecul.
        // Producție: acolada închide salaryBesideAggregate. Șirul a rămas în proces.
        // Expert: Nu este ER_SP_NO_RETSET. Acela este un set trimis dintr-o funcție stocată.
    }
}
