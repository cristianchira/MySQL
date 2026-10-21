package _4_Database_Terminology.DBMS;




/*                                 Database Management Systems (DBMS)


     ○ are software systems used

                 - to store,

                 - retrieve,

                 - and run queries on data.

    ○ A DBMS

                  * serves as an interface between an end-user and a database,

                  * allowing users to

                                - create,

                                - read,

                                - update,

                                - and delete

         data in the database.

    ○ DBMS manage

                  * the data,

                  * the database engine,

                  *  and the database schema,

        allowing for data to be manipulated or extracted by users and other programs. This helps provide

                  *  data security,

                  * data integrity,

                  * concurrency,

                  * and uniform data

        administration procedures.

    ○ DBMS optimizes

                  * the organization of data

                                - by following a database schema design technique called

                                              • normalization,

            which splits a large table into smaller tables when any of its attributes have redundancy in values.

    ○ DBMS offer many benefits over traditional file systems, including

                 * flexibility

                 * and a more complex backup system.

    ○ Database management systems can be classified based on a variety of criteria such as:

                * the data model,

                * the database distribution,

                * or user numbers.


                                        The most widely used types of DBMS software are


    1. Relational:

                * Relational database management systems (RDBMS)

                              - are the most popular data model

                                            • because of its user-friendly interface.

                      It is based on normalizing data in the rows and columns of the tables.
                      This is a viable option when you need a data storage system that is

                               - scalable,

                               - flexible,

                               - and able to manage lots of information.

    2. Distributed:

                * A distributed DBMS is a set of

                               - logically interrelated databases distributed over a network

                       that is managed by a

                                - centralized database application.

                        This type of DBMS

                                  - synchronizes data periodically

                                  - and ensures that any change to data is universally updated in the database.

    3. Hierarchical:

                * Hierarchical databases organize model data

                                  - in a tree-like structure.

                          Data storage is

                                    - either a top-down

                                    - or bottom-up format and is represented using a parent-child relationship.

    4. Object-oriented:

                  * Object-oriented models

                                      - store data in objects instead of rows and columns.

                           It is based on object-oriented programming (OOP)

                                       - that allows objects to have members such as

                                                     • fields,

                                                     • properties,

                                                     • and methods.

    5. Network

                 * The network database model

                               - addresses the need for more complex relationships

                               - by allowing each child to have multiple parents.

                * Entities are organized in a graph

                                - that can be accessed through several paths.

 */

