package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.SUM;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376508#questions

// https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_sum



/*                                             SUM

    ○ SUM([DISTINCT] expr) [over_clause]

                  * Returns the sum of expr.

                  * If the return set has no rows, SUM() returns NULL.

                  * The DISTINCT keyword can be used to sum only the distinct values of expr.

    ○ If there are no matching rows, or if expr is NULL, SUM() returns NULL.


 */


/*
TOPIC:
    SUM(salary) adună valorile nenule din coloană și lasă o singură sumă pe grup. Nu scrie suma înapoi în salaries. Pe un set fără valori nenule, suma nu este zero scris în tabelă.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Totalul salariilor de după 1997-01-01 este o valoare în rezultat. Rândurile rămân. NULL nu se adună ca zero.
MECHANISM:
    Serverul execută șirul din sumSalaryAfterDate. 14.19.1: SUM întoarce suma și ignoră NULL; pentru un argument exact rezultatul este DECIMAL, pentru unul aproximativ DOUBLE. WHERE alege rândurile înainte de adunare. Efectul se vede la sfârșitul propoziției.
LOOKALIKES:
    COUNT(*) numără rândurile, nu salariile. AVG împarte suma la numărul de valori nenule. SUM(salary) + 0 în aplicație, după ce ai citit rândurile, nu este aceeași citire: alt client poate să fi schimbat un salariu între timp.
BOOK THEORY:
    Manualul 9.7, 14.19.1, deschis în tur: SUM întoarce suma, ignoră NULL și nu lucrează pe valori temporale fără conversie la număr. Comentariul din clasă spune că un set fără rânduri dă NULL. SUM().pdf, deschis în tur, are pe paginile citite titlurile SUM() și COUNT(*).
BOOK EXAMPLE:
    sumSalaryAfterDate
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/SUM/SUM_.java
BREAK LAB:
    sumOfDates ține SELECT SUM(from_date) FROM salaries. 14.19.1: SUM pe o valoare temporală o transformă în număr și pierde ce este după primul caracter nenumeric. În tur șirul nu a fost trimis.
TRADE-OFFS:
    Aplicația când rândurile sunt deja la client. AVG când întrebarea este media, nu totalul.
ANTI-PATTERNS:
    SUM(from_date) ca să „aduni” datele contractelor.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Greutatea totală a coletelor primite după o zi este SUM pe greutatea numerică, cu WHERE pe dată. Suma datelor de recepție nu este greutatea.
SPACED REVIEW:
    O zi: 21 octombrie 2026, SUM nu scrie în tabelă. Șapte zile: 27 octombrie 2026, NULL nu este zero în sumă. Treizeci de zile: 19 noiembrie 2026, data nu se adună cu SUM.

1. Enunț: Ce problemă rezolvă SUM(salary) cu un filtru pe from_date?
   Capcană: Suma înlocuiește salariile mai vechi cu totalul și le șterge din salaries.
   Cheie: sumSalaryAfterDate lasă o valoare în rezultat, doar pentru rândurile de după 1997-01-01. salaries rămâne. Nu există CREATE și nu există CALL.
   Demonstrație: sumSalaryAfterDate
2. Enunț: Ce se întâmplă cu un salariu NULL și cu un set din care WHERE nu lasă rânduri?
   Capcană: NULL se adună ca 0, iar setul gol dă 0, ca să nu se piardă totalul.
   Cheie: 14.19.1: SUM ignoră NULL. Comentariul din clasă: dacă nu rămân rânduri, SUM întoarce NULL, nu un rând nou în tabelă. Totalul nu este scris în salaries.
   Demonstrație: sumSalaryAfterDate
3. Enunț: De ce SUM(salary) nu este COUNT(*) și nu este AVG(salary)?
   Capcană: Toate trei dau mărimea tabelului, deci se pot înlocui.
   Cheie: COUNT(*) numără rânduri. AVG este suma valorilor nenule împărțită la numărul lor. SUM este totalul. Un NULL schimbă COUNT(*) și AVG altfel decât schimbă SUM: la SUM lipsește din adunare, la COUNT(*) rândul rămâne în număr.
   Demonstrație: sumSalaryAfterDate
4. Enunț: Cum eșuează SUM(from_date)?
   Capcană: Datele se adună calendaristic și rezultatul este o dată.
   Cheie: sumOfDates cere SUM pe un temporal. 14.19.1: valoarea este convertită la număr și se pierde ce urmează primului caracter nenumeric. Totalul de salarii rămâne SUM(salary), nu suma datelor.
   Demonstrație: sumOfDates
5. Enunț: Când aduni în aplicație și când lași SUM pe server?
   Capcană: Suma în aplicație vede același lucru, pentru că a citit aceleași rânduri mai devreme.
   Cheie: Serverul când totalul trebuie să fie pe setul de acum și nu vrei să muți salaries. Aplicația când rândurile sunt deja la client și nimeni nu le schimbă între citire și adunare. AVG când întrebarea nu este totalul.
   Demonstrație: sumOfDates

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE totalul salariilor de după o dată -> sumSalaryAfterDate -> SELECT SUM(salary) FROM salaries WHERE from_date > '1997-01-01' -> nu garantează că un NULL a fost adunat ca zero
    2. Eșecul cel mai mic
           -> DECIDE să aduni datele -> sumOfDates -> SELECT SUM(from_date) FROM salaries -> nu garantează o dată rezultat
    3. Granița
           -> SUM este agregatul MySQL. Comentariul din clasă nu este propoziția. Java nu adună salaries.
    4. Alternativa
           -> AVG pentru medie. COUNT(*) pentru rânduri. Aplicația când setul este deja la client.
    5. Anti-patternul
           -> SUM(from_date). Data nu se adună: serverul o face număr și pierde restul.
    6. Dovada
           -> sumSalaryAfterDate ține SUM(salary). sumOfDates ține SUM(from_date). root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> SUM adună valorile nenule și nu scrie în tabelă. Setul gol nu lasă un zero în salaries. Data nu se adună cu SUM.
*/

