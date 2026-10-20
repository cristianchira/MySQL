package _12_DB_Optimization.SQL_STATEMENTS_OPTIMIZATION_;

//https://dev.mysql.com/doc/refman/8.0/en/statement-optimization.html
// HISTORICAL: legătura de deasupra este manualul 8.0. Paginile deschise în tur sunt 9.7: 10.2, 10.2.1, 10.2.1.1, 10.2.1.2, 15.8.2.


/*                                              Optimizing SQL Statements


    1. Optimizing SELECT Statements

                   * https://dev.mysql.com/doc/refman/8.0/en/select-optimization.html

    2. Optimizing Subqueries, Derived Tables, View References, and Common Table Expressions

                  * https://dev.mysql.com/doc/refman/8.0/en/subquery-optimization.html

    3. Optimizing INFORMATION_SCHEMA Queries

                 * https://dev.mysql.com/doc/refman/8.0/en/information-schema-optimization.html

    4. Optimizing Performance Schema Queries

                 * https://dev.mysql.com/doc/refman/8.0/en/performance-schema-optimization.html

    5. Optimizing Data Change Statements

                * https://dev.mysql.com/doc/refman/8.0/en/data-change-optimization.html

    6. Optimizing Database Privileges

                * https://dev.mysql.com/doc/refman/8.0/en/permission-optimization.html

    7. Other Optimization Tips

               * https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-optimization-tips.html



 */

