/*
Definiția unei propoziții SQL (SQL statement):
       Propoziția este textul pe care îl execută serverul. SELECT 1 AS data_value cere un rând de rezultat și nu scrie o celulă.

Exemplu:
       Sql_Terms.explainSqlStatement()
       Tabelele rămân neschimbate după ce rezultatul a plecat.

Definiția unei baze de date (database):
       Baza de date este colecția structurată de date. Serverul o adaugă, o citește și o prelucrează. Propoziția nu creează colecția.

Exemplu:
       Sql_Terms.explainDatabase()
       SELECT 1 AS data_value nu adaugă o bază.

Definiția unei tabele (table):
       Tabela ține rândurile. O propoziție care nu o numește nu citește și nu scrie în ea.

Exemplu:
       Sql_Terms.explainTable()
       SELECT data_value FROM entity_row numește tabela. SELECT 1 AS data_value nu o numește.

Definiția unei coloane (column):
       Coloana este atributul vertical stocat în tabelă. Aliasul din lista de rezultat nu o creează în schemă.

Exemplu:
       Sql_Terms.explainColumn()
       data_value din SELECT 1 AS data_value este numele din rezultat, nu o coloană stocată.

Definiția unui rând (row):
       Rândul stocat este înregistrarea din tabelă. Rândul de rezultat al lui SELECT 1 trăiește până pleacă la client.

Exemplu:
       Sql_Terms.explainRow()
       Un 1 primit de client nu este un record din Introduction_To_DB.

Terms & Definitions:
*/
// Începător: glosarul stă în același folder fast și spune ce este propoziția față de celulă.
// Producție: package _1_SQL.fast. Metodele țin șirurile gata de trimis, fără conexiune.
// Expert: Nu redenumește Introduction_To_DB și nu copiază acolo definițiile. Termenii de mai jos rămân.
package _1_SQL.fast;

// Începător: clasa execută definițiile: propoziție, bază, tabelă, coloană, rând.
// Producție: public final class Sql_Terms. Fără ea, Exemplu: de deasupra nu are metodă.
// Expert: Nu predă indexul, lacătul sau tranzacția. Pagina 1.2.1 deschisă nu le folosește ca mecanism al acestei propoziții.
public final class Sql_Terms {

    /*
    🟢 Începător
        Ce este            propoziția care cere o valoare și nu scrie o celulă
        De ce e nevoie     fără șir, definiția rămâne o frază pe care serverul nu o primește
    🟡 Producție
        Cum se scrie       SELECT 1 AS data_value
    🔴 Staff
        Unde               rezultatul este la sfârșitul propoziției; vecinul este celula din Introduction_To_DB
    Pași
        1. Înapoiază șirul.
        2. Nu deschide o conexiune.
    */
    // Începător: metoda dă propoziția din definiție, nu o celulă scrisă.
    // Producție: public static String explainSqlStatement(). Dacă dispare, Exemplu: nu mai are metodă.
    // Expert: Nu este explain din EXPLAIN. Nu citește un plan: în tur serverul a refuzat root@localhost.
    public static String explainSqlStatement() {
        // Începător: rămâne un text pe care serverul îl poate rula, iar tabelele nu se umplu din această linie.
        // Producție: SELECT 1 AS data_value. Fără șir, definiția propoziției nu are exemplu executabil.
        // Expert: Aliasul nu este coloana stocată. Un SELECT care numește entity_row este explainTable, nu această linie.
        return "SELECT 1 AS data_value";
        // Începător: metoda a dat propoziția înapoi și nu a lăsat un rând stocat.
        // Producție: acolada închide explainSqlStatement. Șirul nu a plecat către 3306.
        // Expert: NERULAT pe server nu schimbă definiția. Acolada nu este COMMIT.
    }

    /*
    🟢 Începător
        Ce este            colecția de date, despărțită de propoziția care o întreabă
        De ce e nevoie     fără deosebire, propoziția pare că creează baza
    🟡 Producție
        Cum se scrie       SELECT 1 AS data_value
    🔴 Staff
        Unde               manualul spune că serverul prelucrează colecția; această propoziție nu numește una
    Pași
        1. Înapoiază aceeași propoziție.
        2. Nu scrie CREATE DATABASE.
    */
    // Începător: metoda arată că poți cere o valoare fără să adaugi o colecție.
    // Producție: public static String explainDatabase(). Dacă dispare, termenul database nu are exemplu.
    // Expert: CREATE DATABASE este o propoziție din _2_SQL_Statements. Aici nu se deschide schema.
    public static String explainDatabase() {
        // Începător: valoarea cerută nu creează o bază și nu mută datele din colecție.
        // Producție: SELECT 1 AS data_value. Dacă înlocuiești linia cu CREATE DATABASE, ai ieșit din termenul de aici.
        // Expert: Baza este colecția din manualul 1.2.1. Propoziția nu este fișierul de pe disc și nu este un API care ascunde SQL-ul.
        return "SELECT 1 AS data_value";
        // Începător: nicio colecție nouă nu a fost scrisă de metodă.
        // Producție: acolada închide explainDatabase.
        // Expert: Un API care ascunde sintaxa, cum spune manualul, tot nu este această acoladă. Serverul nu a fost chemat.
    }

