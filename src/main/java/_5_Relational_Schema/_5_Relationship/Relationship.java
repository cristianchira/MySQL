package _5_Relational_Schema._5_Relationship;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338070#questions


/*                                                  Relationships


    ○ Relationships tell you

                   * how much of the data from a foreign key field can be seen

                   * in the primary key column of the table

                               - the data is related to and vice versa.


                                       Types of relationships


    1. one-to-many (many-to-one)

                * One value from the customer ID column under the customer's table

                            - can be found many times in the customer ID column

                            - in the sales table.

    2. one-to-one

    3. many-to-many


 */
/*
TOPIC:
    Un client are multe vânzări: customer.id stă o dată, iar sales.customer_id se repetă; fără FOREIGN KEY, repetarea nu este verificată.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să știi care coloană este părintele. Relația desenată nu refuză orfanul.
MECHANISM:
    Serverul verifică relația doar dacă există FOREIGN KEY. Un one-to-many fără constrângere este doar convenție de coloane.
LOOKALIKES:
    FOREIGN KEY (customer_id) REFERENCES customer (id) este verificarea. Două coloane cu același nume nu sunt relația.
BOOK THEORY:
    Manualul 9.7, 15.1.25.5, deschis în tur: FOREIGN KEY stă pe copil și refuză valoarea fără părinte. Implicit, verificarea InnoDB este în stratul SQL, deci un INSERT respins nu consumă AUTO_INCREMENT.  PDF-ul Relationships.pdf a fost deschis: primele pagini sunt titlul. Comentariul de curs descrie one-to-many pe customer id.
BOOK EXAMPLE:
    foreignKeyIsTheRelationship
REPO DEMO:
    src/main/java/_5_Relational_Schema/_5_Relationship/Relationship.java
BREAK LAB:
    sameColumnNameIsNotAKey ține ALTER TABLE sales ADD COLUMN customer_id INT. Numele coloanei este convenție. FOREIGN KEY este verificarea.
TRADE-OFFS:
    Doar coloana, când copia este permisă și nimeni nu trebuie oprit.
ANTI-PATTERNS:
    ADD COLUMN customer_id și spui că ai relația. Orfanul intră.
STAFF QUESTIONS:
    foreignKeyIsTheRelationship și sameColumnNameIsNotAKey. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O cutie aparține unui lot doar dacă FOREIGN KEY o spune. Coloana lot_no singură nu oprește un lot inexistent.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ALTER TABLE sales ADD CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES customer (id)?
   Capcană: Coloana customer_id este deja FOREIGN KEY.
   Cheie: foreignKeyIsTheRelationship: o vânzare al cărei customer_id nu există este refuzată. fără constrângere, poți scrie customer_id 999 și clientul 999 nu există
   Demonstrație: foreignKeyIsTheRelationship
2. Enunț: Ce rămâne în tabelă sau în sesiune după ALTER TABLE sales ADD CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES customer (id)?
   Capcană: One-to-many din PDF refuză orfanul.
   Cheie: Efectul este: o vânzare al cărei customer_id nu există este refuzată. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: foreignKeyIsTheRelationship
3. Enunț: De ce ALTER TABLE sales ADD CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES customer (id) nu este vecinul său?
   Capcană: FOREIGN KEY stă pe părintele customer.
   Cheie: FOREIGN KEY (customer_id) REFERENCES customer (id) este verificarea. Două coloane cu același nume nu sunt relația.
   Demonstrație: foreignKeyIsTheRelationship
4. Enunț: Cum eșuează ALTER TABLE sales ADD COLUMN customer_id INT?
   Capcană: Relația șterge vânzările vechi.
   Cheie: sameColumnNameIsNotAKey: Numele coloanei este convenție. FOREIGN KEY este verificarea.
   Demonstrație: sameColumnNameIsNotAKey
5. Enunț: Când nu mai trimiți ALTER TABLE sales ADD CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES customer (id)?
   Capcană: Fără săgeată în Workbench, constrângerea nu poate exista.
   Cheie: Doar coloana, când copia este permisă și nimeni nu trebuie oprit.
   Demonstrație: foreignKeyIsTheRelationship

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei ca serverul să refuze vânzarea fără client -> foreignKeyIsTheRelationship -> ALTER TABLE sales ADD CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES customer (id) -> nu desenează relația și nu alege singur one-to-many
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> sameColumnNameIsNotAKey -> ALTER TABLE sales ADD COLUMN customer_id INT -> Numele coloanei este convenție. FOREIGN KEY este verificarea.
    3. Granița
           -> Constrângerea este mecanismul. Săgeata din model nu este propoziția.
    4. Alternativa
           -> FOREIGN KEY (customer_id) REFERENCES customer (id) este verificarea. Două coloane cu același nume nu sunt relația.
    5. Anti-patternul
           -> ADD COLUMN customer_id și spui că ai relația. Orfanul intră.
    6. Dovada
           -> foreignKeyIsTheRelationship ține propoziția. sameColumnNameIsNotAKey ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Relația verificată este FOREIGN KEY. Numele de coloană nu verifică. One-to-many din diagramă nu creează constrângerea.
*/

public class Relationship {
    /*
    🟢 Începător
        Ce este            o vânzare al cărei customer_id nu există este refuzată
        De ce e nevoie     fără constrângere, poți scrie customer_id 999 și clientul 999 nu există
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este aceeași coloană customer_id fără FOREIGN KEY
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu aceeași coloană customer_id fără FOREIGN KEY.
    */
    // Începător: foreignKeyIsTheRelationship păstrează propoziția pentru o vânzare al cărei customer_id nu există este refuzată
    // Producție: public static String foreignKeyIsTheRelationship(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. One-to-many din curs nu creează singur constrângerea.
    public static String foreignKeyIsTheRelationship() {
        // Începător: o vânzare al cărei customer_id nu există este refuzată
        // Producție: ALTER TABLE sales ADD CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES customer (id). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: One-to-many din curs nu creează singur constrângerea. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE sales ADD CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES customer (id)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide foreignKeyIsTheRelationship. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            apare coloana și orice întreg poate intra, inclusiv un client lipsă
        De ce e nevoie     fără textul greșit, numele customer_id pare relația
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD COLUMN customer_id INT
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care apare coloana și orice întreg poate intra, inclusiv un client lipsă
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu aceeași coloană customer_id fără FOREIGN KEY.
    */
    // Începător: sameColumnNameIsNotAKey păstrează textul greșit pentru apare coloana și orice întreg poate intra, inclusiv un client lipsă
    // Producție: public static String sameColumnNameIsNotAKey(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Numele coloanei este convenție. FOREIGN KEY este verificarea.
    public static String sameColumnNameIsNotAKey() {
        // Începător: apare coloana și orice întreg poate intra, inclusiv un client lipsă
        // Producție: ALTER TABLE sales ADD COLUMN customer_id INT. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Numele coloanei este convenție. FOREIGN KEY este verificarea. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE sales ADD COLUMN customer_id INT";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide sameColumnNameIsNotAKey. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
