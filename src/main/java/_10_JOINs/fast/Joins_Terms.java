/*
TOPIC:
    Termenii alegerii: îmbinarea, referința, ON, coloana și indexul care nu schimbă perechea.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Numele din manual legate de efectul observabil: perechea rămâne, lipsa cade la INNER, indexul nu ține locul lui ON.
MECHANISM:
    Metodele de mai jos țin propoziția gata de trimis. Nu deschid conexiunea. root@localhost a primit 1045, fără parolă.
LOOKALIKES:
    LEFT nu este INNER. USING nu este ON la SELECT *. Hint-ul de index nu este potrivirea.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, numește table reference, ON search_condition, USING, NATURAL și index hint. 10.2.1.9 numește rândul NULL generat la LEFT. Pagina JOIN nu numește un lacăt.
BOOK EXAMPLE:
    explainJoin, explainTableReference, explainOnCondition, explainColumn, explainIndex
REPO DEMO:
    src/main/java/_10_JOINs/JOINs.java
BREAK LAB:
    JOIN fără ON, sau IS NULL după INNER.
TRADE-OFFS:
    LEFT când lipsa rămâne. Aplicația când departamentul nu este pe acest server.
ANTI-PATTERNS:
    A numi indexul „join” și a crede că USING și ON arată aceleași coloane la SELECT *.
STAFF QUESTIONS:
    Metodele explain* de mai jos, plus banca din Joins_Staff_Interview.
NOVEL TRANSFER:
    Coletul, raftul și codul: perechea, referința, condiția, coloana, planul.
SPACED REVIEW:
    O zi: explainJoin. Șapte zile: explainOnCondition. Treizeci de zile: explainIndex.

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
           -> DECIDE numești perechea, nu indexul -> explainJoin -> INNER JOIN ON dept_no -> nu garantează că serverul a executat șirul
    2. Eșecul cel mai mic
           -> DECIDE indexul ține locul lui ON -> explainIndex -> USE INDEX nu filtrează perechea -> nu garantează un alt rezultat
    3. Granița
           -> Propoziția este mecanismul. Metoda Java doar o ține. Workbench nu este termenul.
    4. Alternativa
           -> explainOnCondition când lipsa stă în WHERE, nu în ON. explainTableReference când a doua oară este aceeași tabelă.
    5. Anti-patternul
           -> Să numești USE INDEX o potrivire. Managerul fără departament tot cade la INNER.
    6. Dovada
           -> explainJoin ține INNER. explainColumn ține USING. root@localhost a primit 1045.
    7. Interviu
           -> Îmbinarea alege rândul. Referința este numele din FROM. ON este condiția. USING coalizează coloana. Indexul nu schimbă perechea.
*/
// Începător: glosarul ține propozițiile despre rândul care rămâne, nu doar numele.
// Producție: package _10_JOINs.fast. Metodele întorc șirul gata de trimis.
// Expert: Termenii sunt îmbinarea, referința, ON, coloana și indexul. Nu sunt lecția întreagă a lui UNION.
package _10_JOINs.fast;

// Începător: clasa execută termenii prin propoziția pe care o păstrează.
// Producție: public final class Joins_Terms. Fără metode, definiția nu are exemplu care să țină SQL-ul.
// Expert: Nu deschide JDBC. Nu copiază un demo din _3_LEFT_JOIN ca pe un al doilea vocabular.
public final class Joins_Terms {

