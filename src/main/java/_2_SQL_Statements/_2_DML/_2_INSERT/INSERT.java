package _2_SQL_Statements._2_DML._2_INSERT;

// https://dev.mysql.com/doc/refman/8.0/en/insert.html


/*                                           Insert Statement


    ○ is used to insert data in TABLES

    ○ INSERT

                  * inserts new rows into an existing table.


                                          Insert statements types


    1.INSERT ... VALUES:

                * This is the most basic form of the INSERT statement.

                * It inserts a single row of data into a table, specifying the values for each column.

                            - INSERT INTO table_name (column1, column2, column3)
                               VALUES (value1, value2, value3);

    2. INSERT ... VALUES ROW():

                 * This is a variation of the INSERT statement,

                 * which allows you to insert multiple rows of data into a table in a single query.

                 * The ROW() function is used to specify each row's values.

                              - INSERT INTO table_name (column1, column2, column3)
                                VALUES ROW(value1, value2, value3), ROW(value4, value5, value6);

         Note that not all database systems support this syntax.

                  * In some databases, you can simply use multiple VALUES without the ROW() function:

                              - INSERT INTO table_name (column1, column2, column3)
                                VALUES (value1, value2, value3), (value4, value5, value6);
    3.INSERT ... SET:

                 * This variation of the INSERT statement allows you to insert a single row of data

                 * into a table by setting the values for each column in a more verbose syntax.
                       It's often used in MySQL.

                              - INSERT INTO table_name SET column1 = value1, column2 = value2, column3 = value3;

    4.INSERT ... SELECT:

                  * This form of the INSERT statement allows you to insert data into a table based on

                  * the results of a SELECT query.

          This is useful when you want to

                  * copy data from one table to another

                  * or insert data based on some condition.

                              - INSERT INTO table_name (column1, column2, column3)
                                SELECT column1, column2, column3 FROM source_table WHERE some_condition;

           This query

                  * will insert the rows returned by the SELECT statement

                  * into the target table (table_name).

                   * https://dev.mysql.com/doc/refman/8.0/en/insert-select.html


    5.  INSERT ... TABLE

                  * in MySQL 8.0.19 and later to insert rows from a single table.

    6. INSERT with an ON DUPLICATE KEY UPDATE

                 * https://dev.mysql.com/doc/refman/8.0/en/insert-on-duplicate.html

                 * This MySQL-specific extension allows you to update existing rows
                       if the insertion of a new row would cause a duplicate value in
                       a UNIQUE index or PRIMARY KEY.

         In MySQL 8.0.19 and later,

                 * you can use a row alias with optional column aliases to refer to
                       the row being inserted with ON DUPLICATE KEY UPDATE.

                             - INSERT INTO table_name (column1, column2, ...)
                               VALUES (value1, value2, ...)
                               ON DUPLICATE KEY UPDATE column1 = new_value1, column2 = new_value2, ...;


    7. DELAYED   -- deprecated

                 *  keyword is accepted but ignored by the server.

                 *  https://dev.mysql.com/doc/refman/8.0/en/insert-delayed.html


                                            Complete insert statements


                INSERT [LOW_PRIORITY | DELAYED | HIGH_PRIORITY] [IGNORE]

    [INTO] tbl_name

    [PARTITION (partition_name [, partition_name] ...)]
    [(col_name [, col_name] ...)]

    { {VALUES | VALUE} (value_list) [, (value_list)] ... }

    [AS row_alias[(col_alias [, col_alias] ...)]]

    [ON DUPLICATE KEY UPDATE assignment_list]

INSERT [LOW_PRIORITY | DELAYED | HIGH_PRIORITY] [IGNORE]

    [INTO] tbl_name

    [PARTITION (partition_name [, partition_name] ...)]
    SET assignment_list
    [AS row_alias[(col_alias [, col_alias] ...)]]
    [ON DUPLICATE KEY UPDATE assignment_list]

INSERT [LOW_PRIORITY | HIGH_PRIORITY] [IGNORE]
    [INTO] tbl_name

    [PARTITION (partition_name [, partition_name] ...)]
    [(col_name [, col_name] ...)]
    { SELECT ...
      | TABLE table_name
      | VALUES row_constructor_list
    }
    [ON DUPLICATE KEY UPDATE assignment_list]

value:
    {expr | DEFAULT}

value_list:
    value [, value] ...

row_constructor_list:
    ROW(value_list)[, ROW(value_list)][, ...]

assignment:
    col_name =
          value
        | [row_alias.]col_name
        | [tbl_name.]col_name
        | [row_alias.]col_alias

assignment_list:
    assignment [, assignment] ...




 */

