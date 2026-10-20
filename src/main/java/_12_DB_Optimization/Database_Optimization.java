package _12_DB_Optimization;

// Optimization
// https://dev.mysql.com/doc/refman/5.7/en/optimization.html
// HISTORICAL: legătura de deasupra este manualul 5.7. Manualul deschis în tur este 9.7, capitolul 10.


/*                       Database Optimization Overview


    ▬▬▐ Database performance depends on several factors:
                  * Optimizing at the
                                - Database Level
                  * Optimizing at the
                                - Hardware Level
                  * Balancing
                                - Portability and Performance


                        ▬▬ 1. Optimizing at the Database Level ▬▬


    1. Basic Design
           The most important factor in making a database application fast is its Basic Design:
                   * Are the tables structured properly?
                                - In particular, do the columns have the right data types,
                                - and does each table have the appropriate columns for the type of work?
           For example, applications that perform frequent updates often
                  * have many tables with few columns,
                  * while applications that analyze large amounts of data often have
                              - few tables with many columns.

    2. Right Indexes
             * Are the right indexes in place to make queries efficient?

    3. Storage Engine
             * Are you using the appropriate storage engine for each table,
             * and taking advantage of the strengths and features of each storage engine you use?
     In particular, the choice of a transactional storage engine such as
             * InnoDB
             * or a nontransactional
                         • one such as MyISAM
            can be very important for performance and scalability.

    4. Row Format
             * Does each table use an appropriate row format?
             * This choice also depends on the storage engine used for the table. In particular,
                         - Compressed Tables
                 use less disk space and so require less disk I/O to read and write the data.
                         - Compression is available for
                                     • all kinds of workloads with InnoDB tables,
                                     • and for read-only MyISAM tables.

    5. Locking Strategy
                * Does the application use an appropriate locking strategy?
        For example, by allowing shared access when possible so
               * that database operations can run concurrently,
               * and requesting exclusive access when appropriate so that critical operations
                         - get top priority.
        Again,the choice of storage engine is significant.
                * The InnoDB storage engine handles most
                         - locking issues without involvement from you,
                         - allowing for better concurrency in the database
                         - and reducing the amount of experimentation and tuning for your code.

    6. Caching Size
                * Are all memory areas used for caching sized correctly?
                * That is, large enough to hold frequently accessed data,
                * but not so large that they
                         - overload physical memory
                         - and cause paging.
        The main memory areas to configure are the
                * InnoDB buffer pool,
                * the MyISAM key cache,
                * and the MySQL query cache.
                  HISTORICAL: propoziția „MySQL query cache” nu este lista din manualul 9.7, 10.10, deschis în tur. Acolo sunt buffer pool, MyISAM key cache și cache-ul de prepared statements. query-cache.html pe 9.7 a răspuns 404. 10.2.1 încă numește „MySQL query cache” într-o propoziție, deci manualul nu este uniform.
                  HISTORICAL: propoziția de deasupra nu este lista din manualul 9.7, secțiunea 10.1, deschisă în tur. Acolo zonele de configurat sunt buffer pool-ul InnoDB și key cache-ul MyISAM. Pagina query-cache.html pentru 9.7 a răspuns 404. Secțiunea 10.2.1 încă numește query cache lângă cele două zone.



                                          2. Optimizing at the Hardware Level


    Any database application eventually hits hardware limits as the database becomes more and more busy. A DBA must evaluate
        whether it is possible to tune the application or reconfigure the server to avoid these bottlenecks, or whether
        more hardware resources are required.
            System bottlenecks typically arise from these sources:

    Disk seeks.

        It takes time for the disk to find a piece of data. With modern disks, the mean time for this is usually lower
        than 10ms, so we can in theory do about 100 seeks a second. This time improves slowly with new disks and is very
        hard to optimize for a single table. The way to optimize seek time is to distribute the data onto more than one disk.

    Disk reading and writing.

        When the disk is at the correct position, we need to read or write the data. With modern disks, one disk delivers
        at least 10�20MB/s throughput.This is easier to optimize than seeks because you can read in parallel from multiple disks.


    CPU cycles.

        When the data is in main memory, we must process it to get our result. Having large tables compared to the amount
        of memory is the most common limiting factor. But with small tables, speed is usually not the problem.

    Memory bandwidth.

        When the CPU needs more data than can fit in the CPU cache, main memory bandwidth becomes a bottleneck. This is
        an uncommon bottleneck for most systems, but one to be aware of.


                                           3. Balancing Portability and Performance


    To use performance-oriented SQL extensions in a portable MySQL_ program, you can wrap MySQL_-specific keywords in a
    statement within /*  *\/
 comment delimiters. Other SQL servers ignore the commented keywords. For information about writing comments, see Section 9.6, �Comments�.


                                                          NOTE



    ○ InnoDB is the default storage engine for new tables.

                   * In practice, the advanced InnoDB performance features mean that

                   * InnoDB tables often outperform the simpler MyISAM tables,

                                 - especially for a busy database.


 */






