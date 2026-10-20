/*
TOPIC:
    Cererea numește data_value. Optimizerul face planul. Bucla FOR EACH nu pornește.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. root@localhost a primit Access denied. EXPLAIN nu a fost citit. Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să arăți propoziția care cere valoarea stocată, fără să scrii pașii.
MECHANISM:
    SQL_Declarative_Language.namedResult ține SELECT data_value FROM entity_row. main o tipărește.
LOOKALIKES:
    FOR EACH entity_row RETURN data_value scrie pașii. Celula există și fără această cerere.
BOOK THEORY:
    Manualul 9.7, 10.2.1: optimizerul construiește planul, EXPLAIN arată indexul. PDF-ul spune că optimizerul alege abordarea între TRUNCATE și DELETE.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_1_SQL/_2_SQL_Declarative_Language/SQL_Declarative_Language.java
BREAK LAB:
    SQL_Declarative_Language.proseLoopTheServerRejects
TRADE-OFFS:
    Aplicația pentru o listă care nu este în tabelă. Celula când întrebarea este ce rămâne stocat.
ANTI-PATTERNS:
    Comentariul despre optimizer trimis ca plan.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlDeclarativeLanguage_Staff_Interview.
NOVEL TRANSFER:
    Dispecerul cere cantitatea produsului 7 și nu scrie traseul prin depozit.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei propoziții declarative (declarative statement):
       Propoziția numește rezultatul. Optimizerul construiește planul. Tu nu scrii pașii.

Exemplu:
       SqlDeclarativeLanguage_Terms.explainDeclarativeStatement()
       SELECT data_value FROM entity_row cere valoarea. FOR EACH nu este propoziția.

Definiția unui plan de execuție (execution plan):
       Planul este construit de optimizer din statistici. EXPLAIN îl arată. Propoziția nu îl dictează.

Exemplu:
       SqlDeclarativeLanguage_Terms.explainExecutionPlan()
       În tur EXPLAIN nu a fost citit.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei valoarea, fără pași scriși -> main -> SELECT data_value FROM entity_row -> nu garantează indexul: EXPLAIN nu a fost citit
    2. Eșecul cel mai mic
           -> DECIDE scrii parcurgerea -> proseLoopTheServerRejects -> FOR EACH entity_row RETURN data_value -> nu garantează eroarea în tur
    3. Granița
           -> Propoziția este mecanismul. System.out nu este planul. Workbench nu este optimizerul.
    4. Alternativa
           -> Celula când întrebi ce rămâne. Aplicația când lista nu este în tabelă.
    5. Anti-patternul
           -> Bucla trimisă ca plan.
    6. Dovada
           -> main tipărește namedResult. Autentificarea a fost refuzată.
    7. Interviu
           -> Tu numești rezultatul. Optimizerul face planul. FOR EACH nu pornește.

🟢 Începător
    Ce este            exemplul minim: cererea fără pașii de căutare
    De ce e nevoie     fără șir, comentariul despre optimizer pare că este planul
🟡 Producție
    Cum se scrie       System.out.println(SQL_Declarative_Language.namedResult())
🔴 Staff
    Unde               tipărirea nu este execuția. Vecinul este bucla FOR EACH.
Pași
    1. Ia SELECT-ul.
    2. Tipărește-l.
    3. Nu deschide conexiunea și nu trimite bucla.
*/
// Începător: lecția scurtă stă lângă clasa care descrie cererea.
// Producție: package _1_SQL._2_SQL_Declarative_Language.fast. Dacă linia dispare, main nu mai este lângă clasa de topic.
// Expert: Pachetul nu alege planul. Introduction_To_DB rămâne folderul celulei.
package _1_SQL._2_SQL_Declarative_Language.fast;

// Începător: clasa pornește cererea, nu parcurgerea.
// Producție: public final class SqlDeclarativeLanguage_Fast. Fără ea nu există main pentru acest mecanism.
// Expert: Nu predă pe rând DDL, DML, DCL și TCL. SELECT-ul de aici este doar dovada cererii.
public final class SqlDeclarativeLanguage_Fast {
    // Începător: pornește exemplul și scrie cererea.
    // Producție: public static void main(String[] args). args nu schimbă planul.
    // Expert: Nu cheamă proseLoopTheServerRejects. Nu deschide JDBC. Tipărirea nu este EXPLAIN.
    public static void main(String[] args) {
        // Începător: pe ecran apare SELECT-ul, iar optimizerul nu pornește din această linie.
        // Producție: System.out.println(SQL_Declarative_Language.namedResult()), adică SELECT data_value FROM entity_row.
        // Expert: println nu trimite propoziția la 3306. FOR EACH tot nu este un plan. Celula rămâne, pentru că această linie nu scrie și nu citește.
        System.out.println(_1_SQL._2_SQL_Declarative_Language.SQL_Declarative_Language.namedResult());
        // Începător: exemplul s-a oprit după cerere.
        // Producție: acolada închide main. args rămâne nefolosit.
        // Expert: Refuzul root@localhost nu este un plan citit de această acoladă.
    }
    // Începător: lecția se oprește la cerere.
    // Producție: acolada închide SqlDeclarativeLanguage_Fast.
    // Expert: Cine întreabă ce rămâne stocat iese la Introduction_To_DB. Cine clasifică TRUNCATE față de DELETE iese la propozițiile acelea.
}
