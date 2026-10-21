package _7_Data_Types._0_Data_Types_Default_Values;

// Data Type Default Values
//https://dev.mysql.com/doc/refman/8.0/en/data-type-defaults.html


/*                        Data Type Default Values

    ○ Data type specifications can have

                1. explicit

                2. or implicit

        default values.

    ○ A DEFAULT value clause in a data type specification explicitly indicates a default value for a column.
          Examples:

                * CREATE TABLE t1 (
                   i     INT DEFAULT -1,
                   c     VARCHAR(10) DEFAULT '',
                   price DOUBLE(16,2) DEFAULT 0.00
                  );


                                        Default values


    1. INT - 0

    2.FLOAT - 0

    3. DECIMAL - 0

    4.DOUBLE - 0

    5. VARCHAR - ''

    6.CHAR - ''

    7. TEXT - NULL

    8. DATE - NULL

    9. TIME - NULL

    10. DATETIME - NULL

    11. TIMESTAMP - NULL (or current timestamp, depending on the database system)

    12. BLOB - NULL

    13. CLOB - NULL

    14. LONGBLOB - NULL

    15. LONGTEXT - NULL

    16.ENUM - First value listed in the type definition

    17 SET - Empty set (no values)

    18.JSON - NULL

*/

/*
TOPIC:
    DEFAULT -1 pe o coloană INT umple valoarea când INSERT nu o dă; NOT NULL fără default, în mod strict, refuză lipsa.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu rămână coloana fără valoarea pe care ai ales-o. Rândul apare cu -1 dacă nu ai dat i.
MECHANISM:
    Serverul aplică DEFAULT la INSERT când coloana lipsește din listă. Strict mode cere valoare pentru coloana fără default.
LOOKALIKES:
    NOT NULL refuză NULL. DEFAULT umple lipsa. Nu sunt aceeași clauză.
BOOK THEORY:
    Comentariul de curs are CREATE TABLE t1 (i INT DEFAULT -1). Pagina INSERT 9.7 a fost deschisă: coloana nenomită ia defaultul dacă modul permite. Lista de defaulturi implicite din curs nu este copiată aici ca regulă.
BOOK EXAMPLE:
    defaultFillsTheMissingInt
REPO DEMO:
    src/main/java/_7_Data_Types/_0_Data_Types_Default_Values/Data_Types_Default_Values.java
BREAK LAB:
    notNullDoesNotFillMinusOne ține CREATE TABLE t1 (i INT NOT NULL). NOT NULL refuză lipsa. DEFAULT -1 o umple când INSERT nu dă coloana.
TRADE-OFFS:
    NOT NULL fără default când lipsa trebuie să fie eroare, nu -1.
ANTI-PATTERNS:
    NOT NULL ca să capeți -1. Nu capeți -1.
STAFF QUESTIONS:
    defaultFillsTheMissingInt și notNullDoesNotFillMinusOne. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Cantitatea necompletată devine 0: qty INT DEFAULT 0. NOT NULL fără default oprește inserarea, nu pune 0.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE t1 (i INT DEFAULT -1)?
   Capcană: NOT NULL pune -1.
   Cheie: defaultFillsTheMissingInt: un INSERT care nu dă i lasă -1 în coloană, dacă propoziția de inserare o omite. fără DEFAULT, în mod strict, INSERT care omite i eșuează dacă nu există alt default
   Demonstrație: defaultFillsTheMissingInt
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE t1 (i INT DEFAULT -1)?
   Capcană: DEFAULT schimbă și valoarea trimisă explicit.
   Cheie: Efectul este: un INSERT care nu dă i lasă -1 în coloană, dacă propoziția de inserare o omite. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: defaultFillsTheMissingInt
3. Enunț: De ce CREATE TABLE t1 (i INT DEFAULT -1) nu este vecinul său?
   Capcană: În mod strict, coloana fără default se umple cu 0 mereu.
   Cheie: NOT NULL refuză NULL. DEFAULT umple lipsa. Nu sunt aceeași clauză.
   Demonstrație: defaultFillsTheMissingInt
4. Enunț: Cum eșuează CREATE TABLE t1 (i INT NOT NULL)?
   Capcană: DEFAULT este un UPDATE.
   Cheie: notNullDoesNotFillMinusOne: NOT NULL refuză lipsa. DEFAULT -1 o umple când INSERT nu dă coloana.
   Demonstrație: notNullDoesNotFillMinusOne
5. Enunț: Când nu mai trimiți CREATE TABLE t1 (i INT DEFAULT -1)?
   Capcană: INSERT trebuie să trimită -1 chiar dacă ai DEFAULT.
   Cheie: NOT NULL fără default când lipsa trebuie să fie eroare, nu -1.
   Demonstrație: defaultFillsTheMissingInt

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE lipsa din INSERT trebuie să devină -1 -> defaultFillsTheMissingInt -> CREATE TABLE t1 (i INT DEFAULT -1) -> nu schimbă un INSERT care trimite explicit altă valoare
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> notNullDoesNotFillMinusOne -> CREATE TABLE t1 (i INT NOT NULL) -> NOT NULL refuză lipsa. DEFAULT -1 o umple când INSERT nu dă coloana.
    3. Granița
           -> DEFAULT este clauza din definiție. Un placeholder din formular nu este defaultul serverului.
    4. Alternativa
           -> NOT NULL refuză NULL. DEFAULT umple lipsa. Nu sunt aceeași clauză.
    5. Anti-patternul
           -> NOT NULL ca să capeți -1. Nu capeți -1.
    6. Dovada
           -> defaultFillsTheMissingInt ține propoziția. notNullDoesNotFillMinusOne ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DEFAULT umple coloana omisă la INSERT. NOT NULL refuză NULL și nu alege el -1. În mod strict, coloana fără default nu poate fi omisă.
*/

