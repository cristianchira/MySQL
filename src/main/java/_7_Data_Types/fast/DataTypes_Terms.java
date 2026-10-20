/*
TOPIC:
    Termenii cu care spui ce rămâne în example: tipul, coloana, precizia, scara, lățimea deprecată și modul strict.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să numești partea din coloană care păstrează valoarea și partea care o taie.
MECHANISM:
    Metodele întorc propoziția din DataTypes_Fast. Serverul, dacă o primește, convertește la sfârșitul lui INSERT.
LOOKALIKES:
    Lățimea de afișare nu este precizia. Scara nu este DOUBLE.
BOOK THEORY:
    Manualul 9.7, 13.1.3: precizia este numărul de cifre, scara este numărul de după punct. 13.1.6: lățimea întregului nu limitează intervalul și este deprecată. 13.1.7: modul strict respinge valoarea din afara intervalului. PDF-ul Fixed_and_Floating_Point spune că precizia lui 10.523 este 5.
BOOK EXAMPLE:
    explainDataType
REPO DEMO:
    src/main/java/_7_Data_Types/fast/DataTypes_Fast.java
BREAK LAB:
    explainStrictMode întoarce strictRejects256. Nu a fost trimis.
TRADE-OFFS:
    Scara 3 când cifra trebuie să rămână. LPAD când umpli doar rezultatul.
ANTI-PATTERNS:
    Să numești INT(4) precizie și să aștepți respingerea lui 10000.
STAFF QUESTIONS:
    DataTypes_Staff_Interview.q01DecimalScale până la DataTypes_Staff_Interview.q50ExplainIsNotTheStoredValue
NOVEL TRANSFER:
    Pe eticheta farmaciei, scara prețului și scara dozei nu sunt aceeași coloană.
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
*/
// Începător: glosarul stă lângă lecția care alege tipul coloanei example.
// Producție: package _7_Data_Types.fast. Dacă linia dispare, explainScale nu mai compilează lângă chooseType.
// Expert: Pachetul nu este scara și nu este lățimea deprecată. Nu mută aici termenii din _8_Constraints.
package _7_Data_Types.fast;

/*
🟢 Începător
    Ce este            numele părților care decid ce rămâne în id și în price
    De ce e nevoie     fără ele, precizia, scara și lățimea par același lucru
🟡 Producție
    Cum se scrie       metodele care întorc propoziția deja ținută în DataTypes_Fast
🔴 Staff
    Unde               atribuirea este pe server. Metoda doar înapoiază textul. În tur textul nu a plecat.
Pași
    1. Numește termenul.
    2. Întoarce propoziția care îl arată.
    3. Nu deschide o conexiune.
*/
// Începător: clasa numește tipul, coloana, precizia, scara, lățimea și modul strict.
// Producție: public final class DataTypes_Terms. Dacă dispare, exemplul din definiție nu are metodă.
// Expert: Nu predă CHAR față de VARCHAR. Aceia rămân în folderul lor. Clasa nu ține un lacăt.
public final class DataTypes_Terms {
    /*
    🟢 Începător
        Ce este            tipul care spune ce cifră rămâne în celulă
        De ce e nevoie     fără el, 10.235 pare păstrat doar pentru că a fost scris
    🟡 Producție
        Cum se scrie       DataTypes_Fast.chooseType()
    🔴 Staff
        Unde               conversia la sfârșitul lui INSERT. Vecinul este DOUBLE, care nu are scară exactă.
    Pași
        1. Întoarce chooseType.
    */
    // Începător: metoda dă propoziția în care tipul decide soarta lui 1 și a lui 10.235.
    // Producție: public static String explainDataType(). Dacă dispare, definiția nu mai are exemplu executabil.
    // Expert: Nu este un CHECK și nu creează un index pe price.
    public static String explainDataType() {
        // Începător: același text ca în lecție: întregul mic rămâne, a treia zecimală nu are loc în scara 2.
        // Producție: return DataTypes_Fast.chooseType(). Dacă linia dispare, glosarul inventează o a doua propoziție.
        // Expert: Apelul nu trimite INSERT-ul. Trunchierea o face serverul, iar în tur serverul nu a primit textul. Nu este lățimea INT(4).
        return DataTypes_Fast.chooseType();
        // Începător: metoda s-a oprit la propoziție, fără un rând citit.
        // Producție: acolada închide explainDataType.
        // Expert: Închiderea nu schimbă scara unui DECIMAL deja stocat.
    }

