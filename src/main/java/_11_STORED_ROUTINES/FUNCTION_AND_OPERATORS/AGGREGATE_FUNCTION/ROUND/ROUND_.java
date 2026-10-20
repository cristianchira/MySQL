package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.ROUND;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376502#questions


/*                                                           ROUND()


    ○ ROUND(#,decimal_places)


                  * numeric, or math, function you can use

                  * usually applied to the single values that aggregate functions return


    ○ Example


                  * SELECT   ROUND(AVG(salary), 2)         -- round with scale 2

                    FROM salaries

                    WHERE from_date > '1997-01-01';



 */

/*
TOPIC:
    ROUND(număr, scara) schimbă un număr, nu un grup. Pe un DECIMAL, jumătatea se rotunjește în direcția opusă lui zero. Pe un aproximativ, rezultatul ține de biblioteca C. AVG de dinaintea lui este agregatul; ROUND nu este.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Media salariilor se arată cu două zecimale în rezultat. salaries nu își schimbă scara. Fiecare salariu nu este rotunjit în tabelă.
MECHANISM:
    Serverul execută șirul din roundTheAverage. Mai întâi AVG lasă o valoare, apoi ROUND îi pune scara. 14.6, deschis în tur: pentru exact, fracția de la 0.5 în sus pleacă dinspre zero. Efectul se vede la sfârșitul propoziției.
LOOKALIKES:
    AVG(salary) fără ROUND lasă scara mediei. TRUNCATE taie, nu rotunjește jumătatea dinspre zero. Un ALTER pe coloană schimbă scara stocată; ROUND nu.
BOOK THEORY:
    Manualul 9.7, pagina funcțiilor matematice, deschisă în tur: ROUND(X,D) rotunjește un argument; la numere exacte, jumătatea pleacă dinspre zero; la numere aproximative, rezultatul depinde de biblioteca C; ROUND(2.5) și ROUND(25E-1) pot diferi. ROUND().pdf, deschis în tur: ROUND(#, decimal_places). PDF-ul nu spune regula jumătății.
BOOK EXAMPLE:
    roundTheAverage
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/ROUND/ROUND_.java
BREAK LAB:
    roundEachSalary ține SELECT ROUND(salary, 2) FROM salaries. Rămâne un rând pe contract, nu media. În tur șirul nu a fost trimis.
TRADE-OFFS:
    Schimbi scara coloanei când valoarea stocată trebuie să aibă două zecimale pentru orice client, nu doar în acest rezultat.
ANTI-PATTERNS:
    ROUND(salary, 2) crezut agregat, pentru că stă în folderul de agregate.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Greutatea medie afișată cu două zecimale este ROUND pe AVG. Rotunjirea fiecărui colet lasă lista. Un aproximativ 2.5 poate cădea pe par, nu dinspre zero.
SPACED REVIEW:
    O zi: 21 octombrie 2026, ROUND nu grupează. Șapte zile: 27 octombrie 2026, 2.5 exact pleacă dinspre zero. Treizeci de zile: 19 noiembrie 2026, aproximativul nu ține aceeași jumătate.

1. Enunț: Ce problemă rezolvă ROUND(AVG(salary), 2)?
   Capcană: Schimbă coloana salary la două zecimale și rescrie rândurile.
   Cheie: roundTheAverage rotunjește media în rezultat. salaries rămâne. AVG este agregatul. ROUND este funcția numerică de pe acea valoare.
   Demonstrație: roundTheAverage
2. Enunț: Ce se întâmplă cu jumătatea 2.5 când argumentul este exact, și când este aproximativ?
   Capcană: Ambele dau 2, pentru că jumătatea cade mereu la par.
   Cheie: Manualul deschis: la exact, 2.5 pleacă dinspre zero, deci 3. La aproximativ, pe multe sisteme jumătatea cade la par, iar ROUND(2.5) și ROUND(25E-1) pot să nu fie egale. Tabela nu se schimbă.
   Demonstrație: roundTheAverage
3. Enunț: De ce ROUND nu este AVG și nu este TRUNCATE?
   Capcană: Toate trei scurtează salariul, deci sunt agregate.
   Cheie: AVG face o valoare din grup. ROUND primește un număr. TRUNCATE taie fără regula jumătății dinspre zero. roundEachSalary arată că ROUND pe coloană lasă câte un rând, nu media.
   Demonstrație: roundEachSalary
4. Enunț: Cum eșuează ROUND(salary, 2) când voiai media cu două zecimale?
   Capcană: Serverul grupează singur, pentru că ROUND stă lângă agregate.
   Cheie: roundEachSalary nu are AVG și nu are GROUP BY. Rezultatul are un rând pe contract. Media cu două zecimale este ROUND pe AVG, nu ROUND pe coloană.
   Demonstrație: roundEachSalary
5. Enunț: Când schimbi scara coloanei și când lași ROUND doar în rezultat?
   Capcană: ROUND este permanent, pentru că funcția stă în SELECT.
   Cheie: ROUND schimbă valoarea din rezultat, la sfârșitul propoziției. ALTER pe coloană schimbă ce se stochează la următorul INSERT. Aplicația poate rotunji ea, dacă niciun alt client nu trebuie să vadă aceeași scară.
   Demonstrație: roundTheAverage

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE media afișată cu două zecimale -> roundTheAverage -> SELECT ROUND(AVG(salary), 2) FROM salaries WHERE from_date > '1997-01-01' -> nu garantează că salary s-a stocat cu două zecimale
    2. Eșecul cel mai mic
           -> DECIDE ROUND pe coloană să fie media -> roundEachSalary -> SELECT ROUND(salary, 2) FROM salaries -> nu garantează un singur rând
    3. Granița
           -> ROUND este funcția numerică MySQL. Folderul de agregate nu o face agregat. Java BigDecimal nu este regula jumătății din server.
    4. Alternativa
           -> TRUNCATE când tai, nu rotunjești. ALTER pe coloană când scara stocată trebuie să se schimbe. AVG singur când scara mediei este acceptabilă.
    5. Anti-patternul
           -> ROUND(salary, 2) citit ca totalul rotunjit al companiei. Rămân rândurile, nu media.
    6. Dovada
           -> roundTheAverage ține ROUND pe AVG. roundEachSalary ține ROUND pe coloană. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> ROUND schimbă un număr în rezultat. Jumătatea exactă pleacă dinspre zero. Aproximativul poate cădea la par. AVG de dinainte este agregatul.
*/