/*
TOPIC:
    EXPLAIN arată cum ar citi serverul coletele cu status deschis. De acolo alegi un index utilizabil sau lași propoziția pe care serverul o pliază deja. EXPLAIN nu schimbă rândurile. EXPLAIN ANALYZE rulează propoziția.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Clientul local mysql.exe este 9.2.0 și nu este versiunea serverului. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Căutarea coletelor deschise nu se rescrie din obișnuință. După EXPLAIN, rândurile din shipment sunt aceleași. După EXPLAIN ANALYZE pe DELETE-ul multi-tabelă, coletele deschise pot lipsi.
MECHANISM:
    Serverul, dacă primește planTheServerWouldUse, întoarce planul pentru SELECT-ul cu status = 'open'. Nu șterge rânduri. indexWhenNoUsableRestriction adaugă i_status în schemă. analyzeDeleteRunsTheStatement este EXPLAIN ANALYZE pe DELETE multi-tabelă: manualul 15.8.2 spune că această formă rulează propoziția.
LOOKALIKES:
    parenthesesTheServerFolds nu este un plan nou: 10.2.1.1 scoate condiția constantă 1 = 1. FORCE INDEX presupune scanarea foarte scumpă și 10.9.4 îl dă spre înlocuire hint-ului INDEX. Un index cu type ALL și key NULL nu grăbește citirea.
BOOK THEORY:
    Manualul 9.7, capitolul 10, deschis în tur: optimizarea se măsoară la propoziție, la aplicație sau la server. 10.1: primul factor este desenul tabelei, apoi indexul potrivit, motorul (InnoDB este implicit și, pe o bază ocupată, bate adesea MyISAM), formatul de rând, lacătul și memoria. Zonele numite acolo sunt buffer pool-ul InnoDB și key cache-ul MyISAM. 10.2.1: la un SELECT WHERE lent, prima verificare este un index pe coloanele din WHERE; EXPLAIN arată ce index este folosit; o funcție poate fi chemată pentru fiecare rând; nu transforma propoziția doar ca să o faci de necitit, dacă optimizatorul face el transformarea. 10.2.1.1: parantezele inutile și condiția constantă sunt scoase de server; un procent fix nu mai alege scanarea. 10.2.1.23: type ALL este scanarea tabelei, inclusiv când nu există restricție utilizabilă pe o coloană indexată sau când tabela este mică. 10.3.1: fără index, serverul citește de la primul rând; cu index, caută poziția; prefixul din stânga al unui index cu mai multe coloane poate fi folosit; dacă propoziția are nevoie de majoritatea rândurilor, citirea secvențială poate fi mai rapidă. 10.8.2: key NULL înseamnă că nu s-a ales un index; Using index înseamnă că valorile cerute vin din arbore. 15.8.2: EXPLAIN explică cum ar executa serverul; EXPLAIN ANALYZE rulează propoziția și adaugă timpii; forma ANALYZE merge și pe DELETE multi-tabelă. 10.9.4: FORCE INDEX presupune scanarea foarte scumpă; hint-ul INDEX este echivalent și este destinat să îl înlocuiască. query-cache.html pentru 9.7 a răspuns 404. open_books.py nu a găsit niciun PDF.
BOOK EXAMPLE:
    planTheServerWouldUse
REPO DEMO:
    src/main/java/_12_DB_Optimization/Database_Optimization.java
BREAK LAB:
    analyzeDeleteRunsTheStatement ține EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open'. În tur șirul nu a fost trimis.
TRADE-OFFS:
    CREATE INDEX i_status când WHERE este pe status și nu există restricție utilizabilă. Propoziția rămâne când serverul pliază parantezele. Aplicația când filtrul nu este o coloană din shipment.
ANTI-PATTERNS:
    EXPLAIN ANALYZE pe DELETE-ul multi-tabelă, sau FORCE INDEX înainte de plan.
STAFF QUESTIONS:
    planTheServerWouldUse, indexWhenNoUsableRestriction, analyzeDeleteRunsTheStatement, parenthesesTheServerFolds. Întrebările 1–50 din DbOptimization_Staff_Interview.
NOVEL TRANSFER:
    Biletele unui ghișeu: status = 'deschis' pe bilet, nu pe colet. EXPLAIN înainte de index. Nu EXPLAIN ANALYZE pe ștergerea biletelor.
SPACED REVIEW:
    O zi: 21 octombrie 2026, EXPLAIN nu șterge coletul. Șapte zile: 27 octombrie 2026, AND 1 = 1 nu este un index. Treizeci de zile: 19 noiembrie 2026, type ALL cu key NULL nu se repară din query cache.

Definiția unei optimizări (optimization):
       Optimizarea aici este alegerea citită din plan: index utilizabil sau propoziție lăsată în forma pe care serverul o pliază. EXPLAIN nu schimbă rândurile din shipment.

Exemplu:
       DbOptimization_Terms.explainOptimization()
       EXPLAIN pe status = 'open' nu scoate coletul din tabelă.

Definiția unui index (index):
       Indexul este structura prin care serverul poate căuta statusul fără să pornească de la primul rând. Dacă planul nu îl alege, căutarea tot citește tabela.

Exemplu:
       DbOptimization_Terms.explainIndex()
       i_status pe status nu schimbă valorile deja stocate.

Definiția unei propoziții (statement):
       Propoziția este textul pe care EXPLAIN îl explică sau pe care EXPLAIN ANALYZE îl rulează. Parantezele și 1 = 1 nu sunt un alt plan.

Exemplu:
       DbOptimization_Terms.explainStatement()
       status = 'open' AND 1 = 1 rămâne căutarea coletelor deschise.

Definiția unei tabele (table):
       Tabela shipment este locul rândurilor. type ALL înseamnă că serverul o citește pentru combinația de rânduri.

Exemplu:
       DbOptimization_Terms.explainTable()
       SELECT-ul fără WHERE poate citi id, status și shipped_at din toată tabela.

Definiția unei coloane (column):
       Coloana status este valoarea din WHERE pe care un index o poate căuta. shipped_at într-un YEAR() este funcția despre care 10.2.1 spune că poate fi chemată la fiecare rând.

Exemplu:
       DbOptimization_Terms.explainColumn()
       YEAR(shipped_at) = 2026 nu este egalitatea pe status.

1. Enunț: Ce problemă rezolvă citirea planului înainte de un index nou pe shipment?
   Capcană: Orice căutare lentă se repară rescriind WHERE-ul, iar EXPLAIN este doar un comentariu.
   Cheie: planTheServerWouldUse cere planul pentru status = 'open'. Rândurile rămân. Abia planul spune dacă lipsește o restricție utilizabilă, caz în care indexul este i_status, sau dacă rescrii o condiție pe care serverul o pliază.
   Demonstrație: planTheServerWouldUse
2. Enunț: Ce se întâmplă cu coletele deschise la EXPLAIN și la EXPLAIN ANALYZE DELETE s FROM shipment s?
   Capcană: Ambele doar afișează planul, deci niciuna nu poate șterge.
   Cheie: EXPLAIN explică cum ar rula serverul și nu este execuția. 15.8.2: EXPLAIN ANALYZE rulează propoziția. Forma multi-tabelă de DELETE intră în ANALYZE, deci coletele cu status deschis pot dispărea la sfârșitul acelei propoziții. În tur șirul nu a fost trimis.
   Demonstrație: analyzeDeleteRunsTheStatement
3. Enunț: De ce (status = 'open' AND 1 = 1) nu este indexul i_status?
   Capcană: Dacă rescrii parantezele, serverul folosește automat un index nou.
   Cheie: 10.2.1.1 scoate condiția constantă la pregătire. Indexul este CREATE INDEX i_status. Planul cu type ALL și key NULL, când nu există restricție utilizabilă, este copilul de index, nu rescrierea.
   Demonstrație: parenthesesTheServerFolds
4. Enunț: Cum eșuează EXPLAIN ANALYZE pe ștergerea multi-tabelă a coletelor deschise?
   Capcană: ANALYZE este o măsurătoare, deci rândurile rămân până la un DELETE separat.
   Cheie: analyzeDeleteRunsTheStatement rulează DELETE-ul. Simptomul, dacă serverul primește șirul, este lipsa coletelor deschise, nu un rând type ALL. Autentificarea din tur a căzut cu 1045 înainte de orice plan.
   Demonstrație: analyzeDeleteRunsTheStatement
5. Enunț: Când adaugi i_status și când lași propoziția sau treci în aplicație?
   Capcană: FORCE INDEX este primul pas, iar query cache ține rezultatul ca să nu mai citești planul.
   Cheie: i_status când WHERE este egalitate pe status și 10.2.1.23 spune că nu există restricție utilizabilă. Lași AND 1 = 1 nerescrit. Hint-ul INDEX, nu FORCE INDEX, când manualul 10.9.4 spune că FORCE va fi înlocuit. Aplicația când filtrul nu este o coloană din shipment. Query cache nu este în lista din 10.1.
   Demonstrație: indexWhenNoUsableRestriction

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vezi planul coletelor deschise înainte de index -> planTheServerWouldUse -> EXPLAIN SELECT id FROM shipment WHERE status = 'open' -> nu garantează un key, pentru că șirul nu a ajuns la server
    2. Eșecul cel mai mic
           -> DECIDE măsori ștergerea cu ANALYZE -> analyzeDeleteRunsTheStatement -> EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open' -> nu garantează că rândurile au rămas
    3. Granița
           -> Planul este mecanismul MySQL. Șirul din Java nu este execuția. Click-ul din Workbench nu alege indexul. Numele coloanei status, fără să apară în WHERE și în plan, nu este o restricție utilizabilă.
    4. Alternativa
           -> i_status când type ALL vine din lipsa unei restricții pe status. Propoziția pliată când singura diferență este 1 = 1. Aplicația când regula nu este în tabelă.
    5. Anti-patternul
           -> EXPLAIN ANALYZE pe DELETE-ul multi-tabelă, sau FORCE INDEX (i_status) înainte de EXPLAIN. Primul poate șterge. Al doilea presupune scanarea foarte scumpă.
    6. Dovada
           -> planTheServerWouldUse ține EXPLAIN. analyzeDeleteRunsTheStatement ține ANALYZE pe DELETE. root@localhost a primit 1045 (using password: NO).
    7. Interviu
           -> EXPLAIN nu schimbă rândurile. type ALL și key NULL, fără restricție utilizabilă, duc la i_status. AND 1 = 1 nu este index. EXPLAIN ANALYZE pe DELETE multi-tabelă rulează ștergerea.
*/

