/*
TOPIC:
    Termenii alegerii: trigger-ul, tabela de care este legat, coloana NEW sau OLD, propoziția care îl pornește și tranzacția pe care nu o poate încheia.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Numele din manual legate de efectul observabil: rândul rămâne cu 0, SET NEW după scriere nu schimbă nimic, TRUNCATE nu pornește DELETE, COMMIT în corp nu este permis.
MECHANISM:
    Metodele de mai jos țin propoziția gata de trimis. Nu deschid conexiunea. root@localhost a primit 1045, using password: NO.
LOOKALIKES:
    CHECK nu este trigger-ul. CALL nu pornește la INSERT. OLD nu există la INSERT.
BOOK THEORY:
    Manualul 9.7, 27.4.1, numește BEFORE, AFTER, NEW, OLD și darea înapoi a propoziției pe tabela tranzacțională. 15.1.27 numește tabela permanentă, nu view-ul, și spune că TRUNCATE nu activează DELETE. 27.10 numește interdicția de COMMIT, ROLLBACK și START TRANSACTION în trigger.
BOOK EXAMPLE:
    explainTrigger, explainTable, explainColumn, explainStatement, explainTransaction
REPO DEMO:
    src/main/java/_15_TRIGGERS/Triggers.java
BREAK LAB:
    SET NEW într-un AFTER, sau START TRANSACTION în corp.
TRADE-OFFS:
    CHECK când rândul trebuie să cadă. CALL când programul trebuie cerut.
ANTI-PATTERNS:
    A numi CHECK trigger și a crede că TRUNCATE pornește corpul de DELETE.
STAFF QUESTIONS:
    Metodele explain* de mai jos, plus banca din Triggers_Staff_Interview.
NOVEL TRANSFER:
    Mișcarea, raftul și cantitatea: raftul se schimbă după mișcare, cantitatea negativă poate rămâne 0, refuzul nu este trigger-ul.
SPACED REVIEW:
    O zi: explainTrigger. Șapte zile: explainColumn. Treizeci de zile: explainTransaction.

Definiția unui trigger (trigger):
       Trigger-ul este programul legat de o tabelă permanentă, pornit de server la INSERT, UPDATE sau DELETE, o dată pentru fiecare rând.

Exemplu:
       Triggers_Terms.explainTrigger()
       Salariul -50 rămâne 0 dacă BEFORE INSERT face SET NEW.salary = 0.

Definiția unei tabele (table):
       Tabela permanentă este locul de care este legat trigger-ul. Un view și o tabelă TEMPORARY nu pot purta trigger.

Exemplu:
       Triggers_Terms.explainTable()
       salary este tabela de pe care pornește before_salary_insert.

Definiția unei coloane (column):
       Coloana văzută de trigger este NEW la INSERT și la UPDATE, și OLD la UPDATE și la DELETE. OLD este doar citit.

Exemplu:
       Triggers_Terms.explainColumn()
       NEW.salary este valoarea care urmează să fie scrisă.

Definiția unei propoziții (statement):
       Propoziția care scrie rândul este cea care pornește trigger-ul. Dacă trigger-ul eșuează, întreaga propoziție eșuează.

Exemplu:
       Triggers_Terms.explainStatement()
       TRUNCATE TABLE salary nu pornește trigger-ul de DELETE.

Definiția unei tranzacții (transaction):
       Trigger-ul nu poate da COMMIT sau ROLLBACK. Pe tabela tranzacțională, eșecul dă înapoi toată propoziția, inclusiv ce a scris trigger-ul.

Exemplu:
       Triggers_Terms.explainTransaction()
       START TRANSACTION în corpul trigger-ului nu este permis.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE numești programul care schimbă valoarea înainte de scriere -> explainTrigger -> BEFORE INSERT SET NEW.salary = 0 -> nu garantează că serverul a executat șirul
    2. Eșecul cel mai mic
           -> DECIDE TRUNCATE pornește DELETE -> explainStatement -> TRUNCATE TABLE salary -> nu garantează că un corp de DELETE a văzut OLD
    3. Granița
           -> Propoziția este mecanismul. Metoda Java doar o ține. Workbench nu este termenul.
    4. Alternativa
           -> explainTransaction când cineva vrea COMMIT în corp. explainTrigger când valoarea trebuie schimbată înainte de scriere.
    5. Anti-patternul
           -> Să numești CHECK trigger. CHECK nu lasă rândul. BEFORE lasă rândul cu 0.
    6. Dovada
           -> explainTrigger ține CREATE TRIGGER. explainStatement ține TRUNCATE. root@localhost a primit 1045, using password: NO.
    7. Interviu
           -> Trigger-ul pornește pe rând. Tabela permanentă îl ține. NEW se poate schimba doar în BEFORE. TRUNCATE nu este DELETE. COMMIT în corp nu există.
*/
// Începător: glosarul ține propozițiile despre programul de pe tabelă, nu doar numele.
// Producție: package _15_TRIGGERS.fast. Metodele întorc șirul gata de trimis.
// Expert: Termenii sunt trigger-ul, tabela, coloana, propoziția și tranzacția. Nu sunt lecția întreagă a promovării.
package _15_TRIGGERS.fast;