    /*
    🟢 Începător
        Ce este            locul din rând care primește prețul
        De ce e nevoie     fără coloană, scara nu are pe ce să se aplice
    🟡 Producție
        Cum se scrie       CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2))
    🔴 Staff
        Unde               coloana price este cea convertită. id este cealaltă coloană, cu alt interval. Nu este un index.
    Pași
        1. Arată declarația coloanelor.
    */
    // Începător: metoda ține declarația coloanelor din example.
    // Producție: public static String explainColumn(). Dacă dispare, coloana rămâne un cuvânt.
    // Expert: Coloana nu este rezultatul unui LPAD și nu este un rând încă.
    public static String explainColumn() {
        // Începător: example primește două coloane, una pentru cantitate și una pentru preț.
        // Producție: return "CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2))". Fără linie, nu arăți unde se taie valoarea.
        // Expert: Declarația nu inserează rândul. Conversia apare abia la INSERT. Nu este ALTER și nu ține lacăt după ce propoziția s-a terminat.
        return "CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2))";
        // Începător: metoda a înapoiat coloanele, nu un preț stocat.
        // Producție: acolada închide explainColumn.
        // Expert: Închiderea nu adaugă KEY. Tipul singur nu este un index.
    }

    /*
    🟢 Începător
        Ce este            numărul de cifre pe care price le poate ține
        De ce e nevoie     fără precizie, scara pare tot numărul
    🟡 Producție
        Cum se scrie       DECIMAL(5, 2)
    🔴 Staff
        Unde               13.1.3: 5 este precizia, intervalul este -999.99..999.99. PDF-ul: 10.523 are precizia 5. Nu este FLOAT(p).
    Pași
        1. Numește cifra 5 din DECIMAL(5, 2).
    */
    // Începător: metoda ține tipul a cărui precizie este 5.
    // Producție: public static String explainPrecision(). Dacă dispare, 5 se confundă cu scara.
    // Expert: Precizia lui FLOAT(p) este în biți și alege doar mărimea stocării. Nu este această cifră.
    public static String explainPrecision() {
        // Începător: price poate ține cinci cifre, din care două după punct.
        // Producție: return "CREATE TABLE example (price DECIMAL(5, 2))". Dacă linia dispare, precizia nu mai este într-o propoziție.
        // Expert: 10.235 are cinci cifre, deci precizia nu este motivul pentru care a treia zecimală cade. Cade scara. Nu este lățimea de afișare 4.
        return "CREATE TABLE example (price DECIMAL(5, 2))";
        // Începător: metoda a înapoiat precizia, nu valoarea citită.
        // Producție: acolada închide explainPrecision.
        // Expert: Închiderea nu rotunjește 10.523 din PDF. PDF-ul doar numără cifrele.
    }

    /*
    🟢 Începător
        Ce este            câte cifre rămân după punct
        De ce e nevoie     fără scară, 10.235 pare că încape pentru că numărul nu e prea mare
    🟡 Producție
        Cum se scrie       price DECIMAL(5, 2)
    🔴 Staff
        Unde               cifrele peste scară se convertesc la atribuire, în general prin trunchiere. Nu este modul strict și nu este 1264.
    Pași
        1. Arată scara 2.
    */
    // Începător: metoda ține scara care nu păstrează a treia zecimală.
    // Producție: public static String explainScale(). Dacă dispare, pierderea lui 10.235 nu mai are nume.
    // Expert: Scara nu respinge 256. Asta este intervalul lui TINYINT UNSIGNED, cu sau fără TRADITIONAL.
    public static String explainScale() {
        // Începător: după punct rămân două cifre, nu trei.
        // Producție: return "INSERT INTO example (id, price) VALUES (1, 10.235)". Dacă linia dispare, nu mai ai valoarea care își pierde ultima cifră.
        // Expert: 10.235 este în intervalul -999.99..999.99, deci nu este ERROR 1264. Conversia de scară nu garantează o cifră anume în tur: serverul nu a executat INSERT-ul. DOUBLE nu are această scară.
        return "INSERT INTO example (id, price) VALUES (1, 10.235)";
        // Începător: metoda a înapoiat valoarea care nu încape în scară, nu rândul stocat.
        // Producție: acolada închide explainScale.
        // Expert: Închiderea nu urcă scara la 3. Pentru asta este keepThirdDigit.
    }

