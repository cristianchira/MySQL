/*
Definiția unei propoziții declarative (declarative statement):
       Propoziția numește rezultatul pe care îl vrei. Optimizerul construiește planul. Tu nu scrii pașii.

Exemplu:
       SqlDeclarativeLanguage_Terms.explainDeclarativeStatement()
       SELECT data_value FROM entity_row cere valoarea. FOR EACH entity_row RETURN data_value nu este propoziția.

Definiția unui plan de execuție (execution plan):
       Planul este construit de optimizer. ANALYZE TABLE îi dă statistici. EXPLAIN arată indexul folosit. Propoziția nu dictează pașii.

Exemplu:
       SqlDeclarativeLanguage_Terms.explainExecutionPlan()
       În tur EXPLAIN nu a fost citit, pentru că root@localhost a fost refuzat.

Terms & Definitions:
*/
// Începător: glosarul desparte cererea de planul pe care îl face optimizerul.
// Producție: package _1_SQL._2_SQL_Declarative_Language.fast. Metodele țin șirurile, fără conexiune.
// Expert: Nu muta aici definiția celulei. Introduction_To_DB spune ce rămâne stocat.
package _1_SQL._2_SQL_Declarative_Language.fast;

// Începător: clasa execută definiția cererii și a planului.
// Producție: public final class SqlDeclarativeLanguage_Terms. Fără ea, Exemplu: nu are metodă.
// Expert: Nu predă pe rând cele patru familii DDL, DML, DCL și TCL. Ele se clasifică în _2_SQL_Statements.
public final class SqlDeclarativeLanguage_Terms {

    /*
    🟢 Începător
        Ce este            cererea care numește valoarea, nu drumul până la ea
        De ce e nevoie     fără șir, comentariul despre optimizer nu este o propoziție
    🟡 Producție
        Cum se scrie       SELECT data_value FROM entity_row
    🔴 Staff
        Unde               rezultatul este la sfârșitul propoziției. Planul nu este scris în text. Celula rămâne.
    Pași
        1. Înapoiază SELECT-ul.
        2. Nu deschide conexiunea.
    */
    // Începător: metoda dă cererea, nu pașii.
    // Producție: public static String explainDeclarativeStatement(). Dacă dispare, termenul nu are exemplu.
    // Expert: Nu este explain din EXPLAIN. EXPLAIN arată planul și nu schimbă celula. În tur nu a fost rulat.
    public static String explainDeclarativeStatement() {
        // Începător: clientul cere data_value din rândurile care există, fără să spună în ce ordine le cauți.
        // Producție: SELECT data_value FROM entity_row. Fără șir, definiția nu are propoziție.
        // Expert: Nu este INSERT și nu este FOR EACH. INSERT ar scrie o celulă. Bucla nu pornește. Acest SELECT o cere.
        return "SELECT data_value FROM entity_row";
        // Începător: metoda a dat cererea, nu planul.
        // Producție: acolada închide explainDeclarativeStatement. Șirul nu a plecat.
        // Expert: NERULAT pe server nu schimbă definiția. Acolada nu este un index.
    }

    /*
    🟢 Începător
        Ce este            drumul pe care îl alege optimizerul, nu textul pe care îl scrii tu
        De ce e nevoie     fără deosebire, bucla pare planul
    🟡 Producție
        Cum se scrie       SELECT data_value FROM entity_row
    🔴 Staff
        Unde               manualul 10.2.1: statisticile din ANALYZE TABLE intră în plan. EXPLAIN arată indexul. Bucla nu este planul.
    Pași
        1. Înapoiază propoziția al cărei plan nu este scris în ea.
        2. Nu inventa un rând de EXPLAIN.
    */
    // Începător: metoda ține propoziția al cărei plan îl face serverul.
    // Producție: public static String explainExecutionPlan(). Dacă dispare, termenul plan nu are șir.
    // Expert: Nu cita un index anume. În tur EXPLAIN nu a fost citit, pentru că autentificarea a căzut.
    public static String explainExecutionPlan() {
        // Începător: textul spune ce coloană vrei. Nu spune ce index să folosești.
        // Producție: SELECT data_value FROM entity_row. Dacă adaugi pașii în șir, nu mai este planul optimizerului.
        // Expert: PDF-ul arată că optimizerul alege abordarea între TRUNCATE și DELETE. Același fapt: pașii nu stau în propoziția ta. Nu amesteca aici golirea tabelei.
        return "SELECT data_value FROM entity_row";
        // Începător: metoda nu a citit un plan.
        // Producție: acolada închide explainExecutionPlan.
        // Expert: Un index nefolosit nu grăbește cererea. Fără EXPLAIN nu spui care index a fost ales.
    }
    // Începător: glosarul se oprește la cerere și la plan.
    // Producție: acolada închide SqlDeclarativeLanguage_Terms.
    // Expert: DDL, DML, DCL și TCL rămân de clasificat în folderul de propoziții. Nu le goli aici.
}
