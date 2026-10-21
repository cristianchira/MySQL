package _7_Data_Types._3_String_Data_Type;

//String Data Types
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339028#questions

// String Data Types
// https://dev.mysql.com/doc/refman/8.0/en/string-types.html


/*                                   String Data Types


    ○ CHAR

                   * https://dev.mysql.com/doc/refman/8.0/en/char.html

                   * CHAR(255)

                                 - fixed size ( 1 byte)

                                 - 50% faster than varchar



    ○ VARCHAR

                   * https://dev.mysql.com/doc/refman/8.0/en/char.html

                   * VARCHAR(65,535)

                                 - max size (2 byte)

                                  - a lot more responsive to the data value inserted

   ○ ENUM

                    * https://dev.mysql.com/doc/refman/8.0/en/enum.html

                    * ENUM('M','F')

                    * CREATE TABLE shirts (
                       name VARCHAR(40),
                       size ENUM('x-small', 'small', 'medium', 'large', 'x-large')
                        );

   ○ BINARY

                    * https://dev.mysql.com/doc/refman/8.0/en/binary-varbinary.html

   ○ VARBINARY

                    * https://dev.mysql.com/doc/refman/8.0/en/binary-varbinary.html

   ○ BLOB

                    * https://dev.mysql.com/doc/refman/8.0/en/blob.html

                    * TINYBLOB,

                    * BLOB,

                    * MEDIUMBLOB,

                    * LONGBLOB

   ○ TEXT

                * https://dev.mysql.com/doc/refman/8.0/en/blob.html

   ○ SET

                 * https://dev.mysql.com/doc/refman/8.0/en/set.html



 */

/*
TOPIC:
    Șirul se declară după ce ții: CHAR fix, VARCHAR variabil, ENUM o valoare din listă, BLOB octeți.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu pui o mărime închisă în VARCHAR și o poză în CHAR. Definiția spune ce încape.
MECHANISM:
    Serverul aplică tipul din CREATE TABLE. Linkurile din curs către manualul 8.0 rămân; regulile folosite aici sunt din paginile 9.7 deschise pentru CHAR și ENUM.
LOOKALIKES:
    size ENUM(...) refuză valoarea din afara listei în mod strict. size VARCHAR(40) o acceptă.
BOOK THEORY:
    Manualul 9.7, 13.3.2, deschis în tur: CHAR are lungime fixă și spații la stocare; VARCHAR ține lungimea folosită. La citire, CHAR pierde spațiile finale dacă nu este PAD_CHAR_TO_FULL_LENGTH. Manualul 9.7, 13.3.6, deschis în tur: ENUM primește un membru din listă. În mod strict, valoarea din afara listei este eroare, nu un VARCHAR care o acceptă. 
BOOK EXAMPLE:
    enumForTheSize
REPO DEMO:
    src/main/java/_7_Data_Types/_3_String_Data_Type/String_Data_Type.java
BREAK LAB:
    varcharForTheClosedSize ține CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40)). VARCHAR nu limitează lista. ENUM o limitează. BLOB este octet, nu acest size.
TRADE-OFFS:
    VARCHAR când lista de mărimi se schimbă des. BLOB când valoarea este fișier.
ANTI-PATTERNS:
    VARCHAR pentru o listă închisă. Valoarea străină intră.
STAFF QUESTIONS:
    enumForTheSize și varcharForTheClosedSize. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Numele lotului este VARCHAR. Starea închisă este ENUM. Fișierul de analiză este BLOB.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))?
   Capcană: CHAR este mereu cu 50% mai rapid decât VARCHAR.
   Cheie: enumForTheSize: name este variabil, size este din listă. fără ENUM, size VARCHAR acceptă o mărime care nu există
   Demonstrație: enumForTheSize
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))?
   Capcană: VARCHAR refuză mărimea din afara unei liste din comentariu.
   Cheie: Efectul este: name este variabil, size este din listă. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: enumForTheSize
3. Enunț: De ce CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large')) nu este vecinul său?
   Capcană: ENUM ține mai multe valori, ca SET.
   Cheie: size ENUM(...) refuză valoarea din afara listei în mod strict. size VARCHAR(40) o acceptă.
   Demonstrație: enumForTheSize
4. Enunț: Cum eșuează CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40))?
   Capcană: BLOB și VARCHAR(40) sunt același loc pentru o poză.
   Cheie: varcharForTheClosedSize: VARCHAR nu limitează lista. ENUM o limitează. BLOB este octet, nu acest size.
   Demonstrație: varcharForTheClosedSize
5. Enunț: Când nu mai trimiți CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))?
   Capcană: Linkul către manualul 8.0 schimbă tipul pe 9.7.
   Cheie: VARCHAR când lista de mărimi se schimbă des. BLOB când valoarea este fișier.
   Demonstrație: enumForTheSize

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE mărimea este dintr-o listă și numele este variabil -> enumForTheSize -> CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large')) -> nu face CHAR mai rapid și nu ține o imagine în VARCHAR(40)
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> varcharForTheClosedSize -> CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40)) -> VARCHAR nu limitează lista. ENUM o limitează. BLOB este octet, nu acest size.
    3. Granița
           -> Tipul este propoziția. Linkul de manual din comentariu nu creează coloana.
    4. Alternativa
           -> size ENUM(...) refuză valoarea din afara listei în mod strict. size VARCHAR(40) o acceptă.
    5. Anti-patternul
           -> VARCHAR pentru o listă închisă. Valoarea străină intră.
    6. Dovada
           -> enumForTheSize ține propoziția. varcharForTheClosedSize ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CHAR fix, VARCHAR variabil, ENUM o valoare din listă, BLOB octeți. Viteza de 50% nu este regula din manualul deschis. VARCHAR nu refuză valoarea din afara listei.
*/

public class String_Data_Type {
    /*
    🟢 Începător
        Ce este            name este variabil, size este din listă
        De ce e nevoie     fără ENUM, size VARCHAR acceptă o mărime care nu există
    🟡 Producție
        Cum se scrie       CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este size VARCHAR(40)
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu size VARCHAR(40).
    */
    // Începător: enumForTheSize păstrează propoziția pentru name este variabil, size este din listă
    // Producție: public static String enumForTheSize(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CHAR nu este cu 50% mai rapid doar pentru că cursul o spune. Manualul deschis nu dă această măsură.
    public static String enumForTheSize() {
        // Începător: name este variabil, size este din listă
        // Producție: CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large')). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: CHAR nu este cu 50% mai rapid doar pentru că cursul o spune. Manualul deschis nu dă această măsură. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide enumForTheSize. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            'huge' poate intra în size
        De ce e nevoie     fără textul greșit, toate șirurile par VARCHAR
    🟡 Producție
        Cum se scrie       CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care 'huge' poate intra în size
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu size VARCHAR(40).
    */
    // Începător: varcharForTheClosedSize păstrează textul greșit pentru 'huge' poate intra în size
    // Producție: public static String varcharForTheClosedSize(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. VARCHAR nu limitează lista. ENUM o limitează. BLOB este octet, nu acest size.
    public static String varcharForTheClosedSize() {
        // Începător: 'huge' poate intra în size
        // Producție: CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40)). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: VARCHAR nu limitează lista. ENUM o limitează. BLOB este octet, nu acest size. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide varcharForTheClosedSize. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
