package _4_Database_Terminology;

// Database Terminology
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338062#overview


/*                                                Database


    ○ is an organized collection of structured information, or data,

                * typically stored electronically in a computer system.


                                What we need for create a proper Database


    1. DataBase designer

                  * What do database designers actually do?

                                - They plot the entire database system on a canvas using a visualization tool.

                                 - There are two main ways to do that.

                                               1. One is drawing an entity relationship diagram,

                                                            - an E.R. diagram for brief.

                                               2. Another form of representation of a database is

                                                             - the Relational Schema.

                                                                         • It represents a table in the shape
                                                                               of a rectangle.

                                                                          • The name of the table is at the top
                                                                                of the rectangle.

                                                                           • The column names are listed below.

                                                              - All relational schemas in a database form
                                                                    the database schema. ----------------------

    2. Database Creation

                 * use SQL to set up database physically


    3. Database Management

                 * DataBase design +  Database Creation +  Database manipulation = database management


    4. Database administration:

                  * refers to the whole set of activities performed by a database administrator
                        to ensure that a database is

                                - always available as needed.

                                -  database security,

                                 - database monitoring and troubleshooting,

                                 - and planning for future growth.

*/

/*
TOPIC:
    Motorul de stocare numit de ENGINE, sau InnoDB când clauza lipsește pe MySQL 9.7, este componenta care execută schimbarea de rânduri pe acea tabelă și care decide dacă ROLLBACK mai poate scoate rândul.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Problema este să numești schema, diagrama sau încărcarea și să crezi că rândul deja stă într-o tranzacție. După propoziție, tabela este legată de o componentă: la InnoDB schimbarea încă deschisă poate fi dată înapoi; la un motor cu Transactions NO rândul rămâne.
MECHANISM:
    Serverul citește ENGINE sau motorul implicit și încarcă acea componentă. Componenta execută INSERT, UPDATE și DELETE. Definirea tabelei se vede la sfârșitul lui CREATE TABLE sau ALTER TABLE. SHOW ENGINES nu schimbă rânduri: arată Support și Transactions înainte de scriere.
LOOKALIKES:
    MyISAM. Îi separă rezultatul SHOW ENGINES: InnoDB are Support DEFAULT, Transactions YES și lacăt de rând; MyISAM are Transactions NO și lacăt de tabelă. Cuvântul OLTP, depozitul de date sau desenul din Workbench nu este clauza ENGINE.
BOOK THEORY:
    Pe MySQL 9.7, storage engine este componenta care execută propoziția pentru un tip de tabelă. InnoDB este implicit și un CREATE TABLE fără ENGINE creează o tabelă InnoDB, cu commit, rollback, recuperare după oprire, lacăt de rând, index grupat pe cheia primară și FOREIGN KEY. SHOW ENGINES listează Support YES, NO sau DEFAULT și Transactions. MyISAM are Transactions NO și lacăt de tabelă. ENGINE pe CREATE TABLE sau ALTER TABLE schimbă componenta. Dacă motorul cerut nu este disponibil, serverul creează tabela cu motorul implicit și avertizează; cu NO_ENGINE_SUBSTITUTION dă eroare și nu creează tabela. Poți folosi motoare diferite în aceeași schemă. PDF-ul SQL+Keywords.pdf deschis din acest folder este o listă de cuvinte cheie, începând cu ADD, și nu descrie motorul.
BOOK EXAMPLE:
    DatabaseTerminology_Fast.chooseEngine
REPO DEMO:
    src/main/java/_4_Database_Terminology/DataBase_Terminology.java
    src/main/java/_4_Database_Terminology/fast/DatabaseTerminology_Fast.java
BREAK LAB:
    CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING. Fără NO_ENGINE_SUBSTITUTION apare un avertisment și tabela este creată cu motorul implicit, deci numele scris nu este componenta care ține rândul. Cu NO_ENGINE_SUBSTITUTION serverul dă eroare și tabela nu este creată.
TRADE-OFFS:
    MyISAM pentru o tabelă citită aproape tot timpul, când nu ai nevoie ca ROLLBACK să scoată rândul și accepți lacătul de tabelă. MEMORY pentru o căutare care poate dispărea la oprire. Pentru rânduri care trebuie să rămână după commit, rămâi pe InnoDB.
ANTI-PATTERNS:
    CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MyISAM, apoi ROLLBACK după un INSERT care trebuia să dispară. Transactions este NO, deci rândul rămâne.
STAFF QUESTIONS:
    DatabaseTerminology_Staff_Interview.showEngines
    DatabaseTerminology_Staff_Interview.createInnodbTable
    DatabaseTerminology_Staff_Interview.createTableWithoutEngine
    DatabaseTerminology_Staff_Interview.createMyisamTable
    DatabaseTerminology_Staff_Interview.alterTableToInnodb
    DatabaseTerminology_Staff_Interview.createMissingEngine
    DatabaseTerminology_Staff_Interview.setDefaultStorageEngine
NOVEL TRANSFER:
    Două recepții de spital scriu același pat. Înainte să te bazezi pe ROLLBACK ca să anulezi a doua ocupare, citești Transactions pe motorul tabelei de paturi. Eticheta „OLTP” din documentul de analiză nu ține lacătul.
SPACED REVIEW:
    O zi: din SHOW ENGINES spui care rând are Support DEFAULT și ce înseamnă Transactions pentru un rând după ROLLBACK.
    Șapte zile: un CREATE TABLE fără ENGINE pe 9.7, și ce componentă rămâne dacă serverul a pornit cu alt default-storage-engine.
    Treizeci de zile: ENGINE=MISSING, spui dacă tabela există fără NO_ENGINE_SUBSTITUTION și cu modul pornit.
*/

