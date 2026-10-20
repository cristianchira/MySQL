/*
Definiția unei valori de date (data value):
       Valoarea de date este conținutul celulei, la întâlnirea unui rând cu o coloană.

Exemplu:
       IntroductionToDb_Terms.explainDataValue()
       INSERT INTO entity_row (data_value) VALUES (1) pune conținutul. SELECT 1 AS data_value nu îl pune.

Definiția unui rând (row):
       Rândul stocat este înregistrarea din tabelă. Manualul îl numește row. Un rând de rezultat nu rămâne în tabelă.

Exemplu:
       IntroductionToDb_Terms.explainRecord()
       Record-ul cere un INSERT terminat. Constanta nu este un record.

Definiția unei coloane (column):
       Coloana este atributul vertical al tabelei. Fără un rând, nu ține încă o valoare de date.

Exemplu:
       IntroductionToDb_Terms.explainColumn()
       data_value din INSERT este coloana. Același nume după AS este alias de rezultat.

Definiția unei tabele (table):
       Tabela ține rândurile. O bază relațională le ține în tabele separate, nu într-un singur depozit.

Exemplu:
       IntroductionToDb_Terms.explainTable()
       Fără entity_row, INSERT nu are unde să lase celula.

Terms & Definitions:
*/
// Începător: glosarul spune ce rămâne stocat: valoare, rând, coloană, tabelă.
// Producție: package _1_SQL._1_Introduction_To_DB.fast. Metodele țin șirurile, fără conexiune.
// Expert: Nu muta termenii în _2_SQL_Declarative_Language. Acolo întrebarea este cine alege pașii.
package _1_SQL._1_Introduction_To_DB.fast;

// Începător: clasa execută definițiile celulei.
// Producție: public final class IntroductionToDb_Terms. Fără ea, Exemplu: nu are metodă.
// Expert: Nu predă indexul. Pagina 1.2.1 vorbește despre rânduri și coloane, nu despre plan.
public final class IntroductionToDb_Terms {

    /*
    🟢 Începător
        Ce este            conținutul celulei
        De ce e nevoie     fără șir, „data value” rămâne o frază
    🟡 Producție
        Cum se scrie       INSERT INTO entity_row (data_value) VALUES (1)
    🔴 Staff
        Unde               conținutul se vede în rând după propoziție. SELECT 1 nu scrie celula.
    Pași
        1. Înapoiază INSERT-ul.
        2. Nu deschide conexiunea.
    */
    // Începător: metoda dă propoziția care umple celula.
    // Producție: public static String explainDataValue(). Dacă dispare, termenul nu are exemplu.
    // Expert: Nu este explain din EXPLAIN. Planul nu a fost citit în tur.
    public static String explainDataValue() {
        // Începător: 1 ajunge în coloana data_value doar dacă serverul termină acest INSERT într-o tabelă care există.
        // Producție: INSERT INTO entity_row (data_value) VALUES (1). Fără șir, definiția nu are propoziție.
        // Expert: Nu este SELECT 1 AS data_value. Aliasul nu este conținutul unui rând stocat.
        return "INSERT INTO entity_row (data_value) VALUES (1)";
        // Începător: metoda a dat propoziția, nu celula deja scrisă.
        // Producție: acolada închide explainDataValue. Șirul nu a plecat.
        // Expert: NERULAT pe server nu schimbă definiția. Acolada nu este COMMIT.
    }

