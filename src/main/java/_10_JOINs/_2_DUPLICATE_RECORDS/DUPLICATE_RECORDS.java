package _10_JOINs._2_DUPLICATE_RECORDS;

// Martin G. Duplicate Records(Rows)
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8441430#questions


/*                                                    DUPLICATE RECORDS


    ○ duplicate records, also known as duplicate rows,

                   * are identical rows in an SQL table

                   * for a pair of duplicate records, the values in each column coincide

    ○ duplicate rows are not always allowed in a database or a data table

                   * they are sometimes encountered, especially in

                                 - new,

                                 - raw,

                                 - or uncontrolled data


                                   Here’s how to handle duplicates:

            The regular practice of many companies is to

                    * clean their data from such occurrences.

    ○ GROUP BY

                  * the field that differs most among records!
                                  HISTORICAL: propoziția de deasupra nu este regula. Gruparea coloanei care diferă desparte rândurile identice. Coloanele care coincid stau în GROUP BY. HAVING COUNT(*) > 1 arată cheia repetată. Manualul 9.7, 14.19.3, deschis în tur: HAVING filtrează grupurile, nu rândurile dinaintea grupului.



 */

/*
TOPIC:
    Rândul duplicat este un rând din employees_dup ale cărui coloane coincid cu ale altui rând. GROUP BY pe coloanele care coincid și HAVING COUNT(*) > 1 îl arată. DISTINCT doar îl ascunde în rezultat.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Vrei să vezi cheia care stă de două ori în employees_dup, nu o listă din care copia a dispărut.
MECHANISM:
    Serverul grupează rândurile care au aceleași emp_no, first_name și last_name. HAVING COUNT(*) > 1 păstrează grupul cu mai mult de un rând. WHERE nu poate spune „de câte ori”, pentru că filtrul de rând este înainte de grup. Tabela nu se curăță.
LOOKALIKES:
    DISTINCT scoate copia din rezultat și nu întoarce COUNT. UNIQUE la INSERT refuză al doilea rând; nu este această căutare. GROUP BY pe coloana care diferă desparte duplicatele, nu le adună. INNER JOIN nu găsește două rânduri identice în aceeași tabelă.
BOOK THEORY:
    Manualul 9.7, 14.19.3, deschis în tur: HAVING filtrează grupurile; aliasul agregatului este permis în HAVING; ONLY_FULL_GROUP_BY cere coloana din SELECT să fie în GROUP BY sau să depindă de el. Duplicate+Records.pdf, pagina 2, deschisă în tur: duplicate rows sunt rânduri identice, valorile din fiecare coloană coincid. Pagina nu dă GROUP BY. Propoziția „grupează câmpul care diferă cel mai mult” este marcată HISTORICAL în comentariu.
BOOK EXAMPLE:
    repeatedEmployeeKey
REPO DEMO:
    src/main/java/_10_JOINs/_2_DUPLICATE_RECORDS/DUPLICATE_RECORDS.java
BREAK LAB:
    distinctHidesTheCopy nu are COUNT. Copia nu se vede. Șirul nu a fost trimis.
TRADE-OFFS:
    UNIQUE sau PRIMARY KEY când al doilea rând nu are voie să rămână în tabelă. DISTINCT când raportul trebuie să tacă copia și nu să o numere. Aplicația când „duplicatul” se judecă într-un sistem din afara tabelei.
ANTI-PATTERNS:
    GROUP BY pe coloana care diferă, sau DISTINCT crezut curățare a tabelei.
STAFF QUESTIONS:
    Cele cinci de mai jos.
NOVEL TRANSFER:
    Două colete cu același cod, același raft și aceeași dată sunt duplicatul. COUNT arată codul. DISTINCT îl listează o dată și lași copia în tabelă.
SPACED REVIEW:
    O zi: 21 octombrie 2026, HAVING COUNT > 1 arată copia. Șapte zile: 27 octombrie 2026, DISTINCT o ascunde. Treizeci de zile: 19 noiembrie 2026, UNIQUE o refuză la scris, nu o caută.

1. Enunț: Ce problemă rezolvă GROUP BY emp_no, first_name, last_name HAVING COUNT(*) > 1 pe employees_dup?
   Capcană: Șterge rândul identic din tabelă, pentru că așa se curăță datele.
   Cheie: repeatedEmployeeKey arată grupul care are mai mult de un rând identic pe acele coloane. Tabela rămâne. Curățarea ar fi un DELETE sau un UNIQUE, nu acest SELECT.
   Demonstrație: repeatedEmployeeKey
2. Enunț: Ce rămâne în rezultat și în tabelă?
   Capcană: Rezultatul are un singur rând per om, iar al doilea rând dispare din employees_dup la sfârșitul propoziției.
   Cheie: Rezultatul are cheia și copies, doar pentru grupul cu COUNT > 1. Ambele rânduri rămân în tabelă. Efectul se vede la sfârșitul SELECT-ului, nu la COMMIT.
   Demonstrație: repeatedEmployeeKey
3. Enunț: De ce DISTINCT nu este această căutare?
   Capcană: DISTINCT găsește duplicatul, pentru că îl scoate din listă.
   Cheie: distinctHidesTheCopy întoarce combinația o dată și nu spune de câte ori stătea. Nu ai copies. UNIQUE ar fi refuzat al doilea INSERT; DISTINCT nu pune regula în schemă.
   Demonstrație: distinctHidesTheCopy
4. Enunț: Cum eșuează GROUP BY pe coloana care diferă cel mai mult?
   Capcană: Coloana care diferă separă cel mai bine oamenii, deci acolo se văd duplicatele.
   Cheie: Grupul se face pe coloanele care coincid. O coloană care diferă pune rândurile identice în grupuri diferite, iar COUNT nu trece de 1. HAVING nu mai arată copia.
   Demonstrație: repeatedEmployeeKey
5. Enunț: Când cauți cu HAVING și când pui UNIQUE sau rămâi la DISTINCT?
   Capcană: Orice duplicat se rezolvă cu JOIN, pentru că rândurile se potrivesc între ele.
   Cheie: HAVING COUNT când vrei să vezi copia încă stocată. UNIQUE când al doilea INSERT trebuie să cadă. DISTINCT când consumatorul listei nu trebuie să vadă copia și nu îți pasă că ea rămâne. Self join ar putea împerechea rânduri, dar nu este COUNT-ul acestui grup.
   Demonstrație: distinctHidesTheCopy

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vezi cheia care stă de mai multe ori -> repeatedEmployeeKey -> GROUP BY emp_no, first_name, last_name HAVING COUNT(*) > 1 -> nu garantează că employees_dup are două rânduri în tur
    2. Eșecul cel mai mic
           -> DECIDE DISTINCT ca să cureți -> distinctHidesTheCopy -> lista nu mai arată copia, tabela o păstrează -> nu garantează un COUNT
    3. Granița
           -> Grupul cu COUNT este mecanismul. Șirul Java nu șterge rânduri. Workbench nu pune UNIQUE dacă ai rulat doar SELECT.
    4. Alternativa
           -> UNIQUE când scrierea trebuie să cadă. DISTINCT când ascunzi copia în raport. DELETE când chiar scoți rândul, și atunci nu mai este această căutare.
    5. Anti-patternul
           -> GROUP BY pe coloana care diferă. Rândurile identice nu mai stau în același grup.
    6. Dovada
           -> repeatedEmployeeKey ține HAVING COUNT(*). distinctHidesTheCopy nu ține COUNT. 1045 a oprit trimiterea.
    7. Interviu
           -> Duplicatul este rândul ale cărui coloane coincid. HAVING COUNT > 1 îl arată. DISTINCT îl ascunde. UNIQUE îl refuză la scris.
*/

