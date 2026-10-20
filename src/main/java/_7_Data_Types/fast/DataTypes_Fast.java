/*
TOPIC:
    Alegi tipul care spune ce rămâne în example: întregul mic rămâne, scara de două cifre taie restul, aproximarea nu ține loc de preț exact.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Cantitatea id și prețul price trebuie să rămână după regulile coloanei, nu după textul tastat în VALUES.
MECHANISM:
    chooseType este propoziția. Serverul convertește la sfârșitul lui INSERT. Nu deschide un index și nu ține un lacăt de tip.
LOOKALIKES:
    DOUBLE păstrează o aproximare. Îl alegi când viteza bate exactitatea. Nu este DECIMAL(5, 2).
BOOK THEORY:
    Manualul 9.7, 13.8 și 13.1.3, deschise în tur: tipul cel mai precis care acoperă valoarea; DECIMAL(5, 2) este exact, iar cifrele peste scară se convertesc, în general prin trunchiere. 13.1.4: DOUBLE este aproximativ. 13.1.7: 256 într-un TINYINT, cu TRADITIONAL, dă ERROR 1264 și nu lasă rândul; fără mod restrictiv, capătul stocat la TINYINT UNSIGNED este 255. PDF-urile deschise nu descriu această pierdere.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_7_Data_Types/Data_Types.java
BREAK LAB:
    strictRejects256. Nu a fost trimis în tur.
TRADE-OFFS:
    keepThirdDigit când a treia zecimală trebuie stocată. notDouble când accepți aproximarea. padInResult când zerourile sunt doar în rezultat.
ANTI-PATTERNS:
    nonStrictClips256 lasă 255 în loc de 256. displayWidthDoesNotLimit nu respinge 10000.
STAFF QUESTIONS:
    DataTypes_Staff_Interview.q01DecimalScale până la DataTypes_Staff_Interview.q50ExplainIsNotTheStoredValue
NOVEL TRANSFER:
    Eticheta unei farmacii: cutiile sunt un întreg mic, prețul ține două zecimale, doza 10.235 mg nu intră în scara de două cifre.
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
       DECIMAL(5, 2) are precizia 5. 10.523 din PDF are precizia 5.

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

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE întregul mic și prețul exact la două zecimale -> main -> chooseType -> nu garantează cifra rămasă după trunchiere, pentru că serverul nu a primit textul
    2. Eșecul cel mai mic
           -> DECIDE 256 încape -> strictRejects256 -> ERROR 1264 în manual, rândul lipsește -> nu garantează mesajul pe acest server
    3. Granița
           -> Tipul este atribuirea făcută de server. println nu convertește. Workbench nu este mecanismul.
    4. Alternativa
           -> DOUBLE când viteza bate exactitatea. DECIMAL(6, 3) când a treia zecimală rămâne. LPAD când zerourile sunt în rezultat.
    5. Anti-patternul
           -> sql_mode gol și 256 lasă 255. INT(4) nu este o validare.
    6. Dovada
           -> main tipărește chooseType. Rândul nu a fost citit: root@localhost a fost refuzat.
    7. Interviu
           -> DECIMAL ține scara. TINYINT UNSIGNED respinge 256 în mod strict și taie la 255 fără el. DOUBLE nu este prețul exact.
*/

// Începător: lecția stă lângă folderul care alege tipul, nu în fiecare copil.
// Producție: package _7_Data_Types.fast. Dacă linia dispare, chooseType nu mai este lângă Data_Types.java.
// Expert: Pachetul nu convertește 10.235 și nu ține loc de DOUBLE sau de CHECK.
package _7_Data_Types.fast;

