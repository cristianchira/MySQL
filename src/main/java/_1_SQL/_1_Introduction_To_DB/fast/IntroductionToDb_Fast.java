/*
TOPIC:
    Celula stocată: INSERT lasă data_value în rând. SELECT 1 nu o scrie.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. root@localhost a primit Access denied. Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să arăți propoziția care lasă o celulă, fără să pretinzi că a și fost trimisă.
MECHANISM:
    Introduction_To_DB.storedDataValue ține INSERT INTO entity_row (data_value) VALUES (1). main o tipărește.
LOOKALIKES:
    SELECT 1 AS data_value întoarce un rând de rezultat și lasă tabela neschimbată.
BOOK THEORY:
    Manualul 9.7, 1.2.1: datele relaționale stau în tabele, pe rânduri și coloane. Folderul nu are PDF.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_1_SQL/_1_Introduction_To_DB/Introduction_To_DB.java
BREAK LAB:
    Introduction_To_DB.resultIsNotACell
TRADE-OFFS:
    Declarativ când întrebi ce să citești. Celula când valoarea trebuie să rămână.
ANTI-PATTERNS:
    Aliasul data_value luat drept record.
STAFF QUESTIONS:
    Întrebările 1–50 din IntroductionToDb_Staff_Interview.
NOVEL TRANSFER:
    Cantitatea de pe un raft. Afișajul scannerului fără rând scris nu este stocul.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei valori de date (data value):
       Valoarea de date este conținutul celulei, la întâlnirea rândului cu coloana.

Exemplu:
       IntroductionToDb_Terms.explainDataValue()
       INSERT INTO entity_row (data_value) VALUES (1) pune conținutul. SELECT 1 nu îl pune.

Definiția unui rând (row):
       Rândul stocat este înregistrarea din tabelă. Un rând de rezultat nu rămâne după ce clientul l-a primit.

Exemplu:
       IntroductionToDb_Terms.explainRecord()
       Record-ul cere un INSERT terminat într-o tabelă care există.

Definiția unei coloane (column):
       Coloana este atributul vertical. Fără un rând, nu are încă o valoare de date.

Exemplu:
       IntroductionToDb_Terms.explainColumn()
       data_value din INSERT este coloana. Același nume din SELECT 1 este alias.

Definiția unei tabele (table):
       Tabela ține rândurile. O bază relațională le ține în tabele separate.

Exemplu:
       IntroductionToDb_Terms.explainTable()
       Fără entity_row, INSERT nu are unde să lase celula.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei celula -> main -> INSERT INTO entity_row (data_value) VALUES (1) -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE 1 afișat este stocul -> resultIsNotACell -> SELECT 1 AS data_value -> nu garantează un rând în entity_row
    3. Granița
           -> Celula este pe server. System.out arată șirul. Workbench nu este celula.
    4. Alternativa
           -> Declarativ când ceri citirea. Aplicația când valoarea nu trebuie să rămână.
    5. Anti-patternul
           -> Aliasul tratat ca record.
    6. Dovada
           -> main tipărește storedDataValue. Autentificarea a fost refuzată.
    7. Interviu
           -> Data value este celula. INSERT o lasă. SELECT 1 nu o scrie.

🟢 Începător
    Ce este            exemplul minim: propoziția care lasă o celulă
    De ce e nevoie     fără șir, comentariul despre rând și coloană pare că a și scris
🟡 Producție
    Cum se scrie       System.out.println(Introduction_To_DB.storedDataValue())
🔴 Staff
    Unde               tipărirea nu este execuția. Vecinul este SELECT 1 AS data_value.
Pași
    1. Ia INSERT-ul.
    2. Tipărește-l.
    3. Nu deschide conexiunea.
*/
// Începător: lecția scurtă stă lângă clasa care descrie celula.
// Producție: package _1_SQL._1_Introduction_To_DB.fast. Dacă linia dispare, main nu mai este lângă Introduction_To_DB.
// Expert: Pachetul nu scrie celula. _2_SQL_Declarative_Language rămâne folderul cererii, nu al stocului.
package _1_SQL._1_Introduction_To_DB.fast;

// Începător: clasa pornește propoziția care lasă o celulă.
// Producție: public final class IntroductionToDb_Fast. Fără ea nu există main pentru acest mecanism.
// Expert: Nu predă DDL, DML, DCL sau TCL pe rând. INSERT-ul de aici este doar dovada celulei.
public final class IntroductionToDb_Fast {
    // Începător: pornește exemplul și scrie propoziția celulei.
    // Producție: public static void main(String[] args). args nu creează entity_row.
    // Expert: Nu cheamă resultIsNotACell. Nu deschide JDBC. Tipărirea nu este COMMIT.
    public static void main(String[] args) {
        // Începător: pe ecran apare INSERT-ul, iar tabela nu se umple din această linie.
        // Producție: System.out.println(Introduction_To_DB.storedDataValue()), adică INSERT INTO entity_row (data_value) VALUES (1).
        // Expert: println nu trimite propoziția la 3306. SELECT 1 AS data_value tot nu scrie celula. Fără entity_row, INSERT-ul refuzat nu lasă un rând.
        System.out.println(_1_SQL._1_Introduction_To_DB.Introduction_To_DB.storedDataValue());
        // Începător: exemplul s-a oprit după propoziția celulei.
        // Producție: acolada închide main. args rămâne nefolosit.
        // Expert: Refuzul root@localhost nu este un rezultat al acestei acolade.
    }
    // Începător: lecția se oprește la celulă.
    // Producție: acolada închide IntroductionToDb_Fast.
    // Expert: Cine întreabă cine alege pașii iese la limbajul declarativ. Cine clasifică CREATE iese la propoziții.
}
