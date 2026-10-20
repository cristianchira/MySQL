/*
TOPIC:
    Termenii alegerii: regula, tabela, coloana, indexul și propoziția la care se vede refuzul.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Numele din manual legate de efectul observabil: rândul nu rămâne, indexul nu refuză, propoziția se dă înapoi pe InnoDB.
MECHANISM:
    Metodele de mai jos țin propoziția gata de trimis. Nu deschid conexiunea. root@localhost a fost refuzat.
LOOKALIKES:
    Indexul secundar nu este constrângerea. Trigger-ul nu este FOREIGN KEY. DEFAULT nu este NOT NULL.
BOOK THEORY:
    Manualul 9.7, 1.7.3, numește constrângerea și refuzul la execuție. 1.7.3.1 numește propoziția dată înapoi pe InnoDB. 15.1.25.5 numește tabela copil, coloana și lacătul partajat de la FOREIGN KEY. 17.6.1.6 numește indexul de care are nevoie AUTO_INCREMENT.
BOOK EXAMPLE:
    explainConstraint, explainTable, explainColumn, explainIndex, explainStatement
REPO DEMO:
    src/main/java/_8_Constraints/Constraints.java
BREAK LAB:
    INSERT IGNORE pe perechea repetată, sau un index crezut constrângere.
TRADE-OFFS:
    INDEX când duplicatele sunt legale. Aplicația când propoziția nu vede regula.
ANTI-PATTERNS:
    A numi orice index „constrângere” și a crede că trigger-ul ține locul lui FOREIGN KEY.
STAFF QUESTIONS:
    Metodele explain* de mai jos, plus banca din Constraints_Staff_Interview.
NOVEL TRANSFER:
    Raftul, coletul și culoarea: raftul trebuie să existe, coletul o dată pe raft, culoarea poate reveni.
SPACED REVIEW:
    O zi: explainConstraint. Șapte zile: explainIndex. Treizeci de zile: explainStatement.

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
           -> DECIDE numești regula, nu indexul -> explainConstraint -> PRIMARY KEY și FOREIGN KEY -> nu garantează că serverul a executat șirul
    2. Eșecul cel mai mic
           -> DECIDE indexul pe notă refuză duplicatul -> explainIndex -> CREATE INDEX i_grade -> nu garantează că două note A cad
    3. Granița
           -> Propoziția este mecanismul. Metoda Java doar o ține. Workbench nu este termenul.
    4. Alternativa
           -> explainIndex când duplicatele sunt legale. explainConstraint când rândul trebuie să cadă.
    5. Anti-patternul
           -> Să numești i_grade constrângere. Două note A rămân.
    6. Dovada
           -> explainStatement ține INSERT-ul cu 404. explainIndex ține CREATE INDEX. root@localhost a fost refuzat.
    7. Interviu
           -> Regula refuză rândul. Tabela o ține în schemă. Coloana este valoarea văzută. Indexul secundar nu refuză. Efectul se vede la sfârșitul propoziției.
*/
// Începător: glosarul ține propozițiile despre regulă, nu doar numele.
// Producție: package _8_Constraints.fast. Metodele întorc șirul gata de trimis.
// Expert: Termenii sunt constrângerea, tabela, coloana, indexul și propoziția. Nu sunt lecția întreagă a lui AUTO_INCREMENT.
package _8_Constraints.fast;

// Începător: clasa execută termenii prin propoziția pe care o păstrează.
// Producție: public final class Constraints_Terms. Fără metode, definiția nu are exemplu care să țină SQL-ul.
// Expert: Nu deschide JDBC. Nu copiază un demo din _2_Foreign_Key ca pe un al doilea vocabular.
public final class Constraints_Terms {

