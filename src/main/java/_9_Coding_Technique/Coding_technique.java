package _9_Coding_Technique;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8344736#questions


/*                                           Coding Techniques and Best Practices


    ○ Complying with coding style is crucial

                   * you will always work in a team

    ○ Good code

                   * is not the one, computers understand;

                   * it is the one, humans can understand


                                             Code, in general, can be organized in several ways


    1. good practice implies

                   * you will choose the version that will be easiest to read and understand


    2. assumption:

                   * at your workplace, you will always type code cleanly

                                 – as simple as possible,

                                 - perfectly organized,

                                 - maintaining a steady logical flow



   3. Clean code is a code that is

                  * focused

                  * and understandable,

                                - which means it must be

                                              • readable,

                                              • logical

                                              • and changeable.


    4. When assigning names to

                  * variables

                  * or SQL objects,

          always chose

                  * shorter,

                  * meaningful names,

                                - pronounceable, where one word per concept has been picked

                  * conveying specific information

          Names will constitute more than 80% of your code.


    5. Readability

                 * horizontal and vertical organization of code

                 * colour

                               - use ad-hoc software that re-organizes code and colours
                                    different words consistently

                               - use the relevant analogical tool provided in Workbench

                 * time is a factor

                 * unification of coding style is a top-priority

                               - it is unprofessional to merge code written in the same language

                               - but in a different style

                 * intervene manually and adjust your code as you like


    6. Comments

                * lines of text that Workbench will not run as code;

                * they convey a message to someone who reads our code

                *    /* …            (for large comments)

                    # or --          (for one-line comments)

 */

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
    Manualul 9.7: identificatorul nequoted permite ASCII 0-9, a-z, A-Z, underscore și dolar, plus caracterele din planul multilingv de bază; spațiul și cuvântul rezervat cer backtick. Numele de coloană nu distinge literele pe nicio platformă. Numele de tabelă depinde de lower_case_table_names, fixat la inițializarea serverului; pe Windows valoarea implicită 1 salvează numele cu litere mici. Comentariul #, -- urmat de spațiu sau de un caracter de control, și comentariul dintre slash-stea și stea-slash este tăiat și nu schimbă rândurile. -- fără spațiu nu este comentariu. Un comentariu slash-stea-exclamare este executat de MySQL; cu număr de versiune, doar dacă serverul este cel puțin acea versiune, iar pentru 9.7.4 numărul din comentariu este 90704. Comentariile cu număr de versiune nu rămân în corpul unei rutine stocate. Un comentariu slash-stea-plus este hint de optimizer, nu o notiță. Comentariile imbricate nu sunt suportate și sunt deprecate. PDF-ul cursului cere nume scurte, un cuvânt pe concept, purchase_number în loc de spațiu, și comentarii mari cu slash-stea sau o linie cu # ori --. Pe slide, PRIMARY KEY (purcase_number) nu numește coloana purchase_number, deci acea propoziție nu creează tabela. PDF-ul nu spune că un comentariu cu exclamare se execută și nu spune regula de literă a coloanei. Rearanjarea și culoarea din Workbench sunt clientul.
BOOK EXAMPLE:
    Coding_technique.readableSalesTable
REPO DEMO:
    src/main/java/_9_Coding_Technique/Coding_technique.java
BREAK LAB:
    CREATE TABLE sales (purchase number INT) este respins și nu lasă tabela. CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number)) nu găsește coloana.
TRADE-OFFS:
    Când numele trebuie să conțină spațiu sau un cuvânt rezervat, îl încadrezi în backtick. Când problema este planul, nu mai rescrii convenția: alegi un index sau un hint slash-stea-plus.
ANTI-PATTERNS:
    purchase number nequoted, -- fără spațiu, un hint slash-stea-plus crezut comentariu inert, sau PRIMARY KEY (purcase_number) pe coloana purchase_number.
STAFF QUESTIONS:
    CodingTechnique_Staff_Interview.q01 până la q50
NOVEL TRANSFER:
    Un registru de loturi de depozit, nu tabela sales: lot_code rămâne un identificator, iar lot code cu spațiu este respins la fel.
