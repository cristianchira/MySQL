package _5_Relational_Schema._2_Foreign_Key;

//Martin G. Foreign Key
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338066#overview

// FOREIGN KEY Constraints
// https://dev.mysql.com/doc/refman/8.0/en/create-table-foreign-keys.html


/*                                            Foreign Key

    ○ Add FOREIGN KEY on existing table
                * ALTER TABLE orders
                  ADD CONSTRAINT fk_orders_customer_id FOREIGN KEY (customer_id)
                  REFERENCES customers(id);

    ○ identifies the relationships between tables,
                * not the tables themselves

    ○ Example
                * foreign key (customer_id) references customer(customer_id) on delete cascade
                            - customer_id is column from child (references) table  -- sales table
                            - customer is customer table
                            - customer(customer_id) table and column from parent table who was references


                                                on delete cascade


    ○ If a specific value
                  * from the parent's table primary key has been deleted
                  * all the records from child table referring to this value will be removed as well


                                                   Full example


    ○ CREATE TABLE `sales` (
      `purchase_number` int NOT NULL AUTO_INCREMENT,
      `date_of_purchase` date DEFAULT NULL,
      `customer_id` int DEFAULT NULL,
      `item_code` varchar(10) DEFAULT NULL,
      PRIMARY KEY (`purchase_number`),

      KEY `customer_id` (`customer_id`),

      CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
      ON DELETE CASCADE

       ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

 */
/*
TOPIC:
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) refuză vânzarea al cărei client nu există; coloana fără constrângere nu verifică.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu rămână o vânzare cu customer_id fără părinte. Rândul copil fie are părinte, fie nu intră.
MECHANISM:
    Serverul verifică constrângerea la INSERT sau UPDATE pe copil. ON DELETE CASCADE scoate și copiii când dispare părintele, dacă ai scris acțiunea.
LOOKALIKES:
    O coloană customer_id fără FOREIGN KEY acceptă orice întreg. Indexul singur nu verifică părintele.
BOOK THEORY:
    Manualul 9.7, 15.1.25.5, deschis în tur: FOREIGN KEY stă pe copil și refuză valoarea fără părinte. Implicit, verificarea InnoDB este în stratul SQL, deci un INSERT respins nu consumă AUTO_INCREMENT.  PDF-ul Foreign_Key.pdf a fost deschis.
BOOK EXAMPLE:
    foreignKeyRejectsTheOrphan
REPO DEMO:
    src/main/java/_5_Relational_Schema/_2_Foreign_Key/Foreign_Key.java
BREAK LAB:
    indexDoesNotCheckTheParent ține ALTER TABLE sales ADD INDEX customer_id (customer_id). Indexul grăbește căutarea. FOREIGN KEY refuză copilul fără părinte.
TRADE-OFFS:
    Un index simplu când doar cauți customer_id și accepți valori fără părinte.
ANTI-PATTERNS:
    ADD INDEX ca să aperi integritatea. Orfanul intră.
STAFF QUESTIONS:
    foreignKeyRejectsTheOrphan și indexDoesNotCheckTheParent. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O cutie nu poate avea un lot inexistent: FOREIGN KEY (lot_no) REFERENCES lot (lot_no). Un index pe lot_no nu refuză orfanul.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE?
   Capcană: Un index pe customer_id refuză orfanul.
   Cheie: foreignKeyRejectsTheOrphan: un customer_id fără părinte este refuzat, iar ștergerea părintelui poate scoate vânzările copil. fără constrângere, sales primește un customer_id care nu există în customer
   Demonstrație: foreignKeyRejectsTheOrphan
2. Enunț: Ce rămâne în tabelă sau în sesiune după ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE?
   Capcană: ON DELETE CASCADE este implicit.
   Cheie: Efectul este: un customer_id fără părinte este refuzat, iar ștergerea părintelui poate scoate vânzările copil. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: foreignKeyRejectsTheOrphan
3. Enunț: De ce ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE nu este vecinul său?
   Capcană: FOREIGN KEY șterge tabela părinte.
   Cheie: O coloană customer_id fără FOREIGN KEY acceptă orice întreg. Indexul singur nu verifică părintele.
   Demonstrație: foreignKeyRejectsTheOrphan
4. Enunț: Cum eșuează ALTER TABLE sales ADD INDEX customer_id (customer_id)?
   Capcană: Săgeata din Workbench este deja constrângerea.
   Cheie: indexDoesNotCheckTheParent: Indexul grăbește căutarea. FOREIGN KEY refuză copilul fără părinte.
   Demonstrație: indexDoesNotCheckTheParent
5. Enunț: Când nu mai trimiți ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE?
   Capcană: INSERT-ul respins consumă mereu AUTO_INCREMENT pe 9.7.
   Cheie: Un index simplu când doar cauți customer_id și accepți valori fără părinte.
   Demonstrație: foreignKeyRejectsTheOrphan

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei ca sales să nu aibă client lipsă -> foreignKeyRejectsTheOrphan -> ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE -> nu șterge copiii dacă nu ai scris ON DELETE CASCADE
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> indexDoesNotCheckTheParent -> ALTER TABLE sales ADD INDEX customer_id (customer_id) -> Indexul grăbește căutarea. FOREIGN KEY refuză copilul fără părinte.
    3. Granița
           -> FOREIGN KEY este constrângerea. Săgeata din diagramă nu o creează.
    4. Alternativa
           -> O coloană customer_id fără FOREIGN KEY acceptă orice întreg. Indexul singur nu verifică părintele.
    5. Anti-patternul
           -> ADD INDEX ca să aperi integritatea. Orfanul intră.
    6. Dovada
           -> foreignKeyRejectsTheOrphan ține propoziția. indexDoesNotCheckTheParent ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> FOREIGN KEY refuză copilul fără părinte. Indexul nu verifică. CASCADE se întâmplă doar dacă l-ai scris. Pe 9.7, verificarea implicită este în stratul SQL.
*/

