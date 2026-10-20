/*
TOPIC:
    Întrebări despre cererea care numește rezultatul, nu despre celulă și nu despre pașii scriși de tine.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. root@localhost a primit Access denied. EXPLAIN nu a fost citit.
INTENT:
    Să recunoști când trimiți SELECT și când ai scris o buclă.
MECHANISM:
    SELECT data_value FROM entity_row. Clasa nu îl trimite.
LOOKALIKES:
    FOR EACH entity_row RETURN data_value nu este planul.
BOOK THEORY:
    Manualul 9.7, 10.2.1: optimizerul construiește planul. PDF-ul: abordări diferite pentru TRUNCATE și DELETE.
BOOK EXAMPLE:
    SQL_Declarative_Language.namedResult
REPO DEMO:
    src/main/java/_1_SQL/_2_SQL_Declarative_Language/SQL_Declarative_Language.java
BREAK LAB:
    FOR EACH entity_row RETURN data_value
TRADE-OFFS:
    Aplicația pentru o listă locală. Celula pentru ce rămâne stocat.
ANTI-PATTERNS:
    Bucla trimisă ca plan.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlDeclarativeLanguage_Staff_Interview.
NOVEL TRANSFER:
    Dispecerul cere cantitatea și nu scrie traseul.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei propoziții declarative (declarative statement):
       Propoziția numește rezultatul. Optimizerul construiește planul.

Exemplu:
       SqlDeclarativeLanguage_Terms.explainDeclarativeStatement()

Definiția unui plan de execuție (execution plan):
       Planul este al optimizerului. EXPLAIN îl arată. Bucla nu îl înlocuiește.

Exemplu:
       SqlDeclarativeLanguage_Terms.explainExecutionPlan()

1. Enunț: Ce problemă rezolvă SELECT data_value FROM entity_row față de o buclă?
   Capcană: Bucla este mai clară, deci optimizerul o preferă.
   Cheie: SELECT numește rezultatul. Optimizerul face planul. FOR EACH nu este o propoziție.
   Demonstrație: SQL_Declarative_Language.namedResult
2. Enunț: Ce se întâmplă cu celula după ce SELECT-ul s-ar termina?
   Capcană: data_value pleacă din tabelă în client.
   Cheie: Celula rămâne. Clientul primește un rezultat. În tur propoziția nu a fost trimisă.
   Demonstrație: SQL_Declarative_Language.namedResult
3. Enunț: De ce cererea nu este celula?
   Capcană: Ambele se numesc data_value, deci sunt același lucru.
   Cheie: Celula este conținutul stocat. SELECT o cere. O tabelă goală nu primește un record din acest SELECT.
   Demonstrație: SQL_Declarative_Language.namedResult
4. Enunț: Ce pornește serverul pe FOR EACH entity_row RETURN data_value?
   Capcană: Planul, apoi îl întrerupe la primul rând.
   Cheie: Nimic. Textul nu este o propoziție. Celulele rămân neatinse.
   Demonstrație: SQL_Declarative_Language.proseLoopTheServerRejects
5. Enunț: Când parcurgi lista în aplicație?
   Capcană: Niciodată, pentru că orice citire este SQL.
   Cheie: Când lista nu stă într-o tabelă. SELECT pleacă când rezultatul trebuie să vină din celulele serverului.
   Demonstrație: SQL_Declarative_Language.namedResult

Întrebări adăugate:

🟢 Începător
    Ce este            cinci întrebări despre cerere, cu cheia scrisă
    De ce e nevoie     fără ele, bucla pare planul
🟡 Producție
    Cum se scrie       SELECT data_value FROM entity_row
🔴 Staff
    Unde               planul este al optimizerului. Clasa nu trimite SELECT-ul.
Pași
    1. Citește enunțul.
    2. Cheia stă în fișier.
    3. Nu adăuga un main.
*/
// Începător: întrebările stau lângă cerere, fără o metodă care răspunde singură.
// Producție: package _1_SQL._2_SQL_Declarative_Language.fast. Corpul rămâne gol.
// Expert: Nu muta aici banca celulei. Subiectul este cine alege pașii.
package _1_SQL._2_SQL_Declarative_Language.fast;

// Începător: clasa ține întrebările în comentariu.
// Producție: public final class SqlDeclarativeLanguage_Questions. O metodă în corp o scoate din forma goală.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall.
public final class SqlDeclarativeLanguage_Questions {
    // Începător: în corp nu există o propoziție.
    // Producție: acolada închide SqlDeclarativeLanguage_Questions.
    // Expert: namedResult rămâne în clasa de topic. Golul nu șterge cele cinci întrebări.
}