/*
TOPIC:
    DBMS-ul este serverul care execută propoziția; SHOW DATABASES îi cere lista schemelor, iar motorul de stocare este componenta de pe SHOW ENGINES.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu confunzi serverul cu motorul unei tabele. Schemele se văd fără să schimbi un rând.
MECHANISM:
    Serverul execută SHOW DATABASES. SHOW ENGINES listează componentele, nu schemele.
LOOKALIKES:
    SHOW ENGINES arată Support și Transactions. Nu listează schemele.
BOOK THEORY:
    SHOW DATABASES este propoziția din comentariul deja corect din My_Sql_Commands. Pagina SHOW nu a fost adusă separat: NERULAT AICI pentru acel URL. PDF de DBMS în folder nu a apărut la listare.
    HISTORICAL: pagina 15.7.7.17 a fost adusă după aceea. SHOW DATABASES listează schemele pentru care contul are un privilegiu, sau pe toate dacă are privilegiul global SHOW DATABASES. Nu schimbă rânduri. SHOW ENGINES rămâne lista de motoare.
BOOK EXAMPLE:
    showDatabasesAsksTheServer
REPO DEMO:
    src/main/java/_4_Database_Terminology/DBMS/DBMS.java
BREAK LAB:
    showEnginesIsTheComponent ține SHOW ENGINES. SHOW ENGINES este componenta. SHOW DATABASES este schema. DBMS-ul le execută pe amândouă, dar nu sunt același rezultat.
TRADE-OFFS:
    SHOW ENGINES când întrebarea este cine ține rândurile și dacă are Transactions.
ANTI-PATTERNS:
    SHOW ENGINES ca să vezi schemele. Nu vezi numele bazelor.
STAFF QUESTIONS:
    showDatabasesAsksTheServer și showEnginesIsTheComponent. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Înainte de un registru nou, SHOW DATABASES îți spune dacă schema clinic există. SHOW ENGINES îți spune dacă motorul are tranzacții.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SHOW DATABASES?
   Capcană: SHOW ENGINES listează schemele.
   Cheie: showDatabasesAsksTheServer: primești schemele vizibile contului și nu schimbi motorul tabelei. fără propoziție, DBMS-ul din definiție nu întoarce lista
   Demonstrație: showDatabasesAsksTheServer
2. Enunț: Ce rămâne în tabelă sau în sesiune după SHOW DATABASES?
   Capcană: SHOW DATABASES alege InnoDB.
   Cheie: Efectul este: primești schemele vizibile contului și nu schimbi motorul tabelei. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: showDatabasesAsksTheServer
3. Enunț: De ce SHOW DATABASES nu este vecinul său?
   Capcană: DBMS-ul este Workbench.
   Cheie: SHOW ENGINES arată Support și Transactions. Nu listează schemele.
   Demonstrație: showDatabasesAsksTheServer
4. Enunț: Cum eșuează SHOW ENGINES?
   Capcană: SHOW DATABASES creează o schemă.
   Cheie: showEnginesIsTheComponent: SHOW ENGINES este componenta. SHOW DATABASES este schema. DBMS-ul le execută pe amândouă, dar nu sunt același rezultat.
   Demonstrație: showEnginesIsTheComponent
5. Enunț: Când nu mai trimiți SHOW DATABASES?
   Capcană: Ca să vezi bazele, citești coloana Transactions.
   Cheie: SHOW ENGINES când întrebarea este cine ține rândurile și dacă are Transactions.
   Demonstrație: showDatabasesAsksTheServer

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei schemele, nu motoarele -> showDatabasesAsksTheServer -> SHOW DATABASES -> nu arată schemele la care contul nu are privilegiu
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> showEnginesIsTheComponent -> SHOW ENGINES -> SHOW ENGINES este componenta. SHOW DATABASES este schema. DBMS-ul le execută pe amândouă, dar nu sunt același rezultat.
    3. Granița
           -> Propoziția o execută serverul. Interfața este clientul.
    4. Alternativa
           -> SHOW ENGINES arată Support și Transactions. Nu listează schemele.
    5. Anti-patternul
           -> SHOW ENGINES ca să vezi schemele. Nu vezi numele bazelor.
    6. Dovada
           -> showDatabasesAsksTheServer ține propoziția. showEnginesIsTheComponent ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DBMS-ul execută propoziția. SHOW DATABASES listează scheme. SHOW ENGINES listează motoare. Nu sunt același rezultat.
*/

public class DBMS {
    /*
    🟢 Începător
        Ce este            primești schemele vizibile contului și nu schimbi motorul tabelei
        De ce e nevoie     fără propoziție, DBMS-ul din definiție nu întoarce lista
    🟡 Producție
        Cum se scrie       SHOW DATABASES
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SHOW ENGINES, care listează motoarele, nu schemele
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SHOW ENGINES, care listează motoarele, nu schemele.
    */
    // Începător: showDatabasesAsksTheServer păstrează propoziția pentru primești schemele vizibile contului și nu schimbi motorul tabelei
    // Producție: public static String showDatabasesAsksTheServer(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. SHOW DATABASES nu alege InnoDB și nu creează o schemă.
    public static String showDatabasesAsksTheServer() {
        // Începător: primești schemele vizibile contului și nu schimbi motorul tabelei
        // Producție: SHOW DATABASES. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: SHOW DATABASES nu alege InnoDB și nu creează o schemă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SHOW DATABASES";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide showDatabasesAsksTheServer. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            vezi Support și Transactions, nu lista de scheme
        De ce e nevoie     fără textul greșit, cele două SHOW par aceeași listă
    🟡 Producție
        Cum se scrie       SHOW ENGINES
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care vezi Support și Transactions, nu lista de scheme
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SHOW ENGINES, care listează motoarele, nu schemele.
    */
    // Începător: showEnginesIsTheComponent păstrează textul greșit pentru vezi Support și Transactions, nu lista de scheme
    // Producție: public static String showEnginesIsTheComponent(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. SHOW ENGINES este componenta. SHOW DATABASES este schema. DBMS-ul le execută pe amândouă, dar nu sunt același rezultat.
    public static String showEnginesIsTheComponent() {
        // Începător: vezi Support și Transactions, nu lista de scheme
        // Producție: SHOW ENGINES. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: SHOW ENGINES este componenta. SHOW DATABASES este schema. DBMS-ul le execută pe amândouă, dar nu sunt același rezultat. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SHOW ENGINES";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide showEnginesIsTheComponent. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
