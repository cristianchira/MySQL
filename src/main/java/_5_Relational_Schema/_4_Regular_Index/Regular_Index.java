package _5_Relational_Schema._4_Regular_Index;

/*                     A non-unique index (also called a Regular Index)


                                Ways to create Regular Index


    1. Create a Regular Index key on an existing table

                * CREATE INDEX email_index ON users (email_address);

    2. Create a regular index key on a new table using index or key:

                1.  Using the CREATE INDEX statement

                            - CREATE TABLE table_name (
                              -- Column definitions
                              INDEX index_name (column_name)
                              );


                2. Using the KEY index

                            - CREATE TABLE table_name (
                              -- Column definitions
                              KEY index_name (column_name)
                              );

    ○ is an index created on one or more columns in a table

                * without enforcing uniqueness for the indexed columns.

        It means that multiple rows in the table

                * can have the same values in the indexed columns.

        The primary purpose of a non-unique index is to

                * improve the performance of queries that involve the indexed columns,

                            - such as searching,

                            - filtering,

                            - joining,

                            - or sorting operations.

         In the example provided earlier, a non-unique index is defined

                * on the customer_id column of the sales table:

                            - KEY `customer_id` (`customer_id`),

                * This index will help the database management system

                             - quickly find and retrieve rows in the sales table

                             - that have specific customer_id values

                             - or are involved in join operations with another table

                                         • on the customer_id column.

                 * Although the index allows duplicate customer_id values in the sales table,

                              - it still significantly improves the performance of relevant queries.


                                                        NOTE


    ○ Keep in mind that

                * while indexes can speed up query performance,

                * they also have some trade-offs.

        They

                 * increase the storage space required for the table

                 * and can slow down

                             - insert,

                             - update,

                             - or delete

                 operations because the index

                             - needs to be updated when the table data changes.

        It's essential to create indexes

                  * judiciously

         and focus on columns that are

                  * frequently used in queries to balance performance benefits and overheads.

 */