public class SUM_ {

    /*
    🟢 Începător
        Ce este            totalul salariilor de după o dată, fără să schimbi tabela
        De ce e nevoie     fără el, totalul s-ar calcula din rânduri mutate la client
    🟡 Producție
        Cum se scrie       SELECT SUM(salary) FROM salaries WHERE from_date > '1997-01-01'
    🔴 Staff
        Unde               WHERE alege rândurile înainte. NULL nu intră în sumă. Rezultatul exact este DECIMAL, după 14.19.1
    Pași
        1. Adună salary, nu from_date.
        2. Filtrează data cu WHERE.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează adunarea salariilor.
    // Producție: public static String sumSalaryAfterDate(). Dacă dispare, SUM rămâne doar în comentariu.
    // Expert: Nu este COUNT(*). Nu scrie un rând nou cu totalul.
    public static String sumSalaryAfterDate() {
        // Începător: rămâne o singură sumă pentru salariile de după dată, iar contractele rămân.
        // Producție: SELECT SUM(salary) FROM salaries WHERE from_date > '1997-01-01'. Dacă linia dispare, nu mai ai totalul filtrat.
        // Expert: Un salary NULL nu devine 0 în sumă. Dacă WHERE nu lasă rânduri, comentariul din clasă spune că SUM întoarce NULL, nu un UPDATE.
        return "SELECT SUM(salary) FROM salaries WHERE from_date > '1997-01-01'";
        // Începător: metoda a înapoiat adunarea, nu totalul.
        // Producție: acolada închide sumSalaryAfterDate. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă suma NERULAT. Acolada nu este un lacăt pe salaries.
    }

    /*
    🟢 Începător
        Ce este            adunarea unei date, care nu rămâne o dată
        De ce e nevoie     fără ea, pare că SUM știe calendarul
    🟡 Producție
        Cum se scrie       SELECT SUM(from_date) FROM salaries
    🔴 Staff
        Unde               14.19.1: temporalul devine număr și se taie la primul caracter nenumeric
    Pași
        1. Pune data în SUM.
        2. Nu o converti la secunde.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează adunarea care pierde data.
    // Producție: public static String sumOfDates(). Dacă dispare, eșecul rămâne o frază.
    // Expert: SEC_TO_TIME(SUM(TIME_TO_SEC(...))) este ocolirea din manual, pentru timp, nu această linie.
    public static String sumOfDates() {
        // Începător: datele contractelor nu se adună într-o dată nouă.
        // Producție: SELECT SUM(from_date) FROM salaries. Dacă linia dispare, nu mai ai temporalul în SUM.
        // Expert: AVG(from_date) are aceeași limită din 14.19.1. Niciuna nu este SUM(salary).
        return "SELECT SUM(from_date) FROM salaries";
        // Începător: metoda a păstrat adunarea greșită.
        // Producție: acolada închide sumOfDates. Șirul a rămas în proces.
        // Expert: Nu este o eroare de rutină stocată. Este agregatul folosit pe un tip pe care manualul nu îl adună ca timp.
    }
}