public class DUPLICATE_RECORDS {

    /*
    🟢 Începător
        Ce este            cheia care stă de două ori, cu numărul de copii
        De ce e nevoie     fără HAVING, grupul de un rând se amestecă cu grupul duplicat
    🟡 Producție
        Cum se scrie       SELECT emp_no, first_name, last_name, COUNT(*) AS copies FROM employees_dup GROUP BY emp_no, first_name, last_name HAVING COUNT(*) > 1
    🔴 Staff
        Unde               HAVING vede grupul; WHERE nu are încă COUNT; tabela nu se schimbă
    Pași
        1. Grupează coloanele care trebuie să coincidă.
        2. Păstrează COUNT > 1.
        3. Nu trimite șirul.
    */
    // Începător: metoda păstrează căutarea copiei, nu ștergerea ei.
    // Producție: public static String repeatedEmployeeKey(). Dacă dispare, PDF-ul care definește rândul identic nu mai are propoziția.
    // Expert: Nu deschide JDBC. Nu pune DISTINCT. Nu promite că emp_no singur este tot rândul: un emp_no cu două nume sunt două grupuri.
    public static String repeatedEmployeeKey() {
        // Începător: rămâne combinația care apare de mai multe ori, împreună cu de câte ori apare. Un singur rând nu rămâne în listă.
        // Producție: GROUP BY emp_no, first_name, last_name HAVING COUNT(*) > 1. Dacă linia dispare, rămâne sfatul retras, să grupezi coloana care diferă.
        // Expert: HAVING COUNT(*) > 1 nu este WHERE. Sub ONLY_FULL_GROUP_BY, copies este agregat, iar cele trei coloane sunt în GROUP BY, deci nu este 1055. UNIQUE ar fi oprit al doilea INSERT; acest SELECT nu îl oprește.
        return "SELECT emp_no, first_name, last_name, COUNT(*) AS copies FROM employees_dup GROUP BY emp_no, first_name, last_name HAVING COUNT(*) > 1";
        // Începător: metoda a înapoiat căutarea, nu un DELETE.
        // Producție: acolada închide repeatedEmployeeKey. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este un COUNT de 1. Copia se vede doar dacă serverul primește șirul și grupul are mai mult de un rând.
    }

