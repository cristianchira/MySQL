/*
TOPIC:
    Întrebări despre momentul în care programul de pe salary schimbă valoarea, o vede deja scrisă, sau lasă regula să refuze rândul.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști când rândul rămâne cu 0 și când rămâne cu valoarea scrisă.
MECHANISM:
    Șirul din Triggers.beforeInsertRewritesNegativeSalary. Această clasă nu îl trimite.
LOOKALIKES:
    AFTER cu SET NEW nu schimbă rândul. CHECK refuză rândul. TRUNCATE nu pornește DELETE.
BOOK THEORY:
    Manualul 9.7, 27.4.1: SET NEW într-un AFTER nu are efect. 15.1.27: TRUNCATE nu activează trigger-ul de DELETE. PDF-ul deschis nu conține CREATE TRIGGER.
BOOK EXAMPLE:
    Triggers.beforeInsertRewritesNegativeSalary
REPO DEMO:
    src/main/java/_15_TRIGGERS/Triggers.java
BREAK LAB:
    SET NEW.salary = 0 într-un AFTER INSERT, ținut în afterSetNewLeavesTheWrittenSalary.
TRADE-OFFS:
    CHECK când rândul negativ nu are voie să existe. CALL când programul trebuie cerut.
ANTI-PATTERNS:
    SET NEW într-un AFTER, sau TRUNCATE crezut DELETE.
STAFF QUESTIONS:
    Întrebările 1–50 din Triggers_Staff_Interview. Aici sunt cinci, cu cheia scrisă.
NOVEL TRANSFER:
    Cantitatea negativă rămâne 0, sau recepția cade, sau raftul se schimbă după mișcare.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unui trigger (trigger):
       Trigger-ul este programul legat de o tabelă permanentă, pornit de server la INSERT, UPDATE sau DELETE, o dată pentru fiecare rând.

Exemplu:
       Triggers_Terms.explainTrigger()
       Salariul -50 rămâne 0 dacă BEFORE INSERT face SET NEW.salary = 0.

1. Enunț: Ce problemă rezolvă BEFORE INSERT care face SET NEW.salary = 0?
   Capcană: Rândul cu -50 este respins, deci tabela rămâne fără el.
   Cheie: Rândul rămâne. Valoarea scrisă este 0, pusă înainte de scriere. CHECK ar fi refuzat rândul.
   Demonstrație: Triggers.beforeInsertRewritesNegativeSalary
2. Enunț: Ce rămâne în salary dacă AFTER INSERT face SET NEW.salary = 0 și clientul a trimis -50?
   Capcană: Rămâne 0, fiindcă programul a rulat după INSERT.
   Cheie: Rămâne -50, dacă coloana l-a acceptat. SET NEW într-un AFTER nu are efect.
   Demonstrație: Triggers.afterSetNewLeavesTheWrittenSalary
3. Enunț: De ce CHECK (salary >= 0) nu este acest trigger?
   Capcană: Ambele opresc salariul negativ, deci lasă aceeași tabelă.
   Cheie: CHECK nu lasă rândul. BEFORE lasă rândul cu 0. Trigger-ul nu ține locul lui FOREIGN KEY.
   Demonstrație: Triggers.checkRefusesTheNegativeSalary
4. Enunț: Ce se rupe când TRUNCATE TABLE salary este luat drept DELETE?
   Capcană: Trigger-ul de DELETE pornește pentru fiecare rând golit.
   Cheie: TRUNCATE nu folosește DELETE. Trigger-ul de DELETE nu pornește. OLD nu este văzut.
   Demonstrație: Triggers.truncateDoesNotFireADeleteTrigger
5. Enunț: Când lași mărirea în afara trigger-ului?
   Capcană: Orice mărire trebuie să fie AFTER, ca să nu poată fi uitată.
   Cheie: Când mărirea nu trebuie să pornească la fiecare INSERT. Atunci este CALL. AFTER rămâne când altă tabelă trebuie schimbată după ce rândul a rămas.
   Demonstrație: Triggers.beforeInsertRewritesNegativeSalary

Întrebări adăugate:

*/
// Începător: întrebările scurte stau în acest folder, fără o metodă care să răspundă singură.
// Producție: package _15_TRIGGERS.fast. Corpul clasei rămâne gol.
// Expert: Nu muta aici banca de pe hire_date sau de pe dept_manager. Subiectul este alegerea momentului.
package _15_TRIGGERS.fast;

// Începător: clasa ține întrebările în comentariu și nu trimite nimic la server.
// Producție: public final class Triggers_Questions. Dacă primește o metodă, nu mai este clasa goală cerută la creare.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall și nu acoperă până la capăt corpul de promovare.
public final class Triggers_Questions {
    // Începător: nu există o propoziție în corp, fiindcă răspunsurile stau în comentariu.
    // Producție: acolada închide Triggers_Questions. Sub ea nu se adaugă un main.
    // Expert: beforeInsertRewritesNegativeSalary rămâne în Triggers.java. Golul de aici nu șterge cele cinci întrebări.
}
