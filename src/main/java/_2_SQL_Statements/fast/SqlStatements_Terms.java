// Începător: glosarul stă lângă propozițiile care schimbă schema, rândul, privilegiul sau tranzacția.
// Producție: package _2_SQL_Statements.fast; dacă dispare, explainDdl nu mai compilează lângă SqlStatements.
// Expert: pachetul nu execută serverul și nu este calificatorul db1 din GRANT ALL ON db1.*.
package _2_SQL_Statements.fast;

/*
Definiția unei propoziții SQL (SQL statement):
       Serverul execută propoziția și lasă o schimbare de schemă, de rând, de privilegiu sau de tranzacție.

Exemplu:
       SqlStatements_Terms.explainStatement()
       CREATE TABLE customers rămâne după ROLLBACK. INSERT din tranzacția deschisă nu rămâne.

Definiția unui limbaj de definire a datelor (DDL):
       DDL schimbă obiectul. CREATE TABLE netemporar face commit implicit, iar ROLLBACK nu șterge tabela.

Exemplu:
       SqlStatements_Terms.explainDdl()
       Tabela customers există după ROLLBACK.

Definiția unui limbaj de manipulare a datelor (DML):
       DML schimbă sau citește rânduri. INSERT din START TRANSACTION nu este durabil până la COMMIT.

Exemplu:
       SqlStatements_Terms.explainDml()
       ROLLBACK scoate rândul cu customer_id 4.

Definiția unei administrări de cont (account management):
       GRANT schimbă privilegiul din schema mysql și face commit implicit. Nu șterge rânduri din customers.

Exemplu:
       SqlStatements_Terms.explainAccount()
       GRANT ALL ON db1.* TO 'jeffrey'@'localhost' lasă privilegiul după ROLLBACK.

Definiția unui commit implicit (implicit commit):
       Serverul închide tranzacția deschisă înainte de DDL netemporar sau de GRANT, ca și cum ar fi primit COMMIT.

Exemplu:
       SqlStatements_Terms.explainImplicitCommit()
       INSERT-ul de dinainte de GRANT rămâne.

Definiția unei tranzacții (transaction):
       START TRANSACTION deschide tranzacția. COMMIT o face durabilă. ROLLBACK anulează schimbările InnoDB încă necomise.

Exemplu:
       SqlStatements_Terms.explainTransaction()
       Rândul inserat dispare la ROLLBACK.

Definiția unei autocommit (autocommit):
       Cu autocommit=1, propoziția este deja durabilă la sfârșitul ei. Un ROLLBACK ulterior nu o scoate.

Exemplu:
       SqlStatements_Terms.explainAutocommit()
       INSERT fără START TRANSACTION rămâne și fără COMMIT scris de tine.
*/

