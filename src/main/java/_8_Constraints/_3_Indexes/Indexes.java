package _8_Constraints._3_Indexes;

//  https://dev.mysql.com/doc/refman/8.0/en/mysql-indexes.html
// https://dev.mysql.com/doc/search/?d=201&p=1&q=Indexes

//https://dev.mysql.com/doc/refman/8.0/en/create-index.html


/*                                   Index of a table


    ○ the index of a table functions like the index of a book

                   *  data is taken from a column of the table

                   * and is stored in a certain order in a distinct place,

                             called an index.

    ○ we can use an index that

                 * will increase the speed of searches related to a table

    ○ an organizational unit that help retrieve data more easily.

    ○ Of course, this doesn't come without a cost.

                   * It takes more time to update a table

                   * because indexes must be updated, too,

                   * and that's time-consuming.

    ○ Primary and Unique keys are MySQL indexes


                                       SQL Index


    ○ composite index
                  * is applied on multiple columns not only one

    ○ CREATE INDEX index_name
       ON table_name (column_1, column_2, …);

    ○ The parentheses serve as to indicate the column names on which our search will be based.

                   * It will be speeded up and the data will be filtered in a quicker way.

                   * Speaking technically, the idea is to choose columns so your search will be optimized.

                   * These must be fields from your data table.

                   * You will search frequently.




                                                  SQL Example


    ○ create index i_composite  on employees(first_name, last_name);

    ○   drop index i_composite on employees;

         ALTER TABLE employees

         DROP INDEX i_hire_date;

    ○ show index from employees;



                                                CREATE INDEX  FULL Statement


CREATE [UNIQUE | FULLTEXT | SPATIAL] INDEX index_name
    [index_type]
    ON tbl_name (key_part,...)
    [index_option]
    [algorithm_option | lock_option] ...

key_part: {col_name [(length)] | (expr)} [ASC | DESC]

index_option: {
    KEY_BLOCK_SIZE [=] value
  | index_type
  | WITH PARSER parser_name
  | COMMENT 'string'
  | {VISIBLE | INVISIBLE}
  | ENGINE_ATTRIBUTE [=] 'string'
  | SECONDARY_ENGINE_ATTRIBUTE [=] 'string'
}

index_type:
    USING {BTREE | HASH}

algorithm_option:
    ALGORITHM [=] {DEFAULT | INPLACE | COPY}

lock_option:
    LOCK [=] {DEFAULT | NONE | SHARED | EXCLUSIVE}


                                            ADD UNIQUE KEY


           ALTER TABLE table_name
           ADD UNIQUE KEY  (column_name)


                                        DROP UNIQUE KEY


            ALTER TABLE table_name
            DROP index column_name

   will be index instead of Unique Key and column name without parenthesis.

 */