    /*
    🟢 Începător
        Ce este            regula care nu lasă înscrierea dublă și nici cursul lipsă
        De ce e nevoie     fără propoziție, „regulă” rămâne un comentariu
    🟡 Producție
        Cum se scrie       CREATE TABLE course și CREATE TABLE course_registration cu PRIMARY KEY și FOREIGN KEY
    🔴 Staff
        Unde               refuzul este la sfârșitul propoziției; trigger-ul nu este această regulă
    Pași
        1. Ține cele două CREATE.
        2. Nu le trimite.
        3. Nu le înlocui cu un index pe grade.
    */
    // Începător: metoda păstrează regula înscrierii.
    // Producție: public static String explainConstraint(). Dacă dispare, definiția nu mai are propoziția.
    // Expert: Nu predă CHECK până la capăt. NOT ENFORCED creează expresia fără să o aplice, și nu este acest șir.
    public static String explainConstraint() {
        // Începător: cursul trebuie să existe, iar perechea student-curs nu se repetă.
        // Producție: aceleași CREATE ca în registrationRulesTheServerEnforces. Dacă linia dispare, termenul nu mai are propoziție.
        // Expert: grade NULL nu este NOT NULL. AUTO_INCREMENT nu identifică această înscriere. Indexul grupat este PRIMARY KEY, nu i_grade.
        return "CREATE TABLE course (id INT NOT NULL, PRIMARY KEY (id)) ENGINE=InnoDB; CREATE TABLE course_registration (student_id INT NOT NULL, course_id INT NOT NULL, grade CHAR(1) NULL, PRIMARY KEY (student_id, course_id), CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES course (id)) ENGINE=InnoDB";
        // Începător: metoda a înapoiat regula, nu un rând scris.
        // Producție: acolada închide explainConstraint. Șirul nu a plecat la 3306.
        // Expert: Accesul refuzat nu este eroarea 1452. INSERT-ul stă în explainStatement.
    }

    /*
    🟢 Începător
        Ce este            tabela care ține cursurile, fără înscrieri
        De ce e nevoie     fără ea, FOREIGN KEY nu are unde să arate
    🟡 Producție
        Cum se scrie       CREATE TABLE course (id INT NOT NULL, PRIMARY KEY (id)) ENGINE=InnoDB
    🔴 Staff
        Unde               course este părintele. course_registration este copilul. Un view nu ține aceste rânduri
    Pași
        1. Ține doar CREATE-ul părintelui.
        2. Nu îl trimite.
        3. Nu îl numi constrângerea copilului.
    */
    // Începător: metoda păstrează tabela de cursuri.
    // Producție: public static String explainTable(). Dacă dispare, părintele rămâne un nume.
    // Expert: DROP ar scoate obiectul. Acest CREATE îl lasă în schemă, gol, până la un INSERT care nu este aici.
    public static String explainTable() {
        // Începător: apare tabela course, cu un id care poate fi părinte, și fără niciun student înscris.
        // Producție: CREATE TABLE course (id INT NOT NULL, PRIMARY KEY (id)) ENGINE=InnoDB. Dacă linia dispare, nu mai ai locul din schemă.
        // Expert: ENGINE=InnoDB este motorul care dă propoziția înapoi. MyISAM nu ține FOREIGN KEY. Tabela nu este un view.
        return "CREATE TABLE course (id INT NOT NULL, PRIMARY KEY (id)) ENGINE=InnoDB";
        // Începător: metoda a înapoiat tabela, nu înscrierea.
        // Producție: acolada închide explainTable. Tabela nu a fost creată pe server.
        // Expert: Cheia primară de pe id nu este indexul secundar de pe grade.
    }

    /*
    🟢 Începător
        Ce este            nota, care poate lipsi, spre deosebire de student
        De ce e nevoie     fără deosebire, orice coloană pare NOT NULL
    🟡 Producție
        Cum se scrie       ALTER TABLE course_registration MODIFY grade CHAR(1) NULL
    🔴 Staff
        Unde               NULL pe grade nu umple o valoare. DEFAULT ar umple doar dacă INSERT-ul omite coloana
    Pași
        1. Ține coloana care are voie să lipsească.
        2. Nu o trimite.
        3. Nu o confunda cu student_id, care este NOT NULL în CREATE.
    */
    // Începător: metoda păstrează coloana pe care lipsa este permisă.
    // Producție: public static String explainColumn(). Dacă dispare, NOT NULL și NULL rămân aceeași poveste.
    // Expert: Șirul nu pune DEFAULT. O coloană NOT NULL fără DEFAULT, omisă pe InnoDB în modul strict, oprește propoziția.
    public static String explainColumn() {
        // Începător: nota poate rămâne goală. Studentul din înscriere nu poate.
        // Producție: ALTER TABLE course_registration MODIFY grade CHAR(1) NULL. Dacă linia dispare, nu mai ai coloana care acceptă lipsa.
        // Expert: NULL scris explicit nu este DEFAULT. Șirul gol '' pe un CHAR nu este NULL. 1048 este refuzul de NOT NULL, nu 1452.
        return "ALTER TABLE course_registration MODIFY grade CHAR(1) NULL";
        // Începător: metoda a înapoiat coloana, nu regula întregii tabele.
        // Producție: acolada închide explainColumn. ALTER nu a rulat.
        // Expert: Folderul Not_Null predă refuzul lipsei. Aici coloana este doar ce vede regula.
    }

