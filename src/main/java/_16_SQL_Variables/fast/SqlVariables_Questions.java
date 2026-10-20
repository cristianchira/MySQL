/*
TOPIC:
    Întrebări despre alegerea dintre copia de sesiune și valoarea globală.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied for user 'root'@'localhost' (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști când îți trebuie SET SESSION și când propoziția nu are voie să fie SET GLOBAL.
MECHANISM:
    Șirul din SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps. Această clasă nu îl trimite.
LOOKALIKES:
    SET GLOBAL nu rescrie @@SESSION. SET SESSION max_connections este ERROR 1229.
BOOK THEORY:
    Manualul 9.7, 15.7.6.1: SET SESSION schimbă copia conexiunii. SET GLOBAL nu atinge sesiunile deschise. PDF-urile folderului nu conțin SET SESSION sql_mode = 'TRADITIONAL'.
BOOK EXAMPLE:
    SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps
REPO DEMO:
    src/main/java/_16_SQL_Variables/SQL_VARIABLES.java
BREAK LAB:
    SET SESSION max_connections = 1000, ținut în breakSessionKeywordOnGlobalOnly.
TRADE-OFFS:
    SET GLOBAL pentru conectările noi. Aplicația când serverul nu citește valoarea.
ANTI-PATTERNS:
    SET GLOBAL sql_mode ca să schimbi conexiunea deja deschisă.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlVariables_Staff_Interview. Aici sunt cinci, cu cheia scrisă.
NOVEL TRANSFER:
    Tejgheaua deschisă primește TRADITIONAL fără să schimbe tura următoare.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei variabile de sesiune (session system variable):
       SET SESSION sql_mode = 'TRADITIONAL' schimbă copia conexiunii curente.

Exemplu:
       SqlVariables_Terms.explainSessionVariable()
       Celelalte conexiuni nu își schimbă modul.

Definiția unei variabile globale (global system variable):
       SET GLOBAL max_connections = 1000 pregătește conexiunile noi și nu le rescrie pe cele deschise.

Exemplu:
       SqlVariables_Terms.explainGlobalVariable()
       SET SESSION max_connections este ERROR 1229.

Definiția unei variabile de utilizator (user-defined variable):
       SET @p_emp_no = 0 rămâne pe sesiune și nu este sql_mode.

Exemplu:
       SqlVariables_Terms.explainUserVariable()
       Alt client nu vede valoarea.

Definiția unei variabile locale (local variable):
       DECLARE v_avg_salary moare la END.

Exemplu:
       SqlVariables_Terms.explainLocalVariable()
       @ încă se vede după CALL.

Definiția unei coloane cu același nume (column):
       Numele comun face SELECT-ul să citească variabila locală.

Exemplu:
       SqlVariables_Terms.explainColumnNameCollision()
       Coloana nu filtrează.

1. Enunț: Ce problemă rezolvă SET SESSION sql_mode = 'TRADITIONAL'?
   Capcană: Modul se schimbă pentru fiecare conexiune deja deschisă.
   Cheie: Se schimbă copia conexiunii care trimite propoziția, la sfârșitul ei. Globalul rămâne. Nu se scrie un rând.
   Demonstrație: SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps
2. Enunț: Ce rămâne în tabele după SET SESSION?
   Capcană: sql_mode se salvează ca rând până la COMMIT.
   Cheie: Nu este numită o tabelă. Nu rămâne rând, index sau lacăt. SET nu intră în binary log.
   Demonstrație: SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps
3. Enunț: De ce SET GLOBAL max_connections nu este aceeași alegere?
   Capcană: GLOBAL rescrie și sesiunea care a trimis propoziția.
   Cheie: max_connections este doar globală. SET GLOBAL pregătește conectările noi. SET SESSION max_connections este ERROR 1229.
   Demonstrație: SQL_VARIABLES.breakSessionKeywordOnGlobalOnly
4. Enunț: Ce se rupe la SET SESSION max_connections = 1000?
   Capcană: Conexiunea primește limita 1000.
   Cheie: ERROR 1229. Nicio variabilă nu se schimbă. În tur propoziția nu a fost trimisă.
   Demonstrație: SQL_VARIABLES.breakSessionKeywordOnGlobalOnly
5. Enunț: Când lași valoarea în aplicație?
   Capcană: Orice număr trebuie să fie @@SESSION.
   Cheie: Când nicio propoziție de pe server nu o citește. DECLARE dacă moare la END. @ dacă propoziția următoare de pe aceeași conexiune o citește.
   Demonstrație: SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps

Întrebări adăugate:

🟢 Începător
    Ce este            cinci întrebări despre copie de sesiune și global, cu cheia scrisă și corpul gol
    De ce e nevoie     fără ele, slide-ul „toate conexiunile” pare răspunsul
🟡 Producție
    Cum se scrie       SET SESSION sql_mode = 'TRADITIONAL' în SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps
🔴 Staff
    Unde               SET GLOBAL rămâne vecinul. Clasa nu trimite propoziția.
Pași
    1. Citește enunțul.
    2. Cheia stă dedesubt, în fișier.
    3. Nu adăuga un main.
*/
// Începător: întrebările scurte stau în acest folder, fără o metodă care să răspundă singură.
// Producție: package _16_SQL_Variables.fast. Corpul clasei rămâne gol.
// Expert: Nu muta aici banca fiecărui copil. Subiectul este alegerea domeniului.
package _16_SQL_Variables.fast;

// Începător: clasa ține întrebările în comentariu și nu trimite nimic la server.
// Producție: public final class SqlVariables_Questions. Dacă primește o metodă, nu mai este clasa goală cerută la creare.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall și nu acoperă fiecare copil în întregime.
public final class SqlVariables_Questions {
    // Începător: nu există o propoziție în corp, fiindcă răspunsurile stau în comentariu.
    // Producție: acolada închide SqlVariables_Questions. Sub ea nu se adaugă un main.
    // Expert: sessionCopyTheCurrentConnectionKeeps rămâne în SQL_VARIABLES.java. Golul de aici nu șterge cele cinci întrebări.
}
