package _7_Data_Types._1_Numeric._1_Integers;

// https://dev.mysql.com/doc/refman/8.0/en/integer-types.html


/*                                 Integer Types (Exact Value)


   1. TINYINT:

                 * 1 byte, can be signed or unsigned

    2. SMALLINT:

                * 2 bytes, can be signed or unsigned

   3. MEDIUMINT:

                 * 3 bytes, can be signed or unsigned

    4.INTEGER and INT:

                * 4 bytes, can be signed or unsigned

    5. BIGINT:

                 * 8 bytes, can be signed or unsigned


    ○ All are signed by default.

                * If you want to create an unsigned column of any of these data types,  ------------------

                * you need to include the UNSIGNED keyword in the column definition.

        For example:

                * CREATE TABLE example (
                  id TINYINT UNSIGNED,
                  ...
                  );

 */


/*
TOPIC:
    INT ține un întreg exact pe 4 octeți; TINYINT UNSIGNED ține 0..255 pe un octet, nu un număr cu zecimale.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu pui un preț în INT dacă îți trebuie scara. purchase_number încape într-un întreg.
MECHANISM:
    Serverul stochează tipul din definiție. UNSIGNED schimbă intervalul. DECIMAL ține scara; INT taie partea fracționară la conversie.
LOOKALIKES:
    DECIMAL(10,2) ține banii. FLOAT este aproximativ.
BOOK THEORY:
    Manualul 9.7, 13.1.2, deschis în tur: INT ocupă 4 octeți. UNSIGNED se scrie explicit. TINYINT ocupă 1 octet.  PDF-ul Integers.pdf a fost deschis: numere întregi, fără punct zecimal.
BOOK EXAMPLE:
    purchaseNumberIsInt
REPO DEMO:
    src/main/java/_7_Data_Types/_1_Numeric/_1_Integers/Integers.java
BREAK LAB:
    decimalIsNotTheCounter ține CREATE TABLE sales (purchase_number DECIMAL(10,2)). DECIMAL ține scara. INT este exact, fără scară. FLOAT este aproximativ.
TRADE-OFFS:
    DECIMAL când ai zecimale exacte. TINYINT UNSIGNED când intervalul încape în 0..255.
ANTI-PATTERNS:
    DECIMAL(10,2) pentru un număr de ordine care trebuie să fie INT. Ai scară, nu contorul pe 4 octeți.
STAFF QUESTIONS:
    purchaseNumberIsInt și decimalIsNotTheCounter. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Numărul de cutie este INT. Prețul este DECIMAL. Nu pui prețul în INT dacă 10.50 trebuie să rămână 10.50.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE sales (purchase_number INT)?
   Capcană: INT(11) ține unsprezece cifre.
   Cheie: purchaseNumberIsInt: purchase_number este întreg exact pe 4 octeți, cu semn. fără INT, un DECIMAL sau un FLOAT ar fi alt tip de valoare
   Demonstrație: purchaseNumberIsInt
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE sales (purchase_number INT)?
   Capcană: INT păstrează 10.50.
   Cheie: Efectul este: purchase_number este întreg exact pe 4 octeți, cu semn. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: purchaseNumberIsInt
3. Enunț: De ce CREATE TABLE sales (purchase_number INT) nu este vecinul său?
   Capcană: TINYINT și INT ocupă la fel.
   Cheie: DECIMAL(10,2) ține banii. FLOAT este aproximativ.
   Demonstrație: purchaseNumberIsInt
4. Enunț: Cum eșuează CREATE TABLE sales (purchase_number DECIMAL(10,2))?
   Capcană: UNSIGNED este implicit.
   Cheie: decimalIsNotTheCounter: DECIMAL ține scara. INT este exact, fără scară. FLOAT este aproximativ.
   Demonstrație: decimalIsNotTheCounter
5. Enunț: Când nu mai trimiți CREATE TABLE sales (purchase_number INT)?
   Capcană: DECIMAL(10,2) este un INT cu afișare.
   Cheie: DECIMAL când ai zecimale exacte. TINYINT UNSIGNED când intervalul încape în 0..255.
   Demonstrație: purchaseNumberIsInt

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE valoarea este număr întreg -> purchaseNumberIsInt -> CREATE TABLE sales (purchase_number INT) -> nu ține 10.5 exact și nu devine UNSIGNED dacă nu ai scris
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> decimalIsNotTheCounter -> CREATE TABLE sales (purchase_number DECIMAL(10,2)) -> DECIMAL ține scara. INT este exact, fără scară. FLOAT este aproximativ.
    3. Granița
           -> Tipul este în CREATE TABLE. O mască de afișare din client nu schimbă octetul.
    4. Alternativa
           -> DECIMAL(10,2) ține banii. FLOAT este aproximativ.
    5. Anti-patternul
           -> DECIMAL(10,2) pentru un număr de ordine care trebuie să fie INT. Ai scară, nu contorul pe 4 octeți.
    6. Dovada
           -> purchaseNumberIsInt ține propoziția. decimalIsNotTheCounter ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> INT este exact, 4 octeți, cu semn dacă nu scrii UNSIGNED. DECIMAL ține scara. FLOAT aproximează. INT(11) nu este intervalul.
*/

public class Integers {
    /*
    🟢 Începător
        Ce este            purchase_number este întreg exact pe 4 octeți, cu semn
        De ce e nevoie     fără INT, un DECIMAL sau un FLOAT ar fi alt tip de valoare
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este price DECIMAL(10,2), care ține două zecimale
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu price DECIMAL(10,2), care ține două zecimale.
    */
    // Începător: purchaseNumberIsInt păstrează propoziția pentru purchase_number este întreg exact pe 4 octeți, cu semn
    // Producție: public static String purchaseNumberIsInt(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. INT(11) nu mai este lățimea care definește intervalul. Intervalul este al lui INT, nu al parantezei vechi.
    public static String purchaseNumberIsInt() {
        // Începător: purchase_number este întreg exact pe 4 octeți, cu semn
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: INT(11) nu mai este lățimea care definește intervalul. Intervalul este al lui INT, nu al parantezei vechi. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number INT)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide purchaseNumberIsInt. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            coloana ține scară, nu întregul pe 4 octeți pe care îl voiai la număr
        De ce e nevoie     fără textul greșit, orice tip numeric pare INT
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number DECIMAL(10,2))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care coloana ține scară, nu întregul pe 4 octeți pe care îl voiai la număr
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu price DECIMAL(10,2), care ține două zecimale.
    */
    // Începător: decimalIsNotTheCounter păstrează textul greșit pentru coloana ține scară, nu întregul pe 4 octeți pe care îl voiai la număr
    // Producție: public static String decimalIsNotTheCounter(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DECIMAL ține scara. INT este exact, fără scară. FLOAT este aproximativ.
    public static String decimalIsNotTheCounter() {
        // Începător: coloana ține scară, nu întregul pe 4 octeți pe care îl voiai la număr
        // Producție: CREATE TABLE sales (purchase_number DECIMAL(10,2)). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DECIMAL ține scara. INT este exact, fără scară. FLOAT este aproximativ. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number DECIMAL(10,2))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide decimalIsNotTheCounter. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
