/*
TOPIC:
    Alegi regula pe care serverul o verifică la înscriere: perechea unică și cursul care trebuie să existe.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Aceeași înscriere de două ori, sau înscrierea la cursul 404, nu rămâne în course_registration.
MECHANISM:
    Constraints.registrationRulesTheServerEnforces ține CREATE TABLE course și CREATE TABLE course_registration. main doar tipărește șirul. Serverul, dacă îl primește, judecă rândul la sfârșitul propoziției.
LOOKALIKES:
    CREATE INDEX i_grade nu refuză nota repetată. Trigger-ul de dinainte de INSERT rulează cod și nu este FOREIGN KEY.
BOOK THEORY:
    Manualul 9.7, 1.7.3.1: pe InnoDB, încălcarea de PRIMARY KEY, UNIQUE sau FOREIGN KEY dă propoziția înapoi. 1.7.2.3: FOREIGN KEY se verifică imediat, nu la COMMIT. PDF-urile deschise din folder nu spun rollback-ul propoziției. hibernate-primary-keys.pdf spune că cheia primară nu poate fi NULL. NOT_NULL_Constraint.pdf spune că unique key acceptă NULL, iar primary key nu.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_8_Constraints/Constraints.java
BREAK LAB:
    Constraints.insertIgnoreSkipsTheDuplicateGrade. main nu o tipărește.
TRADE-OFFS:
    INDEX pe grade când două note A sunt legale. Aplicația când regula nu poate fi văzută de propoziție.
ANTI-PATTERNS:
    INSERT IGNORE pe perechea repetată, sau un trigger în loc de FOREIGN KEY.
STAFF QUESTIONS:
    Întrebările 1–50 din Constraints_Staff_Interview.
NOVEL TRANSFER:
    Coletul nu se pune de două ori pe același raft, iar raftul trebuie să existe. Culoarea repetată rămâne un index.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei constrângeri (constraint):
       Constrângerea este regula verificată de server la propoziția care scrie rândul. Rândul care o încalcă nu rămâne.

Exemplu:
       Constraints_Terms.explainConstraint()
       Înscrierea la cursul 404 nu rămâne dacă course_id este FOREIGN KEY și 404 lipsește din course.

Definiția unei tabele (table):
       Tabela este locul din schemă unde regula este declarată. INSERT-ul de după este momentul în care rândul este judecat.

Exemplu:
       Constraints_Terms.explainTable()
       course ține părintele. course_registration ține copilul.

Definiția unei coloane (column):
       Coloana este valoarea pe care regula o vede. NOT NULL refuză lipsa pe coloana aceea. DEFAULT o umple doar când INSERT-ul nu o dă.

Exemplu:
       Constraints_Terms.explainColumn()
       grade poate lipsi. student_id nu poate.

Definiția unui index (index):
       Indexul este structura prin care cheia găsește duplicatul. Un index secundar fără UNIQUE nu refuză valoarea repetată.

Exemplu:
       Constraints_Terms.explainIndex()
       Două note A rămân dacă pe grade stă doar un index.

Definiția unei propoziții (statement):
       Efectul se vede la sfârșitul propoziției. Pe InnoDB, încălcarea se dă înapoi cu propoziția. COMMIT-ul nu salvează rândul deja respins.

Exemplu:
       Constraints_Terms.explainStatement()
       INSERT-ul cu 404 nu lasă rândul, iar tranzacția nu așteaptă commit-ul ca să afle refuzul.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE o înscriere o dată, iar cursul trebuie să existe -> main -> șirul cu PRIMARY KEY și FOREIGN KEY -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE IGNORE ca să treacă a doua notă -> Constraints.insertIgnoreSkipsTheDuplicateGrade -> a doua pereche nu rămâne -> nu garantează un avertisment citit în tur
    3. Granița
           -> Regula este mecanismul. System.out doar afișează șirul. Workbench nu este mecanismul.
    4. Alternativa
           -> INDEX când duplicatele sunt legale. Trigger-ul când trebuie cod, nu când trebuie refuzul orfanului.
    5. Anti-patternul
           -> INSERT IGNORE sau CREATE TRIGGER în loc de FOREIGN KEY.
    6. Dovada
           -> main tipărește șirul din registrationRulesTheServerEnforces. root@localhost a fost refuzat.
    7. Interviu
           -> Serverul refuză rândul la sfârșitul propoziției. Perechea este PRIMARY KEY. Cursul lipsă este FOREIGN KEY. Indexul pe notă nu refuză.

🟢 Începător
    Ce este            exemplul minim: regulile care nu lasă înscrierea dublă și nici cursul lipsă
    De ce e nevoie     fără șirul din Constraints.java, comentariul că orice constrângere leagă tabele pare regula
🟡 Producție
    Cum se scrie       System.out.println(Constraints.registrationRulesTheServerEnforces())
🔴 Staff
    Unde               tipărirea nu este execuția. Vecinul este indexul pe grade. Efectul pe server rămâne NERULAT în tur.
Pași
    1. Ia propozițiile din Constraints.registrationRulesTheServerEnforces.
    2. Le tipărește.
    3. Nu deschide o conexiune și nu tipărește INSERT IGNORE.
*/
// Începător: lecția scurtă stă lângă clasa care deja numește regulile, în folderul fast.
// Producție: package _8_Constraints.fast. Dacă linia dispare, main nu mai este lângă Constraints.java.
// Expert: Pachetul nu predă până la capăt PRIMARY KEY, FOREIGN KEY sau indexul. Copiii folderului rămân clasele deja scrise.
package _8_Constraints.fast;

// Începător: clasa pornește o singură alegere, fără să înscrie un student.
// Producție: public final class Constraints_Fast. Fără ea, nu există main pentru exemplul minim.
// Expert: Nu predă fiecare copil. _1_Primary_Key, _2_Foreign_Key și _3_Indexes rămân folderele lor.
public final class Constraints_Fast {
    // Începător: pornește exemplul și scrie regulile, nu un rând de înscriere.
    // Producție: public static void main(String[] args). args nu este o propoziție și nu schimbă serverul.
    // Expert: Nu cheamă insertIgnoreSkipsTheDuplicateGrade. Nu deschide JDBC. Tipărirea nu ține loc de refuzul 1452.
    public static void main(String[] args) {
        // Începător: pe ecran apar propozițiile pe care serverul le-ar putea rula, iar course_registration nu se umple de aici.
        // Producție: System.out.println(Constraints.registrationRulesTheServerEnforces()), adică cele două CREATE. Fără linie, exemplul minim dispare.
        // Expert: println nu trimite propoziția la 3306. Un index pe grade tot nu refuză nota. Un trigger tot nu este FOREIGN KEY.
        System.out.println(_8_Constraints.Constraints.registrationRulesTheServerEnforces());
        // Începător: exemplul s-a oprit după ce a arătat regulile.
        // Producție: acolada închide main. args rămâne nefolosit, pentru că URL-ul nu stă în argumente.
        // Expert: Refuzul root@localhost nu este un rezultat al acestei acolade. INSERT-ul cu 404 rămâne în cealaltă metodă.
    }
    // Începător: lecția scurtă se oprește la alegerea regulii.
    // Producție: acolada închide Constraints_Fast. Nu mai există o a doua clasă Fast.
    // Expert: Cine întreabă doar cheia primară iese la _1_Primary_Key. Cine întreabă doar părintele iese la _2_Foreign_Key.
}