public class Database_Optimization {

    /*
    🟢 Începător
        Ce este            planul pentru coletele cu status deschis, fără să le șteargă
        De ce e nevoie     fără el, rescrierea WHERE-ului pare primul pas
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT id FROM shipment WHERE status = 'open'
    🔴 Staff
        Unde               type și key se citesc din rezultat; EXPLAIN ANALYZE ar rula propoziția; AND 1 = 1 nu creează i_status
    Pași
        1. Ține EXPLAIN pe egalitatea status = 'open'.
        2. Nu îl trimite: root@localhost a primit 1045.
        3. Nu îl înlocui cu EXPLAIN ANALYZE DELETE.
    */
    // Începător: metoda păstrează planul cerut pentru coletele deschise.
    // Producție: public static String planTheServerWouldUse(). Dacă dispare, fast nu mai are exemplul minim.
    // Expert: Metoda nu deschide JDBC și nu pune FORCE INDEX. EXPLAIN ANALYZE pe DELETE rămâne în cealaltă metodă.
    public static String planTheServerWouldUse() {
        // Începător: serverul arată cum ar căuta coletele deschise, iar coletele rămân în shipment.
        // Producție: EXPLAIN SELECT id FROM shipment WHERE status = 'open'. Dacă linia dispare, rămâne doar comentariul despre hardware.
        // Expert: Nu este EXPLAIN ANALYZE, deci nu este forma care rulează. key NULL ar însemna că i_status nu a fost ales. AND 1 = 1 nu schimbă acest text.
        return "EXPLAIN SELECT id FROM shipment WHERE status = 'open'";
        // Începător: metoda a înapoiat planul, nu un colet șters.
        // Producție: acolada închide planTheServerWouldUse. Șirul nu a plecat către 3306.
        // Expert: Eroarea 1045 nu este type ALL. Ștergerea măsurată stă în analyzeDeleteRunsTheStatement.
    }

