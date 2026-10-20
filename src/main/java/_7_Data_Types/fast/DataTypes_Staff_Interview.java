/*
TOPIC:
    cincizeci de întrebări despre ce rămâne în example: scara, intervalul, lățimea deprecată și modul strict.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști propoziția care taie, respinge sau păstrează valoarea, și să nu o confunzi cu DOUBLE, cu INT(4) sau cu un CHECK.
MECHANISM:
    Fiecare metodă ține propoziția. Serverul o convertește la atribuire, dacă o primește. În tur nu a primit-o.
LOOKALIKES:
    DOUBLE este aproximarea. INT(4) este lățimea deprecată. CHECK este predicatul. Niciunul nu este scara lui DECIMAL(5, 2).
BOOK THEORY:
    Manualul 9.7, 13.8, 13.1.3, 13.1.4, 13.1.6 și 13.1.7, deschise în tur. PDF-urile de curs nu descriu 1264 și nu descriu lățimea deprecată.
BOOK EXAMPLE:
    q01DecimalScale
REPO DEMO:
    src/main/java/_7_Data_Types/fast/DataTypes_Fast.java
BREAK LAB:
    q03Error1264 ține TRADITIONAL și 256. Nu a fost trimis.
TRADE-OFFS:
    q41LeastStorage pentru 1..99999. q42SpeedOverExact pentru aproximare. q43CentsInBigint când punctul rămâne în aplicație. q46KeepScale3 când a treia cifră rămâne.
ANTI-PATTERNS:
    q33Silent255 lasă 255. q32Int4DoesNotReject nu respinge 10000.
STAFF QUESTIONS:
    q01DecimalScale până la q50ExplainIsNotTheStoredValue
NOVEL TRANSFER:
    Eticheta unei farmacii nu folosește tabela example: cutiile, prețul și doza au scări diferite.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei alegeri de tip (data type):
       Tipul coloanei decide ce valoare rămâne în celulă la sfârșitul lui INSERT și ce cifră nu rămâne.

Exemplu:
       DataTypes_Terms.explainDataType()
       id 1 rămâne. A treia zecimală din 10.235 nu rămâne într-un DECIMAL(5, 2).

Definiția unei coloane (column):
       Coloana este locul din rând care primește tipul. Fără coloană, valoarea nu are unde să fie tăiată.

Exemplu:
       DataTypes_Terms.explainColumn()
       price DECIMAL(5, 2) este coloana care primește prețul.

Definiția unei precizii (precision):
       Precizia este numărul de cifre semnificative pe care coloana le poate ține.

Exemplu:
       DataTypes_Terms.explainPrecision()
       DECIMAL(5, 2) are precizia 5.

Definiția unei scări (scale):
       Scara este numărul de cifre de după punct pe care coloana le poate ține.

Exemplu:
       DataTypes_Terms.explainScale()
       Scara 2 nu ține a treia cifră din 10.235.

Definiția unei lățimi de afișare (display width):
       Lățimea de afișare este un atribut deprecat al întregului. Nu este intervalul de valori stocate.

Exemplu:
       DataTypes_Terms.explainDisplayWidth()
       INT(4) nu respinge 10000.

Definiția unei respingeri stricte (strict SQL mode):
       Modul strict face ca valoarea din afara intervalului să eșueze INSERT-ul, în loc să taie la capăt.

Exemplu:
       DataTypes_Terms.explainStrictMode()
       TRADITIONAL și 256 într-un TINYINT UNSIGNED nu lasă rândul.
*/
// Începător: banca stă lângă lecția care alege tipul lui example.
// Producție: package _7_Data_Types.fast. Dacă linia dispare, q01DecimalScale nu mai este în pachetul fast.
// Expert: Pachetul nu convertește 10.235 și nu ține loc de folderul CHAR sau de folderul CHECK.
package _7_Data_Types.fast;