public class Foreign_Key {
    /*
    🟢 Începător
        Ce este            un customer_id fără părinte este refuzat, iar ștergerea părintelui poate scoate vânzările copil
        De ce e nevoie     fără constrângere, sales primește un customer_id care nu există în customer
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este KEY customer_id (customer_id), care este index și nu verifică părintele
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu KEY customer_id (customer_id), care este index și nu verifică părintele.
    */
    // Începător: foreignKeyRejectsTheOrphan păstrează propoziția pentru un customer_id fără părinte este refuzat, iar ștergerea părintelui poate scoate vânzările copil
    // Producție: public static String foreignKeyRejectsTheOrphan(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ON DELETE CASCADE nu este implicit. Fără acțiune, ștergerea părintelui cu copii este refuzată, nu cascada.
    public static String foreignKeyRejectsTheOrphan() {
        // Începător: un customer_id fără părinte este refuzat, iar ștergerea părintelui poate scoate vânzările copil
        // Producție: ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: ON DELETE CASCADE nu este implicit. Fără acțiune, ștergerea părintelui cu copii este refuzată, nu cascada. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide foreignKeyRejectsTheOrphan. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            apare un index și un customer_id inexistent tot poate intra
        De ce e nevoie     fără textul greșit, indexul pare integritatea
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD INDEX customer_id (customer_id)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care apare un index și un customer_id inexistent tot poate intra
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu KEY customer_id (customer_id), care este index și nu verifică părintele.
    */
    // Începător: indexDoesNotCheckTheParent păstrează textul greșit pentru apare un index și un customer_id inexistent tot poate intra
    // Producție: public static String indexDoesNotCheckTheParent(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Indexul grăbește căutarea. FOREIGN KEY refuză copilul fără părinte.
    public static String indexDoesNotCheckTheParent() {
        // Începător: apare un index și un customer_id inexistent tot poate intra
        // Producție: ALTER TABLE sales ADD INDEX customer_id (customer_id). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Indexul grăbește căutarea. FOREIGN KEY refuză copilul fără părinte. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE sales ADD INDEX customer_id (customer_id)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide indexDoesNotCheckTheParent. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
