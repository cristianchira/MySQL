/*
TOPIC:
    Alegi între planul coletelor deschise, indexul i_status și rescrierea pe care serverul o pliază.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Clientul local mysql.exe este 9.2.0 și nu este versiunea serverului. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Căutarea status = 'open' pornește de la EXPLAIN. Rândurile din shipment nu se schimbă în această lecție.
MECHANISM:
    main tipărește Database_Optimization.planTheServerWouldUse. Serverul, dacă primește șirul, explică cum ar executa SELECT-ul. Nu îl rulează ca EXPLAIN ANALYZE.
LOOKALIKES:
    AND 1 = 1 nu este i_status. EXPLAIN ANALYZE DELETE s FROM shipment s rulează ștergerea.
BOOK THEORY:
    Manualul 9.7, 15.8.2: EXPLAIN este planul, cum ar executa serverul. 10.2.1: prima verificare la un WHERE lent este indexul, iar EXPLAIN arată ce index este folosit. 10.2.1.1 pliază 1 = 1. 10.3.1: fără index, citirea pornește de la primul rând. open_books.py nu a găsit niciun PDF.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_12_DB_Optimization/Database_Optimization.java
BREAK LAB:
    Database_Optimization.analyzeDeleteRunsTheStatement. main nu o tipărește.
TRADE-OFFS:
    i_status când nu există restricție utilizabilă pe status. Propoziția rămâne când diferența este 1 = 1.
ANTI-PATTERNS:
    EXPLAIN ANALYZE pe DELETE-ul multi-tabelă, sau FORCE INDEX înainte de plan.
STAFF QUESTIONS:
    Întrebările 1–50 din DbOptimization_Staff_Interview.
NOVEL TRANSFER:
    Biletul de ghișeu cu status deschis. EXPLAIN înainte de index. Nu ANALYZE pe ștergere.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

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

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vezi planul înainte de index -> main -> EXPLAIN SELECT id FROM shipment WHERE status = 'open' -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE măsori ștergerea -> Database_Optimization.analyzeDeleteRunsTheStatement -> EXPLAIN ANALYZE DELETE -> nu garantează că rândurile au rămas
    3. Granița
           -> Planul este mecanismul. System.out doar afișează șirul. Workbench nu alege key.
    4. Alternativa
           -> i_status când type ALL vine din lipsa unei restricții pe status. Propoziția pliată când singura diferență este 1 = 1.
    5. Anti-patternul
           -> EXPLAIN ANALYZE pe DELETE multi-tabelă, sau FORCE INDEX înainte de EXPLAIN.
    6. Dovada
           -> main tipărește planTheServerWouldUse. root@localhost a primit 1045 (using password: NO).
    7. Interviu
           -> EXPLAIN nu schimbă rândurile. Fără restricție utilizabilă, următorul pas este i_status. AND 1 = 1 nu este indexul.

🟢 Începător
    Ce este            exemplul minim: planul coletelor deschise
    De ce e nevoie     fără șirul din Database_Optimization, comentariul despre hardware pare regula
🟡 Producție
    Cum se scrie       System.out.println(Database_Optimization.planTheServerWouldUse())
🔴 Staff
    Unde               tipărirea nu este execuția. Vecinul este AND 1 = 1. Efectul pe server rămâne NERULAT în tur.
Pași
    1. Ia propoziția din planTheServerWouldUse.
    2. O tipărește.
    3. Nu tipărește EXPLAIN ANALYZE DELETE.
*/
// Începător: lecția scurtă stă lângă clasa care deja ține planul, în folderul fast.
// Producție: package _12_DB_Optimization.fast. Dacă linia dispare, main nu mai este lângă Database_Optimization.java.
// Expert: Pachetul alege între copii. Nu predă până la capăt B-tree-ul și nici fiecare hint.
package _12_DB_Optimization.fast;

// Începător: clasa pornește o singură alegere, fără să șteargă un colet.
// Producție: public final class DbOptimization_Fast. Fără ea, nu există main pentru exemplul minim.
// Expert: Nu predă fiecare copil. INDEX_OPTIMIZATION și SQL_STATEMENTS_OPTIMIZATION_ rămân folderele lor.
public final class DbOptimization_Fast {
    // Începător: pornește exemplul și scrie planul, nu un rând de colet.
    // Producție: public static void main(String[] args). args nu este o propoziție și nu schimbă serverul.
    // Expert: Nu cheamă analyzeDeleteRunsTheStatement. Nu deschide JDBC. Tipărirea nu ține loc de coloana key.
    public static void main(String[] args) {
        // Începător: pe ecran apare planul pe care serverul l-ar putea citi, iar shipment nu se golește de aici.
        // Producție: System.out.println(Database_Optimization.planTheServerWouldUse()), adică EXPLAIN pe status = 'open'. Fără linie, exemplul minim dispare.
        // Expert: println nu trimite propoziția la 3306. AND 1 = 1 tot nu este i_status. EXPLAIN ANALYZE tot ar rula ștergerea, dar nu este acest apel.
        System.out.println(_12_DB_Optimization.Database_Optimization.planTheServerWouldUse());
        // Începător: exemplul s-a oprit după ce a arătat planul.
        // Producție: acolada închide main. args rămâne nefolosit, pentru că URL-ul nu stă în argumente.
        // Expert: Refuzul 1045 nu este un rezultat al acestei acolade. DELETE-ul măsurat rămâne în cealaltă metodă.
    }
    // Începător: lecția scurtă se oprește la alegerea planului.
    // Producție: acolada închide DbOptimization_Fast. Nu mai există o a doua clasă Fast.
    // Expert: Cine întreabă doar indexul iese la INDEX_OPTIMIZATION. Cine întreabă doar rescrierea iese la SQL_STATEMENTS_OPTIMIZATION_.
}