    /*
    🟢 Începător
        Ce este            calea de căutare pe notă, care lasă două note la fel
        De ce e nevoie     fără ea, orice index pare că apără rândul
    🟡 Producție
        Cum se scrie       CREATE INDEX i_grade ON course_registration (grade)
    🔴 Staff
        Unde               planul poate folosi i_grade; UNIQUE (grade) ar fi regula care refuză a doua notă A
    Pași
        1. Ține indexul fără UNIQUE.
        2. Nu îl trimite.
        3. Nu îl pune în locul lui PRIMARY KEY.
    */
    // Începător: metoda păstrează indexul care nu refuză nota.
    // Producție: public static String explainIndex(). Dacă dispare, vecinul constrângerii dispare.
    // Expert: În InnoDB indexul secundar ține cheia primară. Nu devine el constrângerea.
    public static String explainIndex() {
        // Începător: două înscrieri pot avea aceeași notă, iar căutarea după notă are o cale.
        // Producție: CREATE INDEX i_grade ON course_registration (grade). Dacă linia dispare, nu mai ai propoziția care nu refuză.
        // Expert: EXPLAIN poate arăta i_grade și tot nu schimbă rândurile. PRIMARY KEY (student_id, course_id) este indexul care refuză perechea.
        return "CREATE INDEX i_grade ON course_registration (grade)";
        // Începător: metoda a înapoiat calea de căutare.
        // Producție: acolada închide explainIndex. Indexul nu a fost creat.
        // Expert: Folderul _3_Indexes predă indexul. Aici el este termenul cu care se confundă regula.
    }

    /*
    🟢 Începător
        Ce este            înscrierea la 404, judecată când propoziția se termină
        De ce e nevoie     fără ea, pare că refuzul așteaptă COMMIT
    🟡 Producție
        Cum se scrie       INSERT INTO course_registration (student_id, course_id, grade) VALUES (1, 404, 'A')
    🔴 Staff
        Unde               pe InnoDB propoziția se dă înapoi; lacătul partajat este doar pe durata verificării
    Pași
        1. Ține INSERT-ul cu 404.
        2. Nu îl trimite.
        3. Nu îl amâna până la COMMIT în explicație.
    */
    // Începător: metoda păstrează propoziția la care se vede refuzul.
    // Producție: public static String explainStatement(). Dacă dispare, momentul rămâne o poveste.
    // Expert: FOREIGN KEY nu este amânat până la commit. Un motor netranzacțional s-ar opri la rândul greșit și ar lăsa rândurile dinaintea lui.
    public static String explainStatement() {
        // Începător: înscrierea la cursul 404 nu rămâne dacă părintele lipsește.
        // Producție: INSERT INTO course_registration (student_id, course_id, grade) VALUES (1, 404, 'A'). Dacă linia dispare, nu mai ai momentul refuzului.
        // Expert: 1452 este părintele lipsă. 1062 este perechea repetată. INSERT IGNORE nu este acest șir. Lacătul nu rămâne după ce eroarea a plecat la client.
        return "INSERT INTO course_registration (student_id, course_id, grade) VALUES (1, 404, 'A')";
        // Începător: metoda s-a oprit la propoziția care nu lasă rândul.
        // Producție: acolada închide explainStatement. INSERT-ul nu a rulat.
        // Expert: COMMIT-ul de după nu salvează un rând pe care propoziția l-a respins deja.
    }
}