    /*
    🟢 Începător
        Ce este            perechea manager-departament
        De ce e nevoie     fără propoziție, „join” rămâne un comentariu
    🟡 Producție
        Cum se scrie       INNER JOIN departments_dup d ON m.dept_no = d.dept_no
    🔴 Staff
        Unde               managerul fără pereche cade din rezultat; LEFT nu este acest șir
    Pași
        1. Ține INNER și ON.
        2. Nu îl trimite.
        3. Nu îl înlocui cu USE INDEX.
    */
    // Începător: metoda păstrează îmbinarea care lasă doar perechea.
    // Producție: public static String explainJoin(). Dacă dispare, definiția nu mai are propoziția.
    // Expert: Nu predă UNION. Nu predă AVG-ul înmulțit. Pagina JOIN nu numește un lacăt ținut după rezultat.
    public static String explainJoin() {
        // Începător: rămân managerul și departamentul doar când dept_no se potrivește. Cel fără departament nu apare.
        // Producție: același INNER ca în matchedManagerAndDepartment. Dacă linia dispare, termenul nu mai are propoziție.
        // Expert: JOIN fără cuvântul INNER, cu același ON, este aceeași pereche în MySQL. Fără ON ar fi produsul. Un index nu ține locul lui ON.
        return "SELECT m.emp_no, d.dept_name FROM dept_manager_dup m INNER JOIN departments_dup d ON m.dept_no = d.dept_no";
        // Începător: metoda a înapoiat perechea, nu un rând șters.
        // Producție: acolada închide explainJoin. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este rezultatul gol. IS NULL după INNER stă în JOINs.java, nu aici.
    }

    /*
    🟢 Începător
        Ce este            două nume în FROM pentru o tabelă, când angajatul și managerul stau împreună
        De ce e nevoie     fără alias, a doua referință nu se poate deosebi
    🟡 Producție
        Cum se scrie       FROM emp_manager e1 JOIN emp_manager e2 ON e1.emp_no = e2.manager_no
    🔴 Staff
        Unde               o tabelă stocată; e1 și e2 nu sunt două copii pe disc
    Pași
        1. Ține două aliasuri.
        2. Nu crea o tabelă.
        3. Nu o trimite.
    */
    // Începător: metoda păstrează două referințe către emp_manager.
    // Producție: public static String explainTableReference(). Dacă dispare, aliasul rămâne un sfat.
    // Expert: Nu este join-ul dintre dept_manager și departments. Acolo sunt două tabele stocate.
    public static String explainTableReference() {
        // Începător: angajatul și managerul se citesc din aceeași tabelă, cu două nume.
        // Producție: JOIN emp_manager e2 ON e1.emp_no = e2.manager_no. Dacă linia dispare, referința nu mai are propoziție.
        // Expert: Fără e2, coloana emp_no nu are o singură referință. GROUP BY emp_no nu creează a doua referință și poate lua ERROR 1055 pe e1.*.
        return "SELECT e1.emp_no, e2.emp_no AS manager_row FROM emp_manager e1 JOIN emp_manager e2 ON e1.emp_no = e2.manager_no";
        // Începător: metoda a înapoiat referințele, nu o tabelă nouă.
        // Producție: acolada închide explainTableReference. Șirul nu a plecat.
        // Expert: Self join-ul până la capăt stă în SELF_JOIN. Aici este doar numele de referință.
    }

    /*
    🟢 Începător
        Ce este            condiția care spune ce pereche rămâne
        De ce e nevoie     fără ON, în MySQL rămâne produsul
    🟡 Producție
        Cum se scrie       ON m.dept_no = d.dept_no
    🔴 Staff
        Unde               ON vede doar operanzii; WHERE IS NULL după INNER nu găsește lipsa
    Pași
        1. Ține egalitatea în ON.
        2. Nu o pune după virgulă pe o tabelă din afară.
        3. Nu o trimite.
    */
    // Începător: metoda păstrează condiția de pe dept_no.
    // Producție: public static String explainOnCondition(). Dacă dispare, ON rămâne un cuvânt.
    // Expert: Unknown column în on clause este virgula amestecată cu JOIN, nu această egalitate.
    public static String explainOnCondition() {
        // Începător: rămâne perechea de dept_no. Managerul fără număr în departments_dup nu rămâne.
        // Producție: INNER JOIN ON m.dept_no = d.dept_no. Dacă linia dispare, condiția nu mai este propoziție.
        // Expert: La LEFT, WHERE nu este folosit ca să citească dreapta. IS NULL în ON păstrează toată stânga. Această linie nu este acel truc.
        return "SELECT m.emp_no, d.dept_name FROM dept_manager_dup m INNER JOIN departments_dup d ON m.dept_no = d.dept_no";
        // Începător: metoda a înapoiat condiția, nu produsul.
        // Producție: acolada închide explainOnCondition. Șirul a rămas în proces.
        // Expert: Aceeași egalitate ca la explainJoin. Deosebirea de termen este că aici întrebarea este condiția, nu rândul păstrat de formă.
    }

