/*
TOPIC:
    Alegi îmbinarea după rândul care trebuie să rămână: perechea manager-departament, nu lipsa și nu produsul.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Managerul fără departments_dup nu rămâne în lista de perechi.
MECHANISM:
    Joins.matchedManagerAndDepartment ține INNER JOIN ON m.dept_no = d.dept_no. main doar tipărește șirul. Serverul, dacă îl primește, taie rândul fără pereche la sfârșitul SELECT-ului.
LOOKALIKES:
    LEFT JOIN l-ar păstra, cu NULL. WHERE d.dept_no IS NULL după INNER nu aduce lipsa. UNION lipește blocuri.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2: fără pereche, INNER nu generează rândul NULL pe care LEFT îl generează. 10.2.1.9: WHERE fals pe acel NULL transformă LEFT în inner. Joins.pdf, paginile deschise, are doar titlul.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_10_JOINs/JOINs.java
BREAK LAB:
    Joins.innerCannotListTheMissingDepartment. main nu o tipărește.
TRADE-OFFS:
    LEFT când lipsa trebuie să apară. UNION ALL când lipești două liste. Aplicația când departamentul nu este pe acest server.
ANTI-PATTERNS:
    INNER JOIN și apoi WHERE d.dept_no IS NULL.
STAFF QUESTIONS:
    Întrebările 1–50 din Joins_Staff_Interview.
NOVEL TRANSFER:
    Coletul care are raft este perechea. Coletul fără raft este LEFT. Matricea colet-raft este CROSS.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei îmbinări (join):
       Îmbinarea păstrează, din două referințe, rândurile pe care le cere forma scrisă: perechea, stânga cu NULL, sau produsul.

Exemplu:
       Joins_Terms.explainJoin()
       Managerul al cărui dept_no nu este în departments_dup nu apare după INNER JOIN.

Definiția unei referințe de tabelă (table reference):
       Referința este numele din FROM, cu alias sau fără. Două aliasuri pot numi aceeași tabelă stocată.

Exemplu:
       Joins_Terms.explainTableReference()
       dept_manager_dup m și departments_dup d sunt două referințe.

Definiția unei condiții de îmbinare (ON):
       ON este search_condition pe operanzii îmbinării. WHERE de după nu caută în dreapta la LEFT.

Exemplu:
       Joins_Terms.explainOnCondition()
       ON m.dept_no = d.dept_no ține perechea.

Definiția unei coloane (column):
       Coloana din USING apare o dată. Coloana din ON apare din fiecare referință.

Exemplu:
       Joins_Terms.explainColumn()
       dept_no în USING este una. În ON sunt două.

Definiția unui index (index hint):
       Indexul poate schimba planul. Nu schimbă rândul care se potrivește.

Exemplu:
       Joins_Terms.explainIndex()
       Fără index pe dept_no, perechea tot este pereche.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE perechea manager-departament -> main -> șirul cu INNER JOIN ON dept_no -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE IS NULL după INNER -> Joins.innerCannotListTheMissingDepartment -> managerul fără departament nu apare -> nu garantează un rând NULL
    3. Granița
           -> Potrivirea este mecanismul. System.out doar afișează șirul. Workbench nu este mecanismul.
    4. Alternativa
           -> LEFT când lipsa rămâne. UNION ALL când lipești blocuri. Subcererea când nu îți trebuie dept_name.
    5. Anti-patternul
           -> INNER JOIN și WHERE d.dept_no IS NULL.
    6. Dovada
           -> main tipărește matchedManagerAndDepartment. root@localhost a primit 1045, fără parolă.
    7. Interviu
           -> INNER păstrează perechea. LEFT păstrează managerul cu NULL. UNION nu este join.

🟢 Începător
    Ce este            exemplul minim: doar managerul care are departament
    De ce e nevoie     fără șirul din JOINs.java, orice JOIN pare că aduce și lipsa
🟡 Producție
    Cum se scrie       System.out.println(Joins.matchedManagerAndDepartment())
🔴 Staff
    Unde               tipărirea nu este execuția. Vecinul este LEFT. Efectul pe server rămâne NERULAT în tur.
Pași
    1. Ia propoziția din Joins.matchedManagerAndDepartment.
    2. O tipărește.
    3. Nu deschide o conexiune și nu tipărește WHERE IS NULL.
*/
// Începător: lecția scurtă stă lângă clasa care alege perechea, în folderul fast.
// Producție: package _10_JOINs.fast. Dacă linia dispare, main nu mai este lângă JOINs.java.
// Expert: Pachetul nu predă până la capăt LEFT, CROSS, UNION sau agregarea. Copiii rămân clasele deja scrise.
package _10_JOINs.fast;

// Începător: clasa pornește o singură alegere, fără să șteargă un manager din tabelă.
// Producție: public final class Joins_Fast. Fără ea, nu există main pentru exemplul minim.
// Expert: Nu predă fiecare copil. _1_INNER_JOIN și _3_LEFT_JOIN rămân folderele lor.
public final class Joins_Fast {
    // Începător: pornește exemplul și scrie perechea, nu lipsa.
    // Producție: public static void main(String[] args). args nu este o propoziție și nu schimbă serverul.
    // Expert: Nu cheamă innerCannotListTheMissingDepartment. Nu deschide JDBC. Tipărirea nu ține loc de rezultat gol.
    public static void main(String[] args) {
        // Începător: pe ecran apare propoziția pe care serverul ar putea-o rula, iar dept_manager_dup nu se golește de aici.
        // Producție: System.out.println(Joins.matchedManagerAndDepartment()), adică INNER JOIN pe dept_no. Fără linie, exemplul minim dispare.
        // Expert: println nu trimite propoziția la 3306. LEFT tot ar fi păstrat managerul. Un index pe dept_no tot nu schimbă perechea.
        System.out.println(_10_JOINs.JOINs.matchedManagerAndDepartment());
        // Începător: exemplul s-a oprit după ce a arătat perechea.
        // Producție: acolada închide main. args rămâne nefolosit, pentru că URL-ul nu stă în argumente.
        // Expert: 1045 nu este un rezultat al acestei acolade. WHERE IS NULL rămâne în cealaltă metodă.
    }
    // Începător: lecția scurtă se oprește la alegerea perechii.
    // Producție: acolada închide Joins_Fast. Nu mai există o a doua clasă Fast.
    // Expert: Cine întreabă doar lipsa iese la _3_LEFT_JOIN. Cine întreabă produsul iese la _4_CROSS_JOIN.
}
