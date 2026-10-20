package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.MIN_MAX;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376500#questions

//


/*                                                MON MAX   -- aggregate function


    ○ MAX([DISTINCT] expr) [over_clause]

                 * https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_max

                  * Returns the maximum value of expr.

                  * MAX() may take a string argument;

                  * in such cases, it returns the maximum string value.

                                - See Section 8.3.1, “How MySQL Uses Indexes”.

                                - The DISTINCT keyword can be used to find the maximum of the distinct values of expr,

                                 - however, this produces the same result as omitting DISTINCT.

          If there are no matching rows, or if expr is NULL,

                 * MAX() returns NULL.

    ○ MIN([DISTINCT] expr) [over_clause]

                  * https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_min

                  * Returns the minimum value of expr.

                  * MIN() may take a string argument;

                  * in such cases, it returns the minimum string value. See Section 8.3.1, “How MySQL Uses Indexes”.

                  * The DISTINCT keyword can be used to find the minimum of the distinct values of expr,

                  * however, this produces the same result as omitting DISTINCT.

           If there are no matching rows, or if expr is NULL, MIN() returns NULL.


    ○ Example

           SELECT emp_no, MIN(salary) AS min_salary, MAX(salary) AS max_salary

           FROM salaries

           WHERE from_date > '1997-01-01'

           GROUP BY emp_no

           ORDER BY max_salary DESC

           LIMIT 6;



 */

/*
TOPIC:
    MIN(salary) și MAX(salary) sunt cea mai mică și cea mai mare valoare nenulă pe grup. DISTINCT nu schimbă rezultatul, cum spune comentariul din clasă. Tabela nu se rescrie.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Pentru fiecare emp_no, după 1997-01-01, rămân în rezultat capetele salariului, nu toate contractele. salaries rămâne.
MECHANISM:
    Serverul execută șirul din minMaxPerEmployee. 14.19.1: MIN și MAX întorc extremul și ignoră NULL. GROUP BY emp_no face câte un grup. Efectul se vede la sfârșitul propoziției. Un index poate fi folosit la extrem, dar propoziția nu creează indexul.
LOOKALIKES:
    ORDER BY salary DESC LIMIT 1 ia un rând întreg, nu extremul pe fiecare emp_no. LEAST și GREATEST compară argumente într-o expresie, nu un grup de rânduri. Un index pe salary nu este MAX.
BOOK THEORY:
    Manualul 9.7, 14.19.1, deschis în tur: MIN întoarce minimul, MAX maximul, iar agregatele ignoră NULL. MIN()+MAX().pdf, deschis în tur: MAX întoarce valoarea maximă a unei coloane. Comentariul din clasă spune că DISTINCT nu schimbă MIN sau MAX și că un set fără rânduri dă NULL.
BOOK EXAMPLE:
    minMaxPerEmployee
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/MIN_MAX/DB_MIN_MAX.java
BREAK LAB:
    leastOnTwoLiterals ține SELECT LEAST(10, 20). Nu citește salaries și nu este minimul pe angajat. În tur șirul nu a fost trimis.
TRADE-OFFS:
    ORDER BY salary LIMIT 1 când îți trebuie rândul întreg al unui singur extrem, nu capetele fiecărui angajat.
ANTI-PATTERNS:
    LEAST(salary, 0) ca să „găsești” salariul minim din tabelă: compară pe rând, nu pe grup.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Cel mai greu colet de pe fiecare raft este MAX(weight) cu GROUP BY raft. LEAST între două greutăți scrise în expresie nu citește raftul.
SPACED REVIEW:
    O zi: 21 octombrie 2026, un extrem pe grup. Șapte zile: 27 octombrie 2026, LEAST nu este MIN pe tabelă. Treizeci de zile: 19 noiembrie 2026, DISTINCT nu schimbă extremul.

1. Enunț: Ce problemă rezolvă MIN(salary) și MAX(salary) grupate pe emp_no?
   Capcană: Șterg contractele din mijloc și lasă în salaries doar capetele.
   Cheie: minMaxPerEmployee lasă în rezultat, pentru fiecare angajat cu contract după dată, cel mai mic și cel mai mare salariu. Rândurile rămân. Nu există CREATE.
   Demonstrație: minMaxPerEmployee
2. Enunț: Ce se întâmplă cu un salariu NULL în grup?
   Capcană: NULL este mai mic decât orice salariu, deci MIN devine NULL.
   Cheie: 14.19.1: agregatul ignoră NULL. MIN este cea mai mică valoare nenulă. Dacă grupul nu are valori nenule, comentariul din clasă spune că MIN întoarce NULL. Tabela nu se actualizează.
   Demonstrație: minMaxPerEmployee
3. Enunț: De ce MIN(salary) nu este LEAST(10, 20) și nu este ORDER BY salary LIMIT 1?
   Capcană: Toate trei dau minimul, deci planul și rândurile atinse sunt aceleași.
   Cheie: leastOnTwoLiterals nu citește salaries. ORDER BY salary LIMIT 1 dă un rând, nu câte un extrem pe emp_no. MIN cu GROUP BY emp_no este agregatul pe grup.
   Demonstrație: leastOnTwoLiterals
4. Enunț: Cum eșuează DISTINCT în MIN, crezut ca un alt minim?
   Capcană: MIN(DISTINCT salary) scoate duplicatele și poate da un minim mai mare.
   Cheie: Comentariul din clasă: DISTINCT la MIN și la MAX dă același rezultat ca fără el, pentru că extremul unei valori repetate este tot extremul. Nu este COUNT(DISTINCT), care chiar numără valorile diferite.
   Demonstrație: minMaxPerEmployee
5. Enunț: Când ceri rândul întreg cu ORDER BY și când rămâi pe MIN și MAX?
   Capcană: MIN întoarce și numele, și data, pentru că sunt pe același rând cu salariul minim.
   Cheie: MIN întoarce valoarea, nu rândul. Ca să ai rândul întreg al unui singur extrem, ORDER BY salary LIMIT 1. Ca să ai capetele fiecărui emp_no, rămâi pe GROUP BY. Aplicația când setul este deja la client.
   Demonstrație: leastOnTwoLiterals

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE capetele salariului pe angajat, după o dată -> minMaxPerEmployee -> SELECT emp_no, MIN(salary), MAX(salary) FROM salaries WHERE from_date > '1997-01-01' GROUP BY emp_no -> nu garantează rândul întreg al contractului
    2. Eșecul cel mai mic
           -> DECIDE LEAST să fie minimul din tabelă -> leastOnTwoLiterals -> SELECT LEAST(10, 20) -> nu garantează un salary citit
    3. Granița
           -> MIN și MAX sunt agregatele MySQL. Un index care ajută extremul nu este propoziția. Java nu alege salariul.
    4. Alternativa
           -> ORDER BY salary LIMIT 1 pentru un rând. LEAST pentru două valori deja în expresie. COUNT pentru număr, nu pentru extrem.
    5. Anti-patternul
           -> LEAST(salary, 0) pe fiecare rând, citit ca salariul minim al companiei. Compara pe rând și poate întoarce 0, care nu este în salaries.
    6. Dovada
           -> minMaxPerEmployee ține GROUP BY. leastOnTwoLiterals ține LEAST. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> MIN și MAX dau extremul nenul pe grup. NULL nu câștigă minimul. DISTINCT nu schimbă extremul. LEAST nu citește tabela.
*/