    /*
    🟢 Începător
        Ce este            numele tabelei într-o propoziție, față de propoziția care nu o numește
        De ce e nevoie     fără nume, nu știi dacă citești rânduri stocate sau doar un rezultat
    🟡 Producție
        Cum se scrie       SELECT data_value FROM entity_row
    🔴 Staff
        Unde               FROM numește tabela. Lipsa ei în SELECT 1 înseamnă că nu există rând de citit și nici index
    Pași
        1. Înapoiază propoziția care numește tabela.
        2. Nu o trimite: entitatea poate lipsi, iar în tur autentificarea a căzut.
    */
    // Începător: metoda ține propoziția care spune din ce tabelă vrei valoarea.
    // Producție: public static String explainTable(). Dacă dispare, termenul table nu are șir.
    // Expert: entity_row nu este creat aici. Introduction_To_DB descrie tabela, dar nu o trimite la server.
    public static String explainTable() {
        // Începător: propoziția se uită la o tabelă anume. Dacă tabela nu există, serverul nu are de unde citi o celulă.
        // Producție: SELECT data_value FROM entity_row. Fără FROM, nu mai este exemplul tabelei, ci SELECT 1 AS data_value.
        // Expert: Nu creează tabela și nu este un index. Trimisă fără entity_row, nu lasă un rând; în tur nici nu a fost trimisă.
        return "SELECT data_value FROM entity_row";
        // Începător: metoda s-a oprit la numele tabelei, fără să fi citit un rând.
        // Producție: acolada închide explainTable.
        // Expert: Eșecul unei tabele lipsă nu este bucla FOR EACH. Acolo serverul nu are deloc o propoziție.
    }

    /*
    🟢 Începător
        Ce este            numele din rezultat, care nu deschide o coloană în schemă
        De ce e nevoie     fără deosebire, aliasul pare că a alterat tabela
    🟡 Producție
        Cum se scrie       SELECT 1 AS data_value
    🔴 Staff
        Unde               AS numește coloana din rezultat. Coloana stocată există doar într-o tabelă, iar aici tabela lipsește
    Pași
        1. Înapoiază propoziția cu alias.
        2. Nu scrie ALTER TABLE.
    */
    // Începător: metoda arată numele pe care îl vede clientul, nu o coloană lăsată în urmă.
    // Producție: public static String explainColumn(). Dacă dispare, termenul column nu are exemplu.
    // Expert: ALTER care adaugă o coloană este alt folder. Aliasul de aici moare odată cu rezultatul.
    public static String explainColumn() {
        // Începător: clientul vede numele data_value pe rezultatul lui 1, iar schema nu primește o coloană.
        // Producție: SELECT 1 AS data_value. Dacă scoți AS data_value, rezultatul nu mai poartă numele din Introduction_To_DB.
        // Expert: Coloana stocată este atributul vertical al unui record. Acest alias nu este acel atribut și nu primește un index.
        return "SELECT 1 AS data_value";
        // Începător: schema rămâne fără coloana data_value, pentru că metoda nu a trimis nimic și propoziția nici nu o creează.
        // Producție: acolada închide explainColumn.
        // Expert: Nu confunda aliasul cu coloana din SELECT data_value FROM entity_row. Acolo numele cere un atribut stocat.
    }

    /*
    🟢 Începător
        Ce este            rândul de rezultat, care nu rămâne ca înregistrare în tabelă
        De ce e nevoie     fără deosebire, 1 primit de client pare un record nou
    🟡 Producție
        Cum se scrie       SELECT 1 AS data_value
    🔴 Staff
        Unde               rândul de rezultat se vede la sfârșitul propoziției și pleacă. Record-ul din Introduction_To_DB rămâne și fără propoziție
    Pași
        1. Înapoiază propoziția.
        2. Nu scrie INSERT.
    */
    // Începător: metoda ține propoziția al cărei rând nu se salvează în tabelă.
    // Producție: public static String explainRow(). Dacă dispare, termenul row nu are exemplu.
    // Expert: INSERT ar lăsa un record. Această metodă nu este INSERT și nu ține un lacăt pe un rând care nu există.
    public static String explainRow() {
        // Începător: dacă serverul termină propoziția, clientul vede un rând, iar tabela nu îl păstrează.
        // Producție: SELECT 1 AS data_value. Fără linie, nu ai cum să arăți un rând care nu este un record.
        // Expert: Record-ul este rândul stocat din Introduction_To_DB. COMMIT nu îl face durabil aici, pentru că nu a fost scris.
        return "SELECT 1 AS data_value";
        // Începător: metoda nu a lăsat o înregistrare.
        // Producție: acolada închide explainRow.
        // Expert: Nu este un rând ținut de un lacăt și nu este rezultatul unui JOIN. Nicio tabelă nu a fost numită.
    }
    // Începător: glosarul se oprește la rând, coloană, tabelă, bază și propoziție.
    // Producție: acolada închide Sql_Terms.
    // Expert: Indexul și lacătul nu sunt pe pagina folosită ca mecanism al lui SELECT 1. Nu le adăuga ca termeni goliți.
}