/*
🟢 Începător
    Ce este            alegerea tipului care spune ce rămâne în id și în price
    De ce e nevoie     fără propoziție, comentariul din Integers.java pare că a și stocat valoarea
🟡 Producție
    Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2)); INSERT INTO example (id, price) VALUES (1, 10.235);
🔴 Staff
    Unde               conversia la sfârșitul lui INSERT; vecinul este DOUBLE; lățimea INT(4) nu este intervalul
Pași
    1. Declară tipul care trebuie să rămână.
    2. Inserează valoarea.
    3. Nu trimite textul: root@localhost a fost refuzat.
*/
// Începător: clasa ține propozițiile cu care alegi ce rămâne în example.
// Producție: public final class DataTypes_Fast. Fără ea, main nu are de unde să tipărească tipul.
// Expert: Nu predă CHAR, ENUM sau JSON. Copiii rămân clasele deja scrise. Clasa nu deschide JDBC.
public final class DataTypes_Fast {
    /*
    🟢 Începător
        Ce este            exemplul minim: cantitatea mică și prețul cu două zecimale
        De ce e nevoie     fără el, tipul rămâne o listă de nume în comentariul de curs
    🟡 Producție
        Cum se scrie       System.out.println(chooseType())
    🔴 Staff
        Unde               println nu este atribuirea. Conversia o face serverul, dacă primește textul. În tur nu l-a primit.
    Pași
        1. Ia propozițiile din chooseType.
        2. Le tipărește.
        3. Nu deschide conexiunea și nu trimite 256.
    */
    // Începător: pornește exemplul și arată textul pe care serverul l-ar putea rula.
    // Producție: public static void main(String[] args). args nu este un tip și nu schimbă scara.
    // Expert: Nu cheamă notDouble. Nu ține loc de COMMIT și nu citește un plan.
    public static void main(String[] args) {
        // Începător: pe ecran apare declarația lui example și INSERT-ul cu 1 și 10.235.
        // Producție: System.out.println(chooseType()). Dacă linia dispare, exemplul minim nu mai arată ce rămâne în coloană.
        // Expert: println nu trimite textul la 3306. DOUBLE tot nu devine exact. Un CHECK nu se creează aici.
        System.out.println(chooseType());
        // Începător: exemplul s-a oprit după ce a arătat propoziția.
        // Producție: acolada închide main. Parola nu stă în args.
        // Expert: Refuzul root@localhost nu este un 1264. Acolada nu taie 10.235.
    }

    /*
    🟢 Începător
        Ce este            tipul care păstrează întregul 1 și nu păstrează a treia zecimală
        De ce e nevoie     fără el, 10.235 pare stocat doar pentru că a fost scris în VALUES
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2)); INSERT INTO example (id, price) VALUES (1, 10.235);
    🔴 Staff
        Unde               atribuirea la sfârșitul lui INSERT. Scara 2 convertește, în general prin trunchiere. Nu este un index.
    Pași
        1. Coloana id este TINYINT UNSIGNED.
        2. Coloana price este DECIMAL(5, 2).
        3. INSERT pune (1, 10.235).
    */
    // Începător: metoda ține propoziția în care 1 rămâne și a treia zecimală nu rămâne.
    // Producție: public static String chooseType(). Dacă dispare, main nu mai are exemplul minim.
    // Expert: Nu este DOUBLE și nu este INT(4). Nu garantează cifra citită înapoi: serverul nu a executat textul.
    public static String chooseType() {
        // Începător: id primește un întreg mic, price primește un preț cu două cifre după punct.
        // Producție: CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2)); INSERT INTO example (id, price) VALUES (1, 10.235). Fără șir, rămâne comentariul din Fixed_Point.
        // Expert: 1 este în 0..255. 10.235 este în -999.99..999.99, dar scara 2 nu ține a treia cifră. Manualul spune trunchiere în general, nu o cifră citită de pe acest server. DOUBLE ar fi aproximat, nu ar fi tăiat scara.
        return """
                CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2));
                INSERT INTO example (id, price) VALUES (1, 10.235);
                """;
        // Începător: metoda a înapoiat propoziția, nu un rând citit din example.
        // Producție: acolada închide chooseType. Șirul nu a plecat spre 3306.
        // Expert: Închiderea nu este ERROR 1264 și nu eliberează un lacăt. Tipul nu a ținut un lacăt.
    }