/*
TOPIC:
    CREATE INDEX email_index ON users (email_address) construiește un index care poate repeta valori; nu face emailul unic și nu filtrează singur.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să poată căuta emailul fără să interzică duplicatele. Rândurile rămân.
MECHANISM:
    Serverul construiește indexul la sfârșitul lui CREATE INDEX. WHERE folosește planul doar dacă optimizerul alege indexul. Un index nefolosit nu grăbește propoziția.
LOOKALIKES:
    UNIQUE KEY unique_email_address (email_address) refuză duplicatul. Indexul obișnuit nu.
BOOK THEORY:
    Manualul 9.7, 15.1.18 CREATE INDEX, deschis în tur: indexul se construiește pe coloane. Nu filtrează el însuși rândurile; WHERE le filtrează. Un index nefolosit nu grăbește propoziția.  PDF de index în folderul de constrângeri a fost deschis; acest folder are definiția de index neunic în comentariu.
BOOK EXAMPLE:
    createNonUniqueEmailIndex
REPO DEMO:
    src/main/java/_5_Relational_Schema/_4_Regular_Index/Regular_Index.java
BREAK LAB:
    uniqueRejectsTheSecondEmail ține ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address). UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate.
TRADE-OFFS:
    UNIQUE când duplicatul trebuie refuzat, nu doar căutat.
ANTI-PATTERNS:
    ADD UNIQUE când voiai doar căutare. Al doilea email nu mai intră.
STAFF QUESTIONS:
    createNonUniqueEmailIndex și uniqueRejectsTheSecondEmail. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Cauți telefonul clientului și accepți două fișe cu același număr: CREATE INDEX phone_idx ON person (phone). UNIQUE l-ar refuza pe al doilea.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE INDEX email_index ON users (email_address)?
   Capcană: CREATE INDEX face emailul unic.
   Cheie: createNonUniqueEmailIndex: apare un index care permite același email de mai multe ori. fără index, căutarea poate citi toată tabela; cu UNIQUE ai interzis duplicatele, ceea ce aici nu vrei
   Demonstrație: createNonUniqueEmailIndex
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE INDEX email_index ON users (email_address)?
   Capcană: Orice index este folosit de orice WHERE.
   Cheie: Efectul este: apare un index care permite același email de mai multe ori. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: createNonUniqueEmailIndex
3. Enunț: De ce CREATE INDEX email_index ON users (email_address) nu este vecinul său?
   Capcană: UNIQUE și CREATE INDEX lasă aceleași duplicate.
   Cheie: UNIQUE KEY unique_email_address (email_address) refuză duplicatul. Indexul obișnuit nu.
   Demonstrație: createNonUniqueEmailIndex
4. Enunț: Cum eșuează ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)?
   Capcană: Indexul filtrează rândurile ca WHERE.
   Cheie: uniqueRejectsTheSecondEmail: UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate.
   Demonstrație: uniqueRejectsTheSecondEmail
5. Enunț: Când nu mai trimiți CREATE INDEX email_index ON users (email_address)?
   Capcană: Fără index, rezultatul logic al SELECT se schimbă.
   Cheie: UNIQUE când duplicatul trebuie refuzat, nu doar căutat.
   Demonstrație: createNonUniqueEmailIndex

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei căutare pe email și accepți duplicate -> createNonUniqueEmailIndex -> CREATE INDEX email_index ON users (email_address) -> nu garantează că optimizerul îl folosește și nu face emailul unic
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> uniqueRejectsTheSecondEmail -> ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address) -> UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate.
    3. Granița
           -> CREATE INDEX este propoziția. Un bif pe index în diagramă nu construiește indexul.
    4. Alternativa
           -> UNIQUE KEY unique_email_address (email_address) refuză duplicatul. Indexul obișnuit nu.
    5. Anti-patternul
           -> ADD UNIQUE când voiai doar căutare. Al doilea email nu mai intră.
    6. Dovada
           -> createNonUniqueEmailIndex ține propoziția. uniqueRejectsTheSecondEmail ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Indexul obișnuit permite duplicate și poate scurta căutarea dacă planul îl folosește. UNIQUE refuză copia. Indexul nefolosit nu grăbește propoziția.
*/

public class Regular_Index {
    /*
    🟢 Începător
        Ce este            apare un index care permite același email de mai multe ori
        De ce e nevoie     fără index, căutarea poate citi toată tabela; cu UNIQUE ai interzis duplicatele, ceea ce aici nu vrei
    🟡 Producție
        Cum se scrie       CREATE INDEX email_index ON users (email_address)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address).
    */
    // Începător: createNonUniqueEmailIndex păstrează propoziția pentru apare un index care permite același email de mai multe ori
    // Producție: public static String createNonUniqueEmailIndex(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE INDEX nu schimbă rezultatul logic al unui SELECT. Schimbă doar posibilitatea de plan.
    public static String createNonUniqueEmailIndex() {
        // Începător: apare un index care permite același email de mai multe ori
        // Producție: CREATE INDEX email_index ON users (email_address). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: CREATE INDEX nu schimbă rezultatul logic al unui SELECT. Schimbă doar posibilitatea de plan. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE INDEX email_index ON users (email_address)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide createNonUniqueEmailIndex. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            al doilea același email este refuzat, deci nu mai este indexul care permite duplicate
        De ce e nevoie     fără textul greșit, orice index pare UNIQUE
    🟡 Producție
        Cum se scrie       ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care al doilea același email este refuzat, deci nu mai este indexul care permite duplicate
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address).
    */
    // Începător: uniqueRejectsTheSecondEmail păstrează textul greșit pentru al doilea același email este refuzat, deci nu mai este indexul care permite duplicate
    // Producție: public static String uniqueRejectsTheSecondEmail(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate.
    public static String uniqueRejectsTheSecondEmail() {
        // Începător: al doilea același email este refuzat, deci nu mai este indexul care permite duplicate
        // Producție: ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE users ADD UNIQUE KEY unique_email_address (email_address)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide uniqueRejectsTheSecondEmail. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
