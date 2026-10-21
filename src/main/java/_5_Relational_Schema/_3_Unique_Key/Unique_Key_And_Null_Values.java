package _5_Relational_Schema._3_Unique_Key;

// Martin G. Unique Key
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338068#questions


// PRIMARY KEY and UNIQUE Index Constraints
// https://dev.mysql.com/doc/refman/8.0/en/constraint-primary-key.html


/*                                           Unique Key


    1. Create unique key on existing table

                *  ALTER TABLE table_name
                   ADD UNIQUE KEY index_name (column_name);

    2. When create new table:

                * with declared name   -- best practice

                            - CREATE TABLE users (
                              id INT PRIMARY KEY,
                              username VARCHAR(255),
                              email_address VARCHAR(255),
                              UNIQUE KEY unique_email_address (email_address)
                              );

                 * without declared name  -- MySQL will automatically generate a unique index name

                            - CREATE TABLE users (
                              id INT PRIMARY KEY,
                              username VARCHAR(255),
                              email_address VARCHAR(255),
                              UNIQUE (email_address)
                            );



    ○ used whenever you would like to specify that you

                  * don't want to see duplicate data in a given field

    ○ can contain null values    --------------------------------------------------------------------

    ○ can be several unique key in a table(relational schema)

    ○ one unique key can be applied to

                  * one column

                  * multiple columns


                                              Unique constraint


    ○  CONSTRAINT unique_email_address UNIQUE (`email_address`)

    ○ Unique keys are implemented on SQL

                  * through a constraint

                                - UNIQUE constraint

    ○ If you attempt to insert an already existing, duplicate value in the unique column,

                   * SQL will display an error




*/


/*
TOPIC:
    UNIQUE (email_address) refuză al doilea același email; în InnoDB poate avea mai multe NULL, iar PRIMARY KEY nu acceptă NULL.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu existe două conturi cu același email. NULL nu este un email duplicat.
MECHANISM:
    Serverul respinge valoarea care există deja în indexul unic. PRIMARY KEY este una și nu acceptă NULL. UNIQUE poate apărea de mai multe ori pe tabelă.
LOOKALIKES:
    PRIMARY KEY (id) identifică rândul și nu ține locul unui email unic dacă nu este pe email.
BOOK THEORY:
    PDF-ul Unique_Key_And_Null_Values.pdf a fost deschis. Comentariul de curs spune că UNIQUE poate avea NULL și poate fi mai multe pe tabelă. PRIMARY KEY nu acceptă NULL.
BOOK EXAMPLE:
    uniqueEmailRejectsTheCopy
REPO DEMO:
    src/main/java/_5_Relational_Schema/_3_Unique_Key/Unique_Key_And_Null_Values.java
BREAK LAB:
    primaryKeyDoesNotWatchEmail ține ALTER TABLE users ADD PRIMARY KEY (id). PRIMARY KEY este pe id. UNIQUE pe email este constrângerea care refuză copia.
TRADE-OFFS:
    PRIMARY KEY când coloana identifică rândul și nu acceptă NULL.
ANTI-PATTERNS:
    PRIMARY KEY (id) ca să aperi emailul. Copiile de email intră.
STAFF QUESTIONS:
    uniqueEmailRejectsTheCopy și primaryKeyDoesNotWatchEmail. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Două ecusoane nu pot avea același cod: UNIQUE (badge). NULL pe badge nu ține locul unui cod unic.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)?
   Capcană: UNIQUE interzice orice NULL.
   Cheie: uniqueEmailRejectsTheCopy: al doilea rând cu același email_address este refuzat. fără UNIQUE, două conturi pot avea același email
   Demonstrație: uniqueEmailRejectsTheCopy
2. Enunț: Ce rămâne în tabelă sau în sesiune după ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)?
   Capcană: PRIMARY KEY (id) face emailul unic.
   Cheie: Efectul este: al doilea rând cu același email_address este refuzat. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: uniqueEmailRejectsTheCopy
3. Enunț: De ce ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address) nu este vecinul său?
   Capcană: Poate exista o singură constrângere UNIQUE pe tabelă.
   Cheie: PRIMARY KEY (id) identifică rândul și nu ține locul unui email unic dacă nu este pe email.
   Demonstrație: uniqueEmailRejectsTheCopy
4. Enunț: Cum eșuează ALTER TABLE users ADD PRIMARY KEY (id)?
   Capcană: NULL și șirul gol sunt același duplicat.
   Cheie: primaryKeyDoesNotWatchEmail: PRIMARY KEY este pe id. UNIQUE pe email este constrângerea care refuză copia.
   Demonstrație: primaryKeyDoesNotWatchEmail
5. Enunț: Când nu mai trimiți ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)?
   Capcană: UNIQUE șterge rândul vechi când vine copia.
   Cheie: PRIMARY KEY când coloana identifică rândul și nu acceptă NULL.
   Demonstrație: uniqueEmailRejectsTheCopy

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE emailul nu are voie să se repete, iar NULL poate apărea de mai multe ori -> uniqueEmailRejectsTheCopy -> ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address) -> nu interzice NULL și nu înlocuiește cheia primară
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> primaryKeyDoesNotWatchEmail -> ALTER TABLE users ADD PRIMARY KEY (id) -> PRIMARY KEY este pe id. UNIQUE pe email este constrângerea care refuză copia.
    3. Granița
           -> UNIQUE este constrângerea. O validare doar în formular lasă copia să intre pe alt client.
    4. Alternativa
           -> PRIMARY KEY (id) identifică rândul și nu ține locul unui email unic dacă nu este pe email.
    5. Anti-patternul
           -> PRIMARY KEY (id) ca să aperi emailul. Copiile de email intră.
    6. Dovada
           -> uniqueEmailRejectsTheCopy ține propoziția. primaryKeyDoesNotWatchEmail ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> UNIQUE refuză duplicatul și, în InnoDB, acceptă mai multe NULL. PRIMARY KEY este una și nu acceptă NULL. Cheia pe id nu păzește emailul.
*/