/*
TOPIC:
    Propoziția pe shipment se citește din plan înainte să fie rescrisă. Parantezele și 1 = 1 le pliază serverul. O funcție pe shipped_at poate fi chemată la fiecare rând. EXPLAIN nu schimbă rândurile. EXPLAIN ANALYZE le poate schimba dacă propoziția este o ștergere.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Clientul local mysql.exe este 9.2.0 și nu este versiunea serverului. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Căutarea coletelor deschise nu se rescrie ca să pară mai scurtă. După EXPLAIN, rândurile rămân. După FORCE INDEX, optimizatorul primește presupunerea că scanarea este foarte scumpă, nu un index nou.
MECHANISM:
    Serverul, dacă primește explainBeforeRewrite, întoarce planul pentru status = 'open'. parenthesesTheServerFolds este textul cu AND 1 = 1, scos la pregătire după 10.2.1.1. functionWhileReadingRows ține YEAR(shipped_at). forceIndexAssumesScanIsExpensive nu creează i_status.
LOOKALIKES:
    CREATE INDEX i_status este structura, nu această rescriere. type ALL fără restricție utilizabilă se tratează la index, nu prin paranteze. EXPLAIN ANALYZE rulează propoziția; EXPLAIN doar o explică.
BOOK THEORY:
    Manualul 9.7, 10.2, deschis în tur: nucleul aplicației sunt propozițiile SQL. 10.2.1: la un SELECT WHERE lent, prima verificare este indexul; izolezi funcția care poate rula pentru fiecare rând; EXPLAIN arată indexul folosit; nu transforma propoziția doar ca să o faci greu de citit, dacă optimizatorul face el transformarea; ANALYZE TABLE ține statisticile. 10.2.1.1: parantezele inutile, plierea constantelor și scoaterea condiției constante se fac la pregătire; un procent fix nu mai alege între index și scanare. 10.2.1.23: type ALL este scanarea tabelei; FORCE INDEX spune că scanarea este foarte scumpă față de indexul numit. 10.9.4: FORCE INDEX, USE INDEX și IGNORE INDEX sunt așteptate să fie înlocuite de hint-urile INDEX și NO_INDEX; USE INDEX () înseamnă să nu folosești indexuri; hint-ul cere numele indexului, nu al coloanei. 15.8.2: EXPLAIN este planul, cum ar executa serverul; EXPLAIN ANALYZE rulează propoziția. 10.1 nu listează query cache. open_books.py nu a găsit niciun PDF aici.
BOOK EXAMPLE:
    explainBeforeRewrite
REPO DEMO:
    src/main/java/_12_DB_Optimization/SQL_STATEMENTS_OPTIMIZATION_/SQL_STATEMENTS_OPTIMIZATION_.java
BREAK LAB:
    forceIndexAssumesScanIsExpensive ține FORCE INDEX (i_status) înainte de un plan citit. În tur șirul nu a fost trimis.
TRADE-OFFS:
    i_status când WHERE este egalitate pe status și nu există restricție utilizabilă. Hint-ul INDEX când vrei forma pe care 10.9.4 o lasă în locul lui FORCE INDEX. Aplicația când filtrul nu este în propoziție.
ANTI-PATTERNS:
    FORCE INDEX înainte de EXPLAIN, sau rescrierea lui AND 1 = 1 crezând că apare un index.
STAFF QUESTIONS:
    explainBeforeRewrite, parenthesesTheServerFolds, functionWhileReadingRows, forceIndexAssumesScanIsExpensive. Banca de 50 este la părintele DbOptimization_Staff_Interview. Acest copil nu are al doilea folder fast.
NOVEL TRANSFER:
    Programările unei clinici: status = 'deschis' nu se rescrie cu AND 1 = 1. YEAR(data_programare) rămâne funcția de pe rând, nu un index pe status.
SPACED REVIEW:
    O zi: 21 octombrie 2026, EXPLAIN nu rescrie WHERE-ul. Șapte zile: 27 octombrie 2026, 1 = 1 nu este i_status. Treizeci de zile: 19 noiembrie 2026, FORCE INDEX nu creează indexul.

Definiția unei propoziții (statement):
       Propoziția este textul SELECT sau EXPLAIN pe care serverul îl pregătește. Rescrierea parantezelor nu este un index nou.

Exemplu:
       SQL_STATEMENTS_OPTIMIZATION_.explainBeforeRewrite()
       EXPLAIN pe status = 'open' nu schimbă coletele.

Definiția unui index (index):
       Indexul i_status este vecinul. FORCE INDEX îl numește. Nu îl creează.

Exemplu:
       SQL_STATEMENTS_OPTIMIZATION_.forceIndexAssumesScanIsExpensive()
       FORCE INDEX (i_status) presupune scanarea scumpă și cere ca i_status să existe deja.

Definiția unei coloane (column):
       Coloana status stă în egalitate. shipped_at stă în YEAR(), funcția care poate fi chemată la fiecare rând.

Exemplu:
       SQL_STATEMENTS_OPTIMIZATION_.functionWhileReadingRows()
       YEAR(shipped_at) = 2026 nu este status = 'open'.

Definiția unei tabele (table):
       Tabela shipment este sursa rândurilor. type ALL înseamnă că propoziția o citește, nu că ai rescris-o.

Exemplu:
       SQL_STATEMENTS_OPTIMIZATION_.explainBeforeRewrite()
       Planul spune dacă tabela este citită sau dacă un index este ales.

1. Enunț: Ce problemă rezolvă EXPLAIN înainte de a rescrie căutarea coletelor deschise?
   Capcană: Propoziția mai scurtă este mereu mai rapidă, deci EXPLAIN poate aștepta.
   Cheie: explainBeforeRewrite cere planul pentru status = 'open'. 10.2.1 spune să nu transformi textul doar de dragul formei, dacă optimizatorul pliază el. Rândurile nu se schimbă la EXPLAIN.
   Demonstrație: explainBeforeRewrite
2. Enunț: Ce rămâne în shipment după (status = 'open' AND 1 = 1) și cine schimbă textul?
   Capcană: 1 = 1 șterge coletele închise și creează i_status.
   Cheie: parenthesesTheServerFolds caută tot status deschis. 10.2.1.1 scoate condiția constantă la pregătire. Niciun rând nu este filtrat de 1 = 1. Indexul nu apare din această propoziție.
   Demonstrație: parenthesesTheServerFolds
3. Enunț: De ce YEAR(shipped_at) = 2026 nu este CREATE INDEX i_status?
   Capcană: Orice funcție pe o coloană construiește indexul pe status.
   Cheie: functionWhileReadingRows este costul pe care 10.2.1 îl izolează: funcția poate fi chemată la fiecare rând. i_status este pe status. Cele două texte nu se înlocuiesc.
   Demonstrație: functionWhileReadingRows
4. Enunț: Cum eșuează FORCE INDEX (i_status) pus înainte de plan?
   Capcană: FORCE creează indexul și îl impune, iar scanarea nu mai este posibilă.
   Cheie: forceIndexAssumesScanIsExpensive nu este CREATE INDEX. 10.9.4: scanarea este presupusă foarte scumpă și se folosește doar dacă indexul numit nu poate găsi rândurile. Hint-ul INDEX este forma pe care manualul o lasă în loc. În tur nu s-a văzut un plan: 1045.
   Demonstrație: forceIndexAssumesScanIsExpensive
5. Enunț: Când lași textul pliat și când treci la i_status sau în aplicație?
   Capcană: Orice WHERE lent primește FORCE INDEX, iar query cache păstrează rezultatul.
   Cheie: Lași AND 1 = 1. Treci la CREATE INDEX i_status când 10.2.1.23 arată type ALL fiindcă nu există restricție utilizabilă pe status. Aplicația când filtrul nu este o coloană din shipment. 10.1 nu listează query cache, iar pagina lui din 9.7 este 404.
   Demonstrație: explainBeforeRewrite

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE citești planul înainte de rescriere -> explainBeforeRewrite -> EXPLAIN SELECT id FROM shipment WHERE status = 'open' -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE impui i_status fără plan -> forceIndexAssumesScanIsExpensive -> FORCE INDEX (i_status) -> nu garantează că indexul există și nu creează indexul
    3. Granița
           -> Propoziția este mecanismul MySQL. Șirul Java nu o execută. Workbench nu pliază 1 = 1 în locul serverului. Numele metodei nu este un hint.
    4. Alternativa
           -> i_status când lipsește restricția utilizabilă pe status. Hint-ul INDEX când înlocuiești FORCE INDEX. Aplicația când regula nu este în SQL.
    5. Anti-patternul
           -> FORCE INDEX înainte de EXPLAIN, sau rescrierea lui AND 1 = 1. Primul presupune scanarea scumpă. A doua nu creează indexul.
    6. Dovada
           -> explainBeforeRewrite ține EXPLAIN. parenthesesTheServerFolds ține AND 1 = 1. root@localhost a primit 1045 (using password: NO).
    7. Interviu
           -> EXPLAIN nu schimbă rândurile. 1 = 1 este pliat de server. YEAR(shipped_at) poate rula pe rând. FORCE INDEX nu este CREATE INDEX.
*/