    /*
    🟢 Începător
        Ce este            cel mai mic INSERT greșit: 256 nu încape în cantitatea declarată
        De ce e nevoie     fără el, pare că orice întreg scris în VALUES rămâne
    🟡 Producție
        Cum se scrie       SET sql_mode = 'TRADITIONAL'; INSERT INTO example (id, price) VALUES (256, 10.23);
    🔴 Staff
        Unde               13.1.7: ERROR 1264 (22003), INSERT-ul eșuează, rândul nu este stocat. Vecinul este tăierea la 255 când modul strict lipsește.
    Pași
        1. Pune TRADITIONAL.
        2. Inserează 256 în id.
        3. Nu îl trimite în tur.
    */
    // Începător: metoda ține eșecul în care rândul nu apare.
    // Producție: public static String strictRejects256(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: Nu este INT(4). Lățimea nu produce 1264. Nu este un CHECK.
    public static String strictRejects256() {
        // Începător: 256 nu rămâne în id, iar rândul nu se adaugă.
        // Producție: SET sql_mode = 'TRADITIONAL'; INSERT INTO example (id, price) VALUES (256, 10.23). Dacă linia dispare, nu mai ai cel mai mic SQL greșit.
        // Expert: Manualul arată ERROR 1264 pentru un TINYINT care primește 256, pe coloana lui i1. Aici coloana este id. Textul exact de pe acest server este NERULAT. Cu sql_mode gol, același 256 ar lăsa 255.
        return """
                SET sql_mode = 'TRADITIONAL';
                INSERT INTO example (id, price) VALUES (256, 10.23);
                """;
        // Începător: metoda s-a oprit la textul care respinge rândul.
        // Producție: acolada închide strictRejects256. TRADITIONAL nu a fost trimis.
        // Expert: Acolada nu golește example. Refuzul de autentificare nu este 1264.
    }

    /*
    🟢 Începător
        Ce este            tipul aproximativ, pe care nu îl pui în locul prețului exact
        De ce e nevoie     fără el, DOUBLE și DECIMAL par același număr
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED, price DOUBLE)
    🔴 Staff
        Unde               13.8: DOUBLE când viteza bate exactitatea. 13.1.4: valoarea nu este exactă, iar FLOAT(M,D) este deprecat.
    Pași
        1. Arată vecinul.
        2. Nu îl folosi pentru price-ul care trebuie să țină scara.
    */
    // Începător: metoda ține tipul care nu păstrează prețul exact.
    // Producție: public static String notDouble(). Dacă dispare, vecinul rămâne doar un nume.
    // Expert: Nu este o lățime de afișare și nu este un CHECK (price <= 10). CHECK ar refuza un predicat fără să aproximeze.
    public static String notDouble() {
        // Începător: price nu mai are o scară exactă. Cantitatea id rămâne un întreg mic.
        // Producție: CREATE TABLE example (id TINYINT UNSIGNED, price DOUBLE). Fără șir, nu poți arăta ce alegi când renunți la exactitate.
        // Expert: 13.8 permite DOUBLE când exactitatea nu e critică sau viteza e prioritară. Nu garantează că 10.235 se citește înapoi la fel. DECIMAL(5, 2) este cel care taie scara, nu acesta.
        return """
                CREATE TABLE example (id TINYINT UNSIGNED, price DOUBLE)
                """;
        // Începător: metoda a înapoiat vecinul, nu prețul stocat.
        // Producție: acolada închide notDouble.
        // Expert: Închiderea nu convertește un DECIMAL deja stocat. CAST în SELECT tot nu schimbă coloana.
    }