    /*
    🟢 Începător
        Ce este            înregistrarea, adică rândul din tabelă
        De ce e nevoie     fără deosebire, rândul de rezultat pare un record
    🟡 Producție
        Cum se scrie       INSERT INTO entity_row (data_value) VALUES (1)
    🔴 Staff
        Unde               record-ul rămâne după ce clientul a plecat. Rândul lui SELECT 1 nu rămâne.
    Pași
        1. Înapoiază INSERT-ul care adaugă înregistrarea.
        2. Nu îl numi SELECT.
    */
    // Începător: metoda arată propoziția care adaugă un record.
    // Producție: public static String explainRecord(). Dacă dispare, termenul row nu are șir.
    // Expert: Un view din manual nu este acest rând. View-ul este alt folder.
    public static String explainRecord() {
        // Începător: după INSERT, tabela are o înregistrare în plus, dacă propoziția s-a terminat.
        // Producție: INSERT INTO entity_row (data_value) VALUES (1). Un SELECT nu adaugă acest record.
        // Expert: Record-ul nu este grila din Workbench. Grila poate arăta un rezultat care nu este scris.
        return "INSERT INTO entity_row (data_value) VALUES (1)";
        // Începător: metoda nu a lăsat ea însăși înregistrarea.
        // Producție: acolada închide explainRecord.
        // Expert: ROLLBACK ar putea anula înregistrarea doar dacă INSERT-ul a intrat într-o tranzacție. Aici nu a fost trimis.
    }

    /*
    🟢 Începător
        Ce este            atributul vertical, nu aliasul din rezultat
        De ce e nevoie     fără deosebire, AS data_value pare că a creat coloana
    🟡 Producție
        Cum se scrie       INSERT INTO entity_row (data_value) VALUES (1)
    🔴 Staff
        Unde               coloana trebuie să existe în entity_row înainte de INSERT. Aliasul din SELECT 1 nu o creează.
    Pași
        1. Înapoiază INSERT-ul care numește coloana.
        2. Nu scrie ALTER.
    */
    // Începător: metoda numește coloana în care stă valoarea.
    // Producție: public static String explainColumn(). Dacă dispare, termenul column nu are exemplu.
    // Expert: CREATE TABLE care doar declară coloana, fără rând, nu este încă o valoare de date. Clasificarea CREATE stă în alt folder.
    public static String explainColumn() {
        // Începător: data_value primește 1 doar ca celulă, pe un rând, nu ca nume de rezultat.
        // Producție: INSERT INTO entity_row (data_value) VALUES (1). Dacă scoți coloana din listă, nu mai este exemplul atributului.
        // Expert: SELECT 1 AS data_value folosește același cuvânt și nu atinge atributul stocat.
        return "INSERT INTO entity_row (data_value) VALUES (1)";
        // Începător: schema nu s-a schimbat din metodă.
        // Producție: acolada închide explainColumn.
        // Expert: O coloană goală, fără rând, nu este încă data value. Acest șir cere și rândul.
    }

    /*
    🟢 Începător
        Ce este            locul în care stau rândurile
        De ce e nevoie     fără tabelă, valoarea nu are unde să rămână
    🟡 Producție
        Cum se scrie       INSERT INTO entity_row (data_value) VALUES (1)
    🔴 Staff
        Unde               FROM sau INTO numește tabela. SELECT 1 nu o numește, deci nu are tabelă de umplut.
    Pași
        1. Înapoiază propoziția care numește entity_row.
        2. Nu crea tabela din această metodă.
    */
    // Începător: metoda ține propoziția care spune în ce tabelă intră celula.
    // Producție: public static String explainTable(). Dacă dispare, termenul table nu are șir.
    // Expert: entity_row nu este creată aici. Manualul spune că datele stau în tabele separate, nu că INSERT le inventează.
    public static String explainTable() {
        // Începător: celula are nevoie de entity_row. Dacă tabela lipsește, serverul nu are unde să pună 1.
        // Producție: INSERT INTO entity_row (data_value) VALUES (1). Fără INTO entity_row, nu mai este exemplul tabelei.
        // Expert: Nu este CREATE TABLE. O tabelă lipsă face INSERT-ul să eșueze și lasă zero rânduri.
        return "INSERT INTO entity_row (data_value) VALUES (1)";
        // Începător: metoda s-a oprit la numele tabelei, fără să fi scris un rând.
        // Producție: acolada închide explainTable.
        // Expert: Eșecul unei tabele lipsă nu este bucla FOR EACH. Acolo nici nu există o propoziție de stocare.
    }
    // Începător: glosarul se oprește la valoare, rând, coloană și tabelă.
    // Producție: acolada închide IntroductionToDb_Terms.
    // Expert: Indexul și lacătul nu sunt mecanismul celulei din pagina 1.2.1. Nu le adăuga goale.
}
