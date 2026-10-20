// Începător: pachetul ține definițiile lângă convenția de scriere. Nu creează tabela sales.
// Producție: clasa stă în fast. Fără linia aceasta, glosarul nu mai e lângă lecție.
// Expert: pachetul nu este un identificator și nu fixează lower_case_table_names.
package _9_Coding_Technique.fast;

/*
🟢 Începător
    Ce este            glosarul: convenția, identificatorul, comentariul, tabela, coloana, indexul și hint-ul
    De ce e nevoie     fără termen, notița din curs se confundă cu un plan
🟡 Producție
    Cum se scrie       fiecare metodă întoarce propoziția termenului
🔴 Staff
    Unde               regulile vin din manualul 9.7 deschis în tur, nu din culoarea din Workbench
Pași                   1. Definiția stă deasupra metodei
                       2. Metoda ține propoziția
*/
// Începător: clasa numește piesele convenției. Nu inserează rânduri în sales.
// Producție: CodingTechnique_Terms nu are main. Dacă dispare, exemplele din definiții nu mai au metodă.
// Expert: glosarul nu este banca de interviu și nu este un index. Nu garantează un plan.
public class CodingTechnique_Terms {

    /*
    Definiția unei convenții de scriere (coding technique):
           Convenția alege forma lizibilă a aceleiași propoziții. Serverul păstrează identificatorul purchase_number și lasă tabela sales fără rânduri.

    Exemplu:
           CodingTechnique_Terms.explainCodingTechnique()
           CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)) nu inserează un rând.
    */

    /*
    🟢 Începător
        Ce este            alegerea formei pe care omul o citește, fără să adauge rânduri
        De ce e nevoie     fără un nume dintr-un token, CREATE nu lasă sales
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
    🔴 Staff
        Unde               convenția nu trece prin optimizer; hint-ul slash-stea-plus da
    Pași                   1. Întoarce CREATE lizibil
    */
    // Începător: metoda ține comanda care creează tabela goală.
    // Producție: explainCodingTechnique nu deschide conexiunea. Dacă dispare, definiția nu mai are exemplu.
    // Expert: metoda nu este CREATE INDEX. Nu garantează că litera tabelei rămâne pe disc.
    public static String explainCodingTechnique() {
        // Începător: sales rămâne cu o coloană și fără rânduri.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)). Dacă dispare, termenul nu mai are propoziție.
        // Expert: underscore nu este indexul. Cheia este constrângerea; scrierea nu schimbă planul și nu ține lacăt.
        return """
                CREATE TABLE sales (
                    purchase_number INT,
                    PRIMARY KEY (purchase_number)
                )
                """;
    }

    /*
    Definiția unui identificator (identifier):
           Identificatorul unquoted este un singur token din litere, cifre, underscore și dolar. Spațiul sau cuvântul rezervat cer backtick.

    Exemplu:
           CodingTechnique_Terms.explainIdentifier()
           purchase number fără backtick nu creează tabela sales.
    */

    /*
    🟢 Începător
        Ce este            numele obiectului, într-un singur token sau între backtick-uri
        De ce e nevoie     spațiul singur face serverul să respingă CREATE
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase number INT)
    🔴 Staff
        Unde               lista de caractere din manual; citarea nu este convenția scurtă
    Pași                   1. Întoarce tokenul rupt
    */
    // Începător: metoda ține forma care nu lasă tabela.
    // Producție: explainIdentifier nu trimite comanda. Dacă dispare, spațiul nu mai e arătat ca eșec.
    // Expert: spațiul nu este o coloană cu litere pliate. Nu se repară cu EXPLAIN.
    public static String explainIdentifier() {
        // Începător: serverul nu creează sales, pentru că numele e două cuvinte.
        // Producție: CREATE TABLE sales (purchase number INT). Dacă dispare, rămâne doar forma cu underscore.
        // Expert: tokenul unquoted nu conține spațiul. Backtick-ul ar fi altă propoziție, nu același identificator.
        return """
                CREATE TABLE sales (purchase number INT)
                """;
    }

    /*
    Definiția unui comentariu (comment):
           Comentariul #, -- urmat de spațiu sau de un caracter de control, și comentariul dintre slash-stea și stea-slash este tăiat. Nu schimbă rândurile. -- fără spațiu nu este comentariu.

    Exemplu:
           CodingTechnique_Terms.explainComment()
           Notița de după -- nu devine coloană în rezultat.
    */