    /*
    🟢 Începător
        Ce este            eșecul în care 256 dispare și în celulă rămâne capătul intervalului
        De ce e nevoie     fără el, pare că un avertisment lasă valoarea tastată
    🟡 Producție
        Cum se scrie       SET sql_mode = ''; INSERT INTO example (id, price) VALUES (256, 10.23);
    🔴 Staff
        Unde               13.1.7: Warning 1264, TINYINT UNSIGNED păstrează 255. Rândul există. Nu este respingerea din TRADITIONAL.
    Pași
        1. Golește sql_mode.
        2. Inserează 256.
        3. Celula nu ține 256.
    */
    // Începător: metoda ține propoziția care lasă un rând cu valoarea tăiată.
    // Producție: public static String nonStrictClips256(). Dacă dispare, anti-patternul nu mai este în cod.
    // Expert: Nu este un UPDATE și nu este ZEROFILL. Tăierea nu creează un index.
    public static String nonStrictClips256() {
        // Începător: rândul rămâne, dar id nu este 256.
        // Producție: SET sql_mode = ''; INSERT INTO example (id, price) VALUES (256, 10.23). Dacă linia dispare, nu mai ai starea în care 255 înlocuiește 256.
        // Expert: Manualul, pe i2 TINYINT UNSIGNED, stochează 255 și dă Warning 1264. Coloana din example este id. Nu garantează warning-ul în tur. INT(4) nu ar tăia 256: 256 încape într-un INT.
        return """
                SET sql_mode = '';
                INSERT INTO example (id, price) VALUES (256, 10.23);
                """;
        // Începător: metoda a înapoiat tăierea, nu respingerea.
        // Producție: acolada închide nonStrictClips256.
        // Expert: Acolada nu pune modul strict înapoi. Următorul INSERT din sesiune ar moșteni sql_mode doar dacă serverul a executat SET.
    }

    /*
    🟢 Începător
        Ce este            același 10.235, cu o scară care îi ține a treia cifră
        De ce e nevoie     fără el, DECIMAL(5, 2) pare obligatoriu pentru orice preț
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(6, 3)); INSERT INTO example (id, price) VALUES (1, 10.235);
    🔴 Staff
        Unde               scara 3 acoperă 10.235. Precizia 6 acoperă cifrele. Nu este DOUBLE și nu este o formatare în client.
    Pași
        1. Urcă scara când cifra trebuie să rămână.
        2. Inserează aceeași valoare.
    */
    // Începător: metoda ține alternativa în care a treia zecimală are loc în coloană.
    // Producție: public static String keepThirdDigit(). Dacă dispare, nu mai ai condiția în care schimbi DECIMAL(5, 2).
    // Expert: Nu este LPAD și nu este MEDIUMINT. MEDIUMINT UNSIGNED este alegerea din 13.8 pentru 1..99999, nu pentru o scară.
    public static String keepThirdDigit() {
        // Începător: 10.235 are loc în price. id rămâne întregul 1.
        // Producție: CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(6, 3)); INSERT INTO example (id, price) VALUES (1, 10.235). Fără șir, nu poți arăta când nu mai tai scara.
        // Expert: 10.235 are cinci cifre și trei după punct, deci încape în precizia 6 și scara 3. Nu garantează reprezentarea binară: acesta este DECIMAL, nu FLOAT. Dacă cifra nu trebuie stocată, rămâi la scara 2.
        return """
                CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(6, 3));
                INSERT INTO example (id, price) VALUES (1, 10.235);
                """;
        // Începător: metoda a înapoiat tipul care ține a treia cifră.
        // Producție: acolada închide keepThirdDigit.
        // Expert: Închiderea nu modifică un price DECIMAL(5, 2) deja creat. Pentru asta ar trebui ALTER, care nu este această propoziție.
    }