    /*
    🟢 Începător
        Ce este            indexul pe status, pus când planul nu are o restricție utilizabilă
        De ce e nevoie     fără el, type ALL pe o tabelă mare rămâne singura poveste
    🟡 Producție
        Cum se scrie       CREATE INDEX i_status ON shipment (status)
    🔴 Staff
        Unde               schema primește i_status; rândurile deja stocate nu se schimbă; planul poate tot să ignore indexul
    Pași
        1. Ține CREATE INDEX pe status.
        2. Nu îl trimite.
        3. Nu îl confunda cu YEAR(shipped_at).
    */
    // Începător: metoda păstrează indexul pe care îl adaugi după ce planul nu are restricție pe status.
    // Producție: public static String indexWhenNoUsableRestriction(). Dacă dispare, copilul de index nu mai este alegerea din părinte.
    // Expert: CREATE INDEX nu este FORCE INDEX și nu pliază 1 = 1. Pe InnoDB, FULLTEXT nu este acest B-tree de pe status.
    public static String indexWhenNoUsableRestriction() {
        // Începător: apare un index pe status, iar coletele deschise rămân cu aceleași valori.
        // Producție: CREATE INDEX i_status ON shipment (status). Dacă linia dispare, nu mai ai structura pe care 10.2.1 o verifică prima.
        // Expert: Fără WHERE pe status, 10.2.1.23 tot poate arăta type ALL. Indexul nu acoperă shipped_at, deci SELECT * nu este Using index doar fiindcă i_status există.
        return "CREATE INDEX i_status ON shipment (status)";
        // Începător: metoda a înapoiat indexul, nu planul.
        // Producție: acolada închide indexWhenNoUsableRestriction. Tabela nu a primit indexul în tur.
        // Expert: 1045 a oprit sesiunea înainte de SHOW INDEX. Hint-ul INDEX(shipment i_status) este alt text, din 10.9.4.
    }

