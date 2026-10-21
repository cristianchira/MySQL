package _8_Constraints._2_Foreign_Key;

// https://dev.mysql.com/doc/refman/8.0/en/create-table-foreign-keys.html


/*                                FOREIGN KEY Constraints


    ○ which permit cross-referencing related data across tables.(Points to a column of another table so links the 2 table)

    ○ A foreign key relationship involves a

                   * parent table

                                 - that holds the initial column values,

                   * and a child table

                                 - with column values that reference the parent column values.

                    * a foreign key constraint is defined on the child table.

                    * a foreign key in SQL is defined through a foreign key constraint


    ○ A type of pointer relationship, between rows in separate InnoDB tables.

                    * The foreign key relationship is defined on one column in both

                                 - the parent table

                                 - and the child table.

    ○ In addition to enabling fast lookup of related information,

                   * foreign keys help to enforce referential integrity,

                   * by preventing any of these pointers from becoming invalid as data is

                                 - inserted,

                                 - updated,

                                 - and deleted.

                         This enforcement mechanism is a type of constraint.

    ○ A row that points to another table cannot be inserted

                   * if the associated foreign key value does not exist in the other table.

           If a row

                   * is deleted

                   * or its foreign key value changed,

                   * and rows in another table point to that foreign key value,

                   * the foreign key can be set up to prevent the deletion,

                   * cause the corresponding column values in the other table to

                                 - become null,

                                 - or automatically delete the corresponding rows in the other table.


[CONSTRAINT [symbol]] FOREIGN KEY

    which help keep the related data consistent.(maintains the referential integrity within the database)

    [index_name] (col_name, ...)
    REFERENCES tbl_name (col_name,...)
    [ON DELETE reference_option]
    [ON UPDATE reference_option]

reference_option:
    RESTRICT | CASCADE | SET NULL | NO ACTION | SET DEFAULT

    child table(where is defined FK)   = referencing table

    parent table = referenced table




 */