public class ROUND_ {

    /*
    🟢 Începător
        Ce este            media arătată cu două zecimale, fără să rescrii salariile
        De ce e nevoie     fără ROUND, scara mediei rămâne a agregatului
    🟡 Producție
        Cum se scrie       SELECT ROUND(AVG(salary), 2) FROM salaries WHERE from_date > '1997-01-01'
    🔴 Staff
        Unde               AVG face valoarea, ROUND îi pune scara. Pe exact, jumătatea pleacă dinspre zero
    Pași
        1. Agregă mai întâi.
        2. Rotunjește valoarea.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează media cu două zecimale în rezultat.
    // Producție: public static String roundTheAverage(). Dacă dispare, ROUND rămâne doar în comentariu.
    // Expert: Nu este ALTER TABLE. Nu este TRUNCATE. Aproximativul nu ține aceeași jumătate.
    public static String roundTheAverage() {
        // Începător: rămâne o medie scurtată la două zecimale, iar contractele rămân cu salariul lor.
        // Producție: SELECT ROUND(AVG(salary), 2) FROM salaries WHERE from_date > '1997-01-01'. Dacă linia dispare, nu mai ai scara pusă pe agregat.
        // Expert: Dacă AVG dă DOUBLE, jumătatea ține de biblioteca C. Dacă dă DECIMAL, 0.5 pleacă dinspre zero. Șirul nu schimbă tipul coloanei.
        return "SELECT ROUND(AVG(salary), 2) FROM salaries WHERE from_date > '1997-01-01'";
        // Începător: metoda a înapoiat rotunjirea mediei, nu media.
        // Producție: acolada închide roundTheAverage. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă scara NERULAT. Acolada nu este un UPDATE.
    }

    /*
    🟢 Începător
        Ce este            fiecare salariu rotunjit, încă un rând pe contract
        De ce e nevoie     fără el, pare că ROUND grupează
    🟡 Producție
        Cum se scrie       SELECT ROUND(salary, 2) FROM salaries
    🔴 Staff
        Unde               nu există AVG și nu există GROUP BY. Numărul de rânduri din rezultat urmărește contractele
    Pași
        1. Rotunjește coloana.
        2. Nu pune agregat.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează rotunjirea care nu face media.
    // Producție: public static String roundEachSalary(). Dacă dispare, confuzia cu agregatul rămâne o frază.
    // Expert: Un UPDATE cu ROUND(salary, 2) ar scrie scara în tabelă. Acest SELECT nu scrie.
    public static String roundEachSalary() {
        // Începător: fiecare contract rămâne un rând, cu salariul arătat mai scurt.
        // Producție: SELECT ROUND(salary, 2) FROM salaries. Dacă linia dispare, nu mai ai ROUND fără grup.
        // Expert: Folderul se numește agregat, iar funcția nu agregă. TRUNCATE(salary, 2) ar tăia, nu ar aplica jumătatea dinspre zero.
        return "SELECT ROUND(salary, 2) FROM salaries";
        // Începător: metoda a păstrat lista rotunjită.
        // Producție: acolada închide roundEachSalary. Șirul a rămas în proces.
        // Expert: Nu este COUNT și nu este o rutină stocată.
    }
}
