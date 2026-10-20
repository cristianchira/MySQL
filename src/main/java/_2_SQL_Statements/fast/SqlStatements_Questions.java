// Începător: întrebările stau lângă alegerea propoziției, nu lângă o clauză din SELECT.
// Producție: package _2_SQL_Statements.fast; dacă dispare, întrebările nu mai compilează în folderul propozițiilor.
// Expert: pachetul nu execută SQL și nu deschide tranzacția. Nu este schema mysql în care GRANT scrie privilegiul.
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

// Începător: clasa păstrează întrebările despre ce propoziție lași în urmă, fără să execute un răspuns.
// Producție: public class SqlStatements_Questions nu are metode. Dacă o umpli cu corp, FAQ-ul nu mai este gol.
// Expert: clasa nu trimite CREATE TABLE și nu ține lacăt. Răspunsul executabil stă în SqlStatements_Fast și în banca de cincizeci.
/*
🟢 Începător
    Ce este            întrebările cu care recunoști propoziția după efect
    De ce e nevoie    fără ele, CREATE și INSERT rămân două nume
🟡 Producție
    Cum se scrie       întrebarea stă în comentariu, iar corpul clasei rămâne fără metode
🔴 Staff
    Unde               cheia executabilă este metoda din Fast sau din Staff_Interview, nu acest corp gol
Pași                   1. Citește enunțul. 2. Nu căuta o metodă în această clasă.
*/
public class SqlStatements_Questions
// Începător: deschide lista de întrebări, fără o propoziție executată aici.
// Producție: fără acoladă, comentariile cu întrebări nu au unde să stea.
// Expert: acolada nu face commit implicit și nu separă DDL de DML.
{

// 1. Ce rămâne după CREATE TABLE customers (customer_id INT PRIMARY KEY) urmat de ROLLBACK: tabela sau nimic?
// 2. După START TRANSACTION, INSERT INTO customers (customer_id) VALUES (4) și ROLLBACK, mai este rândul 4?
// 3. De ce CREATE TABLE nu este INSERT, dacă ambele „adaugă” ceva?
// 4. Ce rând rămâne dacă GRANT ALL ON db1.* TO 'jeffrey'@'localhost' stă între INSERT și ROLLBACK?
// 5. Când tabela există deja și rândul trebuie să poată dispărea, ce trimiți în loc de CREATE TABLE?
// 6. Cu autocommit lăsat pe 1, de ce un COMMIT scris după INSERT nu este momentul în care rândul a devenit durabil?

// Întrebări adăugate:

// Începător: închide lista fără să adauge o metodă.
// Producție: fără acolada finală, clasa nu compilează și întrebările nu rămân în acest fișier.
// Expert: închiderea nu anulează un INSERT și nu revocă privilegiul lui jeffrey.
}