SPACED REVIEW:
    o zi, șapte zile, treizeci de zile

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE când echipa citește același CREATE și numele nu are spațiu sau cuvânt rezervat -> Coding_technique.readableSalesTable -> CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)) -> nu garantează un plan și nu inserează un rând
    2. Eșecul cel mai mic
           -> DECIDE când numele a fost scris cu spațiu, ca în slide-ul purchase number -> Coding_technique.spaceIsRejected -> CREATE TABLE sales (purchase number INT) -> nu garantează că tabela există; serverul respinge propoziția
    3. Granița
           -> identificatorul și comentariul sunt mecanismul pe care îl parsează mysqld; culoarea și rearanjarea din Workbench sunt clientul, nu propoziția
    4. Alternativa
           -> când problema este planul, nu cititorul, alegi indexul sau un hint slash-stea-plus, nu încă o rupere de rând
    5. Anti-patternul
           -> PRIMARY KEY (purcase_number) pe coloana purchase_number, sau -- fără spațiu, sau un hint crezut comentariu inert -> cheia nu găsește coloana, restul liniei nu e tăiat, sau optimizerul primește hint-ul
    6. Dovada
           -> Coding_technique.readableSalesTable ține propoziția; Coding_technique.spaceIsRejected ține eșecul; serverul nu a fost întrebat în tur
    7. Interviu
           -> convenția lasă tabela sales cu coloana purchase_number și zero rânduri; CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)); un comentariu simplu nu ține loc de constrângere

1. Enunț: Ce problemă rezolvă convenția de nume și de comentariu pe CREATE TABLE sales?
   Capcană: Un nume mai scurt face serverul să citească mai puține rânduri.
   Cheie: Convenția păstrează un identificator lizibil pentru om. readableSalesTable lasă schema cu sales și purchase_number și zero rânduri. Lungimea numelui nu șterge și nu sare peste rânduri.
   Demonstrație: Coding_technique.readableSalesTable
2. Enunț: Ce rămâne în tabelă după CREATE și după un comentariu -- pe aceeași propoziție?
   Capcană: Comentariul scrie un rând-notiță în sales.
   Cheie: commentLeavesNoRow arată un comentariu tăiat de server. Rezultatul nu capătă o coloană în plus, iar CREATE din readableSalesTable lasă zero rânduri. Cheia primară este constrângerea, nu scrierea cu underscore.
   Demonstrație: Coding_technique.commentLeavesNoRow
3. Enunț: De ce o rupere de rând nu este indexul sau hint-ul de optimizer?
   Capcană: Dacă așez propoziția pe mai multe linii, planul se schimbă la fel ca la un index.
   Cheie: hintIsNotLayout desparte comentariul simplu, pe care serverul îl aruncă, de hint-ul slash-stea-plus, pe care optimizerul îl primește. Planul nu se schimbă din spațiile dintre linii.
   Demonstrație: Coding_technique.hintIsNotLayout
4. Enunț: Cum eșuează purchase number scris fără backtick?
   Capcană: purchase number este aceeași convenție ca purchase_number, doar mai aerisită.
   Cheie: spaceIsRejected nu este o convenție. Spațiul nu face parte din identificatorul nequoted, serverul respinge propoziția și tabela sales nu apare. Backtick-ul este o altă propoziție, nu un sinonim al spațiului.
   Demonstrație: Coding_technique.spaceIsRejected
5. Enunț: Când lași convenția și alegi cheia sau un nume citat?
   Capcană: Regulile de unic și de cuvânt rezervat se scriu într-un comentariu, nu în propoziție.
   Cheie: keyNameMustBeTheColumn arată că un comentariu -- unic nu creează cheia, iar PRIMARY KEY (purcase_number) nu găsește coloana purchase_number. Numele din cheie trebuie să fie coloana. Un cuvânt rezervat se citează, nu se comentează.
   Demonstrație: Coding_technique.keyNameMustBeTheColumn
*/

/*
🟢 Începător
    Ce este            convenția care lasă tabela sales cu coloana purchase_number și fără rânduri
    De ce e nevoie     fără un identificator dintr-un singur token, CREATE nu lasă tabela
🟡 Producție
    Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
🔴 Staff
    Unde               parserul de identificatori și de comentarii, nu planul și nu lacătul de rând
Pași                   1. Formele de mai jos rămân propoziții gata de trimis
*/
public class Coding_technique {

    /*
    🟢 Începător
        Ce este            CREATE care lasă schema cu sales și zero rânduri
        De ce e nevoie     fără purchase_number nu există coloana pe care o citește echipa
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
    🔴 Staff
        Unde               numele coloanei nu distinge literele; convenția nu ține loc de index secundar
    Pași                   1. Întoarce propoziția lizibilă
    */
    // Începător: metoda ține comanda care creează tabela goală. Nu pune rânduri în sales.
    // Producție: readableSalesTable nu deschide o conexiune. Dacă semnătura dispare, dispare și exemplul citat pe card.
    // Expert: metoda nu este un index și nu se confundă cu EXPLAIN. Nu garantează litera cu care numele de tabelă stă pe disc.
    public static String readableSalesTable() {
        // Începător: comanda creează sales cu o coloană și fără rânduri.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)). Fără linia aceasta nu rămâne propoziția.
        // Expert: underscore nu este un index. Cheia primară este constrângerea de pe coloană; scrierea nu filtrează rânduri și nu ține lacăt după ce propoziția s-a terminat.
        return """
                CREATE TABLE sales (
                    purchase_number INT,
                    PRIMARY KEY (purchase_number)
                )
                """;
    }