public class SQL_STATEMENTS_OPTIMIZATION_ {

    /*
    🟢 Începător
        Ce este            planul căutării, citit înainte de orice rescriere
        De ce e nevoie     fără el, textul mai scurt pare mai rapid
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT id FROM shipment WHERE status = 'open'
    🔴 Staff
        Unde               planul nu este CREATE INDEX; EXPLAIN ANALYZE ar rula propoziția
    Pași
        1. Ține EXPLAIN pe status = 'open'.
        2. Nu îl trimite.
        3. Nu pune FORCE INDEX în același șir.
    */
    // Începător: metoda păstrează planul cerut înainte de rescriere.
    // Producție: public static String explainBeforeRewrite(). Dacă dispare, copilul de propoziție nu mai are demonstrația.
    // Expert: Nu deschide JDBC. Nu este EXPLAIN ANALYZE DELETE.
    public static String explainBeforeRewrite() {
        // Începător: vezi cum ar fi căutate coletele deschise, iar ele rămân în tabelă.
        // Producție: EXPLAIN SELECT id FROM shipment WHERE status = 'open'. Dacă linia dispare, rămâne doar cuprinsul din comentariu.
        // Expert: 10.2.1 spune că EXPLAIN arată indexul folosit. key NULL nu se repară ștergând paranteze. i_status nu este creat de această linie.
        return "EXPLAIN SELECT id FROM shipment WHERE status = 'open'";
        // Începător: metoda a înapoiat planul, nu o rescriere.
        // Producție: acolada închide explainBeforeRewrite. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este type ALL. Funcția YEAR stă în cealaltă metodă.
    }

    /*
    🟢 Începător
        Ce este            aceeași căutare, cu o condiție care este mereu adevărată
        De ce e nevoie     fără ea, rescrierea pare că schimbă coletele găsite
    🟡 Producție
        Cum se scrie       SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1)
    🔴 Staff
        Unde               10.2.1.1 scoate condiția constantă la pregătire; nu apare un index
    Pași
        1. Ține AND 1 = 1.
        2. Nu o trimite.
        3. Nu o numi CREATE INDEX.
    */
    // Începător: metoda păstrează rescrierea pe care serverul o poate plia.
    // Producție: public static String parenthesesTheServerFolds(). Dacă dispare, vecinul indexului nu mai are text.
    // Expert: Nu este YEAR(shipped_at). Nu este FORCE INDEX.
    public static String parenthesesTheServerFolds() {
        // Începător: se caută tot coletele deschise, iar 1 = 1 nu scoate un colet închis.
        // Producție: SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1). Dacă linia dispare, plierea rămâne doar în manual.
        // Expert: Scoaterea condiției constante nu umple coloana key. i_status se creează cu CREATE INDEX, în celălalt copil.
        return "SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1)";
        // Începător: metoda a înapoiat textul pliat, nu indexul.
        // Producție: acolada închide parenthesesTheServerFolds. Pregătirea nu a rulat în tur.
        // Expert: type ALL nu se citește de aici. 1045 a oprit sesiunea.
    }

