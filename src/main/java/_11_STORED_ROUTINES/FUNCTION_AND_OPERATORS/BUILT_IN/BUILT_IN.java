package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.BUILT_IN;

// https://dev.mysql.com/doc/refman/8.0/en/built-in-function-reference.html


/*                                                        Built-In Function and Operator Reference


    ○ SELECT SYSDATE()

                  * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-functions.html#function_sysdate

   ○ SELECT DATE_FORMAT('2009-10-04 22:23:00', '%W %M %Y');

                  * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-functions.html#function_date-format



 */


/*
TOPIC:
    Funcția built-in este calculul pe care serverul îl are deja. DATE_FORMAT și SYSDATE() nu cer CREATE. SYSDATE() nu este deterministă: două evaluări pot vedea două momente. Nu se pornește cu CALL.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    O dată se arată în cuvinte, fără o rutină în schemă. Niciun rând nu se schimbă. Nu apare un obiect de șters.
MECHANISM:
    Serverul execută șirul din builtInFormatsADate. Expresia se evaluează la sfârșitul propoziției. Nu se alege un index și nu se ține un lacăt, pentru că nu este numită o tabelă. 15.1.22: un CREATE FUNCTION cu același nume se lovește de built-in.
LOOKALIKES:
    NOW() este tot un timp al serverului, dar SYSDATE() din comentariul de curs este evaluat în momentul apelului, nu o rutină salvată. f_emp_avg_salary este CREATE FUNCTION. CALL nu pornește DATE_FORMAT.
BOOK THEORY:
    Manualul 9.7, 15.1.22, deschis în tur: dacă numele rutinei este un built-in, apare eroare de sintaxă fără spațiu înainte de paranteză; built-in și funcția stocată împart regulile de nume. 27.9: o rutină care conține NOW() sau SYSDATE() nu este deterministă, chiar dacă o declari astfel. Nu există PDF în acest folder. Comentariul din clasă dă SELECT DATE_FORMAT și SELECT SYSDATE(), cu link către manualul 8.0.
BOOK EXAMPLE:
    builtInFormatsADate
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/BUILT_IN/BUILT_IN.java
BREAK LAB:
    createFunctionNamedSysdate ține CREATE FUNCTION SYSDATE() RETURNS DATETIME RETURN CURRENT_TIMESTAMP. Numele este built-in. În tur șirul nu a fost trimis.
TRADE-OFFS:
    O funcție stocată când formatul este o regulă a ta, cu nume și cu EXECUTE, nu un format dintr-un șir. Aplicația când formatul nu trebuie să vină de la server.
ANTI-PATTERNS:
    CREATE FUNCTION SYSDATE() ca să „îngheți” ceasul. Numele nu înlocuiește built-in-ul, iar ceasul tot nu este o coloană.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Data de pe eticheta unui colet, scrisă în cuvinte, este DATE_FORMAT pe o valoare. Momentul de acum este SYSDATE(), diferit dacă propoziția așteaptă. Niciuna nu este o procedură.
SPACED REVIEW:
    O zi: 21 octombrie 2026, DATE_FORMAT nu cere CREATE. Șapte zile: 27 octombrie 2026, numele SYSDATE nu se redeclară. Treizeci de zile: 19 noiembrie 2026, două evaluări pot vedea două momente.

1. Enunț: Ce problemă rezolvă DATE_FORMAT față de o funcție stocată cu același text?
   Capcană: Fără CREATE FUNCTION, serverul nu știe să schimbe forma unei date.
   Cheie: builtInFormatsADate formatează o valoare deja dată. Nu apare obiect în schemă. Nu se schimbă un rând. Nu există CALL.
   Demonstrație: builtInFormatsADate
2. Enunț: Ce se întâmplă cu schema și cu timpul când propoziția este doar DATE_FORMAT pe un literal?
   Capcană: SYSDATE() din același folder îngheață un moment în schemă, iar DATE_FORMAT îl salvează.
   Cheie: Nu este numită o tabelă, deci nu există index și nu există lacăt. Rezultatul se vede la sfârșitul propoziției. SYSDATE() fără argument citește ceasul la evaluare, nu un rând stocat.
   Demonstrație: builtInFormatsADate
3. Enunț: De ce SYSDATE() nu este f_emp_avg_salary și nu este CALL?
   Capcană: Toate trei sunt funcții, deci se creează și se pornesc la fel.
   Cheie: SYSDATE() există fără CREATE. f_emp_avg_salary cere RETURNS și READS SQL DATA. CALL pornește o procedură. createFunctionNamedSysdate încearcă să ocupe numele built-in.
   Demonstrație: createFunctionNamedSysdate
4. Enunț: Cum eșuează CREATE FUNCTION SYSDATE() RETURNS DATETIME RETURN CURRENT_TIMESTAMP?
   Capcană: Înlocuiești ceasul serverului cu un ceas stabil, iar propozițiile vechi văd funcția ta.
   Cheie: createFunctionNamedSysdate folosește numele unui built-in. 15.1.22: eroarea de sintaxă apare dacă paranteza stă lipită. Chiar cu spațiu, nu acesta este felul în care citești ceasul. DETERMINISTIC pe o funcție cu ceas minte optimizerul, iar serverul nu verifică sinceritatea.
   Demonstrație: createFunctionNamedSysdate
5. Enunț: Când formatezi în aplicație și când lași DATE_FORMAT pe server?
   Capcană: Formatul din Java este obligatoriu, pentru că serverul ține doar data brută.
   Cheie: Pe server când toți clienții trebuie să vadă același text din propoziție. În aplicație când formatul ține de ecran și nu vrei să muți regula în SQL. Funcție stocată când formatul este o regulă cu nume, nu când numele este deja SYSDATE.
   Demonstrație: builtInFormatsADate

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE o dată în cuvinte, fără obiect nou -> builtInFormatsADate -> SELECT DATE_FORMAT('2009-10-04 22:23:00', '%W %M %Y') -> nu garantează limba ecranului: formatul este al serverului
    2. Eșecul cel mai mic
           -> DECIDE să redeclarăm ceasul -> createFunctionNamedSysdate -> CREATE FUNCTION SYSDATE() RETURNS DATETIME RETURN CURRENT_TIMESTAMP -> nu garantează că built-in-ul a fost înlocuit
    3. Granița
           -> Built-in este mecanismul MySQL. Linkul către manualul 8.0 nu este propoziția. Java DateTimeFormatter nu este DATE_FORMAT.
    4. Alternativa
           -> Funcție stocată cu nume propriu când regula este a ta. NOW() când vrei timpul propoziției, nu o rutină. Aplicația când formatul este doar al ecranului.
    5. Anti-patternul
           -> CREATE FUNCTION SYSDATE() ca să îngheți ceasul. Numele se lovește de built-in, iar două evaluări tot pot vedea două momente.
    6. Dovada
           -> builtInFormatsADate ține DATE_FORMAT. createFunctionNamedSysdate ține numele interzis. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> DATE_FORMAT schimbă forma unei valori și nu creează o rutină. SYSDATE() citește ceasul la evaluare. CREATE cu același nume nu înlocuiește built-in-ul.
*/