public class Unique_Key_And_Null_Values {
    /*
    🟢 Începător
        Ce este            al doilea rând cu același email_address este refuzat
        De ce e nevoie     fără UNIQUE, două conturi pot avea același email
    🟡 Producție
        Cum se scrie       ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este PRIMARY KEY (id), care nu verifică email_address
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu PRIMARY KEY (id), care nu verifică email_address.
    */
    // Începător: uniqueEmailRejectsTheCopy păstrează propoziția pentru al doilea rând cu același email_address este refuzat
    // Producție: public static String uniqueEmailRejectsTheCopy(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Mai multe NULL sunt permise pe UNIQUE în InnoDB. Nu folosi NULL ca să marchezi un email lipsă pe care vrei să fie unic.
    public static String uniqueEmailRejectsTheCopy() {
        // Începător: al doilea rând cu același email_address este refuzat
        // Producție: ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Mai multe NULL sunt permise pe UNIQUE în InnoDB. Nu folosi NULL ca să marchezi un email lipsă pe care vrei să fie unic. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide uniqueEmailRejectsTheCopy. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            id devine cheie și două emailuri identice tot pot intra
        De ce e nevoie     fără textul greșit, cheia primară pare că face toate coloanele unice
    🟡 Producție
        Cum se scrie       ALTER TABLE users ADD PRIMARY KEY (id)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care id devine cheie și două emailuri identice tot pot intra
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu PRIMARY KEY (id), care nu verifică email_address.
    */
    // Începător: primaryKeyDoesNotWatchEmail păstrează textul greșit pentru id devine cheie și două emailuri identice tot pot intra
    // Producție: public static String primaryKeyDoesNotWatchEmail(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. PRIMARY KEY este pe id. UNIQUE pe email este constrângerea care refuză copia.
    public static String primaryKeyDoesNotWatchEmail() {
        // Începător: id devine cheie și două emailuri identice tot pot intra
        // Producție: ALTER TABLE users ADD PRIMARY KEY (id). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: PRIMARY KEY este pe id. UNIQUE pe email este constrângerea care refuză copia. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE users ADD PRIMARY KEY (id)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide primaryKeyDoesNotWatchEmail. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
