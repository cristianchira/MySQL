/*
TOPIC:
    Întrebări despre propoziția pe care o execută serverul, nu despre celula stocată.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști când îți trebuie propoziția și când îți trebuie tabela.
MECHANISM:
    Șirul din SQL.statementTheServerExecutes. Această clasă nu îl trimite.
LOOKALIKES:
    Celula din Introduction_To_DB există fără propoziție. SELECT 1 AS data_value nu o creează.
BOOK THEORY:
    Manualul 9.7, 1.2.1: SQL este limbajul cu care accesezi colecția, iar serverul execută. PDF-ul de curs nu conține această propoziție.
BOOK EXAMPLE:
    SQL.statementTheServerExecutes
REPO DEMO:
    src/main/java/_1_SQL/SQL.java
BREAK LAB:
    FOR EACH entity_row RETURN data_value, ținut în proseLoopTheServerRejects.
TRADE-OFFS:
    Aplicația, când valoarea nu pleacă din proces. Tabela, când îți trebuie celula.
ANTI-PATTERNS:
    Comentariul de curs trimis ca propoziție.
STAFF QUESTIONS:
    Întrebările 1–50 din Sql_Staff_Interview. Aici sunt cinci, cu cheia scrisă.
NOVEL TRANSFER:
    Cifra de control a unui colet, fără un rând nou pe raft.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei propoziții SQL (SQL statement):
       Propoziția este textul pe care îl execută serverul. SELECT 1 AS data_value cere un rând de rezultat și nu scrie o celulă.

Exemplu:
       Sql_Terms.explainSqlStatement()
       Tabelele rămân neschimbate după ce rezultatul a plecat.

Definiția unei baze de date (database):
       Baza de date este colecția structurată. Serverul o prelucrează. Propoziția nu adaugă o colecție.

Exemplu:
       Sql_Terms.explainDatabase()
       SELECT 1 AS data_value nu creează o bază.

Definiția unei tabele (table):
       Tabela ține rândurile. Propoziția care nu o numește nu citește și nu scrie în ea.

Exemplu:
       Sql_Terms.explainTable()
       SELECT data_value FROM entity_row numește tabela. SELECT 1 AS data_value nu o numește.

Definiția unei coloane (column):
       Coloana este atributul vertical stocat. Aliasul din rezultat nu creează atributul în schemă.

Exemplu:
       Sql_Terms.explainColumn()
       data_value din SELECT 1 AS data_value este numele din rezultat.

Definiția unui rând (row):
       Rândul stocat este înregistrarea din tabelă. Rândul de rezultat trăiește până pleacă la client.

Exemplu:
       Sql_Terms.explainRow()
       Un 1 primit de client nu este un record din Introduction_To_DB.

1. Enunț: Ce problemă rezolvă propoziția când nicio tabelă nu trebuie să primească un rând?
   Capcană: Descrii coloana în comentariu și clientul primește deja valoarea.
   Cheie: Comentariul nu ajunge la server. SELECT 1 AS data_value este propoziția. Celula nu apare.
   Demonstrație: SQL.statementTheServerExecutes
2. Enunț: Ce rămâne în tabele după ce serverul ar termina SELECT 1 AS data_value?
   Capcană: Aliasul data_value lasă o coloană și un index.
   Cheie: Nicio tabelă nu este numită. Nu rămâne rând stocat, index sau lacăt. Rândul de rezultat se vede la sfârșitul propoziției.
   Demonstrație: SQL.statementTheServerExecutes
3. Enunț: De ce data_value de aici nu este celula din Introduction_To_DB?
   Capcană: Același nume înseamnă aceeași celulă.
   Cheie: Celula stă la întâlnirea unui rând cu o coloană, fără propoziție. Aliasul din SELECT 1 este doar numele din rezultat.
   Demonstrație: SQL.statementTheServerExecutes
4. Enunț: Ce se rupe dacă scrii FOR EACH entity_row RETURN data_value?
   Capcană: Optimizerul primește planul și îl rulează.
   Cheie: Textul nu este o propoziție. Serverul nu începe. Rândurile stocate nu se schimbă.
   Demonstrație: SQL.proseLoopTheServerRejects
5. Enunț: Când calculezi 1 în aplicație?
   Capcană: Fără server, 1 nu este o valoare.
   Cheie: Când niciun alt client nu trebuie să o vadă. Trimiți propoziția când rezultatul trebuie să vină de la server. Când îți trebuie celula, numești tabela și ieși din acest folder.
   Demonstrație: SQL.statementTheServerExecutes

Întrebări adăugate:

🟢 Începător
    Ce este            cinci întrebări despre propoziție, cu cheia scrisă și corpul gol
    De ce e nevoie     fără ele, comentariul de curs pare că este deja răspunsul
🟡 Producție
    Cum se scrie       SELECT 1 AS data_value în SQL.statementTheServerExecutes
🔴 Staff
    Unde               celula din Introduction_To_DB rămâne vecinul. Clasa nu trimite propoziția.
Pași
    1. Citește enunțul.
    2. Cheia stă dedesubt, în fișier.
    3. Nu adăuga un main.
*/
// Începător: întrebările scurte stau în acest folder, fără o metodă care să răspundă singură.
// Producție: package _1_SQL.fast. Corpul clasei rămâne gol.
// Expert: Nu muta aici banca de la _2_SQL_Statements. Subiectul este propoziția față de celulă.
package _1_SQL.fast;

// Începător: clasa ține întrebările în comentariu și nu trimite nimic la server.
// Producție: public final class Sql_Questions. Dacă primește o metodă, nu mai este clasa goală cerută la creare.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall și nu acoperă TRUNCATE sau DELETE.
public final class Sql_Questions {
    // Începător: nu există o propoziție în corp, fiindcă răspunsurile stau în comentariu.
    // Producție: acolada închide Sql_Questions. Sub ea nu se adaugă un main.
    // Expert: statementTheServerExecutes rămâne în SQL.java. Golul de aici nu șterge cele cinci întrebări.
}