    /*
    🟢 Începător
        Ce este            dept_no arătat o dată, pentru că USING îl coalizează
        De ce e nevoie     fără deosebire, ON și USING par același SELECT *
    🟡 Producție
        Cum se scrie       SELECT * FROM dept_manager_dup m JOIN departments_dup d USING (dept_no)
    🔴 Staff
        Unde               15.2.13.2: USING coalizează; ON păstrează ambele coloane; la inner, valorile coincid
    Pași
        1. Ține USING (dept_no).
        2. Nu scrie NATURAL: ar prinde orice nume comun.
        3. Nu o trimite.
    */
    // Începător: metoda păstrează coloana comună, o singură dată.
    // Producție: public static String explainColumn(). Dacă dispare, coalizarea rămâne un tabel din manual, fără șir.
    // Expert: NATURAL ar adăuga și alte coloane cu același nume. Nu este acest USING.
    public static String explainColumn() {
        // Începător: dept_no apare o dată în rezultat, nu și la manager și la departament.
        // Producție: JOIN ... USING (dept_no). Dacă linia dispare, nu mai ai coloana coalizată.
        // Expert: La LEFT, coalizarea ia valoarea nenulă. ON ar fi lăsat d.dept_no NULL și m.dept_no umplut, două coloane. SELECT * nu este același.
        return "SELECT * FROM dept_manager_dup m LEFT JOIN departments_dup d USING (dept_no)";
        // Începător: metoda a înapoiat forma cu o coloană, nu execuția.
        // Producție: acolada închide explainColumn. Șirul nu a plecat.
        // Expert: Nu s-a văzut ordinea coloanelor. Manualul o dă: întâi coloana comună, apoi restul primei tabele, apoi restul celei de a doua.
    }

    /*
    🟢 Începător
        Ce este            un sfat de index care nu alege perechea
        De ce e nevoie     fără el, indexul pare că înlocuiește ON
    🟡 Producție
        Cum se scrie       FROM dept_manager_dup m USE INDEX (dept_no) INNER JOIN departments_dup d ON m.dept_no = d.dept_no
    🔴 Staff
        Unde               gramatica din 15.2.13.2: hint-ul spune optimizerului; potrivirea rămâne ON
    Pași
        1. Ține USE INDEX și ON împreună.
        2. Nu scoate ON.
        3. Nu o trimite.
    */
    // Începător: metoda păstrează sfatul de index lângă aceeași pereche.
    // Producție: public static String explainIndex(). Dacă dispare, indexul pare mecanismul de join.
    // Expert: Pagina de index hints, 10.9.4, nu a fost deschisă. Nu inventez ce face FORCE față de USE. Știu din 15.2.13.2 că hint-ul influențează optimizerul.
    public static String explainIndex() {
        // Începător: managerul fără departament tot nu apare. Indexul nu îl aduce înapoi.
        // Producție: USE INDEX (dept_no) și INNER JOIN ON m.dept_no = d.dept_no. Dacă linia dispare, nu mai ai hint-ul lângă pereche.
        // Expert: Fără index, perechea rămâne perechea, eventual cu alt plan. STRAIGHT_JOIN poate încurca un index pentru că oprește semijoin-ul, cum spune 10.2.1.9. Nu este acest șir. Hint-ul nu ține lacăt după rezultat.
        return "SELECT m.emp_no, d.dept_name FROM dept_manager_dup m USE INDEX (dept_no) INNER JOIN departments_dup d ON m.dept_no = d.dept_no";
        // Începător: metoda a înapoiat sfatul, nu un plan citit.
        // Producție: acolada închide explainIndex. Șirul a rămas în proces.
        // Expert: 1045 nu este un EXPLAIN. Planul nu a fost măsurat în tur.
    }
    // Începător: glosarul se oprește la îmbinare, referință, condiție, coloană și index.
    // Producție: acolada închide Joins_Terms. Nu mai există o metodă care să deschidă JDBC.
    // Expert: UNION și AVG nu sunt termeni aici. Pagina JOIN nu a numit un lacăt de pus pe această acoladă.
}
