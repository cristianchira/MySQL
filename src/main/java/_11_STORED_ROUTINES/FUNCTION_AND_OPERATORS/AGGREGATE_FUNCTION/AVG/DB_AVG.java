package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.AVG;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376504#questions

// https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_avg


/*                                                     AVG(expr)


    ○ AVG([DISTINCT] expr) [over_clause]
                  * Returns the average value of all non-null value of expr.
                  * The DISTINCT option can be used to return the average of the distinct values of expr.

          If there are no matching rows,

                  * AVG() returns NULL.

          The function also returns NULL if expr is NULL.

This function executes as a window function if over_clause is present. over_clause is as described in Section 12.21.2, “Window Function Concepts and Syntax”; it cannot be used with DISTINCT.


 */

/*
TOPIC:
    AVG(salary) este media valorilor nenule. NULL nu intră. Fără rânduri rămase, comentariul din clasă spune că rezultatul este NULL. Tabela nu primește media.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Media salariilor de după 1997-01-01 este o valoare în rezultat. Rândurile rămân. Un salariu lipsă nu trage media la zero.
MECHANISM:
    Serverul execută șirul din avgSalaryAfterDate. 14.19.1: AVG ignoră NULL; pe argument exact dă DECIMAL, pe argument aproximativ dă DOUBLE. Efectul se vede la sfârșitul propoziției.
LOOKALIKES:
    SUM este totalul, nu media. f_emp_avg_salary(p_emp_no) este funcția stocată pentru un angajat. AVG(...) OVER (...) este fereastră, nu un alt grup GROUP BY; comentariul din clasă o deosebește și spune că nu stă cu DISTINCT.
BOOK THEORY:
    Manualul 9.7, 14.19.1, deschis în tur: AVG întoarce media, ignoră NULL și dă DECIMAL sau DOUBLE după felul argumentului. AVG().pdf, deschis în tur: extrage media valorilor nenule dintr-un câmp. Pagina citită nu spune ce se întâmplă cu OVER.
BOOK EXAMPLE:
    avgSalaryAfterDate
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/AVG/DB_AVG.java
BREAK LAB:
    avgOfNullSalary ține SELECT AVG(salary) FROM salaries WHERE salary IS NULL. Nu rămân valori nenule, iar media nu este zero scris în tabelă. În tur șirul nu a fost trimis.
TRADE-OFFS:
    Funcția stocată când media unui emp_no trebuie să aibă nume. Aplicația când rândurile sunt deja la client.
ANTI-PATTERNS:
    Să înlocuiești NULL cu 0 înainte de AVG dacă lipsa salariului nu înseamnă salariu zero.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Greutatea medie a coletelor cântărite nu include coletul fără cântar. Să pui 0 pentru el schimbă media. Totalul este SUM, nu AVG.
SPACED REVIEW:
    O zi: 21 octombrie 2026, NULL nu intră în medie. Șapte zile: 27 octombrie 2026, setul gol nu scrie zero în tabelă. Treizeci de zile: 19 noiembrie 2026, funcția stocată este media unui om, nu AVG pe toată tabela.

1. Enunț: Ce problemă rezolvă AVG(salary) față de lista de salarii?
   Capcană: Media se scrie în fiecare rând, în locul salariului.
   Cheie: avgSalaryAfterDate lasă o valoare în rezultat pentru salariile de după dată. salaries rămâne. NULL nu devine un rând nou.
   Demonstrație: avgSalaryAfterDate
2. Enunț: Ce se întâmplă cu un salariu NULL în medie?
   Capcană: Este zero, deci coboară media.
   Cheie: 14.19.1 și AVG().pdf: media este pe valorile nenule. Rândul cu NULL rămâne în tabelă și nu intră în împărțire. COUNT(*) l-ar număra; AVG nu.
   Demonstrație: avgSalaryAfterDate
3. Enunț: De ce AVG(salary) nu este f_emp_avg_salary(10001)?
   Capcană: Funcția este AVG, doar că are un nume, deci media este pe toată tabela în ambele.
   Cheie: AVG de aici nu primește emp_no. Funcția stocată filtrează un angajat, are RETURNS și cere CREATE. Aceeași funcție într-un SELECT este o expresie; AVG singur este agregatul built-in.
   Demonstrație: avgSalaryAfterDate
4. Enunț: Cum eșuează ideea că NULL înseamnă zero în AVG?
   Capcană: WHERE salary IS NULL, apoi AVG, dă media zero, pentru că nulul este zero.
   Cheie: avgOfNullSalary nu are valori nenule. Agregatul ignoră NULL. Nu apare salariul zero în salaries și nu se scrie o medie 0.
   Demonstrație: avgOfNullSalary
5. Enunț: Când înlocuiești NULL cu 0 și când îl lași în afara mediei?
   Capcană: Întotdeauna înlocuiești, altfel media este incompletă.
   Cheie: Lași NULL afară când lipsa nu este salariu zero. Pui 0, cu COALESCE sau cu un UPDATE, doar când regula de afaceri spune că lipsa este zero. UPDATE schimbă rândul; AVG nu.
   Demonstrație: avgOfNullSalary

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE media valorilor scrise, după o dată -> avgSalaryAfterDate -> SELECT AVG(salary) FROM salaries WHERE from_date > '1997-01-01' -> nu garantează că NULL a coborât media
    2. Eșecul cel mai mic
           -> DECIDE NULL să fie zero -> avgOfNullSalary -> SELECT AVG(salary) FROM salaries WHERE salary IS NULL -> nu garantează rezultatul 0
    3. Granița
           -> AVG este agregatul MySQL. PDF-ul deschis spune media valorilor nenule și nu scrie în tabelă. Java nu calculează media.
    4. Alternativa
           -> SUM pentru total. f_emp_avg_salary pentru un angajat, ca funcție stocată. COALESCE înainte de AVG doar când NULL chiar este zero.
    5. Anti-patternul
           -> AVG după ce ai înlocuit lipsa cu 0, fără o regulă care să spună că lipsa este salariu zero. Media coboară și rândul poate rămâne schimbat dacă ai făcut UPDATE.
    6. Dovada
           -> avgSalaryAfterDate ține AVG pe salariile filtrate. avgOfNullSalary ține filtrul pe NULL. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> AVG este media valorilor nenule. NULL nu este zero. Tabela nu primește media. Funcția stocată este alt obiect, cu emp_no.
*/

