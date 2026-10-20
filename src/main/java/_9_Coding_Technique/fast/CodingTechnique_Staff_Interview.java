// Începător: pachetul ține banca de interviu lângă convenția de scriere. Nu creează sales.
// Producție: clasa stă în fast. Fără linia aceasta, q01–q50 nu mai sunt lângă lecție.
// Expert: pachetul nu este un identificator MySQL și nu schimbă planul.
package _9_Coding_Technique.fast;

/*
TOPIC:
    Convenția de nume și de comentariu alege forma lizibilă a aceleiași propoziții: serverul păstrează identificatorul purchase_number și creează tabela sales fără rânduri; comentariul simplu nu inserează nimic.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    Echipa trebuie să citească același CREATE fără să schimbe rândurile. După propoziție, schema are tabela sales cu coloana purchase_number și zero rânduri.
MECHANISM:
    mysqld acceptă identificatorul unquoted din litere, cifre, underscore și dolar, apoi aruncă comentariul #, -- urmat de spațiu, sau comentariul dintre slash-stea și stea-slash. Rezultatul se vede la sfârșitul propoziției: tabela există sau propoziția e respinsă. Un hint slash-stea-plus nu este aruncat.
LOOKALIKES:
    Se confundă cu rescrierea din optimizare și cu un index. O rupere de rând sau un comentariu simplu lasă același plan; un index sau un hint slash-stea-plus poate schimba planul. purchase number cu spațiu nu este convenția: fără backtick tokenul e ilegal.
BOOK THEORY:
    Manualul 9.7: identificatorul unquoted permite ASCII 0-9, a-z, A-Z, underscore și dolar, plus caracterele din planul multilingv de bază; spațiul și cuvântul rezervat cer backtick. Numele de coloană nu distinge literele pe nicio platformă. Numele de tabelă depinde de lower_case_table_names, fixat la inițializarea serverului; pe Windows valoarea implicită 1 salvează numele cu litere mici. Comentariul #, -- urmat de spațiu sau de un caracter de control, și comentariul dintre slash-stea și stea-slash este tăiat și nu schimbă rândurile. -- fără spațiu nu este comentariu. Un comentariu slash-stea-exclamare este executat de MySQL; cu număr de versiune, doar dacă serverul este cel puțin acea versiune, iar pentru 9.7.4 numărul din comentariu este 90704. Comentariile cu număr de versiune nu rămân în corpul unei rutine stocate. Un comentariu slash-stea-plus este hint de optimizer, nu o notiță. Comentariile imbricate nu sunt suportate și sunt deprecate. PDF-ul cursului cere nume scurte, un cuvânt pe concept, purchase_number în loc de spațiu, și comentarii mari cu slash-stea sau o linie cu # ori --. Pe slide, PRIMARY KEY (purcase_number) nu numește coloana purchase_number, deci acea propoziție nu creează tabela. PDF-ul nu spune că un comentariu cu exclamare se execută și nu spune regula de literă a coloanei. Rearanjarea și culoarea din Workbench sunt clientul.
BOOK EXAMPLE:
    Coding_technique.readableSalesTable
REPO DEMO:
    src/main/java/_9_Coding_Technique/Coding_technique.java
BREAK LAB:
    CREATE TABLE sales (purchase number INT) este respins și nu lasă tabela. CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number)) nu găsește coloana.
TRADE-OFFS:
    Când numele trebuie să conțină spațiu sau un cuvânt rezervat, îl încadrezi în backtick. Când problema este planul, nu mai rescrii convenția: alegi un index sau un hint slash-stea-plus.
ANTI-PATTERNS:
    purchase number unquoted, -- fără spațiu, un hint slash-stea-plus crezut comentariu inert, sau PRIMARY KEY (purcase_number) pe coloana purchase_number.
STAFF QUESTIONS:
    CodingTechnique_Staff_Interview.q01 până la q50
NOVEL TRANSFER:
    Un registru de loturi de depozit, nu tabela sales: lot_code rămâne un identificator, iar lot code cu spațiu este respins la fel.
SPACED REVIEW:
    o zi, șapte zile, treizeci de zile

Definiția unei convenții de scriere (coding technique):
       Convenția alege forma lizibilă. Serverul păstrează identificatorul și lasă tabela sales fără rânduri.

Exemplu:
       CodingTechnique_Terms.explainCodingTechnique()
       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)) nu inserează un rând.

Definiția unui identificator (identifier):
       Identificatorul unquoted este un singur token. Spațiul cere backtick.

Exemplu:
       CodingTechnique_Terms.explainIdentifier()
       purchase number fără backtick nu creează tabela.

Definiția unui comentariu (comment):
       Comentariul #, -- cu spațiu sau slash-stea este tăiat și nu schimbă rândurile.

Exemplu:
       CodingTechnique_Terms.explainComment()
       SELECT purchase_number FROM sales cu o notiță nu adaugă o coloană.

Definiția unei tabele (table):
       Numele tabelei depinde de lower_case_table_names. Convenția nu alege planul.

Exemplu:
       CodingTechnique_Terms.explainTable()
       sales este numele obiectului creat, nu un rând.

Definiția unei coloane (column):
       Numele coloanei nu distinge literele pe nicio platformă.

Exemplu:
       CodingTechnique_Terms.explainColumn()
       PurchaseNumber și purchasenumber sunt aceeași coloană. purchase_number este alt identificator, pentru că underscore-ul nu este o literă.
       HISTORICAL: PurchaseNumber și purchase_number sunt aceeași coloană. Manualul 9.7: coloana nu distinge literele; underscore-ul rămâne în token.

Definiția unui index (index):
       Indexul apare din constrângere sau din CREATE INDEX. Underscore-ul din nume nu este un index.

Exemplu:
       CodingTechnique_Terms.explainIndex()
       PRIMARY KEY (purchase_number) este cheia, nu scrierea cu underscore.

Definiția unui hint de optimizer (optimizer hint):
       Hint-ul slash-stea-plus ajunge la optimizer. Notița simplă nu ajunge.

Exemplu:
       CodingTechnique_Terms.explainOptimizerHint()
       BKA(t1) nu este o notiță aruncată și nu garantează planul pe sales.
*/

