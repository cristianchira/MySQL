package _7_Data_Types._1_Numeric;

// Numeric Data Types
// https://dev.mysql.com/doc/refman/8.0/en/numeric-types.html


/*                                Numeric Data Type

    1. Numeric Data Type Syntax

    2. Integer Types (Exact Value) - INTEGER, INT, SMALLINT, TINYINT, MEDIUMINT, BIGINT

    3. Fixed-Point Types (Exact Value) - DECIMAL, NUMERIC

    4. Floating-Point Types (Approximate Value) - FLOAT, DOUBLE

    5. Bit-Value Type - BIT

    6. Numeric Type Attributes

    7.Out-of-Range and Overflow Handling

 */

/*
TOPIC:
    Tipul numeric se alege în CREATE TABLE: INT este exact fără scară, DECIMAL ține scara, FLOAT aproximează.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu pui aceeași coloană în trei tipuri deodată. purchase_number este INT.
MECHANISM:
    Serverul stochează un tip pe coloană. Lista din curs nu este o propoziție. Alegerea se vede în definiție.
LOOKALIKES:
    DECIMAL pentru bani, FLOAT pentru aproximație, INT pentru număr întreg.
BOOK THEORY:
    Manualul 9.7, 13.1.2, deschis în tur: INT ocupă 4 octeți. UNSIGNED se scrie explicit. TINYINT ocupă 1 octet.  PDF-ul de tipuri numerice este acoperit de fișierele copil deschise. Pagina de întregi 9.7 a fost deschisă.
BOOK EXAMPLE:
    chooseIntForTheCounter
REPO DEMO:
    src/main/java/_7_Data_Types/_1_Numeric/Numeric.java
BREAK LAB:
    floatForTheCounter ține CREATE TABLE sales (purchase_number FLOAT). FLOAT aproximează. INT este exact. DECIMAL are scară.
TRADE-OFFS:
    DECIMAL pentru sume. FLOAT pentru măsurători aproximative.
ANTI-PATTERNS:
    FLOAT pentru purchase_number. Contorul nu mai este exact.
STAFF QUESTIONS:
    chooseIntForTheCounter și floatForTheCounter. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Numărul de lot este INT. Suma este DECIMAL. Senzorul este FLOAT.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE sales (purchase_number INT)?
   Capcană: FLOAT este bun pentru un număr de ordine.
   Cheie: chooseIntForTheCounter: purchase_number este întreg exact, nu scară și nu aproximare. fără tip, CREATE TABLE nu are coloană completă
   Demonstrație: chooseIntForTheCounter
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE sales (purchase_number INT)?
   Capcană: Serverul alege tipul după primul INSERT.
   Cheie: Efectul este: purchase_number este întreg exact, nu scară și nu aproximare. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: chooseIntForTheCounter
3. Enunț: De ce CREATE TABLE sales (purchase_number INT) nu este vecinul său?
   Capcană: DECIMAL și INT ocupă la fel și țin la fel.
   Cheie: DECIMAL pentru bani, FLOAT pentru aproximație, INT pentru număr întreg.
   Demonstrație: chooseIntForTheCounter
4. Enunț: Cum eșuează CREATE TABLE sales (purchase_number FLOAT)?
   Capcană: Lista din comentariu creează coloanele.
   Cheie: floatForTheCounter: FLOAT aproximează. INT este exact. DECIMAL are scară.
   Demonstrație: floatForTheCounter
5. Enunț: Când nu mai trimiți CREATE TABLE sales (purchase_number INT)?
   Capcană: UNSIGNED este implicit la INT.
   Cheie: DECIMAL pentru sume. FLOAT pentru măsurători aproximative.
   Demonstrație: chooseIntForTheCounter

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE valoarea este număr întreg -> chooseIntForTheCounter -> CREATE TABLE sales (purchase_number INT) -> nu alege singur tipul după prima valoare inserată
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> floatForTheCounter -> CREATE TABLE sales (purchase_number FLOAT) -> FLOAT aproximează. INT este exact. DECIMAL are scară.
    3. Granița
           -> Tipul este în propoziție. Lista din comentariu nu este CREATE TABLE.
    4. Alternativa
           -> DECIMAL pentru bani, FLOAT pentru aproximație, INT pentru număr întreg.
    5. Anti-patternul
           -> FLOAT pentru purchase_number. Contorul nu mai este exact.
    6. Dovada
           -> chooseIntForTheCounter ține propoziția. floatForTheCounter ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> INT exact fără scară, DECIMAL exact cu scară, FLOAT aproximativ. Tipul stă în CREATE TABLE, nu în lista din curs.
*/

public class Numeric {
    /*
    🟢 Începător
        Ce este            purchase_number este întreg exact, nu scară și nu aproximare
        De ce e nevoie     fără tip, CREATE TABLE nu are coloană completă
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este amount DECIMAL(5,2) sau reading FLOAT
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu amount DECIMAL(5,2) sau reading FLOAT.
    */
    // Începător: chooseIntForTheCounter păstrează propoziția pentru purchase_number este întreg exact, nu scară și nu aproximare
    // Producție: public static String chooseIntForTheCounter(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Un comentariu care enumeră tipurile nu creează coloana.
    public static String chooseIntForTheCounter() {
        // Începător: purchase_number este întreg exact, nu scară și nu aproximare
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Un comentariu care enumeră tipurile nu creează coloana. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number INT)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide chooseIntForTheCounter. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            numărul de ordine este aproximativ
        De ce e nevoie     fără textul greșit, orice tip numeric pare potrivit la un contor
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number FLOAT)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care numărul de ordine este aproximativ
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu amount DECIMAL(5,2) sau reading FLOAT.
    */
    // Începător: floatForTheCounter păstrează textul greșit pentru numărul de ordine este aproximativ
    // Producție: public static String floatForTheCounter(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. FLOAT aproximează. INT este exact. DECIMAL are scară.
    public static String floatForTheCounter() {
        // Începător: numărul de ordine este aproximativ
        // Producție: CREATE TABLE sales (purchase_number FLOAT). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: FLOAT aproximează. INT este exact. DECIMAL are scară. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number FLOAT)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide floatForTheCounter. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