public class DB_MIN_MAX {

    /*
    🟢 Începător
        Ce este            cel mai mic și cel mai mare salariu al fiecărui angajat, după o dată
        De ce e nevoie     fără grup, capetele se amestecă între oameni
    🟡 Producție
        Cum se scrie       SELECT emp_no, MIN(salary) AS min_salary, MAX(salary) AS max_salary FROM salaries WHERE from_date > '1997-01-01' GROUP BY emp_no
    🔴 Staff
        Unde               un grup pe emp_no. NULL ignorat. Rezultatul nu înlocuiește contractele
    Pași
        1. Filtrează data.
        2. Grupează pe emp_no.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează capetele salariului pe angajat.
    // Producție: public static String minMaxPerEmployee(). Dacă dispare, exemplul din comentariu nu este propoziția gata de trimis.
    // Expert: Nu întoarce from_date al contractului minim. ORDER BY max_salary ar ordona grupurile, nu ar schimba extremul.
    public static String minMaxPerEmployee() {
        // Începător: pentru fiecare angajat rămân două cifre, nu toate contractele.
        // Producție: SELECT emp_no, MIN(salary) AS min_salary, MAX(salary) AS max_salary FROM salaries WHERE from_date > '1997-01-01' GROUP BY emp_no. Dacă linia dispare, nu mai ai extremul pe grup.
        // Expert: Comentariul din clasă: MIN(DISTINCT salary) dă același extrem. Un index pe salary poate scurta căutarea și nu este agregatul.
        return "SELECT emp_no, MIN(salary) AS min_salary, MAX(salary) AS max_salary FROM salaries WHERE from_date > '1997-01-01' GROUP BY emp_no";
        // Începător: metoda a înapoiat capetele, nu contractele.
        // Producție: acolada închide minMaxPerEmployee. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă planul NERULAT. Acolada nu creează indexul.
    }

    /*
    🟢 Începător
        Ce este            comparația a două numere scrise în propoziție, nu minimul din tabelă
        De ce e nevoie     fără ea, LEAST pare că citește salaries
    🟡 Producție
        Cum se scrie       SELECT LEAST(10, 20)
    🔴 Staff
        Unde               14.4.2: LEAST întoarce cel mai mic argument. Dacă un argument este NULL, rezultatul este NULL. Nu există grup
    Pași
        1. Compară două literale.
        2. Nu numi tabela.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează minimul a două numere, fără salarii.
    // Producție: public static String leastOnTwoLiterals(). Dacă dispare, vecinul agregatului rămâne o frază.
    // Expert: GREATEST este maximul argumentelor, cu aceeași regulă de NULL. Nu este MAX(salary).
    public static String leastOnTwoLiterals() {
        // Începător: rămâne 10, pentru că este mai mic decât 20, și nicio tabelă nu este citită.
        // Producție: SELECT LEAST(10, 20). Dacă linia dispare, nu mai ai expresia care nu este MIN pe grup.
        // Expert: LEAST(salary, 0) ar merge pe fiecare rând și ar putea da 0. MIN pe grup nu face asta.
        return "SELECT LEAST(10, 20)";
        // Începător: metoda a păstrat comparația de literale.
        // Producție: acolada închide leastOnTwoLiterals. Șirul a rămas în proces.
        // Expert: Nu este un index și nu este ORDER BY salary LIMIT 1.
    }
}
