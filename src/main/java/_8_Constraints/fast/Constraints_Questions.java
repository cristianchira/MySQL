/*
TOPIC:
    Întrebări despre regula pe care o alegi ca să nu rămână înscrierea dublă sau cursul lipsă.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști ce refuză serverul și ce lasă un index sau un trigger.
MECHANISM:
    Șirul din Constraints.registrationRulesTheServerEnforces. Această clasă nu îl trimite.
LOOKALIKES:
    Indexul pe grade nu refuză. Trigger-ul rulează cod. FOREIGN KEY refuză părintele lipsă.
BOOK THEORY:
    Manualul 9.7, 1.7.3.1: pe InnoDB propoziția care încalcă cheia se dă înapoi. 1.7.2.3: verificarea FOREIGN KEY nu așteaptă COMMIT. PDF-urile deschise nu conțin INSERT-ul cu 404.
BOOK EXAMPLE:
    Constraints.registrationRulesTheServerEnforces
REPO DEMO:
    src/main/java/_8_Constraints/Constraints.java
BREAK LAB:
    INSERT IGNORE cu aceeași pereche de două ori, ținut în insertIgnoreSkipsTheDuplicateGrade.
TRADE-OFFS:
    INDEX când notele egale sunt legale. Aplicația când propoziția nu vede regula.
ANTI-PATTERNS:
    INSERT IGNORE, sau un trigger în loc de FOREIGN KEY.
STAFF QUESTIONS:
    Întrebările 1–50 din Constraints_Staff_Interview. Aici sunt cinci, cu cheia scrisă.
NOVEL TRANSFER:
    Coletul de două ori pe același raft, sau pe un raft care nu există.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei constrângeri (constraint):
       Constrângerea este regula verificată de server la propoziția care scrie rândul. Rândul care o încalcă nu rămâne.

Exemplu:
       Constraints_Terms.explainConstraint()
       Înscrierea la cursul 404 nu rămâne dacă course_id este FOREIGN KEY și 404 lipsește din course.

1. Enunț: Ce problemă rezolvă alegerea regulii pe course_registration?
   Capcană: Verificarea din aplicație ajunge înaintea lui INSERT, deci a doua înscriere nu poate fi scrisă.
   Cheie: Doi clienți pot trece de aplicație. PRIMARY KEY pe (student_id, course_id) și FOREIGN KEY spre course sunt regulile. Rândul respins nu rămâne.
   Demonstrație: Constraints.registrationRulesTheServerEnforces
2. Enunț: Ce rămâne în tabelă după INSERT-ul cu course_id 404, dacă 404 nu este în course?
   Capcană: Rândul așteaptă COMMIT, iar abia atunci se verifică părintele.
   Cheie: Pe InnoDB propoziția se dă înapoi la sfârșitul ei. Rândul nu rămâne. Verificarea nu este amânată.
   Demonstrație: Constraints.childCourseTheForeignKeyRefuses
3. Enunț: De ce un index pe course_id nu este FOREIGN KEY?
   Capcană: Indexul găsește cursul, deci cursul lipsă nu se poate scrie.
   Cheie: Indexul grăbește căutarea. Nu refuză 404. Refuzul este CONSTRAINT FOREIGN KEY.
   Demonstrație: Constraints.secondaryIndexKeepsBothGrades
4. Enunț: Ce se rupe la INSERT IGNORE cu notele A și B pe aceeași pereche?
   Capcană: Ambele note rămân, fiindcă IGNORE împacă valorile.
   Cheie: A doua pereche este sărită. Prima notă rămâne. Nu este eroarea 1452.
   Demonstrație: Constraints.insertIgnoreSkipsTheDuplicateGrade
5. Enunț: Când lași căutarea pe notă în afara cheii?
   Capcană: Orice coloană căutată trebuie să fie PRIMARY KEY.
   Cheie: Când două înscrieri pot avea aceeași notă. INDEX pe grade nu refuză. UNIQUE este alegerea când duplicatul notei trebuie să cadă și cheia primară este deja perechea.
   Demonstrație: Constraints.secondaryIndexKeepsBothGrades

Întrebări adăugate:

*/
// Începător: întrebările scurte stau în acest folder, fără o metodă care să răspundă singură.
// Producție: package _8_Constraints.fast. Corpul clasei rămâne gol.
// Expert: Nu muta aici banca lui PRIMARY KEY sau a indexului. Subiectul este alegerea regulii.
package _8_Constraints.fast;

// Începător: clasa ține întrebările în comentariu și nu trimite nimic la server.
// Producție: public final class Constraints_Questions. Dacă primește o metodă, nu mai este clasa goală cerută la creare.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall și nu acoperă până la capăt AUTO_INCREMENT.
public final class Constraints_Questions {
    // Începător: nu există o propoziție în corp, fiindcă răspunsurile stau în comentariu.
    // Producție: acolada închide Constraints_Questions. Sub ea nu se adaugă un main.
    // Expert: registrationRulesTheServerEnforces rămâne în Constraints.java. Golul de aici nu șterge cele cinci întrebări.
}