/*
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE -> DatabaseTerminology_Fast.chooseEngine -> SHOW ENGINES, rândul InnoDB cu Support DEFAULT și Transactions YES -> nu garantează că un server pornit cu alt default-storage-engine încă creează InnoDB când ENGINE lipsește
    2. Eșecul cel mai mic
           -> DECIDE -> DatabaseTerminology_Staff_Interview.createMissingEngine -> CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING -> fără NO_ENGINE_SUBSTITUTION tabela poate apărea pe motorul implicit; cu modul pornit tabela nu apare
    3. Granița
           -> motorul este propoziția pe care o execută serverul. Diagrama din Workbench și cuvântul bază de date din comentariul de curs nu aleg componenta care ține rândul
    4. Alternativa
           -> MyISAM când tabela este citită aproape tot timpul și ROLLBACK nu trebuie să scoată rândul. Îi separă Transactions NO și lacătul de tabelă pe SHOW ENGINES și în tabelul de motoare
    5. Anti-patternul
           -> CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MyISAM urmat de ROLLBACK după INSERT. Rândul rămâne, pentru că Transactions este NO
    6. Dovada
           -> DatabaseTerminology_Fast.chooseEngine ține SHOW ENGINES, propoziția care arată Support și Transactions înainte de scriere
    7. Interviu
           -> Componenta de pe ENGINE execută schimbarea. SHOW ENGINES. Dacă Transactions este NO, ROLLBACK nu scoate rândul
*/

/*
1. Ce problemă rezolvă mecanismul
Enunț: Ai o tabelă nouă și două sesiuni vor schimba același rând. Ce propoziție spune ce componentă execută schimbarea și dacă ROLLBACK mai poate scoate rândul?
Capcană: „Scriem în comentariu că baza este OLTP și tranzacția există.”
Cheie: Motorul de stocare. SHOW ENGINES arată Support și Transactions. InnoDB pe 9.7 este DEFAULT și Transactions YES. Eticheta de încărcare nu leagă tabela.
Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

2. Ce se întâmplă cu rândurile, schema, indexul sau tranzacția
Enunț: Rulezi CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB. Ce este adevărat despre rânduri, despre index și despre o schimbare încă nedusă la capăt, la sfârșitul propoziției?
Capcană: „ENGINE doar redenumește tabela. Rândurile și lacătul rămân la fel pe orice motor.”
Cheie: Tabela există la sfârșitul lui CREATE TABLE, încă fără rânduri de comandă. La InnoDB cheia primară este indexul grupat, deci rândul viitor stă în acel index. O schimbare încă deschisă poate fi dată înapoi, pentru că Transactions este YES. Lacătul de scriere este pe rând, nu pe toată tabela.
Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

3. De ce nu este mecanismul vecin
Enunț: Aceeași tabelă este creată cu ENGINE=MyISAM. De ce nu este același lucru cu ENGINE=INNODB?
Capcană: „Ambele sunt motoare, deci ROLLBACK și FOREIGN KEY se poartă la fel.”
Cheie: MyISAM. SHOW ENGINES are Transactions NO, iar tabelul de motoare dă lacăt de tabelă și FOREIGN KEY No. InnoDB are Transactions YES, lacăt de rând și FOREIGN KEY. Cuvântul OLTP nu schimbă coloana Transactions.
Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

4. Cum eșuează când este folosit greșit
Enunț: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING. Ce vezi în schemă?
Capcană: „Serverul păstrează numele MISSING pe tabelă, deci știm ce componentă am cerut.”
Cheie: Motorul cerut nu este disponibil. Fără NO_ENGINE_SUBSTITUTION serverul avertizează și creează tabela cu motorul implicit, deci numele din propoziție nu este componenta care ține rândul. Cu NO_ENGINE_SUBSTITUTION eroarea oprește crearea și tabela nu există.
Demonstrație: DatabaseTerminology_Staff_Interview.createMissingEngine()

5. Ce alegi în locul lui și în ce condiție
Enunț: Tabela de export este citită rar, nu trebuie să participe la ROLLBACK, iar o căutare temporară poate dispărea la oprire. Ce componentă lași pe comenzi și ce lași pe export?
Capcană: „Un singur motor pe toată schema, ca să nu ne gândim la Transactions.”
Cheie: InnoDB rămâne pe tabela de comenzi, unde commit trebuie să păstreze rândul și FOREIGN KEY trebuie să refuze copilul fără părinte. MEMORY este alternativa pentru căutarea care poate dispărea. MyISAM intră doar dacă accepți lacăt de tabelă și Transactions NO. Aceeași schemă poate ține ambele.
Demonstrație: DatabaseTerminology_Staff_Interview.setDefaultStorageEngine()
*/
public class DataBase_Terminology {
}