    /*
    🟢 Începător
        Ce este            un comentariu pe care serverul îl taie, fără să adauge un rând
        De ce e nevoie     dacă notița ar fi date, rezultatul ar minți cititorul
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales -- comentariul nu este un rând
    🔴 Staff
        Unde               parserul aruncă -- doar dacă după liniuțe urmează spațiu sau un caracter de control
    Pași                   1. Întoarce SELECT-ul cu comentariul de linie
    */
    // Începător: metoda ține o citire. Comentariul nu adaugă un rând în sales.
    // Producție: commentLeavesNoRow nu trimite comanda. Fără metodă, nu ai dovada că notița stă în afara rezultatului.
    // Expert: -- cu spațiu nu este un hint slash-stea-plus și nu este WHERE. Nu garantează că un -- lipit de text rămâne comentariu.
    public static String commentLeavesNoRow() {
        // Începător: rămân valorile din purchase_number. Textul de după liniuțe nu este o coloană.
        // Producție: SELECT purchase_number FROM sales -- comentariul nu este un rând. Fără spațiul de după --, restul liniei nu mai este notiță.
        // Expert: comentariul nu ține lacăt și nu schimbă planul. Se desparte de un hint: aici nu există plus imediat după slash-stea.
        return """
                SELECT purchase_number
                FROM sales
                -- comentariul nu este un rând
                """;
    }

    /*
    🟢 Începător
        Ce este            diferența dintre o notiță aruncată și un hint pe care optimizerul îl primește
        De ce e nevoie     cine crede că orice slash-stea e inert poate schimba planul fără să vadă
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales cu notiță între slash-stea, și SELECT cu hint BKA(t1) slash-stea-plus
    🔴 Staff
        Unde               hint-ul stă în comentariul de optimizer; notița simplă nu ajunge la plan
    Pași                   1. Întoarce notița pe care serverul o taie
    */
    // Începător: prima comandă citește coloana și lasă notița pe dinafară.
    // Producție: plainComment este SELECT cu un comentariu între slash-stea și stea-slash. Dacă dispare, rămâne doar hint-ul.
    // Expert: notița nu este un index. Nu garantează același plan ca hint-ul de mai jos, pentru că hint-ul nu este aruncat.
    public static String plainComment() {
        // Începător: rezultatul este coloana purchase_number. Nota din mijloc nu este o valoare.
        // Producție: SELECT purchase_number /* notiță */ FROM sales. Fără comentariu, lista de coloane rămâne aceeași.
        // Expert: spre deosebire de /*+ BKA(t1) */, aici serverul taie textul. Linia nu cere un algoritm de join și nu ține lacăt.
        return """
                SELECT purchase_number /* notiță */ FROM sales
                """;
    }

    /*
    🟢 Începător
        Ce este            hint-ul primit de optimizer, lângă notița pe care serverul o aruncă
        De ce e nevoie     fără plus, aceleași slash-stea nu schimbă planul
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales, o dată cu notiță și o dată cu BKA(t1)
    🔴 Staff
        Unde               hint-ul nu este o rupere de rând și nu este un index pe sales
    Pași                   1. Ține notița
                           2. Întoarce hint-ul
    */
    // Începător: a doua comandă îi dă optimizerului un hint, nu o notiță pentru om.
    // Producție: hintIsNotLayout conține SELECT /*+ BKA(t1) */ purchase_number FROM sales. Dacă dispare, dispare deosebirea de notiță.
    // Expert: BKA nu este convenția de nume. Nu garantează un alt plan dacă hint-ul nu se aplică tabelei sales, care nu este t1.
    public static String hintIsNotLayout() {
        // Începător: notița din această comandă nu adaugă o coloană și nu cere un plan nou.
        // Producție: SELECT purchase_number /* notiță */ FROM sales. Dacă linia dispare, hint-ul rămâne fără perechea inertă.
        // Expert: notița nu este index. Spre deosebire de forma cu plus, serverul o taie înainte de optimizer.
        String plain = """
                SELECT purchase_number /* notiță */ FROM sales
                """;
        // Începător: tot purchase_number se citește. Plusul de după slash-stea nu este o notiță aruncată.
        // Producție: SELECT /*+ BKA(t1) */ purchase_number FROM sales. Fără plus, același text ar fi fost tăiat ca notiță.
        // Expert: hint-ul se confundă cu un comentariu de convenție. Nu este un index creat pe sales și nu garantează că planul chiar folosește BKA.
        String hint = """
                SELECT /*+ BKA(t1) */ purchase_number FROM sales
                """;
        // Începător: metoda predă hint-ul, nu notița, ca să se vadă forma care poate atinge planul.
        // Producție: return hint. Dacă dispare, propoziția cu BKA nu mai iese din metodă.
        // Expert: întoarcerea nu execută serverul și nu ia lacăt. plain rămâne în metodă ca deosebire, nu ca plan.
        return hint;
    }