    /*
    🟢 Începător
        Ce este            funcția pe data expedierii, care poate fi calculată pentru fiecare colet
        De ce e nevoie     fără ea, orice funcție pare un index
    🟡 Producție
        Cum se scrie       SELECT id FROM shipment WHERE YEAR(shipped_at) = 2026
    🔴 Staff
        Unde               10.2.1: funcția poate fi chemată o dată pe rând; i_status este pe status, nu pe acest text
    Pași
        1. Ține YEAR pe shipped_at.
        2. Nu o trimite.
        3. Nu o înlocui cu status = 'open'.
    */
    // Începător: metoda păstrează funcția de pe data expedierii.
    // Producție: public static String functionWhileReadingRows(). Dacă dispare, costul pe rând rămâne o frază.
    // Expert: Nu afirmă din această linie că un index pe shipped_at este imposibil: pagina deschisă spune că funcția poate rula pe rând, nu că orice index este interzis.
    public static String functionWhileReadingRows() {
        // Începător: anul expedierii este calculat pe shipped_at, nu este egalitatea pe status.
        // Producție: SELECT id FROM shipment WHERE YEAR(shipped_at) = 2026. Dacă linia dispare, nu mai ai funcția izolată de 10.2.1.
        // Expert: i_status nu schimbă acest WHERE. AND 1 = 1 nu este YEAR. EXPLAIN pe această propoziție tot nu șterge rânduri.
        return "SELECT id FROM shipment WHERE YEAR(shipped_at) = 2026";
        // Începător: metoda a înapoiat funcția, nu indexul pe status.
        // Producție: acolada închide functionWhileReadingRows. Nu s-a numărat de câte ori ar fi fost chemată.
        // Expert: 1045 nu este un plan pe YEAR. FORCE INDEX nu este aici.
    }

    /*
    🟢 Începător
        Ce este            porunca care presupune că citirea tabelei este foarte scumpă
        De ce e nevoie     fără ea, FORCE INDEX pare că creează i_status
    🟡 Producție
        Cum se scrie       SELECT id FROM shipment FORCE INDEX (i_status) WHERE status = 'open'
    🔴 Staff
        Unde               10.9.4: scanarea se folosește doar dacă indexul numit nu poate găsi rândurile; hint-ul INDEX este înlocuitorul așteptat
    Pași
        1. Ține FORCE INDEX cu numele i_status, nu cu numele coloanei.
        2. Nu o trimite.
        3. Nu o numi CREATE INDEX.
    */
    // Începător: metoda păstrează hint-ul care nu creează indexul.
    // Producție: public static String forceIndexAssumesScanIsExpensive(). Dacă dispare, anti-patternul nu mai este în cod.
    // Expert: Nu este /*+ INDEX(shipment i_status) */. Acel hint este vecinul pe care 10.9.4 îl lasă în loc.
    public static String forceIndexAssumesScanIsExpensive() {
        // Începător: căutarea coletelor deschise cere indexul i_status și nu îl creează.
        // Producție: SELECT id FROM shipment FORCE INDEX (i_status) WHERE status = 'open'. Dacă linia dispare, presupunerea despre scanare rămâne doar în manual.
        // Expert: USE INDEX () ar însemna niciun index, alt text. EXPLAIN ANALYZE DELETE ar șterge. Acest SELECT nu șterge, dar nici nu înlocuiește planul citit.
        return "SELECT id FROM shipment FORCE INDEX (i_status) WHERE status = 'open'";
        // Începător: metoda a înapoiat hint-ul, nu un index nou în schemă.
        // Producție: acolada închide forceIndexAssumesScanIsExpensive. Planul nu a fost citit.
        // Expert: 1045 nu dovedește că scanarea a fost evitată. i_status tot se creează în copilul de index.
    }
}
