// Începător: banca stă lângă alegerea propoziției, nu lângă o clauză din SELECT.
// Producție: package _2_SQL_Statements.fast; dacă dispare, cele cincizeci de metode nu mai compilează lângă SqlStatements.
// Expert: pachetul nu execută serverul și nu ține lacătul rândului din customers.
package _2_SQL_Statements.fast;

/*
TOPIC:
    Alegi propoziția după ce rămâne: CREATE TABLE schimbă schema și face commit implicit, INSERT schimbă un rând doar cât tranzacția e deschisă, GRANT schimbă privilegiul și face commit implicit, ROLLBACK anulează doar ce nu a fost comis.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Problema este propoziția trimisă pe obiectul greșit: un CREATE sau un GRANT folosit ca să poți da înapoi un rând lasă tabela sau privilegiul după ROLLBACK.
MECHANISM:
    Serverul execută propoziția. CREATE TABLE pe o tabelă netemporară și GRANT ALL termină tranzacția deschisă înainte să ruleze. Schema se vede la sfârșitul lui CREATE TABLE. Rândul din INSERT devine durabil la COMMIT, sau dispare la ROLLBACK.
LOOKALIKES:
    DDL se confundă cu DML. DDL schimbă schema și, la tabelă netemporară, face commit implicit. DML schimbă rânduri și poate sta necomis între START TRANSACTION și COMMIT.
BOOK THEORY:
    Manualul 9.7 împarte propozițiile în definire de date, manipulare de date, control al tranzacției și administrare de cont. Autocommit este 1 implicit, deci fiecare propoziție este o tranzacție; COMMIT o face durabilă, ROLLBACK o anulează. DDL pe obiect netemporar, plus GRANT, REVOKE, ALTER USER, CREATE USER, DROP USER, RENAME USER și SET PASSWORD, fac commit implicit. CREATE TEMPORARY TABLE nu face commit implicit, dar ROLLBACK nu o șterge. JDBC poate deschide tranzacția în loc să trimită START TRANSACTION. DDL.pdf doar numește Data Definition Language. DML.pdf spune că propozițiile manipulează datele din tabele și că SELECT le citește. DCL.pdf doar numește Data Control Language. TCL.pdf titrează Transaction Control Language (DCL) și nu spune autocommit. COMMIT+and+ROLLBACK.pdf are pe prima pagină textul The SQL UPDATE Statement, iar pe a doua numește COMMIT și ROLLBACK.
BOOK EXAMPLE:
    SqlStatements_Fast.chooseByEffect
REPO DEMO:
    src/main/java/_2_SQL_Statements/fast/SqlStatements_Fast.java
BREAK LAB:
    START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); GRANT ALL ON db1.* TO 'jeffrey'@'localhost'; ROLLBACK; Rândul rămâne, pentru că GRANT a comis INSERT-ul. Privilegiul rămâne și el.
TRADE-OFFS:
    Dacă rândul trebuie să poată dispărea, rămâi la INSERT între START TRANSACTION și COMMIT și nu trimiți DDL. Dacă același cont e folosit de mai mulți clienți, privilegiul se schimbă cu GRANT, nu cu un filtru dintr-un singur serviciu.
ANTI-PATTERNS:
    ROLLBACK după CREATE TABLE customers și așteptarea ca tabela să dispară.
STAFF QUESTIONS:
    SqlStatements_Staff_Interview.q01CreateTable până la SqlStatements_Staff_Interview.q50ExplainIsNotTheWrite
NOVEL TRANSFER:
    Registrul unei clinici: CREATE TABLE appointment lasă tabela după ROLLBACK; vizita inserată în tranzacție dispare la ROLLBACK; GRANT ALL către contul asistentului rămâne, pentru că a comis.
SPACED REVIEW:
    o zi: din CREATE TABLE și INSERT, care schimbă schema
    șapte zile: ce anulează ROLLBACK după un INSERT necomis
    treizeci de zile: unde este rândul dacă GRANT stă între INSERT și ROLLBACK
*/

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