    /*
    🟢 Începător
        Ce este            zerourile cerute la afișare, fără să schimbe întregul stocat
        De ce e nevoie     fără el, ZEROFILL pare singurul fel de a vedea 0001
    🟡 Producție
        Cum se scrie       SELECT LPAD(id, 4, '0') FROM example
    🔴 Staff
        Unde               13.1.6: LPAD înlocuiește ZEROFILL. Zerourile sunt în rezultat. Celula id rămâne numărul. ZEROFILL este deprecat.
    Pași
        1. Lasă tipul numeric.
        2. Umple doar rezultatul.
    */
    // Începător: metoda ține afișarea, nu un alt tip stocat.
    // Producție: public static String padInResult(). Dacă dispare, alternativa la ZEROFILL nu este în cod.
    // Expert: Nu este INT(4) ZEROFILL. Lățimea deprecată tot nu limitează intervalul, iar în tabele temporare manualul spune că ZEROFILL presupune că valoarea încape în lățime.
    public static String padInResult() {
        // Începător: clientul poate vedea id umplut cu zerouri, iar celula rămâne numărul.
        // Producție: SELECT LPAD(id, 4, '0') FROM example. Dacă linia dispare, nu mai desparți rezultatul de valoarea stocată.
        // Expert: LPAD nu schimbă tipul coloanei și nu respinge 10000. Nu garantează un rând: example trebuie să existe deja. Un CHAR care stochează '0001' ar fi alt tip, nu această propoziție.
        return """
                SELECT LPAD(id, 4, '0') FROM example
                """;
        // Începător: metoda a înapoiat rezultatul umplut, nu o coloană nouă.
        // Producție: acolada închide padInResult.
        // Expert: Închiderea nu adaugă ZEROFILL pe id. UNSIGNED pe DECIMAL rămâne deprecat și nu ține loc de LPAD.
    }

    /*
    🟢 Începător
        Ce este            paranteza de la întreg, care nu oprește un număr mai lat decât arată
        De ce e nevoie     fără ea, INT(4) pare că respinge tot ce are mai mult de patru cifre
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id INT(4), price DECIMAL(5, 2)); INSERT INTO example (id, price) VALUES (10000, 10.23);
    🔴 Staff
        Unde               13.1.6: lățimea nu constrânge intervalul și este deprecată. 10000 încape într-un INT. Nu este TINYINT UNSIGNED.
    Pași
        1. Declară INT(4).
        2. Inserează 10000.
        3. Valoarea nu este respinsă de paranteză.
    */
    // Începător: metoda ține capcana în care paranteza pare un interval.
    // Producție: public static String displayWidthDoesNotLimit(). Dacă dispare, capcana rămâne doar în comentariu.
    // Expert: Nu este ERROR 1264. 10000 nu este în afara lui INT. Nu este o lățime de VARCHAR.
    public static String displayWidthDoesNotLimit() {
        // Începător: 10000 poate rămâne în id, deși paranteza spune 4.
        // Producție: CREATE TABLE example (id INT(4), price DECIMAL(5, 2)); INSERT INTO example (id, price) VALUES (10000, 10.23). Fără șir, nu ai propoziția periculoasă.
        // Expert: Manualul: SMALLINT(3) tot ține valorile lui SMALLINT, afișate pe mai multe cifre. INT(4) la fel nu taie la 9999. Atributul este deprecat în 9.7, nu scos. Nu garantează că un client chiar umple cu spații: manualul spune că asta ține de aplicație.
        return """
                CREATE TABLE example (id INT(4), price DECIMAL(5, 2));
                INSERT INTO example (id, price) VALUES (10000, 10.23);
                """;
        // Începător: metoda a înapoiat capcana, nu o respingere.
        // Producție: acolada închide displayWidthDoesNotLimit.
        // Expert: Închiderea nu migrează id la TINYINT UNSIGNED. Dacă 10000 trebuie respins, intervalul este alt tip sau un CHECK, nu paranteza.
    }
    // Începător: lecția se oprește după capcana lățimii, nu după un rând citit.
    // Producție: acolada închide DataTypes_Fast. Nu mai există o a doua clasă Fast.
    // Expert: Închiderea nu urcă scara lui price și nu pune TRADITIONAL pe sesiune. Copiii CHAR, DATE și JSON rămân folderele lor.
}
