/*
TOPIC:
    Termenii alegerii: optimizarea, indexul, propoziția, tabela și coloana pe care planul le vede la shipment.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Clientul local mysql.exe este 9.2.0 și nu este versiunea serverului. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Numele din manual legate de efectul observabil: EXPLAIN nu schimbă rândurile, i_status nu schimbă valorile, 1 = 1 nu este un index.
MECHANISM:
    Metodele de mai jos țin propoziția gata de trimis. Nu deschid conexiunea. root@localhost a primit 1045.
LOOKALIKES:
    Indexul nefolosit nu este optimizarea. EXPLAIN ANALYZE nu este EXPLAIN. YEAR(shipped_at) nu este coloana status.
BOOK THEORY:
    Manualul 9.7, 10.1, numește desenul, indexul, motorul, lacătul și memoria, fără query cache în listă. 10.3.1 numește indexul și citirea de la primul rând. 15.8.2 numește EXPLAIN și EXPLAIN ANALYZE. 10.2.1.1 numește condiția constantă scoasă la pregătire. 10.8.2 numește key NULL și Using index.
BOOK EXAMPLE:
    explainOptimization, explainIndex, explainStatement, explainTable, explainColumn
REPO DEMO:
    src/main/java/_12_DB_Optimization/Database_Optimization.java
BREAK LAB:
    EXPLAIN ANALYZE pe DELETE multi-tabelă, sau un index crezut deja folosit fără coloana key.
TRADE-OFFS:
    i_status când lipsește restricția pe status. Aplicația când filtrul nu este o coloană.
ANTI-PATTERNS:
    A numi AND 1 = 1 index, sau a numi EXPLAIN ANALYZE un plan care nu rulează.
STAFF QUESTIONS:
    Metodele explain* de mai jos, plus banca din DbOptimization_Staff_Interview.
NOVEL TRANSFER:
    Biletul de ghișeu: status deschis, planul înainte de index, funcția pe dată nu este coloana status.
SPACED REVIEW:
    O zi: explainOptimization. Șapte zile: explainIndex. Treizeci de zile: explainStatement.

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

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE numești planul, nu rescrierea -> explainOptimization -> EXPLAIN pe status = 'open' -> nu garantează că serverul a executat șirul
    2. Eșecul cel mai mic
           -> DECIDE 1 = 1 este un index -> explainStatement -> SELECT cu AND 1 = 1 -> nu garantează un key i_status
    3. Granița
           -> Propoziția este mecanismul. Metoda Java doar o ține. Workbench nu este termenul.
    4. Alternativa
           -> explainIndex când lipsește restricția pe status. explainStatement când textul este deja pliat de server.
    5. Anti-patternul
           -> Să numești AND 1 = 1 index. Coletele deschise nu sunt căutate printr-o structură nouă.
    6. Dovada
           -> explainOptimization ține EXPLAIN. explainIndex ține CREATE INDEX. root@localhost a primit 1045.
    7. Interviu
           -> Planul nu schimbă rândurile. Indexul nu schimbă valorile. Tabela este citită la type ALL. Coloana status nu este YEAR(shipped_at).
*/
// Începător: glosarul ține propozițiile despre plan, nu doar numele.
// Producție: package _12_DB_Optimization.fast. Metodele întorc șirul gata de trimis.
// Expert: Termenii sunt optimizarea, indexul, propoziția, tabela și coloana. Nu sunt lecția întreagă a lui FULLTEXT.
package _12_DB_Optimization.fast;

// Începător: clasa execută termenii prin propoziția pe care o păstrează.
// Producție: public final class DbOptimization_Terms. Fără metode, definiția nu are exemplu care să țină SQL-ul.
// Expert: Nu deschide JDBC. Nu copiază un demo din _8_Constraints ca pe un al doilea vocabular.
public final class DbOptimization_Terms {