public class DB_AVG {

    /*
    🟢 Începător
        Ce este            media salariilor scrise, de după o dată
        De ce e nevoie     fără ea, media s-ar calcula din rânduri duse la client
    🟡 Producție
        Cum se scrie       SELECT AVG(salary) FROM salaries WHERE from_date > '1997-01-01'
    🔴 Staff
        Unde               NULL nu intră. Argumentul exact dă DECIMAL. Rezultatul nu se scrie în salaries
    Pași
        1. Pune AVG pe salary.
        2. Filtrează data cu WHERE.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează media pe valorile care există.
    // Producție: public static String avgSalaryAfterDate(). Dacă dispare, AVG rămâne doar în comentariu.
    // Expert: Nu este f_emp_avg_salary. Nu are RETURNS și nu primește 10001.
    public static String avgSalaryAfterDate() {
        // Începător: rămâne o medie a salariilor de după dată, iar salariul lipsă nu este numărat ca zero.
        // Producție: SELECT AVG(salary) FROM salaries WHERE from_date > '1997-01-01'. Dacă linia dispare, nu mai ai media filtrată.
        // Expert: 14.19.1: DOUBLE dacă salary ar fi aproximativ. OVER ar face fereastră, nu acest grup. DISTINCT ar media valorile diferite, nu fiecare rând.
        return "SELECT AVG(salary) FROM salaries WHERE from_date > '1997-01-01'";
        // Începător: metoda a înapoiat media, nu cifra.
        // Producție: acolada închide avgSalaryAfterDate. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă media NERULAT. Acolada nu înlocuiește NULL cu 0.
    }

    /*
    🟢 Începător
        Ce este            media doar pe salariile care lipsesc
        De ce e nevoie     fără ea, pare că NULL intră ca zero
    🟡 Producție
        Cum se scrie       SELECT AVG(salary) FROM salaries WHERE salary IS NULL
    🔴 Staff
        Unde               nu rămân valori nenule. Agregatul nu scrie 0 în tabelă
    Pași
        1. Filtrează doar NULL.
        2. Pune AVG.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează media care nu are ce să medieze.
    // Producție: public static String avgOfNullSalary(). Dacă dispare, capcana nulului-zero rămâne o frază.
    // Expert: COALESCE(salary, 0) ar băga zero în medie și ar schimba întrebarea. Acest șir nu îl are.
    public static String avgOfNullSalary() {
        // Începător: salariile lipsă nu fac o medie zero.
        // Producție: SELECT AVG(salary) FROM salaries WHERE salary IS NULL. Dacă linia dispare, nu mai ai setul fără valori nenule.
        // Expert: IS NULL este testul. = NULL nu ar alege aceste rânduri, pentru că comparația cu NULL nu este adevărată. AVG tot nu ar scrie în tabelă.
        return "SELECT AVG(salary) FROM salaries WHERE salary IS NULL";
        // Începător: metoda a păstrat media goală.
        // Producție: acolada închide avgOfNullSalary. Șirul a rămas în proces.
        // Expert: Nu este UPDATE. Rândul cu NULL rămâne NULL.
    }
}