public class Data_Types_Default_Values {
    /*
    🟢 Începător
        Ce este            un INSERT care nu dă i lasă -1 în coloană, dacă propoziția de inserare o omite
        De ce e nevoie     fără DEFAULT, în mod strict, INSERT care omite i eșuează dacă nu există alt default
    🟡 Producție
        Cum se scrie       CREATE TABLE t1 (i INT DEFAULT -1)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este i INT NOT NULL, care refuză NULL și nu pune -1
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu i INT NOT NULL, care refuză NULL și nu pune -1.
    */
    // Începător: defaultFillsTheMissingInt păstrează propoziția pentru un INSERT care nu dă i lasă -1 în coloană, dacă propoziția de inserare o omite
    // Producție: public static String defaultFillsTheMissingInt(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DEFAULT nu înlocuiește o valoare pe care ai scris-o explicit, inclusiv NULL dacă coloana îl permite.
    public static String defaultFillsTheMissingInt() {
        // Începător: un INSERT care nu dă i lasă -1 în coloană, dacă propoziția de inserare o omite
        // Producție: CREATE TABLE t1 (i INT DEFAULT -1). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: DEFAULT nu înlocuiește o valoare pe care ai scris-o explicit, inclusiv NULL dacă coloana îl permite. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE t1 (i INT DEFAULT -1)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide defaultFillsTheMissingInt. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            coloana refuză NULL și nu primește -1 doar pentru că ai oprit NULL
        De ce e nevoie     fără textul greșit, NOT NULL pare că umple un număr
    🟡 Producție
        Cum se scrie       CREATE TABLE t1 (i INT NOT NULL)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care coloana refuză NULL și nu primește -1 doar pentru că ai oprit NULL
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu i INT NOT NULL, care refuză NULL și nu pune -1.
    */
    // Începător: notNullDoesNotFillMinusOne păstrează textul greșit pentru coloana refuză NULL și nu primește -1 doar pentru că ai oprit NULL
    // Producție: public static String notNullDoesNotFillMinusOne(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. NOT NULL refuză lipsa. DEFAULT -1 o umple când INSERT nu dă coloana.
    public static String notNullDoesNotFillMinusOne() {
        // Începător: coloana refuză NULL și nu primește -1 doar pentru că ai oprit NULL
        // Producție: CREATE TABLE t1 (i INT NOT NULL). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: NOT NULL refuză lipsa. DEFAULT -1 o umple când INSERT nu dă coloana. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE t1 (i INT NOT NULL)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide notNullDoesNotFillMinusOne. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