public class BUILT_IN {

    /*
    🟢 Începător
        Ce este            o dată scrisă în cuvinte, fără o rutină salvată
        De ce e nevoie     fără ea, pare că orice formatare cere CREATE FUNCTION
    🟡 Producție
        Cum se scrie       SELECT DATE_FORMAT('2009-10-04 22:23:00', '%W %M %Y')
    🔴 Staff
        Unde               expresia se evaluează la sfârșitul propoziției. Nu există tabelă, deci nu există lacăt
    Pași
        1. Formatează literalul.
        2. Nu pune CREATE.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează formatarea pe care serverul o are deja.
    // Producție: public static String builtInFormatsADate(). Dacă dispare, built-in rămâne doar un link.
    // Expert: Nu este f_emp_avg_salary. Nu cere READS SQL DATA și nu dă 1418, pentru că nu este CREATE.
    public static String builtInFormatsADate() {
        // Începător: data rămâne un text mai lung în rezultat, iar nicio tabelă nu se schimbă.
        // Producție: SELECT DATE_FORMAT('2009-10-04 22:23:00', '%W %M %Y'). Dacă linia dispare, nu mai ai propoziția din comentariul de curs, gata de trimis.
        // Expert: SYSDATE() fără argument ar citi ceasul la evaluare și nu ar fi determinist. Acest literal nu citește ceasul. CALL nu pornește DATE_FORMAT.
        return "SELECT DATE_FORMAT('2009-10-04 22:23:00', '%W %M %Y')";
        // Începător: metoda a înapoiat formatarea, nu textul formatat.
        // Producție: acolada închide builtInFormatsADate. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă textul NERULAT. Acolada nu creează o rutină.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a salva ceasul sub numele pe care serverul îl are deja
        De ce e nevoie     fără ea, pare că poți înlocui SYSDATE
    🟡 Producție
        Cum se scrie       CREATE FUNCTION SYSDATE() RETURNS DATETIME RETURN CURRENT_TIMESTAMP
    🔴 Staff
        Unde               15.1.22: numele built-in dă eroare de sintaxă cu paranteza lipită. Declararea deterministă a unui ceas nu este verificată ca sinceră
    Pași
        1. Folosește numele SYSDATE.
        2. Nu pune spațiu ca să „treacă”.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează CREATE-ul care vrea numele ceasului.
    // Producție: public static String createFunctionNamedSysdate(). Dacă dispare, ciocnirea rămâne o frază.
    // Expert: Un nume propriu, fără SYSDATE, tot ar fi nendeterminist dacă întoarce ceasul. 27.9 nu îl face sigur pentru statement-based logging doar pentru că l-ai declarat DETERMINISTIC.
    public static String createFunctionNamedSysdate() {
        // Începător: serverul nu trebuie să creadă că ceasul lui a fost înlocuit.
        // Producție: CREATE FUNCTION SYSDATE() RETURNS DATETIME RETURN CURRENT_TIMESTAMP. Dacă linia dispare, nu mai ai numele built-in pe o rutină.
        // Expert: Spațiul înainte de paranteză este excepția de sintaxă din 15.1.22, nu o rutină nouă. DATE_FORMAT rămâne built-in-ul care chiar formatează.
        return "CREATE FUNCTION SYSDATE() RETURNS DATETIME RETURN CURRENT_TIMESTAMP";
        // Începător: metoda a păstrat numele greșit.
        // Producție: acolada închide createFunctionNamedSysdate. Șirul a rămas în proces.
        // Expert: Nu este ERROR 1418, care cere mai întâi un nume propriu fără caracteristică. Aici numele este deja luat.
    }
}