// Începător: clasa execută termenii prin propoziția pe care o păstrează.
// Producție: public final class Triggers_Terms. Fără metode, definiția nu are exemplu care să țină SQL-ul.
// Expert: Nu deschide JDBC. Nu copiază exercițiul de hire_date ca pe un al doilea vocabular.
public final class Triggers_Terms {

    /*
    🟢 Începător
        Ce este            programul care pune 0 înainte să se scrie salariul negativ
        De ce e nevoie     fără propoziție, „trigger” rămâne un comentariu
    🟡 Producție
        Cum se scrie       CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW cu SET NEW.salary = 0
    🔴 Staff
        Unde               schimbarea este înainte de scriere; AFTER nu mai poate face acest SET
    Pași
        1. Ține CREATE TRIGGER.
        2. Nu îl trimite.
        3. Nu îl înlocui cu CHECK.
    */
    // Începător: metoda păstrează programul de pe salariu.
    // Producție: public static String explainTrigger(). Dacă dispare, definiția nu mai are propoziția.
    // Expert: Nu predă corpul de pe dept_manager. Acel AFTER scrie în altă tabelă și stă în AFTER_.
    public static String explainTrigger() {
        // Începător: salariul negativ devine 0, iar rândul rămâne.
        // Producție: CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW BEGIN IF NEW.salary < 0 THEN SET NEW.salary = 0; END IF; END. Dacă linia dispare, termenul nu mai are propoziție.
        // Expert: FOR EACH ROW este o dată pe rând. Numele este unic în schemă. Trigger-ul nu ține locul lui FOREIGN KEY.
        return "CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW BEGIN IF NEW.salary < 0 THEN SET NEW.salary = 0; END IF; END";
        // Începător: metoda a înapoiat programul, nu un rând scris.
        // Producție: acolada închide explainTrigger. Șirul nu a plecat la 3306.
        // Expert: Accesul 1045 nu este efectul lui SET NEW. INSERT-ul cu -50 nu este acest șir.
    }

    /*
    🟢 Începător
        Ce este            tabela de salarii, de care se leagă programul
        De ce e nevoie     fără ea, programul nu are pe ce să pornească
    🟡 Producție
        Cum se scrie       CREATE TABLE salary (emp_no INT NOT NULL, salary INT NULL) ENGINE=InnoDB
    🔴 Staff
        Unde               trebuie să fie tabelă permanentă; un view nu primește trigger
    Pași
        1. Ține CREATE TABLE salary.
        2. Nu îl trimite.
        3. Nu îl numi trigger.
    */
    // Începător: metoda păstrează tabela de care se leagă programul.
    // Producție: public static String explainTable(). Dacă dispare, salary rămâne un nume.
    // Expert: ENGINE=InnoDB este motorul pe care eșecul propoziției se dă înapoi. Tabela nu este un view.
    public static String explainTable() {
        // Începător: apare tabela salary, goală, fără niciun trigger încă.
        // Producție: CREATE TABLE salary (emp_no INT NOT NULL, salary INT NULL) ENGINE=InnoDB. Dacă linia dispare, nu mai ai locul din schemă.
        // Expert: TEMPORARY și view-ul nu pot purta trigger-ul. DROP TABLE ar șterge și trigger-ele tabelei. Acest CREATE nu creează before_salary_insert.
        return "CREATE TABLE salary (emp_no INT NOT NULL, salary INT NULL) ENGINE=InnoDB";
        // Începător: metoda a înapoiat tabela, nu programul.
        // Producție: acolada închide explainTable. Tabela nu a fost creată pe server.
        // Expert: salaries din comentariul de UPDATE este alt nume. Acest șir este salary, ca la BEFORE INSERT.
    }