// Începător: clasa întoarce cele cincizeci de propoziții cu care recunoști schema, rândul, privilegiul și tranzacția.
// Producție: public class SqlStatements_Staff_Interview nu are main. Dacă dispare, cardul nu mai are metodele STAFF QUESTIONS.
// Expert: clasa nu deschide conexiunea. Rularea pe server rămâne nefăcută: repo-ul nu are host, utilizator sau port.
/*
🟢 Începător
    Ce este            cele cincizeci de întrebări pe alegerea propoziției
    De ce e nevoie    fără ele, CREATE și INSERT rămân sinonime
🟡 Producție
    Cum se scrie       fiecare metodă întoarce propoziția întrebării
🔴 Staff
    Unde               commit implicit, lacăt și momentul în care rândul devine durabil
Pași                   1. Citește enunțul. 2. Întoarce propoziția. 3. Spune ce rămâne.
*/
public class SqlStatements_Staff_Interview
// Începător: deschide banca, fără să trimită singură propozițiile la server.
// Producție: fără acoladă, q01CreateTable nu are unde să stea.
// Expert: acolada nu face commit implicit și nu scrie privilegiul lui jeffrey.
{

    // Enunț: Ce schimbă CREATE TABLE customers (customer_id INT PRIMARY KEY)?
    // Capcană: Este un INSERT de structură, deci ROLLBACK șterge tabela.
    // Cheie: Schimbă schema. Pe tabela netemporară face commit implicit, iar ROLLBACK de după nu o șterge.
    // Începător: Propoziția lasă tabela customers și după un ROLLBACK ulterior.
    // Producție: CREATE TABLE customers (customer_id INT PRIMARY KEY) Dacă metoda dispare, fără ea nu mai recunoști schimbarea de schemă.
    // Expert: Nu este INSERT. Commit-ul implicit nu ține loc de index și nu anulează un rând vechi.
    /*
    🟢 Începător
        Ce este            Propoziția lasă tabela customers și după un ROLLBACK ulterior.
        De ce e nevoie    fără ea nu mai recunoști schimbarea de schemă
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY)
    🔴 Staff
        Unde               Nu este INSERT. Commit-ul implicit nu ține loc de index și nu anulează un rând vechi.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q01CreateTable()
    // Începător: deschide propoziția pentru: Propoziția lasă tabela customers și după un ROLLBACK ulterior.
    // Producție: fără acoladă, CREATE TABLE customers (customer_id INT PRIMARY KEY) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT. Commit-ul implicit nu ține loc de index și nu anulează un rând vechi.
    {
        // Începător: Propoziția lasă tabela customers și după un ROLLBACK ulterior.
        // Producție: return predă CREATE TABLE customers (customer_id INT PRIMARY KEY). Fără return, fără ea nu mai recunoști schimbarea de schemă.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT. Commit-ul implicit nu ține loc de index și nu anulează un rând vechi.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY)
                """;
    // Începător: închide întrebarea despre: Propoziția lasă tabela customers și după un ROLLBACK ulterior.
    // Producție: fără această acoladă, q01CreateTable nu se închide.
    // Expert: închiderea nu execută CREATE TABLE customers (customer_id INT PRIMARY KEY) și nu schimbă customers.
    }

    // Enunț: Ce este durabil după START TRANSACTION și INSERT INTO customers (customer_id) VALUES (4), înainte de COMMIT?
    // Capcană: INSERT a rulat, deci rândul 4 este deja salvat pentru toți.
    // Cheie: Rândul este vizibil sesiunii care l-a scris și nu este durabil până la COMMIT.
    // Începător: Rândul 4 stă în tranzacția deschisă și încă poate dispărea.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără ea nu mai desparți execuția de durabilitate.
    // Expert: Nu este CREATE TABLE. Nu schimbă schema și nu dă privilegiu lui jeffrey.
    /*
    🟢 Începător
        Ce este            Rândul 4 stă în tranzacția deschisă și încă poate dispărea.
        De ce e nevoie    fără ea nu mai desparți execuția de durabilitate
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4);
    🔴 Staff
        Unde               Nu este CREATE TABLE. Nu schimbă schema și nu dă privilegiu lui jeffrey.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q02InsertInTransaction()
    // Începător: deschide propoziția pentru: Rândul 4 stă în tranzacția deschisă și încă poate dispărea.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este CREATE TABLE. Nu schimbă schema și nu dă privilegiu lui jeffrey.
    {
        // Începător: Rândul 4 stă în tranzacția deschisă și încă poate dispărea.
        // Producție: return predă START TRANSACTION;. Fără return, fără ea nu mai desparți execuția de durabilitate.
        // Expert: return nu face commit și nu ține lacăt. Nu este CREATE TABLE. Nu schimbă schema și nu dă privilegiu lui jeffrey.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                """;
    // Începător: închide întrebarea despre: Rândul 4 stă în tranzacția deschisă și încă poate dispărea.
    // Producție: fără această acoladă, q02InsertInTransaction nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: Ce lasă GRANT ALL ON db1.* TO 'jeffrey'@'localhost'?
    // Capcană: Adaugă un rând în customers pentru jeffrey.
    // Cheie: Scrie privilegiul în schema mysql și face commit implicit. Nu inserează în customers.
    // Începător: jeffrey primește drepturi pe db1, iar tranzacția deschisă se închide.
    // Producție: GRANT ALL ON db1.* TO 'jeffrey'@'localhost' Dacă metoda dispare, fără ea privilegiul este confundat cu un rând de business.
    // Expert: Nu este INSERT. ROLLBACK de după nu ia privilegiul înapoi.
    /*
    🟢 Începător
        Ce este            jeffrey primește drepturi pe db1, iar tranzacția deschisă se închide.
        De ce e nevoie    fără ea privilegiul este confundat cu un rând de business
    🟡 Producție
        Cum se scrie       GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
    🔴 Staff
        Unde               Nu este INSERT. ROLLBACK de după nu ia privilegiul înapoi.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q03GrantAll()
    // Începător: deschide propoziția pentru: jeffrey primește drepturi pe db1, iar tranzacția deschisă se închide.
    // Producție: fără acoladă, GRANT ALL ON db1.* TO 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT. ROLLBACK de după nu ia privilegiul înapoi.
    {
        // Începător: jeffrey primește drepturi pe db1, iar tranzacția deschisă se închide.
        // Producție: return predă GRANT ALL ON db1.* TO 'jeffrey'@'localhost'. Fără return, fără ea privilegiul este confundat cu un rând de business.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT. ROLLBACK de după nu ia privilegiul înapoi.
        return """
                GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
                """;
    // Începător: închide întrebarea despre: jeffrey primește drepturi pe db1, iar tranzacția deschisă se închide.
    // Producție: fără această acoladă, q03GrantAll nu se închide.
    // Expert: închiderea nu execută GRANT ALL ON db1.* TO 'jeffrey'@'localhost' și nu schimbă customers.
    }

    // Enunț: Ce anulează ROLLBACK când tranzacția are doar un INSERT necomis?
    // Capcană: Anulează și CREATE TABLE trimis mai devreme în aceeași sesiune.
    // Cheie: Anulează schimbările InnoDB încă necomise și eliberează lacătele tranzacției. Nu șterge o tabelă deja comisă de CREATE.
    // Începător: ROLLBACK scoate rândul necomis și închide tranzacția.
    // Producție: ROLLBACK Dacă metoda dispare, fără ea ROLLBACK pare o anulare universală.
    // Expert: Nu este ROLLBACK TO SAVEPOINT. Fără nume, tranzacția se termină și savepoint-urile dispar.
    /*
    🟢 Începător
        Ce este            ROLLBACK scoate rândul necomis și închide tranzacția.
        De ce e nevoie    fără ea ROLLBACK pare o anulare universală
    🟡 Producție
        Cum se scrie       ROLLBACK
    🔴 Staff
        Unde               Nu este ROLLBACK TO SAVEPOINT. Fără nume, tranzacția se termină și savepoint-urile dispar.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q04Rollback()
    // Începător: deschide propoziția pentru: ROLLBACK scoate rândul necomis și închide tranzacția.
    // Producție: fără acoladă, ROLLBACK nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este ROLLBACK TO SAVEPOINT. Fără nume, tranzacția se termină și savepoint-urile dispar.
    {
        // Începător: ROLLBACK scoate rândul necomis și închide tranzacția.
        // Producție: return predă ROLLBACK. Fără return, fără ea ROLLBACK pare o anulare universală.
        // Expert: return nu face commit și nu ține lacăt. Nu este ROLLBACK TO SAVEPOINT. Fără nume, tranzacția se termină și savepoint-urile dispar.
        return """
                ROLLBACK
                """;
    // Începător: închide întrebarea despre: ROLLBACK scoate rândul necomis și închide tranzacția.
    // Producție: fără această acoladă, q04Rollback nu se închide.
    // Expert: închiderea nu execută ROLLBACK și nu schimbă customers.
    }

    // Enunț: Când devine durabil rândul aflat într-o tranzacție deschisă cu START TRANSACTION?
    // Capcană: Devine durabil la sfârșitul lui INSERT, chiar dacă autocommit a fost oprit de START TRANSACTION.
    // Cheie: COMMIT face schimbarea durabilă și închide tranzacția. Abia atunci un alt client o vede ca stare comisă.
    // Începător: COMMIT lasă rândul durabil și închide tranzacția deschisă.
    // Producție: COMMIT Dacă metoda dispare, fără el momentul durabilității rămâne pe INSERT.
    // Expert: Nu este GRANT. COMMIT nu scrie un privilegiu în schema mysql.
    /*
    🟢 Începător
        Ce este            COMMIT lasă rândul durabil și închide tranzacția deschisă.
        De ce e nevoie    fără el momentul durabilității rămâne pe INSERT
    🟡 Producție
        Cum se scrie       COMMIT
    🔴 Staff
        Unde               Nu este GRANT. COMMIT nu scrie un privilegiu în schema mysql.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q05Commit()
    // Începător: deschide propoziția pentru: COMMIT lasă rândul durabil și închide tranzacția deschisă.
    // Producție: fără acoladă, COMMIT nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este GRANT. COMMIT nu scrie un privilegiu în schema mysql.
    {
        // Începător: COMMIT lasă rândul durabil și închide tranzacția deschisă.
        // Producție: return predă COMMIT. Fără return, fără el momentul durabilității rămâne pe INSERT.
        // Expert: return nu face commit și nu ține lacăt. Nu este GRANT. COMMIT nu scrie un privilegiu în schema mysql.
        return """
                COMMIT
                """;
    // Începător: închide întrebarea despre: COMMIT lasă rândul durabil și închide tranzacția deschisă.
    // Producție: fără această acoladă, q05Commit nu se închide.
    // Expert: închiderea nu execută COMMIT și nu schimbă customers.
    }

    // Enunț: TRUNCATE TABLE customers golește rândurile. În ce familie stă?
    // Capcană: Este un DELETE fără WHERE, deci poate fi anulat cu ROLLBACK.
    // Cheie: TRUNCATE TABLE este DDL, face commit implicit și lasă definiția. DELETE dintr-o tranzacție poate dispărea la ROLLBACK.
    // Începător: Tabela customers rămâne, fără rânduri, iar golirea este deja comisă.
    // Producție: TRUNCATE TABLE customers Dacă metoda dispare, fără ea TRUNCATE este trimis ca să ștergi un singur client.
    // Expert: Nu este DELETE FROM customers WHERE customer_id = 4. Nu păstrează restul rândurilor și nu stă necomis.
    /*
    🟢 Începător
        Ce este            Tabela customers rămâne, fără rânduri, iar golirea este deja comisă.
        De ce e nevoie    fără ea TRUNCATE este trimis ca să ștergi un singur client
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE customers
    🔴 Staff
        Unde               Nu este DELETE FROM customers WHERE customer_id = 4. Nu păstrează restul rândurilor și nu stă necomis.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q06Truncate()
    // Începător: deschide propoziția pentru: Tabela customers rămâne, fără rânduri, iar golirea este deja comisă.
    // Producție: fără acoladă, TRUNCATE TABLE customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este DELETE FROM customers WHERE customer_id = 4. Nu păstrează restul rândurilor și nu stă necomis.
    {
        // Începător: Tabela customers rămâne, fără rânduri, iar golirea este deja comisă.
        // Producție: return predă TRUNCATE TABLE customers. Fără return, fără ea TRUNCATE este trimis ca să ștergi un singur client.
        // Expert: return nu face commit și nu ține lacăt. Nu este DELETE FROM customers WHERE customer_id = 4. Nu păstrează restul rândurilor și nu stă necomis.
        return """
                TRUNCATE TABLE customers
                """;
    // Începător: închide întrebarea despre: Tabela customers rămâne, fără rânduri, iar golirea este deja comisă.
    // Producție: fără această acoladă, q06Truncate nu se închide.
    // Expert: închiderea nu execută TRUNCATE TABLE customers și nu schimbă customers.
    }

    // Enunț: DROP TABLE customers scoate rândurile sau obiectul?
    // Capcană: Lasă tabela goală, ca TRUNCATE, ca să poți insera din nou.
    // Cheie: Scoate obiectul. Următorul INSERT nu mai are tabelă. Este DDL și face commit implicit.
    // Începător: Obiectul customers dispare, nu doar rândurile din el.
    // Producție: DROP TABLE customers Dacă metoda dispare, fără el aplicația încă trimite INSERT către o tabelă ștearsă.
    // Expert: Nu este TRUNCATE TABLE. TRUNCATE lasă definiția.
    /*
    🟢 Începător
        Ce este            Obiectul customers dispare, nu doar rândurile din el.
        De ce e nevoie    fără el aplicația încă trimite INSERT către o tabelă ștearsă
    🟡 Producție
        Cum se scrie       DROP TABLE customers
    🔴 Staff
        Unde               Nu este TRUNCATE TABLE. TRUNCATE lasă definiția.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q07DropTable()
    // Începător: deschide propoziția pentru: Obiectul customers dispare, nu doar rândurile din el.
    // Producție: fără acoladă, DROP TABLE customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este TRUNCATE TABLE. TRUNCATE lasă definiția.
    {
        // Începător: Obiectul customers dispare, nu doar rândurile din el.
        // Producție: return predă DROP TABLE customers. Fără return, fără el aplicația încă trimite INSERT către o tabelă ștearsă.
        // Expert: return nu face commit și nu ține lacăt. Nu este TRUNCATE TABLE. TRUNCATE lasă definiția.
        return """
                DROP TABLE customers
                """;
    // Începător: închide întrebarea despre: Obiectul customers dispare, nu doar rândurile din el.
    // Producție: fără această acoladă, q07DropTable nu se închide.
    // Expert: închiderea nu execută DROP TABLE customers și nu schimbă customers.
    }

    // Enunț: Ce scoate REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'?
    // Capcană: Șterge rândurile inserate de jeffrey în customers.
    // Cheie: Scoate privilegiul de INSERT. Nu șterge rânduri din customers și face commit implicit.
    // Începător: jeffrey pierde dreptul de INSERT, iar rândurile din customers rămân.
    // Producție: REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' Dacă metoda dispare, fără ea REVOKE este folosit ca DELETE.
    // Expert: Nu este DELETE FROM customers. Privilegiul stă în mysql, nu în rândul cu customer_id 4.
    /*
    🟢 Începător
        Ce este            jeffrey pierde dreptul de INSERT, iar rândurile din customers rămân.
        De ce e nevoie    fără ea REVOKE este folosit ca DELETE
    🟡 Producție
        Cum se scrie       REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'
    🔴 Staff
        Unde               Nu este DELETE FROM customers. Privilegiul stă în mysql, nu în rândul cu customer_id 4.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q08Revoke()
    // Începător: deschide propoziția pentru: jeffrey pierde dreptul de INSERT, iar rândurile din customers rămân.
    // Producție: fără acoladă, REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este DELETE FROM customers. Privilegiul stă în mysql, nu în rândul cu customer_id 4.
    {
        // Începător: jeffrey pierde dreptul de INSERT, iar rândurile din customers rămân.
        // Producție: return predă REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'. Fără return, fără ea REVOKE este folosit ca DELETE.
        // Expert: return nu face commit și nu ține lacăt. Nu este DELETE FROM customers. Privilegiul stă în mysql, nu în rândul cu customer_id 4.
        return """
                REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'
                """;
    // Începător: închide întrebarea despre: jeffrey pierde dreptul de INSERT, iar rândurile din customers rămân.
    // Producție: fără această acoladă, q08Revoke nu se închide.
    // Expert: închiderea nu execută REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' și nu schimbă customers.
    }

    // Enunț: Ce face START TRANSACTION asupra unei tranzacții deja deschise?
    // Capcană: Deschide o a doua tranzacție imbricată, iar ROLLBACK o anulează doar pe cea din interior.
    // Cheie: Comite tranzacția care era deschisă, apoi deschide una nouă. Tranzacțiile nu se imbrică.
    // Începător: Tranzacția veche este comisă și începe una nouă, cu autocommit oprit până la COMMIT sau ROLLBACK.
    // Producție: START TRANSACTION Dacă metoda dispare, fără ea un al doilea START TRANSACTION este crezut inofensiv.
    // Expert: Nu este BEGIN ... END dintr-o rutină. În procedură, BEGIN deschide blocul, nu tranzacția.
    /*
    🟢 Începător
        Ce este            Tranzacția veche este comisă și începe una nouă, cu autocommit oprit până la COMMIT sau ROLLBACK.
        De ce e nevoie    fără ea un al doilea START TRANSACTION este crezut inofensiv
    🟡 Producție
        Cum se scrie       START TRANSACTION
    🔴 Staff
        Unde               Nu este BEGIN ... END dintr-o rutină. În procedură, BEGIN deschide blocul, nu tranzacția.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q09StartTransaction()
    // Începător: deschide propoziția pentru: Tranzacția veche este comisă și începe una nouă, cu autocommit oprit până la COMMIT sau ROLLBACK.
    // Producție: fără acoladă, START TRANSACTION nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este BEGIN ... END dintr-o rutină. În procedură, BEGIN deschide blocul, nu tranzacția.
    {
        // Începător: Tranzacția veche este comisă și începe una nouă, cu autocommit oprit până la COMMIT sau ROLLBACK.
        // Producție: return predă START TRANSACTION. Fără return, fără ea un al doilea START TRANSACTION este crezut inofensiv.
        // Expert: return nu face commit și nu ține lacăt. Nu este BEGIN ... END dintr-o rutină. În procedură, BEGIN deschide blocul, nu tranzacția.
        return """
                START TRANSACTION
                """;
    // Începător: închide întrebarea despre: Tranzacția veche este comisă și începe una nouă, cu autocommit oprit până la COMMIT sau ROLLBACK.
    // Producție: fără această acoladă, q09StartTransaction nu se închide.
    // Expert: închiderea nu execută START TRANSACTION și nu schimbă customers.
    }

    // Enunț: SELECT customer_id FROM customers schimbă rândurile păstrate?
    // Capcană: SELECT cu WHERE customer_id = 4 șterge restul.
    // Cheie: Citește. Nu schimbă schema, nu șterge rândul păstrat și nu ține loc de DELETE.
    // Începător: Rezultatul pleacă spre client, iar customers rămâne cum era.
    // Producție: SELECT customer_id FROM customers Dacă metoda dispare, fără ea citirea este crezută o schimbare de rând.
    // Expert: Nu este DELETE. EXPLAIN pe acest SELECT arată planul și tot nu schimbă rândul.
    /*
    🟢 Începător
        Ce este            Rezultatul pleacă spre client, iar customers rămâne cum era.
        De ce e nevoie    fără ea citirea este crezută o schimbare de rând
    🟡 Producție
        Cum se scrie       SELECT customer_id FROM customers
    🔴 Staff
        Unde               Nu este DELETE. EXPLAIN pe acest SELECT arată planul și tot nu schimbă rândul.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q10SelectReads()
    // Începător: deschide propoziția pentru: Rezultatul pleacă spre client, iar customers rămâne cum era.
    // Producție: fără acoladă, SELECT customer_id FROM customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este DELETE. EXPLAIN pe acest SELECT arată planul și tot nu schimbă rândul.
    {
        // Începător: Rezultatul pleacă spre client, iar customers rămâne cum era.
        // Producție: return predă SELECT customer_id FROM customers. Fără return, fără ea citirea este crezută o schimbare de rând.
        // Expert: return nu face commit și nu ține lacăt. Nu este DELETE. EXPLAIN pe acest SELECT arată planul și tot nu schimbă rândul.
        return """
                SELECT customer_id FROM customers
                """;
    // Începător: închide întrebarea despre: Rezultatul pleacă spre client, iar customers rămâne cum era.
    // Producție: fără această acoladă, q10SelectReads nu se închide.
    // Expert: închiderea nu execută SELECT customer_id FROM customers și nu schimbă customers.
    }

    // Enunț: Cine vede rândul 4 între INSERT și COMMIT, în tranzacția deschisă?
    // Capcană: Îl văd toate sesiunile, pentru că INSERT s-a terminat.
    // Cheie: Îl vede sesiunea care l-a scris. Durabil pentru ceilalți devine la COMMIT.
    // Începător: Sesiunea curentă vede rândul 4 înainte ca el să fie durabil.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără ea un SELECT din aceeași sesiune este luat drept COMMIT.
    // Expert: Nu este un lacăt de tabelă. Lacătul de rând ține până la COMMIT sau ROLLBACK, nu până la sfârșitul lui INSERT.
    /*
    🟢 Începător
        Ce este            Sesiunea curentă vede rândul 4 înainte ca el să fie durabil.
        De ce e nevoie    fără ea un SELECT din aceeași sesiune este luat drept COMMIT
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4);
    🔴 Staff
        Unde               Nu este un lacăt de tabelă. Lacătul de rând ține până la COMMIT sau ROLLBACK, nu până la sfârșitul lui INSERT.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q11SameSessionSeesInsert()
    // Începător: deschide propoziția pentru: Sesiunea curentă vede rândul 4 înainte ca el să fie durabil.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este un lacăt de tabelă. Lacătul de rând ține până la COMMIT sau ROLLBACK, nu până la sfârșitul lui INSERT.
    {
        // Începător: Sesiunea curentă vede rândul 4 înainte ca el să fie durabil.
        // Producție: return predă START TRANSACTION;. Fără return, fără ea un SELECT din aceeași sesiune este luat drept COMMIT.
        // Expert: return nu face commit și nu ține lacăt. Nu este un lacăt de tabelă. Lacătul de rând ține până la COMMIT sau ROLLBACK, nu până la sfârșitul lui INSERT.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                """;
    // Începător: închide întrebarea despre: Sesiunea curentă vede rândul 4 înainte ca el să fie durabil.
    // Producție: fără această acoladă, q11SameSessionSeesInsert nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: Când se vede tabela creată cu CREATE TABLE customers?
    // Capcană: Se vede abia la COMMIT scris de client.
    // Cheie: Se vede la sfârșitul propoziției, pentru că DDL netemporar face commit implicit.
    // Începător: Tabela customers există înainte de un COMMIT scris de tine.
    // Producție: CREATE TABLE customers (customer_id INT PRIMARY KEY) Dacă metoda dispare, fără ea aștepți un COMMIT ca să vezi schema.
    // Expert: Nu este INSERT. Rândul inserat după START TRANSACTION nu se vede durabil la sfârșitul lui INSERT.
    /*
    🟢 Începător
        Ce este            Tabela customers există înainte de un COMMIT scris de tine.
        De ce e nevoie    fără ea aștepți un COMMIT ca să vezi schema
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY)
    🔴 Staff
        Unde               Nu este INSERT. Rândul inserat după START TRANSACTION nu se vede durabil la sfârșitul lui INSERT.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q12CreateVisibleAtStatementEnd()
    // Începător: deschide propoziția pentru: Tabela customers există înainte de un COMMIT scris de tine.
    // Producție: fără acoladă, CREATE TABLE customers (customer_id INT PRIMARY KEY) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT. Rândul inserat după START TRANSACTION nu se vede durabil la sfârșitul lui INSERT.
    {
        // Începător: Tabela customers există înainte de un COMMIT scris de tine.
        // Producție: return predă CREATE TABLE customers (customer_id INT PRIMARY KEY). Fără return, fără ea aștepți un COMMIT ca să vezi schema.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT. Rândul inserat după START TRANSACTION nu se vede durabil la sfârșitul lui INSERT.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY)
                """;
    // Începător: închide întrebarea despre: Tabela customers există înainte de un COMMIT scris de tine.
    // Producție: fără această acoladă, q12CreateVisibleAtStatementEnd nu se închide.
    // Expert: închiderea nu execută CREATE TABLE customers (customer_id INT PRIMARY KEY) și nu schimbă customers.
    }

    // Enunț: Până când ține InnoDB lacătul rândului inserat în tranzacție?
    // Capcană: Îl eliberează la sfârșitul lui INSERT.
    // Cheie: Îl ține până la COMMIT sau până la ROLLBACK complet. Un eșec pe o singură propoziție nu eliberează lacătele tranzacției.
    // Începător: Lacătul rândului 4 rămâne după INSERT, cât tranzacția este deschisă.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără el următoarea sesiune se blochează și nu știi de ce.
    // Expert: Nu este lacătul unui CREATE TABLE. DDL a comis deja și nu ține lacătul acestui INSERT.
    /*
    🟢 Începător
        Ce este            Lacătul rândului 4 rămâne după INSERT, cât tranzacția este deschisă.
        De ce e nevoie    fără el următoarea sesiune se blochează și nu știi de ce
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4);
    🔴 Staff
        Unde               Nu este lacătul unui CREATE TABLE. DDL a comis deja și nu ține lacătul acestui INSERT.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q13InsertLockUntilRollback()
    // Începător: deschide propoziția pentru: Lacătul rândului 4 rămâne după INSERT, cât tranzacția este deschisă.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este lacătul unui CREATE TABLE. DDL a comis deja și nu ține lacătul acestui INSERT.
    {
        // Începător: Lacătul rândului 4 rămâne după INSERT, cât tranzacția este deschisă.
        // Producție: return predă START TRANSACTION;. Fără return, fără el următoarea sesiune se blochează și nu știi de ce.
        // Expert: return nu face commit și nu ține lacăt. Nu este lacătul unui CREATE TABLE. DDL a comis deja și nu ține lacătul acestui INSERT.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                """;
    // Începător: închide întrebarea despre: Lacătul rândului 4 rămâne după INSERT, cât tranzacția este deschisă.
    // Producție: fără această acoladă, q13InsertLockUntilRollback nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: CREATE INDEX idx_customers_id ON customers (customer_id) filtrează rândurile?
    // Capcană: Este un WHERE permanent, deci INSERT-ul din tranzacție rămâne anulabil.
    // Cheie: Este DDL. Face commit implicit. Un INSERT necomis de dinaintea lui devine durabil.
    // Începător: Indexul apare și tranzacția deschisă se închide.
    // Producție: CREATE INDEX idx_customers_id ON customers (customer_id) Dacă metoda dispare, fără el indexul este creat în mijlocul unei tranzacții pe care voiai să o anulezi.
    // Expert: Nu este un filtru WHERE. Nu garantează că planul îl va folosi.
    /*
    🟢 Începător
        Ce este            Indexul apare și tranzacția deschisă se închide.
        De ce e nevoie    fără el indexul este creat în mijlocul unei tranzacții pe care voiai să o anulezi
    🟡 Producție
        Cum se scrie       CREATE INDEX idx_customers_id ON customers (customer_id)
    🔴 Staff
        Unde               Nu este un filtru WHERE. Nu garantează că planul îl va folosi.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q14CreateIndexCommits()
    // Începător: deschide propoziția pentru: Indexul apare și tranzacția deschisă se închide.
    // Producție: fără acoladă, CREATE INDEX idx_customers_id ON customers (customer_id) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este un filtru WHERE. Nu garantează că planul îl va folosi.
    {
        // Începător: Indexul apare și tranzacția deschisă se închide.
        // Producție: return predă CREATE INDEX idx_customers_id ON customers (customer_id). Fără return, fără el indexul este creat în mijlocul unei tranzacții pe care voiai să o anulezi.
        // Expert: return nu face commit și nu ține lacăt. Nu este un filtru WHERE. Nu garantează că planul îl va folosi.
        return """
                CREATE INDEX idx_customers_id ON customers (customer_id)
                """;
    // Începător: închide întrebarea despre: Indexul apare și tranzacția deschisă se închide.
    // Producție: fără această acoladă, q14CreateIndexCommits nu se închide.
    // Expert: închiderea nu execută CREATE INDEX idx_customers_id ON customers (customer_id) și nu schimbă customers.
    }

    // Enunț: DELETE FROM customers WHERE customer_id = 4, în START TRANSACTION, poate dispărea la ROLLBACK?
    // Capcană: Orice ștergere este ca TRUNCATE și rămâne.
    // Cheie: DELETE este DML. În tranzacția deschisă, ROLLBACK pune rândul înapoi. TRUNCATE nu.
    // Începător: Rândul 4 poate reveni la ROLLBACK dacă ștergerea nu a fost comisă.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără ea DELETE și TRUNCATE sunt alese la fel.
    // Expert: Nu este TRUNCATE TABLE customers. DELETE nu golește restul rândurilor și nu face commit implicit.
    /*
    🟢 Începător
        Ce este            Rândul 4 poate reveni la ROLLBACK dacă ștergerea nu a fost comisă.
        De ce e nevoie    fără ea DELETE și TRUNCATE sunt alese la fel
    🟡 Producție
        Cum se scrie       START TRANSACTION; DELETE FROM customers WHERE customer_id = 4; ROLLBACK;
    🔴 Staff
        Unde               Nu este TRUNCATE TABLE customers. DELETE nu golește restul rândurilor și nu face commit implicit.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q15DeleteCanRollBack()
    // Începător: deschide propoziția pentru: Rândul 4 poate reveni la ROLLBACK dacă ștergerea nu a fost comisă.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este TRUNCATE TABLE customers. DELETE nu golește restul rândurilor și nu face commit implicit.
    {
        // Începător: Rândul 4 poate reveni la ROLLBACK dacă ștergerea nu a fost comisă.
        // Producție: return predă START TRANSACTION;. Fără return, fără ea DELETE și TRUNCATE sunt alese la fel.
        // Expert: return nu face commit și nu ține lacăt. Nu este TRUNCATE TABLE customers. DELETE nu golește restul rândurilor și nu face commit implicit.
        return """
                START TRANSACTION;
                DELETE FROM customers WHERE customer_id = 4;
                ROLLBACK;
                """;
    // Începător: închide întrebarea despre: Rândul 4 poate reveni la ROLLBACK dacă ștergerea nu a fost comisă.
    // Producție: fără această acoladă, q15DeleteCanRollBack nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4, după START TRANSACTION, când rămâne?
    // Capcană: Rămâne la sfârșitul lui UPDATE, iar COMMIT este opțional.
    // Cheie: În tranzacția deschisă rămâne durabil la COMMIT. Cu autocommit=1, fără START TRANSACTION, rămâne la sfârșitul lui UPDATE.
    // Începător: Numele Johnson devine durabil la COMMIT, nu la UPDATE.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără el UPDATE este crezut mereu anulabil sau mereu imediat.
    // Expert: Nu este INSERT. UPDATE schimbă rândul care există. Nu creează customers.
    /*
    🟢 Începător
        Ce este            Numele Johnson devine durabil la COMMIT, nu la UPDATE.
        De ce e nevoie    fără el UPDATE este crezut mereu anulabil sau mereu imediat
    🟡 Producție
        Cum se scrie       START TRANSACTION; UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4; COMMIT;
    🔴 Staff
        Unde               Nu este INSERT. UPDATE schimbă rândul care există. Nu creează customers.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q16UpdateDurableAtCommit()
    // Începător: deschide propoziția pentru: Numele Johnson devine durabil la COMMIT, nu la UPDATE.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT. UPDATE schimbă rândul care există. Nu creează customers.
    {
        // Începător: Numele Johnson devine durabil la COMMIT, nu la UPDATE.
        // Producție: return predă START TRANSACTION;. Fără return, fără el UPDATE este crezut mereu anulabil sau mereu imediat.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT. UPDATE schimbă rândul care există. Nu creează customers.
        return """
                START TRANSACTION;
                UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4;
                COMMIT;
                """;
    // Începător: închide întrebarea despre: Numele Johnson devine durabil la COMMIT, nu la UPDATE.
    // Producție: fără această acoladă, q16UpdateDurableAtCommit nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: Unde se vede efectul lui GRANT ALL ON db1.* TO 'jeffrey'@'localhost'?
    // Capcană: Ca un rând nou în customers.
    // Cheie: În privilegiul contului, ținut de tabelele din schema mysql. Nu ca rând de business.
    // Începător: Privilegiul lui jeffrey se schimbă în mysql, nu numărul de rânduri din customers.
    // Producție: GRANT ALL ON db1.* TO 'jeffrey'@'localhost' Dacă metoda dispare, fără ea un SELECT în customers este folosit ca să verifici GRANT.
    // Expert: Nu este INSERT INTO customers. Commit-ul implicit nu arată rândul 4.
    /*
    🟢 Începător
        Ce este            Privilegiul lui jeffrey se schimbă în mysql, nu numărul de rânduri din customers.
        De ce e nevoie    fără ea un SELECT în customers este folosit ca să verifici GRANT
    🟡 Producție
        Cum se scrie       GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
    🔴 Staff
        Unde               Nu este INSERT INTO customers. Commit-ul implicit nu arată rândul 4.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q17GrantIsMysqlSchema()
    // Începător: deschide propoziția pentru: Privilegiul lui jeffrey se schimbă în mysql, nu numărul de rânduri din customers.
    // Producție: fără acoladă, GRANT ALL ON db1.* TO 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT INTO customers. Commit-ul implicit nu arată rândul 4.
    {
        // Începător: Privilegiul lui jeffrey se schimbă în mysql, nu numărul de rânduri din customers.
        // Producție: return predă GRANT ALL ON db1.* TO 'jeffrey'@'localhost'. Fără return, fără ea un SELECT în customers este folosit ca să verifici GRANT.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT INTO customers. Commit-ul implicit nu arată rândul 4.
        return """
                GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
                """;
    // Începător: închide întrebarea despre: Privilegiul lui jeffrey se schimbă în mysql, nu numărul de rânduri din customers.
    // Producție: fără această acoladă, q17GrantIsMysqlSchema nu se închide.
    // Expert: închiderea nu execută GRANT ALL ON db1.* TO 'jeffrey'@'localhost' și nu schimbă customers.
    }

    // Enunț: Cu autocommit lăsat pe 1, ce face un ROLLBACK trimis după INSERT INTO customers (customer_id) VALUES (4)?
    // Capcană: Scoate rândul 4, pentru că nu ai scris COMMIT.
    // Cheie: Nu scoate rândul. Propoziția a fost deja o tranzacție la sfârșitul lui INSERT.
    // Începător: Rândul 4 este durabil la sfârșitul lui INSERT, cât autocommit rămâne 1.
    // Producție: INSERT INTO customers (customer_id) VALUES (4) Dacă metoda dispare, fără ea fiecare INSERT este urmat de un COMMIT inutil și de un ROLLBACK care nu mai are ce anula.
    // Expert: Nu este START TRANSACTION. Nu ține rândul anulabil.
    /*
    🟢 Începător
        Ce este            Rândul 4 este durabil la sfârșitul lui INSERT, cât autocommit rămâne 1.
        De ce e nevoie    fără ea fiecare INSERT este urmat de un COMMIT inutil și de un ROLLBACK care nu mai are ce anula
    🟡 Producție
        Cum se scrie       INSERT INTO customers (customer_id) VALUES (4)
    🔴 Staff
        Unde               Nu este START TRANSACTION. Nu ține rândul anulabil.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q18AutocommitInsertStays()
    // Începător: deschide propoziția pentru: Rândul 4 este durabil la sfârșitul lui INSERT, cât autocommit rămâne 1.
    // Producție: fără acoladă, INSERT INTO customers (customer_id) VALUES (4) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este START TRANSACTION. Nu ține rândul anulabil.
    {
        // Începător: Rândul 4 este durabil la sfârșitul lui INSERT, cât autocommit rămâne 1.
        // Producție: return predă INSERT INTO customers (customer_id) VALUES (4). Fără return, fără ea fiecare INSERT este urmat de un COMMIT inutil și de un ROLLBACK care nu mai are ce anula.
        // Expert: return nu face commit și nu ține lacăt. Nu este START TRANSACTION. Nu ține rândul anulabil.
        return """
                INSERT INTO customers (customer_id) VALUES (4)
                """;
    // Începător: închide întrebarea despre: Rândul 4 este durabil la sfârșitul lui INSERT, cât autocommit rămâne 1.
    // Producție: fără această acoladă, q18AutocommitInsertStays nu se închide.
    // Expert: închiderea nu execută INSERT INTO customers (customer_id) VALUES (4) și nu schimbă customers.
    }

    // Enunț: EXPLAIN SELECT customer_id FROM customers schimbă rândurile?
    // Capcană: Dacă planul arată customers, SELECT a și scris ceva.
    // Cheie: EXPLAIN arată planul. Nu schimbă rânduri și nu ține loc de INSERT sau UPDATE.
    // Începător: Planul pleacă spre client, iar customers rămâne neschimbat.
    // Producție: EXPLAIN SELECT customer_id FROM customers Dacă metoda dispare, fără el EXPLAIN este rulat ca să repare datele.
    // Expert: Nu este INSERT. Planul nu face commit și nu ține lacătul unui rând modificat.
    /*
    🟢 Începător
        Ce este            Planul pleacă spre client, iar customers rămâne neschimbat.
        De ce e nevoie    fără el EXPLAIN este rulat ca să repare datele
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT customer_id FROM customers
    🔴 Staff
        Unde               Nu este INSERT. Planul nu face commit și nu ține lacătul unui rând modificat.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q19ExplainDoesNotWrite()
    // Începător: deschide propoziția pentru: Planul pleacă spre client, iar customers rămâne neschimbat.
    // Producție: fără acoladă, EXPLAIN SELECT customer_id FROM customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT. Planul nu face commit și nu ține lacătul unui rând modificat.
    {
        // Începător: Planul pleacă spre client, iar customers rămâne neschimbat.
        // Producție: return predă EXPLAIN SELECT customer_id FROM customers. Fără return, fără el EXPLAIN este rulat ca să repare datele.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT. Planul nu face commit și nu ține lacătul unui rând modificat.
        return """
                EXPLAIN SELECT customer_id FROM customers
                """;
    // Începător: închide întrebarea despre: Planul pleacă spre client, iar customers rămâne neschimbat.
    // Producție: fără această acoladă, q19ExplainDoesNotWrite nu se închide.
    // Expert: închiderea nu execută EXPLAIN SELECT customer_id FROM customers și nu schimbă customers.
    }

    // Enunț: Al doilea INSERT cu același customer_id eșuează în tranzacție. Ce lacăte rămân?
    // Capcană: Eșecul propoziției face ROLLBACK la toată tranzacția și eliberează tot.
    // Cheie: Propoziția eșuată se anulează, tranzacția rămâne deschisă, iar InnoDB păstrează lacătele până la COMMIT sau ROLLBACK.
    // Începător: Cheia duplicată anulează a doua propoziție și lasă tranzacția deschisă.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără ea sesiunea crede că a ieșit din tranzacție după eroare.
    // Expert: Nu este ROLLBACK. Eroarea de cheie nu eliberează lacătele ținute de tranzacție.
    /*
    🟢 Începător
        Ce este            Cheia duplicată anulează a doua propoziție și lasă tranzacția deschisă.
        De ce e nevoie    fără ea sesiunea crede că a ieșit din tranzacție după eroare
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); INSERT INTO customers (customer_id) VALUES (4);
    🔴 Staff
        Unde               Nu este ROLLBACK. Eroarea de cheie nu eliberează lacătele ținute de tranzacție.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q20StatementErrorKeepsLocks()
    // Începător: deschide propoziția pentru: Cheia duplicată anulează a doua propoziție și lasă tranzacția deschisă.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este ROLLBACK. Eroarea de cheie nu eliberează lacătele ținute de tranzacție.
    {
        // Începător: Cheia duplicată anulează a doua propoziție și lasă tranzacția deschisă.
        // Producție: return predă START TRANSACTION;. Fără return, fără ea sesiunea crede că a ieșit din tranzacție după eroare.
        // Expert: return nu face commit și nu ține lacăt. Nu este ROLLBACK. Eroarea de cheie nu eliberează lacătele ținute de tranzacție.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                INSERT INTO customers (customer_id) VALUES (4);
                """;
    // Începător: închide întrebarea despre: Cheia duplicată anulează a doua propoziție și lasă tranzacția deschisă.
    // Producție: fără această acoladă, q20StatementErrorKeepsLocks nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: De ce CREATE TABLE customers nu este INSERT INTO customers?
    // Capcană: Ambele adaugă, deci stau la fel în tranzacție.
    // Cheie: CREATE schimbă schema și face commit implicit. INSERT schimbă un rând și poate rămâne necomis.
    // Începător: Alegerea de schemă nu ține rândul anulabil.
    // Producție: CREATE TABLE customers (customer_id INT PRIMARY KEY) Dacă metoda dispare, fără ea CREATE este pus în tranzacția în care voiai să anulezi un rând.
    // Expert: Vecinul este INSERT INTO customers (customer_id) VALUES (4). Le separă obiectul schimbat.
    /*
    🟢 Începător
        Ce este            Alegerea de schemă nu ține rândul anulabil.
        De ce e nevoie    fără ea CREATE este pus în tranzacția în care voiai să anulezi un rând
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY)
    🔴 Staff
        Unde               Vecinul este INSERT INTO customers (customer_id) VALUES (4). Le separă obiectul schimbat.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q21DdlNotDml()
    // Începător: deschide propoziția pentru: Alegerea de schemă nu ține rândul anulabil.
    // Producție: fără acoladă, CREATE TABLE customers (customer_id INT PRIMARY KEY) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Vecinul este INSERT INTO customers (customer_id) VALUES (4). Le separă obiectul schimbat.
    {
        // Începător: Alegerea de schemă nu ține rândul anulabil.
        // Producție: return predă CREATE TABLE customers (customer_id INT PRIMARY KEY). Fără return, fără ea CREATE este pus în tranzacția în care voiai să anulezi un rând.
        // Expert: return nu face commit și nu ține lacăt. Vecinul este INSERT INTO customers (customer_id) VALUES (4). Le separă obiectul schimbat.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY)
                """;
    // Începător: închide întrebarea despre: Alegerea de schemă nu ține rândul anulabil.
    // Producție: fără această acoladă, q21DdlNotDml nu se închide.
    // Expert: închiderea nu execută CREATE TABLE customers (customer_id INT PRIMARY KEY) și nu schimbă customers.
    }

    // Enunț: De ce TRUNCATE TABLE customers nu este DELETE FROM customers WHERE customer_id = 4?
    // Capcană: Ambele șterg, deci ROLLBACK le tratează la fel.
    // Cheie: TRUNCATE este DDL, golește tabela și face commit implicit. DELETE cu WHERE scoate rândurile alese și poate fi anulat.
    // Începător: Golește customers și lasă definiția, deja comis.
    // Producție: TRUNCATE TABLE customers Dacă metoda dispare, fără el un singur client este șters golind toată tabela.
    // Expert: Nu este DELETE. Nu primește WHERE și nu stă necomis între START TRANSACTION și ROLLBACK.
    /*
    🟢 Începător
        Ce este            Golește customers și lasă definiția, deja comis.
        De ce e nevoie    fără el un singur client este șters golind toată tabela
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE customers
    🔴 Staff
        Unde               Nu este DELETE. Nu primește WHERE și nu stă necomis între START TRANSACTION și ROLLBACK.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q22TruncateNotDelete()
    // Începător: deschide propoziția pentru: Golește customers și lasă definiția, deja comis.
    // Producție: fără acoladă, TRUNCATE TABLE customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este DELETE. Nu primește WHERE și nu stă necomis între START TRANSACTION și ROLLBACK.
    {
        // Începător: Golește customers și lasă definiția, deja comis.
        // Producție: return predă TRUNCATE TABLE customers. Fără return, fără el un singur client este șters golind toată tabela.
        // Expert: return nu face commit și nu ține lacăt. Nu este DELETE. Nu primește WHERE și nu stă necomis între START TRANSACTION și ROLLBACK.
        return """
                TRUNCATE TABLE customers
                """;
    // Începător: închide întrebarea despre: Golește customers și lasă definiția, deja comis.
    // Producție: fără această acoladă, q22TruncateNotDelete nu se închide.
    // Expert: închiderea nu execută TRUNCATE TABLE customers și nu schimbă customers.
    }

    // Enunț: De ce DROP TABLE customers nu este TRUNCATE TABLE customers?
    // Capcană: Ambele lasă tabelul gata de umplut din nou.
    // Cheie: DROP scoate obiectul. TRUNCATE lasă definiția. Ambele fac commit implicit.
    // Începător: După DROP nu mai există customers, nu doar că nu mai are rânduri.
    // Producție: DROP TABLE customers Dacă metoda dispare, fără el codul de după încă folosește tabela.
    // Expert: Nu este TRUNCATE. Nu păstrează customer_id ca coloană.
    /*
    🟢 Începător
        Ce este            După DROP nu mai există customers, nu doar că nu mai are rânduri.
        De ce e nevoie    fără el codul de după încă folosește tabela
    🟡 Producție
        Cum se scrie       DROP TABLE customers
    🔴 Staff
        Unde               Nu este TRUNCATE. Nu păstrează customer_id ca coloană.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q23DropNotTruncate()
    // Începător: deschide propoziția pentru: După DROP nu mai există customers, nu doar că nu mai are rânduri.
    // Producție: fără acoladă, DROP TABLE customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este TRUNCATE. Nu păstrează customer_id ca coloană.
    {
        // Începător: După DROP nu mai există customers, nu doar că nu mai are rânduri.
        // Producție: return predă DROP TABLE customers. Fără return, fără el codul de după încă folosește tabela.
        // Expert: return nu face commit și nu ține lacăt. Nu este TRUNCATE. Nu păstrează customer_id ca coloană.
        return """
                DROP TABLE customers
                """;
    // Începător: închide întrebarea despre: După DROP nu mai există customers, nu doar că nu mai are rânduri.
    // Producție: fără această acoladă, q23DropNotTruncate nu se închide.
    // Expert: închiderea nu execută DROP TABLE customers și nu schimbă customers.
    }

    // Enunț: De ce UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4 nu este INSERT?
    // Capcană: Ambele scriu Johnson, deci adaugă un client.
    // Cheie: UPDATE schimbă rândul care există. INSERT adaugă un rând. Niciunul nu schimbă schema.
    // Începător: Rândul 4, dacă există, își schimbă numele. Nu apare un al doilea client.
    // Producție: UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4 Dacă metoda dispare, fără el un client lipsă este „actualizat” și nu apare.
    // Expert: Nu este INSERT INTO customers. Nu este CREATE TABLE.
    /*
    🟢 Începător
        Ce este            Rândul 4, dacă există, își schimbă numele. Nu apare un al doilea client.
        De ce e nevoie    fără el un client lipsă este „actualizat” și nu apare
    🟡 Producție
        Cum se scrie       UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4
    🔴 Staff
        Unde               Nu este INSERT INTO customers. Nu este CREATE TABLE.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q24UpdateNotInsert()
    // Începător: deschide propoziția pentru: Rândul 4, dacă există, își schimbă numele. Nu apare un al doilea client.
    // Producție: fără acoladă, UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4 nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT INTO customers. Nu este CREATE TABLE.
    {
        // Începător: Rândul 4, dacă există, își schimbă numele. Nu apare un al doilea client.
        // Producție: return predă UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4. Fără return, fără el un client lipsă este „actualizat” și nu apare.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT INTO customers. Nu este CREATE TABLE.
        return """
                UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4
                """;
    // Începător: închide întrebarea despre: Rândul 4, dacă există, își schimbă numele. Nu apare un al doilea client.
    // Producție: fără această acoladă, q24UpdateNotInsert nu se închide.
    // Expert: închiderea nu execută UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4 și nu schimbă customers.
    }

    // Enunț: De ce GRANT ALL ON db1.* TO 'jeffrey'@'localhost' nu este INSERT INTO customers?
    // Capcană: GRANT ALL adaugă tot, deci și rândurile.
    // Cheie: GRANT schimbă privilegiul și face commit implicit. INSERT adaugă un rând în customers și poate rămâne necomis.
    // Începător: Dreptul se schimbă în mysql, nu se adaugă customer_id 4.
    // Producție: GRANT ALL ON db1.* TO 'jeffrey'@'localhost' Dacă metoda dispare, fără el un cont primește date în loc de drept.
    // Expert: Nu este INSERT. Nu garantează un rând nou în customers.
    /*
    🟢 Începător
        Ce este            Dreptul se schimbă în mysql, nu se adaugă customer_id 4.
        De ce e nevoie    fără el un cont primește date în loc de drept
    🟡 Producție
        Cum se scrie       GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
    🔴 Staff
        Unde               Nu este INSERT. Nu garantează un rând nou în customers.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q25GrantNotInsert()
    // Începător: deschide propoziția pentru: Dreptul se schimbă în mysql, nu se adaugă customer_id 4.
    // Producție: fără acoladă, GRANT ALL ON db1.* TO 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT. Nu garantează un rând nou în customers.
    {
        // Începător: Dreptul se schimbă în mysql, nu se adaugă customer_id 4.
        // Producție: return predă GRANT ALL ON db1.* TO 'jeffrey'@'localhost'. Fără return, fără el un cont primește date în loc de drept.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT. Nu garantează un rând nou în customers.
        return """
                GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
                """;
    // Începător: închide întrebarea despre: Dreptul se schimbă în mysql, nu se adaugă customer_id 4.
    // Producție: fără această acoladă, q25GrantNotInsert nu se închide.
    // Expert: închiderea nu execută GRANT ALL ON db1.* TO 'jeffrey'@'localhost' și nu schimbă customers.
    }

    // Enunț: De ce COMMIT nu este ROLLBACK?
    // Capcană: Ambele închid tranzacția, deci rândul dispare la fel.
    // Cheie: COMMIT face schimbarea durabilă. ROLLBACK o anulează. Ambele termină tranzacția deschisă cu START TRANSACTION.
    // Începător: Rândul necomis rămâne după COMMIT și nu rămâne după ROLLBACK.
    // Producție: COMMIT Dacă metoda dispare, fără el închiderea tranzacției este aleasă pe dos.
    // Expert: Nu este GRANT. COMMIT nu schimbă un privilegiu.
    /*
    🟢 Începător
        Ce este            Rândul necomis rămâne după COMMIT și nu rămâne după ROLLBACK.
        De ce e nevoie    fără el închiderea tranzacției este aleasă pe dos
    🟡 Producție
        Cum se scrie       COMMIT
    🔴 Staff
        Unde               Nu este GRANT. COMMIT nu schimbă un privilegiu.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q26CommitNotRollback()
    // Începător: deschide propoziția pentru: Rândul necomis rămâne după COMMIT și nu rămâne după ROLLBACK.
    // Producție: fără acoladă, COMMIT nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este GRANT. COMMIT nu schimbă un privilegiu.
    {
        // Începător: Rândul necomis rămâne după COMMIT și nu rămâne după ROLLBACK.
        // Producție: return predă COMMIT. Fără return, fără el închiderea tranzacției este aleasă pe dos.
        // Expert: return nu face commit și nu ține lacăt. Nu este GRANT. COMMIT nu schimbă un privilegiu.
        return """
                COMMIT
                """;
    // Începător: închide întrebarea despre: Rândul necomis rămâne după COMMIT și nu rămâne după ROLLBACK.
    // Producție: fără această acoladă, q26CommitNotRollback nu se închide.
    // Expert: închiderea nu execută COMMIT și nu schimbă customers.
    }

    // Enunț: De ce mai trimiți START TRANSACTION dacă autocommit este deja 1?
    // Capcană: START TRANSACTION este obligatoriu la fiecare INSERT, altfel rândul nu se salvează.
    // Cheie: Cu autocommit=1, INSERT este deja durabil. START TRANSACTION îl oprește doar pentru seria care urmează, până la COMMIT sau ROLLBACK.
    // Începător: Autocommit se oprește pentru tranzacția nouă și revine după COMMIT sau ROLLBACK.
    // Producție: START TRANSACTION Dacă metoda dispare, fără el fiecare propoziție este crezută necomisă.
    // Expert: Nu este SET autocommit=0, care lasă sesiunea în modul manual și după ce tranzacția s-a închis.
    /*
    🟢 Începător
        Ce este            Autocommit se oprește pentru tranzacția nouă și revine după COMMIT sau ROLLBACK.
        De ce e nevoie    fără el fiecare propoziție este crezută necomisă
    🟡 Producție
        Cum se scrie       START TRANSACTION
    🔴 Staff
        Unde               Nu este SET autocommit=0, care lasă sesiunea în modul manual și după ce tranzacția s-a închis.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q27StartNotAutocommit()
    // Începător: deschide propoziția pentru: Autocommit se oprește pentru tranzacția nouă și revine după COMMIT sau ROLLBACK.
    // Producție: fără acoladă, START TRANSACTION nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este SET autocommit=0, care lasă sesiunea în modul manual și după ce tranzacția s-a închis.
    {
        // Începător: Autocommit se oprește pentru tranzacția nouă și revine după COMMIT sau ROLLBACK.
        // Producție: return predă START TRANSACTION. Fără return, fără el fiecare propoziție este crezută necomisă.
        // Expert: return nu face commit și nu ține lacăt. Nu este SET autocommit=0, care lasă sesiunea în modul manual și după ce tranzacția s-a închis.
        return """
                START TRANSACTION
                """;
    // Începător: închide întrebarea despre: Autocommit se oprește pentru tranzacția nouă și revine după COMMIT sau ROLLBACK.
    // Producție: fără această acoladă, q27StartNotAutocommit nu se închide.
    // Expert: închiderea nu execută START TRANSACTION și nu schimbă customers.
    }

    // Enunț: WHERE customer_id = 4 șterge rândul?
    // Capcană: Condiția singură scoate clientul 4.
    // Cheie: WHERE este o clauză. Ștergerea este propoziția DELETE FROM customers WHERE customer_id = 4.
    // Începător: Rândul 4 este ales de WHERE și scos de DELETE, nu de clauză singură.
    // Producție: DELETE FROM customers WHERE customer_id = 4 Dacă metoda dispare, fără ea filtrul este trimis în locul propoziției.
    // Expert: Nu este familia DDL. DELETE poate sta necomis. WHERE nu schimbă schema.
    /*
    🟢 Începător
        Ce este            Rândul 4 este ales de WHERE și scos de DELETE, nu de clauză singură.
        De ce e nevoie    fără ea filtrul este trimis în locul propoziției
    🟡 Producție
        Cum se scrie       DELETE FROM customers WHERE customer_id = 4
    🔴 Staff
        Unde               Nu este familia DDL. DELETE poate sta necomis. WHERE nu schimbă schema.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q28WhereIsNotTheStatement()
    // Începător: deschide propoziția pentru: Rândul 4 este ales de WHERE și scos de DELETE, nu de clauză singură.
    // Producție: fără acoladă, DELETE FROM customers WHERE customer_id = 4 nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este familia DDL. DELETE poate sta necomis. WHERE nu schimbă schema.
    {
        // Începător: Rândul 4 este ales de WHERE și scos de DELETE, nu de clauză singură.
        // Producție: return predă DELETE FROM customers WHERE customer_id = 4. Fără return, fără ea filtrul este trimis în locul propoziției.
        // Expert: return nu face commit și nu ține lacăt. Nu este familia DDL. DELETE poate sta necomis. WHERE nu schimbă schema.
        return """
                DELETE FROM customers WHERE customer_id = 4
                """;
    // Începător: închide întrebarea despre: Rândul 4 este ales de WHERE și scos de DELETE, nu de clauză singură.
    // Producție: fără această acoladă, q28WhereIsNotTheStatement nu se închide.
    // Expert: închiderea nu execută DELETE FROM customers WHERE customer_id = 4 și nu schimbă customers.
    }

    // Enunț: De ce o propoziție CALL nu ține loc de CREATE TABLE customers?
    // Capcană: CALL rulează pe server, deci poate lăsa tabela nouă.
    // Cheie: CALL este în capitolul de manipulare și execută o rutină. CREATE TABLE schimbă schema și face commit implicit.
    // Începător: Schema se schimbă cu CREATE TABLE, nu cu apelul unei rutine.
    // Producție: CREATE TABLE customers (customer_id INT PRIMARY KEY) Dacă metoda dispare, fără ea rutina este chemată când lipsește obiectul.
    // Expert: Nu este CALL. Apelul nu face commit-ul implicit al unui CREATE netemporar.
    /*
    🟢 Începător
        Ce este            Schema se schimbă cu CREATE TABLE, nu cu apelul unei rutine.
        De ce e nevoie    fără ea rutina este chemată când lipsește obiectul
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY)
    🔴 Staff
        Unde               Nu este CALL. Apelul nu face commit-ul implicit al unui CREATE netemporar.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q29CallNotCreate()
    // Începător: deschide propoziția pentru: Schema se schimbă cu CREATE TABLE, nu cu apelul unei rutine.
    // Producție: fără acoladă, CREATE TABLE customers (customer_id INT PRIMARY KEY) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este CALL. Apelul nu face commit-ul implicit al unui CREATE netemporar.
    {
        // Începător: Schema se schimbă cu CREATE TABLE, nu cu apelul unei rutine.
        // Producție: return predă CREATE TABLE customers (customer_id INT PRIMARY KEY). Fără return, fără ea rutina este chemată când lipsește obiectul.
        // Expert: return nu face commit și nu ține lacăt. Nu este CALL. Apelul nu face commit-ul implicit al unui CREATE netemporar.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY)
                """;
    // Începător: închide întrebarea despre: Schema se schimbă cu CREATE TABLE, nu cu apelul unei rutine.
    // Producție: fără această acoladă, q29CallNotCreate nu se închide.
    // Expert: închiderea nu execută CREATE TABLE customers (customer_id INT PRIMARY KEY) și nu schimbă customers.
    }

    // Enunț: Într-o procedură, BEGIN deschide tranzacția?
    // Capcană: BEGIN și START TRANSACTION sunt mereu același lucru.
    // Cheie: În rutină, BEGIN deschide blocul BEGIN ... END. Tranzacția se deschide cu START TRANSACTION.
    // Începător: În procedură, tranzacția începe doar cu START TRANSACTION.
    // Producție: START TRANSACTION Dacă metoda dispare, fără el BEGIN din rutină este crezut COMMIT implicit.
    // Expert: Nu este BEGIN ... END. Blocul nu comite tranzacția deschisă.
    /*
    🟢 Începător
        Ce este            În procedură, tranzacția începe doar cu START TRANSACTION.
        De ce e nevoie    fără el BEGIN din rutină este crezut COMMIT implicit
    🟡 Producție
        Cum se scrie       START TRANSACTION
    🔴 Staff
        Unde               Nu este BEGIN ... END. Blocul nu comite tranzacția deschisă.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q30BeginBlockNotTransaction()
    // Începător: deschide propoziția pentru: În procedură, tranzacția începe doar cu START TRANSACTION.
    // Producție: fără acoladă, START TRANSACTION nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este BEGIN ... END. Blocul nu comite tranzacția deschisă.
    {
        // Începător: În procedură, tranzacția începe doar cu START TRANSACTION.
        // Producție: return predă START TRANSACTION. Fără return, fără el BEGIN din rutină este crezut COMMIT implicit.
        // Expert: return nu face commit și nu ține lacăt. Nu este BEGIN ... END. Blocul nu comite tranzacția deschisă.
        return """
                START TRANSACTION
                """;
    // Începător: închide întrebarea despre: În procedură, tranzacția începe doar cu START TRANSACTION.
    // Producție: fără această acoladă, q30BeginBlockNotTransaction nu se închide.
    // Expert: închiderea nu execută START TRANSACTION și nu schimbă customers.
    }

    // Enunț: Ce rând rămâne dacă GRANT ALL stă între INSERT și ROLLBACK?
    // Capcană: ROLLBACK anulează și INSERT, și privilegiul.
    // Cheie: GRANT face commit implicit înainte să ruleze, deci rândul 4 rămâne. Privilegiul rămâne și el.
    // Începător: Rândul 4 și privilegiul lui jeffrey rămân amândouă.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără el un drept dat la mijlocul tranzacției îți salvează datele pe care voiai să le anulezi.
    // Expert: Nu este un COMMIT scris de tine. Serverul comite înainte de GRANT.
    /*
    🟢 Începător
        Ce este            Rândul 4 și privilegiul lui jeffrey rămân amândouă.
        De ce e nevoie    fără el un drept dat la mijlocul tranzacției îți salvează datele pe care voiai să le anulezi
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); GRANT ALL ON db1.* TO 'jeffrey'@'localhost'; ROLLBACK;
    🔴 Staff
        Unde               Nu este un COMMIT scris de tine. Serverul comite înainte de GRANT.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q31GrantCommitsInsert()
    // Începător: deschide propoziția pentru: Rândul 4 și privilegiul lui jeffrey rămân amândouă.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este un COMMIT scris de tine. Serverul comite înainte de GRANT.
    {
        // Începător: Rândul 4 și privilegiul lui jeffrey rămân amândouă.
        // Producție: return predă START TRANSACTION;. Fără return, fără el un drept dat la mijlocul tranzacției îți salvează datele pe care voiai să le anulezi.
        // Expert: return nu face commit și nu ține lacăt. Nu este un COMMIT scris de tine. Serverul comite înainte de GRANT.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                GRANT ALL ON db1.* TO 'jeffrey'@'localhost';
                ROLLBACK;
                """;
    // Începător: închide întrebarea despre: Rândul 4 și privilegiul lui jeffrey rămân amândouă.
    // Producție: fără această acoladă, q31GrantCommitsInsert nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: CREATE INDEX după un INSERT necomis mai lasă ROLLBACK să scoată rândul?
    // Capcană: Indexul este în aceeași tranzacție, deci ROLLBACK le anulează pe amândouă.
    // Cheie: CREATE INDEX face commit implicit, deci INSERT-ul de dinainte rămâne. ROLLBACK nu îl mai scoate.
    // Începător: Rândul 4 rămâne pentru că indexul a comis tranzacția.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără el migrezi indexul în aceeași tranzacție cu datele anulabile.
    // Expert: Nu este un index care filtrează. Este DDL care închide tranzacția.
    /*
    🟢 Începător
        Ce este            Rândul 4 rămâne pentru că indexul a comis tranzacția.
        De ce e nevoie    fără el migrezi indexul în aceeași tranzacție cu datele anulabile
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); CREATE INDEX idx_customers_id ON customers (customer_id); ROLLBACK;
    🔴 Staff
        Unde               Nu este un index care filtrează. Este DDL care închide tranzacția.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q32CreateCommitsEarlierInsert()
    // Începător: deschide propoziția pentru: Rândul 4 rămâne pentru că indexul a comis tranzacția.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este un index care filtrează. Este DDL care închide tranzacția.
    {
        // Începător: Rândul 4 rămâne pentru că indexul a comis tranzacția.
        // Producție: return predă START TRANSACTION;. Fără return, fără el migrezi indexul în aceeași tranzacție cu datele anulabile.
        // Expert: return nu face commit și nu ține lacăt. Nu este un index care filtrează. Este DDL care închide tranzacția.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                CREATE INDEX idx_customers_id ON customers (customer_id);
                ROLLBACK;
                """;
    // Începător: închide întrebarea despre: Rândul 4 rămâne pentru că indexul a comis tranzacția.
    // Producție: fără această acoladă, q32CreateCommitsEarlierInsert nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: CREATE TEMPORARY TABLE table1 (type INT) face commit implicit?
    // Capcană: Orice CREATE comite, iar ROLLBACK șterge și tabela temporară.
    // Cheie: Nu face commit implicit. ROLLBACK tot nu o șterge, deci atomicitatea tranzacției este ruptă.
    // Începător: Tabela temporară table1 rămâne după ROLLBACK, fără să fi comis tranzacția.
    // Producție: CREATE TEMPORARY TABLE table1 (type INT) Dacă metoda dispare, fără ea tabela temporară este folosită ca schimbare anulabilă.
    // Expert: Nu este CREATE TABLE customers fără TEMPORARY. Acela comite.
    /*
    🟢 Începător
        Ce este            Tabela temporară table1 rămâne după ROLLBACK, fără să fi comis tranzacția.
        De ce e nevoie    fără ea tabela temporară este folosită ca schimbare anulabilă
    🟡 Producție
        Cum se scrie       CREATE TEMPORARY TABLE table1 (type INT)
    🔴 Staff
        Unde               Nu este CREATE TABLE customers fără TEMPORARY. Acela comite.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q33TemporarySurvivesRollback()
    // Începător: deschide propoziția pentru: Tabela temporară table1 rămâne după ROLLBACK, fără să fi comis tranzacția.
    // Producție: fără acoladă, CREATE TEMPORARY TABLE table1 (type INT) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este CREATE TABLE customers fără TEMPORARY. Acela comite.
    {
        // Începător: Tabela temporară table1 rămâne după ROLLBACK, fără să fi comis tranzacția.
        // Producție: return predă CREATE TEMPORARY TABLE table1 (type INT). Fără return, fără ea tabela temporară este folosită ca schimbare anulabilă.
        // Expert: return nu face commit și nu ține lacăt. Nu este CREATE TABLE customers fără TEMPORARY. Acela comite.
        return """
                CREATE TEMPORARY TABLE table1 (type INT)
                """;
    // Începător: închide întrebarea despre: Tabela temporară table1 rămâne după ROLLBACK, fără să fi comis tranzacția.
    // Producție: fără această acoladă, q33TemporarySurvivesRollback nu se închide.
    // Expert: închiderea nu execută CREATE TEMPORARY TABLE table1 (type INT) și nu schimbă customers.
    }

    // Enunț: ROLLBACK după o schimbare pe un motor netranzacțional ce lasă?
    // Capcană: Scoate toate schimbările, de pe orice motor.
    // Cheie: Schimbarea netranzacțională rămâne. Serverul avertizează ER_WARNING_NOT_COMPLETE_ROLLBACK. InnoDB din aceeași tranzacție se anulează.
    // Începător: ROLLBACK nu promite că fiecare motor a dat înapoi rândul.
    // Producție: ROLLBACK Dacă metoda dispare, fără el un motor netranzacțional este crezut acoperit de tranzacție.
    // Expert: Nu este anularea unui CREATE TABLE netemporar. Acela a comis înainte, fără acest avertisment.
    /*
    🟢 Începător
        Ce este            ROLLBACK nu promite că fiecare motor a dat înapoi rândul.
        De ce e nevoie    fără el un motor netranzacțional este crezut acoperit de tranzacție
    🟡 Producție
        Cum se scrie       ROLLBACK
    🔴 Staff
        Unde               Nu este anularea unui CREATE TABLE netemporar. Acela a comis înainte, fără acest avertisment.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q34NontransactionalRollback()
    // Începător: deschide propoziția pentru: ROLLBACK nu promite că fiecare motor a dat înapoi rândul.
    // Producție: fără acoladă, ROLLBACK nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este anularea unui CREATE TABLE netemporar. Acela a comis înainte, fără acest avertisment.
    {
        // Începător: ROLLBACK nu promite că fiecare motor a dat înapoi rândul.
        // Producție: return predă ROLLBACK. Fără return, fără el un motor netranzacțional este crezut acoperit de tranzacție.
        // Expert: return nu face commit și nu ține lacăt. Nu este anularea unui CREATE TABLE netemporar. Acela a comis înainte, fără acest avertisment.
        return """
                ROLLBACK
                """;
    // Începător: închide întrebarea despre: ROLLBACK nu promite că fiecare motor a dat înapoi rândul.
    // Producție: fără această acoladă, q34NontransactionalRollback nu se închide.
    // Expert: închiderea nu execută ROLLBACK și nu schimbă customers.
    }

    // Enunț: INSERT apoi COMMIT, cu autocommit rămas 1, când s-a salvat rândul?
    // Capcană: La COMMIT, pentru că doar COMMIT scrie pe disc.
    // Cheie: La sfârșitul lui INSERT. COMMIT de după nu este momentul în care rândul a devenit durabil.
    // Începător: Rândul 4 este deja durabil înainte de COMMIT-ul scris.
    // Producție: INSERT INTO customers (customer_id) VALUES (4); Dacă metoda dispare, fără el logul de aplicație marchează greșit momentul salvării.
    // Expert: Nu este START TRANSACTION urmat de COMMIT. Acolo COMMIT este momentul.
    /*
    🟢 Începător
        Ce este            Rândul 4 este deja durabil înainte de COMMIT-ul scris.
        De ce e nevoie    fără el logul de aplicație marchează greșit momentul salvării
    🟡 Producție
        Cum se scrie       INSERT INTO customers (customer_id) VALUES (4); COMMIT;
    🔴 Staff
        Unde               Nu este START TRANSACTION urmat de COMMIT. Acolo COMMIT este momentul.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q35CommitAfterAutocommit()
    // Începător: deschide propoziția pentru: Rândul 4 este deja durabil înainte de COMMIT-ul scris.
    // Producție: fără acoladă, INSERT INTO customers (customer_id) VALUES (4); nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este START TRANSACTION urmat de COMMIT. Acolo COMMIT este momentul.
    {
        // Începător: Rândul 4 este deja durabil înainte de COMMIT-ul scris.
        // Producție: return predă INSERT INTO customers (customer_id) VALUES (4);. Fără return, fără el logul de aplicație marchează greșit momentul salvării.
        // Expert: return nu face commit și nu ține lacăt. Nu este START TRANSACTION urmat de COMMIT. Acolo COMMIT este momentul.
        return """
                INSERT INTO customers (customer_id) VALUES (4);
                COMMIT;
                """;
    // Începător: închide întrebarea despre: Rândul 4 este deja durabil înainte de COMMIT-ul scris.
    // Producție: fără această acoladă, q35CommitAfterAutocommit nu se închide.
    // Expert: închiderea nu execută INSERT INTO customers (customer_id) VALUES (4); și nu schimbă customers.
    }

    // Enunț: REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' câți clienți șterge din customers?
    // Capcană: Îi șterge pe cei inserați de jeffrey.
    // Cheie: Niciunul. Scoate privilegiul și face commit implicit. Rândurile rămân.
    // Începător: customers păstrează rândurile, iar jeffrey nu mai poate insera.
    // Producție: REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' Dacă metoda dispare, fără el un drept retras este folosit ca să cureți datele.
    // Expert: Nu este DELETE FROM customers. Nu primește customer_id.
    /*
    🟢 Începător
        Ce este            customers păstrează rândurile, iar jeffrey nu mai poate insera.
        De ce e nevoie    fără el un drept retras este folosit ca să cureți datele
    🟡 Producție
        Cum se scrie       REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'
    🔴 Staff
        Unde               Nu este DELETE FROM customers. Nu primește customer_id.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q36RevokeDoesNotDeleteRows()
    // Începător: deschide propoziția pentru: customers păstrează rândurile, iar jeffrey nu mai poate insera.
    // Producție: fără acoladă, REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este DELETE FROM customers. Nu primește customer_id.
    {
        // Începător: customers păstrează rândurile, iar jeffrey nu mai poate insera.
        // Producție: return predă REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'. Fără return, fără el un drept retras este folosit ca să cureți datele.
        // Expert: return nu face commit și nu ține lacăt. Nu este DELETE FROM customers. Nu primește customer_id.
        return """
                REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'
                """;
    // Începător: închide întrebarea despre: customers păstrează rândurile, iar jeffrey nu mai poate insera.
    // Producție: fără această acoladă, q36RevokeDoesNotDeleteRows nu se închide.
    // Expert: închiderea nu execută REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' și nu schimbă customers.
    }

    // Enunț: TRUNCATE TABLE customers poate scoate doar rândul 4?
    // Capcană: Da, dacă înainte ai un WHERE.
    // Cheie: Nu primește WHERE. Golește tabela, lasă definiția și face commit implicit.
    // Începător: Toate rândurile din customers dispar, nu doar 4.
    // Producție: TRUNCATE TABLE customers Dacă metoda dispare, fără el un client este șters odată cu toți ceilalți.
    // Expert: Nu este DELETE FROM customers WHERE customer_id = 4.
    /*
    🟢 Începător
        Ce este            Toate rândurile din customers dispar, nu doar 4.
        De ce e nevoie    fără el un client este șters odată cu toți ceilalți
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE customers
    🔴 Staff
        Unde               Nu este DELETE FROM customers WHERE customer_id = 4.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q37TruncateEmptiesAll()
    // Începător: deschide propoziția pentru: Toate rândurile din customers dispar, nu doar 4.
    // Producție: fără acoladă, TRUNCATE TABLE customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este DELETE FROM customers WHERE customer_id = 4.
    {
        // Începător: Toate rândurile din customers dispar, nu doar 4.
        // Producție: return predă TRUNCATE TABLE customers. Fără return, fără el un client este șters odată cu toți ceilalți.
        // Expert: return nu face commit și nu ține lacăt. Nu este DELETE FROM customers WHERE customer_id = 4.
        return """
                TRUNCATE TABLE customers
                """;
    // Începător: închide întrebarea despre: Toate rândurile din customers dispar, nu doar 4.
    // Producție: fără această acoladă, q37TruncateEmptiesAll nu se închide.
    // Expert: închiderea nu execută TRUNCATE TABLE customers și nu schimbă customers.
    }

    // Enunț: După DROP TABLE customers, ce face următorul INSERT INTO customers?
    // Capcană: Umple tabela goală.
    // Cheie: Nu are obiect. DROP a scos tabela și a făcut commit implicit.
    // Începător: INSERT-ul următor nu mai găsește customers.
    // Producție: DROP TABLE customers Dacă metoda dispare, fără el aplicația continuă să scrie într-o tabelă ștearsă.
    // Expert: Nu este TRUNCATE, care ar fi lăsat obiectul.
    /*
    🟢 Începător
        Ce este            INSERT-ul următor nu mai găsește customers.
        De ce e nevoie    fără el aplicația continuă să scrie într-o tabelă ștearsă
    🟡 Producție
        Cum se scrie       DROP TABLE customers
    🔴 Staff
        Unde               Nu este TRUNCATE, care ar fi lăsat obiectul.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q38DropRemovesObject()
    // Începător: deschide propoziția pentru: INSERT-ul următor nu mai găsește customers.
    // Producție: fără acoladă, DROP TABLE customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este TRUNCATE, care ar fi lăsat obiectul.
    {
        // Începător: INSERT-ul următor nu mai găsește customers.
        // Producție: return predă DROP TABLE customers. Fără return, fără el aplicația continuă să scrie într-o tabelă ștearsă.
        // Expert: return nu face commit și nu ține lacăt. Nu este TRUNCATE, care ar fi lăsat obiectul.
        return """
                DROP TABLE customers
                """;
    // Începător: închide întrebarea despre: INSERT-ul următor nu mai găsește customers.
    // Producție: fără această acoladă, q38DropRemovesObject nu se închide.
    // Expert: închiderea nu execută DROP TABLE customers și nu schimbă customers.
    }

    // Enunț: SET autocommit=0 și apoi INSERT. Când se închide tranzacția?
    // Capcană: La sfârșitul lui INSERT, ca atunci când autocommit este 1.
    // Cheie: Rămâne deschisă pe sesiune până la COMMIT sau ROLLBACK. Conexiunea pusă înapoi în pool poate ține încă tranzacția.
    // Începător: Rândul 4 nu este durabil, iar sesiunea rămâne în tranzacție.
    // Producție: SET autocommit=0; Dacă metoda dispare, fără el pool-ul predă altcuiva o tranzacție deschisă.
    // Expert: Nu este START TRANSACTION, care la final redă autocommit la valoarea dinainte.
    /*
    🟢 Începător
        Ce este            Rândul 4 nu este durabil, iar sesiunea rămâne în tranzacție.
        De ce e nevoie    fără el pool-ul predă altcuiva o tranzacție deschisă
    🟡 Producție
        Cum se scrie       SET autocommit=0; INSERT INTO customers (customer_id) VALUES (4);
    🔴 Staff
        Unde               Nu este START TRANSACTION, care la final redă autocommit la valoarea dinainte.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q39AutocommitZeroStaysOpen()
    // Începător: deschide propoziția pentru: Rândul 4 nu este durabil, iar sesiunea rămâne în tranzacție.
    // Producție: fără acoladă, SET autocommit=0; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este START TRANSACTION, care la final redă autocommit la valoarea dinainte.
    {
        // Începător: Rândul 4 nu este durabil, iar sesiunea rămâne în tranzacție.
        // Producție: return predă SET autocommit=0;. Fără return, fără el pool-ul predă altcuiva o tranzacție deschisă.
        // Expert: return nu face commit și nu ține lacăt. Nu este START TRANSACTION, care la final redă autocommit la valoarea dinainte.
        return """
                SET autocommit=0;
                INSERT INTO customers (customer_id) VALUES (4);
                """;
    // Începător: închide întrebarea despre: Rândul 4 nu este durabil, iar sesiunea rămâne în tranzacție.
    // Producție: fără această acoladă, q39AutocommitZeroStaysOpen nu se închide.
    // Expert: închiderea nu execută SET autocommit=0; și nu schimbă customers.
    }

    // Enunț: Poți trimite GRANT ALL cât o tranzacție XA este ACTIVE?
    // Capcană: Da, iar ROLLBACK de XA anulează și privilegiul.
    // Cheie: Nu. O propoziție care face commit implicit este refuzată cât XA este ACTIVE.
    // Începător: GRANT nu intră într-o tranzacție XA încă activă.
    // Producție: GRANT ALL ON db1.* TO 'jeffrey'@'localhost' Dacă metoda dispare, fără el un drept este dat în mijlocul unei tranzacții XA.
    // Expert: Nu este un INSERT InnoDB dintr-o tranzacție obișnuită, care poate sta necomis.
    /*
    🟢 Începător
        Ce este            GRANT nu intră într-o tranzacție XA încă activă.
        De ce e nevoie    fără el un drept este dat în mijlocul unei tranzacții XA
    🟡 Producție
        Cum se scrie       GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
    🔴 Staff
        Unde               Nu este un INSERT InnoDB dintr-o tranzacție obișnuită, care poate sta necomis.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q40XaRejectsImplicitCommit()
    // Începător: deschide propoziția pentru: GRANT nu intră într-o tranzacție XA încă activă.
    // Producție: fără acoladă, GRANT ALL ON db1.* TO 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este un INSERT InnoDB dintr-o tranzacție obișnuită, care poate sta necomis.
    {
        // Începător: GRANT nu intră într-o tranzacție XA încă activă.
        // Producție: return predă GRANT ALL ON db1.* TO 'jeffrey'@'localhost'. Fără return, fără el un drept este dat în mijlocul unei tranzacții XA.
        // Expert: return nu face commit și nu ține lacăt. Nu este un INSERT InnoDB dintr-o tranzacție obișnuită, care poate sta necomis.
        return """
                GRANT ALL ON db1.* TO 'jeffrey'@'localhost'
                """;
    // Începător: închide întrebarea despre: GRANT nu intră într-o tranzacție XA încă activă.
    // Producție: fără această acoladă, q40XaRejectsImplicitCommit nu se închide.
    // Expert: închiderea nu execută GRANT ALL ON db1.* TO 'jeffrey'@'localhost' și nu schimbă customers.
    }

    // Enunț: Contul jeffrey este folosit de mai mulți clienți și nu trebuie să insereze. Unde tai dreptul?
    // Capcană: În if-ul unui singur serviciu, care nu arată butonul.
    // Cheie: Cu REVOKE sau GRANT pe server. Filtrul dintr-un serviciu nu oprește un alt client al aceluiași cont.
    // Începător: Dreptul de INSERT dispare pentru cont, nu doar pentru un ecran.
    // Producție: REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' Dacă metoda dispare, fără el al doilea client încă inserează.
    // Expert: Nu este un SELECT care omite o coloană. Acela nu schimbă privilegiul.
    /*
    🟢 Începător
        Ce este            Dreptul de INSERT dispare pentru cont, nu doar pentru un ecran.
        De ce e nevoie    fără el al doilea client încă inserează
    🟡 Producție
        Cum se scrie       REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'
    🔴 Staff
        Unde               Nu este un SELECT care omite o coloană. Acela nu schimbă privilegiul.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q41SharedAccountUsesGrant()
    // Începător: deschide propoziția pentru: Dreptul de INSERT dispare pentru cont, nu doar pentru un ecran.
    // Producție: fără acoladă, REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este un SELECT care omite o coloană. Acela nu schimbă privilegiul.
    {
        // Începător: Dreptul de INSERT dispare pentru cont, nu doar pentru un ecran.
        // Producție: return predă REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'. Fără return, fără el al doilea client încă inserează.
        // Expert: return nu face commit și nu ține lacăt. Nu este un SELECT care omite o coloană. Acela nu schimbă privilegiul.
        return """
                REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'
                """;
    // Începător: închide întrebarea despre: Dreptul de INSERT dispare pentru cont, nu doar pentru un ecran.
    // Producție: fără această acoladă, q41SharedAccountUsesGrant nu se închide.
    // Expert: închiderea nu execută REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' și nu schimbă customers.
    }

    // Enunț: Tabela customers există, iar rândul 4 trebuie să poată dispărea. Ce trimiți în loc de CREATE TABLE?
    // Capcană: Încă un CREATE TABLE, ca tabela să fie în tranzacție.
    // Cheie: START TRANSACTION, INSERT, apoi ROLLBACK sau COMMIT. CREATE ar comite și nu ține rândul anulabil.
    // Începător: Rândul 4 rămâne anulabil până la decizia ta.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără el schema este recreată doar ca să poți da înapoi un rând.
    // Expert: Nu este CREATE TABLE customers. Acela schimbă obiectul și comite.
    /*
    🟢 Începător
        Ce este            Rândul 4 rămâne anulabil până la decizia ta.
        De ce e nevoie    fără el schema este recreată doar ca să poți da înapoi un rând
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4);
    🔴 Staff
        Unde               Nu este CREATE TABLE customers. Acela schimbă obiectul și comite.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q42UndoableRowUsesInsert()
    // Începător: deschide propoziția pentru: Rândul 4 rămâne anulabil până la decizia ta.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este CREATE TABLE customers. Acela schimbă obiectul și comite.
    {
        // Începător: Rândul 4 rămâne anulabil până la decizia ta.
        // Producție: return predă START TRANSACTION;. Fără return, fără el schema este recreată doar ca să poți da înapoi un rând.
        // Expert: return nu face commit și nu ține lacăt. Nu este CREATE TABLE customers. Acela schimbă obiectul și comite.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                """;
    // Începător: închide întrebarea despre: Rândul 4 rămâne anulabil până la decizia ta.
    // Producție: fără această acoladă, q42UndoableRowUsesInsert nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: Obiectul customers trebuie să apară și să rămână. Ce accepți odată cu CREATE TABLE?
    // Capcană: Că ROLLBACK îl va șterge dacă te răzgândești.
    // Cheie: Commit-ul implicit. Tabela rămâne. Nu o pui în tranzacția în care mai ai rânduri de anulat.
    // Începător: Schema rămâne, iar tranzacția deschisă înainte se închide.
    // Producție: CREATE TABLE customers (customer_id INT PRIMARY KEY) Dacă metoda dispare, fără el migrația de schemă stă în tranzacția cererii.
    // Expert: Nu este INSERT. Nu poți anula obiectul cu ROLLBACK.
    /*
    🟢 Începător
        Ce este            Schema rămâne, iar tranzacția deschisă înainte se închide.
        De ce e nevoie    fără el migrația de schemă stă în tranzacția cererii
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY)
    🔴 Staff
        Unde               Nu este INSERT. Nu poți anula obiectul cu ROLLBACK.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q43SchemaChangeAcceptsCommit()
    // Începător: deschide propoziția pentru: Schema rămâne, iar tranzacția deschisă înainte se închide.
    // Producție: fără acoladă, CREATE TABLE customers (customer_id INT PRIMARY KEY) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT. Nu poți anula obiectul cu ROLLBACK.
    {
        // Începător: Schema rămâne, iar tranzacția deschisă înainte se închide.
        // Producție: return predă CREATE TABLE customers (customer_id INT PRIMARY KEY). Fără return, fără el migrația de schemă stă în tranzacția cererii.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT. Nu poți anula obiectul cu ROLLBACK.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY)
                """;
    // Începător: închide întrebarea despre: Schema rămâne, iar tranzacția deschisă înainte se închide.
    // Producție: fără această acoladă, q43SchemaChangeAcceptsCommit nu se închide.
    // Expert: închiderea nu execută CREATE TABLE customers (customer_id INT PRIMARY KEY) și nu schimbă customers.
    }

    // Enunț: Vrei să vezi customer_id și să nu lași lacăt de scriere. Ce propoziție alegi?
    // Capcană: START TRANSACTION și UPDATE, ca citirea să fie serioasă.
    // Cheie: SELECT customer_id FROM customers. Nu deschide o tranzacție de scriere și nu schimbă rândul.
    // Începător: Clientul primește identificatorii, iar customers rămâne neschimbat.
    // Producție: SELECT customer_id FROM customers Dacă metoda dispare, fără el citirea ține lacăte de modificare.
    // Expert: Nu este UPDATE. Citirea nu schimbă last_name și nu deschide o tranzacție de scriere.
    /*
    🟢 Începător
        Ce este            Clientul primește identificatorii, iar customers rămâne neschimbat.
        De ce e nevoie    fără el citirea ține lacăte de modificare
    🟡 Producție
        Cum se scrie       SELECT customer_id FROM customers
    🔴 Staff
        Unde               Nu este UPDATE. Citirea nu schimbă last_name și nu deschide o tranzacție de scriere.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q44ReadUsesSelect()
    // Începător: deschide propoziția pentru: Clientul primește identificatorii, iar customers rămâne neschimbat.
    // Producție: fără acoladă, SELECT customer_id FROM customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este UPDATE. Citirea nu schimbă last_name și nu deschide o tranzacție de scriere.
    {
        // Începător: Clientul primește identificatorii, iar customers rămâne neschimbat.
        // Producție: return predă SELECT customer_id FROM customers. Fără return, fără el citirea ține lacăte de modificare.
        // Expert: return nu face commit și nu ține lacăt. Nu este UPDATE. Citirea nu schimbă last_name și nu deschide o tranzacție de scriere.
        return """
                SELECT customer_id FROM customers
                """;
    // Începător: închide întrebarea despre: Clientul primește identificatorii, iar customers rămâne neschimbat.
    // Producție: fără această acoladă, q44ReadUsesSelect nu se închide.
    // Expert: închiderea nu execută SELECT customer_id FROM customers și nu schimbă customers.
    }

    // Enunț: Un singur serviciu nu trebuie să arate last_name, iar contul nu este partajat. Ce trimiți?
    // Capcană: GRANT sau REVOKE pe last_name, ca să ascunzi coloana în ecran.
    // Cheie: SELECT-ul serviciului omite coloana. Asta nu oprește un alt client dacă același cont este folosit și în altă parte.
    // Începător: Serviciul citește customer_id și nu schimbă privilegiul.
    // Producție: SELECT customer_id FROM customers Dacă metoda dispare, fără el un ecran este crezut o graniță de securitate pentru orice client.
    // Expert: Nu este GRANT ALL ON db1.*. GRANT rămâne alegerea când contul este partajat.
    /*
    🟢 Începător
        Ce este            Serviciul citește customer_id și nu schimbă privilegiul.
        De ce e nevoie    fără el un ecran este crezut o graniță de securitate pentru orice client
    🟡 Producție
        Cum se scrie       SELECT customer_id FROM customers
    🔴 Staff
        Unde               Nu este GRANT ALL ON db1.*. GRANT rămâne alegerea când contul este partajat.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q45SingleServiceOmitsColumn()
    // Începător: deschide propoziția pentru: Serviciul citește customer_id și nu schimbă privilegiul.
    // Producție: fără acoladă, SELECT customer_id FROM customers nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este GRANT ALL ON db1.*. GRANT rămâne alegerea când contul este partajat.
    {
        // Începător: Serviciul citește customer_id și nu schimbă privilegiul.
        // Producție: return predă SELECT customer_id FROM customers. Fără return, fără el un ecran este crezut o graniță de securitate pentru orice client.
        // Expert: return nu face commit și nu ține lacăt. Nu este GRANT ALL ON db1.*. GRANT rămâne alegerea când contul este partajat.
        return """
                SELECT customer_id FROM customers
                """;
    // Începător: închide întrebarea despre: Serviciul citește customer_id și nu schimbă privilegiul.
    // Producție: fără această acoladă, q45SingleServiceOmitsColumn nu se închide.
    // Expert: închiderea nu execută SELECT customer_id FROM customers și nu schimbă customers.
    }

    // Enunț: Clientul este JDBC și a oprit autocommit. Mai trimiți și START TRANSACTION?
    // Capcană: Da, altfel INSERT nu intră în tranzacție.
    // Cheie: Manualul 9.7 spune că API-ul, inclusiv JDBC, poate deschide tranzacția. Un START TRANSACTION trimis peste ea comite tranzacția deja deschisă.
    // Începător: Rândul se scrie în tranzacția ținută de JDBC, fără un al doilea început.
    // Producție: INSERT INTO customers (customer_id) VALUES (4) Dacă metoda dispare, fără el START TRANSACTION îți comite pe tăcute tranzacția deschisă de driver.
    // Expert: Nu este setAutoCommit. Metoda întoarce INSERT-ul, iar granița rămâne în JDBC.
    /*
    🟢 Începător
        Ce este            Rândul se scrie în tranzacția ținută de JDBC, fără un al doilea început.
        De ce e nevoie    fără el START TRANSACTION îți comite pe tăcute tranzacția deschisă de driver
    🟡 Producție
        Cum se scrie       INSERT INTO customers (customer_id) VALUES (4)
    🔴 Staff
        Unde               Nu este setAutoCommit. Metoda întoarce INSERT-ul, iar granița rămâne în JDBC.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q46JdbcOwnsTransactionBoundary()
    // Începător: deschide propoziția pentru: Rândul se scrie în tranzacția ținută de JDBC, fără un al doilea început.
    // Producție: fără acoladă, INSERT INTO customers (customer_id) VALUES (4) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este setAutoCommit. Metoda întoarce INSERT-ul, iar granița rămâne în JDBC.
    {
        // Începător: Rândul se scrie în tranzacția ținută de JDBC, fără un al doilea început.
        // Producție: return predă INSERT INTO customers (customer_id) VALUES (4). Fără return, fără el START TRANSACTION îți comite pe tăcute tranzacția deschisă de driver.
        // Expert: return nu face commit și nu ține lacăt. Nu este setAutoCommit. Metoda întoarce INSERT-ul, iar granița rămâne în JDBC.
        return """
                INSERT INTO customers (customer_id) VALUES (4)
                """;
    // Începător: închide întrebarea despre: Rândul se scrie în tranzacția ținută de JDBC, fără un al doilea început.
    // Producție: fără această acoladă, q46JdbcOwnsTransactionBoundary nu se închide.
    // Expert: închiderea nu execută INSERT INTO customers (customer_id) VALUES (4) și nu schimbă customers.
    }

    // Enunț: Cererea HTTP ține o tranzacție cu un INSERT anulabil. Unde rulezi CREATE TABLE?
    // Capcană: În aceeași tranzacție, ca schema și rândul să fie atomice.
    // Cheie: În afara ei. CREATE TABLE netemporar ar comite INSERT-ul cererii.
    // Începător: Migrația de schemă nu stă în tranzacția cererii.
    // Producție: CREATE TABLE customers (customer_id INT PRIMARY KEY) Dacă metoda dispare, fără ea un request care dă eroare lasă totuși rândurile comise de DDL.
    // Expert: Nu este INSERT-ul cererii. Acela poate rămâne necomis până la COMMIT.
    /*
    🟢 Începător
        Ce este            Migrația de schemă nu stă în tranzacția cererii.
        De ce e nevoie    fără ea un request care dă eroare lasă totuși rândurile comise de DDL
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY)
    🔴 Staff
        Unde               Nu este INSERT-ul cererii. Acela poate rămâne necomis până la COMMIT.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q47MigrationOutsideRequest()
    // Începător: deschide propoziția pentru: Migrația de schemă nu stă în tranzacția cererii.
    // Producție: fără acoladă, CREATE TABLE customers (customer_id INT PRIMARY KEY) nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este INSERT-ul cererii. Acela poate rămâne necomis până la COMMIT.
    {
        // Începător: Migrația de schemă nu stă în tranzacția cererii.
        // Producție: return predă CREATE TABLE customers (customer_id INT PRIMARY KEY). Fără return, fără ea un request care dă eroare lasă totuși rândurile comise de DDL.
        // Expert: return nu face commit și nu ține lacăt. Nu este INSERT-ul cererii. Acela poate rămâne necomis până la COMMIT.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY)
                """;
    // Începător: închide întrebarea despre: Migrația de schemă nu stă în tranzacția cererii.
    // Producție: fără această acoladă, q47MigrationOutsideRequest nu se închide.
    // Expert: închiderea nu execută CREATE TABLE customers (customer_id INT PRIMARY KEY) și nu schimbă customers.
    }

    // Enunț: Vrei să anulezi al doilea INSERT și să păstrezi rândul 4, fără să închizi tranzacția. Ce trimiți?
    // Capcană: ROLLBACK, pentru că anulează doar ultima propoziție.
    // Cheie: SAVEPOINT înainte de al doilea INSERT, apoi ROLLBACK TO SAVEPOINT. Tranzacția rămâne deschisă. ROLLBACK fără nume le-ar anula pe amândouă.
    // Începător: Rândul 5 este scos, rândul 4 rămâne, iar tranzacția nu se închide.
    // Producție: START TRANSACTION; Dacă metoda dispare, fără el prima inserare este pierdută odată cu a doua.
    // Expert: Nu este ROLLBACK fără nume. InnoDB nu eliberează lacătele ținute în memorie după savepoint; lacătul unui rând nou inserat se eliberează în undo.
    /*
    🟢 Începător
        Ce este            Rândul 5 este scos, rândul 4 rămâne, iar tranzacția nu se închide.
        De ce e nevoie    fără el prima inserare este pierdută odată cu a doua
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); SAVEPOINT before_second; INSERT INTO customers (customer_id) VALUES (5); ROLLBACK TO SAVEPOINT before_second;
    🔴 Staff
        Unde               Nu este ROLLBACK fără nume. InnoDB nu eliberează lacătele ținute în memorie după savepoint; lacătul unui rând nou inserat se eliberează în undo.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q48SavepointKeepsEarlierInsert()
    // Începător: deschide propoziția pentru: Rândul 5 este scos, rândul 4 rămâne, iar tranzacția nu se închide.
    // Producție: fără acoladă, START TRANSACTION; nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este ROLLBACK fără nume. InnoDB nu eliberează lacătele ținute în memorie după savepoint; lacătul unui rând nou inserat se eliberează în undo.
    {
        // Începător: Rândul 5 este scos, rândul 4 rămâne, iar tranzacția nu se închide.
        // Producție: return predă START TRANSACTION;. Fără return, fără el prima inserare este pierdută odată cu a doua.
        // Expert: return nu face commit și nu ține lacăt. Nu este ROLLBACK fără nume. InnoDB nu eliberează lacătele ținute în memorie după savepoint; lacătul unui rând nou inserat se eliberează în undo.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                SAVEPOINT before_second;
                INSERT INTO customers (customer_id) VALUES (5);
                ROLLBACK TO SAVEPOINT before_second;
                """;
    // Începător: închide întrebarea despre: Rândul 5 este scos, rândul 4 rămâne, iar tranzacția nu se închide.
    // Producție: fără această acoladă, q48SavepointKeepsEarlierInsert nu se închide.
    // Expert: închiderea nu execută START TRANSACTION; și nu schimbă customers.
    }

    // Enunț: Un flag în aplicație spune că jeffrey nu poate insera. Un client SQL se conectează cu același cont. Ce îl oprește?
    // Capcană: Flag-ul, pentru că aplicația este singura cale către server.
    // Cheie: Doar privilegiul de pe server. GRANT sau REVOKE rămâne și pentru clientul care nu trece prin flag.
    // Începător: Contul pierde INSERT și după un client care nu citește flag-ul.
    // Producție: REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' Dacă metoda dispare, fără el un client direct ocolește regula din aplicație.
    // Expert: Nu este o coloană din customers. Flag-ul aplicației nu scrie în schema mysql.
    /*
    🟢 Începător
        Ce este            Contul pierde INSERT și după un client care nu citește flag-ul.
        De ce e nevoie    fără el un client direct ocolește regula din aplicație
    🟡 Producție
        Cum se scrie       REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'
    🔴 Staff
        Unde               Nu este o coloană din customers. Flag-ul aplicației nu scrie în schema mysql.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q49GrantSurvivesOtherClients()
    // Începător: deschide propoziția pentru: Contul pierde INSERT și după un client care nu citește flag-ul.
    // Producție: fără acoladă, REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este o coloană din customers. Flag-ul aplicației nu scrie în schema mysql.
    {
        // Începător: Contul pierde INSERT și după un client care nu citește flag-ul.
        // Producție: return predă REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'. Fără return, fără el un client direct ocolește regula din aplicație.
        // Expert: return nu face commit și nu ține lacăt. Nu este o coloană din customers. Flag-ul aplicației nu scrie în schema mysql.
        return """
                REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost'
                """;
    // Începător: închide întrebarea despre: Contul pierde INSERT și după un client care nu citește flag-ul.
    // Producție: fără această acoladă, q49GrantSurvivesOtherClients nu se închide.
    // Expert: închiderea nu execută REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost' și nu schimbă customers.
    }

    // Enunț: Vrei să schimbi last_name în Johnson. De ce nu trimiți EXPLAIN?
    // Capcană: EXPLAIN îți arată că UPDATE va merge, deci a și scris.
    // Cheie: EXPLAIN nu schimbă rândul. Scrierea este UPDATE, iar într-o tranzacție deschisă rămâne durabilă la COMMIT.
    // Începător: Numele se schimbă doar când serverul execută UPDATE, nu EXPLAIN.
    // Producție: UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4 Dacă metoda dispare, fără el planul este confundat cu scrierea.
    // Expert: Nu este EXPLAIN UPDATE. Planul nu face commit și nu ține lacătul rândului 4.
    /*
    🟢 Începător
        Ce este            Numele se schimbă doar când serverul execută UPDATE, nu EXPLAIN.
        De ce e nevoie    fără el planul este confundat cu scrierea
    🟡 Producție
        Cum se scrie       UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4
    🔴 Staff
        Unde               Nu este EXPLAIN UPDATE. Planul nu face commit și nu ține lacătul rândului 4.
    Pași                   1. Recunoaște propoziția. 2. Spune ce rămâne.
    */
    public static String q50ExplainIsNotTheWrite()
    // Începător: deschide propoziția pentru: Numele se schimbă doar când serverul execută UPDATE, nu EXPLAIN.
    // Producție: fără acoladă, UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4 nu poate fi întors.
    // Expert: acolada nu este trimisă serverului. Nu este EXPLAIN UPDATE. Planul nu face commit și nu ține lacătul rândului 4.
    {
        // Începător: Numele se schimbă doar când serverul execută UPDATE, nu EXPLAIN.
        // Producție: return predă UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4. Fără return, fără el planul este confundat cu scrierea.
        // Expert: return nu face commit și nu ține lacăt. Nu este EXPLAIN UPDATE. Planul nu face commit și nu ține lacătul rândului 4.
        return """
                UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4
                """;
    // Începător: închide întrebarea despre: Numele se schimbă doar când serverul execută UPDATE, nu EXPLAIN.
    // Producție: fără această acoladă, q50ExplainIsNotTheWrite nu se închide.
    // Expert: închiderea nu execută UPDATE customers SET last_name = 'Johnson' WHERE customer_id = 4 și nu schimbă customers.
    }
// Începător: închide banca după cea de-a cincizecea propoziție.
// Producție: fără acolada finală, fișierul nu compilează.
// Expert: închiderea clasei nu face ROLLBACK și nu revocă privilegiul din schema mysql.
}
