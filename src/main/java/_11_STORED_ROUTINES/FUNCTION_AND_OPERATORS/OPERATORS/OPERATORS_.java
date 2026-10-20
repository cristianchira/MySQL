package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.OPERATORS;

// https://dev.mysql.com/doc/refman/8.0/en/non-typed-operators.html


/*                                                   Operators



 */

/*
TOPIC:
    Operatorul de comparație dă 1, 0 sau NULL și nu salvează o rutină. salary = NULL nu este adevărat, deci nu alege rândul. salary <=> NULL este comparația care vede NULL.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Întrebarea dacă un salariu este 0 trebuie să lase rândurile nemișcate și să nu creeze o funcție. Rândul cu salariu NULL nu trebuie să pară egal cu NULL prin =.
MECHANISM:
    Serverul execută șirul din compareSeesNull. 14.4.2: = dă NULL când un operand este NULL; <=> dă 1 când ambele sunt NULL și 0 când doar unul este NULL. Efectul este valoarea din rezultat, la sfârșitul propoziției. Nu se schimbă schema.
LOOKALIKES:
    IS NULL este testul de nul, nu rutina stocată. COALESCE înlocuiește valoarea și nu răspunde 1 sau 0. CREATE FUNCTION nu este nevoie ca să compari.
BOOK THEORY:
    Manualul 9.7, 14.4.2, deschis în tur: comparația dă 1, 0 sau NULL; <=> este egalul sigur la NULL și corespunde lui IS NOT DISTINCT FROM; = între NULL dă NULL. Nu există PDF în acest folder. Comentariul vechi trimite la manualul 8.0; regula citită în tur este cea din 9.7.
BOOK EXAMPLE:
    compareSeesNull
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/OPERATORS/OPERATORS_.java
BREAK LAB:
    equalsNullDropsTheRow ține SELECT salary FROM salaries WHERE salary = NULL. Comparația nu este adevărată, deci rândul nu iese. În tur șirul nu a fost trimis.
TRADE-OFFS:
    IS NULL când întrebarea este doar testul, nu o comparație între două coloane care pot fi ambele NULL. Aplicația când valorile sunt deja la client.
ANTI-PATTERNS:
    WHERE salary = NULL ca să găsești salariul lipsă.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Un colet fără greutate nu iese la WHERE weight = NULL. Iese la WHERE weight IS NULL sau weight <=> NULL. Comparația nu creează o procedură.
SPACED REVIEW:
    O zi: 21 octombrie 2026, = NULL nu este adevărat. Șapte zile: 27 octombrie 2026, <=> vede două NULL ca egale. Treizeci de zile: 19 noiembrie 2026, operatorul nu este o rutină salvată.

1. Enunț: Ce problemă rezolvă o comparație în SELECT față de o rutină salvată?
   Capcană: Fără CREATE FUNCTION, serverul nu poate spune dacă două valori sunt egale.
   Cheie: compareSeesNull cere 1, 0 sau NULL și nu lasă un obiect în schemă. salaries nu se schimbă. Nu există CALL.
   Demonstrație: compareSeesNull
2. Enunț: Ce se întâmplă cu rândul când salary este NULL și comparația este = sau <=>?
   Capcană: NULL = NULL este adevărat, deci rândul iese la WHERE salary = NULL.
   Cheie: 14.4.2: NULL = NULL dă NULL, nu 1, iar WHERE păstrează doar adevăratul. NULL <=> NULL dă 1. equalsNullDropsTheRow pierde rândul. Rândul stocat rămâne.
   Demonstrație: equalsNullDropsTheRow
3. Enunț: De ce <=> nu este IS NULL și nu este COALESCE?
   Capcană: Toate trei înlocuiesc NULL cu 0.
   Cheie: IS NULL testează un operand. <=> compară doi operanzi și poate spune că două coloane nule sunt egale. COALESCE întoarce o valoare din listă, nu 1 sau 0.
   Demonstrație: compareSeesNull
4. Enunț: Cum eșuează WHERE salary = NULL?
   Capcană: Găsește salariile lipsă, pentru că = este egalul.
   Cheie: equalsNullDropsTheRow nu este adevărat pe NULL. Rândurile lipsă rămân în tabelă și nu apar în rezultat. IS NULL sau <=> este testul.
   Demonstrație: equalsNullDropsTheRow
5. Enunț: Când compari în aplicație și când lași operatorul pe server?
   Capcană: Comparația în Java este aceeași cu <=>, pentru că null == null.
   Cheie: Pe server când filtrul trebuie să arunce rândurile înainte să plece la client. În aplicație când valorile sunt deja acolo. Nu crea o funcție stocată doar ca să scrii =.
   Demonstrație: compareSeesNull

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE să vezi și egalul care suportă NULL -> compareSeesNull -> SELECT NULL = NULL AS plain_equal, NULL <=> NULL AS null_safe -> nu garantează un rând din salaries: propoziția nu numește tabela
    2. Eșecul cel mai mic
           -> DECIDE = NULL să găsească lipsa -> equalsNullDropsTheRow -> SELECT salary FROM salaries WHERE salary = NULL -> nu garantează rândurile cu salariu lipsă
    3. Granița
           -> Operatorul este mecanismul MySQL. Linkul către manualul 8.0 nu este propoziția. Java null nu este <=>.
    4. Alternativa
           -> IS NULL pentru un singur operand. COALESCE când vrei valoarea, nu testul. O rutină stocată când expresia trebuie să aibă nume și privilegiu.
    5. Anti-patternul
           -> WHERE salary = NULL. Rezultatul pierde exact rândurile căutate.
    6. Dovada
           -> compareSeesNull ține = și <=>. equalsNullDropsTheRow ține filtrul fals. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> Comparația dă 1, 0 sau NULL și nu salvează o rutină. = NULL nu alege rândul. <=> îl alege când ambele sunt NULL.
*/