    /*
    🟢 Începător
        Ce este            planul care alege între index și textul pliat
        De ce e nevoie     fără propoziție, „optimizare” rămâne comentariul despre discuri
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT id FROM shipment WHERE status = 'open'
    🔴 Staff
        Unde               EXPLAIN nu șterge; EXPLAIN ANALYZE pe DELETE multi-tabelă rulează
    Pași
        1. Ține EXPLAIN pe status = 'open'.
        2. Nu îl trimite.
        3. Nu îl înlocui cu ANALYZE pe DELETE.
    */
    // Începător: metoda păstrează planul coletelor deschise.
    // Producție: public static String explainOptimization(). Dacă dispare, definiția nu mai are propoziția.
    // Expert: Nu predă hash join până la capăt. FORMAT=TREE nu este acest șir.
    public static String explainOptimization() {
        // Începător: serverul arată cum ar căuta coletele deschise, iar ele rămân.
        // Producție: EXPLAIN SELECT id FROM shipment WHERE status = 'open'. Dacă linia dispare, termenul nu mai are propoziție.
        // Expert: Nu este EXPLAIN ANALYZE. key NULL ar însemna că i_status nu a fost ales. 1 = 1 nu este în text.
        return "EXPLAIN SELECT id FROM shipment WHERE status = 'open'";
        // Începător: metoda a înapoiat planul, nu un colet șters.
        // Producție: acolada închide explainOptimization. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este type ALL. Indexul stă în explainIndex.
    }

    /*
    🟢 Începător
        Ce este            structura de pe status, fără schimbarea valorilor
        De ce e nevoie     fără ea, planul cu key NULL nu are ce index să numească
    🟡 Producție
        Cum se scrie       CREATE INDEX i_status ON shipment (status)
    🔴 Staff
        Unde               schema se schimbă; rândurile nu; planul poate tot să nu aleagă indexul
    Pași
        1. Ține CREATE INDEX pe status.
        2. Nu îl trimite.
        3. Nu îl numi FULLTEXT.
    */
    // Începător: metoda păstrează indexul pe status.
    // Producție: public static String explainIndex(). Dacă dispare, termenul de index rămâne un nume.
    // Expert: Pe InnoDB, FULLTEXT este listă inversată. Acest INDEX pe status nu este acea listă.
    public static String explainIndex() {
        // Începător: apare i_status, iar statusul stocat rămâne neschimbat.
        // Producție: CREATE INDEX i_status ON shipment (status). Dacă linia dispare, nu mai ai structura din 10.3.1.
        // Expert: Fără WHERE, type ALL poate rămâne. shipped_at nu intră în acest index, deci SELECT * nu este covering doar pe status.
        return "CREATE INDEX i_status ON shipment (status)";
        // Începător: metoda a înapoiat indexul, nu planul.
        // Producție: acolada închide explainIndex. SHOW INDEX nu a rulat.
        // Expert: FORCE INDEX nu creează acest nume. 1045 a oprit sesiunea.
    }

    /*
    🟢 Începător
        Ce este            aceeași căutare, cu o condiție mereu adevărată
        De ce e nevoie     fără ea, rescrierea pare un alt mecanism
    🟡 Producție
        Cum se scrie       SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1)
    🔴 Staff
        Unde               10.2.1.1 scoate condiția constantă; nu se creează i_status
    Pași
        1. Ține AND 1 = 1.
        2. Nu o trimite.
        3. Nu o numi index.
    */
    // Începător: metoda păstrează propoziția pe care serverul o pliază.
    // Producție: public static String explainStatement(). Dacă dispare, termenul de propoziție nu mai are textul pliat.
    // Expert: Nu este YEAR(shipped_at). Funcția pe dată este explainColumn.
    public static String explainStatement() {
        // Începător: se caută tot coletele deschise, iar 1 = 1 nu le schimbă numărul.
        // Producție: SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1). Dacă linia dispare, plierea nu mai are propoziție.
        // Expert: Plierea nu umple key. EXPLAIN ANALYZE DELETE ar șterge și nu este acest SELECT.
        return "SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1)";
        // Începător: metoda a înapoiat textul, nu indexul.
        // Producție: acolada închide explainStatement. Pregătirea nu a rulat.
        // Expert: type ALL nu se citește din acoladă. 1045 nu este un plan.
    }