/*
🟢 Începător
    Ce este            cincizeci de propoziții despre convenție, comentariu și vecinul lor, planul
    De ce e nevoie     fără ele, un nume scurt pare că șterge rânduri
🟡 Producție
    Cum se scrie       fiecare qNN întoarce propoziția gata de trimis
🔴 Staff
    Unde               zece recunoașteri, zece efecte, zece vecini, zece eșecuri, zece alegeri
Pași                   1. Nu există main de recall
*/
// Începător: clasa nu pornește singură. Propozițiile stau în metode.
// Producție: nu există main. Dacă apare, recall-ul ar arăta cheia înainte de încercare.
// Expert: banca nu este FAQ-ul gol și nu este un index. Nu garantează că studentul a răspuns.
public class CodingTechnique_Staff_Interview {
    /*
    🟢 Începător
        Ce este            un nume dintr-un singur token
        De ce e nevoie     fără token, sales nu are coloana pe care o citește echipa
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               parserul de identificatori, nu optimizerul
    Pași                   1. Întoarce propoziția
    Enunț: Din CREATE TABLE sales (purchase_number INT), ce identificator vede serverul?
    Capcană: Underscore-ul face două coloane, purchase și number.
    Cheie: purchase_number este un singur token. Tabela, dacă propoziția trece, rămâne fără rânduri. Nu este un index.
    */
    // Începător: metoda ține recunoașterea unui singur nume. Nu pune rânduri în sales.
    // Producție: q01 nu deschide conexiunea. Dacă dispare, spațiul și underscore-ul par la fel.
    // Expert: tokenul nu este un index și nu este un hint. Nu garantează litera cu care sales stă pe disc.
    public static String q01() {
        // Începător: coloana purchase_number rămâne una. Niciun rând nu e inserat.
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă dispare, nu mai ai semnul din SQL.
        // Expert: underscore-ul nu desparte tokenul și nu creează index. Se desparte de purchase number, care are spațiu.
        return """
                CREATE TABLE sales (purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q01 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un nume rupt de spațiu
        De ce e nevoie     fără respingere, cineva crede că tabela există
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase number INT)
    🔴 Staff
        Unde               lista de caractere a identificatorului unquoted
    Pași                   1. Întoarce propoziția
    Enunț: Ce semn vezi în CREATE TABLE sales (purchase number INT)?
    Capcană: Spațiul este aceeași convenție, doar mai lizibilă.
    Cheie: Serverul nu vede un identificator. Propoziția e respinsă și sales nu apare. Backtick-ul ar fi altă propoziție.
    */
    // Începător: metoda ține forma respinsă. Nu lasă o tabelă în urmă.
    // Producție: q02 nu trimite comanda. Dacă dispare, eșecul cel mai mic nu mai e în bancă.
    // Expert: spațiul nu se repară cu EXPLAIN și nu este lower_case_table_names.
    public static String q02() {
        // Începător: sales nu este creată. Nu rămâne nicio coloană.
        // Producție: CREATE TABLE sales (purchase number INT). Dacă dispare, rămâne doar forma care reușește.
        // Expert: tokenul unquoted nu conține spațiul. Nu este un comentariu și nu ține lacăt.
        return """
                CREATE TABLE sales (purchase number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q02 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un nume citat, cu spațiu înăuntru
        De ce e nevoie     fără citare, spațiul respinge CREATE
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (`purchase number` INT)
    🔴 Staff
        Unde               citarea, nu convenția scurtă din PDF
    Pași                   1. Întoarce propoziția
    Enunț: Cum recunoști CREATE TABLE sales (`purchase number` INT)?
    Capcană: Backtick-ul e doar stil, la fel ca underscore-ul.
    Cheie: Backtick-ul face un identificator cu spațiu. Este altă propoziție decât purchase_number și lasă tabela fără rânduri.
    */
    // Începător: metoda ține forma citată. Tot nu inserează rânduri.
    // Producție: q03 nu se conectează. Dacă dispare, spațiul pare mereu ilegal.
    // Expert: numele citat nu este purchase_number și nu este un index secundar.
    public static String q03() {
        // Începător: sales apare cu o coloană al cărei nume are spațiu. Rândurile rămân zero.
        // Producție: CREATE TABLE sales (`purchase number` INT). Fără backtick, aceeași propoziție cade.
        // Expert: citarea nu pliază litera tabelei. Nu garantează portabilitatea numelui între Windows și Unix.
        return """
                CREATE TABLE sales (`purchase number` INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q03 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o notiță de linie pe care serverul o taie
        De ce e nevoie     fără spațiul de după --, restul liniei nu mai e notiță
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales -- nota nu este un rând
    🔴 Staff
        Unde               parserul de comentarii, nu un lacăt de rând
    Pași                   1. Întoarce propoziția
    Enunț: Ce este textul de după -- în SELECT purchase_number FROM sales -- nota nu este un rând?
    Capcană: Nota se întoarce ca o a doua coloană.
    Cheie: -- urmat de spațiu este tăiat. Rezultatul rămâne coloana purchase_number. Nu se inserează nimic.
    */
    // Începător: metoda ține citirea cu notiță. Notița nu e date.
    // Producție: q04 nu execută SELECT-ul. Dacă dispare, notița pare un rând.
    // Expert: -- cu spațiu nu este un hint cu plus și nu este WHERE.
    public static String q04() {
        // Începător: rămân valorile din purchase_number. Textul de după liniuțe nu e coloană.
        // Producție: SELECT purchase_number FROM sales -- nota nu este un rând. Fără spațiu după --, tăierea nu pornește.
        // Expert: notița nu schimbă planul și nu ține lacăt după ce rezultatul a plecat.
        return """
                SELECT purchase_number FROM sales -- nota nu este un rând
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q04 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            două minusuri lipite, nu o notiță
        De ce e nevoie     dacă le crezi comentariu, tai o expresie care încă se execută
    🟡 Producție
        Cum se scrie       SELECT 1--1
    🔴 Staff
        Unde               regula spațiului de după --, din manualul 9.7
    Pași                   1. Întoarce propoziția
    Enunț: Cum recunoști SELECT 1--1?
    Capcană: -- începe mereu un comentariu, deci 1 dispare.
    Cheie: Fără spațiu sau caracter de control după --, secvența nu este comentariu. Serverul încă parsează restul ca expresie.
    */
    // Începător: metoda ține forma care nu taie linia.
    // Producție: q05 nu întreabă serverul. Dacă dispare, -- cu spațiu și -- lipit par identice.
    // Expert: expresia nu este un comentariu de convenție și nu este un hint.
    public static String q05() {
        // Începător: serverul nu aruncă restul liniei. Propoziția rămâne de parsat.
        // Producție: SELECT 1--1. Dacă dispare, nu mai ai semnul lipsei de spațiu.
        // Expert: nu garantează un rezultat numeric, pentru că serverul nu a fost întrebat. Nu este COMMIT.
        return """
                SELECT 1--1
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q05 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o notiță de la diez până la capătul liniei
        De ce e nevoie     fără diez, textul de după ar fi tokeni de parsat
    🟡 Producție
        Cum se scrie       SELECT 1+1 # nota se oprește la capătul liniei
    🔴 Staff
        Unde               parserul mysqld, nu culoarea din Workbench
    Pași                   1. Întoarce propoziția
    Enunț: Ce comentariu este # în SELECT 1+1 # nota se oprește la capătul liniei?
    Capcană: # este o comandă de client, nu o notiță de server.
    Cheie: # până la capătul liniei este comentariu de server. Nu schimbă suma și nu scrie un rând.
    */
    // Începător: metoda ține diez-ul. Suma rămâne o expresie.
    // Producție: q06 nu trimite propoziția. Dacă dispare, diez-ul pare doar un obicei de editor.
    // Expert: diez-ul nu este -- și nu cere spațiu după el. Nu este un index.
    public static String q06() {
        // Începător: expresia 1+1 rămâne. Nota nu devine al treilea termen.
        // Producție: SELECT 1+1 # nota se oprește la capătul liniei. Dacă dispare, nu vezi unde se oprește notița.
        // Expert: diez-ul nu ține lacăt și nu schimbă planul. Se desparte de un hint, care folosește slash-stea-plus.
        return """
                SELECT 1+1 # nota se oprește la capătul liniei
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q06 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o notiță în mijlocul expresiei
        De ce e nevoie     fără închidere, restul propoziției ar intra în notiță
    🟡 Producție
        Cum se scrie       SELECT 1 slash-stea nota stea-slash + 1
    🔴 Staff
        Unde               comentariul care poate sta pe mai multe linii, tăiat înainte de execuție
    Pași                   1. Întoarce propoziția
    Enunț: Unde se termină notița din SELECT 1 slash-stea nota stea-slash + 1?
    Capcană: Notița înghite și + 1, deci rezultatul rămâne 1.
    Cheie: Comentariul dintre slash-stea și stea-slash se termină la stea-slash. + 1 rămâne în expresie. Nu se scrie un rând.
    */
    // Începător: metoda ține notița din mijloc. Expresia rămâne o adunare.
    // Producție: q07 nu rulează SELECT-ul. Dacă dispare, pare că notița șterge + 1.
    // Expert: notița închisă nu este un comentariu imbricat și nu este un hint.
    public static String q07() {
        // Începător: 1 și + 1 rămân. Nota nu este un operand.
        // Producție: SELECT 1 /* nota */ + 1. Dacă dispare, nu mai vezi că expresia continuă după notiță.
        // Expert: închiderea notiței nu ia lacăt. Un plus imediat după slash-stea ar fi fost hint, nu această formă.
        return """
                SELECT 1 /* nota */ + 1
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q07 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un comentariu executabil, nu o notiță
        De ce e nevoie     dacă îl crezi inert, forțezi un join fără să vezi
    🟡 Producție
        Cum se scrie       SELECT slash-stea-exclamare STRAIGHT_JOIN stea-slash purchase_number FROM sales
    🔴 Staff
        Unde               parserul execută conținutul; alte servere ar trebui să îl ignore
    Pași                   1. Întoarce propoziția
    Enunț: Ce este SELECT slash-stea-exclamare STRAIGHT_JOIN stea-slash purchase_number FROM sales?
    Capcană: Tot ce e între slash-stea e inert, deci STRAIGHT_JOIN nu ajunge la server.
    Cheie: Slash-stea-exclamare este cod executat de MySQL, nu o notiță. STRAIGHT_JOIN poate schimba felul în care se leagă tabelele. Nu este convenția de nume.
    */
    // Începător: metoda ține forma executabilă. Nu e o notiță de echipă.
    // Producție: q08 nu deschide sesiunea. Dacă dispare, exclamarea pare decor.
    // Expert: STRAIGHT_JOIN nu este purchase_number și nu garantează un plan pe o tabelă fără pereche.
    public static String q08() {
        // Începător: serverul primește STRAIGHT_JOIN. Coloana citită rămâne purchase_number.
        // Producție: SELECT /*! STRAIGHT_JOIN */ purchase_number FROM sales. Fără exclamare, textul ar fi fost tăiat.
        // Expert: nu este un index pe sales. Nu garantează că există un join de forțat în această propoziție.
        return """
                SELECT /*! STRAIGHT_JOIN */ purchase_number FROM sales
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q08 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un hint, nu o notiță de convenție
        De ce e nevoie     dacă îl arunci din reflex, pierzi singura formă care poate atinge planul
    🟡 Producție
        Cum se scrie       SELECT slash-stea-plus BKA(t1) stea-slash purchase_number FROM sales
    🔴 Staff
        Unde               optimizerul, nu parserul care taie notițele
    Pași                   1. Întoarce propoziția
    Enunț: Cum recunoști SELECT slash-stea-plus BKA(t1) stea-slash purchase_number FROM sales?
    Capcană: Plusul e o notiță mai puternică, tot aruncată.
    Cheie: Slash-stea-plus este hint de optimizer. Nu este convenția de rânduri goale. Nu garantează că BKA se aplică la sales.
    */
    // Începător: metoda ține hint-ul. Citirea rămâne pe purchase_number.
    // Producție: q09 nu rulează EXPLAIN. Dacă dispare, plusul se confundă cu o notiță.
    // Expert: BKA(t1) nu creează un index pe sales și nu ține lacăt după rezultat.
    public static String q09() {
        // Începător: coloana se citește. Hint-ul nu este un rând în plus.
        // Producție: SELECT /*+ BKA(t1) */ purchase_number FROM sales. Fără plus, BKA ar fi fost tăiat.
        // Expert: hint-ul nu este CREATE INDEX. Nu garantează alt plan când t1 nu este sales.
        return """
                SELECT /*+ BKA(t1) */ purchase_number FROM sales
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q09 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            planul, nu rescrierea numelui
        De ce e nevoie     fără EXPLAIN, o rupere de rând pare o optimizare
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT purchase_number FROM sales
    🔴 Staff
        Unde               EXPLAIN nu execută schimbarea de rânduri; convenția nici ea
    Pași                   1. Întoarce propoziția
    Enunț: Ce îți spune EXPLAIN SELECT purchase_number FROM sales despre convenție?
    Capcană: EXPLAIN rescrie numele și schimbă rândurile ca să arate planul.
    Cheie: EXPLAIN arată planul și nu schimbă rândurile. Convenția de nume nu apare ca un operator. Planul se schimbă din index sau din hint, nu din linia nouă.
    */
    // Începător: metoda ține EXPLAIN-ul. sales nu pierde rânduri din cauza lui.
    // Producție: q10 nu a fost rulat în tur. Dacă dispare, planul se confundă cu numele coloanei.
    // Expert: EXPLAIN nu este un comentariu și nu creează indexul. Nu garantează un anumit tip de acces, pentru că nu l-am văzut.
    public static String q10() {
        // Începător: rândurile din sales rămân. Vezi planul, nu o convenție aplicată.
        // Producție: EXPLAIN SELECT purchase_number FROM sales. Dacă dispare, rămâne doar SELECT-ul care citește.
        // Expert: planul nu se decide din underscore. Un hint slash-stea-plus este alt mecanism, care poate intra în plan.
        return """
                EXPLAIN SELECT purchase_number FROM sales
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q10 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o tabelă goală cu cheie pe coloană
        De ce e nevoie     fără cheie pe coloana reală, duplicatul nu e refuzat mai târziu
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
    🔴 Staff
        Unde               sfârșitul propoziției, pe schemă, nu un lacăt care rămâne
    Pași                   1. Întoarce propoziția
    Enunț: Câte rânduri lasă CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))?
    Capcană: Cheia primară inserează un rând cu numărul 1.
    Cheie: La sfârșitul propoziției schema are sales, coloana și cheia. Rândurile sunt zero. Efectul se vede în propoziția următoare, care poate numi tabela.
    */
    // Începător: metoda ține CREATE-ul complet. Nu face INSERT.
    // Producție: q11 nu se conectează. Dacă dispare, cheia pare un efect al underscore-ului.
    // Expert: PRIMARY KEY este constrângerea. Nu este comentariul -- unic și nu ține lacăt după ce CREATE s-a terminat.
    public static String q11() {
        // Începător: sales rămâne fără rânduri. Cheia stă pe purchase_number.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)). Dacă dispare, nu vezi că rândurile sunt zero.
        // Expert: cheia nu vine din scrierea lizibilă. Nu garantează un plan de citire.
        return """
                CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q11 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o citire cu notiță, fără scriere
        De ce e nevoie     dacă notița ar fi date, raportul ar minți
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales slash-stea nota nu este rând stea-slash
    🔴 Staff
        Unde               tăierea se vede în rezultatul propoziției, nu la un COMMIT ulterior
    Pași                   1. Întoarce propoziția
    Enunț: Un comentariu pus pe SELECT schimbă rândurile din sales?
    Capcană: Da, notița se salvează în tabelă ca observație.
    Cheie: Comentariul e tăiat. SELECT nu inserează. Rândurile rămân cum erau la începutul propoziției.
    */
    // Începător: metoda ține SELECT-ul. Notița nu e un INSERT.
    // Producție: q12 nu scrie în sales. Dacă dispare, comentariul pare DML.
    // Expert: notița nu ia lacăt de rând, pentru că nu e o propoziție care atinge rânduri ca să le schimbe.
    public static String q12() {
        // Începător: rezultatul are coloana purchase_number. Nota nu e un rând nou.
        // Producție: SELECT purchase_number FROM sales /* nota nu este rând */. Dacă dispare, nu vezi că notița stă în afara listei.
        // Expert: spre deosebire de un hint cu plus, aici textul e tăiat. Nu schimbă planul doar pentru că stă lângă coloană.
        return """
                SELECT purchase_number FROM sales /* nota nu este rând */
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q12 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            cheia pe coloană, nu scrierea
        De ce e nevoie     fără PRIMARY KEY, numele lizibil lasă coloana fără cheie
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
    🔴 Staff
        Unde               obiectul index numit de cheie, nu tokenul cu underscore
    Pași                   1. Întoarce propoziția
    Enunț: Indexul din CREATE vine din numele purchase_number sau din PRIMARY KEY?
    Capcană: Underscore-ul creează indexul secundar.
    Cheie: Cheia este constrângerea PRIMARY KEY pe coloana scrisă. Numele scurt nu este un al doilea index. Efectul e în schemă, la sfârșitul CREATE.
    */
    // Începător: metoda desparte cheia de convenție.
    // Producție: q13 nu creează un index în plus. Dacă dispare, snake_case pare planul.
    // Expert: un index nefolosit nu grăbește o citire. Cheia de aici nici nu a fost măsurată cu EXPLAIN.
    public static String q13() {
        // Începător: rămâne o cheie pe purchase_number. Nu rămâne un index făcut din liniuța jos.
        // Producție: aceeași propoziție ca la tabela goală, citită pentru cheie. Dacă dispare, nu mai ai unde vedea PRIMARY KEY.
        // Expert: cheia se confundă cu un hint. Hint-ul nu creează obiectul de index; cheia da, când numele ei este coloana.
        return """
                CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q13 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o coloană a cărei literă nu o dublează
        De ce e nevoie     dacă litera ar dubla coloana, același fapt ar sta de două ori
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (PurchaseNumber INT)
    🔴 Staff
        Unde               regula coloanei, nu lower_case_table_names
    Pași                   1. Întoarce propoziția
    Enunț: PurchaseNumber și purchasenumber sunt două coloane?
    Capcană: Litera mare creează o a doua coloană, iar purchase_number e aceeași cu PurchaseNumber.
    Cheie: Pe orice platformă numele de coloană nu distinge literele: PurchaseNumber și purchasenumber sunt una. purchase_number este alt token, pentru că underscore-ul nu e literă.
    */
    // Începător: metoda ține coloana cu literă mare. Nu două coloane.
    // Producție: q14 nu face ALTER. Dacă dispare, litera mare pare un obiect nou.
    // Expert: regula nu se mută pe numele de tabelă în Unix cu lower_case_table_names 0.
    public static String q14() {
        // Începător: sales are o coloană. Rânduri nu apar.
        // Producție: CREATE TABLE sales (PurchaseNumber INT). Citirea purchasenumber vede aceeași coloană.
        // Expert: purchase_number nu este această coloană. Linia nu creează index și nu pliază numele tabelei.
        return """
                CREATE TABLE sales (PurchaseNumber INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q14 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            două litere pentru același nume de tabelă
        De ce e nevoie     fără o singură literă, propoziția nu se poate muta între Windows și Unix
    🟡 Producție
        Cum se scrie       SELECT * FROM my_table WHERE MY_TABLE.col = 1
    🔴 Staff
        Unde               numele de tabelă, dependent de sistemul de fișiere și de variabila fixată la inițializare
    Pași                   1. Întoarce propoziția
    Enunț: Când se vede că my_table și MY_TABLE nu sunt o convenție de citire?
    Capcană: La COMMIT, pentru că litera e o tranzacție.
    Cheie: În aceeași propoziție, manualul spune că referința cu două litere nu merge. Nu se schimbă rânduri. lower_case_table_names nu se modifică din SELECT.
    */
    // Începător: metoda ține amestecul de litere. Nu un stil de indentare.
    // Producție: q15 nu inițializează serverul. Dacă dispare, litera tabelei pare ca litera coloanei.
    // Expert: valoarea 1 pe Windows salvează numele cu litere mici. Nu o schimbi din această propoziție.
    public static String q15() {
        // Începător: niciun rând nu e scris. Referința e deja amestecată la execuția propoziției.
        // Producție: SELECT * FROM my_table WHERE MY_TABLE.col = 1. Dacă dispare, nu vezi cele două litere.
        // Expert: nu este cazul coloanei PurchaseNumber. Nu garantează același eșec pe Windows, unde comparația de tabelă nu distinge literele când valoarea e 1.
        return """
                SELECT * FROM my_table WHERE MY_TABLE.col = 1
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q15 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            schema la sfârșitul propoziției
        De ce e nevoie     dacă aștepți un comentariu să confirme, tabela există deja sau propoziția a căzut deja
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               sfârșitul propoziției, nu un lacăt lăsat de o notiță
    Pași                   1. Întoarce propoziția
    Enunț: Când se vede tabela sales după CREATE TABLE sales (purchase_number INT)?
    Capcană: Doar după un COMMIT scris de mână, pentru că un comentariu ține tranzacția deschisă.
    Cheie: Efectul pe schemă se vede la sfârșitul CREATE: propoziția următoare poate numi sales. Comentariul nu amână vizibilitatea, pentru că nu e o scriere de rând.
    */
    // Începător: metoda ține momentul. Nu un COMMIT.
    // Producție: q16 nu deschide tranzacție. Dacă dispare, convenția pare TCL.
    // Expert: nu afirm că CREATE cere sau nu cere COMMIT, pentru că pagina de DDL implicit nu a fost deschisă în tur.
    public static String q16() {
        // Începător: după propoziție, sales poate fi numită. Rândurile sunt zero.
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă dispare, nu ai momentul de arătat.
        // Expert: un comentariu pe linia următoare nu ține schema nedecisă. Nu este un hint care schimbă planul.
        return """
                CREATE TABLE sales (purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q16 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o notiță care nu atinge rânduri
        De ce e nevoie     dacă o crezi lacăt, cauți blocajul în editor
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales # fără lacăt din notiță
    🔴 Staff
        Unde               parserul taie diez-ul; lacătul ar veni dintr-o scriere sau dintr-o citire care chiar ține înregistrarea
    Pași                   1. Întoarce propoziția
    Enunț: O linie care este doar # nota ține un lacăt pe un rând din sales?
    Capcană: Da, comentariul blochează tabela până la COMMIT.
    Cheie: Notița nu este o propoziție care atinge rânduri. Nu ține lacăt de rând. Nu schimbă sales.
    */
    // Începător: metoda ține o citire cu diez. Notița nu e LOCK TABLES.
    // Producție: q17 nu ia lacăt, pentru că nu trimite comanda. Dacă dispare, diez-ul pare blocaj.
    // Expert: un SELECT poate lua lacăt în alt mecanism, după izolarea sesiunii. Diez-ul din linie nu este acel mecanism.
    public static String q17() {
        // Începător: se citesc valorile. Nota nu ține un rând după ce rezultatul a plecat.
        // Producție: SELECT purchase_number FROM sales # fără lacăt din notiță. Dacă dispare, notița pare parte din lacăt.
        // Expert: se desparte de un index lipsă, care poate lărgi ce ține InnoDB la o citire reală. Aici notița nu e filtrul.
        return """
                SELECT purchase_number FROM sales # fără lacăt din notiță
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q17 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un comentariu cu prag de versiune
        De ce e nevoie     fără prag, o extensie veche ar rula și unde nu e recunoscută, sau ai crede că nu rulează deloc
    🟡 Producție
        Cum se scrie       CREATE TABLE t1 (a INT, KEY (a)) slash-stea-exclamare50110 KEY_BLOCK_SIZE=1024 stea-slash
    🔴 Staff
        Unde               parserul de comentarii cu versiune, la sfârșitul propoziției
    Pași                   1. Întoarce propoziția
    Enunț: Când se execută CREATE TABLE t1 (a INT, KEY (a)) slash-stea-exclamare50110 KEY_BLOCK_SIZE=1024 stea-slash?
    Capcană: Numărul din comentariu e doar documentație, pe orice server.
    Cheie: Manualul 9.7: sintaxa din comentariul cu exclamare și număr se execută doar dacă versiunea serverului este cel puțin acel număr. 50110 înseamnă 5.1.10. Pe 9.7 se execută. Nu este convenția de nume.
    */
    // Începător: metoda ține exemplul din manual. Nu e o notiță de echipă.
    // Producție: q18 nu a fost trimis. Dacă dispare, numărul pare un comentariu de curs.
    // Expert: pentru 9.7.4 pragul de șase cifre ar fi 090704 sau forma de cinci 90704, după regula deschisă. 50110 este pragul din exemplul manualului, nu un stil.
    public static String q18() {
        // Începător: dacă serverul e cel puțin 5.1.10, KEY_BLOCK_SIZE rămâne în CREATE. Altfel bucata e sărită.
        // Producție: CREATE TABLE t1 (a INT, KEY (a)) /*!50110 KEY_BLOCK_SIZE=1024 */. Dacă dispare, nu vezi pragul.
        // Expert: nu garantează dimensiunea blocului pe sales, pentru că exemplul e t1. Nu este un index creat de underscore.
        return """
                CREATE TABLE t1 (a INT, KEY (a)) /*!50110 KEY_BLOCK_SIZE=1024 */
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q18 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o bucată de versiune care nu se păstrează în rutină
        De ce e nevoie     dacă te bazezi pe textul salvat, versiunea dispare din corp
    🟡 Producție
        Cum se scrie       SELECT 1 slash-stea-exclamare90704 stea-slash
    🔴 Staff
        Unde               la parsarea propoziției; corpul rutinei nu păstrează această formă
    Pași                   1. Întoarce propoziția
    Enunț: Un comentariu slash-stea-exclamare90704 stea-slash scris într-o rutină rămâne în corpul salvat?
    Capcană: Da, ca să documenteze versiunea lângă procedură.
    Cheie: Manualul 9.7: comentariile cu număr de versiune nu sunt păstrate în corpul rutinei. Efectul lor, dacă rulează, e la execuția propoziției, nu ca text salvat.
    */
    // Începător: metoda ține pragul 90704, cel scris în manual pentru 9.7.4.
    // Producție: q19 nu creează o procedură. Dacă dispare, pare că numărul rămâne în SHOW CREATE.
    // Expert: forma nu înlocuiește un comentariu -- cu spațiu, care e tot tăiat, dar regula de stocare e despre comentariul cu număr.
    public static String q19() {
        // Începător: pe 9.7.4 bucata poate intra în propoziție. Nu adaugă un rând lângă 1.
        // Producție: SELECT 1 /*!90704 */. Dacă dispare, nu ai pragul acestei versiuni.
        // Expert: nu garantează că un client mysql și serverul taie la fel limitele de propoziție. Pagina spune că clientul își face propria tăiere.
        return """
                SELECT 1 /*!90704 */
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q19 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o imbricare pe care serverul nu o suportă
        De ce e nevoie     dacă ascunzi cod înăuntru, închiderea nu e cea pe care o crezi
    🟡 Producție
        Cum se scrie       SELECT 1 slash-stea exterior slash-stea interior stea-slash + 1
    🔴 Staff
        Unde               parserul de comentarii, care nu ține imbricare
    Pași                   1. Întoarce propoziția
    Enunț: Ce se întâmplă cu SELECT 1 slash-stea exterior slash-stea interior stea-slash + 1?
    Capcană: Imbricarea e un comentariu mai lung, tot inert, și + 1 rămâne mereu.
    Cheie: Comentariile imbricate nu sunt suportate și sunt deprecate. Nu te baza pe ele ca să ascunzi + 1. Nu e convenția de nume.
    */
    // Începător: metoda ține forma deprecated. Nu o folosi ca notiță de echipă.
    // Producție: q20 nu a fost trimisă. Dacă dispare, imbricarea pare legală.
    // Expert: nu garantează un număr de eroare, pentru că serverul nu a răspuns în tur. Manualul spune că de obicei nu e permisă.
    public static String q20() {
        // Începător: nu lăsa + 1 în seama unei a doua deschideri. Rânduri nu se scriu.
        // Producție: SELECT 1 /* exterior /* interior */ + 1. Dacă dispare, nu vezi imbricarea.
        // Expert: nu este un hint cu plus și nu este un lacăt. Convenția de citire folosește o singură notiță închisă.
        return """
                SELECT 1 /* exterior /* interior */ + 1
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q20 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            spații între linii, nu un obiect de index
        De ce e nevoie     fără index real, rescrierea din obișnuință lasă același plan
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               familia Optimization: planul, nu propoziția rescrisă din obișnuință
    Pași                   1. Întoarce propoziția
    Enunț: De ce o rupere de rând în CREATE TABLE sales nu este un index?
    Capcană: Mai multe linii înseamnă un plan mai bun, ca un index nou.
    Cheie: Liniile sunt convenție. Indexul apare din PRIMARY KEY sau din CREATE INDEX, când planul îl folosește. Ruperea nu schimbă rândurile și nu scurtează căutarea.
    */
    // Începător: metoda ține CREATE-ul pe o linie. Nu CREATE INDEX.
    // Producție: q21 nu măsoară un plan. Dacă dispare, indentarea pare optimizare.
    // Expert: un index nefolosit nu grăbește. O linie nouă nici măcar nu e index.
    public static String q21() {
        // Începător: schema primește coloana. Planul unei citiri ulterioare nu e decis de câte rânduri de text ai folosit.
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă dispare, nu ai propoziția de comparat cu un index.
        // Expert: vecinul este hint-ul sau indexul. Această linie nu conține niciunul.
        return """
                CREATE TABLE sales (purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q21 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un plan citit, nu un nume rescris
        De ce e nevoie     fără deosebire, formatarea pare cauza unui full scan
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT purchase_number FROM sales
    🔴 Staff
        Unde               EXPLAIN este vecinul; convenția nu schimbă rândurile, planul poate
    Pași                   1. Întoarce propoziția
    Enunț: De ce EXPLAIN nu este convenția de nume?
    Capcană: EXPLAIN aplică snake_case și rescrie coloanele.
    Cheie: EXPLAIN arată planul și nu schimbă rândurile. purchase_number rămâne identificatorul din SELECT. Convenția nu apare ca operator în plan.
    */
    // Începător: metoda ține EXPLAIN-ul ca vecin, nu ca stil.
    // Producție: q22 nu a produs un plan în tur. Dacă dispare, numele coloanei pare diagnosticul.
    // Expert: nu inventa un type sau un key din EXPLAIN. Nu l-am rulat.
    public static String q22() {
        // Începător: rândurile rămân. Vezi planul, nu un comentariu aplicat.
        // Producție: EXPLAIN SELECT purchase_number FROM sales. Dacă dispare, vecinul nu mai e în propoziție.
        // Expert: un hint în SELECT poate intra în plan. EXPLAIN singur nu e acel hint.
        return """
                EXPLAIN SELECT purchase_number FROM sales
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q22 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o notiță care nu filtrează
        De ce e nevoie     dacă ascunzi filtrul în comentariu, raportul întoarce tot
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales -- purchase_number = 1
    🔴 Staff
        Unde               parserul taie notița înainte de orice filtrare
    Pași                   1. Întoarce propoziția
    Enunț: De ce -- purchase_number = 1 nu filtrează ca WHERE?
    Capcană: Comentariul e un WHERE mai lizibil.
    Cheie: Textul de după -- cu spațiu e tăiat. WHERE purchase_number = 1 ar fi filtrul. Notița lasă toate rândurile pe care SELECT le-ar fi întors.
    */
    // Începător: metoda ține filtrul fals, pus în comentariu.
    // Producție: q23 nu citește sales. Dacă dispare, -- pare o clauză.
    // Expert: nu este HAVING și nu este un index. Nu garantează un număr de rânduri, pentru că nu am rulat.
    public static String q23() {
        // Începător: niciun rând nu e șters. Filtrul nu există pentru server.
        // Producție: SELECT purchase_number FROM sales -- purchase_number = 1. Dacă dispare, nu vezi filtrul mort.
        // Expert: WHERE ar fi mecanismul vecin care chiar alege rânduri. Această linie nu îl conține.
        return """
                SELECT purchase_number FROM sales -- purchase_number = 1
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q23 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o notiță, nu sfârșitul unei tranzacții
        De ce e nevoie     dacă aștepți durabilitatea de la o notiță, scrierea reală rămâne nedecisă
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales -- nu este COMMIT
    🔴 Staff
        Unde               parserul de comentarii, nu jurnalul tranzacției
    Pași                   1. Întoarce propoziția
    Enunț: De ce un comentariu nu este COMMIT?
    Capcană: Notița închide tranzacția și face scrierea durabilă.
    Cheie: Comentariul nu începe și nu închide o tranzacție. Nu face durabilă o schimbare, pentru că nu schimbă rânduri.
    */
    // Începător: metoda ține o citire. Nu un COMMIT.
    // Producție: q24 nu deschide tranzacție. Dacă dispare, notița pare TCL.
    // Expert: COMMIT face schimbarea durabilă. Această linie nu are ce face durabil.
    public static String q24() {
        // Începător: rândurile nu se schimbă. Nu există un moment de commit al notiței.
        // Producție: SELECT purchase_number FROM sales -- nu este COMMIT. Dacă dispare, vecinul de tranzacție rămâne nenumit.
        // Expert: nu este ROLLBACK și nu eliberează un lacăt, pentru că notița nu a luat unul.
        return """
                SELECT purchase_number FROM sales -- nu este COMMIT
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q24 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            hint-ul, vecinul notiței
        De ce e nevoie     fără plus, nu ai cum să arăți optimizerului o alegere
    🟡 Producție
        Cum se scrie       SELECT slash-stea-plus BKA(t1) stea-slash purchase_number FROM sales
    🔴 Staff
        Unde               optimizerul primește hint-ul; notița nu ajunge
    Pași                   1. Întoarce propoziția
    Enunț: Ce desparte SELECT slash-stea-plus BKA(t1) stea-slash purchase_number FROM sales de o notiță?
    Capcană: Amândouă sunt convenție de echipă, deci optimizerul nu le vede.
    Cheie: Plusul face hint. Fără plus, același text e tăiat. Hint-ul este vecinul care poate atinge planul; convenția de rând nu.
    */
    // Începător: metoda ține vecinul. Nu o notiță de nume.
    // Producție: q25 nu măsoară planul. Dacă dispare, plusul pare decor.
    // Expert: nu garantează BKA pe sales. t1 nu e această tabelă.
    public static String q25() {
        // Începător: coloana citită e aceeași. Diferă doar că hint-ul nu e aruncat.
        // Producție: SELECT /*+ BKA(t1) */ purchase_number FROM sales. Fără plus, nu mai e vecinul.
        // Expert: hint-ul nu creează indexul și nu ține loc de PRIMARY KEY.
        return """
                SELECT /*+ BKA(t1) */ purchase_number FROM sales
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q25 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un cuvânt rezervat citat
        De ce e nevoie     fără backtick, CREATE se poate respinge pe un nume care pare lizibil
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (`select` INT)
    🔴 Staff
        Unde               lista de cuvinte rezervate; după punct, într-un nume calificat, citarea nu mai e obligatorie
    Pași                   1. Întoarce propoziția
    Enunț: De ce CREATE TABLE sales (`select` INT) nu este un nume scurt obișnuit?
    Capcană: select cu litere mici e mereu o coloană legală fără citare.
    Cheie: select este cuvânt rezervat. Fără backtick, nu e un identificator unquoted sigur. Citarea e decizia, nu convenția scurtă din PDF.
    */
    // Începător: metoda ține coloana rezervată. Nu purchase_number.
    // Producție: q26 nu creează sales. Dacă dispare, rezervatul pare un nume ca oricare.
    // Expert: excepția de după punct nu face `select` legal la începutul definiției de coloană.
    public static String q26() {
        // Începător: dacă propoziția trece, coloana există și rândurile sunt zero. Numele nu e convenția scurtă.
        // Producție: CREATE TABLE sales (`select` INT). Dacă dispare, nu vezi citarea.
        // Expert: nu este un JOIN și nu filtrează rânduri. Citarea nu creează un index.
        return """
                CREATE TABLE sales (`select` INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q26 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un nume, nu o potrivire de rânduri
        De ce e nevoie     dacă aștepți perechea de la nume, rândul fără pereche rămâne sau dispare din alt motiv
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales
    🔴 Staff
        Unde               JOIN schimbă mulțimea de rânduri; numele nu
    Pași                   1. Întoarce propoziția
    Enunț: De ce un nume de coloană nu este un INNER JOIN?
    Capcană: purchase_number leagă singur rândurile de o altă tabelă.
    Cheie: Numele nu caută perechi. INNER JOIN păstrează rândurile care au pereche. Convenția lasă identificatorul, nu rezultatul unui join.
    */
    // Începător: metoda ține o citire dintr-o tabelă. Nu un JOIN.
    // Producție: q27 nu leagă department. Dacă dispare, coloana pare o relație.
    // Expert: INNER JOIN fără pereche cade rândul din stânga. Această propoziție nu are a doua tabelă.
    public static String q27() {
        // Începător: rândurile rămân cele din sales. Niciun departament nu e căutat.
        // Producție: SELECT purchase_number FROM sales. Dacă dispare, nu ai propoziția fără join.
        // Expert: nu este un index pe cheia de join. Nu garantează că sales are rânduri.
        return """
                SELECT purchase_number FROM sales
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q27 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un nume lângă un tip, nu tipul însuși
        De ce e nevoie     fără INT, numele nu știe ce să păstreze
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               tipul stă în definiție; identificatorul doar o numește
    Pași                   1. Întoarce propoziția
    Enunț: De ce purchase_number nu alege tipul valorii?
    Capcană: Numele scurt face coloana INT mai îngustă.
    Cheie: Tipul scris, INT, ține valoarea. Numele este identificator. Convenția nu schimbă ce se pierde la o valoare prea mare pentru tip.
    */
    // Începător: metoda ține coloana și tipul ei. Nu doar porecla.
    // Producție: q28 nu schimbă un DECIMAL în INT. Dacă dispare, numele pare tipul.
    // Expert: INT nu e DECIMAL. Convenția nu mută scara. Nu garantează lățimea de afișare, scoasă din manualele noi și necitită aici ca lățime.
    public static String q28() {
        // Începător: schema are coloana de tip întreg. Rânduri nu apar.
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă dispare, nu vezi că tipul e scris separat de nume.
        // Expert: nu este un comentariu care să schimbe tipul. Un hint nu transformă INT în alt tip.
        return """
                CREATE TABLE sales (purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q28 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o propoziție fără culoare
        De ce e nevoie     dacă unifici stilul doar în editor, alt client tot trimite același SQL
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               granița: serverul parsează tokenul; Workbench doar îl pictează
    Pași                   1. Întoarce propoziția
    Enunț: De ce culoarea din Workbench nu este mecanismul?
    Capcană: Serverul primește culoarea și respinge stilul necolorat.
    Cheie: Culoarea și rearanjarea sunt clientul. mysqld primește propoziția. CREATE de mai jos nu conține culoare și lasă aceeași schemă.
    */
    // Începător: metoda ține propoziția care chiar pleacă. Nu click-ul.
    // Producție: q29 nu deschide Workbench. Dacă dispare, culoarea pare regulă de server.
    // Expert: PDF-ul cere unealta din Workbench. Manualul nu face din culoare un identificator.
    public static String q29() {
        // Începător: sales rămâne o chestiune de schemă. Culoarea nu lasă rânduri.
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă dispare, rămâne doar povestea despre editor.
        // Expert: nu este un hint și nu este un lacăt. Două culori pentru același token sunt aceeași propoziție.
        return """
                CREATE TABLE sales (purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q29 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            numele coloanei, nu valoarea din rând
        De ce e nevoie     dacă amesteci regulile, unicitatea valorilor pare rezolvată de litera numelui
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (PurchaseNumber INT)
    🔴 Staff
        Unde               regula de literă a identificatorului de coloană, deschisă în manualul 9.7
    Pași                   1. Întoarce propoziția
    Enunț: De ce litera din PurchaseNumber nu este litera din valoarea 'A'?
    Capcană: Dacă coloana nu distinge literele, nici 'A' și 'a' nu se disting.
    Cheie: Identificatorul de coloană nu distinge literele. Valoarea din rând ține de collation-ul coloanei, care e alt mecanism. Nu l-am măsurat aici.
    */
    // Începător: metoda ține numele. Nu un șir 'A'.
    // Producție: q30 nu compară șiruri. Dacă dispare, litera numelui pare collation.
    // Expert: INT nu ține 'A'. Exemplul de literă al numelui nu spune nimic despre 'a' egal cu 'A' în date.
    public static String q30() {
        // Începător: o coloană, zero rânduri. Nicio valoare nu e comparată.
        // Producție: CREATE TABLE sales (PurchaseNumber INT). Dacă dispare, nu ai identificatorul de despărțit de date.
        // Expert: purchase_number rămâne alt token. Nu este un index pe valoare.
        return """
                CREATE TABLE sales (PurchaseNumber INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q30 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            eșecul care nu lasă schema
        De ce e nevoie     dacă continui cu INSERT, eșuezi a doua oară, pe o tabelă care nu există
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase number INT)
    🔴 Staff
        Unde               respins la parsare, înainte de un rând
    Pași                   1. Întoarce propoziția
    Enunț: Ce rămâne după CREATE TABLE sales (purchase number INT) în producție?
    Capcană: Tabela există, cu două coloane: purchase și number.
    Cheie: Nu rămâne tabela. Tokenul cu spațiu e respins. Următoarea propoziție care numește sales nu o găsește din acest CREATE.
    */
    // Începător: metoda ține eșecul de pus în producție o singură dată, ca să-l vezi.
    // Producție: q31 nu a primit codul numeric, pentru că serverul nu a răspuns. Dacă dispare, spațiul pare creat.
    // Expert: nu inventa 1064 pentru această propoziție. Pagina deschisă arată 1064 la ghilimelele duble fără ANSI_QUOTES, nu la acest spațiu.
    public static String q31() {
        // Începător: sales nu apare. Niciun rând nu e de dat înapoi.
        // Producție: CREATE TABLE sales (purchase number INT). Dacă dispare, eșecul nu mai e în bancă.
        // Expert: nu se repară cu un comentariu deasupra. Backtick-ul este altă propoziție.
        return """
                CREATE TABLE sales (purchase number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q31 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            cheia din slide, scrisă greșit
        De ce e nevoie     fără coloana din cheie, tabela nu apare și încărcarea următoare cade
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number))
    🔴 Staff
        Unde               numele din cheie trebuie să fie coloana scrisă
    Pași                   1. Întoarce propoziția
    Enunț: Ce face PRIMARY KEY (purcase_number) când coloana este purchase_number?
    Capcană: E aceeași coloană, pentru că literele nu contează și o literă în plus e tot literă.
    Cheie: purcase_number este alt identificator, nu o pliere. Cheia nu găsește coloana. CREATE nu lasă sales.
    */
    // Începător: metoda ține typo-ul din PDF. Nu convenția corectă.
    // Producție: q32 nu a fost rulat. Dacă dispare, slide-ul pare reușit.
    // Expert: lower_case_table_names nu scoate o literă din mijlocul cuvântului.
    public static String q32() {
        // Începător: sales nu rămâne. Nu există cheie de folosit la o citire.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number)). Dacă dispare, nu vezi cuvântul greșit.
        // Expert: nu este un plan rău. Este o propoziție care nu creează obiectul. Comentariul nu o repară.
        return """
                CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number))
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q32 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un comentariu care nu a început
        De ce e nevoie     dacă voiai să dezactivezi restul liniei, restul încă rulează
    🟡 Producție
        Cum se scrie       SELECT 1--1
    🔴 Staff
        Unde               regula spațiului obligatoriu după --
    Pași                   1. Întoarce propoziția
    Enunț: Ce se rupe dacă lași SELECT 1--1 într-un script de producție?
    Capcană: 1--1 e o notiță, deci scriptul sare 1 și continuă curat.
    Cheie: -- fără spațiu nu taie linia. Expresia rămâne în script. Poți calcula altceva decât credeai că ai comentat.
    */
    // Începător: metoda ține linia periculoasă din producție.
    // Producție: q33 nu a întors un număr. Dacă dispare, spațiul după -- pare opțional.
    // Expert: nu afirma că rezultatul e 2. Serverul nu a fost întrebat. Afirmă doar că nu e notiță.
    public static String q33() {
        // Începător: propoziția încă are restul liniei. Nu e un rând șters.
        // Producție: SELECT 1--1. Dacă dispare, eșecul de comentariu nu mai e vizibil.
        // Expert: nu este #, care nu cere spațiu. Nu este un lacăt.
        return """
                SELECT 1--1
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q33 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un comentariu pe care nu te poți baza
        De ce e nevoie     codul pe care voiai să-l ascunzi poate rămâne în propoziție
    🟡 Producție
        Cum se scrie       SELECT 1 slash-stea exterior slash-stea interior stea-slash + 1
    🔴 Staff
        Unde               manualul 9.7: imbricare nesuportată
    Pași                   1. Întoarce propoziția
    Enunț: Ce risc are SELECT 1 slash-stea exterior slash-stea interior stea-slash + 1 într-un script?
    Capcană: Imbricarea comentă tot până la ultimul stea-slash, mereu.
    Cheie: Imbricarea nu e suportată și e deprecated. Închiderea pe care o vezi poate să nu fie închiderea pe care o parsează serverul. Nu ascunde + 1 așa.
    */
    // Începător: metoda ține forma de nefolosit în producție.
    // Producție: q34 nu a fost parsată în tur. Dacă dispare, imbricarea intră în standardul echipei.
    // Expert: nu garanta un mesaj de eroare anume. Pagina spune că de obicei nu e permis.
    public static String q34() {
        // Începător: nu lăsa operandul în seama unei a doua deschideri.
        // Producție: aceeași propoziție imbricată, ca eșec de producție. Dacă dispare, riscul rămâne doar în teorie.
        // Expert: nu este hint-ul cu plus. Nu schimbă schema.
        return """
                SELECT 1 /* exterior /* interior */ + 1
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q34 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un nume care începe cu dolar
        De ce e nevoie     dacă ignori avertismentul, următorul nume cu încă un dolar cade, iar stilul nu e portabil
    🟡 Producție
        Cum se scrie       CREATE TABLE sales ($purchase_number INT)
    🔴 Staff
        Unde               regula dolarului din pagina de identificatori, 9.7
    Pași                   1. Întoarce propoziția
    Enunț: Ce avertisment aduce CREATE TABLE sales ($purchase_number INT)?
    Capcană: Dolarul e interzis și tabela nu apare niciodată.
    Cheie: Un identificator unquoted care începe cu dolar e deprecated pentru coloană și dă avertisment. Nu poate conține un al doilea dolar. Nu e convenția scurtă din PDF.
    */
    // Începător: metoda ține numele cu dolar. Nu purchase_number.
    // Producție: q35 nu a văzut textul avertismentului în tur. Dacă dispare, dolarul pare un underscore.
    // Expert: citarea poate ține dolarul la început fără avertisment, după pagina deschisă. Asta e altă propoziție.
    public static String q35() {
        // Începător: poate rămâne o coloană, cu avertisment. Nu afirm că tabela e respinsă.
        // Producție: CREATE TABLE sales ($purchase_number INT). Dacă dispare, nu vezi dolarul de la început.
        // Expert: un al doilea dolar în același nume unquoted nu e permis. Nu este un index.
        return """
                CREATE TABLE sales ($purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q35 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            amestecul de litere la tabelă
        De ce e nevoie     scriptul care merge pe un sistem cade pe altul
    🟡 Producție
        Cum se scrie       SELECT * FROM my_table WHERE MY_TABLE.col = 1
    🔴 Staff
        Unde               litera tabelei, nu litera coloanei
    Pași                   1. Întoarce propoziția
    Enunț: Ce se rupe când același SELECT folosește my_table și MY_TABLE?
    Capcană: E doar stil, serverul le unește mereu.
    Cheie: Manualul spune că propoziția nu merge, pentru că același nume e scris cu două litere. Pe Windows, cu valoarea 1, comparația nu distinge literele, dar tot nu amesteci stilul dacă muți scriptul pe Unix.
    */
    // Începător: metoda ține eșecul de portabilitate.
    // Producție: q36 nu a rulat pe Unix și nici pe Windows. Dacă dispare, două litere par sigure.
    // Expert: nu schimba lower_case_table_names dintr-un SELECT. Se fixează la inițializare.
    public static String q36() {
        // Începător: niciun rând nu e actualizat. Referința e deja suspectă.
        // Producție: SELECT * FROM my_table WHERE MY_TABLE.col = 1. Dacă dispare, nu vezi cele două forme.
        // Expert: coloana PurchaseNumber nu are acest eșec. Aliasul de tabelă pe Unix e și el sensibil la literă, alt obiect decât coloana.
        return """
                SELECT * FROM my_table WHERE MY_TABLE.col = 1
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q36 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un mod care mută ghilimelele de la șir la nume
        De ce e nevoie     dacă lași ghilimele duble pe un literal, literalul devine identificator
    🟡 Producție
        Cum se scrie       SET sql_mode='ANSI_QUOTES'
    🔴 Staff
        Unde               modul de server, nu convenția de underscore
    Pași                   1. Întoarce propoziția
    Enunț: Ce schimbă SET sql_mode='ANSI_QUOTES' pentru ghilimele?
    Capcană: Doar stilul de citare a șirurilor, fără efect pe identificatori.
    Cheie: Cu ANSI_QUOTES, ghilimelele duble sunt identificatori, nu șiruri. CREATE TABLE "test" (col INT) cade fără mod și trece cu modul, cum arată manualul. Șirurile rămân între apostrofuri.
    */
    // Începător: metoda ține comutatorul. Nu un nume de coloană.
    // Producție: q37 nu a setat modul în tur. Dacă dispare, ghilimelele par mereu șiruri.
    // Expert: manualul arată ERROR 1064 pe CREATE TABLE "test" fără mod. Nu muta numărul pe spațiul din purchase number.
    public static String q37() {
        // Începător: după setare, sesiunea citește ghilimelele duble ca nume. Rândurile unei tabele nu se schimbă din SET.
        // Producție: SET sql_mode='ANSI_QUOTES'. Dacă dispare, nu vezi de ce un șir între ghilimele duble se rupe.
        // Expert: nu este backtick-ul, care citează și fără acest mod. Nu este un index.
        return """
                SET sql_mode='ANSI_QUOTES'
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q37 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            două litere pentru același alias de tabelă
        De ce e nevoie     scriptul de pe laptopul Windows cade pe serverul Unix
    🟡 Producție
        Cum se scrie       SELECT col_name FROM tbl_name AS a WHERE a.col_name = 1 OR A.col_name = 2
    🔴 Staff
        Unde               aliasul de tabelă, excepție față de coloană
    Pași                   1. Întoarce propoziția
    Enunț: Unde crapă SELECT col_name FROM tbl_name AS a WHERE a.col_name = 1 OR A.col_name = 2?
    Capcană: Aliasul e coloană, deci litera nu contează nicăieri.
    Cheie: Manualul: aliasul de tabelă e sensibil la literă pe Unix și nu e pe Windows. a și A în aceeași propoziție nu merg pe Unix. Nu e convenția de nume a coloanei.
    */
    // Începător: metoda ține exemplul din manual, cu a și A.
    // Producție: q38 nu a fost rulat pe Unix. Dacă dispare, aliasul pare insensibil peste tot.
    // Expert: coloana rămâne insensibilă. Nu muta regula aliasului pe purchase_number.
    public static String q38() {
        // Începător: niciun rând nu e scris. Eșecul e la numele aliasului, în timpul propoziției.
        // Producție: aceeași propoziție din manual. Dacă dispare, a și A nu mai sunt vizibile.
        // Expert: nu este lower_case_table_names pe coloană. Nu garantează eșecul pe Windows.
        return """
                SELECT col_name FROM tbl_name AS a WHERE a.col_name = 1 OR A.col_name = 2
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q38 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un identificator ambiguu lângă un exponent
        De ce e nevoie     o expresie din raport poate fi citită ca număr, nu ca coloană plus trei
    🟡 Producție
        Cum se scrie       SELECT 1e+3
    🔴 Staff
        Unde               ambiguitatea descrisă la identificatori, în manualul 9.7
    Pași                   1. Întoarce propoziția
    Enunț: De ce nu numești o coloană 1e?
    Capcană: 1e e un nume scurt, deci e convenția bună.
    Cheie: Manualul: 1e+3 poate fi numărul sau expresia 1e + 3. Numele care începe cu cifră și e, ca 1e, e ambiguu. Nu e un nume scurt sigur.
    */
    // Începător: metoda ține expresia ambiguă. Nu o coloană purchase_number.
    // Producție: q39 nu a ales una dintre cele două citiri. Dacă dispare, 1e pare sigur.
    // Expert: nu afirma care citire câștigă. Pagina spune că depinde de context.
    public static String q39() {
        // Începător: nu se creează o tabelă. Riscul e la parsarea expresiei.
        // Producție: SELECT 1e+3. Dacă dispare, nu vezi forma de evitat.
        // Expert: nu este dolarul de la început și nu este un index. PDF-ul cere un cuvânt pe concept, nu un exponent.
        return """
                SELECT 1e+3
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q39 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o variabilă pusă în locul numelui
        De ce e nevoie     dacă construiești FROM din @sales, propoziția nu vede tabela
    🟡 Producție
        Cum se scrie       SELECT * FROM @sales
    🔴 Staff
        Unde               identificatorul nu se ia din variabila de utilizator
    Pași                   1. Întoarce propoziția
    Enunț: Poți scrie SELECT * FROM @sales dacă @sales ține numele tabelei?
    Capcană: Variabila de utilizator înlocuiește identificatorul la execuție.
    Cheie: Pagina de identificatori: o variabilă de utilizator nu poate fi folosită direct ca identificator. @sales nu numește tabela sales.
    */
    // Începător: metoda ține forma care nu numește sales.
    // Producție: q40 nu a deschis pagina 11.4 cu ocolurile. Dacă dispare, @sales pare un alias.
    // Expert: nu inventa un PREPARE care nu e în această propoziție. Afirmă doar interdicția directă.
    public static String q40() {
        // Începător: niciun rând din sales nu e citit prin această formă, pentru că @sales nu e numele.
        // Producție: SELECT * FROM @sales. Dacă dispare, variabila pare legală în FROM.
        // Expert: nu este un comentariu și nu este backtick. Nu ține lacăt pe un rând, pentru că nu ajunge la tabelă.
        return """
                SELECT * FROM @sales
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q40 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            citarea, doar când tokenul unquoted nu poate ține numele
        De ce e nevoie     fără această alegere, ori respingi un nume obligatoriu, ori citezi tot și îngreui citirea
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (`select` INT)
    🔴 Staff
        Unde               cuvânt rezervat sau spațiu; nu stilul de zi cu zi
    Pași                   1. Întoarce propoziția
    Enunț: Când alegi backtick-ul în locul convenției purchase_number?
    Capcană: Mereu, ca să accepți spații și cuvinte rezervate în tot schema.
    Cheie: Alegi backtick-ul când numele trebuie să aibă spațiu sau e rezervat, ca `select`. Altfel rămâi la purchase_number, un token, fără citare.
    */
    // Începător: metoda ține alegerea de citare. Nu numele scurt.
    // Producție: q41 nu creează schema. Dacă dispare, backtick-ul pare implicit.
    // Expert: după un punct, un rezervat nu trebuie citat. La definiția coloanei, da.
    public static String q41() {
        // Începător: coloana poate exista cu numele rezervat. Rânduri nu se inserează.
        // Producție: CREATE TABLE sales (`select` INT). Dacă dispare, nu vezi când părăsești convenția scurtă.
        // Expert: citarea nu înlocuiește PRIMARY KEY și nu schimbă planul.
        return """
                CREATE TABLE sales (`select` INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q41 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            forma trimisă, fără culoare
        De ce e nevoie     dacă ceri serverului să-ți țină stilul, îl confunzi cu schema
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               clientul formatează; mysqld parsează
    Pași                   1. Întoarce propoziția
    Enunț: Când lași formatarea în aplicație, nu pe server?
    Capcană: Serverul trebuie să primească SQL-ul deja colorat, altfel îl respinge.
    Cheie: Culoarea și indentarea rămân în editor sau în aplicație. Serverul primește tokenii. Aceeași propoziție, cu sau fără linii goale, creează aceeași coloană.
    */
    // Începător: metoda ține propoziția care contează pentru server.
    // Producție: q42 nu cheamă unealta din Workbench. Dacă dispare, stilul pare obiect în schemă.
    // Expert: PDF-ul spune să unifici stilul în echipă. Asta nu e o constrângere InnoDB.
    public static String q42() {
        // Începător: sales rămâne cu o coloană. Stilul nu lasă un rând de metadate de culoare.
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă dispare, nu ai ce trimite în locul poveștii despre editor.
        // Expert: nu este un hint. Nu garantează că două formatoare emit același spațiu într-un șir, doar că spațiul din afara tokenului nu e numele.
        return """
                CREATE TABLE sales (purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q42 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            numele de tabelă cu litere mici
        De ce e nevoie     fără o singură convenință de literă, Unix cu valoarea 0 vede două tabele
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               recomandarea de portabilitate din pagina de literă
    Pași                   1. Întoarce propoziția
    Enunț: Când scrii tabela cu litere mici, nu Sales?
    Capcană: Mereu, pentru că și coloanele disting literele.
    Cheie: Manualul recomandă litere mici la baze și tabele, ca să muți scriptul. Coloanele nu disting literele oricum. Alegi litere mici la tabelă pentru portabilitate, nu ca să schimbi planul.
    */
    // Începător: metoda ține sales cu litere mici.
    // Producție: q43 nu setează lower_case_table_names. Dacă dispare, Sales și sales par mereu identice.
    // Expert: pe Windows, valoarea 1 le salvează oricum cu litere mici. Pe Unix, valoarea 0 nu le unește.
    public static String q43() {
        // Începător: o tabelă, zero rânduri. Litera nu inserează.
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă dispare, nu ai forma portabilă.
        // Expert: nu plia coloana purchase_number în purchasenumber. Sunt alegeri diferite: litera tabelei, nu underscore-ul coloanei.
        return """
                CREATE TABLE sales (purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q43 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            hint-ul, doar pentru plan
        De ce e nevoie     fără această graniță, un comentariu de echipă schimbă planul
    🟡 Producție
        Cum se scrie       SELECT slash-stea-plus BKA(t1) stea-slash purchase_number FROM sales
    🔴 Staff
        Unde               optimizerul; convenția nu intră acolo
    Pași                   1. Întoarce propoziția
    Enunț: Când lași convenția și scrii un hint?
    Capcană: La fiecare SELECT, ca să arăți că stăpânești optimizerul.
    Cheie: Scrii hint-ul când problema e planul și ai un motiv pentru BKA sau pentru alt hint. Când problema e cititorul, lași notița sau numele, nu plusul.
    */
    // Începător: metoda ține alegerea de hint. Nu notița.
    // Producție: q44 nu a măsurat planul. Dacă dispare, plusul pare obligatoriu.
    // Expert: nu garantează un plan mai bun pe sales. Hint-ul poate fi ignorat dacă nu se aplică.
    public static String q44() {
        // Începător: rândurile citite rămân ale coloanei. Nu se creează un index din hint.
        // Producție: SELECT /*+ BKA(t1) */ purchase_number FROM sales. Dacă dispare, nu vezi când părăsești notița.
        // Expert: CREATE INDEX este cealaltă unealtă când vrei un obiect, nu o indicație de plan.
        return """
                SELECT /*+ BKA(t1) */ purchase_number FROM sales
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q44 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o notiță care pleacă spre server doar dacă e în SQL
        De ce e nevoie     dacă documentezi regula doar în Java, cine citește SQL-ul nu o vede; dacă o pui ca hint, atinge planul
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales -- rămâne pentru cititor, nu pentru rânduri
    🔴 Staff
        Unde               comentariul SQL e tăiat; cel din aplicație nu ajunge
    Pași                   1. Întoarce propoziția
    Enunț: Comentariul din Java și comentariul SQL sunt aceeași alegere?
    Capcană: Da, amândouă rămân în tabelă.
    Cheie: Comentariul Java nu e trimis. Comentariul SQL -- cu spațiu e trimis și tăiat de server. Niciunul nu inserează un rând. Doar cel SQL poate fi, din greșeală, un hint dacă scrii slash-stea-plus.
    */
    // Începător: metoda ține notița SQL. Nu un comentariu de clasă.
    // Producție: q45 nu e un INSERT de documentație. Dacă dispare, cele două comentarii par același canal.
    // Expert: slash-stea-plus în SQL nu e echivalentul unui comentariu de metodă. Ajunge la optimizer.
    public static String q45() {
        // Începător: rezultatul e coloana. Nota nu e valoare.
        // Producție: SELECT purchase_number FROM sales -- rămâne pentru cititor, nu pentru rânduri. Dacă dispare, nu vezi canalul SQL.
        // Expert: nu ține loc de constrângere. Nu garantează că un formatator nu lipește -- de tokenul următor.
        return """
                SELECT purchase_number FROM sales -- rămâne pentru cititor, nu pentru rânduri
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q45 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o cheie, nu o notiță de unic
        De ce e nevoie     fără cheie, al doilea același număr intră
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
    🔴 Staff
        Unde               constrângerea rămâne în schemă; -- unic e tăiat
    Pași                   1. Întoarce propoziția
    Enunț: Când nu lași unicitatea într-un comentariu?
    Capcană: Când echipa e atentă, -- unic e de ajuns.
    Cheie: Comentariul nu refuză un al doilea purchase_number. Alegi PRIMARY KEY (purchase_number) când duplicatul trebuie respins. Notița rămâne pentru om.
    */
    // Începător: metoda ține alegerea de cheie.
    // Producție: q46 nu face INSERT duplicat. Dacă dispare, notița pare UNIQUE.
    // Expert: cheia nu e un index secundar făcut din nume. E constrângerea scrisă.
    public static String q46() {
        // Începător: sales rămâne goală, dar cu cheie. Unicitatea se vede la o scriere ulterioară, nu în comentariu.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)). Dacă dispare, rămâne doar -- unic, care nu constrânge.
        // Expert: nu este un hint. Nu ține lacăt după CREATE.
        return """
                CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q46 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un token scurt, nu o frază citată
        De ce e nevoie     fără un cuvânt stabil, 80% din textul pe care îl citești devine zgomot, cum spune cursul, fără să schimbe schema
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               convenția de nume din PDF; citarea rămâne pentru spațiu și rezervate
    Pași                   1. Întoarce propoziția
    Enunț: Când refuzi un nume-propoziție citat și rămâi la un cuvânt?
    Capcană: Numele lung citat e mereu mai clar pentru optimizer.
    Cheie: PDF-ul cere un cuvânt pe concept, scurt. `the unique customer purchase number` e un identificator legal doar citat, dar nu e convenția. purchase_number rămâne tokenul. Planul nu citește lungimea numelui ca să sară rânduri.
    */
    // Începător: metoda ține cuvântul scurt.
    // Producție: q47 nu citează o frază. Dacă dispare, lungimea pare un index.
    // Expert: nu garantează că un nume scurt e mai rapid. Manualul nu leagă lungimea tokenului de plan.
    public static String q47() {
        // Începător: o coloană, zero rânduri. Fraza citată ar fi fost alt identificator, tot fără rânduri.
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă dispare, nu ai forma aleasă de curs.
        // Expert: se desparte de `purchase number`, care există doar cu backtick și tot nu filtrează rânduri.
        return """
                CREATE TABLE sales (purchase_number INT)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q47 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            o extensie păzită de versiune
        De ce e nevoie     fără prag, ori pierzi portabilitatea, ori rulezi sintaxa pe un server prea vechi
    🟡 Producție
        Cum se scrie       SELECT 1 slash-stea-exclamare90704 stea-slash
    🔴 Staff
        Unde               comentariul executabil cu număr, din manualul 9.7
    Pași                   1. Întoarce propoziția
    Enunț: Când folosești un comentariu cu versiune, nu un cuvânt MySQL gol?
    Capcană: Niciodată, pentru că 9.7 execută orice comentariu.
    Cheie: Folosești slash-stea-exclamare90704 ... stea-slash când bucata trebuie să ruleze doar de la 9.7.4 în sus și să fie ignorată de alte servere. Un cuvânt MySQL gol cade pe serverele care nu îl cunosc. Convenția de nume nu rezolvă asta.
    */
    // Începător: metoda ține pragul acestei versiuni.
    // Producție: q48 nu a verificat un server mai vechi. Dacă dispare, 90704 pare o notiță.
    // Expert: comentariile cu număr nu rămân în corpul unei rutine. Nu le folosi ca documentație salvată.
    public static String q48() {
        // Începător: pe 9.7.4 bucata poate intra în propoziție. Nu adaugă un rând.
        // Producție: SELECT 1 /*!90704 */. Dacă dispare, nu ai pragul citit din manual.
        // Expert: nu este # și nu este un index. Nu garantează același parsaj în clientul mysql, care taie propozițiile înainte de server.
        return """
                SELECT 1 /*!90704 */
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q48 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            stil în editor, token în propoziție
        De ce e nevoie     dacă muți cheia doar în culoare, duplicatul intră
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
    🔴 Staff
        Unde               granița dintre client și mysqld
    Pași                   1. Întoarce propoziția
    Enunț: Ce lași în editor și ce trebuie să fie în propoziție?
    Capcană: Tot, inclusiv cheia, poate rămâne o culoare și un comentariu.
    Cheie: În editor lași culoarea și indentarea. În propoziție trebuie identificatorul și, dacă vrei unicitate, PRIMARY KEY. Culoarea nu creează sales.
    */
    // Începător: metoda ține ce trebuie să rămână în propoziție.
    // Producție: q49 nu colorează nimic. Dacă dispare, editorul pare serverul.
    // Expert: PDF-ul cere unealta de rearanjare. Ea nu înlocuiește cheia.
    public static String q49() {
        // Începător: schema are coloana și cheia. Culoarea nu e în catalogul de obiecte.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)). Dacă dispare, rămâne doar sfatul de editor.
        // Expert: nu este EXPLAIN. Nu garantează un plan doar pentru că textul e aliniat.
        return """
                CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q49 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    /*
    🟢 Începător
        Ce este            un index, când problema e planul
        De ce e nevoie     fără index, rescrierea pe mai multe linii lasă aceeași căutare
    🟡 Producție
        Cum se scrie       CREATE INDEX sales_purchase ON sales (purchase_number)
    🔴 Staff
        Unde               obiectul de index, vecinul convenției
    Pași                   1. Întoarce propoziția
    Enunț: Când alegi CREATE INDEX, nu încă o rescriere a convenției?
    Capcană: Când propoziția e urâtă, un index o face lizibilă.
    Cheie: Alegi CREATE INDEX sales_purchase ON sales (purchase_number) când problema e căutarea și planul poate folosi indexul. Convenția rămâne când problema e cititorul. Un index nefolosit nu grăbește.
    */
    // Începător: metoda ține indexul. Nu o notiță.
    // Producție: q50 nu a rulat EXPLAIN. Dacă dispare, liniuța de jos pare indexul.
    // Expert: nu garantează că optimizerul alege sales_purchase. Un index nefolosit nu scurtează căutarea.
    public static String q50() {
        // Începător: apare un index pe coloana care există. Rândurile nu se copiază din cauza numelui.
        // Producție: CREATE INDEX sales_purchase ON sales (purchase_number). Dacă dispare, nu vezi unealta de plan.
        // Expert: hint-ul e cealaltă unealtă, fără obiect nou. Convenția nu e nici index, nici hint.
        return """
                CREATE INDEX sales_purchase ON sales (purchase_number)
                """;
        // Începător: din închiderea metodei nu apare tabela sales și nu apare niciun rând.
        // Producție: fără acoladă, q50 nu compilează. Propoziția a rămas în return.
        // Expert: acolada nu citează un nume și nu taie un comentariu. purchase number rămâne ilegal doar în propoziția de deasupra, nu în Java.
    }

    // Începător: clasa se închide. Niciuna dintre propoziții nu a rulat pe server.
    // Producție: acolada finală închide banca de cincizeci. Dacă dispare, fișierul nu compilează.
    // Expert: închiderea clasei nu este un index și nu este EXPLAIN. Serverul rămâne nerulat în tur.
}