public class OPERATORS_ {

    /*
    🟢 Începător
        Ce este            două feluri de a întreba dacă nulul este egal cu nulul
        De ce e nevoie     fără ele, = pare că găsește lipsa
    🟡 Producție
        Cum se scrie       SELECT NULL = NULL AS plain_equal, NULL <=> NULL AS null_safe
    🔴 Staff
        Unde               prima dă NULL, a doua dă 1. Nu se creează o rutină și nu se citește salaries
    Pași
        1. Pune ambele comparații.
        2. Nu pune WHERE.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează comparația care nu salvează o rutină.
    // Producție: public static String compareSeesNull(). Dacă dispare, operatorul rămâne un fișier gol.
    // Expert: <=> nu este IS NULL. IS NULL are un singur operand. Aici sunt doi.
    public static String compareSeesNull() {
        // Începător: egalul obișnuit nu spune da la două nuluri; celălalt spune da.
        // Producție: SELECT NULL = NULL AS plain_equal, NULL <=> NULL AS null_safe. Dacă linia dispare, nu mai ai perechea din 14.4.2.
        // Expert: WHERE păstrează doar 1. NULL de la = nu alege un rând. Nu există CALL și nu există CREATE FUNCTION.
        return "SELECT NULL = NULL AS plain_equal, NULL <=> NULL AS null_safe";
        // Începător: metoda a înapoiat comparația, nu un rând din salaries.
        // Producție: acolada închide compareSeesNull. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă rezultatul NERULAT. Acolada nu este un index.
    }

    /*
    🟢 Începător
        Ce este            filtrul care caută salariul lipsă și nu îl găsește
        De ce e nevoie     fără el, = NULL pare un test de nul
    🟡 Producție
        Cum se scrie       SELECT salary FROM salaries WHERE salary = NULL
    🔴 Staff
        Unde               comparația nu este adevărată. IS NULL ar fi testul. Rândul stocat rămâne
    Pași
        1. Pune = NULL în WHERE.
        2. Nu pune IS NULL.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează filtrul care pierde rândurile căutate.
    // Producție: public static String equalsNullDropsTheRow(). Dacă dispare, eșecul rămâne o frază.
    // Expert: salary <=> NULL ar păstra rândul nul. COALESCE nu filtrează, înlocuiește.
    public static String equalsNullDropsTheRow() {
        // Începător: salariile lipsă nu apar, deși tot în tabelă sunt.
        // Producție: SELECT salary FROM salaries WHERE salary = NULL. Dacă linia dispare, nu mai ai filtrul fals.
        // Expert: Trei valori: adevărat, fals, necunoscut. WHERE vrea adevăratul. Necunoscutul de la = NULL cade.
        return "SELECT salary FROM salaries WHERE salary = NULL";
        // Începător: metoda a păstrat filtrul care nu alege.
        // Producție: acolada închide equalsNullDropsTheRow. Șirul a rămas în proces.
        // Expert: Nu este o eroare de procedură. Este operatorul folosit în locul testului de nul.
    }
}