/*
🟢 Începător
    Ce este            cincizeci de propoziții despre ce rămâne în id și în price
    De ce e nevoie     fără ele, răspunsul de interviu poate fi lipit pe un join
🟡 Producție
    Cum se scrie       metodele q01DecimalScale până la q50ExplainIsNotTheStoredValue
🔴 Staff
    Unde               fiecare metodă înapoiază textul. Nu există main. Serverul nu l-a primit în tur.
Pași
    1. Recunoaște propoziția.
    2. Spune ce rămâne în rând.
    3. Nu trimite textul.
*/
// Începător: clasa ține banca, nu un INSERT executat.
// Producție: public final class DataTypes_Staff_Interview. Dacă dispare, cardul din Data_Types.java nu mai are metodele.
// Expert: Nu este un main de recall. Nu predă fiecare copil: CHAR, ENUM, DATE și JSON rămân folderele lor.
public final class DataTypes_Staff_Interview {
    /*
    🟢 Începător
        Ce este            recunoașterea scării exacte pe price
        De ce e nevoie     fără ea, DECIMAL(5, 2) pare o lățime de afișare
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price DECIMAL(5, 2))
    🔴 Staff
        Unde               13.1.3: 5 este precizia, 2 este scara, intervalul este -999.99..999.99
    Pași
        1. Recunoaște DECIMAL, nu INT(M).
    */
    // Începător: Propoziția declară prețul exact, nu un întreg cu paranteză.
    // Producție: public static String q01DecimalScale(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este INT(4) și nu este DOUBLE.
    public static String q01DecimalScale() {
        // Începător: price ține un număr exact, cu două cifre după punct.
        // Producție: CREATE TABLE example (price DECIMAL(5, 2)). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Paranteza de la DECIMAL este precizie și scară. La INT, paranteza este lățime deprecată și nu taie intervalul.
        return "CREATE TABLE example (price DECIMAL(5, 2))";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q01DecimalScale. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu stochează 10.235 și nu citește scara de pe server.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea intervalului cantității mici
        De ce e nevoie     fără ea, TINYINT UNSIGNED pare că ține orice întreg pozitiv
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED)
    🔴 Staff
        Unde               13.1.7: capătul stocat fără mod strict este 255, nu un număr mai mare
    Pași
        1. Recunoaște tipul lui id din example.
    */
    // Începător: id nu este un INT lat și nu este un șir.
    // Producție: public static String q02TinyintUnsigned(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Capătul 255 nu este lățimea 4 și nu este scara lui price.
    public static String q02TinyintUnsigned() {
        // Începător: Cantitatea nu poate trece de capătul tipului.
        // Producție: CREATE TABLE example (id TINYINT UNSIGNED). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: UNSIGNED mută intervalul la valori nenegative. Nu creează un index și nu este AUTO_INCREMENT.
        return "CREATE TABLE example (id TINYINT UNSIGNED)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q02TinyintUnsigned. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu inserează 256 și nu deschide un lacăt.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea erorii care oprește rândul
        De ce e nevoie     fără ea, 1264 pare un avertisment care tot salvează 256
    🟡 Producție
        Cum se scrie       SET sql_mode = 'TRADITIONAL'; INSERT INTO example (id, price) VALUES (256, 10.23)
    🔴 Staff
        Unde               13.1.7: cu TRADITIONAL, ERROR 1264 (22003) și INSERT-ul eșuează
    Pași
        1. Leagă 256 de modul strict.
    */
    // Începător: Textul este eșecul, nu valoarea rămasă.
    // Producție: public static String q03Error1264(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Coloana din manual este i1. În example este id. Mesajul din tur este NERULAT.
    public static String q03Error1264() {
        // Începător: 256 nu ajunge în celulă cât timp modul strict stă pornit.
        // Producție: SET sql_mode = 'TRADITIONAL'; INSERT INTO example (id, price) VALUES (256, 10.23). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Același cod 1264, fără mod strict, este Warning și lasă 255. Nu confunda cele două.
        return "SET sql_mode = 'TRADITIONAL'; INSERT INTO example (id, price) VALUES (256, 10.23)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q03Error1264. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu a primit Access denied în loc de 1264: textul nu a fost trimis.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea parantezei care nu este interval
        De ce e nevoie     fără ea, INT(4) pare că respinge 10000
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id INT(4))
    🔴 Staff
        Unde               13.1.6: lățimea nu constrânge intervalul și este deprecată, nu scoasă
    Pași
        1. Desparte paranteza de precizia lui DECIMAL.
    */
    // Începător: Patru nu este numărul maxim de cifre stocate.
    // Producție: public static String q04DisplayWidth(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este TINYINT UNSIGNED, al cărui capăt este 255.
    public static String q04DisplayWidth() {
        // Începător: id poate ține valori mai late decât 4.
        // Producție: CREATE TABLE example (id INT(4)). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Manualul spune că folosirea lățimii la afișare ține de aplicație. Serverul nu taie 10000.
        return "CREATE TABLE example (id INT(4))";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q04DisplayWidth. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu activează ZEROFILL și nu umple cu spații.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea tipului aproximativ
        De ce e nevoie     fără ea, DOUBLE pare un DECIMAL mai rapid cu aceeași scară
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price DOUBLE)
    🔴 Staff
        Unde               13.1.4 și 13.8: DOUBLE este aproximativ și se alege când viteza bate exactitatea
    Pași
        1. Recunoaște vecinul prețului exact.
    */
    // Începător: price nu mai are scară declarată.
    // Producție: public static String q05DoubleApprox(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: FLOAT(M,D) este deprecat. Această propoziție nu folosește (M,D).
    public static String q05DoubleApprox() {
        // Începător: Prețul nu mai este un număr exact.
        // Producție: CREATE TABLE example (price DOUBLE). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu garantează că 10.235 se citește înapoi identic. DECIMAL(5, 2) este tipul care taie scara, nu acesta.
        return "CREATE TABLE example (price DOUBLE)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q05DoubleApprox. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu convertește un price deja stocat ca DECIMAL.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea modului care respinge capătul
        De ce e nevoie     fără el, TRADITIONAL pare un stil de scriere, nu o regulă de stocare
    🟡 Producție
        Cum se scrie       SET sql_mode = 'TRADITIONAL'
    🔴 Staff
        Unde               13.1.7: TRADITIONAL este exemplul de mod strict în care 256 eșuează
    Pași
        1. Pune modul înainte de INSERT.
    */
    // Începător: Modul schimbă soarta lui 256, nu numele coloanei.
    // Producție: public static String q06TraditionalMode(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este NO_UNSIGNED_SUBTRACTION. Acela schimbă semnul unei scăderi, nu INSERT-ul în TINYINT.
    public static String q06TraditionalMode() {
        // Începător: Sesiunea, dacă ar primi SET, nu ar mai tăia 256 la 255.
        // Producție: SET sql_mode = 'TRADITIONAL'. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: SET nu inserează rândul. Fără INSERT-ul următor, example rămâne neatins.
        return "SET sql_mode = 'TRADITIONAL'";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q06TraditionalMode. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu lasă modul pe server: propoziția nu a plecat.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea modului în care tăierea este permisă
        De ce e nevoie     fără el, lipsa modului pare că lasă 256 intact
    🟡 Producție
        Cum se scrie       SET sql_mode = ''
    🔴 Staff
        Unde               13.1.7: sql_mode gol, Warning 1264, capătul stocat
    Pași
        1. Recunoaște opusul lui TRADITIONAL.
    */
    // Începător: Golul nu înseamnă «păstrează ce am tastat».
    // Producție: public static String q07EmptyMode(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este un ROLLBACK. Rândul cu valoarea tăiată rămâne.
    public static String q07EmptyMode() {
        // Începător: Următorul INSERT poate tăia fără să eșueze.
        // Producție: SET sql_mode = ''. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Manualul arată apoi 127 și 255 pentru cele două TINYINT. Aici id este UNSIGNED, deci capătul analog este 255.
        return "SET sql_mode = ''";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q07EmptyMode. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu execută INSERT-ul. Doar pregătește tăierea.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea umplerii deprecate
        De ce e nevoie     fără ea, ZEROFILL pare că lățește intervalul
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id INT(4) ZEROFILL)
    🔴 Staff
        Unde               13.1.6: ZEROFILL este deprecat, adaugă UNSIGNED și nu ține în expresii sau UNION
    Pași
        1. Nu îl folosi ca validare.
    */
    // Începător: Zerourile nu sunt o scară.
    // Producție: public static String q08Zerofill(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: În tabele temporare, manualul spune că ZEROFILL presupune că valoarea încape în lățime.
    public static String q08Zerofill() {
        // Începător: 5 poate fi afișat 0005, dar 10000 nu este respins de lățimea 4.
        // Producție: CREATE TABLE example (id INT(4) ZEROFILL). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Alternativa deschisă în manual este LPAD sau un CHAR. Nu este DECIMAL.
        return "CREATE TABLE example (id INT(4) ZEROFILL)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q08Zerofill. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu umple id-ul deja stocat.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea sinonimului exact
        De ce e nevoie     fără ea, NUMERIC pare un al treilea fel de număr, între DECIMAL și DOUBLE
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price NUMERIC(5, 2))
    🔴 Staff
        Unde               13.1.3: în MySQL, NUMERIC este implementat ca DECIMAL
    Pași
        1. Tratează NUMERIC(5, 2) ca scara exactă.
    */
    // Începător: Același preț, alt cuvânt.
    // Producție: public static String q09NumericIsDecimal(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este FLOAT(p). p de acolo alege octeții, nu scara.
    public static String q09NumericIsDecimal() {
        // Începător: price NUMERIC(5, 2) taie la fel ca DECIMAL(5, 2).
        // Producție: CREATE TABLE example (price NUMERIC(5, 2)). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu garantează o stocare diferită de DECIMAL. Manualul spune că observațiile despre DECIMAL se aplică și aici.
        return "CREATE TABLE example (price NUMERIC(5, 2))";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q09NumericIsDecimal. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu creează o a doua coloană price.
    }
    /*
    🟢 Începător
        Ce este            recunoașterea tipului cel mai mic care ține 1..99999
        De ce e nevoie     fără ea, TINYINT UNSIGNED pare destul pentru 99999
    🟡 Producție
        Cum se scrie       CREATE TABLE example (qty MEDIUMINT UNSIGNED)
    🔴 Staff
        Unde               13.8: MEDIUMINT UNSIGNED este tipul precis cu stocarea cea mai mică pentru 1..99999
    Pași
        1. Alege după interval, nu după un nume scurt.
    */
    // Începător: 99999 nu încape în capătul 255.
    // Producție: public static String q10MediumintRange(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este un index pe qty și nu este AUTO_INCREMENT.
    public static String q10MediumintRange() {
        // Începător: qty poate ține 99999 fără să schimbe scara unui preț.
        // Producție: CREATE TABLE example (qty MEDIUMINT UNSIGNED). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Manualul nu spune să pui DOUBLE aici. Intervalul este întreg.
        return "CREATE TABLE example (qty MEDIUMINT UNSIGNED)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q10MediumintRange. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu umple qty cu 99999.
    }
    /*
    🟢 Începător
        Ce este            momentul în care 10.235 își pierde locul în scară
        De ce e nevoie     fără el, pierderea pare să apară la COMMIT
    🟡 Producție
        Cum se scrie       INSERT INTO example (id, price) VALUES (1, 10.235)
    🔴 Staff
        Unde               atribuirea este la sfârșitul lui INSERT, nu la un COMMIT ulterior și nu într-un EXPLAIN
    Pași
        1. Uită-te la VALUES, nu la plan.
    */
    // Începător: Rândul primește valoarea convertită când INSERT se termină.
    // Producție: public static String q11InsertAssigns(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Tipul nu ține un lacăt după propoziție. Lacătul, dacă există, este al INSERT-ului, nu al scării.
    public static String q11InsertAssigns() {
        // Începător: 1 rămâne. A treia zecimală nu rămâne în DECIMAL(5, 2).
        // Producție: INSERT INTO example (id, price) VALUES (1, 10.235). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: 10.235 este în -999.99..999.99, deci nu este 1264. Este conversie de scară. Cifra rămasă nu a fost citită în tur.
        return "INSERT INTO example (id, price) VALUES (1, 10.235)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q11InsertAssigns. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu face COMMIT și nu deschide tranzacția.
    }
    /*
    🟢 Începător
        Ce este            rândul care nu apare după 256 în mod strict
        De ce e nevoie     fără el, eroarea pare să lase totuși o celulă cu 256
    🟡 Producție
        Cum se scrie       SET sql_mode = 'TRADITIONAL'; INSERT INTO example (id, price) VALUES (256, 10.23); SELECT id, price FROM example
    🔴 Staff
        Unde               13.1.7: după eșec, SELECT arată mulțimea goală când acesta era singurul INSERT
    Pași
        1. Verifică absența rândului, nu doar codul.
    */
    // Începător: example nu primește linia respinsă.
    // Producție: public static String q12RowMissing(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este DELETE. Rândul nu a existat.
    public static String q12RowMissing() {
        // Începător: Nu rămâne un id 256 de șters mai târziu.
        // Producție: SET sql_mode = 'TRADITIONAL'; INSERT INTO example (id, price) VALUES (256, 10.23); SELECT id, price FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Manualul arată Empty set pentru t1. Tabela din propoziție este example. Rezultatul în tur este NERULAT.
        return "SET sql_mode = 'TRADITIONAL'; INSERT INTO example (id, price) VALUES (256, 10.23); SELECT id, price FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q12RowMissing. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu șterge rânduri vechi. Doar descrie un INSERT eșuat.
    }
    /*
    🟢 Începător
        Ce este            rândul care rămâne cu capătul, nu cu 256
        De ce e nevoie     fără el, Warning 1264 pare inofensiv
    🟡 Producție
        Cum se scrie       SET sql_mode = ''; INSERT INTO example (id, price) VALUES (256, 10.23)
    🔴 Staff
        Unde               13.1.7: fără mod restrictiv, TINYINT UNSIGNED stochează 255
    Pași
        1. Citește celula, nu valoarea din VALUES.
    */
    // Începător: Rândul există și este greșit.
    // Producție: public static String q13ClipTo255(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este o tăiere de scară. 256 este în afara intervalului, nu peste scară.
    public static String q13ClipTo255() {
        // Începător: id devine 255. price 10.23 încape în DECIMAL(5, 2).
        // Producție: SET sql_mode = ''; INSERT INTO example (id, price) VALUES (256, 10.23). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Manualul arată 255 pe coloana unsigned. Nu garantează warning-ul în tur.
        return "SET sql_mode = ''; INSERT INTO example (id, price) VALUES (256, 10.23)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q13ClipTo255. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu pune înapoi 256. Ca să nu rămână 255, îți trebuie modul strict înainte de INSERT.
    }
    /*
    🟢 Începător
        Ce este            tipul nu adaugă un index pe price
        De ce e nevoie     fără deosebire, DECIMAL pare că indexează prețul
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2))
    🔴 Staff
        Unde               declarația nu are KEY. Un index este alt obiect, în folderul lui
    Pași
        1. Caută KEY și nu îl găsi.
    */
    // Începător: Rândul se stochează și fără index.
    // Producție: public static String q14TypeIsNotIndex(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu grăbește căutarea și nu o promite.
    public static String q14TypeIsNotIndex() {
        // Începător: example are două coloane și niciun index declarat.
        // Producție: CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2)). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Lipsa lui KEY nu înseamnă că InnoDB nu are un index grupat dacă apare o cheie primară mai târziu. Această propoziție nu o declară.
        return "CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2))";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q14TypeIsNotIndex. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu creează un index secundar și nu ține un lacăt.
    }
    /*
    🟢 Începător
        Ce este            conversia nu așteaptă COMMIT
        De ce e nevoie     fără ea, a treia zecimală pare să cadă doar când tranzacția se închide
    🟡 Producție
        Cum se scrie       START TRANSACTION; INSERT INTO example (id, price) VALUES (1, 10.235)
    🔴 Staff
        Unde               valoarea este convertită la atribuire. COMMIT face durabil ce este deja convertit
    Pași
        1. Desparte atribuirea de durabilitate.
    */
    // Începător: Sesiunea vede valoarea convertită înainte de COMMIT.
    // Producție: public static String q15NotAtCommit(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este un lacăt ținut de tip. Nu garantează izolarea: acest folder nu o predă.
    public static String q15NotAtCommit() {
        // Începător: INSERT-ul din tranzacție tot taie scara.
        // Producție: START TRANSACTION; INSERT INTO example (id, price) VALUES (1, 10.235). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Dacă autocommit este 1 și nu ai deschis tranzacția, INSERT-ul este deja durabil la sfârșit. START TRANSACTION schimbă asta. Nu am citit autocommit în tur.
        return "START TRANSACTION; INSERT INTO example (id, price) VALUES (1, 10.235)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q15NotAtCommit. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu face COMMIT și nu anulează tăierea de scară.
    }
    /*
    🟢 Începător
        Ce este            tipul nu ține un lacăt după ce propoziția s-a terminat
        De ce e nevoie     fără ea, DECIMAL pare că blochează price până la deconectare
    🟡 Producție
        Cum se scrie       SELECT id, price FROM example
    🔴 Staff
        Unde               scara este o regulă de atribuire, nu un lacăt de rând
    Pași
        1. Nu căuta un lacăt în tip.
    */
    // Începător: După INSERT, tipul nu mai ține nimic.
    // Producție: public static String q16TypeIsNotALock(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Un lacăt de rând, dacă INSERT-ul l-a luat, nu este acest mecanism.
    public static String q16TypeIsNotALock() {
        // Începător: SELECT citește price și nu prelungește o rezervă a tipului.
        // Producție: SELECT id, price FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Propoziția nu este LOCK TABLES. Nu garantează o citire consistentă: nu am deschis pagina de izolare în tur.
        return "SELECT id, price FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q16TypeIsNotALock. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu eliberează un lacăt, pentru că tipul nu a luat unul.
    }
    /*
    🟢 Începător
        Ce este            SELECT arată ce a rămas, nu textul din VALUES
        De ce e nevoie     fără el, clientul crede că 10.235 este încă în celulă
    🟡 Producție
        Cum se scrie       SELECT id, price FROM example
    🔴 Staff
        Unde               citirea vine după atribuire. Planul nu este valoarea
    Pași
        1. Citește coloana, nu literalul.
    */
    // Începător: Rezultatul este valoarea stocată.
    // Producție: public static String q17SelectShowsStored(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu repară scara și nu o urcă la 3.
    public static String q17SelectShowsStored() {
        // Începător: id și price ies cum au fost stocate.
        // Producție: SELECT id, price FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Dacă INSERT-ul nu a rulat, SELECT nu dovedește trunchierea. În tur nu a rulat.
        return "SELECT id, price FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q17SelectShowsStored. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu este EXPLAIN și nu schimbă tipul.
    }
    /*
    🟢 Începător
        Ce este            ALTER schimbă tipul rândurilor deja stocate, nu este INSERT-ul
        De ce e nevoie     fără deosebire, modificarea coloanei pare o atribuire ca VALUES
    🟡 Producție
        Cum se scrie       ALTER TABLE example MODIFY price DECIMAL(5, 2)
    🔴 Staff
        Unde               MODIFY price DECIMAL(5, 2) se aplică coloanei. Momentul nu este un singur VALUES
    Pași
        1. Nu amesteca schema cu un rând nou.
    */
    // Începător: Rândurile vechi trec prin noul tip.
    // Producție: public static String q18AlterIsLater(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este un index nou doar pentru că scara s-a schimbat.
    public static String q18AlterIsLater() {
        // Începător: Un price cu trei zecimale își pierde cifra dacă scara coboară la 2.
        // Producție: ALTER TABLE example MODIFY price DECIMAL(5, 2). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu garantează că ALTER eșuează sau reușește în tur. Nu este chooseType.
        return "ALTER TABLE example MODIFY price DECIMAL(5, 2)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q18AlterIsLater. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu inserează 10.235. Doar descrie schimbarea coloanei.
    }
    /*
    🟢 Începător
        Ce este            depășirea din expresie nu este tăierea unei coloane
        De ce e nevoie     fără ea, 1690 pare același lucru cu 1264
    🟡 Producție
        Cum se scrie       SELECT 9223372036854775807 + 1
    🔴 Staff
        Unde               13.1.7: 9223372036854775807 + 1 dă ERROR 1690, BIGINT în afara intervalului, fără un INSERT
    Pași
        1. Nu căuta un rând în example.
    */
    // Începător: Nicio celulă nu se taie, pentru că nu există coloană.
    // Producție: public static String q19ExprOverflow(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este clip la capătul unui TINYINT.
    public static String q19ExprOverflow() {
        // Începător: Expresia eșuează înainte de un tabel.
        // Producție: SELECT 9223372036854775807 + 1. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: CAST la UNSIGNED sau un literal cu punct, cum arată manualul, schimbă rezultatul. Această propoziție nu o face.
        return "SELECT 9223372036854775807 + 1";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q19ExprOverflow. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu scrie în example.
    }
    /*
    🟢 Începător
        Ce este            scăderea fără semn nu este un INSERT tăiat
        De ce e nevoie     fără ea, rezultatul negativ pare că se stochează în TINYINT UNSIGNED
    🟡 Producție
        Cum se scrie       SELECT CAST(0 AS UNSIGNED) - 1
    🔴 Staff
        Unde               13.1.7: CAST(0 AS UNSIGNED) - 1 dă ERROR 1690 dacă NO_UNSIGNED_SUBTRACTION lipsește
    Pași
        1. Desparte expresia de coloană.
    */
    // Începător: Nu se scrie un rând.
    // Producție: public static String q20UnsignedMinus(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Cu NO_UNSIGNED_SUBTRACTION, manualul arată -1. Apoi, dacă valoarea intră într-un UNSIGNED, modul strict respinge, iar fără el taie.
    public static String q20UnsignedMinus() {
        // Începător: Expresia nu este id-ul din example.
        // Producție: SELECT CAST(0 AS UNSIGNED) - 1. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu seta modul în această propoziție. q06 este SET-ul. Aici vezi doar scăderea.
        return "SELECT CAST(0 AS UNSIGNED) - 1";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q20UnsignedMinus. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu actualizează id.
    }
    /*
    🟢 Începător
        Ce este            DECIMAL nu este DOUBLE
        De ce e nevoie     fără deosebire, alegi aproximarea pentru un preț
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED, price DOUBLE)
    🔴 Staff
        Unde               13.8: DOUBLE doar când exactitatea nu e critică sau viteza e prioritară
    Pași
        1. Păstrează scara pentru price.
    */
    // Începător: Vecinul nu ține 10.235 ca număr exact.
    // Producție: public static String q21NotDouble(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este o lățime și nu este un CHECK.
    public static String q21NotDouble() {
        // Începător: id rămâne TINYINT UNSIGNED. Doar price se schimbă.
        // Producție: CREATE TABLE example (id TINYINT UNSIGNED, price DOUBLE). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu promite aceeași citire ca DECIMAL(5, 2).
        return "CREATE TABLE example (id TINYINT UNSIGNED, price DOUBLE)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q21NotDouble. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu migrează un DECIMAL existent.
    }
    /*
    🟢 Începător
        Ce este            CHECK nu taie scara
        De ce e nevoie     fără deosebire, predicatul pare că rotunjește price
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price DECIMAL(5, 2), CONSTRAINT price_cap CHECK (price <= 10.00))
    🔴 Staff
        Unde               CHECK refuză rândul care nu trece predicatul. DECIMAL convertește scara și poate lăsa rândul
    Pași
        1. Desparte constrângerea de tip.
    */
    // Începător: 10.50 încape în DECIMAL(5, 2) și pică pe CHECK (price <= 10).
    // Producție: public static String q22NotCheck(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Invers, CHECK nu transformă 10.235 în două zecimale.
    public static String q22NotCheck() {
        // Începător: Predicatul este o regulă în plus, nu o scară.
        // Producție: CREATE TABLE example (price DECIMAL(5, 2), CONSTRAINT price_cap CHECK (price <= 10.00)). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu este FOREIGN KEY și nu este index. Folderul de constrângeri rămâne acolo.
        return "CREATE TABLE example (price DECIMAL(5, 2), CONSTRAINT price_cap CHECK (price <= 10.00))";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q22NotCheck. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu evaluează 10.50: textul nu a fost trimis.
    }
    /*
    🟢 Începător
        Ce este            CAST în SELECT nu schimbă tipul stocat
        De ce e nevoie     fără deosebire, rezultatul aproximat pare scris înapoi în price
    🟡 Producție
        Cum se scrie       SELECT CAST(price AS DOUBLE) FROM example
    🔴 Staff
        Unde               CAST(price AS DOUBLE) trăiește în rezultat. Coloana rămâne DECIMAL
    Pași
        1. Nu înlocui ALTER cu un CAST.
    */
    // Începător: Celula nu își schimbă scara.
    // Producție: public static String q23NotCast(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este INSERT și nu taie rândurile vechi.
    public static String q23NotCast() {
        // Începător: Clientul vede o aproximare. example păstrează exactul.
        // Producție: SELECT CAST(price AS DOUBLE) FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu garantează un rând dacă example e goală. Nu este notDouble, care schimbă declarația.
        return "SELECT CAST(price AS DOUBLE) FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q23NotCast. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu face UPDATE price.
    }
    /*
    🟢 Începător
        Ce este            INT(4) nu este TINYINT UNSIGNED
        De ce e nevoie     fără deosebire, paranteza 4 pare capătul 255
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id INT(4)); INSERT INTO example (id) VALUES (256)
    🔴 Staff
        Unde               10000 încape în INT și nu încape în TINYINT UNSIGNED
    Pași
        1. Alege intervalul, nu lățimea.
    */
    // Începător: 256 într-un INT nu dă 1264 din cauza parantezei.
    // Producție: public static String q24NotTinyint(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: 1264 pentru 256 ține de TINYINT, nu de INT(4).
    public static String q24NotTinyint() {
        // Începător: Aceeași cifră, alt tip, altă soartă.
        // Producție: CREATE TABLE example (id INT(4)); INSERT INTO example (id) VALUES (256). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Lățimea este deprecată. Chiar și scoasă din minte, INT tot ține 256.
        return "CREATE TABLE example (id INT(4)); INSERT INTO example (id) VALUES (256)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q24NotTinyint. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu taie 256 la 255.
    }
    /*
    🟢 Începător
        Ce este            99999 nu stă în TINYINT UNSIGNED
        De ce e nevoie     fără deosebire, orice UNSIGNED pare să țină 99999
    🟡 Producție
        Cum se scrie       INSERT INTO example (id) VALUES (99999)
    🔴 Staff
        Unde               13.8 cere MEDIUMINT UNSIGNED pentru 1..99999. Capătul arătat pentru TINYINT UNSIGNED este 255
    Pași
        1. Urcă tipul întreg, nu scara.
    */
    // Începător: 99999 este în afara lui id din chooseType.
    // Producție: public static String q25NotEnoughTinyint(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este o problemă de zecimale.
    public static String q25NotEnoughTinyint() {
        // Începător: qty din q10 este tipul care acoperă intervalul. id nu este.
        // Producție: INSERT INTO example (id) VALUES (99999). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Cu TRADITIONAL, aștepți eșec de interval, nu o scară tăiată. Nu am trimis propoziția.
        return "INSERT INTO example (id) VALUES (99999)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q25NotEnoughTinyint. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu schimbă id în MEDIUMINT.
    }
    /*
    🟢 Începător
        Ce este            LPAD nu este ZEROFILL
        De ce e nevoie     fără deosebire, umplerea din rezultat pare scrisă în celulă
    🟡 Producție
        Cum se scrie       SELECT LPAD(id, 4, '0') FROM example
    🔴 Staff
        Unde               13.1.6 recomandă LPAD. ZEROFILL este deprecat și nu se aplică în expresii sau UNION
    Pași
        1. Umple rezultatul.
    */
    // Începător: id stocat rămâne numărul.
    // Producție: public static String q26NotZerofill(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu respinge 10000 și nu ține loc de scară.
    public static String q26NotZerofill() {
        // Începător: Clientul vede zerouri. Coloana nu devine CHAR.
        // Producție: SELECT LPAD(id, 4, '0') FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu creează un index pe expresie în această propoziție.
        return "SELECT LPAD(id, 4, '0') FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q26NotZerofill. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu adaugă ZEROFILL pe id.
    }
    /*
    🟢 Începător
        Ce este            VARCHAR nu aplică scara
        De ce e nevoie     fără deosebire, '10.235' pare un DECIMAL
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price VARCHAR(10)); INSERT INTO example (price) VALUES ('10.235')
    🔴 Staff
        Unde               șirul din PDF este text. Scara este a lui DECIMAL, nu a textului
    Pași
        1. Nu ține prețul într-un șir dacă vrei scară.
    */
    // Începător: Caracterele 10.235 rămân text, inclusiv a treia zecimală.
    // Producție: public static String q27NotVarchar(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este conversia de la 13.1.3 și nu dă 1264 pentru o cifră în plus.
    public static String q27NotVarchar() {
        // Începător: price nu mai este număr.
        // Producție: CREATE TABLE example (price VARCHAR(10)); INSERT INTO example (price) VALUES ('10.235'). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Compararea cu un număr poate converti mai târziu. Această propoziție nu o cere. Nu este CHAR de lungime fixă: acela este copilul lui.
        return "CREATE TABLE example (price VARCHAR(10)); INSERT INTO example (price) VALUES ('10.235')";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q27NotVarchar. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu schimbă VARCHAR în DECIMAL.
    }
    /*
    🟢 Începător
        Ce este            precizia lui DECIMAL nu este FLOAT(p)
        De ce e nevoie     fără deosebire, 5 din DECIMAL(5, 2) pare un număr de biți
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price DECIMAL(5, 2), approx FLOAT(7))
    🔴 Staff
        Unde               13.1.4: FLOAT(p) cu p 0..23 alege 4 octeți, 24..53 alege 8 octeți. 13.1.3: M la DECIMAL este numărul de cifre
    Pași
        1. Numără cifre la preț, biți doar la FLOAT(p).
    */
    // Începător: Cele două paranteze nu spun același lucru.
    // Producție: public static String q28ScaleNotBits(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: 23 din comentariul vechi nu este 23 de cifre zecimale. Rândul HISTORICAL spune asta.
    public static String q28ScaleNotBits() {
        // Începător: DECIMAL(5, 2) nu alege 4 octeți prin cifra 5.
        // Producție: CREATE TABLE example (price DECIMAL(5, 2), approx FLOAT(7)). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: FLOAT(7) alege stocarea, nu scara lui price. FLOAT(M,D) ar fi deprecat și nu este această propoziție.
        return "CREATE TABLE example (price DECIMAL(5, 2), approx FLOAT(7))";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q28ScaleNotBits. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu rescrie comentariul vechi din Floating_Point.java.
    }
    /*
    🟢 Începător
        Ce este            FLOAT(7,4) nu este forma portabilă
        De ce e nevoie     fără deosebire, (7,4) pare o scară exactă ca DECIMAL
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price FLOAT(7, 4))
    🔴 Staff
        Unde               13.1.4: FLOAT(M,D) este extensie nestandard, deprecată; 999.00009 inserat în FLOAT(7,4) iese aproximativ 999.0001
    Pași
        1. Nu declara prețul așa.
    */
    // Începător: Afișarea are cifre, stocarea rămâne aproximativă.
    // Producție: public static String q29NotFloatMD(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este DECIMAL(7, 4), care este exact.
    public static String q29NotFloatMD() {
        // Începător: Manualul dă rotunjirea la afișare pentru acest tip, nu pentru example.price.
        // Producție: CREATE TABLE example (price FLOAT(7, 4)). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Suportul poate dispărea într-o versiune viitoare. 9.7 încă îl descrie ca deprecat, nu ca scos.
        return "CREATE TABLE example (price FLOAT(7, 4))";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q29NotFloatMD. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu inserează 999.00009.
    }
    /*
    🟢 Începător
        Ce este            CHAR nu este intervalul lui TINYINT
        De ce e nevoie     fără deosebire, '0256' pare că încalcă capătul 255
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id CHAR(4)); INSERT INTO example (id) VALUES ('256')
    🔴 Staff
        Unde               CHAR ține caractere. 256 ca număr este problema lui TINYINT UNSIGNED, nu a unui șir de patru caractere
    Pași
        1. Nu valida cantitatea cu un șir.
    */
    // Începător: Lungimea fixă nu este capătul numeric.
    // Producție: public static String q30NotCharId(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este lățimea INT(4) și nu dă 1264 pentru '256'.
    public static String q30NotCharId() {
        // Începător: id nu mai este cantitate.
        // Producție: CREATE TABLE example (id CHAR(4)); INSERT INTO example (id) VALUES ('256'). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Copilul CHAR față de VARCHAR rămâne în folderul lui. Aici doar nu este intervalul numeric.
        return "CREATE TABLE example (id CHAR(4)); INSERT INTO example (id) VALUES ('256')";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q30NotCharId. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu taie '256' la '255'.
    }
    /*
    🟢 Începător
        Ce este            prețul lăsat în DOUBLE se citește aproximativ
        De ce e nevoie     fără eșecul ăsta, 10.23 pare sigur în DOUBLE pentru că arată scurt
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price DOUBLE); INSERT INTO example (price) VALUES (10.23)
    🔴 Staff
        Unde               13.1.4: valorile nu sunt exacte, iar comparațiile cu ele dau probleme
    Pași
        1. Nu încasa dintr-o coloană aproximativă.
    */
    // Începător: Banii nu au scară în această propoziție.
    // Producție: public static String q31MoneyInDouble(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este o eroare 1264. Rândul se stochează și este greșit pentru un total exact.
    public static String q31MoneyInDouble() {
        // Începător: INSERT reușește și tot nu promite cifra.
        // Producție: CREATE TABLE example (price DOUBLE); INSERT INTO example (price) VALUES (10.23). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu garanta egalitatea cu 10.23 la o comparație. Manualul trimite problemele de virgulă mobilă la o altă secțiune, nedeschisă aici pe text.
        return "CREATE TABLE example (price DOUBLE); INSERT INTO example (price) VALUES (10.23)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q31MoneyInDouble. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu înlocuiește coloana cu DECIMAL(5, 2).
    }
    /*
    🟢 Începător
        Ce este            INT(4) acceptă 10000
        De ce e nevoie     fără eșec, paranteza pare o validare de producție
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id INT(4), price DECIMAL(5, 2)); INSERT INTO example (id, price) VALUES (10000, 10.23)
    🔴 Staff
        Unde               13.1.6: valorile mai late decât lățimea se afișează întreg. Intervalul este al lui INT
    Pași
        1. Nu pune validația în paranteză.
    */
    // Începător: Rândul rămâne cu 10000.
    // Producție: public static String q32Int4DoesNotReject(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este 1264.
    public static String q32Int4DoesNotReject() {
        // Începător: price 10.23 se stochează separat, în scară.
        // Producție: CREATE TABLE example (id INT(4), price DECIMAL(5, 2)); INSERT INTO example (id, price) VALUES (10000, 10.23). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Atributul este deprecat. Chiar și așa, în 9.7 nu respinge 10000.
        return "CREATE TABLE example (id INT(4), price DECIMAL(5, 2)); INSERT INTO example (id, price) VALUES (10000, 10.23)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q32Int4DoesNotReject. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu adaugă un CHECK (id <= 9999).
    }
    /*
    🟢 Începător
        Ce este            producția rămâne cu 255 și crede că a primit 256
        De ce e nevoie     fără el, warning-ul pare că anulează INSERT-ul
    🟡 Producție
        Cum se scrie       SET sql_mode = ''; INSERT INTO example (id, price) VALUES (256, 10.23); SELECT id FROM example
    🔴 Staff
        Unde               13.1.7: rândul există, i2 este 255, Level Warning, Code 1264
    Pași
        1. Tratează warning-ul ca pe un rând greșit.
    */
    // Începător: Cantitatea stocată nu este cea din comandă.
    // Producție: public static String q33Silent255(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este modul strict, unde rândul ar lipsi.
    public static String q33Silent255() {
        // Începător: Un stoc de 256 de cutii devine 255.
        // Producție: SET sql_mode = ''; INSERT INTO example (id, price) VALUES (256, 10.23); SELECT id FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Manualul arată 255. SELECT-ul din tur nu a rulat.
        return "SET sql_mode = ''; INSERT INTO example (id, price) VALUES (256, 10.23); SELECT id FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q33Silent255. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu înregistrează warning-ul într-un log. SHOW WARNINGS este altă propoziție.
    }
    /*
    🟢 Începător
        Ce este            a treia zecimală nu supraviețuiește scării 2
        De ce e nevoie     fără eșec, 10.235 pare rotunjit previzibil la 10.24
    🟡 Producție
        Cum se scrie       INSERT INTO example (price) VALUES (10.235)
    🔴 Staff
        Unde               13.1.3: conversia la scară este, în general, trunchiere, dependentă de sistem. Nu am observat cifra în tur
    Pași
        1. Nu promite 10.24.
    */
    // Începător: Prețul stocat nu are trei zecimale.
    // Producție: public static String q34ThirdDigit(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este 1264: valoarea este în interval.
    public static String q34ThirdDigit() {
        // Începător: Pierderea este de scară, nu de interval.
        // Producție: INSERT INTO example (price) VALUES (10.235). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Dacă îți trebuie cifra, scara trebuie să fie cel puțin 3 înainte de INSERT, nu după.
        return "INSERT INTO example (price) VALUES (10.235)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q34ThirdDigit. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu citește price înapoi.
    }
    /*
    🟢 Începător
        Ce este            ZEROFILL pe o tabelă temporară presupune că valoarea încape în lățime
        De ce e nevoie     fără eșec, lățimea pare doar cosmetică și în temporar
    🟡 Producție
        Cum se scrie       CREATE TEMPORARY TABLE example (id INT(4) ZEROFILL)
    🔴 Staff
        Unde               13.1.6: la unele join-uri care fac tabele temporare, serverul presupune că valoarea încape în lățime
    Pași
        1. Nu combina ZEROFILL cu valori mai late decât lățimea.
    */
    // Începător: Temporarul nu repară atributul deprecat.
    // Producție: public static String q35ZerofillTemporary(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este un index spațial și nu este JSON.
    public static String q35ZerofillTemporary() {
        // Începător: id poate fi tăiat la presupunerea de lățime în temporar, nu în sensul unui WHERE.
        // Producție: CREATE TEMPORARY TABLE example (id INT(4) ZEROFILL). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Manualul nu dă un număr de rând afectat. Nu inventa un plan. Atributul rămâne deprecat.
        return "CREATE TEMPORARY TABLE example (id INT(4) ZEROFILL)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q35ZerofillTemporary. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu creează tabela temporară în tur.
    }
    /*
    🟢 Începător
        Ce este            scăderea fără semn, scrisă într-un UNSIGNED, nu dă -1
        De ce e nevoie     fără eșec, UPDATE pare că salvează negativul
    🟡 Producție
        Cum se scrie       UPDATE example SET id = CAST(0 AS UNSIGNED) - 1
    🔴 Staff
        Unde               13.1.7: dacă rezultatul fără semn intră într-o coloană UNSIGNED, modul strict lasă coloana neschimbată; fără el, taie
    Pași
        1. Nu scădea dintr-un stoc UNSIGNED fără să știi modul.
    */
    // Începător: id nu devine -1.
    // Producție: public static String q36UnsignedSubUpdate(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: NO_UNSIGNED_SUBTRACTION schimbă expresia la -1, iar apoi coloana tot nu ține negativul dacă rămâne UNSIGNED.
    public static String q36UnsignedSubUpdate() {
        // Începător: Rândul fie rămâne cu valoarea veche, fie primește capătul, după mod.
        // Producție: UPDATE example SET id = CAST(0 AS UNSIGNED) - 1. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: ERROR 1690 este al expresiei înainte de scriere, în exemplul manualului fără coloană. Cu coloană, soarta depinde de mod. Nu am rulat-o.
        return "UPDATE example SET id = CAST(0 AS UNSIGNED) - 1";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q36UnsignedSubUpdate. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu pune NO_UNSIGNED_SUBTRACTION.
    }
    /*
    🟢 Începător
        Ce este            un + 1 pe limita lui BIGINT nu se taie într-o coloană
        De ce e nevoie     fără eșec, pare că serverul stochează 9223372036854775808 într-un BIGINT cu semn
    🟡 Producție
        Cum se scrie       SELECT 9223372036854775807 + 1
    🔴 Staff
        Unde               13.1.7: expresia dă ERROR 1690. Ca să meargă, CAST la UNSIGNED sau un literal cu punct, care intră pe DECIMAL
    Pași
        1. Nu aduna la limită fără să schimbi tipul expresiei.
    */
    // Începător: Nu există un rând de salvat.
    // Producție: public static String q37PlusOneBigint(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este clip-ul lui TINYINT la 255.
    public static String q37PlusOneBigint() {
        // Începător: Calculele cu DECIMAL au 65 de cifre, cum spune 13.8, și de aceea literalul cu punct nu eșuează la fel.
        // Producție: SELECT 9223372036854775807 + 1. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Propoziția care reușește în manual este CAST(... AS UNSIGNED) + 1 sau 9223372036854775807.0 + 1. Nu o amesteca cu aceasta.
        return "SELECT 9223372036854775807 + 1";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q37PlusOneBigint. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu creează un BIGINT în example.
    }
    /*
    🟢 Începător
        Ce este            după 1264, SELECT nu găsește rândul respins
        De ce e nevoie     fără eșec, aplicația crede că stocul a crescut pentru că a trimis VALUES
    🟡 Producție
        Cum se scrie       SELECT id, price FROM example
    🔴 Staff
        Unde               13.1.7: Empty set după INSERT-ul eșuat, când nu era alt rând
    Pași
        1. Numără rândurile după eroare.
    */
    // Începător: Comanda nu a lăsat 256.
    // Producție: public static String q38FailedInsertEmpty(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este un rollback de tranzacție scris de tine. Propoziția a eșuat.
    public static String q38FailedInsertEmpty() {
        // Începător: example nu conține linia.
        // Producție: SELECT id, price FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Dacă în tabelă existau rânduri vechi, SELECT nu este gol. Manualul pleca de la o tabelă goală. Nu generaliza golul peste rânduri vechi.
        return "SELECT id, price FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q38FailedInsertEmpty. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu face ROLLBACK: nu există o scriere reușită de anulat.
    }
    /*
    🟢 Începător
        Ce este            UNSIGNED pe DECIMAL este deprecat
        De ce e nevoie     fără eșec, UNSIGNED pare felul corect de a interzice prețul negativ
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price DECIMAL(5, 2) UNSIGNED)
    🔴 Staff
        Unde               13.1.6: UNSIGNED pe FLOAT, DOUBLE și DECIMAL este deprecat. Alternativa numită este CHECK
    Pași
        1. Nu marca prețul UNSIGNED.
    */
    // Începător: Intervalul de sus al lui DECIMAL nu se mută în sus, spre deosebire de întregi.
    // Producție: public static String q39UnsignedDecimal(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este TINYINT UNSIGNED, unde capătul chiar se mută la 255.
    public static String q39UnsignedDecimal() {
        // Începător: Prețul negativ trebuie refuzat de CHECK, nu de un atribut care poate dispărea.
        // Producție: CREATE TABLE example (price DECIMAL(5, 2) UNSIGNED). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: 9.7 încă descrie atributul și spune să te aștepți să fie scos. Nu spune că a fost deja scos.
        return "CREATE TABLE example (price DECIMAL(5, 2) UNSIGNED)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q39UnsignedDecimal. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu adaugă CHECK (price >= 0).
    }
    /*
    🟢 Începător
        Ce este            AUTO_INCREMENT pe FLOAT este deprecat
        De ce e nevoie     fără eșec, un identificator aproximativ pare o cheie stabilă
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id FLOAT AUTO_INCREMENT PRIMARY KEY)
    🔴 Staff
        Unde               13.1.6: AUTO_INCREMENT pe FLOAT și DOUBLE este deprecat. Alternativa este un întreg
    Pași
        1. Nu număra rândurile cu un aproximativ.
    */
    // Începător: Următorul id nu este o scară exactă.
    // Producție: public static String q40FloatAutoIncrement(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este TINYINT UNSIGNED din chooseType și nu este un PRIMARY KEY declarat aici.
    public static String q40FloatAutoIncrement() {
        // Începător: Cheia poate să nu mai fie acceptată într-o versiune viitoare.
        // Producție: CREATE TABLE example (id FLOAT AUTO_INCREMENT PRIMARY KEY). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu garanta că 9.7 acceptă propoziția: nu a fost trimisă. Manualul spune doar că atributul este deprecat.
        return "CREATE TABLE example (id FLOAT AUTO_INCREMENT PRIMARY KEY)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q40FloatAutoIncrement. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu inserează NULL ca să genereze id.
    }
    /*
    🟢 Începător
        Ce este            MEDIUMINT UNSIGNED este alegerea pentru 1..99999
        De ce e nevoie     fără trade-off, BIGINT pare mereu mai sigur
    🟡 Producție
        Cum se scrie       CREATE TABLE example (qty MEDIUMINT UNSIGNED)
    🔴 Staff
        Unde               13.8: dintre tipurile care acoperă intervalul, acesta folosește stocarea cea mai mică
    Pași
        1. Nu urca la BIGINT doar din obișnuință.
    */
    // Începător: 99999 încape, 1 încape.
    // Producție: public static String q41LeastStorage(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este scara unui preț și nu este DOUBLE.
    public static String q41LeastStorage() {
        // Începător: Stocarea mai mică nu este un index mai rapid. Acest folder nu promite planul.
        // Producție: CREATE TABLE example (qty MEDIUMINT UNSIGNED). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Dacă intervalul trece de ce ține MEDIUMINT UNSIGNED, alegerea din 13.8 nu mai stă. Nu am deschis limita lui în pagina de întregi.
        return "CREATE TABLE example (qty MEDIUMINT UNSIGNED)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q41LeastStorage. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu măsoară octeții pe disc.
    }
    /*
    🟢 Începător
        Ce este            DOUBLE rămâne când viteza bate exactitatea
        De ce e nevoie     fără condiție, DOUBLE pare interzis mereu
    🟡 Producție
        Cum se scrie       CREATE TABLE example (reading DOUBLE)
    🔴 Staff
        Unde               13.8 spune că DOUBLE poate fi de ajuns dacă exactitatea nu e critică sau viteza e prioritară
    Pași
        1. Scrie condiția înainte de tip.
    */
    // Începător: Măsurătoarea aproximativă nu este prețul.
    // Producție: public static String q42SpeedOverExact(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu folosi condiția ca să scapi de 1264: DOUBLE tot nu este un TINYINT.
    public static String q42SpeedOverExact() {
        // Începător: Renunți la scară cu bună știință.
        // Producție: CREATE TABLE example (reading DOUBLE). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: reading nu este price. Prețul rămâne DECIMAL.
        return "CREATE TABLE example (reading DOUBLE)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q42SpeedOverExact. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu face o măsurătoare.
    }
    /*
    🟢 Începător
        Ce este            punctul zecimal poate rămâne în aplicație, iar coloana ține un întreg
        De ce e nevoie     fără trade-off, orice bănuț pare să ceară DECIMAL pe server
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price_cents BIGINT); INSERT INTO example (price_cents) VALUES (1023)
    🔴 Staff
        Unde               13.8: pentru precizie mare, stochezi un punct fix într-un BIGINT și convertești înapoi după calcule pe 64 de biți
    Pași
        1. Ține banii întregi când tu deții scara.
    */
    // Începător: 1023 în coloană nu este 10.23 până când aplicația pune punctul.
    // Producție: public static String q43CentsInBigint(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este DOUBLE și nu este o scară declarată.
    public static String q43CentsInBigint() {
        // Începător: Serverul nu mai taie 10.235, pentru că nu primește zecimale.
        // Producție: CREATE TABLE example (price_cents BIGINT); INSERT INTO example (price_cents) VALUES (1023). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Aplicația trebuie să împartă la 100 la fel peste tot. Coloana nu garantează asta.
        return "CREATE TABLE example (price_cents BIGINT); INSERT INTO example (price_cents) VALUES (1023)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q43CentsInBigint. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu afișează 10.23.
    }
    /*
    🟢 Începător
        Ce este            zerourile rămân în rezultat, nu în tip
        De ce e nevoie     fără trade-off, ZEROFILL pare obligatoriu pentru un cod de patru cifre
    🟡 Producție
        Cum se scrie       SELECT LPAD(id, 4, '0') FROM example
    🔴 Staff
        Unde               13.1.6: LPAD sau un CHAR. Lățimea deprecată nu este obligația
    Pași
        1. Lasă id numeric.
    */
    // Începător: Celula nu primește '0001'.
    // Producție: public static String q44PadOutside(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu respinge valori peste 9999.
    public static String q44PadOutside() {
        // Începător: Afișarea este a clientului sau a SELECT-ului.
        // Producție: SELECT LPAD(id, 4, '0') FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Dacă un alt client citește id, vede numărul, nu zerourile. Asta este condiția.
        return "SELECT LPAD(id, 4, '0') FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q44PadOutside. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu schimbă tipul lui id.
    }
    /*
    🟢 Începător
        Ce este            semnul prețului se interzice cu CHECK, nu cu UNSIGNED pe DECIMAL
        De ce e nevoie     fără trade-off, atributul deprecat pare singura opțiune
    🟡 Producție
        Cum se scrie       CREATE TABLE example (price DECIMAL(5, 2), CONSTRAINT price_nonneg CHECK (price >= 0))
    🔴 Staff
        Unde               13.1.6: UNSIGNED pe DECIMAL este deprecat. CHECK este alternativa numită
    Pași
        1. Scrie predicatul.
    */
    // Începător: Prețul negativ este respins de constrângere, nu tăiat la scară.
    // Producție: public static String q45CheckNotUnsigned(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu mută capătul de sus, cum face UNSIGNED la un întreg.
    public static String q45CheckNotUnsigned() {
        // Începător: 0 rămâne permis. -0.01 nu trece predicatul.
        // Producție: CREATE TABLE example (price DECIMAL(5, 2), CONSTRAINT price_nonneg CHECK (price >= 0)). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: CHECK nu convertește 10.235 la două zecimale. Scara o face DECIMAL.
        return "CREATE TABLE example (price DECIMAL(5, 2), CONSTRAINT price_nonneg CHECK (price >= 0))";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q45CheckNotUnsigned. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu evaluează -0.01 în tur.
    }
    /*
    🟢 Începător
        Ce este            scara 3 când 10.235 trebuie să rămână
        De ce e nevoie     fără condiție, două zecimale par legea pentru orice coloană
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(6, 3)); INSERT INTO example (id, price) VALUES (1, 10.235)
    🔴 Staff
        Unde               precizia 6 și scara 3 acoperă 10.235. DECIMAL(5, 2) nu o face
    Pași
        1. Urcă scara înainte de INSERT.
    */
    // Începător: A treia cifră are loc.
    // Producție: public static String q46KeepScale3(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este DOUBLE și nu este VARCHAR.
    public static String q46KeepScale3() {
        // Începător: 1 rămâne în id.
        // Producție: CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(6, 3)); INSERT INTO example (id, price) VALUES (1, 10.235). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu promite o cifră citită în tur. Promite că scara declarată o poate ține.
        return "CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(6, 3)); INSERT INTO example (id, price) VALUES (1, 10.235)";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q46KeepScale3. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu coboară scara înapoi la 2.
    }
    /*
    🟢 Începător
        Ce este            zerourile stocate cer CHAR, nu un întreg
        De ce e nevoie     fără trade-off, vrei și interval numeric, și '0001' în aceeași celulă
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id_text CHAR(4)); INSERT INTO example (id_text) VALUES ('0001')
    🔴 Staff
        Unde               13.1.6: numerele deja formatate pot sta în CHAR. Atunci nu mai ai interval de TINYINT
    Pași
        1. Alege șirul doar dacă zeroul este dat, nu afișare.
    */
    // Începător: '0001' rămâne text.
    // Producție: public static String q47StoreFormattedChar(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu dă 1264 pentru '256'. Nu este cantitatea din chooseType.
    public static String q47StoreFormattedChar() {
        // Începător: Pierzi aritmetica exactă a lui DECIMAL pe această coloană.
        // Producție: CREATE TABLE example (id_text CHAR(4)); INSERT INTO example (id_text) VALUES ('0001'). Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: CHAR față de VARCHAR este copilul de șir. Aici condiția este doar: zeroul trebuie să fie stocat.
        return "CREATE TABLE example (id_text CHAR(4)); INSERT INTO example (id_text) VALUES ('0001')";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q47StoreFormattedChar. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu adună id_text cu 1 ca pe un întreg sigur.
    }
    /*
    🟢 Începător
        Ce este            modul strict este alegerea când 255 nu are voie să înlocuiască 256
        De ce e nevoie     fără ea, sql_mode gol pare mai tolerant și deci mai bun
    🟡 Producție
        Cum se scrie       SET sql_mode = 'TRADITIONAL'
    🔴 Staff
        Unde               13.1.7: TRADITIONAL respinge. Modul gol stochează capătul
    Pași
        1. Pornește modul înainte de încărcare.
    */
    // Începător: Comanda eșuează și stocul nu minte.
    // Producție: public static String q48StrictOnPurpose(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu înlocuiește un CHECK de business peste 255 dacă tipul este deja INT.
    public static String q48StrictOnPurpose() {
        // Începător: Preferi eroarea în locul unui rând tăcut greșit.
        // Producție: SET sql_mode = 'TRADITIONAL'. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: SET nu repară rândurile deja tăiate la 255. Le repară un UPDATE, care nu este această propoziție.
        return "SET sql_mode = 'TRADITIONAL'";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q48StrictOnPurpose. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu schimbă modul altei sesiuni.
    }
    /*
    🟢 Începător
        Ce este            nu coborî scara dacă a treia cifră trebuie să rămână
        De ce e nevoie     fără trade-off, ALTER la DECIMAL(5, 2) pare o cosmetizare
    🟡 Producție
        Cum se scrie       SELECT ROUND(price, 2) FROM example
    🔴 Staff
        Unde               MODIFY la scara 2 convertește valorile deja stocate. keepThirdDigit evită asta
    Pași
        1. Lasă DECIMAL(6, 3) dacă doza are trei cifre.
    */
    // Începător: Rândurile vechi nu își pierd cifra.
    // Producție: public static String q49DontNarrowLater(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este un CAST de afișare.
    public static String q49DontNarrowLater() {
        // Începător: Formatarea la două zecimale rămâne în SELECT sau în aplicație.
        // Producție: SELECT ROUND(price, 2) FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: ROUND în rezultat nu scrie înapoi în price. ALTER ar scrie.
        return "SELECT ROUND(price, 2) FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q49DontNarrowLater. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu face MODIFY.
    }
    /*
    🟢 Începător
        Ce este            EXPLAIN nu este valoarea rămasă în price
        De ce e nevoie     fără deosebire, planul pare să arate dacă 10.235 a fost tăiat
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT price FROM example
    🔴 Staff
        Unde               EXPLAIN nu atribuie coloana. Dovada este celula sau ERROR 1264, nu planul
    Pași
        1. Nu citi scara din plan.
    */
    // Începător: Niciun rând nu se convertește în această propoziție.
    // Producție: public static String q50ExplainIsNotTheStoredValue(). Dacă dispare, banca nu mai are această propoziție.
    // Expert: Nu este un index doar pentru că ai cerut planul.
    public static String q50ExplainIsNotTheStoredValue() {
        // Începător: Planul nu îți spune 255 sau 10.23.
        // Producție: EXPLAIN SELECT price FROM example. Dacă linia dispare, nu mai poți trimite cazul.
        // Expert: Nu am citit un plan în tur: root a fost refuzat. Nu inventa type sau rows.
        return "EXPLAIN SELECT price FROM example";
        // Începător: metoda a înapoiat textul, nu un rând citit din example.
        // Producție: acolada închide q50ExplainIsNotTheStoredValue. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu stochează și nu respinge 256.
    }
    // Începător: banca se oprește la planul care nu este valoarea stocată.
    // Producție: acolada închide DataTypes_Staff_Interview. Nu urmează un main.
    // Expert: Închiderea nu trimite q33Silent255 și nu schimbă scara lui price.
}