    /*
    🟢 Începător
        Ce este            forma cu spațiu, pe care serverul o respinge, și forma citată, care e altă propoziție
        De ce e nevoie     spațiul nu lasă tabela sales deloc
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase number INT) față de CREATE TABLE sales (`purchase number` INT)
    🔴 Staff
        Unde               lista de caractere a identificatorului nequoted; backtick-ul este citarea, nu convenția
    Pași                   1. Întoarce forma cu spațiu, respinsă
    */
    // Începător: comanda cu spațiu nu creează sales. Nu rămâne nicio coloană.
    // Producție: spaceIsRejected este CREATE TABLE sales (purchase number INT). Dacă dispare, dispare eșecul cel mai mic.
    // Expert: spațiul nu este snake_case și nu este un index. Nu garantează un cod numeric de eroare, pentru că serverul nu a fost întrebat în tur.
    public static String spaceIsRejected() {
        // Începător: serverul nu creează tabela, pentru că numele e rupt în două.
        // Producție: CREATE TABLE sales (purchase number INT). Fără această propoziție rămâne doar forma care reușește.
        // Expert: tokenul nequoted nu acceptă spațiul. Nu este același lucru cu un comentariu și nu se repară prin EXPLAIN.
        return """
                CREATE TABLE sales (purchase number INT)
                """;
    }

    /*
    🟢 Începător
        Ce este            același înțeles de coloană, citat, ca să spațiul fie un singur nume
        De ce e nevoie     fără backtick serverul respinge purchase number și sales nu apare
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (`purchase number` INT)
    🔴 Staff
        Unde               citarea este alt identificator decât purchase_number; nu este convenția scurtă
    Pași                   1. Întoarce numele citat
    */
    // Începător: backtick-ul face din cele două cuvinte un singur nume de coloană. Tot nu inserează rânduri.
    // Producție: quotedName este CREATE TABLE sales (`purchase number` INT). Dacă dispare, spațiul pare interzis și când este citat.
    // Expert: citarea nu este convenția purchase_number. Nu garantează portabilitatea numelui de tabelă între Windows și Unix.
    public static String quotedName() {
        // Începător: tabela sales apare cu o coloană al cărei nume conține spațiu. Rânduri nu apar.
        // Producție: CREATE TABLE sales (`purchase number` INT). Fără backtick, aceeași propoziție e respinsă.
        // Expert: numele citat se desparte de purchase_number. Coloana tot nu distinge literele; backtick-ul nu creează un index secundar.
        return """
                CREATE TABLE sales (`purchase number` INT)
                """;
    }

    /*
    🟢 Începător
        Ce este            o cheie al cărei nume nu este coloana, și o notiță care nu ține loc de cheie
        De ce e nevoie     altfel tabela fie nu apare, fie apare fără constrângere
    🟡 Producție
        Cum se scrie       PRIMARY KEY (purcase_number) față de PRIMARY KEY (purchase_number)
    🔴 Staff
        Unde               numele din cheie trebuie să fie un identificator de coloană deja scris în CREATE
    Pași                   1. Întoarce cheia care nu numește coloana
    */
    // Începător: cheia caută o coloană care nu este în tabelă, deci sales nu rămâne creată.
    // Producție: missedKeyColumn este CREATE cu PRIMARY KEY (purcase_number). Dacă dispare, rămâne doar typo-ul din slide nearătat.
    // Expert: purcase_number nu este o convenție de litere. Coloanele nu disting literele, dar litera în plus din mijlocul cuvântului este alt identificator.
    public static String missedKeyColumn() {
        // Începător: serverul nu găsește coloana din cheie. Tabela nu rămâne.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number)). Fără purcase, cheia ar vedea coloana.
        // Expert: eșecul nu este un plan rău și nu este un lacăt. Se desparte de lower_case_table_names, care pliază literele, nu repară un cuvânt scris greșit.
        return """
                CREATE TABLE sales (
                    purchase_number INT,
                    PRIMARY KEY (purcase_number)
                )
                """;
    }