    /*
    🟢 Începător
        Ce este            măsurătoarea care chiar șterge coletele deschise
        De ce e nevoie     fără ea, ANALYZE pare doar un EXPLAIN mai lung
    🟡 Producție
        Cum se scrie       EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open'
    🔴 Staff
        Unde               15.8.2 permite ANALYZE pe DELETE multi-tabelă și spune că rulează propoziția
    Pași
        1. Ține forma cu alias, DELETE s FROM shipment s.
        2. Nu o trimite.
        3. Nu o folosi ca dovadă că EXPLAIN simplu a șters rânduri.
    */
    // Începător: metoda păstrează propoziția care pare un plan și poate goli coletele deschise.
    // Producție: public static String analyzeDeleteRunsTheStatement(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: Nu este un DELETE simplu fără ANALYZE și nu este EXPLAIN fără ANALYZE. În tur nu s-a văzut câte rânduri au plecat.
    public static String analyzeDeleteRunsTheStatement() {
        // Începător: coletele cu status deschis pot dispărea, pentru că măsurătoarea rulează ștergerea.
        // Producție: EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open'. Dacă linia dispare, nu mai ai cel mai mic eșec de lângă EXPLAIN.
        // Expert: Forma este multi-tabelă, singura DELETE pe care 15.8.2 o pune la ANALYZE. Un EXPLAIN fără ANALYZE pe același text nu este această linie. i_status nu oprește ștergerea.
        return "EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open'";
        // Începător: metoda a păstrat ștergerea măsurată, fără să o trimită.
        // Producție: acolada închide analyzeDeleteRunsTheStatement. Niciun rând nu a fost numărat.
        // Expert: 1045 nu este un plan TREE. Parantezele cu 1 = 1 nu șterg.
    }

    /*
    🟢 Începător
        Ce este            aceeași căutare de colete deschise, cu o condiție constantă în plus
        De ce e nevoie     fără ea, rescrierea parantezelor pare că schimbă planul
    🟡 Producție
        Cum se scrie       SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1)
    🔴 Staff
        Unde               10.2.1.1 scoate condiția constantă la pregătire; nu se creează i_status
    Pași
        1. Ține AND 1 = 1 lângă status = 'open'.
        2. Nu o trimite.
        3. Nu o numi CREATE INDEX.
    */
    // Începător: metoda păstrează rescrierea care nu adaugă un index.
    // Producție: public static String parenthesesTheServerFolds(). Dacă dispare, vecinul propoziției rămâne doar în comentariu.
    // Expert: Nu este YEAR(shipped_at). Funcția pe dată este costul de la 10.2.1, nu plierea lui 1 = 1.
    public static String parenthesesTheServerFolds() {
        // Începător: se caută tot coletele deschise, iar 1 = 1 nu scoate și nu adaugă un colet.
        // Producție: SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1). Dacă linia dispare, nu mai ai rescrierea pe care serverul o pliază.
        // Expert: Plierea nu umple key cu i_status. FORCE INDEX nu este în acest text. Query cache nu păstrează rezultatul: pagina lui din 9.7 este 404, iar 10.1 nu îl listează.
        return "SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1)";
        // Începător: metoda a înapoiat căutarea, nu indexul.
        // Producție: acolada închide parenthesesTheServerFolds. Planul nu a fost citit.
        // Expert: type ALL nu se citește din această acoladă. Ștergerea ANALYZE este cealaltă metodă.
    }
}