/*
TOPIC:
    CREATE INDEX i_composite ON employees (first_name, last_name) construiește indexul; nu filtrează singur și nu face coloanele unice.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să poată căuta perechea de nume. Rândurile rămân, inclusiv duplicatele.
MECHANISM:
    Serverul construiește indexul. WHERE îl poate folosi. Un index nefolosit nu grăbește propoziția. UNIQUE ar refuza duplicatul.
LOOKALIKES:
    ADD UNIQUE (first_name, last_name) refuză a doua pereche. DROP INDEX scoate indexul și nu șterge rândurile.
BOOK THEORY:
    Manualul 9.7, 15.1.18, deschis în tur: CREATE INDEX construiește indexul.  PDF-ul MySQL+Indexes.pdf a fost deschis. Comentariul de curs spune costul la scriere: indexul se actualizează.
BOOK EXAMPLE:
    createCompositeNameIndex
REPO DEMO:
    src/main/java/_8_Constraints/_3_Indexes/Indexes.java
BREAK LAB:
    uniqueRejectsTheSecondName ține ALTER TABLE employees ADD UNIQUE (first_name, last_name). UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate. DROP INDEX nu șterge angajați.
TRADE-OFFS:
    UNIQUE când perechea de nume chiar nu are voie să se repete.
ANTI-PATTERNS:
    ADD UNIQUE când voiai doar viteză. Al doilea Ion Popescu nu mai intră.
STAFF QUESTIONS:
    createCompositeNameIndex și uniqueRejectsTheSecondName. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Cauți destinatarul după nume și oraș și accepți duplicate: CREATE INDEX pe (last_name, city). UNIQUE le-ar opri.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE INDEX i_composite ON employees (first_name, last_name)?
   Capcană: CREATE INDEX face numele unic.
   Cheie: createCompositeNameIndex: apare un index pe perechea de nume și duplicatele de nume rămân posibile. fără index, căutarea pe nume poate citi toată tabela
   Demonstrație: createCompositeNameIndex
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE INDEX i_composite ON employees (first_name, last_name)?
   Capcană: Orice index este folosit de orice SELECT.
   Cheie: Efectul este: apare un index pe perechea de nume și duplicatele de nume rămân posibile. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: createCompositeNameIndex
3. Enunț: De ce CREATE INDEX i_composite ON employees (first_name, last_name) nu este vecinul său?
   Capcană: DROP INDEX șterge angajații.
   Cheie: ADD UNIQUE (first_name, last_name) refuză a doua pereche. DROP INDEX scoate indexul și nu șterge rândurile.
   Demonstrație: createCompositeNameIndex
4. Enunț: Cum eșuează ALTER TABLE employees ADD UNIQUE (first_name, last_name)?
   Capcană: Indexul ține loc de WHERE.
   Cheie: uniqueRejectsTheSecondName: UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate. DROP INDEX nu șterge angajați.
   Demonstrație: uniqueRejectsTheSecondName
5. Enunț: Când nu mai trimiți CREATE INDEX i_composite ON employees (first_name, last_name)?
   Capcană: Scrierea nu plătește nimic pentru index.
   Cheie: UNIQUE când perechea de nume chiar nu are voie să se repete.
   Demonstrație: createCompositeNameIndex

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei căutare pe nume și accepți două persoane cu același nume -> createCompositeNameIndex -> CREATE INDEX i_composite ON employees (first_name, last_name) -> nu garantează că planul îl folosește și nu face numele unic
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> uniqueRejectsTheSecondName -> ALTER TABLE employees ADD UNIQUE (first_name, last_name) -> UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate. DROP INDEX nu șterge angajați.
    3. Granița
           -> CREATE INDEX este propoziția. Un index desenat și neexecutat nu există.
    4. Alternativa
           -> ADD UNIQUE (first_name, last_name) refuză a doua pereche. DROP INDEX scoate indexul și nu șterge rândurile.
    5. Anti-patternul
           -> ADD UNIQUE când voiai doar viteză. Al doilea Ion Popescu nu mai intră.
    6. Dovada
           -> createCompositeNameIndex ține propoziția. uniqueRejectsTheSecondName ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Indexul scurtează căutarea doar dacă planul îl folosește. Nu filtrează și nu face unic. UNIQUE refuză duplicatul. DROP INDEX lasă rândurile.
*/

public class Indexes {
    /*
    🟢 Începător
        Ce este            apare un index pe perechea de nume și duplicatele de nume rămân posibile
        De ce e nevoie     fără index, căutarea pe nume poate citi toată tabela
    🟡 Producție
        Cum se scrie       CREATE INDEX i_composite ON employees (first_name, last_name)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este ALTER TABLE employees ADD UNIQUE (first_name, last_name)
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE employees ADD UNIQUE (first_name, last_name).
    */
    // Începător: createCompositeNameIndex păstrează propoziția pentru apare un index pe perechea de nume și duplicatele de nume rămân posibile
    // Producție: public static String createCompositeNameIndex(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DROP INDEX i_composite ON employees scoate indexul și lasă rândurile. Nu este DELETE.
    public static String createCompositeNameIndex() {
        // Începător: apare un index pe perechea de nume și duplicatele de nume rămân posibile
        // Producție: CREATE INDEX i_composite ON employees (first_name, last_name). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: DROP INDEX i_composite ON employees scoate indexul și lasă rândurile. Nu este DELETE. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE INDEX i_composite ON employees (first_name, last_name)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide createCompositeNameIndex. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            a doua pereche identică de nume este refuzată
        De ce e nevoie     fără textul greșit, indexul de căutare pare unic
    🟡 Producție
        Cum se scrie       ALTER TABLE employees ADD UNIQUE (first_name, last_name)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care a doua pereche identică de nume este refuzată
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE employees ADD UNIQUE (first_name, last_name).
    */
    // Începător: uniqueRejectsTheSecondName păstrează textul greșit pentru a doua pereche identică de nume este refuzată
    // Producție: public static String uniqueRejectsTheSecondName(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate. DROP INDEX nu șterge angajați.
    public static String uniqueRejectsTheSecondName() {
        // Începător: a doua pereche identică de nume este refuzată
        // Producție: ALTER TABLE employees ADD UNIQUE (first_name, last_name). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: UNIQUE este constrângerea. CREATE INDEX fără UNIQUE permite duplicate. DROP INDEX nu șterge angajați. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE employees ADD UNIQUE (first_name, last_name)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide uniqueRejectsTheSecondName. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
