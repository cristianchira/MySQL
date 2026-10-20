/*
TOPIC:
    Întrebări despre celula stocată, nu despre rândul de rezultat.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. root@localhost a primit Access denied. Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști când 1 este o celulă și când este doar un rezultat.
MECHANISM:
    INSERT INTO entity_row (data_value) VALUES (1). Clasa nu îl trimite.
LOOKALIKES:
    SELECT 1 AS data_value nu scrie entity_row.
BOOK THEORY:
    Manualul 9.7, 1.2.1: rândurile și coloanele țin datele. Folderul nu are PDF.
BOOK EXAMPLE:
    Introduction_To_DB.storedDataValue
REPO DEMO:
    src/main/java/_1_SQL/_1_Introduction_To_DB/Introduction_To_DB.java
BREAK LAB:
    SELECT 1 AS data_value
TRADE-OFFS:
    Declarativ pentru citire. Celula pentru ce rămâne.
ANTI-PATTERNS:
    Aliasul luat drept record.
STAFF QUESTIONS:
    Întrebările 1–50 din IntroductionToDb_Staff_Interview.
NOVEL TRANSFER:
    Stocul de pe raft, nu cifra de pe ecranul scannerului.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei valori de date (data value):
       Valoarea de date este conținutul celulei, la întâlnirea rândului cu coloana.

Exemplu:
       IntroductionToDb_Terms.explainDataValue()
       INSERT pune conținutul. SELECT 1 nu îl pune.

Definiția unui rând (row):
       Rândul stocat este înregistrarea din tabelă.

Exemplu:
       IntroductionToDb_Terms.explainRecord()

Definiția unei coloane (column):
       Coloana este atributul vertical. Fără rând, nu are valoare de date.

Exemplu:
       IntroductionToDb_Terms.explainColumn()

Definiția unei tabele (table):
       Tabela ține rândurile. Baza relațională le separă în tabele.

Exemplu:
       IntroductionToDb_Terms.explainTable()

1. Enunț: Ce problemă rezolvă celula când alt client trebuie să vadă același 1 mai târziu?
   Capcană: Îi trimiți SELECT 1 AS data_value și ambii văd stocul.
   Cheie: SELECT 1 nu lasă celulă. INSERT INTO entity_row (data_value) VALUES (1) o lasă, dacă tabela există și propoziția se termină.
   Demonstrație: Introduction_To_DB.storedDataValue
2. Enunț: Ce este în entity_row după un SELECT 1 AS data_value terminat?
   Capcană: Un rând nou, pentru că aliasul se numește data_value.
   Cheie: Nimic nou. Propoziția nu numește tabela. Celula cere INSERT.
   Demonstrație: Introduction_To_DB.resultIsNotACell
3. Enunț: De ce comentariul despre rând și coloană nu este celula?
   Capcană: Descrie intersecția, deci valoarea există.
   Cheie: Comentariul nu este trimis. Fără propoziție terminată într-o tabelă reală, nu există conținut.
   Demonstrație: Introduction_To_DB.storedDataValue
4. Enunț: Ce se întâmplă dacă INSERT pleacă și entity_row nu există?
   Capcană: Serverul creează tabela și pune 1.
   Cheie: INSERT nu creează schema. Propoziția eșuează. Nu rămâne nici rând, nici coloană.
   Demonstrație: Introduction_To_DB.storedDataValue
5. Enunț: Când lași 1 doar în aplicație?
   Capcană: Orice valoare de date trebuie să treacă printr-un INSERT.
   Cheie: Când niciun alt client nu trebuie să o citească din tabelă. Celula este pentru valoarea care rămâne după ce procesul se închide.
   Demonstrație: Introduction_To_DB.resultIsNotACell

Întrebări adăugate:

🟢 Începător
    Ce este            cinci întrebări despre celulă, cu cheia scrisă
    De ce e nevoie     fără ele, 1 de pe ecran pare un record
🟡 Producție
    Cum se scrie       INSERT INTO entity_row (data_value) VALUES (1)
🔴 Staff
    Unde               celula este în tabelă după propoziție. Clasa nu trimite INSERT-ul.
Pași
    1. Citește enunțul.
    2. Cheia stă în fișier.
    3. Nu adăuga un main.
*/
// Începător: întrebările stau lângă celulă, fără o metodă care răspunde singură.
// Producție: package _1_SQL._1_Introduction_To_DB.fast. Corpul rămâne gol.
// Expert: Nu muta aici banca de la _2_SQL_Statements. Subiectul este celula față de rezultat.
package _1_SQL._1_Introduction_To_DB.fast;

// Începător: clasa ține întrebările în comentariu.
// Producție: public final class IntroductionToDb_Questions. O metodă în corp o scoate din forma goală.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall.
public final class IntroductionToDb_Questions {
    // Începător: în corp nu există o propoziție.
    // Producție: acolada închide IntroductionToDb_Questions.
    // Expert: storedDataValue rămâne în Introduction_To_DB. Golul nu șterge cele cinci întrebări.
}
