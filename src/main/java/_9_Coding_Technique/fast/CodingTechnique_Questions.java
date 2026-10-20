// Începător: pachetul ține întrebările lângă lecția despre cum se scrie o propoziție. Nu creează sales.
// Producție: clasa rămâne în fast. Fără linia aceasta, FAQ-ul nu mai stă lângă CodingTechnique_Fast.
// Expert: pachetul nu pliază numele de tabelă și nu este lower_case_table_names.
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

1. Enunț: Ce rămâne în schemă după CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))?
   Capcană: Numele scurt face serverul să citească mai puține rânduri.
   Cheie: Tabela sales există, coloana purchase_number există, rândurile sunt zero. Cheia este constrângerea, nu underscore-ul.
   Demonstrație: Coding_technique.readableSalesTable
2. Enunț: Ce face un comentariu -- cu spațiu pus pe SELECT purchase_number FROM sales?
   Capcană: Notița se inserează ca rând.
   Cheie: Serverul taie notița. Rezultatul nu capătă o coloană nouă.
   Demonstrație: Coding_technique.commentLeavesNoRow
3. Enunț: De ce purchase number fără backtick nu este aceeași convenție ca purchase_number?
   Capcană: Spațiul este doar aerisirea numelui.
   Cheie: Spațiul rupe tokenul. Serverul respinge propoziția și sales nu apare.
   Demonstrație: Coding_technique.spaceIsRejected
4. Enunț: Cum deosebești o notiță slash-stea de hint-ul cu plus, pe aceeași coloană purchase_number?
   Capcană: Orice text dintre slash-stea este ignorat, deci planul rămâne neatins.
   Cheie: Notița este tăiată. Forma cu plus și BKA(t1) ajunge la optimizer și nu este un index pe sales.
   Demonstrație: Coding_technique.hintIsNotLayout
5. Enunț: Când nu mai ajunge convenția și scrii cheia sau backtick-ul?
   Capcană: Unicitatea se lasă într-un comentariu -- unic.
   Cheie: Comentariul nu creează cheia. PRIMARY KEY trebuie să numească purchase_number, nu purcase_number. Cuvântul rezervat se citează.
   Demonstrație: Coding_technique.keyNameMustBeTheColumn

Întrebări adăugate
*/

/*
🟢 Începător
    Ce este            lista de întrebări despre convenție, fără o propoziție executată aici
    De ce e nevoie     fără întrebări, notița din curs pare o regulă de server
🟡 Producție
    Cum se scrie       corpul rămâne gol; propozițiile stau în Coding_technique
🔴 Staff
    Unde               cheia este scrisă deasupra; banca de cincizeci stă în CodingTechnique_Staff_Interview
Pași                   1. Nu trimite SQL
*/
// Începător: clasa nu creează sales și nu șterge rânduri. Întrebările sunt în comentariul de deasupra.
// Producție: declarația nu deschide o conexiune. Dacă dispare, dispare locul în care se adaugă întrebări.
// Expert: un corp gol nu este un index și nu este EXPLAIN. Nu garantează că studentul a răspuns.
public class CodingTechnique_Questions {
    // Începător: acolada nu lasă o tabelă. Nu există metodă care să trimită o propoziție.
    // Producție: închiderea păstrează FAQ-ul gol. O metodă adăugată aici ar umple corpul.
    // Expert: golul desparte această clasă de CodingTechnique_Staff_Interview, unde q01–q50 țin propozițiile.
}