    /*
    🟢 Începător
        Ce este            paranteza întregului, care nu este un plafon
        De ce e nevoie     fără deosebire, INT(4) pare că oprește 10000
    🟡 Producție
        Cum se scrie       DataTypes_Fast.displayWidthDoesNotLimit()
    🔴 Staff
        Unde               13.1.6: lățimea este deprecată și nu constrânge intervalul. Zerofill-ul deprecat nu o transformă în precizie.
    Pași
        1. Întoarce propoziția cu INT(4) și 10000.
    */
    // Începător: metoda dă capcana în care paranteza pare interval.
    // Producție: public static String explainDisplayWidth(). Dacă dispare, lățimea se confundă cu precizia.
    // Expert: Nu este DECIMAL(5, 2). La DECIMAL, M este precizia, nu o lățime de afișare.
    public static String explainDisplayWidth() {
        // Începător: 10000 poate sta într-un INT chiar dacă paranteza arată 4.
        // Producție: return DataTypes_Fast.displayWidthDoesNotLimit(). Dacă linia dispare, glosarul nu mai are propoziția.
        // Expert: Apelul nu inserează 10000. Nu este TINYINT UNSIGNED, al cărui capăt în manual este 255. Nu garantează că un client umple cu spații.
        return DataTypes_Fast.displayWidthDoesNotLimit();
        // Începător: metoda a înapoiat capcana, nu un număr tăiat.
        // Producție: acolada închide explainDisplayWidth.
        // Expert: Închiderea nu activează ZEROFILL. ZEROFILL ar adăuga UNSIGNED și tot ar fi deprecat.
    }

    /*
    🟢 Începător
        Ce este            modul în care 256 nu lasă un rând
        De ce e nevoie     fără el, singura poveste este că serverul taie mereu la capăt
    🟡 Producție
        Cum se scrie       DataTypes_Fast.strictRejects256()
    🔴 Staff
        Unde               13.1.7: TRADITIONAL, ERROR 1264, INSERT eșuat. Fără mod restrictiv rămâne 255.
    Pași
        1. Întoarce strictRejects256.
    */
    // Începător: metoda dă propoziția în care rândul nu apare.
    // Producție: public static String explainStrictMode(). Dacă dispare, respingerea se confundă cu trunchierea de scară.
    // Expert: Modul strict nu este un CHECK (id <= 255). CHECK este o constrângere. Aici refuzul vine din intervalul tipului.
    public static String explainStrictMode() {
        // Începător: 256 nu se salvează în id cât timp modul strict este pornit.
        // Producție: return DataTypes_Fast.strictRejects256(). Dacă linia dispare, nu mai ai SET sql_mode = 'TRADITIONAL' lângă definiție.
        // Expert: Manualul numește coloana i1. Aici coloana este id. Mesajul de pe acest server este NERULAT. sql_mode gol nu este această metodă: acolo rândul rămâne cu 255.
        return DataTypes_Fast.strictRejects256();
        // Începător: metoda a înapoiat respingerea, nu capătul 255.
        // Producție: acolada închide explainStrictMode.
        // Expert: Închiderea nu schimbă sql_mode-ul sesiunii. Autentificarea refuzată nu este 1264.
    }
    // Începător: glosarul se oprește la modul strict, nu la un rând stocat.
    // Producție: acolada închide DataTypes_Terms. Metodele de mai sus rămân exemplele definițiilor.
    // Expert: Închiderea nu mută precizia în biții lui FLOAT(p) și nu creează un CHECK.
}
