/*
TOPIC:
    Alegi momentul: BEFORE când salariul negativ trebuie să rămână ca 0, AFTER când rândul este deja scris și SET NEW nu mai schimbă nimic.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Salariul negativ rămâne în salary, cu 0, dacă programul este BEFORE INSERT. AFTER cu același SET lasă valoarea scrisă.
MECHANISM:
    Triggers.beforeInsertRewritesNegativeSalary ține CREATE TRIGGER BEFORE INSERT. main doar tipărește șirul. Serverul, dacă îl primește, schimbă NEW înainte să scrie rândul.
LOOKALIKES:
    AFTER cu SET NEW.salary = 0 nu schimbă rândul deja scris. CHECK (salary >= 0) nu lasă rândul.
BOOK THEORY:
    Manualul 9.7, 27.4.1: SET NEW într-un AFTER nu are efect. 15.1.27: BEFORE se activează înainte de fiecare rând de modificat. PDF-ul deschis spune că programul pornește la INSERT, UPDATE sau DELETE și nu conține CREATE TRIGGER.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_15_TRIGGERS/Triggers.java
BREAK LAB:
    Triggers.afterSetNewLeavesTheWrittenSalary. main nu o tipărește.
TRADE-OFFS:
    CHECK când rândul negativ nu are voie să existe. CALL când mărirea nu trebuie să pornească singură.
ANTI-PATTERNS:
    SET NEW într-un AFTER, sau TRUNCATE crezut că pornește trigger-ul de DELETE.
STAFF QUESTIONS:
    Întrebările 1–50 din Triggers_Staff_Interview.
NOVEL TRANSFER:
    Cantitatea negativă din raft rămâne 0 dacă o schimbi înainte de scriere. Refuzul recepției este CHECK.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unui trigger (trigger):
       Trigger-ul este programul legat de o tabelă permanentă, pornit de server la INSERT, UPDATE sau DELETE, o dată pentru fiecare rând.

Exemplu:
       Triggers_Terms.explainTrigger()
       Salariul -50 rămâne 0 dacă BEFORE INSERT face SET NEW.salary = 0.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE salariul negativ rămâne ca 0 -> main -> șirul BEFORE INSERT -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE AFTER ca să pui 0 după scriere -> Triggers.afterSetNewLeavesTheWrittenSalary -> SET NEW.salary = 0 -> nu garantează că -50 devine 0
    3. Granița
           -> Trigger-ul este mecanismul. System.out doar afișează șirul. Workbench nu este mecanismul.
    4. Alternativa
           -> CHECK când rândul trebuie să cadă. AFTER pe altă tabelă când rândul care pornește programul trebuie să fie deja scris.
    5. Anti-patternul
           -> SET NEW într-un AFTER, sau TRUNCATE în loc de DELETE.
    6. Dovada
           -> main tipărește șirul din beforeInsertRewritesNegativeSalary. root@localhost a primit 1045, using password: NO.
    7. Interviu
           -> BEFORE schimbă valoarea înainte de scriere și rândul rămâne. AFTER nu mai schimbă NEW. CHECK nu lasă rândul.

🟢 Începător
    Ce este            exemplul minim: programul care pune 0 înainte să se scrie salariul negativ
    De ce e nevoie     fără șirul din Triggers.java, comentariul că trigger-ul rulează după ce propoziția s-a executat pare regula
🟡 Producție
    Cum se scrie       System.out.println(Triggers.beforeInsertRewritesNegativeSalary())
🔴 Staff
    Unde               tipărirea nu este execuția. Vecinul este AFTER cu SET NEW, care nu schimbă rândul. Efectul pe server rămâne NERULAT în tur.
Pași
    1. Ia propoziția din Triggers.beforeInsertRewritesNegativeSalary.
    2. O tipărește.
    3. Nu deschide o conexiune și nu tipărește TRUNCATE.
*/
// Începător: lecția scurtă stă lângă clasa care deja numește programul, în folderul fast.
// Producție: package _15_TRIGGERS.fast. Dacă linia dispare, main nu mai este lângă Triggers.java.
// Expert: Pachetul nu predă până la capăt corpul de pe dept_manager și nici hire_date. Copiii rămân BEFORE și AFTER.
package _15_TRIGGERS.fast;

// Începător: clasa pornește o singură alegere, fără să scrie un salariu.
// Producție: public final class Triggers_Fast. Fără ea, nu există main pentru exemplul minim.
// Expert: Nu predă fiecare copil. BEFORE și AFTER rămân folderele lor.
public final class Triggers_Fast {
    // Începător: pornește exemplul și scrie programul, nu un rând de salariu.
    // Producție: public static void main(String[] args). args nu este o propoziție și nu schimbă serverul.
    // Expert: Nu cheamă afterSetNewLeavesTheWrittenSalary. Nu deschide JDBC. Tipărirea nu ține loc de scrierea lui 0.
    public static void main(String[] args) {
        // Începător: pe ecran apare programul pe care serverul l-ar putea lega de salary, iar tabela nu se umple de aici.
        // Producție: System.out.println(_15_TRIGGERS.Triggers.beforeInsertRewritesNegativeSalary()), adică BEFORE INSERT cu SET NEW.salary = 0. Fără linie, exemplul minim dispare.
        // Expert: println nu trimite propoziția la 3306. Un AFTER cu același SET tot nu schimbă rândul deja scris. CHECK tot nu lasă rândul.
        System.out.println(_15_TRIGGERS.Triggers.beforeInsertRewritesNegativeSalary());
        // Începător: exemplul s-a oprit după ce a arătat programul.
        // Producție: acolada închide main. args rămâne nefolosit, pentru că URL-ul nu stă în argumente.
        // Expert: Refuzul 1045 nu este un rezultat al acestei acolade. TRUNCATE rămâne în cealaltă metodă.
    }
    // Începător: lecția scurtă se oprește la alegerea momentului.
    // Producție: acolada închide Triggers_Fast. Nu mai există o a doua clasă Fast.
    // Expert: Cine întreabă doar SET NEW înainte de scriere iese la BEFORE. Cine întreabă doar cealaltă tabelă iese la AFTER.
}
