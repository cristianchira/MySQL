/*
TOPIC:
    Întrebări despre planul coletelor deschise: index utilizabil sau propoziție pe care serverul o pliază.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Clientul local mysql.exe este 9.2.0 și nu este versiunea serverului. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști ce schimbă EXPLAIN, ce schimbă EXPLAIN ANALYZE și ce nu schimbă AND 1 = 1.
MECHANISM:
    Șirul din Database_Optimization.planTheServerWouldUse. Această clasă nu îl trimite.
LOOKALIKES:
    i_status nu este rescrierea cu 1 = 1. EXPLAIN ANALYZE pe DELETE multi-tabelă nu este EXPLAIN.
BOOK THEORY:
    Manualul 9.7, 15.8.2: EXPLAIN explică cum ar executa serverul; EXPLAIN ANALYZE rulează propoziția. 10.2.1.1 scoate condiția constantă. 10.3.1: fără index, citirea pornește de la primul rând. PDF local: open_books.py a raportat NO_LOCAL_PDF.
BOOK EXAMPLE:
    Database_Optimization.planTheServerWouldUse
REPO DEMO:
    src/main/java/_12_DB_Optimization/Database_Optimization.java
BREAK LAB:
    EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open', ținut în analyzeDeleteRunsTheStatement.
TRADE-OFFS:
    i_status când lipsește restricția utilizabilă. Aplicația când filtrul nu este în shipment.
ANTI-PATTERNS:
    EXPLAIN ANALYZE pe ștergere, sau FORCE INDEX înainte de plan.
STAFF QUESTIONS:
    Întrebările 1–50 din DbOptimization_Staff_Interview. Aici sunt cinci, cu cheia scrisă.
NOVEL TRANSFER:
    Biletul deschis la ghișeu: același EXPLAIN, nu aceeași tabelă shipment.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei optimizări (optimization):
       Optimizarea aici este alegerea citită din plan: index utilizabil sau propoziție lăsată în forma pe care serverul o pliază. EXPLAIN nu schimbă rândurile din shipment.

Exemplu:
       DbOptimization_Terms.explainOptimization()
       EXPLAIN pe status = 'open' nu scoate coletul din tabelă.

1. Enunț: Ce problemă rezolvă planul înainte de un index pe shipment.status?
   Capcană: Rescrii WHERE-ul până devine scurt, iar EXPLAIN este opțional.
   Cheie: EXPLAIN pe status = 'open' nu schimbă rândurile și spune dacă lipsește o restricție utilizabilă. Atunci indexul este i_status.
   Demonstrație: Database_Optimization.planTheServerWouldUse
2. Enunț: Ce colete rămân după EXPLAIN și după EXPLAIN ANALYZE DELETE s FROM shipment s?
   Capcană: Ambele doar afișează planul.
   Cheie: EXPLAIN nu rulează căutarea ca o ștergere. EXPLAIN ANALYZE rulează propoziția. Forma multi-tabelă de DELETE poate scoate coletele deschise.
   Demonstrație: Database_Optimization.analyzeDeleteRunsTheStatement
3. Enunț: De ce (status = 'open' AND 1 = 1) nu este i_status?
   Capcană: Parantezele noi creează indexul.
   Cheie: Serverul scoate condiția constantă. CREATE INDEX i_status este structura. Sunt texte diferite.
   Demonstrație: Database_Optimization.parenthesesTheServerFolds
4. Enunț: Ce se rupe dacă măsori ștergerea coletelor deschise cu EXPLAIN ANALYZE?
   Capcană: ANALYZE nu poate șterge, fiindcă are cuvântul EXPLAIN în față.
   Cheie: Propoziția rulează. Coletele cu status deschis pot lipsi la sfârșitul ei. În tur șirul nu a fost trimis: 1045.
   Demonstrație: Database_Optimization.analyzeDeleteRunsTheStatement
5. Enunț: Când pui i_status și când lași textul pliat?
   Capcană: FORCE INDEX este primul pas pentru orice căutare lentă.
   Cheie: i_status când WHERE este egalitate pe status și nu există restricție utilizabilă. Lași AND 1 = 1. FORCE INDEX presupune scanarea foarte scumpă și nu creează indexul.
   Demonstrație: Database_Optimization.indexWhenNoUsableRestriction

Întrebări adăugate:

*/
// Începător: întrebările scurte stau în acest folder, fără o metodă care să răspundă singură.
// Producție: package _12_DB_Optimization.fast. Corpul clasei rămâne gol.
// Expert: Nu muta aici banca lui B-tree sau a fiecărui hint. Subiectul este alegerea din plan.
package _12_DB_Optimization.fast;

// Începător: clasa ține întrebările în comentariu și nu trimite nimic la server.
// Producție: public final class DbOptimization_Questions. Dacă primește o metodă, nu mai este clasa goală cerută la creare.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall și nu acoperă până la capăt indexul spațial.
public final class DbOptimization_Questions {
    // Începător: nu există o propoziție în corp, fiindcă răspunsurile stau în comentariu.
    // Producție: acolada închide DbOptimization_Questions. Sub ea nu se adaugă un main.
    // Expert: planTheServerWouldUse rămâne în Database_Optimization.java. Golul de aici nu șterge cele cinci întrebări.
}
