package _2_SQL_Statements;


// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8777552#overview

// https://en.wikipedia.org/wiki/SQL_syntax

// https://dev.mysql.com/doc/refman/8.0/en/sql-statements.html



/*                                                  SQLSyntax


    ○ Comprises several types of statements
                  * that allow you to perform various
                                - commands
                                - and operations


                                                       DDL


    ○ A syntax

    ○ A set of statements that allow the user
                  * to define
                  * or modify data structure and objects, such as table

 */



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
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE -> SqlStatements_Fast.chooseByEffect -> CREATE TABLE customers rămâne după ROLLBACK, rândul inserat nu rămâne -> nu garantează că un GRANT din același script ar fi fost anulat
    2. Eșecul cel mai mic
           -> DECIDE -> SqlStatements_Fast.grantCommitsInsert -> INSERT INTO customers este deja comis când începe GRANT ALL -> nu garantează că ROLLBACK mai scoate acel rând
    3. Granița
           -> propoziția este textul executat de server. setAutoCommit din JDBC este API-ul clientului. Click-ul din Workbench nu este mecanismul.
    4. Alternativa
           -> dacă rândul trebuie să rămână anulabil, alegi INSERT între START TRANSACTION și COMMIT, nu CREATE TABLE. Le separă obiectul: schema față de rând.
    5. Anti-patternul
           -> ROLLBACK după CREATE TABLE customers lasă tabela. Commit-ul implicit s-a întâmplat la CREATE, nu la ROLLBACK.
    6. Dovada
           -> SqlStatements_Fast.chooseByEffect ține propozițiile. Efectul pe server este NERULAT AICI: repo-ul nu are host, utilizator sau port.
    7. Interviu
           -> CREATE TABLE schimbă schema și face commit implicit; INSERT din START TRANSACTION dispare la ROLLBACK; GRANT între ele comite rândul.
1. Ce problemă rezolvă alegerea propoziției
    Enunț: Tabela trebuie să rămână, iar rândul adăugat trebuie să poată dispărea. Ce propoziții trimiți, și în ce ordine?
    Capcană: Trimit CREATE TABLE în aceeași tranzacție cu INSERT și dau ROLLBACK la amândouă.
    Cheie: CREATE TABLE customers (customer_id INT PRIMARY KEY) schimbă schema și face commit implicit. INSERT de după START TRANSACTION dispare la ROLLBACK. Tabela rămâne.
    Demonstrație: SqlStatements_Fast.chooseByEffect
2. Ce se întâmplă cu rândurile, schema sau tranzacția
    Enunț: După START TRANSACTION, INSERT INTO customers (customer_id) VALUES (4) și ROLLBACK, ce rând mai este în customers?
    Capcană: Rândul rămâne, pentru că INSERT s-a executat.
    Cheie: În tranzacția deschisă, rândul este vizibil sesiunii care l-a scris și nu este durabil. ROLLBACK îl scoate. Schema nu se schimbă cu aceste trei propoziții.
    Demonstrație: SqlStatements_Fast.effectOnRows
3. De ce nu este mecanismul vecin
    Enunț: De ce CREATE TABLE customers nu este un INSERT?
    Capcană: Ambele adaugă ceva, deci ROLLBACK le anulează la fel.
    Cheie: CREATE TABLE schimbă schema și face commit implicit pe tabela netemporară. INSERT schimbă un rând și poate rămâne necomis până la COMMIT sau ROLLBACK.
    Demonstrație: SqlStatements_Fast.notDml
4. Cum eșuează când este folosit greșit
    Enunț: Ce rând rămâne dacă GRANT ALL stă între INSERT și ROLLBACK?
    Capcană: ROLLBACK anulează tot ce s-a scris după START TRANSACTION, inclusiv GRANT.
    Cheie: GRANT ALL ON db1.* TO 'jeffrey'@'localhost' face commit implicit înainte să ruleze, deci comite INSERT-ul. ROLLBACK nu mai are acel rând de anulat. Privilegiul rămâne.
    Demonstrație: SqlStatements_Fast.grantCommitsInsert
5. Ce alegi în loc și în ce condiție
    Enunț: Tabela customers există deja, iar rândul trebuie să poată fi anulat până decizi tu. Ce trimiți în loc de CREATE TABLE?
    Capcană: Mai trimit un CREATE TABLE, ca să fiu sigur că tabelul există în tranzacție.
    Cheie: Alegi START TRANSACTION, INSERT INTO customers (customer_id) VALUES (4), apoi COMMIT sau ROLLBACK. CREATE TABLE nu ține rândul anulabil: el închide tranzacția.
    Demonstrație: SqlStatements_Fast.stayInApplication
*/

public class SqlStatements {
}