    /*
    🟢 Începător
        Ce este            text pe care serverul îl taie înainte să dea rezultatul
        De ce e nevoie     fără tăiere, notița ar minți lista de coloane
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales -- comentariul nu este un rând
    🔴 Staff
        Unde               parserul, nu lacătul; hint-ul cu plus nu este acest comentariu
    Pași                   1. Întoarce SELECT-ul cu notiță
    */
    // Începător: metoda ține o citire. Notița nu adaugă un rând.
    // Producție: explainComment nu se conectează. Dacă dispare, comentariul pare date.
    // Expert: -- cu spațiu nu este WHERE și nu este slash-stea-plus. Nu garantează că -- lipit de text e tot notiță.
    public static String explainComment() {
        // Începător: rămâne coloana purchase_number. Textul de după liniuțe nu e valoare.
        // Producție: SELECT purchase_number FROM sales -- comentariul nu este un rând. Fără spațiul de după --, restul nu mai e notiță.
        // Expert: notița nu ține lacăt și nu schimbă planul. Se desparte de un hint, care nu are aici plusul.
        return """
                SELECT purchase_number
                FROM sales
                -- comentariul nu este un rând
                """;
    }

    /*
    Definiția unei tabele (table):
           Tabela este obiectul numit în CREATE. Litera numelui depinde de lower_case_table_names, fixat la inițializarea serverului. Pe Windows valoarea implicită 1 salvează numele cu litere mici. În aceeași propoziție nu amesteci două litere ale aceluiași nume.

    Exemplu:
           CodingTechnique_Terms.explainTable()
           my_table și MY_TABLE în același SELECT nu sunt o convenție de citire.
    */

    /*
    🟢 Începător
        Ce este            obiectul care ține coloanele, nu rândurile scrise de un comentariu
        De ce e nevoie     două litere în aceeași propoziție fac referința inutilizabilă
    🟡 Producție
        Cum se scrie       SELECT * FROM my_table WHERE MY_TABLE.col = 1
    🔴 Staff
        Unde               litera tabelei ține de fișier și de lower_case_table_names, nu de coloană
    Pași                   1. Întoarce propoziția care amestecă literele
    */
    // Începător: metoda ține un SELECT care nu trebuie citit cu două litere.
    // Producție: explainTable nu creează my_table. Dacă dispare, regula de literă rămâne doar la coloană.
    // Expert: pe coloană literele nu contează; pe tabelă, în Unix cu valoarea 0, contează. Nu garantează un plan.
    public static String explainTable() {
        // Începător: propoziția se referă la același obiect cu două litere. Nu inserează rânduri.
        // Producție: SELECT * FROM my_table WHERE MY_TABLE.col = 1. Dacă dispare, nu se vede amestecul pe care manualul îl respinge.
        // Expert: nu este plierea unei coloane. lower_case_table_names nu se schimbă din această propoziție, pentru că se fixează la inițializare.
        return """
                SELECT * FROM my_table WHERE MY_TABLE.col = 1
                """;
    }

    /*
    Definiția unei coloane (column):
           Numele coloanei, al indexului, al aliasului de coloană, al rutinei, al partiției și al grupului de resurse nu distinge literele pe nicio platformă.

    Exemplu:
           CodingTechnique_Terms.explainColumn()
           PurchaseNumber și purchasenumber sunt aceeași coloană. purchase_number este alt identificator, pentru că underscore-ul nu este o literă.
       HISTORICAL: PurchaseNumber și purchase_number sunt aceeași coloană. Manualul 9.7: coloana nu distinge literele; underscore-ul rămâne în token.
    */