    /*
    🟢 Începător
        Ce este            salariul care urmează să fie scris, văzut ca NEW
        De ce e nevoie     fără deosebire, OLD și NEW par aceeași valoare la un rând nou
    🟡 Producție
        Cum se scrie       CREATE TRIGGER before_salary_reads_old BEFORE INSERT ON salary FOR EACH ROW SET NEW.salary = OLD.salary
    🔴 Staff
        Unde               la INSERT nu există OLD; OLD este doar citit când există, la UPDATE sau DELETE
    Pași
        1. Ține coloana citită greșit la INSERT.
        2. Nu o trimite.
        3. Nu o muta într-un UPDATE, unde OLD ar exista.
    */
    // Începător: metoda păstrează coloana pe care un INSERT nu o are în urmă.
    // Producție: public static String explainColumn(). Dacă dispare, NEW și OLD rămân aceeași poveste.
    // Expert: Șirul este eșecul, nu definiția fericită. Coloana generată nu poate fi folosită prin NEW sau OLD.
    public static String explainColumn() {
        // Începător: programul caută salariul vechi al unui rând care încă nu există.
        // Producție: CREATE TRIGGER before_salary_reads_old BEFORE INSERT ON salary FOR EACH ROW SET NEW.salary = OLD.salary. Dacă linia dispare, nu mai ai coloana care lipsește la INSERT.
        // Expert: La UPDATE, OLD.salary este valoarea de dinainte și nu poate fi ținta unui SET. La DELETE nu există NEW. AUTO_INCREMENT în BEFORE este 0, nu numărul generat.
        return "CREATE TRIGGER before_salary_reads_old BEFORE INSERT ON salary FOR EACH ROW SET NEW.salary = OLD.salary";
        // Începător: metoda a înapoiat coloana citită prea devreme.
        // Producție: acolada închide explainColumn. Trigger-ul nu a fost creat.
        // Expert: SET NEW într-un AFTER nu schimbă coloana deja scrisă. Aici problema este că OLD nu există.
    }

    /*
    🟢 Începător
        Ce este            golirea tabelei, care nu pornește programul de ștergere
        De ce e nevoie     fără ea, orice propoziție care golește pare un DELETE
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE salary
    🔴 Staff
        Unde               TRUNCATE nu folosește DELETE; eșecul unui trigger, când chiar pornește, face să cadă toată propoziția
    Pași
        1. Ține TRUNCATE.
        2. Nu o trimite.
        3. Nu o numi DELETE.
    */
    // Începător: metoda păstrează propoziția care nu pornește corpul de DELETE.
    // Producție: public static String explainStatement(). Dacă dispare, momentul activării rămâne o poveste.
    // Expert: REPLACE poate activa DELETE. TRUNCATE nu. Eroarea dintr-un trigger care chiar a pornit dă înapoi propoziția pe InnoDB.
    public static String explainStatement() {
        // Începător: salary rămâne fără rânduri, iar programul de pe fiecare ștergere nu pornește.
        // Producție: TRUNCATE TABLE salary. Dacă linia dispare, nu mai ai propoziția cu care se confundă DELETE.
        // Expert: DROP TABLE nu activează DELETE și șterge trigger-ele odată cu tabela. LOAD DATA, spre deosebire de TRUNCATE, activează INSERT. Cascada, implicit, nu activează trigger-ul.
        return "TRUNCATE TABLE salary";
        // Începător: metoda s-a oprit la golire.
        // Producție: acolada închide explainStatement. TRUNCATE nu a rulat.
        // Expert: 1045 nu dovedește că un DELETE a tăcut. Șirul nu a plecat.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a deschide o tranzacție din interiorul programului
        De ce e nevoie     fără ea, mărirea pare că se poate închide singură, înainte de restul propoziției
    🟡 Producție
        Cum se scrie       CREATE TRIGGER after_salary_commits AFTER INSERT ON salary FOR EACH ROW START TRANSACTION
    🔴 Staff
        Unde               START TRANSACTION, COMMIT și ROLLBACK nu sunt permise în trigger; pe InnoDB eșecul dă înapoi toată propoziția
    Pași
        1. Ține START TRANSACTION în corp.
        2. Nu îl trimite.
        3. Nu îl înlocui cu ROLLBACK TO SAVEPOINT, care nu încheie tranzacția.
    */
    // Începător: metoda păstrează programul care vrea propria tranzacție.
    // Producție: public static String explainTransaction(). Dacă dispare, interdicția rămâne fără propoziție.
    // Expert: ROLLBACK TO SAVEPOINT este permis tocmai pentru că nu încheie tranzacția. Acest șir nu este un savepoint.
    public static String explainTransaction() {
        // Începător: programul vrea să înceapă o tranzacție după ce salariul a fost scris.
        // Producție: CREATE TRIGGER after_salary_commits AFTER INSERT ON salary FOR EACH ROW START TRANSACTION. Dacă linia dispare, nu mai ai corpul care nu poate încheia singur.
        // Expert: Pe tabela tranzacțională, eșecul trigger-ului dă înapoi propoziția, inclusiv schimbările făcute de corp înainte de eroare. Pe tabela netranzacțională, schimbările dinaintea erorii rămân. Acest START TRANSACTION nu deschide acea ieșire.
        return "CREATE TRIGGER after_salary_commits AFTER INSERT ON salary FOR EACH ROW START TRANSACTION";
        // Începător: metoda a înapoiat programul care nu poate ține o tranzacție a lui.
        // Producție: acolada închide explainTransaction. Trigger-ul nu a fost creat.
        // Expert: COMMIT-ul de după INSERT, trimis de client, nu este acest șir. Trigger-ul nu este cel care face schimbarea durabilă.
    }
}