/*
TOPIC:
    FOREIGN KEY de pe sales refuză customer_id care nu există în customer; coloana singură nu verifică.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu rămână o vânzare fără client. INSERT-ul orfan eșuează.
MECHANISM:
    Serverul verifică la scrierea copilului. Pe 9.7, implicit verificarea este în stratul SQL și INSERT-ul respins nu consumă AUTO_INCREMENT.
LOOKALIKES:
    INDEX (customer_id) nu refuză orfanul. PRIMARY KEY pe sales nu se uită în customer.
BOOK THEORY:
    Manualul 9.7, 15.1.25.5, deschis în tur: FOREIGN KEY refuză copilul fără părinte. 
BOOK EXAMPLE:
    foreignKeyOnSales
REPO DEMO:
    src/main/java/_8_Constraints/_2_Foreign_Key/Foreign_Key.java
BREAK LAB:
    indexLetsTheOrphanIn ține ALTER TABLE sales ADD INDEX (customer_id). Indexul nu verifică părintele. FOREIGN KEY îl verifică.
TRADE-OFFS:
    Doar indexul când cauți customer_id și orfanul este permis.
ANTI-PATTERNS:
    ADD INDEX ca integritate referențială. Orfanul intră.
STAFF QUESTIONS:
    foreignKeyOnSales și indexLetsTheOrphanIn. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O linie de comandă nu poate avea un produs inexistent. FOREIGN KEY o spune. Indexul pe product_id nu o spune.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ALTER TABLE sales ADD CONSTRAINT sales_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)?
   Capcană: Indexul pe customer_id refuză orfanul.
   Cheie: foreignKeyOnSales: un customer_id fără rând în customer este refuzat. fără constrângere, orfanul intră
   Demonstrație: foreignKeyOnSales
2. Enunț: Ce rămâne în tabelă sau în sesiune după ALTER TABLE sales ADD CONSTRAINT sales_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)?
   Capcană: ON DELETE CASCADE este implicit.
   Cheie: Efectul este: un customer_id fără rând în customer este refuzat. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: foreignKeyOnSales
3. Enunț: De ce ALTER TABLE sales ADD CONSTRAINT sales_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id) nu este vecinul său?
   Capcană: FOREIGN KEY stă pe customer, nu pe sales.
   Cheie: INDEX (customer_id) nu refuză orfanul. PRIMARY KEY pe sales nu se uită în customer.
   Demonstrație: foreignKeyOnSales
4. Enunț: Cum eșuează ALTER TABLE sales ADD INDEX (customer_id)?
   Capcană: INSERT-ul respins consumă mereu AUTO_INCREMENT.
   Cheie: indexLetsTheOrphanIn: Indexul nu verifică părintele. FOREIGN KEY îl verifică.
   Demonstrație: indexLetsTheOrphanIn
5. Enunț: Când nu mai trimiți ALTER TABLE sales ADD CONSTRAINT sales_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)?
   Capcană: PRIMARY KEY pe sales verifică clientul.
   Cheie: Doar indexul când cauți customer_id și orfanul este permis.
   Demonstrație: foreignKeyOnSales

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vânzarea nu are voie fără client -> foreignKeyOnSales -> ALTER TABLE sales ADD CONSTRAINT sales_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id) -> nu șterge vânzările la ștergerea clientului dacă nu ai scris CASCADE
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> indexLetsTheOrphanIn -> ALTER TABLE sales ADD INDEX (customer_id) -> Indexul nu verifică părintele. FOREIGN KEY îl verifică.
    3. Granița
           -> Constrângerea este propoziția. Săgeata din diagramă nu este ALTER TABLE.
    4. Alternativa
           -> INDEX (customer_id) nu refuză orfanul. PRIMARY KEY pe sales nu se uită în customer.
    5. Anti-patternul
           -> ADD INDEX ca integritate referențială. Orfanul intră.
    6. Dovada
           -> foreignKeyOnSales ține propoziția. indexLetsTheOrphanIn ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> FOREIGN KEY refuză copilul fără părinte. Indexul nu. CASCADE doar dacă este scris. Pe 9.7, INSERT-ul respins de stratul SQL nu consumă AUTO_INCREMENT.
*/

public class Foreign_Key {
    /*
    🟢 Începător
        Ce este            un customer_id fără rând în customer este refuzat
        De ce e nevoie     fără constrângere, orfanul intră
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD CONSTRAINT sales_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este ALTER TABLE sales ADD INDEX (customer_id)
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE sales ADD INDEX (customer_id).
    */
    // Începător: foreignKeyOnSales păstrează propoziția pentru un customer_id fără rând în customer este refuzat
    // Producție: public static String foreignKeyOnSales(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Fără ON DELETE CASCADE, ștergerea clientului care are vânzări este refuzată, nu cascada.
    public static String foreignKeyOnSales() {
        // Începător: un customer_id fără rând în customer este refuzat
        // Producție: ALTER TABLE sales ADD CONSTRAINT sales_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Fără ON DELETE CASCADE, ștergerea clientului care are vânzări este refuzată, nu cascada. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE sales ADD CONSTRAINT sales_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide foreignKeyOnSales. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            căutarea poate folosi indexul și orfanul tot intră
        De ce e nevoie     fără textul greșit, indexul pare integritatea
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD INDEX (customer_id)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care căutarea poate folosi indexul și orfanul tot intră
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE sales ADD INDEX (customer_id).
    */
    // Începător: indexLetsTheOrphanIn păstrează textul greșit pentru căutarea poate folosi indexul și orfanul tot intră
    // Producție: public static String indexLetsTheOrphanIn(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Indexul nu verifică părintele. FOREIGN KEY îl verifică.
    public static String indexLetsTheOrphanIn() {
        // Începător: căutarea poate folosi indexul și orfanul tot intră
        // Producție: ALTER TABLE sales ADD INDEX (customer_id). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Indexul nu verifică părintele. FOREIGN KEY îl verifică. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE sales ADD INDEX (customer_id)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide indexLetsTheOrphanIn. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