    /*
    🟢 Începător
        Ce este            numele valorii din rând, scris o dată, citit cu orice litere
        De ce e nevoie     cine crede că litera mare face o a doua coloană dublează datele degeaba
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (PurchaseNumber INT)
    🔴 Staff
        Unde               insensibilitatea coloanei nu se aplică la fel numelui de tabelă pe Unix
    Pași                   1. Întoarce CREATE cu literă mare în coloană
    */
    // Începător: metoda ține crearea unei singure coloane. Nu două coloane.
    // Producție: explainColumn nu trimite comanda. Dacă dispare, PurchaseNumber pare alt obiect.
    // Expert: coloana nu ține de lower_case_table_names. Aliasul de tabelă pe Unix este alt caz.
    public static String explainColumn() {
        // Începător: sales rămâne cu o coloană, chiar dacă numele e scris cu literă mare. Rânduri nu apar.
        // Producție: CREATE TABLE sales (PurchaseNumber INT). Citirea purchasenumber vede aceeași coloană. Dacă dispare, nu rămâne propoziția.
        // Expert: PurchaseNumber și purchasenumber sunt același identificator de coloană. Nu sunt același lucru cu purchase_number, care are underscore. Linia nu creează un index.
        // HISTORICAL: Aceeași coloană se poate citi purchase_number doar dacă numele e același cuvânt; aici cuvântul este PurchaseNumber, iar literele nu îl schimbă. Manualul 9.7: underscore-ul nu este literă, deci purchase_number este alt identificator.
        return """
                CREATE TABLE sales (PurchaseNumber INT)
                """;
    }

    /*
    Definiția unui index (index):
           Indexul este un obiect numit. Convenția de underscore nu îl creează. PRIMARY KEY (purchase_number) pune cheia pe coloana care există. PRIMARY KEY (purcase_number) nu găsește coloana.

    Exemplu:
           CodingTechnique_Terms.explainIndex()
           Cheia scrisă greșit nu lasă tabela sales.
    */

    /*
    🟢 Începător
        Ce este            cheia care cere coloana reală, nu un nume apropiat
        De ce e nevoie     fără coloana din cheie, CREATE nu lasă sales
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number))
    🔴 Staff
        Unde               numele indexului este identificator; litera în plus nu e pliere
    Pași                   1. Întoarce cheia care nu găsește coloana
    */
    // Începător: metoda ține eșecul din slide. Tabela nu rămâne.
    // Producție: explainIndex nu se conectează. Dacă dispare, typo-ul pare o convenție.
    // Expert: purcase_number nu este un plan și nu este un lacăt. Se repară scriind purchase_number în cheie, nu cu un comentariu.
    public static String explainIndex() {
        // Începător: serverul nu găsește coloana din cheie, deci sales nu apare.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number)). Dacă dispare, nu se vede diferența față de cheia corectă.
        // Expert: eșecul nu este lipsa unui hint. Un index apare când cheia numește coloana scrisă, nu când numele e scurt.
        return """
                CREATE TABLE sales (
                    purchase_number INT,
                    PRIMARY KEY (purcase_number)
                )
                """;
    }

    /*
    Definiția unui hint de optimizer (optimizer hint):
           Comentariul care începe cu slash-stea-plus este hint. Optimizerul îl primește. Notița fără plus este tăiată. Hint-ul nu garantează un plan dacă nu se aplică tabelelor din propoziție.

    Exemplu:
           CodingTechnique_Terms.explainOptimizerHint()
           BKA(t1) nu este o notiță și nu este un index pe sales.
    */

    /*
    🟢 Începător
        Ce este            o indicație pentru plan, scrisă acolo unde omul s-ar aștepta la o notiță
        De ce e nevoie     cine o crede inertă poate schimba planul fără să vadă
    🟡 Producție
        Cum se scrie       SELECT cu BKA(t1) în hint, din purchase_number
    🔴 Staff
        Unde               optimizerul, nu parserul care aruncă notițele; nu ține loc de index pe sales
    Pași                   1. Întoarce hint-ul
    */
    // Începător: metoda ține citirea cu hint. Nu creează un index.
    // Producție: explainOptimizerHint nu rulează EXPLAIN. Dacă dispare, plusul pare tot o notiță.
    // Expert: BKA(t1) nu se aplică neapărat la sales. Nu garantează alt plan și nu ține lacăt după rezultat.
    public static String explainOptimizerHint() {
        // Începător: se citește purchase_number. Plusul nu este text aruncat.
        // Producție: SELECT /*+ BKA(t1) */ purchase_number FROM sales. Fără plus, textul ar fi fost tăiat.
        // Expert: hint-ul se confundă cu convenția de comentariu. Nu este CREATE INDEX și nu garantează că planul folosește BKA.
        return """
                SELECT /*+ BKA(t1) */ purchase_number FROM sales
                """;
    }
}
