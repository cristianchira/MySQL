// Începător: pachetul ține lecția scurtă lângă notițele despre cum se scrie SQL. Nu creează tabela sales.
// Producție: folderul fast este calea compilată. Fără linia aceasta, clasa nu mai stă lângă Coding_technique.
// Expert: pachetul Java nu este un identificator MySQL și nu schimbă lower_case_table_names. Planul nu se decide aici.
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
    CodingTechnique_Fast.main
REPO DEMO:
    src/main/java/_9_Coding_Technique/fast/CodingTechnique_Fast.java
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
    Ce este            forma lizibilă care lasă sales cu purchase_number și zero rânduri
    De ce e nevoie     fără un singur token, CREATE nu lasă tabela
🟡 Producție
    Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
🔴 Staff
    Unde               parserul de identificatori; vecinul este hint-ul sau indexul, care pot schimba planul
Pași                   1. Ține propoziția
                       2. O tipărește, fără să o trimită
*/
// Începător: clasa arată comanda pe care echipa o poate citi. Nu umple sales cu rânduri.
// Producție: CodingTechnique_Fast nu deschide o conexiune. Dacă declarația dispare, lecția nu mai are main.
// Expert: clasa nu este un index și nu înlocuiește EXPLAIN. Nu garantează litera tabelei pe disc.
public class CodingTechnique_Fast {

    /*
    🟢 Începător
        Ce este            exemplul minim: același CREATE pe care îl citește omul
        De ce e nevoie     fără propoziție, convenția rămâne o poveste despre Workbench
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției, pe schemă, nu pe un lacăt de rând
    Pași                   1. Construiește textul
                           2. Îl scrie la ieșire
    */
    // Începător: main pornește exemplul. Tabela nu se creează în acest proces.
    // Producție: main(String[] args) nu primește un URL. Dacă dispare, nu mai există lecția care se poate rula local.
    // Expert: argumentele nu sunt un identificator MySQL. Rularea pe server rămâne nerulată cât nu există o conexiune în repo.
    public static void main(String[] args) {
        // Începător: textul este comanda care lasă sales goală, cu o coloană și cu cheia pe ea.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)). Dacă dispare, main nu mai are propoziția.
        // Expert: ruperea pe mai multe linii nu este un index. Se desparte de un hint slash-stea-plus, care poate atinge planul; linia aceasta nu îl conține.
        String sql = """
                CREATE TABLE sales (
                    purchase_number INT,
                    PRIMARY KEY (purchase_number)
                )
                """;
        // Începător: pe ecran apare comanda, nu rândurile din sales.
        // Producție: System.out.println(sql) nu deschide mysqld. Dacă dispare, propoziția rămâne doar în variabilă.
        // Expert: tipărirea nu este EXPLAIN și nu ia lacăt. Nu garantează că serverul ar accepta propoziția, pentru că nu a fost trimisă.
        System.out.println(sql);
    }
}