/*
TOPIC:
    INSERT INTO sales (purchase_number) VALUES (1) adaugă un rând într-o tabelă care există; nu creează sales.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să apară cumpărarea. După propoziție, sales are un rând în plus.
MECHANISM:
    Serverul execută INSERT la sfârșitul propoziției. În 9.7, DELAYED este acceptat și ignorat. ON DUPLICATE KEY UPDATE cere și privilegiul UPDATE când chiar actualizează.
LOOKALIKES:
    CREATE TABLE sales (purchase_number INT) face obiectul și zero rânduri. UPDATE schimbă un rând existent.
BOOK THEORY:
    Manualul 9.7, 15.2.7 INSERT, deschis în tur: INSERT adaugă rânduri într-o tabelă care există. VALUES dă valorile, INSERT SELECT le ia din altă tabelă. DELAYED este acceptat și ignorat. 
BOOK EXAMPLE:
    insertPurchaseRow
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_2_INSERT/INSERT.java
BREAK LAB:
    delayedIsIgnored ține INSERT DELAYED INTO sales (purchase_number) VALUES (1). Manualul 9.7: DELAYED este acceptat și ignorat. Nu este o scriere amânată.
TRADE-OFFS:
    CREATE TABLE când sales lipsește. UPDATE când rândul există.
ANTI-PATTERNS:
    INSERT DELAYED ca să amâni scrierea. Pe 9.7 cuvântul este ignorat.
STAFF QUESTIONS:
    insertPurchaseRow și delayedIsIgnored. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un flacon se scrie cu INSERT când fișa lotului există. CREATE TABLE face fișa goală.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă INSERT INTO sales (purchase_number) VALUES (1)?
   Capcană: INSERT DELAYED ține rândul într-o coadă pe 9.7.
   Cheie: insertPurchaseRow: un rând nou stă în sales. fără INSERT, CREATE TABLE lasă sales goală
   Demonstrație: insertPurchaseRow
2. Enunț: Ce rămâne în tabelă sau în sesiune după INSERT INTO sales (purchase_number) VALUES (1)?
   Capcană: INSERT creează sales dacă lipsește.
   Cheie: Efectul este: un rând nou stă în sales. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: insertPurchaseRow
3. Enunț: De ce INSERT INTO sales (purchase_number) VALUES (1) nu este vecinul său?
   Capcană: UPDATE adaugă rândul când purchase_number nu există.
   Cheie: CREATE TABLE sales (purchase_number INT) face obiectul și zero rânduri. UPDATE schimbă un rând existent.
   Demonstrație: insertPurchaseRow
4. Enunț: Cum eșuează INSERT DELAYED INTO sales (purchase_number) VALUES (1)?
   Capcană: VALUES și SELECT sunt același lucru pentru server.
   Cheie: delayedIsIgnored: Manualul 9.7: DELAYED este acceptat și ignorat. Nu este o scriere amânată.
   Demonstrație: delayedIsIgnored
5. Enunț: Când nu mai trimiți INSERT INTO sales (purchase_number) VALUES (1)?
   Capcană: Ca să adaugi un rând, faci CREATE TABLE încă o dată.
   Cheie: CREATE TABLE când sales lipsește. UPDATE când rândul există.
   Demonstrație: insertPurchaseRow

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE tabela există și vrei un rând nou -> insertPurchaseRow -> INSERT INTO sales (purchase_number) VALUES (1) -> nu creează tabela și nu amână scrierea prin DELAYED
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> delayedIsIgnored -> INSERT DELAYED INTO sales (purchase_number) VALUES (1) -> Manualul 9.7: DELAYED este acceptat și ignorat. Nu este o scriere amânată.
    3. Granița
           -> INSERT este propoziția. DELAYED nu mai este mecanismul de așteptare.
    4. Alternativa
           -> CREATE TABLE sales (purchase_number INT) face obiectul și zero rânduri. UPDATE schimbă un rând existent.
    5. Anti-patternul
           -> INSERT DELAYED ca să amâni scrierea. Pe 9.7 cuvântul este ignorat.
    6. Dovada
           -> insertPurchaseRow ține propoziția. delayedIsIgnored ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> INSERT adaugă rânduri într-o tabelă existentă. DELAYED este ignorat pe 9.7. UPDATE nu creează rândul lipsă.
*/

public class INSERT {
    /*
    🟢 Începător
        Ce este            un rând nou stă în sales
        De ce e nevoie     fără INSERT, CREATE TABLE lasă sales goală
    🟡 Producție
        Cum se scrie       INSERT INTO sales (purchase_number) VALUES (1)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este UPDATE sales SET purchase_number = 1 WHERE purchase_number = 1, care nu adaugă un rând dacă acela lipsește
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE sales SET purchase_number = 1 WHERE purchase_number = 1, care nu adaugă un rând dacă acela lipsește.
    */
    // Începător: insertPurchaseRow păstrează propoziția pentru un rând nou stă în sales
    // Producție: public static String insertPurchaseRow(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. INSERT DELAYED nu întârzie scrierea pe 9.7: cuvântul este ignorat.
    public static String insertPurchaseRow() {
        // Începător: un rând nou stă în sales
        // Producție: INSERT INTO sales (purchase_number) VALUES (1). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: INSERT DELAYED nu întârzie scrierea pe 9.7: cuvântul este ignorat. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT INTO sales (purchase_number) VALUES (1)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide insertPurchaseRow. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul ignoră DELAYED; nu capeți o inserare amânată, iar forma fără DELAYED este propoziția
        De ce e nevoie     fără textul greșit, DELAYED pare încă un mod de scriere
    🟡 Producție
        Cum se scrie       INSERT DELAYED INTO sales (purchase_number) VALUES (1)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul ignoră DELAYED; nu capeți o inserare amânată, iar forma fără DELAYED este propoziția
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE sales SET purchase_number = 1 WHERE purchase_number = 1, care nu adaugă un rând dacă acela lipsește.
    */
    // Începător: delayedIsIgnored păstrează textul greșit pentru serverul ignoră DELAYED; nu capeți o inserare amânată, iar forma fără DELAYED este propoziția
    // Producție: public static String delayedIsIgnored(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Manualul 9.7: DELAYED este acceptat și ignorat. Nu este o scriere amânată.
    public static String delayedIsIgnored() {
        // Începător: serverul ignoră DELAYED; nu capeți o inserare amânată, iar forma fără DELAYED este propoziția
        // Producție: INSERT DELAYED INTO sales (purchase_number) VALUES (1). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Manualul 9.7: DELAYED este acceptat și ignorat. Nu este o scriere amânată. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT DELAYED INTO sales (purchase_number) VALUES (1)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide delayedIsIgnored. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