// Începător: clasa execută termenii cu care alegi propoziția după ce rămâne.
// Producție: public class SqlStatements_Terms ține metodele din Exemplu. Dacă dispare, glosarul nu mai are propoziție de trimis.
// Expert: clasa nu ține lacăt după ce textul a plecat. Nu înlocuiește un filtru dintr-un singur serviciu când contul este partajat.
/*
🟢 Începător
    Ce este            termenii efectului: propoziție, DDL, DML, cont, commit implicit, tranzacție, autocommit
    De ce e nevoie    fără ei, ROLLBACK este crezut suficient după CREATE TABLE
🟡 Producție
    Cum se scrie       fiecare metodă întoarce propoziția termenului
🔴 Staff
    Unde               serverul aplică commit-ul implicit; metoda doar predă textul
Pași                   1. Numește efectul. 2. Întoarce propoziția. 3. Spune ce nu anulează ROLLBACK.
*/
public class SqlStatements_Terms
// Începător: deschide metodele care arată propoziția fiecărui termen.
// Producție: fără acoladă, explainStatement nu are corp.
// Expert: acolada nu face commit implicit și nu scrie privilegiul în schema mysql.
{

    // Începător: tabela rămâne, rândul 4 din tranzacția de după CREATE nu rămâne.
    // Producție: CREATE TABLE customers, START TRANSACTION, INSERT, ROLLBACK. Dacă metoda dispare, termenul de propoziție nu mai are script.
    // Expert: nu este un cuvânt cheie din SELECT. Commit-ul implicit al lui CREATE nu garantează că GRANT ar fi fost anulat.
    /*
    🟢 Începător
        Ce este            propoziția care lasă o schimbare, nu un nume de folder
        De ce e nevoie    fără script, DDL și DML par același gest
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY); START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK;
    🔴 Staff
        Unde               schema la sfârșitul lui CREATE; rândul dispare la ROLLBACK
    Pași                   1. Schimbă schema. 2. Deschide tranzacția. 3. Anulează rândul.
    */
    public static String explainStatement()
    // Începător: deschide scriptul care desparte tabela rămasă de rândul anulat.
    // Producție: fără acoladă, cele patru propoziții nu pot fi întoarse.
    // Expert: acolada nu este trimisă serverului și nu ține lacătul rândului 4.
    {
        // Începător: întoarce propozițiile în care customers rămâne și rândul 4 dispare.
        // Producție: return predă scriptul. Fără return, exemplul termenului nu poate fi trimis.
        // Expert: return nu face commit. Serverul comite CREATE TABLE înainte de START TRANSACTION.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY);
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                ROLLBACK;
                """;
    // Începător: închide termenul de propoziție.
    // Producție: fără această acoladă, explainDdl nu mai compilează.
    // Expert: închiderea nu anulează tabela customers.
    }

    // Începător: propoziția creează tabela customers, care rămâne și dacă urmează ROLLBACK.
    // Producție: CREATE TABLE customers (customer_id INT PRIMARY KEY). Dacă metoda dispare, DDL nu mai are propoziție.
    // Expert: vecinul este INSERT. CREATE netemporar face commit implicit. Nu garantează ștergerea unui rând vechi.
    /*
    🟢 Începător
        Ce este            schimbarea obiectului, nu a rândului
        De ce e nevoie    fără ea, ROLLBACK este așteptat să șteargă tabela
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY)
    🔴 Staff
        Unde               commit implicit înainte și după, doar dacă lipsește TEMPORARY
    Pași                   1. Trimite CREATE TABLE. 2. Nu îl pune în locul unui rând anulabil.
    */
    public static String explainDdl()
    // Începător: deschide propoziția care lasă tabela.
    // Producție: fără acoladă, CREATE TABLE customers nu poate fi întors.
    // Expert: acolada nu face commit. Commit-ul îl face serverul la CREATE netemporar.
    {
        // Începător: întoarce crearea tabelei customers.
        // Producție: return predă CREATE TABLE customers (customer_id INT PRIMARY KEY). Fără return, termenul DDL nu are text.
        // Expert: return nu este un index. CREATE INDEX ar fi tot DDL și ar comite și el.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY)
                """;
    // Începător: închide termenul de schemă.
    // Producție: fără această acoladă, explainDml nu compilează.
    // Expert: închiderea nu șterge customers.
    }

    // Începător: rândul 4 este adăugat și apoi scos. Tabela nu este creată aici.
    // Producție: START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK; Dacă metoda dispare, DML nu mai arată rândul anulat.
    // Expert: nu este CREATE TABLE. Nu schimbă privilegiul lui jeffrey și nu face commit implicit.
    /*
    🟢 Începător
        Ce este            schimbarea de rând care încă poate dispărea
        De ce e nevoie    fără ea, INSERT pare durabil de îndată ce a rulat
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK;
    🔴 Staff
        Unde               sesiunea vede rândul înainte de ROLLBACK; alt client nu îl primește durabil
    Pași                   1. Deschide tranzacția. 2. Inserează. 3. Anulează.
    */
    public static String explainDml()
    // Începător: deschide propozițiile care scot rândul 4.
    // Producție: fără acoladă, INSERT și ROLLBACK nu pot fi întoarse.
    // Expert: acolada nu ține lacătul. InnoDB îl ține până la ROLLBACK.
    {
        // Începător: întoarce adăugarea și anularea rândului 4.
        // Producție: return predă START TRANSACTION, INSERT și ROLLBACK. Fără return, termenul DML nu are text.
        // Expert: return nu schimbă schema. Un CREATE TABLE înaintea acestor propoziții ar fi rămas.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                ROLLBACK;
                """;
    // Începător: închide termenul de rând.
    // Producție: fără această acoladă, explainAccount nu compilează.
    // Expert: închiderea nu revocă un privilegiu.
    }

    // Începător: jeffrey primește drepturi pe db1. Rândurile din customers nu sunt șterse.
    // Producție: GRANT ALL ON db1.* TO 'jeffrey'@'localhost'. Dacă metoda dispare, administrarea de cont nu mai are propoziția din repo.
    // Expert: GRANT face commit implicit. Nu este INSERT și nu garantează că ROLLBACK ia privilegiul înapoi.
    /*
    🟢 Începător
        Ce este            privilegiul scris în schema mysql
        De ce e nevoie    fără el, un filtru dintr-un serviciu este crezut suficient pentru orice client
    🟡 Producție
        Cum se scrie       GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
    🔴 Staff
        Unde               commit implicit, pentru că se modifică tabele din mysql
    Pași                   1. Dă privilegiul. 2. Nu aștepta ca ROLLBACK să îl scoată.
    */
    public static String explainAccount()
    // Începător: deschide propoziția care dă privilegiul, nu un rând în customers.
    // Producție: fără acoladă, GRANT ALL ON db1.* TO 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu autentifică. Serverul scrie privilegiul și comite tranzacția deschisă.
    {
        // Începător: întoarce privilegiul lui jeffrey pe db1.
        // Producție: return predă GRANT ALL din Grant_.java. Fără return, termenul de cont nu are text.
        // Expert: return nu șterge rânduri. REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' este scoaterea privilegiului, tot cu commit implicit.
        return """
                GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
                """;
    // Începător: închide termenul de privilegiu.
    // Producție: fără această acoladă, explainImplicitCommit nu compilează.
    // Expert: închiderea nu șterge dreptul din schema mysql.
    }

    // Începător: rândul 4 rămâne, pentru că GRANT a închis tranzacția înainte de ROLLBACK.
    // Producție: START TRANSACTION; INSERT; GRANT ALL ON db1.* TO 'jeffrey'@'localhost'; ROLLBACK. Dacă metoda dispare, commit-ul implicit nu mai are eșecul lui.
    // Expert: nu este un COMMIT scris de tine. Serverul îl face înainte de GRANT. Nu garantează eliberarea lacătului înainte de acel commit.
    /*
    🟢 Începător
        Ce este            închiderea tranzacției de către o propoziție care nu se numește COMMIT
        De ce e nevoie    fără ea, ROLLBACK pare să anuleze și GRANT
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); GRANT ALL ON db1.* TO 'jeffrey'@'localhost'; ROLLBACK;
    🔴 Staff
        Unde               înainte ca GRANT să ruleze, tranzacția deschisă este deja comisă
    Pași                   1. Inserează. 2. Dă privilegiul. 3. Observă că rândul a rămas.
    */
    public static String explainImplicitCommit()
    // Începător: deschide eșecul în care privilegiul salvează rândul.
    // Producție: fără acoladă, GRANT dintre INSERT și ROLLBACK nu poate fi întors.
    // Expert: acolada nu scrie în mysql. Serverul comite INSERT-ul înainte de GRANT.
    {
        // Începător: întoarce scriptul în care rândul 4 rămâne.
        // Producție: return predă GRANT ALL între INSERT și ROLLBACK. Fără return, termenul nu are text.
        // Expert: return nu este XA. În XA ACTIVE, propoziția care face commit implicit este refuzată.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                GRANT ALL ON db1.* TO 'jeffrey'@'localhost';
                ROLLBACK;
                """;
    // Începător: închide termenul de commit implicit.
    // Producție: fără această acoladă, explainTransaction nu compilează.
    // Expert: închiderea nu anulează privilegiul lăsat de GRANT.
    }

    // Începător: rândul 4 dispare când tranzacția este anulată. Nu se creează o tabelă.
    // Producție: START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK. Dacă metoda dispare, tranzacția nu mai are cele trei propoziții.
    // Expert: ROLLBACK fără nume de SAVEPOINT închide tranzacția. ROLLBACK TO SAVEPOINT nu o închide. Nu garantează anularea unui CREATE de dinainte.
    /*
    🟢 Începător
        Ce este            deschiderea, păstrarea sau anularea schimbărilor încă necomise
        De ce e nevoie    fără ea, autocommit=1 este crezut oprit
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK;
    🔴 Staff
        Unde               la ROLLBACK lacătele tranzacției se eliberează; un eșec pe o singură propoziție nu le eliberează
    Pași                   1. Deschide. 2. Schimbă rândul. 3. Anulează.
    */
    public static String explainTransaction()
    // Începător: deschide tranzacția care scoate rândul 4.
    // Producție: fără acoladă, START TRANSACTION și ROLLBACK nu pot fi întoarse.
    // Expert: acolada nu eliberează lacătul. Îl eliberează ROLLBACK complet, pe server.
    {
        // Începător: întoarce anularea rândului 4.
        // Producție: return predă cele trei propoziții. Fără return, termenul de tranzacție nu are text.
        // Expert: return nu reface autocommit. După ROLLBACK, autocommit revine la starea dinainte de START TRANSACTION.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                ROLLBACK;
                """;
    // Începător: închide termenul de tranzacție.
    // Producție: fără această acoladă, explainAutocommit nu compilează.
    // Expert: închiderea nu lasă o tranzacție deschisă pe server.
    }

    // Începător: rândul 4 rămâne la sfârșitul lui INSERT, fără un COMMIT scris de tine.
    // Producție: INSERT INTO customers (customer_id) VALUES (4). Dacă metoda dispare, autocommit nu mai are propoziția care este deja durabilă.
    // Expert: nu este START TRANSACTION. Un ROLLBACK ulterior nu scoate rândul. Nu garantează același lucru dacă autocommit a fost pus pe 0.
    /*
    🟢 Începător
        Ce este            propoziția care se salvează singură, pentru că autocommit este 1
        De ce e nevoie    fără ea, fiecare INSERT este crezut anulabil
    🟡 Producție
        Cum se scrie       INSERT INTO customers (customer_id) VALUES (4)
    🔴 Staff
        Unde               durabil la sfârșitul propoziției, nu la un COMMIT scris după
    Pași                   1. Lasă autocommit pe 1. 2. Inserează. 3. Nu aștepta un COMMIT ca să rămână.
    */
    public static String explainAutocommit()
    // Începător: deschide INSERT-ul care rămâne fără START TRANSACTION.
    // Producție: fără acoladă, INSERT INTO customers (customer_id) VALUES (4) nu poate fi întors.
    // Expert: acolada nu setează autocommit. SET autocommit=0 ar lăsa tranzacția deschisă pe sesiune.
    {
        // Începător: întoarce inserarea rândului 4, deja durabilă la sfârșitul ei.
        // Producție: return predă INSERT-ul. Fără return, termenul autocommit nu are text.
        // Expert: return nu înlocuiește setAutoCommit din JDBC. Dacă JDBC a oprit autocommit, acest INSERT nu mai este durabil la sfârșitul lui.
        return """
                INSERT INTO customers (customer_id) VALUES (4)
                """;
    // Începător: închide termenul autocommit.
    // Producție: fără această acoladă, clasa nu se închide.
    // Expert: închiderea nu comite o tranzacție deschisă cu START TRANSACTION.
    }
// Începător: închide glosarul celor șapte termeni.
// Producție: fără acolada finală, fișierul nu compilează.
// Expert: închiderea clasei nu închide tranzacția de pe server și nu șterge privilegiul din mysql.
}