    /*
    🟢 Începător
        Ce este            aceeași listă, fără să vezi că un rând stătea de două ori
        De ce e nevoie     fără ea, DISTINCT pare că a găsit duplicatul
    🟡 Producție
        Cum se scrie       SELECT DISTINCT emp_no, first_name, last_name FROM employees_dup
    🔴 Staff
        Unde               rezultatul pierde copia; employees_dup o păstrează; nu este HAVING
    Pași
        1. Nu pune COUNT.
        2. Nu o trimite.
        3. Nu o numi ștergere.
    */
    // Începător: metoda păstrează lista care tace copia.
    // Producție: public static String distinctHidesTheCopy(). Dacă dispare, vecinul rămâne o definiție.
    // Expert: Nu este UNION. UNION scoate duplicatul dintre două blocuri. Aici copia este în aceeași tabelă.
    public static String distinctHidesTheCopy() {
        // Începător: fiecare combinație apare o dată în rezultat, chiar dacă în tabelă stă de două ori.
        // Producție: SELECT DISTINCT emp_no, first_name, last_name FROM employees_dup. Dacă linia dispare, nu mai ai ascunderea.
        // Expert: DISTINCT nu pune UNIQUE în schemă. Următorul INSERT poate aduce încă o copie. ORDER BY o coloană care nu este în listă poate cădea sub regula de DISTINCT din 14.19.3; acest șir nu ordonează.
        return "SELECT DISTINCT emp_no, first_name, last_name FROM employees_dup";
        // Începător: metoda a păstrat lista curată la vedere, nu tabela.
        // Producție: acolada închide distinctHidesTheCopy. Șirul a rămas în proces.
        // Expert: Nu s-a numărat copies. Fără COUNT nu știi dacă ascunderea a avut ce ascunde.
    }
    // Începător: clasa se oprește la grupul care numără copia și la lista care o ascunde.
    // Producție: acolada închide DUPLICATE_RECORDS. Niciun DELETE nu este în aceste metode.
    // Expert: UNIQUE ar refuza următorul INSERT. Acolada nu pune constrângerea.
}
