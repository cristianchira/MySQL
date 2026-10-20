// Începător: lecția stă lângă propozițiile SQL și spune pe care o trimiți ca să rămână schema, rândul sau privilegiul.
// Producție: package _2_SQL_Statements.fast; dacă dispare, chooseByEffect nu mai compilează în folderul care alege între DDL, DML, GRANT și ROLLBACK.
// Expert: pachetul nu execută propoziția și nu deschide tranzacția. Nu este calificatorul db1 din GRANT ALL ON db1.*.
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

// Începător: clasa ține propozițiile cu care alegi între schemă, rând, privilegiu și tranzacție.
// Producție: public class SqlStatements_Fast este intrarea din main. Dacă dispare, nu mai ai unde să trimiți chooseByEffect.
// Expert: clasa nu este serverul și nu ține lacăt după ce textul a fost întors. Vecinul ei este un filtru din aplicație, care nu schimbă privilegiul din mysql.
/*
🟢 Începător
    Ce este            alegerea propoziției după efectul care rămâne în schemă, în rânduri, în privilegiu sau în tranzacție
    De ce e nevoie    fără ea, ROLLBACK este trimis și după CREATE TABLE, iar tabela tot rămâne
🟡 Producție
    Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY); START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK;
🔴 Staff
    Unde               commit implicit la CREATE TABLE netemporar; lacătul rândului inserat ține până la ROLLBACK
Pași                   1. Alege obiectul schimbat. 2. Trimite propoziția acelei familii. 3. Spune ce mai poate anula ROLLBACK.
*/
public class SqlStatements_Fast
// Începător: deschide corpul care ține propozițiile de schemă, de rând, de privilegiu și de tranzacție.
// Producție: fără acoladă, chooseByEffect nu are unde să stea și lecția nu compilează.
// Expert: acolada nu este trimisă serverului și nu ține lacătul rândului din customers.
{

    // Începător: pornește exemplul minim și arată textul pe care serverul îl poate rula.
    // Producție: main apelează chooseByEffect. Dacă dispare, nu mai vezi ordinea CREATE, START TRANSACTION, INSERT, ROLLBACK.
    // Expert: main nu deschide o conexiune. Repo-ul nu are host, utilizator sau port, deci rularea pe server rămâne nefacută.
    /*
    🟢 Începător
        Ce este            exemplul minim care lasă tabela și scoate rândul
        De ce e nevoie    fără el, familia de propoziții rămâne o listă de nume
    🟡 Producție
        Cum se scrie       apelul chooseByEffect()
    🔴 Staff
        Unde               JVM tipărește textul. Commit-ul implicit îl face serverul, nu println
    Pași                   1. Ia propozițiile. 2. Le tipărește.
    */
    public static void main(String[] args)
    // Începător: deschide exemplul care tipărește scriptul, fără să deschidă o conexiune.
    // Producție: fără acoladă, println(chooseByEffect()) nu are corp și ordinea propozițiilor nu mai apare.
    // Expert: acolada nu face commit implicit și nu înlocuiește ROLLBACK de pe server.
    {
        // Începător: tipărește propozițiile care lasă tabela customers și scot rândul 4.
        // Producție: System.out.println(chooseByEffect()) arată scriptul. Dacă linia dispare, main nu mai dovedește ordinea.
        // Expert: println nu trimite textul la mysqld și nu eliberează lacătul unui INSERT. Nu ține loc de ROLLBACK.
        System.out.println(chooseByEffect());
    // Începător: închide exemplul minim după ce textul a fost tipărit.
    // Producție: fără această acoladă, main nu se închide și următoarele metode nu mai compilează.
    // Expert: închiderea metodei nu eliberează lacătul InnoDB și nu anulează CREATE TABLE.
    }

    // Începător: tabela customers rămâne, iar rândul 4 inserat după ea nu rămâne.
    // Producție: CREATE TABLE, apoi START TRANSACTION, INSERT și ROLLBACK. Dacă metoda dispare, nu mai ai exemplul care desparte schema de rând.
    // Expert: CREATE TABLE netemporar nu este INSERT. Commit-ul lui implicit nu ține loc de index și nu anulează un GRANT trimis mai târziu.
    /*
    🟢 Începător
        Ce este            propozițiile în ordinea care lasă schema și anulează rândul
        De ce e nevoie    fără ordine, CREATE și INSERT par amândouă anulabile
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY); START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK;
    🔴 Staff
        Unde               commit implicit la sfârșitul lui CREATE TABLE; rândul inserat ține lacătul până la ROLLBACK
    Pași                   1. Schimbă schema. 2. Deschide tranzacția. 3. Adaugă rândul. 4. Anulează rândul.
    */
    public static String chooseByEffect()
    // Începător: deschide scriptul în care tabela customers rămâne și rândul 4 dispare.
    // Producție: fără acoladă, CREATE TABLE urmat de ROLLBACK nu poate fi întors.
    // Expert: acolada nu este commit-ul implicit al lui CREATE TABLE și nu ține loc de index.
    {
        // Începător: întoarce scriptul în care tabela rămâne și rândul 4 dispare.
        // Producție: return predă cele patru propoziții. Fără return, apelantul nu primește textul gata de trimis.
        // Expert: return nu face commit. Commit-ul implicit îl face serverul când primește CREATE TABLE, nu când primește acest return.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY);
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                ROLLBACK;
                """;
    // Începător: închide scriptul care desparte tabela rămasă de rândul anulat.
    // Producție: fără această acoladă, chooseByEffect nu se închide și effectOnRows nu mai compilează.
    // Expert: închiderea nu trimite ROLLBACK. Rândul dispare doar când serverul primește propoziția din text.
    }

    // Începător: după ROLLBACK, customers nu are rândul 4. Tabela, dacă exista, rămâne neschimbată de aceste trei propoziții.
    // Producție: START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK; Dacă metoda dispare, nu mai arăți momentul în care rândul dispare.
    // Expert: nu este CREATE TABLE. Aceste propoziții nu schimbă schema și nu eliberează privilegiul lui jeffrey.
    /*
    🟢 Începător
        Ce este            soarta rândului 4 între START TRANSACTION și ROLLBACK
        De ce e nevoie    fără ele, INSERT pare deja durabil la sfârșitul lui
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); ROLLBACK;
    🔴 Staff
        Unde               sesiunea vede rândul înainte de ROLLBACK; durabilitatea lipsește până la COMMIT, care aici nu vine
    Pași                   1. Deschide tranzacția. 2. Inserează. 3. Anulează.
    */
    public static String effectOnRows()
    // Începător: deschide propozițiile care adaugă rândul 4 și apoi îl scot.
    // Producție: fără acoladă, START TRANSACTION și ROLLBACK nu pot fi întoarse.
    // Expert: acolada nu ține lacătul de rând. Lacătul îl ține InnoDB până la ROLLBACK pe server.
    {
        // Începător: întoarce propozițiile care adaugă rândul 4 și apoi îl scot.
        // Producție: return predă START TRANSACTION, INSERT și ROLLBACK. Fără return, efectul asupra rândului nu poate fi arătat.
        // Expert: return nu ține lacătul. Lacătul rândului inserat îl ține InnoDB până la ROLLBACK, pe server, nu în această metodă.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                ROLLBACK;
                """;
    // Începător: închide demonstrația în care rândul 4 nu rămâne.
    // Producție: fără această acoladă, effectOnRows rămâne deschisă și notDml nu compilează.
    // Expert: închiderea nu schimbă schema. Aceste trei propoziții nu sunt CREATE TABLE.
    }

    // Începător: propoziția creează tabela customers. Nu adaugă un rând pe care ROLLBACK să îl poată scoate.
    // Producție: CREATE TABLE customers (customer_id INT PRIMARY KEY); Dacă metoda dispare, nu mai desparți schema de INSERT.
    // Expert: vecinul este INSERT INTO customers. CREATE face commit implicit pe tabela netemporară. Nu garantează că un rând vechi a fost șters.
    /*
    🟢 Începător
        Ce este            propoziția de schemă cu care nu înlocuiești un INSERT
        De ce e nevoie    fără ea, candidatul dă ROLLBACK și așteaptă ca tabela să dispară
    🟡 Producție
        Cum se scrie       CREATE TABLE customers (customer_id INT PRIMARY KEY)
    🔴 Staff
        Unde               commit implicit înainte și după, pe tabelă care nu este TEMPORARY
    Pași                   1. Arată propoziția de DDL. 2. Nu o pune în locul unui rând anulabil.
    */
    public static String notDml()
    // Începător: deschide propoziția de schemă, nu un INSERT.
    // Producție: fără acoladă, CREATE TABLE customers (customer_id INT PRIMARY KEY) nu poate fi întors.
    // Expert: acolada nu face commit implicit. Commit-ul îl face serverul când execută CREATE TABLE netemporar.
    {
        // Începător: întoarce propoziția care lasă tabela customers și după un ROLLBACK ulterior.
        // Producție: return predă CREATE TABLE customers (customer_id INT PRIMARY KEY). Fără return, vecinul DML nu are cu ce să fie comparat.
        // Expert: return nu este un index și nu filtrează rânduri. Un INSERT în aceeași tranzacție, trimis înainte de acest CREATE, ar fi comis de el.
        return """
                CREATE TABLE customers (customer_id INT PRIMARY KEY)
                """;
    // Începător: închide propoziția care schimbă schema, nu rândul.
    // Producție: fără această acoladă, notDml nu se închide și grantCommitsInsert nu compilează.
    // Expert: închiderea nu anulează tabela. ROLLBACK ulterior tot lasă customers.
    }

    // Începător: rândul 4 rămâne, pentru că GRANT a închis tranzacția înainte ca ROLLBACK să apuce să șteargă ceva.
    // Producție: START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); GRANT ALL ON db1.* TO 'jeffrey'@'localhost'; ROLLBACK; Dacă metoda dispare, dispare eșecul cel mai mic.
    // Expert: GRANT nu este un DELETE. Commit-ul lui implicit nu garantează că privilegiul poate fi scos cu ROLLBACK.
    /*
    🟢 Începător
        Ce este            eșecul în care GRANT salvează rândul pe care voiai să îl anulezi
        De ce e nevoie    fără el, ROLLBACK pare să anuleze și privilegiul, și rândul
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); GRANT ALL ON db1.* TO 'jeffrey'@'localhost'; ROLLBACK;
    🔴 Staff
        Unde               GRANT face commit implicit înainte să ruleze, deci INSERT este deja durabil
    Pași                   1. Deschide tranzacția. 2. Inserează. 3. Dă privilegiul. 4. ROLLBACK nu mai scoate rândul.
    */
    public static String grantCommitsInsert()
    // Începător: deschide eșecul în care privilegiul salvează rândul 4.
    // Producție: fără acoladă, GRANT ALL ON db1.* TO 'jeffrey'@'localhost' nu poate fi întors împreună cu INSERT.
    // Expert: acolada nu scrie în schema mysql. Serverul comite INSERT-ul înainte să aplice GRANT.
    {
        // Începător: întoarce scriptul în care rândul 4 și privilegiul lui jeffrey rămân amândouă.
        // Producție: return predă INSERT-ul urmat de GRANT ALL ON db1.* TO 'jeffrey'@'localhost' și de ROLLBACK. Fără return, eșecul nu poate fi trimis.
        // Expert: return nu autentifică pe jeffrey. Serverul scrie privilegiul în schema mysql și comite rândul din customers înainte de GRANT.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                GRANT ALL ON db1.* TO 'jeffrey'@'localhost';
                ROLLBACK;
                """;
    // Începător: închide scriptul în care ROLLBACK nu mai scoate rândul 4.
    // Producție: fără această acoladă, grantCommitsInsert rămâne deschisă și stayInApplication nu compilează.
    // Expert: închiderea nu revocă privilegiul lui jeffrey. REVOKE este altă propoziție, tot cu commit implicit.
    }

    // Începător: tabela există deja. Adaugi rândul 4 și îl faci durabil doar când trimiți COMMIT. Nu mai trimiți CREATE TABLE.
    // Producție: START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); COMMIT; Dacă metoda dispare, nu mai ai alternativa la DDL.
    // Expert: condiția este că obiectul există și vrei un rând anulabil până la COMMIT. Un filtru dintr-un singur serviciu nu înlocuiește GRANT când contul este folosit și de alți clienți.
    /*
    🟢 Începător
        Ce este            alegerea DML când rândul trebuie să rămână anulabil până la decizia ta
        De ce e nevoie    fără ea, CREATE TABLE este trimis doar ca să „țină” rândul în tranzacție
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO customers (customer_id) VALUES (4); COMMIT;
    🔴 Staff
        Unde               durabilitatea se vede la COMMIT. JDBC poate ține granița tranzacției în setAutoCommit(false), fără un al doilea START TRANSACTION
    Pași                   1. Nu trimite DDL. 2. Inserează în tranzacția deschisă. 3. COMMIT face rândul durabil.
    */
    public static String stayInApplication()
    // Începător: deschide alternativa: rândul 4 devine durabil la COMMIT, fără CREATE TABLE.
    // Producție: fără acoladă, INSERT dintre START TRANSACTION și COMMIT nu poate fi întors.
    // Expert: acolada nu este setAutoCommit(false). Dacă JDBC a deschis deja tranzacția, START TRANSACTION din text o comite pe cea veche.
    {
        // Începător: întoarce propozițiile care adaugă rândul 4 și îl lasă durabil la COMMIT, fără să creeze tabela.
        // Producție: return predă START TRANSACTION, INSERT și COMMIT. Fără return, alternativa la CREATE TABLE nu este în cod.
        // Expert: return nu deschide tranzacția în JDBC. Dacă și JDBC a făcut setAutoCommit(false), un START TRANSACTION trimis pe deasupra comite tranzacția deja deschisă.
        return """
                START TRANSACTION;
                INSERT INTO customers (customer_id) VALUES (4);
                COMMIT;
                """;
    // Începător: închide alternativa în care rândul 4 rămâne la COMMIT.
    // Producție: fără această acoladă, stayInApplication nu se închide și clasa nu compilează.
    // Expert: închiderea nu face COMMIT. Durabilitatea apare când serverul execută COMMIT din text.
    }
// Începător: închide clasa după cele cinci propoziții.
// Producție: fără acolada finală, fișierul nu compilează și chooseByEffect nu poate fi apelat.
// Expert: închiderea clasei nu închide tranzacția de pe server și nu șterge privilegiul din schema mysql.
}