    /*
    🟢 Începător
        Ce este            o notiță scrisă în locul cheii, care nu oprește duplicatele
        De ce e nevoie     fără PRIMARY KEY coloana purchase_number primește același număr de două ori
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT -- unic)
    🔴 Staff
        Unde               comentariul este tăiat; constrângerea ar fi rămas în schemă
    Pași                   1. Întoarce notița care nu constrânge
    */
    // Începător: notița „unic” nu oprește un al doilea purchase_number. Tabela, dacă apare, nu are cheie.
    // Producție: commentIsNotAKey este CREATE TABLE sales (purchase_number INT -- unic). Dacă dispare, pare că un comentariu constrânge.
    // Expert: -- unic nu este PRIMARY KEY și nu este un index. Nu garantează unicitatea și nu ține un lacăt pe un rând care nici nu există.
    public static String commentIsNotAKey() {
        // Începător: coloana purchase_number rămâne fără cheie. Comentariul nu oprește duplicatele.
        // Producție: CREATE TABLE sales (purchase_number INT -- unic). Fără comentariu, schema este aceeași: tot fără cheie.
        // Expert: notița se confundă cu o constrângere. Nu este FOREIGN KEY și nu filtrează rândurile la INSERT.
        return """
                CREATE TABLE sales (
                    purchase_number INT
                    -- unic
                )
                """;
    }

    /*
    🟢 Începător
        Ce este            cheia pusă pe coloana care există, nu pe un cuvânt scris greșit și nu pe o notiță
        De ce e nevoie     altfel sales fie nu apare, fie apare fără să refuze duplicatul
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number))
    🔴 Staff
        Unde               numele din PRIMARY KEY trebuie să fie identificatorul coloanei, nu convenția de comentariu
    Pași                   1. Ține cheia scrisă greșit
                           2. Ține notița
                           3. Întoarce cheia pe coloană
    */
    // Începător: cheia stă pe coloana care chiar există. Tabela rămâne goală, dar un al doilea același număr este refuzat mai târziu, la scriere.
    // Producție: keyNameMustBeTheColumn este CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)). Dacă dispare, rămâne doar eșecul.
    // Expert: cheia nu vine din snake_case. Nu garantează un plan de citire și nu ține lacătul după ce CREATE s-a terminat.
    public static String keyNameMustBeTheColumn() {
        // Începător: această formă nu lasă tabela, pentru că cheia caută o coloană care nu e scrisă.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purcase_number)). Dacă dispare, metoda nu mai arată eșecul din slide.
        // Expert: purcase_number nu este plierea de litere. lower_case_table_names nu repară o literă în plus.
        String missed = """
                CREATE TABLE sales (
                    purchase_number INT,
                    PRIMARY KEY (purcase_number)
                )
                """;
        // Începător: notița nu oprește un număr repetat. Tabela ar avea coloana, fără cheie.
        // Producție: CREATE TABLE sales (purchase_number INT -- unic). Dacă dispare, rămâne nearătat că un comentariu nu constrânge.
        // Expert: -- unic este tăiat. Nu este UNIQUE și nu este un index secundar.
        String note = """
                CREATE TABLE sales (
                    purchase_number INT
                    -- unic
                )
                """;
        // Începător: variabila missed doar păstrează eșecul lângă forma aleasă. Nu creează a doua tabelă.
        // Producție: folosirea lui missed ține propoziția greșită în metodă. Dacă dispare, compilatorul poate elimina textul.
        // Expert: citirea variabilei nu trimite SQL și nu ia lacăt. note rămâne din același motiv: dovadă, nu execuție.
        int kept = missed.length() + note.length();
        // Începător: sales rămâne cu o coloană și cu cheia pe ea. Niciun rând nu este inserat.
        // Producție: CREATE TABLE sales (purchase_number INT, PRIMARY KEY (purchase_number)). Numele din cheie este numele coloanei.
        // Expert: aceasta este constrângerea, nu convenția de comentariu. Un hint nu o înlocuiește, iar comentariul -- unic nu o creează.
        return """
                CREATE TABLE sales (
                    purchase_number INT,
                    PRIMARY KEY (purchase_number)
                )
                """;
    }
}