    /*
    🟢 Începător
        Ce este            citirea tabelei când nu filtrezi statusul
        De ce e nevoie     fără ea, orice index pare că oprește scanarea
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT id, status, shipped_at FROM shipment
    🔴 Staff
        Unde               fără restricție utilizabilă, 10.2.1.23 numește type ALL
    Pași
        1. Ține SELECT-ul fără WHERE.
        2. Nu îl trimite.
        3. Nu îl numi covering.
    */
    // Începător: metoda păstrează citirea întregii tabele.
    // Producție: public static String explainTable(). Dacă dispare, scanarea rămâne un cuvânt.
    // Expert: shipped_at nu este în i_status. Using index nu este semnul acestui text.
    public static String explainTable() {
        // Începător: planul poate citi toate coletele, cu id, status și data expedierii.
        // Producție: EXPLAIN SELECT id, status, shipped_at FROM shipment. Dacă linia dispare, tabela nu mai are propoziția de scanare.
        // Expert: 10.3.1: când se cer majoritatea rândurilor, citirea secvențială poate fi mai rapidă decât indexul. EXPLAIN nu șterge rândurile.
        return "EXPLAIN SELECT id, status, shipped_at FROM shipment";
        // Începător: metoda a înapoiat citirea tabelei, nu căutarea după status.
        // Producție: acolada închide explainTable. type nu a fost observat.
        // Expert: 1045 nu este ALL. CREATE INDEX este explainIndex.
    }

    /*
    🟢 Începător
        Ce este            funcția pe data expedierii, nu egalitatea pe status
        De ce e nevoie     fără ea, orice coloană pare la fel în WHERE
    🟡 Producție
        Cum se scrie       SELECT id FROM shipment WHERE YEAR(shipped_at) = 2026
    🔴 Staff
        Unde               10.2.1: funcția poate fi chemată pe fiecare rând; i_status este pe status
    Pași
        1. Ține YEAR(shipped_at).
        2. Nu o trimite.
        3. Nu o înlocui cu status = 'open'.
    */
    // Începător: metoda păstrează coloana folosită într-o funcție.
    // Producție: public static String explainColumn(). Dacă dispare, shipped_at și status par aceeași coloană.
    // Expert: Pagina deschisă nu spune că un index pe expresie este interzis. Spune că funcția poate rula pe rând.
    public static String explainColumn() {
        // Începător: anul se calculează din shipped_at, nu din status.
        // Producție: SELECT id FROM shipment WHERE YEAR(shipped_at) = 2026. Dacă linia dispare, coloana din funcție nu mai are propoziție.
        // Expert: i_status nu schimbă acest WHERE. AND 1 = 1 nu este YEAR. EXPLAIN pe acest text tot nu șterge colete.
        return "SELECT id FROM shipment WHERE YEAR(shipped_at) = 2026";
        // Începător: metoda a înapoiat funcția de pe coloană, nu indexul.
        // Producție: acolada închide explainColumn. Nu s-a numărat câte apeluri ar fi fost.
        // Expert: FORCE INDEX nu este aici. 1045 a oprit sesiunea înainte de plan.
    }
    // Începător: glosarul se oprește la coloana din funcție.
    // Producție: acolada închide DbOptimization_Terms. Nu mai există o a doua clasă de termeni.
    // Expert: Cine întreabă doar B-tree-ul iese la INDEX_OPTIMIZATION. Cine întreabă doar FORCE INDEX iese la SQL_STATEMENTS_OPTIMIZATION_.
}
